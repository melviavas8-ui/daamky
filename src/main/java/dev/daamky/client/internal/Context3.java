package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.setting.StringSetting;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Context3 extends SettingRow {
   public final StringSetting setting;
   public boolean flag;
   public long longVal;

   public Context3(StringSetting setting) {
      super(settingx);
      this.setting = settingx;
      this.handleFloat(this.getFloat());
   }

   public void handleSetting(DrawContext context, int mouseX, int mouseY, float delta) {
      this.handleFloat(this.getFloat());
      MatrixStack var5 = context.getMatrices();
      float var6 = InterfaceMetrics.computeFloat(6.4F);
      float var7 = InterfaceMetrics.computeFloat(6.4F);
      int var8 = (int)(this.clamp() * 255.0F);
      FontBootstrap.clientFont10
         .handleColor(
            var5,
            EngHelper.computeString(this.setting.getName()),
            this.getX(),
            this.getY() + InterfaceMetrics.computeFloat(1.0F),
            var6,
            InterfaceMetrics.computeColor4(var8)
         );
      float var9 = InterfaceMetrics.computeFloat(10.0F);
      float var10 = this.getY() + var6 + InterfaceMetrics.computeFloat(3.0F);
      float var11 = InterfaceMetrics.computeFloat(3.0F);
      Render.rectRenderer
         .handleRgb(
            var5,
            this.getX(),
            var10,
            this.getWidth(),
            var9,
            var11,
            this.flag ? InterfaceMetrics.computeColor3((int)((float)var8 * 0.16F)) : InterfaceMetrics.computeMax2(var8, 0.62F)
         );
      String var12 = this.setting.getValue() == null ? "" : this.setting.getValue();
      boolean var13 = var12.isEmpty();
      boolean var14 = var13 && !this.flag;
      String var15 = var14 ? this.setting.getPlaceholder() : var12;
      boolean var16 = this.flag && (System.currentTimeMillis() - this.longVal) % 1000L < 500L;
      String var17 = var16 ? "|" : "";
      float var18 = InterfaceMetrics.computeFloat(4.0F);
      float var19 = this.getWidth() - var18 * 2.0F;
      String var20 = var15;

      while (!var20.isEmpty() && FontBootstrap.clientFont10.computeFloat(var20 + var17, var7) > var19) {
         var20 = var20.substring(1);
      }

      Color var21 = var14 ? InterfaceMetrics.computeColor4((int)((float)var8 * 0.55F)) : InterfaceMetrics.computeColor2(var8);
      float var22 = this.getX() + var18;
      float var23 = var10 + var9 / 2.0F - var7 / 2.0F + InterfaceMetrics.computeFloat(0.1F);
      FontBootstrap.clientFont10.handleColor(var5, var20 + var17, var22, var23, var7, var21);
   }

   @Override
   public float getFloat() {
      return 22.0F;
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         boolean var6;
         this.flag = var6 = Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight());
         if (var6) {
            this.longVal = System.currentTimeMillis();
         }
      }
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.flag) {
         Object var4 = this.setting.getValue() == null ? "" : this.setting.getValue();
         boolean var5 = (modifiers & 2) != 0;
         if (var5 && keyCode == 86) {
            try {
               String var9 = MinecraftClient.getInstance().keyboard.getClipboard();
               this.setting.setValue(this.initAppend((String)var4, var9));
            } catch (Exception var8) {
            }

            this.longVal = System.currentTimeMillis();
         } else {
            switch (keyCode) {
               case 32:
               case 44:
                  this.setting.setValue(this.computeMax(var4 + " "));
                  break;
               case 45:
               case 333:
                  this.setting.setValue(this.computeMax(var4 + "-"));
                  break;
               case 46:
               case 330:
                  this.setting.setValue(this.computeMax(var4 + "."));
                  break;
               case 256:
               case 257:
               case 335:
                  this.flag = false;
                  break;
               case 259:
                  if (!var4.isEmpty()) {
                     this.setting.setValue(var4.substring(0, var4.length() - 1));
                  }
                  break;
               default:
                  Character var7 = this.computeCharacter(keyCode);
                  if (var7 != null) {
                     this.setting.setValue(this.computeMax(var4 + var7));
                  }
            }

            this.longVal = System.currentTimeMillis();
         }
      }
   }

   public String initAppend(String base, String add) {
      if (add == null) {
         return base;
      } else {
         StringBuilder var3 = new StringBuilder(base);

         for (char var7 : add.toCharArray()) {
            if (Character.isDigit(var7) || var7 == '-' || var7 == '.') {
               var3.append(var7);
            } else if (var7 == ' ' || var7 == ',' || var7 == ';') {
               var3.append(' ');
            }
         }

         return this.computeMax(var3.toString());
      }
   }

   public String computeMax(String s) {
      int var2 = this.setting.getMaxLength();
      return s.length() > var2 ? s.substring(0, var2) : s;
   }

   public Character computeCharacter(int keyCode) {
      if (keyCode >= 48 && keyCode <= 57) {
         return(charVal)(48 + (keyCode - 48));
      } else {
         return keyCode >= 320 && keyCode <= 329 ? (char)(48 + (keyCode - 320)) : null;
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double h, double v) {
   }
}
