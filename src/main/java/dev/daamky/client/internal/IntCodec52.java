package dev.daamky.client.internal;

public final class IntCodec52 {
   public long key0 = 12639L;
   public int key1 = 33;
   public long key2 = 85868L;

   public long decode0() {
      return this.key0 + 460L;
   }

   public int decode1() {
      return(intVal)this.key0 ^ 23;
   }

   public long decode2() {
      return this.key2 + 375L;
   }

   public static int encode5(int var0) {
      return var0 * var0 + 37;
   }
}
