package dev.daamky.client.gui;

import dev.daamky.client.config.ConfigManager;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorTheme;
import dev.daamky.client.internal.Context;
import dev.daamky.client.internal.DisplayScale;
import dev.daamky.client.internal.Geometry;
import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.ModuleRegistry;
import dev.daamky.client.internal.ModuleRow;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ScissorUtil;
import dev.daamky.client.internal.TintColorHelper;
import dev.daamky.client.internal.White;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.theme.ThemeManager;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen implements MinecraftAccess {
   public static final ClickGuiScreen clickGuiScreen = new ClickGuiScreen();
   public boolean flag;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue mnikw = new AnimatedValue();
   public String string = "Combat";
   public String string2 = "RU";
   public String string3 = "";
   public boolean flag2 = false;
   public final Map<String, List<ModuleRow>> map = new HashMap<>();
   public double doubleVal = 0.0;
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public boolean flag3 = false;
   public String mriqi = "";
   public boolean flag4 = false;
   public static final Color[] colorList = new Color[]{
      new Color(255, 255, 255), new Color(100, 150, 255), new Color(180, 100, 255), new Color(220, 60, 80), new Color(60, 200, 120)
   };
   public static final String[] stringList = new String[]{"White", "Blue", "Purple", "Crimson", "Emerald"};

   public ClickGuiScreen() {
      super(Text.of(""));
      this.initInit();
   }

   public void initInit() {
      this.map.clear();
      this.map.put("Combat", new ArrayList<>());
      this.map.put("Movement", new ArrayList<>());
      this.map.put("Render", new ArrayList<>());
      this.map.put("Player", new ArrayList<>());

      for (Module var2 : ModuleRegistry.getInstance().getList()) {
         if (!var2.getName().equalsIgnoreCase("Click GUI")) {
            ModuleRow var3 = new ModuleRow(var2);
            String var4 = this.computeRender(var2.getCategory());
            if (this.map.containsKey(var4)) {
               this.map.get(var4).add(var3);
            }
         }
      }
   }

   public String computeRender(ModuleCategory tag) {
      if (tag == ModuleCategory.BATTLE) {
         return "Combat";
      } else if (tag == ModuleCategory.MOTION) {
         return "Movement";
      } else if (tag == ModuleCategory.VISUALS) {
         return "Render";
      } else {
         return tag == ModuleCategory.ACTOR ? "Player" : "Render";
      }
   }

   public void init() {
      White.getInstance().runTheme();
      this.initInit();
      this.flag = true;
      this.animatedValue.handleStart(0.0);
      this.animatedValue2.handleStart(1.0);
      this.doubleVal = 0.0;
      super.init();
   }

   public void close() {
      if (this.flag) {
         White.getInstance().handleTheme(false);
         this.flag = false;
         this.flag2 = false;
         this.flag4 = false;
      } else {
         super.close();
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void renderBackground(DrawContext ctx, int mx, int my, float d) {
   }

   public void applyBlur() {
   }

   public void blur() {
   }

   public ClickGuiScreen.LayoutBounds getMc() {
      float var1 = (float)mc.getWindow().getScaledWidth();
      float var2 = (float)mc.getWindow().getScaledHeight();
      float var3 = Math.min(var1 * 0.88F, computeFloat(470.0F));
      float var4 = Math.min(var2 * 0.84F, computeFloat(310.0F));
      float var5 = (var1 - var3) / 2.0F;
      float var6 = (var2 - var4) / 2.0F;
      float var7 = computeFloat(125.0F);
      float var8 = computeFloat(30.0F);
      return new ClickGuiScreen.LayoutBounds(var5, var6, var3, var4, var7, var8);
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      this.animatedValue.update();
      this.animatedValue.animateTo(this.flag ? 1.0 : 0.0, 220L, Easing.QUINT_OUT);
      float var5 = (float)this.animatedValue.getValue();
      if (!this.flag && var5 < 0.01F) {
         super.close();
      } else {
         MatrixStack var6 = context.getMatrices();
         ClickGuiScreen.LayoutBounds var7 = this.getMc();
         float var8 = ((float)mc.getWindow().getScaledHeight() - var7.wY) * (1.0F - var5);
         float var9 = 0.9F + 0.1F * var5;
         var6.push();
         float var10 = var7.wX + var7.wW / 2.0F;
         float var11 = var7.wY + var7.wH / 2.0F;
         var6.translate(var10, var11 + var8 * 0.25F, 0.0F);
         var6.scale(var9, var9, 1.0F);
         var6.translate(-var10, -var11, 0.0F);
         int var12 = (int)(255.0F * var5);
         float var13 = computeFloat(9.0F);
         Color var14 = new Color(5, 5, 7, (int)((float)var12 * 0.98F));
         Color var15 = new Color(255, 255, 255, (int)((float)var12 * 0.12F));
         Render.rectRenderer.handleRgb(var6, var7.wX, var7.wY, var7.wW, var7.wH, var13, var15);
         Render.rectRenderer
            .handleRgb(
               var6,
               var7.wX + computeFloat(1.0F),
               var7.wY + computeFloat(1.0F),
               var7.wW - computeFloat(2.0F),
               var7.wH - computeFloat(2.0F),
               var13 - computeFloat(1.0F),
               var14
            );
         if (Interface.getInstance().isEnabled() && Interface.check3()) {
            Interface.handleVal(var6, var7.wX, var7.wY, var7.wW, var7.wH, var13, var5, Interface.check4());
         }

         this.initInit(context, var6, mouseX, mouseY, var7.wX, var7.wY, var7.sidebarW, var7.wH, var12);
         this.initInit2(context, var6, mouseX, mouseY, var7.wX + var7.sidebarW, var7.wY, var7.wW - var7.sidebarW, var7.headerH, var12);
         this.renderContentArea(
            context, var6, mouseX, mouseY, var7.wX + var7.sidebarW, var7.wY + var7.headerH, var7.wW - var7.sidebarW, var7.wH - var7.headerH, var12, var5
         );
         if (this.flag3) {
            this.initInit4(context, var6, mouseX, mouseY, var7.wX, var7.wY, var7.wW, var7.wH, var12);
         }

         var6.pop();
         Render.uProjHelper.runGl2();
      }
   }

   public void initInit(DrawContext ctx, MatrixStack ms, int mx, int my, float sx, float sy, float sw, float sh, int alpha) {
      Render.rectRenderer.handleRgb(ms, sx + sw - computeFloat(1.0F), sy, computeFloat(1.0F), sh, 0.0F, new Color(255, 255, 255, (int)((float)alpha * 0.08F)));
      float var10 = computeFloat(10.0F);
      float var11 = computeFloat(13.0F);
      FontBootstrap.mdoswa.handleColor(ms, "GONE", sx + var10, sy + var10 + computeFloat(1.0F), var11, new Color(255, 255, 255, alpha));
      float var12 = sy + var10 + var11 + computeFloat(12.0F);
      float var13 = computeFloat(18.0F);
      var12 = this.initInit(ms, mx, my, "Combat", new String[]{"Combat"}, new String[]{"0"}, sx, var12, sw, var13, alpha);
      var12 = this.initInit(ms, mx, my, "Movement", new String[]{"Movement"}, new String[]{"1"}, sx, var12, sw, var13, alpha);
      var12 = this.initInit(ms, mx, my, "Visuals", new String[]{"Render", "Player"}, new String[]{"2", "3"}, sx, var12, sw, var13, alpha);
      var12 = this.initInit(ms, mx, my, "Settings", new String[]{"Configs"}, new String[]{"4"}, sx, var12, sw, var13, alpha);
      float var14 = computeFloat(26.0F);
      float var15 = sy + sh - var14 - var10;
      float var16 = sx + var10;
      float var17 = sw - var10 * 2.0F;
      Render.rectRenderer.handleRgb(ms, var16, var15, var17, var14, computeFloat(6.0F), new Color(8, 8, 12, alpha));
      Render.rectRenderer.handleRgb(ms, var16, var15, var17, computeFloat(1.0F), 0.0F, new Color(255, 255, 255, (int)((float)alpha * 0.08F)));
      float var18 = computeFloat(18.0F);
      float var19 = var16 + computeFloat(4.0F);
      float var20 = var15 + var14 / 2.0F - var18 / 2.0F;
      Render.rectRenderer.handleRgb(ms, var19, var20, var18, var18, computeFloat(4.0F), new Color(34, 34, 48, alpha));
      float var21 = computeFloat(7.5F);
      FontBootstrap.mdoswa.handleColor2(ms, "R", var19 + var18 / 2.0F, var20 + var18 / 2.0F - var21 / 2.0F, var21, new Color(255, 255, 255, alpha));
      float var22 = computeFloat(5.5F);
      float var23 = computeFloat(4.2F);
      String var24 = mc.player != null ? mc.player.getName().getString() : "returnd";
      FontBootstrap.mdoswa.handleColor(ms, var24, var19 + var18 + computeFloat(5.0F), var15 + computeFloat(4.0F), var22, new Color(255, 255, 255, alpha));
      FontBootstrap.clientFont11
         .handleColor(
            ms,
            this.string2.equals("RU") ? "До 01.01.2027" : "Till 01.01.2027",
            var19 + var18 + computeFloat(5.0F),
            var15 + computeFloat(14.0F),
            var23,
            new Color(140, 150, 175, alpha)
         );
   }

   public float initInit(MatrixStack ms, int mx, int my, String groupName, String[] items, String[] icons, float sx, float sy, float sw, float itemH, int alpha) {
      float var12 = computeFloat(4.5F);
      float var13 = computeFloat(10.0F);
      FontBootstrap.mdoswa.handleColor(ms, groupName, sx + var13, sy, var12, new Color(130, 140, 160, (int)((float)alpha * 0.75F)));
      float var14 = sy + var12 + computeFloat(4.0F);

      for (int var15 = 0; var15 < items.length; var15++) {
         String var16 = items[var15];
         String var17 = icons[var15];
         boolean var18 = this.string.equals(var16);
         boolean var19 = Geometry.computeFlag((double)mx, (double)my, (double)(sx + var13), (double)var14, (double)(sw - var13 * 2.0F), (double)itemH);
         if (var18) {
            Render.rectRenderer
               .handleRgb(ms, sx + var13, var14, sw - var13 * 2.0F, itemH, computeFloat(5.0F), new Color(255, 255, 255, (int)((float)alpha * 0.14F)));
            Render.rectRenderer
               .handleRgb(
                  ms,
                  sx + var13,
                  var14 + computeFloat(2.0F),
                  computeFloat(2.5F),
                  itemH - computeFloat(4.0F),
                  computeFloat(1.2F),
                  InterfaceMetrics.computeColor3(alpha)
               );
         } else if (var19) {
            Render.rectRenderer
               .handleRgb(ms, sx + var13, var14, sw - var13 * 2.0F, itemH, computeFloat(5.0F), new Color(255, 255, 255, (int)((float)alpha * 0.06F)));
         }

         float var20 = computeFloat(7.0F);
         Color var21 = var18 ? InterfaceMetrics.computeColor3(alpha) : new Color(175, 185, 205, (int)((float)alpha * (var19 ? 0.9F : 0.6F)));
         FontBootstrap.clientFont15.handleColor(ms, var17, sx + var13 + computeFloat(6.0F), var14 + itemH / 2.0F - var20 / 2.0F, var20, var21);
         float var22 = computeFloat(5.5F);
         Color var23 = var18 ? new Color(255, 255, 255, alpha) : new Color(185, 195, 215, (int)((float)alpha * (var19 ? 0.9F : 0.6F)));
         FontBootstrap.clientFont10.handleColor(ms, var16, sx + var13 + computeFloat(18.0F), var14 + itemH / 2.0F - var22 / 2.0F, var22, var23);
         var14 += itemH + computeFloat(2.5F);
      }

      return var14 + computeFloat(4.0F);
   }

   public void initInit2(DrawContext ctx, MatrixStack ms, int mx, int my, float hx, float hy, float hw, float hh, int alpha) {
      float var10 = computeFloat(10.0F);
      float var11 = computeFloat(6.0F);
      FontBootstrap.mdoswa.handleColor(ms, "Features", hx + var10, hy + hh / 2.0F - var11 / 2.0F, var11, new Color(160, 170, 190, alpha));
      float var12 = FontBootstrap.mdoswa.computeFloat("Features", var11);
      FontBootstrap.clientFont11.handleColor(ms, " / ", hx + var10 + var12, hy + hh / 2.0F - var11 / 2.0F, var11, new Color(120, 130, 150, alpha));
      float var13 = FontBootstrap.clientFont11.computeFloat(" / ", var11);
      FontBootstrap.mdoswa.handleColor(ms, this.string, hx + var10 + var12 + var13, hy + hh / 2.0F - var11 / 2.0F, var11, new Color(255, 255, 255, alpha));
      float var14 = hx + hw - var10;
      float var15 = computeFloat(6.5F);
      float var16 = computeFloat(4.5F);
      float var17 = (float)colorList.length * var15 + (float)(colorList.length - 1) * var16;
      var14 -= var17;
      float var18 = hy + hh / 2.0F - var15 / 2.0F;

      for (int var19 = 0; var19 < colorList.length; var19++) {
         float var20 = var14 + (float)var19 * (var15 + var16);
         boolean var21 = Geometry.computeFlag((double)mx, (double)my, (double)var20, (double)var18, (double)var15, (double)var15);
         Color var22 = colorList[var19];
         Render.rectRenderer.handleRgb(ms, var20, var18, var15, var15, var15 / 2.0F, new Color(var22.getRed(), var22.getGreen(), var22.getBlue(), alpha));
         if (var21) {
            Render.rectRenderer
               .handleRgb(
                  ms,
                  var20 - computeFloat(1.0F),
                  var18 - computeFloat(1.0F),
                  var15 + computeFloat(2.0F),
                  var15 + computeFloat(2.0F),
                  (var15 + computeFloat(2.0F)) / 2.0F,
                  new Color(255, 255, 255, (int)((float)alpha * 0.4F))
               );
         }
      }

      var14 -= computeFloat(40.0F);
      float var36 = computeFloat(34.0F);
      float var37 = computeFloat(13.0F);
      float var38 = hy + hh / 2.0F - var37 / 2.0F;
      Render.rectRenderer.handleRgb(ms, var14, var38, var36, var37, computeFloat(3.5F), new Color(20, 20, 28, alpha));
      float var39 = var36 / 2.0F;
      if (this.string2.equals("RU")) {
         Render.rectRenderer.handleRgb(ms, var14, var38, var39, var37, computeFloat(3.5F), new Color(255, 255, 255, alpha));
      } else {
         Render.rectRenderer.handleRgb(ms, var14 + var39, var38, var39, var37, computeFloat(3.5F), new Color(255, 255, 255, alpha));
      }

      float var23 = computeFloat(4.5F);
      FontBootstrap.mdoswa
         .handleColor2(
            ms,
            "RU",
            var14 + var39 / 2.0F,
            var38 + var37 / 2.0F - var23 / 2.0F,
            var23,
            this.string2.equals("RU") ? new Color(10, 10, 15, alpha) : new Color(180, 190, 210, alpha)
         );
      FontBootstrap.mdoswa
         .handleColor2(
            ms,
            "ENG",
            var14 + var39 + var39 / 2.0F,
            var38 + var37 / 2.0F - var23 / 2.0F,
            var23,
            this.string2.equals("ENG") ? new Color(10, 10, 15, alpha) : new Color(180, 190, 210, alpha)
         );
      var14 -= computeFloat(105.0F);
      float var24 = computeFloat(98.0F);
      float var25 = computeFloat(16.0F);
      float var26 = hy + hh / 2.0F - var25 / 2.0F;
      Render.rectRenderer.handleRgb(ms, var14, var26, var24, var25, computeFloat(4.0F), new Color(16, 16, 24, alpha));
      if (this.flag2) {
         Render.rectRenderer.handleRgb(ms, var14, var26, var24, var25, computeFloat(4.0F), InterfaceMetrics.computeColor3((int)((float)alpha * 0.2F)));
         TintColorHelper.handleFloat(var14, var26, var24, var25, computeFloat(1.0F), InterfaceMetrics.computeColor3(alpha).getRGB(), computeFloat(4.0F));
      }

      String var28 = !this.flag2 && this.string3.isEmpty() ? (this.string2.equals("RU") ? "Поиск..." : "Search...") : this.string3;
      Color var29 = this.string3.isEmpty() && !this.flag2 ? new Color(130, 140, 160, alpha) : new Color(255, 255, 255, alpha);
      float var30 = computeFloat(6.5F);
      float var31 = computeFloat(6.5F);
      FontBootstrap.clientFont15.handleColor(ms, "s", var14 + computeFloat(6.0F), var26 + var25 / 2.0F - var31 / 2.0F, var31, var29);
      String var32 = this.flag2 && System.currentTimeMillis() % 1000L > 500L ? "_" : "";
      FontBootstrap.clientFont10
         .handleColor(ms, var28 + (this.flag2 ? var32 : ""), var14 + computeFloat(16.0F), var26 + var25 / 2.0F - var30 / 2.0F, var30, var29);
   }

   public void renderContentArea(DrawContext ctx, MatrixStack ms, int mx, int my, float cx, float cy, float cw, float ch, int alpha, float af) {
      this.animatedValue2.update();
      this.animatedValue2.animateTo(1.0, 280L, Easing.QUINT_OUT);
      float var11 = (float)this.animatedValue2.getValue();
      if ("Configs".equals(this.string)) {
         this.initInit3(ctx, ms, mx, my, cx, cy, cw, ch, alpha);
      } else {
         Object var12 = this.map.get(this.string);
         if (var12 == null) {
            var12 = new ArrayList();
         }

         if (!this.string3.isEmpty()) {
            String var13 = this.string3.toLowerCase();
            var12 = var12.stream().filter(ClickGuiScreen::lambda$renderContentArea$0).toList();
         }

         float var34 = computeFloat(10.0F);
         float var14 = computeFloat(10.0F);
         float var15 = (cw - var34 * 2.0F - var14) / 2.0F;
         ArrayList var16 = new ArrayList();
         ArrayList var17 = new ArrayList();

         for (int var18 = 0; var18 < var12.size(); var18++) {
            if (var18 % 2 == 0) {
               var16.add((ModuleRow)var12.get(var18));
            } else {
               var17.add((ModuleRow)var12.get(var18));
            }
         }

         float var35 = this.computeNext(var16);
         float var19 = this.computeNext(var17);
         float var20 = Math.max(var35, var19) + var34 * 2.0F;
         double var21 = (double)Math.max(0.0F, var20 - ch);
         this.doubleVal = Math.max(0.0, Math.min(this.doubleVal, var21));
         this.animatedValue3.update();
         this.animatedValue3.animateTo(this.doubleVal, 30L, Easing.LINEAR);
         float var23 = (float)this.animatedValue3.getValue();
         ScissorUtil.handleEnable(ms, cx, cy, cw, ch);
         ms.push();
         ms.translate(0.0F, -var23 + (1.0F - var11) * computeFloat(16.0F), 0.0F);
         int var24 = (int)((float)alpha * var11);
         ArrayList var25 = new ArrayList();
         float var26 = cy + var34;
         float var27 = cy + var23;
         float var28 = cy + var23 + ch;

         for (ModuleRow var30 : var16) {
            float var31 = var30.getNext();
            var30.setX(cx + var34);
            var30.setY(var26);
            var30.setW(var15);
            var30.setA(af * var11);
            if (var26 + var31 >= var27 && var26 <= var28) {
               ModuleRow.DropdownOverlay var32 = var30.initInit(ctx, ms, mx, (int)((float)my + var23), var24);
               if (var32 != null) {
                  var25.add(var32);
               }
            }

            var26 += var31 + computeFloat(10.0F);
         }

         float var36 = cy + var34;

         for (ModuleRow var39 : var17) {
            float var41 = var39.getNext();
            var39.setX(cx + var34 + var15 + var14);
            var39.setY(var36);
            var39.setW(var15);
            var39.setA(af * var11);
            if (var36 + var41 >= var27 && var36 <= var28) {
               ModuleRow.DropdownOverlay var33 = var39.initInit(ctx, ms, mx, (int)((float)my + var23), var24);
               if (var33 != null) {
                  var25.add(var33);
               }
            }

            var36 += var41 + computeFloat(10.0F);
         }

         for (ModuleRow.DropdownOverlay var40 : var25) {
            ModuleRow.initInit(ctx, ms, mx, (int)((float)my + var23), var40);
         }

         ms.pop();
         ScissorUtil.handleDisable(ms);
      }
   }

   public float computeNext(List<ModuleRow> col) {
      float var2 = 0.0F;

      for (ModuleRow var4 : col) {
         var2 += var4.getNext() + computeFloat(10.0F);
      }

      return var2;
   }

   public void initInit3(DrawContext ctx, MatrixStack ms, int mx, int my, float cx, float cy, float cw, float ch, int alpha) {
      float var10 = computeFloat(10.0F);
      float var11 = computeFloat(10.0F);
      float var12 = (cw - var10 * 2.0F - var11) / 2.0F;
      float var13 = computeFloat(95.0F);
      float var14 = cx + var10;
      float var15 = cy + var10;
      boolean var16 = Geometry.computeFlag((double)mx, (double)my, (double)var14, (double)var15, (double)var12, (double)var13);
      Render.rectRenderer.handleRgb(ms, var14, var15, var12, var13, computeFloat(7.0F), var16 ? new Color(22, 22, 32, alpha) : new Color(16, 16, 24, alpha));
      Render.rectRenderer.handleRgb(ms, var14, var15, var12, computeFloat(1.0F), 0.0F, new Color(255, 255, 255, (int)((float)alpha * 0.08F)));
      float var17 = computeFloat(18.0F);
      float var18 = var14 + var12 / 2.0F - var17 / 2.0F;
      float var19 = var15 + computeFloat(16.0F);
      Render.rectRenderer.handleRgb(ms, var18, var19, var17, var17, var17 / 2.0F, new Color(255, 255, 255, (int)((float)alpha * 0.12F)));
      float var20 = computeFloat(9.0F);
      FontBootstrap.mdoswa.handleColor2(ms, "+", var18 + var17 / 2.0F, var19 + var17 / 2.0F - var20 / 2.0F, var20, new Color(255, 255, 255, alpha));
      float var21 = computeFloat(6.2F);
      String var22 = this.string2.equals("RU") ? "Создать Конфиг" : "Create Config";
      FontBootstrap.mdoswa.handleColor2(ms, var22, var14 + var12 / 2.0F, var19 + var17 + computeFloat(8.0F), var21, new Color(255, 255, 255, alpha));
      float var23 = computeFloat(4.3F);
      String var24 = this.string2.equals("RU") ? "Сохранить настройки в .gone файл" : "Save current settings to .gone file";
      FontBootstrap.clientFont11
         .handleColor2(ms, var24, var14 + var12 / 2.0F, var19 + var17 + computeFloat(8.0F) + var21 + computeFloat(3.0F), var23, new Color(140, 150, 175, alpha));
      List var25 = ConfigManager.getInstance().getConfigsNames();
      float var26 = cx + var10 + var12 + var11;
      float var27 = computeFloat(26.0F);
      float var28 = computeFloat(6.0F);

      for (int var29 = 0; var29 < var25.size(); var29++) {
         String var30 = (String)var25.get(var29);
         float var31 = cy + var10 + (float)var29 * (var27 + var28);
         boolean var32 = var30.equals(ConfigManager.getInstance().getCurrentConfig());
         Render.rectRenderer
            .handleRgb(
               ms,
               var26,
               var31,
               var12,
               var27,
               computeFloat(6.0F),
               var32 ? InterfaceMetrics.computeColor3((int)((float)alpha * 0.18F)) : new Color(16, 16, 24, alpha)
            );
         float var33 = computeFloat(5.5F);
         FontBootstrap.mdoswa.handleColor(ms, var30, var26 + computeFloat(8.0F), var31 + computeFloat(5.0F), var33, new Color(255, 255, 255, alpha));
         float var34 = computeFloat(4.0F);
         FontBootstrap.clientFont11
            .handleColor(ms, "Created: 2026-08-13", var26 + computeFloat(8.0F), var31 + computeFloat(14.0F), var34, new Color(140, 150, 175, alpha));
         float var35 = computeFloat(20.0F);
         float var36 = computeFloat(10.0F);
         float var37 = var31 + var27 / 2.0F - var36 / 2.0F;
         float var38 = var26 + var12 - var35 * 2.0F - computeFloat(16.0F);
         boolean var39 = Geometry.computeFlag((double)mx, (double)my, (double)var38, (double)var37, (double)var35, (double)var36);
         Render.rectRenderer
            .handleRgb(ms, var38, var37, var35, var36, computeFloat(3.0F), var39 ? InterfaceMetrics.computeColor3(alpha) : new Color(30, 30, 42, alpha));
         float var40 = computeFloat(4.2F);
         FontBootstrap.clientFont10.handleColor2(ms, "Load", var38 + var35 / 2.0F, var37 + var36 / 2.0F - var40 / 2.0F, var40, new Color(255, 255, 255, alpha));
         float var41 = var26 + var12 - var35 - computeFloat(12.0F);
         boolean var42 = Geometry.computeFlag((double)mx, (double)my, (double)var41, (double)var37, (double)var35, (double)var36);
         Render.rectRenderer
            .handleRgb(ms, var41, var37, var35, var36, computeFloat(3.0F), var42 ? InterfaceMetrics.computeColor3(alpha) : new Color(30, 30, 42, alpha));
         FontBootstrap.clientFont10.handleColor2(ms, "Save", var41 + var35 / 2.0F, var37 + var36 / 2.0F - var40 / 2.0F, var40, new Color(255, 255, 255, alpha));
         float var43 = var26 + var12 - computeFloat(9.0F);
         boolean var44 = Geometry.computeFlag((double)mx, (double)my, (double)var43, (double)var37, (double)computeFloat(9.0F), (double)var36);
         FontBootstrap.mdoswa
            .handleColor(ms, "✕", var43, var37 + var36 / 2.0F - var40 / 2.0F, var40, var44 ? new Color(240, 60, 60, alpha) : new Color(170, 70, 70, alpha));
      }
   }

   public void initInit4(DrawContext ctx, MatrixStack ms, int mx, int my, float wx, float wy, float ww, float wh, int alpha) {
      Render.rectRenderer.handleRgb(ms, wx, wy, ww, wh, computeFloat(9.0F), new Color(0, 0, 0, (int)((float)alpha * 0.7F)));
      float var10 = computeFloat(170.0F);
      float var11 = computeFloat(90.0F);
      float var12 = wx + (ww - var10) / 2.0F;
      float var13 = wy + (wh - var11) / 2.0F;
      Render.rectRenderer.handleRgb(ms, var12, var13, var10, var11, computeFloat(8.0F), new Color(16, 16, 24, alpha));
      float var14 = computeFloat(6.2F);
      String var15 = this.string2.equals("RU") ? "Создать Новый Конфиг" : "Create New Config";
      FontBootstrap.mdoswa.handleColor(ms, var15, var12 + computeFloat(12.0F), var13 + computeFloat(12.0F), var14, new Color(255, 255, 255, alpha));
      float var16 = var13 + computeFloat(28.0F);
      float var17 = var10 - computeFloat(24.0F);
      float var18 = computeFloat(16.0F);
      Render.rectRenderer.handleRgb(ms, var12 + computeFloat(12.0F), var16, var17, var18, computeFloat(4.0F), new Color(26, 26, 38, alpha));
      String var19 = this.flag4 && System.currentTimeMillis() % 1000L > 500L ? "|" : "";
      String var20 = this.mriqi.isEmpty() && !this.flag4 ? "Name..." : this.mriqi + var19;
      float var21 = computeFloat(4.8F);
      FontBootstrap.clientFont10
         .handleColor(ms, var20, var12 + computeFloat(15.0F), var16 + var18 / 2.0F - var21 / 2.0F, var21, new Color(245, 245, 255, alpha));
      float var22 = computeFloat(48.0F);
      float var23 = computeFloat(14.0F);
      float var24 = var13 + var11 - var23 - computeFloat(12.0F);
      float var25 = var12 + var10 - var22 * 2.0F - computeFloat(14.0F);
      boolean var26 = Geometry.computeFlag((double)mx, (double)my, (double)var25, (double)var24, (double)var22, (double)var23);
      Render.rectRenderer.handleRgb(ms, var25, var24, var22, var23, computeFloat(3.5F), var26 ? new Color(42, 42, 58, alpha) : new Color(28, 28, 40, alpha));
      float var27 = computeFloat(4.5F);
      FontBootstrap.clientFont10
         .handleColor2(
            ms,
            this.string2.equals("RU") ? "Отмена" : "Cancel",
            var25 + var22 / 2.0F,
            var24 + var23 / 2.0F - var27 / 2.0F,
            var27,
            new Color(210, 215, 230, alpha)
         );
      float var28 = var12 + var10 - var22 - computeFloat(12.0F);
      boolean var29 = Geometry.computeFlag((double)mx, (double)my, (double)var28, (double)var24, (double)var22, (double)var23);
      Render.rectRenderer
         .handleRgb(
            ms,
            var28,
            var24,
            var22,
            var23,
            computeFloat(3.5F),
            var29 ? InterfaceMetrics.computeColor3(alpha) : InterfaceMetrics.computeColor3((int)((float)alpha * 0.88F))
         );
      FontBootstrap.clientFont10
         .handleColor2(
            ms,
            this.string2.equals("RU") ? "Сохранить" : "Save",
            var28 + var22 / 2.0F,
            var24 + var23 / 2.0F - var27 / 2.0F,
            var27,
            new Color(255, 255, 255, alpha)
         );
   }

   public static float computeFloat(float v) {
      return DisplayScale.getInstance().computeGui(v);
   }

   public boolean mouseClicked(double mx, double my, int button) {
      ClickGuiScreen.LayoutBounds var6 = this.getMc();
      float var7 = var6.wX;
      float var8 = var6.wY;
      float var9 = var6.wW;
      float var10 = var6.wH;
      float var11 = var6.sidebarW;
      float var12 = var6.headerH;
      if (this.flag3) {
         float var46 = computeFloat(170.0F);
         float var47 = computeFloat(90.0F);
         float var48 = var7 + (var9 - var46) / 2.0F;
         float var49 = var8 + (var10 - var47) / 2.0F;
         float var50 = var49 + computeFloat(28.0F);
         float var51 = var46 - computeFloat(24.0F);
         float var52 = computeFloat(16.0F);
         if (Geometry.computeFlag(mx, my, (double)(var48 + computeFloat(12.0F)), (double)var50, (double)var51, (double)var52)) {
            this.flag4 = true;
            return true;
         } else {
            this.flag4 = false;
            float var53 = computeFloat(48.0F);
            float var55 = computeFloat(14.0F);
            float var56 = var49 + var47 - var55 - computeFloat(12.0F);
            float var57 = var48 + var46 - var53 * 2.0F - computeFloat(14.0F);
            float var58 = var48 + var46 - var53 - computeFloat(12.0F);
            if (Geometry.computeFlag(mx, my, (double)var57, (double)var56, (double)var53, (double)var55)) {
               this.flag3 = false;
               return true;
            } else if (Geometry.computeFlag(mx, my, (double)var58, (double)var56, (double)var53, (double)var55)) {
               if (!this.mriqi.isEmpty()) {
                  String var60 = this.mriqi.endsWith(".gone") ? this.mriqi : this.mriqi + ".gone";
                  ConfigManager.getInstance().setCurrentConfig(var60);
                  NotificationManager.handleSuccess("Сохранён конфиг " + var60);
                  this.mriqi = "";
               }

               this.flag3 = false;
               return true;
            } else {
               return true;
            }
         }
      } else {
         float var13 = computeFloat(10.0F);
         float var14 = var7 + var9 - var13 - computeFloat(34.0F);
         float var15 = var8 + var12 / 2.0F - computeFloat(13.0F) / 2.0F;
         if (Geometry.computeFlag(mx, my, (double)var14, (double)var15, (double)computeFloat(34.0F), (double)computeFloat(13.0F))) {
            this.string2 = this.string2.equals("RU") ? "ENG" : "RU";
            return true;
         } else {
            float var16 = computeFloat(6.5F);
            float var17 = computeFloat(4.5F);
            float var18 = (float)colorList.length * var16 + (float)(colorList.length - 1) * var17;
            float var19 = var14 - var18 - computeFloat(10.0F);
            float var20 = var8 + var12 / 2.0F - var16 / 2.0F;

            for (int var21 = 0; var21 < colorList.length; var21++) {
               if (Geometry.computeFlag(mx, my, (double)(var19 + (float)var21 * (var16 + var17)), (double)var20, (double)var16, (double)var16)) {
                  this.handleTheme(var21);
                  return true;
               }
            }

            float var54 = computeFloat(98.0F);
            float var22 = computeFloat(16.0F);
            float var23 = var7 + var9 - var13 - var18 - computeFloat(40.0F) - computeFloat(105.0F);
            float var24 = var8 + var12 / 2.0F - var22 / 2.0F;
            if (Geometry.computeFlag(mx, my, (double)var23, (double)var24, (double)var54, (double)var22)) {
               this.flag2 = true;
               return true;
            } else {
               this.flag2 = false;
               float var25 = var8 + var13 + computeFloat(13.0F) + computeFloat(12.0F);
               float var26 = computeFloat(18.0F);
               String[][] var27 = new String[][]{{"Combat"}, {"Movement"}, {"Render", "Player"}, {"Configs"}};

               for (String[] var31 : var27) {
                  var25 += computeFloat(8.5F);

                  for (String var35 : var31) {
                     if (Geometry.computeFlag(mx, my, (double)(var7 + var13), (double)var25, (double)(var11 - var13 * 2.0F), (double)var26)) {
                        if (!this.string.equals(var35)) {
                           this.string = var35;
                           this.doubleVal = 0.0;
                           this.animatedValue2.handleStart(0.0);
                        }

                        return true;
                     }

                     var25 += var26 + computeFloat(2.5F);
                  }

                  var25 += computeFloat(4.0F);
               }

               if ("Configs".equals(this.string)) {
                  float var62 = var7 + var11;
                  float var64 = var8 + var12;
                  float var66 = var9 - var11;
                  float var68 = computeFloat(10.0F);
                  float var69 = (var66 - var13 * 2.0F - var68) / 2.0F;
                  if (Geometry.computeFlag(mx, my, (double)(var62 + var13), (double)(var64 + var13), (double)var69, (double)computeFloat(95.0F))) {
                     this.flag3 = true;
                     this.mriqi = "";
                     this.flag4 = true;
                     return true;
                  } else {
                     List var70 = ConfigManager.getInstance().getConfigsNames();
                     float var71 = var62 + var13 + var69 + var68;
                     float var72 = computeFloat(26.0F);
                     float var36 = computeFloat(6.0F);
                     float var37 = computeFloat(20.0F);
                     float var38 = computeFloat(10.0F);

                     for (int var39 = 0; var39 < var70.size(); var39++) {
                        String var40 = (String)var70.get(var39);
                        float var41 = var64 + var13 + (float)var39 * (var72 + var36);
                        float var42 = var41 + var72 / 2.0F - var38 / 2.0F;
                        float var43 = var71 + var69 - var37 * 2.0F - computeFloat(16.0F);
                        float var44 = var71 + var69 - var37 - computeFloat(12.0F);
                        float var45 = var71 + var69 - computeFloat(9.0F);
                        if (Geometry.computeFlag(mx, my, (double)var43, (double)var42, (double)var37, (double)var38)) {
                           ConfigManager.getInstance().loadJson(var40);
                           NotificationManager.handleSuccess("Загружен конфиг: " + var40);
                           return true;
                        }

                        if (Geometry.computeFlag(mx, my, (double)var44, (double)var42, (double)var37, (double)var38)) {
                           ConfigManager.getInstance().setCurrentConfig(var40);
                           NotificationManager.handleSuccess("Сохранён конфиг: " + var40);
                           return true;
                        }

                        if (Geometry.computeFlag(mx, my, (double)var45, (double)var42, (double)computeFloat(9.0F), (double)var38)) {
                           ConfigManager.getInstance().handleConfig(var40);
                           NotificationManager.handleInfo("Удалён конфиг: " + var40);
                           return true;
                        }
                     }

                     return super.mouseClicked(mx, my, button);
                  }
               } else {
                  List var61 = this.map.get(this.string);
                  if (var61 != null) {
                     float var63 = (float)this.animatedValue3.getValue();

                     for (ModuleRow var67 : var61) {
                        if (var67.computeMin(mx, my + (double)var63, button)) {
                           return true;
                        }
                     }
                  }

                  return super.mouseClicked(mx, my, button);
               }
            }
         }
      }
   }

   public boolean mouseReleased(double mx, double my, int button) {
      List var6 = this.map.get(this.string);
      if (var6 != null) {
         float var7 = (float)this.animatedValue3.getValue();

         for (ModuleRow var9 : var6) {
            var9.computeFlag(mx, my + (double)var7, button);
         }
      }

      return super.mouseReleased(mx, my, button);
   }

   public boolean mouseScrolled(double mx, double my, double h, double v) {
      this.doubleVal = this.doubleVal - v * (double)computeFloat(16.0F);
      return true;
   }

   public boolean charTyped(char chr, int mods) {
      if (this.flag4 && chr >= ' ' && this.mriqi.length() < 32) {
         this.mriqi = this.mriqi + chr;
         return true;
      } else if (this.flag2 && chr >= ' ') {
         this.string3 = this.string3 + chr;
         return true;
      } else {
         return super.charTyped(chr, mods);
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.flag4) {
         if (keyCode == 257) {
            if (!this.mriqi.isEmpty()) {
               String var7 = this.mriqi.endsWith(".gone") ? this.mriqi : this.mriqi + ".gone";
               ConfigManager.getInstance().setCurrentConfig(var7);
               NotificationManager.handleSuccess("Сохранён конфиг " + var7);
               this.mriqi = "";
            }

            this.flag3 = false;
            this.flag4 = false;
            return true;
         } else if (keyCode == 256) {
            this.flag3 = false;
            this.flag4 = false;
            return true;
         } else if (keyCode == 259 && !this.mriqi.isEmpty()) {
            this.mriqi = this.mriqi.substring(0, this.mriqi.length() - 1);
            return true;
         } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
         }
      } else if (this.flag2) {
         if (keyCode == 257 || keyCode == 256) {
            this.flag2 = false;
            return true;
         } else if (keyCode == 259 && !this.string3.isEmpty()) {
            this.string3 = this.string3.substring(0, this.string3.length() - 1);
            return true;
         } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
         }
      } else if (keyCode == 256) {
         this.flag = false;
         mc.mouse.lockCursor();
         return true;
      } else {
         List var4 = this.map.get(this.string);
         if (var4 != null) {
            for (ModuleRow var6 : var4) {
               if (var6.computeBind(keyCode, scanCode, modifiers)) {
                  return true;
               }
            }
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public void handleTheme(int i) {
      List var2 = White.getInstance().getList();
      if (i < var2.size()) {
         ColorTheme var3 = ((Context)var2.get(i)).getTheme();
         White.getInstance().setTheme(var3);
         ThemeManager.getInstance().initName(var3);
         ThemeManager.getInstance().writeName(var3);
         NotificationManager.handleSuccess("Тема: " + stringList[i]);
      }
   }

   public static ClickGuiScreen getInstance() {
      return clickGuiScreen;
   }

   public static boolean lambda$renderContentArea$0(String q, ModuleRow c) {
      return c.getFeature().getName().toLowerCase().contains(q);
   }

   private static record LayoutBounds(float wX, float wY, float wW, float wH, float sidebarW, float headerH) {
   }
}
