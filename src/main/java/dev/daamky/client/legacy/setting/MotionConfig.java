package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.CameraManager;
import dev.daamky.client.internal.GameTimer;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.OffHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.StartVecHelper;
import dev.daamky.client.module.combat.AttackAura;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MotionConfig extends LegacySetting {
   public float floatVal;
   public float float2;
   public boolean flag = false;
   public float mgoji = ThreadLocalRandom.current().nextFloat() * 1000.0F;
   public int intVal = 0;
   public int int2 = 0;
   public long timeStamp = 0L;

   public MotionConfig() {
      super("ReallyWorld");
   }

   @Override
   public void run() {
      this.intVal2 = 0;
      this.intVal = 0;
   }

   public void run2() {
      this.intVal = 3;
   }

   @Override
   public Rotation initNext(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d, Entity entity) {
      if (mc.player != null && mc.world != null) {
         if (!this.flag && entity instanceof LivingEntity var5 && var5.isAlive()) {
            long var28 = System.nanoTime();
            if (this.longVal == 0L) {
               this.longVal = var28;
            }

            float var29 = (float)(var28 - this.longVal) / 1.0E9F;
            this.longVal = var28;
            var29 = MathHelper.clamp(var29, 0.001F, 0.05F);
            if (!this.flag) {
               this.floatVal = currentRotation.getYaw();
               this.floatVal2 = currentRotation.getPitch();
               this.flag = true;
            }

            this.intVal2++;
            if (this.intVal > 0) {
               this.intVal--;
            }

            boolean var9 = this.intVal > 0 || this.intVal2 <= 2;
            Vec3d var10 = mc.player.getEyePos();
            Box var11 = var5.getBoundingBox();
            boolean var12 = var11.contains(var10) || var10.squaredDistanceTo(var11.getCenter()) < 0.2;
            float var13;
            float var14;
            if (var12) {
               Vec3d var15 = var11.getCenter().subtract(var10);
               if (Math.hypot(var15.x, var15.z) < 0.08) {
                  var13 = currentRotation.getYaw();
                  var14 = MathHelper.clamp(currentRotation.getPitch() * 0.9F + 1.5F, -89.0F, 89.0F);
               } else {
                  var13 = (float)Math.toDegrees(Math.atan2(-var15.x, var15.z));
                  var14 = (float)MathHelper.clamp(
                     -Math.toDegrees(Math.atan2(var15.y, Math.hypot(var15.x, var15.z))), -89.0, 89.0
                  );
               }
            } else {
               Vec3d var31;
               if (vec3d != null) {
                  var31 = vec3d;
               } else {
                  var31 = StartVecHelper.initInit(var5.getBoundingBox(), true);
                  if (var31 == null) {
                     var31 = var5.getBoundingBox().getCenter();
                  }
               }

               double var16 = (double)((float)System.currentTimeMillis() + this.mgoji) * 0.0035;
               double var18 = (var11.maxX - var11.minX) * 0.25;
               double var20 = (var11.maxY - var11.minY) * 0.2;
               float var22 = (float)(Math.sin(var16 * 1.3) * var18 + Math.cos(var16 * 0.7) * var18 * 0.5);
               float var23 = (float)(Math.sin(var16 * 2.1) * var20);
               float var24 = (float)(Math.cos(var16 * 1.3) * var18 + Math.sin(var16 * 0.7) * var18 * 0.5);
               float var25 = var9 ? 0.15F : 1.0F;
               Vec3d var26 = new Vec3d(
                  MathHelper.clamp(var31.x + (double)(var22 * var25), var11.minX + 0.02, var11.maxX - 0.02),
                  MathHelper.clamp(var31.y + (double)(var23 * var25), var11.minY + 0.05, var11.maxY - 0.05),
                  MathHelper.clamp(var31.z + (double)(var24 * var25), var11.minZ + 0.02, var11.maxZ - 0.02)
               );
               Vec3d var27 = var26.subtract(var10);
               var13 = (float)Math.toDegrees(Math.atan2(-var27.x, var27.z));
               var14 = (float)MathHelper.clamp(
                  -Math.toDegrees(Math.atan2(var27.y, Math.hypot(var27.x, var27.z))), -89.0, 89.0
               );
            }

            float var32 = MathHelper.wrapDegrees(var13 - currentRotation.getYaw());
            float var33 = var14 - currentRotation.getPitch();
            float var17 = (float)Math.hypot((double)var32, (double)var33);
            double var34 = (double)AttackAura.getInstance().getOverride();
            boolean var35 = var12 || OffHelper.initInit(var10, currentRotation.getYaw(), currentRotation.getPitch(), var34, var5, true);
            float var21;
            if (var35) {
               var21 = 18.0F + ThreadLocalRandom.current().nextFloat() * 6.0F;
            } else if (var17 < 15.0F) {
               var21 = 35.0F + var17 * 2.5F;
            } else if (var17 < 60.0F) {
               var21 = 65.0F + var17 * 3.5F;
            } else {
               var21 = 120.0F + var17 * 4.0F;
            }

            float var36 = MathHelper.clamp(var21 * 8.0F, 150.0F, 1200.0F);
            float var37 = var36 * var29;
            float var38 = MathHelper.clamp(var32 * 0.45F, -var37, var37);
            float var39 = MathHelper.clamp(var33 * 0.42F, -var37 * 0.6F, var37 * 0.6F);
            if (!var9 && !var12) {
               var38 += (ThreadLocalRandom.current().nextFloat() - 0.5F) * 0.35F;
               var39 += (ThreadLocalRandom.current().nextFloat() - 0.5F) * 0.2F;
            }

            this.floatVal = currentRotation.getYaw() + var38;
            this.floatVal2 = MathHelper.clamp(currentRotation.getPitch() + var39, -89.0F, 89.0F);
            Rotation var40 = this.initYaw(new Rotation(this.floatVal, this.floatVal2), currentRotation);
            if (var9 && !var12 && !OffHelper.initInit(var10, var40.getYaw(), var40.getPitch(), var34, var5, true)) {
               var40 = this.initYaw(new Rotation(var13, var14), currentRotation);
            }

            return var40;
         }

         float var6 = CameraManager.getFlag() ? CameraManager.getFloat() : (mc.player != null ? mc.player.getYaw() : 0.0F);
         float var7 = CameraManager.getFlag() ? CameraManager.getMrula() : (mc.player != null ? mc.player.getPitch() : 0.0F);
         Rotation var8 = new Rotation(var6, var7);
         if (!this.flag) {
            this.runCurrent();
         }

         return this.initYaw(currentRotation, var8);
      } else {
         return targetRotation;
      }
   }

   @Override
   public Rotation initYaw(Rotation targetRot, Rotation previousRot) {
      float var3 = GameTimer.getMc2();
      if (var3 <= 1.0E-4F) {
         return targetRot;
      } else {
         float var4 = MathHelper.wrapDegrees(targetRot.getYaw() - previousRot.getYaw());
         float var5 = targetRot.getPitch() - previousRot.getPitch();
         float var6 = (float)Math.round(var4 / var3) * var3;
         float var7 = (float)Math.round(var5 / var3) * var3;
         float var8 = MathHelper.wrapDegrees(previousRot.getYaw() + var6);
         float var9 = MathHelper.clamp(previousRot.getPitch() + var7, -89.0F, 89.0F);
         return new Rotation(var8, var9);
      }
   }

   public void run3() {
      this.flag = false;
      this.intVal = 0;
      this.intVal2 = 0;
      this.longVal = 0L;
      this.flag = false;
   }
}
