package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Context extends Widget {
   public final ColorTheme theme;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();

   public Context(ColorTheme theme) {
      this.theme = themex;
   }

   public void handleTheme(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var5 = context.getMatrices();
      boolean var6 = this.computeFlag((float)mouseX, (float)mouseY);
      this.animatedValue2.update();
      this.animatedValue2.animateTo(ThemeColor.getTheme().getName().equals(this.theme.getName()) ? 1.0 : 0.7, 200L, Easing.SINE_OUT);
      this.animatedValue.update();
      this.animatedValue.animateTo(var6 ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
      int var7 = (int)((double)this.clamp() * this.animatedValue2.getValue() * 255.0);
      Color var8 = ColorMath.initBlue(this.theme.getPrimary(), var7);
      Color var9 = ColorMath.initBlue(this.theme.getSecondary(), var7);
      float var10 = this.getHeight() * 0.2F;
      float var11 = (float)((double)(this.getHeight() * 0.4F) + this.animatedValue.getValue());
      Render.uSizeHelper.handleRgb(var5, this.getX(), this.getY(), this.getWidth(), this.getHeight(), var10, var8, var9, var8, var9);
      FontBootstrap.mdoswa
         .handleColor2(
            var5,
            this.theme.getName(),
            this.getX() + this.getWidth() / 2.0F,
            this.getY() + this.getHeight() / 2.0F - var11 / 2.0F,
            var11,
            ColorMath.initBlue(this.theme.getText(), var7)
         );
   }

   public boolean computeFlag(float mouseX, float mouseY) {
      return Geometry.computeFlag(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
   }

   public ColorTheme getTheme() {
      return this.theme;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public AnimatedValue getAnimatedValue2() {
      return this.animatedValue2;
   }
}
