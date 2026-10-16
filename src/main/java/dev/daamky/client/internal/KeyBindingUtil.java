package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.option.KeyBinding;

public final class KeyBindingUtil implements MinecraftAccess {
   public static KeyBinding[] getMc() {
      return mc != null && mc.options != null
         ? new KeyBinding[]{
            mc.options.sprintKey,
            mc.options.forwardKey,
            mc.options.backKey,
            mc.options.leftKey,
            mc.options.rightKey,
            mc.options.jumpKey
         }
         : new KeyBinding[0];
   }

   public static boolean check() {
      return mc != null && mc.options != null && mc.options.backKey != null && mc.options.backKey.isPressed();
   }

   public static boolean check2() {
      return mc != null && mc.options != null && mc.options.leftKey != null && mc.options.leftKey.isPressed();
   }

   public static boolean check3() {
      return mc != null && mc.options != null && mc.options.rightKey != null && mc.options.rightKey.isPressed();
   }

   public static boolean check4() {
      return mc != null && mc.player != null ? mc.player.forwardSpeed != 0.0F || mc.player.sidewaysSpeed != 0.0F : false;
   }

   public static double computeRadians(float rotationYaw, float moveForward, float moveStrafing) {
      if (moveForward < 0.0F) {
         rotationYaw += 180.0F;
      }

      float var3 = 1.0F;
      if (moveForward < 0.0F) {
         var3 = -0.5F;
      }

      if (moveForward > 0.0F) {
         var3 = 0.5F;
      }

      if (moveStrafing > 0.0F) {
         rotationYaw -= 90.0F * var3;
      }

      if (moveStrafing < 0.0F) {
         rotationYaw += 90.0F * var3;
      }

      return Math.toRadians((double)rotationYaw);
   }

   public static double[] computeRadians(double speed) {
      if (mc != null && mc.player != null && mc.player.input != null) {
         VecRotationHelper var2 = VecRotationHelper.getInstance();
         Rotation var3 = var2 != null ? var2.getCurrent() : null;
         FromRotationHelper var4 = var2 != null ? var2.getLast() : null;
         float var5 = mc.player.input.movementForward;
         float var6 = mc.player.input.movementSideways;
         float var7 = var4 != null && var3 != null ? var3.getYaw() : mc.player.getYaw();
         if (var5 != 0.0F) {
            if (var6 > 0.0F) {
               var7 += (float)(var5 > 0.0F ? -45 : 45);
            } else if (var6 < 0.0F) {
               var7 += (float)(var5 > 0.0F ? 45 : -45);
            }

            var6 = 0.0F;
            if (var5 > 0.0F) {
               var5 = 1.0F;
            } else if (var5 < 0.0F) {
               var5 = -1.0F;
            }
         }

         double var8 = Math.sin(Math.toRadians((double)(var7 + 90.0F)));
         double var10 = Math.cos(Math.toRadians((double)(var7 + 90.0F)));
         double var12 = (double)var5 * speed * var10 + (double)var6 * speed * var8;
         double var14 = (double)var5 * speed * var8 - (double)var6 * speed * var10;
         return new double[]{var12, var14};
      } else {
         return new double[]{0.0, 0.0};
      }
   }

   public static void handleDouble(double speed) {
      if (mc != null && mc.player != null) {
         double[] var2 = computeRadians(speed);
         mc.player.setVelocity(var2[0], mc.player.getVelocity().y, var2[1]);
      }
   }

   public KeyBindingUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
