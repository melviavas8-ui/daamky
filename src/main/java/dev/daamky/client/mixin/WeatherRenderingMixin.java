package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.daamky.client.module.misc.Optimization;
import dev.daamky.client.module.render.Ambience;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.world.biome.Biome.Precipitation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WeatherRendering.class})
public abstract class WeatherRenderingMixin {
   @ModifyExpressionValue(
      method = {"addParticlesAndSound"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"
      )}
   )
   private float computeEnabled(float original) {
      if (Optimization.getInstance().checkEnabled3()) {
         return 0.0F;
      } else {
         Ambience var2 = Ambience.getInstance();
         return var2.isEnabled() && var2.weather.is(Ambience.Weather.SNOWY) ? 0.0F : original;
      }
   }

   @ModifyReturnValue(
      method = {"getPrecipitationAt"},
      at = {@At(
         value = "RETURN",
         ordinal = 1
      )}
   )
   private Precipitation computeEnabled(Precipitation original) {
      if (Optimization.getInstance().checkEnabled3()) {
         return Precipitation.NONE;
      } else {
         Ambience var2 = Ambience.getInstance();
         return var2.isEnabled() && var2.weather.is(Ambience.Weather.SNOWY) ? Precipitation.SNOW : original;
      }
   }

   @Inject(
      method = {"renderPrecipitation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderPrecipitationWorld(CallbackInfo ci) {
      if (Optimization.getInstance().checkEnabled3()) {
         ci.cancel();
      }
   }
}
