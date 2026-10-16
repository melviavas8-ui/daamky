package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.FloatSetting;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Opacity extends SettingRow {
   public final FloatSetting setting;
   public boolean flag;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();

   public Opacity(FloatSetting setting) {
      super(settingx);
      this.setting = settingx;
      float var2 = (settingx.getValue() - settingx.getMin()) / (settingx.getMax() - settingx.getMin()) * this.getWidth();
      this.animatedValue.handleStart((double)var2);
      this.handleFloat(this.getFloat());
   }

   public void initSetting(DrawContext context, int mouseX, int mouseY, float delta) {
      this.handleFloat(this.getFloat());
      if (this.flag) {
         float var5 = ((float)mouseX - this.getX()) / this.getWidth();
         var5 = this.setting.getMin() + var5 * (this.setting.getMax() - this.setting.getMin());
         var5 = (float)Math.round(var5 / this.setting.getStep()) * this.setting.getStep();
         this.setting.setValue(EntityHelper.computeRound(Math.max(this.setting.getMin(), Math.min(this.setting.getMax(), var5)), this.setting.getStep()));
      }

      MatrixStack var32 = context.getMatrices();
      float var6 = InterfaceMetrics.computeFloat(7.0F);
      float var7 = InterfaceMetrics.computeFloat(7.0F);
      int var8 = (int)(this.clamp() * 255.0F);
      boolean var9 = this.setting.getName().toLowerCase().contains("opacity")
         || this.setting.getName().toLowerCase().contains("alpha")
         || this.setting.getName().toLowerCase().contains("chance")
         || this.setting.getName().toLowerCase().contains("percent")
         || this.setting.getMax() == 100.0F;
      String var10 = var9 ? "%" : "";
      String var11 = this.computeEquals(this.setting.getValue()) + var10;
      float var12 = FontBootstrap.clientFont10.computeFloat(var11, var7);
      FontBootstrap.clientFont10
         .handleColor(
            var32,
            EngHelper.computeString(this.setting.getName()),
            this.getX(),
            this.getY() + InterfaceMetrics.computeFloat(1.0F),
            var6,
            InterfaceMetrics.computeColor4(var8)
         );
      FontBootstrap.clientFont10
         .handleColor(
            var32, var11, this.getX() + this.getWidth() - var12, this.getY() + InterfaceMetrics.computeFloat(1.0F), var7, InterfaceMetrics.computeColor3(var8)
         );
      this.animatedValue2.update();
      boolean var13 = Geometry.computeFlag(
         (double)mouseX, (double)mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight()
      );
      this.animatedValue2.animateTo(!this.flag && !var13 ? 0.0 : 1.0, 180L, Easing.QUINT_OUT);
      float var14 = (float)this.animatedValue2.getValue();
      float var15 = this.getY() + var6 + InterfaceMetrics.computeFloat(3.0F);
      float var16 = InterfaceMetrics.computeFloat(2.6F);
      float var17 = var15 + InterfaceMetrics.computeFloat(6.0F) / 2.0F - var16 / 2.0F;
      float var18 = (this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * this.getWidth();
      this.animatedValue.update();
      this.animatedValue.animateTo(var18, 180L, Easing.QUINT_OUT);
      float var19 = (float)this.animatedValue.getValue();
      Render.rectRenderer.handleRgb(var32, this.getX(), var17, this.getWidth(), var16, var16 / 2.0F, InterfaceMetrics.computeColor7(var8));
      if (var19 > 0.0F) {
         Render.rectRenderer.handleRgb(var32, this.getX(), var17, var19, var16, var16 / 2.0F, InterfaceMetrics.computeColor3(var8));
      }

      float var20 = this.getX() + var19;
      float var21 = var17 + var16 / 2.0F;
      float var22 = InterfaceMetrics.computeFloat(8.5F) + InterfaceMetrics.computeFloat(2.0F) * var14;
      float var23 = InterfaceMetrics.computeFloat(6.5F) + InterfaceMetrics.computeFloat(1.5F) * var14;
      float var24 = InterfaceMetrics.computeFloat(5.2F) + InterfaceMetrics.computeFloat(0.8F) * var14;
      Render.ringRenderer.handleRgb(var32, var20, var21, var22 / 2.0F, InterfaceMetrics.computeColor3((int)((float)var8 * (0.16F + 0.1F * var14))));
      Render.ringRenderer.handleRgb(var32, var20, var21, var23 / 2.0F, InterfaceMetrics.computeColor3((int)((float)var8 * (0.38F + 0.12F * var14))));
      Color var25 = InterfaceMetrics.computeColor3(var8);
      double var26 = 0.299 * (double)var25.getRed() + 0.587 * (double)var25.getGreen() + 0.114 * (double)var25.getBlue();
      boolean var28 = var26 > 200.0;
      Color var29 = var28 ? new Color(25, 25, 32, var8) : new Color(255, 255, 255, var8);
      Render.ringRenderer.handleRgb(var32, var20, var21, var24 / 2.0F, var29);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0 && Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight())) {
         this.flag = true;
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
      this.flag = false;
   }

   @Override
   public float getFloat() {
      return 24.0F;
   }

   public String computeEquals(float value) {
      float var2 = this.setting.getStep();
      int var3 = !(var2 >= 1.0F) && !(var2 <= 0.0F) ? (var2 >= 0.1F ? 1 : (var2 >= 0.01F ? 2 : (var2 >= 0.001F ? 3 : (var2 >= 1.0E-4F ? 4 : 5)))) : 0;
      String var4 = String.format(Locale.US, "%." + var3 + "f", value);
      if (var4.equals("-0") || var4.equals("-0.0") || var4.equals("-0.00") || var4.equals("-0.000") || var4.equals("-0.0000")) {
         var4 = "0";
      }

      return var4;
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double h, double v) {
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }
}
