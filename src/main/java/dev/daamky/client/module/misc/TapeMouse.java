package dev.daamky.client.module.misc;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.function.Supplier;

@ModuleInfo(
   name = "Tape Mouse",
   category = ModuleCategory.MICS,
   description = "Имитирует зажатие кнопки мыши"
)
public class TapeMouse extends Module {
   public static final TapeMouse tapeMouse = new TapeMouse();
   public final MultiBooleanSetting use = new MultiBooleanSetting("Actions")
      .value(new BooleanSetting("Attack").value(true), new BooleanSetting("Use").value(false));
   public final Supplier<Boolean> supplier = this::lambda$new$0;
   public final Supplier<Boolean> supplier2 = this::lambda$new$1;
   public final FloatSetting attackDelay = new FloatSetting("Attack delay").value(10.0F).range(1.0F, 20.0F).step(1.0F).visibleWhen(this.supplier);
   public final FloatSetting useDelay = new FloatSetting("Use delay").value(10.0F).range(1.0F, 20.0F).step(1.0F).visibleWhen(this.supplier2);
   public final DelayHelper mcudl = new DelayHelper();
   public final DelayHelper delayHelper = new DelayHelper();

   public TapeMouse() {
      this.addSettings(new Setting[]{this.use, this.attackDelay, this.useDelay});
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$4));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void handleRun(float delay, DelayHelper timer, Runnable run) {
      if (timer.computeCurrent(delay * 50.0F)) {
         run.run();
         timer.runCurrent();
      }
   }

   public static TapeMouse getInstance() {
      return tapeMouse;
   }

   public void lambda$onEvent$4(TickEvent event) {
      if (this.supplier.get()) {
         this.handleRun(this.attackDelay.getValue(), this.mcudl, TapeMouse::lambda$onEvent$2);
      }

      if (this.supplier2.get()) {
         this.handleRun(this.useDelay.getValue(), this.delayHelper, TapeMouse::lambda$onEvent$3);
      }
   }

   public static void lambda$onEvent$3() {
      mc.doItemUse();
   }

   public static void lambda$onEvent$2() {
      mc.doAttack();
   }

   public Boolean lambda$new$1() {
      return this.use.isEnabled("Use");
   }

   public Boolean lambda$new$0() {
      return this.use.isEnabled("Attack");
   }
}
