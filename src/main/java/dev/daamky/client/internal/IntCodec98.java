package dev.daamky.client.internal;

public final class IntCodec98 {
   public int key0 = 644;
   public int key1 = 1532;

   public int decode0() {
      return this.key0 ^ 37;
   }

   public long decode1() {
      return (long)this.key0 + 491L;
   }

   public int decode2() {
      return this.key1 ^ 48;
   }

   public static int encode3(int var0) {
      return var0 * var0 + 32;
   }
}
