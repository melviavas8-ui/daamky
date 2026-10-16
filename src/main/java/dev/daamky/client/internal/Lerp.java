package dev.daamky.client.internal;

public class Lerp {
   public static <T extends Number> T initRound(T input, T target, double step) {
      double var4 = input.doubleValue();
      double var6 = target.doubleValue();
      double var8 = var4 + step * (var6 - var4);
      if (input instanceof Integer) {
         return (T)(int)Math.round(var8);
      } else if (input instanceof Double) {
         return (T)var8;
      } else if (input instanceof Float) {
         return (T)(float)var8;
      } else if (input instanceof Long) {
         return (T)Math.round(var8);
      } else if (input instanceof Short) {
         return (T)(short)((int)Math.round(var8));
      } else if (input instanceof Byte) {
         return (T)(byte)((int)Math.round(var8));
      } else {
         throw new IllegalArgumentException("Unsupported type: " + input.getClass().getSimpleName());
      }
   }
}
