package dev.daamky.client.internal;


public class ValueToHelper {
   public long start;
   public double duration;
   public double fromValue;
   public double toValue;
   public double value;
   public double prevValue;
   public ApplyFunction2 easing = ValueHelper.applyFunction2;
   public boolean debug = false;
   public Runnable finishAction;

   public ValueToHelper computeValueToHelper(double valueTo, double duration) {
      return this.computeDebug(valueTo, duration, ValueHelper.applyFunction2, false);
   }

   public ValueToHelper computeValueToHelper(double valueTo, double duration, ApplyFunction2 easing) {
      return this.computeDebug(valueTo, duration, easing, false);
   }

   public ValueToHelper computeValueToHelper(double valueTo, double duration, boolean safe) {
      return this.computeDebug(valueTo, duration, ValueHelper.applyFunction2, safe);
   }

   public ValueToHelper computeDebug(double valueTo, double duration, ApplyFunction2 easing, boolean safe) {
      if (this.computeFlag(safe, valueTo)) {
         if (this.isDebug()) {
            System.out.println("Animate cancelled due to target val equals from val");
         }
      } else {
         this.withEasing(easing).withDuration(duration * 1000.0).withStart(System.currentTimeMillis()).withFromValue(this.getValue()).withToValue(valueTo);
         if (this.isDebug()) {
            System.out
               .println(
                  "#animate {\n    to value: " + this.getToValue() + "\n    from value: " + this.getValue() + "\n    duration: " + this.getDuration() + "\n}"
               );
         }
      }

      return this;
   }

   public boolean check() {
      return !this.check2();
   }

   public boolean check2() {
      return this.getCurrent() >= 1.0;
   }

   public double getCurrent() {
      return(doubleVal)(System.currentTimeMillis() - this.getStart()) / this.getDuration();
   }

   public boolean computeFlag(boolean safe, double valueTo) {
      return safe && this.check() && (valueTo == this.getFromValue() || valueTo == this.getToValue() || valueTo == this.getValue());
   }

   public double computeDouble(double start, double end, double pct) {
      return start + (end - start) * pct;
   }

   public ValueToHelper withStart(long start) {
      this.start = start;
      return this;
   }

   public ValueToHelper withDuration(double duration) {
      this.duration = duration;
      return this;
   }

   public ValueToHelper withFromValue(double fromValue) {
      this.fromValue = fromValue;
      return this;
   }

   public ValueToHelper withToValue(double toValue) {
      this.toValue = toValue;
      return this;
   }

   public ValueToHelper withValue(double value) {
      this.value = value;
      return this;
   }

   public ValueToHelper withPrevValue(double prevValue) {
      this.prevValue = prevValue;
      return this;
   }

   public ValueToHelper withEasing(ApplyFunction2 easing) {
      this.easing = easing;
      return this;
   }

   public ValueToHelper withDebug(boolean debug) {
      this.debug = debug;
      return this;
   }

   public ValueToHelper withFinishAction(Runnable action) {
      this.finishAction = action;
      return this;
   }

   public void handleAction(double value) {
      this.computeValueToHelper(value, 0.0);
      this.getFinishAction();
      this.withValue(value);
   }

      public long getStart() {
      return this.start;
   }

      public double getDuration() {
      return this.duration;
   }

      public double getFromValue() {
      return this.fromValue;
   }

      public double getToValue() {
      return this.toValue;
   }

      public double getValue() {
      return this.value;
   }

      public double getPrevValue() {
      return this.prevValue;
   }

      public ApplyFunction2 getEasing() {
      return this.easing;
   }

      public boolean isDebug() {
      return this.debug;
   }

      public Runnable getFinishAction() {
      return this.finishAction;
   }
}
