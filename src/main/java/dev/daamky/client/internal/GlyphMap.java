package dev.daamky.client.internal;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.model.enums.ThemeVariant;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public final class GlyphMap {
   public static final long timeStamp = 60000L;
   public static final ConcurrentHashMap<GlyphMap.ColorKey, GlyphMap.CacheEntry> concurrentHashMap = new ConcurrentHashMap<>();
   public static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
   public static final DelayQueue<GlyphMap.CacheEntry> delayQueue = new DelayQueue<>();
   public static Char2IntArrayMap char2IntArrayMap = new Char2IntArrayMap() {
      {
         this.put('0', 0);
         this.put('1', 170);
         this.put('2', 43520);
         this.put('3', 43690);
         this.put('4', 11141120);
         this.put('5', 11141290);
         this.put('6', 16755200);
         this.put('7', 11184810);
         this.put('8', 5592405);
         this.put('9', 5592575);
         this.put('A', 5635925);
         this.put('B', 5636095);
         this.put('C', 16733525);
         this.put('D', 16733695);
         this.put('E', 16777045);
         this.put('F', 16777215);
      }
   };
   public static final Pattern pattern = Pattern.compile("(?i)§[0-9a-f-or]");
   public static final int intVal = computeColor(255, 0, 0);
   public static final int int2 = computeColor(0, 255, 0);
   public static final int int3 = computeColor(0, 0, 255);
   public static final int int4 = computeColor(255, 255, 0);
   public static final int mkuflag = computeInt5(255);
   public static final int int5 = computeInt5(0);

   public static int[] computeInts(int color) {
      return new int[]{color, color, color, color, color, color, color, color};
   }

   public static int computeCurrent(int speed, int index, int startColor, int endColor, boolean trueColor) {
      int var5 = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      var5 = (var5 >= 180 ? 360 - var5 : var5) * 2;
      return initBlue(startColor, endColor, (float)var5 / 360.0F, trueColor);
   }

   public static int initInit(float health, float maxHealth) {
      float var2 = MathHelper.clamp(health / maxHealth, 0.0F, 1.0F);
      return var2 <= 0.5F
         ? initBlue(new Color(255, 0, 0).getRGB(), new Color(255, 255, 0).getRGB(), var2 / 0.5F)
         : initBlue(new Color(255, 255, 0).getRGB(), new Color(0, 255, 0).getRGB(), (var2 - 0.5F) / 0.5F);
   }

   public static int getAnimation() {
      ThemeVariant var0 = getSelected();
      ThemeVariant var1 = getPre();
      return computeInt2(var0.getClient(), var1.getClient(), (double)(1.0F - MainMenuScreen.animation14.getEnd()));
   }

   public static int getRect() {
      ThemeVariant var0 = getSelected();
      ThemeVariant var1 = getPre();
      return computeInt2(var0.getRect(), var1.getRect(), (double)(1.0F - MainMenuScreen.animation14.getEnd()));
   }

   public static ThemeVariant getSelected() {
      return MainMenuScreen.selectedTheme != null ? MainMenuScreen.selectedTheme : ThemeVariant.NIGHT;
   }

   public static ThemeVariant getPre() {
      return MainMenuScreen.preSelectedTheme != null ? MainMenuScreen.preSelectedTheme : ThemeVariant.NIGHT;
   }

   public static int initBlue(int color1, int color2, float amount) {
      amount = MathHelper.clamp(amount, 0.0F, 1.0F);
      Color var3 = new Color(color1, true);
      Color var4 = new Color(color2, true);
      int var5 = (int)((float)var3.getRed() + (float)(var4.getRed() - var3.getRed()) * amount);
      int var6 = (int)((float)var3.getGreen() + (float)(var4.getGreen() - var3.getGreen()) * amount);
      int var7 = (int)((float)var3.getBlue() + (float)(var4.getBlue() - var3.getBlue()) * amount);
      int var8 = (int)((float)var3.getAlpha() + (float)(var4.getAlpha() - var3.getAlpha()) * amount);
      return new Color(var5, var6, var7, var8).getRGB();
   }

   public static int initBlue(int color1, int color2, float amount, boolean trueColor) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      Color var4 = new Color(color1, true);
      Color var5 = new Color(color2, true);
      return new Color(
            (int)((float)var4.getRed() + (float)(var5.getRed() - var4.getRed()) * amount),
            (int)((float)var4.getGreen() + (float)(var5.getGreen() - var4.getGreen()) * amount),
            (int)((float)var4.getBlue() + (float)(var5.getBlue() - var4.getBlue()) * amount),
            (int)((float)var4.getAlpha() + (float)(var5.getAlpha() - var4.getAlpha()) * amount)
         )
         .getRGB();
   }

   public static int computeMax(int color, float opacity) {
      int var2 = color >> 24 & 0xFF;
      int var3 = (int)((float)var2 * opacity);
      var3 = Math.max(0, Math.min(255, var3));
      return color & 16777215 | var3 << 24;
   }

   public static int computeMax(int color1, int color2, float factor) {
      factor = Math.min(1.0F, Math.max(0.0F, factor));
      int var3 = color1 >> 24 & 0xFF;
      int var4 = color1 >> 16 & 0xFF;
      int var5 = color1 >> 8 & 0xFF;
      int var6 = color1 & 0xFF;
      int var7 = color2 >> 24 & 0xFF;
      int var8 = color2 >> 16 & 0xFF;
      int var9 = color2 >> 8 & 0xFF;
      int var10 = color2 & 0xFF;
      int var11 = (int)((float)var3 + (float)(var7 - var3) * factor);
      int var12 = (int)((float)var4 + (float)(var8 - var4) * factor);
      int var13 = (int)((float)var5 + (float)(var9 - var5) * factor);
      int var14 = (int)((float)var6 + (float)(var10 - var6) * factor);
      return var11 << 24 | var12 << 16 | var13 << 8 | var14;
   }

   public static int[] computeInts2(int color) {
      return new int[]{color, color, color, color, color, color, color, color, color};
   }

   public static String computeReplace(String text) {
      return text != null && !text.isEmpty() ? pattern.matcher(text).replaceAll("") : null;
   }

   public static int computeMin(int color, float percent01) {
      return computeInt2(
         computeInt(color),
         Math.min(255, Math.round((float)computeInt2(color) / percent01)),
         Math.min(255, Math.round((float)computeInt3(color) / percent01)),
         computeInt4(color)
      );
   }

   public static int computeInt(int color, int alpha) {
      return MathHelper.clamp(alpha, 0, 255) << 24 | color & 16777215;
   }

   public static int computeInt(int color, float alpha) {
      int var2 = color >> 16 & 0xFF;
      int var3 = color >> 8 & 0xFF;
      int var4 = color & 0xFF;
      return computeInt(var2, var3, var4, (int)alpha);
   }

   public static int computeInt(int red, int green, int blue, int alpha) {
      int var4 = 0;
      var4 |= alpha << 24;
      var4 |= red << 16;
      var4 |= green << 8;
      int var8;
      return var8 = var4 | blue;
   }

   public static Color initBlue(Color color, Color color2, double n) {
      float var4 = MathHelper.clamp((float)Math.sin((Math.PI * 6) * (n / 4.0 % 1.0)) / 2.0F + 0.5F, 0.0F, 1.0F);
      return new Color(
         MathHelper.lerp((float)color.getRed() / 255.0F, (float)color2.getRed() / 255.0F, var4),
         MathHelper.lerp((float)color.getGreen() / 255.0F, (float)color2.getGreen() / 255.0F, var4),
         MathHelper.lerp((float)color.getBlue() / 255.0F, (float)color2.getBlue() / 255.0F, var4),
         MathHelper.lerp((float)color.getAlpha() / 255.0F, (float)color2.getAlpha() / 255.0F, var4)
      );
   }

   public static int computeInt(int oldValue, int newValue, double interpolationValue) {
      return computeDouble((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   public static Double computeDouble(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   public static int computeColor(int color) {
      float var1 = (float)(color >> 24 & 0xFF) / 255.0F;
      float var2 = (float)(color >> 16 & 0xFF) / 255.0F;
      float var3 = (float)(color >> 8 & 0xFF) / 255.0F;
      float var4 = (float)(color & 0xFF) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
      return color;
   }

   public static int computeCode(int speed, int index) {
      double var2 = (double)((int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L));
      double var4;
      return Color.getHSBColor((var4 = var2 % 360.0) / 360.0 < 0.5 ? -((float)(var4 / 360.0)) : (float)(var4 / 360.0), 0.5F, 1.0F).hashCode();
   }

   public static float[] computeBlue(Color color) {
      return new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F};
   }

   public static float[] computeFloats(int color) {
      int[] var1 = computeInts3(color);
      return new float[]{(float)var1[0] / 255.0F, (float)var1[1] / 255.0F, (float)var1[2] / 255.0F, (float)var1[3] / 255.0F};
   }

   public static int[] computeInts3(int color) {
      return new int[]{color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF};
   }

   public static int computeCurrent(float speed, int offset, int color1, int color2) {
      long var4 = System.currentTimeMillis() + (long)offset;
      double var6 = (Math.sin((double)var4 * 0.001 * (double)speed) + 1.0) / 2.0;
      int var8 = color1 >> 16 & 0xFF;
      int var9 = color1 >> 8 & 0xFF;
      int var10 = color1 & 0xFF;
      int var11 = color2 >> 16 & 0xFF;
      int var12 = color2 >> 8 & 0xFF;
      int var13 = color2 & 0xFF;
      int var14 = (int)((double)var8 + (double)(var11 - var8) * var6);
      int var15 = (int)((double)var9 + (double)(var12 - var9) * var6);
      int var16 = (int)((double)var10 + (double)(var13 - var10) * var6);
      return var14 << 16 | var15 << 8 | var16;
   }

   public static int computeInt(int from, int to, float fraction) {
      fraction = computeFloat(fraction);
      int var3 = from >> 24 & 0xFF;
      int var4 = from >> 16 & 0xFF;
      int var5 = from >> 8 & 0xFF;
      int var6 = from & 0xFF;
      int var7 = to >> 24 & 0xFF;
      int var8 = to >> 16 & 0xFF;
      int var9 = to >> 8 & 0xFF;
      int var10 = to & 0xFF;
      int var11 = (int)((float)var3 + (float)(var7 - var3) * fraction);
      int var12 = (int)((float)var4 + (float)(var8 - var4) * fraction);
      int var13 = (int)((float)var5 + (float)(var9 - var5) * fraction);
      int var14 = (int)((float)var6 + (float)(var10 - var6) * fraction);
      return (var11 & 0xFF) << 24 | (var12 & 0xFF) << 16 | (var13 & 0xFF) << 8 | var14 & 0xFF;
   }

   public static float computeFloat(float v) {
      if (v < 0.0F) {
         return 0.0F;
      } else {
         return v > 1.0F ? 1.0F : v;
      }
   }

   public static float[] computeFloats2(int color) {
      return new float[]{
         (float)(color >> 16 & 0xFF) / 255.0F, (float)(color >> 8 & 0xFF) / 255.0F, (float)(color & 0xFF) / 255.0F, (float)(color >> 24 & 0xFF) / 255.0F
      };
   }

   public static int[] computeInts4(int color) {
      return new int[]{
         (int)((float)(color >> 16 & 0xFF) / 255.0F),
         (int)((float)(color >> 8 & 0xFF) / 255.0F),
         (int)((float)(color & 0xFF) / 255.0F),
         (int)((float)(color >> 24 & 0xFF) / 255.0F)
      };
   }

   public static int computeInt(int c) {
      return c >> 16 & 0xFF;
   }

   public static int computeInt2(int c) {
      return c >> 8 & 0xFF;
   }

   public static int computeInt3(int c) {
      return c & 0xFF;
   }

   public static int computeInt4(int c) {
      return c >> 24 & 0xFF;
   }

   public static float computeFloat(int c) {
      return(floatVal)computeInt(c) / 255.0F;
   }

   public static float computeFloat2(int c) {
      return(floatVal)computeInt2(c) / 255.0F;
   }

   public static float computeFloat3(int c) {
      return(floatVal)computeInt3(c) / 255.0F;
   }

   public static float computeFloat4(int c) {
      return(floatVal)computeInt4(c) / 255.0F;
   }

   public static int[] computeInts5(int c) {
      return new int[]{computeInt(c), computeInt2(c), computeInt3(c), computeInt4(c)};
   }

   public static int[] computeInts6(int c) {
      return new int[]{computeInt(c), computeInt2(c), computeInt3(c)};
   }

   public static float[] computeFloats3(int c) {
      return new float[]{computeFloat(c), computeFloat2(c), computeFloat3(c), computeFloat4(c)};
   }

   public static float[] computeFloats4(int c) {
      return new float[]{computeFloat(c), computeFloat2(c), computeFloat3(c)};
   }

   public static int computeRound(float red, float green, float blue, float alpha) {
      return getColor(Math.round(red * 255.0F), Math.round(green * 255.0F), Math.round(blue * 255.0F), Math.round(alpha * 255.0F));
   }

   public static int computeColor(int red, int green, int blue, float alpha) {
      return getColor(red, green, blue, Math.round(alpha * 255.0F));
   }

   public static int computeInt(float red, float green, float blue) {
      return computeRound(red, green, blue, 1.0F);
   }

   public static int computeColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public static int computeRound(int brightness, float alpha) {
      return computeColor(brightness, Math.round(alpha * 255.0F));
   }

   public static int computeInt5(int brightness) {
      return computeColor(brightness, brightness, brightness);
   }

   public static int computeInt2(int color, int alpha) {
      return computeInt2(computeInt(color), computeInt2(color), computeInt3(color), alpha);
   }

   public static int computeRound2(int color, float alpha) {
      return computeInt2(computeInt(color), computeInt2(color), computeInt3(color), Math.round(alpha * 255.0F));
   }

   public static int computeRound3(int color, float percent01) {
      return computeInt2(computeInt(color), computeInt2(color), computeInt3(color), Math.round((float)computeInt4(color) * percent01));
   }

   public static int computeRound4(int color, float percent01) {
      int var2 = computeInt(color);
      int var3 = computeInt2(color);
      int var4 = computeInt3(color);
      int var5 = computeInt4(color);
      short var6 = 128;
      var2 = Math.round((float)var2 + (float)(var6 - var2) * percent01);
      var3 = Math.round((float)var3 + (float)(var6 - var3) * percent01);
      var4 = Math.round((float)var4 + (float)(var6 - var4) * percent01);
      float var7 = percent01 / 2.0F;
      var2 = Math.round((float)var2 * var7);
      var3 = Math.round((float)var3 * var7);
      var4 = Math.round((float)var4 * var7);
      return computeInt2(var2, var3, var4, var5);
   }

   public static int computeRound5(int color, float percent01) {
      return computeInt2(
         Math.round((float)computeInt(color) * percent01),
         Math.round((float)computeInt2(color) * percent01),
         Math.round((float)computeInt3(color) * percent01),
         computeInt4(color)
      );
   }

   public static int computeMin2(int color, float percent01) {
      return computeInt2(
         Math.min(255, Math.round((float)computeInt(color) / percent01)),
         Math.min(255, Math.round((float)computeInt2(color) / percent01)),
         Math.min(255, Math.round((float)computeInt3(color) / percent01)),
         computeInt4(color)
      );
   }

   public static int computeInt2(int color1, int color2, float percent01) {
      float var3 = MathHelper.clamp(percent01, 0.0F, 1.0F);
      return computeInt2(
         Lerp.initRound(computeInt(color1), computeInt(color2), (double)var3),
         Lerp.initRound(computeInt2(color1), computeInt2(color2), (double)var3),
         Lerp.initRound(computeInt3(color1), computeInt3(color2), (double)var3),
         Lerp.initRound(computeInt4(color1), computeInt4(color2), (double)var3)
      );
   }

   public static int computeInt3(int color1, int color2) {
      return computeInt2(color1, color2, 0.5F);
   }

   public static int[] computeInts(int color1, int color2, int length) {
      int[] var3 = new int[length];

      for (int var4 = 0; var4 < length; var4++) {
         float var5 = (float)var4 / (float)(length - 1);
         var3[var4] = computeInt2(color1, color2, var5);
      }

      return var3;
   }

   public static int computeInt2(int color1, int color2, double amount) {
      amount = (double)((float)MathHelper.clamp(amount, 0.0, 1.0));
      return computeInt2(
         Lerp.initRound(computeInt(color1), computeInt(color2), amount),
         Lerp.initRound(computeInt2(color1), computeInt2(color2), amount),
         Lerp.initRound(computeInt3(color1), computeInt3(color2), amount),
         Lerp.initRound(computeInt4(color1), computeInt4(color2), amount)
      );
   }

   public static int computeBto(int speed, int index, float saturation, float brightness, float opacity) {
      int var5 = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      float var6 = (float)var5 / 360.0F;
      int var7 = Color.HSBtoRGB(var6, saturation, brightness);
      return getColor(computeInt(var7), computeInt2(var7), computeInt3(var7), Math.round(opacity * 255.0F));
   }

   public static int computeCurrent(int speed, int index, int first, int second) {
      int var4 = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      var4 = var4 >= 180 ? 360 - var4 : var4;
      return computeInt2(first, second, (float)var4 / 180.0F);
   }

   public static int computeInt6(int index) {
      return computeCurrent(10, index, computeInt7(index), computeMin2(computeInt7(index), 0.75F));
   }

   public static int computeInt7(int index) {
      return getAnimation();
   }

   public static int computeInt(float alpha) {
      return computeRound2(GlyphMetrics.getInt5(), alpha);
   }

   public static int computeInt8(int index) {
      return getAnimation();
   }

   public static int computeCurrent(int c1, int c2, int c3, int c4, int c5, int index, int speed) {
      double var7 = (double)((System.currentTimeMillis() + (long)index * 50L) % (long)speed);
      float var9 = (float)(var7 / (double)speed);
      int var10;
      int var11;
      float var12;
      if (var9 < 0.2F) {
         var10 = c1;
         var11 = c2;
         var12 = var9 / 0.2F;
      } else if (var9 < 0.4F) {
         var10 = c2;
         var11 = c3;
         var12 = (var9 - 0.2F) / 0.2F;
      } else if (var9 < 0.6F) {
         var10 = c3;
         var11 = c4;
         var12 = (var9 - 0.4F) / 0.2F;
      } else if (var9 < 0.8F) {
         var10 = c4;
         var11 = c5;
         var12 = (var9 - 0.6F) / 0.2F;
      } else {
         var10 = c5;
         var11 = c1;
         var12 = (var9 - 0.8F) / 0.2F;
      }

      return computeBto(var10, var11, var12);
   }

   public static int computeBto(int color1, int color2, float ratio) {
      float[] var3 = Color.RGBtoHSB(color1 >> 16 & 0xFF, color1 >> 8 & 0xFF, color1 & 0xFF, null);
      float[] var4 = Color.RGBtoHSB(color2 >> 16 & 0xFF, color2 >> 8 & 0xFF, color2 & 0xFF, null);
      float var5 = (float)(1.0 - Math.cos((double)ratio * Math.PI)) / 2.0F;
      float var6 = computeAbs(var3[0], var4[0], var5);
      float var7 = computeAbs(var3[1], var4[1], var5);
      float var8 = computeAbs(var3[2], var4[2], var5);
      return Color.HSBtoRGB(var6, var7, var8);
   }

   public static float computeAbs(float f1, float f2, float ratio) {
      if ((double)Math.abs(f2 - f1) > 0.5) {
         if (f2 > f1) {
            f1++;
         } else {
            f2++;
         }
      }

      float var3 = f1 + (f2 - f1) * ratio;
      return var3 > 1.0F ? var3 - 1.0F : var3;
   }

   public static int computeInt3(int b1, int b2, float fraction) {
      return(intVal)((float)b1 + (float)(b2 - b1) * fraction);
   }

   public static int computeInt2(float alpha) {
      return computeRound2(computeInt8(1), alpha);
   }

   public static int computeBto(int first, int second, int third, int index, int speed) {
      float var5 = ((float)System.currentTimeMillis() / (float)speed + (float)index) % 360.0F / 360.0F;
      int var6;
      if (var5 < 0.5F) {
         var6 = computeInt2(first, second, (double)(var5 * 2.0F));
      } else {
         var6 = computeInt2(second, third, (double)((var5 - 0.5F) * 2.0F));
      }

      float[] var7 = computeFloats2(var6);
      float[] var8 = Color.RGBtoHSB((int)(var7[0] * 255.0F), (int)(var7[1] * 255.0F), (int)(var7[2] * 255.0F), null);
      var8[1] *= 1.5F;
      var8[1] = Math.min(var8[1], 1.0F);
      return Color.HSBtoRGB(var8[0], var8[1], var8[2]);
   }

   public static int computeBto(int start, int end, int index, int speed) {
      int var4 = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      var4 = (var4 > 180 ? 360 - var4 : var4) + 180;
      int var5 = computeInt2(start, end, (double)MathHelper.clamp((float)var4 / 180.0F - 1.0F, 0.0F, 1.0F));
      float[] var6 = computeFloats2(var5);
      float[] var7 = Color.RGBtoHSB((int)(var6[0] * 255.0F), (int)(var6[1] * 255.0F), (int)(var6[2] * 255.0F), null);
      var7[1] *= 1.5F;
      var7[1] = Math.min(var7[1], 1.0F);
      return Color.HSBtoRGB(var7[0], var7[1], var7[2]);
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      GlyphMap.ColorKey var4 = new GlyphMap.ColorKey(red, green, blue, alpha);
      GlyphMap.CacheEntry var5 = concurrentHashMap.computeIfAbsent(var4, GlyphMap::lambda$getColor$1);
      return var5.getColor();
   }

   public static int computeInt2(int red, int green, int blue, int alpha) {
      return MathHelper.clamp(alpha, 0, 255) << 24
         | MathHelper.clamp(red, 0, 255) << 16
         | MathHelper.clamp(green, 0, 255) << 8
         | MathHelper.clamp(blue, 0, 255);
   }

   public static int computeRound2(float red, float green, float blue, float alpha) {
      return MathHelper.clamp(Math.round(alpha), 0, 255) << 24
         | MathHelper.clamp(Math.round(red), 0, 255) << 16
         | MathHelper.clamp(Math.round(green), 0, 255) << 8
         | MathHelper.clamp(Math.round(blue), 0, 255);
   }

   public static int computeColor(int red, int green, int blue) {
      return getColor(red, green, blue, 255);
   }

   public static int computeInt3(int red, int green, int blue, int alpha) {
      return MathHelper.clamp(alpha, 0, 255) << 24
         | MathHelper.clamp(red, 0, 255) << 16
         | MathHelper.clamp(green, 0, 255) << 8
         | MathHelper.clamp(blue, 0, 255);
   }

   public static String computeString(int red, int green, int blue, int alpha) {
      return red + "," + green + "," + blue + "," + alpha;
   }

   public static void runShutdown() {
      scheduledExecutorService.shutdown();
   }

      public GlyphMap() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static GlyphMap.CacheEntry lambda$getColor$1(int red, int green, int blue, int alpha, GlyphMap.ColorKey k) {
      GlyphMap.CacheEntry var5 = new GlyphMap.CacheEntry(k, computeInt3(red, green, blue, alpha), 60000L);
      delayQueue.offer(var5);
      return var5;
   }

   public static void lambda$static$0() {
      for (GlyphMap.CacheEntry var0 = delayQueue.poll(); var0 != null; var0 = delayQueue.poll()) {
         if (var0.isExpired()) {
            concurrentHashMap.remove(var0.getKey());
         }
      }
   }

   static {
      scheduledExecutorService.scheduleWithFixedDelay(GlyphMap::lambda$static$0, 0L, 1L, TimeUnit.SECONDS);
   }

   private static class CacheEntry implements Delayed {
      public final GlyphMap.ColorKey key;
      public final int color;
      public final long expirationTime;

      public CacheEntry(GlyphMap.ColorKey key, int color, long ttl) {
         this.key = key;
         this.color = color;
         this.expirationTime = System.currentTimeMillis() + ttl;
      }

      @Override
      public long getDelay(TimeUnit unit) {
         long var2 = this.expirationTime - System.currentTimeMillis();
         return unit.convert(var2, TimeUnit.MILLISECONDS);
      }

      public int compareTo(Delayed other) {
         return other instanceof GlyphMap.CacheEntry ? Long.compare(this.expirationTime, ((GlyphMap.CacheEntry)other).expirationTime) : 0;
      }

      public boolean isExpired() {
         return System.currentTimeMillis() > this.expirationTime;
      }

            public GlyphMap.ColorKey getKey() {
         return this.key;
      }

            public int getColor() {
         return this.color;
      }

            public long getExpirationTime() {
         return this.expirationTime;
      }

   }

   private static class ColorKey {
      public final int red;
      public final int green;
      public final int blue;
      public final int alpha;

            public int getRed() {
         return this.red;
      }

            public int getGreen() {
         return this.green;
      }

            public int getBlue() {
         return this.blue;
      }

            public int getAlpha() {
         return this.alpha;
      }

            public ColorKey(int red, int green, int blue, int alpha) {
         this.red = red;
         this.green = green;
         this.blue = blue;
         this.alpha = alpha;
      }

            public boolean computeBlue(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof GlyphMap.ColorKey var2)) {
            return false;
         } else if (!var2.computeFlag(this)) {
            return false;
         } else if (this.getRed() != var2.getRed()) {
            return false;
         } else if (this.getGreen() != var2.getGreen()) {
            return false;
         } else {
            return this.getBlue() != var2.getBlue() ? false : this.getAlpha() == var2.getAlpha();
         }
      }

            public boolean computeFlag(Object other) {
         return other instanceof GlyphMap.ColorKey;
      }

            public int getBlue2() {
         byte var1 = 59;
         int var2 = 1;
         var2 = var2 * 59 + this.getRed();
         var2 = var2 * 59 + this.getGreen();
         var2 = var2 * 59 + this.getBlue();
         return var2 * 59 + this.getAlpha();
      }
   }
}
