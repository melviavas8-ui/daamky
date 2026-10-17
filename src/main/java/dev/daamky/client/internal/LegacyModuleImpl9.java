package dev.daamky.client.internal;

import dev.daamky.client.gui.component.ColorPickerComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.setting.ColorSetting;
import java.awt.Color;

public class LegacyModuleImpl9 extends ColorPickerComponent {
   public LegacyModuleImpl9(final LegacyModule this$0, Module parent, String name, Integer value, final ColorSetting param5) {
      super(parent, name, value);
      this.colorSetting = param5;
   }

   public ColorPickerComponent withCachedValue(Integer value) {
      super.withCachedValue(value);
      this.colorSetting.handleAction(new Color(value, true));
      return this;
   }

   public Integer getCachedValue() {
      Color var1 = (Color)this.colorSetting.getValue();
      return var1 != null ? var1.getRGB() : -1;
   }

   public Object getValue() {
      return this.getCachedValue();
   }

   public Component withValue(Integer param1) {
      return this.withCachedValue(param1);
   }

}
