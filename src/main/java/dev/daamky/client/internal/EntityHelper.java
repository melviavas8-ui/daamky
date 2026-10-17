package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public final class EntityHelper implements MinecraftAccess {
   public static final NextInt mfuhepw = new NextInt();

   public static double computeDouble(Entity entity) {
      return Math.hypot(entity.prevX - entity.getX(), entity.prevZ - entity.getZ()) * 20.0;
   }

   public static double computeMax(Vec3d position, double size) {
      Camera var3 = mc.getEntityRenderDispatcher().camera;
      double var4 = var3.getPos().distanceTo(position);
      if (var4 < 1.0E-4) {
         var4 = 1.0E-4;
      }

      double var6 = (double)mc.gameRenderer.getFov(var3, mc.getRenderTickCounter().getTickDelta(false), true);
      return Math.max(10.0, 1000.0 / var4) * (size / 30.0) / (var6 == 70.0 ? 1.0 : var6 / 70.0);
   }

   public static float computeFloat(double oldValue, double newValue) {
      return (float)(oldValue + (newValue - oldValue) * (double)mc.getRenderTickCounter().getTickDelta(false));
   }

   public static double computeDouble(double start, double end, double delta) {
      return start + (end - start) * delta;
   }

   public static float computeFloat(float start, float end, float delta) {
      return start + (end - start) * delta;
   }

   public static Color initBlue(Color color1, Color color2, float amount) {
      if (amount <= 0.0F) {
         return color1;
      } else if (amount >= 1.0F) {
         return color2;
      } else {
         int var3 = (int)computeFloat((float)color1.getRed(), (float)color2.getRed(), amount);
         int var4 = (int)computeFloat((float)color1.getGreen(), (float)color2.getGreen(), amount);
         int var5 = (int)computeFloat((float)color1.getBlue(), (float)color2.getBlue(), amount);
         int var6 = (int)computeFloat((float)color1.getAlpha(), (float)color2.getAlpha(), amount);
         return new Color(var3, var4, var5, var6);
      }
   }

   public static int computeInt(int oldValue, int newValue, float interpolationValue) {
      return (int)((float)oldValue + (float)(newValue - oldValue) * interpolationValue);
   }

   public static double computeRound(double value, double step) {
      return (double)Math.round(value / step) * step;
   }

   public static float computeRound(float value, float step) {
      return (float)Math.round(value / step) * step;
   }

   public static int computeRandom(int min, int max) {
      return mfuhepw.randomInRange(min, max);
   }

   public static float computeRandom(float min, float max) {
      return mfuhepw.randomInRange(min, max);
   }

   public static double computeRandom(double min, double max) {
      return mfuhepw.randomInRange(min, max);
   }

   public EntityHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
