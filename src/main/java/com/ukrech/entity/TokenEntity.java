package com.ukrech.entity;

import com.ukrech.Tabletop;
import com.ukrech.item.TokenItem;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
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

   //

   @Override
   public void setOriginItemStack(ItemStack stack) {
      super.setOriginItemStack(stack);
      this.updateName(stack);
   }

   @Override
   protected boolean tryReturnToHand(ItemStack originStack, PlayerEntity player, Hand hand) {
      return player.giveItemStack(originStack.copyWithCount(1));
   }

   @Override
   public ActionResult interact(PlayerEntity player, Hand hand) {
      var originStack = this.getOriginItemStack();

      if (player.isSneaking()) {
         if (this.tryReturnToHand(originStack, player, hand)) {
            this.increment(originStack, -1);

            return ActionResult.SUCCESS;
         }
      }
      else {
         var stack = player.getStackInHand(hand);

         if (this.isStackEqualToOriginStack(originStack, stack)) {
            this.increment(originStack, 1);
            stack.decrement(1);

            return ActionResult.SUCCESS;
         }
      }

      return ActionResult.PASS;
   }
}
