package dev.daamky.client.internal;

import net.minecraft.util.Identifier;

public final class RenderKit implements RenderAccess {
      public RenderKit() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static final class Blur {
      public static void handleFloat(float x, float y, float width, float height, float alpha, int tintColor) {
         TintColorHelper.handleFloat(x, y, width, height, alpha, tintColor);
      }

      public static void handleFloat(float x, float y, float width, float height, float alpha, float cornerRadius, int tintColor) {
         TintColorHelper.handleFloat(x, y, width, height, alpha, cornerRadius, tintColor);
      }

      public static void handleFloat(
         float x, float y, float width, float height, float alpha, float topLeft, float topRight, float bottomRight, float bottomLeft, int tintColor
      ) {
         TintColorHelper.handleMax(x, y, width, height, alpha, topLeft, topRight, bottomRight, bottomLeft, tintColor);
      }

      public static void handleFloat(
         float x, float y, float width, float height, float alpha, float radius, int tintColor, float distortion, float waveSize, float edgeLight, float shine
      ) {
         TintColorHelper.handleFloat(x, y, width, height, alpha, radius, tintColor);
      }

      public static void handleFloat(float x, float y, float width, float height, float alpha, float radius, float distortion, float waveSize) {
         TintColorHelper.handleFloat(x, y, width, height, alpha, radius, 0);
      }

      public static void handleFloat(
         float x,
         float y,
         float width,
         float height,
         float alpha,
         float topLeft,
         float topRight,
         float bottomRight,
         float bottomLeft,
         int tintColor,
         float distortion,
         float waveSize,
         float edgeLight,
         float shine
      ) {
         TintColorHelper.handleFloat(x, y, width, height, alpha, topLeft, topRight, bottomRight, bottomLeft, tintColor);
      }

            public Blur() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }
   }

   public static final class Images {
      public static void handleVal(Identifier id, float x, float y, float width, float height, int color) {
         TintColorHelper.handleVal(id, x, y, width, height, color);
      }

      public static void handleVal(Identifier id, float x, float y, float width, float height, float smoothness, int color) {
         TintColorHelper.handleVal(id, x, y, width, height, color);
      }

      public static void handleVal(Identifier id, float x, float y, float width, float height, float smoothness, float radius, int color) {
         TintColorHelper.handleVal(id, x, y, width, height, color);
      }

            public Images() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }
   }

   public static final class Render2D {
      public static void handleFloat(float x, float y, float width, float height, float thickness, int color, float radius) {
         TintColorHelper.handleFloat(x, y, width, height, thickness, color, radius);
      }

      public static void handleFloat(
         float x, float y, float width, float height, float thickness, int color, float topLeft, float topRight, float bottomRight, float bottomLeft
      ) {
         TintColorHelper.handleFloat(x, y, width, height, thickness, color, topLeft, topRight, bottomRight, bottomLeft);
      }

      public static void handleFloat(float x, float y, float width, float height, int color) {
         TintColorHelper.handleFloat(x, y, width, height, color);
      }

      public static void handleFloat(float x, float y, float width, float height, int color, float radius) {
         TintColorHelper.handleFloat(x, y, width, height, color, radius);
      }

      public static void handleFloat(float x, float y, float width, float height, int color, float topLeft, float topRight, float bottomRight, float bottomLeft) {
         TintColorHelper.handleFloat(x, y, width, height, color, topLeft, topRight, bottomRight, bottomLeft);
      }

      public static void handleFloat(float x, float y, float width, float height, int color, float radius, float glowSize, float strength) {
         TintColorHelper.handleFloat(x, y, width, height, color, radius, glowSize, strength);
      }

      public static void handleFloat(float x, float y, float width, float height, int[] colors, float radius) {
         TintColorHelper.handleFloat(x, y, width, height, colors, radius);
      }

      public static void handleFloat(
         float x, float y, float width, float height, int[] colors, float topLeft, float topRight, float bottomRight, float bottomLeft
      ) {
         TintColorHelper.handleFloat(x, y, width, height, colors, topLeft, topRight, bottomRight, bottomLeft);
      }

      public static int computeMin(float alpha) {
         return GlyphMap.computeRound(255, Math.min(alpha, 1.0F));
      }

            public Render2D() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }
   }
}
