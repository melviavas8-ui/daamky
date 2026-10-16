package dev.daamky.client.internal;

public final class IntCodec63 {
   public int key0 = 1584;
   public long key1 = 61840L;

   public long decode0() {
      return this.key1 + 512L;
   }

   public long decode1() {
      return this.key1 + 544L;
   }

   public int decode2() {
      return this.key0 ^ 44;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 35;
   }
}
