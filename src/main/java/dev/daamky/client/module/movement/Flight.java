package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.MotionEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.MotionEventData;
import dev.daamky.client.internal.NamedChoice;
import dev.daamky.client.internal.Vanilla2;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Flight",
   category = ModuleCategory.MOTION,
   description = "Позволяет летать как в креативе"
)
public class Flight extends Module {
   public static final Flight flight = new Flight();
   public final Vanilla2 vanilla2 = new Vanilla2(this::lambda$new$0);
   public final MotionEventData[] motionEventDataList = new MotionEventData[]{this.vanilla2};
   public MotionEventData motionEventData = this.vanilla2;
   public final ModeSetting mode = new ModeSetting("Mode")
      .value(this.vanilla2.getName())
      .values(NamedChoice.getValues(this.motionEventDataList))
      .onAction(this::lambda$new$1);

   public Flight() {
      this.addSettings(new Setting[]{this.mode});
      this.addSettings(this.vanilla2.getSettings());
   }

   @Override
   public void toggle() {
      super.toggle();
      this.motionEventData.run3();
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.motionEventData.run();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.motionEventData.run2();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var2 = MotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public static Flight getInstance() {
      return flight;
   }

   public ModeSetting getMode() {
      return this.mode;
   }

   public void lambda$onEvent$3(MotionEvent.MotionEventData event) {
      this.motionEventData.handleMotionEventData(event);
   }

   public void lambda$onEvent$2(UpdateEvent event) {
      this.motionEventData.runSpeed();
   }

   public void lambda$new$1() {
      if (this.isEnabled()) {
         this.motionEventData.run2();
      }

      this.motionEventData = (MotionEventData)NamedChoice.computeEquals(this.getMode().getValue(), this.motionEventDataList);
      if (this.isEnabled()) {
         this.motionEventData.run();
      }
   }

   public Boolean lambda$new$0() {
      return this.getMode().is("Vanilla");
   }
}
