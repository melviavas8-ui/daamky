package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TravelEvent;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.NamedChoice;
import dev.daamky.client.internal.NamedChoiceBase;
import dev.daamky.client.internal.PushBoost;
import dev.daamky.client.internal.Vanilla;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Speed",
   category = ModuleCategory.MOTION,
   description = "Speed"
)
public class Speed extends Module {
   public static final Speed speed = new Speed();
   public final PushBoost pushBoost = new PushBoost();
   public final Vanilla mhoqs = new Vanilla(this::lambda$new$0);
   public final NamedChoiceBase[] namedChoiceBaseList = new NamedChoiceBase[]{this.mhoqs, this.pushBoost};
   public NamedChoiceBase namedChoiceBase = this.pushBoost;
   public final ModeSetting mode = new ModeSetting("Mode")
      .value(this.pushBoost.getName())
      .values(NamedChoice.getValues(this.namedChoiceBaseList))
      .onAction(this::lambda$new$1);

   public Speed() {
      this.addSettings(new Setting[]{this.mode});
      this.addSettings(this.pushBoost.getSettings());
      this.addSettings(this.mhoqs.getSettings());
   }

   @Override
   public void toggle() {
      super.toggle();
      this.namedChoiceBase.run2();
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.namedChoiceBase.onEnable();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.namedChoiceBase.runUnsubscribe();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var2 = TravelEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public static Speed getInstance() {
      return speed;
   }

   public ModeSetting getMode() {
      return this.mode;
   }

   public void lambda$onEvent$3(TravelEvent event) {
      this.namedChoiceBase.runSpeed();
   }

   public void lambda$onEvent$2(UpdateEvent event) {
      this.namedChoiceBase.run();
   }

   public void lambda$new$1() {
      if (this.isEnabled()) {
         this.namedChoiceBase.runUnsubscribe();
      }

      this.namedChoiceBase = (NamedChoiceBase)NamedChoice.computeEquals(this.getMode().getValue(), this.namedChoiceBaseList);
      if (this.isEnabled()) {
         this.namedChoiceBase.onEnable();
      }
   }

   public Boolean lambda$new$0() {
      return this.getMode().is("Vanilla");
   }
}
