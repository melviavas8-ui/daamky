package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;

public class RotationHelper2 {
   public final LinearHelper linearHelper = new LinearHelper();
   public final LinearHelper linearHelper2 = new LinearHelper();

   public Rotation initInit(Rotation rotation, int yawSpeed, int pitchSpeed) {
      return new Rotation(this.linearHelper.initDestination(rotation.getYaw(), yawSpeed), this.linearHelper2.initDestination(rotation.getPitch(), pitchSpeed));
   }

   public float getFloat() {
      return this.linearHelper.getDestination();
   }

   public float getFloat2() {
      return this.linearHelper2.getDestination();
   }

   public RotationHelper2 computeEasing(Easing easing) {
      this.linearHelper.withEasing(easing);
      this.linearHelper2.withEasing(easing);
      return this;
   }
}
