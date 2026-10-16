package dev.daamky.client.internal;

import net.minecraft.client.MinecraftClient;

public class StaffHudHelper {
   public long timeStamp = System.nanoTime();
   public int intVal;
   public final boolean useMCFrameRate;
   public int fps = 0;
   public long long2 = 0L;

   public StaffHudHelper(boolean useMCFrameRate) {
      this.useMCFrameRate = useMCFrameRatex;
      this.intVal = 0;
   }

   public void handleFps(int fps, RunTask... calls) {
      if (this.fps != fpsx) {
         this.longVal2 = 1000000000L / (long)fpsx;
         this.fps = fpsx;
      }

      long var3;
      long var5;
      if ((var5 = (var3 = System.nanoTime()) - this.longVal) > 50000000L) {
         var5 = 50000000L;
         this.longVal = var3 - var5;
      }

      this.intVal = this.intVal + (int)(var5 / this.longVal2);
      this.longVal = this.longVal + (long)this.intVal * this.longVal2;

      for (this.intVal = Math.min(this.intVal, this.useMCFrameRate ? Math.min(this.fps, MinecraftClient.getInstance().getCurrentFps()) : this.fps); this.intVal > 0; this.intVal--) {
         for (RunTask var10 : calls) {
            var10.run();
         }
      }
   }
}
