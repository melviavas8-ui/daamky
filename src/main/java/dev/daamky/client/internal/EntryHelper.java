package dev.daamky.client.internal;

import dev.daamky.client.module.movement.InventoryMove;
import dev.daamky.client.util.MinecraftAccess;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class EntryHelper implements MinecraftAccess {
   public static final List<EntryHelper.Entry> list = new ArrayList<>(4);
   public static boolean flag = false;

   public static boolean check() {
      try {
         InventoryMove var0 = InventoryMove.getInstance();
         return var0 != null && var0.checkEnabled();
      } catch (Throwable var1) {
         return false;
      }
   }

   public static void initAdd(long totalMs, long delayMs, Runnable action) {
      long var5 = System.nanoTime();
      long var7 = var5 + delayMs * 1000000L;
      long var9 = var5 + totalMs * 1000000L;
      list.add(new EntryHelper.Entry(var7, var9, action));
      if (!flag) {
         for (KeyBinding var14 : KeyBindingUtil.getMc()) {
            var14.setPressed(false);
         }

         flag = true;
      }
   }

   public static void handleLong(long totalMs, Runnable action) {
      initAdd(totalMs, 1L, action);
   }

   public static void initNext() {
      if (flag || !list.isEmpty()) {
         long var0 = System.nanoTime();
         ArrayList var2 = null;

         for (EntryHelper.Entry var4 : list) {
            if (!var4.flag && var0 >= var4.execNs) {
               if (var2 == null) {
                  var2 = new ArrayList(4);
               }

               var2.add(var4);
            }
         }

         if (var2 != null) {
            for (EntryHelper.Entry var13 : var2) {
               try {
                  if (var13.action != null) {
                     var13.action.run();
                  }
               } catch (Throwable var9) {
                  var9.printStackTrace();
               } finally {
                  var13.flag = true;
               }
            }
         }

         for (int var12 = list.size() - 1; var12 >= 0; var12--) {
            EntryHelper.Entry var14 = list.get(var12);
            if (var14.flag && var0 >= var14.endNs) {
               list.remove(var12);
            }
         }

         if (list.isEmpty() && flag) {
            runClear();
         }
      }
   }

   public static void runClear() {
      if (mc.player != null && mc.world != null) {
         long var0 = mc.getWindow().getHandle();

         for (KeyBinding var5 : KeyBindingUtil.getMc()) {
            var5.setPressed(InputUtil.isKeyPressed(var0, var5.getDefaultKey().getCode()));
         }

         flag = false;
      } else {
         list.clear();
         flag = false;
      }
   }

   public EntryHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   private static final class Entry {
      public final long execNs;
      public final long endNs;
      public final Runnable action;
      public boolean flag = false;

      public Entry(long execNs, long endNs, Runnable action) {
         this.execNs = execNsx;
         this.endNs = endNsx;
         this.action = actionx;
      }
   }
}
