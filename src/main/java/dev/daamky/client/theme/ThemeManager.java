package dev.daamky.client.theme;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.ClientPaths;
import dev.daamky.client.internal.ColorTheme;
import dev.daamky.client.internal.Context;
import dev.daamky.client.internal.White;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeManager {
   public static final Charset charset = StandardCharsets.UTF_8;
   public static final ThemeManager themeManager = new ThemeManager();
   public final String path;
   public final File file;
   public final File file2;
   public boolean lightMode = false;

   public boolean getIsDayMode() {
      return MainMenuScreen.isDayMode;
   }

   public ThemeManager(String path) {
      this.path = path;
      this.file = new File(path);
      this.file2 = new File(this.file, "last_selected");
      this.run();
      this.loadDefaultThemes();
   }

   public ThemeManager() {
      this(ClientPaths.string6);
   }

   public void initName(ColorTheme theme) {
      if (theme != null) {
         this.run();
         Path var2 = this.file.toPath().resolve(this.computeReplace(theme.getName()) + ".themes");
         ArrayList var3 = new ArrayList();
         var3.add("name=" + theme.getName());

         for (ColorTheme.ElementColor var5 : theme.getList()) {
            var3.add(var5.getName() + "=" + var5.getColor().getRGB());
         }

         try {
            Files.write(var2, var3, charset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }
   }

   public void runNext() {
      for (Context var2 : White.getInstance().getList()) {
         if (var2 != null && var2.getTheme() != null) {
            this.initName(var2.getTheme());
         }
      }
   }

   public void writeName(ColorTheme theme) {
      if (theme != null) {
         this.run();

         try {
            Files.writeString(
               this.file2.toPath(),
               this.computeReplace(theme.getName()) + "\n" + this.lightMode,
               charset,
               StandardOpenOption.CREATE,
               StandardOpenOption.TRUNCATE_EXISTING
            );
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }
   }

   public void setLightMode(boolean lightMode) {
      this.lightMode = lightMode;
      if (White.getInstance() != null) {
         this.writeName(White.getInstance().getTheme());
      }
   }

   public boolean remove(String themeName) {
      if (themeName != null && !themeName.isEmpty()) {
         this.run();
         File[] var2 = this.file.listFiles(ThemeManager::lambda$remove$0);
         if (var2 == null) {
            return false;
         } else {
            for (File var6 : var2) {
               try {
                  String var7 = Files.lines(var6.toPath(), charset).findFirst().orElse("");
                  if (var7.startsWith("name=") && themeName.equals(var7.substring(5).trim())) {
                     return var6.delete();
                  }
               } catch (IOException var8) {
                  var8.printStackTrace();
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public ColorTheme initInit(String name) {
      if (name != null && !name.isEmpty()) {
         File var2 = new File(this.file, this.computeReplace(name) + ".themes");
         return var2.exists() ? this.initName(var2) : null;
      } else {
         return null;
      }
   }

   public ColorTheme initName(File file) {
      if (file != null && file.exists()) {
         Map var2 = this.initNext(file.toPath());
         String var3 = var2.getOrDefault("name", this.computeLast(file.getName()));
         ColorTheme var4 = new ColorTheme(var3);

         for (ColorTheme.ElementColor var6 : var4.getList()) {
            String var7 = var6.getName();
            if (var2.containsKey(var7)) {
               try {
                  int var8 = Integer.parseInt((String)var2.get(var7));
                  var6.setColor(new Color(var8, true));
               } catch (NumberFormatException var9) {
               }
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   public void loadDefault() {
      this.loadDefaultThemes();
   }

   public ColorTheme initTrim() {
      if (!this.file2.exists()) {
         return null;
      } else {
         try {
            List var1 = Files.readAllLines(this.file2.toPath(), charset);
            if (var1.isEmpty()) {
               return null;
            } else {
               String var2 = ((String)var1.get(0)).trim();
               if (var1.size() > 1) {
                  this.lightMode = Boolean.parseBoolean(((String)var1.get(1)).trim());
               }

               if (var2.isEmpty()) {
                  return null;
               } else {
                  File var3 = new File(this.file, var2 + ".themes");
                  return var3.exists() ? this.initName(var3) : null;
               }
            }
         } catch (IOException var4) {
            var4.printStackTrace();
            return null;
         }
      }
   }

   public void run() {
      if (!this.file.exists()) {
         this.file.mkdirs();
      }
   }

   public void loadDefaultThemes() {
      ColorTheme[] var1 = new ColorTheme[]{
         this.initInit("White", 255, 255, 255, 220, 220, 220),
         this.initInit("Red", 255, 50, 50, 200, 10, 10),
         this.initInit("Blue", 50, 120, 255, 0, 70, 200),
         this.initInit("Green", 50, 255, 50, 10, 200, 10),
         this.initInit("Purple", 170, 50, 255, 120, 0, 200)
      };

      for (ColorTheme var5 : var1) {
         File var6 = new File(this.file, this.computeReplace(var5.getName()) + ".themes");
         if (!var6.exists()) {
            this.initName(var5);
         }
      }

      ArrayList var13 = new ArrayList();
      File[] var14 = this.file.exists() ? this.file.listFiles(ThemeManager::lambda$loadDefaultThemes$1) : null;
      if (var14 != null) {
         for (File var8 : var14) {
            ColorTheme var9 = this.initName(var8);
            if (var9 != null) {
               boolean var10 = false;

               for (Context var12 : (Iterable<Context>)(Iterable)var13) {
                  if (var12.getTheme().getName().equalsIgnoreCase(var9.getName())) {
                     var10 = true;
                     break;
                  }
               }

               if (!var10) {
                  var13.add(new Context(var9));
               }
            }
         }
      }

      White.getInstance().getList().clear();
      White.getInstance().getList().addAll(var13);
   }

   public ColorTheme initInit(String name, int r, int g, int b, int r2, int g2, int b2) {
      ColorTheme var8 = new ColorTheme(name);
      var8.computeNext("Primary").setColor(new Color(r, g, b));
      var8.computeNext("Secondary").setColor(new Color(r2, g2, b2));
      return var8;
   }

   public Map<String, String> initNext(Path p) {
      HashMap var2 = new HashMap();

      try {
         for (String var5 : Files.readAllLines(p, charset)) {
            String var7 = var5.trim();
            int var6;
            if (!var7.isEmpty() && !var7.startsWith("#") && (var6 = var7.indexOf(61)) > 0) {
               String var8 = var7.substring(0, var6);
               String var9 = var7.substring(var6 + 1);
               var2.put(var8, var9);
            }
         }
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      return var2;
   }

   public String computeReplace(String name) {
      return name == null ? "themes" : name.replaceAll("[\\\\/:*?\"<>|]", "_");
   }

   public String computeLast(String name) {
      int var2 = name.lastIndexOf(46);
      return var2 <= 0 ? name : name.substring(0, var2);
   }

   public String getPath() {
      return this.path;
   }

   public File getFile() {
      return this.file;
   }

   public File getFile2() {
      return this.file2;
   }

   public static ThemeManager getInstance() {
      return themeManager;
   }

   public static boolean lambda$loadDefaultThemes$1(File d, String n) {
      return n.toLowerCase().endsWith(".themes");
   }

   public static boolean lambda$remove$0(File d, String n) {
      return n.toLowerCase().endsWith(".themes");
   }
}
