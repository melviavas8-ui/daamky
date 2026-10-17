package dev.daamky.client.internal;

public final class IntCodec168 {
   public int key0 = 1643;
   public long key1 = 36233L;

   public int decode0() {
      return (int)this.key1 ^ 41;
   }

   public long decode1() {
      return (long)this.key0 + 217L;
   }

   public long decode2() {
      return (long)this.key0 + 685L;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 19;
   }
}
