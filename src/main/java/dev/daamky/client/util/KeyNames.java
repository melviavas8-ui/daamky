package dev.daamky.client.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.lwjgl.glfw.GLFW;

public final class KeyNames implements MinecraftAccess {
   public static final Int2ObjectOpenHashMap<String> int2ObjectOpenHashMap = new Int2ObjectOpenHashMap(200);
   public static final HashMap<String, Integer> maeqo = new HashMap<>(200);

   public static boolean computeGlfw(int keyCode) {
      if (keyCode == -1 || keyCode == -999) {
         return false;
      } else {
         return keyCode <= -93
            ? GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 100 + keyCode) == 1
            : GLFW.glfwGetKey(mc.getWindow().getHandle(), keyCode) == 1;
      }
   }

   public static int parseCase(String s) {
      if (s == null || s.isEmpty()) {
         return -1;
      } else if (s.equalsIgnoreCase("None")) {
         return -1;
      } else if (s.startsWith("Mouse")) {
         try {
            int var3 = Integer.parseInt(s.substring(5)) - 1;
            if (var3 >= 0 && var3 <= 7) {
               return -100 + var3;
            }
         } catch (NumberFormatException var2) {
         }

         return -1;
      } else {
         Integer var1 = maeqo.get(s.toLowerCase());
         return var1 != null ? var1 : -1;
      }
   }

   public static String computeNone(int key) {
      if (key != -1 && key != -999) {
         for (int var1 = 0; var1 <= 7; var1++) {
            if (key == -100 + var1) {
               return "Mouse" + (var1 + 1);
            }
         }

         String var2 = (String)int2ObjectOpenHashMap.get(key);
         return var2 != null ? var2 : "None";
      } else {
         return "None";
      }
   }

   public static String computeString(String input) {
      if (input == null || input.isEmpty()) {
         return input;
      } else if (input.startsWith("LEFT_")) {
         return "L" + computeReplace(input.substring(5));
      } else if (input.startsWith("RIGHT_")) {
         return "R" + computeReplace(input.substring(6));
      } else if (input.startsWith("KP_")) {
         return "Numpad" + computeReplace(input.substring(3));
      } else {
         return switch (input) {
            case "PRINT_SCREEN" -> "PrintScreen";
            case "CAPS_LOCK" -> "CapsLock";
            case "NUM_LOCK" -> "NumLock";
            case "PAGE_UP" -> "PageUp";
            case "PAGE_DOWN" -> "PageDown";
            default -> computeReplace(input.replace("_", ""));
         };
      }
   }

   public static String computeReplace(String raw) {
      if (raw != null && !raw.isEmpty()) {
         String var1 = raw.toLowerCase();
         var1 = var1.replace("accent", "");
         var1 = var1.replace("control", "ctrl");
         var1 = var1.replace("super", "Super");
         var1 = var1.replace("minus", "Minus");
         var1 = var1.replace("equals", "Equals");
         return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
      } else {
         return "";
      }
   }

   public KeyNames() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      try {
         for (Field var3 : GLFW.class.getDeclaredFields()) {
            if (var3.getName().startsWith("GLFW_KEY_") && var3.getType() == intVal.classVal) {
               int var4 = var3.getInt(null);
               String var5 = var3.getName().substring("GLFW_KEY_".length());
               String var6 = computeString(var5);
               int2ObjectOpenHashMap.put(var4, var6);
               maeqo.put(var6.toLowerCase(), var4);
               maeqo.put(var5.toLowerCase(), var4);
            }
         }
      } catch (IllegalAccessException var7) {
         var7.printStackTrace();
      }
   }
}
