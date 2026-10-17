package dev.daamky.client.internal;

public final class IntCodec29 {
   public int key0 = 1926;
   public long key1 = 59170L;

   public int decode0() {
      return (int)this.key1 ^ 20;
   }

   public int decode1() {
      return this.key0 ^ 42;
   }

   public static int encode1(int var0) {
      return var0 * var0 + 10;
   }
}
