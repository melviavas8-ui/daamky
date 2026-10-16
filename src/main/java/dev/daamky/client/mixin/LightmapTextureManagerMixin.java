package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.daamky.client.module.render.Gamma;
import dev.daamky.client.module.render.NightVision;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LightmapTextureManager.class})
public class LightmapTextureManagerMixin {
   @Shadow
   private boolean field_4135;
   @Unique
   private boolean daamky$prevActive;

   @Inject(
      method = {"update"},
      at = {@At("HEAD")}
   )
   private void forceNightVisionRefresh(float delta, CallbackInfo ci) {
      boolean var3 = NightVision.getInstance().isEnabled() || Gamma.getInstance().isEnabled() && Gamma.getInstance().getEnabled3() > 0.0F;
      if (var3 || this.daamky$prevActive) {
         this.field_4135 = true;
      }

      this.daamky$prevActive = var3;
   }

   @Inject(
      method = {"update"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gl/SimpleFramebuffer;beginWrite(Z)V"
      )}
   )
   private void applyNightVisionUniforms(float delta, CallbackInfo ci, @Local ShaderProgram shaderProgram) {
      if (NightVision.getInstance().isEnabled()) {
         float var4 = NightVision.getInstance().getEnabled2();
         shaderProgram.getUniformOrDefault("NightVisionFactor").set(var4);
      }

      if (Gamma.getInstance().isEnabled()) {
         float var5 = Gamma.getInstance().getEnabled3();
         if (var5 > 0.0F) {
            shaderProgram.getUniformOrDefault("BrightnessFactor").set(1.0F + var5);
         }
      }
   }
}
