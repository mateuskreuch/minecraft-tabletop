package com.ukrech.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ukrech.Tabletop;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

@Mixin(FireworkRocketItem.class)
public class TntFireworkRocketTooltipMixin {
   @Inject(
      method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
      at = @At("TAIL")
   )
   private void addTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
      var nbt = stack.getSubNbt(FireworkRocketItem.FIREWORKS_KEY);

      if (nbt.getBoolean(Tabletop.TNT_FIREWORK_KEY)) {
         tooltip.add(Text.translatable("item.minecraft.firework_rocket.tnt").formatted(Formatting.GRAY));
      }
   }
}
