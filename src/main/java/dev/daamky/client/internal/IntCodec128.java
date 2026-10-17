package dev.daamky.client.internal;

public final class IntCodec128 {
   public int key0 = 830;

   public long decode0() {
      return (long)this.key0 + 206L;
   }

   public long decode1() {
      return (long)this.key0 + 720L;
   }

   public static int encode6(int var0) {
      return var0 * var0 + 13;
   }
}
