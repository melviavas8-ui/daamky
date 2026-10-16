package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;

public class BaseTimeHelper implements MinecraftAccess {
   public long delay = 0L;
   public long timeStamp = System.currentTimeMillis();

   public boolean checkCurrent() {
      float var1 = mc.player != null ? mc.player.getAttackCooldownProgress(0.5F) : 1.0F;
      long var2 = System.currentTimeMillis();
      long var4 = var2 - this.longVal;
      return var4 >= this.delay && var1 > 0.9F;
   }

   public boolean computeFlag(int ticks) {
      return this.getCurrent() >= (long)ticks * 50L;
   }

   public long getCurrent() {
      return System.currentTimeMillis() - this.longVal;
   }

   public void setDelay(long delay) {
      this.longVal = System.currentTimeMillis();
      this.delay = delayx;
   }

   public boolean check() {
      return this.computeDelay(3);
   }

   public boolean computeDelay(int tick) {
      long var4 = this.getCurrent();
      long var2;
      return var4 >= this.delay - (var2 = (long)tick * 50L) && var4 < this.delay + var2;
   }

   public long getDelay() {
      return this.delay - this.getCurrent();
   }

   public float computeFloat(int baseTime) {
      return(floatVal)(mc.player.lastAttackedTicks + baseTime) / mc.player.getAttackCooldownProgressPerTick();
   }
}
