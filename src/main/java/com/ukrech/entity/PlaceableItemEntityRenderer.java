package com.ukrech.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.TintableCompositeModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public abstract class PlaceableItemEntityRenderer<E extends PlaceableItemEntity> extends LivingEntityRenderer<E, TintableCompositeModel<E>> {
   public PlaceableItemEntityRenderer(Context context, TintableCompositeModel<E> model, float shadowRadius) {
      super(context, model, shadowRadius);
   }

   //

   @Override
   public void render(E entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
      var color = Vec3d.unpackRgb(entity.getColor()).toVector3f();

      this.model.setColorMultiplier(color.x, color.y, color.z);
      super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
      this.model.setColorMultiplier(1f, 1f, 1f);
   }
}
