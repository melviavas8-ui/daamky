package dev.daamky.client.mixin;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.TravelEvent;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntity.class})
public abstract class PlayerEntityMixin extends LivingEntityMixin {
   @Inject(
      method = {"attack"},
      at = {@At("HEAD")}
   )
   public void attackEventHook(Entity target, CallbackInfo ci) {
      if (PlatformUtils.getClass_746() != null) {
         if (this == PlatformUtils.getClass_746()) {
            AttackEvent.getInstance().postAndCheckCancelled(new AttackEvent.AttackEventData(target));
         }
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")}
   )
   public void travelHook(Vec3d movementInput, CallbackInfo ci) {
      if (this == PlatformUtils.getClass_746()) {
         TravelEvent.getInstance().postAndCheckCancelled();
      }
   }
}
