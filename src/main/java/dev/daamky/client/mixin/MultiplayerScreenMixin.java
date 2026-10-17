package dev.daamky.client.mixin;

import dev.daamky.client.internal.FrameSizeHelper;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SplashOverlay.class})
public abstract class MultiplayerScreenMixin extends Overlay {
   @Shadow
   @Final
   public MinecraftClient field_18217;
   @Shadow
   @Final
   public ResourceReload field_17767;
   @Shadow
   public float field_17770;
   @Shadow
   public long field_17771;
   @Unique
   public long startTime = -1L;
   @Unique
   public FrameSizeHelper introAnimation;
   @Unique
   public final String[] LOADING_MESSAGES = new String[]{
      "Загрузка ресурсов...", "Инициализация модулей...", "Подготовка интерфейса...", "Загрузка компонентов...", "Почти готово..."
   };

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRenderCustomSplash(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      ci.cancel();
      int var6 = context.getScaledWindowWidth();
      int var7 = context.getScaledWindowHeight();
      long var8 = Util.getMeasuringTimeMs();
      if (this.startTime == -1L) {
         this.startTime = var8;
      }

      if (this.introAnimation == null) {
         this.introAnimation = new FrameSizeHelper(Identifier.of("daamky", "images/interface/atlas.png"), 8, 8, 61, 14, 256, 190);
      }

      context.fill(0, 0, var6, var7, -16052972);
      float var10 = 200.0F;
      float var11 = 148.0F;
      float var12 = ((float)var6 - var10) / 2.0F;
      float var13 = ((float)var7 - var11) / 2.0F - 24.0F;

      try {
         this.introAnimation.handleFrame(context, var12, var13, var10, var11, Color.WHITE);
      } catch (Throwable var21) {
      }

      if (this.field_18217.textRenderer != null) {
         long var14 = var8 - this.startTime;
         int var16 = (int)(var14 / 1800L % (long)this.LOADING_MESSAGES.length);
         String var17 = this.LOADING_MESSAGES[var16];
         int var18 = -7431764;
         int var19 = this.field_18217.textRenderer.getWidth(var17);
         context.drawText(this.field_18217.textRenderer, var17, (var6 - var19) / 2, (int)var13 + (int)var11 + 8, var18, false);
      }

      this.field_17770 = MathHelper.clamp(this.field_17770 * 0.92F + this.field_17767.getProgress() * 0.08F, 0.0F, 1.0F);
      int var22 = Math.min(240, (int)((float)var6 * 0.5F));
      byte var15 = 3;
      int var23 = (var6 - var22) / 2;
      int var24 = (int)var13 + (int)var11 + 26;
      context.fill(var23, var24, var23 + var22, var24 + var15, -15197402);
      int var25 = (int)((float)var22 * this.field_17770);
      if (var25 > 0) {
         context.fill(var23, var24, var23 + var25, var24 + var15, -10777105);
      }

      if (this.field_18217.textRenderer != null) {
         String var26 = (int)(this.field_17770 * 100.0F) + "%";
         int var20 = this.field_18217.textRenderer.getWidth(var26);
         context.drawText(this.field_18217.textRenderer, var26, (var6 - var20) / 2, var24 + 7, -10785918, false);
      }

      if (this.field_17767.isComplete()) {
         this.field_18217.setOverlay(null);
      }
   }
}
