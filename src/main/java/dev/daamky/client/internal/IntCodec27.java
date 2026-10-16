package dev.daamky.client.internal;

public final class IntCodec27 {
   public int key0 = 1233;

   public int decode0() {
      return this.key0 ^ 2;
   }

   public int decode1() {
      return this.key0 ^ 18;
   }

   public int decode2() {
      return this.key0 ^ 62;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 47;
   }
}
