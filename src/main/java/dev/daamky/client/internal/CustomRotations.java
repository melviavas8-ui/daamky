package dev.daamky.client.internal;

import dev.daamky.client.config.Configurable;
import dev.daamky.client.model.enums.AnchorPoint;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.combat.ElytraTarget;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.util.MinecraftAccess;
import java.util.function.Supplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CustomRotations extends Configurable implements MinecraftAccess {
   public final ElytraTarget elytraTargetModule;
   public AnchorPoint anchorPoint = AnchorPoint.CENTER;
   public static final float floatVal = 45.0F;
   public static final float float2 = 35.0F;
   public static final int intVal = 10;
   public final Supplier<Prediction> supplier = Prediction::new;
   public final BooleanSetting customRotations = new BooleanSetting("Custom rotations").value(true);
   public final BooleanSetting sharp = new BooleanSetting("Sharp").value(false).visibleWhen(this.customRotations::getValue);
   public final BooleanSetting autoDistance = new BooleanSetting("Auto distance").value(true);
   public final ModeSetting rotateAt = new ModeSetting("Rotate at").value(this.anchorPoint).enumValues(AnchorPoint.values()).onAction(this::lambda$new$0);

   public CustomRotations(ElytraTarget elytraTargetModule) {
      this.elytraTargetModule = elytraTargetModulex;
      this.addSettings(new Setting[]{this.customRotations, this.sharp, this.autoDistance, this.rotateAt});
      this.addSettings(this.supplier.get().getSettings());
   }

   public boolean checkEnabled() {
      return this.elytraTargetModule.isEnabled() && AttackAura.getInstance().class_1309 != null && mc.player.isGliding();
   }

   public void initInit() {
      LivingEntity var1 = AttackAura.getInstance().class_1309;
      if (this.checkEnabled() && this.customRotations.getValue() && var1 != null) {
         Rotation var2 = this.computeAuto(var1);
         Rotation var3 = VecRotationHelper.getInstance().getCurrentRotation();
         if (var3 == null) {
            var3 = new Rotation(mc.player.getYaw(), mc.player.getPitch());
         }

         Rotation var4 = this.initSin(var3, var2);
         VecRotationHelper.getInstance()
            .handleRotation(
               new Rotation.VecRotation(var4, this.computePosition(var1)), var1, RotationHelper.rotationHelper3, Priority.HIGH, this.elytraTargetModule
            );
      }
   }

   public Rotation initSin(Rotation currentRotation, Rotation targetRotation) {
      DeltaHelper var3 = currentRotation.initYaw(targetRotation);
      float var4 = var3.getDeltaYaw();
      float var5 = var3.getDeltaPitch();
      float var6 = (float)Math.sqrt((double)(var4 * var4 + var5 * var5));
      long var7 = System.currentTimeMillis();
      boolean var9 = Math.sin((double)var7 / 300.0) > 0.8;
      boolean var10 = Math.abs(var4) > 90.0F;
      float var11 = var9 ? 2.0F : 1.2F;
      float var12 = var9 ? (float)(Math.sin((double)((float)(var7 % 360L) / 300.0F) * Math.PI) * 0.8F + 1.2F) : 1.2F;
      float var13 = var10 ? (float)(2.2F * Math.sin((double)var7 / 150.0) * 0.2 + 1.0) : 1.2F;
      float var14 = var11 * var12;
      float var15 = this.getSharp() * var14 * var13;
      float var16 = this.getSharp2() * var14;
      float var17 = (float)(Math.sin((double)var7 / 80.0) * 0.08 + Math.cos((double)var7 / 120.0) * 0.05);
      float var18 = MathHelper.clamp(var4, -var15, var15);
      float var19 = MathHelper.clamp(var5, -var16, var16);
      if (var6 < 5.0F) {
         var18 += var17 * 0.2F;
         var19 += var17 * 0.8F;
      }

      return new Rotation(currentRotation.getYaw() + var18, MathHelper.clamp(currentRotation.getPitch() + var19, -90.0F, 90.0F));
   }

   public Rotation computeAuto(LivingEntity target) {
      Vec3d var2 = this.computePosition(target);
      if (this.autoDistance.getValue()) {
         Vec3d var3 = mc.player.getPos();
         Vec3d var4 = var2.subtract(var3).normalize();
         double var5 = var3.squaredDistanceTo(var4);
         if (var5 < 100.0) {
            var2 = var2.subtract(var4.multiply(10.0 - var5));
         }
      }

      return RotationMath.initAtan(var2);
   }

   public Vec3d computePosition(LivingEntity target) {
      return this.supplier.get().computePredict(target, this.anchorPoint.getPosition(target)).add(this.initRandom().multiply(4.0));
   }

   public float getSharp() {
      return (this.sharp.getValue() ? 67.5F : 45.0F) / 3.0F;
   }

   public float getSharp2() {
      return (this.sharp.getValue() ? 52.5F : 35.0F) / 3.0F;
   }

   public Vec3d initRandom() {
      double var1 = (double)System.currentTimeMillis() / 1000.0;
      return new Vec3d(
         Math.sin(var1 * 1.8) * 0.04 + (Math.random() - 0.5) * 0.02,
         Math.sin(var1 * 2.2) * 0.03 + (Math.random() - 0.5) * 0.015,
         Math.cos(var1 * 1.8) * 0.04 + (Math.random() - 0.5) * 0.02
      );
   }

   public ModeSetting getRotateAt() {
      return this.rotateAt;
   }

   public void lambda$new$0() {
      this.anchorPoint = NamedChoice.getChoiceByName(this.getRotateAt().getValue(), AnchorPoint.values());
   }
}
