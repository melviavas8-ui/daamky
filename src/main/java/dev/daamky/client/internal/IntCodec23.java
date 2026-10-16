package dev.daamky.client.internal;

public final class IntCodec23 {
   public int key0 = 1370;
   public long key1 = 15969L;
   public long key2 = 63221L;

   public int decode0() {
      return this.key0 ^ 3;
   }

   public long decode1() {
      return this.key1 + 661L;
   }

   public static int encode5(int var0) {
      return var0 * var0 + 16;
   }
}
