package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.misc.Optimization;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class FpsHud extends HudElement {
   public static final int intVal = -6908266;
   public static final int int2 = -10158236;
   public final int[] mgasaq = new int[20];
   public int int3 = 0;
   public int int4 = 0;
   public int int5 = -1;
   public String string = "0";

   public FpsHud() {
      super(50.0F, 100.0F);
   }

   @Override
   public String getName() {
      return "FPS";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack matrixStack) {
      float var2 = this.getAutoHelper().getX();
      float var3 = this.getAutoHelper().getY();
      int var4 = Optimization.getInstance().computeEnabled(mc.getCurrentFps());
      this.mgasaq[this.int3] = var4;
      this.int3 = (this.int3 + 1) % this.mgasaq.length;
      if (this.int4 < this.mgasaq.length) {
         this.int4++;
      }

      if (var4 != this.int5) {
         this.int5 = var4;
         this.string = Integer.toString(var4);
      }

      float var5 = this.computeFloat(6.5F);
      float var6 = this.computeFloat(7.0F);
      float var7 = this.computeFloat(6.0F);
      float var8 = this.computeFloat(30.0F);
      String var9 = this.string;
      String var10 = "FPS";
      float var11 = this.getMkadw().computeFloat(var10, var5);
      float var12 = this.getValue2().computeFloat("]", var6)
         + this.computeFloat(4.0F)
         + var11
         + this.computeFloat(4.0F)
         + this.getMheuy().computeFloat(var9, var5);
      float var13 = var7 * 2.0F + var12 + this.computeFloat(8.0F) + var8;
      float var14 = var5 + this.computeFloat(9.0F);
      this.getAutoHelper().setWidth(var13);
      this.getAutoHelper().setHeight(var14);
      Color var15 = this.initInit();
      float var16 = this.getHeight();
      if (this.getAutoHelper().isBlur()) {
         Render.shaderProgram.handleVal(matrixStack, var2, var3, var13, var14, var16, var15.getRGB());
      } else {
         Render.rectRenderer.handleVal(matrixStack, var2, var3, var13, var14, var16, var15.getRGB());
      }

      this.runContent();
      if (Interface.getInstance().isEnabled() && Interface.check2()) {
         Interface.handleVal(matrixStack, var2, var3, var13, var14, this.getHeight(), 1.0F, Interface.check4());
      }

      boolean var17 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      int var18 = var17 ? -10193781 : -6908266;
      float var19 = var3 + var14 / 2.0F;
      float var20 = var2 + var7;
      this.getValue2().handleVal(matrixStack, "]", var20, var19 - var6 / 2.0F, var6, this.getColor().getRGB());
      float var30;
      this.getMkadw()
         .handleVal(matrixStack, var10, var30 = var20 + this.getValue2().computeFloat("]", var6) + this.computeFloat(4.0F), var19 - var5 / 2.0F, var5, var18);
      this.getMheuy().handleVal(matrixStack, var9, var30 + var11 + this.computeFloat(4.0F), var19 - var5 / 2.0F, var5, this.getColor().getRGB());
      float var21 = var2 + var13 - var7 - var8;
      float var22 = this.computeFloat(8.0F);
      float var23 = var8 / 20.0F;
      int var24 = mc.getWindow().getRefreshRate();
      if (var24 > 240 || var24 <= 0) {
         var24 = 240;
      }

      int var25 = this.int4 == this.mgasaq.length ? this.int3 : 0;

      for (int var26 = 0; var26 < this.int4; var26++) {
         int var27 = this.mgasaq[(var25 + var26) % this.mgasaq.length];
         float var28 = Math.min((float)var27 / (float)var24, 1.0F);
         float var29 = Math.max(var28 * var22, 1.0F);
         Render.rectRenderer.handleVal(matrixStack, var21 + (float)var26 * var23, var19 + var22 / 2.0F - var29, var23 - 0.5F, var29, 0.0F, -10158236);
      }
   }
}
