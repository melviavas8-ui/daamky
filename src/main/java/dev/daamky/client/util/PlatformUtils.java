package dev.daamky.client.util;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public final class PlatformUtils implements MinecraftAccess {
   public static ClientPlayerEntity getClass_746() {
      return MinecraftClient.getInstance().player;
   }

   public static ClientWorld getClass_638() {
      return MinecraftClient.getInstance().world;
   }

   public static boolean getMc() {
      if (mc != null && mc.inGameHud != null) {
         BossBarHud var0 = mc.inGameHud.getBossBarHud();
         if (var0 != null && var0.bossBars != null) {
            Map var1 = var0.bossBars;

            for (ClientBossBar var3 : var1.values()) {
               if (var3 != null && var3.getName() != null) {
                  String var4 = var3.getName().getString().toLowerCase(Locale.ROOT);
                  if (var4.contains("pvp") || var4.contains("пвп")) {
                     return true;
                  }
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean computeContains(String path) {
      String var1 = getContains().toLowerCase();

      try {
         if (var1.contains("win")) {
            Runtime.getRuntime().exec("explorer \"" + path + "\"");
            return true;
         }

         if (var1.contains("mac")) {
            Runtime.getRuntime().exec(new String[]{"open", path});
            return true;
         }

         if (var1.contains("lin")) {
            String[] var2 = new String[]{"thunar", "dolphin", "nautilus", "nemo", "pcmanfm", "caja", "konqueror"};

            for (String var6 : var2) {
               try {
                  Process var7 = Runtime.getRuntime().exec(new String[]{var6, path});
                  if (var7.isAlive()) {
                     return true;
                  }
               } catch (IOException var8) {
               }
            }
         }
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      return false;
   }

   public static String getContains() {
      if (!System.getProperty("java.vendor").toLowerCase().contains("android") && !System.getProperty("java.vm.vendor").toLowerCase().contains("android")) {
         String var0 = System.getProperty("os.name").toLowerCase();
         if (var0.contains("win")) {
            return "Windows";
         } else if (var0.contains("mac")) {
            return "MacOS";
         } else if (var0.contains("lin")) {
            return "Linux";
         } else {
            return !var0.contains("nix") && !var0.contains("nux") && !var0.contains("aix") ? "Unknown" : "Linux/Unix";
         }
      } else {
         return "Android";
      }
   }

   public PlatformUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
