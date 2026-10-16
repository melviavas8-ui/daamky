package dev.daamky.client.internal;

import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.util.function.Supplier;

public class Vanilla extends NamedChoiceBase {
   public final FloatSetting speed = new FloatSetting("Speed").value(1.0F).range(0.1F, 5.0F).step(0.1F);

   @Override
   public String getName() {
      return "Vanilla";
   }

   public Vanilla(Supplier<Boolean> condition) {
      this.speed.visibleWhen(condition);
      this.addSettings(new Setting[]{this.speed});
   }

   @Override
   public void runSpeed() {
      KeyBindingUtil.handleDouble((double)this.speed.getValue().floatValue());
   }
}
