package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class RotationMath implements MinecraftAccess {
   public static float floatVal = -1.0F;
   public static float float2 = -1.0F;

   public static Rotation getMc() {
      return new Rotation(mc.player.getYaw(), mc.player.getPitch());
   }

   public static Rotation computeRotation(Vec3d to) {
      return initAtan2(to.subtract(mc.player.getEyePos()));
   }

   public static boolean computeAbs(Vec3d vec3d, float fov) {
      Rotation var2 = initAtan(vec3d);
      float var3 = MathHelper.wrapDegrees(var2.getYaw() - mc.player.getYaw());
      float var4 = MathHelper.wrapDegrees(var2.getPitch() - mc.player.getPitch());
      return Math.abs(var3) <= fov && Math.abs(var4) <= fov;
   }

   public static Rotation initAtan(Vec3d position) {
      if (position == null) {
         return Rotation.rotation2;
      } else {
         Vec3d var1 = mc.player.getPos().add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
         float var2 = (float)(position.x - var1.x);
         float var3 = (float)(position.y - var1.y);
         float var4 = (float)(position.z - var1.z);
         float var5 = (float)Math.sqrt((double)(var2 * var2 + var4 * var4));
         float var6 = (float)(Math.toDegrees(Math.atan2((double)var4, (double)var2)) - 90.0);
         float var7 = (float)(-Math.toDegrees(Math.atan2((double)var3, (double)var5)));
         return new Rotation(var6, var7);
      }
   }

   public static Rotation initInit(Vec2f vector2f) {
      return new Rotation(vector2f.y, vector2f.x);
   }

   public static Rotation initAtan2(Vec3d vector) {
      return new Rotation(
         MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(vector.z, vector.x)) - 90.0F),
         MathHelper.wrapDegrees((float)Math.toDegrees(-Math.atan2(vector.y, Math.hypot(vector.x, vector.z))))
      );
   }

   public static Rotation initYaw(Rotation start, Rotation end) {
      float var2 = MathHelper.wrapDegrees(end.getYaw() - start.getYaw());
      float var3 = MathHelper.wrapDegrees(end.getPitch() - start.getPitch());
      return new Rotation(var2, var3);
   }

   public static Vec3d initInit(Entity entity) {
      Vec3d var1 = mc.player.getEyePos();
      Box var2 = entity.getBoundingBox();
      return new Vec3d(
         MathHelper.clamp(var1.x, var2.minX, var2.maxX),
         MathHelper.clamp(var1.y, var2.minY, var2.maxY),
         MathHelper.clamp(var1.z, var2.minZ, var2.maxZ)
      );
   }

   public static Vec3d computeClass_243(Entity entity, Vec3d end) {
      Box var2 = entity.getBoundingBox();
      Vec3d var3 = mc.getCameraEntity().getCameraPosVec(1.0F);
      Vec3d var4 = new Vec3d(var2.minX, var2.minY, var2.minZ);
      Vec3d var5 = new Vec3d(var2.maxX, var2.maxY, var2.maxZ);
      double var6 = -Double.MAX_VALUE;
      double var8 = Double.MAX_VALUE;
      Vec3d var10 = end.normalize();

      for (int var11 = 0; var11 < 3; var11++) {
         double var12;
         double var14;
         double var16;
         double var18;
         switch (var11) {
            case 0:
               var18 = var10.x;
               var16 = var4.x;
               var14 = var5.x;
               var12 = var3.x;
               break;
            case 1:
               var18 = var10.y;
               var16 = var4.y;
               var14 = var5.y;
               var12 = var3.y;
               break;
            case 2:
               var18 = var10.z;
               var16 = var4.z;
               var14 = var5.z;
               var12 = var3.z;
               break;
            default:
               continue;
         }

         if (Math.abs(var18) < 1.0E-7) {
            if (var12 < var16 || var12 > var14) {
               return end;
            }
         } else {
            double var20 = (var16 - var12) / var18;
            double var22 = (var14 - var12) / var18;
            if (var20 > var22) {
               double var24 = var20;
               var20 = var22;
               var22 = var24;
            }

            if ((var6 = Math.max(var6, var20)) > (var8 = Math.min(var8, var22))) {
               return end;
            }
         }
      }

      double var26 = var3.distanceTo(end);
      return !(var6 > var26) && !(var6 < 0.0) ? var3.add(var10.multiply(var6)) : end;
   }

   public static float getMax() {
      float var0 = ((Double)mc.options.getMouseSensitivity().getValue()).floatValue();
      if (var0 != floatVal) {
         floatVal = var0;
         float var1 = var0 * 0.6F + 0.2F;
         float2 = var1 * var1 * var1 * 1.2F;
      }

      return Math.max(float2, 1.0E-6F);
   }

   public static float computeRound(float value) {
      float var1 = getMax();
      return (float)Math.round(value / var1) * var1;
   }

   public static Rotation initInit(Rotation rotation) {
      return new Rotation(computeRound(rotation.getYaw()), MathHelper.clamp(computeRound(rotation.getPitch()), -90.0F, 90.0F));
   }

   public RotationMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
