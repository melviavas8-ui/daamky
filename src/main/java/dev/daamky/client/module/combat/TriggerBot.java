package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.mixin.ClientPlayerEntityAccessor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.misc.FakePlayer;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "TriggerBot",
   category = ModuleCategory.BATTLE,
   description = "Автоматически бьет, когда вы наводитесь на врага"
)
public class TriggerBot extends Module {
   public static TriggerBot triggerBot;
   public final MultiBooleanSetting friends = new MultiBooleanSetting("Targets")
      .value(
         new BooleanSetting("Players").value(true),
         new BooleanSetting("Naked").value(true),
         new BooleanSetting("Mobs").value(false),
         new BooleanSetting("Invisibles").value(true),
         new BooleanSetting("Friends").value(false)
      );
   public final MultiBooleanSetting weaponOnly = new MultiBooleanSetting("Options")
      .value(new BooleanSetting("Target ESP").value(true), new BooleanSetting("Weapon only").value(true));
   public final ModeSetting smartCrits = new ModeSetting("Crit mode").value("Smart Crits").values("Only Crits", "Smart Crits");
   public final ModeSetting modeSetting = new ModeSetting("Sprint reset").value("Smart").values("Smart", "None");
   public final ModeSetting modeSetting2 = new ModeSetting("Delay mode").value("Neuro").values("Neuro", "Default");
   public Subscription subscription;
   public Subscription subscription2;
   public int intVal = 0;
   public LivingEntity class_1309 = null;
   public long longVal = 0L;
   public int intVal2 = 0;

   public static TriggerBot getInstance() {
      if (triggerBot == null) {
         triggerBot = new TriggerBot();
      }

      return triggerBot;
   }

   public TriggerBot() {
      this.addSettings(new Setting[]{this.friends, this.weaponOnly, this.smartCrits, this.modeSetting, this.modeSetting2});
   }

   @Override
   public void onEvent() {
      this.subscription = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{this.subscription});
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.class_1309 = null;
      this.intVal = 0;
      this.longVal = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.class_1309 = null;
      this.intVal2 = 0;
   }

   public void runOnly() {
      if (mc.player != null && mc.world != null) {
         if (this.weaponOnly.isEnabled("Weapon only")) {
            boolean var3 = mc.player.getMainHandStack().getItem() instanceof SwordItem || mc.player.getMainHandStack().getItem() instanceof AxeItem;
            if (!var3) {
               this.class_1309 = null;
               return;
            }
         }

         if (this.intVal2 > 0) {
            this.intVal2--;
            mc.player.setSprinting(false);
            if (this.intVal2 == 0) {
               mc.player.setSprinting(true);
            }
         }

         boolean var6 = mc.player.isOnGround();
         double var4 = mc.player.getVelocity().y;
         if (var6) {
            if (this.longVal == 0L) {
               this.longVal = System.currentTimeMillis();
            }

            this.intVal = 0;
         } else {
            this.longVal = 0L;
            this.intVal = var4 < -0.03 ? ++this.intVal : 0;
         }

         this.class_1309 = null;
         LivingEntity var1;
         Entity var2;
         if (mc.crosshairTarget != null
            && mc.crosshairTarget.getType() == Type.ENTITY
            && (var2 = ((EntityHitResult)mc.crosshairTarget).getEntity()) instanceof LivingEntity
            && this.computeEnabled(var1 = (LivingEntity)var2)) {
            this.class_1309 = var1;
            this.handleAbs(var1, var6, var4);
         }
      }
   }

   public void handleAbs(LivingEntity target, boolean wasOnGround, double velY) {
      if (!mc.player.isUsingItem()) {
         boolean var6 = mc.player.isTouchingWater() || mc.player.isInLava();
         boolean var7 = mc.player.hasStatusEffect(StatusEffects.BLINDNESS);
         boolean var8 = mc.player.hasVehicle();
         boolean var9 = mc.player.isClimbing();
         boolean var10 = mc.player.isGliding();
         boolean var11 = !wasOnGround
            && !var6
            && !var10
            && Math.abs(mc.player.getVelocity().x) < 0.005
            && Math.abs(mc.player.getVelocity().z) < 0.005
            && Math.abs(velY) < 0.01
            && mc.player.fallDistance < 0.01F;
         boolean var12 = false;
         boolean var13 = !wasOnGround && velY < -0.0 && mc.player.fallDistance > 0.0F && !var7 && !var8 && !var9 && this.intVal >= 1;
         if (var13) {
            var12 = true;
         } else if (var6 || var11 || var10) {
            var12 = true;
         } else if (wasOnGround && this.smartCrits.is("Smart Crits") && !mc.options.jumpKey.isPressed()) {
            var12 = true;
         }

         if (var12 || !this.smartCrits.is("Only Crits")) {
            float var5;
            if (this.modeSetting2.is("Neuro")) {
               float var14 = ThreadLocalRandom.current().nextFloat() * 0.05F - 0.025F;
               var5 = MathHelper.clamp(0.92F + var14, 0.83F, 0.95F);
            } else {
               var5 = 0.95F;
            }

            if (!(mc.player.getAttackCooldownProgress(0.5F) < var5)) {
               if (!mc.options.attackKey.isPressed()) {
                  String var19 = this.modeSetting.getValue();
                  if (!"None".equals(var19)) {
                     boolean var15 = !mc.player.isOnGround();
                     boolean var16 = !mc.player.isTouchingWater() && !mc.player.isInLava();
                     ClientPlayerEntityAccessor var17 = (ClientPlayerEntityAccessor)mc.player;
                     if (var15 && var16) {
                        boolean var18 = var17.getLastSprinting() || mc.player.isSprinting();
                        if ("Smart".equals(var19) && var18) {
                           mc.player.setSprinting(false);
                           if (mc.options.sprintKey != null) {
                              mc.options.sprintKey.setPressed(false);
                           }

                           this.intVal2 = 1;
                        }
                     }
                  }

                  mc.interactionManager.attackEntity(mc.player, target);
                  mc.player.swingHand(Hand.MAIN_HAND);
                  mc.player.resetLastAttackedTicks();
                  this.intVal2 = 2;
               }
            }
         }
      }
   }

   public boolean computeEnabled(LivingEntity entity) {
      if (entity == mc.player || !entity.isAlive() || entity.getHealth() <= 0.0F || entity instanceof ArmorStandEntity) {
         return false;
      } else if (FakePlayer.getInstance().computeFlag(entity)) {
         return true;
      } else if (entity.isInvisible() && !this.friends.isEnabled("Invisibles")) {
         return false;
      } else {
         if (entity instanceof PlayerEntity var2) {
            if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(entity.getUuid()) == null) {
               return false;
            }

            if (FriendsStore.getInstance().computeContains(var2.getName().getString()) && !this.friends.isEnabled("Friends")) {
               return false;
            }

            boolean var3 = !var2.getEquippedStack(EquipmentSlot.CHEST).isEmpty() || !var2.getEquippedStack(EquipmentSlot.LEGS).isEmpty();
            if (var3 && !this.friends.isEnabled("Players")) {
               return false;
            }

            if (!var3 && !this.friends.isEnabled("Naked")) {
               return false;
            }
         } else {
            if (!(entity instanceof MobEntity) && !(entity instanceof AnimalEntity)) {
               return false;
            }

            if (!this.friends.isEnabled("Mobs")) {
               return false;
            }
         }

         return true;
      }
   }

   public MultiBooleanSetting getWeaponOnly() {
      return this.weaponOnly;
   }

   public LivingEntity getClass_1309() {
      return this.class_1309;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.runOnly();
   }
}
