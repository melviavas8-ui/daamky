package dev.daamky.client.internal;

import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.pve.AutoBuy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class AutoBuyState {
   public static int intVal = -1;
   public static boolean flag = false;

   public static void runCurrent() {
      intVal = -1;
      runListen();
      AutoBuy.getInstance().setV("Получение цен");
      AutoBuy.getInstance().setLong(System.currentTimeMillis());
   }

   public static void runListen() {
      if (++intVal < ItemActionCatalog.values().length) {
         MinecraftClient var0 = MinecraftClient.getInstance();
         ClientPlayerEntity var1 = var0.player;
         if (var1 == null) {
            return;
         }

         ItemActionCatalog var2 = ItemActionCatalog.values()[intVal];
         var1.closeScreen();
         var1.networkHandler.sendChatMessage("/ah search " + var2.getSearchString());
         PriceCheckHelper.setListen(true);
         PriceCheckHelper.setCurrentItem(var2);
      } else {
         intVal = -1;
         PriceCheckHelper.setListen(false);
         MinecraftClient var3 = MinecraftClient.getInstance();
         ClientPlayerEntity var4 = var3.player;
         if (var4 == null) {
            return;
         }

         if (flag) {
            BuyCheckHelper.run();
            var4.networkHandler.sendChatMessage("/ah");
            AutoBuy.getInstance().setV("Покупка");
         } else {
            var4.closeScreen();
            AutoBuy.getInstance().setV("N/A");
         }
      }
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setContinueAutoBuying(boolean continueAutoBuying) {
      flag = continueAutoBuying;
   }
}
