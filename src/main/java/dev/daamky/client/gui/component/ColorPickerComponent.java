package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.GlyphMap;
import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class ColorPickerComponent extends Component<Integer> {
   public Integer cachedValue;
   public boolean pickerOpen = false;
   public Animation pickerAnim = new Direction2(220, 1.0);

   public ColorPickerComponent(Module parent, String name) {
      super(parent, name, GlyphMap.intVal);
      this.cachedValue = GlyphMap.intVal;
   }

   public ColorPickerComponent(Module parent, String name, Integer value) {
      super(parent, name, value);
      this.cachedValue = value;
   }

   public ColorPickerComponent withCachedValue(Integer value) {
      ColorPickerComponent var2 = (ColorPickerComponent)super.withValue(value);
      this.cachedValue = (Integer)super.getValue();
      return var2;
   }

   public ColorPickerComponent computeColorPickerComponent(Supplier<Boolean> value) {
      return (ColorPickerComponent)super.withValue(value);
   }

   public ColorPickerComponent onAction(Runnable action) {
      return (ColorPickerComponent)super.withOnAction(this::lambda$onAction$0);
   }

   public ColorPickerComponent computeVisible(Runnable action) {
      return (ColorPickerComponent)super.withOnSetVisible(action);
   }

   public Integer getCachedValue() {
      if (this.cachedValue == null) {
         this.cachedValue = (Integer)super.getValue();
      }

      return this.cachedValue;
   }

   @Override
   public Object getValue() {
      return this.getCachedValue();
   }

   @Override
   public Component withOnSetVisible(Runnable param1) {
      return this.computeVisible(var1);
   }

   @Override
   public Component withOnAction(Runnable param1) {
      return this.onAction(var1);
   }

   @Override
   public Component withValue(Supplier param1) {
      return this.computeColorPickerComponent(var1);
   }

   @Override
   public Component withValue(Object param1) {
      return this.withCachedValue((Integer)var1);
   }

   public void lambda$onAction$0(Runnable action) {
      action.run();
      this.cachedValue = (Integer)super.getValue();
   }
}
