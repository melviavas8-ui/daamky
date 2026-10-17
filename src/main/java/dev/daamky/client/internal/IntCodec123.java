package dev.daamky.client.internal;

public final class IntCodec123 {
   public long key0 = 66210L;

   public int decode0() {
      return (int)this.key0 ^ 9;
   }

   public int decode1() {
      return (int)this.key0 ^ 57;
   }

   public static int encode1(int var0) {
      return var0 * var0 + 22;
   }
}
