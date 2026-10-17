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
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;

@ModuleInfo(
   name = "SpawnerESP",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Подсвечивает спавнеры"
)
public class SpawnerESP extends Module {
   public static SpawnerESP spawnerESP;
   public final FloatSetting range = new FloatSetting("Range").value(32.0F).range(8.0F, 64.0F).step(1.0F);
   public final FloatSetting scanDelayMs = new FloatSetting("Scan Delay (ms)").value(500.0F).range(100.0F, 2000.0F).step(50.0F);
   public final ModeSetting both = new ModeSetting("Render Mode").value("Outline").values("Fill", "Outline", "Both");
   public final ColorSetting outlineColor = new ColorSetting("Outline Color")
      .computeColorSetting(new Color(255, 50, 50, 200))
      .computeVisible(this::lambda$new$0);
   public final ColorSetting fillColor = new ColorSetting("Fill Color").computeColorSetting(new Color(255, 50, 50, 50)).computeVisible(this::lambda$new$1);
   public final List<BlockPos> list = new CopyOnWriteArrayList<>();
   public long longVal = 0L;
   public Thread thread;

   public static SpawnerESP getInstance() {
      if (spawnerESP == null) {
         spawnerESP = new SpawnerESP();
      }

      return spawnerESP;
   }

   public SpawnerESP() {
      this.addSettings(new Setting[]{this.range, this.scanDelayMs, this.both, this.outlineColor, this.fillColor});
      spawnerESP = this;
   }

   @Override
   public void onEvent() {
      this.addEventListeners(
         new Subscription[]{
            PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2)),
            Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3))
         }
      );
   }

   @Override
   public void onDisable() {
      this.list.clear();
      super.onDisable();
   }

   public void onMotion() {
      if (mc.player != null && mc.world != null) {
         long var1 = System.currentTimeMillis();
         if ((float)(var1 - this.longVal) > this.scanDelayMs.getValue()) {
            this.longVal = var1;
            if (this.thread == null || !this.thread.isAlive()) {
               int var3 = this.range.getValue().intValue();
               BlockPos var4 = mc.player.getBlockPos();
               int var5 = var4.getX();
               int var6 = var4.getY();
               int var7 = var4.getZ();
               ClientWorld var8 = mc.world;
               this.thread = new Thread(() -> this.lambda$onMotion$4(var8, var3, var6, var5, var7), "daamky-spawneresp-scan");
               this.thread.setDaemon(true);
               this.thread.start();
            }
         }
      }
   }

   public void handleBoth(Render3DEvent.Render3DEventData event) {
      if (this.isEnabled() && mc.player != null && mc.world != null) {
         for (BlockPos var3 : this.list) {
            boolean var4 = this.both.is("Outline") || this.both.is("Both");
            boolean var5 = this.both.is("Fill") || this.both.is("Both");
            float var6 = (float)var3.getX();
            float var7 = (float)var3.getY();
            float var8 = (float)var3.getZ();
            float var9 = var6 + 1.0F;
            float var10 = var7 + 1.0F;
            float var11 = var8 + 1.0F;
            if (var5) {
               Render.boxRenderer.handleFloat(var6, var7, var8, var9, var10, var11, 0.0F, this.fillColor.getValue(), BoxRenderer.Render.FILL, 0.0F);
            }

            if (var4) {
               Render.boxRenderer.handleFloat(var6, var7, var8, var9, var10, var11, 1.5F, this.outlineColor.getValue(), BoxRenderer.Render.OUTLINE, 0.0F);
            }
         }
      }
   }

   public void lambda$onMotion$4(ClientWorld world, int r, int cy, int cx, int cz) {
      if (world != null) {
         ArrayList var6 = new ArrayList();
         Mutable var7 = new Mutable();

         try {
            for (int var8 = -r; var8 <= r; var8++) {
               int var9 = cy + var8;
               if (var9 >= -64 && var9 <= 320) {
                  for (int var10 = -r; var10 <= r; var10++) {
                     int var11 = cx + var10;

                     for (int var12 = -r; var12 <= r; var12++) {
                        var7.set(var11, var9, cz + var12);
                        Block var13 = world.getBlockState(var7).getBlock();
                        if (var13 == Blocks.SPAWNER) {
                           var6.add(var7.toImmutable());
                        }
                     }
                  }
               }
            }

            if (!this.isEnabled()) {
               return;
            }

            this.list.clear();
            this.list.addAll(var6);
         } catch (Exception var14) {
         }
      }
   }

   public void lambda$onEvent$3(Render3DEvent.Render3DEventData event) {
      this.handleBoth(event);
   }

   public void lambda$onEvent$2(PostMotionEvent.EventPostMotionData event) {
      this.onMotion();
   }

   public Boolean lambda$new$1() {
      return this.both.is("Fill") || this.both.is("Both");
   }

   public Boolean lambda$new$0() {
      return this.both.is("Outline") || this.both.is("Both");
   }
}
