package dev.daamky.client.setting;

import java.awt.Color;
import java.util.function.Supplier;

public class ColorSetting extends Setting<Color> {
   public ColorSetting(String name) {
      super(name);
   }

   public ColorSetting computeColorSetting(Color value) {
      this.handleAction(value);
      return this;
   }

   public void handleAction(Color value) {
      if (!this.isValue(value)) {
         super.setValue(value);
         this.invokeAction();
      }
   }

   public ColorSetting computeVisible(Supplier<Boolean> condition) {
      return (ColorSetting)super.visibleWhen(condition);
   }

   public ColorSetting computeAction(Runnable action) {
      return (ColorSetting)super.onAction(action);
   }

   @Override
   public void setValue(Object param1) {
      this.handleAction((Color)var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.computeColorSetting((Color)var1);
   }

   @Override
   public Setting onAction(Runnable param1) {
      return this.computeAction(var1);
   }

   @Override
   public Setting visibleWhen(Supplier param1) {
      return this.computeVisible(var1);
   }
}
