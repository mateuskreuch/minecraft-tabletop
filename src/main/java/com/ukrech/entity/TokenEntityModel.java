package com.ukrech.entity;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class TokenEntityModel extends PlaceableItemEntityModel<TokenEntity> {
   public TokenEntityModel(ModelPart modelPart) {
      super(modelPart);
   }

   //

   public static TexturedModelData getTexturedModelData() {
      var modelData = new ModelData();

      modelData.getRoot().addChild(
         EntityModelPartNames.CUBE,
         ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, 0f, -1.5f, 3f, 4f, 3f),
         ModelTransform.pivot(0f, 20f, 0f)
      );
      
      return TexturedModelData.of(modelData, 12, 7);
   }
}
