package dev.daamky.client.setting;

import dev.daamky.client.internal.ValueHelper;
import dev.daamky.client.internal.ValueToHelper;
import java.util.function.Supplier;

public class ButtonSetting extends Setting<Runnable> {
   public final ValueToHelper pressAnim = new ValueToHelper();

   public ButtonSetting(String name) {
      super(name);
   }

   public ButtonSetting action(Runnable value) {
      this.value = value;
      return this;
   }

   public void press() {
      if (this.value != null) {
         this.value.run();
      }

      this.pressAnim.handleAction(1.0);
      this.pressAnim.computeValueToHelper(0.0, 0.45F, ValueHelper.applyFunction23);
   }

   public void resetAnimation() {
      this.press();
   }

   public ButtonSetting visibleWhen(Supplier<Boolean> condition) {
      return (ButtonSetting)super.visibleWhen(condition);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.action((Runnable)var1);
   }

}
