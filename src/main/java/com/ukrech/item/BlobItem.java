package com.ukrech.item;

import com.ukrech.Tabletop;
import com.ukrech.entity.BlobEntity;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public class BlobItem extends Item implements DyeableItem {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "blob");
   public static final BlobItem ITEM = new BlobItem(new FabricItemSettings());

   //

   public BlobItem(Settings settings) {
      super(settings);
   }

   //

   public static int getItemColor(ItemStack itemStack, int tintIndex) {
      return ((DyeableItem) itemStack.getItem()).getColor(itemStack);
   }

   public static void registerDispenser() {
      DispenserBlock.registerBehavior(ITEM, new ItemDispenserBehavior() {
         @Override
         public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            var direction = pointer.getBlockState().get(DispenserBlock.FACING);
            var blockPos = pointer.getPos().offset(direction);

            BlobItem.place(stack, blockPos, pointer.getWorld(), null);
   
            return stack;
         }
      });
   }

   public static boolean place(ItemStack stack, BlockPos pos, ServerWorld world, PlayerEntity player) {
      var entity = BlobEntity.ENTITY.create(
         world,
         stack.getNbt(),
         EntityType.nbtCopier(e -> {}, world, stack, player),
         pos,
         SpawnReason.SPAWN_EGG,
         true,
         true
      );

      if (entity == null) {
         return false;
      }

      var x = entity.getX() + 0.8*(world.random.nextDouble() - 0.5);
      var y = entity.getY();
      var z = entity.getZ() + 0.8*(world.random.nextDouble() - 0.5);

      entity.setOriginItemStack(stack);
      entity.refreshPositionAndAngles(x, y, z, world.random.nextBetween(0, 360), 0.0f);
      world.spawnEntityAndPassengers(entity);
      world.playSound(null, x, y, z, SoundEvents.ENTITY_SLIME_JUMP_SMALL, SoundCategory.BLOCKS, 0.75f, 0.8f);
      
      if (player != null) {
         entity.emitGameEvent(GameEvent.ENTITY_PLACE, player);
      }

      stack.decrement(1);

      return true;
   }

   //

   public int getColor(ItemStack stack) {
      var nbtCompound = stack.getSubNbt(DISPLAY_KEY);

      if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
         return nbtCompound.getInt(COLOR_KEY);
      }
      else {
         return 0x8cd782;
      }
   }

   //

   @Override
   public ActionResult useOnBlock(ItemUsageContext context) {
      var world = context.getWorld();

      if (world instanceof ServerWorld) {
         BlobItem.place(context.getStack(), new ItemPlacementContext(context).getBlockPos(), (ServerWorld) world, context.getPlayer());
      }

      return ActionResult.success(world.isClient);
   }
}
