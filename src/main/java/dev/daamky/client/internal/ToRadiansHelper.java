package dev.daamky.client.internal;

import dev.daamky.client.event.InputTransformEvent;
import dev.daamky.client.util.MinecraftAccess;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class ToRadiansHelper implements MinecraftAccess {
   public static boolean getMc() {
      if (mc.player == null) {
         return false;
      } else if (mc.options == null
         || !mc.options.forwardKey.isPressed()
            && !mc.options.backKey.isPressed()
            && !mc.options.leftKey.isPressed()
            && !mc.options.rightKey.isPressed()) {
         return mc.player.input == null ? false : mc.player.input.movementForward != 0.0F || mc.player.input.movementSideways != 0.0F;
      } else {
         return true;
      }
   }

   public static boolean getMc2() {
      return getMc();
   }

   public static double[] computeRadians(double distance) {
      float var2 = mc.player.input.movementForward;
      float var3 = mc.player.input.movementSideways;
      float var4 = mc.player.getYaw();
      if (var2 != 0.0F) {
         if (var3 > 0.0F) {
            var4 += var2 > 0.0F ? -45.0F : 45.0F;
         } else if (var3 < 0.0F) {
            var4 += var2 > 0.0F ? 45.0F : -45.0F;
         }

         var3 = 0.0F;
         var2 = var2 > 0.0F ? 1.0F : -1.0F;
      }

      double var5 = Math.sin(Math.toRadians((double)(var4 + 90.0F)));
      double var7 = Math.cos(Math.toRadians((double)(var4 + 90.0F)));
      double var9 = (double)var2 * distance * var7 + (double)var3 * distance * var5;
      double var11 = (double)var2 * distance * var5 - (double)var3 * distance * var7;
      return new double[]{var9, var11};
   }

   public static double computeSqrt(Entity entity) {
      double var1 = entity.getX() - entity.prevX;
      double var3 = entity.getY() - entity.prevY;
      double var5 = entity.getZ() - entity.prevZ;
      return Math.sqrt(var1 * var1 + var5 * var5 + var3 * var3);
   }

   public static void handleNon(double velocity) {
      double[] var2 = computeRadians(velocity);
      Objects.requireNonNull(mc.player).setVelocity(var2[0], mc.player.getVelocity().getY(), var2[1]);
   }

   public static void handleNon(double velocity, double y) {
      double[] var4 = computeRadians(velocity);
      Objects.requireNonNull(mc.player).setVelocity(var4[0], y, var4[1]);
   }

   public static double computeAtan(Vec3d positionRelativeToPlayer, float yaw) {
      float var2 = (float)Math.atan2(-positionRelativeToPlayer.x, positionRelativeToPlayer.z);
      double var3 = Math.toRadians((double)MathHelper.wrapDegrees(yaw));
      return Math.toDegrees(MathHelper.wrapDegrees((double)var2 - var3));
   }

   public static PlayerInput initComp(PlayerInput input, double dgs, float deadAngle) {
      boolean var4 = input.forward();
      boolean var5 = input.backward();
      boolean var6 = input.left();
      boolean var7 = input.right();
      if (dgs >= (double)(-90.0F + deadAngle) && dgs <= (double)(90.0F - deadAngle)) {
         var4 = true;
      } else if (dgs < (double)(-90.0F - deadAngle) || dgs > (double)(90.0F + deadAngle)) {
         var5 = true;
      }

      if (dgs >= (double)(0.0F + deadAngle) && dgs <= (double)(180.0F - deadAngle)) {
         var7 = true;
      } else if (dgs >= (double)(-180.0F + deadAngle) && dgs <= (double)(0.0F - deadAngle)) {
         var6 = true;
      }

      return new PlayerInput(var4, var5, var6, var7, input.jump(), input.sneak(), input.sprint());
   }

   public static void handleDegrees(InputTransformEvent event, float targetYaw) {
      float var2 = event.getForward();
      float var3 = event.getStrafe();
      if ((var2 != 0.0F || var3 != 0.0F) && mc.player != null) {
         float var4 = mc.player.getYaw();
         double var5 = MathHelper.wrapDegrees(Math.toDegrees(computeRadians(targetYaw, var2, var3)));
         float var7 = 0.0F;
         float var8 = 0.0F;
         float var9 = Float.MAX_VALUE;

         for (float var10 = -1.0F; var10 <= 1.0F; var10++) {
            for (float var11 = -1.0F; var11 <= 1.0F; var11++) {
               if (var10 != 0.0F || var11 != 0.0F) {
                  double var12 = MathHelper.wrapDegrees(Math.toDegrees(computeRadians(var4, var10, var11)));
                  float var14 = Math.abs(MathHelper.wrapDegrees((float)(var5 - var12)));
                  if (var14 < var9) {
                     var9 = var14;
                     var7 = var10;
                     var8 = var11;
                  }
               }
            }
         }

         event.setForward(var7);
         event.setStrafe(var8);
      }
   }

   public static void handleForward(InputTransformEvent event) {
      float var1 = event.getForward();
      float var2 = event.getStrafe();
      if ((var1 != 0.0F || var2 != 0.0F) && mc.player != null) {
         float var3 = CameraManager.getFlag() ? CameraManager.getFloat() : mc.player.getYaw();
         handleDegrees(event, var3);
      }
   }

   public static double computeRadians(float rotationYaw, float moveForward, float moveStrafing) {
      float var6 = moveForward < 0.0F ? rotationYaw + 180.0F : rotationYaw;
      float var3;
      if (moveStrafing > 0.0F) {
         float var5;
         if (moveForward < 0.0F) {
            var5 = -0.5F;
         } else {
            var5 = moveForward > 0.0F ? 0.5F : 1.0F;
         }

         var3 = -90.0F * var5;
      } else if (moveStrafing < 0.0F) {
         float var4;
         if (moveForward < 0.0F) {
            var4 = -0.5F;
         } else {
            var4 = moveForward > 0.0F ? 0.5F : 1.0F;
         }

         var3 = 90.0F * var4;
      } else {
         var3 = 0.0F;
      }

      return Math.toRadians((double)(var6 + var3));
   }

   public static PlayerInput computeClass_10185(PlayerInput input, double dgs) {
      return initComp(input, dgs, 20.0F);
   }

   public ToRadiansHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
