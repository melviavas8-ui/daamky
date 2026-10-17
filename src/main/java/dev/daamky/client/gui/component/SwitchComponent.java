package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.ValueHelper;
import dev.daamky.client.internal.ValueToHelper;
import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class SwitchComponent extends Component<Boolean> {
   public final Runnable action;
   public final ValueToHelper valueToHelper = new ValueToHelper();

   public SwitchComponent(Module parent, String name, Runnable action) {
      super(parent, name, false);
      this.action = action;
   }

   public void runAction() {
      if (this.action != null) {
         this.action.run();
      }

      this.valueToHelper.handleAction(1.0);
      this.valueToHelper.computeValueToHelper(0.0, 0.45F, ValueHelper.applyFunction23);
   }

   public SwitchComponent computeSwitchComponent(Supplier<Boolean> value) {
      return (SwitchComponent)super.withValue(value);
   }

   @Override
   public Component withValue(Supplier param1) {
      return this.computeSwitchComponent(param1);
   }
}
