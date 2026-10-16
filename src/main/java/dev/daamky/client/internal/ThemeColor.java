package dev.daamky.client.internal;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;

public final class ThemeColor {
   public static ColorTheme mxauz = null;
   public static ColorTheme mretal = null;
   public static final AnimatedValue animatedValue = new AnimatedValue();

   public static ColorTheme getTheme() {
      ColorTheme var0 = White.getInstance().getTheme();
      if (mretal != var0) {
         if (mretal != null) {
            mxauz = mretal;
            animatedValue.handleStart(0.0);
         } else {
            animatedValue.handleStart(1.0);
         }

         mretal = var0;
      }

      animatedValue.update();
      animatedValue.animateTo(1.0, 300L, Easing.SINE_OUT, true);
      if (animatedValue.getValue() >= 0.99F && mxauz != null) {
         mxauz = null;
      }

      return mretal;
   }

   public static Color initColor(ColorTheme.ElementColor prev, ColorTheme.ElementColor current) {
      if (prev != null && current != null) {
         return ColorMath.initBlue(prev.getColor(), current.getColor(), (float)animatedValue.getValue());
      } else {
         return current != null ? current.getColor() : new Color(-1);
      }
   }

   public static Color computeColor(String element) {
      if (!"Primary".equals(element) && !"Secondary".equals(element)) {
         ColorTheme var2 = getTheme();
         Color var6 = mxauz != null && !(animatedValue.getValue() >= 0.99)
            ? initColor(mxauz.computeNext(element), var2.computeNext(element))
            : var2.initColor(element);
         if (!ThemeManager.getInstance().getIsDayMode() && !MainMenuScreen.isDayMode) {
            switch (element) {
               case "Blur":
               case "Widget blur":
               case "Background blur":
                  return new Color(9, 10, 14, 255);
            }
         } else {
            switch (element) {
               case "Text":
                  return new Color(15, 23, 42, var6.getAlpha());
               case "Inactive text":
                  return new Color(100, 116, 139, var6.getAlpha());
               case "Knob":
                  return new Color(15, 23, 42, var6.getAlpha());
               case "Inactive knob":
                  return var2.getPrimary();
               case "Blur":
               case "Widget blur":
               case "Background blur":
                  return new Color(255, 255, 255, 255);
            }
         }

         return var6;
      } else {
         int var1 = MainMenuScreen.getAccent();
         return new Color(var1, true);
      }
   }

   public static Color computeColor(Color color, int alpha) {
      if (alpha == 255 && color.getAlpha() == 255) {
         return color;
      } else {
         int var2 = (int)((float)color.getAlpha() / 255.0F * (float)alpha);
         return color.getAlpha() == var2 ? color : ColorMath.initBlue(color, var2);
      }
   }

   public static Color computeColor(int index) {
      return computeColor(index, 255);
   }

   public static Color computeColor(int index, int alpha) {
      return computeColor(ColorMath.computeCurrent(15, index, computePrimary(alpha), computeSecondary(alpha)), alpha);
   }

   public static int getInt() {
      return MainMenuScreen.getAccent();
   }

   public static int computeInt(int alpha) {
      int var1 = MainMenuScreen.getAccent();
      return var1 & 16777215 | (alpha & 0xFF) << 24;
   }

   public static int getInt2() {
      return MainMenuScreen.getAccent2();
   }

   public static int computeInt2(int alpha) {
      int var1 = MainMenuScreen.getAccent2();
      return var1 & 16777215 | (alpha & 0xFF) << 24;
   }

   public static int computeInt3(int index) {
      return computeCurrent(index, 255);
   }

   public static int computeCurrent(int index, int alpha) {
      return computeInt(index, alpha, System.currentTimeMillis());
   }

   public static int computeInt(int index, int alpha, long now) {
      int var4 = computeInt(alpha);
      int var5 = computeInt2(alpha);
      int var6 = (int)((now / 15L + (long)index) % 360L);
      var6 = (var6 > 180 ? 360 - var6 : var6) + 180;
      float var7 = (float)var6 / 180.0F;
      int var8 = (int)var7;
      float var9 = var7 - (float)var8;
      int var10 = var8 == 0 ? var4 : var5;
      int var11 = var8 == 0 ? var5 : var4;
      return ColorMath.computeInt(var10, var11, var9);
   }

   public static Color getColor() {
      return computeBlur(255);
   }

   public static Color computeBlur(int alpha) {
      return computeColor(computeColor("Blur"), alpha);
   }

   public static Color getColor2() {
      return computeBlur2(255);
   }

   public static Color computeBlur2(int alpha) {
      return computeColor(computeColor("Widget blur"), alpha);
   }

   public static Color getColor3() {
      return computeBackground(255);
   }

   public static Color computeBackground(int alpha) {
      return computeColor(computeColor("Background blur"), alpha);
   }

   public static Color getColor4() {
      return computePrimary(255);
   }

   public static Color computePrimary(int alpha) {
      return computeColor(computeColor("Primary"), alpha);
   }

   public static Color getColor5() {
      return computeSecondary(255);
   }

   public static Color computeSecondary(int alpha) {
      return computeColor(computeColor("Secondary"), alpha);
   }

   public static Color getColor6() {
      return computeKnob(255);
   }

   public static Color computeKnob(int alpha) {
      return computeColor(computeColor("Knob"), alpha);
   }

   public static Color getColor7() {
      return computeInactive(255);
   }

   public static Color computeInactive(int alpha) {
      return computeColor(computeColor("Inactive knob"), alpha);
   }

   public static Color getColor8() {
      return computeText(255);
   }

   public static Color computeText(int alpha) {
      return computeColor(computeColor("Text"), alpha);
   }

   public static Color getColor9() {
      return computeInactive2(255);
   }

   public static Color computeInactive2(int alpha) {
      return computeColor(computeColor("Inactive text"), alpha);
   }

   public static Color getColor10() {
      return computePositive(255);
   }

   public static Color computePositive(int alpha) {
      return computeColor(computeColor("Positive"), alpha);
   }

   public static Color getColor11() {
      return computeColor2(255);
   }

   public static Color computeColor2(int alpha) {
      return computeColor(computeColor("Middle"), alpha);
   }

   public static Color getColor12() {
      return computeNegative(255);
   }

   public static Color computeNegative(int alpha) {
      return computeColor(computeColor("Negative"), alpha);
   }

   public ThemeColor() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
