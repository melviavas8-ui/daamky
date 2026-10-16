package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class InventoryHelper implements MinecraftAccess {
   public boolean shouldBreakShield(PlayerEntity entity) {
      if (entity.isBlocking() && this.computeAcos(entity)) {
         boolean var2 = EntryHelper.check();
         int var3 = InventoryUtil.computeInt(false);
         int var4 = InventoryUtil.computeInt(true);
         if (var4 == -1 && var3 != -1) {
            if (!var2) {
               return this.computeFlag("Inventory", var4, var3, entity);
            }

            EntryHelper.handleLong(10L, this::lambda$shouldBreakShield$0);
            return true;
         }

         if (var4 != -1) {
            if (var2) {
               EntryHelper.handleLong(150L, this::lambda$shouldBreakShield$1);
               return true;
            }

            this.computeFlag("Hotbar", var4, var3, entity);
         }
      }

      return false;
   }

   public boolean computeFlag(String action, int hotBarSlot, int invSlot, PlayerEntity entity) {
      int var5 = mc.player.getInventory().selectedSlot;
      switch (action) {
         case "Hotbar":
            InventoryUtil.handleInt(hotBarSlot);
            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
            InventoryUtil.handleInt(var5);
            return true;
         case "Inventory":
            int var8 = InventoryUtil.getInt();
            InventoryUtil.initInit(invSlot, var8);
            InventoryUtil.handleInt(var8);
            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
            InventoryUtil.initInit(invSlot, var8);
            InventoryUtil.handleInt(var5);
            return true;
         default:
            return false;
      }
   }

   public boolean computeAcos(PlayerEntity target) {
      if (mc.player == null) {
         return false;
      } else {
         Vec3d var3 = target.getRotationVec(1.0F).normalize();
         double var4 = var3.dotProduct(mc.player.getEyePos().subtract(target.getEyePos()).normalize());
         double var6 = Math.toDegrees(Math.acos(var4));
         return var6 < 100.0;
      }
   }

   public void lambda$shouldBreakShield$1(int hotBarSlot, int invSlot, PlayerEntity entity) {
      this.computeFlag("Hotbar", hotBarSlot, invSlot, entity);
   }

   public void lambda$shouldBreakShield$0(int hotBarSlot, int invSlot, PlayerEntity entity) {
      this.computeFlag("Inventory", hotBarSlot, invSlot, entity);
   }
}
