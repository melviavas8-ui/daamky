package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;

public class AnimatedValue {
   public long start = 0L;
   public long duration = 0L;
   public double invDurationNanos = 0.0;
   public double fromValue = 0.0;
   public double toValue = 0.0;
   public double value = 0.0;
   public Easing easing = Easing.LINEAR;

   public AnimatedValue animateTo(float valueTo, long duration, Easing easing) {
      return this.animateTo((double)valueTo, duration, easing, false);
   }

   public AnimatedValue animateTo(double valueTo, long duration, Easing easing) {
      return this.animateTo(valueTo, duration, easing, false);
   }

   public AnimatedValue animateTo(double valueTo, long duration, Easing easing, boolean safe) {
      if (!(Math.abs(this.toValue - valueTo) < 1.0E-5) || this.start <= 0L && !(Math.abs(this.value - valueTo) < 1.0E-5)) {
         if (!this.computeAbs(safe, valueTo)) {
            this.easing = easing;
            this.duration = Math.max(1L, duration);
            this.invDurationNanos = 1.0 / ((double)this.duration * 1000000.0);
            this.start = System.nanoTime();
            this.fromValue = this.value;
            this.toValue = valueTo;
         }

         return this;
      } else {
         return this;
      }
   }

   public boolean update() {
      if (this.start == 0L) {
         return false;
      } else {
         long var1 = System.nanoTime() - this.start;
         double var3 = (double)var1 * this.invDurationNanos;
         if (var3 >= 1.0) {
            this.start = 0L;
            this.value = this.toValue;
            this.fromValue = this.toValue;
            return false;
         } else {
            double var5 = this.easing.apply(var3);
            this.value = this.fromValue + (this.toValue - this.fromValue) * var5;
            return true;
         }
      }
   }

   public boolean checkStart() {
      return this.start == 0L ? false : (double)(System.nanoTime() - this.start) * this.invDurationNanos < 1.0;
   }

   public boolean check() {
      return !this.checkStart();
   }

   public boolean computeAbs(boolean safe, double valueTo) {
      return safe
         && (this.checkStart() || Math.abs(this.value - valueTo) < 1.0E-5)
         && (Math.abs(valueTo - this.fromValue) < 1.0E-5 || Math.abs(valueTo - this.toValue) < 1.0E-5 || Math.abs(valueTo - this.value) < 1.0E-5);
   }

   public void handleDuration(long duration) {
      this.duration = Math.max(1L, duration);
      this.invDurationNanos = 1.0 / ((double)this.duration * 1000000.0);
   }

   public double getFromValue() {
      return this.fromValue;
   }

   public double getToValue() {
      return this.toValue;
   }

   public void handleStart(double value) {
      this.value = value;
      this.fromValue = value;
      this.toValue = value;
      this.start = 0L;
   }

   public double getValue() {
      return this.value;
   }

   public void setEasing(Easing easing) {
      this.easing = easing;
   }
}
