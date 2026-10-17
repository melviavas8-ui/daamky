package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Component;
import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class TextFieldComponent extends Component<String> {
   public final boolean onlyNumber;
   public String cachedValue;

   public TextFieldComponent(Module parent, String name, String value) {
      super(parent, name, value);
      this.onlyNumber = false;
      this.cachedValue = value;
   }

   public TextFieldComponent(Module parent, String name, String value, boolean onlyNumber) {
      super(parent, name, value);
      this.onlyNumber = onlyNumber;
      this.cachedValue = value;
   }

   public TextFieldComponent withCachedValue(String value) {
      TextFieldComponent var2 = (TextFieldComponent)super.withValue(value);
      this.cachedValue = (String)super.getValue();
      return var2;
   }

   public TextFieldComponent computeTextFieldComponent(Supplier<Boolean> value) {
      return (TextFieldComponent)super.withValue(value);
   }

   public TextFieldComponent onAction(Runnable action) {
      return (TextFieldComponent)super.withOnAction(this::lambda$onAction$0);
   }

   public TextFieldComponent computeVisible(Runnable action) {
      return (TextFieldComponent)super.withOnSetVisible(action);
   }

   public String getValue() {
      if (this.cachedValue == null) {
         this.cachedValue = (String)super.getValue();
      }

      return this.cachedValue;
   }

      public boolean isOnlyNumber() {
      return this.onlyNumber;
   }

      public String getCachedValue() {
      return this.cachedValue;
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
      return this.computeTextFieldComponent(param1);
   }

   @Override
   public Component withValue(String param1) {
      return this.withCachedValue(param1);
   }

   public void lambda$onAction$0(Runnable action) {
      action.run();
      this.cachedValue = (String)super.getValue();
   }
}
