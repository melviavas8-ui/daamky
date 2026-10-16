package dev.daamky.client.internal;

public final class IntCodec33 {
   public long key0 = 12899L;

   public int decode0() {
      return(intVal)this.key0 ^ 48;
   }

   public long decode1() {
      return this.key0 + 567L;
   }

   public int decode2() {
      return(intVal)this.key0 ^ 36;
   }

   public static int encode0(int var0) {
      return var0 * var0 + 29;
   }
}
