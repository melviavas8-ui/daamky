package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Gamma",
   category = ModuleCategory.VISUALS,
   description = "Регулировка насыщенности и яркости цветов"
)
public class Gamma extends Module {
   public static final Gamma gamma = new Gamma();
   public final FloatSetting saturation = new FloatSetting("Saturation").value(1.25F).range(0.0F, 2.5F).step(0.05F);
   public final FloatSetting brightness = new FloatSetting("Brightness").value(1.0F).range(0.5F, 2.0F).step(0.05F);
   public final FloatSetting gammaBoost = new FloatSetting("Gamma Boost").value(0.0F).range(0.0F, 10.0F).step(0.5F);

   public Gamma() {
      this.addSettings(new Setting[]{this.saturation, this.brightness, this.gammaBoost});
   }

   public static Gamma getInstance() {
      return gamma;
   }

   @Override
   public void onEvent() {
   }

   public float getEnabled() {
      return !this.isEnabled() ? 1.0F : this.saturation.getValue();
   }

   public float getEnabled2() {
      return !this.isEnabled() ? 1.0F : this.brightness.getValue();
   }

   public float getEnabled3() {
      return !this.isEnabled() ? 0.0F : this.gammaBoost.getValue();
   }

   public FloatSetting getSaturation() {
      return this.saturation;
   }

   public FloatSetting getBrightness() {
      return this.brightness;
   }

   public FloatSetting getGammaBoost() {
      return this.gammaBoost;
   }
}
