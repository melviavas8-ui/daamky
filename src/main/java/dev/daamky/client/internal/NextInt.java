package dev.daamky.client.internal;

import java.util.Random;

public class NextInt extends Random {
   public long seed;

   public NextInt(long seed) {
      this.seed = seed;
   }

   public NextInt() {
      this(System.nanoTime());
   }

   public int randomInRange(int min, int max) {
      return min != max && min <= max ? this.nextInt(max - min + 1) + min : min;
   }

   public float randomInRange(float min, float max) {
      return min + this.nextFloat() * (max - min);
   }

   public double randomInRange(double min, double max) {
      return min + this.nextDouble() * (max - min);
   }

   @Override
   public int next(int bits) {
      this.seed = this.seed ^ this.seed << 13;
      this.seed = this.seed ^ this.seed >>> 17;
      this.seed = this.seed ^ this.seed << 5;
      return (int)(this.seed & (1L << bits) - 1L);
   }
}
