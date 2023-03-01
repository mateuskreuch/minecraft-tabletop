package com.ukrech.item;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EchoHoeItem extends Item {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "echo_hoe");
   public static final EchoHoeItem ITEM = new EchoHoeItem(new FabricItemSettings().maxCount(1));

   //

   public EchoHoeItem(Settings settings) {
      super(settings);
   }

   //

   public static void register() {
      Registry.register(Registries.ITEM, ID, ITEM);
   }

   //

   @Override
   public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
      return 65536.0f;
   }

   @Override
   public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
      if (!world.isClient) {
         if (miner.experienceLevel > 0 || miner.experienceProgress > 0) {
            world.setBlockState(pos, Blocks.SCULK.getDefaultState());
            world.breakBlock(pos, false);
            miner.addExperience(-1);
         }
      }

      return false;
   }
}
