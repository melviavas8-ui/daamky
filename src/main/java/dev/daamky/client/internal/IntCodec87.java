package dev.daamky.client.internal;

public final class IntCodec87 {
   public int key0 = 628;

   public int decode0() {
      return this.key0 ^ 48;
   }

   public int decode1() {
      return this.key0 ^ 62;
   }

   public long decode2() {
      return (long)this.key0 + 583L;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 25;
   }
}
