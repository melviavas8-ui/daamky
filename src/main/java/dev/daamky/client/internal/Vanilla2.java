package dev.daamky.client.internal;

import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.util.function.Supplier;

public class Vanilla2 extends MotionEventData {
   public final FloatSetting speedH = new FloatSetting("Speed H").value(1.0F).range(0.1F, 5.0F).step(0.1F);
   public final FloatSetting speedV = new FloatSetting("Speed V").value(1.0F).range(0.1F, 5.0F).step(0.1F);

   @Override
   public String getName() {
      return "Vanilla";
   }

   public Vanilla2(Supplier<Boolean> condition) {
      this.addSettings(new Setting[]{this.speedH, this.speedV});

      for (Setting var3 : this.getSettings()) {
         var3.visibleWhen(condition);
      }
   }

   @Override
   public void runSpeed() {
      KeyBindingUtil.handleDouble((double)this.speedH.getValue().floatValue());
      float var1 = this.speedV.getValue();
      mc.player.setVelocity(mc.player.getVelocity().x, mc.options.jumpKey.isPressed()
         ? (double)var1
         : (mc.options.sneakKey.isPressed() ? (double)(-var1) : 0.0), mc.player.getVelocity().z);
   }
}
