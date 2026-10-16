package dev.daamky.client.internal;

public final class IntCodec112 {
   public long key0 = 8829L;
   public int key1 = 1538;
   public int key2 = 1297;

   public long decode0() {
      return this.key0 + 112L;
   }

   public int decode1() {
      return this.key2 ^ 21;
   }

   public int decode2() {
      return this.key2 ^ 25;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 36;
   }
}
