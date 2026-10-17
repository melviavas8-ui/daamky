package dev.daamky.client.internal;

public final class IntCodec151 {
   public long key0 = 96229L;
   public int key1 = 79;

   public long decode0() {
      return (long)this.key1 + 216L;
   }

   public int decode1() {
      return (int)this.key0 ^ 24;
   }

   public int decode2() {
      return (int)this.key0 ^ 34;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 36;
   }
}
