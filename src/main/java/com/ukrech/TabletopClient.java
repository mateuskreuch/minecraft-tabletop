package com.ukrech;

import net.fabricmc.api.ClientModInitializer;

import com.ukrech.block.PhantomPrismBlock;
import com.ukrech.entity.BlobEntity;
import com.ukrech.entity.HorseDollEntity;
import com.ukrech.entity.TokenEntity;
import com.ukrech.item.BlobItem;
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
      HoneyBallItem.registerClient();

      BlobEntity.registerClient();
      TokenEntity.registerClient();
      HorseDollEntity.registerClient();
   }
}
