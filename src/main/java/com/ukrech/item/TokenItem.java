package com.ukrech.item;

import org.jetbrains.annotations.Nullable;

import com.ukrech.Tabletop;
import com.ukrech.entity.TokenEntity;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TokenItem extends PlaceableItem {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "token");
   public static final TokenItem ITEM = new TokenItem(new FabricItemSettings());

   //

   public TokenItem(Settings settings) {
      super(settings);
   }

   //

   public static void register() {
      PlaceableItem.register(ID, ITEM, TokenEntity.TOKEN.entity, TokenItem.getDispenserBehavior());
   }

   public static void registerClient() {
      ColorProviderRegistry.ITEM.register(TokenItem::getItemColor, ITEM);
   }

   //

   @Override
   protected int getPlacingAmount(@Nullable PlayerEntity player, ItemStack stack) {
      return player == null || player.isSneaking() ? stack.getCount() : 1;
   }

   @Override
   protected int getDefaultColor() {
      return 0xeb693a;
   }

   @Override
   protected void playPlacingSound(World world, double x, double y, double z) {
      world.playSound(null, x, y, z, SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.75f, 1.5f);
   }
}
