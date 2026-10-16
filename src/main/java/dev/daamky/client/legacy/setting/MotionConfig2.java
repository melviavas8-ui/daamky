package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.GameTimer;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.rotation.RotationProfileManager;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MotionConfig2 extends LegacySetting {
   public float majxa;
   public float mtoaqu;
   public boolean flag = false;
   public long timeStamp = System.currentTimeMillis();
   public int intVal = -1;
   public float floatVal = 0.0F;
   public float float2 = 0.0F;
   public float float3 = 0.0F;
   public float float4 = 0.0F;
   public float float5 = 0.0F;
   public float float6 = 0.0F;
   public float float7 = 0.0F;
   public float float8 = 0.0F;
   public float float9 = 0.0F;
   public float float10 = 0.0F;
   public float float11 = 1.0F;
   public float float12 = 0.0F;
   public float float13 = 0.0F;
   public float float14 = 0.0F;
   public float float15 = 0.0F;
   public float minuk = 0.0F;
   public float float16 = 0.0F;
   public float float17 = 0.0F;
   public long long2 = 0L;
   public boolean flag2 = false;
   public long now = 0L;
   public float float18 = 0.0F;
   public float float19 = 0.0F;
   public final Random random = new Random();

   public MotionConfig2() {
      super("Custom");
   }

   public void runNext() {
      RotationProfileManager.getInstance().initActive2();
      this.floatVal9 = this.floatVal9 + (this.random.nextFloat() - 0.5F) * 0.06F;
      this.floatVal10 = this.floatVal10 + 0.04F + this.random.nextFloat() * 0.04F;
   }

   @Override
   public void run() {
      this.flag = false;
      this.intVal = -1;
      this.floatVal = 0.0F;
      this.floatVal2 = 0.0F;
      this.floatVal3 = 0.0F;
      this.floatVal4 = 0.0F;
      this.floatVal5 = 0.0F;
      this.floatVal6 = 0.0F;
      this.floatVal7 = 0.0F;
      this.floatVal8 = 0.0F;
      this.floatVal9 = 0.0F;
      this.floatVal10 = 0.0F;
      this.flag2 = false;
      this.flag = false;
   }

   @Override
   public Rotation initNext(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d, Entity entity) {
      if (mc.player == null || mc.world == null) {
         return targetRotation != null ? targetRotation : currentRotation;
      } else if (this.flag) {
         return this.initYaw(currentRotation, targetRotation);
      } else {
         long var5 = System.currentTimeMillis();
         RotationProfileManager var7 = RotationProfileManager.getInstance();
         if (!this.flag) {
            this.majxa = currentRotation != null ? currentRotation.getYaw() : mc.player.getYaw();
            this.mtoaqu = currentRotation != null ? currentRotation.getPitch() : mc.player.getPitch();
            this.floatVal = 0.0F;
            this.floatVal2 = 0.0F;
            this.floatVal3 = 0.0F;
            this.floatVal4 = 0.0F;
            this.flag = true;
            this.longVal = var5;
            this.intVal = entity != null ? entity.getId() : -1;
            this.handleChance(var7, var5);
         }

         if (entity != null && entity.getId() != this.intVal) {
            this.intVal = entity.getId();
            this.longVal = var5;
            this.floatVal3 *= 0.25F;
            this.floatVal4 *= 0.25F;
            this.floatVal11 = this.random.nextBoolean() ? 1.0F : -1.0F;
            this.floatVal12 = 0.6F + this.random.nextFloat() * 1.2F;
            this.handleChance(var7, var5);
         }

         if (var5 - this.longVal2 > 1200L + (long)this.random.nextInt(800)) {
            this.longVal2 = var5;
            this.minuk = (float)(this.random.nextGaussian() * 0.06);
            this.floatVal16 = (float)(this.random.nextGaussian() * 0.05);
            this.floatVal17 = (float)(this.random.nextGaussian() * 0.06);
         }

         this.floatVal13 = this.floatVal13 + (this.minuk - this.floatVal13) * 0.08F;
         this.floatVal14 = this.floatVal14 + (this.floatVal16 - this.floatVal14) * 0.08F;
         this.floatVal15 = this.floatVal15 + (this.floatVal17 - this.floatVal15) * 0.08F;
         long var8 = var5 - this.longVal;
         float var10 = MathHelper.clamp((float)(var8 % 1200L) / 1200.0F, 0.0F, 1.0F);
         float[] var11;
         if (var7.isSmoothPath()) {
            var11 = var7.computeRel(var10);
         } else {
            RotationProfileManager.AimNode var12 = var7.initActive();
            if (var12 == null) {
               var12 = var7.initActive2();
            }

            var11 = new float[]{var12.relX, var12.relY, var12.relZ};
         }

         Vec3d var44 = mc.player.getEyePos();
         Vec3d var13;
         if (entity instanceof LivingEntity var14) {
            Box var15 = var14.getBoundingBox();
            double var16 = var15.maxY - var15.minY;
            double var18 = (var15.maxX - var15.minX + var15.maxZ - var15.minZ) / 2.0;
            double var20 = var16 / 1.8;
            double var22 = (var14.getX() - var14.prevX) * 0.45;
            double var24 = (var14.getZ() - var14.prevZ) * 0.45;
            double var26 = var14.getX() + (double)var11[0] * var18 + var22 + (double)this.floatVal13;
            double var28 = var15.minY + MathHelper.clamp((double)var11[1] * var20 + (double)this.floatVal14, 0.1, var16 - 0.05);
            double var30 = var14.getZ() + (double)var11[2] * var18 + var24 + (double)this.floatVal15;
            var13 = new Vec3d(var26, var28, var30);
         } else {
            if (vec3d == null) {
               if (targetRotation != null) {
                  return targetRotation;
               }

               return currentRotation;
            }

            var13 = vec3d;
         }

         Rotation var45 = Rotation.computeRotation(var13, var44);
         if (var45 == null) {
            return targetRotation != null ? targetRotation : currentRotation;
         } else {
            float var46 = var7.getHumanizeJitter();
            float var47 = var7.getYawNoise() * (0.025F + var46 * 0.04F);
            float var17 = var7.getPitchNoise() * (0.02F + var46 * 0.03F);
            this.floatVal7 = (this.floatVal7 + (float)(this.random.nextGaussian() * (double)var47)) * 0.78F;
            this.floatVal8 = (this.floatVal8 + (float)(this.random.nextGaussian() * (double)var17)) * 0.78F;
            this.floatVal5 = MathHelper.clamp(this.floatVal5 + this.floatVal7, -0.3F, 0.3F);
            this.floatVal6 = MathHelper.clamp(this.floatVal6 + this.floatVal8, -0.22F, 0.22F);
            this.floatVal9 *= 0.65F;
            this.floatVal10 *= 0.65F;
            float var48 = 0.0F;
            float var19 = 0.0F;
            if (this.flag2) {
               long var49 = var5 - this.now;
               float var51 = var7.getOvershootDuration();
               if ((float)var49 < var51 && var51 > 10.0F) {
                  float var23 = (float)var49 / var51;
                  float var54 = (float)Math.sin((double)var23 * Math.PI);
                  var48 = this.floatVal18 * var54;
                  var19 = this.floatVal19 * var54;
               } else {
                  this.flag2 = false;
               }
            }

            float var50 = MathHelper.wrapDegrees(var45.getYaw() - this.majxa);
            float var21 = var45.getPitch() - this.mtoaqu;
            float var52 = (float)Math.hypot((double)var50, (double)var21);
            float var53 = 0.0F;
            if (var52 > 3.0F && var52 < 65.0F) {
               float var55 = (float)Math.sin(Math.PI * Math.min(1.0, (double)var52 / 50.0));
               var53 = var55 * this.floatVal11 * this.floatVal12;
            }

            float var56 = var7.computeCurve(var10);
            float var25 = var7.getRotationSpeed() * var56;
            float var57 = MathHelper.clamp(var7.getAccelFactor(), 0.05F, 1.0F);
            float var27 = Math.min(0.65F, 0.22F * var57 * var7.getYawSpeed() * var25);
            float var58 = 0.68F;
            float var29 = var50 * var27 + var53 * 0.15F;
            float var59 = var21 * var27 * 0.88F;
            this.floatVal3 = (this.floatVal3 + var29) * var58;
            this.floatVal4 = (this.floatVal4 + var59) * var58;
            float var31 = this.floatVal3 + this.floatVal5 + this.floatVal9 + var48;
            float var32 = this.floatVal4 + this.floatVal6 + this.floatVal10 + var19;
            float var33 = Math.min(var7.getMaxYawStep(), 55.0F);
            float var34 = Math.min(var7.getMaxPitchStep(), 35.0F);
            var31 = MathHelper.clamp(var31, -var33, var33);
            var32 = MathHelper.clamp(var32, -var34, var34);
            float var35 = GameTimer.getMc2();
            if (var35 <= 1.0E-4F) {
               var35 = 0.001F;
            }

            float var36 = currentRotation != null ? currentRotation.getYaw() : this.majxa;
            float var37 = currentRotation != null ? currentRotation.getPitch() : this.mtoaqu;
            float var38 = (var31 + this.floatVal) / var35;
            float var39 = (var32 + this.floatVal2) / var35;
            int var40 = Math.round(var38);
            int var41 = Math.round(var39);
            this.floatVal = var31 + this.floatVal - (float)var40 * var35;
            this.floatVal2 = var32 + this.floatVal2 - (float)var41 * var35;
            float var42 = (float)var40 * var35;
            float var43 = (float)var41 * var35;
            this.majxa = MathHelper.wrapDegrees(var36 + var42);
            this.mtoaqu = MathHelper.clamp(var37 + var43, -89.0F, 89.0F);
            return new Rotation(this.majxa, this.mtoaqu);
         }
      }
   }

   public void handleChance(RotationProfileManager profile, long now) {
      if (profile.getOvershootChance() > 0.01F && Math.random() < (double)profile.getOvershootChance()) {
         this.flag2 = true;
         this.now = nowx;
         float var4 = profile.getOvershootDistance();
         float var5 = (float)(Math.random() * Math.PI * 2.0);
         this.floatVal18 = (float)Math.cos((double)var5) * var4;
         this.floatVal19 = (float)Math.sin((double)var5) * var4 * 0.6F;
      } else {
         this.flag2 = false;
      }
   }
}
