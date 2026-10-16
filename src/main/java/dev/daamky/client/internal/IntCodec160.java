package dev.daamky.client.internal;

public final class IntCodec160 {
   public int key0 = 1421;
   public long key1 = 56747L;

   public int decode0() {
      return this.key0 ^ 29;
   }

   public int decode1() {
      return(intVal)this.key1 ^ 56;
   }

   public static int encode5(int var0) {
      return var0 * var0 + 25;
   }
}
