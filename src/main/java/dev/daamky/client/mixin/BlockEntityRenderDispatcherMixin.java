package dev.daamky.client.mixin;

import dev.daamky.client.module.misc.Optimization;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BlockEntityRenderDispatcher.class})
public class BlockEntityRenderDispatcherMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private <E extends BlockEntity> void onRender(E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
      Optimization var16 = Optimization.getInstance();
      double var6;
      double var8;
      double var10;
      double var12;
      if (var16.checkEnabled7()
         && MinecraftClient.getInstance().player != null
         && (var12 = (double)blockEntity.getPos().getX() - MinecraftClient.getInstance().player.getX()) * var12
               + (var10 = (double)blockEntity.getPos().getY() - MinecraftClient.getInstance().player.getY()) * var10
               + (var8 = (double)blockEntity.getPos().getZ() - MinecraftClient.getInstance().player.getZ()) * var8
            > (var6 = (double)var16.getBlock()) * var6) {
         ci.cancel();
      }
   }
}
