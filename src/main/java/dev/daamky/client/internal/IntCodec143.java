package dev.daamky.client.internal;

public final class IntCodec143 {
   public long key0 = 36047L;

   public int decode0() {
      return (int)this.key0 ^ 33;
   }

   public long decode1() {
      return this.key0 + 143L;
   }

   public static int encode0(int var0) {
      return var0 * var0 + 34;
   }
}
