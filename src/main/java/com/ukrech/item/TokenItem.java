package com.ukrech.item;

import com.ukrech.Tabletop;
import com.ukrech.entity.PlaceableItemEntity;
import com.ukrech.entity.TokenEntity;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TokenItem extends PlaceableItem {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "token");
   public static final TokenItem ITEM = new TokenItem(new FabricItemSettings());

   //

   public TokenItem(Settings settings) {
      super(settings);
   }

   //

   public static ItemDispenserBehavior getDispenserBehavior() {
      return new ItemDispenserBehavior() {
         @Override
         public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            var direction = pointer.getBlockState().get(DispenserBlock.FACING);
            var pos = pointer.getPos().offset(direction).toCenterPos();

            ((PlaceableItem) stack.getItem()).place(stack, pos, pointer.getWorld(), null, stack.getCount());
   
            return stack;
         }
      };
   }

   //

   @Override
   public void onRaycast(ItemStack stack, PlayerEntity player, Vec3d hit) {
      ((PlaceableItem) stack.getItem()).place(
         stack,
         hit,
         (ServerWorld) player.world,
         player,
         player.isSneaking() ? stack.getCount() : 1
      );
   }

   @Override
   protected EntityType<? extends PlaceableItemEntity> getEntity() {
      return TokenEntity.ENTITY;
   }

   @Override
   protected int getDefaultColor() {
      return 0xeb693a;
   }

   @Override
   protected double getPlacingMargin() {
      return 0.8125;
   }

   @Override
   protected void playPlacingSound(World world, double x, double y, double z) {
      world.playSound(null, x, y, z, SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.75f, 1.5f);
   }
}
