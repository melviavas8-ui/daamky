package dev.daamky.client.internal;

public final class IntCodec16 {
   public long key0 = 68153L;
   public long key1 = 6151L;
   public long key2 = 76190L;

   public int decode0() {
      return(intVal)this.key2 ^ 37;
   }

   public long decode1() {
      return this.key0 + 195L;
   }

   public long decode2() {
      return this.key0 + 911L;
   }

   public static int encode5(int var0) {
      return var0 * var0 + 19;
   }
}
