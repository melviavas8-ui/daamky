package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.KeyBindingUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "Strafe",
   category = ModuleCategory.MOTION,
   description = "Позволяет свободно стрейфиться и контролировать движение в воздухе"
)
public class Strafe extends Module {
   public static final Strafe mitikc = new Strafe();
   public final BooleanSetting autoJump = new BooleanSetting("Auto Jump").value(true);
   public final BooleanSetting damageBoost = new BooleanSetting("Damage Boost").value(true);
   public final FloatSetting damageSpeed = new FloatSetting("Damage Speed").value(0.4F).range(0.05F, 0.8F).step(0.01F);
   public double doubleVal;
   public int intVal;
   public int mxijm;

   public Strafe() {
      this.addSettings(new Setting[]{this.autoJump, this.damageBoost, this.damageSpeed});
   }

   public static Strafe getInstance() {
      return mitikc;
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   @Override
   public void onDisable() {
      this.run();
   }

   public void runAuto() {
      if (mc.player != null && mc.world != null) {
         if (KeyBindingUtil.check4()
            && !mc.player.isGliding()
            && !mc.player.isClimbing()
            && !mc.player.isTouchingWater()
            && !mc.player.isInLava()) {
            if (this.autoJump.getValue() && mc.player.isOnGround() && !mc.options.jumpKey.isPressed()) {
               mc.player.jump();
            }

            double var1;
            if (!((var1 = this.getDamage()) <= 0.0)) {
               this.handleCos(var1);
               this.doubleVal = Math.hypot(mc.player.getVelocity().x, mc.player.getVelocity().z);
            }
         } else {
            this.run();
         }
      }
   }

   public double getDamage() {
      double var2 = this.getMc();
      boolean var1 = this.damageBoost.getValue() && mc.player.hurtTime > 0;
      if (mc.player.isOnGround()) {
         this.mxijm++;
         this.intVal = 0;
         return var1 ? (double)this.damageSpeed.getValue().floatValue() : (this.autoJump.getValue() ? 0.42 : var2);
      } else {
         this.intVal++;
         this.mxijm = 0;
         double var5 = this.doubleVal - this.doubleVal / 159.0;
         double var7 = var1 ? (double)this.damageSpeed.getValue().floatValue() : 0.0;
         return Math.max(var2, Math.max(var5, var2 + var7 * 0.35));
      }
   }

   public double getMc() {
      double var2 = 0.2873;
      if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
         int var1 = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
         var2 *= 1.0 + 0.2 * (double)(var1 + 1);
      }

      if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
         int var4 = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
         var2 /= 1.0 + 0.2 * (double)(var4 + 1);
      }

      return var2;
   }

   public void handleCos(double speed) {
      float var3 = mc.player.input.movementForward;
      float var4 = mc.player.input.movementSideways;
      float var5 = mc.player.getYaw();
      if (var3 != 0.0F || var4 != 0.0F) {
         if (var3 != 0.0F) {
            if (var4 > 0.0F) {
               var5 += var3 > 0.0F ? -45.0F : 45.0F;
            } else if (var4 < 0.0F) {
               var5 += var3 > 0.0F ? 45.0F : -45.0F;
            }

            var4 = 0.0F;
            var3 = MathHelper.clamp(var3 > 0.0F ? 1.0F : -1.0F, -1.0F, 1.0F);
         }

         var4 = MathHelper.clamp(var4, -1.0F, 1.0F);
         double var6 = Math.toRadians((double)(var5 + 90.0F));
         double var8 = Math.sin(var6);
         double var10 = Math.cos(var6);
         double var12 = (double)var3 * speed * var10 + (double)var4 * speed * var8;
         double var14 = (double)var3 * speed * var8 - (double)var4 * speed * var10;
         mc.player.setVelocity(var12, mc.player.getVelocity().y, var14);
      }
   }

   public void run() {
      this.doubleVal = 0.0;
      this.intVal = 0;
      this.mxijm = 0;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.runAuto();
   }
}
