package dev.daamky.client.internal;

import dev.daamky.client.gui.component.CheckComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.setting.BooleanSetting;

public class LegacyModuleImpl6 extends CheckComponent {
   public LegacyModuleImpl6(final LegacyModule this$0, Module parent, String name, Boolean value, final BooleanSetting param5) {
      super(parent, name, value);
      this.cachedValue = param5;
   }

   public CheckComponent withCachedValue(Boolean value) {
      super.withCachedValue(value);
      this.cachedValue.setValue(value);
      return this;
   }

   public Boolean getCachedValue() {
      return (Boolean)this.cachedValue.getValue();
   }

   public Object getValue() {
      return this.getCachedValue();
   }

   public Component withValue(Boolean param1) {
      return this.withCachedValue(param1);
   }

}
