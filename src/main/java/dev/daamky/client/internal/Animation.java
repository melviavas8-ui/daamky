package dev.daamky.client.internal;

import dev.daamky.client.model.enums.AnimationDirection;

public abstract class Animation {
   public Timer timerUtil = new Timer();
   public int duration;
   public double endPoint;
   public AnimationDirection direction;

   public Animation(int ms, double endPoint) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = AnimationDirection.FORWARDS;
   }

   public Animation(int ms, double endPoint, AnimationDirection direction) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = direction;
   }

   public boolean computeEquals(AnimationDirection direction) {
      return this.checkDuration() && this.direction.equals(direction);
   }

   public double getDuration() {
      return 1.0 - (double)this.timerUtil.getCurrent() / (double)this.duration * this.endPoint;
   }

   public double getEndPoint() {
      return this.endPoint;
   }

   public void setEndPoint(double endPoint) {
      this.endPoint = endPoint;
   }

   public void getTimerUtil() {
      this.timerUtil.runCurrent();
   }

   public boolean checkDuration() {
      return this.timerUtil.computeCurrent((double)this.duration);
   }

   public void getDirection() {
      this.setDirection(this.direction.opposite());
   }

   public void setDirection(AnimationDirection direction) {
      if (this.direction != direction) {
         this.direction = direction;
         this.timerUtil.setLastMS(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getCurrent())));
      }
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public boolean check() {
      return false;
   }

   public float getEnd() {
      if (this.direction == AnimationDirection.FORWARDS) {
         return this.checkDuration() ? (float)this.endPoint : (float)(this.computeDuration((double)this.timerUtil.getCurrent()) * this.endPoint);
      } else if (this.checkDuration()) {
         return 0.0F;
      } else if (this.check()) {
         double var1 = (double)Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getCurrent()));
         return(floatVal)(this.computeDuration(var1) * this.endPoint);
      } else {
         return(floatVal)((1.0 - this.computeDuration((double)this.timerUtil.getCurrent())) * this.endPoint);
      }
   }

   public abstract double computeDuration(double var1);
}
