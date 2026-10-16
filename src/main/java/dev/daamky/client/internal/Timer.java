package dev.daamky.client.internal;

public class Timer {
   public long lastMS = -1L;

   public Timer() {
      this.lastMS = System.currentTimeMillis();
   }

   public boolean computeCurrent(double delay) {
      return(doubleVal)(System.currentTimeMillis() - this.lastMS) >= delay;
   }

   public boolean computeFlag(boolean active, double delay) {
      return active || this.computeCurrent(delay);
   }

   public long getLastMS() {
      return this.lastMS;
   }

   public void runCurrent() {
      this.lastMS = System.currentTimeMillis();
   }

   public long getCurrent() {
      return System.currentTimeMillis() - this.lastMS;
   }

   public long getNano() {
      return System.nanoTime() / 1000000L;
   }

   public void setLastMS(long time) {
      this.lastMS = time;
   }
}
