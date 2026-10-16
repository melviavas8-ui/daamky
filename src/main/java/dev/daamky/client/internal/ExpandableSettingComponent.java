package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ExpandableSettingComponent extends ExpandableSettingComponent3.ExpandableSettingComponent {
   public final ColorTheme.ElementColor elementColor;
   public final ColorSetting setting;
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public float floatVal = 0.0F;
   public boolean flag4;

   public ExpandableSettingComponent(ColorSetting setting) {
      super(settingx);
      this.setting = settingx;
      this.elementColor = null;
      this.handleFloat(this.getFloat7());
   }

   public ExpandableSettingComponent(ColorTheme.ElementColor elementColor) {
      super(null);
      this.elementColor = elementColorx;
      this.setting = null;
      this.handleFloat(this.getFloat7());
   }

   public void runBlue() {
      if (!this.flag4) {
         Color var1 = this.getColor();
         float[] var2 = Color.RGBtoHSB(var1.getRed(), var1.getGreen(), var1.getBlue(), null);
         this.floatVal = var2[0];
         this.flag4 = true;
      }
   }

   @Override
   public float computeFloat(float value) {
      return InterfaceMetrics.computeFloat(value);
   }

   public Color getColor() {
      return this.setting != null ? this.setting.getValue() : this.elementColor.getColor();
   }

   public void handleColor(Color color) {
      if (this.setting != null) {
         this.setting.handleAction(color);
      } else {
         this.elementColor.setColor(color);
      }
   }

   public void initSetting(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var6 = context.getMatrices();
      this.run();
      this.runBlue();
      if (this.flag) {
         this.setMouseX((double)mouseX);
      }

      if (this.flag2) {
         this.initBlue((double)mouseX, (double)mouseY);
      }

      if (this.flag3) {
         this.initBlue((double)mouseX);
      }

      float var7 = this.computeFloat(this.getFloat7());
      float var8 = this.computeFloat(6.0F);
      int var9 = (int)(this.clamp() * 255.0F);
      if (this.setting != null) {
         FontBootstrap.clientFont10
            .handleColor(
               var6,
               EngHelper.computeString(this.setting.getName()),
               this.getX(),
               this.getY() + var7 / 2.0F - var8 / 2.0F,
               var8,
               new Color(200, 200, 200, var9)
            );
         float var10 = this.computeFloat(8.0F);
         float var11 = this.getX() + this.getWidth() - var10;
         float var12 = this.getY() + var7 / 2.0F - var10 / 2.0F;
         Render.rectRenderer
            .handleRgb(
               var6,
               var11,
               var12,
               var10,
               var10,
               this.computeFloat(2.0F),
               ColorMath.initBlue(this.getColor(), (int)((float)this.getColor().getAlpha() / 255.0F * (float)var9))
            );
         this.handleFloat(this.getFloat7());
      }

      float var13 = this.getFloat8();
      if ((double)var13 > 0.0) {
         Color[] var14 = this.initBto(var13);
         Render.uSizeHelper
            .handleRgb(
               var6,
               this.getX(),
               this.getSetting() + this.getFloat(),
               this.getWidth(),
               this.getFloat2(),
               this.computeFloat(2.0F),
               var14[0],
               var14[1],
               var14[2],
               var14[3]
            );
         this.initImage(var6, var13);
         this.initBlue(var6, var13);
         this.handleBlue(var6);
         float var15 = (this.getFloat4() + this.getFloat2() + this.getFloat6() + this.getFloat() * 2.0F) * var13;
         this.setHeight((this.setting != null ? var7 : 0.0F) + var15);
      }
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.setting != null
         && Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.computeFloat(this.getFloat7()))
         )
       {
         this.run2();
      } else if (!this.check()) {
         if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getSetting(), (double)this.getWidth(), (double)this.getFloat2())) {
            this.flag2 = true;
            this.initBlue(mouseX, mouseY);
         } else if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getFloat3(), (double)this.getWidth(), (double)this.getFloat4())) {
            this.flag = true;
            this.setMouseX(mouseX);
         } else if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getFloat5(), (double)this.getWidth(), (double)this.getFloat6())) {
            this.flag3 = true;
            this.initBlue(mouseX);
         }
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
      this.flag3 = false;
      this.flag2 = false;
      this.flag = false;
   }

   public void setMouseX(double mouseX) {
      float var3;
      this.floatVal = var3 = Math.max(0.0F, Math.min(1.0F, (float)((mouseX - (double)this.getX()) / (double)this.getWidth())));
      Color var4 = this.getColor();
      float[] var5 = Color.RGBtoHSB(var4.getRed(), var4.getGreen(), var4.getBlue(), null);
      this.handleColor(new Color(Color.HSBtoRGB(var3, var5[1], var5[2]), true));
   }

   public void initBlue(double mouseX, double mouseY) {
      float var5 = Math.max(0.0F, Math.min(1.0F, (float)((mouseX - (double)this.getX()) / (double)this.getWidth())));
      float var6 = Math.max(0.0F, Math.min(1.0F, 1.0F - (float)((mouseY - (double)this.getSetting()) / (double)this.getFloat2())));
      Color var7 = this.getColor();
      float[] var8 = Color.RGBtoHSB(var7.getRed(), var7.getGreen(), var7.getBlue(), null);
      float var9 = var8[1] != 0.0F && var8[2] != 0.0F ? var8[0] : this.floatVal;
      Color var10 = new Color(Color.HSBtoRGB(var9, var5, var6));
      this.handleColor(new Color(var10.getRed(), var10.getGreen(), var10.getBlue(), var7.getAlpha()));
   }

   public void initBlue(double mouseX) {
      float var3 = Math.max(0.0F, Math.min(1.0F, (float)((mouseX - (double)this.getX()) / (double)this.getWidth())));
      Color var4 = this.getColor();
      this.handleColor(new Color(var4.getRed(), var4.getGreen(), var4.getBlue(), (int)(var3 * 255.0F)));
   }

   public void initBlue(MatrixStack ms, float animValue) {
      float var3 = this.getFloat5() + this.getFloat();
      Color var4 = this.getColor();
      Color var5 = new Color(var4.getRed(), var4.getGreen(), var4.getBlue(), 0);
      Color var6 = new Color(var4.getRed(), var4.getGreen(), var4.getBlue(), (int)(this.getFloat8() * this.clamp() * 255.0F));
      Render.uSizeHelper.handleRgb(ms, this.getX(), var3, this.getWidth(), this.getFloat6(), this.computeFloat(1.5F), var5, var6, var5, var6);
   }

   public void initImage(MatrixStack ms, float animValue) {
      float var3 = this.getFloat3() + this.getFloat();
      Render.myazem
         .handleRgb(
            ms,
            this.getX(),
            var3,
            this.getWidth(),
            this.getFloat4(),
            this.computeFloat(1.5F),
            new Color(255, 255, 255, (int)(this.getFloat8() * this.clamp() * 255.0F)),
            0.0F,
            0.0F,
            1.0F,
            1.0F,
            MinecraftAccess.mc.getTextureManager().getTexture(InputHelper.getImage("interface/hue")).getGlId()
         );
   }

   public void handleBlue(MatrixStack ms) {
      int var2 = (int)(this.getFloat8() * this.clamp() * 255.0F);
      Color var3 = this.getColor();
      Color var4 = ColorMath.initBlue(Color.WHITE, var2);
      float[] var5 = Color.RGBtoHSB(var3.getRed(), var3.getGreen(), var3.getBlue(), null);
      float var6 = this.computeFloat(1.5F);
      float var7 = this.getX() + var5[1] * this.getWidth();
      float var8 = this.getSetting() + (1.0F - var5[2]) * this.getFloat2();
      Render.rectRenderer
         .handleRgb(
            ms,
            var7 - this.computeFloat(2.0F),
            var8 + this.getFloat() - this.computeFloat(2.0F),
            this.computeFloat(4.0F),
            this.computeFloat(4.0F),
            this.computeFloat(2.0F),
            var4
         );
      float var9 = this.getX() + this.floatVal * this.getWidth();
      Render.rectRenderer
         .handleRgb(
            ms, var9 - var6 / 2.0F, this.getFloat3() + this.getFloat() - this.computeFloat(1.0F), var6, this.getFloat4() + this.computeFloat(2.0F), 0.0F, var4
         );
      float var10 = this.getX() + (float)var3.getAlpha() / 255.0F * this.getWidth();
      Render.rectRenderer
         .handleRgb(
            ms, var10 - var6 / 2.0F, this.getFloat5() + this.getFloat() - this.computeFloat(1.0F), var6, this.getFloat6() + this.computeFloat(2.0F), 0.0F, var4
         );
   }

   @Override
   public float getFloat() {
      return -this.getFloat() * (1.0F - this.getFloat8());
   }

   public float getSetting() {
      return this.getY() + (this.setting != null ? this.computeFloat(this.getFloat7()) : 0.0F);
   }

   @Override
   public float getFloat2() {
      return this.getWidth() * this.getFloat8() * 0.4F;
   }

   public float getFloat3() {
      return this.getSetting() + this.getFloat2() + this.getFloat();
   }

   public float getFloat4() {
      return this.computeFloat(6.0F) * this.getFloat8();
   }

   public float getFloat5() {
      return this.getFloat3() + this.getFloat4() + this.getFloat();
   }

   public float getFloat6() {
      return this.getFloat4();
   }

   @Override
   public float getX() {
      return this.getX();
   }

   @Override
   public float getWidth() {
      return this.getWidth();
   }

   public float getFloat7() {
      return 14.0F;
   }

   public float getFloat8() {
      return this.getFloat();
   }

   public Color[] initBto(float anim) {
      int var2 = (int)(anim * this.clamp() * 255.0F);
      Color var3 = ColorMath.initBlue(Color.WHITE, var2);
      Color var4 = ColorMath.initBlue(Color.BLACK, var2);
      Color var5 = new Color(Color.HSBtoRGB(this.floatVal, 1.0F, 1.0F));
      Color var6 = ColorMath.initBlue(var5, var2);
      return new Color[]{var3, var6, var4, var4};
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
   }
}
