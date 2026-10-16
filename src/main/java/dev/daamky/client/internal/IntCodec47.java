package dev.daamky.client.internal;

public final class IntCodec47 {
   public long key0 = 53568L;

   public long decode0() {
      return this.key0 + 73L;
   }

   public int decode1() {
      return(intVal)this.key0 ^ 29;
   }

   public int decode2() {
      return(intVal)this.key0 ^ 6;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 48;
   }
}
