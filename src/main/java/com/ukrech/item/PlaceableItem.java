package com.ukrech.item;

import org.jetbrains.annotations.Nullable;

import com.ukrech.Tabletop;
import com.ukrech.entity.PlaceableItemEntity;
import com.ukrech.event.ItemRaycastEvent;
import com.ukrech.event.ItemRaycastEvent.RaycastingItem;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public abstract class PlaceableItem extends Item implements DyeableItem, RaycastingItem {
   public PlaceableItem(Settings settings) {
      super(settings);
   }

   //

   public static void register(Identifier id, Item item, EntityType<? extends PlaceableItemEntity> entity, ItemDispenserBehavior behavior) {
      Tabletop.register(id, item);
      Tabletop.PLACEABLE_ITEM_ENTITY_PAIRS.put(item, entity);
      DispenserBlock.registerBehavior(item, behavior);
   }

   public static int getItemColor(ItemStack itemStack, int tintIndex) {
      return ((DyeableItem) itemStack.getItem()).getColor(itemStack);
   }

   public static ItemDispenserBehavior getDispenserBehavior() {
      return new ItemDispenserBehavior() {
         @Override
         public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            var direction = pointer.getBlockState().get(DispenserBlock.FACING);
            var pos = pointer.getPos().offset(direction).toCenterPos();
            var item = ((PlaceableItem) stack.getItem());

            item.place(stack, pos, pointer.getWorld(), null, item.getPlacingAmount(null, stack));
   
            return stack;
         }
      };
   }

   //

   public boolean place(ItemStack stack, Vec3d pos, ServerWorld world, PlayerEntity player, int amount) {
      var entity = Tabletop.PLACEABLE_ITEM_ENTITY_PAIRS.get(this).create(
         world,
         stack.getNbt(),
         EntityType.nbtCopier(e -> {}, world, stack, player),
         new BlockPos(pos),
         SpawnReason.SPAWN_EGG,
         true,
         true
      );

      if (entity == null) {
         return false;
      }

      var x = pos.getX();
      var y = pos.getY();
      var z = pos.getZ();

      if (player == null) {
         var s = this.getPlacingStep();
         var m = this.getPlacingMargin();

         x += m*(Math.floor(s*(world.random.nextDouble() - 0.5) + 0.5)/s);
         z += m*(Math.floor(s*(world.random.nextDouble() - 0.5) + 0.5)/s);
      }

      entity.setOriginItemStack(stack.copyWithCount(amount));
      entity.refreshPositionAndAngles(x, y, z, world.random.nextBetween(0, 360), 0.0f);
      world.spawnEntityAndPassengers(entity);
      this.playPlacingSound(world, x, y, z);
      
      if (player != null) {
         entity.emitGameEvent(GameEvent.ENTITY_PLACE, player);
      }

      stack.decrement(amount);

      return true;
   }

   //

   public int getColor(ItemStack stack) {
      var nbtCompound = stack.getSubNbt(DISPLAY_KEY);

      if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
         return nbtCompound.getInt(COLOR_KEY);
      }
      else {
         return this.getDefaultColor();
      }
   }

   public void onRaycast(PlayerEntity player, Hand hand, Vec3d hit) {
      var stack = player.getStackInHand(hand);

      ((PlaceableItem) stack.getItem()).place(
         stack,
         hit,
         (ServerWorld) player.world,
         player,
         this.getPlacingAmount(player, stack)
      );
   }

   //

   protected abstract int getDefaultColor();
   protected abstract void playPlacingSound(World world, double x, double y, double z);
   
   protected int getPlacingAmount(@Nullable PlayerEntity player, ItemStack stack) { return 1; }
   protected double getPlacingMargin() { return 0.8125; };
   protected double getPlacingStep() { return 8.0; };

   //

   @Override
   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
      var stack = user.getStackInHand(hand);

      if (world.isClient) {
         if (ItemRaycastEvent.trySend(hand)) {
            return TypedActionResult.success(stack);
         }
         else {
            return TypedActionResult.fail(stack);
         }
      }
      else {
         return TypedActionResult.consume(stack);
      }
   }
}
