package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

public class TokenEntityRenderer extends PlaceableItemEntityRenderer<TokenEntity> {
   public static final Identifier TEXTURE_PATH = new Identifier(Tabletop.MOD_ID, "textures/entity/token/token.png");
   
   //
   
   public TokenEntityRenderer(Context context) {
      super(context, new TokenEntityModel(context.getPart(TokenEntityModel.LAYER)), 0.1875f);
   }

   //

   @Override
   public Identifier getTexture(TokenEntity entity) {
      return TEXTURE_PATH;
   }

   @Override
   protected boolean hasLabel(TokenEntity entity) {
      return entity == this.dispatcher.targetedEntity;
   }
}
