package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class CoordinatesHud extends HudElement {
   public static final int intVal = -10193781;
   public static final Color color = new Color(150, 150, 150);

   public CoordinatesHud() {
      super(30.0F, 120.0F);
   }

   @Override
   public String getName() {
      return "XYZ";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack matrixStack) {
      if (mc.player != null) {
         this.handleRound(matrixStack);
      }
   }

   public void handleRound(MatrixStack matrixStack) {
      float var2 = this.getAutoHelper().getX();
      float var3 = this.getAutoHelper().getY();
      String var4 = Integer.toString((int)Math.round(mc.player.getX()));
      String var5 = Integer.toString((int)Math.round(mc.player.getY()));
      String var6 = Integer.toString((int)Math.round(mc.player.getZ()));
      float var7 = this.computeFloat(6.0F);
      float var8 = this.computeFloat(7.0F);
      float var9 = this.computeFloat(6.0F);
      float var10 = this.computeFloat(3.0F);
      float var11 = this.computeFloat(4.5F);
      float var12 = this.getMkadw().computeFloat("X: ", var7);
      float var13 = this.getMkadw().computeFloat("Y: ", var7);
      float var14 = this.getMkadw().computeFloat("Z: ", var7);
      float var15 = this.getMheuy().computeFloat(var4, var7);
      float var16 = this.getMheuy().computeFloat(var5, var7);
      float var17 = this.getMheuy().computeFloat(var6, var7);
      float var18 = var12 + var15;
      float var19 = var13 + var16;
      float var20 = var14 + var17;
      float var21 = var9 + var8 + var10 + var18 + var11 + var19 + var11 + var20 + var9;
      float var22 = var7 + this.computeFloat(7.0F);
      this.getAutoHelper().setWidth(var21);
      this.getAutoHelper().setHeight(var22);
      Color var23 = this.initInit();
      float var24 = this.getHeight();
      if (this.getAutoHelper().isBlur()) {
         Render.shaderProgram.handleRgb(matrixStack, var2, var3, var21, var22, var24, var23);
      } else {
         Render.rectRenderer.handleRgb(matrixStack, var2, var3, var21, var22, var24, var23);
      }

      this.runContent();
      if (Interface.getInstance().isEnabled() && Interface.check2()) {
         Interface.handleVal(matrixStack, var2, var3, var21, var22, var24, 1.0F, Interface.check4());
      }

      boolean var25 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      int var26 = var25 ? -10193781 : color.getRGB();
      Color var27 = this.getColor();
      float var28 = var3 + var22 / 2.0F;
      float var29 = var28 - var7 / 2.0F - this.computeFloat(0.2F);
      float var30 = var28 - var8 / 2.0F - this.computeFloat(0.6F);
      float var31 = var2 + var9;
      this.getValue2().handleColor(matrixStack, "\\", var31, var30, var8, var27);
      float var32;
      this.getMkadw().handleVal(matrixStack, "X: ", var32 = var31 + var8 + var10, var29, var7, var26);
      this.getMheuy().handleColor(matrixStack, var4, var31 = var32 + var12, var29, var7, this.getColor());
      float var34;
      this.getMkadw().handleVal(matrixStack, "Y: ", var34 = var31 + var15 + var11, var29, var7, var26);
      this.getMheuy().handleColor(matrixStack, var5, var31 = var34 + var13, var29, var7, this.getColor());
      float var36;
      this.getMkadw().handleVal(matrixStack, "Z: ", var36 = var31 + var16 + var11, var29, var7, var26);
      this.getMheuy().handleColor(matrixStack, var6, var31 = var36 + var14, var29, var7, this.getColor());
   }
}
