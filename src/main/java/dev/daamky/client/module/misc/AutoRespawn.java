package dev.daamky.client.module.misc;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import net.minecraft.client.gui.screen.DeathScreen;

@ModuleInfo(
   name = "Auto Respawn",
   category = ModuleCategory.MICS,
   description = "Автоматически возрождается после смерти"
)
public class AutoRespawn extends Module {
   public static final AutoRespawn autoRespawn = new AutoRespawn();

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(AutoRespawn::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static AutoRespawn getInstance() {
      return autoRespawn;
   }

   public static void lambda$onEvent$0(TickEvent event) {
      if (mc.currentScreen instanceof DeathScreen && mc.player.deathTime > 2) {
         mc.player.requestRespawn();
         mc.setScreen(null);
      }
   }
}
