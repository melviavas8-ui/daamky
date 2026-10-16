package dev.daamky.client.internal;

import com.google.gson.Gson;
import dev.daamky.client.util.MinecraftAccess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.util.Identifier;

public final class InputHelper implements MinecraftAccess {
   public static final Gson GSON = new Gson();
   public static final Map<String, Identifier> IMAGE_CACHE = new ConcurrentHashMap<>();
   public static final Map<String, Identifier> SHADER_CACHE = new ConcurrentHashMap<>();

   public static InputStream computeCase(String input) {
      return InputHelper.class.getResourceAsStream("/assets/" + "daamky".toLowerCase() + "/" + input);
   }

   public static Identifier getImage(String path) {
      return IMAGE_CACHE.computeIfAbsent(path, InputHelper::lambda$getImage$0);
   }

   public static Identifier getIcon(String path) {
      return IMAGE_CACHE.computeIfAbsent("icon:" + path, InputHelper::lambda$getIcon$1);
   }

   public static Identifier getShader(String name) {
      return SHADER_CACHE.computeIfAbsent(name, InputHelper::lambda$getShader$2);
   }

   public static <T> T computeJson(Identifier identifier, Class<T> clazz) {
      return (T)GSON.fromJson(computeString(identifier), clazz);
   }

   public static String computeString(Identifier identifier) {
      return closeClose(identifier, "\n");
   }

   public static byte[] closeClose(String fileName) {
      try {
         byte[] var4;
         try (InputStream var1 = computeCase("sounds/" + fileName)) {
            if (var1 == null) {
               Object var9 = null;
               return null;
            }

            byte[] var3 = var1.readAllBytes();
            var4 = var3;
         }

         return var4;
      } catch (IOException var8) {
         return null;
      }
   }

   public static String closeClose(Identifier identifier, String delimiter) {
      InputStream var2 = null;

      try {
         if (mc != null && mc.getResourceManager() != null) {
            var2 = mc.getResourceManager().open(identifier);
         }
      } catch (Throwable var9) {
      }

      if (var2 == null) {
         var2 = computeCase(identifier.getPath());
      }

      if (var2 == null) {
         return "";
      } else {
         try {
            String var4;
            try (BufferedReader var3 = new BufferedReader(new InputStreamReader(var2))) {
               var4 = var3.lines().collect(Collectors.joining(delimiter));
            }

            return var4;
         } catch (IOException var8) {
            return "";
         }
      }
   }

   public InputHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Identifier lambda$getShader$2(String n) {
      return Identifier.of("daamky".toLowerCase(), "core/" + n);
   }

   public static Identifier lambda$getIcon$1(String path, String p) {
      return Identifier.of("daamky".toLowerCase(), "textures/icons/" + path + ".png");
   }

   public static Identifier lambda$getImage$0(String p) {
      return Identifier.of("daamky".toLowerCase(), "images/" + p + ".png");
   }
}
