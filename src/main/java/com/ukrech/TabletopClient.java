package com.ukrech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import com.ukrech.block.PhantomPrismBlock;
import com.ukrech.entity.BlobEntity;
import com.ukrech.entity.BlobEntityModel;
import com.ukrech.entity.BlobEntityRenderer;
import com.ukrech.entity.TokenEntity;
import com.ukrech.entity.TokenEntityModel;
import com.ukrech.entity.TokenEntityRenderer;
import com.ukrech.item.BlobItem;
import com.ukrech.item.HoneyBallItem;
import com.ukrech.item.SoulCompassItem;
import com.ukrech.item.TokenItem;

public class TabletopClient implements ClientModInitializer {
   @Override
   public void onInitializeClient() {
      ColorProviderRegistry.BLOCK.register(PhantomPrismBlock::getBlockColor, PhantomPrismBlock.BLOCK);

      Tabletop.LOGGER.info("Registered block colors");

      ColorProviderRegistry.ITEM.register(PhantomPrismBlock::getItemColor, PhantomPrismBlock.ITEM);
      ColorProviderRegistry.ITEM.register(BlobItem::getItemColor, BlobItem.ITEM);
      ColorProviderRegistry.ITEM.register(SoulCompassItem::getItemColor, SoulCompassItem.ITEM);
      ColorProviderRegistry.ITEM.register(TokenItem::getItemColor, TokenItem.ITEM);

      Tabletop.LOGGER.info("Registered item colors");

      EntityRendererRegistry.register(BlobEntity.ENTITY, BlobEntityRenderer::new);
      EntityRendererRegistry.register(TokenEntity.ENTITY, TokenEntityRenderer::new);

      Tabletop.LOGGER.info("Registered entity renderers");

      EntityModelLayerRegistry.registerModelLayer(BlobEntityModel.LAYER, BlobEntityModel::getTexturedModelData);
      EntityModelLayerRegistry.registerModelLayer(TokenEntityModel.LAYER, TokenEntityModel::getTexturedModelData);

      Tabletop.LOGGER.info("Registered model layers");

      ModelPredicateProviderRegistry.register(SoulCompassItem.ITEM, new Identifier("angle"), new CompassAnglePredicateProvider(SoulCompassItem::getPos));
      ModelPredicateProviderRegistry.register(HoneyBallItem.ITEM, new Identifier("storing"), HoneyBallItem::isStoring);

      Tabletop.LOGGER.info("Registered model predicates");
   }
}
