package dev.daamky.client.mixin;

import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.misc.Optimization;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderDispatcher.class})
public class EntityRenderDispatcherMixin {
   @Inject(
      method = {"renderShadow"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onRenderShadow(
      MatrixStack matrices, VertexConsumerProvider vertexConsumers, EntityRenderState state, float opacity, float tickDelta, WorldView world, float radius, CallbackInfo ci
   ) {
      if (Optimization.getInstance().checkEnabled5()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private <E extends Entity> void onRender(
      E entity, double x, double y, double z, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci
   ) {
      Optimization var13 = Optimization.getInstance();
      if (var13.checkEnabled6() && entity != null) {
         MinecraftClient var14 = MinecraftClient.getInstance();
         if (var14.player != null && entity != var14.player) {
            if (entity instanceof PlayerEntity) {
               double var15 = x * x + y * y + z * z;
               if (var15 <= 9216.0) {
                  return;
               }
            }

            try {
               LivingEntity var20 = AttackAura.getInstance().class_1309;
               if (var20 != null && var20 == entity) {
                  return;
               }
            } catch (Throwable var19) {
            }

            if (entity.isGlowing() || entity instanceof EnderDragonEntity || entity instanceof WitherEntity) {
               return;
            }

            double var21 = x * x + y * y + z * z;
            double var17 = (double)var13.getCull();
            if (var21 > var17 * var17) {
               ci.cancel();
            }
         }
      }
   }
}
