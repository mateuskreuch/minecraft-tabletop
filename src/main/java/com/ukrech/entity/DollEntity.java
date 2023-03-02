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
   public static class DollEntityInfo extends PlaceableItemEntityInfo<DollEntity> {
      public DollEntityInfo(String name) {
         super(name, 5/16f, 8/16f, DollEntity::new);

         this.texturePath = new Identifier(Tabletop.MOD_ID, String.format("textures/item/%s.png", name));
      }
   }

   //

   public static final float SHADOW_RADIUS = 4/16f;

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
