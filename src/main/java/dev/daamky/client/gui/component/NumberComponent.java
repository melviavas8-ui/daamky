package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class NumberComponent extends Component<Integer> {
   public final boolean allowMouse;
   public Animation animation = new Direction2(300, 1.0);
   public Integer cachedValue;

   public NumberComponent(Module parent, String name) {
      super(parent, name, -1);
      this.allowMouse = true;
      this.cachedValue = -1;
   }

   public NumberComponent(Module parent, String name, boolean allowMouse) {
      super(parent, name, -1);
      this.allowMouse = allowMousex;
      this.cachedValue = -1;
   }

   public NumberComponent(Module parent, String name, Integer value) {
      super(parent, name, value);
      this.allowMouse = true;
      this.cachedValue = value;
   }

   public NumberComponent(Module parent, String name, Integer value, boolean allowMouse) {
      super(parent, name, value);
      this.allowMouse = allowMousex;
      this.cachedValue = value;
   }

   public NumberComponent computeNumberComponent(Supplier<Boolean> value) {
      return (NumberComponent)super.withValue(value);
   }

   public NumberComponent withCachedValue(Integer value) {
      NumberComponent var2 = (NumberComponent)super.withValue(value);
      this.cachedValue = (Integer)super.getValue();
      return var2;
   }

   public NumberComponent onAction(Runnable action) {
      return (NumberComponent)super.withOnAction(this::lambda$onAction$0);
   }

   public NumberComponent computeVisible(Runnable action) {
      return (NumberComponent)super.withOnSetVisible(action);
   }

   public Integer getCachedValue() {
      if (this.cachedValue == null) {
         this.cachedValue = (Integer)super.getValue();
      }

      return this.cachedValue;
   }

   public Integer getValue() {
      if (this.cachedValue == null) {
         this.cachedValue = (Integer)super.getValue();
      }

      return this.cachedValue;
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
      return this.computeNumberComponent(var1);
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
