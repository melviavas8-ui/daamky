package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.ValueToHelper;
import dev.daamky.client.legacy.module.Module;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CategoryComponent extends Component<Map<String, CheckComponent>> {
   public final Map<String, CheckComponent> settingsMap = new LinkedHashMap<>();
   public ValueToHelper animation = new ValueToHelper();
   public boolean opened;

   public CategoryComponent(Module parent, String name, CheckComponent... values) {
      super(parent, name, new LinkedHashMap<>());

      for (CheckComponent var7 : values) {
         this.getValue().put(var7.getName().toLowerCase(), var7);
         this.settingsMap.put(var7.getName().toLowerCase(), var7);
      }
   }

   public CheckComponent computeCase(String name) {
      return this.settingsMap.get(name.toLowerCase());
   }

   public boolean computeCached(String name) {
      CheckComponent var2 = this.computeCase(name);
      return var2 != null && var2.getCachedValue() && this.getVisible().get();
   }

   public Collection<CheckComponent> getValues() {
      return this.getValue().values();
   }

   public CategoryComponent computeCategoryComponent(Supplier<Boolean> value) {
      return (CategoryComponent)super.withValue(value);
   }

   public CategoryComponent computeAction(Runnable action) {
      return (CategoryComponent)super.withOnAction(action);
   }

   public CategoryComponent computeVisible(Runnable action) {
      return (CategoryComponent)super.withOnSetVisible(action);
   }

   public String initNext() {
      ArrayList var1 = new ArrayList();

      for (CheckComponent var3 : this.getValues()) {
         if (var3.getCachedValue()) {
            var1.add(var3.getName());
         }
      }

      return String.join(", ", var1);
   }

   public boolean getSettingsMap() {
      for (CheckComponent var2 : this.settingsMap.values()) {
         if (var2.getCachedValue()) {
            return true;
         }
      }

      return false;
   }

      @Override
   public ValueToHelper getAnimation() {
      return this.animation;
   }

   @Override
   public Component withOnSetVisible(Runnable param1) {
      return this.computeVisible(param1);
   }

   @Override
   public Component withOnAction(Runnable param1) {
      return this.computeAction(param1);
   }

   @Override
   public Component withValue(Supplier param1) {
      return this.computeCategoryComponent(param1);
   }
}
