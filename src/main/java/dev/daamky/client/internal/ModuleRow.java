package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleRow {
   public final Module feature;
   public float x;
   public float y;
   public float w;
   public float a = 1.0F;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public final AnimatedValue animatedValue4 = new AnimatedValue();
   public boolean flag = false;
   public FloatSetting floatSetting = null;
   public ModeSetting modeSetting = null;
   public boolean flag2 = false;
   public static final Map<String, String> map = new HashMap<>();

   public ModuleRow(Module feature) {
      this.feature = featurex;
      this.animatedValue.handleStart(featurex.isEnabled() ? 1.0 : 0.0);
   }

   public float getNext() {
      float var1 = computeFloat(30.0F);
      List var2 = this.initNext();
      if (var2.isEmpty()) {
         return var1;
      } else {
         float var3 = computeFloat(6.0F);

         for (Setting var5 : var2) {
            if (var5 instanceof FloatSetting) {
               var3 += computeFloat(36.0F);
            } else if (var5 instanceof ModeSetting) {
               var3 += computeFloat(34.0F);
            } else if (var5 instanceof BooleanSetting) {
               var3 += computeFloat(26.0F);
            } else if (var5 instanceof MultiBooleanSetting var6) {
               var3 += computeFloat(18.0F) + (float)(var6.getValues().size() / 3 + 1) * computeFloat(13.0F);
            } else if (var5 instanceof ColorSetting) {
               var3 += computeFloat(18.0F);
            }
         }

         return var1 + var3 + computeFloat(6.0F);
      }
   }

   public List<Setting<?>> initNext() {
      ArrayList var1 = new ArrayList();

      for (Setting var3 : this.feature.getSettings()) {
         if (var3 != null && var3.getName() != null) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public ModuleRow.DropdownOverlay initInit(DrawContext ctx, MatrixStack ms, int mx, int my, int alphaVal) {
      this.animatedValue.update();
      this.animatedValue.animateTo(this.feature.isEnabled() ? 1.0 : 0.0, 180L, Easing.QUINT_OUT);
      float var6 = (float)this.animatedValue.getValue();
      float var7 = this.getNext();
      this.animatedValue2.update();
      boolean var8 = Geometry.computeFlag((double)mx, (double)my, (double)this.x, (double)this.y, (double)this.w, (double)var7);
      this.animatedValue2.animateTo(var8 ? 1.0 : 0.0, 140L, Easing.QUINT_OUT);
      float var9 = (float)this.animatedValue2.getValue();
      float var10 = computeFloat(7.0F);
      int var11 = (int)((float)alphaVal * this.a);
      if (var11 <= 5) {
         return null;
      } else {
         Render.rectRenderer
            .handleRgb(
               ms,
               this.x + computeFloat(2.0F),
               this.y + computeFloat(2.5F),
               this.w - computeFloat(4.0F),
               var7 + computeFloat(1.0F),
               var10,
               new Color(0, 0, 0, (int)((float)var11 * 0.52F))
            );
         Render.rectRenderer.handleRgb(ms, this.x, this.y, this.w, var7, var10, new Color(16, 15, 22, (int)((float)var11 * 0.97F)));
         Render.uSizeHelper
            .handleRgb(
               ms,
               this.x,
               this.y,
               this.w,
               var7,
               var10,
               new Color(255, 255, 255, (int)((float)var11 * 0.055F)),
               new Color(255, 255, 255, (int)((float)var11 * 0.055F)),
               new Color(0, 0, 0, (int)((float)var11 * 0.25F)),
               new Color(0, 0, 0, (int)((float)var11 * 0.25F))
            );
         Render.rectRenderer
            .handleRgb(
               ms,
               this.x + var10 * 0.5F,
               this.y + computeFloat(0.8F),
               this.w - var10,
               computeFloat(0.9F),
               0.0F,
               new Color(255, 255, 255, (int)((float)var11 * (0.22F + 0.07F * var9)))
            );
         Render.rectRenderer
            .handleRgb(
               ms,
               this.x + var10 * 0.5F,
               this.y + var7 - computeFloat(1.6F),
               this.w - var10,
               computeFloat(0.8F),
               0.0F,
               new Color(0, 0, 0, (int)((float)var11 * 0.4F))
            );
         if (var9 > 0.01F) {
            Render.rectRenderer.handleRgb(ms, this.x, this.y, this.w, var7, var10, new Color(255, 255, 255, (int)((float)var11 * 0.025F * var9)));
         }

         float var12 = computeFloat(10.0F);
         float var13 = this.y + computeFloat(15.0F);
         float var14 = computeFloat(6.5F);
         Color var15 = var6 > 0.5F ? new Color(255, 255, 255, var11) : new Color(215, 220, 235, (int)((float)var11 * 0.88F));
         FontBootstrap.mdoswa.handleColor(ms, this.feature.getName(), this.x + var12, var13 - var14 / 2.0F, var14, var15);
         String var16 = this.flag2 ? "..." : this.computeString(this.feature.getBind());
         float var17 = computeFloat(4.5F);
         float var18 = FontBootstrap.clientFont10.computeFloat(var16, var17) + computeFloat(8.0F);
         float var19 = computeFloat(11.0F);
         float var20 = this.x + this.w - var12 - computeFloat(24.0F) - var18;
         float var21 = var13 - var19 / 2.0F;
         Render.rectRenderer
            .handleRgb(
               ms,
               var20,
               var21,
               var18,
               var19,
               computeFloat(3.5F),
               this.flag2 ? InterfaceMetrics.computeColor3((int)((float)var11 * 0.85F)) : new Color(28, 28, 38, var11)
            );
         FontBootstrap.clientFont10.handleColor2(ms, var16, var20 + var18 / 2.0F, var21 + var19 / 2.0F - var17 / 2.0F, var17, new Color(170, 180, 200, var11));
         float var22 = computeFloat(20.0F);
         float var23 = computeFloat(10.0F);
         float var24 = this.x + this.w - var12 - var22;
         float var25 = var13 - var23 / 2.0F;
         Color var26 = ColorMath.initBlue(new Color(32, 32, 42, var11), InterfaceMetrics.computeColor3(var11), var6);
         Render.rectRenderer.handleRgb(ms, var24, var25, var22, var23, var23 / 2.0F, var26);
         float var27 = var23 - computeFloat(2.5F);
         float var28 = var24 + computeFloat(1.25F);
         float var29 = var24 + var22 - var27 - computeFloat(1.25F);
         float var30 = var28 + var6 * (var29 - var28);
         float var31 = var25 + computeFloat(1.25F);
         Render.rectRenderer.handleRgb(ms, var30, var31, var27, var27, var27 / 2.0F, new Color(255, 255, 255, var11));
         ModuleRow.DropdownOverlay var32 = null;
         List var33 = this.initNext();
         if (!var33.isEmpty()) {
            float var34 = this.y + computeFloat(30.0F);
            Render.rectRenderer
               .handleRgb(ms, this.x + var12, var34, this.w - var12 * 2.0F, computeFloat(0.7F), 0.0F, new Color(255, 255, 255, (int)((float)var11 * 0.05F)));
            var34 += computeFloat(6.0F);

            for (Setting var36 : var33) {
               if (var36 instanceof FloatSetting var37) {
                  var34 = this.initMin(ms, mx, my, var37, this.x + var12, var34, this.w - var12 * 2.0F, var11);
               } else if (var36 instanceof ModeSetting) {
                  ModeSetting var38 = (ModeSetting)var36;
                  ModuleRow.DropdownOverlay var42 = this.initInit(ms, mx, my, var38, this.x + var12, var34, this.w - var12 * 2.0F, var11);
                  if (var42 != null) {
                     var32 = var42;
                  }

                  var34 += computeFloat(34.0F);
               } else if (var36 instanceof BooleanSetting var39) {
                  var34 = this.initInit(ms, mx, my, var39, this.x + var12, var34, this.w - var12 * 2.0F, var11);
               } else if (var36 instanceof MultiBooleanSetting var40) {
                  var34 = this.initInit(ms, mx, my, var40, this.x + var12, var34, this.w - var12 * 2.0F, var11);
               } else if (var36 instanceof ColorSetting var41) {
                  var34 = this.initInit(ms, mx, my, var41, this.x + var12, var34, this.w - var12 * 2.0F, var11);
               }
            }
         }

         return var32;
      }
   }

   public float initMin(MatrixStack ms, int mx, int my, FloatSetting sl, float sx, float sy, float sw, int a) {
      float var9 = computeFloat(5.2F);
      float var10 = computeFloat(5.2F);
      float var11 = computeFloat(4.2F);
      if (this.flag && this.floatValValSetting == sl) {
         float var12 = ((float)mx - sx) / sw;
         var12 = sl.getMin() + var12 * (sl.getMax() - sl.getMin());
         var12 = (float)Math.round(var12 / sl.getStep()) * sl.getStep();
         sl.setValue(Math.max(sl.getMin(), Math.min(sl.getMax(), var12)));
      }

      String var34 = this.computeContains(sl.getName());
      String var13 = this.computeFormat(sl.getValue(), sl.getStep()) + var34;
      FontBootstrap.clientFont10.handleColor(ms, sl.getName(), sx, sy, var9, new Color(255, 255, 255, ax));
      float var14 = FontBootstrap.mdoswa.computeFloat(var13, var10);
      FontBootstrap.mdoswa.handleColor(ms, var13, sx + sw - var14, sy, var10, new Color(255, 255, 255, ax));
      String var15 = map.getOrDefault(sl.getName(), "");
      if (!var15.isEmpty()) {
         FontBootstrap.clientFont11.handleColor(ms, var15, sx, sy + var9 + computeFloat(2.0F), var11, new Color(135, 145, 165, (int)((float)ax * 0.75F)));
      }

      float var16 = sy + var9 + computeFloat(10.0F);
      float var17 = computeFloat(2.8F);
      float var18 = (sl.getValue() - sl.getMin()) / (sl.getMax() - sl.getMin()) * sw;
      var18 = Math.max(0.0F, Math.min(sw, var18));
      this.animatedValue4.update();
      this.animatedValue4.animateTo(var18, 40L, Easing.LINEAR);
      float var19 = (float)this.animatedValue4.getValue();
      Render.rectRenderer.handleRgb(ms, sx, var16, sw, var17, var17 / 2.0F, new Color(32, 32, 44, ax));
      if (var19 > 0.0F) {
         Render.rectRenderer.handleRgb(ms, sx, var16, var19, var17, var17 / 2.0F, new Color(255, 255, 255, ax));
      }

      float var20 = sx + var19;
      float var21 = var16 + var17 / 2.0F;
      float var22 = computeFloat(3.5F);
      Render.rectRenderer.handleRgb(ms, var20 - var22, var21 - var22, var22 * 2.0F, var22 * 2.0F, var22, new Color(255, 255, 255, ax));
      float var23 = computeFloat(4.3F);
      float var24 = var16 + var17 + computeFloat(3.0F);

      for (int var25 = 0; var25 < 4; var25++) {
         float var26 = (float)var25 / 3.0F;
         float var27 = sl.getMin() + var26 * (sl.getMax() - sl.getMin());
         String var28 = this.computeFormat(var27, sl.getStep());
         float var29 = FontBootstrap.clientFont11.computeFloat(var28, var23);
         float var30 = sx + var26 * sw;
         float var31 = var25 == 0 ? var30 : (var25 == 3 ? var30 - var29 : var30 - var29 / 2.0F);
         FontBootstrap.clientFont11.handleColor(ms, var28, var31, var24, var23, new Color(125, 135, 155, (int)((float)ax * 0.7F)));
      }

      return sy + computeFloat(36.0F);
   }

   public ModuleRow.DropdownOverlay initInit(MatrixStack ms, int mx, int my, ModeSetting mo, float sx, float sy, float sw, int a) {
      float var9 = computeFloat(5.2F);
      float var10 = computeFloat(4.2F);
      FontBootstrap.clientFont10.handleColor(ms, mo.getName(), sx, sy, var9, new Color(255, 255, 255, ax));
      String var11 = mo.getValue();
      float var12 = FontBootstrap.mdoswa.computeFloat(var11, var9);
      FontBootstrap.mdoswa.handleColor(ms, var11, sx + sw - var12, sy, var9, new Color(235, 240, 250, ax));
      String var13 = map.getOrDefault(mo.getName(), "");
      if (!var13.isEmpty()) {
         FontBootstrap.clientFont11.handleColor(ms, var13, sx, sy + var9 + computeFloat(2.0F), var10, new Color(135, 145, 165, (int)((float)ax * 0.75F)));
      }

      float var14 = sy + var9 + computeFloat(10.0F);
      float var15 = computeFloat(15.0F);
      boolean var16 = Geometry.computeFlag((double)mx, (double)my, (double)sx, (double)var14, (double)sw, (double)var15);
      Render.rectRenderer.handleRgb(ms, sx, var14, sw, var15, computeFloat(4.5F), var16 ? new Color(24, 24, 34, ax) : new Color(18, 18, 26, ax));
      Render.rectRenderer.handleRgb(ms, sx, var14, sw, computeFloat(1.0F), 0.0F, new Color(255, 255, 255, (int)((float)ax * 0.08F)));
      Render.rectRenderer
         .handleRgb(ms, sx, var14 + var15 - computeFloat(1.0F), sw, computeFloat(1.0F), 0.0F, new Color(255, 255, 255, (int)((float)ax * 0.08F)));
      float var17 = computeFloat(4.8F);
      FontBootstrap.clientFont10.handleColor(ms, var11, sx + computeFloat(6.0F), var14 + var15 / 2.0F - var17 / 2.0F, var17, new Color(240, 240, 250, ax));
      FontBootstrap.clientFont10
         .handleColor(
            ms, this.modeSetting == mo ? "^" : "v", sx + sw - computeFloat(10.0F), var14 + var15 / 2.0F - var17 / 2.0F, var17, new Color(140, 150, 170, ax)
         );
      this.animatedValue3.update();
      boolean var18 = this.modeSetting == mo;
      this.animatedValue3.animateTo(var18 ? 1.0 : 0.0, 180L, Easing.QUINT_OUT);
      float var19 = (float)this.animatedValue3.getValue();
      if (var19 > 0.01F) {
         ModuleRow.DropdownOverlay var20 = new ModuleRow.DropdownOverlay();
         var20.modeSetting = mo;
         var20.floatVal = sx;
         var20.float2 = var14 + var15 + computeFloat(2.0F);
         var20.float3 = sw;
         var20.mbosx = var19;
         var20.intVal = ax;
         return var20;
      } else {
         return null;
      }
   }

   public static void initInit(DrawContext ctx, MatrixStack ms, int mx, int my, ModuleRow.DropdownOverlay ov) {
      if (ov != null && !(ov.mbosx <= 0.01F)) {
         ModeSetting var5 = ov.modeSetting;
         List var6 = var5.getValues();
         String var7 = var5.getValue();
         float var8 = computeFloat(13.0F);
         float var9 = (float)var6.size() * var8 + computeFloat(3.0F);
         float var10 = var9 * ov.mbosx;
         int var11 = ov.intVal;
         float var12 = computeFloat(4.8F);
         Color var13 = new Color(255, 255, 255, (int)((float)var11 * 0.16F * ov.mbosx));
         Color var14 = new Color(14, 14, 22, (int)((float)var11 * 0.98F * ov.mbosx));
         Render.rectRenderer.handleRgb(ms, ov.floatVal, ov.float2, ov.float3, var10, computeFloat(4.5F), var13);
         Render.rectRenderer
            .handleRgb(
               ms,
               ov.floatVal + computeFloat(1.0F),
               ov.float2 + computeFloat(1.0F),
               ov.float3 - computeFloat(2.0F),
               var10 - computeFloat(2.0F),
               computeFloat(3.5F),
               var14
            );
         if (ov.mbosx > 0.3F) {
            for (int var15 = 0; var15 < var6.size(); var15++) {
               String var16 = (String)var6.get(var15);
               float var17 = ov.float2 + computeFloat(1.5F) + (float)var15 * var8;
               if (var17 + var8 > ov.float2 + var10) {
                  break;
               }

               boolean var18 = var16.equals(var7);
               boolean var19 = Geometry.computeFlag((double)mx, (double)my, (double)ov.floatVal, (double)var17, (double)ov.float3, (double)var8);
               if (var19 || var18) {
                  Render.rectRenderer
                     .handleRgb(
                        ms,
                        ov.floatVal + computeFloat(2.0F),
                        var17,
                        ov.float3 - computeFloat(4.0F),
                        var8,
                        computeFloat(3.5F),
                        var18 ? InterfaceMetrics.computeColor3((int)((float)var11 * 0.88F * ov.mbosx)) : new Color(32, 32, 46, (int)((float)var11 * ov.mbosx))
                     );
               }

               FontBootstrap.clientFont10
                  .handleColor(
                     ms,
                     var16,
                     ov.floatVal + computeFloat(7.0F),
                     var17 + var8 / 2.0F - var12 / 2.0F,
                     var12,
                     !var18 && !var19 ? new Color(165, 170, 185, (int)((float)var11 * ov.mbosx)) : new Color(255, 255, 255, (int)((float)var11 * ov.mbosx))
                  );
            }
         }
      }
   }

   public float initInit(MatrixStack ms, int mx, int my, BooleanSetting bo, float sx, float sy, float sw, int a) {
      float var9 = computeFloat(5.2F);
      float var10 = computeFloat(4.2F);
      boolean var11 = bo.getValue();
      FontBootstrap.clientFont10.handleColor(ms, bo.getName(), sx, sy, var9, var11 ? new Color(255, 255, 255, ax) : new Color(200, 205, 220, ax));
      String var12 = map.getOrDefault(bo.getName(), "");
      if (!var12.isEmpty()) {
         FontBootstrap.clientFont11.handleColor(ms, var12, sx, sy + var9 + computeFloat(2.0F), var10, new Color(135, 145, 165, (int)((float)ax * 0.75F)));
      }

      float var13 = computeFloat(20.0F);
      float var14 = computeFloat(10.0F);
      float var15 = sx + sw - var13;
      float var16 = sy + computeFloat(1.5F);
      Color var17 = var11 ? InterfaceMetrics.computeColor3(ax) : new Color(32, 32, 42, ax);
      Render.rectRenderer.handleRgb(ms, var15, var16, var13, var14, var14 / 2.0F, var17);
      float var18 = var14 - computeFloat(2.5F);
      float var19 = var15 + computeFloat(1.25F);
      float var20 = var15 + var13 - var18 - computeFloat(1.25F);
      float var21 = var11 ? var20 : var19;
      Render.rectRenderer.handleRgb(ms, var21, var16 + computeFloat(1.25F), var18, var18, var18 / 2.0F, new Color(255, 255, 255, ax));
      return sy + computeFloat(26.0F);
   }

   public float initInit(MatrixStack ms, int mx, int my, MultiBooleanSetting mb, float sx, float sy, float sw, int a) {
      float var9 = computeFloat(5.2F);
      FontBootstrap.clientFont10.handleColor(ms, mb.getName(), sx, sy, var9, new Color(255, 255, 255, ax));
      float var10 = computeFloat(13.0F);
      float var11 = sx;
      float var12 = sy + var9 + computeFloat(4.0F);

      for (String var14 : mb.getValues()) {
         boolean var15 = mb.isEnabled(var14);
         float var16 = computeFloat(4.3F);
         float var17 = FontBootstrap.clientFont10.computeFloat(var14, var16) + computeFloat(9.0F);
         if (var11 + var17 > sx + sw) {
            var11 = sx;
            var12 += var10 + computeFloat(3.0F);
         }

         Render.rectRenderer
            .handleRgb(ms, var11, var12, var17, var10, computeFloat(3.5F), var15 ? InterfaceMetrics.computeColor3(ax) : new Color(26, 26, 36, ax));
         FontBootstrap.clientFont10
            .handleColor2(
               ms, var14, var11 + var17 / 2.0F, var12 + var10 / 2.0F - var16 / 2.0F, var16, var15 ? new Color(255, 255, 255, ax) : new Color(160, 165, 180, ax)
            );
         var11 += var17 + computeFloat(4.0F);
      }

      return var12 + var10 + computeFloat(4.0F);
   }

   public float initInit(MatrixStack ms, int mx, int my, ColorSetting co, float sx, float sy, float sw, int a) {
      float var9 = computeFloat(5.2F);
      FontBootstrap.clientFont10.handleColor(ms, co.getName(), sx, sy, var9, new Color(255, 255, 255, ax));
      Color var10 = co.getValue();
      float var11 = computeFloat(11.0F);
      float var12 = sx + sw - var11;
      Render.rectRenderer.handleRgb(ms, var12, sy, var11, var11, computeFloat(3.0F), new Color(var10.getRed(), var10.getGreen(), var10.getBlue(), ax));
      return sy + computeFloat(18.0F);
   }

   public String computeContains(String name) {
      if (name.contains("Дистанция") || name.contains("Distance")) {
         return " m";
      } else if (name.contains("Радиус")) {
         return " px";
      } else if (name.contains("Длительность")) {
         return " s";
      } else if (name.contains("Размер")) {
         return "x";
      } else {
         return !name.contains("высота") && !name.contains("Distance") ? "" : " м";
      }
   }

   public String computeFormat(float val, float step) {
      int var3 = step >= 1.0F ? 0 : (step >= 0.1F ? 1 : 2);
      return String.format(Locale.US, "%." + var3 + "f", val);
   }

   public String computeString(int keyCode) {
      if (keyCode <= 0) {
         return "NONE";
      } else {
         return switch (keyCode) {
            case 32 -> "SPACE";
            case 340 -> "L_SHIFT";
            case 341 -> "L_CTRL";
            case 344 -> "R_SHIFT";
            case 345 -> "R_CTRL";
            default -> keyCode >= 65 && keyCode <= 90 ? String.valueOf((char)keyCode) : "KEY_" + keyCode;
         };
      }
   }

   public static float computeFloat(float v) {
      return DisplayScale.getInstance().computeGui(v);
   }

   public boolean computeMin(double mx, double my, int button) {
      float var6 = computeFloat(10.0F);
      float var7 = this.y + computeFloat(15.0F);
      String var8 = this.computeString(this.feature.getBind());
      float var9 = computeFloat(4.5F);
      float var10 = FontBootstrap.clientFont10.computeFloat(var8, var9) + computeFloat(8.0F);
      float var11 = computeFloat(11.0F);
      float var12 = this.x + this.w - var6 - computeFloat(24.0F) - var10;
      float var13 = var7 - var11 / 2.0F;
      if (Geometry.computeFlag(mx, my, (double)var12, (double)var13, (double)var10, (double)var11)) {
         this.flag2 = !this.flag2;
         return true;
      } else {
         float var14 = computeFloat(20.0F);
         float var15 = computeFloat(10.0F);
         float var16 = this.x + this.w - var6 - var14;
         float var17 = var7 - var15 / 2.0F;
         if (Geometry.computeFlag(mx, my, (double)var16, (double)var17, (double)var14, (double)var15)) {
            this.feature.toggle();
            return true;
         } else {
            List var18 = this.initNext();
            if (!var18.isEmpty()) {
               float var19 = this.x + var6;
               float var20 = this.w - var6 * 2.0F;
               float var21 = this.y + computeFloat(30.0F);

               for (Setting var23 : var18) {
                  if (var23 instanceof FloatSetting var24) {
                     float var36 = var21 + computeFloat(15.0F);
                     float var38 = computeFloat(10.0F);
                     if (Geometry.computeFlag(mx, my, (double)var19, (double)(var36 - computeFloat(3.0F)), (double)var20, (double)var38)) {
                        this.flag = true;
                        this.floatValValSetting = var24;
                        float var40 = ((float)mx - var19) / var20;
                        var40 = var24.getMin() + var40 * (var24.getMax() - var24.getMin());
                        var40 = (float)Math.round(var40 / var24.getStep()) * var24.getStep();
                        var24.setValue(Math.max(var24.getMin(), Math.min(var24.getMax(), var40)));
                        return true;
                     }

                     var21 += computeFloat(36.0F);
                  } else if (var23 instanceof ModeSetting var25) {
                     float var35 = var21 + computeFloat(15.0F);
                     float var37 = computeFloat(15.0F);
                     if (this.modeSetting == var25) {
                        List var39 = var25.getValues();
                        float var43 = var35 + var37 + computeFloat(2.0F);
                        float var44 = computeFloat(13.0F);

                        for (int var45 = 0; var45 < var39.size(); var45++) {
                           float var46 = var43 + computeFloat(1.5F) + (float)var45 * var44;
                           if (Geometry.computeFlag(mx, my, (double)var19, (double)var46, (double)var20, (double)var44)) {
                              var25.setValue((String)var39.get(var45));
                              this.modeSetting = null;
                              return true;
                           }
                        }
                     }

                     if (Geometry.computeFlag(mx, my, (double)var19, (double)var35, (double)var20, (double)var37)) {
                        this.modeSetting = this.modeSetting == var25 ? null : var25;
                        return true;
                     }

                     var21 += computeFloat(34.0F);
                  } else if (var23 instanceof BooleanSetting var26) {
                     if (Geometry.computeFlag(mx, my, (double)var19, (double)var21, (double)var20, (double)computeFloat(20.0F))) {
                        var26.toggle();
                        return true;
                     }

                     var21 += computeFloat(26.0F);
                  } else if (!(var23 instanceof MultiBooleanSetting var27)) {
                     if (var23 instanceof ColorSetting) {
                        var21 += computeFloat(18.0F);
                     }
                  } else {
                     float var28 = computeFloat(13.0F);
                     float var29 = var19;
                     float var30 = var21 + computeFloat(9.2F);

                     for (String var32 : var27.getValues()) {
                        float var33 = computeFloat(4.3F);
                        float var34 = FontBootstrap.clientFont10.computeFloat(var32, var33) + computeFloat(9.0F);
                        if (var29 + var34 > var19 + var20) {
                           var29 = var19;
                           var30 += var28 + computeFloat(3.0F);
                        }

                        if (Geometry.computeFlag(mx, my, (double)var29, (double)var30, (double)var34, (double)var28)) {
                           var27.has(var32);
                           return true;
                        }

                        var29 += var34 + computeFloat(4.0F);
                     }

                     var21 = var30 + var28 + computeFloat(4.0F);
                  }
               }
            }

            return false;
         }
      }
   }

   public boolean computeFlag(double mx, double my, int button) {
      if (button == 0) {
         this.flag = false;
         this.floatValValSetting = null;
      }

      return false;
   }

   public boolean computeBind(int keyCode, int scanCode, int modifiers) {
      if (this.flag2) {
         if (keyCode == 256) {
            this.feature.setBind(0);
         } else {
            this.feature.setBind(keyCode);
         }

         this.flag2 = false;
         return true;
      } else {
         return false;
      }
   }

   public void setX(float x) {
      this.x = xx;
   }

   public void setY(float y) {
      this.y = yx;
   }

   public void setW(float w) {
      this.w = wx;
   }

   public void setA(float a) {
      this.a = ax;
   }

   public Module getFeature() {
      return this.feature;
   }

   static {
      map.put("GUI Blur", "Размытие заднего фона");
      map.put("GUI Scale", "Масштаб интерфейса");
      map.put("Blur Strength", "Сила размытия");
      map.put("Strength", "Сила эффекта");
      map.put("Режим наводки", "Алгоритм обхода углов ротации");
      map.put("Aim mode", "Rotation bypass algorithm");
      map.put("Дистанция", "Дальность атаки в блоках");
      map.put("Distance", "Attack range in blocks");
      map.put("Режим через стены", "Обход ударов сквозь стены");
      map.put("Wall mode", "Bypass wall checks");
      map.put("Сортировка целей", "Приоритет выбора приоритетного врага");
      map.put("Sort mode", "Target selection priority");
      map.put("Задержка атаки", "Тип тайминга ударов");
      map.put("Attack delay", "Hit timing mechanism");
      map.put("Сброс спринта", "Способ сброса спринта при ударе");
      map.put("Sprint reset", "Sprint reset method on attack");
      map.put("Режим Критов", "Тип пакета для урон-крита");
      map.put("Criticals Mode", "Fall packet bypass method");
      map.put("Усиление урона", "Дополнительный урон при падении");
      map.put("Damage Boost", "Extra smash damage on fall");
      map.put("Авто-смена булавы", "Переключение слота при ударе");
      map.put("Auto Mace Swap", "Switch slot when falling");
      map.put("Мин. высота падения", "Высота для активации булавы");
      map.put("Min Fall Distance", "Fall height required to trigger");
      map.put("Игнорировать Сущностей", "Не ломать блоки сквозь мобов");
      map.put("Ignore Entities", "Bypass block click trace");
      map.put("Режим", "Визуальный эффект подсветки цели");
      map.put("Текстура маркер", "Форма маркера подсветки");
      map.put("Анимация появления", "Эффект появления кольца");
      map.put("Длительность", "Длительность эффекта");
      map.put("Размер", "Масштаб метки");
      map.put("Радиус Стрелок", "Отступ от центра экрана");
      map.put("Смещение X", "Положение оружия по X-оси");
   }

   public static class DropdownOverlay {
      public ModeSetting modeSetting;
      public float floatVal;
      public float float2;
      public float float3;
      public float mbosx;
      public int intVal;
   }
}
