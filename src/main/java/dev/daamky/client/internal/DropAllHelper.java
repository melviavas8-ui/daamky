package dev.daamky.client.internal;

import dev.daamky.client.event.ScreenEvent;
import dev.daamky.client.util.MinecraftAccess;
import java.util.ArrayList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public final class DropAllHelper implements MinecraftAccess {
   public static void drawButton(ScreenEvent.ScreenEventData event) {
      byte var3 = 80;
      byte var4 = 20;
      Screen var5 = event.getScreen();
      if (var5 instanceof InventoryScreen var6) {
         int var7 = (var5.width - var3) / 2;
         int var8 = (var5.height - var6.backgroundHeight) / 2 - var4 - 5;
         addButtons(event, var7, var8, var3, var4, new Pair<>("Drop all", DropAllHelper::lambda$drawButton$0));
      } else {
         HandledScreen var1;
         ScreenHandler var2;
         if (var5 instanceof HandledScreen && (var2 = (var1 = (HandledScreen)var5).getScreenHandler()) instanceof GenericContainerScreenHandler) {
            GenericContainerScreenHandler var12 = (GenericContainerScreenHandler)var2;
            String var13 = var1.getTitle().getString();
            if (var13.contains("Аукцион") && var13.contains("Поиск")) {
               return;
            }

            int var14 = var12.getRows();
            int var9 = 114 + var14 * 18;
            int var10 = var5.width / 2 + 90;
            int var11 = (var5.height - var9) / 2;
         }
      }
   }

   @SafeVarargs
   public static void addButtons(ScreenEvent.ScreenEventData event, int x, int y, int width, int height, Pair<String, Runnable>... pairs) {
      ArrayList var6 = new ArrayList();
      int var7 = y;

      for (Pair var11 : pairs) {
         ButtonWidget var12 = ButtonWidget.builder(Text.literal((String)var11.getLeft()), DropAllHelper::lambda$addButtons$1)
            .method_46434(x, var7, width, height)
            .method_46431();
         var6.add(var12);
         var7 += height + 5;
      }

      event.getList().addAll(var6);
   }

   public static void handleClass_1707(GenericContainerScreenHandler containerHandler) {
      for (int var1 = 0; var1 < containerHandler.getInventory().size(); var1++) {
         Slot var2 = containerHandler.getSlot(var1);
         if (var2.hasStack()) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var1, 0, SlotActionType.QUICK_MOVE, mc.player);
         }
      }
   }

   public static void handleSize(GenericContainerScreenHandler containerHandler) {
      for (int var1 = containerHandler.getInventory().size(); var1 < containerHandler.slots.size(); var1++) {
         Slot var2 = containerHandler.getSlot(var1);
         if (var2.hasStack()) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var1, 0, SlotActionType.QUICK_MOVE, mc.player);
         }
      }
   }

   public static void handleNext(HandledScreen<?> screen) {
      for (Slot var2 : screen.getScreenHandler().slots) {
         if (!var2.getStack().isEmpty()) {
            InventoryUtil.initInit(var2.id);
         }
      }
   }

   public DropAllHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void lambda$addButtons$1(Pair pair, ButtonWidget b) {
      ((Runnable)pair.getRight()).run();
   }

   public static void lambda$drawButton$0(InventoryScreen inv) {
      handleNext(inv);
   }
}
