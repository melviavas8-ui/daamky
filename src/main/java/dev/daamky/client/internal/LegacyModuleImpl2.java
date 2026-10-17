package dev.daamky.client.internal;

import dev.daamky.client.gui.component.NumberComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.setting.IntegerSetting;

public class LegacyModuleImpl2 extends NumberComponent {
   public LegacyModuleImpl2(final LegacyModule this$0, Module parent, String name, Integer value, final IntegerSetting param5) {
      super(parent, name, value);
      this.intValValegerSetting = param5;
   }

   public NumberComponent withCachedValue(Integer value) {
      super.withCachedValue(value);
      this.intValValegerSetting.setValue(value);
      return this;
   }

   public Integer getCachedValue() {
      return ((Number)this.intValValegerSetting.getValue()).intValue();
   }

   public Integer getValue() {
      return ((Number)this.intValValegerSetting.getValue()).intValue();
   }

   public Component withValue(Integer param1) {
      return this.withCachedValue(param1);
   }

}
