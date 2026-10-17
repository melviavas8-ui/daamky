package dev.daamky.client.internal;

public final class IntCodec2 {
   public long key0 = 71605L;
   public int key1 = 1397;

   public int decode0() {
      return (int)this.key0 ^ 34;
   }

   public int decode1() {
      return (int)this.key0 ^ 16;
   }

   public long decode2() {
      return (long)this.key1 + 411L;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 47;
   }
}
