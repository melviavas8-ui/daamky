package dev.daamky.client.setting;

import java.util.function.Supplier;

public class FloatSetting extends Setting<Float> {
   public float min = Float.MIN_VALUE;
   public float max = Float.MAX_VALUE;
   public float step = 1.0F;

   public FloatSetting(String name) {
      super(name);
   }

   public FloatSetting value(Float value) {
      this.setValue(value);
      return this;
   }

   public void setValue(Float value) {
      if (!this.isValue(value)) {
         super.setValue(value);
         this.invokeAction();
      }
   }

   public FloatSetting visibleWhen(Supplier<Boolean> condition) {
      return (FloatSetting)super.visibleWhen(condition);
   }

   public FloatSetting onAction(Runnable action) {
      return (FloatSetting)super.onAction(action);
   }

   public FloatSetting range(float min, float max) {
      this.min = minx;
      this.max = maxx;
      return this;
   }

   public FloatSetting step(float step) {
      this.step = stepx;
      return this;
   }

   public float getMin() {
      return this.min;
   }

   public float getMax() {
      return this.max;
   }

   public float getStep() {
      return this.step;
   }

   @Override
   public void setValue(Object param1) {
      this.setValue((Float)var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.value((Float)var1);
   }

}
