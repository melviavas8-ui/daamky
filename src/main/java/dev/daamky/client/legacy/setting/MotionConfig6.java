package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MotionConfig6 extends LegacySetting {
   public MotionConfig6() {
      super("Smooth");
   }

   @Override
   public Rotation initNext(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d, Entity entity) {
      float var5 = MathHelper.wrapDegrees(targetRotation.getYaw() - currentRotation.getYaw());
      float var6 = targetRotation.getPitch() - currentRotation.getPitch();
      float var7 = 180.0F;
      float var8 = 90.0F;
      return new Rotation(
         currentRotation.getYaw() + MathHelper.clamp(var5, -var7, var7),
         MathHelper.clamp(currentRotation.getPitch() + MathHelper.clamp(var6, -var8, var8), -90.0F, 90.0F)
      );
   }
}
