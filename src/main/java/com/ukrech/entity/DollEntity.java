package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DollEntity extends PlaceableItemEntity {
   public static class DollEntityInfo extends PlaceableItemEntityInfo<DollEntity> {
      public DollEntityInfo(String name) {
         super(name, 5/16f, 8/16f, DollEntity::new);

         this.texturePath = new Identifier(Tabletop.MOD_ID, String.format("textures/item/%s.png", name));
      }
   }

   //

   public static final DollEntityInfo ALLAY = new DollEntityInfo("allay_doll");
   public static final DollEntityInfo CREEPER = new DollEntityInfo("creeper_doll");
   public static final DollEntityInfo GUARDIAN = new DollEntityInfo("guardian_doll");
   public static final DollEntityInfo HORSE = new DollEntityInfo("horse_doll");
   public static final DollEntityInfo SHULKER = new DollEntityInfo("shulker_doll");
   public static final DollEntityInfo SKELETON = new DollEntityInfo("skeleton_doll");
   public static final DollEntityInfo SNOW_GOLEM = new DollEntityInfo("snow_golem_doll");
   public static final DollEntityInfo VILLAGER = new DollEntityInfo("villager_doll");

   //

   public DollEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static void register() {
      Tabletop.register(ALLAY.id, ALLAY.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(CREEPER.id, CREEPER.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(GUARDIAN.id, GUARDIAN.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(HORSE.id, HORSE.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(SHULKER.id, SHULKER.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(SKELETON.id, SKELETON.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(SNOW_GOLEM.id, SNOW_GOLEM.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(VILLAGER.id, VILLAGER.entity, DollEntity.createPlaceableItemAttributes());
   }
}
