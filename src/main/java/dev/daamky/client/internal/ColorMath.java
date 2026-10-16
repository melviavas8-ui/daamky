package dev.daamky.client.internal;

import java.awt.Color;

public final class ColorMath {
   public static final float[] floats = new float[4];
   public static final Color[] mdiowv = new Color[256];

   public static Color computeColor(int value) {
      return mdiowv[value & 0xFF];
   }

   public static float[] computeBlue(Color color) {
      floats[0] = (float)color.getRed() / 255.0F;
      floats[1] = (float)color.getGreen() / 255.0F;
      floats[2] = (float)color.getBlue() / 255.0F;
      floats[3] = (float)color.getAlpha() / 255.0F;
      return floats;
   }

   public static float[] computeFloats(int color) {
      floats[0] = (float)(color >> 16 & 0xFF) / 255.0F;
      floats[1] = (float)(color >> 8 & 0xFF) / 255.0F;
      floats[2] = (float)(color & 0xFF) / 255.0F;
      floats[3] = (float)(color >> 24 & 0xFF) / 255.0F;
      return floats;
   }

   public static int[] computeInts(int color) {
      return new int[]{color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF};
   }

   public static Color initBlue(Color color, int alpha) {
      int var2 = alpha < 0 ? 0 : Math.min(alpha, 255);
      return color.getAlpha() == var2 ? color : new Color(color.getRed(), color.getGreen(), color.getBlue(), var2);
   }

   public static int computeMin(int color, int alpha) {
      int var2 = alpha < 0 ? 0 : Math.min(alpha, 255);
      return color & 16777215 | var2 << 24;
   }

   public static Color computeCurrent(int speed, int index, Color... colors) {
      int var3 = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      var3 = (var3 > 180 ? 360 - var3 : var3) + 180;
      int var4 = (int)((float)var3 / 360.0F * (float)colors.length);
      if (var4 == colors.length) {
         var4--;
      }

      Color var5 = colors[var4];
      Color var6 = colors[var4 == colors.length - 1 ? 0 : var4 + 1];
      return initBlue(var5, var6, (float)var3 / 360.0F * (float)colors.length - (float)var4);
   }

   public static Color computeColor(Color to, Color from, double amount) {
      return initBlue(to, from, (float)amount);
   }

   public static Color initBlue(Color to, Color from, float amount) {
      if (amount <= 0.0F) {
         return from;
      } else if (amount >= 1.0F) {
         return to;
      } else {
         float var3 = 1.0F - amount;
         int var4 = (int)((float)to.getRed() + (float)(from.getRed() - to.getRed()) * var3);
         int var5 = (int)((float)to.getGreen() + (float)(from.getGreen() - to.getGreen()) * var3);
         int var6 = (int)((float)to.getBlue() + (float)(from.getBlue() - to.getBlue()) * var3);
         int var7 = (int)((float)to.getAlpha() + (float)(from.getAlpha() - to.getAlpha()) * var3);
         return new Color(var4, var5, var6, var7);
      }
   }

   public static int computeInt(int to, int from, float amount) {
      if (amount <= 0.0F) {
         return from;
      } else if (amount >= 1.0F) {
         return to;
      } else {
         float var3 = 1.0F - amount;
         int var4 = to >> 24 & 0xFF;
         int var5 = to >> 16 & 0xFF;
         int var6 = to >> 8 & 0xFF;
         int var7 = to & 0xFF;
         int var8 = from >> 24 & 0xFF;
         int var9 = from >> 16 & 0xFF;
         int var10 = from >> 8 & 0xFF;
         int var11 = from & 0xFF;
         int var12 = (int)((float)var5 + (float)(var9 - var5) * var3);
         int var13 = (int)((float)var6 + (float)(var10 - var6) * var3);
         int var14 = (int)((float)var7 + (float)(var11 - var7) * var3);
         int var15 = (int)((float)var4 + (float)(var8 - var4) * var3);
         return var15 << 24 | var12 << 16 | var13 << 8 | var14;
      }
   }

   public static Color initBlue(float progress, Color color1, Color color2) {
      if (progress < 0.0F || progress > 1.0F) {
         progress %= 1.0F;
         if (progress < 0.0F) {
            progress++;
         }
      }

      return new Color(
         (int)((float)color1.getRed() + (float)(color2.getRed() - color1.getRed()) * progress),
         (int)((float)color1.getGreen() + (float)(color2.getGreen() - color1.getGreen()) * progress),
         (int)((float)color1.getBlue() + (float)(color2.getBlue() - color1.getBlue()) * progress)
      );
   }

   public static int computeSin(int color1, int color2, float position, float totalWidth, float time, float offset) {
      float var6 = 18.0F / offset;
      float var7 = (time + position / (totalWidth * var6)) % 1.0F;
      float var8 = (float)Math.sin((double)var7 * Math.PI * 2.0) * 0.5F + 0.5F;
      int var9 = color1 >> 24 & 0xFF;
      int var10 = color1 >> 16 & 0xFF;
      int var11 = color1 >> 8 & 0xFF;
      int var12 = color1 & 0xFF;
      int var13 = color2 >> 24 & 0xFF;
      int var14 = color2 >> 16 & 0xFF;
      int var15 = color2 >> 8 & 0xFF;
      int var16 = color2 & 0xFF;
      return(intVal)((float)var9 + (float)(var13 - var9) * var8) << 24
         | (int)((float)var10 + (float)(var14 - var10) * var8) << 16
         | (int)((float)var11 + (float)(var15 - var11) * var8) << 8
         | (int)((float)var12 + (float)(var16 - var12) * var8);
   }

   public static Color computeColor(Color flashColor, Color defaultColor) {
      return computeCurrent(flashColor, defaultColor, 1.0F);
   }

   public static Color computeCurrent(Color flashColor, Color defaultColor, float alpha) {
      double var3 = (double)(System.currentTimeMillis() % 1000L) / 1000.0;
      float var5 = (float)(Math.sin(var3 * Math.PI * 2.0) * 0.5 + 0.5);
      return initBlue(flashColor, defaultColor, alpha, var5);
   }

   public static int computeInt(int r, int g, int b, int a) {
      return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
   }

   public static Color initBlue(Color flashColor, Color defaultColor, float alpha, float flashFactor) {
      float var4 = (float)defaultColor.getRed() / 255.0F;
      float var5 = (float)defaultColor.getGreen() / 255.0F;
      float var6 = (float)defaultColor.getBlue() / 255.0F;
      float var7 = (float)flashColor.getRed() / 255.0F;
      float var8 = (float)flashColor.getGreen() / 255.0F;
      float var9 = (float)flashColor.getBlue() / 255.0F;
      return new Color(
         (int)((var7 * flashFactor + var4 * (1.0F - flashFactor)) * 255.0F),
         (int)((var8 * flashFactor + var5 * (1.0F - flashFactor)) * 255.0F),
         (int)((var9 * flashFactor + var6 * (1.0F - flashFactor)) * 255.0F),
         (int)(alpha * 255.0F)
      );
   }

   public ColorMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      for (int var0 = 0; var0 < 256; var0++) {
         mdiowv[var0] = new Color(var0, var0, var0);
      }
   }
}
