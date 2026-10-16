package dev.daamky.client.gui;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.Binding;
import dev.daamky.client.internal.ClickGui;
import dev.daamky.client.internal.ColorTheme;
import dev.daamky.client.internal.DisplayScale;
import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.internal.Geometry;
import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.ModuleRegistry;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.Translator;
import dev.daamky.client.internal.White;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.theme.ThemeManager;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class PVEScreen extends Screen implements MinecraftAccess {
   public static final PVEScreen pVEScreen = new PVEScreen();
   public boolean flag;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public ColorTheme colorTheme = null;
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public boolean flag2 = false;
   public long timeStamp = 0L;
   public final List<ClickGui> list = new ArrayList<>();
   public final White white = White.getInstance();
   public final Translator translator = Translator.getInstance();
   public String string = "";
   public boolean flag3 = false;
   public boolean flag4 = false;
   public long long2 = 0L;
   public String string2 = "";
   public String string3 = "";
   public final AnimatedValue animatedValue4 = new AnimatedValue();
   public float floatVal = 0.65F;
   public final AnimatedValue animatedValue5 = new AnimatedValue();
   public static boolean flag5 = false;
   public final ClickGui clickGui = new ClickGui(ModuleCategory.HUD);
   public final AnimatedValue animatedValue6 = new AnimatedValue();

   public void handleName(ColorTheme theme) {
      White.getInstance().setTheme(theme);
      ThemeManager.getInstance().initName(theme);
      ThemeManager.getInstance().writeName(theme);
      NotificationManager.handleSuccess("Применена тема " + theme.getName());
   }

   public void initAdd() {
      this.list.clear();

      for (ModuleCategory var4 : ModuleCategory.values()) {
         if (var4 != ModuleCategory.HUD) {
            this.list.add(new ClickGui(var4));
         }
      }
   }

   public PVEScreen() {
      super(Text.of(""));
      this.initAdd();
   }

   public void close() {
      if (this.flag) {
         White.getInstance().handleTheme(false);
         this.flag = false;
         this.flag3 = false;
      } else {
         super.close();
      }
   }

   public void init() {
      White.getInstance().runTheme();
      if (this.list.isEmpty()) {
         this.initAdd();
      }

      this.flag = true;
      this.animatedValue.handleStart(0.0);
      super.init();
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      long var5;
      if (this.flag4 && (var5 = System.currentTimeMillis()) - this.longValVal2 > 35L) {
         if (!this.string.isEmpty()) {
            this.string = this.string.substring(0, this.string.length() - 1);
            this.longValVal2 = var5;
         } else {
            this.flag4 = false;
         }
      }

      super.render(context, mouseX, mouseY, delta);
      this.animatedValue.update();
      this.animatedValue.animateTo(this.flag ? 1.0 : 0.0, 250L, Easing.QUINT_OUT);
      float var7 = (float)this.animatedValue.getValue();
      if (!this.flag && (double)var7 < 0.01) {
         super.close();
      } else {
         this.animatedValue6.update();
         this.animatedValue6.animateTo(flag5 ? 1.0 : 0.0, 300L, Easing.QUINT_OUT);
         float var8 = (float)this.animatedValue6.getValue();
         float var9 = (float)mc.getWindow().getScaledHeight();
         float var10 = (float)mc.getWindow().getScaledWidth();
         float var11 = DisplayScale.getInstance().computeGui(135.0F);
         float var12 = 0.62F;
         float var13 = var9 * var12;
         float var14 = DisplayScale.getInstance().computeGui(10.0F);
         int var15 = this.list.size() + 1;
         float var16 = (float)var15 * var11 + (float)(var15 - 1) * var14;
         float var17 = (var10 - var16) / 2.0F;
         float var18 = (var9 - var13) / 2.0F;
         float var19 = (var10 - var11) / 2.0F;
         MatrixStack var20 = context.getMatrices();
         float var21 = (var9 - var18) * (1.0F - var7);
         float var22 = -(var10 / 2.0F + var16) * var8;
         var20.push();
         var20.translate(0.0F, var21, 0.0F);
         this.string2 = "";
         if (this.list.isEmpty() || this.list.get(0).getList().isEmpty() && !ModuleRegistry.getInstance().getList().isEmpty()) {
            this.initAdd();
         }

         if (var8 < 1.0F) {
            float var23 = var7 * (1.0F - var8);

            for (int var24 = 0; var24 < this.list.size(); var24++) {
               ClickGui var25 = this.list.get(var24);
               var25.setAlpha(var23);
               var25.setWidth(var11);
               var25.setHeight(var13);
               var25.setX(var17 + var22 + (float)var24 * (var11 + var14));
               var25.setY(var18);
               var25.handleNext(context, mouseX, mouseY, delta);
            }

            this.translator.setAlpha(var23);
            this.translator.setWidth(var11);
            this.translator.setHeight(var13);
            this.translator.setX(var17 + var22 + (float)this.list.size() * (var11 + var14));
            this.translator.setY(var18);
            this.translator.handleEnabled(context, mouseX, mouseY, delta);
         }

         if (var8 > 0.0F) {
            float var30 = var7 * var8;
            float var31 = (var10 / 2.0F + var11) * (1.0F - var8);
            this.clickGui.setAlpha(var30);
            this.clickGui.setWidth(var11);
            this.clickGui.setHeight(var13);
            this.clickGui.setX(var19 + var31);
            this.clickGui.setY(var18);
            this.clickGui.handleNext(context, mouseX, mouseY, delta);
            float var32 = DisplayScale.getInstance().computeGui(60.0F);
            float var26 = DisplayScale.getInstance().computeGui(15.0F);
            float var27 = var19 + var31 + var11 / 2.0F - var32 / 2.0F;
            float var28 = var18 + var13 + var14;
            boolean var29 = Geometry.computeFlag((float)mouseX, (float)mouseY, var27, var28, var32, var26);
            Render.rectRenderer
               .handleRgb(var20, var27, var28, var32, var26, DisplayScale.getInstance().computeGui(3.0F), InterfaceMetrics.computeColor((int)(255.0F * var30)));
            if (var29) {
               Render.rectRenderer
                  .handleRgb(
                     var20, var27, var28, var32, var26, DisplayScale.getInstance().computeGui(3.0F), InterfaceMetrics.computeColor3((int)(30.0F * var30))
                  );
            }

            FontBootstrap.clientFont10
               .handleColor(
                  var20,
                  "Назад",
                  var27 + var32 / 2.0F - FontBootstrap.clientFont10.computeFloat("Назад", DisplayScale.getInstance().computeGui(6.5F)) / 2.0F,
                  var28 + var26 / 2.0F - DisplayScale.getInstance().computeGui(6.5F) / 2.0F,
                  DisplayScale.getInstance().computeGui(6.5F),
                  InterfaceMetrics.computeColor2((int)(255.0F * var30))
               );
         }

         this.initMax(var20, var10, var18, var7);
         this.initInit(var20, var10, var9, var7);
         var20.pop();
         Render.uProjHelper.runGl2();
      }
   }

   public void setDescription(String description) {
      this.string2 = description == null ? "" : description;
   }

   public void initMax(MatrixStack matrixStack, float windowWidth, float panelsY, float alphaScale) {
      this.animatedValue4.update();
      if (!this.string2.isEmpty()) {
         this.string3 = this.string2;
      }

      this.animatedValue4.animateTo(this.string2.isEmpty() ? 0.0 : 1.0, 120L, Easing.QUINT_OUT);
      float var5 = (float)this.animatedValue4.getValue();
      if (!(var5 <= 0.01F)) {
         int var6 = (int)(255.0F * alphaScale * var5);
         float var7 = DisplayScale.getInstance().computeGui(6.5F);
         float var8 = DisplayScale.getInstance().computeGui(8.0F);
         float var9 = DisplayScale.getInstance().computeGui(4.0F);
         float var10 = windowWidth * 0.48F;
         String var11 = this.computeString(this.string3, var7, var10);
         float var12 = FontBootstrap.clientFont10.computeFloat(var11, var7);
         float var13 = var12 + var8 * 2.0F;
         float var14 = var7 + var9 * 2.0F;
         float var15 = windowWidth / 2.0F - var13 / 2.0F;
         float var16 = Math.max(DisplayScale.getInstance().computeGui(10.0F), panelsY - var14 - DisplayScale.getInstance().computeGui(10.0F));
         float var17 = DisplayScale.getInstance().computeGui(4.0F);
         Render.rectRenderer.handleRgb(matrixStack, var15, var16, var13, var14, var17, InterfaceMetrics.computeColor((int)((float)var6 * 0.92F)));
         Render.rectRenderer.handleRgb(matrixStack, var15, var16, var13, var14, var17, InterfaceMetrics.computeColor3((int)((float)var6 * 0.06F)));
         Render.rectRenderer
            .handleRgb(
               matrixStack,
               var15 + var8,
               var16 + DisplayScale.getInstance().computeGui(1.0F),
               var13 - var8 * 2.0F,
               Math.max(0.5F, DisplayScale.getInstance().computeGui(0.55F)),
               0.0F,
               new Color(255, 255, 255, (int)((float)var6 * 0.1F))
            );
         FontBootstrap.clientFont10
            .handleColor(matrixStack, var11, var15 + var8, var16 + var14 / 2.0F - var7 / 2.0F, var7, InterfaceMetrics.computeColor2(var6));
      }
   }

   public String computeString(String text, float fontSize, float maxWidth) {
      if (FontBootstrap.clientFont10.computeFloat(text, fontSize) <= maxWidth) {
         return text;
      } else {
         String var4 = text;

         while (var4.length() > 3 && FontBootstrap.clientFont10.computeFloat(var4 + "...", fontSize) > maxWidth) {
            var4 = var4.substring(0, var4.length() - 1);
         }

         return var4 + "...";
      }
   }

   public void initInit(MatrixStack matrixStack, float windowWidth, float windowHeight, float alphaScale) {
      float var5 = DisplayScale.getInstance().computeGui(130.0F);
      float var6 = DisplayScale.getInstance().computeGui(18.0F);
      float var7 = windowWidth / 2.0F - var5 / 2.0F;
      float var8 = windowHeight - var6 - DisplayScale.getInstance().computeGui(60.0F);
      int var9 = (int)(255.0F * alphaScale);
      this.animatedValue5.update();
      this.animatedValue5.animateTo(this.flag3 ? 1.0 : 0.0, 250L, Easing.QUINT_OUT, true);
      float var10 = (float)this.animatedValue5.getValue();
      float var11 = DisplayScale.getInstance().computeGui(3.5F);
      if (Interface.check()) {
         Render.shaderProgram.handleRgb(matrixStack, var7, var8, var5, var6, var11, InterfaceMetrics.computeColor(var9), Interface.getDay3());
      } else {
         Render.rectRenderer.handleRgb(matrixStack, var7, var8, var5, var6, var11, InterfaceMetrics.computeColor(var9));
      }

      String var12 = this.flag3 && System.currentTimeMillis() % 1000L > 500L ? "_" : "";
      String var13 = "Search...";
      String var14 = "Search: " + this.string + var12;
      float var15 = DisplayScale.getInstance().computeGui(7.0F);
      float var16 = FontBootstrap.clientFont10.computeFloat(var13, var15);
      float var17 = FontBootstrap.clientFont10.computeFloat(var14, var15);
      float var18 = var7 + var5 / 2.0F - var16 / 2.0F;
      float var19 = var7 + DisplayScale.getInstance().computeGui(6.0F);
      float var20 = EntityHelper.computeFloat(var18, var19, var10);
      float var21 = var8 + var6 / 2.0F - DisplayScale.getInstance().computeGui(2.5F);
      Color var22 = this.string.isEmpty() && !this.flag3 ? new Color(150, 150, 150, var9) : new Color(255, 255, 255, var9);
      String var23 = var10 > 0.5F ? var14 : var13;
      FontBootstrap.clientFont10.handleColor(matrixStack, var23, var20, var21, var15, var22);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 70 && (modifiers & 2) != 0) {
         this.flag3 = true;
         return true;
      } else if (this.flag3) {
         if (keyCode != 257) {
            if (keyCode == 256) {
               if (!this.string.isEmpty()) {
                  this.flag4 = true;
                  this.flag3 = false;
                  this.longValVal2 = System.currentTimeMillis();
               } else {
                  this.flag3 = false;
               }

               return true;
            } else {
               if (keyCode == 259 && !this.string.isEmpty()) {
                  this.flag4 = false;
                  this.string = this.string.substring(0, this.string.length() - 1);
               }

               return super.keyPressed(keyCode, scanCode, modifiers);
            }
         } else {
            this.flag3 = false;
            if (!this.string.isEmpty()) {
               String var4 = this.string.toLowerCase();
               Binding var5 = null;
               ClickGui var6 = null;

               for (ClickGui var8 : this.list) {
                  for (Binding var10 : var8.getList()) {
                     if (var10.getFeature().getName().equalsIgnoreCase(var4)) {
                        var5 = var10;
                        var6 = var8;
                        break;
                     }
                  }

                  if (var5 == null) {
                  }
               }

               if (var5 == null) {
                  for (ClickGui var13 : this.list) {
                     for (Binding var15 : var13.getList()) {
                        if (var15.string.contains(var4)) {
                           var5 = var15;
                           var6 = var13;
                           break;
                        }
                     }

                     if (var5 == null) {
                     }
                  }
               }

               if (var5 != null) {
                  if (var6.getTag() == ModuleCategory.MICS) {
                     boolean var12 = var5.getFeature().getSubCategory().equalsIgnoreCase("PVE");
                     var6.setPveMode(var12);
                  }

                  var5.getQuintOutHelper().setOpen(true);
                  var6.handleMax(var5);
                  var5.runCurrent();
               }
            }

            return true;
         }
      } else if (keyCode == 256) {
         this.flag = false;
         mc.mouse.lockCursor();
         return true;
      } else {
         this.list.forEach(PVEScreen::lambda$keyPressed$0);
         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean charTyped(char chr, int modifiers) {
      if (this.flag3) {
         this.flag4 = false;
         if (FontBootstrap.clientFont10.computeFloat(this.string + chr, 7.0F) < DisplayScale.getInstance().computeGui(135.0F)) {
            this.string = this.string + chr;
            return true;
         }
      }

      return super.charTyped(chr, modifiers);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      float var6 = (float)mc.getWindow().getScaledWidth();
      float var7 = (float)mc.getWindow().getScaledHeight();
      float var8 = DisplayScale.getInstance().computeGui(135.0F);
      float var9 = 0.62F;
      float var10 = var7 * var9;
      float var11 = DisplayScale.getInstance().computeGui(10.0F);
      int var12 = this.list.size() + 1;
      float var13 = (float)var12 * var8 + (float)(var12 - 1) * var11;
      float var14 = (var7 - var10) / 2.0F;
      if (flag5) {
         float var20 = (var6 - var8) / 2.0F;
         this.clickGui.mouseClicked(mouseX, mouseY, button);
         float var21 = DisplayScale.getInstance().computeGui(60.0F);
         float var22 = DisplayScale.getInstance().computeGui(15.0F);
         float var23 = var20 + var8 / 2.0F - var21 / 2.0F;
         float var19 = var14 + var10 + var11;
         if (button == 0 && Geometry.computeFlag(mouseX, mouseY, (double)var23, (double)var19, (double)var21, (double)var22)) {
            flag5 = false;
            return true;
         } else {
            return super.mouseClicked(mouseX, mouseY, button);
         }
      } else {
         float var15 = DisplayScale.getInstance().computeGui(130.0F);
         float var16 = DisplayScale.getInstance().computeGui(18.0F);
         float var17 = var6 / 2.0F - var15 / 2.0F;
         float var18 = var7 - var16 - DisplayScale.getInstance().computeGui(60.0F);
         if (button == 0) {
            if (mouseX >= (double)var17 && mouseX <= (double)(var17 + var15) && mouseY >= (double)var18 && mouseY <= (double)(var18 + var16)) {
               this.flag3 = true;
               this.flag4 = false;
               return true;
            }

            this.flag3 = false;
         }

         this.list.forEach(PVEScreen::lambda$mouseClicked$1);
         this.translator.mouseClicked(mouseX, mouseY, button);
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (flag5) {
         this.clickGui.mouseReleased(mouseX, mouseY, button);
         return super.mouseReleased(mouseX, mouseY, button);
      } else {
         this.list.forEach(PVEScreen::lambda$mouseReleased$2);
         this.translator.mouseReleased(mouseX, mouseY, button);
         return super.mouseReleased(mouseX, mouseY, button);
      }
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double h, double v) {
      this.list.forEach(PVEScreen::lambda$mouseScrolled$3);
      this.translator.handleDouble(mouseX, mouseY, h, v);
      return super.mouseScrolled(mouseX, mouseY, h, v);
   }

   public void blur() {
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public boolean shouldPause() {
      return false;
   }

   public void applyBlur() {
   }

   public boolean check() {
      return this.flag;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public ColorTheme getColorTheme() {
      return this.colorTheme;
   }

   public AnimatedValue getAnimatedValue2() {
      return this.animatedValue2;
   }

   public AnimatedValue getAnimatedValue3() {
      return this.animatedValue3;
   }

   public boolean check2() {
      return this.flag2;
   }

   public long getLong() {
      return this.longVal;
   }

   public List<ClickGui> getList() {
      return this.list;
   }

   public White getWhite() {
      return this.white;
   }

   public Translator getTranslator() {
      return this.translator;
   }

   public boolean check3() {
      return this.flag3;
   }

   public boolean check4() {
      return this.flag4;
   }

   public long getLong2() {
      return this.longValVal2;
   }

   public String getString2() {
      return this.string2;
   }

   public String getString3() {
      return this.string3;
   }

   public AnimatedValue getAnimatedValue4() {
      return this.animatedValue4;
   }

   public float getFloat() {
      return this.floatVal;
   }

   public AnimatedValue getAnimatedValue5() {
      return this.animatedValue5;
   }

   public static PVEScreen getInstance() {
      return pVEScreen;
   }

   public String getString() {
      return this.string;
   }

   public static void lambda$mouseScrolled$3(double mouseX, double mouseY, double h, double v, ClickGui panel) {
      panel.handleDouble(mouseX, mouseY, h, v);
   }

   public static void lambda$mouseReleased$2(double mouseX, double mouseY, int button, ClickGui panel) {
      panel.mouseReleased(mouseX, mouseY, button);
   }

   public static void lambda$mouseClicked$1(double mouseX, double mouseY, int button, ClickGui panel) {
      panel.mouseClicked(mouseX, mouseY, button);
   }

   public static void lambda$keyPressed$0(int keyCode, int scanCode, int modifiers, ClickGui panel) {
      panel.keyPressed(keyCode, scanCode, modifiers);
   }
}
