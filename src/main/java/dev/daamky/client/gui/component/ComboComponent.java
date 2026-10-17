package dev.daamky.client.gui.component;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.legacy.module.Module;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ComboComponent extends Component<String> {
   public List<String> list;
   public String string;
   public Animation animation = new Direction2(300, 1.0);
   public Animation animation2 = new Direction2(300, 1.0);
   public Animation animation3 = new Direction2(300, 1.0);
   public boolean miereh;

   public ComboComponent(Module parent, String name, String... values) {
      super(parent, name, values != null && values.length > 0 ? values[0] : "Default");
      this.list = values != null ? List.of(values) : Collections.emptyList();
      this.string = (String)super.getValue();
   }

   public int getNext() {
      int var1 = 0;

      for (String var3 : this.list) {
         if (var3.equalsIgnoreCase(this.getValue())) {
            return var1;
         }

         var1++;
      }

      return 0;
   }

   public boolean computeCase(String value) {
      return this.getValue().equalsIgnoreCase(value) && this.getVisible().get();
   }

   public ComboComponent withValue(String value) {
      ComboComponent var2 = (ComboComponent)super.withValue(value);
      this.string = (String)super.getValue();
      return var2;
   }

   public ComboComponent computeComboComponent(Supplier<Boolean> value) {
      return (ComboComponent)super.withValue(value);
   }

   public ComboComponent onAction(Runnable action) {
      return (ComboComponent)super.withOnAction(() -> this.lambda$onAction$0(action));
   }

   public ComboComponent computeVisible(Runnable action) {
      return (ComboComponent)super.withOnSetVisible(action);
   }

   public String getValue() {
      if (this.string == null) {
         this.string = (String)super.getValue();
      }

      return this.string;
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
      return this.computeComboComponent(param1);
   }

   public void lambda$onAction$0(Runnable action) {
      action.run();
      this.string = (String)super.getValue();
   }
}
