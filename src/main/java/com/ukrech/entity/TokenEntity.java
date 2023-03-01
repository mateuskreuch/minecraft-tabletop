package com.ukrech.entity;

import com.ukrech.Tabletop;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TokenEntity extends PlaceableItemEntity {
   public static final PlaceableItemEntityInfo<TokenEntity> TOKEN = new PlaceableItemEntityInfo<>("token", 3/16f, 4/16f, TokenEntity::new);
   public static final float SHADOW_RADIUS = 2.5f/16f;

   //

   public TokenEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   public static void register() {
      Tabletop.register(TOKEN.id, TOKEN.entity, TokenEntity.createPlaceableItemAttributes());
   }

   public static void registerClient() {
      EntityModelLayerRegistry.registerModelLayer(TOKEN.layer, () -> {
         var modelData = new ModelData();

         modelData.getRoot().addChild(
            EntityModelPartNames.CUBE,
            ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, 0f, -1.5f, 3f, 4f, 3f),
            ModelTransform.pivot(0f, 20f, 0f)
         );
         
         return TexturedModelData.of(modelData, 12, 7);
      });

      EntityRendererRegistry.register(TOKEN.entity, (context) -> new PlaceableItemEntityRenderer<TokenEntity>(context, TOKEN.layer, SHADOW_RADIUS) {
         @Override
         public Identifier getTexture(TokenEntity entity) {
            return TOKEN.texturePath;
         }
      });
   }

   //

   private void updateName(ItemStack originStack) {
      if (originStack.hasCustomName()) {
         this.setCustomName(Text.of(String.format("%d %s", originStack.getCount(), originStack.getName().getString())));
      }
      else {
         this.setCustomName(Text.of(String.format("%d", originStack.getCount())));
      }
   }

   private void increment(ItemStack originStack, int count) {
      originStack.increment(count);
      this.updateName(originStack);

      if (originStack.isEmpty()) {
         this.kill();
      }
   }

   private int getMaxIncrement(ItemStack originStack, int count) {
      return Math.min(count, 100 - originStack.getCount());
   }

   //

   @Override
   public void setOriginItemStack(ItemStack stack) {
      super.setOriginItemStack(stack);
      this.updateName(stack);
   }

   @Override
   public ActionResult interact(PlayerEntity player, Hand hand) {
      var originStack = this.getOriginItemStack();
      var stack = player.getStackInHand(hand);
      var count = this.getMaxIncrement(originStack, player.isSneaking() ? stack.getCount() : 1);

      if (ItemStack.canCombine(originStack, stack)) {
         if (count > 0) {
            this.increment(originStack, count);
            stack.decrement(count);
         }

         return ActionResult.SUCCESS;
      }
      else {
         return ActionResult.PASS;
      }
   }

   @Override
   public boolean damage(DamageSource source, float amount) {
      if (source.getAttacker() instanceof PlayerEntity) {
         var player = (PlayerEntity) source.getAttacker();
         
         if (!player.isSneaking()) {
            var originStack = this.getOriginItemStack();

            if (player.giveItemStack(originStack.copyWithCount(1))) {
               this.increment(originStack, -1);
            }
            
            return true;
         }
      }
   
      this.drop();

      return true;
   }
}
