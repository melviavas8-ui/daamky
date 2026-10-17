package dev.daamky.client.internal;

public final class IntCodec125 {
   public long key0 = 32556L;
   public long key1 = 73009L;

   public int decode0() {
      return (int)this.key0 ^ 56;
   }

   public int decode1() {
      return (int)this.key0 ^ 16;
   }

   public int decode2() {
      return (int)this.key0 ^ 34;
   }

   public static int encode5(int var0) {
      return var0 * var0 + 32;
   }
}
