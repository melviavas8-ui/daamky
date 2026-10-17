package dev.daamky.client.internal;

public final class IntCodec73 {
   public long key0 = 62968L;
   public long key1 = 42076L;
   public int key2 = 736;

   public long decode0() {
      return this.key0 + 609L;
   }

   public int decode1() {
      return (int)this.key1 ^ 62;
   }

   public int decode2() {
      return this.key2 ^ 6;
   }

   public static int encode7(int var0) {
      return var0 * var0 + 30;
   }
}
