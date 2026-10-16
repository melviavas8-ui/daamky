package dev.daamky.client.internal;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFWNativeWin32;

public class DwmSetWindowAttributeHelper implements MinecraftAccess {
   public static final DwmSetWindowAttributeHelper dwmSetWindowAttributeHelper = new DwmSetWindowAttributeHelper();
   public boolean flag = false;

   public void run() {
      this.initInit();
   }

   public void initInit() {
      if (!this.flag) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.getWindow() != null && var1.getWindow().getHandle() != 0L) {
            try {
               long var2 = GLFWNativeWin32.glfwGetWin32Window(var1.getWindow().getHandle());
               if (var2 != 0L) {
                  Pointer var4 = new Pointer(var2);
                  NativeLibrary var5 = NativeLibrary.getInstance("dwmapi");
                  Function var6 = var5.getFunction("DwmSetWindowAttribute");
                  IntByReference var7 = new IntByReference(1);
                  var6.invokeInt(new Object[]{var4, 20, var7, 4});
                  IntByReference var8 = new IntByReference(2960685);
                  var6.invokeInt(new Object[]{var4, 35, var8, 4});
                  IntByReference var9 = new IntByReference(16777215);
                  var6.invokeInt(new Object[]{var4, 36, var9, 4});
                  this.flag = true;
                  System.out.println("[GoneClient] Successfully customized window decoration colors.");
               }
            } catch (Throwable var10) {
               System.err.println("[GoneClient] Failed to apply Dwmapi colors: " + var10.getMessage());
               this.flag = true;
            }
         }
      }
   }

   public boolean check() {
      return this.flag;
   }

   public static DwmSetWindowAttributeHelper getInstance() {
      return dwmSetWindowAttributeHelper;
   }
}
