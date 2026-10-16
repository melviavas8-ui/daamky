package dev.daamky.client.internal;

import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.pve.AutoBuy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
import net.minecraft.util.collection.DefaultedList;

public class BuyCheckHelper {
   public static boolean flag = false;
   public static final Random random = new Random();

   public static void run() {
      flag = true;
   }

   public static void onScreenOpen(HandledScreen<?> screen) {
      if (flag && screen instanceof GenericContainerScreen) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.player != null) {
            Text var2 = screen.getTitle();
            if (var2.getString().startsWith("Аукцион (")) {
               AutoBuy var3 = AutoBuy.getInstance();
               var3.initAdd(NextIntHelper.getInt(), BuyCheckHelper::lambda$onScreenOpen$0, "buyCheck");
               flag = false;
            }
         }
      }
   }

   public static void checkInventory(ScreenHandler handler) {
      AutoBuy var1 = AutoBuy.getInstance();
      var1.setV(false);
      var1.setV("Покупка");
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.player != null) {
         long var3 = System.currentTimeMillis();
         if (var1.check4() && var1.getLong() + (long)var1.getInt7() * 60L * 1000L < var3) {
            var1.setLong(var3);
            AutoBuyState.setContinueAutoBuying(true);
            SortResetHelper.run();
         } else if (AutoBuy.parseParse() <= var1.getInt4()) {
            var2.player.closeHandledScreen();
            SellCmdHelper.setNeedOpen(true);
            SellCmdHelper.runEnabled();
         } else if (computeNext(handler)) {
            flag = true;
         } else {
            DefaultedList var5 = handler.slots;
            if (random.nextDouble() < 0.2) {
               ArrayList var6 = new ArrayList(var5);
               Collections.shuffle(var6, random);

               for (Slot var8 : var6) {
                  if (checkAndBuySlot(var8, handler, var2, var1)) {
                     return;
                  }
               }
            } else {
               for (Slot var11 : (List)var5) {
                  if (checkAndBuySlot(var11, handler, var2, var1)) {
                     return;
                  }
               }
            }

            if (NextIntHelper.checkNext3()) {
               var1.initAdd(NextIntHelper.getInt7(), BuyCheckHelper::lambda$checkInventory$1, "humanPause");
            }

            int var10 = NextIntHelper.getInt2();
            var1.initAdd(var10, BuyCheckHelper::lambda$checkInventory$2, "updateAuction");
         }
      }
   }

   public static boolean checkAndBuySlot(Slot slot, ScreenHandler handler, MinecraftClient client, AutoBuy f) {
      ItemStack var4 = slot.getStack();
      if (var4.isEmpty()) {
         return false;
      } else {
         ItemActionCatalog var5 = ItemInfo.computeNext(var4);
         if (var5 != null && !f.getSet().contains(var5)) {
            List var6 = var4.getTooltip(TooltipContext.DEFAULT, client.player, TooltipType.BASIC);
            String var7 = "?";
            boolean var8 = false;

            for (Text var10 : var6) {
               String var11 = var10.getString();
               if (var11.contains(" ▍ Продавец: ")) {
                  var7 = var11.split(" ▍ Продавец: ")[1];
               } else if (var11.contains("Цена за 1 ед.: ")) {
                  if (NameFilter.initInit().contains(var7.toLowerCase())) {
                     if (NextIntHelper.checkNext2()) {
                        client.player.sendMessage(Text.literal("§7[AutoBuy] §fПропускаю стаффера §c" + var7), false);
                     }
                  } else {
                     try {
                        String var14 = var11.replaceAll("[^0-9]", "");
                        int var15 = Integer.parseInt(var14);
                        int var16 = f.getMap().getOrDefault(var5, -1);
                        if (var16 > 0) {
                           double var17 = f.getV6() * (0.95 + random.nextDouble() * 0.1);
                           int var12;
                           int var13;
                           if ((double)var15 <= (double)var16 * var17 && (var13 = var15 * var4.getCount()) <= (var12 = AutoBuy.parseParse())) {
                              client.player
                                 .sendMessage(
                                    Text.literal("§a[AutoBuy] §fНайден: §e" + var5.getName() + " §fза §6" + var15 + " §7(рынок: " + var16 + ")"),
                                    false
                                 );
                              int var19 = client.player.currentScreenHandler.syncId;
                              DebugStartHelper.getInstance().initTime(var19, slot.id, 1, SlotActionType.PICKUP_ALL);
                              client.interactionManager.clickSlot(var19, slot.id, 1, SlotActionType.PICKUP_ALL, client.player);
                              UpdateAuctionHelper.setListen(true);
                              f.handleSize(new AuctionListing(var5, var15, var13, var16, var12));
                              int var20 = NextIntHelper.getInt3();
                              f.initAdd(var20, BuyCheckHelper::lambda$checkAndBuySlot$3, "updateAuction");
                              var8 = true;
                              break;
                           }
                        }
                     } catch (NumberFormatException var21) {
                     }
                  }
               }
            }

            return var8;
         } else {
            return false;
         }
      }
   }

   public static boolean computeNext(ScreenHandler handler) {
      if (NextIntHelper.checkNext4()) {
         return false;
      } else {
         MinecraftClient var1 = MinecraftClient.getInstance();
         Slot var2 = (Slot)handler.slots.get(52);
         ItemStack var3 = var2.getStack();
         List var4 = var3.getTooltip(TooltipContext.DEFAULT, var1.player, TooltipType.BASIC);
         boolean var5 = false;

         for (Text var7 : var4) {
            String var8 = var7.getString();
            if (var8.contains("✔") && var8.contains("дешевые")) {
               var5 = true;
               break;
            }
         }

         if (var5) {
            return false;
         } else {
            SlotActionType var9 = random.nextDouble() < 0.3 ? SlotActionType.PICKUP_ALL : SlotActionType.PICKUP;
            DebugStartHelper.getInstance().initTime(var1.player.currentScreenHandler.syncId, 52, 1, var9);
            var1.interactionManager.clickSlot(var1.player.currentScreenHandler.syncId, 52, 1, var9, var1.player);
            return true;
         }
      }
   }

   public static void handleClass_310(MinecraftClient client) {
      int var1 = NextIntHelper.computeNext(50);
      int var2 = client.player.currentScreenHandler.syncId;
      DebugStartHelper.getInstance().initTime(var2, var1, 1, SlotActionType.PICKUP_ALL);
      client.interactionManager.clickSlot(var2, var1, 1, SlotActionType.PICKUP_ALL, client.player);
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setListen(boolean listen) {
      flag = listen;
   }

   public static void lambda$checkAndBuySlot$3(MinecraftClient client, AutoBuy f) {
      try {
         handleClass_310(client);
         flag = true;
      } catch (Exception var3) {
         f.setV("N/A");
      }
   }

   public static void lambda$checkInventory$2(MinecraftClient client, AutoBuy f) {
      try {
         handleClass_310(client);
         flag = true;
      } catch (Exception var3) {
         f.setV("N/A");
      }
   }

   public static void lambda$checkInventory$1() {
   }

   public static void lambda$onScreenOpen$0(HandledScreen screen) {
      checkInventory(screen.getScreenHandler());
   }
}
