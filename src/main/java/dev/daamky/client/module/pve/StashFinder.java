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
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

@ModuleInfo(
   name = "StashFinder",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Ищет обычный и плачущий обсидиан в большом радиусе — помогает находить чужие базы и стеши"
)
public class StashFinder extends Module {
   public static StashFinder stashFinder;
   public static final int moromd = 6000;
   public final BooleanSetting obsidian = new BooleanSetting("Obsidian").value(true);
   public final BooleanSetting cryingObsidian = new BooleanSetting("Crying Obsidian").value(true);
   public final FloatSetting range = new FloatSetting("Range").value(96.0F).range(50.0F, 200.0F).step(1.0F);
   public final FloatSetting scanDelayMs = new FloatSetting("Scan Delay (ms)").value(2000.0F).range(500.0F, 8000.0F).step(100.0F);
   public final ModeSetting both = new ModeSetting("Render Mode").value("Outline").values("Fill", "Outline", "Both");
   public final ColorSetting obsidianColor = new ColorSetting("Obsidian Color").computeColorSetting(new Color(150, 60, 255, 200));
   public final ColorSetting cryingColor = new ColorSetting("Crying Color").computeColorSetting(new Color(90, 130, 255, 220));
   public final List<BlockPos> list = new CopyOnWriteArrayList<>();
   public final List<BlockPos> list2 = new CopyOnWriteArrayList<>();
   public long longVal = 0L;
   public Thread thread;

   public static StashFinder getInstance() {
      if (stashFinder == null) {
         stashFinder = new StashFinder();
      }

      return stashFinder;
   }

   public StashFinder() {
      this.addSettings(new Setting[]{this.obsidian, this.cryingObsidian, this.range, this.scanDelayMs, this.both, this.obsidianColor, this.cryingColor});
      stashFinder = this;
      Runnable var1 = this::lambda$new$0;
      this.obsidian.onAction(var1);
      this.cryingObsidian.onAction(var1);
      this.range.onAction(var1);
      this.scanDelayMs.onAction(var1);
   }

   @Override
   public void onEnable() {
      this.list.clear();
      this.list2.clear();
      this.longVal = 0L;
   }

   @Override
   public void onDisable() {
      this.list.clear();
      this.list2.clear();
      super.onDisable();
   }

   @Override
   public void onEvent() {
      this.addEventListeners(
         new Subscription[]{
            PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1)),
            Render3DEvent.getInstance().subscribe(new EventListener<>(this::initBoth))
         }
      );
   }

   public void onScanTick() {
      if (mc.player != null && mc.world != null) {
         long var1 = System.currentTimeMillis();
         if (var1 - this.longVal >= this.scanDelayMs.getValue().longValue()) {
            this.longVal = var1;
            if (this.thread == null || !this.thread.isAlive()) {
               boolean var3 = this.obsidian.getValue();
               boolean var4 = this.cryingObsidian.getValue();
               if (!var3 && !var4) {
                  this.list.clear();
                  this.list2.clear();
               } else {
                  int var5 = this.range.getValue().intValue();
                  BlockPos var6 = mc.player.getBlockPos();
                  int var7 = var6.getX();
                  int var8 = var6.getY();
                  int var9 = var6.getZ();
                  ClientWorld var10 = mc.world;
                  this.thread = new Thread(() -> this.lambda$onScanTick$3(var10, var5, var7, var9, var3, var4, var8), "daamky-stashfinder-scan");
                  this.thread.setDaemon(true);
                  this.thread.start();
               }
            }
         }
      }
   }

   public void initBoth(Render3DEvent.Render3DEventData event) {
      if (this.isEnabled() && mc.player != null && mc.world != null) {
         boolean var2 = this.both.is("Outline") || this.both.is("Both");
         boolean var3 = this.both.is("Fill") || this.both.is("Both");
         Color var4 = this.obsidianColor.getValue();
         Color var5 = this.cryingColor.getValue();
         Color var6 = new Color(var4.getRed(), var4.getGreen(), var4.getBlue(), Math.min(60, var4.getAlpha()));
         Color var7 = new Color(var5.getRed(), var5.getGreen(), var5.getBlue(), Math.min(60, var5.getAlpha()));
         this.handleNext(this.list, var4, var6, var2, var3);
         this.handleNext(this.list2, var5, var7, var2, var3);
      }
   }

   public void handleNext(List<BlockPos> list, Color outlineCol, Color fillCol, boolean outline, boolean fill) {
      for (BlockPos var7 : list) {
         float var8 = (float)var7.getX();
         float var9 = (float)var7.getY();
         float var10 = (float)var7.getZ();
         float var11 = var8 + 1.0F;
         float var12 = var9 + 1.0F;
         float var13 = var10 + 1.0F;
         if (fill) {
            Render.boxRenderer.handleFloat(var8, var9, var10, var11, var12, var13, 0.0F, fillCol, BoxRenderer.Render.FILL, 0.0F);
         }

         if (outline) {
            Render.boxRenderer.handleFloat(var8, var9, var10, var11, var12, var13, 1.5F, outlineCol, BoxRenderer.Render.OUTLINE, 0.0F);
         }
      }
   }

   public void lambda$onScanTick$3(ClientWorld world, int r, int px, int pz, boolean wantObs, boolean wantCry, int py) {
      if (world != null) {
         ArrayList var9 = new ArrayList();
         ArrayList var8 = new ArrayList();

         try {
            long var10 = (long)r * (long)r;
            int var12 = px - r >> 4;
            int var13 = px + r >> 4;
            int var14 = pz - r >> 4;
            int var15 = pz + r >> 4;
            int var16 = world.getBottomSectionCoord();

            label120:
            for (int var17 = var12; var17 <= var13; var17++) {
               for (int var18 = var14; var18 <= var15; var18++) {
                  WorldChunk var19 = world.getChunk(var17, var18);
                  if (var19 != null) {
                     ChunkSection[] var20 = var19.getSectionArray();
                     int var21 = var17 << 4;
                     int var22 = var18 << 4;

                     for (int var23 = 0; var23 < var20.length; var23++) {
                        ChunkSection var25 = var20[var23];
                        if (var25 != null && !var25.isEmpty() && var25.hasAny((p0) -> StashFinder.lambda$onScanTick$2(wantObs, wantCry, p0))) {
                           int var26 = var16 + var23 << 4;

                           for (int var27 = 0; var27 < 16; var27++) {
                              int var28 = var26 + var27;
                              long var29 = (long)(var28 - py);

                              for (int var31 = 0; var31 < 16; var31++) {
                                 int var32 = var21 + var31;
                                 long var33 = (long)(var32 - px);

                                 for (int var35 = 0; var35 < 16; var35++) {
                                    BlockState var39 = var25.getBlockState(var31, var27, var35);
                                    boolean var40 = wantObs && var39.isOf(Blocks.OBSIDIAN);
                                    boolean var41 = wantCry && var39.isOf(Blocks.CRYING_OBSIDIAN);
                                    int var36;
                                    long var37;
                                    if ((var40 || var41) && var33 * var33 + var29 * var29 + (var37 = (long)((var36 = var22 + var35) - pz)) * var37 <= var10) {
                                       if (var40) {
                                          var9.add(new BlockPos(var32, var28, var36));
                                       } else {
                                          var8.add(new BlockPos(var32, var28, var36));
                                       }

                                       if (var9.size() + var8.size() >= 6000) {
                                          break label120;
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
            this.list.addAll(var9);
            this.list2.clear();
            this.list2.addAll(var8);
         }
      }
   }

   public static boolean lambda$onScanTick$2(boolean wantObs, boolean wantCry, BlockState arg_0) {
      return wantObs && arg_0.isOf(Blocks.OBSIDIAN) || wantCry && arg_0.isOf(Blocks.CRYING_OBSIDIAN);
   }

   public void lambda$onEvent$1(PostMotionEvent.EventPostMotionData event) {
      this.onScanTick();
   }

   public void lambda$new$0() {
      this.list.clear();
      this.list2.clear();
   }
}
