package dev.daamky.client.legacy.setting;

import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MotionConfig4 extends LegacySetting {
   public MotionConfig4() {
      super("Instant");
   }

   @Override
   public Rotation initNext(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d, Entity entity) {
      return targetRotation != null ? targetRotation : currentRotation;
   }
}
