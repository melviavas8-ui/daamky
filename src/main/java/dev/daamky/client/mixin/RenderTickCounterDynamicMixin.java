package dev.daamky.client.mixin;

import dev.daamky.client.internal.FloatValueHelper;
import net.minecraft.client.render.RenderTickCounter.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Dynamic.class})
public class RenderTickCounterDynamicMixin {
   @Shadow
   private float field_51958;

   @Inject(
      method = {"beginRenderTick(J)I"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastFrameDuration:F",
         shift = Shift.AFTER
      )}
   )
   private void timer(CallbackInfoReturnable<Integer> callback) {
      float var2 = FloatValueHelper.getInstance().getFloat();
      if (var2 > 0.0F) {
         this.field_51958 *= var2;
      }
   }
}
