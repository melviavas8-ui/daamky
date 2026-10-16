package dev.daamky.client.mixin;

import dev.daamky.client.module.misc.NoAnnoying;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractSoundInstance.class})
public class AbstractSoundInstanceMixin {
   @Inject(
      method = {"getVolume"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onGetVolume(CallbackInfoReturnable<Float> cir) {
      NoAnnoying var2 = NoAnnoying.getInstance();
      if (var2 != null && var2.isEnabled()) {
         Identifier var3 = ((SoundInstance)this).getId();
         if (var3 != null) {
            float var4 = var2.computeContains(var3);
            if (var4 < 1.0F) {
               cir.setReturnValue(cir.getReturnValueF() * var4);
            }
         }
      }
   }
}
