package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.RotationEvent;
import dev.daamky.client.event.SprintEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.EntityFilterHelper;
import dev.daamky.client.internal.FromRotationHelper;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.OffHelper;
import dev.daamky.client.internal.OptionsHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.RotationHelper;
import dev.daamky.client.internal.RotationMath;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.legacy.setting.MotionConfig;
import dev.daamky.client.legacy.setting.MotionConfig2;
import dev.daamky.client.legacy.setting.MotionConfig3;
import dev.daamky.client.legacy.setting.MotionConfig7;
import dev.daamky.client.legacy.setting.MotionConfig8;
import dev.daamky.client.mixin.ClientPlayerEntityAccessor;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.misc.FakePlayer;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

@ModuleInfo(
   name = "AttackAura",
   category = ModuleCategory.BATTLE,
   description = "Автоматически атакует врагов вокруг вас"
)
public class AttackAura extends Module {
   public static AttackAura attackAura;
   public static volatile boolean flag = false;
   public static volatile int intVal = -1;
   public final MotionConfig8 motionConfig8 = new MotionConfig8();
   public final MotionConfig motionConfig = new MotionConfig();
   public final MotionConfig7 motionConfig7 = new MotionConfig7();
   public final MotionConfig3 motionConfig3 = new MotionConfig3();
   public final MotionConfig2 motionConfig2 = new MotionConfig2();
   public final EntityFilterHelper entityFilterHelper = new EntityFilterHelper();
   public final OptionsHelper optionsHelper = new OptionsHelper();
   public final DelayHelper delayHelper = new DelayHelper();
   public final ModeSetting modeSetting = new ModeSetting("Aim mode").value("FunTime").values("FunTime", "SpookyTime", "ReallyWorld", "Neuro", "Custom");
   public final FloatSetting distance = new FloatSetting("Distance").value(3.0F).range(2.5F, 6.0F).step(0.01F);
   public final ModeSetting grim = new ModeSetting("Wall mode").value("Matrix").values("Matrix", "Grim");
   public final MultiBooleanSetting invisibles = new MultiBooleanSetting("Targets")
      .value(
         new BooleanSetting("Players").value(true),
         new BooleanSetting("Naked").value(true),
         new BooleanSetting("Mobs").value(false),
         new BooleanSetting("Invisibles").value(true)
      );
   public final ModeSetting health = new ModeSetting("Sort mode").value("Best").values("Best", "FOV", "Distance", "Health");
   public final ModeSetting adaptive = new ModeSetting("Attack delay").value("Fast").values("Fast", "Adaptive");
   public final ModeSetting smartCrits = new ModeSetting("Crit mode").value("Only Crits").values("Only Crits", "Smart Crits");
   public final ModeSetting modeSetting2 = new ModeSetting("Sprint reset").value("Legit").values("Legit", "Smart", "None");
   public final FloatSetting hitChance = new FloatSetting("Hit chance").value(100.0F).range(90.0F, 100.0F).step(1.0F);
   public final MultiBooleanSetting noAttackInContainer = new MultiBooleanSetting("Options")
      .value(
         new BooleanSetting("Shield break").value(true),
         new BooleanSetting("Always shield").value(false),
         new BooleanSetting("Ignore walls").value(false),
         new BooleanSetting("Sync with TPS").value(true),
         new BooleanSetting("No attack if eat").value(true),
         new BooleanSetting("Weapon only").value(false),
         new BooleanSetting("No attack in container").value(false)
      );
   public final ModeSetting target = new ModeSetting("Motion mode").value("Target").values("Free", "Target");
   public final BooleanSetting elytraOverride = new BooleanSetting("Elytra override").value(false);
   public final FloatSetting elytraDistance = new FloatSetting("Elytra distance")
      .value(4.0F)
      .range(2.5F, 6.0F)
      .step(0.1F)
      .visibleWhen(this.elytraOverride::getValue);
   public final FloatSetting elytraPreDistance = new FloatSetting("Elytra pre distance")
      .value(16.0F)
      .range(0.0F, 32.0F)
      .step(0.1F)
      .visibleWhen(this.elytraOverride::getValue);
   public LivingEntity class_1309;
   public LivingEntity class_13092;
   public long longVal;
   public static final long long2 = 30000L;
   public int int2;
   public long long3;
   public long long4;
   public static int int3 = 0;
   public int int4 = 0;
   public int int5 = 0;
   public int int6 = 0;
   public int int7 = -1;
   public Vec3d class_243;
   public float floatVal;
   public float meivp;
   public float float2 = 20.0F;
   public Box class_238;
   public long long5;
   public final List<LivingEntity> list = new ArrayList<>();
   public final Map<Integer, Boolean> map = new HashMap<>();
   public final Map<Integer, Long> map2 = new HashMap<>();
   public long long6 = 0L;
   public long mosif = -1L;
   public boolean flag2 = false;
   public Subscription muxum;
   public Subscription subscription;
   public Subscription subscription2;
   public Subscription msosupu;
   public Subscription subscription3;
   public int int8 = 0;
   public int int9 = 0;
   public boolean flag3 = false;

   public LivingEntity getClass_1309() {
      return this.class_1309;
   }

   public static AttackAura getInstance() {
      if (attackAura == null) {
         attackAura = new AttackAura();
      }

      return attackAura;
   }

   public AttackAura() {
      this.addSettings(
         new Setting[]{
            this.modeSetting,
            this.distance,
            this.grim,
            this.invisibles,
            this.health,
            this.adaptive,
            this.smartCrits,
            this.modeSetting2,
            this.hitChance,
            this.noAttackInContainer,
            this.target,
            this.elytraOverride,
            this.elytraDistance,
            this.elytraPreDistance
         }
      );
      this.grim.visibleWhen(this::lambda$new$0);
   }

   @Override
   public void onEvent() {
      this.muxum = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.subscription = RotationEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.subscription2 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.subscription3 = SprintEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$4));
      this.addEventListeners(new Subscription[]{this.muxum, this.subscription, this.subscription2, this.subscription3});
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.entityFilterHelper.runTarget();
      this.class_1309 = null;
      this.class_13092 = null;
      this.class_243 = null;
      OffHelper.run();
      this.int6 = 0;
      this.int7 = -1;
      this.int4 = 0;
      this.int5 = 0;
      this.flag2 = mc.player != null && mc.player.isSprinting();
      this.run();
   }

   @Override
   public void onDisable() {
      LegacySetting var1 = this.getLegacySetting();
      if (var1 != null) {
         var1.run();
      }

      VecRotationHelper.getInstance().handleRotation(this);
      super.onDisable();
      this.entityFilterHelper.runTarget();
      if (this.class_1309 != null) {
         this.class_1309.setGlowing(false);
      }

      this.class_1309 = null;
      this.class_13092 = null;
      this.class_243 = null;
      this.int7 = -1;
      flag = false;
      intVal = -1;
      this.int4 = 0;
      this.int5 = 0;
      OffHelper.run();
   }

   public void run() {
      this.motionConfig8.run3();
      this.motionConfig.run3();
      this.motionConfig7.runCurrent3();
      this.motionConfig3.run2();
      this.motionConfig2.run();
   }

   public void initAtan() {
      if (mc.player != null && mc.player.isAlive()) {
         if (int3 > 0) {
            int3--;
         } else {
            this.int6++;
            if (mc.player != null) {
               if (mc.player.isOnGround()) {
                  this.int8 = 0;
               } else {
                  this.int8++;
               }
            }

            if (mc.currentScreen != null && this.noAttackInContainer.isEnabled("No attack in container")) {
               if (this.class_1309 != null) {
                  this.class_1309 = null;
                  this.run();
               }
            } else {
               this.runNext();
               if (this.class_1309 != null) {
                  if (!this.noAttackInContainer.isEnabled("Ignore walls") && !this.initPut(this.class_1309)) {
                     this.class_1309 = null;
                     this.class_243 = null;
                     return;
                  }

                  this.class_243 = this.initGaussian(this.class_1309);
                  Vec3d var1 = this.class_243.subtract(mc.player.getEyePos());
                  double var2 = Math.sqrt(var1.x * var1.x + var1.z * var1.z);
                  float var4;
                  float var5;
                  if (var2 <= 0.08) {
                     var4 = this.floatVal;
                     var5 = MathHelper.clamp((float)(-Math.toDegrees(Math.atan2(var1.y, Math.max(0.1, var2)))), -89.0F, 89.0F);
                  } else {
                     var4 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var1.z, var1.x)) - 90.0);
                     var5 = (float)(-Math.toDegrees(Math.atan2(var1.y, var2)));
                  }

                  this.floatVal = var4;
                  this.meivp = var5;
                  Rotation var6 = new Rotation(var4, var5);
                  Rotation var7 = RotationMath.initInit(var6);
                  this.initFun(this.class_1309, this.class_243, var7);
                  LegacySetting var8 = this.getLegacySetting();
                  float var9 = this.computeFloat(0.5F);
                  if (var9 >= 0.75F || this.check2() || this.checkCrits()) {
                     if (var8 instanceof MotionConfig7 var10) {
                        var10.run();
                     }

                     if (var8 instanceof MotionConfig8 var11) {
                        var11.run2();
                     }

                     if (var8 instanceof MotionConfig3 var12) {
                        var12.run();
                     }

                     if (var8 instanceof MotionConfig var13) {
                        var13.run2();
                     }
                  }

                  this.initMax();
               } else if (this.class_243 != null) {
                  this.class_243 = null;
                  VecRotationHelper.getInstance().setProvider(this);
               }

               if (System.currentTimeMillis() - this.long6 > 5000L) {
                  this.map.clear();
                  this.map2.clear();
                  this.long6 = System.currentTimeMillis();
               }

               if (int3 > 0) {
                  int3--;
               }

               if (this.int4 > 0 && mc.player != null && this.int7 != mc.player.age) {
                  this.int4--;
               }

               if (this.int5 > 0 && mc.player != null && this.int7 != mc.player.age) {
                  this.int5--;
               }
            }
         }
      } else {
         if (this.isEnabled()) {
            this.toggle();
         }
      }
   }

   public void runYaw() {
      if (this.class_1309 != null) {
         Rotation var1 = VecRotationHelper.getInstance().getCurrent();
         if (var1 != null) {
            this.floatVal = var1.getYaw();
            this.meivp = var1.getPitch();
         }
      }
   }

   public boolean getMc() {
      return mc.player == null
         ? false
         : mc.options.jumpKey.isPressed() || mc.player.input != null && mc.player.input.playerInput.jump();
   }

   public boolean check() {
      if (mc.player != null && mc.world != null) {
         BlockPos var1 = BlockPos.ofFloored(mc.player.getEyePos());
         FluidState var2 = mc.world.getFluidState(var1);
         return !mc.player.hasStatusEffect(StatusEffects.LEVITATION)
            && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
            && !mc.player.hasStatusEffect(StatusEffects.SLOW_FALLING)
            && !var2.isIn(FluidTags.WATER)
            && !var2.isIn(FluidTags.LAVA)
            && !mc.player.isSubmergedInWater()
            && !mc.player.isInLava()
            && !mc.player.getAbilities().flying
            && !mc.player.isGliding()
            && !mc.player.isClimbing()
            && !mc.player.hasVehicle();
      } else {
         return false;
      }
   }

   public boolean checkIterator() {
      if (mc.player != null && mc.world != null) {
         double var1 = (mc.player.getVelocity().y - 0.08) * 0.98;
         if (var1 >= 0.0) {
            return false;
         } else {
            Box var3 = mc.player.getBoundingBox().offset(0.0, var1, 0.0);
            return mc.world.getBlockCollisions(mc.player, var3).iterator().hasNext();
         }
      } else {
         return false;
      }
   }

   public boolean check2() {
      ClientPlayerEntity var1 = mc.player;
      if (var1 == null || !this.check()) {
         return false;
      } else if (var1.isOnGround()) {
         return false;
      } else {
         double var2 = var1.getVelocity().y;
         if (var2 >= 0.0) {
            return false;
         } else if (var1.horizontalCollision) {
            return false;
         } else if (var2 > -0.2 && this.int8 < 8) {
            return false;
         } else {
            boolean var4 = this.checkIterator();
            return var4 ? var2 <= -0.08 : var2 <= -0.2 || this.int8 >= 8 && this.int8 <= 11;
         }
      }
   }

   public float computeFloat(float baseTime) {
      return mc.player == null ? 0.0F : mc.player.getAttackCooldownProgress(baseTime);
   }

   public long getMax() {
      double var1 = 4.0;
      if (mc.player != null) {
         var1 = mc.player.getAttributeValue(EntityAttributes.ATTACK_SPEED);
      }

      boolean var3 = "Fast".equals(this.adaptive.getValue());
      double var4 = var3 ? 0.75 : 0.85;
      long var6 = var3 ? 200L : 350L;
      long var8 = (long)(1.0 / Math.max(var1, 0.1) * 1000.0 * var4);
      return Math.max(var8, var6);
   }

   public boolean checkCrits() {
      ClientPlayerEntity var1 = mc.player;
      if (var1 == null) {
         return false;
      } else if (!this.check()) {
         return true;
      } else if (!var1.isOnGround()) {
         return this.check2();
      } else if ("Only Crits".equals(this.smartCrits.getValue())) {
         return false;
      } else if (!"Smart Crits".equals(this.smartCrits.getValue()) || !var1.isSprinting() && !this.getMc()) {
         return this.getMc() ? false : this.int8 == 0;
      } else {
         return false;
      }
   }

   public void initMax() {
      if (this.class_1309 != null && mc.player != null && mc.interactionManager != null) {
         if (mc.player.age != this.int7) {
            if (this.checkCurrent()) {
               boolean var1 = "Fast".equals(this.adaptive.getValue());
               float var2 = var1 ? 0.92F : 0.95F;
               if (this.noAttackInContainer.isEnabled("Sync with TPS")) {
                  float var3 = 20.0F / Math.max(this.float2, 1.0F);
                  if (var3 > 1.05F) {
                     var2 = Math.max(var1 ? 0.9F : 0.92F, var2 / var3);
                  }
               }

               if (!(this.computeFloat(0.5F) < var2)) {
                  if (this.int7 == -1 || this.int6 >= 10) {
                     boolean var32 = this.check2();
                     if (var32 || this.delayHelper.computeCurrent(this.getMax())) {
                        Rotation var4 = VecRotationHelper.getInstance().getCurrent();
                        float var5 = var4 != null ? var4.getYaw() : mc.player.getYaw();
                        float var6 = var4 != null ? var4.getPitch() : mc.player.getPitch();
                        double var7 = (double)this.getOverride();
                        Vec3d var9 = mc.player.getEyePos();
                        Vec3d var10 = mc.player.getPos();
                        Vec3d var11 = this.class_1309.getPos();
                        Vec3d var12 = mc.player.getVelocity();
                        double var13 = var11.x - var10.x;
                        double var15 = var11.z - var10.z;
                        double var17 = Math.hypot(var13, var15);
                        double var19 = var7;
                        if (var17 > 0.01) {
                           double var21 = -(var12.x * (var13 / var17) + var12.z * (var15 / var17));
                           if (var21 > 0.02) {
                              var19 = Math.max(2.4, var7 - var21 * 0.75);
                           }
                        }

                        if (!(this.computeSqrt(this.class_1309, var9) > var19)) {
                           boolean var33 = this.noAttackInContainer.isEnabled("Ignore walls");
                           boolean var22 = this.computeSqrt(this.class_1309, var9) <= 0.6
                              || this.class_1309.getBoundingBox().intersects(mc.player.getBoundingBox())
                              || this.class_1309.getBoundingBox().expand(0.35).intersects(mc.player.getBoundingBox());
                           boolean var23 = var22 || OffHelper.initInit(var9, var5, var6, var19, this.class_1309, var33);
                           if (var23) {
                              if (this.checkCrits()) {
                                 if (!mc.player.isUsingItem()) {
                                    String var24 = this.modeSetting2.getValue();
                                    boolean var25 = !"None".equals(var24);
                                    if (var25) {
                                       ClientPlayerEntity var26 = mc.player;
                                       ClientPlayerEntityAccessor var27 = (ClientPlayerEntityAccessor)var26;
                                       if (var26.isSprinting() || var27.getLastSprinting()) {
                                          var26.setSprinting(false);
                                          this.int5 = 2;
                                          return;
                                       }

                                       this.int5 = 2;
                                    }

                                    boolean var34 = false;
                                    int var35 = -1;
                                    if (this.noAttackInContainer.isEnabled("Shield break") && this.computeFlag(this.class_1309)) {
                                       int var28 = this.getMc();
                                       if (var28 != -1 && var28 != mc.player.getInventory().selectedSlot) {
                                          var35 = mc.player.getInventory().selectedSlot;
                                          mc.player.getInventory().selectedSlot = var28;
                                          if (mc.getNetworkHandler() != null) {
                                             mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(var28));
                                          }

                                          var34 = true;
                                       }
                                    }

                                    this.long3 = System.currentTimeMillis();
                                    if (this.long4 == 0L) {
                                       this.long4 = this.long3;
                                    }

                                    if (!mc.player.isOnGround() && var32 && mc.player.fallDistance <= 0.0F) {
                                       mc.player.fallDistance = (float)Math.max(0.15, -mc.player.getVelocity().y);
                                    }

                                    this.int7 = mc.player.age;
                                    flag = true;
                                    intVal = mc.player.age;

                                    try {
                                       mc.interactionManager.attackEntity(mc.player, this.class_1309);
                                    } finally {
                                       flag = false;
                                    }

                                    mc.player.swingHand(Hand.MAIN_HAND);
                                    this.motionConfig7.runCurrent2();
                                    this.motionConfig8.run();
                                    this.motionConfig3.runNext();
                                    this.motionConfig.run();
                                    this.motionConfig2.runNext();
                                    if (var34 && var35 != -1) {
                                       mc.player.getInventory().selectedSlot = var35;
                                       if (mc.getNetworkHandler() != null) {
                                          mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(var35));
                                       }
                                    }

                                    this.int6 = 0;
                                    mc.player.resetLastAttackedTicks();
                                    if (this.delayHelper != null) {
                                       this.delayHelper.runCurrent();
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void runNext() {
      float var1 = this.getOverride();
      long var2 = System.currentTimeMillis();
      Vec3d var4 = mc.player.getEyePos();
      Vec3d var5 = mc.player.getRotationVec(1.0F);
      if (this.class_1309 != null) {
         double var17 = (double)var1 + 3.0;
         if (!mc.player.isOnGround()) {
            var17 += 0.5;
         }

         double var8 = this.computeSqrt(this.class_1309, var4);
         if (this.class_1309.isAlive() && this.class_1309.getHealth() > 0.0F && var8 <= var17) {
            this.int2 = 0;
         } else {
            this.int2++;
            if (this.int2 > 5) {
               if (this.class_1309 != null) {
                  this.class_1309.setGlowing(false);
               }

               this.class_1309 = null;
               this.class_13092 = null;
               this.int2 = 0;
            }
         }
      } else {
         if (this.class_13092 != null && this.computeEnabled(this.class_13092, var1, var4, var5)) {
            double var6 = this.computeSqrt(this.class_13092, var4);
            if (var6 <= (double)var1 && var2 - this.longVal < 30000L) {
               this.class_1309 = this.class_13092;
               return;
            }
         }

         LivingEntity var16 = null;
         double var7 = Double.MAX_VALUE;
         boolean var9 = this.noAttackInContainer.isEnabled("Ignore walls");
         Box var10 = mc.player.getBoundingBox().expand((double)var1 + 0.5);

         for (Entity var12 : mc.world.getOtherEntities(mc.player, var10)) {
            if (var12 instanceof LivingEntity) {
               LivingEntity var13 = (LivingEntity)var12;
               if (this.computeEnabled2(var13, var1, var4, var5) && (var9 || this.initPut(var13))) {
                  double var14 = this.computeDouble(var13, var4, var5);
                  if (var14 < var7) {
                     var7 = var14;
                     var16 = var13;
                  }
               }
            }
         }

         if (var16 != null && var16 != this.class_1309) {
            if (this.class_1309 != null) {
               this.class_1309.setGlowing(false);
            }

            this.class_13092 = var16;
            this.longVal = var2;
            this.long4 = 0L;
         }

         this.class_1309 = var16;
      }
   }

   public double computeDouble(LivingEntity entity, Vec3d eyePos, Vec3d lookVec) {
      String var4 = this.health.getValue();

      return switch (var4) {
         case "FOV" -> 1.0 - this.computeSqrt(entity, eyePos, lookVec);
         case "Distance" -> this.computeSqrt(entity, eyePos);
         case "Health" -> (double)(entity.getHealth() + entity.getAbsorptionAmount());
         default -> (double)entity.getArmor() * 10.0 + (double)(entity.getHealth() + entity.getAbsorptionAmount()) * 2.0 + this.computeSqrt(entity, eyePos);
      };
   }

   public boolean computeEnabled(LivingEntity entity, float maxRange, Vec3d eyePos, Vec3d lookVec) {
      return !this.computeEnabled2(entity, maxRange, eyePos, lookVec) ? false : this.noAttackInContainer.isEnabled("Ignore walls") || this.initPut(entity);
   }

   public boolean computeEnabled2(LivingEntity entity, float maxRange, Vec3d eyePos, Vec3d lookVec) {
      if (entity == null
         || entity == mc.player
         || !entity.isAlive()
         || entity.getHealth() <= 0.0F
         || entity instanceof ArmorStandEntity
         || entity instanceof ClientPlayerEntity) {
         return false;
      } else if (FakePlayer.getInstance().computeFlag(entity)) {
         double var12 = (double)maxRange;
         double var13 = this.computeSqrt(entity, eyePos);
         double var14 = (double)mc.player.distanceTo(entity);
         return !(var13 > var12) && !(var14 > var12 + 0.5);
      } else if (entity.isInvisible() && !this.invisibles.isEnabled("Invisibles")) {
         return false;
      } else {
         if (entity instanceof PlayerEntity var5) {
            boolean var6 = !var5.getEquippedStack(EquipmentSlot.CHEST).isEmpty() || !var5.getEquippedStack(EquipmentSlot.LEGS).isEmpty();
            if (var6 && !this.invisibles.isEnabled("Players")) {
               return false;
            }

            if (!var6 && !this.invisibles.isEnabled("Naked")) {
               return false;
            }
         } else {
            if (!(entity instanceof MobEntity) && !(entity instanceof AnimalEntity)) {
               return false;
            }

            if (!this.invisibles.isEnabled("Mobs")) {
               return false;
            }
         }

         double var11 = (double)maxRange;
         double var7 = this.computeSqrt(entity, eyePos);
         double var9 = (double)mc.player.distanceTo(entity);
         return !(var7 > var11) && !(var9 > var11 + 0.5);
      }
   }

   public boolean computeFlag(LivingEntity entity) {
      if (entity == null) {
         return false;
      } else if (entity.isBlocking()) {
         return true;
      } else if (!entity.isUsingItem()) {
         return false;
      } else {
         ItemStack var2 = entity.getActiveItem();
         return !var2.isEmpty() && var2.getItem() instanceof ShieldItem;
      }
   }

   public int getMcInt() {
      for (int var1 = 0; var1 < 9; var1++) {
         ItemStack var2 = mc.player.getInventory().getStack(var1);
         if (!var2.isEmpty() && var2.getItem() instanceof AxeItem) {
            return var1;
         }
      }

      return -1;
   }

   public void run2() {
   }

   public void initFun(LivingEntity target, Vec3d targetVec, Rotation rotation) {
      RotationHelper var4 = new RotationHelper(this.getLegacySetting(), true);
      if (this.modeSetting.is("FunTime") || this.modeSetting.is("FunTimeNew") || this.modeSetting.is("Neuro") || this.modeSetting.is("ReallyWorld")) {
         var4.withNoClamp(true);
      }

      var4.withClientLook(false);
      FromRotationHelper var5 = var4.initClamp(rotation, targetVec, target, this);
      VecRotationHelper.getInstance().initAdd(var5, Priority.HIGH, this);
   }

   public LegacySetting getLegacySetting() {
      String var1 = this.modeSetting.getValue();

      return (LegacySetting)(switch (var1) {
         case "SpookyTime" -> this.motionConfig8;
         case "ReallyWorld" -> this.motionConfig;
         case "Neuro" -> this.motionConfig3;
         case "Custom" -> this.motionConfig2;
         default -> this.motionConfig7;
      });
   }

   public Vec3d initGaussian(LivingEntity target) {
      Vec3d var2 = mc.player.getEyePos();
      Box var3 = target.method_5829();
      if (!var3.contains(var2) && (mc.player == null || !var3.intersects(mc.player.getBoundingBox()))) {
         String var4 = this.noAttackInContainer.isEnabled("Ignore walls") ? this.grim.getValue() : "Off";
         Vec3d var5 = OffHelper.initInit(target, true, var4);
         if (var5 == null) {
            var5 = var3.getCenter();
         }

         ThreadLocalRandom var6 = ThreadLocalRandom.current();
         double var7 = var6.nextGaussian() * 0.02;
         double var9 = var6.nextGaussian() * 0.01;
         double var11 = var6.nextGaussian() * 0.02;
         Vec3d var13 = var5.add(var7, var9, var11);
         double var14 = 0.02;
         return new Vec3d(
            MathHelper.clamp(var13.x, var3.minX + var14, var3.maxX - var14),
            MathHelper.clamp(var13.y, var3.minY + var14, var3.maxY - var14),
            MathHelper.clamp(var13.z, var3.minZ + var14, var3.maxZ - var14)
         );
      } else {
         return new Vec3d(target.method_23317(), target.method_23318() + (double)target.method_17682() * 0.45, target.method_23321());
      }
   }

   public boolean initPut(Entity entity) {
      int var2 = entity.getId();
      long var3 = System.currentTimeMillis();
      Long var5 = this.map2.get(var2);
      if (this.map.containsKey(var2) && var5 != null && var3 - var5 < 500L) {
         return this.map.get(var2);
      } else if (entity.getBoundingBox().contains(mc.player.getEyePos())) {
         this.map.put(var2, true);
         this.map2.put(var2, var3);
         return true;
      } else {
         Vec3d var6 = mc.player.getEyePos();
         BlockHitResult var7 = mc.world
            .raycast(new RaycastContext(var6, entity.getBoundingBox().getCenter(), ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
         boolean var8 = var7.getType() == Type.MISS;
         this.map.put(var2, var8);
         this.map2.put(var2, var3);
         return var8;
      }
   }

   public boolean checkCurrent() {
      if (mc.player.isUsingItem()) {
         return false;
      } else {
         if (this.noAttackInContainer.isEnabled("Weapon only")) {
            ItemStack var1 = mc.player.getMainHandStack();
            if (var1.isEmpty()) {
               return false;
            }

            Item var2 = var1.getItem();
            boolean var3 = var2 instanceof SwordItem || var2 instanceof AxeItem || var2 instanceof MaceItem || var2 instanceof TridentItem;
            if (!var3) {
               return false;
            }
         }

         float var4 = this.hitChance.getValue();
         if (var4 < 100.0F) {
            float var5 = ThreadLocalRandom.current().nextFloat() * 100.0F;
            if (var5 > var4) {
               return false;
            }
         }

         return true;
      }
   }

   public double computeSqrt(Entity entity, Vec3d eyePos) {
      Box var3 = entity.getBoundingBox();
      double var4 = MathHelper.clamp(eyePos.x, var3.minX, var3.maxX);
      double var6 = MathHelper.clamp(eyePos.y, var3.minY, var3.maxY);
      double var8 = MathHelper.clamp(eyePos.z, var3.minZ, var3.maxZ);
      double var10 = eyePos.x - var4;
      double var12 = eyePos.y - var6;
      double var14 = eyePos.z - var8;
      return Math.sqrt(var10 * var10 + var12 * var12 + var14 * var14);
   }

   public double computeSqrt(LivingEntity entity, Vec3d eyePos, Vec3d lookVec) {
      Box var4 = entity.getBoundingBox();
      double var5 = MathHelper.clamp(eyePos.x, var4.minX, var4.maxX);
      double var7 = MathHelper.clamp(eyePos.y, var4.minY, var4.maxY);
      double var9 = MathHelper.clamp(eyePos.z, var4.minZ, var4.maxZ);
      double var11 = var5 - eyePos.x;
      double var13 = var7 - eyePos.y;
      double var15 = var9 - eyePos.z;
      double var17 = Math.sqrt(var11 * var11 + var13 * var13 + var15 * var15);
      return var17 < 1.0E-4 ? 1.0 : (var11 * lookVec.x + var13 * lookVec.y + var15 * lookVec.z) / var17;
   }

   public float getOverride() {
      return mc.player != null && mc.player.isGliding() && this.elytraOverride.getValue() ? this.elytraDistance.getValue() : this.distance.getValue();
   }

   public boolean is() {
      return this.target.is("Target");
   }

   public boolean check3() {
      return false;
   }

   public boolean checkCrits2() {
      if (!this.isEnabled() || mc.player == null) {
         return false;
      } else if (this.int5 > 0) {
         return true;
      } else if (this.class_1309 == null) {
         return false;
      } else {
         if (!mc.player.isOnGround()) {
            if (this.int8 >= 2 || mc.player.getVelocity().y <= 0.2 || this.check2()) {
               return true;
            }
         } else {
            float var1 = this.computeFloat(0.5F);
            if (var1 >= 0.88F
               && this.computeSqrt(this.class_1309, mc.player.getEyePos()) <= (double)this.getOverride() + 0.3
               && !"Smart Crits".equals(this.smartCrits.getValue())) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean check4() {
      return false;
   }

   public boolean is2() {
      return this.target.is("Free");
   }

   @Override
   public void toggle() {
      super.toggle();
      if (!this.isEnabled()) {
         this.class_1309 = null;
         this.class_13092 = null;
         this.class_243 = null;
         VecRotationHelper.getInstance().handleRotation(this);
         OffHelper.run();
         this.run2();
      }
   }

   public ModeSetting getModeSetting() {
      return this.modeSetting;
   }

   public ModeSetting getGrim() {
      return this.grim;
   }

   public MultiBooleanSetting getNoAttackInContainer() {
      return this.noAttackInContainer;
   }

   @Override
   public String getDisplayName() {
      return this.smartCrits != null ? this.smartCrits.getValue() : null;
   }

   public void lambda$onEvent$4(SprintEvent.SprintEventData event) {
      if (this.isEnabled() && mc.player != null && this.checkCrits2()) {
         event.setSprint(false);
      }
   }

   public void lambda$onEvent$3(PacketEvent.PacketEventData event) {
      if (mc.player != null) {
         if (event.packetType() == PacketEvent.PacketEventData.PacketType.RECEIVE && event.packet() instanceof WorldTimeUpdateS2CPacket) {
            long var2 = System.currentTimeMillis();
            if (this.mosif > 0L) {
               long var4 = var2 - this.mosif;
               float var6 = 20000.0F / (float)var4;
               this.float2 = MathHelper.clamp(var6, 1.0F, 20.0F);
            }

            this.mosif = var2;
         }
      }
   }

   public void lambda$onEvent$2(RotationEvent event) {
      this.runYaw();
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      this.initAtan();
   }

   public Boolean lambda$new$0() {
      return this.noAttackInContainer.isEnabled("Ignore walls");
   }
}
