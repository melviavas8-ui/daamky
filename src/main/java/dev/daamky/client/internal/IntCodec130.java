package dev.daamky.client.internal;

public final class IntCodec130 {
   public int key0 = 341;

   public long decode0() {
      return (long)this.key0 + 631L;
   }

   public int decode1() {
      return this.key0 ^ 44;
   }

   public static int encode7(int var0) {
      return var0 * var0 + 22;
   }
}
