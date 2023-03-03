package com.ukrech.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.util.Identifier;

public class ClientBlobEntity {
   public static final float SHADOW_RADIUS = 2.5f/16f;
   public static final EntityModelLayer LAYER = new EntityModelLayer(BlobEntity.BLOB.id, "main");

   //

   public static void registerClient() {
      EntityModelLayerRegistry.registerModelLayer(LAYER, () -> {
         var modelData = new ModelData();

         modelData.getRoot().addChild(
            EntityModelPartNames.CUBE,
            ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, 0f, -1.5f, 3f, 3f, 3f),
            ModelTransform.pivot(0f, 21f, 0f)
         );
         
         return TexturedModelData.of(modelData, 12, 6);
      });
   
      EntityRendererRegistry.register(BlobEntity.BLOB.entity, (context) -> new PlaceableItemEntityRenderer<BlobEntity>(context, LAYER, SHADOW_RADIUS) {
         @Override
         public Identifier getTexture(BlobEntity entity) {
            return BlobEntity.BLOB.texturePath;
         }

         @Override
         protected boolean hasLabel(BlobEntity entity) {
            return entity.hasCustomName();
         }
      });
   }
}
