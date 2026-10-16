package dev.daamky.client.mixin;

import dev.daamky.client.module.render.ItemPhysic;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
public class ItemEntityRendererMixin {
   @Shadow
   @Final
   private Random field_4725;

   @Shadow
   public static void method_56858(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStackEntityRenderState state, Random random) {
      throw new AssertionError();
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void daamky$flatOnGround(ItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
      ItemPhysic var6 = ItemPhysic.getInstance();
      if (var6.isEnabled() && !state.itemRenderState.isEmpty()) {
         matrices.push();

         try {
            matrices.translate(0.0F, var6.getFloat(), 0.0F);
            if (var6.checkRotate()) {
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var6.getCurrent()));
            }

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            method_56858(matrices, vertexConsumers, light, state, this.field_4725);
         } catch (Throwable var11) {
         } finally {
            matrices.pop();
         }

         ci.cancel();
      }
   }
}
