package dev.daamky.client.internal;

import dev.daamky.client.setting.Setting;

public abstract class SettingRow extends Widget {
   public final Setting<?> setting;
   public final AnimatedValue animatedValue = new AnimatedValue();

   public void handleFloat(float value) {
      this.setHeight(InterfaceMetrics.computeFloat(value));
   }

   public Setting<?> getSetting() {
      return this.setting;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public SettingRow(Setting<?> setting) {
      this.setting = settingx;
   }
}
