package dev.daamky.client.gui;

import dev.daamky.client.font.FontRegistry;
import dev.daamky.client.font.IconFontRegistry;
import dev.daamky.client.internal.Direction;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.GlyphMap;
import dev.daamky.client.internal.MaxMinHelper;
import dev.daamky.client.internal.RenderKit;
import dev.daamky.client.internal.RenderQueue;
import dev.daamky.client.internal.TintColorHelper;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.model.enums.AnimationDirection;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.render.Boxes;
import dev.daamky.client.module.render.Chams;
import dev.daamky.client.module.render.ChinaHat;
import dev.daamky.client.module.render.NameTags;
import dev.daamky.client.module.render.SkeletonESP;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ESPConfiguratorScreen extends Screen {
   public final Screen parentScreen;
   public final MinecraftClient class_310 = net.minecraft.client.MinecraftClient.getInstance();
   public final Direction direction = new Direction(260, 1.0, AnimationDirection.FORWARDS);
   public boolean flag = false;
   public long timeStamp = 0L;
   public final Direction2 mqiwt = new Direction2(220, 1.0, AnimationDirection.FORWARDS);
   public float floatVal = -1.0F;
   public float float2 = -1.0F;
   public int intVal = 0;
   public float momevou = 0.0F;
   public float float3 = 0.0F;
   public float float4 = 0.0F;
   public FloatSetting floatSetting = null;
   public ColorSetting colorSetting = null;
   public ColorSetting colorSetting2 = null;
   public final Map<ColorSetting, Float> map = new HashMap<>();
   public final Map<Module, Float> map2 = new HashMap<>();
   public final Map<BooleanSetting, Float> map3 = new HashMap<>();
   public final Map<String, Float> map4 = new HashMap<>();
   public final Map<String, Float> map5 = new HashMap<>();

   public static int computeRound(int r, int g, int b, float a) {
      int var4 = MathHelper.clamp(Math.round(a * 255.0F), 0, 255);
      return var4 << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
   }

   public static float computeFloat(float start, float end, float delta) {
      return start + delta * (end - start);
   }

   public ESPConfiguratorScreen(Screen parentScreen) {
      super(Text.literal("ESP Configurator"));
      this.parentScreen = parentScreen;
   }

   public void init() {
      super.init();
      this.flag = false;
      this.direction.setDirection(AnimationDirection.FORWARDS);
      this.mqiwt.setDirection(AnimationDirection.FORWARDS);
      this.momevou = 0.0F;
      this.float3 = 0.0F;
      this.floatVal = -1.0F;
      this.float2 = -1.0F;
   }

   public void runCurrent() {
      if (!this.flag) {
         this.flag = true;
         this.timeStamp = System.currentTimeMillis();
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.runCurrent();
         return true;
      } else {
         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.float3 -= (float)(verticalAmount * 28.0);
      this.float3 = MathHelper.clamp(this.float3, 0.0F, Math.max(0.0F, this.float4));
      return true;
   }

   public double getDouble() {
      double var1 = this.class_310.getWindow().getScaleFactor();
      return var1 > 0.0 ? var1 : 2.0;
   }

   public float getFloat() {
      return (float)(950.0 / this.getDouble());
   }

   public float getFloat2() {
      return (float)(580.0 / this.getDouble());
   }

   public float getMin() {
      float var1 = (float)this.width * 0.9F;
      float var2 = (float)this.height * 0.9F;
      float var3 = this.getFloat();
      float var4 = this.getFloat2();
      float var5 = Math.min(var1 / var3, var2 / var4);
      return Math.min(1.0F, var5);
   }

   public float computeFloat(float u) {
      return this.getFloat() * u;
   }

   public float computeFloat2(float u) {
      return this.getFloat2() * u;
   }

   public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
      float var5 = this.direction.getEnd();
      if (this.flag) {
         float var6 = (float)(System.currentTimeMillis() - this.timeStamp) / 160.0F;
         if (var6 >= 1.0F) {
            if (this.client != null) {
               this.client.setScreen(this.parentScreen);
            }

            return;
         }

         var5 = Math.max(0.0F, 1.0F - var6);
      }

      if (this.parentScreen != null) {
         this.parentScreen.render(ctx, 0, 0, delta);
      }

      int var31 = computeRound(0, 0, 0, 0.62F * var5);
      RenderKit.Render2D.handleFloat(0.0F, 0.0F, (float)this.width, (float)this.height, var31);
      int var7 = MainMenuScreen.getAccent();
      boolean var8 = MainMenuScreen.isDayMode;
      FontRegistry var9 = IconFontRegistry.fontRegistry3;
      FontRegistry var10 = IconFontRegistry.fontRegistry2;
      FontRegistry var11 = IconFontRegistry.fontRegistry;
      float var12 = this.getMin();
      float var13 = this.computeFloat(var12);
      float var14 = this.computeFloat2(var12);
      float var15 = ((float)this.width - var13) / 2.0F;
      float var16 = ((float)this.height - var14) / 2.0F;
      float var17 = 0.94F + 0.06F * var5;
      float var18 = var15 + var13 / 2.0F;
      float var19 = var16 + var14 / 2.0F;
      MatrixStack var20 = ctx.getMatrices();
      UHalfTexelSizeHelper.handleClass_4587(var20);
      var20.push();
      var20.translate(var18, var19, 0.0F);
      var20.scale(var17, var17, 1.0F);
      var20.translate(-var18, -var19, 0.0F);
      TintColorHelper.class_4587.push();
      TintColorHelper.class_4587.translate(var18, var19, 0.0F);
      TintColorHelper.class_4587.scale(var17, var17, 1.0F);
      TintColorHelper.class_4587.translate(-var18, -var19, 0.0F);
      float var21 = 9.0F * var12;
      float var22 = Math.max(1.0F, 0.8F * var12);
      int var23 = var8 ? -1 : -16316663;
      int var24 = var8 ? -3418655 : -14671832;
      TintColorHelper.handleFloat(var15, var16, var13, var14, var5, var21, var23);
      RenderKit.Render2D.handleFloat(var15, var16, var13, var14, GlyphMap.computeRound2(var24, var5 * 0.35F), var21);
      float var25 = (var8 ? 0.16F : 0.1F) * var5;
      RenderKit.Render2D.handleFloat(
         var15 + var22, var16 + var22, var13 - var22 * 2.0F, var14 - var22 * 2.0F, GlyphMap.computeRound2(var23, var25), Math.max(0.5F, var21 - var22)
      );
      float var26 = 30.0F * var12;
      this.handleMax(var15, var16, var13, var26, var12, mouseX, mouseY, var5, var7, var8, var9, var10, var11);
      float var27 = var15 + 12.0F * var12;
      float var28 = var16 + var26 + 4.0F * var12;
      float var29 = var13 - 24.0F * var12;
      float var30 = var14 - var26 - 10.0F * var12;
      this.initAdd(ctx, var27, var28, var29, var30, var12, mouseX, mouseY, var5, var7, var8, var9, var10, var11);
      TintColorHelper.class_4587.pop();
      var20.pop();
      this.momevou = computeFloat(this.momevou, this.float3, 0.25F);
   }

   public void handleMax(
      float winX,
      float winY,
      float winW,
      float headH,
      float u,
      int mx,
      int my,
      float alpha,
      int accent,
      boolean isDay,
      FontRegistry fontBody,
      FontRegistry fontMed,
      FontRegistry fontBold
   ) {
      String[] var14 = new String[]{EngHelper.computeString("All"), "Chams", "Boxes", "NameTags", "China Hat", "Skeleton"};
      float var15 = 14.5F * u;
      float var16 = 4.0F * u;
      float var17 = 4.0F * u;
      float var18 = 6.2F * u;
      float var19 = Math.max(1.0F, 0.75F * u);
      float var20 = 0.0F;
      float[] var21 = new float[var14.length];

      for (int var22 = 0; var22 < var14.length; var22++) {
         var21[var22] = fontMed.computeDelegate(var14[var22], var18) + 14.0F * u;
         var20 += var21[var22];
      }

      var20 += (float)(var14.length - 1) * var17;
      float var44 = winX + (winW - var20) / 2.0F;
      float var23 = winY + (headH - var15) / 2.0F;
      float var24 = var44;
      float var25 = var21[0];
      float var26 = var44;
      float[] var27 = new float[var14.length];

      for (int var28 = 0; var28 < var14.length; var28++) {
         var27[var28] = var26;
         if (this.intVal == var28) {
            var24 = var27[var28];
            var25 = var21[var28];
         }

         var26 += var21[var28] + var17;
      }

      if (this.floatVal < 0.0F) {
         this.floatVal = var24;
         this.float2 = var25;
      } else {
         this.floatVal = computeFloat(this.floatVal, var24, 0.22F);
         this.float2 = computeFloat(this.float2, var25, 0.22F);
      }

      int var45 = isDay ? computeRound(241, 245, 249, 0.45F * alpha) : computeRound(20, 24, 34, 0.4F * alpha);
      int var29 = isDay ? computeRound(0, 0, 0, 0.08F * alpha) : computeRound(255, 255, 255, 0.07F * alpha);

      for (int var30 = 0; var30 < var14.length; var30++) {
         boolean var31 = MaxMinHelper.computeFlag((float)mx, (float)my, var27[var30], var23, var21[var30], var15);
         int var32 = var45;
         if (var31 && this.intVal != var30) {
            var32 = GlyphMap.computeInt2(var45, isDay ? computeRound(226, 232, 240, 0.65F * alpha) : computeRound(35, 42, 58, 0.55F * alpha), 0.65F);
         }

         RenderKit.Render2D.handleFloat(var27[var30], var23, var21[var30], var15, var29, var16);
         RenderKit.Render2D.handleFloat(
            var27[var30] + var19, var23 + var19, var21[var30] - var19 * 2.0F, var15 - var19 * 2.0F, var32, Math.max(0.5F, var16 - var19)
         );
      }

      int var46 = accent & 16777215;
      float var47 = 0.14F;
      float var48 = 0.5F * u;
      float var33 = 1.4F * u;
      float[] var34 = new float[]{0.24F, 0.16F, 0.1F, 0.05F, 0.02F};
      float[] var35 = new float[]{0.4F, 0.9F, 1.6F, 2.6F, 3.8F};
      float[] var36 = new float[]{0.55F, 0.7F, 0.85F, 1.0F, 1.1F};

      for (int var37 = 0; var37 < 5; var37++) {
         float var38 = var35[var37] * u;
         float var39 = var34[var37] * var47 * alpha * (isDay ? 0.45F : 1.0F);
         int var40 = var46 | (int)(var39 * 255.0F) << 24;
         RenderKit.Render2D.handleFloat(
            this.floatVal - var38 + var48, var23 + var33 * var36[var37], this.float2 + var38 * 2.0F, var15 - var33 * 0.35F + var38, var40, var16 + var38
         );
      }

      RenderKit.Render2D.handleFloat(this.floatVal, var23, this.float2, var15, GlyphMap.computeRound2(accent, 0.92F * alpha), var16);

      for (int var49 = 0; var49 < var14.length; var49++) {
         boolean var50 = this.intVal == var49;
         boolean var51 = MaxMinHelper.computeFlag((float)mx, (float)my, var27[var49], var23, var21[var49], var15);
         int var52 = GlyphMap.computeRound(MainMenuScreen.computeFlag(accent) ? 20 : 255, alpha);
         int var41 = isDay ? -10193781 : computeRound(155, 170, 192, 0.78F * alpha);
         int var42 = var50 ? var52 : (var51 ? (isDay ? -15788246 : computeRound(225, 235, 250, alpha)) : var41);
         fontMed.handleDelegate2(
            var14[var49], var27[var49] + var21[var49] / 2.0F, var23 + (var15 - fontMed.computeDelegate(var18)) / 2.0F - 0.8F * u, var18, var42
         );
      }

      RenderKit.Render2D.handleFloat(winX + 10.0F * u, winY + headH, winW - 20.0F * u, 1.0F, computeRound(255, 255, 255, 0.05F * alpha));
   }

   public void handleDegrees(MatrixStack ms, float cx, float cy, float size, int color) {
      float var6 = size / 2.0F;
      float var7 = Math.max(1.0F, 1.2F);
      ms.push();
      ms.translate(cx, cy, 0.0F);
      ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0F));
      RenderKit.Render2D.handleFloat(-var6, -var7 / 2.0F, size, var7, color, var7 / 2.0F);
      RenderKit.Render2D.handleFloat(-var7 / 2.0F, -var6, var7, size, color, var7 / 2.0F);
      ms.pop();
   }

   public void initAdd(
      DrawContext ctx,
      float bodyX,
      float bodyY,
      float bodyW,
      float bodyH,
      float u,
      int mx,
      int my,
      float alpha,
      int accent,
      boolean isDay,
      FontRegistry fontBody,
      FontRegistry fontMed,
      FontRegistry fontBold
   ) {
      RenderQueue.handleFloat(bodyX, bodyY, bodyW, bodyH);
      float var15 = this.mqiwt.getEnd();
      float var16 = alpha * MathHelper.clamp(var15, 0.0F, 1.0F);
      float var17 = (1.0F - var15) * 6.0F * u;
      ESPConfiguratorScreen.EspItem[] var18 = ESPConfiguratorScreen.EspItem.values();
      ArrayList var19 = new ArrayList();
      if (this.intVal == 0) {
         Collections.addAll(var19, var18);
      } else {
         int var20 = this.intVal - 1;
         if (var20 >= 0 && var20 < var18.length) {
            var19.add(var18[var20]);
         }
      }

      float var30 = 8.0F * u;
      float var21 = 0.0F;
      if (this.intVal == 0) {
         float var22 = (bodyW - var30) / 2.0F;
         float[] var23 = new float[]{bodyY - this.momevou + var17, bodyY - this.momevou + var17};

         for (ESPConfiguratorScreen.EspItem var25 : var19) {
            float var26 = this.computeNext(var25, var22, u);
            int var27 = var23[0] <= var23[1] ? 0 : 1;
            float var28 = bodyX + (float)var27 * (var22 + var30);
            float var29 = var23[var27];
            var23[var27] += var26 + var30;
            if (var29 + var26 >= bodyY - 20.0F && var29 <= bodyY + bodyH + 20.0F) {
               this.handleName(ctx, var28, var29, var22, var26, var25, u, mx, my, var16, accent, isDay, fontBody, fontMed, fontBold);
            }
         }

         var21 = Math.max(var23[0], var23[1]) + this.momevou - var17 - bodyY;
      } else {
         float var32 = Math.min(bodyW, 400.0F * u);
         float var33 = bodyX + (bodyW - var32) / 2.0F;
         ESPConfiguratorScreen.EspItem var34 = (ESPConfiguratorScreen.EspItem)var19.get(0);
         float var35 = this.computeNext(var34, var32, u);
         float var36 = bodyY - this.momevou + var17;
         this.handleName(ctx, var33, var36, var32, var35, var34, u, mx, my, var16, accent, isDay, fontBody, fontMed, fontBold);
         var21 = var35 + 10.0F * u;
      }

      this.float4 = Math.max(0.0F, var21 - bodyH);
      RenderQueue.runGl();
   }

   public float computeNext(ESPConfiguratorScreen.EspItem item, float cardW, float u) {
      float var4 = 11.0F * u;
      float var5 = IconFontRegistry.fontRegistry.computeDelegate(8.5F * u);
      float var6 = IconFontRegistry.fontRegistry3.computeDelegate(6.8F * u);
      float var7 = Math.max(1.0F, 0.75F * u);
      float var8 = var4 + var5 + 3.0F * u + var6 + 6.0F * u + var7 + 7.0F * u;

      for (Setting var10 : item.feature.getSettings()) {
         if (var10.isVisible()) {
            var8 += this.computeDefault(var10, u) + 3.5F * u;
         }
      }

      return var8 + 7.0F * u;
   }

   public float computeDefault(Setting<?> s, float u) {
      if (s instanceof FloatSetting) {
         return 22.0F * u;
      } else if (s instanceof BooleanSetting) {
         return 16.0F * u;
      } else if (s instanceof ModeSetting) {
         return 28.0F * u;
      } else if (s instanceof MultiBooleanSetting) {
         return 26.0F * u;
      } else if (s instanceof ColorSetting var3) {
         float var4 = this.map.getOrDefault(var3, 0.0F);
         return 17.0F * u + 40.0F * u * var4;
      } else {
         return s instanceof ButtonSetting ? 19.0F * u : 19.0F * u;
      }
   }

   public void handleName(
      DrawContext ctx,
      float cardX,
      float cardY,
      float cardW,
      float cardH,
      ESPConfiguratorScreen.EspItem item,
      float u,
      int mx,
      int my,
      float alpha,
      int accent,
      boolean isDay,
      FontRegistry fontBody,
      FontRegistry fontMed,
      FontRegistry fontBold
   ) {
      Module var16 = item.feature;
      boolean var17 = var16.isEnabled();
      float var18 = var17 ? 1.0F : 0.0F;
      float var19 = this.map2.getOrDefault(var16, var18);
      var19 = computeFloat(var19, var18, 0.22F);
      this.map2.put(var16, var19);
      float var20 = 7.0F * u;
      float var21 = Math.max(1.0F, 0.75F * u);
      int var22 = accent & 16777215;
      float var23 = var19 * 0.02F + 0.08F;
      float var24 = 0.5F * u;
      float var25 = 1.6F * u;
      float[] var26 = new float[]{0.24F, 0.16F, 0.1F, 0.05F, 0.02F};
      float[] var27 = new float[]{0.4F, 0.9F, 1.6F, 2.6F, 3.8F};
      float[] var28 = new float[]{0.55F, 0.7F, 0.85F, 1.0F, 1.1F};

      for (int var29 = 0; var29 < 5; var29++) {
         float var30 = var27[var29] * u;
         float var31 = var26[var29] * var23 * alpha * (isDay ? 0.45F : 1.0F);
         int var32 = var22 | (int)(var31 * 255.0F) << 24;
         RenderKit.Render2D.handleFloat(
            cardX - var30 + var24, cardY + var25 * var28[var29], cardW + var30 * 2.0F, cardH - var25 * 0.35F + var30, var32, var20 + var30
         );
      }

      int var108 = isDay ? -1 : -15856110;
      int var109 = isDay ? -920071 : -16119283;
      int var110 = GlyphMap.computeInt2(var109, var108, var19);
      boolean var111 = MaxMinHelper.computeFlag((float)mx, (float)my, cardX, cardY, cardW, cardH);
      int var33 = isDay ? -1906448 : -15461348;
      int var34 = var111 ? GlyphMap.computeInt2(var110, var33, 0.2F) : var110;
      int var35 = isDay ? -3418655 : (var19 > 0.05F ? GlyphMap.computeInt2(-14342090, accent, 0.35F * var19) : -14802902);
      float var36 = (isDay ? 0.22F : (var19 > 0.05F ? 0.35F : 0.2F)) * alpha;
      float var37 = (isDay ? 0.38F : (var19 > 0.05F ? 0.38F : 0.28F)) * alpha;
      RenderKit.Render2D.handleFloat(cardX, cardY, cardW, cardH, GlyphMap.computeRound2(var35, var36), var20);
      RenderKit.Render2D.handleFloat(
         cardX + var21, cardY + var21, cardW - var21 * 2.0F, cardH - var21 * 2.0F, GlyphMap.computeRound2(var34, var37), Math.max(0.5F, var20 - var21)
      );
      float var38 = cardX + 12.0F * u;
      float var39 = cardW - 24.0F * u;
      float var40 = 11.0F * u;
      float var41 = cardY + var40;
      float var42 = fontBody.computeDelegate("ESP", 6.2F * u) + 8.0F * u;
      float var43 = 11.5F * u;
      float var44 = cardX + cardW - 12.0F * u - var42;
      float var45 = var41 - 1.0F * u;
      RenderKit.Render2D.handleFloat(var44, var45, var42, var43, computeRound(255, 255, 255, 0.08F * alpha), 3.0F * u);
      RenderKit.Render2D.handleFloat(
         var44 + 0.8F, var45 + 0.8F, var42 - 1.6F, var43 - 1.6F, computeRound(20, 24, 34, 0.85F * alpha), Math.max(0.5F, 3.0F * u - 0.8F)
      );
      fontBody.handleDelegate(
         "ESP", var44 + 4.0F * u, var45 + (var43 - fontBody.computeDelegate(6.2F * u)) / 2.0F - 0.3F * u, 6.2F * u, computeRound(160, 175, 195, 0.75F * alpha)
      );
      float var46 = 18.0F * u;
      float var47 = 10.5F * u;
      float var48 = var44 - 7.0F * u - var46;
      float var49 = var45 + (var43 - var47) / 2.0F;
      int var50 = computeRound(24, 28, 40, alpha);
      int var51 = GlyphMap.computeRound2(accent, alpha);
      int var52 = GlyphMap.computeInt2(var50, var51, var19);
      RenderKit.Render2D.handleFloat(var48, var49, var46, var47, var52, var47 / 2.0F);
      float var53 = var47 - 2.5F * u;
      float var54 = var48 + 1.25F * u + (var46 - var53 - 2.5F * u) * var19;
      float var55 = var49 + 1.25F * u;
      RenderKit.Render2D.handleFloat(var54, var55, var53, var53, computeRound(255, 255, 255, alpha), var53 / 2.0F);
      int var56 = GlyphMap.computeInt2(computeRound(170, 185, 205, 0.85F * alpha), computeRound(255, 255, 255, alpha), var19);
      fontBold.handleDelegate(item.displayName, var38, var41, 8.5F * u, var56);
      float var57 = var41 + fontBold.computeDelegate(8.5F * u) + 3.0F * u;
      fontBody.handleDelegate(EngHelper.computeString(item.desc), var38, var57, 6.8F * u, computeRound(140, 155, 175, 0.7F * alpha));
      float var58 = var57 + fontBody.computeDelegate(6.8F * u) + 6.0F * u;
      int var59 = GlyphMap.computeRound2(isDay ? -15788246 : -1, alpha * (isDay ? 0.08F : 0.06F));
      int var60 = GlyphMap.computeInt2(var59, GlyphMap.computeRound2(accent, alpha * 0.35F), 0.75F);
      int var61 = GlyphMap.computeInt2(var59, var60, var19);
      RenderKit.Render2D.handleFloat(var38, var58, var39, var21, var61, var21 / 2.0F);
      float var62 = var58 + var21 + 7.0F * u;
      float var63 = var38;
      float var64 = var39;

      for (Setting var66 : item.feature.getSettings()) {
         if (var66.isVisible()) {
            if (var66 instanceof FloatSetting) {
               FloatSetting var67 = (FloatSetting)var66;
               float var117 = 22.0F * u;
               float var122 = (var67.getValue() - var67.getMin()) / (var67.getMax() - var67.getMin());
               var122 = MathHelper.clamp(var122, 0.0F, 1.0F);
               fontMed.handleDelegate(EngHelper.computeString(var67.getName()), var63, var62, 7.0F * u, computeRound(215, 225, 240, 0.9F * alpha));
               String var128 = String.format(Locale.US, "%.2f", var67.getValue());
               float var134 = fontMed.computeDelegate(var128, 7.0F * u);
               fontMed.handleDelegate(var128, var63 + var64 - var134, var62, 7.0F * u, GlyphMap.computeRound2(accent, alpha));
               float var139 = var62 + 13.0F * u;
               float var143 = 3.5F * u;
               RenderKit.Render2D.handleFloat(var63, var139, var64, var143, computeRound(28, 34, 48, 0.9F * alpha), var143 / 2.0F);
               float var147 = var64 * var122;
               if (var147 > 0.0F) {
                  RenderKit.Render2D.handleFloat(var63, var139, var147, var143, GlyphMap.computeRound2(accent, alpha), var143 / 2.0F);
               }

               float var151 = 4.0F * u;
               float var155 = var63 + var147;
               float var159 = var139 + var143 / 2.0F;
               RenderKit.Render2D.handleFloat(
                  var155 - var151 - 0.5F * u,
                  var159 - var151 - 0.5F * u,
                  (var151 + 0.5F * u) * 2.0F,
                  (var151 + 0.5F * u) * 2.0F,
                  computeRound(0, 0, 0, 0.35F * alpha),
                  var151 + 0.5F * u
               );
               RenderKit.Render2D.handleFloat(var155 - var151, var159 - var151, var151 * 2.0F, var151 * 2.0F, computeRound(255, 255, 255, alpha), var151);
               var62 += var117 + 3.5F * u;
            } else if (var66 instanceof BooleanSetting var68) {
               float var116 = 16.0F * u;
               boolean var121 = var68.getValue();
               float var127 = var121 ? 1.0F : 0.0F;
               float var132 = this.map3.getOrDefault(var68, var127);
               var132 = computeFloat(var132, var127, 0.24F);
               this.map3.put(var68, var132);
               fontMed.handleDelegate(
                  EngHelper.computeString(var68.getName()),
                  var63,
                  var62 + (var116 - fontMed.computeDelegate(7.0F * u)) / 2.0F,
                  7.0F * u,
                  computeRound(215, 225, 240, 0.9F * alpha)
               );
               float var138 = 18.0F * u;
               float var142 = 10.5F * u;
               float var146 = var63 + var64 - var138;
               float var150 = var62 + (var116 - var142) / 2.0F;
               int var154 = computeRound(28, 34, 48, 0.9F * alpha);
               int var158 = GlyphMap.computeRound2(accent, 0.88F * alpha);
               int var162 = GlyphMap.computeInt2(var154, var158, var132);
               RenderKit.Render2D.handleFloat(var146, var150, var138, var142, var162, var142 / 2.0F);
               float var165 = var142 - 2.5F * u;
               float var168 = var146 + 1.25F * u + (var138 - var165 - 2.5F * u) * var132;
               float var171 = var150 + 1.25F * u;
               RenderKit.Render2D.handleFloat(var168, var171, var165, var165, computeRound(255, 255, 255, alpha), var165 / 2.0F);
               var62 += var116 + 3.5F * u;
            } else if (var66 instanceof ModeSetting var69) {
               float var115 = 28.0F * u;
               fontMed.handleDelegate(EngHelper.computeString(var69.getName()), var63, var62, 7.0F * u, computeRound(215, 225, 240, 0.9F * alpha));
               List var120 = var69.getValues();
               float var126 = var63;
               float var131 = var62 + 10.5F * u;
               float var137 = 13.5F * u;
               float var141 = 3.5F * u;
               float var145 = Math.max(0.8F, 0.75F * u);
               boolean var149 = MainMenuScreen.computeFlag(accent);

               for (String var157 : var120) {
                  String var161 = EngHelper.computeString(var157);
                  float var164 = fontBody.computeDelegate(var161, 6.4F * u) + 10.0F * u;
                  boolean var167 = var157.equalsIgnoreCase(var69.getValue());
                  String var170 = var69.getName() + ":" + var157;
                  float var174 = var167 ? 1.0F : 0.0F;
                  float var176 = this.map4.getOrDefault(var170, var174);
                  var176 = computeFloat(var176, var174, 0.22F);
                  this.map4.put(var170, var176);
                  boolean var179 = MaxMinHelper.computeFlag((float)mx, (float)my, var126, var131, var164, var137);
                  float var182 = var179 ? 1.0F : 0.0F;
                  float var185 = this.map5.getOrDefault(var170, var182);
                  var185 = computeFloat(var185, var182, 0.22F);
                  this.map5.put(var170, var185);
                  if (var176 > 0.01F) {
                     float var190 = var176 * 0.12F;
                     float[] var195 = new float[]{0.24F, 0.14F, 0.05F};
                     float[] var200 = new float[]{0.4F, 1.0F, 2.0F};

                     for (int var205 = 0; var205 < 3; var205++) {
                        float var209 = var200[var205] * u;
                        float var213 = var195[var205] * var190 * alpha * (isDay ? 0.45F : 1.0F);
                        int var216 = var22 | (int)(var213 * 255.0F) << 24;
                        RenderKit.Render2D.handleFloat(
                           var126 - var209 + 0.3F * u, var131 + 1.0F * u + var209 * 0.4F, var164 + var209 * 2.0F, var137 + var209, var216, var141 + var209
                        );
                     }
                  }

                  int var191 = isDay ? computeRound(235, 240, 245, (0.65F + 0.2F * var185) * alpha) : computeRound(22, 27, 38, (0.75F + 0.2F * var185) * alpha);
                  int var196 = isDay
                     ? GlyphMap.computeRound2(-15788246, (0.85F + 0.15F * var185) * alpha)
                     : GlyphMap.computeRound2(accent, (0.82F + 0.18F * var185) * alpha);
                  int var201 = GlyphMap.computeInt2(var191, var196, var176);
                  int var206 = isDay ? computeRound(0, 0, 0, (0.08F + 0.08F * var185) * alpha) : computeRound(255, 255, 255, (0.07F + 0.08F * var185) * alpha);
                  int var210 = isDay ? GlyphMap.computeRound2(-15788246, 0.95F * alpha) : GlyphMap.computeRound2(accent, 0.95F * alpha);
                  int var214 = GlyphMap.computeInt2(var206, var210, var176);
                  int var217 = isDay
                     ? computeRound(100, 116, 139, (0.85F + 0.15F * var185) * alpha)
                     : computeRound(155, 170, 190, (0.75F + 0.2F * var185) * alpha);
                  int var219 = isDay ? computeRound(255, 255, 255, alpha) : (var149 ? computeRound(15, 23, 42, alpha) : computeRound(255, 255, 255, alpha));
                  int var220 = GlyphMap.computeInt2(var217, var219, var176);
                  RenderKit.Render2D.handleFloat(var126, var131, var164, var137, var214, var141);
                  RenderKit.Render2D.handleFloat(
                     var126 + var145, var131 + var145, var164 - var145 * 2.0F, var137 - var145 * 2.0F, var201, Math.max(0.5F, var141 - var145)
                  );
                  fontBody.handleDelegate2(
                     var161, var126 + var164 / 2.0F, var131 + (var137 - fontBody.computeDelegate(6.4F * u)) / 2.0F - 0.7F * u, 6.4F * u, var220
                  );
                  var126 += var164 + 3.0F * u;
               }

               var62 += var115 + 3.5F * u;
            } else if (var66 instanceof MultiBooleanSetting var70) {
               float var114 = 26.0F * u;
               fontMed.handleDelegate(EngHelper.computeString(var70.getName()), var63, var62, 7.0F * u, computeRound(215, 225, 240, 0.9F * alpha));
               float var119 = var63;
               float var125 = var62 + 10.5F * u;
               float var130 = 13.5F * u;
               float var136 = 3.5F * u;
               float var140 = Math.max(0.8F, 0.75F * u);
               boolean var144 = MainMenuScreen.computeFlag(accent);

               for (BooleanSetting var152 : var70.getValue()) {
                  String var156 = EngHelper.computeString(var152.getName());
                  float var160 = fontBody.computeDelegate(var156, 6.2F * u) + 9.0F * u;
                  boolean var163 = var152.getValue();
                  String var166 = var70.getName() + ":" + var152.getName();
                  float var169 = var163 ? 1.0F : 0.0F;
                  float var172 = this.map4.getOrDefault(var166, var169);
                  var172 = computeFloat(var172, var169, 0.22F);
                  this.map4.put(var166, var172);
                  boolean var175 = MaxMinHelper.computeFlag((float)mx, (float)my, var119, var125, var160, var130);
                  float var178 = var175 ? 1.0F : 0.0F;
                  float var180 = this.map5.getOrDefault(var166, var178);
                  var180 = computeFloat(var180, var178, 0.22F);
                  this.map5.put(var166, var180);
                  if (var172 > 0.01F) {
                     float var183 = var172 * 0.12F;
                     float[] var188 = new float[]{0.24F, 0.14F, 0.05F};
                     float[] var193 = new float[]{0.4F, 1.0F, 2.0F};

                     for (int var198 = 0; var198 < 3; var198++) {
                        float var203 = var193[var198] * u;
                        float var207 = var188[var198] * var183 * alpha * (isDay ? 0.45F : 1.0F);
                        int var211 = var22 | (int)(var207 * 255.0F) << 24;
                        RenderKit.Render2D.handleFloat(
                           var119 - var203 + 0.3F * u, var125 + 1.0F * u + var203 * 0.4F, var160 + var203 * 2.0F, var130 + var203, var211, var136 + var203
                        );
                     }
                  }

                  int var184 = isDay ? computeRound(235, 240, 245, (0.65F + 0.2F * var180) * alpha) : computeRound(22, 27, 38, (0.75F + 0.2F * var180) * alpha);
                  int var189 = isDay
                     ? GlyphMap.computeRound2(-15788246, (0.85F + 0.15F * var180) * alpha)
                     : GlyphMap.computeRound2(accent, (0.82F + 0.18F * var180) * alpha);
                  int var194 = GlyphMap.computeInt2(var184, var189, var172);
                  int var199 = isDay ? computeRound(0, 0, 0, (0.08F + 0.08F * var180) * alpha) : computeRound(255, 255, 255, (0.07F + 0.08F * var180) * alpha);
                  int var204 = isDay ? GlyphMap.computeRound2(-15788246, 0.95F * alpha) : GlyphMap.computeRound2(accent, 0.95F * alpha);
                  int var208 = GlyphMap.computeInt2(var199, var204, var172);
                  int var212 = isDay
                     ? computeRound(100, 116, 139, (0.85F + 0.15F * var180) * alpha)
                     : computeRound(155, 170, 190, (0.75F + 0.2F * var180) * alpha);
                  int var215 = isDay ? computeRound(255, 255, 255, alpha) : (var144 ? computeRound(15, 23, 42, alpha) : computeRound(255, 255, 255, alpha));
                  int var218 = GlyphMap.computeInt2(var212, var215, var172);
                  RenderKit.Render2D.handleFloat(var119, var125, var160, var130, var208, var136);
                  RenderKit.Render2D.handleFloat(
                     var119 + var140, var125 + var140, var160 - var140 * 2.0F, var130 - var140 * 2.0F, var194, Math.max(0.5F, var136 - var140)
                  );
                  fontBody.handleDelegate2(
                     var156, var119 + var160 / 2.0F, var125 + (var130 - fontBody.computeDelegate(6.2F * u)) / 2.0F - 0.7F * u, 6.2F * u, var218
                  );
                  var119 += var160 + 3.0F * u;
               }

               var62 += var114 + 3.5F * u;
            } else if (var66 instanceof ColorSetting var71) {
               float var112 = this.map.getOrDefault(var71, 0.0F);
               float var118 = this.colorSetting == var71 ? 1.0F : 0.0F;
               var112 = computeFloat(var112, var118, 0.26F);
               this.map.put(var71, var112);
               float var124 = 17.0F * u;
               float var129 = 40.0F * u * var112;
               float var135 = var124 + var129;
               fontMed.handleDelegate(
                  EngHelper.computeString(var71.getName()),
                  var63,
                  var62 + (var124 - fontMed.computeDelegate(7.0F * u)) / 2.0F,
                  7.0F * u,
                  computeRound(215, 225, 240, 0.9F * alpha)
               );
               Color var78 = var71.getValue();
               float var79 = 22.0F * u;
               float var80 = 11.0F * u;
               float var81 = var63 + var64 - var79;
               float var82 = var62 + (var124 - var80) / 2.0F;
               int var83 = computeRound(var78.getRed(), var78.getGreen(), var78.getBlue(), (float)var78.getAlpha() / 255.0F * alpha);
               int var84 = this.colorSetting == var71 ? GlyphMap.computeRound2(accent, alpha) : computeRound(255, 255, 255, 0.2F * alpha);
               RenderKit.Render2D.handleFloat(var81, var82, var79, var80, var84, 3.0F * u);
               RenderKit.Render2D.handleFloat(var81 + 0.8F, var82 + 0.8F, var79 - 1.6F, var80 - 1.6F, var83, Math.max(0.5F, 3.0F * u - 0.8F));
               if (var112 > 0.02F) {
                  float var85 = var63 + 3.0F * u;
                  float var86 = var62 + var124 + 2.5F * u;
                  float var87 = var64 - 6.0F * u;
                  float var88 = 33.0F * u * var112;
                  float var89 = 3.5F * u;
                  byte var90 = 36;
                  float var91 = var87 / (float)var90;

                  for (int var92 = 0; var92 < var90; var92++) {
                     float var93 = (float)var92 / (float)var90;
                     float var94 = (float)(var92 + 1) / (float)var90;
                     int var95 = Color.HSBtoRGB(var93, 1.0F, 1.0F);
                     int var96 = Color.HSBtoRGB(var94, 1.0F, 1.0F);
                     int var97 = Color.HSBtoRGB(var93, 1.0F, 0.08F);
                     int var98 = Color.HSBtoRGB(var94, 1.0F, 0.08F);
                     int var99 = GlyphMap.computeRound2(var95, alpha * var112);
                     int var100 = GlyphMap.computeRound2(var96, alpha * var112);
                     int var101 = GlyphMap.computeRound2(var98, alpha * var112);
                     int var102 = GlyphMap.computeRound2(var97, alpha * var112);
                     float var103 = var92 == 0 ? var89 : 0.0F;
                     float var104 = var92 == 0 ? var89 : 0.0F;
                     float var105 = var92 == var90 - 1 ? var89 : 0.0F;
                     float var106 = var92 == var90 - 1 ? var89 : 0.0F;
                     RenderKit.Render2D.handleFloat(
                        var85 + (float)var92 * var91, var86, var91 + 0.5F * u, var88, new int[]{var99, var100, var101, var102}, var103, var105, var106, var104
                     );
                  }

                  float[] var187 = Color.RGBtoHSB(var78.getRed(), var78.getGreen(), var78.getBlue(), null);
                  float var192 = var85 + var87 * var187[0];
                  float var197 = var86 + var88 * (1.0F - var187[2]);
                  float var202 = 3.0F * u;
                  RenderKit.Render2D.handleFloat(
                     var192 - var202 - 0.8F * u,
                     var197 - var202 - 0.8F * u,
                     (var202 + 0.8F * u) * 2.0F,
                     (var202 + 0.8F * u) * 2.0F,
                     computeRound(0, 0, 0, 0.4F * alpha * var112),
                     var202 + 0.8F * u
                  );
                  RenderKit.Render2D.handleFloat(
                     var192 - var202, var197 - var202, var202 * 2.0F, var202 * 2.0F, computeRound(255, 255, 255, alpha * var112), var202
                  );
                  RenderKit.Render2D.handleFloat(
                     var192 - var202 + 0.8F * u, var197 - var202 + 0.8F * u, (var202 - 0.8F * u) * 2.0F, (var202 - 0.8F * u) * 2.0F, var83, var202 - 0.8F * u
                  );
               }

               var62 += var135 + 3.5F * u;
            } else if (var66 instanceof ButtonSetting var72) {
               float var73 = 19.0F * u;
               boolean var74 = MaxMinHelper.computeFlag((float)mx, (float)my, var63, var62, var64, var73);
               int var75 = var74
                  ? GlyphMap.computeRound2(accent, (isDay ? 0.35F : 0.25F) * alpha)
                  : (isDay ? computeRound(240, 244, 248, 0.45F * alpha) : computeRound(24, 29, 41, 0.4F * alpha));
               int var76 = var74
                  ? GlyphMap.computeRound2(accent, 0.9F * alpha)
                  : (isDay ? computeRound(0, 0, 0, 0.08F * alpha) : computeRound(255, 255, 255, 0.08F * alpha));
               RenderKit.Render2D.handleFloat(var63, var62, var64, var73, var76, 3.5F * u);
               RenderKit.Render2D.handleFloat(var63 + 1.0F, var62 + 1.0F, var64 - 2.0F, var73 - 2.0F, var75, Math.max(0.5F, 3.5F * u - 1.0F));
               int var77 = isDay ? (var74 ? -15788246 : -13418155) : computeRound(245, 248, 255, alpha);
               fontMed.handleDelegate2(
                  var72.getName(), var63 + var64 / 2.0F, var62 + (var73 - fontMed.computeDelegate(7.2F * u)) / 2.0F - 0.75F * u, 7.2F * u, var77
               );
               var62 += var73 + 4.0F * u;
            }
         }
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) {
         return super.mouseClicked(mouseX, mouseY, button);
      } else {
         float var6 = this.getMin();
         float var7 = this.computeFloat(var6);
         float var8 = this.computeFloat2(var6);
         float var9 = ((float)this.width - var7) / 2.0F;
         float var10 = ((float)this.height - var8) / 2.0F;
         float var11 = 30.0F * var6;
         String[] var12 = new String[]{EngHelper.computeString("All"), "Chams", "Boxes", "NameTags", "China Hat", "Skeleton"};
         float var13 = 14.5F * var6;
         float var14 = 4.0F * var6;
         float var15 = 6.2F * var6;
         float var16 = 0.0F;
         float[] var17 = new float[var12.length];

         for (int var18 = 0; var18 < var12.length; var18++) {
            var17[var18] = IconFontRegistry.fontRegistry2.computeDelegate(var12[var18], var15) + 14.0F * var6;
            var16 += var17[var18];
         }

         var16 += (float)(var12.length - 1) * var14;
         float var37 = var9 + (var7 - var16) / 2.0F;
         float var19 = var10 + (var11 - var13) / 2.0F;
         float var20 = var37;

         for (int var21 = 0; var21 < var12.length; var21++) {
            float var22 = var17[var21];
            if (MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var20, var19, var22, var13)) {
               if (this.intVal != var21) {
                  this.intVal = var21;
                  this.mqiwt.getTimerUtil();
                  this.mqiwt.setDirection(AnimationDirection.FORWARDS);
                  this.momevou = 0.0F;
                  this.float3 = 0.0F;
               }

               return true;
            }

            var20 += var22 + var14;
         }

         float var38 = var9 + 12.0F * var6;
         float var39 = var10 + var11 + 4.0F * var6;
         float var23 = var7 - 24.0F * var6;
         float var24 = var8 - var11 - 10.0F * var6;
         if (!MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var38, var39, var23, var24)) {
            return super.mouseClicked(mouseX, mouseY, button);
         } else {
            ESPConfiguratorScreen.EspItem[] var25 = ESPConfiguratorScreen.EspItem.values();
            ArrayList var26 = new ArrayList();
            if (this.intVal == 0) {
               Collections.addAll(var26, var25);
            } else {
               int var27 = this.intVal - 1;
               if (var27 >= 0 && var27 < var25.length) {
                  var26.add(var25[var27]);
               }
            }

            float var40 = 8.0F * var6;
            if (this.intVal == 0) {
               float var28 = (var23 - var40) / 2.0F;
               float[] var29 = new float[]{var39 - this.momevou, var39 - this.momevou};

               for (ESPConfiguratorScreen.EspItem var31 : var26) {
                  float var32 = this.computeNext(var31, var28, var6);
                  int var33 = var29[0] <= var29[1] ? 0 : 1;
                  float var34 = var38 + (float)var33 * (var28 + var40);
                  float var35 = var29[var33];
                  var29[var33] += var32 + var40;
                  if (this.computeNext(var31, var34, var35, var28, var32, var6, (float)mouseX, (float)mouseY)) {
                     return true;
                  }
               }
            } else {
               float var41 = Math.min(var23, 400.0F * var6);
               float var42 = var38 + (var23 - var41) / 2.0F;
               ESPConfiguratorScreen.EspItem var43 = (ESPConfiguratorScreen.EspItem)var26.get(0);
               float var44 = this.computeNext(var43, var41, var6);
               float var45 = var39 - this.momevou;
               if (this.computeNext(var43, var42, var45, var41, var44, var6, (float)mouseX, (float)mouseY)) {
                  return true;
               }
            }

            return super.mouseClicked(mouseX, mouseY, button);
         }
      }
   }

   public boolean computeNext(ESPConfiguratorScreen.EspItem item, float cardX, float cardY, float cardW, float cardH, float u, float mx, float my) {
      float var9 = cardX + 12.0F * u;
      float var10 = cardW - 24.0F * u;
      float var11 = 11.0F * u;
      float var12 = cardY + var11;
      float var13 = IconFontRegistry.fontRegistry3.computeDelegate("ESP", 6.2F * u) + 8.0F * u;
      float var14 = 11.5F * u;
      float var15 = cardX + cardW - 12.0F * u - var13;
      float var16 = var12 - 1.0F * u;
      float var17 = 18.0F * u;
      float var18 = 10.5F * u;
      float var19 = var15 - 7.0F * u - var17;
      float var20 = var16 + (var14 - var18) / 2.0F;
      if (MaxMinHelper.computeFlag(mx, my, var19 - 4.0F * u, var20 - 4.0F * u, var17 + 8.0F * u, var18 + 8.0F * u)) {
         item.feature.toggle();
         return true;
      } else {
         float var21 = IconFontRegistry.fontRegistry.computeDelegate(8.5F * u);
         float var22 = IconFontRegistry.fontRegistry3.computeDelegate(6.8F * u);
         float var23 = Math.max(1.0F, 0.75F * u);
         float var24 = var12 + var21 + 3.0F * u + var22 + 6.0F * u;
         float var25 = var24 + var23 + 7.0F * u;
         float var26 = var9;
         float var27 = var10;

         for (Setting var29 : item.feature.getSettings()) {
            if (var29.isVisible()) {
               if (var29 instanceof FloatSetting var30) {
                  float var52 = 22.0F * u;
                  float var55 = var25 + 13.0F * u;
                  float var58 = 3.5F * u;
                  if (MaxMinHelper.computeFlag(mx, my, var26 - 2.0F * u, var55 - 5.0F * u, var27 + 4.0F * u, var58 + 10.0F * u)) {
                     this.floatSetting = var30;
                     this.handleMax(var30, mx, var26, var27);
                     return true;
                  }

                  var25 += var52 + 3.5F * u;
               } else if (var29 instanceof BooleanSetting var31) {
                  float var51 = 16.0F * u;
                  if (MaxMinHelper.computeFlag(mx, my, var26, var25, var27, var51)) {
                     var31.setValue(!var31.getValue());
                     return true;
                  }

                  var25 += var51 + 3.5F * u;
               } else if (var29 instanceof ModeSetting var32) {
                  float var50 = 28.0F * u;
                  float var54 = var26;
                  float var57 = var25 + 10.5F * u;
                  float var60 = 13.5F * u;

                  for (String var64 : var32.getValues()) {
                     float var66 = IconFontRegistry.fontRegistry3.computeDelegate(EngHelper.computeString(var64), 6.4F * u) + 10.0F * u;
                     if (MaxMinHelper.computeFlag(mx, my, var54, var57, var66, var60)) {
                        var32.setValue(var64);
                        return true;
                     }

                     var54 += var66 + 3.0F * u;
                  }

                  var25 += var50 + 3.5F * u;
               } else if (!(var29 instanceof MultiBooleanSetting var33)) {
                  if (var29 instanceof ColorSetting var34) {
                     float var48 = 17.0F * u;
                     float var53 = this.map.getOrDefault(var34, 0.0F);
                     float var56 = 40.0F * u * var53;
                     float var59 = var48 + var56;
                     float var61 = 22.0F * u;
                     float var63 = 11.0F * u;
                     float var65 = var26 + var27 - var61;
                     float var43 = var25 + (var48 - var63) / 2.0F;
                     if (MaxMinHelper.computeFlag(mx, my, var65 - 2.0F * u, var43 - 2.0F * u, var61 + 4.0F * u, var63 + 4.0F * u)) {
                        this.colorSetting = this.colorSetting == var34 ? null : var34;
                        return true;
                     }

                     if (var53 > 0.05F) {
                        float var44 = var26 + 3.0F * u;
                        float var45 = var25 + var48 + 2.5F * u;
                        float var46 = var27 - 6.0F * u;
                        float var47 = 33.0F * u * var53;
                        if (MaxMinHelper.computeFlag(mx, my, var44, var45, var46, var47)) {
                           this.colorSetting2 = var34;
                           this.initBto(var34, mx, my, var44, var45, var46, var47);
                           return true;
                        }
                     }

                     var25 += var59 + 3.5F * u;
                  } else if (var29 instanceof ButtonSetting var35) {
                     float var49 = 19.0F * u;
                     if (MaxMinHelper.computeFlag(mx, my, var26, var25, var27, var49)) {
                        var35.press();
                        return true;
                     }

                     var25 += var49 + 3.5F * u;
                  }
               } else {
                  float var36 = 26.0F * u;
                  float var37 = var26;
                  float var38 = var25 + 10.5F * u;
                  float var39 = 13.5F * u;

                  for (BooleanSetting var41 : var33.getValue()) {
                     float var42 = IconFontRegistry.fontRegistry3.computeDelegate(EngHelper.computeString(var41.getName()), 6.2F * u) + 9.0F * u;
                     if (MaxMinHelper.computeFlag(mx, my, var37, var38, var42, var39)) {
                        var41.setValue(!var41.getValue());
                        return true;
                     }

                     var37 += var42 + 3.0F * u;
                  }

                  var25 += var36 + 3.5F * u;
               }
            }
         }

         return false;
      }
   }

   public void handleMax(FloatSetting ss, float mouseX, float trackX, float trackW) {
      float var5 = MathHelper.clamp((mouseX - trackX) / trackW, 0.0F, 1.0F);
      float var6 = ss.getMin();
      float var7 = ss.getMax();
      float var8 = ss.getStep();
      float var9 = var6 + var5 * (var7 - var6);
      if (var8 > 0.0F) {
         var9 = (float)Math.round(var9 / var8) * var8;
      }

      var9 = MathHelper.clamp(var9, var6, var7);
      ss.setValue(var9);
   }

   public void initBto(ColorSetting cs, float mouseX, float mouseY, float gX, float gY, float gW, float gH) {
      float var8 = MathHelper.clamp((mouseX - gX) / gW, 0.0F, 1.0F);
      float var9 = Math.max(0.04F, MathHelper.clamp(1.0F - (mouseY - gY) / gH, 0.0F, 1.0F));
      int var10 = Color.HSBtoRGB(var8, 1.0F, var9);
      Color var11 = cs.getValue();
      int var12 = var11 != null ? var11.getAlpha() : 255;
      cs.handleAction(new Color(var10 & 16777215 | var12 << 24, true));
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.floatSetting != null) {
         float var40 = this.getMin();
         float var41 = this.computeFloat(var40);
         float var42 = this.computeFloat2(var40);
         float var43 = ((float)this.width - var41) / 2.0F;
         float var44 = ((float)this.height - var42) / 2.0F;
         float var45 = var41 - 24.0F * var40;
         float var46 = 8.0F * var40;
         float var47 = this.intVal == 0 ? (var45 - var46) / 2.0F - 22.0F * var40 : Math.min(var45, 400.0F * var40) - 22.0F * var40;
         ESPConfiguratorScreen.EspItem[] var48 = ESPConfiguratorScreen.EspItem.values();

         for (ESPConfiguratorScreen.EspItem var52 : var48) {
            if (var52.feature.getSettings().contains(this.floatSetting)) {
               float var53 = 38.0F * var40;
               float var54 = var43 + 12.0F * var40;
               float var55 = var44 + var53 + 6.0F * var40;
               float var56;
               if (this.intVal == 0) {
                  float var57 = (var45 - var46) / 2.0F;
                  float[] var62 = new float[]{var55 - this.momevou, var55 - this.momevou};
                  var56 = var54;

                  for (ESPConfiguratorScreen.EspItem var70 : var48) {
                     float var72 = this.computeNext(var70, var57, var40);
                     int var74 = var62[0] <= var62[1] ? 0 : 1;
                     if (var70 == var52) {
                        var56 = var54 + (float)var74 * (var57 + var46);
                        break;
                     }

                     var62[var74] += var72 + var46;
                  }
               } else {
                  float var58 = Math.min(var45, 400.0F * var40);
                  var56 = var54 + (var45 - var58) / 2.0F;
               }

               float var59 = var56 + 11.0F * var40;
               this.handleMax(this.floatSetting, (float)mouseX, var59, var47);
               return true;
            }
         }

         return true;
      } else if (this.colorSetting2 == null) {
         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      } else {
         float var10 = this.getMin();
         float var11 = this.computeFloat(var10);
         float var12 = this.computeFloat2(var10);
         float var13 = ((float)this.width - var11) / 2.0F;
         float var14 = ((float)this.height - var12) / 2.0F;
         float var15 = var11 - 24.0F * var10;
         float var16 = 8.0F * var10;
         float var17 = this.intVal == 0 ? (var15 - var16) / 2.0F - 22.0F * var10 : Math.min(var15, 400.0F * var10) - 22.0F * var10;
         ESPConfiguratorScreen.EspItem[] var18 = ESPConfiguratorScreen.EspItem.values();

         for (ESPConfiguratorScreen.EspItem var22 : var18) {
            if (var22.feature.getSettings().contains(this.colorSetting2)) {
               float var23 = 38.0F * var10;
               float var24 = var13 + 12.0F * var10;
               float var25 = var14 + var23 + 6.0F * var10;
               float var26;
               float var27;
               if (this.intVal == 0) {
                  float var28 = (var15 - var16) / 2.0F;
                  float[] var29 = new float[]{var25 - this.momevou, var25 - this.momevou};
                  var26 = var24;
                  var27 = var25;

                  for (ESPConfiguratorScreen.EspItem var33 : var18) {
                     float var34 = this.computeNext(var33, var28, var10);
                     int var35 = var29[0] <= var29[1] ? 0 : 1;
                     if (var33 == var22) {
                        var26 = var24 + (float)var35 * (var28 + var16);
                        var27 = var29[var35];
                        break;
                     }

                     var29[var35] += var34 + var16;
                  }
               } else {
                  float var60 = Math.min(var15, 400.0F * var10);
                  var26 = var24 + (var15 - var60) / 2.0F;
                  var27 = var25 - this.momevou;
               }

               float var61 = var27 + 24.0F * var10;
               float var63 = var61 + 4.5F * var10;
               float var65 = var63 + IconFontRegistry.fontRegistry3.computeDelegate(6.4F * var10) + 6.0F * var10;
               float var67 = var26 + 11.0F * var10;

               for (Setting var71 : var22.feature.getSettings()) {
                  if (var71.isVisible()) {
                     if (var71 == this.colorSetting2) {
                        float var73 = 17.0F * var10;
                        float var75 = this.map.getOrDefault(this.colorSetting2, 1.0F);
                        float var36 = var67 + 3.0F * var10;
                        float var37 = var65 + var73 + 2.5F * var10;
                        float var38 = var17 - 6.0F * var10;
                        float var39 = 33.0F * var10 * var75;
                        this.initBto(this.colorSetting2, (float)mouseX, (float)mouseY, var36, var37, var38, var39);
                        return true;
                     }

                     var65 += this.computeDefault(var71, var10) + 3.5F * var10;
                  }
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.floatSetting = null;
      this.colorSetting2 = null;
      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean shouldPause() {
      return false;
   }

   public static enum EspItem {
      CHAMS("Chams", "Chams", Chams.getInstance(), "Отображает цветные силуэты и контуры игроков сквозь стены"),
      BOXES("Boxes", "Boxes", Boxes.getInstance(), "2D и 3D боксы, угловые рамки и индикаторы здоровья"),
      NAMETAGS("Name Tags", "NameTags", NameTags.getInstance(), "Информационные таблички с никами, бронёй и пингом"),
      CHINA_HAT("China Hat", "China Hat", ChinaHat.getInstance(), "Эстетичная конусообразная шляпа над головой персонажа"),
      SKELETON("Skeleton ESP", "Skeleton", SkeletonESP.getInstance(), "Отрисовка костей и скелета игроков сквозь препятствия");

      public final String rawName;
      public final String displayName;
      public final Module feature;
      public final String desc;

      private EspItem(String rawName, String displayName, Module feature, String desc) {
         this.rawName = rawName;
         this.displayName = displayName;
         this.feature = feature;
         this.desc = desc;
      }

      public static ESPConfiguratorScreen.EspItem[] $values() {
         return new ESPConfiguratorScreen.EspItem[]{CHAMS, BOXES, NAMETAGS, CHINA_HAT, SKELETON};
      }
   }
}
