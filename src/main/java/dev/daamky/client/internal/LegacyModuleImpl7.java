package dev.daamky.client.internal;

import dev.daamky.client.gui.component.SliderComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.setting.FloatSetting;

public class LegacyModuleImpl7 extends SliderComponent {
   public LegacyModuleImpl7(
      final LegacyModule this$0, Module parent, String name, float value, float min, float max, float increment, final FloatSetting param8
   ) {
      super(parent, name, value, min, max, increment);
      this.mkeouon = var8;
   }

   public SliderComponent withCachedValue(Float value) {
      super.withCachedValue(value);
      this.mkeouon.setValue(value);
      return this;
   }

   public Float getCachedValue() {
      return ((Number)this.mkeouon.getValue()).floatValue();
   }

   public Object getValue() {
      return this.getCachedValue();
   }

   public Component withValue(Object param1) {
      return this.withCachedValue((Float)var1);
   }
}
