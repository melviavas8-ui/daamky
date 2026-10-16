package dev.daamky.client.internal;

public final class IntCodec96 {
   public long key0 = 20L;
   public long key1 = 57518L;

   public long decode0() {
      return this.key0 + 714L;
   }

   public int decode1() {
      return(intVal)this.key0 ^ 21;
   }

   public long decode2() {
      return this.key1 + 237L;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 22;
   }
}
