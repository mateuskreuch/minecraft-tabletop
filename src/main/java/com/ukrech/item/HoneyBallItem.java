package com.ukrech.item;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class HoneyBallItem extends Item {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "honey_ball");
   public static final HoneyBallItem ITEM = new HoneyBallItem(new FabricItemSettings().maxCount(1));
   public static final String STATE_KEY = Tabletop.MOD_ID.concat("_blockstate");

   //

   public HoneyBallItem(Settings settings) {
      super(settings);
   }

   //

   public static void register() {
      Tabletop.register(ID, ITEM);
   }

   public static void registerClient() {
      ModelPredicateProviderRegistry.register(ITEM, new Identifier("storing"), HoneyBallItem::isStoring);
   }

   public static float isStoring(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
      return stack.getOrCreateNbt().contains(STATE_KEY) ? 1.0f : 0.0f;
   }

   //
   
   @Override
   public ActionResult useOnBlock(ItemUsageContext context) {
      var stack = context.getStack();
      var pos = context.getBlockPos();
      var world = context.getWorld();
      var state = world.getBlockState(pos);
      var nbt = stack.getOrCreateNbt();

      if (nbt.contains(STATE_KEY)) {
         var player = context.getPlayer();
         var toPlace = (BlockItem) Tabletop.getNbtCodec(BlockState.CODEC, nbt, STATE_KEY).getBlock().asItem();
         var replace = player.isSneaking() && canStore(state, world, pos);

         // prepare placing
         stack.increment(1);

         if (replace) {
            world.removeBlock(pos, false);
         }

         if (toPlace.place(new ItemPlacementContext(context)) == ActionResult.FAIL) {
            // failed; revert
            stack.decrement(1);

            if (replace) {
               world.setBlockState(pos, state);
            }

            return ActionResult.FAIL;
         }
         else {
            if (replace) {
               Tabletop.putNbtCodec(BlockState.CODEC, nbt, STATE_KEY, state);
            }
            else {
               nbt.remove(STATE_KEY);
            }
         }
      }
      else {
         if (!canStore(state, world, pos)) {
            return ActionResult.FAIL;
         }

         Tabletop.removeBlockWithSound(world, pos, false);
         Tabletop.putNbtCodec(BlockState.CODEC, nbt, STATE_KEY, state);
      }

      return ActionResult.SUCCESS;
   }

   //

   private boolean canStore(BlockState state, World world, BlockPos pos) {
      return PistonBlock.isMovable(state, world, pos, Direction.NORTH, false, Direction.NORTH);
   }
}
