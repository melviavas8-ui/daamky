package dev.daamky.client.internal;

import dev.daamky.client.theme.ThemeConfigManager;

public final class GlyphMetrics {
   public static int getInt() {
      return GlyphMap.getAnimation();
   }

   public static int getInt2() {
      return GlyphMap.getAnimation();
   }

   public static int computeInt(float alpha) {
      return GlyphMap.computeRound2(GlyphMap.getAnimation(), alpha);
   }

   public static int getInt3() {
      return GlyphMap.computeInt5(255);
   }

   public static int getInt4() {
      return GlyphMap.computeInt5(175);
   }

   public static int getInt5() {
      return ThemeConfigManager.getInstance().initActive().computeColors(1);
   }

   public static int getInt6() {
      return ThemeConfigManager.getInstance().initActive().computeColors(2);
   }

   public static int getInt7() {
      return GlyphMap.computeRound(255, 0.1F);
   }

   public static int getInt8() {
      return ThemeConfigManager.getInstance().initActive().computeColors(7);
   }

   public static int computeInt(int index) {
      return ThemeConfigManager.getInstance().initActive().computeColors(index);
   }

   public static float getFloat() {
      return ThemeConfigManager.getInstance().initActive().computeSliders(0);
   }

   public static int getRound() {
      return Math.round(ThemeConfigManager.getInstance().initActive().computeSliders(1));
   }

   public static boolean check() {
      return ThemeConfigManager.getInstance().initActive().computeBooleans(0);
   }

   public static boolean check2() {
      return ThemeConfigManager.getInstance().initActive().computeBooleans(1);
   }

   public static int computeInt2(float alpha) {
      return GlyphMap.computeRound3(ThemeConfigManager.getInstance().initActive().computeColors(6), alpha);
   }

   public static int computeInt3(float alpha) {
      return GlyphMap.computeRound2(ThemeConfigManager.getInstance().initActive().computeColors(3), alpha);
   }

   public static int computeInt4(float alpha) {
      return GlyphMap.computeRound3(ThemeConfigManager.getInstance().initActive().computeColors(7), alpha);
   }

   public static int computeInt5(float alpha) {
      return GlyphMap.computeRound3(ThemeConfigManager.getInstance().initActive().computeColors(2), alpha);
   }

   public static int computeInt6(float alpha) {
      return GlyphMap.computeRound2(ThemeConfigManager.getInstance().initActive().computeColors(4), alpha);
   }

   public static int computeInt7(float alpha) {
      return GlyphMap.computeRound2(ThemeConfigManager.getInstance().initActive().computeColors(1), alpha);
   }
}
