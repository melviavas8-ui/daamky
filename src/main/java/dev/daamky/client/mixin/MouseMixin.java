package dev.daamky.client.mixin;

import dev.daamky.client.event.KeyEvent;
import dev.daamky.client.event.MouseDeltaEvent;
import dev.daamky.client.gui.PVEScreen;
import dev.daamky.client.internal.AutoHelper;
import dev.daamky.client.internal.DraggableManagerSaveHelper;
import dev.daamky.client.internal.GpsHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.module.render.Zoom;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Mouse.class})
public class MouseMixin {
   @Shadow
   @Final
   private MinecraftClient field_1779;

   @Inject(
      method = {"lockCursor"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"
      )},
      cancellable = true
   )
   private void lockCursorHook(CallbackInfo ci) {
      if (MinecraftClient.getInstance().currentScreen instanceof PVEScreen) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"onMouseButton"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/InactivityFpsLimiter;onInput()V"
      )}
   )
   public void mousePressHook(long window, int button, int action, int mods, CallbackInfo ci) {
      if (PlatformUtils.getClass_746() != null) {
         KeyEvent.getInstance().postAndCheckCancelled(new KeyEvent.KeyEventData(button, action, true));
         if (this.field_1779.currentScreen instanceof ChatScreen && action == 1) {
            float var7 = (float)(this.field_1779.mouse.getX() / this.field_1779.getWindow().getScaleFactor());
            float var8 = (float)(this.field_1779.mouse.getY() / this.field_1779.getWindow().getScaleFactor());
            if (button == 0) {
               if (GpsHelper.getInstance().saveActive(var7, var8)) {
                  return;
               }
            } else if (button == 1) {
               for (HudElement var10 : GpsHelper.getInstance().getList()) {
                  if (var10.checkGps() && var10.isVisible() && var10.getAutoHelper().checkVisual()) {
                     GpsHelper.getInstance().handleMax(var10, var7, var8);
                     return;
                  }
               }

               GpsHelper.getInstance().setActiveConfigWidget(null);
            }
         }

         DraggableManagerSaveHelper.getInstance().getDraggables().forEach((p0, p1) -> MouseMixin.lambda$mousePressHook$0(action, button, p0, p1));
      }
   }

   @Inject(
      method = {"onMouseScroll"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onMouseScrollHook(long window, double horizontal, double vertical, CallbackInfo ci) {
      if (PlatformUtils.getClass_746() != null) {
         if (Zoom.getInstance().isEnabled() && MinecraftClient.getInstance().currentScreen == null) {
            Zoom.handleLevel(vertical);
            ci.cancel();
         }
      }
   }

   @Redirect(
      method = {"updateMouse"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
      )
   )
   private void redirectChangeLookDirection(ClientPlayerEntity player, double yaw, double pitch) {
      MouseDeltaEvent var6 = MouseDeltaEvent.getInstance();
      var6.handleYaw(yaw, pitch);
      var6.postAndCheckCancelled();
      if (!var6.isCancelled()) {
         player.changeLookDirection(var6.getYaw(), var6.getPitch());
      }
   }

   private static void lambda$mousePressHook$0(int action, int button, String s, AutoHelper draggable) {
      if (draggable.getFeature().isEnabled()) {
         if (action == 0) {
            draggable.saveDragging(button);
         } else if (action == 1) {
            draggable.handleNext(button);
         }
      }
   }
}
