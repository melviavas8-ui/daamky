package dev.daamky.client.internal;

import dev.daamky.client.module.pve.AutoBuy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.SlotActionType;

public class UpdateAuctionHelper {
   public static boolean flag = false;

   public static void setScreen(HandledScreen<?> screen) {
      if (flag && screen instanceof GenericContainerScreen) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.player != null) {
            if (screen.getTitle().getString().startsWith("Покупка предмета")) {
               AutoBuy var2 = AutoBuy.getInstance();
               var2.cancelAll("updateAuction");
               DebugStartHelper.getInstance().initTime(var1.player.currentScreenHandler.syncId, 10, 1, SlotActionType.PICKUP_ALL);
               var1.interactionManager.clickSlot(var1.player.currentScreenHandler.syncId, 10, 1, SlotActionType.PICKUP_ALL, var1.player);
               BuyCheckHelper.run();
               var2.setV(var2.getV3() + 1);
               flag = false;
            }
         }
      }
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setListen(boolean listen) {
      flag = listen;
   }
}
