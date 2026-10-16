package dev.daamky.client.mixin;

import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.module.render.CustomCrosshair;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.module.render.Removals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.JumpingMount;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public class InGameHudMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   public void renderHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      UHalfTexelSizeHelper.handleEnabled(context.getMatrices());
      Render2DEvent.Render2DEventData var4 = Render2DEvent.getRender2DEventData();
      var4.context = context;
      var4.matrixStack = context.getMatrices();
      var4.partialTicks = tickCounter.getTickDelta(false);
      Render2DEvent.getInstance().postAndCheckCancelled(var4);
   }

   @Inject(
      method = {"renderStatusEffectOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Potions")) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"renderScoreboardSidebar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      if (MinecraftClient.getInstance().player != null) {
         if (Removals.getInstance().checkEnabled5()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"renderScoreboardSidebar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderScoreboardSidebarObj(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
      if (MinecraftClient.getInstance().player != null) {
         if (Removals.getInstance().checkEnabled5()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"renderHotbar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"renderStatusBars"},
      at = {@At("HEAD")}
   )
   private void renderStatusBarsHead(DrawContext context, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, -12.0F, 0.0F);
         }
      }
   }

   @Inject(
      method = {"renderStatusBars"},
      at = {@At("RETURN")}
   )
   private void renderStatusBarsReturn(DrawContext context, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().pop();
         }
      }
   }

   @Inject(
      method = {"renderExperienceBar"},
      at = {@At("HEAD")}
   )
   private void renderExperienceBarHead(DrawContext context, int x, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, -12.0F, 0.0F);
         }
      }
   }

   @Inject(
      method = {"renderExperienceBar"},
      at = {@At("RETURN")}
   )
   private void renderExperienceBarReturn(DrawContext context, int x, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().pop();
         }
      }
   }

   @Inject(
      method = {"renderMountJumpBar"},
      at = {@At("HEAD")}
   )
   private void renderMountJumpBarHead(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, -12.0F, 0.0F);
         }
      }
   }

   @Inject(
      method = {"renderMountJumpBar"},
      at = {@At("RETURN")}
   )
   private void renderMountJumpBarReturn(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().pop();
         }
      }
   }

   @Inject(
      method = {"renderMountHealth"},
      at = {@At("HEAD")}
   )
   private void renderMountHealthHead(DrawContext context, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, -12.0F, 0.0F);
         }
      }
   }

   @Inject(
      method = {"renderMountHealth"},
      at = {@At("RETURN")}
   )
   private void renderMountHealthReturn(DrawContext context, CallbackInfo ci) {
      if (Interface.getInstance().isEnabled()) {
         if (Interface.getInstance().gps.isEnabled("Hotbar")) {
            context.getMatrices().pop();
         }
      }
   }

   @Inject(
      method = {"renderCrosshair"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      if (CustomCrosshair.getInstance().isEnabled()) {
         ci.cancel();
      }
   }
}
