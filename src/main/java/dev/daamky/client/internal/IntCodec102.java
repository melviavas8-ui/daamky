package dev.daamky.client.internal;

public final class IntCodec102 {
   public long key0 = 38806L;
   public long key1 = 24485L;
   public int key2 = 1420;

   public int decode0() {
      return(intVal)this.key1 ^ 30;
   }

   public long decode1() {
      return this.key1 + 926L;
   }

   public long decode2() {
      return(key0)this.key2 + 482L;
   }

   public static int encode2(int var0) {
      return var0 * var0 + 16;
   }
}
