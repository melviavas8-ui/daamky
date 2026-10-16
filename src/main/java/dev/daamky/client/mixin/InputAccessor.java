package dev.daamky.client.mixin;

import dev.daamky.client.internal.Callback9;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Input.class})
public abstract class InputAccessor implements Callback9 {
   @Shadow
   public float field_3905;
   @Shadow
   public float field_3907;
   @Unique
   protected PlayerInput untransformed = PlayerInput.DEFAULT;

   @Override
   public PlayerInput evelina$getUntransformed() {
      return this.untransformed;
   }
}
