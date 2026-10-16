package dev.daamky.client.mixin;

import dev.daamky.client.module.misc.HealthResolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {
   private static long timeStamp = 0L;

   public PlayerEntityRendererMixin(Context ctx, PlayerEntityModel model, float shadowRadius) {
      super(ctx, model, shadowRadius);
   }

   @Inject(
      method = {"updateRenderState"},
      at = {@At("HEAD")}
   )
   private void rwHealthFix(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
      MinecraftClient var5 = MinecraftClient.getInstance();
      if (var5 != null && var5.player != null && var5.world != null) {
         if (HealthResolver.getInstance().checkEnabled()) {
            Scoreboard var6 = abstractClientPlayerEntity.getScoreboard();
            ScoreboardObjective var7 = var6.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
            if (var7 != null) {
               long var8 = System.currentTimeMillis();
               if (var8 - longVal >= 1000L) {
                  longVal = var8;

                  for (PlayerEntity var11 : var5.world.getPlayers()) {
                     if (var11 != null && var11 != var5.player) {
                        try {
                           ScoreAccess var12 = var6.getOrCreateScore(var11, var7);
                           int var13 = var12.getScore();
                           if (var13 >= 0 && (float)var13 <= var11.getMaxHealth()) {
                              var11.setHealth((float)var13);
                           }
                        } catch (Exception var14) {
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected float getShadowRadius(EntityRenderState param1) {
      return super.getShadowRadius((LivingEntityRenderState)var1);
   }
}
