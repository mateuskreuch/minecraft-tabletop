package com.ukrech.block;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class PhantomPrismBlock extends Block {
   public static final IntProperty POWER = IntProperty.of("power", 0, 15);

   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "phantom_prism");
   public static final PhantomPrismBlock BLOCK = new PhantomPrismBlock(FabricBlockSettings.copy(Blocks.GLOWSTONE).sounds(BlockSoundGroup.HONEY));
   public static final BlockItem ITEM = new BlockItem(BLOCK, new FabricItemSettings());
   
   //

   public PhantomPrismBlock(Settings settings) {
      super(settings);
      this.setDefaultState(this.getStateManager().getDefaultState().with(POWER, 0));
   }

   //

   public static int getBlockColor(BlockState state, BlockRenderView view, BlockPos pos, int tintIndex) {
      switch (state.get(POWER)) {
         case 1:  return 0xf18abb; // pink
         case 2:  return 0xd342cb; // magenta
         case 3:  return 0x892ac8; // purple
         case 4:  return 0x3c3fbb; // blue
         case 5:  return 0xa9a99d; // light gray
         case 6:  return 0x68abed; // light blue
         case 7:  return 0x1da0b3; // cyan
         case 8:  return 0x82d422; // lime
         case 9:  return 0x647d31; // green
         case 10: return 0x4b4f55; // gray
         case 11: return 0xba2c2c; // red
         case 12: return 0xfcd81d; // yellow
         case 13: return 0xf68601; // orange
         case 14: return 0x82512a; // brown
         case 15: return 0x191f33; // black
         default: return 0xf0e9de; // white
      }
   }

   public static int getItemColor(ItemStack itemStack, int tintIndex) {
      return 0xf0e9de;
   }

   //

   private void updatePower(World world, BlockPos pos, BlockState state) {
      var power = world.getReceivedRedstonePower(pos);

      world.setBlockState(pos, state.with(POWER, power), NOTIFY_ALL | NO_REDRAW);
   }

   //

   @Override
   public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
      if (!world.isClient) {
         this.updatePower(world, pos, state);
      }
   }

   @Override
   public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos,
         boolean notify) {
      super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
      this.updatePower(world, pos, state);
   }

   @Override
   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(POWER);
   }
}
