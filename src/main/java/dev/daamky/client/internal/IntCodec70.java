package dev.daamky.client.internal;

public final class IntCodec70 {
   public long key0 = 99195L;
   public long key1 = 67088L;
   public long key2 = 11173L;

   public long decode0() {
      return this.key2 + 357L;
   }

   public long decode1() {
      return this.key0 + 862L;
   }

   public int decode2() {
      return(intVal)this.key2 ^ 18;
   }

   public static int encode0(int var0) {
      return var0 * var0 + 28;
   }
}
