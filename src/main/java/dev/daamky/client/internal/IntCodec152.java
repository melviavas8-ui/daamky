package dev.daamky.client.internal;

public final class IntCodec152 {
   public int key0 = 864;
   public long key1 = 85526L;

   public long decode0() {
      return (long)this.key0 + 906L;
   }

   public int decode1() {
      return (int)this.key1 ^ 21;
   }

   public int decode2() {
      return this.key0 ^ 39;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 37;
   }
}
