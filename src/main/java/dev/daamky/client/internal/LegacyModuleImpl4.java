package dev.daamky.client.internal;

import dev.daamky.client.gui.component.ComboComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.setting.ModeSetting;

public class LegacyModuleImpl4 extends ComboComponent {
   public LegacyModuleImpl4(final LegacyModule this$0, Module parent, String name, String[] values, final ModeSetting param5) {
      super(parent, name, values);
      this.modeSetting = var5;
   }

   public ComboComponent withValue(String value) {
      super.withValue(value);
      this.modeSetting.setValue(value);
      return this;
   }

   public String getValue() {
      return (String)this.modeSetting.getValue();
   }

   public Component withValue(Object param1) {
      return this.withValue((String)var1);
   }
}
