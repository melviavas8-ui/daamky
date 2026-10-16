package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.util.Optional;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class OffHelper implements MinecraftAccess {
   public static Vec3d class_243 = null;
   public static Vec3d class_2432 = null;
   public static long timeStamp = 0L;
   public static int intVal = -1;
   public static final double[] doubles = new double[768];

   public static Vec3d initInit(LivingEntity entity, boolean resolver, String wallMode) {
      if (entity != null && mc.player != null && mc.world != null) {
         boolean var3 = !wallMode.equals("Off");
         long var4 = System.currentTimeMillis();
         int var6 = entity.getId();
         if (class_2432 != null && var4 < longVal && intVal == var6) {
            return class_2432;
         } else {
            Box var7 = entity.getBoundingBox();
            Vec3d var8 = mc.player.getEyePos();
            Vec3d var9 = entity.getVelocity();
            boolean var10 = Math.hypot(var9.x, var9.z) > 0.15;
            double var11 = 0.0;
            double var13 = 0.0;
            double var15 = 0.0;
            if (var10) {
               double var17 = (double)computeInt(entity) * 0.0025;
               double var19 = MathHelper.clamp(1.0 + var17, 1.0, 2.5);
               var11 = MathHelper.clamp(var9.x * var19, -0.5, 0.5);
               var15 = MathHelper.clamp(var9.z * var19, -0.5, 0.5);
               var13 = var9.y * 0.5;
            }

            double var43 = (var7.minX + var7.maxX) * 0.5 + var11;
            double var44 = (var7.minY + var7.maxY) * 0.5 + var13;
            double var21 = (var7.minZ + var7.maxZ) * 0.5 + var15;
            double var23 = (var7.maxY - var7.minY) * 0.5;
            double var25 = (var7.maxX - var7.minX) * 0.5;
            Vec3d var27 = new Vec3d(var43, var7.minY + var13 + var23 * 1.5, var21);
            RaycastContext var28 = new RaycastContext(var8, var27, ShapeType.COLLIDER, FluidHandling.NONE, mc.player);
            if (mc.world.raycast(var28).getType() == Type.MISS) {
               class_243 = var27;
               class_2432 = var27;
               longVal = var4 + 80L;
               intVal = var6;
               return var27;
            } else {
               Vec3d[] var29 = new Vec3d[]{
                  new Vec3d(var43, var7.maxY + var13 - 0.1, var21),
                  new Vec3d(var43, var44, var21),
                  new Vec3d(var43, var7.minY + var13 + 0.2, var21),
                  new Vec3d(var43 + var25 * 0.7, var44 + 0.2, var21),
                  new Vec3d(var43 - var25 * 0.7, var44 + 0.2, var21),
                  new Vec3d(var43, var44 + 0.2, var21 + var25 * 0.7),
                  new Vec3d(var43, var44 + 0.2, var21 - var25 * 0.7)
               };
               Vec3d var30 = null;
               double var31 = Double.MAX_VALUE;
               Vec3d var33 = null;
               double var34 = Double.MAX_VALUE;

               for (Vec3d var39 : var29) {
                  double var40 = var8.squaredDistanceTo(var39);
                  if (var40 < var34) {
                     var34 = var40;
                     var33 = var39;
                  }

                  RaycastContext var42 = new RaycastContext(var8, var39, ShapeType.COLLIDER, FluidHandling.NONE, mc.player);
                  if (mc.world.raycast(var42).getType() == Type.MISS && var40 < var31) {
                     var31 = var40;
                     var30 = var39;
                  }
               }

               if (var30 == null) {
                  var30 = var3 ? (var33 != null ? var33 : var27) : var27;
               }

               if (class_243 != null) {
                  double var45 = class_243.distanceTo(var30);
                  if (var45 > 0.3) {
                     var30 = class_243.add(var30.subtract(class_243).multiply(0.25));
                  }
               }

               class_243 = var30;
               class_2432 = var30;
               longVal = var4 + 80L;
               intVal = var6;
               return var30;
            }
         }
      } else {
         return entity != null ? entity.getBoundingBox().getCenter() : net.minecraft.util.math.Vec3d.ZERO;
      }
   }

   public static int computeInt(Entity entity) {
      if (mc.getNetworkHandler() != null && entity instanceof PlayerEntity) {
         PlayerListEntry var1 = mc.getNetworkHandler().getPlayerListEntry(entity.getUuid());
         return var1 != null ? var1.getLatency() : 0;
      } else {
         return 0;
      }
   }

   public static float computeSqrt(Entity entity) {
      Box var1 = entity.getBoundingBox();
      Vec3d var2 = mc.player.getEyePos();
      double var3 = MathHelper.clamp(var2.x, var1.minX, var1.maxX);
      double var5 = MathHelper.clamp(var2.y, var1.minY, var1.maxY);
      double var7 = MathHelper.clamp(var2.z, var1.minZ, var1.maxZ);
      double var9 = var2.x - var3;
      double var11 = var2.y - var5;
      double var13 = var2.z - var7;
      return(floatVal)Math.sqrt(var9 * var9 + var11 * var11 + var13 * var13);
   }

   public static Vec3d computeClass_243(Entity entity) {
      Vec3d var1 = mc.player.getEyePos();
      return computeClass_243(var1, entity).subtract(var1);
   }

   public static Vec3d computeClass_243(Vec3d vec, Entity entity) {
      return initInit(vec, entity.getBoundingBox());
   }

   public static Vec3d initInit(Vec3d vec, Box AABB) {
      return new Vec3d(
         MathHelper.clamp(vec.x, AABB.minX, AABB.maxX),
         MathHelper.clamp(vec.y, AABB.minY, AABB.maxY),
         MathHelper.clamp(vec.z, AABB.minZ, AABB.maxZ)
      );
   }

   public static double computeDouble(Vec3d eye, Entity entity) {
      Box var2 = entity.getBoundingBox();
      double var3 = MathHelper.clamp(eye.x, var2.minX, var2.maxX);
      double var5 = MathHelper.clamp(eye.y, var2.minY, var2.maxY);
      double var7 = MathHelper.clamp(eye.z, var2.minZ, var2.maxZ);
      double var9 = var3 - eye.x;
      double var11 = var5 - eye.y;
      double var13 = var7 - eye.z;
      return var9 * var9 + var11 * var11 + var13 * var13;
   }

   public static boolean computeFlag(Vec3d eye, Entity entity, double maxReach) {
      return computeDouble(eye, entity) <= maxReach * maxReach;
   }

   public static boolean initInit(Vec3d rayOrigin, float yaw, float pitch, double distance, Entity entity, boolean throughWalls) {
      if (mc.player != null && mc.world != null && entity != null) {
         Box var7 = entity.getBoundingBox();
         if (!var7.contains(rayOrigin)
            && (mc.player == null || !var7.intersects(mc.player.getBoundingBox()) && !var7.expand(0.35).intersects(mc.player.getBoundingBox()))) {
            Vec3d var8 = net.minecraft.util.math.Vec3d.fromPolar(pitch, yaw).multiply(distance);
            float var9 = entity.getTargetingMargin();
            Box var10 = var9 > 0.0F ? var7.expand((double)var9) : var7;
            Optional var11 = var10.raycast(rayOrigin, rayOrigin.add(var8));
            if (var11.isEmpty()) {
               return false;
            } else {
               double var12 = ((Vec3d)var11.get()).distanceTo(rayOrigin);
               return var12 > distance
                  ? false
                  : throughWalls
                     || mc.world
                           .raycast(new RaycastContext(rayOrigin, (Vec3d)var11.get(), ShapeType.OUTLINE, FluidHandling.NONE, mc.player))
                           .getType()
                        == Type.MISS;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static void run() {
      class_2432 = null;
      longVal = 0L;
      intVal = -1;
      class_243 = null;
   }

   public OffHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
