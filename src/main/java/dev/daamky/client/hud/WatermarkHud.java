package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.FrameSizeHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.misc.Optimization;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WatermarkHud extends HudElement {
   public static final Identifier class_2960 = net.minecraft.util.Identifier.of("minecraft", "textures/gui/title/g_icon.png");
   public final AnimatedValue animatedValue = new AnimatedValue();
   public FrameSizeHelper frameSizeHelper = null;
   public static Identifier class_29602 = null;
   public static Identifier class_29603 = null;
   public static long mnehi = 0L;
   public static long timeStamp = 0L;

   public WatermarkHud() {
      super(5.0F, 5.0F);
      this.getAutoHelper().setX(20.0F);
      this.getAutoHelper().setY(20.0F);
   }

   @Override
   public String getName() {
      return "Watermark";
   }

   public String getMc() {
      float var1 = this.getAutoHelper().getX();
      float var2 = (float)mc.getWindow().getScaledWidth();
      float var3 = var2 / 2.0F;
      if (var1 + this.getAutoHelper().getScale2() / 2.0F < var3 - 40.0F) {
         return "left";
      } else {
         return var1 + this.getAutoHelper().getScale2() / 2.0F > var3 + 40.0F ? "right" : "center";
      }
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
      this.animatedValue.update();
      this.animatedValue.animateTo((double)var4, 300L, Easing.SINE_OUT, true);
      int var5 = (int)Math.round(this.animatedValue.getValue());
      String var6 = mc.player != null && mc.getNetworkHandler() != null
         ? String.valueOf(
            mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) != null ? mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency() : 0
         )
         : "0";
      float var7 = this.computeFloat(6.0F);
      float var8 = this.computeFloat(7.0F);
      float var9 = this.computeFloat(6.0F);
      float var10 = this.computeFloat(3.0F);
      float var11 = this.computeFloat(0.5F);
      float var12 = this.computeFloat(4.5F);
      String var13 = mc.player != null
         ? mc.player.getName().getString()
         : (mc.getSession() != null && mc.getSession().getUsername() != null ? mc.getSession().getUsername() : "Player");
      String var14 = String.valueOf(var5);
      String var15 = var14 + " FPS";
      String var16 = var6 + " ms";
      float var17 = this.getMheuy().computeFloat("daamky", var7);
      float var18 = this.computeFloat(6.0F);
      float var19 = this.getMkadw().computeFloat(var13, var7);
      float var20 = this.getMheuy().computeFloat(var14, var7);
      String var21 = "FPS";
      float var22 = this.getMkadw().computeFloat(var21, var18);
      float var23 = this.getMheuy().computeFloat(var6, var7);
      String var24 = "MS";
      float var25 = this.getMkadw().computeFloat(var24, var18);
      float var26 = this.computeFloat(10.0F);
      float var27 = this.computeFloat(4.5F) + var26 + this.computeFloat(4.5F);
      float var28 = this.computeFloat(9.0F);
      float var29 = this.computeFloat(3.0F);
      float var30 = var28 + var29 + var19;
      float var31 = var8 + var10 + var20 + this.computeFloat(1.5F) + var22;
      float var32 = var8 + var10 + var23 + this.computeFloat(1.5F) + var25;
      float var33 = var12 + var11 + var12;
      float var34 = var9 + var30 + var33 + var31 + var33 + var32 + var9;
      boolean var35 = this.getAutoHelper().getMergeCards();
      float var36 = var35 ? 0.0F : this.computeFloat(4.0F);
      float var37 = var27 + var36 + var34;
      float var38 = var7 + this.computeFloat(7.0F);
      this.getAutoHelper().setWidth(var37);
      this.getAutoHelper().setHeight(var38);
      Color var39 = this.initInit();
      boolean var40 = MainMenuScreen.isDayMode || ThemeManager.getInstance().getIsDayMode();
      int var41 = var39.getAlpha() << 24;
      int var42 = var40 ? var41 | 16777215 : var41 | 460553;
      int var43 = var40 ? 503316480 : 520093695;
      float var44 = this.getHeight();
      boolean var45 = this.getAutoHelper().isBlur();
      if (var35) {
         if (var45) {
            Render.shaderProgram.handleVal(matrixStack, var2, var3, var37, var38, var44, var39.getRGB());
         } else {
            Render.rectRenderer.handleVal(matrixStack, var2, var3, var37, var38, var44, var39.getRGB());
         }
      } else if (var45) {
         Render.shaderProgram.handleVal(matrixStack, var2, var3, var27, var38, var44, var42);
         Render.shaderProgram.handleVal(matrixStack, var2 + var27 + var36, var3, var34, var38, var44, var39.getRGB());
      } else {
         Render.rectRenderer.handleVal(matrixStack, var2, var3, var27, var38, var44, var42);
         Render.rectRenderer.handleVal(matrixStack, var2 + var27 + var36, var3, var34, var38, var44, var39.getRGB());
      }

      this.runContent();
      if (Interface.getInstance().isEnabled() && Interface.check2()) {
         boolean var46 = Interface.check4();
         float var47 = 1.0F;
         if (var35) {
            Interface.handleVal(matrixStack, var2, var3, var37, var38, var44, var47, var46);
         } else {
            Interface.handleVal(matrixStack, var2, var3, var27, var36, var34, var38, var44, var47, var46);
         }
      }

      float var54 = var3 + var38 / 2.0F;
      float var55 = var54 - var7 / 2.0F - this.computeFloat(0.8F);
      float var48 = var54 - var8 / 2.0F - this.computeFloat(1.2F);
      int var49 = ThemeColor.getColor4().getRGB();
      float var50 = var2 + var27 / 2.0F - var26 / 2.0F;
      float var51 = var54 - var26 / 2.0F;
      int var52 = var40 ? -15461356 : -1;
      Render.myazem.handleClass_2960(matrixStack, var50, var51, var26, var26, 0.0F, var52, 0.0F, 0.0F, 1.0F, 1.0F, class_2960);
      if (var35) {
         Render.rectRenderer
            .handleVal(matrixStack, var2 + var27 - var11 / 2.0F, var3 + this.computeFloat(4.0F), var11, var38 - this.computeFloat(8.0F), 0.0F, var43);
      }

      float var53 = var2 + var27 + var36 + var9;
      Render.myazem
         .handleClass_2960(
            matrixStack,
            var53,
            var54 - var28 / 2.0F,
            var28,
            var28,
            this.computeFloat(0.5F),
            -1,
            0.0F,
            0.0F,
            1.0F,
            1.0F,
            net.minecraft.util.Identifier.of("daamky", "avatar.png")
         );
      float var56;
      this.getMkadw().handleVal(matrixStack, var13, var56 = var53 + var28 + var29, var55, var7, ThemeColor.getColor8().getRGB());
      var53 = var56 + var19;
      float var58;
      Render.rectRenderer.handleVal(matrixStack, var58 = var53 + var12, var3 + this.computeFloat(4.0F), var11, var38 - this.computeFloat(8.0F), 0.0F, var43);
      this.getValue2().handleVal(matrixStack, "]", var53 = var58 + var11 + var12, var48, var8, ThemeColor.getColor8().getRGB());
      this.getMheuy().handleVal(matrixStack, var14, var53 + var8 + var10, var55, var7, ThemeColor.getColor8().getRGB());
      this.getMkadw()
         .handleVal(
            matrixStack,
            var21,
            var53 + var8 + var10 + var20 + this.computeFloat(1.5F),
            var54 - var18 / 2.0F - this.computeFloat(0.8F),
            var18,
            ThemeColor.getColor9().getRGB()
         );
      var53 += var31;
      float var61;
      Render.rectRenderer.handleVal(matrixStack, var61 = var53 + var12, var3 + this.computeFloat(4.0F), var11, var38 - this.computeFloat(8.0F), 0.0F, var43);
      this.getValue2().handleVal(matrixStack, "U", var53 = var61 + var11 + var12, var48, var8, ThemeColor.getColor8().getRGB());
      this.getMheuy().handleVal(matrixStack, var6, var53 + var8 + var10, var55, var7, ThemeColor.getColor8().getRGB());
      this.getMkadw()
         .handleVal(
            matrixStack,
            var24,
            var53 + var8 + var10 + var23 + this.computeFloat(1.5F),
            var54 - var18 / 2.0F - this.computeFloat(0.8F),
            var18,
            ThemeColor.getColor9().getRGB()
         );
      var53 += var32;
   }

   public static Identifier getClass_2960() {
      return class_2960;
   }

   public static int getInt() {
      try {
         if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getTextureManager() != null) {
            AbstractTexture var0 = MinecraftClient.getInstance().getTextureManager().getTexture(class_2960);
            if (var0 != null) {
               return var0.getGlId();
            }
         }
      } catch (Throwable var1) {
      }

      return -1;
   }
}
