package com.ukrech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
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
import com.ukrech.entity.DollEntity;
import com.ukrech.entity.PlaceableItemEntity;
import com.ukrech.entity.TokenEntity;
import com.ukrech.item.BlobItem;
import com.ukrech.item.EchoHoeItem;
import com.ukrech.item.HoneyBallItem;
import com.ukrech.item.DollItem;
import com.ukrech.item.SoulCompassItem;
import com.ukrech.item.TokenItem;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.serialization.Codec;

public class Tabletop implements ModInitializer {
   public static final String MOD_ID = "tabletop";
   public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

   //

   public static final String TNT_FIREWORK_KEY = MOD_ID.concat("Tnt");

   //

   public static final Map<Item, EntityType<? extends PlaceableItemEntity>> PLACEABLE_ITEM_ENTITY_PAIRS = new HashMap<Item, EntityType<? extends PlaceableItemEntity>>();

   //

   private static final ItemGroup TAB_GROUP = FabricItemGroup.builder(new Identifier(MOD_ID, "all"))
                                                             .displayName(Text.of("Tabletop"))
                                                             .icon(() -> new ItemStack(RedstoneMeterBlock.ITEM))
                                                             .build();

   //

   @Override
   public void onInitialize() {
      ItemRaycastEvent.register();

      PhantomPrismBlock.register();
      RedstoneMeterBlock.register();
      
      BlobItem.register();
      TokenItem.register();
      SoulCompassItem.register();
      HoneyBallItem.register();
      EchoHoeItem.register();

      BlobEntity.register();
      TokenEntity.register();
      HorseDollEntity.register();

      ItemGroupEvents.modifyEntriesEvent(TAB_GROUP).register(content -> {
         content.add(BlobItem.ITEM);
         content.add(TokenItem.ITEM);
         content.add(HoneyBallItem.ITEM);
         content.add(SoulCompassItem.ITEM);
         content.add(EchoHoeItem.ITEM);

         content.add(PhantomPrismBlock.ITEM);
         content.add(RedstoneMeterBlock.ITEM);
      });
   }

   //

   public static void register(Identifier id, Item item) {
      Registry.register(Registries.ITEM, id, item);
   }

   public static void register(Identifier id, Block block, Item item) {
      Registry.register(Registries.BLOCK, id, block);
      Registry.register(Registries.ITEM, id, item);
   }

   public static void register(Identifier id, EntityType<? extends LivingEntity> entity, DefaultAttributeContainer.Builder builder) {
      Registry.register(Registries.ENTITY_TYPE, id, entity);
      FabricDefaultAttributeRegistry.register(entity, builder);
   }

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
