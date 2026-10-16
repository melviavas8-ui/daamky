package dev.daamky.client.mixin;

import dev.daamky.client.event.KeyEvent;
import dev.daamky.client.internal.EnabledSet;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Keyboard.class})
public class KeyboardMixin {
   @Inject(
      method = {"onKey"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/InactivityFpsLimiter;onInput()V"
      )}
   )
   public void keyPressHook(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      if (PlatformUtils.getClass_746() != null) {
         if (action == 1 && key == 80 && (modifiers & 1) != 0 && EnabledSet.getInstance().check()) {
            EnabledSet.getInstance().runNext();
         } else {
            KeyEvent.getInstance().postAndCheckCancelled(new KeyEvent.KeyEventData(key, action, false));
         }
      }
   }
}
