package com.ukrech.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public interface RaycastableItem {
   public void onRaycast(ItemStack stack, PlayerEntity player, Vec3d hit);
}
