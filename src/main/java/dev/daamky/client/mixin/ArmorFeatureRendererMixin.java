package dev.daamky.client.mixin;

import dev.daamky.client.module.render.CustomGlint;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ArmorFeatureRenderer.class})
public class ArmorFeatureRendererMixin {
   @Inject(
      method = {"render*"},
      at = {@At("HEAD")},
      require = 0
   )
   private void onPreRenderArmor(CallbackInfo ci) {
      CustomGlint.flag = true;
   }

   @Inject(
      method = {"render*"},
      at = {@At("RETURN")},
      require = 0
   )
   private void onPostRenderArmor(CallbackInfo ci) {
      CustomGlint.flag = false;
   }
}
