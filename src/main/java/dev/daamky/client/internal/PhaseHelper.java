package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;

public final class PhaseHelper {
   public static PhaseHelper.Phase initInit(int count, long periodMs, long transMs) {
      if (count <= 0) {
         return new PhaseHelper.Phase(0, 0, 0.0F);
      } else if (count == 1) {
         return new PhaseHelper.Phase(0, 0, 0.0F);
      } else {
         long var5 = System.currentTimeMillis();
         int var7 = (int)(var5 / periodMs % (long)count);
         int var8 = (var7 + 1) % count;
         long var9 = var5 % periodMs;
         float var11 = 0.0F;
         if (var9 > periodMs - transMs) {
            var11 = (float)(var9 - (periodMs - transMs)) / (float)transMs;
            var11 = Easing.QUINT_OUT.apply(var11);
         }

         return new PhaseHelper.Phase(var7, var8, var11);
      }
   }

   public static record Phase(int index, int next, float k) {
   }
}
