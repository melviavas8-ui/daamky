package dev.daamky.client.internal;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;

public final class TintColorHelper {
   public static final MatrixStack class_4587 = new MatrixStack();
   public static final Vector4f vector4f = new Vector4f();

   public static void run() {
   }

   public static void handleFloat(float x, float y, float width, float height, float alpha, int tintColor) {
      handleMax(x, y, width, height, alpha, 0.0F, 0.0F, 0.0F, 0.0F, tintColor);
   }

   public static void handleFloat(float x, float y, float width, float height, float alpha, float radius, int tintColor) {
      handleMax(x, y, width, height, alpha, radius, radius, radius, radius, tintColor);
   }

   public static void handleMax(
      float x, float y, float width, float height, float alpha, float topLeft, float topRight, float bottomRight, float bottomLeft, int tintColor
   ) {
      if (!(alpha <= 0.01F) && !(width <= 0.0F) && !(height <= 0.0F)) {
         int var10 = tintColor & 16777215;
         if (var10 == 0 || var10 == 658708 || var10 == 527378) {
            var10 = 460553;
         }

         int var11 = Math.max(1, Math.min(255, (int)(alpha * 255.0F)));
         int var12 = var11 << 24 | var10;
         boolean var13 = (var10 >> 16 & 0xFF) > 200 && (var10 >> 8 & 0xFF) > 200 && (var10 & 0xFF) > 200;
         float var14 = var13 ? 0.35F : 0.45F;
         vector4f.set(bottomRight, topRight, topLeft, bottomLeft);
         Render.shaderProgram.handleMin(class_4587, x, y, width, height, vector4f, var12, var12, var12, var12, var14);
      }
   }

   public static void handleFloat(
      float x, float y, float width, float height, float alpha, float topLeft, float topRight, float bottomRight, float bottomLeft, int tintColor
   ) {
      handleMax(x, y, width, height, alpha, topLeft, topRight, bottomRight, bottomLeft, tintColor);
   }

   public static void handleFloat(float x, float y, float width, float height, int color) {
      handleFloat(x, y, width, height, color, 0.0F);
   }

   public static void handleFloat(float x, float y, float width, float height, int color, float radius) {
      handleFloat(x, y, width, height, color, radius, radius, radius, radius);
   }

   public static void handleFloat(float x, float y, float width, float height, int color, float topLeft, float topRight, float bottomRight, float bottomLeft) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && color >>> 24 != 0) {
         vector4f.set(bottomRight, topRight, topLeft, bottomLeft);
         Render.rectRenderer.handleBlend(class_4587, x, y, width, height, vector4f, color);
      }
   }

   public static void handleFloat(float x, float y, float width, float height, int[] colors, float radius) {
      handleFloat(x, y, width, height, colors, radius, radius, radius, radius);
   }

   public static void handleFloat(float x, float y, float width, float height, int[] colors, float topLeft, float topRight, float bottomRight, float bottomLeft) {
      if (!(width <= 0.0F) && !(height <= 0.0F)) {
         int var9 = colors.length > 0 ? colors[0] : -1;
         int var10 = colors.length > 1 ? colors[1] : var9;
         int var11 = colors.length > 2 ? colors[2] : var9;
         int var12 = colors.length > 3 ? colors[3] : var9;
         vector4f.set(bottomRight, topRight, topLeft, bottomLeft);
         Render.uSizeHelper.handleBlend(class_4587, x, y, width, height, vector4f, var9, var10, var11, var12);
      }
   }

   public static void handleFloat(float x, float y, float width, float height, int color, float radius, float glowSize, float strength) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && color >>> 24 != 0) {
         vector4f.set(radius + glowSize, radius + glowSize, radius + glowSize, radius + glowSize);
         Render.rectRenderer.handleBlend(class_4587, x - glowSize, y - glowSize, width + glowSize * 2.0F, height + glowSize * 2.0F, vector4f, color);
      }
   }

   public static void handleFloat2(float x, float y, float width, float height, int color, float radius, float glowSize, float strength, float softness) {
      handleFloat(x, y, width, height, color, radius, glowSize, strength);
   }

   public static void handleFloat2(float x, float y, float width, float height, float thickness, int color) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(thickness <= 0.0F) && color >>> 24 != 0) {
         handleFloat(x, y, width, thickness, color);
         handleFloat(x, y + height - thickness, width, thickness, color);
         handleFloat(x, y + thickness, thickness, height - thickness * 2.0F, color);
         handleFloat(x + width - thickness, y + thickness, thickness, height - thickness * 2.0F, color);
      }
   }

   public static void handleFloat(float x, float y, float width, float height, float thickness, int color, float radius) {
      handleFloat2(x, y, width, height, thickness, color);
   }

   public static void handleFloat(
      float x, float y, float width, float height, float thickness, int color, float topLeft, float topRight, float bottomRight, float bottomLeft
   ) {
      handleFloat2(x, y, width, height, thickness, color);
   }

   public static void handleVal(Identifier id, float x, float y, float width, float height, int color) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && id != null) {
         Render.myazem.handleClass_2960(class_4587, x, y, width, height, 0.0F, color, 0.0F, 0.0F, 1.0F, 1.0F, id);
      }
   }
}
