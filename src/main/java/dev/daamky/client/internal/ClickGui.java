package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.PVEScreen;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.render.Interface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGui extends Widget {
   public final ModuleCategory tag;
   public final List<Binding> list = new ArrayList<>();
   public double doubleVal = 0.0;
   public float floatVal = 0.0F;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public final AnimatedValue animatedValue4 = new AnimatedValue();
   public boolean pveMode = false;
   public final AnimatedValue animatedValue5 = new AnimatedValue();

   public ClickGui(ModuleCategory tag) {
      this.tag = tag;

      for (Module var3 : ModuleRegistry.getInstance().getList()) {
         if (var3.getCategory() == tag
            && !var3.getName().equalsIgnoreCase("Click GUI")
            && !var3.getName().equalsIgnoreCase("Configs")
            && !var3.getName().equalsIgnoreCase("IRC Chat")) {
            this.list.add(new Binding(var3));
         }
      }
   }

   public void handleNext(DrawContext context, int mouseX, int mouseY, float delta) {
      this.animatedValue.update();
      this.animatedValue.animateTo(this.doubleVal, 30L, Easing.LINEAR);
      MatrixStack var5 = context.getMatrices();
      int var6 = (int)(this.clamp() * 255.0F);
      float var7 = InterfaceMetrics.computeFloat(10.0F);
      Render.shaderProgram
         .handleRgb(var5, this.getX(), this.getY(), this.getWidth(), this.getHeight(), var7, InterfaceMetrics.computeColor(var6), Interface.getDay3());
      if (Interface.getInstance().isEnabled() && Interface.check3()) {
         Interface.handleVal(var5, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.clamp(), Interface.check4());
      }

      float var8 = InterfaceMetrics.computeFloat(8.0F);
      float var9 = this.getY() + this.getMics();
      Render.rectRenderer.handleRgb(var5, this.getX() + var8, var9 - 1.0F, this.getWidth() - var8 * 2.0F, 1.0F, 0.0F, InterfaceMetrics.initInit(var6));
      float var10 = InterfaceMetrics.computeFloat(8.0F);
      float var11 = InterfaceMetrics.computeFloat(7.0F);
      float var12 = this.getX() + var8;
      float var13 = InterfaceMetrics.computeFloat(28.0F);
      float var14 = this.getY() + var13 / 2.0F;
      FontBootstrap.clientFont15
         .handleColor(
            var5, this.tag.getIcon(), var12, var14 - var10 / 2.0F - InterfaceMetrics.computeFloat(0.8F), var10, InterfaceMetrics.initBlue(Color.WHITE, var6)
         );
      float var15 = var12 + var10 + InterfaceMetrics.computeFloat(4.0F);
      FontBootstrap.mdoswa.handleColor(var5, this.tag.getLabel(), var15, var14 - var11 / 2.0F, var11, InterfaceMetrics.computeColor2(var6));
      FontBootstrap.clientFont15
         .handleColor(
            var5,
            "K",
            this.getX() + this.getWidth() - var8 - var10,
            var14 - var10 / 2.0F - InterfaceMetrics.computeFloat(0.8F),
            var10,
            InterfaceMetrics.initBlue(Color.WHITE, var6)
         );
      if (this.tag == ModuleCategory.MICS) {
         this.animatedValue5.update();
         this.animatedValue5.animateTo(this.pveMode ? 1.0 : 0.0, 150L, Easing.QUINT_OUT);
         float var16 = (float)this.animatedValue5.getValue();
         float var17 = this.getWidth() - var8 * 2.0F;
         float var18 = InterfaceMetrics.computeFloat(14.0F);
         float var19 = this.getX() + var8;
         float var20 = this.getY() + var13;
         Render.rectRenderer
            .handleRgb(var5, var19, var20, var17, var18, InterfaceMetrics.computeFloat(3.0F), InterfaceMetrics.computeColor((int)((float)var6 * 0.5F)));
         float var21 = var17 / 2.0F - InterfaceMetrics.computeFloat(1.0F);
         float var22 = var19 + InterfaceMetrics.computeFloat(0.5F) + var16 * (var17 / 2.0F);
         Render.rectRenderer
            .handleRgb(
               var5,
               var22,
               var20 + InterfaceMetrics.computeFloat(0.5F),
               var21,
               var18 - InterfaceMetrics.computeFloat(1.0F),
               InterfaceMetrics.computeFloat(2.5F),
               InterfaceMetrics.computeColor3(var6)
            );
         float var23 = var20 + var18 / 2.0F - InterfaceMetrics.computeFloat(6.0F) / 2.0F;
         Color var24 = InterfaceMetrics.initInit(InterfaceMetrics.computeColor3(255), var6);
         Color var25 = InterfaceMetrics.computeColor4(var6);
         Color var26 = ColorMath.initBlue(var25, var24, var16);
         Color var27 = ColorMath.initBlue(var24, var25, var16);
         FontBootstrap.clientFont10.handleColor2(var5, "Other", var19 + var17 / 4.0F, var23, InterfaceMetrics.computeFloat(6.0F), var26);
         FontBootstrap.clientFont10.handleColor2(var5, "PvE", var19 + var17 * 0.75F, var23, InterfaceMetrics.computeFloat(6.0F), var27);
      }

      context.enableScissor((int)this.getX(), (int)var9, (int)(this.getX() + this.getWidth()), (int)(var9 + this.getHeight() - this.getMics()));
      String var28 = PVEScreen.getInstance().getString();
      if (var28.isEmpty()) {
         String var10000 = "";
      } else {
         var28.toLowerCase();
      }

      float var29 = var9 + var8 + (float)this.animatedValue.getValue();
      float var30 = var8;
      Binding var31 = null;
      String var32 = this.pveMode ? "PVE" : "PVP";

      for (Binding var35 : this.list) {
         if (this.tag != ModuleCategory.MICS || var35.getFeature().getSubCategory().equalsIgnoreCase(var32)) {
            var35.setAlpha(this.clamp());
            var35.setX(this.getX());
            var35.setY(var29);
            var35.setWidth(this.getWidth());
            var35.runFeature();
            if (Geometry.computeFlag((float)mouseX, (float)mouseY, var35.getX(), var35.getY(), var35.getWidth(), var35.getFloat())) {
               var31 = var35;
            }

            var29 += var35.getHeight();
            var30 += var35.getHeight();
         }
      }

      if (var31 != null) {
         PVEScreen.getInstance().setDescription(var31.getFeature().getDescription());
      }

      this.handleOut(var5, var31, var6);
      float var34 = var9;
      float var36 = this.getY() + this.getHeight();

      for (Binding var38 : this.list) {
         if ((this.tag != ModuleCategory.MICS || var38.getFeature().getSubCategory().equalsIgnoreCase(var32))
            && !(var38.getY() + var38.getHeight() < var34)
            && !(var38.getY() > var36)) {
            var38.initInit(context, mouseX, mouseY, delta);
         }
      }

      context.disableScissor();
      this.floatVal = Math.max(0.0F, var30 - (this.getHeight() - this.getMics() - var8));
      if (this.doubleVal < (double)(-this.floatVal)) {
         this.doubleVal = (double)(-this.floatVal);
      }

      if (this.doubleVal > 0.0) {
         this.doubleVal = 0.0;
      }
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      this.list.forEach(ClickGui::lambda$keyPressed$0);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      String var6 = PVEScreen.getInstance().getString();
      String var7 = var6.isEmpty() ? "" : var6.toLowerCase();
      if (this.tag == ModuleCategory.MICS) {
         float var9 = InterfaceMetrics.computeFloat(8.0F);
         float var10 = InterfaceMetrics.computeFloat(28.0F);
         float var11 = this.getWidth() - var9 * 2.0F;
         float var12 = InterfaceMetrics.computeFloat(14.0F);
         float var13 = this.getX() + var9;
         float var14 = this.getY() + var10;
         if (mouseX >= (double)var13 && mouseX <= (double)(var13 + var11) && mouseY >= (double)var14 && mouseY <= (double)(var14 + var12)) {
            this.pveMode = !this.pveMode;
            this.doubleVal = 0.0;
            this.animatedValue.handleStart(0.0);
            return;
         }
      }

      if (this.computeFlag(mouseX, mouseY)) {
         String var15 = this.pveMode ? "PVE" : "PVP";

         for (Binding var17 : this.list) {
            if ((this.tag != ModuleCategory.MICS || var17.getFeature().getSubCategory().equalsIgnoreCase(var15))
               && Geometry.computeFlag(mouseX, mouseY, (double)var17.getX(), (double)var17.getY(), (double)var17.getWidth(), (double)var17.getHeight())) {
               var17.mouseClicked(mouseX, mouseY, button);
               return;
            }
         }
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
      this.list.forEach(ClickGui::lambda$mouseReleased$1);
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight())) {
         this.doubleVal += verticalAmount * 25.0;
         if (this.doubleVal < (double)(-this.floatVal)) {
            this.doubleVal = (double)(-this.floatVal);
         }

         if (this.doubleVal > 0.0) {
            this.doubleVal = 0.0;
         }
      }
   }

   public float getMics() {
      float var1 = InterfaceMetrics.computeFloat(28.0F);
      if (this.tag == ModuleCategory.MICS) {
         var1 += InterfaceMetrics.computeFloat(20.0F);
      }

      return var1;
   }

   public void handleMax(Binding module) {
      float var2 = InterfaceMetrics.computeFloat(8.0F);
      float var3 = this.getY() + this.getMics() + var2;
      float var4 = this.getY() + this.getHeight() - var2;
      float var5 = (float)(this.doubleVal - this.animatedValue.getValue());
      float var6 = module.getY() + var5;
      float var7 = var6 + module.getFeature();
      float var8 = var2;

      for (Binding var10 : this.list) {
         var8 += var10 == module ? var10.getFeature() : var10.getHeight();
      }

      float var11 = Math.max(0.0F, var8 - (this.getHeight() - this.getMics() - var2));
      if (var7 > var4) {
         this.doubleVal = this.doubleVal - (double)(var7 - var4 + InterfaceMetrics.computeFloat(2.0F));
      } else if (var6 < var3) {
         this.doubleVal = this.doubleVal + (double)(var3 - var6 + InterfaceMetrics.computeFloat(2.0F));
      }

      this.floatVal = Math.max(this.floatVal, var11);
      if (this.doubleVal < (double)(-this.floatVal)) {
         this.doubleVal = (double)(-this.floatVal);
      }

      if (this.doubleVal > 0.0) {
         this.doubleVal = 0.0;
      }

      this.animatedValue.handleStart(this.doubleVal);
   }

   public boolean computeFlag(double mouseX, double mouseY) {
      return Geometry.computeFlag(
         mouseX, mouseY, (double)this.getX(), (double)(this.getY() + this.getMics()), (double)this.getWidth(), (double)(this.getHeight() - this.getMics())
      );
   }

   public void handleOut(MatrixStack matrixStack, Binding hoveredModule, int alpha) {
      this.animatedValue2.update();
      this.animatedValue3.update();
      this.animatedValue4.update();
      if (hoveredModule != null) {
         if (this.animatedValue4.getValue() < 0.01) {
            this.animatedValue2.handleStart((double)hoveredModule.getY());
            this.animatedValue3.handleStart((double)hoveredModule.getFloat());
         }

         this.animatedValue2.animateTo(hoveredModule.getY(), 115L, Easing.QUINT_OUT);
         this.animatedValue3.animateTo(hoveredModule.getFloat(), 115L, Easing.QUINT_OUT);
         this.animatedValue4.animateTo(1.0, 95L, Easing.SINE_OUT);
      } else {
         this.animatedValue4.animateTo(0.0, 120L, Easing.SINE_OUT);
      }

      float var4 = (float)this.animatedValue4.getValue();
      if (!(var4 <= 0.01F)) {
         float var5 = InterfaceMetrics.computeFloat(8.0F);
         float var6 = InterfaceMetrics.computeFloat(1.4F);
         float var7 = this.getX();
         float var8 = (float)this.animatedValue2.getValue() + var6;
         float var9 = this.getWidth();
         float var10 = Math.max(1.0F, (float)this.animatedValue3.getValue() - var6 * 2.0F);
         float var11 = InterfaceMetrics.computeFloat(4.0F);
         Render.shaderProgram.handleRgb(matrixStack, var7, var8, var9, var10, var11, InterfaceMetrics.computeColor((int)((float)alpha * 0.32F * var4)), 0.18F);
         Render.rectRenderer.handleRgb(matrixStack, var7, var8, var9, var10, var11, InterfaceMetrics.computeColor3((int)((float)alpha * 0.07F * var4)));
      }
   }

   public ModuleCategory getTag() {
      return this.tag;
   }

   public List<Binding> getList() {
      return this.list;
   }

   public double getDouble() {
      return this.doubleVal;
   }

   @Override
   public float getFloat() {
      return this.floatVal;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public AnimatedValue getAnimatedValue2() {
      return this.animatedValue2;
   }

   public AnimatedValue getAnimatedValue3() {
      return this.animatedValue3;
   }

   public AnimatedValue getAnimatedValue4() {
      return this.animatedValue4;
   }

   public boolean isPveMode() {
      return this.pveMode;
   }

   public AnimatedValue getAnimatedValue5() {
      return this.animatedValue5;
   }

   public void setPveMode(boolean pveMode) {
      this.pveMode = pveMode;
   }

   public static void lambda$mouseReleased$1(double mouseX, double mouseY, int button, Binding module) {
      module.mouseReleased(mouseX, mouseY, button);
   }

   public static void lambda$keyPressed$0(int keyCode, int scanCode, int modifiers, Binding module) {
      module.keyPressed(keyCode, scanCode, modifiers);
   }
}
