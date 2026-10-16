package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class RaytraceUtil implements MinecraftAccess {
   public static boolean computeFlag(Vec3d start, Vec3d entityPos, BlockHitResult blockHit) {
      if (blockHit == null || blockHit.getType() != Type.BLOCK) {
         return false;
      } else if (mc.world == null) {
         return false;
      } else {
         BlockState var3 = mc.world.getBlockState(blockHit.getBlockPos());
         if (var3 == null) {
            return false;
         } else if (!var3.getCollisionShape(mc.world, blockHit.getBlockPos()).isEmpty()) {
            Vec3d var4 = blockHit.getPos();
            return var4.squaredDistanceTo(start) < entityPos.squaredDistanceTo(start);
         } else {
            return false;
         }
      }
   }

   public static BlockHitResult computeClass_3965(Rotation rotation, double range, boolean includeFluids, float tickDelta) {
      return initInit(range, includeFluids, mc.player.getCameraPosVec(tickDelta), rotation.initInit(), mc.cameraEntity);
   }

   public static BlockHitResult initInit(double range, boolean includeFluids, Vec3d start, Vec3d direction, Entity entity) {
      Vec3d var6 = start.add(direction.x * range, direction.y * range, direction.z * range);
      return mc.world
         .raycast(new RaycastContext(start, var6, ShapeType.OUTLINE, includeFluids ? FluidHandling.ANY : FluidHandling.NONE, entity));
   }

   public static EntityHitResult raytraceEntity(double range, Rotation rotation, boolean ignoreWalls) {
      return raytraceEntity(range, rotation, ignoreWalls, RaytraceUtil::lambda$raytraceEntity$0, 0.0F);
   }

   public static EntityHitResult raytraceEntity(double range, Rotation rotation, boolean ignoreWalls, float expand) {
      return raytraceEntity(range, rotation, ignoreWalls, RaytraceUtil::lambda$raytraceEntity$1, expand);
   }

   public static EntityHitResult raytraceEntity(double range, Rotation rotation, boolean ignoreWalls, Predicate<Entity> filter, float expand) {
      Entity var7 = mc.getCameraEntity();
      if (var7 == null) {
         return null;
      } else {
         Vec3d var8 = var7.getEyePos();
         Vec3d var9 = rotation.initInit();
         Vec3d var10 = var8.add(var9.x * range, var9.y * range, var9.z * range);
         EntityHitResult var11 = ProjectileUtil.raycast(
            var7,
            var8,
            var10,
            var7.getBoundingBox().stretch(var9.multiply(range)).expand(1.0, 1.0, 1.0),
            RaytraceUtil::lambda$raytraceEntity$2,
            range * range
         );
         if (var11 == null) {
            return null;
         } else if (ignoreWalls) {
            return var11;
         } else {
            Vec3d var12 = var11.getEntity().getBoundingBox().raycast(var8, var10).orElse(var11.getEntity().getPos());
            BlockHitResult var13 = mc.world != null
               ? mc.world.raycast(new RaycastContext(var8, var12, ShapeType.COLLIDER, FluidHandling.ANY, mc.player))
               : null;
            return computeFlag(var8, var12, var13) ? null : var11;
         }
      }
   }

   public RaytraceUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean lambda$raytraceEntity$2(Predicate filter, Entity e) {
      return e.canHit() && e.isAlive() && !e.isSpectator() && filter.test(e);
   }

   public static boolean lambda$raytraceEntity$1(Entity entity) {
      return true;
   }

   public static boolean lambda$raytraceEntity$0(Entity entity) {
      return true;
   }
}
