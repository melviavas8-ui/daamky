package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.pve.AutoBuy;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class DebugStartHelper {
   public static final DebugStartHelper INSTANCE = new DebugStartHelper();
   public boolean recording = false;
   public long startTime = 0L;
   public int tickCounter = 0;
   public final List<DebugStartHelper.AuctionSnapshot> snapshots = new CopyOnWriteArrayList<>();
   public final List<DebugStartHelper.ClickEvent> clickLog = new CopyOnWriteArrayList<>();
   public final List<DebugStartHelper.ActionEvent> actionLog = new CopyOnWriteArrayList<>();
   public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public static final Path DEBUG_DIR = Paths.get("config/autobuy/debug");

   public static DebugStartHelper getInstance() {
      return INSTANCE;
   }

   public boolean isRecording() {
      return this.recording;
   }

   public void runClear() {
      this.recording = true;
      this.startTime = System.currentTimeMillis();
      this.tickCounter = 0;
      this.snapshots.clear();
      this.clickLog.clear();
      this.actionLog.clear();
      this.initTime("DEBUG_START", "Запись датасета начата");
      AutoBuy.getInstance().setV("\ud83d\udcdd DEBUG");
      TextShaper.handleString("§a[Debug] §fЗапись датасета начата");
   }

   public void runSize() {
      this.recording = false;
      this.initSize();
      AutoBuy.getInstance().setV("N/A");
      TextShaper.handleString(
         "§a[Debug] §fЗапись остановлена. Снимков: §e"
            + this.snapshots.size()
            + " §f| Кликов: §e"
            + this.clickLog.size()
            + " §f| Действий: §e"
            + this.actionLog.size()
      );
   }

   public void runContains() {
      if (this.recording) {
         this.tickCounter++;
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var3.player != null && var3.currentScreen != null) {
            Screen var4 = var3.currentScreen;
            GenericContainerScreen var1;
            String var2;
            if (var4 instanceof GenericContainerScreen
               && ((var2 = (var1 = (GenericContainerScreen)var4).getTitle().getString()).contains("Аукцион") || var2.contains("Поиск") || var2.contains("Покупка"))
               && this.tickCounter % 20 == 0) {
               this.initContains(var1, var2);
            }
         }
      }
   }

   public void initContains(GenericContainerScreen screen, String title) {
      DebugStartHelper.AuctionSnapshot var3 = new DebugStartHelper.AuctionSnapshot();
      var3.tick = this.tickCounter;
      var3.time = System.currentTimeMillis() - this.startTime;
      var3.screenTitle = title;
      var3.money = AutoBuy.parseParse();
      var3.status = AutoBuy.getInstance().getV4();
      GenericContainerScreenHandler var4 = (GenericContainerScreenHandler)screen.getScreenHandler();
      if (var4 != null) {
         MinecraftClient var5 = MinecraftClient.getInstance();
         if (var4.slots.size() > 52) {
            ItemStack var6 = ((Slot)var4.slots.get(52)).getStack();
            List var7 = var6.getTooltip(TooltipContext.DEFAULT, var5.player, TooltipType.BASIC);

            for (Text var9 : var7) {
               String var10 = var9.getString();
               if (var10.contains("✔")) {
                  var3.sortMode = var10.trim();
                  break;
               }
            }
         }

         for (int var17 = 0; var17 < Math.min(var4.slots.size(), 54); var17++) {
            ItemStack var18 = ((Slot)var4.slots.get(var17)).getStack();
            if (!var18.isEmpty()) {
               DebugStartHelper.SlotData var19 = new DebugStartHelper.SlotData();
               var19.slotIndex = var17;
               var19.itemName = var18.getName().getString();
               var19.count = var18.getCount();
               List var20 = var18.getTooltip(TooltipContext.DEFAULT, var5.player, TooltipType.BASIC);

               for (Text var11 : var20) {
                  String var13 = var11.getString();
                  String var12;
                  if (var13.contains(" Цена за 1 ед.:") && !(var12 = var13.replaceAll("[^0-9]", "")).isEmpty()) {
                     try {
                        var19.price = Integer.parseInt(var12);
                     } catch (Exception var15) {
                     }
                  }

                  if (var13.contains("Продавец:")) {
                     try {
                        var19.seller = var13.split("Продавец:")[1].trim();
                     } catch (Exception var16) {
                     }
                  }
               }

               ItemActionCatalog var22 = ItemInfo.computeNext(var18);
               var19.detectedType = var22 != null ? var22.name() : "UNKNOWN";
               String var23 = var19.detectedType;
               if (var22 != null && AutoBuy.getInstance().getMap().containsKey(var22)) {
                  var19.marketPrice = AutoBuy.getInstance().getMap().get(var22);
               }

               var3.slots.add(var19);
            }
         }

         this.snapshots.add(var3);
      }
   }

   public void initTime(int syncId, int slotId, int button, SlotActionType action) {
      if (this.recording) {
         DebugStartHelper.ClickEvent var5 = new DebugStartHelper.ClickEvent();
         var5.tick = this.tickCounter;
         var5.time = System.currentTimeMillis() - this.startTime;
         var5.syncId = syncId;
         var5.slotId = slotId;
         var5.button = button;
         var5.action = action.toString();
         this.clickLog.add(var5);
      }
   }

   public void initTime(String type, String description) {
      if (this.recording) {
         DebugStartHelper.ActionEvent var3 = new DebugStartHelper.ActionEvent();
         var3.tick = this.tickCounter;
         var3.time = System.currentTimeMillis() - this.startTime;
         var3.type = type;
         var3.description = description;
         this.actionLog.add(var3);
      }
   }

   public void initSize() {
      try {
         Files.createDirectories(DEBUG_DIR);
         String var1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
         Path var2 = DEBUG_DIR.resolve("dataset_" + var1 + ".json");
         DebugStartHelper.DebugDataset var3 = new DebugStartHelper.DebugDataset();
         var3.datasetMeta = new DebugStartHelper.DatasetMeta();
         var3.datasetMeta.longVal = System.currentTimeMillis() - this.startTime;
         var3.datasetMeta.intVal = this.tickCounter;
         var3.datasetMeta.int2 = this.snapshots.size();
         var3.datasetMeta.int3 = this.clickLog.size();
         var3.datasetMeta.mesmo = this.actionLog.size();
         var3.datasetMeta.doubleVal = AutoBuy.getInstance().getV6();
         var3.datasetMeta.int4 = AutoBuy.getInstance().getMap().size();
         var3.list = new ArrayList<>(this.snapshots);
         var3.mimluph = new ArrayList<>(this.clickLog);
         var3.list2 = new ArrayList<>(this.actionLog);

         try (FileWriter var4 = new FileWriter(var2.toFile())) {
            GSON.toJson(var3, var4);
         }

         TextShaper.handleString("§a[Debug] §fДатасет: §e" + var2.toAbsolutePath());
         TextShaper.handleString("§7  Снимков: " + this.snapshots.size() + " | Кликов: " + this.clickLog.size() + " | Действий: " + this.actionLog.size());
      } catch (IOException var9) {
         TextShaper.handleString("§c[Debug] §fОшибка: " + var9.getMessage());
      }
   }

   static class ActionEvent {
      public int tick;
      public long time;
      public String type;
      public String description;

      public ActionEvent() {
      }
   }

   static class AuctionSnapshot {
      public int tick;
      public long time;
      public String screenTitle;
      public int money = -1;
      public String status;
      public String sortMode = "";
      public List<DebugStartHelper.SlotData> slots = new ArrayList<>();

      public AuctionSnapshot() {
      }
   }

   static class ClickEvent {
      public int tick;
      public long time;
      public int syncId;
      public int slotId;
      public int button;
      public String action;

      public ClickEvent() {
      }
   }

   static class DatasetMeta {
      public long longVal;
      public int intVal;
      public int int2;
      public int int3;
      public int mesmo;
      public double doubleVal;
      public int int4;

      public DatasetMeta() {
      }
   }

   static class DebugDataset {
      public DebugStartHelper.DatasetMeta datasetMeta;
      public List<DebugStartHelper.AuctionSnapshot> list;
      public List<DebugStartHelper.ClickEvent> mimluph;
      public List<DebugStartHelper.ActionEvent> list2;

      public DebugDataset() {
      }
   }

   static class SlotData {
      public int slotIndex;
      public String itemName;
      public int count;
      public int price = -1;
      public int marketPrice = -1;
      public String seller = "";
      public String detectedType = "";

      public SlotData() {
      }
   }
}
