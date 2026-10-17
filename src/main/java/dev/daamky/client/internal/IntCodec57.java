package dev.daamky.client.internal;

public final class IntCodec57 {
   public long key0 = 1293L;
   public long key1 = 23508L;

   public int decode0() {
      return (int)this.key0 ^ 29;
   }

   public int decode1() {
      return (int)this.key1 ^ 21;
   }

   public long decode2() {
      return this.key0 + 175L;
   }

   public static int encode7(int var0) {
      return var0 * var0 + 34;
   }
}
