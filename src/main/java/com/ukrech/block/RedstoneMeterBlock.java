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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneMeterBlock extends Block {
   public static final IntProperty POWER = IntProperty.of("power", 0, 15);

   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "redstone_meter");
   public static final RedstoneMeterBlock BLOCK = new RedstoneMeterBlock(FabricBlockSettings.copy(Blocks.GLOWSTONE).luminance((state) -> state.get(POWER) > 0 ? 9 : 0));
   public static final BlockItem ITEM = new BlockItem(BLOCK, new FabricItemSettings());
   
   //

   public RedstoneMeterBlock(Settings settings) {
      super(settings);
      this.setDefaultState(this.getStateManager().getDefaultState().with(POWER, 0));
   }
   
   //

   public static void register() {
      Tabletop.register(ID, BLOCK, ITEM);
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
   public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
      super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
      this.updatePower(world, pos, state);
   }

   @Override
   public boolean emitsRedstonePower(BlockState state) {
      return true;
   }

   @Override
   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(POWER);
   }
}
