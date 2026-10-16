package dev.daamky.client.mixin;

import dev.daamky.client.event.MovePostEvent;
import dev.daamky.client.event.VelocityEvent;
import dev.daamky.client.internal.Profile;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.movement.FreeCam;
import dev.daamky.client.module.render.Removals;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class EntityMixin {
   @Inject(
      method = {"isGlowing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void cancelGlowingHook(CallbackInfoReturnable<Boolean> cir) {
      if (Removals.getInstance().checkEnabled6()) {
         cir.setReturnValue(false);
      }
   }

   @Redirect(
      method = {"updateVelocity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"
      )
   )
   public Vec3d updateVelocityHook(Vec3d movementInput, float speed, float yaw) {
      if (this == PlatformUtils.getClass_746()) {
         VelocityEvent.VelocityEventData var4 = new VelocityEvent.VelocityEventData(
            movementInput, speed, yaw, Entity.movementInputToVelocity(movementInput, speed, yaw)
         );
         VelocityEvent.getInstance().postAndCheckCancelled(var4);
         return var4.getVelocity();
      } else {
         return Entity.movementInputToVelocity(movementInput, speed, yaw);
      }
   }

   @Inject(
      method = {"isInsideWall"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isInsideWallHook(CallbackInfoReturnable<Boolean> cir) {
      if (FreeCam.getInstance().isEnabled() && this == PlatformUtils.getClass_746()) {
         cir.setReturnValue(false);
      }
   }

   @Inject(
      method = {"updateVelocity"},
      at = {@At("TAIL")}
   )
   private void injectUpdateVelocityTail(float speed, Vec3d movementInput, CallbackInfo ci) {
      if (this == PlatformUtils.getClass_746()) {
         MovePostEvent.EventOnMovePostData var4 = new MovePostEvent.EventOnMovePostData(speed, movementInput);
         MovePostEvent.getInstance().postAndCheckCancelled(var4);
      }
   }

   @Inject(
      method = {"pushAwayFrom"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushAwayFromHook(Entity entity, CallbackInfo ci) {
      if (!(entity instanceof Profile) && !(this instanceof Profile)) {
         AttackAura var3 = AttackAura.getInstance();
         if (var3 != null && var3.isEnabled() && (this == PlatformUtils.getClass_746() || entity == PlatformUtils.getClass_746())) {
            ci.cancel();
         }
      } else {
         ci.cancel();
      }
   }
}
