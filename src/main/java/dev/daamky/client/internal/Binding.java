package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.PVEScreen;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.render.CustomCrosshair;
import dev.daamky.client.module.render.HitTweaks;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import dev.daamky.client.util.KeyNames;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Binding extends ExpandableSettingComponent3 {
   public final List<SettingRow> list = new ArrayList<>();
   public final Module feature;
   public final AnimatedValue[] animatedValueList = new AnimatedValue[]{
      new AnimatedValue(), new AnimatedValue(), new AnimatedValue(), new AnimatedValue(), new AnimatedValue()
   };
   public boolean flag;
   public boolean flag2;
   public final String string;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public float floatVal;
   public float float2;
   public float float3;
   public float float4;
   public float float5;
   public float float6;
   public float float7;
   public float float8;
   public float float9;
   public float float10;
   public float float11;
   public float float12;
   public float float13;
   public float float14;
   public float float15;
   public float float16;
   public float float17;
   public float float18;
   public float float19;
   public float float20;
   public long timeStamp = 0L;
   public static final long long2 = 800L;

   public void runCurrent() {
      this.longVal = System.currentTimeMillis();
   }

   public Binding(Module feature) {
      this.feature = featurex;
      this.string = featurex.getName().toLowerCase();

      for (Setting var3 : featurex.getSettings()) {
         if (var3 instanceof BooleanSetting var4) {
            this.list.add(new BooleanValue(var4));
         }

         if (var3 instanceof MultiBooleanSetting var5) {
            this.list.add(new ExpandableSettingComponent2(var5));
         }

         if (var3 instanceof ModeSetting var6) {
            this.list.add(new Bound(var6));
         }

         if (var3 instanceof FloatSetting var7) {
            this.list.add(new Opacity(var7));
         }

         if (var3 instanceof ColorSetting var8) {
            this.list.add(new dev.daamky.client.internal.ExpandableSettingComponent(var8));
         }

         if (var3 instanceof ButtonSetting var9) {
            this.list.add(new Context2(var9));
         }

         if (var3 instanceof IntegerSetting var10) {
            this.list.add(new None(var10));
         }

         if (var3 instanceof StringSetting var11) {
            this.list.add(new Context3(var11));
         }
      }

      this.animatedValue.handleStart(featurex.isEnabled() ? 1.0 : 0.0);
   }

   public void runFeature() {
      this.run();
      float var1 = this.getFloat();
      float var2 = this.getFloat();
      float var3 = InterfaceMetrics.computeFloat(3.0F);
      var2 = (float)((double)var2 + (double)InterfaceMetrics.computeFloat(48.0F) * this.animatedValue3.getValue());
      if (this.feature instanceof CustomCrosshair) {
         var2 += InterfaceMetrics.computeFloat(60.0F) * var1;
      } else if (this.feature instanceof HitTweaks) {
         var2 += InterfaceMetrics.computeFloat(120.0F) * var1;
      } else if (this.feature instanceof Interface) {
         var2 += InterfaceMetrics.computeFloat(28.0F) * var1;
      }

      if (var1 > 0.0F) {
         float var10 = 0.0F;

         for (SettingRow var12 : this.list) {
            var12.getAnimatedValue().update();
            var12.getAnimatedValue().animateTo(var12.getSetting().isVisible() ? 1.0 : 0.0, 120L, Easing.SINE_OUT);
            if (var12.getAnimatedValue().getValue() > 0.0) {
               var10 = (float)((double)var10 + (double)(var12.getHeight() + var3) * var12.getAnimatedValue().getValue());
            }
         }

         var2 += var10 * var1;
      }

      this.setHeight(var2 + var3);
   }

   public void initInit(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var7 = context.getMatrices();
      this.animatedValue2.update();
      this.animatedValue.update();
      this.animatedValue3.update();
      this.animatedValue2
         .animateTo(
            Geometry.computeFlag((float)mouseX, (float)mouseY, this.getX(), this.getY(), this.getWidth(), this.getFloat()) ? 1.0 : 0.0, 160L, Easing.QUINT_OUT
         );
      this.animatedValue.animateTo(this.feature.isEnabled() ? 1.0 : 0.0, 160L, Easing.QUINT_OUT);
      this.animatedValue3.animateTo(this.flag2 ? 1.0 : 0.0, 130L, Easing.QUINT_OUT);
      int var8 = (int)(this.clamp() * 255.0F);
      float var9 = (float)this.animatedValue.getValue();
      float var10 = (float)this.animatedValue2.getValue();
      float var11 = this.getFloat();
      float var12 = this.getY() + var11 / 2.0F;
      float var13 = InterfaceMetrics.computeFloat(6.4F);
      float var14 = InterfaceMetrics.computeFloat(2.6F);
      float var15 = InterfaceMetrics.computeFloat(8.0F);
      float var16 = this.getX() + var15 + InterfaceMetrics.computeFloat(5.0F);
      float var17 = this.getX() + var15 + InterfaceMetrics.computeFloat(11.0F);
      float var18 = InterfaceMetrics.computeFloat(3.5F);
      Render.rectRenderer.handleRgb(var7, this.getX(), this.getY(), this.getWidth(), var11, var18, InterfaceMetrics.computeMax(var8, 0.3F));
      if (var10 > 0.01F) {
         Render.rectRenderer
            .handleRgb(var7, this.getX(), this.getY(), this.getWidth(), var11, var18, InterfaceMetrics.computeMax2((int)((float)var8 * var10), 0.2F));
      }

      if (var9 > 0.01F) {
         int var19 = (int)((float)var8 * var9);
         float var20 = var12 - var14 / 2.0F;
         Render.rectRenderer
            .handleRgb(
               var7,
               var16 - InterfaceMetrics.computeFloat(1.0F),
               var20 - InterfaceMetrics.computeFloat(1.0F),
               var14 + InterfaceMetrics.computeFloat(2.0F),
               var14 + InterfaceMetrics.computeFloat(2.0F),
               var14,
               InterfaceMetrics.computeColor3((int)((float)var19 * 0.16F))
            );
         Render.rectRenderer.handleRgb(var7, var16, var20, var14, var14, var14 / 2.0F, InterfaceMetrics.computeColor3((int)((float)var19 * 0.92F)));
      }

      Color var65 = InterfaceMetrics.computeColor4(var8);
      Color var66 = InterfaceMetrics.computeColor2(var8);
      Color var21 = ColorMath.initBlue(InterfaceMetrics.computeColor3(var8), var66, 0.65F);
      Color var22 = ColorMath.initBlue(var66, var65, var9);
      Color var23 = ColorMath.initBlue(var21, var22, var10 * 0.38F);
      String var24 = this.flag ? "Binding..." : this.feature.getName();
      float var25 = InterfaceMetrics.computeFloat(0.85F) * var10;
      float var26 = var17 + var25;
      float var27 = Math.min(1.0F, var9 * 0.95F + var10 * 0.45F);
      if (var27 > 0.01F) {
         Color var28 = InterfaceMetrics.computeColor3((int)((float)var8 * 0.16F * var27));
         FontBootstrap.clientFont10.handleColor(var7, var24, var26 - InterfaceMetrics.computeFloat(0.35F), var12 - var13 / 2.0F, var13, var28);
         FontBootstrap.clientFont10.handleColor(var7, var24, var26 + InterfaceMetrics.computeFloat(0.35F), var12 - var13 / 2.0F, var13, var28);
      }

      FontBootstrap.clientFont10.handleColor(var7, var24, var26, var12 - var13 / 2.0F, var13, var23);
      float var67 = this.getX() + this.getWidth() - var15 - InterfaceMetrics.computeFloat(5.0F);
      if (!this.list.isEmpty()) {
         float var29 = InterfaceMetrics.computeFloat(6.5F);
         float var68;
         FontBootstrap.clientFont15
            .handleColor(
               var7,
               "h",
               var68 = var67 - var29,
               var12 - var29 / 2.0F - InterfaceMetrics.computeFloat(0.8F),
               var29,
               InterfaceMetrics.initBlue(Color.WHITE, var8)
            );
         var67 = var68 - InterfaceMetrics.computeFloat(5.0F);
      }

      boolean var71 = this.feature.getBind() != 0 && this.feature.getBind() != -1 && this.feature.getBind() != -999;
      float var30 = Math.max(var10, !this.flag2 && !var71 ? 0.0F : 1.0F);
      if (var30 > 0.01F) {
         String var31 = var71 ? KeyNames.computeNone(this.feature.getBind()).toUpperCase() : "Z";
         float var32 = var13 * 0.85F;
         float var33 = var71 ? FontBootstrap.clientFont10.computeFloat(var31, var32) : FontBootstrap.clientFont15.computeFloat(var31, var32);
         float var34 = InterfaceMetrics.computeFloat(3.5F);
         float var35 = Math.max(InterfaceMetrics.computeFloat(11.0F), var33 + var34 * 2.0F);
         float var36 = InterfaceMetrics.computeFloat(9.5F);
         float var69;
         this.floatVal = var69 = var67 - var35;
         this.floatValVal2 = var12 - var36 / 2.0F;
         this.floatValVal3 = var35;
         this.floatValVal4 = var36;
         Render.rectRenderer
            .handleRgb(
               var7,
               this.floatVal,
               this.floatValVal2,
               this.floatValVal3,
               this.floatValVal4,
               InterfaceMetrics.computeFloat(2.4F),
               InterfaceMetrics.computeMax2((int)((float)var8 * var30), 0.62F)
            );
         if (var71) {
            FontBootstrap.clientFont10
               .handleColor(
                  var7,
                  var31,
                  this.floatVal + this.floatValVal3 / 2.0F - var33 / 2.0F,
                  var12 - var32 / 2.0F + InterfaceMetrics.computeFloat(0.15F),
                  var32,
                  InterfaceMetrics.computeColor2((int)((float)var8 * var30))
               );
         } else {
            FontBootstrap.clientFont15
               .handleColor(
                  var7,
                  "Z",
                  this.floatVal + this.floatValVal3 / 2.0F - var33 / 2.0F,
                  var12 - var32 / 2.0F - InterfaceMetrics.computeFloat(0.65F),
                  var32,
                  InterfaceMetrics.initBlue(Color.WHITE, (int)((float)var8 * var30))
               );
         }

         var67 = var69 - InterfaceMetrics.computeFloat(5.0F);
      }

      this.handleBind(var7, var8);
      float var72 = this.getFloat();
      if (var72 > 0.0F) {
         float var38 = this.getY() + var11 + InterfaceMetrics.computeFloat(3.0F) + InterfaceMetrics.computeFloat(48.0F) * (float)this.animatedValue3.getValue();
         float var39 = InterfaceMetrics.computeFloat(4.0F) * var72;
         if (this.feature instanceof CustomCrosshair) {
            var39 += InterfaceMetrics.computeFloat(60.0F) * var72;
         } else if (this.feature instanceof HitTweaks) {
            var39 += InterfaceMetrics.computeFloat(120.0F) * var72;
         } else if (this.feature instanceof Interface) {
            var39 += InterfaceMetrics.computeFloat(22.0F) * var72;
         }

         for (SettingRow var41 : this.list) {
            float var42 = (float)var41.getAnimatedValue().getValue();
            if (var42 > 0.0F) {
               var39 += (var41.getHeight() + InterfaceMetrics.computeFloat(3.0F)) * var42;
            }
         }

         if (var39 > 0.0F) {
            float var93 = InterfaceMetrics.computeFloat(3.5F);
            Render.rectRenderer
               .handleRgb(var7, this.getX(), var38, this.getWidth(), var39, var93, InterfaceMetrics.computeMax((int)((float)var8 * var72), 0.3F));
            var38 += InterfaceMetrics.computeFloat(2.0F);
         }

         if (this.feature instanceof CustomCrosshair) {
            float var94 = InterfaceMetrics.computeFloat(60.0F) * var72;
            float var73 = InterfaceMetrics.computeFloat(5.0F);
            float var76 = InterfaceMetrics.computeFloat(4.0F);
            float var79 = this.getWidth() - var15 * 2.0F - var73 * 2.0F;
            float var82 = InterfaceMetrics.computeFloat(56.0F) * var72;
            Render.rectRenderer
               .handleRgb(var7, this.getX() + var15 + var73, var38, var79, var82, var76, InterfaceMetrics.computeMax2((int)((float)var8 * var72), 0.62F));
            float var85 = this.getX() + var15 + var73 + var79 / 2.0F;
            float var37 = var38 + var82 / 2.0F;
            var7.push();
            var7.translate(var85, var37, 0.0F);
            var7.scale(var72, var72, 1.0F);
            var7.translate(-var85, -var37, 0.0F);
            CustomCrosshair.handleWindow(var7, var85, var37);
            var7.pop();
            var38 += var94;
         } else if (this.feature instanceof HitTweaks) {
            float var95 = InterfaceMetrics.computeFloat(120.0F) * var72;
            float var74 = InterfaceMetrics.computeFloat(5.0F);
            float var77 = InterfaceMetrics.computeFloat(4.0F);
            float var80 = this.getWidth() - var15 * 2.0F - var74 * 2.0F;
            float var83 = InterfaceMetrics.computeFloat(116.0F) * var72;
            Render.rectRenderer
               .handleRgb(var7, this.getX() + var15 + var74, var38, var80, var83, var77, InterfaceMetrics.computeMax2((int)((float)var8 * var72), 0.62F));
            float var86 = this.getX() + var15 + var74 + var80 / 2.0F;
            float var88 = var38 + var83 / 2.0F;
            var7.push();
            var7.translate(var86, var88, 0.0F);
            var7.scale(var72, var72, 1.0F);
            var7.translate(-var86, -var88, 0.0F);
            HitTweaks.initEquals(context, var7, var86, var88, var80, var83);
            var7.pop();
            var38 += var95;
         }

         boolean var96 = false;

         for (SettingRow var100 : this.list) {
            float var43 = (float)var100.getAnimatedValue().getValue();
            if (var43 > 0.0F) {
               if (var96) {
                  float var44 = var38 - InterfaceMetrics.computeFloat(3.0F) / 2.0F;
                  Render.rectRenderer
                     .handleRgb(
                        var7,
                        this.getX() + var15 + InterfaceMetrics.computeFloat(7.0F),
                        var44,
                        this.getWidth() - var15 * 2.0F - InterfaceMetrics.computeFloat(14.0F),
                        Math.max(0.45F, InterfaceMetrics.computeFloat(0.45F)),
                        0.0F,
                        InterfaceMetrics.initInit((int)((float)var8 * var72 * var43 * 1.35F))
                     );
               }

               var100.setX(this.getX() + var15);
               var100.setY(var38);
               var100.setWidth(this.getWidth() - var15 * 2.0F);
               var100.setAlpha(this.clamp() * var72 * var43);
               var100.handleVal(context, mouseX, mouseY, delta);
               var38 += (var100.getHeight() + InterfaceMetrics.computeFloat(3.0F)) * var43;
               var96 = true;
            }
         }

         if (this.feature instanceof Interface) {
            float var99 = var38 - InterfaceMetrics.computeFloat(3.0F) / 2.0F;
            Render.rectRenderer
               .handleRgb(
                  var7,
                  this.getX() + var15 + InterfaceMetrics.computeFloat(7.0F),
                  var99,
                  this.getWidth() - var15 * 2.0F - InterfaceMetrics.computeFloat(14.0F),
                  Math.max(0.45F, InterfaceMetrics.computeFloat(0.45F)),
                  0.0F,
                  InterfaceMetrics.initInit((int)((float)var8 * var72 * 1.35F))
               );
            float var101 = InterfaceMetrics.computeFloat(5.5F);
            FontBootstrap.clientFont10
               .handleColor(
                  var7, "Accent Theme", this.getX() + var15, var38 + InterfaceMetrics.computeFloat(1.0F), var101, InterfaceMetrics.computeColor4(var8)
               );
            float var102 = this.getWidth() - var15 * 2.0F;
            float var103 = InterfaceMetrics.computeFloat(16.0F);
            float var45 = InterfaceMetrics.computeFloat(4.0F);
            float var46 = this.getX() + var15;
            float var47 = var38 + var101 + InterfaceMetrics.computeFloat(3.0F);
            Render.rectRenderer.handleRgb(var7, var46, var47, var102, var103, var45, new Color(16, 16, 22, (int)((float)var8 * 0.75F * var72)));
            Render.rectRenderer.handleRgb(var7, var46, var47, var102, var103, var45, new Color(255, 255, 255, (int)((float)var8 * 0.06F * var72)));
            int var48 = this.getCase();
            Color[] var49 = new Color[]{
               new Color(245, 60, 60), new Color(55, 130, 255), new Color(40, 215, 100), new Color(175, 60, 255), new Color(245, 245, 245)
            };
            float var50 = InterfaceMetrics.computeFloat(12.0F);
            float var51 = InterfaceMetrics.computeFloat(10.0F);
            float var52 = InterfaceMetrics.computeFloat(3.0F);
            float var53 = var102 / 5.0F;

            for (int var54 = 0; var54 < 5; var54++) {
               this.animatedValueList[var54].update();
               this.animatedValueList[var54].animateTo(var48 == var54 ? 1.0 : 0.0, 200L, Easing.QUINT_OUT);
               float var55 = (float)this.animatedValueList[var54].getValue();
               float var56 = var46 + var53 * (float)var54 + var53 / 2.0F;
               float var57 = var47 + var103 / 2.0F;
               float var58 = var50 + InterfaceMetrics.computeFloat(1.5F) * var55;
               float var59 = var51 + InterfaceMetrics.computeFloat(1.5F) * var55;
               float var60 = var56 - var58 / 2.0F;
               float var61 = var57 - var59 / 2.0F;
               if (var55 > 0.01F) {
                  float var62 = InterfaceMetrics.computeFloat(1.2F) * var55;
                  Render.rectRenderer
                     .handleRgb(
                        var7,
                        var60 - var62,
                        var61 - var62,
                        var58 + var62 * 2.0F,
                        var59 + var62 * 2.0F,
                        var52 + var62,
                        ColorMath.initBlue(var49[var54], (int)((float)var8 * 0.35F * var55 * var72))
                     );
               }

               Color var104 = ColorMath.initBlue(var49[var54], (int)(var72 * (float)var8));
               Render.rectRenderer.handleRgb(var7, var60, var61, var58, var59, var52, var104);
               if (var55 > 0.01F) {
                  Color var63 = var54 == 4
                     ? new Color(20, 20, 20, (int)((float)var8 * 0.85F * var55))
                     : new Color(255, 255, 255, (int)((float)var8 * 0.9F * var55));
                  float var64 = InterfaceMetrics.computeFloat(2.6F) * var55;
                  Render.rectRenderer.handleRgb(var7, var56 - var64 / 2.0F, var57 - var64 / 2.0F, var64, var64, var64 / 2.0F, var63);
               }
            }

            var38 += var101 + InterfaceMetrics.computeFloat(3.0F) + var103 + InterfaceMetrics.computeFloat(3.0F);
         }
      }

      long var5;
      if ((var5 = System.currentTimeMillis() - this.longVal) >= 0L && var5 < 800L) {
         float var75 = (float)var5 / 800.0F;
         float var78 = var75 < 0.5F ? 2.0F * var75 * var75 : 1.0F - (float)Math.pow((double)(-2.0F * var75 + 2.0F), 2.0) / 2.0F;
         float var81 = InterfaceMetrics.computeFloat(3.5F);
         float var84 = this.getHeight() - InterfaceMetrics.computeFloat(3.0F);
         float var87 = this.getX() + var81;
         float var89 = this.getWidth() - 2.0F * var81;
         if (var89 > 0.0F) {
            float var91 = this.getWidth() * 0.42F;
            float var92 = var87 - var91 + var78 * (var89 + 2.0F * var91);
            int var97 = (int)((float)var8 * 0.5F);
            this.handleAbs(var7, Math.max(var87, var92 - var91), Math.min(var87 + var89, var92), this.getY(), var84, var92, var91, var97);
            this.handleAbs(var7, Math.max(var87, var92), Math.min(var87 + var89, var92 + var91), this.getY(), var84, var92, var91, var97);
         }
      }
   }

   public void handleAbs(MatrixStack ms, float x0, float x1, float y, float h, float centerX, float half, int maxA) {
      if (!(x1 <= x0)) {
         float var9 = Math.max(0.0F, 1.0F - Math.abs(x0 - centerX) / half);
         float var10 = Math.max(0.0F, 1.0F - Math.abs(x1 - centerX) / half);
         var9 *= var9;
         var10 *= var10;
         if (!(var9 < 0.004F) || !(var10 < 0.004F)) {
            Color var11 = InterfaceMetrics.computeColor3((int)((float)maxA * var9));
            Color var12 = InterfaceMetrics.computeColor3((int)((float)maxA * var10));
            Render.uSizeHelper.handleRgb(ms, x0, y, x1 - x0, h, 0.0F, var11, var12, var11, var12);
         }
      }
   }

   public void handleBind(MatrixStack matrixStack, int alpha) {
      float var3 = (float)this.animatedValue3.getValue();
      if (!(var3 <= 0.01F)) {
         float var4 = InterfaceMetrics.computeFloat(5.5F);
         float var5 = InterfaceMetrics.computeFloat(4.0F);
         float var6 = InterfaceMetrics.computeFloat(6.5F);
         float var7 = InterfaceMetrics.computeFloat(5.5F);
         float var8 = InterfaceMetrics.computeFloat(8.0F);
         this.floatValVal7 = this.getWidth() - var8 * 2.0F - InterfaceMetrics.computeFloat(4.0F);
         this.floatValVal8 = InterfaceMetrics.computeFloat(46.0F);
         this.floatValVal5 = this.getX() + var8 + InterfaceMetrics.computeFloat(2.0F);
         this.floatValVal6 = this.getY() + this.getFloat() + InterfaceMetrics.computeFloat(3.0F);
         int var9 = (int)((float)alpha * var3);
         Render.rectRenderer
            .handleRgb(matrixStack, this.floatValVal5, this.floatValVal6, this.floatValVal7, this.floatValVal8, var5, InterfaceMetrics.computeColor((int)((float)var9 * 0.92F)));
         Render.rectRenderer
            .handleRgb(matrixStack, this.floatValVal5, this.floatValVal6, this.floatValVal7, this.floatValVal8, var5, InterfaceMetrics.computeColor3((int)((float)var9 * 0.055F)));
         FontBootstrap.mdoswa
            .handleColor(matrixStack, "Bind", this.floatValVal5 + var4, this.floatValVal6 + InterfaceMetrics.computeFloat(4.5F), var6, InterfaceMetrics.computeColor2(var9));
         String var10 = this.flag ? "Press key..." : KeyNames.computeNone(this.feature.getBind()).toUpperCase();
         this.floatValVal11 = this.floatValVal7 - var4 * 2.0F;
         this.floatValVal12 = InterfaceMetrics.computeFloat(13.0F);
         this.floatValVal9 = this.floatValVal5 + var4;
         this.floatValVal10 = this.floatValVal6 + InterfaceMetrics.computeFloat(15.5F);
         Render.rectRenderer
            .handleRgb(
               matrixStack,
               this.floatValVal9,
               this.floatValVal10,
               this.floatValVal11,
               this.floatValVal12,
               InterfaceMetrics.computeFloat(3.0F),
               this.flag ? InterfaceMetrics.computeColor3((int)((float)var9 * 0.16F)) : InterfaceMetrics.computeMax2(var9, 0.62F)
            );
         FontBootstrap.clientFont10
            .handleColor(
               matrixStack,
               var10,
               this.floatValVal9 + InterfaceMetrics.computeFloat(4.0F),
               this.floatValVal10 + this.floatValVal12 / 2.0F - var7 / 2.0F + InterfaceMetrics.computeFloat(0.1F),
               var7,
               this.flag ? InterfaceMetrics.computeColor2(var9) : InterfaceMetrics.computeColor4(var9)
            );
         float var11 = InterfaceMetrics.computeFloat(4.5F);
         this.floatValVal19 = this.floatValVal15 = (this.floatValVal11 - var11) / 2.0F;
         this.floatValVal20 = this.floatValVal16 = InterfaceMetrics.computeFloat(12.0F);
         this.floatValVal13 = this.floatValVal9;
         this.floatValVal17 = this.floatValVal13 + this.floatValVal15 + var11;
         this.floatValVal18 = this.floatValVal14 = this.floatValVal10 + this.floatValVal12 + InterfaceMetrics.computeFloat(4.5F);
         this.handleWhite(matrixStack, this.floatValVal13, this.floatValVal14, this.floatValVal15, this.floatValVal16, "c", "Toggle", !this.feature.isBindHold(), var9);
         this.handleWhite(matrixStack, this.floatValVal17, this.floatValVal18, this.floatValVal19, this.floatValVal20, "W", "Hold", this.feature.isBindHold(), var9);
      }
   }

   public void handleWhite(MatrixStack matrixStack, float x, float y, float width, float height, String icon, String label, boolean active, int alpha) {
      float var10 = InterfaceMetrics.computeFloat(3.0F);
      float var11 = InterfaceMetrics.computeFloat(5.5F);
      float var12 = InterfaceMetrics.computeFloat(5.5F);
      float var13 = InterfaceMetrics.computeFloat(3.0F);
      float var14 = FontBootstrap.clientFont15.computeFloat(icon, var11);
      float var15 = FontBootstrap.clientFont10.computeFloat(label, var12);
      float var16 = var14 + var13 + var15;
      float var17 = x + width / 2.0F - var16 / 2.0F;
      float var18 = y + height / 2.0F;
      Render.rectRenderer
         .handleRgb(
            matrixStack,
            x,
            y,
            width,
            height,
            var10,
            active ? InterfaceMetrics.computeColor3((int)((float)alpha * 0.22F)) : InterfaceMetrics.computeMax2(alpha, 0.45F)
         );
      FontBootstrap.clientFont15
         .handleColor(
            matrixStack,
            icon,
            var17,
            var18 - var11 / 2.0F - InterfaceMetrics.computeFloat(0.7F),
            var11,
            active ? InterfaceMetrics.initBlue(Color.WHITE, alpha) : InterfaceMetrics.initBlue(Color.WHITE, (int)((float)alpha * 0.6F))
         );
      FontBootstrap.clientFont10
         .handleColor(
            matrixStack,
            label,
            var17 + var14 + var13,
            var18 - var12 / 2.0F + InterfaceMetrics.computeFloat(0.1F),
            var12,
            active ? InterfaceMetrics.computeColor2(alpha) : InterfaceMetrics.computeColor4(alpha)
         );
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.flag) {
         this.feature.setBind(keyCode != 256 && keyCode != 261 ? keyCode : -999);
         this.flag = false;
      }

      if (!this.check()) {
         this.list.forEach(Binding::lambda$keyPressed$0);
      }
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.flag && button != 0 && button != 1 && button != 2) {
         this.feature.setBind(-100 + button);
         this.flag = false;
      } else {
         if (button == 0 && this.animatedValue3.getValue() > 0.05F) {
            if (Geometry.computeFlag(mouseX, mouseY, (double)this.floatValVal9, (double)this.floatValVal10, (double)this.floatValVal11, (double)this.floatValVal12)) {
               this.flag = true;
               return;
            }

            if (Geometry.computeFlag(mouseX, mouseY, (double)this.floatValVal13, (double)this.floatValVal14, (double)this.floatValVal15, (double)this.floatValVal16)) {
               this.feature.setBindHold(false);
               return;
            }

            if (Geometry.computeFlag(mouseX, mouseY, (double)this.floatValVal17, (double)this.floatValVal18, (double)this.floatValVal19, (double)this.floatValVal20)) {
               this.feature.setBindHold(true);
               return;
            }

            if (Geometry.computeFlag(mouseX, mouseY, (double)this.floatValVal5, (double)this.floatValVal6, (double)this.floatValVal7, (double)this.floatValVal8)) {
               return;
            }
         }

         if (button == 0 && Geometry.computeFlag(mouseX, mouseY, (double)this.floatVal, (double)this.floatValVal2, (double)this.floatValVal3, (double)this.floatValVal4)) {
            this.flag2 = !this.flag2;
            this.flag = false;
         } else if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getFloat())) {
            switch (button) {
               case 0:
                  this.feature.toggle();
                  break;
               case 1:
                  if (!this.list.isEmpty()) {
                     this.run2();
                  }
                  break;
               case 2:
                  this.flag2 = true;
                  this.flag = true;
            }
         } else {
            if (!Geometry.computeFlag(mouseX, mouseY, (double)this.floatValVal5, (double)this.floatValVal6, (double)this.floatValVal7, (double)this.floatValVal8)) {
               this.flag2 = false;
               this.flag = false;
            }

            if (this.isOpen() && button == 0 && this.feature instanceof Interface) {
               float var6 = this.getY()
                  + this.getFloat()
                  + InterfaceMetrics.computeFloat(3.0F)
                  + InterfaceMetrics.computeFloat(48.0F) * (float)this.animatedValue3.getValue();
               float var7 = InterfaceMetrics.computeFloat(8.0F);

               for (SettingRow var9 : this.list) {
                  float var10 = (float)var9.getAnimatedValue().getValue();
                  if (var10 > 0.0F) {
                     var6 += (var9.getHeight() + InterfaceMetrics.computeFloat(3.0F)) * var10;
                  }
               }

               float var16 = InterfaceMetrics.computeFloat(5.5F);
               float var17 = this.getWidth() - var7 * 2.0F;
               float var18 = InterfaceMetrics.computeFloat(16.0F);
               float var11 = this.getX() + var7;
               float var12 = var6 + var16 + InterfaceMetrics.computeFloat(3.0F);
               float var13 = var17 / 5.0F;
               if (mouseX >= (double)var11 && mouseX <= (double)(var11 + var17) && mouseY >= (double)var12 && mouseY <= (double)(var12 + var18)) {
                  int var14 = (int)((mouseX - (double)var11) / (double)var13);
                  if (var14 >= 0 && var14 < 5) {
                     ColorTheme var15;
                     switch (var14) {
                        case 0:
                           var15 = new ColorTheme("Red");
                           var15.computeNext("Primary").setColor(new Color(245, 60, 60));
                           var15.computeNext("Secondary").setColor(new Color(200, 10, 10));
                           break;
                        case 1:
                           var15 = new ColorTheme("Blue");
                           var15.computeNext("Primary").setColor(new Color(55, 130, 255));
                           var15.computeNext("Secondary").setColor(new Color(0, 70, 200));
                           break;
                        case 2:
                           var15 = new ColorTheme("Green");
                           var15.computeNext("Primary").setColor(new Color(40, 215, 100));
                           var15.computeNext("Secondary").setColor(new Color(10, 200, 10));
                           break;
                        case 3:
                           var15 = new ColorTheme("Purple");
                           var15.computeNext("Primary").setColor(new Color(175, 60, 255));
                           var15.computeNext("Secondary").setColor(new Color(120, 0, 200));
                           break;
                        default:
                           var15 = new ColorTheme("White");
                           var15.computeNext("Primary").setColor(new Color(245, 245, 245));
                           var15.computeNext("Secondary").setColor(new Color(220, 220, 220));
                     }

                     PVEScreen.getInstance().handleName(var15);
                     return;
                  }
               }
            }

            if (!this.check()) {
               this.list.forEach(Binding::lambda$mouseClicked$1);
            }
         }
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
      if (!this.check()) {
         this.list.forEach(Binding::lambda$mouseReleased$2);
      }
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double h, double v) {
   }

   @Override
   public float getFloat() {
      return InterfaceMetrics.computeFloat(18.5F);
   }

   public List<SettingRow> getList() {
      return this.list;
   }

   public Module getFeature() {
      return this.feature;
   }

   @Override
   public boolean check() {
      return this.flag;
   }

   public boolean check2() {
      return this.flag2;
   }

   public String getString() {
      return this.string;
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

   public float getValue() {
      return this.floatVal;
   }

   @Override
   public float getFloat2() {
      return this.floatValVal2;
   }

   public float getFloat3() {
      return this.floatValVal3;
   }

   public float getFloat4() {
      return this.floatValVal4;
   }

   public float getFloat5() {
      return this.floatValVal5;
   }

   public float getFloat6() {
      return this.floatValVal6;
   }

   public float getFloat7() {
      return this.floatValVal7;
   }

   public float getFloat8() {
      return this.floatValVal8;
   }

   public float getFloat9() {
      return this.floatValVal9;
   }

   public float getFloat10() {
      return this.floatValVal10;
   }

   public float getFloat11() {
      return this.floatValVal11;
   }

   public float getFloat12() {
      return this.floatValVal12;
   }

   public float getFloat13() {
      return this.floatValVal13;
   }

   public float getFloat14() {
      return this.floatValVal14;
   }

   public float getFloat15() {
      return this.floatValVal15;
   }

   public float getFloat16() {
      return this.floatValVal16;
   }

   public float getFloat17() {
      return this.floatValVal17;
   }

   public float getFloat18() {
      return this.floatValVal18;
   }

   public float getFloat19() {
      return this.floatValVal19;
   }

   public float getFloat20() {
      return this.floatValVal20;
   }

   public long getLong() {
      return this.longVal;
   }

   public int getCase() {
      ColorTheme var1 = White.getInstance().getTheme();
      if (var1 == null) {
         return 4;
      } else {
         String var2 = var1.getName();
         if ("Red".equalsIgnoreCase(var2)) {
            return 0;
         } else if ("Blue".equalsIgnoreCase(var2)) {
            return 1;
         } else if ("Green".equalsIgnoreCase(var2)) {
            return 2;
         } else {
            return "Purple".equalsIgnoreCase(var2) ? 3 : 4;
         }
      }
   }

   public static void lambda$mouseReleased$2(double mouseX, double mouseY, int button, SettingRow s) {
      if ((double)s.clamp() > 0.1) {
         s.mouseReleased(mouseX, mouseY, button);
      }
   }

   public static void lambda$mouseClicked$1(double mouseX, double mouseY, int button, SettingRow s) {
      if ((double)s.clamp() > 0.1) {
         s.mouseClicked(mouseX, mouseY, button);
      }
   }

   public static void lambda$keyPressed$0(int keyCode, int scanCode, int modifiers, SettingRow s) {
      if ((double)s.clamp() > 0.1) {
         s.keyPressed(keyCode, scanCode, modifiers);
      }
   }
}
