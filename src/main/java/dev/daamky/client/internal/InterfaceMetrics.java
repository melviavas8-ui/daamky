package dev.daamky.client.internal;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;

public final class InterfaceMetrics {
   public static final float floatVal = 130.0F;
   public static final float float2 = 10.0F;
   public static final float float3 = 10.0F;
   public static final float float4 = 8.0F;
   public static final float float5 = 3.5F;
   public static final float float6 = 3.0F;
   public static final float float7 = 28.0F;
   public static final float float8 = 7.0F;
   public static final float float9 = 8.0F;
   public static final float float10 = 4.0F;
   public static final float float11 = 18.5F;
   public static final float float12 = 6.4F;
   public static final float megmeds = 3.0F;
   public static final float float13 = 7.0F;
   public static final float float14 = 3.0F;
   public static final float float15 = 6.5F;
   public static final float mpujae = 10.0F;
   public static final float mowdu = 1.0F;
   public static final float float16 = 4.0F;
   public static final float float17 = 5.0F;
   public static final float float18 = 10.0F;
   public static final float float19 = 2.0F;
   public static final float float20 = 3.0F;
   public static final float float21 = 5.0F;
   public static final float float22 = 12.0F;
   public static final float float23 = 2.0F;
   public static final float float24 = 14.0F;
   public static final float float25 = 6.0F;
   public static final float float26 = 4.0F;
   public static final float float27 = 20.0F;
   public static final float float28 = 16.0F;
   public static final Color color = new Color(0, 0, 0, 255);
   public static final Color color2 = new Color(255, 255, 255, 255);
   public static final Color color3 = new Color(102, 102, 102, 255);
   public static final Color color4 = new Color(136, 136, 136, 255);
   public static final Color color5 = new Color(85, 85, 85, 255);
   public static final Color color6 = new Color(34, 34, 34, 255);
   public static final Color color7 = new Color(10, 10, 10, 255);
   public static final Color color8 = new Color(34, 34, 34, 255);
   public static final Color color9 = new Color(51, 51, 51, 255);
   public static final Color color10 = new Color(10, 10, 10, 255);
   public static final Color color11 = new Color(5, 5, 5, 255);
   public static final Color color12 = new Color(30, 30, 30, 255);
   public static final Color color13 = new Color(0, 0, 0, 255);
   public static final Color color14 = new Color(255, 255, 255, 255);

   public static float computeFloat(float value) {
      return DisplayScale.getInstance().computeGui(value);
   }

   public static Color initBlue(Color color, int alpha) {
      int var2 = alpha & 0xFF;
      return new Color(colorx.getRed(), colorx.getGreen(), colorx.getBlue(), var2);
   }

   public static Color initMax(int alpha, float strength) {
      if (!Interface.check()) {
         strength = Math.min(1.0F, Math.max(0.92F, strength * 2.2F));
      }

      int var2 = Math.max(0, Math.min(255, (int)((float)alpha * strength)));
      boolean var3 = ThemeManager.getInstance().getIsDayMode();
      Color var4 = var3 ? color14 : color13;
      return new Color(var4.getRed(), var4.getGreen(), var4.getBlue(), var2);
   }

   public static Color computeColor(int alpha) {
      return initMax(alpha, 0.85F);
   }

   public static Color initInit(int alpha) {
      boolean var1 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      int var2 = Math.max(0, Math.min(255, (int)((float)alpha * (var1 ? 0.12F : 0.075F))));
      return var1 ? new Color(0, 0, 0, var2) : new Color(255, 255, 255, var2);
   }

   public static Color computeColor2(int alpha) {
      return ThemeColor.computeText(alpha);
   }

   public static Color computeColor3(int alpha) {
      return ThemeColor.computePrimary(alpha);
   }

   public static Color computeColor4(int alpha) {
      return ThemeColor.computeInactive2(alpha);
   }

   public static Color computeColor5(int alpha) {
      return ThemeColor.computeInactive2((int)((float)alpha * 0.7F));
   }

   public static Color computeColor6(int alpha) {
      return ThemeColor.computeInactive(alpha);
   }

   public static Color computeColor7(int alpha) {
      return initMax(alpha, 0.38F);
   }

   public static Color computeColor8(int alpha) {
      return initMax(alpha, 0.78F);
   }

   public static Color computeColor9(int alpha) {
      return initMax(alpha, 0.82F);
   }

   public static Color computeColor10(int alpha) {
      return initMax(alpha, 0.9F);
   }

   public static Color computeMax(int alpha, float extraAlpha) {
      return initMax(alpha, Math.max(0.25F, extraAlpha));
   }

   public static Color computeMax2(int alpha, float extraAlpha) {
      return initMax(alpha, Math.max(0.25F, extraAlpha));
   }

   public static Color initInit(Color bg, int alpha) {
      double var2 = (0.299 * (double)bg.getRed() + 0.587 * (double)bg.getGreen() + 0.114 * (double)bg.getBlue()) / 255.0;
      return var2 > 0.6 ? new Color(20, 20, 20, alpha) : new Color(245, 245, 245, alpha);
   }
}
