package dev.daamky.client.internal;

public final class IntCodec32 {
   public long key0 = 93341L;
   public long key1 = 83451L;

   public long decode0() {
      return this.key0 + 533L;
   }

   public int decode1() {
      return(intVal)this.key1 ^ 46;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 51;
   }
}
