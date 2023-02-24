package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

public class BlobEntityRenderer extends PlaceableItemEntityRenderer<BlobEntity> {
   public static final Identifier TEXTURE_PATH = new Identifier(Tabletop.MOD_ID, "textures/entity/blob/blob.png");
   
   //
   
   public BlobEntityRenderer(Context context) {
      super(context, new BlobEntityModel(context.getPart(BlobEntityModel.LAYER)), 0.1875f);
   }

   //

   @Override
   public Identifier getTexture(BlobEntity entity) {
      return TEXTURE_PATH;
   }

   @Override
   protected boolean hasLabel(BlobEntity entity) {
      return entity.hasCustomName();
   }
}
