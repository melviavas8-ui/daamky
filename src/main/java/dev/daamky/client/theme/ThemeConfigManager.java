package dev.daamky.client.theme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.daamky.client.internal.ArraycopyHelper;
import dev.daamky.client.internal.GlyphMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ThemeConfigManager {
   public static final Path THEME_DIR = Path.of("daamky/theme");
   public static final Path ACTIVE_FILE = THEME_DIR.resolve(".active");
   public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public static ThemeConfigManager instance;
   public static final int MAX_THEMES = 9;
   public final List<ArraycopyHelper> themes = new ArrayList<>();
   public ArraycopyHelper active;

   public static ThemeConfigManager getInstance() {
      if (instance == null) {
         instance = new ThemeConfigManager();
         instance.load();
      }

      return instance;
   }

   public List<ArraycopyHelper> getThemes() {
      return this.themes;
   }

   public ArraycopyHelper initActive() {
      if (this.active == null) {
         this.active = this.themes.isEmpty() ? new ArraycopyHelper("Default") : this.themes.get(0);
         if (this.themes.isEmpty()) {
            this.themes.add(this.active);
         }
      }

      return this.active;
   }

   public void setActive(ArraycopyHelper theme) {
      if (theme != null) {
         this.active = theme;
         this.createCreate();
      }
   }

   public ArraycopyHelper computeThemes(String name) {
      if (!this.getThemes()) {
         return this.initActive();
      } else {
         name = this.computeTheme(name, null);
         ArraycopyHelper var2 = this.initActive();
         ArraycopyHelper var3 = var2.initArraycopy(name);
         this.themes.add(var3);
         this.setActive(var3);
         this.initAdd(var3);
         return var3;
      }
   }

   public void handleName(ArraycopyHelper theme, String newName) {
      if (theme != null && !theme.builtin) {
         newName = newName == null ? "" : newName.trim();
         if (!newName.isEmpty() && !newName.equalsIgnoreCase(theme.name)) {
            try {
               Files.deleteIfExists(THEME_DIR.resolve(theme.name + ".json"));
            } catch (IOException var4) {
            }

            theme.name = this.computeTheme(newName, theme);
            this.initAdd(theme);
            if (this.active == theme) {
               this.createCreate();
            }
         }
      }
   }

   public void handleThemes(ArraycopyHelper theme) {
      if (theme != null && !theme.builtin && this.themes.size() > 1) {
         this.themes.remove(theme);

         try {
            Files.deleteIfExists(THEME_DIR.resolve(theme.name + ".json"));
         } catch (IOException var3) {
         }

         if (this.active == theme) {
            this.active = this.themes.get(0);
            this.createCreate();
         }
      }
   }

   public static ArraycopyHelper initColors(String name, int visual, int hud) {
      ArraycopyHelper var3 = new ArraycopyHelper(name);
      var3.builtin = true;
      var3.colors[0] = visual;
      var3.colors[5] = hud;
      var3.colors[3] = ArraycopyHelper.computeInt(255, 255, 255);
      var3.colors[4] = ArraycopyHelper.computeInt(177, 177, 177);
      var3.colors[1] = ArraycopyHelper.computeInt(15, 15, 15);
      var3.colors[2] = GlyphMap.getColor(255, 255, 255, 15);
      var3.colors[6] = GlyphMap.getColor(255, 255, 255, 15);
      var3.colors[7] = GlyphMap.getColor(255, 255, 255, 10);
      var3.handleSliders(0, 0.9F);
      var3.handleSliders(1, 4.0F);
      var3.handleBooleans(0, true);
      var3.handleBooleans(1, false);
      return var3;
   }

   public void load() {
      this.themes.clear();
      this.themes.add(initColors("Blue", ArraycopyHelper.computeInt(124, 143, 246), ArraycopyHelper.computeInt(143, 161, 250)));
      this.themes.add(initColors("Red", ArraycopyHelper.computeInt(246, 124, 126), ArraycopyHelper.computeInt(246, 124, 126)));

      try {
         Files.createDirectories(THEME_DIR);

         try (DirectoryStream var1 = Files.newDirectoryStream(THEME_DIR, "*.json")) {
            for (Path var3 : (Iterable<Path>)(Iterable)var1) {
               ArraycopyHelper var4 = this.initInit(var3);
               if (var4 != null && !this.nameExists(var4.name, null)) {
                  this.themes.add(var4);
               }
            }
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      String var8 = this.getACTIVE_FILE();
      this.active = this.themes.stream().filter((p0) -> ThemeConfigManager.lambda$load$0(var8, p0)).findFirst().orElse(this.themes.get(0));
   }

   public void initAdd(ArraycopyHelper theme) {
      if (theme != null && !theme.builtin) {
         try {
            Files.createDirectories(THEME_DIR);
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", theme.name);

            for (int var3 = 0; var3 < 8; var3++) {
               var2.addProperty(computeString(var3), theme.colors[var3]);
            }

            for (int var10 = 0; var10 < theme.sliders.length; var10++) {
               var2.addProperty(computeString2(var10), theme.sliders[var10]);
            }

            for (int var11 = 0; var11 < theme.booleans.length; var11++) {
               var2.addProperty(computeString3(var11), theme.booleans[var11]);
            }

            Path var12 = THEME_DIR.resolve(theme.name + ".json");

            try (OutputStreamWriter var4 = new OutputStreamWriter(new FileOutputStream(var12.toFile()), StandardCharsets.UTF_8)) {
               GSON.toJson(var2, var4);
            }
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }
   }

   public void run() {
      this.initAdd(this.initActive());
   }

   public ArraycopyHelper initInit(Path path) {
      try {
         ArraycopyHelper var13;
         try (InputStreamReader var2 = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8)) {
            JsonObject var3 = JsonParser.parseReader(var2).getAsJsonObject();
            String var4 = var3.has("name") ? var3.get("name").getAsString() : computeLast(path.getFileName().toString());
            ArraycopyHelper var5 = new ArraycopyHelper(var4);

            for (int var6 = 0; var6 < 8; var6++) {
               String var7 = computeString(var6);
               if (var3.has(var7)) {
                  var5.colors[var6] = var3.get(var7).getAsInt();
               }
            }

            for (int var11 = 0; var11 < var5.sliders.length; var11++) {
               String var14 = computeString2(var11);
               if (var3.has(var14)) {
                  var5.handleSliders(var11, var3.get(var14).getAsFloat());
               }
            }

            for (int var12 = 0; var12 < var5.booleans.length; var12++) {
               String var15 = computeString3(var12);
               if (var3.has(var15)) {
                  var5.handleBooleans(var12, var3.get(var15).getAsBoolean());
               }
            }

            var13 = var5;
         }

         return var13;
      } catch (Exception var10) {
         var10.printStackTrace();
         return null;
      }
   }

   public void createCreate() {
      try {
         Files.createDirectories(THEME_DIR);
         Files.write(ACTIVE_FILE, this.initActive().name.getBytes(StandardCharsets.UTF_8));
      } catch (IOException var2) {
      }
   }

   public String getACTIVE_FILE() {
      try {
         if (Files.exists(ACTIVE_FILE)) {
            return new String(Files.readAllBytes(ACTIVE_FILE), StandardCharsets.UTF_8).trim();
         }
      } catch (IOException var2) {
      }

      return null;
   }

   public String computeTheme(String base, ArraycopyHelper exclude) {
      if (base == null || base.isBlank()) {
         base = "Theme";
      }

      base = base.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
      if (base.isEmpty()) {
         base = "Theme";
      }

      String var3 = base;
      int var4 = 1;

      while (this.nameExists(var3, exclude)) {
         var3 = base + " " + ++var4;
      }

      return var3;
   }

   public boolean nameExists(String name, ArraycopyHelper exclude) {
      return this.themes.stream().anyMatch((p0) -> ThemeConfigManager.lambda$nameExists$1(exclude, name, p0));
   }

   public static String computeString(int i) {
      return switch (i) {
         case 0 -> "accent";
         case 1 -> "background";
         case 2 -> "outline";
         case 3 -> "text";
         case 4 -> "text2";
         case 5 -> "element";
         case 6 -> "separator";
         case 7 -> "lightbg";
         default -> "c" + i;
      };
   }

   public static String computeString2(int i) {
      return switch (i) {
         case 0 -> "opacity";
         case 1 -> "blur";
         default -> "s" + i;
      };
   }

   public static String computeString3(int i) {
      return switch (i) {
         case 0 -> "shadow";
         case 1 -> "distortion";
         default -> "b" + i;
      };
   }

   public static String computeLast(String fileName) {
      int var1 = fileName.lastIndexOf(46);
      return var1 > 0 ? fileName.substring(0, var1) : fileName;
   }

   public static boolean lambda$nameExists$1(ArraycopyHelper exclude, String name, ArraycopyHelper t) {
      return t != exclude && t.name.equalsIgnoreCase(name);
   }

   public static boolean lambda$load$0(String activeName, ArraycopyHelper t) {
      return t.name.equals(activeName);
   }
}
