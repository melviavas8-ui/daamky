package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;

public class LinearHelper {
   public float floatVal;
   public float destination;
   public Easing easing = Easing.LINEAR;
   public GlideState glideState = new GlideState().withSize(0.0F).withSpeed(0).withForward(false).withEasing(this.easing);

   public float initDestination(float destination, int ms) {
      ms = Math.max(1, ms);
      this.floatVal = this.destination - this.glideState.applySpeed();
      this.destination = destinationx;
      if (this.floatVal != this.destination - destinationx) {
         this.glideState = new GlideState().withSize(this.destination - this.floatVal).withSpeed(ms).withForward(false).withEasing(this.easing);
      }

      return this.floatVal;
   }

   public boolean checkDestination() {
      return this.floatVal == this.destination || this.glideState.checkForward() || this.glideState.computeForward(false);
   }

   public float getDestination() {
      this.floatVal = this.destination - this.glideState.applySpeed();
      return this.floatVal;
   }

   public LinearHelper withEasing(Easing easing) {
      this.easing = easingx;
      return this;
   }

   public GlideState getGlideState() {
      return this.glideState;
   }
}
