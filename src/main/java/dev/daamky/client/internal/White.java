package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class White extends Widget {
   public static final White white = new White();
   public final List<Context> list = new ArrayList<>();
   public final List<White.ThemeBound> list2 = new ArrayList<>();
   public final ColorTheme colorTheme;
   public ColorTheme theme;
   public ColorTheme colorTheme2;
   public float floatVal;
   public float float2;
   public float float3;
   public float float4;
   public float float5;
   public float float6;
   public final AnimatedValue animatedValue;
   public boolean open;
   public float anim;

   public White() {
      this.theme = this.colorTheme = new ColorTheme("White");
      this.animatedValue = new AnimatedValue();
   }

   public void runTheme() {
      String var1 = this.theme != null ? this.theme.getName() : null;
      ThemeManager.getInstance().loadDefault();
      if (var1 != null) {
         for (Context var3 : this.list) {
            if (var3.getTheme().getName().equalsIgnoreCase(var1)) {
               this.theme = var3.getTheme();
               break;
            }
         }
      }
   }

   public void run() {
      ThemeManager.getInstance().loadDefault();
   }

   public void handleTheme(boolean last) {
      if (!last) {
         ThemeManager.getInstance().runNext();
      } else if (this.theme != null) {
         ThemeManager.getInstance().writeName(this.theme);
      }
   }

   public void setTheme(ColorTheme theme) {
      if (themex != null) {
         this.theme = themex;
      }
   }

   public void handleColorTheme(ColorTheme theme) {
      this.colorTheme2 = themex;
      if (themex != null) {
         this.animatedValue.handleStart(0.0);
      }
   }

   public void initTheme() {
      ThemeManager.getInstance().loadDefault();
      ColorTheme var1 = ThemeManager.getInstance().initTrim();
      if (var1 != null) {
         Context var2 = null;

         for (Context var4 : this.list) {
            if (var4.getTheme().getName().equalsIgnoreCase(var1.getName())) {
               var2 = var4;
               break;
            }
         }

         if (var2 != null) {
            this.theme = var2.getTheme();
         } else {
            this.list.add(new Context(var1));
            this.theme = var1;
         }
      } else {
         Context var5 = null;

         for (Context var7 : this.list) {
            if (var7.getTheme().getName().equalsIgnoreCase(this.colorTheme.getName())) {
               var5 = var7;
               break;
            }
         }

         if (var5 != null) {
            this.theme = var5.getTheme();
         } else {
            this.list.add(new Context(this.colorTheme));
            this.theme = this.colorTheme;
         }
      }
   }

   public void initNext(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.colorTheme2 != null) {
         this.animatedValue.update();
         this.animatedValue.animateTo(this.open ? 1.0 : 0.0, 200L, Easing.QUINT_OUT, true);
         float var5 = (float)this.animatedValue.getValue();
         if (!(var5 <= 0.01F)) {
            int var6 = (int)(var5 * this.anim * 255.0F);
            if (var6 >= 5) {
               float var7 = InterfaceMetrics.computeFloat(8.0F);
               float var8 = InterfaceMetrics.computeFloat(130.0F);
               this.setWidth(var8);
               MatrixStack var9 = context.getMatrices();
               this.list2.clear();
               float var10 = InterfaceMetrics.computeFloat(16.0F);
               float var11 = InterfaceMetrics.computeFloat(6.5F);
               float var12 = this.getWidth() - var7 * 2.0F;
               float var13 = this.getY() + InterfaceMetrics.computeFloat(10.0F);
               float var14 = InterfaceMetrics.computeFloat(7.0F);
               var13 += var14 + InterfaceMetrics.computeFloat(10.0F);

               for (ColorTheme.ElementColor var16 : this.colorTheme2.getList()) {
                  float var17 = var10;
                  ExpandableSettingComponent var18 = var16.getExpandableSettingComponent();
                  var18.run();
                  float var19 = var18.getFloat();
                  if ((double)var19 > 0.0) {
                     var17 = var10 + var18.getHeight() + InterfaceMetrics.computeFloat(2.0F) * var19;
                  }

                  this.list2.add(new White.ThemeBound(this.getX() + var7, var13, var12, var17, var16));
                  var13 += var17 + InterfaceMetrics.computeFloat(2.0F);
               }

               this.setHeight(Math.max(var13 - this.getY() + var7, InterfaceMetrics.computeFloat(100.0F)));
               Render.shaderProgram
                  .handleRgb(
                     var9,
                     this.getX(),
                     this.getY(),
                     this.getWidth(),
                     this.getHeight(),
                     InterfaceMetrics.computeFloat(10.0F),
                     InterfaceMetrics.computeColor(var6),
                     Interface.getDay3()
                  );
               if (Interface.getInstance().isEnabled() && Interface.check3()) {
                  Interface.handleVal(var9, this.getX(), this.getY(), this.getWidth(), this.getHeight(), var5 * this.anim, Interface.check4());
               }

               var13 = this.getY() + InterfaceMetrics.computeFloat(10.0F);
               FontBootstrap.clientFont15.handleColor(var9, "h", this.getX() + var7, var13, var14, InterfaceMetrics.computeColor2(var6));
               FontBootstrap.mdoswa
                  .handleColor(
                     var9,
                     this.colorTheme2.getName(),
                     this.getX() + var7 + var14 + InterfaceMetrics.computeFloat(4.0F),
                     var13,
                     var11,
                     InterfaceMetrics.computeColor2(var6)
                  );
               this.floatVal3 = InterfaceMetrics.computeFloat(7.0F);
               this.floatVal = this.getX() + this.getWidth() - var7 - this.floatVal3;
               this.floatVal2 = var13;
               boolean var28 = Geometry.computeFlag((float)mouseX, (float)mouseY, this.floatVal, this.floatVal2, this.floatVal3, this.floatVal3);
               FontBootstrap.clientFont15
                  .handleColor(
                     var9, "o", this.floatVal, this.floatVal2, this.floatVal3, var28 ? InterfaceMetrics.computeColor2(var6) : InterfaceMetrics.computeColor5(var6)
                  );
               if (this.list.size() > 1) {
                  this.floatVal6 = InterfaceMetrics.computeFloat(7.0F);
                  this.floatVal4 = this.floatVal - this.floatVal6 - InterfaceMetrics.computeFloat(6.0F);
                  this.floatVal5 = var13;
                  boolean var29 = Geometry.computeFlag((float)mouseX, (float)mouseY, this.floatVal4, this.floatVal5, this.floatVal6, this.floatVal6);
                  FontBootstrap.clientFont15
                     .handleColor(
                        var9, "j", this.floatVal4, this.floatVal5, this.floatVal6, var29 ? ThemeColor.computeNegative(var6) : InterfaceMetrics.computeColor5(var6)
                     );
               } else {
                  this.floatVal4 = -1.0F;
               }

               var13 += var14 + InterfaceMetrics.computeFloat(10.0F);

               for (White.ThemeBound var31 : this.list2) {
                  ColorTheme.ElementColor var32 = var31.elementColor;
                  float var33 = var31.y;
                  Render.rectRenderer
                     .handleRgb(var9, var31.x, var33, var31.width, var10, InterfaceMetrics.computeFloat(3.5F), InterfaceMetrics.computeMax(var6, 0.2F));
                  FontBootstrap.clientFont10
                     .handleColor(
                        var9,
                        var32.getName(),
                        var31.x + InterfaceMetrics.computeFloat(4.0F),
                        var33 + var10 / 2.0F - var11 / 2.0F,
                        var11,
                        InterfaceMetrics.computeColor2(var6)
                     );
                  float var20 = InterfaceMetrics.computeFloat(10.0F);
                  float var21 = InterfaceMetrics.computeFloat(8.0F);
                  float var22 = var31.x + var31.width - var20 - InterfaceMetrics.computeFloat(4.0F);
                  float var23 = var33 + var10 / 2.0F - var21 / 2.0F;
                  Render.rectRenderer
                     .handleRgb(var9, var22, var23, var20, var21, InterfaceMetrics.computeFloat(2.0F), ColorMath.initBlue(var32.getColor(), var6));
                  ExpandableSettingComponent var24 = var32.getExpandableSettingComponent();
                  if ((double)var24.getFloat() > 0.0) {
                     var24.setX(var31.x);
                     var24.setY(var33 + var10);
                     var24.setWidth(var31.width);
                     var24.setAlpha((float)var6 / 255.0F);
                     var24.initSetting(context, mouseX, mouseY, delta);
                  }
               }
            }
         }
      }
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.open && this.colorTheme2 != null) {
         if (Geometry.computeFlag(mouseX, mouseY, (double)this.floatVal, (double)this.floatVal2, (double)this.floatVal3, (double)this.floatVal3)) {
            this.colorTheme2 = null;
         } else if (this.floatVal4 != -1.0F
            && Geometry.computeFlag(mouseX, mouseY, (double)this.floatVal4, (double)this.floatVal5, (double)this.floatVal6, (double)this.floatVal6)) {
            if (this.list.removeIf(this::lambda$mouseClicked$0)) {
               ThemeManager.getInstance().remove(this.colorTheme2.getName());
               this.colorTheme2 = null;
            }
         } else {
            for (White.ThemeBound var7 : this.list2) {
               ExpandableSettingComponent var8 = var7.elementColor.getExpandableSettingComponent();
               float var9 = InterfaceMetrics.computeFloat(16.0F);
               if (Geometry.computeFlag(mouseX, mouseY, (double)var7.x, (double)var7.y, (double)var7.width, (double)var9)) {
                  var8.run2();
                  return;
               }

               if (Geometry.computeFlag(mouseX, mouseY, (double)var7.x, (double)(var7.y + var9), (double)var7.width, (double)(var7.height - var9))) {
                  var8.mouseClicked(mouseX, mouseY, button);
               }
            }
         }
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
      if (this.open && this.colorTheme2 != null) {
         for (White.ThemeBound var7 : this.list2) {
            var7.elementColor.getExpandableSettingComponent().mouseReleased(mouseX, mouseY, button);
         }
      }
   }

   public boolean computeFlag(char chr, int modifiers) {
      return false;
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
   }

   public List<Context> getList() {
      return this.list;
   }

   public List<White.ThemeBound> getList2() {
      return this.list2;
   }

   public ColorTheme getColorTheme() {
      return this.colorTheme;
   }

   public ColorTheme getTheme() {
      return this.theme;
   }

   public ColorTheme getColorTheme2() {
      return this.colorTheme2;
   }

   @Override
   public float getFloat() {
      return this.floatVal;
   }

   @Override
   public float getFloat2() {
      return this.floatVal2;
   }

   public float getFloat3() {
      return this.floatVal3;
   }

   public float getFloat4() {
      return this.floatVal4;
   }

   public float getFloat5() {
      return this.floatVal5;
   }

   public float getFloat6() {
      return this.floatVal6;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public boolean isOpen() {
      return this.open;
   }

   public float getAnim() {
      return this.anim;
   }

   public static White getInstance() {
      return white;
   }

   public void setOpen(boolean open) {
      this.open = openx;
   }

   public void setAnim(float anim) {
      this.anim = animx;
   }

   public boolean lambda$mouseClicked$0(Context ts) {
      return ts.getTheme() == this.colorTheme2;
   }

   private static record ThemeBound(float x, float y, float width, float height, ColorTheme.ElementColor elementColor) {
   }
}
