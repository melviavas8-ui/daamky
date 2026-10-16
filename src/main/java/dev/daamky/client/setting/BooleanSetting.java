package dev.daamky.client.setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting<Boolean> {
   public BooleanSetting(String name) {
      super(name);
   }

   public BooleanSetting(String name, boolean value) {
      super(name);
      this.setValue(value);
   }

   public BooleanSetting value(Boolean value) {
      this.setValue(value);
      return this;
   }

   public void setValue(Boolean value) {
      if (!this.isValue(value)) {
         super.setValue(value);
         this.invokeAction();
      }
   }

   public BooleanSetting visibleWhen(Supplier<Boolean> condition) {
      return (BooleanSetting)super.visibleWhen(condition);
   }

   public BooleanSetting onAction(Runnable action) {
      return (BooleanSetting)super.onAction(action);
   }

   public void toggle() {
      this.setValue(!this.getValue());
   }

   @Override
   public void setValue(Object param1) {
      this.setValue((Boolean)var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.value((Boolean)var1);
   }

}
