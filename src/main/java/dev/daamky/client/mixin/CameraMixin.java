package dev.daamky.client.mixin;

import dev.daamky.client.event.CameraEvent;
import dev.daamky.client.module.movement.FreeCam;
import dev.daamky.client.module.render.CameraTweaks;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Camera.class})
public abstract class CameraMixin {
   @Shadow
   protected abstract float method_19318(float var1);

   @ModifyArgs(
      method = {"update"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V",
         ordinal = 0
      )
   )
   private void update(Args args) {
      if (CameraTweaks.getInstance().isEnabled()) {
         args.set(0, -this.method_19318(CameraTweaks.getInstance().distance.getValue()));
      }
   }

   @Inject(
      method = {"clipToSpace"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void clipToSpace(float f, CallbackInfoReturnable<Float> returnable) {
      if (CameraTweaks.getInstance().isEnabled()) {
         returnable.setReturnValue(CameraTweaks.getInstance().distance.getValue());
      }
   }

   @Redirect(
      method = {"update"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
      )
   )
   private void redirectSetRotation(
      Camera instance, float yaw, float pitch, BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta
   ) {
      CameraEvent var9 = CameraEvent.getInstance();
      var9.handlePartial(yaw, pitch, tickDelta);
      var9.postAndCheckCancelled();
      float var10 = var9.getYaw();
      float var11 = var9.getPitch();
      if (thirdPerson && inverseView) {
         var10 += 180.0F;
         var11 = -var11;
      }

      ((CameraAccessor)instance).setCustomRotation(var10, var11);
   }

   @Inject(
      method = {"update"},
      at = {@At("TAIL")}
   )
   private void onUpdateTail(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
      FreeCam var7 = FreeCam.getInstance();
      if (var7.isEnabled()) {
         Vec3d var8 = var7.initInit(tickDelta);
         if (var8 != null) {
            ((CameraAccessor)this).setCustomPos(var8.x, var8.y, var8.z);
         }

         float[] var9 = var7.getFloats();
         if (var9 != null) {
            ((CameraAccessor)this).setCustomRotation(var9[0], var9[1]);
         }
      }
   }
}
