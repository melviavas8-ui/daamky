package dev.daamky.client.internal;

import net.minecraft.util.math.Vec2f;

public class DeltaHelper {
   public final float deltaYaw;
   public final float deltaPitch;

   public DeltaHelper(float deltaYaw, float deltaPitch) {
      this.deltaYaw = deltaYaw;
      this.deltaPitch = deltaPitch;
   }

   public float sqrt() {
      return (float)Math.sqrt((double)(this.deltaYaw * this.deltaYaw + this.deltaPitch * this.deltaPitch));
   }

   public float getDeltaYaw() {
      return this.deltaYaw;
   }

   public float getDeltaPitch() {
      return this.deltaPitch;
   }

   public Vec2f initDelta() {
      return new Vec2f(this.deltaYaw, this.deltaPitch);
   }

   public boolean computeFlag(float delta) {
      return this.computeAbs(delta, delta);
   }

   public boolean computeAbs(float maxDeltaYaw, float maxDeltaPitch) {
      return Math.abs(this.deltaYaw) < maxDeltaYaw && Math.abs(this.deltaPitch) < maxDeltaPitch;
   }
}
