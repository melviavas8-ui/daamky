package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.daamky.client.module.render.Ambience;
import dev.daamky.client.module.render.Removals;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BackgroundRenderer.class})
public class BackgroundRendererMixin {
   @Inject(
      method = {"getFogModifier"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      if (Removals.getInstance().checkEnabled7()) {
         info.setReturnValue(null);
      }
   }

   @ModifyReturnValue(
      method = {"applyFog"},
      at = {@At("RETURN")}
   )
   private static Fog computeClass_9958(
      Fog original, @Local(argsOnly = true) Camera camera, @Local(argsOnly = true,ordinal = 0) float viewDistance
   ) {
      return Ambience.getInstance().initFog(camera, viewDistance, original);
   }
}
