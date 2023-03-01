package com.ukrech.item;

import com.ukrech.Tabletop;
import com.ukrech.entity.DollEntity;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DollItem extends PlaceableItem {
   public static final Identifier HORSE_ID = new Identifier(Tabletop.MOD_ID, "horse_doll");
   public static final Identifier SKELETON_ID = new Identifier(Tabletop.MOD_ID, "skeleton_doll");
   public static final DollItem HORSE_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem SKELETON_ITEM = new DollItem(new FabricItemSettings());

   //

   public DollItem(Settings settings) {
      super(settings);
   }

   //

   public static void register() {
      PlaceableItem.register(HORSE_ID, HORSE_ITEM, DollEntity.HORSE.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(SKELETON_ID, SKELETON_ITEM, DollEntity.SKELETON.entity, DollItem.getDispenserBehavior());
   }

   public static void registerClient() {
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, HORSE_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, SKELETON_ITEM);
   }

   //

   @Override
   protected int getDefaultColor() {
      return 0xeeeeee;
   }

   @Override
   protected double getPlacingMargin() {
      return 0.6875;
   }

   @Override
   protected void playPlacingSound(World world, double x, double y, double z) {
      world.playSound(null, x, y, z, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 0.75f, 1.5f);
   }
}
