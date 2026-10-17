package dev.daamky.client.module.misc;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.Arrays;
import org.framesync.client.RenderManager;
import org.framesync.config.ModConfig;

@ModuleInfo(
   name = "Optimization",
   category = ModuleCategory.MICS,
   description = "Оптимизирует игру"
)
public class Optimization extends Module {
   public static final Optimization optimization = new Optimization();
   public final BooleanSetting fpsBoost = new BooleanSetting("FPS Boost").value(true);
   public final String[] stringList = new String[]{
      "Particles", "Weather", "Grass", "Shadows", "Entity Culling", "Block Entity Culling", "Explosion Optimization", "Fast Math", "Fast Leaves", "RAM Cleaner"
   };
   public final MultiBooleanSetting remove = new MultiBooleanSetting("Remove")
      .value(Arrays.stream(this.stringList).map(Optimization::lambda$new$0).toArray(Optimization::lambda$new$1));
   public final FloatSetting cullDistance = new FloatSetting("Cull Distance").value(48.0F).range(16.0F, 128.0F).step(2.0F).visibleWhen(this::lambda$new$2);
   public final FloatSetting blockEntityCullDist = new FloatSetting("Block Entity Cull Dist")
      .value(48.0F)
      .range(16.0F, 128.0F)
      .step(2.0F)
      .visibleWhen(this::lambda$new$3);
   public static boolean flag = false;
   public static long timeStamp = 0L;

   public Optimization() {
      this.addSettings(new Setting[]{this.fpsBoost, this.remove, this.cullDistance, this.blockEntityCullDist});
      this.fpsBoost.onAction(this::updateAuto);
      this.remove.onAction(this::lambda$new$4);
      this.updateAuto();
      this.setEnabledInternal(true, true);
   }

   public void updateAuto() {
      try {
         ModConfig var1 = ModConfig.getInstance();
         if (var1 != null) {
            var1.setAutoDetectMonitorHz(false);
            var1.setEnableRenderLimit(false);
            var1.setLowEndMode(false);
            RenderManager var2 = RenderManager.getInstance();
            if (var2 != null) {
               var2.updateSettings(1000, false);
            }
         }
      } catch (Throwable var3) {
      }
   }

   public boolean checkEnabled() {
      return this.isEnabled() && this.fpsBoost.getValue();
   }

   public boolean checkEnabled2() {
      return this.isEnabled() && this.remove.isEnabled("Particles");
   }

   public boolean checkEnabled3() {
      return this.isEnabled() && this.remove.isEnabled("Weather");
   }

   public boolean checkEnabled4() {
      return this.isEnabled() && this.remove.isEnabled("Grass");
   }

   public boolean checkEnabled5() {
      return this.isEnabled() && this.remove.isEnabled("Shadows");
   }

   public boolean checkEnabled6() {
      return this.isEnabled() && this.remove.isEnabled("Entity Culling");
   }

   public boolean checkEnabled7() {
      return this.isEnabled() && this.remove.isEnabled("Block Entity Culling");
   }

   public boolean checkEnabled8() {
      return this.isEnabled() && this.remove.isEnabled("Explosion Optimization");
   }

   public boolean checkEnabled9() {
      return this.isEnabled() && this.remove.isEnabled("Fast Math");
   }

   public boolean checkEnabled10() {
      return this.isEnabled() && this.remove.isEnabled("Fast Leaves");
   }

   public boolean checkEnabled11() {
      return this.isEnabled() && this.remove.isEnabled("RAM Cleaner");
   }

   public static void cleanRam() {
      long var0 = System.currentTimeMillis();
      if (var0 - timeStamp >= 60000L) {
         timeStamp = var0;
         Thread var2 = new Thread(Optimization::lambda$cleanRam$5, "Daamky-RAM-Cleaner");
         var2.setDaemon(true);
         var2.start();
      }
   }

   public float getCull() {
      return this.cullDistance.getValue();
   }

   public float getBlock() {
      return this.blockEntityCullDist.getValue();
   }

   public int computeEnabled(int baseFps) {
      return this.isEnabled() && this.checkEnabled() ? Math.max(baseFps, Math.round((float)baseFps * 1.5F)) : baseFps;
   }

   @Override
   public void onEvent() {
   }

   @Override
   public void onEnable() {
      flag = this.checkEnabled4();
      this.updateAuto();
      if (mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }
   }

   @Override
   public void onDisable() {
      flag = false;
      this.updateAuto();
      if (mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }
   }

   public static Optimization getInstance() {
      return optimization;
   }

   public static void lambda$cleanRam$5() {
      try {
         System.gc();
      } catch (Throwable var1) {
      }
   }

   public void lambda$new$4() {
      flag = this.checkEnabled4();
      if (mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }
   }

   public Boolean lambda$new$3() {
      return this.remove.isEnabled("Block Entity Culling");
   }

   public Boolean lambda$new$2() {
      return this.remove.isEnabled("Entity Culling");
   }

   public static BooleanSetting[] lambda$new$1(int x$0) {
      return new BooleanSetting[x$0];
   }

   public static BooleanSetting lambda$new$0(String name) {
      return new BooleanSetting(name).value(true);
   }
}
