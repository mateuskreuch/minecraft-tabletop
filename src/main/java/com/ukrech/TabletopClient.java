package com.ukrech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

import com.ukrech.block.PhantomPrismBlock;
import com.ukrech.entity.BlobEntity;
import com.ukrech.entity.TokenEntity;
import com.ukrech.item.BlobItem;
import com.ukrech.item.HoneyBallItem;
import com.ukrech.item.SoulCompassItem;
import com.ukrech.item.TokenItem;

public class TabletopClient implements ClientModInitializer {
   @Override
   public void onInitializeClient() {
      ColorProviderRegistry.BLOCK.register(PhantomPrismBlock::getBlockColor, PhantomPrismBlock.BLOCK);

      //

      ColorProviderRegistry.ITEM.register(PhantomPrismBlock::getItemColor, PhantomPrismBlock.ITEM);
      ColorProviderRegistry.ITEM.register(SoulCompassItem::getItemColor, SoulCompassItem.ITEM);
      ColorProviderRegistry.ITEM.register(BlobItem::getItemColor, BlobItem.ITEM);
      ColorProviderRegistry.ITEM.register(TokenItem::getItemColor, TokenItem.ITEM);

      //

      BlobEntity.register();
      TokenEntity.register();
      
      //

      ModelPredicateProviderRegistry.register(SoulCompassItem.ITEM, new Identifier("angle"), new CompassAnglePredicateProvider(SoulCompassItem::getPos));
      ModelPredicateProviderRegistry.register(HoneyBallItem.ITEM, new Identifier("storing"), HoneyBallItem::isStoring);
   }
}
