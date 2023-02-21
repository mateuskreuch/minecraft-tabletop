package com.ukrech.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.ukrech.Tabletop;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(FireworkRocketEntity.class)
public class TntFireworkRocketEffectMixin {
   @Inject(at = @At("TAIL"), method = "explode()V", locals = LocalCapture.CAPTURE_FAILHARD)
   private void addTntAddonEffect(CallbackInfo ci, float f, ItemStack stack) {
      var power = stack.getOrCreateSubNbt(FireworkRocketItem.FIREWORKS_KEY).getByte(Tabletop.TNT_FIREWORK_KEY);

      if (power > 0) {
         var rocket = ((FireworkRocketEntity)(Object) this);

         rocket.world.createExplosion(rocket, rocket.getX(), rocket.getY(), rocket.getZ(), 2.0f + power, World.ExplosionSourceType.TNT);
      }
   }
}
