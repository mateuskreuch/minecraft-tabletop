package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DollEntity extends PlaceableItemEntity {
   public static final float SHADOW_RADIUS = 4/16f;

   public static final PlaceableItemEntityInfo<DollEntity> ALLAY = new PlaceableItemEntityInfo<>("allay_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> CREEPER = new PlaceableItemEntityInfo<>("creeper_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> GUARDIAN = new PlaceableItemEntityInfo<>("guardian_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> HORSE = new PlaceableItemEntityInfo<>("horse_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> SHULKER = new PlaceableItemEntityInfo<>("shulker_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> SKELETON = new PlaceableItemEntityInfo<>("skeleton_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> SNOW_GOLEM = new PlaceableItemEntityInfo<>("snow_golem_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> VILLAGER = new PlaceableItemEntityInfo<>("villager_doll", 5/16f, 8/16f, DollEntity::new);

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

   public static void registerClient() {
      DollEntity.registerClient(ALLAY.entity, ALLAY.layer, ALLAY.texturePath);
      DollEntity.registerClient(CREEPER.entity, CREEPER.layer, CREEPER.texturePath);
      DollEntity.registerClient(GUARDIAN.entity, GUARDIAN.layer, GUARDIAN.texturePath);
      DollEntity.registerClient(HORSE.entity, HORSE.layer, HORSE.texturePath);
      DollEntity.registerClient(SHULKER.entity, SHULKER.layer, SHULKER.texturePath);
      DollEntity.registerClient(SKELETON.entity, SKELETON.layer, SKELETON.texturePath);
      DollEntity.registerClient(SNOW_GOLEM.entity, SNOW_GOLEM.layer, SNOW_GOLEM.texturePath);
      DollEntity.registerClient(VILLAGER.entity, VILLAGER.layer, VILLAGER.texturePath);
   }

   public static void registerClient(EntityType<DollEntity> entity, EntityModelLayer layer, Identifier texturePath) {
      EntityModelLayerRegistry.registerModelLayer(layer, DollEntity::getTexturedModelData);
      EntityRendererRegistry.register(entity, (context) -> new PlaceableItemEntityRenderer<DollEntity>(context, layer, SHADOW_RADIUS) {
         @Override
         public Identifier getTexture(DollEntity entity) {
            return texturePath;
         }
      });
   }

   public static TexturedModelData getTexturedModelData() {
      var modelData = new ModelData();

      modelData.getRoot().addChild(
         EntityModelPartNames.CUBE,
         ModelPartBuilder.create().uv(0, 0).cuboid(-3f, 0f, -2f, 6f, 8f, 4f),
         ModelTransform.pivot(0f, 16f, 0f)
      );
      
      return TexturedModelData.of(modelData, 20, 12);
   }
}
