package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.util.Arrays;
import java.util.Locale;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class BpsHud extends HudElement {
   public static final int mtodpeb = -10193781;
   public static final Color color = new Color(120, 120, 120);
   public float floatVal = 0.0F;
   public final float[] floats = new float[30];
   public int intVal = 0;
   public int int2 = 0;
   public long timeStamp = 0L;

   public BpsHud() {
      super(30.0F, 140.0F);
   }

   @Override
   public String getName() {
      return "BPS";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   public boolean checkGraph() {
      return this.getAutoHelper().getGraphMode();
   }

   public void handleGraph(boolean graphMode) {
      this.getAutoHelper().setGraphMode(graphMode);
   }

   @Override
   public void render(MatrixStack matrixStack) {
      if (mc.player != null) {
         this.handleBlur(matrixStack);
      }
   }

   public void handleDegrees(MatrixStack matrixStack, float x1, float y1, float x2, float y2, float thickness, int color) {
      float var8 = x2 - x1;
      float var9 = y2 - y1;
      float var10 = (float)Math.hypot((double)var8, (double)var9);
      if (!(var10 < 0.05F)) {
         float var11 = (float)Math.toDegrees(Math.atan2((double)var9, (double)var8));
         matrixStack.push();
         matrixStack.translate(x1, y1, 0.0F);
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var11));
         Render.rectRenderer.handleVal(matrixStack, 0.0F, -thickness / 2.0F, var10, thickness, thickness / 2.0F, color);
         matrixStack.pop();
      }
   }

   public void handleBlur(MatrixStack matrixStack) {
      float var2 = this.getAutoHelper().getX();
      float var3 = this.getAutoHelper().getY();
      float var4 = (float)EntityHelper.computeDouble(mc.player);
      this.floatVal = EntityHelper.computeFloat(this.floatVal, var4, 0.15F);
      long var5 = System.currentTimeMillis();
      if (this.timeStamp == 0L) {
         this.timeStamp = var5;
         Arrays.fill(this.floats, this.floatVal);
         this.int2 = this.floats.length;
      } else if (var5 - this.timeStamp >= 50L) {
         this.timeStamp = var5;
         this.floats[this.intVal] = this.floatVal;
         this.intVal = (this.intVal + 1) % this.floats.length;
         if (this.int2 < this.floats.length) {
            this.int2++;
         }
      }

      boolean var7 = this.checkGraph();
      boolean var8 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      Color var9 = this.initInit();
      float var10 = this.getHeight();
      if (var7) {
         float var11 = this.computeFloat(72.0F);
         float var12 = this.computeFloat(34.0F);
         this.getAutoHelper().setWidth(var11);
         this.getAutoHelper().setHeight(var12);
         if (this.getAutoHelper().isBlur()) {
            Render.shaderProgram.handleRgb(matrixStack, var2, var3, var11, var12, var10, var9);
         } else {
            Render.rectRenderer.handleRgb(matrixStack, var2, var3, var11, var12, var10, var9);
         }

         this.runContent();
         if (Interface.getInstance().isEnabled() && Interface.check2()) {
            Interface.handleVal(matrixStack, var2, var3, var11, var12, var10, 1.0F, Interface.check4());
         }

         int var13 = var8 ? -15788246 : -460036;
         int var14 = var8 ? -14800581 : -1906448;
         int var15 = var8 ? -10193781 : -7035976;
         int var16 = this.getColor().getRGB();
         float var17 = this.computeFloat(6.0F);
         float var18 = this.computeFloat(5.0F);
         float var19 = this.computeFloat(6.5F);
         this.getMheuy().handleVal(matrixStack, "Speed", var2 + var17, var3 + var18, var19, var13);
         float var20 = this.computeFloat(7.0F);
         float var21 = this.getValue2().computeFloat("_", var20);
         float var22 = var2 + var11 - var17 - var21;
         this.getValue2().handleVal(matrixStack, "_", var22, var3 + var18 - this.computeFloat(0.5F), var20, var16);
         float var23 = var11 - var17 * 2.0F;
         float var24 = var3 + this.computeFloat(18.5F);
         float var25 = this.computeFloat(8.5F);
         float var26 = 15.0F;

         for (int var27 = 0; var27 < this.int2; var27++) {
            float var28 = this.floats[var27];
            if (var28 > var26) {
               var26 = var28;
            }
         }

         int var57 = this.int2;
         if (var57 > 1) {
            float var58 = var23 / (float)(var57 - 1);
            int var29 = (this.intVal - var57 + this.floats.length) % this.floats.length;
            float var30 = 0.0F;
            float var31 = 0.0F;
            float var32 = this.computeFloat(1.1F);
            int var33 = var16 & 16777215 | -872415232;

            for (int var34 = 0; var34 < var57; var34++) {
               int var35 = (var29 + var34) % this.floats.length;
               float var36 = this.floats[var35];
               float var37 = Math.max(0.0F, Math.min(var36 / var26, 1.0F));
               float var38 = var2 + var17 + (float)var34 * var58;
               float var39 = var24 + var25 * 0.4F - var37 * var25;
               if (var34 > 0) {
                  this.handleDegrees(matrixStack, var30, var31, var38, var39, var32, var33);
               }

               var30 = var38;
               var31 = var39;
            }
         }

         float var59 = this.computeFloat(5.5F);
         float var60 = var3 + this.computeFloat(24.5F);
         String var61 = String.format(Locale.US, "%.2f", this.floatVal);
         this.getMheuy().handleVal(matrixStack, var61, var2 + var17, var60, var59, var14);
         float var62 = this.getMheuy().computeFloat(var61, var59);
         this.getMkadw().handleVal(matrixStack, " bps", var2 + var17 + var62, var60, var59, var15);
      } else {
         float var40 = this.computeFloat(6.0F);
         float var41 = this.computeFloat(7.0F);
         float var42 = this.computeFloat(6.0F);
         float var43 = this.computeFloat(3.0F);
         String var44 = "BPS: ";
         String var45 = String.format(Locale.US, "%.1f", this.floatVal);
         float var46 = this.getMkadw().computeFloat(var44, var40);
         float var47 = this.getMheuy().computeFloat(var45, var40);
         float var48 = var46 + var47;
         float var49 = var42 + var41 + var43 + var48 + var42;
         float var50 = var40 + this.computeFloat(7.0F);
         this.getAutoHelper().setWidth(var49);
         this.getAutoHelper().setHeight(var50);
         if (this.getAutoHelper().isBlur()) {
            Render.shaderProgram.handleRgb(matrixStack, var2, var3, var49, var50, var10, var9);
         } else {
            Render.rectRenderer.handleRgb(matrixStack, var2, var3, var49, var50, var10, var9);
         }

         this.runContent();
         if (Interface.getInstance().isEnabled() && Interface.check2()) {
            Interface.handleVal(matrixStack, var2, var3, var49, var50, var10, 1.0F, Interface.check4());
         }

         int var51 = var8 ? -10193781 : color.getRGB();
         float var52 = var2 + var42;
         float var54 = var3 + var50 / 2.0F;
         float var55 = var54 - var40 / 2.0F - this.computeFloat(0.8F);
         float var56 = var54 - var41 / 2.0F - this.computeFloat(1.2F);
         this.getValue2().handleColor(matrixStack, "_", var52, var56, var41, this.getColor());
         float var53;
         this.getMkadw().handleVal(matrixStack, var44, var53 = var52 + var41 + var43, var55, var40, var51);
         this.getMheuy().handleColor(matrixStack, var45, var53 + var46, var55, var40, this.getColor());
      }
   }
}
