package dev.daamky.client.module.render;

import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;

@ModuleInfo(
   name = "Hand Shader",
   category = ModuleCategory.VISUALS,
   description = "Накладывает анимированный шейдер на руку и предметы от первого лица"
)
public class HandShader extends Module {
   public static final HandShader handShader = new HandShader();
   public final ModeSetting glass = new ModeSetting("Mode").value("Noise").values("Noise", "Plasma", "Electric", "Rainbow", "Glow Outline", "Glass");
   public final FloatSetting opacity = new FloatSetting("Opacity").value(0.7F).range(0.0F, 1.0F).step(0.05F);
   public final FloatSetting speed = new FloatSetting("Speed").value(1.0F).range(0.1F, 3.0F).step(0.1F);
   public final FloatSetting scale = new FloatSetting("Scale").value(1.5F).range(0.5F, 4.0F).step(0.1F);
   public final ModeSetting modeSetting = new ModeSetting("Color Mode").value("Custom").values("Theme", "Custom");
   public final ColorSetting customColor = new ColorSetting("Custom Color").computeColorSetting(new Color(0, 185, 255, 255)).computeVisible(this::lambda$new$0);

   public HandShader() {
      this.addSettings(new Setting[]{this.glass, this.opacity, this.speed, this.scale, this.modeSetting, this.customColor});
   }

   public static HandShader getInstance() {
      return handShader;
   }

   public ModeSetting getGlass() {
      return this.glass;
   }

   public int getCase() {
      String var1 = this.glass.getValue();
      if ("Plasma".equalsIgnoreCase(var1)) {
         return 1;
      } else if ("Electric".equalsIgnoreCase(var1)) {
         return 2;
      } else if ("Rainbow".equalsIgnoreCase(var1)) {
         return 3;
      } else if ("Glow Outline".equalsIgnoreCase(var1)) {
         return 4;
      } else {
         return "Glass".equalsIgnoreCase(var1) ? 5 : 0;
      }
   }

   public FloatSetting getOpacity() {
      return this.opacity;
   }

   public FloatSetting getSpeed() {
      return this.speed;
   }

   public FloatSetting getScale() {
      return this.scale;
   }

   public ModeSetting getModeSetting() {
      return this.modeSetting;
   }

   public ColorSetting getCustomColor() {
      return this.customColor;
   }

   public Color getEquals() {
      return "Theme".equals(this.modeSetting.getValue()) ? ThemeColor.getColor4() : this.customColor.getValue();
   }

   @Override
   public void onEvent() {
   }

   public Boolean lambda$new$0() {
      return "Custom".equals(this.modeSetting.getValue());
   }
}
