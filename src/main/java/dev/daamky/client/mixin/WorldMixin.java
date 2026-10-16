package dev.daamky.client.mixin;

import dev.daamky.client.internal.NamedChoice;
import dev.daamky.client.module.render.Ambience;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({World.class})
public class WorldMixin {
   @Inject(
      method = {"getRainGradient"},
      cancellable = true,
      at = {@At("HEAD")}
   )
   private void overrideWeather(float delta, CallbackInfoReturnable<Float> cir) {
      Ambience var3 = Ambience.getInstance();
      Ambience.Weather var4 = NamedChoice.getChoiceByName(var3.weather.getValue(), Ambience.Weather.values());
      if (var3.isEnabled()) {
         switch (var4) {
            case SUNNY:
               cir.setReturnValue(0.0F);
               break;
            case RAINY:
            case THUNDER:
               cir.setReturnValue(1.0F);
               break;
            case SNOWY:
               cir.setReturnValue(0.9F);
         }
      }
   }

   @Inject(
      method = {"getThunderGradient"},
      cancellable = true,
      at = {@At("HEAD")}
   )
   private void overrideThunder(float delta, CallbackInfoReturnable<Float> cir) {
      Ambience var3 = Ambience.getInstance();
      Ambience.Weather var4 = NamedChoice.getChoiceByName(var3.weather.getValue(), Ambience.Weather.values());
      if (var3.isEnabled()) {
         switch (var4) {
            case SUNNY:
            case RAINY:
            case SNOWY:
               cir.setReturnValue(0.0F);
               break;
            case THUNDER:
               cir.setReturnValue(1.0F);
         }
      }
   }
}
