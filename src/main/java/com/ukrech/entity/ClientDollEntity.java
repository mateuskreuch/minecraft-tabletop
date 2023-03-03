package com.ukrech.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

public class ClientDollEntity {
   public static final float SHADOW_RADIUS = 4/16f;
   public static final EntityModelLayer ALLAY_LAYER = new EntityModelLayer(DollEntity.ALLAY.id, "main");
   public static final EntityModelLayer CREEPER_LAYER = new EntityModelLayer(DollEntity.CREEPER.id, "main");
   public static final EntityModelLayer GUARDIAN_LAYER = new EntityModelLayer(DollEntity.GUARDIAN.id, "main");
   public static final EntityModelLayer HORSE_LAYER = new EntityModelLayer(DollEntity.HORSE.id, "main");
   public static final EntityModelLayer SHULKER_LAYER = new EntityModelLayer(DollEntity.SHULKER.id, "main");
   public static final EntityModelLayer SKELETON_LAYER = new EntityModelLayer(DollEntity.SKELETON.id, "main");
   public static final EntityModelLayer SNOW_GOLEM_LAYER = new EntityModelLayer(DollEntity.SNOW_GOLEM.id, "main");
   public static final EntityModelLayer VILLAGER_LAYER = new EntityModelLayer(DollEntity.VILLAGER.id, "main");

   //

   public static void registerClient() {
      ClientDollEntity.registerClient(DollEntity.ALLAY.entity, ALLAY_LAYER, DollEntity.ALLAY.texturePath);
      ClientDollEntity.registerClient(DollEntity.CREEPER.entity, CREEPER_LAYER, DollEntity.CREEPER.texturePath);
      ClientDollEntity.registerClient(DollEntity.GUARDIAN.entity, GUARDIAN_LAYER, DollEntity.GUARDIAN.texturePath);
      ClientDollEntity.registerClient(DollEntity.HORSE.entity, HORSE_LAYER, DollEntity.HORSE.texturePath);
      ClientDollEntity.registerClient(DollEntity.SHULKER.entity, SHULKER_LAYER, DollEntity.SHULKER.texturePath);
      ClientDollEntity.registerClient(DollEntity.SKELETON.entity, SKELETON_LAYER, DollEntity.SKELETON.texturePath);
      ClientDollEntity.registerClient(DollEntity.SNOW_GOLEM.entity, SNOW_GOLEM_LAYER, DollEntity.SNOW_GOLEM.texturePath);
      ClientDollEntity.registerClient(DollEntity.VILLAGER.entity, VILLAGER_LAYER, DollEntity.VILLAGER.texturePath);
   }

   //

   private static void registerClient(EntityType<DollEntity> entity, EntityModelLayer layer, Identifier texturePath) {
      EntityModelLayerRegistry.registerModelLayer(layer, ClientDollEntity::getTexturedModelData);
      EntityRendererRegistry.register(entity, (context) -> new PlaceableItemEntityRenderer<DollEntity>(context, layer, SHADOW_RADIUS) {
         @Override
         public Identifier getTexture(DollEntity entity) {
            return texturePath;
         }
      });
   }

   private static TexturedModelData getTexturedModelData() {
      var modelData = new ModelData();

      modelData.getRoot().addChild(
         EntityModelPartNames.CUBE,
         ModelPartBuilder.create().uv(0, 0).cuboid(-3f, 0f, -2f, 6f, 8f, 4f),
         ModelTransform.pivot(0f, 16f, 0f)
      );
      
      return TexturedModelData.of(modelData, 20, 12);
   }
}
