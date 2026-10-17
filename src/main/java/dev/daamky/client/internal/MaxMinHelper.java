package dev.daamky.client.internal;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class MaxMinHelper implements RenderAccess {
   public static final double[] doubles = new double[65536];
   public static float[] floats;
   public static float[] moxucu;
   public static long timeStamp = 0L;
   public static double doubleVal = Math.PI * 2;
   public static final Random random = new Random();
   public static final Matrix4f matrix4f = new Matrix4f();
   public static final Matrix4f muokcax = new Matrix4f();
   public static final Matrix4f matrix4f2 = new Matrix4f();

   public static int computeFloor(int x, int n) {
      return n * (int)Math.floor((double)x / (double)n);
   }

   public static double getMc() {
      return mc.getCurrentFps() > 0 ? 1.0 / (double)mc.getCurrentFps() : 1.0;
   }

   public static float initInit(float value, float minInput, float maxInput, float minOutput, float maxOutput) {
      if (maxInput - minInput == 0.0F) {
         throw new IllegalArgumentException("Диапазон входных значений не может быть равен нулю.");
      } else {
         float var5 = (maxInput - value) / (maxInput - minInput) * (maxOutput - minOutput) + minOutput;
         return Math.max(minOutput, Math.min(maxOutput, var5));
      }
   }

   public static float computeCurrent(float min, float max, float smoothness) {
      if (floats == null) {
         floats = new float[]{(min + max) / 2.0F};
         moxucu = new float[]{computeCurrent(min, max)};
      }

      long var3 = System.currentTimeMillis();
      if ((double)(var3 - longVal) > 250.0 + Math.random() * 300.0) {
         moxucu[0] = computeCurrent(min, max);
         longVal = var3;
      }

      floats[0] = floats[0] + (moxucu[0] - floats[0]) * smoothness;
      return floats[0];
   }

   public static Vec3d initCos(int i, int size, double width) {
      int var4 = Math.min(i, size);
      float var5 = (float)(Math.cos((double)var4 * doubleVal / (double)size) * width);
      float var6 = (float)(-Math.sin((double)var4 * doubleVal / (double)size) * width);
      return new Vec3d((double)var5, 0.0, (double)var6);
   }

   public static float computeFloat(float step, float input, float target) {
      return input + step * MathHelper.wrapDegrees(target - input);
   }

   public static double computePow(double value) {
      value = (value > 0.5 ? 1.0 - value : value) * 2.0;
      value = value < 0.5 ? 2.0 * value * value : 1.0 - Math.pow(-2.0 * value + 2.0, 2.0) / 2.0;
      return value > 1.0 ? 1.0 : (value < 0.0 ? 0.0 : value);
   }

   public static float computeFloat(float p_76142_0_) {
      if ((p_76142_0_ = p_76142_0_ % 360.0F) >= 180.0F) {
         p_76142_0_ -= 360.0F;
      }

      if (p_76142_0_ < -180.0F) {
         p_76142_0_ += 360.0F;
      }

      return p_76142_0_;
   }

   public static double computeDouble(double radians) {
      int var2 = (int)(radians * 10430.378350470453) & 65535;
      return doubles[var2];
   }

   public static double computeDouble2(double radians) {
      int var2 = (int)(radians * 10430.378350470453 + 16384.0) & 65535;
      return doubles[var2];
   }

   public static float computeRandom(float min, float max) {
      return (float)(Math.random() * (double)(max - min) + (double)min);
   }

   public static float computeRandom(double min, double max) {
      return (float)(min + (max - min) * Math.random());
   }

   public static float computeCurrent(double min, double max, double time) {
      return (float)(Math.sin((double)System.currentTimeMillis() / time) * (double)initInit((float)min, (float)max));
   }

   public static float computeCos(double min, double max, double time) {
      return (float)(Math.cos((double)System.currentTimeMillis() / time) * (double)initInit((float)min, (float)max));
   }

   public static float computeGaussian(double min, double max) {
      double var4 = (min + max) * 0.5;
      double var6 = (max - min) / 6.0;
      double var8 = var4 + random.nextGaussian() * var6;
      return (float)Math.max(min, Math.min(max, var8));
   }

   public static float computeCurrent2(double min, double max, double time) {
      double var6 = (min + max) * 0.5;
      double var8 = (max - min) / 6.0;
      double var10 = random.nextGaussian() * var8;
      double var12 = Math.sin((double)System.currentTimeMillis() / time);
      double var14 = var6 + var10 * var12;
      return (float)Math.max(min, Math.min(max, var14));
   }

   public static double computeDouble(double delta, double start, double end) {
      return start + delta * (end - start);
   }

   public static float computeFloat2(float delta, float start, float end) {
      return start + delta * (end - start);
   }

   public static float computeFloat3(float end, float start, float multiple) {
      return (1.0F - MathHelper.clamp((float)(getMc() * (double)multiple), 0.0F, 1.0F)) * end
         + MathHelper.clamp((float)(getMc() * (double)multiple), 0.0F, 1.0F) * start;
   }

   public static double computeMax(double min, double max, double value) {
      return Math.max(min, Math.min(max, value));
   }

   public static float initInit(float min, float max) {
      return Lerp.initRound(max, min, (double)new SecureRandom().nextFloat());
   }

   public static float computeFloat(double oldValue, double newValue, double interpolationValue) {
      return (float)(oldValue + (newValue - oldValue) * interpolationValue);
   }

   public static Vec3d initInit(Vec3d prevPos, Vec3d pos) {
      return new Vec3d(
         computeDouble(prevPos.x, pos.x),
         computeDouble(prevPos.y, pos.y),
         computeDouble(prevPos.z, pos.z)
      );
   }

   public static double computeDouble(double prev, double orig) {
      return computeDouble((double)mc.getRenderTickCounter().getTickDelta(false), prev, orig);
   }

   public static double computeMax2(double value, double min, double max) {
      return Math.max(min, Math.min(max, value));
   }

   public static float computeFloat4(float value, float min, float max) {
      if (value < min) {
         return min;
      } else {
         return value > max ? max : value;
      }
   }

   public static int computeRandom(int min, int max) {
      return min + (int)(Math.random() * (double)(max - min + 1));
   }

   public static double computeCurrent(double min, double max) {
      initInit(min, max);
      return min + ThreadLocalRandom.current().nextDouble() * (max - min);
   }

   public static float computeCurrent(float min, float max) {
      initInit((double)min, (double)max);
      return min + ThreadLocalRandom.current().nextFloat() * (max - min);
   }

   public static void initInit(double min, double max) {
      if (max < min) {
         throw new IllegalArgumentException("max не может быть меньше min.");
      }
   }

   public static float computeCurrent(float textWidth) {
      short var1 = 6000;
      return (float)MathHelper.clamp((double)(System.currentTimeMillis() % (long)var1) * Math.PI / (double)var1, 0.0, 1.0) * textWidth;
   }

   public static int computeInt(int value, int min) {
      return value < min ? min : value;
   }

   public static float computeFloat(float value, float min) {
      return value < min ? min : value;
   }

   public static double computeDouble2(double value, double min) {
      return value < min ? min : value;
   }

   public static boolean computeFlag(int mouseX, int mouseY, float x, float y, float width, float height) {
      return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x + width && (float)mouseY <= y + height;
   }

   public static boolean computeFlag(float mouseX, float mouseY, float x, float y, float width, float height) {
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
   }

   public static float computeRound(float value, float step) {
      return (float)Math.round(value / step) * step;
   }

   public static double computePow(double value, int increment) {
      double var3 = Math.pow(10.0, (double)increment);
      return (double)Math.round(value * var3) / var3;
   }

   public static float computePow(float value, int decimals) {
      float var2 = (float)Math.pow(10.0, (double)decimals);
      return (float)Math.round(value * var2) / var2;
   }

   public static float computeRound2(float value, float increment) {
      return (float)Math.round(value / increment) * increment;
   }

   public static double getDouble() {
      return mc.getWindow().getScaleFactor();
   }

      public MaxMinHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      for (int var0 = 0; var0 < 65536; var0++) {
         doubles[var0] = Math.sin((double)var0 * (Math.PI * 2) / 65536.0);
      }
   }
}
