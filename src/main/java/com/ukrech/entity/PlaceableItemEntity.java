package com.ukrech.entity;

import java.util.Collections;
import java.util.List;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public abstract class PlaceableItemEntity extends LivingEntity {
   public static class PlaceableItemEntityInfo<E extends PlaceableItemEntity> {
      public Identifier id;
      public Identifier texturePath;
      public EntityModelLayer layer;
      public EntityType<E> entity;

      public PlaceableItemEntityInfo(String name, float width, float height, EntityFactory<E> factory) {
         this.id = new Identifier(Tabletop.MOD_ID, name.concat("_entity"));
         this.texturePath = new Identifier(Tabletop.MOD_ID, String.format("textures/entity/placeableitems/%s.png", name));
         this.layer = new EntityModelLayer(this.id, "main");
         this.entity = FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory)
                                              .dimensions(EntityDimensions.fixed(width, height))
                                              .build();
      }
   }

   public static final TrackedData<ItemStack> ORIGIN_ITEM_STACK = DataTracker.registerData(PlaceableItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

   private static final List<ItemStack> EMPTY_STACK_LIST = Collections.emptyList();

   //

   protected PlaceableItemEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static DefaultAttributeContainer.Builder createPlaceableItemAttributes() {
      return LivingEntity.createLivingAttributes();
   }

   //

   public void setOriginItemStack(ItemStack stack) {
      this.dataTracker.set(ORIGIN_ITEM_STACK, stack);

      if (stack.hasCustomName()) {
         this.setCustomName(stack.getName());
      }
   }

   public ItemStack getOriginItemStack() {
      return this.dataTracker.get(ORIGIN_ITEM_STACK);
   }

   public int getColor() {
      var stack = this.getOriginItemStack();

      return ((DyeableItem) stack.getItem()).getColor(stack);
   }

   public void drop() {
      Block.dropStack(this.world, this.getBlockPos(), this.getOriginItemStack());
      this.kill();
   }

   //

   protected boolean tryReturnToHand(ItemStack originStack, PlayerEntity player, Hand hand) {
      var stack = player.getStackInHand(hand);
      var unit = originStack.copyWithCount(1);

      if (stack.isEmpty()) {
         player.setStackInHand(hand, unit);
         return true;
      }
      else if (ItemStack.canCombine(originStack, stack) && stack.getCount() < stack.getMaxCount()) {
         stack.increment(1);
         return true;
      }
      else {
         return player.giveItemStack(unit);
      }
   }

   //
   
   @Override
   public void initDataTracker() {
      super.initDataTracker();
      this.dataTracker.startTracking(ORIGIN_ITEM_STACK, ItemStack.EMPTY);
   }

   @Override
   public void writeCustomDataToNbt(NbtCompound nbt) {
      super.writeCustomDataToNbt(nbt);
      nbt.put("OriginItem", this.getOriginItemStack().writeNbt(new NbtCompound()));
   }

   @Override
   public void readCustomDataFromNbt(NbtCompound nbt) {
      super.readCustomDataFromNbt(nbt);
      this.setOriginItemStack(ItemStack.fromNbt(nbt.getCompound("OriginItem")));
   }

   @Override
   public Iterable<ItemStack> getArmorItems() {
      return EMPTY_STACK_LIST;
   }

   @Override
   public ItemStack getEquippedStack(EquipmentSlot slot) {
      return ItemStack.EMPTY;
   }

   @Override
   public void equipStack(EquipmentSlot slot, ItemStack stack) {}

   @Override
   public Arm getMainArm() {
      return Arm.RIGHT;
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   public ActionResult interact(PlayerEntity player, Hand hand) {
      if (this.tryReturnToHand(this.getOriginItemStack(), player, hand)) {
         this.kill();
         return ActionResult.SUCCESS;
      }
      else {
         return ActionResult.PASS;
      }
   }

   @Override
   public void kill() {
      this.remove(Entity.RemovalReason.KILLED);
      this.emitGameEvent(GameEvent.ENTITY_DIE);
   }

   @Override
   public boolean isMobOrPlayer() {
      return false;
   }

   @Override
   public boolean isAffectedBySplashPotions() {
      return false;
   }

   @Override
   public boolean damage(DamageSource source, float amount) {
      if (source.getAttacker() instanceof PlayerEntity) {
         var player = (PlayerEntity) source.getAttacker();

         if (player.getMainHandStack().getItem() instanceof SwordItem) {
            return true;
         }
      }

      this.drop();

      return true;
   }
}
