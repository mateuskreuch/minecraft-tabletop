package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BlobEntity extends PlaceableItemEntity {
   public static final PlaceableItemEntityInfo<BlobEntity> BLOB = new PlaceableItemEntityInfo<>("blob", 3/16f, 3/16f, BlobEntity::new);
   public static final float SHADOW_RADIUS = 2.5f/16f;

   //

   public BlobEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static void register() {
      Tabletop.register(BLOB.id, BLOB.entity, BlobEntity.createPlaceableItemAttributes());
   }

   public static void registerClient() {
      EntityModelLayerRegistry.registerModelLayer(BLOB.layer, () -> {
         var modelData = new ModelData();

         modelData.getRoot().addChild(
            EntityModelPartNames.CUBE,
            ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, 0f, -1.5f, 3f, 3f, 3f),
            ModelTransform.pivot(0f, 21f, 0f)
         );
         
         return TexturedModelData.of(modelData, 12, 6);
      });
   
      EntityRendererRegistry.register(BLOB.entity, (context) -> new PlaceableItemEntityRenderer<BlobEntity>(context, BLOB.layer, SHADOW_RADIUS) {
         @Override
         public Identifier getTexture(BlobEntity entity) {
            return BLOB.texturePath;
         }

         @Override
         protected boolean hasLabel(BlobEntity entity) {
            return entity.hasCustomName();
         }
      });
   }
}
