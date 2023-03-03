package com.ukrech;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

import com.ukrech.block.PhantomPrismBlock;
import com.ukrech.entity.ClientBlobEntity;
import com.ukrech.entity.ClientDollEntity;
import com.ukrech.entity.ClientTokenEntity;
import com.ukrech.item.BlobItem;
import com.ukrech.item.DollItem;
import com.ukrech.item.HoneyBallItem;
import com.ukrech.item.SoulCompassItem;
import com.ukrech.item.TokenItem;

public class TabletopClient implements ClientModInitializer {
   @Override
   public void onInitializeClient() {
      PhantomPrismBlock.registerClient();

      SoulCompassItem.registerClient();
      BlobItem.registerClient();
      TokenItem.registerClient();
      DollItem.registerClient();

      ClientBlobEntity.registerClient();
      ClientTokenEntity.registerClient();
      ClientDollEntity.registerClient();

      ModelPredicateProviderRegistry.register(HoneyBallItem.ITEM, new Identifier("storing"), HoneyBallItem::isStoring);
      ModelPredicateProviderRegistry.register(SoulCompassItem.ITEM, new Identifier("angle"), new CompassAnglePredicateProvider(SoulCompassItem::getPos));
   }
}
