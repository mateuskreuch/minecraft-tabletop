package com.ukrech.mixin;

import com.ukrech.item.PlaceableItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {
   @Inject(
      method = "interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
      at = @At("HEAD"), cancellable = true
   )
   private void placeableItemsPreventRotation(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
      if (player.getStackInHand(hand).getItem() instanceof PlaceableItem) {
         ci.setReturnValue(ActionResult.PASS);
      }
   }
}
