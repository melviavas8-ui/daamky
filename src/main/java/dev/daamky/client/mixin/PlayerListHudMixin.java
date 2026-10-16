package dev.daamky.client.mixin;

import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.module.misc.MinecraftTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerListHud.class})
public class PlayerListHudMixin {
   private float floatVal = 0.0F;
   private long msoahdi = 0L;
   private boolean flag = false;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void onRenderHead(DrawContext context, int windowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
      this.flag = false;
      long var6 = System.currentTimeMillis();
      if (var6 - this.msoahdi > 50L) {
         this.floatVal = 0.0F;
      }

      this.msoahdi = var6;
      if (MinecraftTweaks.getInstance().tabAnimations.getValue()) {
         float var8 = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
         this.floatVal = EntityHelper.computeFloat(this.floatVal, 1.0F, 0.45F * var8);
         context.getMatrices().push();
         this.flag = true;
         float var9 = 0.8F + this.floatVal * 0.2F;
         float var10 = (float)windowWidth / 2.0F * (1.0F - var9);
         float var11 = -100.0F * (1.0F - this.floatVal);
         context.getMatrices().translate(var10, var11, 0.0F);
         context.getMatrices().scale(var9, var9, 1.0F);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void onRenderReturn(DrawContext context, int windowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
      if (this.flag) {
         context.getMatrices().pop();
         this.flag = false;
      }
   }
}
