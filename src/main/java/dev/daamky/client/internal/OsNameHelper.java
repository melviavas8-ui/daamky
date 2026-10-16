package dev.daamky.client.internal;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFWNativeWin32;

public class OsNameHelper {
   public static void initWindow() {
      try {
         if (System.getProperty("os.name").toLowerCase().contains("win")) {
            MinecraftClient var0 = MinecraftClient.getInstance();
            if (var0 == null || var0.getWindow() == null) {
               return;
            }

            long var1 = GLFWNativeWin32.glfwGetWin32Window(var0.getWindow().getHandle());
            Pointer var3 = new Pointer(var1);
            IntByReference var4 = new IntByReference(1);
            OsNameHelper.Dwmapi.INSTANCE.DwmSetWindowAttribute(var3, 20, var4, 4);
            OsNameHelper.Dwmapi.INSTANCE.DwmSetWindowAttribute(var3, 19, var4, 4);
         }
      } catch (Throwable var5) {
         System.err.println("Failed to set dark title bar: " + var5.getMessage());
      }
   }

   private interface Dwmapi extends StdCallLibrary {
      OsNameHelper.Dwmapi INSTANCE = (OsNameHelper.Dwmapi)Native.load("dwmapi", OsNameHelper.Dwmapi.class);

      int DwmSetWindowAttribute(Pointer var1, int var2, IntByReference var3, int var4);
   }
}
