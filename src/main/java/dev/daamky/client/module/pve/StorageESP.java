package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PostMotionEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.BoxRenderer;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

@ModuleInfo(
   name = "StorageESP",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Подсвечивает контейнеры: сундуки, шалкеры, бочки, печи, воронки и т.д."
)
public class StorageESP extends Module {
   public static StorageESP storageESP;
   public static final int intVal = 4000;
   public final BooleanSetting chests = new BooleanSetting("Chests").value(true);
   public final BooleanSetting enderChests = new BooleanSetting("Ender Chests").value(true);
   public final BooleanSetting shulkers = new BooleanSetting("Shulkers").value(true);
   public final BooleanSetting barrels = new BooleanSetting("Barrels").value(true);
   public final BooleanSetting furnaces = new BooleanSetting("Furnaces").value(false);
   public final BooleanSetting hoppersDispensers = new BooleanSetting("Hoppers/Dispensers").value(false);
   public final MultiBooleanSetting multiBooleanSetting = new MultiBooleanSetting("Containers")
      .value(this.chests, this.enderChests, this.shulkers, this.barrels, this.furnaces, this.hoppersDispensers);
   public final FloatSetting range = new FloatSetting("Range").value(48.0F).range(8.0F, 128.0F).step(1.0F);
   public final FloatSetting scanDelayMs = new FloatSetting("Scan Delay (ms)").value(1000.0F).range(300.0F, 4000.0F).step(50.0F);
   public final ModeSetting both = new ModeSetting("Render Mode").value("Outline").values("Fill", "Outline", "Both");
   public final ColorSetting outlineColor = new ColorSetting("Outline Color")
      .computeColorSetting(new Color(255, 200, 40, 220))
      .computeVisible(this::lambda$new$0);
   public final ColorSetting fillColor = new ColorSetting("Fill Color").computeColorSetting(new Color(255, 200, 40, 45)).computeVisible(this::lambda$new$1);
   public final List<BlockPos> list = new CopyOnWriteArrayList<>();
   public long longVal = 0L;
   public Thread thread;

   public static StorageESP getInstance() {
      if (storageESP == null) {
         storageESP = new StorageESP();
      }

      return storageESP;
   }

   public StorageESP() {
      this.addSettings(new Setting[]{this.multiBooleanSetting, this.range, this.scanDelayMs, this.both, this.outlineColor, this.fillColor});
      storageESP = this;
      Runnable var1 = this.list::clear;
      this.chests.onAction(var1);
      this.enderChests.onAction(var1);
      this.shulkers.onAction(var1);
      this.barrels.onAction(var1);
      this.furnaces.onAction(var1);
      this.hoppersDispensers.onAction(var1);
      this.range.onAction(var1);
      this.scanDelayMs.onAction(var1);
   }

   @Override
   public void onEnable() {
      this.list.clear();
      this.longVal = 0L;
   }

   @Override
   public void onDisable() {
      this.list.clear();
      super.onDisable();
   }

   @Override
   public void onEvent() {
      this.addEventListeners(
         new Subscription[]{
            PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2)),
            Render3DEvent.getInstance().subscribe(new EventListener<>(this::handleBoth))
         }
      );
   }

   public void onScanTick() {
      if (mc.player != null && mc.world != null) {
         long var1 = System.currentTimeMillis();
         if (var1 - this.longVal >= this.scanDelayMs.getValue().longValue()) {
            this.longVal = var1;
            if (this.thread == null || !this.thread.isAlive()) {
               boolean var3 = this.chests.getValue();
               boolean var4 = this.enderChests.getValue();
               boolean var5 = this.shulkers.getValue();
               boolean var6 = this.barrels.getValue();
               boolean var7 = this.furnaces.getValue();
               boolean var8 = this.hoppersDispensers.getValue();
               if (!var3 && !var4 && !var5 && !var6 && !var7 && !var8) {
                  this.list.clear();
               } else {
                  int var9 = this.range.getValue().intValue();
                  BlockPos var10 = mc.player.getBlockPos();
                  int var11 = var10.getX();
                  int var12 = var10.getY();
                  int var13 = var10.getZ();
                  ClientWorld var14 = mc.world;
                  this.thread = new Thread(() -> this.lambda$onScanTick$4(var14, var9, var11, var13, var3, var4, var5, var6, var7, var8, var12), "daamky-storageesp-scan");
                  this.thread.setDaemon(true);
                  this.thread.start();
               }
            }
         }
      }
   }

   public static boolean computeFlag(BlockState st, boolean tChest, boolean tEnder, boolean tShulker, boolean tBarrel, boolean tFurnace, boolean tHopper) {
      Block var7 = st.getBlock();
      if (tChest && var7 instanceof ChestBlock) {
         return true;
      } else if (tEnder && var7 instanceof EnderChestBlock) {
         return true;
      } else if (tShulker && var7 instanceof ShulkerBoxBlock) {
         return true;
      } else if (tBarrel && var7 instanceof BarrelBlock) {
         return true;
      } else {
         return tFurnace && var7 instanceof AbstractFurnaceBlock ? true : tHopper && (var7 instanceof HopperBlock || var7 instanceof DispenserBlock);
      }
   }

   public void handleBoth(Render3DEvent.Render3DEventData event) {
      if (this.isEnabled() && mc.player != null && mc.world != null) {
         boolean var2 = this.both.is("Outline") || this.both.is("Both");
         boolean var3 = this.both.is("Fill") || this.both.is("Both");
         Color var4 = this.outlineColor.getValue();
         Color var5 = this.fillColor.getValue();

         for (BlockPos var7 : this.list) {
            float var8 = (float)var7.getX();
            float var9 = (float)var7.getY();
            float var10 = (float)var7.getZ();
            float var11 = var8 + 1.0F;
            float var12 = var9 + 1.0F;
            float var13 = var10 + 1.0F;
            if (var3) {
               Render.boxRenderer.handleFloat(var8, var9, var10, var11, var12, var13, 0.0F, var5, BoxRenderer.Render.FILL, 0.0F);
            }

            if (var2) {
               Render.boxRenderer.handleFloat(var8, var9, var10, var11, var12, var13, 1.5F, var4, BoxRenderer.Render.OUTLINE, 0.0F);
            }
         }
      }
   }

   public void lambda$onScanTick$4(
      ClientWorld world, int r, int px, int pz, boolean tChest, boolean tEnder, boolean tShulker, boolean tBarrel, boolean tFurnace, boolean tHopper, int py
   ) {
      if (world != null) {
         ArrayList var12 = new ArrayList();

         try {
            long var13 = (long)r * (long)r;
            int var15 = px - r >> 4;
            int var16 = px + r >> 4;
            int var17 = pz - r >> 4;
            int var18 = pz + r >> 4;
            int var19 = world.getBottomSectionCoord();

            label92:
            for (int var20 = var15; var20 <= var16; var20++) {
               for (int var21 = var17; var21 <= var18; var21++) {
                  WorldChunk var22 = world.getChunk(var20, var21);
                  if (var22 != null) {
                     ChunkSection[] var23 = var22.getSectionArray();
                     int var24 = var20 << 4;
                     int var25 = var21 << 4;

                     for (int var26 = 0; var26 < var23.length; var26++) {
                        ChunkSection var28 = var23[var26];
                        if (var28 != null && !var28.isEmpty() && var28.hasAny((p0) -> StorageESP.lambda$onScanTick$3(tChest, tEnder, tShulker, tBarrel, tFurnace, tHopper, p0))) {
                           int var29 = var19 + var26 << 4;

                           for (int var30 = 0; var30 < 16; var30++) {
                              int var31 = var29 + var30;
                              long var32 = (long)(var31 - py);

                              for (int var34 = 0; var34 < 16; var34++) {
                                 int var35 = var24 + var34;
                                 long var36 = (long)(var35 - px);

                                 for (int var38 = 0; var38 < 16; var38++) {
                                    BlockState var42 = var28.getBlockState(var34, var30, var38);
                                    int var39;
                                    long var40;
                                    if (computeFlag(var42, tChest, tEnder, tShulker, tBarrel, tFurnace, tHopper)
                                       && var36 * var36 + var32 * var32 + (var40 = (long)((var39 = var25 + var38) - pz)) * var40 <= var13) {
                                       var12.add(new BlockPos(var35, var31, var39));
                                       if (var12.size() >= 4000) {
                                          break label92;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (Exception var43) {
         }

         if (this.isEnabled()) {
            this.list.clear();
            this.list.addAll(var12);
         }
      }
   }

   public static boolean lambda$onScanTick$3(boolean tChest, boolean tEnder, boolean tShulker, boolean tBarrel, boolean tFurnace, boolean tHopper, BlockState s) {
      return computeFlag(s, tChest, tEnder, tShulker, tBarrel, tFurnace, tHopper);
   }

   public void lambda$onEvent$2(PostMotionEvent.EventPostMotionData event) {
      this.onScanTick();
   }

   public Boolean lambda$new$1() {
      return this.both.is("Fill") || this.both.is("Both");
   }

   public Boolean lambda$new$0() {
      return this.both.is("Outline") || this.both.is("Both");
   }
}
