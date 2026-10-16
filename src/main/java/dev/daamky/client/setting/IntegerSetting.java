package dev.daamky.client.setting;

import java.util.function.Supplier;

public class IntegerSetting extends Setting<Integer> {
   public IntegerSetting(String name) {
      super(name);
      this.value = -999;
   }

   public IntegerSetting value(Integer value) {
      this.setValue(value);
      return this;
   }

   public void setValue(Integer value) {
      if (!this.isValue(value)) {
         super.setValue(value);
         this.invokeAction();
      }
   }

   public IntegerSetting visibleWhen(Supplier<Boolean> condition) {
      return (IntegerSetting)super.visibleWhen(condition);
   }

   public IntegerSetting onAction(Runnable action) {
      return (IntegerSetting)super.onAction(action);
   }

   @Override
   public void setValue(Object param1) {
      this.setValue((Integer)var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.value((Integer)var1);
   }

}
