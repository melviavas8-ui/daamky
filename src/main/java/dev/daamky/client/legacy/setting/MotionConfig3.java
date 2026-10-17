package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.CameraManager;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.NeuroRecHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.StrokesHelper;
import dev.daamky.client.util.MinecraftAccess;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MotionConfig3 extends LegacySetting implements MinecraftAccess {
   public float floatVal;
   public float float2;
   public boolean flag = false;
   public float float3 = 0.0F;
   public float float4 = 0.0F;
   public float float5 = 0.0F;
   public float float6 = 0.0F;
   public float float7 = 0.0F;
   public float float8 = 0.0F;
   public float float9 = 0.0F;
   public float float10 = 0.0F;
   public float float11 = 0.0F;
   public float float12 = 0.0F;
   public float float13 = 0.5F;
   public float float14 = 0.55F;
   public float mjidp = 0.5F;
   public float float15 = 0.55F;
   public float float16 = 0.55F;
   public long timeStamp = 0L;
   public float float17 = 0.0F;
   public float float18 = 0.0F;
   public float float19 = 0.0F;
   public float float20 = 0.0F;
   public float float21 = 0.0F;
   public float float22 = 0.0F;
   public long long2 = 0L;
   public float float23 = 0.0F;
   public float float24 = 0.0F;
   public boolean flag2 = false;
   public float float25 = 1.0F;
   public float float26 = 1.0F;
   public float float27 = 0.0F;
   public float float28 = 0.0F;
   public float float29 = 0.0F;
   public float float30 = 0.0F;
   public float float31 = 0.0F;
   public float float32 = 0.0F;
   public int intVal = 0;
   public int int2 = 999;
   public int int3 = -1;
   public final Random random = new Random();

   public MotionConfig3() {
      super("Neuro");
   }

   public void runNext() {
      this.intVal = 0;
      this.int2 = 0;
      this.float11 = this.float11 + (this.random.nextFloat() - 0.5F) * 0.08F;
      this.float12 = this.float12 + 0.05F + this.random.nextFloat() * 0.05F;
   }

   @Override
   public void run() {
      this.intVal = 3;
   }

   public void run2() {
      this.flag = false;
      this.float3 = 0.0F;
      this.float4 = 0.0F;
      this.float5 = 0.0F;
      this.float6 = 0.0F;
      this.float7 = 0.0F;
      this.float8 = 0.0F;
      this.float9 = 0.0F;
      this.float10 = 0.0F;
      this.float11 = 0.0F;
      this.float12 = 0.0F;
      this.intVal = 0;
      this.int2 = 999;
      this.float27 = 0.0F;
      this.float28 = 0.0F;
      this.float29 = 0.0F;
      this.float30 = 0.0F;
      this.float31 = 0.0F;
      this.float32 = 0.0F;
      this.float13 = 0.5F;
      this.float14 = 0.55F;
      this.mjidp = 0.5F;
      this.int3 = -1;
      this.timeStamp = 0L;
      this.long2 = 0L;
      this.flag2 = false;
      this.flag = false;
   }

   @Override
   public void runCurrent() {
      if (!this.flag) {
         super.runCurrent();
         if (this.flag) {
            this.floatVal = this.floatVal;
            this.mubizl = this.float2;
         }
      }
   }

   @Override
   public Rotation initNext(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d, Entity entity) {
      if (mc.player == null || mc.world == null) {
         return targetRotation != null ? targetRotation : currentRotation;
      } else if (this.flag) {
         float var40 = CameraManager.getFlag() ? CameraManager.getFloat() : mc.player.getYaw();
         float var42 = CameraManager.getFlag() ? CameraManager.getMrula() : mc.player.getPitch();
         return this.initYaw(currentRotation, new Rotation(var40, var42));
      } else {
         if (!this.flag) {
            this.floatVal = currentRotation != null ? currentRotation.getYaw() : mc.player.getYaw();
            this.float2 = currentRotation != null ? currentRotation.getPitch() : mc.player.getPitch();
            this.float3 = 0.0F;
            this.float4 = 0.0F;
            this.float5 = 0.0F;
            this.float6 = 0.0F;
            this.flag = true;
            this.int3 = entity != null ? entity.getId() : -1;
         }

         if (entity instanceof LivingEntity var5 && var5.isAlive()) {
            if (var5.getId() != this.int3) {
               this.int3 = var5.getId();
               this.float5 *= 0.25F;
               this.float6 *= 0.25F;
               this.float27 = 0.0F;
               this.float28 = 0.0F;
               this.float29 = 0.0F;
               this.float30 = 0.0F;
               this.float31 = 0.0F;
               this.float32 = 0.0F;
               this.float25 = this.random.nextBoolean() ? 1.0F : -1.0F;
               this.float26 = 0.7F + this.random.nextFloat() * 0.8F;
            }

            this.int2 = Math.min(this.int2 + 1, 999);
            if (this.intVal > 0) {
               this.intVal--;
            }

            long var41 = System.currentTimeMillis();
            if (var41 - this.timeStamp > 1200L + (long)this.random.nextInt(1000)) {
               this.timeStamp = var41;
               float var8 = mc.player.getY() > var5.getY() + 0.3 ? 0.65F : 0.48F;
               this.float16 = var8 + (this.random.nextFloat() - 0.5F) * 0.25F;
               this.float20 = (float)(this.random.nextGaussian() * 0.07);
               this.float21 = (float)(this.random.nextGaussian() * 0.05);
               this.float22 = (float)(this.random.nextGaussian() * 0.07);
            }

            this.float15 = this.float15 + (this.float16 - this.float15) * 0.06F;
            this.float17 = this.float17 + (this.float20 - this.float17) * 0.08F;
            this.float18 = this.float18 + (this.float21 - this.float18) * 0.08F;
            this.float19 = this.float19 + (this.float22 - this.float19) * 0.08F;
            if (var41 - this.long2 > 2200L + (long)this.random.nextInt(1800)) {
               this.long2 = var41;
               this.flag2 = this.random.nextFloat() < 0.35F;
               if (this.flag2) {
                  float var43 = this.random.nextFloat() * (float) (Math.PI * 2);
                  float var9 = 0.12F + this.random.nextFloat() * 0.18F;
                  this.float23 = (float)Math.cos((double)var43) * var9;
                  this.float24 = (float)Math.sin((double)var43) * var9;
               } else {
                  this.float23 = 0.0F;
                  this.float24 = 0.0F;
               }
            }

            Vec3d var44 = mc.player.getEyePos();
            Box var45 = var5.getBoundingBox();
            Vec3d var10 = var45.getCenter();
            Vec3d var11 = var10.subtract(var44);
            float var12 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var11.z, var11.x)) - 90.0);
            float var13 = (float)MathHelper.clamp(
               -Math.toDegrees(Math.atan2(var11.y, Math.hypot(var11.x, var11.z))), -89.0, 89.0
            );
            float var14 = MathHelper.wrapDegrees(var12 - this.floatVal);
            float var15 = var13 - this.float2;
            double var16 = var11.length();
            Vec3d var18 = var5.getVelocity();
            Vec3d var19 = mc.player.getVelocity();
            float var20 = 0.0F;
            float var21 = 0.0F;
            if (var16 > 0.01) {
               Vec3d var22 = var11.multiply(1.0 / var16);
               Vec3d var23 = new Vec3d(-var22.z, 0.0, var22.x);
               var20 = (float)(var18.dotProduct(var23) / var16 * 20.0);
               var21 = (float)(var18.y / var16 * 20.0);
            }

            float var46 = (float)(var5.getY() - mc.player.getY());
            float var47 = mc.player.getAttackCooldownProgress(0.5F);
            float var24 = this.float27 - this.float29;
            float var25 = this.float28 - this.float30;
            double[] var26 = StrokesHelper.computeMin(
               var14,
               var15,
               this.float27,
               this.float28,
               this.float29,
               this.float30,
               var24,
               var25,
               this.float13,
               this.float14,
               this.mjidp,
               (float)var16,
               var46,
               this.float2,
               var13,
               var20,
               var21,
               (float)var18.x,
               (float)var18.y,
               (float)var18.z,
               (float)var19.y,
               var47,
               (float)this.int2,
               1.0F,
               0.0F
            );
            float[] var27 = NeuroRecHelper.getInstance().computeFloats(var14, var15, (float)this.int2);
            StrokesHelper.Prediction var28 = NeuroRecHelper.getInstance().initInit(var26);
            float var29 = var28 != null ? MathHelper.clamp(var28.hitX, 0.2F, 0.8F) : 0.5F;
            float var30 = var28 != null ? MathHelper.clamp(var28.hitY * 0.6F + this.float15 * 0.4F, 0.3F, 0.85F) : this.float15;
            float var31 = var28 != null ? MathHelper.clamp(var28.hitZ, 0.2F, 0.8F) : 0.5F;
            this.float13 = this.float13 + (var29 - this.float13) * 0.08F;
            this.float14 = this.float14 + (var30 - this.float14) * 0.08F;
            this.mjidp = this.mjidp + (var31 - this.mjidp) * 0.08F;
            double var32 = MathHelper.lerp((double)this.float13, var45.minX, var45.maxX) + (double)this.float17 + (double)this.float23;
            double var34 = var45.minY + (var45.maxY - var45.minY) * (double)this.float14 + (double)this.float18;
            double var36 = MathHelper.lerp((double)this.mjidp, var45.minZ, var45.maxZ) + (double)this.float19 + (double)this.float24;
            if (var28 != null) {
               double var38 = 0.065;
               var32 += (double)var28.yawLead * var38 * var18.x;
               var34 += (double)var28.pitchLead * var38 * var18.y;
               var36 += (double)var28.yawLead * var38 * var18.z;
            }

            var32 = MathHelper.clamp(var32, var45.minX - 0.3, var45.maxX + 0.3);
            var34 = MathHelper.clamp(var34, var45.minY + 0.05, var45.maxY + 0.15);
            var36 = MathHelper.clamp(var36, var45.minZ - 0.3, var45.maxZ + 0.3);
            Vec3d var51 = new Vec3d(var32, var34, var36);
            return this.initGaussian(var51, var5, var28, var27);
         }

         if (vec3d != null) {
            return this.initGaussian(vec3d, null, null, null);
         } else {
            float var6 = CameraManager.getFlag() ? CameraManager.getFloat() : mc.player.getYaw();
            float var7 = CameraManager.getFlag() ? CameraManager.getMrula() : mc.player.getPitch();
            if (!this.flag) {
               this.runCurrent();
            }

            return this.initYaw(currentRotation, new Rotation(var6, var7));
         }
      }
   }

   public Rotation initGaussian(Vec3d aimPoint, LivingEntity target, StrokesHelper.Prediction pred, float[] datasetTrajMove) {
      Vec3d var5 = mc.player.getEyePos();
      double var6 = aimPoint.x - var5.x;
      double var8 = aimPoint.y - var5.y;
      double var10 = aimPoint.z - var5.z;
      double var12 = Math.sqrt(var6 * var6 + var10 * var10);
      float var14 = (float)Math.toDegrees(Math.atan2(var10, var6)) - 90.0F;
      float var15 = (float)(-Math.toDegrees(Math.atan2(var8, var12)));
      float var16 = (float)Math.hypot((double)this.float27, (double)this.float28);
      float[] var17 = NeuroRecHelper.getInstance().computeSpeed(var16);
      float var18 = pred != null ? Math.max(0.04F, Math.min(pred.yawShake, 0.3F)) : 0.12F;
      float var19 = pred != null ? Math.max(0.03F, Math.min(pred.pitchShake, 0.2F)) : 0.08F;
      float var20 = var17[0] * 0.7F + this.float7 * 0.3F;
      float var21 = var17[1] * 0.7F + this.float8 * 0.3F;
      this.float9 = (this.float9 + (float)(this.random.nextGaussian() * 0.03 * (double)var18)) * 0.84F;
      this.float10 = (this.float10 + (float)(this.random.nextGaussian() * 0.022 * (double)var19)) * 0.84F;
      this.float7 = MathHelper.clamp(this.float7 + this.float9, -0.22F, 0.22F);
      this.float8 = MathHelper.clamp(this.float8 + this.float10, -0.16F, 0.16F);
      this.float11 *= 0.65F;
      this.float12 *= 0.65F;
      float var22 = MathHelper.wrapDegrees(var14 - this.floatVal);
      float var23 = var15 - this.float2;
      float var24 = (float)Math.hypot((double)var22, (double)var23);
      float var25 = 0.0F;
      if (var24 > 4.0F && var24 < 70.0F) {
         float var26 = (float)Math.sin(Math.PI * Math.min(1.0, (double)var24 / 55.0));
         float var27 = pred != null ? pred.curveArc : 1.0F;
         var25 = var26 * this.float25 * this.float26 * var27 * 0.65F;
      }

      float var49 = 0.0F;
      if (var24 > 16.0F) {
         float var50 = pred != null ? pred.overshootRatio : 0.5F;
         var49 = Math.min(1.6F, (var24 - 16.0F) * 0.035F * var50);
      }

      float var51 = var14 + var20 + this.float11 + var25 + (var22 > 0.0F ? var49 : -var49);
      float var28 = MathHelper.clamp(var15 + var21 + this.float12, -89.5F, 89.5F);
      float var29 = MathHelper.wrapDegrees(var51 - this.floatVal);
      float var30 = var28 - this.float2;
      float var31 = (float)Math.hypot((double)var29, (double)var30);
      float var32;
      float var33;
      if (datasetTrajMove != null) {
         float var34 = datasetTrajMove[0];
         float var35 = datasetTrajMove[1];
         float var36 = Math.max(0.015F, var16 * 0.005F);
         float var37 = (float)this.random.nextGaussian() * var36;
         float var38 = (float)this.random.nextGaussian() * var36;
         var32 = var34 + var37;
         var33 = var35 + var38;
         this.float5 = var32;
         this.float6 = var33;
      } else {
         float var52 = var31 > 45.0F ? 0.4F : (var31 > 15.0F ? 0.34F : (var31 > 4.0F ? 0.26F : 0.19F));
         float var54 = var31 > 45.0F ? 0.48F : (var31 > 15.0F ? 0.52F : (var31 > 4.0F ? 0.58F : 0.64F));
         float var56 = pred != null ? MathHelper.clamp(pred.yawSpeed / 60.0F, 0.75F, 1.35F) : 1.0F;
         float var58 = pred != null ? MathHelper.clamp(pred.smoothness, 0.8F, 1.2F) : 1.0F;
         if (this.intVal > 0) {
            var56 *= 1.15F;
         }

         float var60 = var52 * var56;
         float var39 = MathHelper.clamp(var54 * var58, 0.35F, 0.75F);
         float var40 = pred != null ? MathHelper.clamp(pred.yawAccel * 0.08F, -2.5F, 2.5F) : 0.0F;
         float var41 = pred != null ? MathHelper.clamp(pred.pitchAccel * 0.08F, -1.8F, 1.8F) : 0.0F;
         float var42 = var29 * var60 + var40;
         float var43 = var30 * var60 + var41;
         this.float5 = (this.float5 + var42) * (1.0F - var39);
         this.float6 = (this.float6 + var43) * (1.0F - var39);
         float var44 = Math.max(5.0F, var31 * 0.55F);
         var32 = MathHelper.clamp(this.float5, -var44, var44);
         var33 = MathHelper.clamp(this.float6, -var44, var44);
      }

      float var53 = (float)((Double)mc.options.getMouseSensitivity().getValue()).doubleValue();
      float var55 = var53 * 0.6F + 0.2F;
      float var57 = var55 * var55 * var55 * 8.0F * 0.15F;
      if (var57 < 1.0E-5F) {
         var57 = 0.001F;
      }

      float var59 = var32 + this.float3;
      float var61 = var33 + this.float4;
      int var62 = Math.round(var59 / var57);
      int var63 = Math.round(var61 / var57);
      float var64 = (float)var62 * var57;
      float var65 = (float)var63 * var57;
      this.float3 = var59 - var64;
      this.float4 = var61 - var65;
      float var66 = this.floatVal + var64;
      float var67 = MathHelper.clamp(this.float2 + var65, -89.5F, 89.5F);
      float var45 = MathHelper.wrapDegrees(var66 - this.floatVal);
      float var46 = var67 - this.float2;
      float var47 = var45 - this.float27;
      float var48 = var46 - this.float28;
      this.float31 = var47;
      this.float32 = var48;
      this.float29 = this.float27;
      this.float30 = this.float28;
      this.float27 = var45;
      this.float28 = var46;
      this.floatVal = var66;
      this.float2 = var67;
      return new Rotation(this.floatVal, this.float2);
   }
}
