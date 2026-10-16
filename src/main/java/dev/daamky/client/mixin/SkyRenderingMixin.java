package dev.daamky.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.internal.GonedlcSkyboxHelper;
import dev.daamky.client.internal.ModuleRegistry;
import dev.daamky.client.module.render.Ambience;
import net.minecraft.client.render.SkyRendering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SkyRendering.class})
public class SkyRenderingMixin {
   @Inject(
      method = {"renderSky(FFF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderCustomSkybox(float red, float green, float blue, CallbackInfo info) {
      Ambience var5 = ModuleRegistry.getInstance().computeNext(Ambience.class);
      if (var5 != null && var5.isEnabled() && !var5.polarshine.getValue().equals("Off")) {
         float var6 = (float)(System.currentTimeMillis() % 100000000L) / 1000.0F;
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         GonedlcSkyboxHelper.computeColor(var5.getEquals(), var5.getColor(), var6, 1.0F);
         RenderSystem.depthMask(true);
         info.cancel();
      }
   }

   @Inject(
      method = {"close()V"},
      at = {@At("HEAD")}
   )
   private void closeCustomSkybox(CallbackInfo info) {
      GonedlcSkyboxHelper.closeClear();
   }
}
