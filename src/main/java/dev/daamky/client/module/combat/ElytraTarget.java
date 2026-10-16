package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.RotationEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.CustomRotations;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;

@ModuleInfo(
   name = "Elytra Target",
   category = ModuleCategory.BATTLE,
   description = "Elytra Target"
)
public class ElytraTarget extends Module {
   public static final ElytraTarget elytraTarget = new ElytraTarget();
   public final CustomRotations customRotations = new CustomRotations(this);

   public ElytraTarget() {
      this.addSettings(this.customRotations.getSettings());
   }

   @Override
   public void onEvent() {
      Subscription var1 = RotationEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static ElytraTarget getInstance() {
      return elytraTarget;
   }

   public void lambda$onEvent$0(RotationEvent event) {
      this.customRotations.initInit();
   }
}
