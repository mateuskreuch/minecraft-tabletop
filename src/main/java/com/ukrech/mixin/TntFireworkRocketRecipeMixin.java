package com.ukrech.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Constant;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.FireworkRocketRecipe;

import com.ukrech.Tabletop;

@Mixin(FireworkRocketRecipe.class)
public class TntFireworkRocketRecipeMixin {
   private static final int MAX_GUNPOWDER = 3;

   @ModifyConstant(
      method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z",
      constant = @Constant(intValue = MAX_GUNPOWDER)
   )
   private int increaseMaxGunpowderPossible(int value) {
      return 8;
   }

   @Redirect(
      method = "craft(Lnet/minecraft/inventory/CraftingInventory;)Lnet/minecraft/item/ItemStack;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putByte(Ljava/lang/String;B)V")
   )
   private void assignNbt(NbtCompound nbt, String tag, byte flight) {
      nbt.putByte(tag, flight);

      if (flight > MAX_GUNPOWDER) {
         nbt.putByte(Tabletop.TNT_FIREWORK_KEY, (byte) (flight - MAX_GUNPOWDER));
      }
   }
}