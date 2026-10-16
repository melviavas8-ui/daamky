package dev.daamky.client.internal;

public final class IntCodec140 {
   public int key0 = 946;
   public int key1 = 632;
   public long key2 = 61352L;

   public int decode0() {
      return this.key1 ^ 49;
   }

   public long decode1() {
      return this.key2 + 40L;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 13;
   }
}
