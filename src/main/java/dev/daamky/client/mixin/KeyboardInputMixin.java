package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.daamky.client.event.InputTransformEvent;
import dev.daamky.client.event.MovementInputEvent;
import dev.daamky.client.event.SprintEvent;
import dev.daamky.client.internal.CameraManager;
import dev.daamky.client.internal.FromRotationHelper;
import dev.daamky.client.internal.InputState;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({KeyboardInput.class})
public class KeyboardInputMixin extends InputAccessor {
   @Unique
   private int daamky$lastSector = 0;

   @Unique
   private float abobaGetMovementMultiplier(boolean positive, boolean negative) {
      if (positive == negative) {
         return 0.0F;
      } else {
         return positive ? 1.0F : -1.0F;
      }
   }

   @ModifyExpressionValue(
      method = {"tick"},
      at = {@At(
         value = "NEW",
         target = "(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;"
      )}
   )
   private PlayerInput initInit(PlayerInput original) {
      MovementInputEvent.MovementInputEventData var2 = new MovementInputEvent.MovementInputEventData(
         original, original.jump(), original.sneak(), new InputState(original)
      );
      MovementInputEvent.getInstance().postAndCheckCancelled(var2);
      InputState var3 = var2.getDirectionalInput();
      InputState var4 = this.transformDirection(var3);
      SprintEvent.SprintEventData var5 = new SprintEvent.SprintEventData(var4);
      SprintEvent.getInstance().computeCancelled(var5);
      float var6 = this.abobaGetMovementMultiplier(var4.isForwards(), var4.isBackwards());
      float var7 = this.abobaGetMovementMultiplier(var4.isLeft(), var4.isRight());
      InputTransformEvent var8 = InputTransformEvent.getInstance();
      var8.handleForward(original, var6, var7);
      var8.postAndCheckCancelled();
      var6 = var8.getForward();
      var7 = var8.getStrafe();
      this.field_3905 = var6;
      this.field_3907 = var7;
      AttackAura var9 = AttackAura.getInstance();
      boolean var10 = var9 != null && var9.isEnabled() && var9.int4 > 0;
      ClientPlayerEntity var11 = PlatformUtils.getClass_746();
      boolean var12 = var11 != null && var11.isUsingItem();
      boolean var13 = !var10 && var5.isSprint() && var6 > 0.0F && !var2.isSneak() && !var12;
      return new PlayerInput(var6 > 0.0F, var6 < 0.0F, var7 > 0.0F, var7 < 0.0F, var2.isJump(), var2.isSneak(), var13);
   }

   @Unique
   private InputState transformDirection(InputState input) {
      ClientPlayerEntity var2 = PlatformUtils.getClass_746();
      VecRotationHelper var3 = VecRotationHelper.getInstance();
      Rotation var4 = var3.getCurrentRotation();
      FromRotationHelper var5 = var3.getLast();
      if (var5 != null && var4 != null && var2 != null && var5.isMoveCorrection()) {
         float var6 = KeyboardInput.getMovementMultiplier(input.isForwards(), input.isBackwards());
         float var7 = KeyboardInput.getMovementMultiplier(input.isLeft(), input.isRight());
         if (var6 == 0.0F && var7 == 0.0F) {
            return input;
         } else {
            AttackAura var8 = AttackAura.getInstance();
            float var9 = var4.getYaw();
            float var10 = CameraManager.getFlag() ? CameraManager.getFloat() : var2.getYaw();
            float var11;
            if (var6 > 0.0F) {
               if (var7 > 0.0F) {
                  var11 = -45.0F;
               } else if (var7 < 0.0F) {
                  var11 = 45.0F;
               } else {
                  var11 = 0.0F;
               }
            } else if (var6 < 0.0F) {
               if (var7 > 0.0F) {
                  var11 = -135.0F;
               } else if (var7 < 0.0F) {
                  var11 = 135.0F;
               } else {
                  var11 = 180.0F;
               }
            } else if (var7 > 0.0F) {
               var11 = -90.0F;
            } else {
               var11 = 90.0F;
            }

            boolean var12 = var8 != null && var8.isEnabled() && var8.is() && var8.class_1309 != null;
            float var13;
            if (var12) {
               double var14 = var8.class_1309.getX() - var2.getX();
               double var16 = var8.class_1309.getZ() - var2.getZ();
               if (var14 * var14 + var16 * var16 > 0.01) {
                  float var18 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var16, var14)) - 90.0);
                  var13 = var18 + var11;
               } else {
                  var13 = var10 + var11;
               }
            } else {
               var13 = var10 + var11;
            }

            float var20 = MathHelper.wrapDegrees(var13 - var9);
            int var15 = this.daamky$lastSector;
            float var21 = Float.MAX_VALUE;

            for (int var17 = 0; var17 < 8; var17++) {
               float var23 = (float)var17 * 45.0F;
               float var19 = Math.abs(MathHelper.wrapDegrees(var20 - var23));
               if (var17 == this.daamky$lastSector) {
                  var19 -= 6.0F;
               }

               if (var19 < var21) {
                  var21 = var19;
                  var15 = var17;
               }
            }

            if (var12) {
               if (var6 > 0.0F) {
                  if (var15 == 2 || var15 == 3) {
                     var15 = 1;
                  } else if (var15 == 6 || var15 == 5) {
                     var15 = 7;
                  } else if (var15 == 4) {
                     var15 = 0;
                  }
               } else if (var6 < 0.0F) {
                  if (var15 == 1 || var15 == 2) {
                     var15 = 3;
                  } else if (var15 == 7 || var15 == 6) {
                     var15 = 5;
                  } else if (var15 == 0) {
                     var15 = 4;
                  }
               }
            }

            this.daamky$lastSector = var15;
            float var22;
            float var24;
            switch (var15) {
               case 0:
                  var22 = 1.0F;
                  var24 = 0.0F;
                  break;
               case 1:
                  var22 = 1.0F;
                  var24 = -1.0F;
                  break;
               case 2:
                  var22 = 0.0F;
                  var24 = -1.0F;
                  break;
               case 3:
                  var22 = -1.0F;
                  var24 = -1.0F;
                  break;
               case 4:
                  var22 = -1.0F;
                  var24 = 0.0F;
                  break;
               case 5:
                  var22 = -1.0F;
                  var24 = 1.0F;
                  break;
               case 6:
                  var22 = 0.0F;
                  var24 = 1.0F;
                  break;
               case 7:
                  var22 = 1.0F;
                  var24 = 1.0F;
                  break;
               default:
                  var22 = 1.0F;
                  var24 = 0.0F;
            }

            return new InputState(var22, var24);
         }
      } else {
         return input;
      }
   }
}
