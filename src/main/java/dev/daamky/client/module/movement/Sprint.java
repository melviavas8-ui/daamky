package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.SprintEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.FromRotationHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "Sprint",
   category = ModuleCategory.MOTION,
   description = "Автоматически использует спринт (бег)"
)
public class Sprint extends Module {
   public static final Sprint sprint = new Sprint();
   public final ModeSetting modeSetting = new ModeSetting("Mode").value("Legit").values("Legit", "Packet", "Grim", "None");

   public Sprint() {
      this.addSettings(new Setting[]{this.modeSetting});
      this.setEnabled(true);
   }

   @Override
   public void onEvent() {
      Subscription var1 = SprintEvent.getInstance().subscribe(new EventListener<>(1, this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public boolean getMc() {
      if (mc.player == null) {
         return false;
      } else if (mc.player.hasVehicle()) {
         return false;
      } else if (mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
         return false;
      } else if (mc.player.isUsingItem()) {
         return false;
      } else {
         return mc.player.input != null && mc.player.input.movementForward <= 0.0F
            ? false
            : mc.player.getHungerManager().getFoodLevel() > 6 || mc.player.getAbilities().allowFlying;
      }
   }

   public boolean checkEnabled() {
      AttackAura var1 = AttackAura.getInstance();
      if (var1 != null && var1.isEnabled() && var1.checkCrits2()) {
         return false;
      } else {
         return this.modeSetting.is("Grim")
            ? (mc.options.sprintKey.isPressed() || this.initCurrent()) && (var1 == null || var1.class_1309 == null || !var1.isEnabled())
            : (!mc.options.sprintKey.isPressed() || mc.player.input.movementForward != 0.0F) && this.initCurrent();
      }
   }

   public boolean initCurrent() {
      VecRotationHelper var1 = VecRotationHelper.getInstance();
      FromRotationHelper var2 = var1.getLast();
      if (var2 != null && !(var2.getProvider() instanceof Strafe) && !var2.isMoveCorrection() && !this.modeSetting.is("None")) {
         Rotation var3 = var1.getCurrentRotation() != null
            ? var1.getCurrentRotation()
            : new Rotation(mc.player.getYaw(), mc.player.getPitch());
         float var4 = mc.player.getYaw() - var3.getYaw();
         float var5 = mc.player.input.movementForward;
         float var6 = mc.player.input.movementSideways;
         return var5 * MathHelper.cos(var4 * (float) (Math.PI / 180.0)) + var6 * MathHelper.sin(var4 * (float) (Math.PI / 180.0)) <= 1.0E-5F;
      } else {
         return false;
      }
   }

   public static Sprint getInstance() {
      return sprint;
   }

   public void lambda$onEvent$0(SprintEvent.SprintEventData event) {
      AttackAura var2 = AttackAura.getInstance();
      if (var2 != null && var2.isEnabled() && var2.checkCrits2()) {
         event.setSprint(false);
      } else {
         if (!this.checkEnabled() && this.getMc()) {
            event.setSprint(true);
         }
      }
   }
}
