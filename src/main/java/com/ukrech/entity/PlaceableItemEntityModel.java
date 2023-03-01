package com.ukrech.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.TintableCompositeModel;
import net.minecraft.entity.LivingEntity;

public class PlaceableItemEntityModel<E extends LivingEntity> extends TintableCompositeModel<E> {
   protected final ModelPart root;

   //
   
   public PlaceableItemEntityModel(ModelPart modelPart) {
      this.root = modelPart.getChild(EntityModelPartNames.CUBE);
   }

   //

   @Override
   public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

   @Override
   public ModelPart getPart() {
      return this.root;
   }
}
