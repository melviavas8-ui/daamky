package dev.daamky.client.internal;

import dev.daamky.client.model.enums.ThemeVariant;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class GuiSettingsHelper {
   public static final File muofz = new File("daamky/config", "theme/theme.json");
   public ThemeVariant theme = ThemeVariant.NIGHT;

   public void createFile() {
      try {
         File var1 = muofz.getParentFile();
         if (var1 != null && !var1.exists()) {
            var1.mkdirs();
         }

         if (!muofz.exists()) {
            muofz.createNewFile();
            this.closeClose();
         } else {
            this.closeClose2();
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public void setTheme(ThemeVariant theme) {
      this.theme = theme;
      this.closeClose();
   }

   public ThemeVariant getTheme() {
      return this.theme;
   }

   public void closeClose() {
      try (FileWriter var1 = new FileWriter(muofz)) {
         Properties var2 = new Properties();
         var2.setProperty("theme", this.theme.name());
         var2.store(var1, "GUI Settings");
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   public void closeClose2() {
      try (FileReader var1 = new FileReader(muofz)) {
         Properties var2 = new Properties();
         var2.load(var1);
         this.theme = ThemeVariant.valueOf(var2.getProperty("theme", ThemeVariant.NIGHT.name()));
      } catch (IllegalArgumentException | IOException var6) {
         var6.printStackTrace();
      }
   }
}
