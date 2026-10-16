package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.internal.RaytraceUtil;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.SaturationHelper;
import dev.daamky.client.internal.ShaderContext;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.module.combat.NoEntityTrace;
import dev.daamky.client.module.render.NightVision;
import dev.daamky.client.module.render.Removals;
import dev.daamky.client.module.render.Zoom;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public class GameRendererMixin {
   @Unique
   private static final MatrixStack daamky$renderMatrixStack = new MatrixStack();

   @ModifyExpressionValue(
      method = {"findCrosshairTarget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
      )}
   )
   private EntityHitResult computeClass_3966(EntityHitResult original, Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
      if (original != null && original.getEntity() != null && PlatformUtils.getClass_746() != null) {
         return NoEntityTrace.getInstance().computeEnabled(original.getEntity()) ? null : original;
      } else {
         return original;
      }
   }

   @ModifyExpressionValue(
      method = {"findCrosshairTarget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;"
      )}
   )
   private Vec3d computeCurrent(Vec3d original, Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
      if (camera != PlatformUtils.getClass_746()) {
         return original;
      } else {
         Rotation var8 = VecRotationHelper.getInstance().getCurrentRotation();
         return var8 != null ? var8.initInit() : original;
      }
   }

   @ModifyExpressionValue(
      method = {"findCrosshairTarget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"
      )}
   )
   private HitResult computeCurrent(HitResult original, Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
      if (camera != PlatformUtils.getClass_746()) {
         return original;
      } else {
         Rotation var8 = VecRotationHelper.getInstance().getCurrentRotation();
         return (HitResult)(var8 == null
            ? original
            : RaytraceUtil.computeClass_3965(var8, Math.max(blockInteractionRange, entityInteractionRange), false, tickDelta));
      }
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
      if (MinecraftClient.getInstance().player != null) {
         if (Removals.getInstance().checkEnabled2()) {
            ci.cancel();
         }
      }
   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"
      )
   )
   private float onRenderWorld(float delta, float first, float second) {
      if (MinecraftClient.getInstance().player == null) {
         return 0.0F;
      } else {
         return Removals.getInstance().checkEnabled7() ? 0.0F : MathHelper.lerp(delta, first, second);
      }
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
         opcode = 180,
         ordinal = 0
      )}
   )
   void render3D(RenderTickCounter renderTickCounter, CallbackInfo ci) {
      if (PlatformUtils.getClass_746() != null && MinecraftClient.getInstance().world != null) {
         if (Render3DEvent.getInstance().isEmpty()) {
            Camera var3 = MinecraftClient.getInstance().gameRenderer.getCamera();
            MatrixStack var4 = daamky$renderMatrixStack;
            var4.loadIdentity();
            var4.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var3.getPitch()));
            var4.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var3.getYaw() + 180.0F));
            Matrix4fStack var5 = RenderSystem.getModelViewStack();
            var5.pushMatrix().identity();
            Render3DEvent.Render3DEventData var6 = Render3DEvent.getMbenap();
            var6.matrixStack = var4;
            var6.partialTicks = renderTickCounter.getTickDelta(true);
            Render3DEvent.getInstance().postAndCheckCancelled(var6);
            Render.boxRenderer.initBlend(var4);
            var5.popMatrix();

            while (!var4.isEmpty()) {
               var4.pop();
            }
         }
      }
   }

   @Inject(
      method = {"renderHand"},
      at = {@At("HEAD")}
   )
   private void onPreRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
      ShaderContext.getInstance().initEnabled();
   }

   @Inject(
      method = {"renderHand"},
      at = {@At("TAIL")}
   )
   private void onPostRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
      ShaderContext.getInstance().initShader();
   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo ci) {
      if (Removals.getInstance().checkEnabled9()) {
         ci.cancel();
      }
   }

   @ModifyReturnValue(
      method = {"getFov"},
      at = {@At("RETURN")}
   )
   private float computeFloat(float original) {
      return original * Zoom.getEnabled();
   }

   @ModifyReturnValue(
      method = {"getNightVisionStrength"},
      at = {@At("RETURN")}
   )
   private static float computeEnabled(float original, LivingEntity entity, float tickDelta) {
      return NightVision.getInstance().isEnabled() && entity == MinecraftClient.getInstance().player ? NightVision.getInstance().getEnabled2() : original;
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At("TAIL")}
   )
   private void onRenderWorldTail(RenderTickCounter renderTickCounter, CallbackInfo ci) {
      SaturationHelper.getInstance().initShader();
   }
}
