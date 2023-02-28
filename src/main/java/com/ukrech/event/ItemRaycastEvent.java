package com.ukrech.event;

import com.ukrech.Tabletop;
import com.ukrech.item.RaycastableItem;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ItemRaycastEvent {
   public static Identifier ID = new Identifier(Tabletop.MOD_ID, "item_raycast");

   public static void send(ItemStack stack) {
      var client = MinecraftClient.getInstance();
      var hit = client.crosshairTarget.getPos().toVector3f();
      var packet = PacketByteBufs.create();

      packet.writeItemStack(stack);
      packet.writeFloat(hit.x);
      packet.writeFloat(hit.y);
      packet.writeFloat(hit.z);

      ClientPlayNetworking.send(ID, packet);
   }

   public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
      var stack = packet.readItemStack();

      ((RaycastableItem) stack.getItem()).onRaycast(
         stack,
         player,
         new Vec3d(packet.readFloat(), packet.readFloat(), packet.readFloat())
      );
   }
}
