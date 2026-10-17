package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.ButtonSetting;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Context2 extends SettingRow {
   public final ButtonSetting setting;
   public final AnimatedValue animatedValue = new AnimatedValue();

   public Context2(ButtonSetting setting) {
      super(setting);
      this.setting = setting;
      this.handleFloat(this.getFloat());
   }

   public void handleName(DrawContext context, int mouseX, int mouseY, float delta) {
      this.handleFloat(this.getFloat());
      MatrixStack var5 = context.getMatrices();
      int var6 = (int)(this.clamp() * 255.0F);
      this.animatedValue.update();
      this.animatedValue.animateTo(this.computeFlag((double)mouseX, (double)mouseY) ? 1.0 : 0.0, 200L, Easing.QUINT_OUT);
      float var7 = InterfaceMetrics.computeFloat(2.0F);
      Color var8 = this.computeFlag((double)mouseX, (double)mouseY)
         ? InterfaceMetrics.computeColor8((int)((float)var6 * 1.5F))
         : InterfaceMetrics.computeColor8(var6);
      Render.rectRenderer.handleRgb(var5, this.getX(), this.getY(), this.getWidth(), this.getHeight(), var7, var8);
      float var9 = InterfaceMetrics.computeFloat(6.4F);
      FontBootstrap.clientFont10
         .handleColor2(
            var5,
            EngHelper.computeString(this.setting.getName()),
            this.getX() + this.getWidth() / 2.0F,
            this.getY() + this.getHeight() / 2.0F - var9 / 2.0F - InterfaceMetrics.computeFloat(0.6F),
            var9,
            InterfaceMetrics.computeColor2(var6)
         );
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0 && this.computeFlag(mouseX, mouseY) && this.setting.getValue() != null) {
         this.setting.getValue().run();
      }
   }

   public boolean computeFlag(double mouseX, double mouseY) {
      return Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight());
   }

   @Override
   public float getFloat() {
      return 12.0F;
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double h, double v) {
   }
}
