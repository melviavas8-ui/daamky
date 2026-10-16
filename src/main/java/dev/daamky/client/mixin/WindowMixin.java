package dev.daamky.client.mixin;

import dev.daamky.client.event.FramebufferResizeEvent;
import dev.daamky.client.event.WindowResizeEvent;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Window.class})
public class WindowMixin {
   @Shadow
   @Final
   private long field_5187;

   @Inject(
      method = {"onWindowSizeChanged"},
      at = {@At("RETURN")}
   )
   public void windowResizeHook(long window, int width, int height, CallbackInfo ci) {
      try {
         FramebufferResizeEvent.getInstance().postAndCheckCancelled();
      } catch (Throwable var7) {
      }
   }

   @Inject(
      method = {"onFramebufferSizeChanged"},
      at = {@At("RETURN")}
   )
   public void framebufferResizeHook(long window, int width, int height, CallbackInfo callbackInfo) {
      if (window == this.field_5187) {
         try {
            WindowResizeEvent.getInstance().postAndCheckCancelled();
         } catch (Throwable var7) {
         }
      }
   }
}
