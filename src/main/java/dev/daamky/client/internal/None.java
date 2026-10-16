package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.util.KeyNames;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class None extends SettingRow {
   public final IntegerSetting setting;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public boolean flag;

   public None(IntegerSetting setting) {
      super(settingx);
      this.setting = settingx;
      this.handleFloat(this.getFloat());
   }

   public void handleSetting(DrawContext context, int mouseX, int mouseY, float delta) {
      this.handleFloat(this.getFloat());
      MatrixStack var5 = context.getMatrices();
      this.animatedValue.update();
      this.animatedValue.animateTo(this.flag ? 1.0 : 0.0, 300L, Easing.QUINT_OUT);
      float var6 = InterfaceMetrics.computeFloat(6.4F);
      int var7 = (int)(this.clamp() * 255.0F);
      ClientFont var8 = FontBootstrap.clientFont10;
      var8.handleColor(
         var5,
         EngHelper.computeString(this.setting.getName()),
         this.getX(),
         this.getY() + this.getHeight() / 2.0F - var6 / 2.0F,
         var6,
         InterfaceMetrics.computeColor4(var7)
      );
      float var9 = (float)this.animatedValue.getValue();
      float var10 = 1.0F - var9;
      String var11 = this.setting.getValue() == -999 ? "None" : KeyNames.computeNone(this.setting.getValue());
      String var12 = "Press...";
      float var13 = var8.computeFloat(var11, var6);
      float var14 = var8.computeFloat(var12, var6);
      float var15 = var13 * var10 + var14 * var9;
      float var16 = InterfaceMetrics.computeFloat(4.0F);
      float var17 = InterfaceMetrics.computeFloat(14.0F);
      float var18 = var15 + var16 * 2.0F;
      float var19 = this.getX() + this.getWidth() - var18 - InterfaceMetrics.computeFloat(2.0F);
      float var20 = this.getY() + this.getHeight() / 2.0F - var17 / 2.0F;
      float var21 = InterfaceMetrics.computeFloat(3.0F);
      Color var22 = this.flag ? InterfaceMetrics.computeColor8((int)((float)var7 * 1.5F)) : InterfaceMetrics.computeColor8(var7);
      Render.rectRenderer.handleRgb(var5, var19, var20, var18, var17, var21, var22);
      if (var10 > 0.0F) {
         var8.handleColor(var5, var11, var19 + var16, var20 + var17 / 2.0F - var6 / 2.0F, var6, InterfaceMetrics.computeColor3((int)(var10 * (float)var7)));
      }

      if (var9 > 0.0F) {
         var8.handleColor(var5, var12, var19 + var16, var20 + var17 / 2.0F - var6 / 2.0F, var6, InterfaceMetrics.computeColor3((int)(var9 * (float)var7)));
      }
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.flag) {
         this.setting.setValue(keyCode != 261 && keyCode != 256 ? keyCode : -999);
         this.flag = false;
      }
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.flag && button != 1 && button != 0) {
         this.setting.setValue(-100 + button);
         this.flag = false;
      } else {
         if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight())) {
            this.flag = !this.flag;
         }
      }
   }

   @Override
   public float getFloat() {
      return 18.5F;
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double h, double v) {
   }
}
