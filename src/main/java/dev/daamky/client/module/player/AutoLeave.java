package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.client.network.AbstractClientPlayerEntity;

@ModuleInfo(
   name = "Auto Leave",
   category = ModuleCategory.ACTOR,
   description = "Автоматически выходит с сервера при малом здоровье"
)
public class AutoLeave extends Module {
   public static final AutoLeave autoLeave = new AutoLeave();
   public final FloatSetting distance = new FloatSetting("Distance").value(50.0F).range(1.0F, 100.0F).step(1.0F);
   public final ModeSetting home = new ModeSetting("Action").value("Spawn").values("Hub", "Spawn", "Home");

   public AutoLeave() {
      this.addSettings(new Setting[]{this.distance, this.home});
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void runNext() {
      for (AbstractClientPlayerEntity var2 : mc.world.getPlayers()) {
         if (mc.player != var2
            && !FriendsStore.getInstance().computeContains(var2.getName().getString())
            && var2.getPos().distanceTo(mc.player.getPos()) <= (double)this.distance.getValue().floatValue()) {
            this.run();
            this.toggle();
            break;
         }
      }
   }

   public void run() {
      String var1 = this.home.getValue();
      switch (var1) {
         case "Hub":
            mc.player.networkHandler.sendChatCommand("hub");
            break;
         case "Spawn":
            mc.player.networkHandler.sendChatCommand("spawn");
            break;
         case "Home":
            mc.player.networkHandler.sendChatCommand("home home");
      }
   }

   public static AutoLeave getInstance() {
      return autoLeave;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.runNext();
   }
}
