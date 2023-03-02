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
   public static final Identifier ALLAY_ID = new Identifier(Tabletop.MOD_ID, "allay_doll");
   public static final Identifier CREEPER_ID = new Identifier(Tabletop.MOD_ID, "creeper_doll");
   public static final Identifier GUARDIAN_ID = new Identifier(Tabletop.MOD_ID, "guardian_doll");
   public static final Identifier HORSE_ID = new Identifier(Tabletop.MOD_ID, "horse_doll");
   public static final Identifier SHULKER_ID = new Identifier(Tabletop.MOD_ID, "shulker_doll");
   public static final Identifier SKELETON_ID = new Identifier(Tabletop.MOD_ID, "skeleton_doll");
   public static final Identifier SNOW_GOLEM_ID = new Identifier(Tabletop.MOD_ID, "snow_golem_doll");
   public static final Identifier VILLAGER_ID = new Identifier(Tabletop.MOD_ID, "villager_doll");
   public static final DollItem ALLAY_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem CREEPER_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem GUARDIAN_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem HORSE_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem SHULKER_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem SKELETON_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem SNOW_GOLEM_ITEM = new DollItem(new FabricItemSettings());
   public static final DollItem VILLAGER_ITEM = new DollItem(new FabricItemSettings());

   //

   public DollItem(Settings settings) {
      super(settings);
   }

   //

   public static void register() {
      PlaceableItem.register(ALLAY_ID, ALLAY_ITEM, DollEntity.ALLAY.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(CREEPER_ID, CREEPER_ITEM, DollEntity.CREEPER.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(GUARDIAN_ID, GUARDIAN_ITEM, DollEntity.GUARDIAN.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(HORSE_ID, HORSE_ITEM, DollEntity.HORSE.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(SHULKER_ID, SHULKER_ITEM, DollEntity.SHULKER.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(SKELETON_ID, SKELETON_ITEM, DollEntity.SKELETON.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(SNOW_GOLEM_ID, SNOW_GOLEM_ITEM, DollEntity.SNOW_GOLEM.entity, DollItem.getDispenserBehavior());
      PlaceableItem.register(VILLAGER_ID, VILLAGER_ITEM, DollEntity.VILLAGER.entity, DollItem.getDispenserBehavior());
   }

   public static void registerClient() {
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, ALLAY_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, CREEPER_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, GUARDIAN_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, HORSE_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, SHULKER_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, SKELETON_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, SNOW_GOLEM_ITEM);
      ColorProviderRegistry.ITEM.register(DollItem::getItemColor, VILLAGER_ITEM);
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
