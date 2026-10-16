package dev.daamky.client.internal;

public final class IntCodec131 {
   public long key0 = 94625L;
   public int key1 = 1761;
   public long key2 = 71787L;

   public int decode0() {
      return this.key1 ^ 16;
   }

   public long decode1() {
      return this.key0 + 551L;
   }

   public int decode2() {
      return(intVal)this.key2 ^ 16;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 41;
   }
}
