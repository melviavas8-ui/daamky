package dev.daamky.client.internal;

public final class IntCodec104 {
   public int key0 = 1729;
   public int key1 = 602;
   public long key2 = 51030L;

   public long decode0() {
      return this.key2 + 627L;
   }

   public int decode1() {
      return(intVal)this.key2 ^ 46;
   }

   public long decode2() {
      return(longVal)this.key0 + 884L;
   }

   public static int encode4(int var0) {
      return var0 * var0 + 40;
   }
}
