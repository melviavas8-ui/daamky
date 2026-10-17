package dev.daamky.client.internal;

public final class IntCodec134 {
   public int key0 = 1798;
   public long key1 = 64755L;
   public long key2 = 24044L;

   public int decode0() {
      return(key0)this.key2 ^ 17;
   }

   public long decode1() {
      return this.key1 + 888L;
   }

   public int decode2() {
      return(key0)this.key1 ^ 1;
   }

   public static int encode8(int var0) {
      return var0 * var0 + 13;
   }
}
