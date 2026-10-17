package dev.daamky.client.internal;

import dev.daamky.client.gui.component.CheckComponent;
import dev.daamky.client.setting.BooleanSetting;

public class LegacyModuleImpl8 extends CheckComponent {
   public LegacyModuleImpl8(final LegacyModule this$0, String name, Boolean value, final BooleanSetting param4) {
      super(name, value);
      this.cachedValue = param4;
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
