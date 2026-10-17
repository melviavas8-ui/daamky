package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.Rotation;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MotionConfig5 extends LegacySetting {
   public float floatVal;
   public float float2;
   public boolean mcanuy = false;
   public float float3 = 0.0F;
   public float float4 = 0.0F;
   public float float5 = 0.0F;
   public float mpohs = 0.0F;
   public float float6 = 0.6F;
   public float float7 = 0.6F;
   public long timeStamp = 0L;
   public float moewse = 0.0F;
   public float float8 = 0.0F;
   public float mjiut = 0.0F;
   public float float9 = 0.0F;
   public float float10 = 0.0F;
   public float float11 = 0.0F;
   public long long2 = 0L;
   public float float12 = 0.0F;
   public float float13 = 0.0F;
   public boolean flag = false;
   public float float14 = 0.0F;
   public float float15 = 0.0F;
   public float float16 = 0.0F;
   public float float17 = 0.0F;
   public float miuakqe = 1.0F;
   public float float18 = 0.0F;
   public float float19 = 0.0F;
   public float float20 = 0.0F;
   public int intVal = 0;
   public int int2 = -1;
   public final Random random = new Random();

   public MotionConfig5() {
      super("Polar");
   }

   public void runNext() {
      this.intVal = 0;
      this.float19 = this.float19 + (this.random.nextFloat() - 0.5F) * 0.08F;
      this.float20 = this.float20 + 0.06F + this.random.nextFloat() * 0.05F;
   }

   @Override
   public void run() {
      this.intVal = 3;
   }

   public void run2() {
      this.mcanuy = false;
      this.float3 = 0.0F;
      this.float4 = 0.0F;
      this.float5 = 0.0F;
      this.mpohs = 0.0F;
      this.float14 = 0.0F;
      this.float15 = 0.0F;
      this.float16 = 0.0F;
      this.float17 = 0.0F;
      this.float19 = 0.0F;
      this.float20 = 0.0F;
      this.intVal = 0;
      this.int2 = -1;
      this.timeStamp = 0L;
      this.long2 = 0L;
      this.flag = false;
      this.flag = false;
   }

   @Override
   public void runCurrent() {
      if (!this.flag) {
         super.runCurrent();
         if (this.mcanuy) {
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
         return this.initYaw(currentRotation, targetRotation);
      } else {
         if (!this.mcanuy) {
            this.floatVal = currentRotation != null ? currentRotation.getYaw() : mc.player.getYaw();
            this.float2 = currentRotation != null ? currentRotation.getPitch() : mc.player.getPitch();
            this.float3 = 0.0F;
            this.float4 = 0.0F;
            this.float5 = 0.0F;
            this.mpohs = 0.0F;
            this.mcanuy = true;
            this.int2 = entity != null ? entity.getId() : -1;
         }

         if (entity != null && entity.getId() != this.int2) {
            this.int2 = entity.getId();
            this.float5 *= 0.25F;
            this.mpohs *= 0.25F;
            this.float7 = 0.4F + this.random.nextFloat() * 0.4F;
            this.miuakqe = this.random.nextBoolean() ? 1.0F : -1.0F;
            this.float18 = 0.8F + this.random.nextFloat() * 1.6F;
         }

         long var5 = System.currentTimeMillis();
         if (var5 - this.timeStamp > 1400L + (long)this.random.nextInt(1200)) {
            this.timeStamp = var5;
            this.float7 = 0.38F + this.random.nextFloat() * 0.45F;
            this.float9 = (float)(this.random.nextGaussian() * 0.08);
            this.float10 = (float)(this.random.nextGaussian() * 0.06);
            this.float11 = (float)(this.random.nextGaussian() * 0.08);
         }

         this.float6 = this.float6 + (this.float7 - this.float6) * 0.06F;
         this.moewse = this.moewse + (this.float9 - this.moewse) * 0.07F;
         this.float8 = this.float8 + (this.float10 - this.float8) * 0.07F;
         this.mjiut = this.mjiut + (this.float11 - this.mjiut) * 0.07F;
         if (var5 - this.long2 > 2500L + (long)this.random.nextInt(2000)) {
            this.long2 = var5;
            this.flag = this.random.nextFloat() < 0.4F;
            if (this.flag) {
               float var7 = this.random.nextFloat() * (float) (Math.PI * 2);
               float var8 = 0.15F + this.random.nextFloat() * 0.22F;
               this.float12 = (float)Math.cos((double)var7) * var8;
               this.float13 = (float)Math.sin((double)var7) * var8;
            } else {
               this.float12 = 0.0F;
               this.float13 = 0.0F;
            }
         }

         Vec3d var45 = mc.player.getEyePos();
         Vec3d var46;
         if (entity instanceof LivingEntity var9) {
            Box var10 = var9.getBoundingBox();
            double var11 = var10.maxY - var10.minY;
            double var13 = var10.minY + var11 * (double)this.float6 + (double)this.float8;
            double var15 = (var9.getX() - var9.prevX) * 0.55;
            double var17 = (var9.getZ() - var9.prevZ) * 0.55;
            double var19 = var10.minX + (var10.maxX - var10.minX) * 0.5 + var15 + (double)this.moewse + (double)this.float12;
            double var21 = var10.minZ + (var10.maxZ - var10.minZ) * 0.5 + var17 + (double)this.mjiut + (double)this.float13;
            var19 = MathHelper.clamp(var19, var10.minX - 0.35, var10.maxX + 0.35);
            var13 = MathHelper.clamp(var13, var10.minY + 0.05, var10.maxY + 0.15);
            var21 = MathHelper.clamp(var21, var10.minZ - 0.35, var10.maxZ + 0.35);
            var46 = new Vec3d(var19, var13, var21);
         } else {
            if (vec3d == null) {
               return targetRotation;
            }

            var46 = vec3d;
         }

         double var47 = var46.x - var45.x;
         double var48 = var46.y - var45.y;
         double var50 = var46.z - var45.z;
         double var51 = Math.sqrt(var47 * var47 + var50 * var50);
         float var52 = (float)Math.toDegrees(Math.atan2(var50, var47)) - 90.0F;
         float var18 = (float)(-Math.toDegrees(Math.atan2(var48, var51)));
         this.float16 = (this.float16 + (float)(this.random.nextGaussian() * 0.035)) * 0.82F;
         this.float17 = (this.float17 + (float)(this.random.nextGaussian() * 0.025)) * 0.82F;
         this.float14 = MathHelper.clamp(this.float14 + this.float16, -0.25F, 0.25F);
         this.float15 = MathHelper.clamp(this.float15 + this.float17, -0.18F, 0.18F);
         this.float19 *= 0.65F;
         this.float20 *= 0.65F;
         float var54 = MathHelper.wrapDegrees(var52 - this.floatVal);
         float var20 = var18 - this.float2;
         float var56 = (float)Math.hypot((double)var54, (double)var20);
         float var22 = 0.0F;
         if (var56 > 4.0F && var56 < 75.0F) {
            float var23 = (float)Math.sin(Math.PI * Math.min(1.0, (double)var56 / 55.0));
            var22 = var23 * this.miuakqe * this.float18;
         }

         float var57 = 0.0F;
         if (var56 > 18.0F) {
            var57 = Math.min(1.8F, (var56 - 18.0F) * 0.04F);
         }

         float var24 = var52 + this.float14 + this.float19 + var22 + (var54 > 0.0F ? var57 : -var57);
         float var25 = MathHelper.clamp(var18 + this.float15 + this.float20, -89.5F, 89.5F);
         float var26 = MathHelper.wrapDegrees(var24 - this.floatVal);
         float var27 = var25 - this.float2;
         float var28 = (float)Math.hypot((double)var26, (double)var27);
         float var29;
         float var30;
         if (var28 > 45.0F) {
            var29 = 0.42F;
            var30 = 0.48F;
         } else if (var28 > 15.0F) {
            var29 = 0.36F;
            var30 = 0.52F;
         } else if (var28 > 4.0F) {
            var29 = 0.28F;
            var30 = 0.58F;
         } else {
            var29 = 0.2F;
            var30 = 0.65F;
         }

         float var31 = var26 * var29;
         float var32 = var27 * var29;
         this.float5 = (this.float5 + var31) * (1.0F - var30);
         this.mpohs = (this.mpohs + var32) * (1.0F - var30);
         float var33 = Math.max(4.5F, var28 * 0.55F);
         float var34 = MathHelper.clamp(this.float5, -var33, var33);
         float var35 = MathHelper.clamp(this.mpohs, -var33, var33);
         float var36 = (float)((Double)mc.options.getMouseSensitivity().getValue()).doubleValue();
         float var37 = var36 * 0.6F + 0.2F;
         float var38 = var37 * var37 * var37 * 8.0F * 0.15F;
         if (var38 < 1.0E-5F) {
            var38 = 0.001F;
         }

         float var39 = var34 + this.float3;
         float var40 = var35 + this.float4;
         int var41 = Math.round(var39 / var38);
         int var42 = Math.round(var40 / var38);
         float var43 = (float)var41 * var38;
         float var44 = (float)var42 * var38;
         this.float3 = var39 - var43;
         this.float4 = var40 - var44;
         this.floatVal += var43;
         this.float2 = MathHelper.clamp(this.float2 + var44, -89.5F, 89.5F);
         return new Rotation(this.floatVal, this.float2);
      }
   }
}
