package dev.daamky.client.mixin;

import dev.daamky.client.internal.CameraManager;
import dev.daamky.client.module.render.SwingAnimation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemRenderer.class})
public abstract class HeldItemRendererMixin {
   @Unique
   private float savedYaw;
   @Unique
   private float savedPitch;
   @Unique
   private float savedPrevYaw;
   @Unique
   private float savedPrevPitch;
   @Unique
   private float savedBodyYaw;
   @Unique
   private float savedPrevBodyYaw;
   @Unique
   private float savedHeadYaw;
   @Unique
   private float savedPrevHeadYaw;
   @Unique
   private float savedLastRenderYaw;
   @Unique
   private float savedRenderYaw;
   @Unique
   private float savedLastRenderPitch;
   @Unique
   private float savedRenderPitch;
   @Unique
   private boolean swapped = false;

   @Inject(
      method = {"renderItem"},
      at = {@At("HEAD")}
   )
   private void onRenderItemHead(float tickDelta, MatrixStack matrices, Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
      if (player != null && CameraManager.getFlag()) {
         this.savedYaw = player.getYaw();
         this.savedPitch = player.getPitch();
         this.savedPrevYaw = player.prevYaw;
         this.savedPrevPitch = player.prevPitch;
         this.savedBodyYaw = player.bodyYaw;
         this.savedPrevBodyYaw = player.prevBodyYaw;
         this.savedHeadYaw = player.headYaw;
         this.savedPrevHeadYaw = player.prevHeadYaw;
         this.savedLastRenderYaw = player.lastRenderYaw;
         this.savedRenderYaw = player.renderYaw;
         this.savedLastRenderPitch = player.lastRenderPitch;
         this.savedRenderPitch = player.renderPitch;
         float var7 = CameraManager.getFloat();
         float var8 = CameraManager.getMrula();
         player.setYaw(var7);
         player.setPitch(var8);
         player.prevYaw = var7;
         player.prevPitch = var8;
         player.bodyYaw = var7;
         player.prevBodyYaw = var7;
         player.headYaw = var7;
         player.prevHeadYaw = var7;
         player.lastRenderYaw = var7;
         player.renderYaw = var7;
         player.lastRenderPitch = var8;
         player.renderPitch = var8;
         this.swapped = true;
      }
   }

   @Inject(
      method = {"renderItem"},
      at = {@At("RETURN")}
   )
   private void onRenderItemReturn(float tickDelta, MatrixStack matrices, Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
      if (player != null && this.swapped) {
         player.setYaw(this.savedYaw);
         player.setPitch(this.savedPitch);
         player.prevYaw = this.savedPrevYaw;
         player.prevPitch = this.savedPrevPitch;
         player.bodyYaw = this.savedBodyYaw;
         player.prevBodyYaw = this.savedPrevBodyYaw;
         player.headYaw = this.savedHeadYaw;
         player.prevHeadYaw = this.savedPrevHeadYaw;
         player.lastRenderYaw = this.savedLastRenderYaw;
         player.renderYaw = this.savedRenderYaw;
         player.lastRenderPitch = this.savedLastRenderPitch;
         player.renderPitch = this.savedRenderPitch;
         this.swapped = false;
      }
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderItem(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (!item.isEmpty() && !(item.getItem() instanceof FilledMapItem)) {
         ci.cancel();
         SwingAnimation.getInstance().handleDegrees(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
      }
   }
}
