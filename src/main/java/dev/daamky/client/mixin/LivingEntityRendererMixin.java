package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.daamky.client.event.EntityColorEvent;
import dev.daamky.client.module.misc.Optimization;
import dev.daamky.client.module.render.Chams;
import dev.daamky.client.module.render.HitTweaks;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   @Shadow
   protected M field_4737;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderHead(S state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
      double var6;
      double var8;
      double var10;
      double var12;
      ClientPlayerEntity var16;
      if (Optimization.getInstance().checkEnabled6()
         && (var16 = MinecraftClient.getInstance().player) != null
         && (var12 = state.x - var16.getX()) * var12
               + (var10 = state.y - var16.getY()) * var10
               + (var8 = state.z - var16.getZ()) * var8
            > (var6 = (double)Optimization.getInstance().getCull()) * var6) {
         ci.cancel();
      }
   }

   @Shadow
   @Nullable
   protected abstract RenderLayer method_24302(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4);

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"
      )
   )
   private RenderLayer renderHook(LivingEntityRenderer instance, LivingEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
      if (!translucent && state.width == 0.6F) {
         byte var6 = -1;
         if (EntityColorEvent.getInstance().isEmpty()) {
            EntityColorEvent.EntityColorEventData var7 = new EntityColorEvent.EntityColorEventData(var6);
            if (EntityColorEvent.getInstance().postAndCheckCancelled(var7) && var7.getColor() != var6) {
               translucent = true;
            }
         }
      }

      return this.method_24302(state, showBody, translucent, showOutline);
   }

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
      )
   )
   private void renderModelHook(
      EntityModel<?> instance,
      MatrixStack matrixStack,
      VertexConsumer vertexConsumer,
      int light,
      int overlay,
      int color,
      @Local(ordinal = 0,argsOnly = true) LivingEntityRenderState renderState
   ) {
      int var8 = color;
      if (EntityColorEvent.getInstance().isEmpty()) {
         EntityColorEvent.EntityColorEventData var9 = new EntityColorEvent.EntityColorEventData(color);
         if (renderState.invisibleToPlayer) {
            EntityColorEvent.getInstance().postAndCheckCancelled(var9);
         }

         var8 = var9.getColor();
      }

      HitTweaks var14 = HitTweaks.getInstance();
      boolean var10 = (overlay >> 16 & 65535) == OverlayTexture.getV(true);
      if (var14.isEnabled() && var14.getCustomHitColor().getValue() && var10) {
         Color var12 = var14.getHitColor().getValue();
         int var13 = var12.getAlpha() << 24 | var12.getRed() << 16 | var12.getGreen() << 8 | var12.getBlue();
         instance.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, var13);
      } else {
         instance.render(matrixStack, vertexConsumer, light, overlay, var8);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"
      )}
   )
   private void chamsHook(S state, MatrixStack matrices, VertexConsumerProvider vcp, int light, CallbackInfo ci) {
      if (this.field_4737 != null) {
         Chams var14 = Chams.getInstance();
         if (var14.isEnabled()) {
            boolean var15 = this.field_4737 instanceof PlayerEntityModel;
            if (!var15 || var14.getPlayers().getValue() || var14.getFriends().getValue()) {
               if (var15 || var14.getMobs().getValue()) {
                  ClientPlayerEntity var16 = MinecraftClient.getInstance().player;
                  double var6;
                  double var8;
                  double var10;
                  double var12;
                  if (var16 == null
                     || !(
                           (
                                 var12 = (var10 = state.x - var16.getX()) * var10
                                    + (var8 = state.y - var16.getY()) * var8
                                    + (var6 = state.z - var16.getZ()) * var6
                              )
                              > 2304.0
                        )
                        && !(var12 < 0.7224999999999999)) {
                     Chams.handleColor(matrices, this.field_4737, light, 0);
                  }
               }
            }
         }
      }
   }
}
