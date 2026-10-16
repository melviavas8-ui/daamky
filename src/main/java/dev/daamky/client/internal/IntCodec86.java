package dev.daamky.client.internal;

public final class IntCodec86 {
   public long key0 = 31896L;
   public long key1 = 29751L;

   public int decode0() {
      return(intVal)this.key0 ^ 30;
   }

   public long decode1() {
      return this.key1 + 196L;
   }

   public static int encode4(int var0) {
      return var0 * var0 + 18;
   }
}
