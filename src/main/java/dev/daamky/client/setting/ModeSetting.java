package dev.daamky.client.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting<String> {
   public final List<String> values = new ArrayList<>();

   public ModeSetting(String name) {
      super(name);
   }

   public ModeSetting values(String... values) {
      this.values.addAll(Arrays.asList(valuesx));
      if (this.value == null && !this.values.isEmpty()) {
         this.value = this.values.getFirst();
      }

      return this;
   }

   public ModeSetting enumValues(Enum<?>... enums) {
      this.values.addAll(Arrays.stream(enums).map(ModeSetting::lambda$values$0).toList());
      if (this.value == null && !this.values.isEmpty()) {
         this.value = this.values.getFirst();
      }

      return this;
   }

   public ModeSetting value(Enum<?> e) {
      return this.value(e != null ? (e instanceof ModeSetting.NamedChoice ? ((ModeSetting.NamedChoice)e).getName() : e.name()) : null);
   }

   public boolean is(Enum<?> e) {
      if (e == null) {
         return false;
      } else {
         String var2 = e instanceof ModeSetting.NamedChoice ? ((ModeSetting.NamedChoice)e).getName() : e.name();
         return this.value.equals(var2);
      }
   }

   public ModeSetting value(String value) {
      this.setValue(value);
      return this;
   }

   public void setValue(String value) {
      if (!this.isValue(value)) {
         super.setValue(value);
         this.invokeAction();
      }
   }

   public ModeSetting visibleWhen(Supplier<Boolean> condition) {
      return (ModeSetting)super.visibleWhen(condition);
   }

   public ModeSetting onAction(Runnable action) {
      return (ModeSetting)super.onAction(action);
   }

   public boolean is(String value) {
      return this.value.equals(value);
   }

   public List<String> getValues() {
      return this.values;
   }

   @Override
   public void setValue(Object param1) {
      this.setValue((String)var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.value((String)var1);
   }

   public static String lambda$values$0(Enum e) {
      return e instanceof ModeSetting.NamedChoice ? ((ModeSetting.NamedChoice)e).getName() : e.name();
   }

   public interface NamedChoice {
      String getName();
   }
}
