package dev.daamky.client.internal;

import java.util.List;

public class ListenersHelper {
   public static final int mfuot = 8;

   public static Callback8 computeRemove(List<Callback8> listeners, Callback8 listener) {
      int var2 = listener.getValue();

      for (int var3 = 0; var3 < listeners.size(); var3++) {
         Callback8 var4 = (Callback8)listeners.get(var3);
         if (var4 == listener) {
            return (Callback8)listeners.remove(var3);
         }

         if (var4.getValue() < var2) {
            break;
         }
      }

      return null;
   }

   public static boolean computeNext(List<Callback8> listeners, Callback8 listener) {
      int var2 = listener.getValue();

      for (Callback8 var4 : listeners) {
         if (var4 == listener) {
            return true;
         }

         if (var4.getValue() < var2) {
            break;
         }
      }

      return false;
   }

   public static void handleAdd(List<Callback8> listeners, Callback8 listener) {
      int var2 = listeners.size() < 8 ? computeSize(listeners, listener) : computeCompare(listeners, listener);
      listeners.add(var2, listener);
   }

   public static int computeSize(List<Callback8> listeners, Callback8 listener) {
      int var2 = listeners.size();

      for (int var3 = 0; var3 < var2; var3++) {
         if (listener.getValue() > ((Callback8)listeners.get(var3)).getValue()) {
            return var3;
         }
      }

      return var2;
   }

   public static int computeCompare(List<Callback8> listeners, Callback8 listener) {
      int var2 = 0;
      int var3 = listeners.size() - 1;

      while (var2 <= var3) {
         int var4 = var2 + (var3 - var2) / 2;
         Callback8 var5 = (Callback8)listeners.get(var4);
         int var6 = Integer.compare(listener.getValue(), var5.getValue());
         if (var6 == 0) {
            return var4;
         }

         if (var6 < 0) {
            var2 = var4 + 1;
         } else {
            var3 = var4 - 1;
         }
      }

      return var2;
   }
}
