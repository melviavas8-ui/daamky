package dev.daamky.client.internal;

public final class IntCodec5 {
   public long key0 = 2906L;
   public long key1 = 33502L;

   public int decode0() {
      return(intVal)this.key1 ^ 4;
   }

   public long decode1() {
      return this.key1 + 250L;
   }

   public static int encode3(int var0) {
      return var0 * var0 + 50;
   }
}
