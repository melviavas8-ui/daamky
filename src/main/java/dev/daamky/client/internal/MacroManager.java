package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MacroManager implements MinecraftAccess {
   public static final MacroManager macroManager = new MacroManager();
   public final File file = new File(ClientPaths.string5 + "/macros.json");
   public final List<MacroManager.Macro> list = new ArrayList<>();

   public void run() {
      MacroHelper.getInstance().closeAdd(this.file, this.list);
   }

   public void run2() {
      MacroHelper.getInstance().closeClose(this.file, this.list);
   }

   public void initAdd(String name, String message, int key) {
      this.list.add(new MacroManager.Macro(name, message, key));
      this.run2();
   }

   public void remove(String name) {
      this.list.removeIf((p0) -> MacroManager.lambda$remove$0(name, p0));
      this.run2();
   }

   public boolean has(String name) {
      return this.list.stream().anyMatch((p0) -> MacroManager.lambda$has$1(name, p0));
   }

   public void runClear() {
      this.list.clear();
      this.run2();
   }

   public void onKeyPressed(int key) {
      if (mc.player != null) {
         this.list.stream().filter((p0) -> MacroManager.lambda$onKeyPressed$2(key, p0)).findFirst().ifPresent(MacroManager::lambda$onKeyPressed$3);
      }
   }

   public File getFile() {
      return this.file;
   }

   public List<MacroManager.Macro> getList() {
      return this.list;
   }

   public static MacroManager getInstance() {
      return macroManager;
   }

   public static void lambda$onKeyPressed$3(MacroManager.Macro m) {
      if (m.message.startsWith("/")) {
         mc.player.networkHandler.sendChatCommand(m.message.replace("/", ""));
      } else {
         mc.player.networkHandler.sendChatMessage(m.message);
      }
   }

   public static boolean lambda$onKeyPressed$2(int key, MacroManager.Macro m) {
      return m.getKey() == key;
   }

   public static boolean lambda$has$1(String name, MacroManager.Macro m) {
      return m.getName().equalsIgnoreCase(name);
   }

   public static boolean lambda$remove$0(String name, MacroManager.Macro m) {
      return m.getName().equalsIgnoreCase(name);
   }

   public static final class Macro {
      public final String name;
      public final String message;
      public final int key;

      public Macro(String name, String message, int key) {
         this.name = name;
         this.message = message;
         this.key = key;
      }

      public String getName() {
         return this.name;
      }

      public String getMessage() {
         return this.message;
      }

      public int getKey() {
         return this.key;
      }

      public boolean computeEquals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof MacroManager.Macro var2)) {
            return false;
         } else if (this.getKey() != var2.getKey()) {
            return false;
         } else {
            String var3 = this.getName();
            String var4 = var2.getName();
            if (var3 == null ? var4 == null : var3.equals(var4)) {
               String var5 = this.getMessage();
               String var6 = var2.getMessage();
               return var5 != null ? var5.equals(var6) : var6 == null;
            } else {
               return false;
            }
         }
      }

      public int getCode() {
         byte var1 = 59;
         int var2 = 1;
         var2 = var2 * 59 + this.getKey();
         String var3 = this.getName();
         var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
         String var4 = this.getMessage();
         return var2 * 59 + (var4 == null ? 43 : var4.hashCode());
      }

   }
}
