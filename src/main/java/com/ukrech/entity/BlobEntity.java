package com.ukrech.entity;

import com.ukrech.Tabletop;
import com.ukrech.item.BlobItem;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BlobEntity extends PlaceableItemEntity {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "blob_entity");
   public static final EntityType<BlobEntity> ENTITY = FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlobEntity::new)
                                                                              .dimensions(EntityDimensions.fixed(0.1875f, 0.1875f))
                                                                              .build();

   //

   public BlobEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   @Override
   protected Item getOriginItem() {
      return BlobItem.ITEM;
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
