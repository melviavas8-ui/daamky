package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.daamky.client.event.JumpEvent;
import dev.daamky.client.internal.FromRotationHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.module.render.SwingAnimation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends EntityMixin {
   @Shadow
   public int field_6228;

   @Shadow
   public abstract boolean method_6128();

   @Inject(
      method = {"getHandSwingDuration"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
      SwingAnimation var2 = SwingAnimation.getInstance();
      if (var2.isEnabled() && var2.slow.getValue()) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var3 != null && this == var3.player) {
            callbackInfoReturnable.setReturnValue(var2.speed.getValue().intValue());
         }
      }
   }

   @Inject(
      method = {"jump"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F"
      )}
   )
   public void onJumping(CallbackInfo ci) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && this == var2.player) {
         JumpEvent.getInstance().postAndCheckCancelled();
      }
   }

   @ModifyExpressionValue(
      method = {"jump"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"
      )}
   )
   public float fixJumpVelocity(float original) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && this == var2.player) {
         VecRotationHelper var3 = VecRotationHelper.getInstance();
         Rotation var4 = var3.getCurrent();
         FromRotationHelper var5 = var3.getLast();
         return var5 != null && var5.isMoveCorrection() ? var4.getYaw() : original;
      } else {
         return original;
      }
   }

   @ModifyExpressionValue(
      method = {"calcGlidingVelocity"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"
      )}
   )
   private float computeCorrection(float original) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && this == var2.player) {
         VecRotationHelper var3 = VecRotationHelper.getInstance();
         Rotation var4 = var3.getCurrent();
         FromRotationHelper var5 = var3.getLast();
         return var5 != null && var5.isMoveCorrection() ? var4.getPitch() : original;
      } else {
         return original;
      }
   }

   @ModifyExpressionValue(
      method = {"calcGlidingVelocity"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
      )}
   )
   private Vec3d computeCorrection(Vec3d original) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && this == var2.player) {
         VecRotationHelper var3 = VecRotationHelper.getInstance();
         Rotation var4 = var3.getCurrent();
         FromRotationHelper var5 = var3.getLast();
         return var5 != null && var5.isMoveCorrection() ? var4.initInit() : original;
      } else {
         return original;
      }
   }
}
