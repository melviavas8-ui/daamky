package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;

public final class Geometry implements MinecraftAccess {
   public static float getMc() {
      float var0 = ((Double)mc.options.getMouseSensitivity().getValue()).floatValue() * 0.6F + 0.2F;
      float var1 = var0 * var0 * var0 * 8.0F;
      return var1 * 0.15F;
   }

   public static boolean computeFlag(float mouseX, float mouseY, float x, float y, float width, float height) {
      return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
   }

   public static boolean computeFlag(double mouseX, double mouseY, double x, double y, double width, double height) {
      return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
   }

   public static boolean computeMax(float mouseX, float mouseY, float x, float y, float width, float height, float round) {
      if (mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height) {
         return false;
      } else if (round <= 0.0F) {
         return true;
      } else {
         float var9 = Math.max(x + round, Math.min(mouseX, x + width - round));
         float var10 = mouseX - var9;
         float var8;
         float var11 = var10 * var10 + (var8 = mouseY - Math.max(y + round, Math.min(mouseY, y + height - round))) * var8;
         return var11 <= round * round;
      }
   }

   public static boolean computeMax(double mouseX, double mouseY, double x, double y, double width, double height, double round) {
      if (mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height) {
         return false;
      } else if (round <= 0.0) {
         return true;
      } else {
         double var18 = Math.max(x + round, Math.min(mouseX, x + width - round));
         double var20 = mouseX - var18;
         double var16;
         double var22 = var20 * var20 + (var16 = mouseY - Math.max(y + round, Math.min(mouseY, y + height - round))) * var16;
         return var22 <= round * round;
      }
   }

   public Geometry() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
