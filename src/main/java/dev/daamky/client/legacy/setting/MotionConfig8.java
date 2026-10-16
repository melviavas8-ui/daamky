package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.CameraManager;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.module.combat.AttackAura;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class MotionConfig8 extends LegacySetting {
   public float floatVal;
   public float float2;
   public boolean flag = false;
   public int intVal = 0;
   public float float3 = 0.0F;
   public float float4 = 0.0F;
   public float float5 = 0.0F;
   public float muunid = 0.0F;
   public int int2 = 0;
   public float float6 = 0.0F;
   public float float7 = 0.0F;
   public int int3 = 0;
   public int int4 = -1;
   public static final Vec3d[] class_243List = new Vec3d[20];
   public static int int5 = 0;

   public MotionConfig8() {
      super("SpookyTime");
   }

   @Override
   public void run() {
      this.intVal3 = 0;
   }

   public void run2() {
      this.intVal3 = 3;
   }

   public void run3() {
      this.flag = false;
      this.intVal = 0;
      this.floatVal3 = 0.0F;
      this.floatVal4 = 0.0F;
      this.floatVal5 = 0.0F;
      this.muunid = 0.0F;
      this.intVal2 = 0;
      this.floatVal6 = 0.0F;
      this.floatVal7 = 0.0F;
      this.intVal4 = -1;
   }

   @Override
   public void runCurrent() {
      if (!this.flag) {
         super.runCurrent();
         if (this.flag) {
            this.floatVal = this.floatVal;
            this.mubizl = this.floatVal2;
         }
      }
   }

   @Override
   public Rotation initNext(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d, Entity entity) {
      if (mc.player != null && mc.world != null) {
         if (!this.flag) {
            this.floatVal = currentRotation != null ? currentRotation.getYaw() : mc.player.getYaw();
            this.floatVal2 = currentRotation != null ? currentRotation.getPitch() : mc.player.getPitch();
            this.intVal = 0;
            this.floatVal3 = 0.0F;
            this.floatVal4 = 0.0F;
            this.floatVal5 = computeCurrent(-2.4F, 2.4F);
            this.muunid = computeCurrent(-1.5F, 1.5F);
            this.intVal2 = 0;
            this.floatVal6 = 0.0F;
            this.floatVal7 = 0.0F;
            this.flag = true;
            this.intVal4 = entity != null ? entity.getId() : -1;
         }

         if (entity != null && entity.getId() != this.intVal4) {
            this.intVal4 = entity.getId();
            this.intVal = 0;
            this.floatVal3 = 0.0F;
            this.floatVal4 = 0.0F;
            this.floatVal5 = computeCurrent(-2.4F, 2.4F);
            this.muunid = computeCurrent(-1.5F, 1.5F);
            this.intVal2 = 0;
            this.floatVal6 = 0.0F;
            this.floatVal7 = 0.0F;
         }

         if (this.intVal3 > 0) {
            this.intVal3--;
         }

         if (!this.flag && entity instanceof LivingEntity var5 && var5.isAlive()) {
            Vec3d var28 = mc.player.getEyePos();
            double var29 = (double)AttackAura.getInstance().getOverride();
            Vec3d var9 = this.initAtan(var28, var5, var29, true);
            Vec3d var10 = var9 != Vec3d.ZERO
               ? var9
               : (vec3d != null ? vec3d.subtract(var28) : var5.getBoundingBox().getCenter().subtract(var28));
            float var11 = var9 == Vec3d.ZERO && vec3d == null
               ? mc.player.getYaw()
               : (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var10.z, var10.x)) - 90.0);
            float var12 = var9 == Vec3d.ZERO && vec3d == null
               ? mc.player.getPitch()
               : (float)(-Math.toDegrees(Math.atan2(var10.y, Math.hypot(var10.x, var10.z))));
            float var13 = this.floatVal;
            float var14 = this.floatVal2;
            if (this.intVal <= 0) {
               this.intVal = (int)computeCurrent(30.0F, 60.0F);
               this.floatVal5 = computeCurrent(-2.4F, 2.4F);
               this.muunid = computeCurrent(-1.5F, 1.5F);
            } else {
               this.intVal--;
            }

            this.floatVal3 = this.floatVal3 + (this.floatVal5 - this.floatVal3) * 0.045F;
            this.floatVal4 = this.floatVal4 + (this.muunid - this.floatVal4) * 0.045F;
            float var15 = MathHelper.wrapDegrees(MathHelper.wrapDegrees(var11 + this.floatVal3) - var13);
            float var16 = MathHelper.wrapDegrees(MathHelper.clamp(var12 + this.floatVal4, -89.0F, 89.0F) - var14);
            if (this.intVal2 > 0) {
               this.intVal2--;
               var15 *= 0.35F;
               var16 *= 0.35F;
            } else if (Math.abs(var15) > 1.5F && ThreadLocalRandom.current().nextDouble() < 0.08) {
               this.intVal2 = (int)computeCurrent(1.0F, 4.0F);
            }

            float var17 = MathHelper.clamp(0.5F + Math.abs(var15) * 0.12F, 0.5F, 1.0F);
            float var18 = MathHelper.clamp(0.5F + Math.abs(var16) * 0.12F, 0.5F, 1.0F);
            float var19 = this.intVal3 > 0 ? 15.0F : 12.0F;
            float var20 = this.intVal3 > 0 ? 10.0F : 8.0F;
            float var21 = MathHelper.clamp(this.floatVal6 + (MathHelper.clamp(var15 * 0.6F, -var19, var19) - this.floatVal6) * var17, -var19, var19);
            float var22 = MathHelper.clamp(this.floatVal7 + (MathHelper.clamp(var16 * 0.6F, -var20, var20) - this.floatVal7) * var18, -var20, var20);
            this.floatVal6 = var21;
            this.floatVal7 = var22;
            float var23 = var13 + var21;
            float var24 = MathHelper.clamp(var14 + var22, -89.0F, 89.0F);
            if (ThreadLocalRandom.current().nextDouble() > 0.985) {
               var23 += (ThreadLocalRandom.current().nextBoolean() ? 1.0F : -1.0F) * computeCurrent(0.3F, 0.9F);
               var24 = MathHelper.clamp(var24 + (ThreadLocalRandom.current().nextBoolean() ? 1.0F : -1.0F) * computeCurrent(0.15F, 0.5F), -89.0F, 89.0F);
            }

            float var25 = this.computeRound(var13, var23);
            float var26 = this.computeRound(var14, var24);
            Vec3d var27 = Vec3d.fromPolar(var26, var25).multiply(var29);
            if (var5.getBoundingBox().expand(0.08).raycast(var28, var28.add(var27)).isEmpty()) {
               var25 = MathHelper.lerp(0.5F, var25, var11);
               var26 = MathHelper.lerp(0.5F, var26, var12);
               var25 = this.computeRound(var13, var25);
               var26 = this.computeRound(var14, var26);
            }

            this.floatVal = var25;
            this.floatVal2 = MathHelper.clamp(var26, -89.0F, 89.0F);
            return new Rotation(this.floatVal, this.floatVal2);
         }

         float var6 = CameraManager.getFlag() ? CameraManager.getFloat() : mc.player.getYaw();
         float var7 = CameraManager.getFlag() ? CameraManager.getMrula() : mc.player.getPitch();
         Rotation var8 = new Rotation(var6, var7);
         if (!this.flag) {
            this.runCurrent();
         }

         return this.initYaw(currentRotation, var8);
      } else {
         return targetRotation;
      }
   }

   public float computeRound(float lastAngle, float current) {
      if (mc.options == null) {
         return current;
      } else {
         double var3 = (Double)mc.options.getMouseSensitivity().getValue() * 0.6000000498956214 + 0.19999998556632664;
         double var5 = var3 * var3 * var3 * 8.0 * 0.15000007F;
         double var7 = (double)MathHelper.wrapDegrees(current - lastAngle);
         return(floatVal)((double)lastAngle + (double)Math.round(var7 / var5) * var5);
      }
   }

   public boolean initInit(float yaw, float pitch, double distance, LivingEntity entity, boolean throughWalls) {
      if (mc.player != null && mc.world != null && entity != null) {
         Vec3d var7 = mc.player.getEyePos();
         Vec3d var8 = Vec3d.fromPolar(pitch, yaw).multiply(distance);
         float var9 = Math.max(entity.getTargetingMargin(), 0.08F);
         Box var10 = entity.getBoundingBox().expand((double)var9);
         Optional var11 = var10.contains(var7) ? Optional.of(var7) : var10.raycast(var7, var7.add(var8));
         return var11.isEmpty()
            ? false
            : throughWalls
               || mc.world
                     .raycast(new RaycastContext(var7, (Vec3d)var11.get(), ShapeType.OUTLINE, FluidHandling.NONE, mc.player))
                     .getType()
                  == Type.MISS;
      } else {
         return false;
      }
   }

   public static float computeCurrent(float min, float max) {
      return min + ThreadLocalRandom.current().nextFloat() * (max - min);
   }

   public Vec3d initAtan(Vec3d eye, LivingEntity target, double reach, boolean throughWalls) {
      Box var6 = target.getBoundingBox();
      boolean var7 = mc.player != null
         && (mc.player.getMainHandStack().isOf(Items.MACE) || mc.player.getOffHandStack().isOf(Items.MACE));
      Vec3d var8 = var7 && mc.player != null ? eye.add(mc.player.getVelocity()) : eye;
      double var9 = (var6.minX + var6.maxX) * 0.5;
      double var11 = (var6.minZ + var6.maxZ) * 0.5;
      Vec3d var13 = target.getPos().add(0.0, (double)target.getStandingEyeHeight(), 0.0);
      double var14 = var8.distanceTo(var13);
      Vec3d var16 = var8;
      if (var7 && var14 > 3.0) {
         var16 = new Vec3d(var8.x, var13.y, var8.z);
      }

      double var17 = var8.y;
      if (var7 && var14 > 3.0) {
         var17 = var13.y;
      }

      double var19 = MathHelper.clamp(var17, var6.minY + 0.2, var6.maxY - 0.2);
      int5 = 0;
      initInit(var9, var19, var11);
      initInit(var9, var6.maxY - 0.08, var11);
      initInit(var9, var6.minY + (var6.maxY - var6.minY) * 0.75, var11);
      initInit(var9, (var6.minY + var6.maxY) * 0.5, var11);
      initInit(var9, var6.minY + 0.1, var11);
      initInit(var6.maxX - 0.05, var19, var11);
      initInit(var6.minX + 0.05, var19, var11);
      initInit(var9, var19, var6.maxZ - 0.05);
      initInit(var9, var19, var6.minZ + 0.05);
      initInit(var6.maxX - 0.05, var6.maxY - 0.1, var6.maxZ - 0.05);
      initInit(var6.minX + 0.05, var6.maxY - 0.1, var6.maxZ - 0.05);
      initInit(var6.maxX - 0.05, var6.maxY - 0.1, var6.minZ + 0.05);
      initInit(var6.minX + 0.05, var6.maxY - 0.1, var6.minZ + 0.05);
      initInit(var6.maxX - 0.05, var19, var6.maxZ - 0.05);
      initInit(var6.minX + 0.05, var19, var6.maxZ - 0.05);
      initInit(var6.maxX - 0.05, var19, var6.minZ + 0.05);
      initInit(var6.minX + 0.05, var19, var6.minZ + 0.05);

      for (double var24 : new double[]{0.0, 0.2}) {
         Vec3d var26 = null;
         double var27 = Double.MAX_VALUE;

         for (int var29 = 0; var29 < int5; var29++) {
            Vec3d var30 = class_243List[var29];
            Vec3d var31 = var30.subtract(var16);
            double var32 = var31.length();
            double var34 = reach + var24;
            if (var7 || !(var32 > var34)) {
               float var36 = (float)(var7 ? var32 + var24 + 0.01 : var34);
               float var37 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var31.z, var31.x)) - 90.0);
               float var38 = (float)(-Math.toDegrees(Math.atan2(var31.y, Math.hypot(var31.x, var31.z))));
               if (this.initInit(var37, var38, (double)var36, target, false)) {
                  double var39 = var30.squaredDistanceTo(var16);
                  if (var39 < var27) {
                     var27 = var39;
                     var26 = var30;
                  }
               }
            }
         }

         if (var26 != null) {
            return var26.subtract(var16);
         }

         if (throughWalls) {
            Vec3d var44 = null;
            double var45 = Double.MAX_VALUE;

            for (int var46 = 0; var46 < int5; var46++) {
               Vec3d var33 = class_243List[var46];
               Vec3d var47 = var33.subtract(var16);
               double var35 = var47.length();
               double var48 = reach + var24;
               if (var7 || !(var35 > var48)) {
                  float var49 = (float)(var7 ? var35 + var24 + 0.01 : var48);
                  float var40 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var47.z, var47.x)) - 90.0);
                  float var41 = (float)(-Math.toDegrees(Math.atan2(var47.y, Math.hypot(var47.x, var47.z))));
                  if (this.initInit(var40, var41, (double)var49, target, true)) {
                     double var42 = var33.squaredDistanceTo(var16);
                     if (var42 < var45) {
                        var45 = var42;
                        var44 = var33;
                     }
                  }
               }
            }

            if (var44 != null) {
               return var44.subtract(var16);
            }
         }
      }

      return Vec3d.ZERO;
   }

   public static void initInit(double x, double y, double z) {
      if (int5 < class_243List.length) {
         class_243List[int5++] = new Vec3d(x, y, z);
      }
   }
}
