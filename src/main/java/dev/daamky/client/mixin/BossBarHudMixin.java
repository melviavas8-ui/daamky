package dev.daamky.client.mixin;

import dev.daamky.client.module.render.Removals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BossBarHud.class})
public class BossBarHudMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRender(CallbackInfo ci) {
      if (MinecraftClient.getInstance().player != null) {
         if (Removals.getInstance().checkEnabled8()) {
            ci.cancel();
         }
      }
   }
}
