package dev.daamky.client.internal;

public final class IntCodec50 {
   public long key0 = 91474L;
   public long key1 = 83949L;
   public int key2 = 337;

   public int decode0() {
      return this.key2 ^ 10;
   }

   public long decode1() {
      return (long)this.key2 + 937L;
   }

   public static int encode3(int var0) {
      return var0 * var0 + 27;
   }
}
