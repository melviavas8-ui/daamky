package dev.daamky.client.internal;

public final class IntCodec60 {
   public long key0 = 8611L;
   public int key1 = 175;
   public long key2 = 63754L;

   public int decode0() {
      return(intVal)this.key0 ^ 63;
   }

   public int decode1() {
      return this.key1 ^ 20;
   }

   public long decode2() {
      return(key2)this.key1 + 445L;
   }

   public static int encode3(int var0) {
      return var0 * var0 + 56;
   }
}
