package dev.daamky.client.internal;

import dev.daamky.client.item.ItemActionCatalog;
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

public class PriceCheckHelper {
   public static ItemActionCatalog mseuay;
   public static boolean flag = false;
   public static int intVal = 0;

   public static void onScreenOpen(HandledScreen<?> screen) {
      if (flag && screen instanceof GenericContainerScreen) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.player != null) {
            if (screen.getTitle().getString().startsWith("Аукцион (")) {
               AutoBuy var2 = AutoBuy.getInstance();
               var2.initAdd(1, PriceCheckHelper::lambda$onScreenOpen$0, "priceCheck");
            }

            flag = false;
         }
      }
   }

   public static void checkInventory(ScreenHandler handler) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.player != null) {
         if (setHandler(handler)) {
            flag = true;
         } else {
            AutoBuy var2 = AutoBuy.getInstance();
            if (intVal <= 1) {
               var2.initAdd(10, PriceCheckHelper::lambda$checkInventory$1, "priceLook");
            } else {
               parseNext(handler);
            }
         }
      }
   }

   public static void parseNext(ScreenHandler handler) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      AutoBuy var2 = AutoBuy.getInstance();

      for (Slot var4 : handler.slots) {
         ItemStack var6 = var4.getStack();
         if (!var6.isEmpty() && ItemInfo.computeNext(var6) != null) {
            List var7 = var6.getTooltip(TooltipContext.DEFAULT, var1.player, TooltipType.BASIC);

            for (Text var9 : var7) {
               String var10 = var9.getString();
               if (var10.contains("Цена за 1 ед.: ")) {
                  try {
                     String var11 = var10.replaceAll("[^0-9]", "");
                     int var12 = Integer.parseInt(var11);
                     var1.player.sendMessage(Text.literal("§a[AutoBuy] §fЦена '" + mseuay.getName() + "': " + var12), false);
                     var2.getMap().put(mseuay, var12);
                     AutoBuyState.runListen();
                     return;
                  } catch (NumberFormatException var13) {
                  }
               }
            }
         }
      }

      var2.getMap().put(mseuay, -1);
      AutoBuyState.runListen();
   }

   public static boolean setHandler(ScreenHandler handler) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      Slot var2 = (Slot)handler.slots.get(52);
      ItemStack var3 = var2.getStack();
      List var4 = var3.getTooltip(TooltipContext.DEFAULT, var1.player, TooltipType.BASIC);

      for (Text var6 : var4) {
         if (var6.getString().contains("✔") && var6.getString().contains("дешевые за ед")) {
            return false;
         }
      }

      DebugStartHelper.getInstance().initTime(var1.player.currentScreenHandler.syncId, 52, 1, SlotActionType.PICKUP_ALL);
      var1.interactionManager.clickSlot(var1.player.currentScreenHandler.syncId, 52, 1, SlotActionType.PICKUP_ALL, var1.player);
      intVal++;
      return true;
   }

   public static ItemActionCatalog getMseuay() {
      return mseuay;
   }

   public static void setCurrentItem(ItemActionCatalog currentItem) {
      mseuay = currentItem;
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setListen(boolean listen) {
      flag = listen;
   }

   public static void lambda$checkInventory$1(ScreenHandler handler) {
      parseNext(handler);
   }

   public static void lambda$onScreenOpen$0(HandledScreen screen) {
      checkInventory(screen.getScreenHandler());
   }
}
