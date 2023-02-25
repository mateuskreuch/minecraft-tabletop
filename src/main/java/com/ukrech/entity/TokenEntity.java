package com.ukrech.entity;

import com.ukrech.Tabletop;
import com.ukrech.item.TokenItem;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TokenEntity extends PlaceableItemEntity {
   public static final Identifier ID = new Identifier(Tabletop.MOD_ID, "token_entity");
   public static final EntityType<TokenEntity> ENTITY = FabricEntityTypeBuilder.create(SpawnGroup.MISC, TokenEntity::new)
                                                                               .dimensions(EntityDimensions.fixed(0.1875f, 0.25f))
                                                                               .build();

   //

   public TokenEntity(EntityType<? extends LivingEntity> entityType, World world) {
      super(entityType, world);
   }

   //

   @Override
   protected Item getOriginItem() {
      return TokenItem.ITEM;
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

      if (this.isStackEqualToOriginStack(originStack, stack) && count > 0) {
         this.increment(originStack, count);
         stack.decrement(count);

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
         }
      }
   
      this.drop();

      return true;
   }
}
