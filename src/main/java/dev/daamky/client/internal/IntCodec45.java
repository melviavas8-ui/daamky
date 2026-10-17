package dev.daamky.client.internal;

public final class IntCodec45 {
   public int key0 = 696;
   public long key1 = 72510L;

   public int decode0() {
      return (int)this.key1 ^ 26;
   }

   public int decode1() {
      return this.key0 ^ 4;
   }

   public long decode2() {
      return this.key1 + 974L;
   }

   public static int encode4(int var0) {
      return var0 * var0 + 42;
   }
}
