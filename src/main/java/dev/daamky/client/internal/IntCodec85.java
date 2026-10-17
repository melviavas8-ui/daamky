package dev.daamky.client.internal;

public final class IntCodec85 {
   public int key0 = 158;
   public long key1 = 3988L;

   public long decode0() {
      return (long)this.key0 + 62L;
   }

   public long decode1() {
      return this.key1 + 393L;
   }

   public int decode2() {
      return (int)this.key1 ^ 37;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 25;
   }
}
