package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import dev.daamky.client.event.MotionEvent;
import dev.daamky.client.event.MoveEvent;
import dev.daamky.client.event.PostMotionEvent;
import dev.daamky.client.event.SprintEvent;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.ClickSlotEventDataHelper;
import dev.daamky.client.module.movement.FreeCam;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
   @Shadow
   public Input field_3913;

   public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
      super(world, profile);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tickHook(CallbackInfo ci) {
      UpdateEvent.getInstance().postAndCheckCancelled();
   }

   @Inject(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
      )},
      cancellable = true
   )
   public void moveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
      MoveEvent.MoveEventData var4 = MoveEvent.getMoveEventData();
      var4.setX(movement.x);
      var4.setY(movement.y);
      var4.setZ(movement.z);
      if (MoveEvent.getInstance().postAndCheckCancelled(var4)) {
         super.move(movementType, new Vec3d(var4.getX(), var4.getY(), var4.getZ()));
         ci.cancel();
      }
   }

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void preMotion(CallbackInfo ci) {
      MotionEvent.MotionEventData var2 = MotionEvent.getMotionEventData();
      var2.withX(this.getX())
         .withY(this.getY())
         .withZ(this.getZ())
         .withYaw((double)this.getYaw(1.0F))
         .withPitch((double)this.getPitch(1.0F))
         .withGround(this.isOnGround());
      if (MotionEvent.getInstance().postAndCheckCancelled(var2)) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("TAIL")}
   )
   private void postMotion(CallbackInfo ci) {
      PostMotionEvent.getInstance().postAndCheckCancelled(PostMotionEvent.getEventPostMotionData());
   }

   @ModifyExpressionValue(
      method = {"tickMovement"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;canSprint()Z"
      )}
   )
   private boolean computeHook(boolean original) {
      return this.sprintHook(original);
   }

   @ModifyExpressionValue(
      method = {"tickMovement"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"
      )}
   )
   private boolean computeHook2(boolean original) {
      return this.sprintHook(original);
   }

   @Inject(
      method = {"pushOutOfBlocks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void noPushByBlocksHook(double x, double d, CallbackInfo ci) {
      if (FreeCam.getInstance().isEnabled()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"closeHandledScreen"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"
      )},
      cancellable = true
   )
   private void onCloseHandledScreen(CallbackInfo ci) {
      if (ClickSlotEventDataHelper.checkCancelled()) {
         ci.cancel();
      }
   }

   @Unique
   private boolean sprintHook(boolean origin) {
      if (this.field_3913 != null && !(this.field_3913.movementForward <= 0.0F)) {
         SprintEvent.SprintEventData var2 = SprintEvent.getSprintEventData();
         var2.getDirectionalInput().handleComp(this.field_3913);
         var2.setSprint(origin);
         return SprintEvent.getInstance().computeCancelled(var2);
      } else {
         return false;
      }
   }
}
