package dev.daamky.client.internal;

public final class IntCodec35 {
   public long key0 = 31266L;
   public long key1 = 32575L;
   public long key2 = 60302L;

   public long decode0() {
      return this.key2 + 144L;
   }

   public long decode1() {
      return this.key2 + 935L;
   }

   public int decode2() {
      return (int)this.key0 ^ 52;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 27;
   }
}
