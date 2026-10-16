package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.GameTimer;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "AntiAFK",
   category = ModuleCategory.ACTOR,
   description = "Предотвращает кик с сервера за долгое бездействие"
)
public class AntiAFK extends Module {
   public static final AntiAFK antiAFK = new AntiAFK();
   public final ModeSetting modeSetting = new ModeSetting("Mode").value("FunTime").values("FunTime", "HolyWorld", "Default");
   public final FloatSetting intervalS = new FloatSetting("Interval (s)").value(60.0F).range(10.0F, 300.0F).step(5.0F);
   public int intVal = 0;
   public int meufijk = 0;
   public int int2 = 0;
   public float floatVal = 0.0F;
   public float float2 = 0.0F;

   public AntiAFK() {
      this.addSettings(new Setting[]{this.modeSetting, this.intValervalS});
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.getMc();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void run() {
      this.meufijk = 1;
      this.intVal = 0;
      String var1 = this.modeSetting.getValue();
      switch (var1) {
         case "FunTime":
            this.intVal2 = ThreadLocalRandom.current().nextInt(10, 30);
            this.floatVal = ThreadLocalRandom.current().nextFloat(-60.0F, 60.0F);
            this.floatVal2 = ThreadLocalRandom.current().nextFloat(-20.0F, 20.0F);
            break;
         case "HolyWorld":
            this.intVal2 = ThreadLocalRandom.current().nextInt(5, 15);
            this.floatVal = ThreadLocalRandom.current().nextFloat(-90.0F, 90.0F);
            this.floatVal2 = ThreadLocalRandom.current().nextFloat(-30.0F, 30.0F);
            break;
         case "Default":
            this.intVal2 = ThreadLocalRandom.current().nextInt(20, 40);
            this.floatVal = ThreadLocalRandom.current().nextFloat(-180.0F, 180.0F);
            this.floatVal2 = ThreadLocalRandom.current().nextFloat(-40.0F, 40.0F);
      }
   }

   public void run2() {
      this.intVal2--;
      if (this.floatVal != 0.0F || this.floatVal2 != 0.0F) {
         float var1 = this.floatVal * 0.15F;
         float var2 = this.floatVal2 * 0.15F;
         float var3 = GameTimer.getMc2();
         var1 = (float)Math.round(var1 / var3) * var3;
         var2 = (float)Math.round(var2 / var3) * var3;
         mc.player.setYaw(mc.player.getYaw() + var1);
         mc.player.setPitch(Math.max(-90.0F, Math.min(90.0F, mc.player.getPitch() + var2)));
         this.floatVal -= var1;
         this.floatVal2 -= var2;
         if (Math.abs(this.floatVal) < 1.0F) {
            this.floatVal = 0.0F;
         }

         if (Math.abs(this.floatVal2) < 1.0F) {
            this.floatVal2 = 0.0F;
         }
      }

      boolean var7 = false;
      boolean var9 = false;
      boolean var10 = false;
      String var4 = this.modeSetting.getValue();
      switch (var4) {
         case "FunTime":
            if (this.intVal2 % 4 == 0) {
               var9 = true;
            }

            if (this.intVal2 == 5) {
               mc.player.swingHand(Hand.MAIN_HAND);
            }
            break;
         case "HolyWorld":
            if (this.intVal2 == 10) {
               var7 = true;
            }

            if (this.intVal2 % 3 == 0) {
               mc.player.swingHand(Hand.MAIN_HAND);
            }
            break;
         case "Default":
            var10 = true;
            if (this.intVal2 == 15) {
               var7 = true;
            }
      }

      mc.options.jumpKey.setPressed(var7);
      mc.options.sneakKey.setPressed(var9);
      mc.options.forwardKey.setPressed(var10);
      if (this.intVal2 <= 0) {
         this.meufijk = 0;
         mc.options.jumpKey.setPressed(false);
         mc.options.sneakKey.setPressed(false);
         mc.options.forwardKey.setPressed(false);
      }
   }

   public void getMc() {
      this.intVal = 0;
      this.meufijk = 0;
      this.intVal2 = 0;
      this.floatVal = 0.0F;
      this.floatVal2 = 0.0F;
      if (mc.options != null) {
         mc.options.jumpKey.setPressed(false);
         mc.options.sneakKey.setPressed(false);
         mc.options.forwardKey.setPressed(false);
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.getMc();
   }

   public static AntiAFK getInstance() {
      return antiAFK;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      if (mc.player != null) {
         this.intVal++;
         int var2 = this.intValervalS.getValue().intValue() * 20;
         if (this.meufijk == 0) {
            if (this.intVal >= var2 + ThreadLocalRandom.current().nextInt(-40, 40)) {
               this.run();
            }
         } else {
            this.run2();
         }
      }
   }
}
