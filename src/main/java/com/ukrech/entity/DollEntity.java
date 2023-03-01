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

   public static final PlaceableItemEntityInfo<DollEntity> HORSE = new PlaceableItemEntityInfo<>("horse_doll", 5/16f, 8/16f, DollEntity::new);
   public static final PlaceableItemEntityInfo<DollEntity> SKELETON = new PlaceableItemEntityInfo<>("skeleton_doll", 5/16f, 8/16f, DollEntity::new);

   //

   public DollEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static void register() {
      Tabletop.register(HORSE.id, HORSE.entity, DollEntity.createPlaceableItemAttributes());
      Tabletop.register(SKELETON.id, SKELETON.entity, DollEntity.createPlaceableItemAttributes());
   }

   public static void registerClient() {
      DollEntity.registerClient(HORSE.entity, HORSE.layer, HORSE.texturePath);
      DollEntity.registerClient(SKELETON.entity, SKELETON.layer, SKELETON.texturePath);
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
