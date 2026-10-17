package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.ScreenEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.gui.AutoBuyScreen;
import dev.daamky.client.internal.AuctionListing;
import dev.daamky.client.internal.BuyCheckHelper;
import dev.daamky.client.internal.DebugStartHelper;
import dev.daamky.client.internal.PriceCheckHelper;
import dev.daamky.client.internal.SellCmdHelper;
import dev.daamky.client.internal.SortResetHelper;
import dev.daamky.client.internal.TextShaper;
import dev.daamky.client.internal.UpdateAuctionHelper;
import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.Setting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@ModuleInfo(
   name = "AutoBuy",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Автоматическая скупка предметов на аукционе"
)
public class AutoBuy extends Module {
   public static final AutoBuy autoBuy = new AutoBuy();
   public final List<AutoBuy.DelayedTask> list = new ArrayList<>();
   public final List<AutoBuy.DelayedTask> list2 = new ArrayList<>();
   public long v2 = -1L;
   public int v3 = 0;
   public String v4 = "N/A";
   public boolean v5 = false;
   public long timeStamp = -1L;
   public int intVal = 0;
   public final List<AuctionListing> list3 = new ArrayList<>();
   public final Map<ItemActionCatalog, Integer> map = new HashMap<>();
   public final Set<ItemActionCatalog> set = new HashSet<>();
   public double v6 = 0.65;
   public boolean v = false;
   public int int2 = 500000;
   public int int3 = 800000;
   public int int4 = 100000;
   public String string = "[ник]";
   public boolean flag = false;
   public boolean flag2 = true;
   public boolean flag3 = false;
   public int int5 = 60;
   public int int6 = 1;
   public int int7 = 8;
   public final ButtonSetting openMenu = new ButtonSetting("Open Menu").action(AutoBuy::lambda$new$1);

   public void handleDefault(int ticks, Runnable action) {
      this.initAdd(ticks, action, "default");
   }

   public void initAdd(int ticks, Runnable action, String tag) {
      this.list.add(new AutoBuy.DelayedTask(action, ticks, tag));
   }

   public void cancelAll(String tag) {
      this.list.removeIf(AutoBuy::lambda$cancelAll$0);
   }

   public void runNext() {
      if (!this.list.isEmpty()) {
         this.list2.clear();

         for (AutoBuy.DelayedTask var2 : this.list) {
            if (--var2.ticks <= 0) {
               this.list2.add(var2);
            }
         }

         if (!this.list2.isEmpty()) {
            this.list.removeAll(this.list2);

            for (AutoBuy.DelayedTask var6 : this.list2) {
               try {
                  var6.action.run();
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }
         }
      }
   }

   public long getV2() {
      return this.v2;
   }

   public void setV(long v) {
      this.v2 = v;
   }

   public int getV3() {
      return this.v3;
   }

   public void setV(int v) {
      this.v3 = v;
   }

   public String getV4() {
      return this.v4;
   }

   public void setV(String v) {
      this.v4 = v;
   }

   public boolean check() {
      return this.v5;
   }

   public void setV(boolean v) {
      this.v5 = v;
   }

   public long getLong() {
      return this.timeStamp;
   }

   public void setLong(long v) {
      this.timeStamp = v;
   }

   public List<AuctionListing> getList3() {
      return this.list3;
   }

   public void handleSize(AuctionListing entry) {
      this.list3.add(0, entry);

      while (this.list3.size() > 200) {
         this.list3.remove(this.list3.size() - 1);
      }
   }

   public Map<ItemActionCatalog, Integer> getMap() {
      return this.map;
   }

   public Set<ItemActionCatalog> getSet() {
      return this.set;
   }

   public double getV6() {
      return this.v6;
   }

   public void setV(double v) {
      this.v6 = v;
   }

   public boolean isV() {
      return this.v;
   }

   public void setV2(boolean v) {
      this.v = v;
   }

   public int getInt2() {
      return this.int2;
   }

   public void setInt2(int v) {
      this.int2 = v;
   }

   public int getInt3() {
      return this.int3;
   }

   public void setInt3(int v) {
      this.int3 = v;
   }

   public String getString() {
      return this.string;
   }

   public void setString(String v) {
      this.string = v;
   }

   public boolean check2() {
      return this.flag;
   }

   public void setFlag(boolean v) {
      this.flag = v;
   }

   public int getInt5() {
      return this.int5;
   }

   public void setInt5(int v) {
      this.int5 = v;
   }

   public boolean check3() {
      return this.flag2;
   }

   public void setFlag2(boolean v) {
      this.flag2 = v;
   }

   public int getInt6() {
      return this.int6;
   }

   public void setInt6(int v) {
      this.int6 = v;
   }

   public int getInt4() {
      return this.int4;
   }

   public void setInt4(int v) {
      this.int4 = v;
   }

   public boolean check4() {
      return this.flag3;
   }

   public void setFlag3(boolean v) {
      this.flag3 = v;
   }

   public int getInt7() {
      return this.int7;
   }

   public void setInt7(int v) {
      this.int7 = v;
   }

   public AutoBuy() {
      this.addSettings(new Setting[]{this.openMenu});
   }

   public static AutoBuy getInstance() {
      return autoBuy;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.mouse != null) {
         var1.setScreen(AutoBuyScreen.getInstance());
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      BuyCheckHelper.setListen(false);
      SellCmdHelper.openAuction();
      SortResetHelper.setListen(false);
      PriceCheckHelper.setListen(false);
      this.list.clear();
      this.list2.clear();
      this.v4 = "N/A";
   }

   @Override
   public void onEvent() {
      Subscription var1 = ScreenEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var1, var2});
      this.initAdd(20, this::lambda$onEvent$4, "updateInfo");
   }

   public void runEnabled() {
      if (this.isEnabled()) {
         this.initAdd(20, this::runEnabled, "updateInfo");
      }
   }

   public void runEquals() {
      int var1 = parseParse();
      if (var1 != -1 && this.intVal != var1) {
         String var2 = MinecraftClient.getInstance().player.getNameForScoreboard();
         if (var1 >= this.int3 && !var2.equalsIgnoreCase(this.string) && this.v && var1 - this.int2 > 0 && !"[ник]".equals(this.string)) {
            MinecraftClient.getInstance().player.networkHandler.sendChatMessage("/pay " + this.string + " " + (var1 - this.int2));
         }

         if (var1 > this.int4 && this.v5) {
            this.v5 = false;
            ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
            if (var3 != null) {
               BuyCheckHelper.run();
               var3.networkHandler.sendChatMessage("/ah");
               this.v4 = "Покупка";
            }
         }

         this.intVal = var1;
      }
   }

   public static String getMoneyLine() {
      ClientPlayerEntity var0 = MinecraftClient.getInstance().player;
      if (var0 == null) {
         return null;
      } else {
         ClientWorld var1 = MinecraftClient.getInstance().world;
         if (var1 == null) {
            return null;
         } else {
            Scoreboard var2 = var1.getScoreboard();
            if (var2 == null) {
               return null;
            } else {
               ScoreboardObjective var3 = var2.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
               if (var3 == null) {
                  Team var6 = var2.getScoreHolderTeam(var0.getNameForScoreboard());
                  ScoreboardDisplaySlot var4;
                  Formatting var5;
                  if (var6 != null && (var5 = var6.getColor()) != null && var5.getColorIndex() >= 0 && (var4 = ScoreboardDisplaySlot.fromFormatting(var5)) != null) {
                     var3 = var2.getObjectiveForSlot(var4);
                  }

                  if (var3 == null) {
                     return null;
                  }
               }

               return var2.getScoreboardEntries(var3).stream().map(AutoBuy::lambda$getMoneyLine$5).filter(AutoBuy::lambda$getMoneyLine$6).findFirst().orElse(null);
            }
         }
      }
   }

   public static int parseParse() {
      String var0 = getMoneyLine();
      if (var0 == null) {
         return -1;
      } else {
         for (String var4 : new String[]{"Монеток: ", "Монеты: ", "Баланс: ", "Money: ", "Coins: ", "Golds: "}) {
            int var5 = var0.indexOf(var4);
            if (var5 >= 0) {
               String var6 = var0.substring(var5 + var4.length());
               StringBuilder var7 = new StringBuilder();

               for (char var11 : var6.toCharArray()) {
                  if (var11 >= '0' && var11 <= '9') {
                     var7.append(var11);
                  } else if (var7.length() > 0) {
                     break;
                  }
               }

               if (var7.length() > 0) {
                  try {
                     return Integer.parseInt(var7.toString());
                  } catch (Exception var13) {
                  }
               }
            }
         }

         String var14 = var0.replaceAll("[^\\d]", "");
         if (!var14.isEmpty()) {
            try {
               return Integer.parseInt(var14);
            } catch (Exception var12) {
            }
         }

         return -1;
      }
   }

   public void runEnabled2() {
      if (this.map.isEmpty()) {
         TextShaper.handleString("§a[AutoBuy] §fЦены не получены. Сначала выполните парсинг");
      } else {
         this.setEnabled(true);
         BuyCheckHelper.run();
         ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
         if (var1 != null) {
            var1.networkHandler.sendChatMessage("/ah");
         }

         this.v4 = "Покупка";
      }
   }

   public void runEnabled3() {
      this.setEnabled(true);
      SortResetHelper.run();
   }

   public static boolean lambda$getMoneyLine$6(String l) {
      return l.contains("Монеток:") || l.contains("Монеты:") || l.contains("Баланс:");
   }

   public static String lambda$getMoneyLine$5(Scoreboard sb, ScoreboardEntry e) {
      Object var2 = e.display();
      if (var2 == null) {
         var2 = Team.decorateName(sb.getScoreHolderTeam(e.owner()), Text.literal(e.owner()));
      }

      return var2.getString().replaceAll("(?i)§[0-9A-FK-OR]", "").trim();
   }

   public void lambda$onEvent$4() {
      if (this.isEnabled()) {
         this.initAdd(20, this::runEnabled, "updateInfo");
      }
   }

   public void lambda$onEvent$3(UpdateEvent event) {
      this.runNext();
      DebugStartHelper.getInstance().runContains();
      this.runEquals();
   }

   public void lambda$onEvent$2(ScreenEvent.ScreenEventData data) {
      if (this.isEnabled()) {
         if (data != null) {
            if (data.getScreen() instanceof HandledScreen var3) {
               BuyCheckHelper.onScreenOpen(var3);
               PriceCheckHelper.onScreenOpen(var3);
               UpdateAuctionHelper.setScreen(var3);
               SortResetHelper.onScreenOpen(var3);
            }
         }
      }
   }

   public static void lambda$new$1() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0 != null && var0.mouse != null) {
         var0.setScreen(AutoBuyScreen.getInstance());
      }
   }

   public static boolean lambda$cancelAll$0(String tag, AutoBuy.DelayedTask t) {
      return t.tag.equals(tag);
   }

   private static class DelayedTask {
      public final Runnable action;
      public int ticks;
      public final String tag;

      public DelayedTask(Runnable action, int ticks, String tag) {
         this.action = action;
         this.ticks = ticks;
         this.tag = tag;
      }
   }
}
