package dev.daamky.client.internal;

import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.legacy.module.ModuleCategory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class Clear extends LinkedHashMap<Class<? extends Module>, Module> {
   public final List<Module> list = new ArrayList<>();
   public final List<Module> list2 = new ArrayList<>();
   public LegacyModule2 legacyModule2;

   public void init() {
      this.clear();
      this.list.clear();
      this.list2.clear();
      this.legacyModule2 = new LegacyModule2();
      this.put(LegacyModule2.class, this.legacyModule2);
      this.list.add(this.legacyModule2);
      List var1 = ModuleRegistry.getInstance().getList();
      if (var1 != null) {
         for (dev.daamky.client.module.Module var3 : var1) {
            LegacyModule var4 = new LegacyModule(var3);
            this.put((Class<? extends Module>)var4.getClass(), var4);
            this.list.add(var4);
         }
      }

      this.list2.addAll(this.list);
      this.list2.sort(Comparator.comparing(Module::getBigName, String.CASE_INSENSITIVE_ORDER));
   }

   public void refreshFeatures() {
      this.init();
   }

   public void addSorted(Module... modules) {
      for (Module var5 : modules) {
         this.put((Class<? extends Module>)var5.getClass(), var5);
         if (!this.list.contains(var5)) {
            this.list.add(var5);
         }
      }

      this.list2.clear();
      this.list2.addAll(this.list);
      this.list2.sort(Comparator.comparing(Module::getBigName, String.CASE_INSENSITIVE_ORDER));
   }

   public <T extends Module> T get(String name) {
      return (T)this.list.stream().filter(Clear::lambda$get$0).findFirst().orElse(null);
   }

   public <T extends Module> T get(Class<T> clazz) {
      return (T)(clazz == LegacyModule2.class && this.legacyModule2 != null
         ? this.legacyModule2
         : this.list.stream().filter(Clear::lambda$get$1).findFirst().orElse(null));
   }

   public List<Module> get(ModuleCategory category) {
      return this.list.stream().filter(Clear::lambda$get$2).collect(Collectors.toList());
   }

   public Module getModule(String name) {
      return this.get(name);
   }

   @Override
   public Collection<Module> values() {
      return this.list2;
   }

   public static boolean lambda$get$2(ModuleCategory category, Module module) {
      return module.getCategory() == category;
   }

   public static boolean lambda$get$1(Class clazz, Module module) {
      return clazz.isAssignableFrom(module.getClass());
   }

   public static boolean lambda$get$0(String name, Module module) {
      return module.getName().equalsIgnoreCase(name) || module.getBigName() != null && module.getBigName().equalsIgnoreCase(name);
   }
}
