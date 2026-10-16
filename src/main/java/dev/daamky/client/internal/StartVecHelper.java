package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.util.ArrayList;
import java.util.stream.IntStream;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class StartVecHelper implements MinecraftAccess {
   public static HitResult initInit(Vec3d startVec, Vec3d endVec) {
      return mc.world.raycast(new RaycastContext(startVec, endVec, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
   }

   public static boolean initInit(ClientPlayerEntity self, double x, double y, double z) {
      Vec3d var7 = new Vec3d(x, y, z);
      return mc.world != null && initInit(self.getEyePos(), var7).getType() != Type.BLOCK;
   }

   public static boolean computeFlag(ClientPlayerEntity selfEntity, Vec3d xyz, float scale) {
      return initInit(selfEntity, xyz.x, xyz.y, xyz.z);
   }

   public static Vec3d computeClass_243(Box aabb) {
      return initInit(aabb, true);
   }

   public static Vec3d initInit(Box aabb, boolean preferVisible) {
      ArrayList var2 = new ArrayList();
      double var3 = 0.01F;
      aabb = aabb.offset(-var3, -var3, -var3);
      double[] var5 = new double[]{aabb.maxX - aabb.minX, aabb.maxY - aabb.minY, (aabb.maxY - aabb.minY) / 1.05};
      double[] var6 = new double[]{aabb.minX + var5[0] / 2.0, aabb.minY, aabb.minZ + var5[0] / 2.0};
      double[] var7 = new double[]{aabb.minX, aabb.minY, aabb.minZ};
      double[] var8 = new double[]{aabb.maxX, aabb.maxY, aabb.maxZ};
      ClientPlayerEntity var9 = mc.player;
      if (var9 == null) {
         return null;
      } else {
         Vec3d var10 = new Vec3d(var9.getX(), var9.getY(), var9.getZ());
         float var11 = (float)(
            1.0
               - Math.min(var10.distanceTo(new Vec3d(var6[0], var6[1], var6[2])) / 5.0, 1.0)
                  * Math.min(var10.distanceTo(new Vec3d(var6[0], var9.getY(), var6[2])) / 0.6F, 1.0)
         );
         int var12 = (int)(5.0F + var11 * 12.0F);
         int var13 = (int)(6.0F + var11 * 18.0F);

         for (int var17 : IntStream.range(0, var12).toArray()) {
            boolean var18 = var17 == 0 || var17 == var12 - 1;
            double var19 = var7[0] + (var8[0] - var7[0]) * ((double)var17 / (double)(var12 - 1));

            for (int var24 : IntStream.range(0, var12).toArray()) {
               boolean var25 = var24 == 0 || var24 == var12 - 1;
               double var26 = var7[2] + (var8[2] - var7[2]) * ((double)var24 / (double)(var12 - 1));

               for (int var31 : IntStream.range(0, var13).toArray()) {
                  boolean var32 = var31 == 0 || var31 == var13 - 1;
                  double var33 = var7[1] + (var8[1] - var7[1]) * ((double)var31 / (double)(var13 - 1));
                  Vec3d var35 = new Vec3d(var19, var33, var26);
                  if ((var18 || var25 || var32) && computeFlag(var9, var35, 0.0F)) {
                     var2.add(var35);
                  }
               }
            }
         }

         if (var2.isEmpty()) {
            return new Vec3d((var7[0] + var8[0]) / 2.0, (var7[1] + var8[1]) / 2.0, (var7[2] + var8[2]) / 2.0);
         } else if (!preferVisible) {
            return (Vec3d)var2.get(0);
         } else {
            Vec3d var37 = (Vec3d)var2.get(0);
            double var38 = Double.MAX_VALUE;

            for (Vec3d var40 : var2) {
               double var41 = var40.x - var9.getX();
               double var42 = var40.y - var9.getEyeY();
               double var43 = var40.z - var9.getZ();
               float var44 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var43, var41)) - 90.0);
               float var45 = (float)(-Math.toDegrees(Math.atan2(var42, Math.hypot(var41, var43))));
               double var27 = Math.hypot((double)MathHelper.wrapDegrees(var44 - var9.getYaw()), (double)(var45 - var9.getPitch()));
               if (var27 < var38) {
                  var38 = var27;
                  var37 = var40;
               }
            }

            return var37;
         }
      }
   }

   public StartVecHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
