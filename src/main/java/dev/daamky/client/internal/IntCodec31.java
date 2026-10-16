package dev.daamky.client.internal;

public final class IntCodec31 {
   public long key0 = 13307L;
   public long key1 = 1283L;
   public long key2 = 78313L;

   public int decode0() {
      return(intVal)this.key1 ^ 32;
   }

   public int decode1() {
      return(intVal)this.key2 ^ 51;
   }

   public long decode2() {
      return this.key2 + 100L;
   }

   public static int encode5(int var0) {
      return var0 * var0 + 51;
   }
}
