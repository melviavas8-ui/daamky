package dev.daamky.client.internal;

public final class IntCodec65 {
   public long key0 = 90051L;
   public int key1 = 1853;

   public int decode0() {
      return this.key1 ^ 58;
   }

   public static int encode0(int var0) {
      return var0 * var0 + 44;
   }
}
