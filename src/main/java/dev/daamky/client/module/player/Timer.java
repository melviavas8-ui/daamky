package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.FloatValueHelper;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Timer",
   category = ModuleCategory.ACTOR,
   description = "Ускоряет или замедляет время в игре"
)
public class Timer extends Module {
   public static final Timer mwuotb = new Timer();
   public final ModeSetting matrix = new ModeSetting("Mode").value("Normal").values("Normal", "Grim", "Matrix");
   public final FloatSetting multiplier = new FloatSetting("Multiplier").value(2.0F).range(0.1F, 5.0F).step(0.1F);

   public Timer() {
      this.addSettings(new Setting[]{this.matrix, this.multiplier});
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static Timer getInstance() {
      return mwuotb;
   }

   public void lambda$onEvent$0(TickEvent event) {
      float var2 = this.multiplier.getValue();
      String var3 = this.matrix.getValue();
      switch (var3) {
         case "Grim":
            if (var2 > 1.05F) {
               var2 = 1.05F;
            }
            break;
         case "Matrix":
            if (var2 > 1.15F) {
               var2 = 1.15F;
            }
      }

      FloatValueHelper.getInstance().initAdd(var2, Priority.NORMAL, this, 1);
   }
}
