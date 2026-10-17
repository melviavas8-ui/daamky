package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class BooleanValue extends SettingRow {
   public final BooleanSetting setting;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();

   public BooleanValue(BooleanSetting setting) {
      super(setting);
      this.setting = setting;
      this.animatedValue.handleStart(setting.getValue() ? 1.0 : 0.0);
      this.handleFloat(this.getFloat());
   }

   public BooleanValue(BooleanSetting setting, boolean inMenu) {
      this(setting);
   }

   public void initInit(DrawContext context, int mouseX, int mouseY, float delta) {
      this.handleFloat(this.getFloat());
      MatrixStack var5 = context.getMatrices();
      this.animatedValue2.update();
      boolean var6 = Geometry.computeFlag(
         (double)mouseX, (double)mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight()
      );
      this.animatedValue2.animateTo(var6 ? 1.0 : 0.0, 180L, Easing.QUINT_OUT);
      float var7 = (float)this.animatedValue2.getValue();
      this.animatedValue.update();
      this.animatedValue.animateTo(this.setting.getValue() ? 1.0 : 0.0, 220L, Easing.QUINT_OUT);
      float var8 = (float)this.animatedValue.getValue();
      int var9 = (int)(this.clamp() * 255.0F);
      float var10 = InterfaceMetrics.computeFloat(6.4F);
      Color var11 = ColorMath.initBlue(InterfaceMetrics.computeColor2(var9), InterfaceMetrics.computeColor4(var9), var8);
      float var12 = this.getX();
      BooleanSetting var13 = this.setting;
      if (var13 instanceof Class_1792Holder && this.clamp() >= 0.2F) {
         Class_1792Holder var14 = (Class_1792Holder)var13;
         if (var14.getIcon() != null) {
            var5.push();
            var5.translate(var12, this.getY() + this.getHeight() / 2.0F - 6.0F, 0.0F);
            var5.scale(0.75F, 0.75F, 1.0F);
            context.drawItem(var14.getIcon().getDefaultStack(), 0, 0);
            var5.pop();
            var12 += 14.0F;
         }
      } else if (var13 instanceof Class_1792Holder) {
         var12 += 14.0F;
      }

      FontBootstrap.clientFont10
         .handleColor(var5, EngHelper.computeString(this.setting.getName()), var12, this.getY() + this.getHeight() / 2.0F - var10 / 2.0F, var10, var11);
      float var35 = InterfaceMetrics.computeFloat(15.0F);
      float var15 = InterfaceMetrics.computeFloat(8.5F);
      float var16 = InterfaceMetrics.computeFloat(3.0F);
      float var17 = this.getX() + this.getWidth() - var35 - var16;
      float var18 = this.getY() + this.getHeight() / 2.0F - var15 / 2.0F;
      boolean var19 = ThemeManager.getInstance().getIsDayMode();
      Color var20 = InterfaceMetrics.computeColor3(var9);
      double var21 = 0.299 * (double)var20.getRed() + 0.587 * (double)var20.getGreen() + 0.114 * (double)var20.getBlue();
      boolean var23 = var21 > 200.0;
      Color var25 = var19 ? new Color(185, 185, 195, var9) : new Color(28, 28, 36, var9);
      Color var26 = ColorMath.initBlue(var20, var25, var8);
      Render.rectRenderer.handleRgb(var5, var17, var18, var35, var15, var15 / 2.0F, var26);
      float var27 = var15 - InterfaceMetrics.computeFloat(2.0F);
      float var28 = var17 + InterfaceMetrics.computeFloat(1.0F);
      float var29 = var17 + var35 - var27 - InterfaceMetrics.computeFloat(1.0F);
      float var30 = var28 + var8 * (var29 - var28);
      float var31 = var18 + InterfaceMetrics.computeFloat(1.0F);
      Color var32 = var23 ? new Color(25, 25, 32, var9) : new Color(255, 255, 255, var9);
      Color var33 = var19 ? new Color(110, 110, 120, var9) : new Color(130, 130, 145, var9);
      Color var34 = ColorMath.initBlue(var32, var33, var8);
      Render.ringRenderer.handleRgb(var5, var30 + var27 / 2.0F, var31 + var27 / 2.0F, var27 / 2.0F, var34);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight())) {
         this.setting.toggle();
      }
   }

   @Override
   public float getFloat() {
      return 18.5F;
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
