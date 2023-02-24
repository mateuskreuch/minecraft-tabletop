package com.ukrech.entity;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public abstract class PlaceableItemEntity extends LivingEntity {
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

   protected abstract Item getOriginItem();

   protected boolean isStackEqualToOriginStack(ItemStack originStack, ItemStack stack) {
      return stack.isOf(this.getOriginItem()) && ItemStack.areNbtEqual(stack, originStack);
   }

   protected boolean tryReturnToHand(ItemStack originStack, PlayerEntity player, Hand hand) {
      var stack = player.getStackInHand(hand);
      var unit = originStack.copyWithCount(1);

      if (stack.isEmpty()) {
         player.setStackInHand(hand, unit);
         return true;
      }
      else if (this.isStackEqualToOriginStack(originStack, stack) && stack.getCount() < stack.getMaxCount()) {
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

         if (!(player.getMainHandStack().getItem() instanceof SwordItem)) {
            this.drop();
         }
      }
      else {
         this.drop();
      }

      return true;
   }
}
