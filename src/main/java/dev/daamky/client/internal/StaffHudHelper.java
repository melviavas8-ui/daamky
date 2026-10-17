package dev.daamky.client.internal;

import net.minecraft.client.MinecraftClient;

public class StaffHudHelper {
   public long timeStamp = System.nanoTime();
   public int intVal;
   public final boolean useMCFrameRate;
   public int fps = 0;
   public long long2 = 0L;

   public StaffHudHelper(boolean useMCFrameRate) {
      this.useMCFrameRate = useMCFrameRate;
      this.intVal = 0;
   }

   public void handleFps(int fps, RunTask... calls) {
      if (this.fps != fps) {
         this.long2 = 1000000000L / (long)fps;
         this.fps = fps;
      }

      long var3;
      long var5;
      if ((var5 = (var3 = System.nanoTime()) - this.timeStamp) > 50000000L) {
         var5 = 50000000L;
         this.timeStamp = var3 - var5;
      }

      this.intVal = this.intVal + (int)(var5 / this.long2);
      this.timeStamp = this.timeStamp + (long)this.intVal * this.long2;

      for (this.intVal = Math.min(this.intVal, this.useMCFrameRate ? Math.min(this.fps, MinecraftClient.getInstance().getCurrentFps()) : this.fps); this.intVal > 0; this.intVal--) {
         for (RunTask var10 : calls) {
            var10.run();
         }
      }
   }
}
