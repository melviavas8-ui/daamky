package dev.daamky.client.internal;

public final class IntCodec15 {
   public int key0 = 372;
   public int key1 = 1423;
   public int key2 = 1303;

   public long decode0() {
      return (long)this.key2 + 662L;
   }

   public int decode1() {
      return this.key0 ^ 25;
   }

   public long decode2() {
      return (long)this.key1 + 40L;
   }

   public static int encode3(int var0) {
      return var0 * var0 + 27;
   }
}
