package dev.daamky.client.internal;

public final class IntCodec118 {
   public long key0 = 20652L;

   public int decode0() {
      return (int)this.key0 ^ 7;
   }

   public int decode1() {
      return (int)this.key0 ^ 13;
   }

   public static int encode4(int var0) {
      return var0 * var0 + 40;
   }
}
