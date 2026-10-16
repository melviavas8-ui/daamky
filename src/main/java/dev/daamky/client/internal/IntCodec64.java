package dev.daamky.client.internal;

public final class IntCodec64 {
   public int key0 = 319;
   public int key1 = 543;

   public int decode0() {
      return this.key0 ^ 25;
   }

   public int decode1() {
      return this.key1 ^ 57;
   }

   public int decode2() {
      return this.key0 ^ 60;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 35;
   }
}
