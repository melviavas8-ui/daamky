package dev.daamky.client.internal;

import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.item.Item;

public class Class_1792Holder extends BooleanSetting {
   public final Item icon;

   public Class_1792Holder(String name, Item icon) {
      super(name);
      this.icon = iconx;
   }

   public Class_1792Holder computeClass_1792Holder(Boolean value) {
      this.setValue(value);
      return this;
   }

   public Item getIcon() {
      return this.icon;
   }

   @Override
   public BooleanSetting value(Boolean param1) {
      return this.computeClass_1792Holder(var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.computeClass_1792Holder((Boolean)var1);
   }
}
