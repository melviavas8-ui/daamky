package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.minecraft.client.util.math.MatrixStack;

public class ServerInfoHud extends HudElement {
   public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
   public static final int meldut = -10193781;
   public static final int intVal = -6908266;
   public static final int int2 = 587202560;
   public static final int int3 = 603979775;
   public String miepuja = "00:00";
   public long mfocd = 0L;

   public ServerInfoHud() {
      super(250.0F, 5.0F);
   }

   @Override
   public String getName() {
      return "ServerInfo";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack matrixStack) {
      float var2 = this.getAutoHelper().getX();
      float var3 = this.getAutoHelper().getY();
      long var4 = System.currentTimeMillis();
      if (var4 - this.mfocd > 1000L) {
         this.miepuja = LocalTime.now().format(dateTimeFormatter);
         this.mfocd = var4;
      }

      String var6 = this.miepuja;
      String var7 = "20.0";
      String var8 = mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : "Singleplayer";
      String var9 = "time ";
      String var10 = "tps ";
      float var11 = this.computeFloat(5.7F);
      float var12 = this.computeFloat(5.7F);
      float var13 = this.computeFloat(6.5F);
      float var14 = this.computeFloat(5.0F);
      float var15 = this.computeFloat(3.0F);
      float var16 = this.computeFloat(0.5F);
      float var17 = this.computeFloat(4.5F);
      boolean var18 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      int var19 = var18 ? -10193781 : -6908266;
      int var20 = var18 ? 587202560 : 603979775;
      Color var21 = this.getColor();
      float var22 = var13 + var15 + this.getMkadw().computeFloat(var9, var12) + this.getMheuy().computeFloat(var6, var11);
      float var23 = var13 + var15 + this.getMkadw().computeFloat(var10, var12) + this.getMheuy().computeFloat(var7, var11);
      float var24 = var13 + var15 + this.getMkadw().computeFloat(var8, var11);
      float var25 = var14 + var22 + var17 + var16 + var17 + var23 + var17 + var16 + var17 + var24 + var14;
      float var26 = var11 + this.computeFloat(7.0F);
      this.getAutoHelper().setWidth(var25);
      this.getAutoHelper().setHeight(var26);
      Color var27 = this.initInit();
      float var28 = this.getHeight();
      if (this.getAutoHelper().isBlur()) {
         Render.shaderProgram.handleRgb(matrixStack, var2, var3, var25, var26, var28, var27);
      } else {
         Render.rectRenderer.handleRgb(matrixStack, var2, var3, var25, var26, var28, var27);
      }

      this.runContent();
      if (Interface.getInstance().isEnabled() && Interface.check2()) {
         Interface.handleVal(matrixStack, var2, var3, var25, var26, var28, 1.0F, Interface.check4());
      }

      float var29 = var3 + var26 / 2.0F;
      float var30 = var2 + var14;
      float var31 = var29 - var11 / 2.0F - this.computeFloat(0.8F);
      float var32 = var29 - var13 / 2.0F - this.computeFloat(0.45F);
      this.getValue2().handleColor(matrixStack, "G", var30, var32, var13, var21);
      float var33;
      this.getMkadw().handleVal(matrixStack, var9, var33 = var30 + var13 + var15, var29 - var12 / 2.0F - this.computeFloat(0.8F), var12, var19);
      this.getMheuy().handleColor(matrixStack, var6, var30 = var33 + this.getMkadw().computeFloat(var9, var12), var31, var11, this.getColor());
      float var35;
      Render.rectRenderer
         .handleVal(
            matrixStack,
            var35 = var30 + this.getMheuy().computeFloat(var6, var11) + var17,
            var29 - this.computeFloat(3.5F),
            var16,
            this.computeFloat(7.0F),
            0.0F,
            var20
         );
      this.getValue2().handleColor(matrixStack, "h", var30 = var35 + var16 + var17, var32, var13, var21);
      float var37;
      this.getMkadw().handleVal(matrixStack, var10, var37 = var30 + var13 + var15, var29 - var12 / 2.0F - this.computeFloat(0.8F), var12, var19);
      this.getMheuy().handleColor(matrixStack, var7, var30 = var37 + this.getMkadw().computeFloat(var10, var12), var31, var11, this.getColor());
      float var39;
      Render.rectRenderer
         .handleVal(
            matrixStack,
            var39 = var30 + this.getMheuy().computeFloat(var7, var11) + var17,
            var29 - this.computeFloat(3.5F),
            var16,
            this.computeFloat(7.0F),
            0.0F,
            var20
         );
      this.getValue2().handleColor(matrixStack, "U", var30 = var39 + var16 + var17, var32, var13, var21);
      float var41;
      this.getMkadw().handleColor(matrixStack, var8, var41 = var30 + var13 + var15, var31, var11, this.getColor());
   }
}
