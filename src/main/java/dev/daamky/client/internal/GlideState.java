package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;

public class GlideState {
   public final DelayHelper delayHelper = new DelayHelper();
   public int speed;
   public double doubleVal = 1.0;
   public boolean forward;
   public Easing easing;

   public boolean computeForward(boolean forward) {
      return this.delayHelper.computeCurrent((long)this.speed) && (forwardx ? this.forward : !this.forward);
   }

   public boolean checkForward() {
      return this.delayHelper.computeCurrent((long)this.speed) && this.forward;
   }

   public GlideState withForward(boolean forward) {
      if (this.forward != forwardx) {
         this.forward = forwardx;
         this.delayHelper.setMillis((long)((double)System.currentTimeMillis() - (this.doubleVal - Math.min(this.doubleVal, (double)this.delayHelper.getCurrent()))));
      }

      return this;
   }

   public GlideState getCurrent() {
      this.delayHelper.setMillis(System.currentTimeMillis() - (long)this.speed);
      return this;
   }

   public GlideState withEasing(Easing easing) {
      this.easing = easingx;
      return this;
   }

   public GlideState withSpeed(int speed) {
      this.speed = speedx;
      return this;
   }

   public GlideState withSize(float size) {
      this.doubleVal = (double)size;
      return this;
   }

   public float getSpeed() {
      if (this.forward) {
         return this.delayHelper.computeCurrent((long)this.speed)
            ? (float)this.doubleVal
            : (float)((double)this.delayHelper.getCurrent() / (double)this.speed * this.doubleVal);
      } else {
         return this.delayHelper.computeCurrent((long)this.speed)
            ? 0.0F
            : (float)((1.0 - (double)this.delayHelper.getCurrent() / (double)this.speed) * this.doubleVal);
      }
   }

   public float applySpeed() {
      if (this.forward) {
         return this.delayHelper.computeCurrent((long)this.speed)
            ? (float)this.doubleVal
            : (float)(this.easing.apply((double)this.delayHelper.getCurrent() / (double)this.speed) * this.doubleVal);
      } else {
         return this.delayHelper.computeCurrent((long)this.speed)
            ? 0.0F
            : (float)((1.0 - this.easing.apply((double)this.delayHelper.getCurrent() / (double)this.speed)) * this.doubleVal);
      }
   }

   public float getFloat() {
      return 1.0F - this.applySpeed();
   }

   public void run() {
      this.delayHelper.runCurrent();
   }

   public void runForward() {
      if (this.checkForward()) {
         this.withForward(false);
      } else if (this.computeForward(false)) {
         this.withForward(true);
      }
   }

   public DelayHelper getDelayHelper() {
      return this.delayHelper;
   }

   public double getDouble() {
      return this.doubleVal;
   }

   public Easing getEasing() {
      return this.easing;
   }

   public boolean isForward() {
      return this.forward;
   }
}
