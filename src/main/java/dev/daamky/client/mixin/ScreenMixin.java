package dev.daamky.client.mixin;

import dev.daamky.client.module.render.Interface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
public abstract class ScreenMixin {
   @Shadow
   @Nullable
   protected MinecraftClient field_22787;

   @Inject(
      method = {"renderBackground"},
      at = {@At("HEAD")},
      cancellable = true,
      require = 0
   )
   private void onRenderBackground4(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (this.field_22787 != null && this.field_22787.world != null) {
         Interface var6 = Interface.getInstance();
         if (var6.isEnabled() && Interface.check()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
   }
}
