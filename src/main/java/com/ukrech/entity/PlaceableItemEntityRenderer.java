package com.ukrech.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.TintableCompositeModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class PlaceableItemEntityRenderer<E extends PlaceableItemEntity> extends LivingEntityRenderer<E, TintableCompositeModel<E>> {
   public PlaceableItemEntityRenderer(Context context, PlaceableItemEntityModelProvider<E> modelProvider, EntityModelLayer layer, float shadowRadius) {
      super(context, modelProvider.get(context.getPart(layer)), shadowRadius);
   }

   //

   @Override
   public void render(E entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
      var color = Vec3d.unpackRgb(entity.getColor()).toVector3f();

      this.model.setColorMultiplier(color.x, color.y, color.z);
      super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
      this.model.setColorMultiplier(1f, 1f, 1f);
   }

   @Override
   public Identifier getTexture(E entity) {
      throw new UnsupportedOperationException("Unimplemented method 'getTexture'");
   }

   @Override
   protected boolean hasLabel(E entity) {
      return entity == this.dispatcher.targetedEntity;
   }

   //

   @FunctionalInterface
   public interface PlaceableItemEntityModelProvider<E extends PlaceableItemEntity> {
      PlaceableItemEntityModel<E> get(ModelPart modelPart);
   }
}
