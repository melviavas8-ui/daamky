package dev.daamky.client.internal;

import dev.daamky.client.module.pve.AutoBuy;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class SortResetHelper {
   public static boolean flag = false;

   public static void run() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0.player != null) {
         var0.player.networkHandler.sendChatMessage("/ah");
         flag = true;
      }
   }

   public static void onScreenOpen(HandledScreen<?> screen) {
      if (flag && screen instanceof GenericContainerScreen) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.player != null) {
            if (screen.getTitle().getString().startsWith("Аукцион (")) {
               AutoBuy var2 = AutoBuy.getInstance();
               var2.initAdd(3, SortResetHelper::lambda$onScreenOpen$0, "sortReset");
               flag = false;
            }
         }
      }
   }

   public static void setHandler(ScreenHandler handler) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.player != null) {
         if (computeContains(handler)) {
            flag = true;
         } else {
            AutoBuyState.runCurrent();
         }
      }
   }

   public static boolean computeContains(ScreenHandler handler) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      Slot var2 = (Slot)handler.slots.get(52);
      ItemStack var3 = var2.getStack();
      List var4 = var3.getTooltip(TooltipContext.DEFAULT, var1.player, TooltipType.BASIC);

      for (Text var6 : var4) {
         if (var6.getString().contains("✔") && var6.getString().contains("дешевые")) {
            return false;
         }
      }

      DebugStartHelper.getInstance().initTime(var1.player.currentScreenHandler.syncId, 52, 1, SlotActionType.PICKUP);
      var1.interactionManager.clickSlot(var1.player.currentScreenHandler.syncId, 52, 1, SlotActionType.PICKUP, var1.player);
      return true;
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setListen(boolean listen) {
      flag = listen;
   }

   public static void lambda$onScreenOpen$0(HandledScreen screen) {
      setHandler(screen.getScreenHandler());
   }
}
