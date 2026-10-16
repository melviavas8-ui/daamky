package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;

@ModuleInfo(
   name = "No Jump Delay",
   category = ModuleCategory.MOTION,
   description = "Убирает задержку перед следующим прыжком (Баннихоп)"
)
public class NoJumpDelay extends Module {
   public static final NoJumpDelay noJumpDelay = new NoJumpDelay();

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(NoJumpDelay::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static NoJumpDelay getInstance() {
      return noJumpDelay;
   }

   public static void lambda$onEvent$0(UpdateEvent event) {
      if (mc.player != null) {
         mc.player.jumpingCooldown = 0;
      }
   }
}
