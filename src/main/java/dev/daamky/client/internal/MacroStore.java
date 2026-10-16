package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.daamky.client.util.MinecraftAccess;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MacroStore implements MinecraftAccess {
   public static final MacroStore macroStore = new MacroStore();
   public static final Gson GSON = new GsonBuilder().create();
   public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
   public final Map<String, Callback5> actionRegistry = new HashMap<>();
   public final Map<String, MacroPlayer> runningMacros = new ConcurrentHashMap<>();
   public final List<MacroEngine.Macro> list = new CopyOnWriteArrayList<>();
   public final File configFile = new File(ClientPaths.string5 + "/macros_new.json");
   public boolean initialized = false;

   public void isInitialized() {
      if (!this.initialized) {
         this.initialized = true;
         this.run();
         this.closeAdd();
      }
   }

   public void handlePut(String type, Callback5 action) {
      this.actionRegistry.put(type, action);
   }

   public void handlePut(Callback5 action) {
      this.actionRegistry.put(action.getLabel(), action);
   }

   public void run() {
      KeyHelper var1 = new KeyHelper();
      SlotHelper var2 = new SlotHelper();
      ConditionHelper var3 = new ConditionHelper();
      BlockHelper var4 = new BlockHelper();

      for (MacroEngine.ActionType var8 : MacroEngine.ActionType.values()) {
         switch (var8) {
            case HOLD_INPUT:
            case USE_COMMAND:
            case SEND_MESSAGE:
            case NOTIFY:
            case BREAK:
               this.handlePut(var8.getLabel(), var1);
               break;
            case SWAP_SLOT:
            case SWAP_SLOT_TO_ITEM:
            case MOVE_ITEM_TO_OFFHAND:
            case MOVE_ITEM_TO_HOTBAR:
            case AWAIT:
            case CLICK_ITEM:
            case SWAP_BETWEEN:
               this.handlePut(var8.getLabel(), var2);
               break;
            case ENABLE_MODULE:
            case IF_CONDITION:
            case EXECUTE_MACROS:
               this.handlePut(var8.getLabel(), var3);
               break;
            case ROTATE_BLOCK:
            case ROTATE_ENTITY:
            case ROTATE_OPERATION:
            case ROTATE_RELATIVE:
            case ROTATE_SET:
               this.handlePut(var8.getLabel(), var4);
         }
      }
   }

   public List<MacroEngine.Macro> getList() {
      return this.list;
   }

   public MacroEngine.Macro getMacro(String name) {
      return name == null ? null : this.list.stream().filter(MacroStore::lambda$getMacro$0).findFirst().orElse(null);
   }

   public void handleAdd(MacroEngine.Macro macro) {
      this.list.add(macro);
      this.closeFile();
   }

   public void removeMacro(String name) {
      this.list.removeIf(MacroStore::lambda$removeMacro$1);
      this.closeFile();
   }

   public boolean hasMacro(String name) {
      return this.list.stream().anyMatch(MacroStore::lambda$hasMacro$2);
   }

   public void initRunning(MacroEngine.Macro macro) {
      if (macro != null && !macro.getSteps().isEmpty()) {
         String var2 = macro.getName();
         if (!this.runningMacros.containsKey(var2)) {
            MacroPlayer var3 = new MacroPlayer(macro);
            this.runningMacros.put(var2, var3);
            this.handleCurrent(var3);
         }
      }
   }

   public void handleRemove(String name) {
      this.runningMacros.remove(name);
   }

   public void getRunningMacros() {
      this.runningMacros.clear();
   }

   public boolean computeContains(String name) {
      return this.runningMacros.containsKey(name);
   }

   public void runNext() {
      Iterator var1 = this.runningMacros.entrySet().iterator();

      while (var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         MacroPlayer var3 = (MacroPlayer)var2.getValue();
         this.handleCurrent(var3);
         if (var3.checkSize()) {
            var1.remove();
         }
      }
   }

   public boolean computeType(MacroEngine.MacroStep step, MacroPlayer ctx) {
      if (step.getType() == null) {
         return true;
      } else {
         Callback5 var3 = this.actionRegistry.get(step.getType().getLabel());
         return var3 == null ? true : var3.computeFlag(step, ctx);
      }
   }

   public void handleCurrent(MacroPlayer ctx) {
      if (!ctx.checkSize()) {
         ctx.runHold();
         if (!ctx.checkAwait()) {
            byte var2 = 20;

            for (int var3 = 0; !ctx.checkSize() && var3 < var2; var3++) {
               if (ctx.getCurrentIndex() >= ctx.getMacro2().getSteps().size()) {
                  ctx.runFinished();
                  return;
               }

               MacroEngine.MacroStep var4 = ctx.getMacro2().getSteps().get(ctx.getCurrentIndex());
               boolean var5 = this.computeType(var4, ctx);
               if (ctx.checkAwait()) {
                  return;
               }

               if (!var5) {
                  ctx.runFinished();
                  return;
               }

               ctx.getCurrentIndex();
            }
         }
      }
   }

   public void closeAdd() {
      if (!this.configFile.exists()) {
         this.initInit();
      } else {
         try (FileReader var1 = new FileReader(this.configFile)) {
            Type var2 = (new TypeToken<List<MacroEngine.Macro>>() {
            }).getType();
            List var3 = (List)GSON.fromJson(var1, var2);
            this.list.clear();
            if (var3 != null) {
               this.list.addAll(var3);
            }
         } catch (Exception var6) {
            System.err.println("[MacroManager] Failed to load: " + var6.getMessage());
            this.initInit();
         }
      }
   }

   public void closeFile() {
      File var1 = this.configFile.getParentFile();
      if (var1 != null && !var1.exists()) {
         var1.mkdirs();
      }

      try (FileWriter var2 = new FileWriter(this.configFile)) {
         GSON_PRETTY.toJson(this.list.stream().map(MacroEngine.Macro::initAdd).toList(), var2);
      } catch (Exception var7) {
         System.err.println("[MacroManager] Failed to save: " + var7.getMessage());
      }
   }

   public void initInit() {
      File var1 = new File(ClientPaths.string5 + "/macros.json");
      if (var1.exists()) {
         try {
            try (FileReader var2 = new FileReader(var1)) {
               Type var3 = (new TypeToken<List<MacroStore.OldMacro>>() {
               }).getType();
               List var4 = (List)GSON.fromJson(var2, var3);
               if (var4 != null && !var4.isEmpty()) {
                  for (MacroStore.OldMacro var6 : var4) {
                     MacroEngine.Macro var7 = new MacroEngine.Macro(var6.mocukxpq, var6.mehexmc);
                     boolean var8 = var6.mhsydf.startsWith("/");
                     MacroEngine.ActionType var9 = var8 ? MacroEngine.ActionType.USE_COMMAND : MacroEngine.ActionType.SEND_MESSAGE;
                     MacroEngine.MacroStep var10 = new MacroEngine.MacroStep(var9);
                     String var11 = var8 ? var6.mhsydf.substring(1) : var6.mhsydf;
                     var10.handleAdd(var8 ? "command" : "message", var11);
                     var7.getSteps().add(var10);
                     this.list.add(var7);
                  }

                  this.closeFile();
                  System.out.println("[MacroManager] Migrated " + var4.size() + " old macros.");
                  return;
               }
            }
         } catch (Exception var14) {
            System.err.println("[MacroManager] Migration failed: " + var14.getMessage());
         }
      }
   }

   public static MacroStore getInstance() {
      return macroStore;
   }

   public static boolean lambda$hasMacro$2(String name, MacroEngine.Macro m) {
      return m.getName().equalsIgnoreCase(name);
   }

   public static boolean lambda$removeMacro$1(String name, MacroEngine.Macro m) {
      return m.getName().equalsIgnoreCase(name);
   }

   public static boolean lambda$getMacro$0(String name, MacroEngine.Macro m) {
      return m.getName().equalsIgnoreCase(name);
   }

   static final class OldMacro {
      public String mocukxpq;
      public String mhsydf;
      public int mehexmc;

      public OldMacro() {
      }
   }
}
