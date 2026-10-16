package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.daamky.client.module.render.Ambience;
import net.minecraft.client.world.ClientWorld.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Properties.class})
public class ClientWorldPropertiesMixin {
   @ModifyReturnValue(
      method = {"getTimeOfDay"},
      at = {@At("RETURN")}
   )
   private long computeLong(long original) {
      return Ambience.getInstance().computeLong(original);
   }
}
