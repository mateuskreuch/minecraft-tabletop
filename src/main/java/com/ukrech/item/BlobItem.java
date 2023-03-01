package com.ukrech.item;

import com.ukrech.Tabletop;
import com.ukrech.entity.BlobEntity;
import com.ukrech.entity.PlaceableItemEntity;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
      Registry.register(Registries.ITEM, ID, ITEM);
      DispenserBlock.registerBehavior(ITEM, BlobItem.getDispenserBehavior());
   }

   public static void registerClient() {
      ColorProviderRegistry.ITEM.register(BlobItem::getItemColor, ITEM);
   }

   //

   @Override
   protected EntityType<? extends PlaceableItemEntity> getEntity() {
      return BlobEntity.ENTITY;
   }

   @Override
   protected int getDefaultColor() {
      return 0x8cd782;
   }

   @Override
   protected double getPlacingMargin() {
      return 0.8125;
   }

   @Override
   protected void playPlacingSound(World world, double x, double y, double z) {
      world.playSound(null, x, y, z, SoundEvents.ENTITY_SLIME_JUMP_SMALL, SoundCategory.BLOCKS, 0.75f, 0.8f);
   }
}
