package dev.daamky.client.internal;

public final class IntCodec113 {
   public long key0 = 18610L;
   public long key1 = 98076L;

   public int decode0() {
      return(intVal)this.key0 ^ 6;
   }

   public int decode1() {
      return(intVal)this.key0 ^ 15;
   }

   public long decode2() {
      return this.key1 + 402L;
   }

   public static int encode4(int var0) {
      return var0 * var0 + 41;
   }
}
