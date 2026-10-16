package dev.daamky.client.internal;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class FrameTimer {
   public static long timeStamp = 0L;
   public static int intVal = 60;
   public static long msiood = 0L;
   public static final long long2 = 1000000000L;

   public static void runGlfw() {
      long var0 = System.nanoTime();
      if (var0 - msiood >= 1000000000L) {
         msiood = var0;
         MinecraftClient var2 = MinecraftClient.getInstance();
         if (var2 != null && var2.getWindow() != null) {
            long var3 = var2.getWindow().getHandle();
            long var5 = GLFW.glfwGetWindowMonitor(var3);
            if (var5 == 0L) {
               var5 = computeGlfw(var3, var2.getWindow().getWidth(), var2.getWindow().getHeight());
            }

            if (var5 != longVal) {
               intVal = computeGlfw(var5);
               longVal = var5;
            }
         }
      }
   }

   public static int getInt() {
      return intVal;
   }

   public static long computeGlfw(long window, int windowWidth, int windowHeight) {
      int[] var4 = new int[1];
      int[] var5 = new int[1];
      GLFW.glfwGetWindowPos(window, var4, var5);
      int var6 = var4[0] + windowWidth / 2;
      int var7 = var5[0] + windowHeight / 2;
      long var8 = GLFW.glfwGetPrimaryMonitor();
      PointerBuffer var10 = GLFW.glfwGetMonitors();
      if (var10 != null) {
         for (int var11 = 0; var11 < var10.limit(); var11++) {
            long var12 = var10.get(var11);
            int[] var14 = new int[1];
            int[] var15 = new int[1];
            GLFW.glfwGetMonitorPos(var12, var14, var15);
            GLFWVidMode var16 = GLFW.glfwGetVideoMode(var12);
            if (var16 != null) {
               int var17 = var16.width();
               int var18 = var16.height();
               if (var6 >= var14[0] && var6 < var14[0] + var17 && var7 >= var15[0] && var7 < var15[0] + var18) {
                  var8 = var12;
                  break;
               }
            }
         }
      }

      return var8;
   }

   public static int computeGlfw(long monitor) {
      GLFWVidMode var2 = GLFW.glfwGetVideoMode(monitor);
      return var2 != null ? var2.refreshRate() : 60;
   }
}
