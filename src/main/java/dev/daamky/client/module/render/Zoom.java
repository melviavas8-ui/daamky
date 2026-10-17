package dev.daamky.client.module.render;

import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.client.MinecraftClient;

@ModuleInfo(
   name = "Zoom",
   category = ModuleCategory.VISUALS,
   bind = 67,
   description = "Приближает камеру (как OptiFine зум)"
)
public class Zoom extends Module {
   public static final Zoom zoom = new Zoom();
   public final FloatSetting zoomLevel = new FloatSetting("Zoom Level").value(3.0F).range(1.5F, 8.0F).step(0.5F);
   public final FloatSetting smoothSpeed = new FloatSetting("Smooth Speed").value(10.0F).range(3.0F, 30.0F).step(1.0F);
   public static final AnimatedValue animatedValue = new AnimatedValue();
   public static double doubleVal = 1.0;

   public Zoom() {
      this.addSettings(new Setting[]{this.zoomLevel, this.smoothSpeed});
   }

   public static float getEnabled() {
      float var2 = (float)animatedValue.getValue();
      if (var2 < 0.5F && !zoom.isEnabled()) {
         animatedValue.animateTo(1.0, 0L, Easing.LINEAR);
         animatedValue.update();
         return 1.0F;
      } else {
         animatedValue.update();
         double var0 = zoom.isEnabled() ? 1.0 / (double)zoom.zoomLevel.getValue().floatValue() : 1.0;
         if (Math.abs(var0 - doubleVal) > 0.001) {
            doubleVal = var0;
            animatedValue.animateTo(var0, (long)(zoom.smoothSpeed.getValue() * 50.0F), Easing.QUINT_OUT);
         }

         return (float)animatedValue.getValue();
      }
   }

   public static void handleLevel(double vertical) {
      if (zoom.isEnabled()) {
         if (MinecraftClient.getInstance().currentScreen == null) {
            float var2 = zoom.zoomLevel.getValue();
            float var3 = zoom.zoomLevel.getStep();
            float var4 = var2 + (float)(vertical * (double)var3 * 2.0);
            var4 = Math.max(zoom.zoomLevel.getMin(), Math.min(zoom.zoomLevel.getMax(), var4));
            zoom.zoomLevel.setValue(var4);
         }
      }
   }

   @Override
   public void onEvent() {
   }

   public static Zoom getInstance() {
      return zoom;
   }
}
