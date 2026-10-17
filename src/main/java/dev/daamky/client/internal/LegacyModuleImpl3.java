package dev.daamky.client.internal;

import dev.daamky.client.gui.component.TextFieldComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.setting.StringSetting;

public class LegacyModuleImpl3 extends TextFieldComponent {
   public LegacyModuleImpl3(final LegacyModule this$0, Module parent, String name, String value, final StringSetting param5) {
      super(parent, name, value);
      this.stringSetting = param5;
   }

   public TextFieldComponent withCachedValue(String value) {
      super.withCachedValue(value);
      this.stringSetting.setValue(value);
      return this;
   }

   public String getValue() {
      return (String)this.stringSetting.getValue();
   }

   public Component withValue(String param1) {
      return this.withCachedValue(param1);
   }

}
