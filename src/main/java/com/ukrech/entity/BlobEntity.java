package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class BlobEntity extends PlaceableItemEntity {
   public static final PlaceableItemEntityInfo<BlobEntity> BLOB = new PlaceableItemEntityInfo<>("blob", 3/16f, 3/16f, BlobEntity::new);

   //

   public BlobEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static void register() {
      Tabletop.register(BLOB.id, BLOB.entity, BlobEntity.createPlaceableItemAttributes());
   }
}
