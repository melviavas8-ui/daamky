package dev.daamky.client.internal;

public final class IntCodec71 {
   public long key0 = 87584L;
   public long key1 = 34379L;

   public int decode0() {
      return(intVal)this.key1 ^ 6;
   }

   public int decode1() {
      return(intVal)this.key0 ^ 33;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 28;
   }
}
