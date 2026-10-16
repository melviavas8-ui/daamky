package dev.daamky.client.internal;

public class DelayHelper {
   public long millis2;

   public DelayHelper() {
      this.runCurrent();
   }

   public boolean computeCurrent(float delay) {
      return System.currentTimeMillis() - this.millis2 >= (long)delay;
   }

   public boolean computeCurrent(long delay) {
      return System.currentTimeMillis() - this.millis2 >= delay;
   }

   public void runCurrent() {
      this.millis2 = System.currentTimeMillis();
   }

   public long getCurrent() {
      return System.currentTimeMillis() - this.millis2;
   }

   public long getMillis2() {
      return this.millis2;
   }

   public void setMillis(long millis2) {
      this.millis2 = millis2x;
   }
}
