package com.ukrech.entity;

import com.ukrech.Tabletop;
import com.ukrech.item.BlobItem;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
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
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BlobEntity extends LivingEntity {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "blob_entity");
   public static final TrackedData<Integer> COLOR = DataTracker.registerData(BlobEntity.class, TrackedDataHandlerRegistry.INTEGER);
   
   public static EntityType<BlobEntity> ENTITY = FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlobEntity::new)
                                                                        .dimensions(EntityDimensions.fixed(0.1875f, 0.1875f))
                                                                        .build();

   private static final List<ItemStack> EMPTY_STACK_LIST = Collections.emptyList();

   private ItemStack originStack = ItemStack.EMPTY;

   //

   public BlobEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static DefaultAttributeContainer.Builder createBlobAttributes() {
      return LivingEntity.createLivingAttributes();
   }

   //

   public void setOriginItemStack(ItemStack stack) {
      this.originStack = stack.copy();

      this.originStack.setCount(1);
      this.dataTracker.set(COLOR, ((DyeableItem) stack.getItem()).getColor(stack));

      if (stack.hasCustomName()) {
         this.setCustomName(stack.getName());
      }
   }

   public int getColor() {
      return this.dataTracker.get(COLOR);
   }

   public void drop() {
      Block.dropStack(this.world, this.getBlockPos(), this.originStack);
      this.kill();
   }

   private boolean tryReturnToHand(PlayerEntity player, Hand hand) {
      var handStack = player.getStackInHand(hand);

      if (handStack.isEmpty()) {
         player.setStackInHand(hand, this.originStack);
         return true;
      }
      else if (handStack.getItem() instanceof BlobItem
           && ItemStack.areNbtEqual(handStack, this.originStack)
           && handStack.getCount() < handStack.getMaxCount())
      {
         handStack.increment(1);
         return true;
      }
      else {
         return false;
      }
   }

   //

   @Override
   public void initDataTracker() {
      super.initDataTracker();
      this.dataTracker.startTracking(COLOR, 0);
   }

   @Override
   public void writeCustomDataToNbt(NbtCompound nbt) {
      super.writeCustomDataToNbt(nbt);
      nbt.put("OriginItem", this.originStack.writeNbt(new NbtCompound()));
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
   public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
      if (this.tryReturnToHand(player, Hand.MAIN_HAND) || player.giveItemStack(this.originStack)) {
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

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.ENTITY_SLIME_HURT_SMALL;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SLIME_DEATH_SMALL;
   }
}
