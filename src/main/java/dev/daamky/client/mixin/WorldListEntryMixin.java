package dev.daamky.client.mixin;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.Render;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.WorldListWidget.WorldEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldEntry.class})
public abstract class WorldListEntryMixin {
   @Shadow
   @Final
   public LevelSummary field_19138;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRender(
      DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci
   ) {
      MatrixStack var12 = context.getMatrices();
      LevelSummary var13 = this.field_19138;
      if (var13 != null) {
         Color var14 = hovered ? new Color(20, 20, 24, 255) : new Color(10, 10, 12, 190);
         Render.rectRenderer.handleRgb(var12, (float)x, (float)y, (float)entryWidth, (float)entryHeight, 4.0F, var14);
         String var15 = var13.getDisplayName();
         String var16 = var13.getDetails().getString();
         FontBootstrap.clientFont10.handleColor(var12, var15, (float)(x + 36), (float)(y + 4), 7.5F, Color.WHITE);
         FontBootstrap.clientFont11.handleColor(var12, var16, (float)(x + 36), (float)(y + 16), 6.0F, new Color(170, 170, 175));
         if (hovered) {
            float var17 = (float)(x + 16);
            float var18 = (float)y + (float)entryHeight / 2.0F;
            FontBootstrap.clientFont14.handleColor(var12, "`", var17 - 4.0F, var18 - 5.0F, 14.0F, new Color(255, 255, 255, 200));
         }

         ci.cancel();
      }
   }
}
