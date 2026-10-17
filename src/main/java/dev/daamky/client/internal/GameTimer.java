package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;

public final class GameTimer implements MinecraftAccess {
   public static float getMc2() {
      return getMc();
   }

   public static float getMc() {
      float var0 = (float)((double)((Double)mc.options.getMouseSensitivity().getValue()).floatValue() * 0.6 + 0.2);
      return var0 * var0 * var0 * 1.2F;
   }

   public static float computeRound(float delta) {
      return (float)Math.round(delta / getMc2());
   }

   public static float computeRound2(float value) {
      float var1 = getMc2();
      return (float)Math.round(value / var1) * var1;
   }

   public GameTimer() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
