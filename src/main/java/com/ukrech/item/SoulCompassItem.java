package com.ukrech.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import com.ukrech.Tabletop;

public class SoulCompassItem extends Item {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "soul_compass");
   public static final SoulCompassItem ITEM = new SoulCompassItem(new FabricItemSettings().maxCount(1));
   public static final String POS_KEY = Tabletop.MOD_ID.concat("_pos");

   private static final double EFFECT_DISTANCE = 32.0; 
   private static final int EFFECT_DURATION = 200;

   //

   public SoulCompassItem(Settings settings) {
      super(settings);
   }

   //

   public static int getItemColor(ItemStack itemStack, int tintIndex) {
      return 0x7cffd2;
   }

   public static GlobalPos getPos(ClientWorld world, ItemStack stack, Entity entity) {
      var nbt = stack.getNbt();

      return nbt != null ? Tabletop.getNbtCodec(GlobalPos.CODEC, nbt, POS_KEY) : null;
   }

   // 

   @Override
   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
      var stack = user.getStackInHand(hand);

      if (!world.isClient) {
         var pos = user.getPos();
         var closestPlayer = user;
         var closestDistance = Double.POSITIVE_INFINITY;

         for (var player : PlayerLookup.world((ServerWorld) world)) {
            var distance = player.squaredDistanceTo(pos);

            if (distance <= EFFECT_DISTANCE) {
               player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, EFFECT_DURATION, 0));
            }

            if (player != user && distance < closestDistance) {
               closestDistance = distance;
               closestPlayer = player;
            }
         }

         var globalPos = GlobalPos.create(world.getRegistryKey(), closestPlayer.getBlockPos());
         
         Tabletop.putNbtCodec(GlobalPos.CODEC, stack.getOrCreateNbt(), POS_KEY, globalPos);
         closestPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, EFFECT_DURATION, 0));
      }

      user.getItemCooldownManager().set(this, EFFECT_DURATION);

      return TypedActionResult.consume(stack);
   }
}
