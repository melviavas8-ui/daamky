package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.SprintEvent;
import dev.daamky.client.module.movement.Sprint;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class SprintState implements MinecraftAccess {
   public final BaseTimeHelper baseTimeHelper = new BaseTimeHelper();
   public final SprintTypeHelper sprintTypeHelper = new SprintTypeHelper(SprintTypeHelper.SprintType.LEGIT);
   public final InventoryHelper inventoryHelper = new InventoryHelper();
   public OptionsHelper.CombatConfigurable configurable;

   public SprintState() {
      SprintEvent.getInstance().subscribe(new EventListener<>(1, this::lambda$new$0));
   }

   public void run() {
      SprintTypeHelper var10000 = this.sprintTypeHelper;
      String var1 = Sprint.getInstance().modeSetting.getValue();

      var10000.sprintType = switch (var1) {
         case "Packet" -> SprintTypeHelper.SprintType.PACKET;
         case "Legit" -> SprintTypeHelper.SprintType.LEGIT;
         default -> SprintTypeHelper.SprintType.NONE;
      };
      boolean var3 = this.checkConfigurable();
      if (var3) {
         if (this.computeConfigurable(this.getConfigurable())) {
            return;
         }

         if (mc.player.isBlocking() && this.configurable.alwaysShield) {
            mc.interactionManager.stopUsingItem(mc.player);
         }

         this.sprintTypeHelper.handlePacket(false);
         mc.interactionManager.attackEntity(mc.player, this.configurable.target);
         mc.player.swingHand(Hand.MAIN_HAND);
         this.sprintTypeHelper.handlePacket(true);
         this.baseTimeHelper.setDelay(500L);
      }
   }

   public boolean checkConfigurable() {
      if (this.check()) {
         return false;
      } else if (this.configurable.noAttackIfEat && CollisionUtil.check()) {
         return false;
      } else {
         if (this.configurable.shieldBreak) {
            LivingEntity var2 = this.configurable.target;
            PlayerEntity var1;
            if (this.configurable.target instanceof PlayerEntity && this.inventoryHelper.shouldBreakShield(var1 = (PlayerEntity)var2)) {
               return true;
            }
         }

         if (!mc.options.jumpKey.isPressed() && mc.player.isOnGround() && this.configurable.onlyCrits && this.configurable.smartCrits) {
            return true;
         } else {
            return !mc.options.jumpKey.isPressed() && CollisionUtil.check2() ? true : this.check2() || !this.configurable.onlyCrits;
         }
      }
   }

   public boolean computeConfigurable(Entity targetEntity) {
      return this.configurable.flag && targetEntity != this.configurable.target && !mc.player.isGliding();
   }

   public boolean check() {
      return !this.baseTimeHelper.checkCurrent();
   }

   public boolean getMc() {
      return mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
         || mc.player.hasStatusEffect(StatusEffects.LEVITATION)
         || mc.player.hasStatusEffect(StatusEffects.SLOW_FALLING)
         || CollisionUtil.isInWeb()
         || mc.player.isInLava()
         || mc.player.isClimbing()
         || mc.player.isRiding()
         || mc.player.hasVehicle()
         || mc.player.isSubmergedInWater()
         || mc.player.hasNoGravity()
         || mc.player.getAbilities().flying;
   }

   public boolean check2() {
      return !mc.player.isOnGround() && mc.player.fallDistance > 0.0F || this.getMc();
   }

   public BaseTimeHelper getBaseTimeHelper() {
      return this.baseTimeHelper;
   }

   public SprintTypeHelper getSprintTypeHelper() {
      return this.sprintTypeHelper;
   }

   public InventoryHelper getInventoryHelper() {
      return this.inventoryHelper;
   }

   public OptionsHelper.CombatConfigurable getConfigurable() {
      return this.configurable;
   }

   public SprintState withConfigurable(OptionsHelper.CombatConfigurable configurable) {
      this.configurable = configurable;
      return this;
   }

   public void lambda$new$0(SprintEvent.SprintEventData event) {
      boolean var2 = this.baseTimeHelper.check();
      boolean var3 = this.configurable != null
         && this.configurable.target != null
         && this.configurable.onlyCrits
         && !mc.player.isOnGround()
         && !this.getMc()
         && var2
         && mc.player.getVelocity().y <= 0.16477328182606651;
      this.sprintTypeHelper.handleLegit(event, var3);
   }
}
