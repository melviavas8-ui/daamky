package dev.daamky.client.internal;

public final class IntCodec49 {
   public long key0 = 35195L;
   public long key1 = 60676L;

   public int decode0() {
      return(intVal)this.key0 ^ 33;
   }

   public long decode1() {
      return this.key0 + 33L;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 26;
   }
}
