package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class SliderComponent extends Component<Float> {
   public float min;
   public float max;
   public float increment;
   public Float cachedValue;
   public Animation animation = new Direction2(300, 1.0);
   public Animation animation2 = new Direction2(300, 1.0);
   public Animation animation3 = new Direction2(300, 1.0);

   public SliderComponent(Module parent, String name, float value, float min, float max, float increment) {
      super(parent, name, value);
      this.min = min;
      this.max = max;
      this.increment = increment;
      this.cachedValue = value;
   }

   public SliderComponent withCachedValue(Float value) {
      SliderComponent var2 = (SliderComponent)super.withValue(value);
      this.cachedValue = (Float)super.getValue();
      return var2;
   }

   public SliderComponent computeSliderComponent(Supplier<Boolean> value) {
      return (SliderComponent)super.withValue(value);
   }

   public SliderComponent onAction(Runnable action) {
      return (SliderComponent)super.withOnAction(this::lambda$onAction$0);
   }

   public SliderComponent computeVisible(Runnable action) {
      return (SliderComponent)super.withOnSetVisible(action);
   }

   public Float getCachedValue() {
      if (this.cachedValue == null) {
         this.cachedValue = (Float)super.getValue();
      }

      return this.cachedValue;
   }

   @Override
   public Object getValue() {
      return this.getCachedValue();
   }

   @Override
   public Component withOnSetVisible(Runnable param1) {
      return this.computeVisible(param1);
   }

   @Override
   public Component withOnAction(Runnable param1) {
      return this.onAction(param1);
   }

   @Override
   public Component withValue(Supplier param1) {
      return this.computeSliderComponent(param1);
   }

   @Override
   public Component withValue(Float param1) {
      return this.withCachedValue(param1);
   }

   public void lambda$onAction$0(Runnable action) {
      action.run();
      this.cachedValue = (Float)super.getValue();
   }
}
