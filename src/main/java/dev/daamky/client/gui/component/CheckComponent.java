package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class CheckComponent extends Component<Boolean> {
   public Boolean cachedValue;
   public Animation animation = new Direction2(300, 1.0);
   public Animation animation2 = new Direction2(300, 1.0);
   public Animation animation3 = new Direction2(300, 1.0);

   public CheckComponent(String name, Boolean value) {
      super(name, value);
      this.cachedValue = value;
   }

   public CheckComponent(Module parent, String name, Boolean value) {
      super(parent, name, value);
      this.cachedValue = value;
   }

   public static CheckComponent initInit(String name, Boolean value) {
      return new CheckComponent(name, value);
   }

   public CheckComponent computeCheckComponent(Supplier<Boolean> value) {
      return (CheckComponent)super.withValue(value);
   }

   public CheckComponent withCachedValue(Boolean value) {
      CheckComponent var2 = (CheckComponent)super.withValue(value);
      this.cachedValue = (Boolean)super.getValue() && this.getVisible().get();
      return var2;
   }

   public CheckComponent onAction(Runnable action) {
      return (CheckComponent)super.withOnAction(this::lambda$onAction$0);
   }

   public CheckComponent computeVisible(Runnable action) {
      return (CheckComponent)super.withOnSetVisible(action);
   }

   public Boolean getCachedValue() {
      if (this.cachedValue == null) {
         this.cachedValue = (Boolean)super.getValue();
      }

      return this.cachedValue && this.getVisible().get();
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
      return this.computeCheckComponent(var1);
   }

   @Override
   public Component withValue(Object param1) {
      return this.withCachedValue((Boolean)var1);
   }

   public void lambda$onAction$0(Runnable action) {
      action.run();
      this.cachedValue = (Boolean)super.getValue() && this.getVisible().get();
   }
}
