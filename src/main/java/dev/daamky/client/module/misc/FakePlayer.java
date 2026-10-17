package dev.daamky.client.module.misc;

import com.mojang.authlib.GameProfile;
import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.KeyEvent;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.Profile;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EquipmentSlot.Type;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "FakePlayer",
   category = ModuleCategory.MICS,
   description = "Создает тренировочного бота для отработки ударов и проверки киллауры"
)
public class FakePlayer extends Module {
   public static final FakePlayer mquaq = new FakePlayer();
   public Profile profile;
   public float floatVal = 0.0F;
   public float float2 = 0.0F;

   public static FakePlayer getInstance() {
      return mquaq;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.initInit();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.run();
      this.floatVal = 0.0F;
      this.float2 = 0.0F;
   }

   @Override
   public void onEvent() {
      Subscription var1 = KeyEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = AttackEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public void initInit() {
      if (mc.player != null && mc.world != null) {
         this.run();
         GameProfile var1 = new GameProfile(UUID.fromString("66123666-6666-6666-6666-666666666600"), "FakePlayer");
         this.profile = new Profile(mc.world, var1);
         this.profile.copyPositionAndRotation(mc.player);
         this.profile.setStackInHand(Hand.MAIN_HAND, mc.player.getMainHandStack().copy());
         this.profile.setStackInHand(Hand.OFF_HAND, mc.player.getOffHandStack().copy());

         for (EquipmentSlot var5 : EquipmentSlot.values()) {
            if (var5.getType() == Type.HUMANOID_ARMOR) {
               this.profile.equipStack(var5, mc.player.getEquippedStack(var5).copy());
            }
         }

         this.profile.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 9999, 2));
         this.profile.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 9999, 4));
         this.profile.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 9999, 1));
         this.profile.run();
      }
   }

   public void run() {
      if (this.profile != null) {
         this.profile.run2();
         this.profile = null;
      }
   }

   public Profile getProfile() {
      return this.profile;
   }

   public boolean computeFlag(Entity entity) {
      return entity != null && (entity == this.profile || entity instanceof Profile);
   }

   public void handleEnabled(PlayerEntity attacker, Entity target) {
      if (this.isEnabled() && this.profile != null && attacker != null) {
         if (target == this.profile && this.profile.hurtTime == 0) {
            this.initRandom(attacker, this.profile);
         }
      }
   }

   public void initRandom(PlayerEntity attacker, Entity target) {
      if (this.profile != null && attacker != null && mc.world != null) {
         if (this.profile.hurtTime <= 0) {
            mc.world
               .playSound(
                  attacker,
                  this.profile.getX(),
                  this.profile.getY(),
                  this.profile.getZ(),
                  SoundEvents.ENTITY_PLAYER_HURT,
                  SoundCategory.PLAYERS,
                  1.0F,
                  1.0F
               );
            if (attacker.fallDistance > 0.0F) {
               mc.world
                  .playSound(
                     attacker,
                     this.profile.getX(),
                     this.profile.getY(),
                     this.profile.getZ(),
                     SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                     SoundCategory.PLAYERS,
                     1.0F,
                     1.0F
                  );

               for (int var3 = 0; var3 < 12; var3++) {
                  mc.world
                     .addParticle(
                        ParticleTypes.CRIT,
                        this.profile.getX() + (Math.random() - 0.5) * 0.8,
                        this.profile.getY() + 0.5 + Math.random() * 1.2,
                        this.profile.getZ() + (Math.random() - 0.5) * 0.8,
                        (Math.random() - 0.5) * 0.5,
                        0.2 + Math.random() * 0.3,
                        (Math.random() - 0.5) * 0.5
                     );
               }
            } else {
               mc.world
                  .playSound(
                     attacker,
                     this.profile.getX(),
                     this.profile.getY(),
                     this.profile.getZ(),
                     SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                     SoundCategory.PLAYERS,
                     1.0F,
                     1.0F
                  );
               mc.world
                  .addParticle(
                     ParticleTypes.SWEEP_ATTACK, this.profile.getX(), this.profile.getY() + 1.0, this.profile.getZ(), 0.0, 0.0, 0.0
                  );
            }

            this.profile.animateDamage(attacker.getYaw());
            this.profile.hurtTime = 10;
            this.profile.maxHurtTime = 10;
            this.profile.onDamaged(mc.world.getDamageSources().generic());
            this.profile.setHealth(this.profile.getHealth() + this.profile.getAbsorptionAmount() - 1.0F);
            if (this.profile.isDead() || this.profile.getHealth() <= 0.0F) {
               this.profile.setHealth(10.0F);
               EntityStatusS2CPacket var4 = new EntityStatusS2CPacket(this.profile, (byte)35);
               if (mc.player != null && mc.player.networkHandler != null) {
                  var4.apply(mc.player.networkHandler);
               }

               PacketEvent.getInstance().postAndCheckCancelled(new PacketEvent.PacketEventData(var4, PacketEvent.PacketEventData.PacketType.RECEIVE));
            }
         }
      }
   }

   public void lambda$onEvent$2(AttackEvent.AttackEventData data) {
      if (this.isEnabled() && this.profile != null && mc.player != null) {
         if (data.entity() == this.profile && this.profile.hurtTime == 0) {
            this.initRandom(mc.player, this.profile);
         }
      }
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      if (this.isEnabled() && this.profile != null && mc.player != null && mc.world != null) {
         if (mc.currentScreen != null) {
            this.floatVal = 0.0F;
            this.float2 = 0.0F;
            this.profile.setSprinting(false);
         } else if (this.floatVal == 0.0F && this.float2 == 0.0F) {
            this.profile.setSprinting(false);
            this.profile.setVelocity(0.0, this.profile.getVelocity().y, 0.0);
            this.profile.limbAnimator.setSpeed(0.0F);
         } else {
            float var2 = mc.player.getYaw();
            double var3 = 0.2;
            double var5 = (double)this.float2 * Math.cos(Math.toRadians((double)var2)) - (double)this.floatVal * Math.sin(Math.toRadians((double)var2));
            double var7 = (double)this.floatVal * Math.cos(Math.toRadians((double)var2)) + (double)this.float2 * Math.sin(Math.toRadians((double)var2));
            Vec3d var9 = new Vec3d(var5 * var3, this.profile.getVelocity().y, var7 * var3);
            this.profile.move(MovementType.SELF, var9);
            this.profile.setSprinting(true);
         }
      }
   }

   public void lambda$onEvent$0(KeyEvent.KeyEventData data) {
      if (this.isEnabled() && this.profile != null && mc.currentScreen == null) {
         int var2 = data.key();
         int var3 = data.action();
         float var4 = 2.0F;
         if (var2 == 265) {
            this.floatVal = var3 != 0 ? var4 : 0.0F;
         } else if (var2 == 264) {
            this.floatVal = var3 != 0 ? -var4 : 0.0F;
         } else if (var2 == 263) {
            this.float2 = var3 != 0 ? var4 : 0.0F;
         } else if (var2 == 262) {
            this.float2 = var3 != 0 ? -var4 : 0.0F;
         }
      }
   }
}
