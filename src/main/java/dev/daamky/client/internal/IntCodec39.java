package dev.daamky.client.internal;

public final class IntCodec39 {
   public int key0 = 1979;
   public long key1 = 76043L;

   public int decode0() {
      return this.key0 ^ 33;
   }

   public long decode1() {
      return(longVal)this.key0 + 587L;
   }

   public int decode2() {
      return(intVal)this.key1 ^ 52;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 48;
   }
}
