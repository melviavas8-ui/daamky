package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Translator extends Widget implements MinecraftAccess {
   public static final Translator translator = new Translator();
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final Map<String, Float> mudqo = new HashMap<>();
   public long timeStamp = 0L;
   public String string = EngHelper.getInstance().getEng();
   public float alpha = 1.0F;

   public float computeDefault(String key, boolean target) {
      float var3 = this.mudqo.getOrDefault(key, 0.0F);
      float var4 = target ? 1.0F : 0.0F;
      float var5 = 0.22F;
      var3 += (var4 - var3) * var5;
      this.mudqo.put(key, var3);
      return var3;
   }

   public void handleEnabled(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var5 = context.getMatrices();
      int var6 = (int)(this.getAlpha() * 255.0F);
      Render.shaderProgram
         .handleRgb(
            var5,
            this.getX(),
            this.getY(),
            this.getWidth(),
            this.getHeight(),
            InterfaceMetrics.computeFloat(10.0F),
            InterfaceMetrics.computeColor(var6),
            Interface.getDay3()
         );
      if (Interface.getInstance().isEnabled() && Interface.check3()) {
         Interface.handleVal(var5, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getAlpha(), Interface.check4());
      }

      this.initInit(var5, mouseX, mouseY, var6);
      this.initInit2(var5, mouseX, mouseY, var6);
      this.initInit3(var5, mouseX, mouseY, var6);
   }

   @Override
   public float getFloat() {
      return this.getY() + InterfaceMetrics.computeFloat(10.0F);
   }

   @Override
   public float getFloat2() {
      return InterfaceMetrics.computeFloat(18.0F);
   }

   public float getFloat3() {
      return this.getFloat() + this.getFloat2() + InterfaceMetrics.computeFloat(8.0F);
   }

   public void initInit(MatrixStack ms, int mouseX, int mouseY, int a) {
      float var6 = InterfaceMetrics.computeFloat(8.0F);
      float var7 = this.getX() + var6;
      float var8 = this.getFloat();
      float var9 = this.getWidth() - var6 * 2.0F;
      float var10 = this.getFloat2();
      float var11 = InterfaceMetrics.computeFloat(4.0F);
      boolean var12 = Geometry.computeFlag((float)mouseX, (float)mouseY, var7, var8, var9, var10);
      this.animatedValue.update();
      this.animatedValue.animateTo(var12 ? 1.0 : 0.0, 150L, Easing.QUINT_OUT, true);
      float var13 = (float)this.animatedValue.getValue();
      float var5;
      if (this.longVal == 0L) {
         var5 = 1.0F;
      } else {
         float var14 = (float)(System.currentTimeMillis() - this.longVal) / 380.0F;
         if (var14 >= 1.0F) {
            var5 = 1.0F;
            this.longVal = 0L;
         } else {
            var5 = Easing.QUINT_OUT.apply(var14);
         }
      }

      float var34 = (float)Math.sin(Math.PI * (double)var5);
      Render.rectRenderer.handleRgb(ms, var7, var8, var9, var10, var11, InterfaceMetrics.computeMax(a, 0.2F + 0.12F * var13));
      if (var34 > 0.001F) {
         Render.rectRenderer.handleRgb(ms, var7, var8, var9, var10, var11, InterfaceMetrics.computeColor3((int)((float)a * 0.3F * var34)));
      }

      float var15 = var8 + var10 / 2.0F;
      float var16 = InterfaceMetrics.computeFloat(8.0F);
      float var17 = var7 + InterfaceMetrics.computeFloat(7.0F);
      Color var18 = ColorMath.initBlue(InterfaceMetrics.computeColor4(a), new Color(255, 255, 255, a), 0.4F + 0.6F * var13);
      FontBootstrap.clientFont15.handleColor(ms, "U", var17, var15 - var16 / 2.0F - InterfaceMetrics.computeFloat(0.8F), var16, var18);
      float var19 = InterfaceMetrics.computeFloat(6.5F);
      float var20 = var17 + var16 + InterfaceMetrics.computeFloat(5.0F);
      FontBootstrap.mdoswa.handleColor(ms, "Translator", var20, var15 - var19 / 2.0F, var19, new Color(255, 255, 255, a));
      float var21 = InterfaceMetrics.computeFloat(6.0F);
      float var22 = InterfaceMetrics.computeFloat(5.0F);
      float var23 = FontBootstrap.mdoswa.computeFloat("ENG", var21) + var22 * 2.0F;
      float var24 = InterfaceMetrics.computeFloat(12.0F);
      float var25 = var7 + var9 - var23 - InterfaceMetrics.computeFloat(4.0F);
      float var26 = var15 - var24 / 2.0F;
      float var27 = var25 + var23 / 2.0F;
      float var28 = var26 + var24 / 2.0F;
      float var29 = 1.0F + 0.16F * var34;
      ms.push();
      ms.translate(var27, var28, 0.0F);
      ms.scale(var29, var29, 1.0F);
      ms.translate(-var27, -var28, 0.0F);
      Render.rectRenderer
         .handleRgb(ms, var25, var26, var23, var24, InterfaceMetrics.computeFloat(3.0F), InterfaceMetrics.computeColor3((int)((float)a * 0.9F)));
      String var30 = EngHelper.getInstance().getEng();
      float var31 = var21 * 0.95F;
      float var32 = var28 - var21 / 2.0F;
      Color var33 = InterfaceMetrics.initInit(InterfaceMetrics.computeColor3(255), a);
      if (var5 < 0.999F) {
         FontBootstrap.mdoswa.handleColor2(ms, this.string, var27, var32 - var5 * var31, var21, ColorMath.initBlue(var33, (int)((float)a * (1.0F - var5))));
      }

      FontBootstrap.mdoswa.handleColor2(ms, var30, var27, var32 + (1.0F - var5) * var31, var21, ColorMath.initBlue(var33, (int)((float)a * var5)));
      ms.pop();
   }

   public void initInit2(MatrixStack ms, int mouseX, int mouseY, int a) {
      float var5 = InterfaceMetrics.computeFloat(8.0F);
      float var6 = this.getFloat3();
      float var7 = this.getWidth() - var5 * 2.0F;
      float var8 = this.getX() + var5;
      FontBootstrap.clientFont6
         .handleColor(ms, "БЫСТРЫЙ ДОСТУП", var8 + InterfaceMetrics.computeFloat(2.0F), var6, InterfaceMetrics.computeFloat(5.0F), new Color(145, 150, 170, a));
      float var9 = var6 + InterfaceMetrics.computeFloat(8.5F);
      float var10 = InterfaceMetrics.computeFloat(24.0F);
      float var11 = InterfaceMetrics.computeFloat(5.0F);
      float var12 = InterfaceMetrics.computeFloat(4.5F);
      boolean var13 = Geometry.computeFlag((float)mouseX, (float)mouseY, var8, var9, var7, var10);
      float var14 = this.computeDefault("btn_cfg", var13);
      int var15 = (int)(22.0 + (double)var14 * 25.0);
      int var16 = (int)(20.0 + (double)var14 * 15.0);
      int var17 = (int)(32.0 + (double)var14 * 45.0);
      Render.rectRenderer.handleRgb(ms, var8, var9, var7, var10, var12, new Color(var15, var16, var17, a));
      Render.rectRenderer.handleRgb(ms, var8, var9, var7, var10, var12, new Color(255, 255, 255, (int)(14.0 * ((double)a / 255.0))));
      if (var14 > 0.02F) {
         Render.rectRenderer.handleRgb(ms, var8, var9, var7, var10, var12, new Color(145, 95, 235, (int)(80.0 * (double)var14)));
      }

      FontBootstrap.clientFont6
         .handleColor(
            ms,
            "\ud83d\udcc1 Конфигурации",
            var8 + InterfaceMetrics.computeFloat(7.0F),
            var9 + InterfaceMetrics.computeFloat(4.5F),
            InterfaceMetrics.computeFloat(6.0F),
            new Color(255, 255, 255, a)
         );
      FontBootstrap.moyewa
         .handleColor(
            ms,
            "Локальные профили",
            var8 + InterfaceMetrics.computeFloat(7.0F),
            var9 + InterfaceMetrics.computeFloat(13.5F),
            InterfaceMetrics.computeFloat(4.6F),
            new Color(165, 165, 185, a)
         );
      FontBootstrap.clientFont6
         .handleColor(
            ms,
            "›",
            var8 + var7 - InterfaceMetrics.computeFloat(10.0F),
            var9 + InterfaceMetrics.computeFloat(7.0F),
            InterfaceMetrics.computeFloat(8.5F),
            new Color(185, 185, 210, a)
         );
      var9 += var10 + var11;
      boolean var18 = Geometry.computeFlag((float)mouseX, (float)mouseY, var8, var9, var7, var10);
      float var19 = this.computeDefault("btn_panic", var18);
      int var20 = (int)(28.0 + (double)var19 * 65.0);
      int var21 = (int)(18.0 + (double)var19 * 10.0);
      int var22 = (int)(22.0 + (double)var19 * 15.0);
      Render.rectRenderer.handleRgb(ms, var8, var9, var7, var10, var12, new Color(var20, var21, var22, a));
      Render.rectRenderer.handleRgb(ms, var8, var9, var7, var10, var12, new Color(255, 255, 255, (int)(14.0 * ((double)a / 255.0))));
      if (var19 > 0.02F) {
         Render.rectRenderer.handleRgb(ms, var8, var9, var7, var10, var12, new Color(235, 75, 75, (int)(90.0 * (double)var19)));
      }

      FontBootstrap.clientFont6
         .handleColor(
            ms,
            "⚡ Panic Key",
            var8 + InterfaceMetrics.computeFloat(7.0F),
            var9 + InterfaceMetrics.computeFloat(4.5F),
            InterfaceMetrics.computeFloat(6.0F),
            new Color(255, 240, 240, a)
         );
      FontBootstrap.moyewa
         .handleColor(
            ms,
            "Скрыть софт мгновенно",
            var8 + InterfaceMetrics.computeFloat(7.0F),
            var9 + InterfaceMetrics.computeFloat(13.5F),
            InterfaceMetrics.computeFloat(4.6F),
            new Color(230, 160, 160, a)
         );
      FontBootstrap.clientFont6
         .handleColor(
            ms,
            "✕",
            var8 + var7 - InterfaceMetrics.computeFloat(10.0F),
            var9 + InterfaceMetrics.computeFloat(7.0F),
            InterfaceMetrics.computeFloat(6.8F),
            new Color(245, 165, 165, a)
         );
   }

   public void initInit3(MatrixStack ms, int mouseX, int mouseY, int a) {
      float var5 = InterfaceMetrics.computeFloat(8.0F);
      float var6 = this.getWidth() - var5 * 2.0F;
      float var7 = InterfaceMetrics.computeFloat(46.0F);
      float var8 = this.getX() + var5;
      float var9 = this.getY() + this.getHeight() - var7 - InterfaceMetrics.computeFloat(8.0F);
      float var10 = InterfaceMetrics.computeFloat(6.0F);
      Render.rectRenderer.handleRgb(ms, var8, var9, var6, var7, var10, new Color(16, 15, 24, (int)(245.0 * ((double)a / 255.0))));
      Render.rectRenderer.handleRgb(ms, var8, var9, var6, var7, var10, new Color(255, 255, 255, (int)(22.0 * ((double)a / 255.0))));
      float var11 = InterfaceMetrics.computeFloat(30.0F);
      float var12 = var8 + InterfaceMetrics.computeFloat(7.0F);
      float var13 = var9 + var7 / 2.0F - var11 / 2.0F;
      Render.rectRenderer
         .handleRgb(
            ms,
            var12 - InterfaceMetrics.computeFloat(1.0F),
            var13 - InterfaceMetrics.computeFloat(1.0F),
            var11 + InterfaceMetrics.computeFloat(2.0F),
            var11 + InterfaceMetrics.computeFloat(2.0F),
            InterfaceMetrics.computeFloat(5.0F),
            new Color(145, 105, 255, (int)(80.0 * ((double)a / 255.0)))
         );
      Identifier var14 = mc.player != null ? mc.player.getSkinTextures().texture() : DefaultSkinHelper.getTexture();
      Render.myazem.handleRgb(ms, var14, var12, var13, var11, var11, 0.0F, InterfaceMetrics.computeFloat(4.0F), Color.WHITE);
      Render.rectRenderer
         .handleRgb(ms, var12, var13, var11, var11, InterfaceMetrics.computeFloat(4.0F), new Color(255, 255, 255, (int)(20.0 * ((double)a / 255.0))));
      float var15 = var12 + var11 + InterfaceMetrics.computeFloat(7.5F);
      String var16 = mc.player != null ? mc.player.getName().getString() : "Player";
      FontBootstrap.clientFont6
         .handleColor(ms, var16, var15, var13 + InterfaceMetrics.computeFloat(1.0F), InterfaceMetrics.computeFloat(7.6F), new Color(255, 255, 255, a));
      float var17 = var13 + InterfaceMetrics.computeFloat(11.5F);
      float var18 = InterfaceMetrics.computeFloat(3.5F);
      Render.rectRenderer
         .handleRgb(
            ms,
            var15 - InterfaceMetrics.computeFloat(0.5F),
            var17 + InterfaceMetrics.computeFloat(1.5F),
            var18 + InterfaceMetrics.computeFloat(1.0F),
            var18 + InterfaceMetrics.computeFloat(1.0F),
            (var18 + InterfaceMetrics.computeFloat(1.0F)) / 2.0F,
            new Color(60, 235, 130, (int)(80.0 * ((double)a / 255.0)))
         );
      Render.rectRenderer.handleRgb(ms, var15, var17 + InterfaceMetrics.computeFloat(2.0F), var18, var18, var18 / 2.0F, new Color(60, 240, 130, a));
      FontBootstrap.clientFont6
         .handleColor(
            ms, "✦ PREMIUM", var15 + var18 + InterfaceMetrics.computeFloat(4.5F), var17, InterfaceMetrics.computeFloat(5.6F), new Color(255, 215, 110, a)
         );
      FontBootstrap.clientFont8
         .handleColor(
            ms,
            "• Active",
            var15 + var18 + InterfaceMetrics.computeFloat(38.0F),
            var17 + InterfaceMetrics.computeFloat(0.5F),
            InterfaceMetrics.computeFloat(4.8F),
            new Color(130, 240, 175, a)
         );
      String var19 = mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : (mc.isInSingleplayer() ? "Одиночный мир" : "В главном меню");
      if (var19.length() > 16) {
         var19 = var19.substring(0, 16) + "..";
      }

      FontBootstrap.clientFont8
         .handleColor(
            ms, "\ud83c\udf10 " + var19, var15, var13 + InterfaceMetrics.computeFloat(21.5F), InterfaceMetrics.computeFloat(5.2F), new Color(170, 175, 200, a)
         );
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         float var6 = InterfaceMetrics.computeFloat(8.0F);
         float var7 = this.getX() + var6;
         float var8 = this.getWidth() - var6 * 2.0F;
         if (Geometry.computeFlag(mouseX, mouseY, (double)var7, (double)this.getFloat(), (double)var8, (double)this.getFloat2())) {
            this.string = EngHelper.getInstance().getEng();
            EngHelper.getInstance().run();
            this.longVal = System.currentTimeMillis();
         } else {
            float var9 = this.getFloat3();
            float var10 = var9 + InterfaceMetrics.computeFloat(9.0F);
            float var11 = InterfaceMetrics.computeFloat(23.0F);
            float var12 = InterfaceMetrics.computeFloat(5.0F);
            if (Geometry.computeFlag(mouseX, mouseY, (double)var7, (double)var10, (double)var8, (double)var11)) {
               MainMenuScreen.currentView = MainMenuScreen.ViewMode.CONFIGS;
               mc.setScreen(FactoryHelper.getInstance().getMainMenuScreen());
            } else {
               var10 += var11 + var12;
               if (Geometry.computeFlag(mouseX, mouseY, (double)var7, (double)var10, (double)var8, (double)var11)) {
                  EnabledSet.getInstance().runEnabled();
               }
            }
         }
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double h, double v) {
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   public float getAlpha() {
      return this.alpha;
   }

   @Override
   public void setAlpha(float alpha) {
      this.alpha = alphax;
   }

   public static Translator getInstance() {
      return translator;
   }
}
