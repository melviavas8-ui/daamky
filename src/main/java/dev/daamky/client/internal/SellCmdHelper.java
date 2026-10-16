package dev.daamky.client.internal;

import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.pve.AutoBuy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class SellCmdHelper {
   public static boolean flag = false;
   public static int intVal = 0;
   public static boolean flag2 = false;

   public static void runEnabled() {
      if (!flag) {
         AutoBuy.getInstance().setEnabled(true);
         flag = true;
         intVal = 0;
         runContains();
         AutoBuy.getInstance().setV("Продажа");
      }
   }

   public static void openAuction() {
      flag = false;
      AutoBuy.getInstance().setV("N/A");
      openAuctionIfNeeded();
   }

   public static void runContains() {
      if (flag) {
         MinecraftClient var0 = MinecraftClient.getInstance();
         if (var0.player == null) {
            openAuction();
         } else {
            AutoBuy var1 = AutoBuy.getInstance();
            PlayerInventory var2 = var0.player.getInventory();
            var2.updateItems();

            while (intVal < var2.size()) {
               ItemStack var3 = var2.getStack(intVal);
               ItemActionCatalog var4 = ItemInfo.computeNext(var3);
               var2.updateItems();
               if (!var3.isEmpty() && var4 != null && var1.getMap().containsKey(var4)) {
                  int var5 = intVal++;
                  int var6 = var3.getCount();
                  sellItem(var5, var4, var6);
                  return;
               }

               intVal++;
            }

            openAuction();
         }
      }
   }

   public static void sellItem(int slot, ItemActionCatalog itemType, int itemCount) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3.player != null && var3.interactionManager != null) {
         AutoBuy var4 = AutoBuy.getInstance();
         PlayerInventory var5 = var3.player.getInventory();
         var5.updateItems();
         int var6 = computeInt(var5);
         if (var6 == -1) {
            var6 = 0;
         }

         var5.updateItems();
         DebugStartHelper.getInstance().initTime(var3.player.currentScreenHandler.syncId, slot < 9 ? slot + 36 : slot, var6, SlotActionType.SWAP);
         var3.interactionManager.clickSlot(var3.player.currentScreenHandler.syncId, slot < 9 ? slot + 36 : slot, var6, SlotActionType.SWAP, var3.player);
         var3.player.getInventory().selectedSlot = var6;
         var5.updateItems();
         if (var4.getMap().containsKey(itemType)) {
            int var7 = var4.getMap().get(itemType) * itemCount;
            String var8 = "/ah sell " + var7;
            var4.initAdd(2, SellCmdHelper::lambda$sellItem$1, "sellCmd");
         }
      } else {
         openAuction();
      }
   }

   public static int computeInt(PlayerInventory inv) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (inv.getStack(var1).isEmpty()) {
            return var1;
         }
      }

      return -1;
   }

   public static void openAuctionIfNeeded() {
      AutoBuy var0 = AutoBuy.getInstance();
      var0.initAdd(20, SellCmdHelper::lambda$openAuctionIfNeeded$2, "openAuction");
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setRunning(boolean running) {
      flag = running;
   }

   public static boolean getFlag2() {
      return flag2;
   }

   public static void setNeedOpen(boolean needOpen) {
      flag2 = needOpen;
   }

   public static void lambda$openAuctionIfNeeded$2(AutoBuy f) {
      if (flag2) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         BuyCheckHelper.run();
         var1.player.networkHandler.sendChatMessage("/ah");
         f.setV("Покупка");
         flag2 = false;
         f.setV(true);
      }
   }

   public static void lambda$sellItem$1(PlayerInventory inv, MinecraftClient client, String cmd, AutoBuy f) {
      inv.updateItems();
      client.player.networkHandler.sendChatMessage(cmd);
      inv.updateItems();
      f.initAdd(2, SellCmdHelper::lambda$sellItem$0, "sellNext");
   }

   public static void lambda$sellItem$0(PlayerInventory inv) {
      inv.updateItems();
      runContains();
   }
}
