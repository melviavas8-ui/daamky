package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.ModeSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Bound extends SettingRow {
   public final ModeSetting setting;
   public final List<dev.daamky.client.internal.Bound.BoundRec> list = new ArrayList<>();
   public final Map<String, AnimatedValue> map = new HashMap<>();
   public final Map<String, AnimatedValue> map2 = new HashMap<>();

   public Bound(ModeSetting setting) {
      super(settingx);
      this.setting = settingx;

      for (String var3 : settingx.getValues()) {
         this.map.put(var3, new AnimatedValue());
         this.map2.put(var3, new AnimatedValue());
      }
   }

   public void initInit(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var5 = context.getMatrices();
      float var6 = InterfaceMetrics.computeFloat(5.5F);
      int var7 = (int)(this.clamp() * 255.0F);
      FontBootstrap.clientFont10
         .handleColor(
            var5,
            EngHelper.computeString(this.setting.getName()),
            this.getX(),
            this.getY() + InterfaceMetrics.computeFloat(1.0F),
            var6,
            InterfaceMetrics.computeColor4(var7)
         );
      this.list.clear();
      float var8 = this.getX();
      float var9 = this.getY() + var6 + InterfaceMetrics.computeFloat(5.0F);
      float var10 = InterfaceMetrics.computeFloat(13.0F);
      float var11 = InterfaceMetrics.computeFloat(2.0F);
      float var12 = InterfaceMetrics.computeFloat(3.0F);
      float var13 = InterfaceMetrics.computeFloat(6.0F);

      for (String var15 : this.setting.getValues()) {
         AnimatedValue var16 = this.map.get(var15);
         var16.update();
         var16.animateTo(this.setting.is(var15) ? 1.0 : 0.0, 220L, Easing.QUINT_OUT);
         float var17 = (float)var16.getValue();
         AnimatedValue var18 = this.map2.get(var15);
         var18.update();
         String var19 = EngHelper.computeString(var15);
         float var20 = FontBootstrap.clientFont10.computeFloat(var19, var6);
         float var21 = var20 + var13 * 2.0F;
         if (var21 > this.getWidth()) {
            var21 = this.getWidth();
         }

         if (var8 + var21 > this.getX() + this.getWidth()) {
            var8 = this.getX();
            var9 += var10 + var12;
         }

         boolean var22 = Geometry.computeFlag((float)mouseX, (float)mouseY, var8, var9, var21, var10);
         var18.animateTo(var22 ? 1.0 : 0.0, 180L, Easing.QUINT_OUT);
         float var23 = (float)var18.getValue();
         this.list.add(new dev.daamky.client.internal.Bound.BoundRec(var8, var9, var21, var10, var15));
         if (var17 > 0.02F) {
            float var24 = InterfaceMetrics.computeFloat(1.0F) * var17;
            Render.rectRenderer
               .handleRgb(
                  var5,
                  var8 - var24,
                  var9 - var24,
                  var21 + var24 * 2.0F,
                  var10 + var24 * 2.0F,
                  InterfaceMetrics.computeFloat(4.0F),
                  InterfaceMetrics.computeColor3((int)((float)var7 * 0.22F * var17))
               );
         }

         Color var33 = InterfaceMetrics.computeColor3((int)((float)var7 * 0.9F));
         Color var25 = new Color(24, 24, 32, (int)((float)var7 * (0.35F + 0.25F * var23)));
         Color var26 = ColorMath.initBlue(var33, var25, var17);
         Render.rectRenderer.handleRgb(var5, var8, var9, var21, var10, InterfaceMetrics.computeFloat(3.5F), var26);
         Color var27 = InterfaceMetrics.computeColor3(255);
         double var28 = 0.299 * (double)var27.getRed() + 0.587 * (double)var27.getGreen() + 0.114 * (double)var27.getBlue();
         Color var30 = var28 > 170.0 ? new Color(20, 20, 20, var7 & 0xFF) : new Color(255, 255, 255, var7 & 0xFF);
         Color var31 = ColorMath.initBlue(InterfaceMetrics.computeColor2(var7), InterfaceMetrics.computeColor5(var7), var23);
         Color var32 = ColorMath.initBlue(var30, var31, var17);
         FontBootstrap.clientFont10
            .handleColor2(var5, var19, var8 + var21 / 2.0F, var9 + var10 / 2.0F - var6 / 2.0F - InterfaceMetrics.computeFloat(0.5F), var6, var32);
         var8 += var21 + var11;
      }

      this.setHeight(var9 - this.getY() + var10 + InterfaceMetrics.computeFloat(3.0F));
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      for (dev.daamky.client.internal.Bound.BoundRec var7 : this.list) {
         if (Geometry.computeFlag(mouseX, mouseY, (double)var7.x, (double)var7.y, (double)var7.width, (double)var7.height)) {
            this.setting.setValue(var7.value);
         }
      }
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
   }

   private static class BoundRec {
      public final float x;
      public final float y;
      public final float width;
      public final float height;
      public final String value;

      public BoundRec(float x, float y, float width, float height, String value) {
         this.x = xx;
         this.y = yx;
         this.width = widthx;
         this.height = heightx;
         this.value = valuex;
      }
   }
}
