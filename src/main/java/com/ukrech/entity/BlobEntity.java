package com.ukrech.entity;

import com.ukrech.Tabletop;
import com.ukrech.item.BlobItem;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BlobEntity extends PlaceableItemEntity {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "blob_entity");
   public static final EntityModelLayer LAYER = new EntityModelLayer(ID, "main");
   public static final Identifier TEXTURE_PATH = new Identifier(Tabletop.MOD_ID, "textures/entity/placeableitems/blob.png");
   public static final float SHADOW_RADIUS = 3/16f;
   public static final EntityType<BlobEntity> ENTITY = FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlobEntity::new)
                                                                              .dimensions(EntityDimensions.fixed(0.1875f, 0.1875f))
                                                                              .build();

   //

   public BlobEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static void register() {
      Registry.register(Registries.ENTITY_TYPE, ID, ENTITY);
      FabricDefaultAttributeRegistry.register(ENTITY, BlobEntity.createPlaceableItemAttributes());
   }

   public static void registerClient() {
      EntityModelLayerRegistry.registerModelLayer(LAYER, PlaceableItemEntityModel::getTexturedModelData);
      EntityRendererRegistry.register(ENTITY, (context) -> new PlaceableItemEntityRenderer<BlobEntity>(context, PlaceableItemEntityModel::new, LAYER, SHADOW_RADIUS) {
         @Override
         public Identifier getTexture(BlobEntity entity) {
            return TEXTURE_PATH;
         }

         @Override
         protected boolean hasLabel(BlobEntity entity) {
            return entity.hasCustomName();
         }
      });
   }

   //

   @Override
   protected Item getOriginItem() {
      return BlobItem.ITEM;
   }
}
