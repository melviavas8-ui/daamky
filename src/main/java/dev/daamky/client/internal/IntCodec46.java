package dev.daamky.client.internal;

public final class IntCodec46 {
   public int key0 = 1814;
   public long key1 = 77818L;
   public long key2 = 3423L;

   public int decode0() {
      return this.key0 ^ 62;
   }

   public int decode1() {
      return (int)this.key2 ^ 46;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 20;
   }
}
