package com.ukrech.item;

import com.ukrech.Tabletop;
import com.ukrech.entity.BlobEntity;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BlobItem extends PlaceableItem {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "blob");
   public static final BlobItem ITEM = new BlobItem(new FabricItemSettings());

   //

   public BlobItem(Settings settings) {
      super(settings);
   }

   //

   public static void register() {
      PlaceableItem.register(ID, ITEM, BlobEntity.BLOB.entity, BlobItem.getDispenserBehavior());
   }

   public static void registerClient() {
      ColorProviderRegistry.ITEM.register(BlobItem::getItemColor, ITEM);
   }

   //

   @Override
   protected int getDefaultColor() {
      return 0x8cd782;
   }

   @Override
   protected void playPlacingSound(World world, double x, double y, double z) {
      world.playSound(null, x, y, z, SoundEvents.ENTITY_SLIME_JUMP_SMALL, SoundCategory.BLOCKS, 0.75f, 0.8f);
   }
}
