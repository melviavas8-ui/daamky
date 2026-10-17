package dev.daamky.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.daamky.client.internal.ClientPaths;
import dev.daamky.client.internal.GpsHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.ModuleRegistry;
import dev.daamky.client.module.Module;
import dev.daamky.client.rotation.RotationProfileManager;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ConfigManager {
   public static final ConfigManager instance = new ConfigManager();
   public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public final Path CONFIG_DIR = Paths.get(ClientPaths.string7);
   public String currentConfig = "default";
   public static List<String> cachedNames = null;
   public static long lastScanTime = 0L;

   public ConfigManager() {
      try {
         Files.createDirectories(this.CONFIG_DIR);
      } catch (IOException var2) {
         throw new RuntimeException("Failed to create config dir", var2);
      }
   }

   public List<String> getConfigsNames() {
      if (cachedNames != null && System.currentTimeMillis() - lastScanTime <= 2000L) {
         return cachedNames;
      } else {
         File var1 = new File(ClientPaths.string7);
         if (!var1.exists()) {
            return Collections.emptyList();
         } else {
            File[] var2 = var1.listFiles(ConfigManager::lambda$getConfigsNames$0);
            if (var2 == null) {
               return Collections.emptyList();
            } else {
               cachedNames = Arrays.stream(var2).map(ConfigManager::lambda$getConfigsNames$1).distinct().collect(Collectors.toList());
               lastScanTime = System.currentTimeMillis();
               return cachedNames;
            }
         }
      }
   }

   public static void runCached() {
      cachedNames = null;
   }

   public static String computeDefault(String name) {
      if (name == null) {
         return "default";
      } else {
         String var1 = name.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
         return var1.isEmpty() ? "default" : var1;
      }
   }

   public Path computeResolve(String name) {
      Path var2 = this.CONFIG_DIR.resolve(computeDefault(name) + ".json");
      if (Files.exists(var2)) {
         return var2;
      } else {
         Path var3 = this.CONFIG_DIR.resolve(computeDefault(name) + ".gone");
         return Files.exists(var3) ? var3 : var2;
      }
   }

   public JsonObject getGSON() {
      JsonObject var1 = new JsonObject();
      List var2 = ModuleRegistry.getInstance().getList();
      JsonObject var3 = new JsonObject();

      for (Module var5 : (Iterable<Module>)(Iterable)var2) {
         var3.add(var5.getName(), this.createConfigFromModule(var5));
      }

      var1.add("Modules", var3);
      JsonObject var9 = new JsonObject();

      for (HudElement var6 : GpsHelper.getInstance().getList()) {
         JsonObject var7 = new JsonObject();
         var7.addProperty("x", var6.getAutoHelper().getX());
         var7.addProperty("y", var6.getAutoHelper().getY());
         var7.addProperty("enabled", var6.checkGps());
         var9.add(var6.getName(), var7);
      }

      var1.add("Widgets", var9);

      try {
         var1.add("RotationProfile", GSON.toJsonTree(RotationProfileManager.getInstance()));
      } catch (Throwable var8) {
      }

      return var1;
   }

   public void loadFromJson(JsonObject root) {
      if (root != null && root.has("Modules")) {
         if (root.has("RotationProfile")) {
            try {
               RotationProfileManager.getInstance().handleJson(root.getAsJsonObject("RotationProfile"));
            } catch (Throwable var7) {
            }
         }

         JsonObject var2 = root.getAsJsonObject("Modules");
         var2.entrySet().forEach(this::lambda$loadFromJson$3);
         if (root.has("Widgets")) {
            JsonObject var3 = root.getAsJsonObject("Widgets");

            for (HudElement var5 : GpsHelper.getInstance().getList()) {
               if (var3.has(var5.getName())) {
                  JsonObject var6 = var3.getAsJsonObject(var5.getName());
                  if (var6.has("x")) {
                     var5.getAutoHelper().setX(var6.get("x").getAsFloat());
                  }

                  if (var6.has("y")) {
                     var5.getAutoHelper().setY(var6.get("y").getAsFloat());
                  }

                  if (var6.has("enabled")) {
                     var5.setEnabled(var6.get("enabled").getAsBoolean());
                  }
               }
            }
         }
      }
   }

   public void setCurrentConfig(String name) {
      runCached();
      JsonObject var2 = this.getGSON();
      Path var3 = this.computeResolve(name);

      try {
         if (!Files.exists(var3)) {
            Files.createFile(var3);
         }

         Files.writeString(var3, GSON.toJson(var2));
         this.currentConfig = name;
      } catch (IOException var5) {
         System.err.printf("Failed to save config %s: %s%n", name, var5.getMessage());
      }
   }

   public void loadJson(String name) {
      Path var2 = this.computeResolve(name);
      if (Files.exists(var2)) {
         try {
            String var3 = Files.readString(var2);
            JsonElement var4 = (JsonElement)GSON.fromJson(var3, JsonElement.class);
            if (var4 != null && var4.isJsonObject()) {
               this.loadFromJson(var4.getAsJsonObject());
               this.currentConfig = name;
            }
         } catch (Throwable var5) {
            System.err.printf("Failed to load config %s: %s%n", name, var5.getMessage());
         }
      }
   }

   public void handleConfig(String name) {
      try {
         Files.deleteIfExists(this.CONFIG_DIR.resolve(computeDefault(name) + ".json"));
         Files.deleteIfExists(this.CONFIG_DIR.resolve(computeDefault(name) + ".gone"));
      } catch (IOException var3) {
         System.err.printf("Failed to remove config %s: %s%n", name, var3.getMessage());
      }

      runCached();
   }

   public JsonObject createConfigFromModule(Module feature) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("enabled", feature.isEnabled());
      var2.addProperty("bind", feature.getBind());
      var2.addProperty("bindHold", feature.isBindHold());
      JsonObject var3 = new JsonObject();
      feature.getSettings().forEach((p0) -> this.lambda$createConfigFromModule$4(var3, p0));
      var2.add("settings", var3);
      return var2;
   }

   public void applyConfigToModule(Module feature, JsonObject json) {
      if (json.has("enabled")) {
         feature.setEnabledInternal(json.get("enabled").getAsBoolean(), true);
      }

      if (json.has("bind")) {
         feature.setBind(json.get("bind").getAsInt());
      }

      if (json.has("bindHold")) {
         feature.setBindHold(json.get("bindHold").getAsBoolean());
      }

      if (json.has("settings")) {
         JsonObject var3 = json.getAsJsonObject("settings");
         feature.getSettings().forEach((p0) -> this.lambda$applyConfigToModule$5(var3, p0));
      }
   }

   public JsonElement serializeSetting(Setting<?> s) {
      if (s == null) {
         return null;
      } else if (s instanceof BooleanSetting) {
         return GSON.toJsonTree(((BooleanSetting)s).getValue());
      } else if (s instanceof ModeSetting) {
         return GSON.toJsonTree(((ModeSetting)s).getValue());
      } else if (s instanceof FloatSetting) {
         return GSON.toJsonTree(((FloatSetting)s).getValue());
      } else if (s instanceof StringSetting) {
         return GSON.toJsonTree(((StringSetting)s).getValue());
      } else if (s instanceof IntegerSetting) {
         return GSON.toJsonTree(((IntegerSetting)s).getValue());
      } else if (s instanceof MultiBooleanSetting var5) {
         JsonObject var6 = new JsonObject();
         if (var5.getValue() != null) {
            var5.getValue().forEach((p0) -> ConfigManager.lambda$serializeSetting$6(var6, p0));
         }

         return var6;
      } else if (s instanceof ColorSetting var2) {
         Color var3 = var2.getValue();
         if (var3 == null) {
            return null;
         } else {
            JsonObject var4 = new JsonObject();
            var4.addProperty("r", var3.getRed());
            var4.addProperty("g", var3.getGreen());
            var4.addProperty("b", var3.getBlue());
            var4.addProperty("a", var3.getAlpha());
            return var4;
         }
      } else {
         return null;
      }
   }

   public void deserializeSetting(Setting<?> s, JsonElement e) {
      if (s != null && e != null && !e.isJsonNull()) {
         try {
            if (s instanceof BooleanSetting) {
               ((BooleanSetting)s).setValue(e.getAsBoolean());
            } else if (s instanceof ModeSetting) {
               ((ModeSetting)s).setValue(e.getAsString());
            } else if (s instanceof FloatSetting) {
               ((FloatSetting)s).setValue(e.getAsFloat());
            } else if (s instanceof StringSetting) {
               ((StringSetting)s).setValue(e.getAsString());
            } else if (s instanceof IntegerSetting) {
               ((IntegerSetting)s).setValue(e.getAsInt());
            } else if (s instanceof MultiBooleanSetting var3) {
               JsonObject var4 = e.getAsJsonObject();
               if (var3.getValue() != null) {
                  var3.getValue().forEach((p0) -> ConfigManager.lambda$deserializeSetting$7(var4, p0));
               }
            } else if (s instanceof ColorSetting var10) {
               JsonObject var11 = e.getAsJsonObject();
               int var5 = var11.get("r").getAsInt();
               int var6 = var11.get("g").getAsInt();
               int var7 = var11.get("b").getAsInt();
               int var8 = var11.get("a").getAsInt();
               var10.handleAction(new Color(var5, var6, var7, var8));
            }
         } catch (Exception var9) {
            System.err.printf("Failed to deserialize %s: %s%n", s.getName(), var9.getMessage());
         }
      }
   }

   public static ConfigManager getInstance() {
      return instance;
   }

   public String getCurrentConfig() {
      return this.currentConfig;
   }

   public static void lambda$deserializeSetting$7(JsonObject obj, BooleanSetting b) {
      if (b != null && b.getName() != null && obj.has(b.getName())) {
         b.setValue(obj.get(b.getName()).getAsBoolean());
      }
   }

   public static void lambda$serializeSetting$6(JsonObject obj, BooleanSetting b) {
      if (b != null && b.getName() != null) {
         obj.addProperty(b.getName(), b.getValue());
      }
   }

   public void lambda$applyConfigToModule$5(JsonObject settings, Setting s) {
      if (s != null && s.getName() != null) {
         if (settings.has(s.getName())) {
            this.deserializeSetting((Setting<?>)s, settings.get(s.getName()));
         }
      }
   }

   public void lambda$createConfigFromModule$4(JsonObject settings, Setting s) {
      if (s != null && s.getName() != null) {
         JsonElement var3 = this.serializeSetting((Setting<?>)s);
         if (var3 != null) {
            settings.add(s.getName(), var3);
         }
      }
   }

   public void lambda$loadFromJson$3(Entry entry) {
      Module var2 = ModuleRegistry.getInstance().getList().stream().filter((p0) -> ConfigManager.lambda$loadFromJson$2(entry, p0)).findFirst().orElse(null);
      if (var2 == null) {
         System.err.println("Module not found for config: " + (String)entry.getKey());
      } else {
         this.applyConfigToModule(var2, ((JsonElement)entry.getValue()).getAsJsonObject());
      }
   }

   public static boolean lambda$loadFromJson$2(Entry entry, Module m) {
      return m.getName().equalsIgnoreCase((String)entry.getKey());
   }

   public static String lambda$getConfigsNames$1(File f) {
      return f.getName().replaceAll("\\.(json|gone)$", "");
   }

   public static boolean lambda$getConfigsNames$0(File dir, String name) {
      return (name.endsWith(".json") || name.endsWith(".gone")) && !name.startsWith(".") && !name.startsWith("_") && !name.toLowerCase().contains("meta");
   }
}
