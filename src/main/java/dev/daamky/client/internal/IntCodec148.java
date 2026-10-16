package dev.daamky.client.internal;

public final class IntCodec148 {
   public long key0 = 31964L;
   public long key1 = 64351L;

   public int decode0() {
      return(intVal)this.key0 ^ 42;
   }

   public long decode1() {
      return this.key1 + 928L;
   }

   public static int encode0(int var0) {
      return var0 * var0 + 10;
   }
}
