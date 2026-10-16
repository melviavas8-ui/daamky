package dev.daamky.client.internal;

public final class IntCodec162 {
   public long key0 = 18186L;
   public long key1 = 64702L;
   public long key2 = 11780L;

   public int decode0() {
      return(intVal)this.key2 ^ 7;
   }

   public int decode1() {
      return(intVal)this.key0 ^ 42;
   }

   public static int encode4(int var0) {
      return var0 * var0 + 41;
   }
}
