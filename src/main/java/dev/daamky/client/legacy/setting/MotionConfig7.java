package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.CameraManager;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.Rotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class MotionConfig7 extends LegacySetting {
   public float floatVal;
   public float float2;
   public boolean flag = false;
   public long timeStamp = ThreadLocalRandom.current().nextLong(1000L, 2001L);
   public long momott = System.currentTimeMillis() + this.longVal;
   public int intVal = 0;
   public int int2 = 1;
   public float float3 = 0.0F;
   public float float4 = 0.0F;
   public long long2 = ThreadLocalRandom.current().nextLong(2000L, 4001L);
   public long long3 = System.currentTimeMillis() + this.longVal2;
   public int int3 = 0;
   public int mbovumb = 1;
   public float float5 = 40.0F;
   public float float6 = 12.0F;
   public static final long long4 = 1000L;
   public static final long long5 = 2500L;
   public float float7 = 0.0F;
   public float float8 = 0.0F;
   public int int4 = 0;
   public long long6 = 0L;
   public long long7 = 0L;
   public boolean flag2 = false;
   public int majhos = 0;
   public int miqca = ThreadLocalRandom.current().nextInt(15, 20);
   public static final Vec3d[] class_243List = new Vec3d[32];
   public static int int5 = 0;
   public final MotionConfig7.JitterPreset[] jitterPresetList = new MotionConfig7.JitterPreset[]{new MotionConfig7.JitterPreset() {
      @Override
      public float computeSin(long t) {
         return(floatVal)(Math.cos((double)t / 90.0) * 12.0);
      }

      @Override
      public float computeCos(long t) {
         return(floatVal)(Math.sin((double)t / 90.0) * 16.0);
      }
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }, new MotionConfig7.JitterPreset() {
   }};
   public final MotionConfig7.SpeedPreset[] speedPresetList = new MotionConfig7.SpeedPreset[]{new MotionConfig7.SpeedPreset() {
      @Override
      public float getFloat() {
         return 40.0F;
      }

      @Override
      public float getFloat2() {
         return 12.0F;
      }
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }, new MotionConfig7.SpeedPreset() {
   }};

   public MotionConfig7() {
      super("FunTime");
   }

   public void runCurrent2() {
      this.intVal4 = 3;
      this.majhos++;
      if (this.majhos >= this.miqca) {
         this.majhos = 0;
         this.miqca = ThreadLocalRandom.current().nextInt(15, 20);
         this.flag2 = true;
         this.longVal6 = System.currentTimeMillis();
         this.longVal7 = ThreadLocalRandom.current().nextLong(50L, 71L);
      }
   }

   @Override
   public void run() {
      this.intVal4 = 3;
   }

   public void runCurrent3() {
      this.flag = false;
      this.floatVal3 = 0.0F;
      this.floatVal4 = 0.0F;
      this.floatVal5 = 44.0F;
      this.floatVal6 = 120.0F;
      this.intVal4 = 0;
      this.flag2 = false;
      this.majhos = 0;
      this.miqca = ThreadLocalRandom.current().nextInt(15, 20);
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
            this.floatVal7 = this.floatVal;
            this.floatVal8 = this.floatVal2;
            this.flag = true;
         }

         if (!this.flag && entity instanceof LivingEntity var5 && var5.isAlive()) {
            Box var53 = var5.getBoundingBox();
            long var54 = System.currentTimeMillis();
            double var9 = 0.06 * Math.sin((double)var54 / 250.0);
            double var11 = (double)var5.getHeight() / 12.0 * Math.cos((double)var54 / 850.0);
            double var13 = 0.06 * Math.cos((double)var54 / 250.0);
            Vec3d var15 = initInit(var53, false);
            Vec3d var16 = new Vec3d(
               MathHelper.clamp(var15.x + var9, var53.minX + 0.05, var53.maxX - 0.05),
               MathHelper.clamp(var15.y + var11, var53.minY + 0.15, var53.maxY - 0.15),
               MathHelper.clamp(var15.z + var13, var53.minZ + 0.05, var53.maxZ - 0.05)
            );
            Vec3d var17 = var16.subtract(mc.player.getEyePos()).normalize();
            float var18 = (float)Math.toDegrees(Math.atan2(-var17.x, var17.z));
            float var19 = (float)MathHelper.clamp(
               -Math.toDegrees(Math.atan2(var17.y, Math.hypot(var17.x, var17.z))), -89.0, 89.0
            );
            boolean var20 = this.intVal4 > 0;
            if (var20) {
               this.intVal4--;
               this.floatVal7 = var18;
               this.floatVal8 = var19;
            } else {
               this.floatVal7 = this.floatVal7 + (float)((double)MathHelper.wrapDegrees(var18 - this.floatVal7) * 0.45);
               this.floatVal8 = this.floatVal8 + (float)((double)(var19 - this.floatVal8) * 0.45);
            }

            if (var54 >= this.momott) {
               this.intVal = this.intVal2;
               this.intVal2 = ThreadLocalRandom.current().nextInt(this.jitterPresetList.length);

               while (this.intVal2 == this.intVal) {
                  this.intVal2 = ThreadLocalRandom.current().nextInt(this.jitterPresetList.length);
               }

               this.longVal = ThreadLocalRandom.current().nextLong(1000L, 2001L);
               this.momott = var54 + this.longVal;
            }

            MotionConfig7.JitterPreset var21 = this.jitterPresetList[this.intVal];
            MotionConfig7.JitterPreset var22 = this.jitterPresetList[this.intVal2];
            float var23 = 1.0F - (float)(this.momott - var54) / (float)this.longVal;
            var23 = MathHelper.clamp(var23, 0.0F, 1.0F);
            var23 = var23 * var23 * (3.0F - 2.0F * var23);
            var23 = var23 * var23 * (3.0F - 2.0F * var23);
            float var24 = var21.computeSin(var54);
            float var25 = var22.computeSin(var54);
            float var26 = var21.computeCos(var54);
            float var27 = var22.computeCos(var54);
            float var28 = MathHelper.lerp(var23, var24, var25);
            float var29 = MathHelper.lerp(var23, var26, var27);
            this.floatVal3 = this.floatVal3 + (var28 - this.floatVal3) * 0.6F;
            this.floatVal4 = this.floatVal4 + (var29 - this.floatVal4) * 0.6F;
            float var30 = this.floatVal3;
            float var31 = this.floatVal4;
            if (this.flag2 && var54 - this.longVal6 >= this.longVal7) {
               this.flag2 = false;
            }

            if (var54 >= this.longVal3) {
               this.intVal3 = this.mbovumb;
               this.mbovumb = ThreadLocalRandom.current().nextInt(this.speedPresetList.length);

               while (this.mbovumb == this.intVal3) {
                  this.mbovumb = ThreadLocalRandom.current().nextInt(this.speedPresetList.length);
               }

               this.longVal2 = ThreadLocalRandom.current().nextLong(2000L, 4001L);
               this.longVal3 = var54 + this.longVal2;
            }

            MotionConfig7.SpeedPreset var32 = this.speedPresetList[this.intVal3];
            MotionConfig7.SpeedPreset var33 = this.speedPresetList[this.mbovumb];
            float var34 = 1.0F - (float)(this.longVal3 - var54) / (float)this.longVal2;
            var34 = MathHelper.clamp(var34, 0.0F, 1.0F);
            var34 = var34 * var34 * (3.0F - 2.0F * var34);
            var34 = var34 * var34 * (3.0F - 2.0F * var34);
            float var35 = MathHelper.lerp(var34, var32.getFloat(), var33.getFloat());
            float var36 = MathHelper.lerp(var34, var32.getFloat2(), var33.getFloat2());
            this.floatVal5 = this.floatVal5 + (var35 - this.floatVal5) * 0.5F;
            this.floatVal6 = this.floatVal6 + (var36 - this.floatVal6) * 0.5F;
            float var37 = this.floatVal5 + computeCurrent(-2.5F, 2.5F);
            float var38 = this.floatVal6 + computeCurrent(-1.0F, 1.0F);
            if (var20) {
               var37 *= 1.8F;
               var38 *= 1.6F;
            }

            float var39 = var20 ? 0.2F : 1.0F;
            float var40 = this.floatVal7 + var30 * var39;
            float var41 = MathHelper.clamp(this.floatVal8 + var31 * var39, -89.0F, 89.0F);
            float var42 = this.floatVal;
            float var43 = this.floatVal2;
            float var44 = MathHelper.wrapDegrees(var40 - var42);
            float var45 = var41 - var43;
            float var46 = MathHelper.clamp(var44, -var37, var37);
            float var47 = MathHelper.clamp(var45, -var38, var38);
            float var48 = var42 + var46;
            float var49 = MathHelper.clamp(var43 + var47, -89.0F, 89.0F);
            float var50 = computeRound(var42, var48);
            float var51 = MathHelper.clamp(computeRound(var43, var49), -89.0F, 89.0F);
            Vec3d var52 = Vec3d.fromPolar(var51, var50).multiply(6.0);
            if (var53.expand(0.08).raycast(mc.player.getEyePos(), mc.player.getEyePos().add(var52)).isEmpty()) {
               var50 = MathHelper.lerp(0.5F, var50, var18);
               var51 = MathHelper.lerp(0.5F, var51, var19);
               var50 = computeRound(var42, var50);
               var51 = MathHelper.clamp(computeRound(var43, var51), -89.0F, 89.0F);
            }

            this.floatVal = var50;
            this.floatVal2 = var51;
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

   public static float computeRound(float lastAngle, float current) {
      if (mc.options == null) {
         return current;
      } else {
         double var2 = (Double)mc.options.getMouseSensitivity().getValue() * 0.6000000498956214 + 0.19999998556632664;
         double var4 = var2 * var2 * var2 * 8.0 * 0.15000007F;
         double var6 = (double)MathHelper.wrapDegrees(current - lastAngle);
         return(floatVal)((double)lastAngle + (double)Math.round(var6 / var4) * var4);
      }
   }

   public static float computeCurrent(float min, float max) {
      return min + ThreadLocalRandom.current().nextFloat() * (max - min);
   }

   public static Vec3d initInit(Box aabb, boolean alwaysMultipoints) {
      if (aabb != null && mc.player != null) {
         double[] var2 = new double[]{aabb.maxX - aabb.minX, aabb.maxY - aabb.minY, (aabb.maxY - aabb.minY) / 1.1F};
         double[] var3 = new double[]{aabb.minX + var2[0] / 2.0, aabb.minY, aabb.minZ + var2[0] / 2.0};
         double[] var4 = new double[]{mc.player.getY() - var3[1], computeSqrt(mc.player, var3[0], var3[2])};
         double var5 = MathHelper.clamp((var4[1] - var2[0] / 2.0) / (5.0 + var2[0] / 2.0), 0.1, 0.95);
         double var7 = MathHelper.clamp(var5 * var5, 0.0, 1.0);
         double var9 = MathHelper.clamp(var2[2] / 2.0 * var7 + var2[2] / 2.0 * MathHelper.clamp(var4[0] + var7, 0.0, 1.0), 0.0, var2[2]);
         Vec3d var11 = new Vec3d(var3[0], var3[1] + var9, var3[2]);
         if (!alwaysMultipoints && !initInit(mc.player, var11)) {
            var11 = var11.add(0.0, -var9 / 2.0, 0.0);
         }

         if (!(var2[1] <= 1.0) && (alwaysMultipoints || !initInit(mc.player, var11))) {
            List var12 = setAabb(aabb);
            float var13 = 1.0F - (float)Math.max(Math.min((var4[1] - 2.0) / 3.0, 1.0), 0.0);
            Vec3d var14 = new Vec3d(
               mc.player.getX(),
               mc.player.getY() + 0.6F + MathHelper.lerp((double)var13, var9, var9 / 2.5),
               mc.player.getZ()
            );
            if (var12 != null && !var12.isEmpty()) {
               Vec3d var15 = (Vec3d)var12.get(0);
               double var16 = var14.squaredDistanceTo(var15);

               for (int var18 = 1; var18 < var12.size(); var18++) {
                  double var19 = var14.squaredDistanceTo((Vec3d)var12.get(var18));
                  if (var19 < var16) {
                     var16 = var19;
                     var15 = (Vec3d)var12.get(var18);
                  }
               }

               return var15;
            } else {
               return var11;
            }
         } else {
            return var11;
         }
      } else {
         return mc.player != null ? mc.player.getEyePos() : Vec3d.ZERO;
      }
   }

   public static double computeSqrt(ClientPlayerEntity self, double x, double z) {
      double var5 = self.getX() - x;
      double var7 = self.getZ() - z;
      return Math.sqrt(var5 * var5 + var7 * var7);
   }

   public static boolean initInit(ClientPlayerEntity self, Vec3d vec) {
      if (mc.world == null) {
         return false;
      } else {
         Vec3d var2 = self.getEyePos();
         return mc.world.raycast(new RaycastContext(var2, vec, ShapeType.COLLIDER, FluidHandling.NONE, self)).getType()
            != Type.BLOCK;
      }
   }

   public static boolean initInit(ClientPlayerEntity self, double x, double y, double z) {
      return initInit(self, new Vec3d(x, y, z));
   }

   public static boolean computeFlag(ClientPlayerEntity selfEntity, Vec3d xyz, float scale) {
      return scale == 0.0F
         ? initInit(selfEntity, xyz.x, xyz.y, xyz.z)
         : initInit(selfEntity, xyz.x, xyz.y, xyz.z)
            && initInit(selfEntity, xyz.x, xyz.y + (double)scale, xyz.z)
            && initInit(selfEntity, xyz.x, xyz.y - (double)scale, xyz.z)
            && initInit(selfEntity, xyz.x + (double)scale, xyz.y, xyz.z)
            && initInit(selfEntity, xyz.x - (double)scale, xyz.y, xyz.z)
            && initInit(selfEntity, xyz.x, xyz.y, xyz.z + (double)scale)
            && initInit(selfEntity, xyz.x, xyz.y, xyz.z - (double)scale);
   }

   public static List<Vec3d> setAabb(Box aabb) {
      if (aabb != null && mc.player != null) {
         ClientPlayerEntity var1 = mc.player;
         Vec3d var2 = var1.getEyePos();
         double var3 = aabb.minX + 0.01;
         double var5 = aabb.maxX - 0.01;
         double var7 = aabb.minY + 0.01;
         double var9 = aabb.maxY - 0.01;
         double var11 = aabb.minZ + 0.01;
         double var13 = aabb.maxZ - 0.01;
         double var15 = (var3 + var5) * 0.5;
         double var17 = (var7 + var9) * 0.5;
         double var19 = (var11 + var13) * 0.5;
         int5 = 0;
         initInit(var15, var9 - 0.05, var19);
         initInit(var15, var9 - (var9 - var7) * 0.25, var19);
         initInit(var15, var17, var19);
         initInit(var15, var7 + (var9 - var7) * 0.25, var19);
         initInit(var15, var7 + 0.1, var19);
         initInit(var5 - 0.03, var17, var19);
         initInit(var3 + 0.03, var17, var19);
         initInit(var15, var17, var13 - 0.03);
         initInit(var15, var17, var11 + 0.03);
         initInit(var5 - 0.03, var9 - 0.1, var13 - 0.03);
         initInit(var3 + 0.03, var9 - 0.1, var13 - 0.03);
         initInit(var5 - 0.03, var9 - 0.1, var11 + 0.03);
         initInit(var3 + 0.03, var9 - 0.1, var11 + 0.03);
         initInit(var5 - 0.03, var17, var13 - 0.03);
         initInit(var3 + 0.03, var17, var13 - 0.03);
         initInit(var5 - 0.03, var17, var11 + 0.03);
         initInit(var3 + 0.03, var17, var11 + 0.03);
         initInit(var15, var9 - (var9 - var7) * 0.15, var19);
         initInit(var15, var7 + (var9 - var7) * 0.35, var19);
         initInit(var5 - 0.03, var9 - (var9 - var7) * 0.25, var19);
         initInit(var3 + 0.03, var9 - (var9 - var7) * 0.25, var19);
         initInit(var15, var9 - (var9 - var7) * 0.25, var13 - 0.03);
         initInit(var15, var9 - (var9 - var7) * 0.25, var11 + 0.03);
         initInit(var15, var9 - 0.15, var19);
         int var21 = int5;

         for (int var22 = 1; var22 < var21; var22++) {
            Vec3d var23 = class_243List[var22];
            double var24 = var2.squaredDistanceTo(var23);

            int var26;
            for (var26 = var22 - 1; var26 >= 0 && var2.squaredDistanceTo(class_243List[var26]) > var24; var26--) {
               class_243List[var26 + 1] = class_243List[var26];
            }

            class_243List[var26 + 1] = var23;
         }

         ArrayList var27 = new ArrayList(8);

         for (int var28 = 0; var28 < var21; var28++) {
            Vec3d var29 = class_243List[var28];
            if (mc.world.raycast(new RaycastContext(var2, var29, ShapeType.COLLIDER, FluidHandling.NONE, var1)).getType()
               != Type.BLOCK) {
               var27.add(var29);
            }
         }

         return var27.isEmpty() ? null : var27;
      } else {
         return null;
      }
   }

   public static void initInit(double x, double y, double z) {
      if (int5 < class_243List.length) {
         class_243List[int5++] = new Vec3d(x, y, z);
      }
   }

   interface JitterPreset {
      float computeSin(long var1);

      float computeCos(long var1);
   }

   interface SpeedPreset {
      float getFloat();

      float getFloat2();
   }
}
