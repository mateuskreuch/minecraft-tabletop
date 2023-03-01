package com.ukrech.event;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.hit.HitResult;

public class ItemRaycastEvent {
   public static Identifier ID = new Identifier(Tabletop.MOD_ID, "item_raycast");

   //

   public static void register() {
      ServerPlayNetworking.registerGlobalReceiver(ID, ItemRaycastEvent::receive);
   }

   public static boolean trySend(Hand hand) {
      var client = MinecraftClient.getInstance();
      var hit = client.crosshairTarget;

      if (hit.getType() != HitResult.Type.MISS) {
         var pos = hit.getPos().toVector3f();
         var packet = PacketByteBufs.create();

         packet.writeEnumConstant(hand);
         packet.writeFloat(pos.x);
         packet.writeFloat(pos.y);
         packet.writeFloat(pos.z);

         ClientPlayNetworking.send(ID, packet);

         return true;
      }
      else {
         return false;
      }
   }

   public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
      var hand = packet.readEnumConstant(Hand.class);
      var item = player.getStackInHand(hand).getItem();

      if (item instanceof RaycastingItem) {
         ((RaycastingItem) item).onRaycast(
            player,
            hand,
            new Vec3d(packet.readFloat(), packet.readFloat(), packet.readFloat())
         );
      }
   }

   //

   public interface RaycastingItem {
      public void onRaycast(PlayerEntity player, Hand hand, Vec3d hit);
   }
}
