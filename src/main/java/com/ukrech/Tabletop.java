package com.ukrech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ukrech.block.PhantomPrismBlock;
import com.ukrech.block.RedstoneMeterBlock;
import com.ukrech.event.ItemRaycastEvent;
import com.ukrech.entity.BlobEntity;
import com.ukrech.entity.TokenEntity;
import com.ukrech.item.BlobItem;
import com.ukrech.item.EchoHoeItem;
import com.ukrech.item.HoneyBallItem;
import com.ukrech.item.SoulCompassItem;
import com.ukrech.item.TokenItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.serialization.Codec;

public class Tabletop implements ModInitializer {
   public static final String MOD_ID = "tabletop";
   public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

   public static final String TNT_FIREWORK_KEY = MOD_ID.concat("Tnt");

   private static final ItemGroup TAB_GROUP = FabricItemGroup.builder(new Identifier(MOD_ID, "all"))
                                                             .displayName(Text.of("Tabletop"))
                                                             .icon(() -> new ItemStack(RedstoneMeterBlock.ITEM))
                                                             .build();

   //

   @Override
   public void onInitialize() {
      Registry.register(Registries.BLOCK, PhantomPrismBlock.ID, PhantomPrismBlock.BLOCK);
      Registry.register(Registries.BLOCK, RedstoneMeterBlock.ID, RedstoneMeterBlock.BLOCK);

      //

      Registry.register(Registries.ITEM, BlobItem.ID, BlobItem.ITEM);
      Registry.register(Registries.ITEM, TokenItem.ID, TokenItem.ITEM);
      Registry.register(Registries.ITEM, SoulCompassItem.ID, SoulCompassItem.ITEM);
      Registry.register(Registries.ITEM, HoneyBallItem.ID, HoneyBallItem.ITEM);
      Registry.register(Registries.ITEM, EchoHoeItem.ID, EchoHoeItem.ITEM);

      Registry.register(Registries.ITEM, PhantomPrismBlock.ID, PhantomPrismBlock.ITEM);
      Registry.register(Registries.ITEM, RedstoneMeterBlock.ID, RedstoneMeterBlock.ITEM);

      //

      Registry.register(Registries.ENTITY_TYPE, BlobEntity.ID, BlobEntity.ENTITY);
      FabricDefaultAttributeRegistry.register(BlobEntity.ENTITY, BlobEntity.createPlaceableItemAttributes());

      Registry.register(Registries.ENTITY_TYPE, TokenEntity.ID, TokenEntity.ENTITY);
      FabricDefaultAttributeRegistry.register(TokenEntity.ENTITY, TokenEntity.createPlaceableItemAttributes());

      //

      DispenserBlock.registerBehavior(BlobItem.ITEM, BlobItem.getDispenserBehavior());
      DispenserBlock.registerBehavior(TokenItem.ITEM, TokenItem.getDispenserBehavior());

      //

      ItemGroupEvents.modifyEntriesEvent(TAB_GROUP).register(content -> {
         content.add(BlobItem.ITEM);
         content.add(TokenItem.ITEM);
         content.add(HoneyBallItem.ITEM);
         content.add(SoulCompassItem.ITEM);
         content.add(EchoHoeItem.ITEM);

         content.add(PhantomPrismBlock.ITEM);
         content.add(RedstoneMeterBlock.ITEM);
      });

      //

      ServerPlayNetworking.registerGlobalReceiver(ItemRaycastEvent.ID, ItemRaycastEvent::receive);
   }

   //

   public static void removeBlockWithSound(World world, BlockPos pos, boolean move) {
      var sound = world.getBlockState(pos).getSoundGroup();
      
      world.removeBlock(pos, move);
      world.playSound(
         null,
         pos,
         sound.getBreakSound(),
         SoundCategory.BLOCKS,
         sound.getVolume(),
         sound.getPitch()
      );
   }

   public static <T> void putNbtCodec(Codec<T> codec, NbtCompound nbt, String key, T value) {
      codec.encodeStart(NbtOps.INSTANCE, value)
           .resultOrPartial(LOGGER::error)
           .ifPresent(element -> nbt.put(key, (NbtElement) element));
   }

   public static <T> T getNbtCodec(Codec<T> codec, NbtCompound nbt, String key) {
      return codec.parse(NbtOps.INSTANCE, nbt.get(key))
                  .resultOrPartial(Tabletop.LOGGER::error)
                  .orElse(null);
   }
}
