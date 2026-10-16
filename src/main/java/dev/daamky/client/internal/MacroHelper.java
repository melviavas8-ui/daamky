package dev.daamky.client.internal;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class MacroHelper {
   public static final MacroHelper macroHelper = new MacroHelper();
   public final GsonBuilder gson = new GsonBuilder().setPrettyPrinting();

   public void closeAdd(File file, List<MacroManager.Macro> macros) {
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (Exception var7) {
            System.out.println(var7.getMessage());
            return;
         }
      }

      try (FileReader var3 = new FileReader(file)) {
         Type var4 = (new TypeToken<List<MacroManager.Macro>>() {
         }).getType();
         List var5 = (List)this.gson.create().fromJson(var3, var4);
         macros.clear();
         if (var5 != null) {
            macros.addAll(var5);
         }
      } catch (Exception var9) {
         System.out.println(var9.getMessage());
      }
   }

   public void closeClose(File file, List<MacroManager.Macro> macros) {
      File var3 = file.getParentFile();
      if (var3 != null) {
         var3.mkdirs();
      }

      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (Exception var10) {
            System.out.println(var10.getMessage());
            return;
         }
      }

      try (FileWriter var4 = new FileWriter(file)) {
         this.gson.create().toJson(macros, var4);
      } catch (Exception var9) {
         System.out.println(var9.getMessage());
      }
   }

   public static MacroHelper getInstance() {
      return macroHelper;
   }
}
