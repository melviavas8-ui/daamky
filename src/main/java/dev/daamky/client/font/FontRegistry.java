package dev.daamky.client.font;

import dev.daamky.client.internal.ClientFont;
import dev.daamky.client.internal.TintColorHelper;
import net.minecraft.client.util.math.MatrixStack;

public class FontRegistry {
   public static final MatrixStack SHARED_MS = TintColorHelper.class_4587;
   public final String name;
   public final ClientFont delegate;

   public FontRegistry(String name) {
      this.name = name;
      this.delegate = computeClientFont(name);
   }

   public static ClientFont computeClientFont(String name) {
      if (name == null) {
         return FontBootstrap.moyewa;
      } else {
         String var1 = name.toLowerCase();

         return switch (var1) {
            case "sf_bold" -> FontBootstrap.clientFont6;
            case "sf_medium" -> FontBootstrap.clientFont8;
            case "sf_regular" -> FontBootstrap.moyewa;
            case "icon", "nightix", "nightix_2", "category", "gui", "icongui" -> FontBootstrap.clientFont15;
            case "exp_icons", "inter_icons", "icons_exp" -> FontBootstrap.clientFont5;
            case "client_logo", "logo" -> FontBootstrap.clientFont16;
            default -> FontBootstrap.moyewa;
         };
      }
   }

   public void handleDelegate(String text, float x, float y, float size, int color) {
      if (text != null && !text.isEmpty()) {
         this.delegate.handleVal(SHARED_MS, text, x, y, size, color);
      }
   }

   public void handleDelegate2(String text, float x, float y, float size, int color) {
      if (text != null && !text.isEmpty()) {
         this.delegate.handleVal2(SHARED_MS, text, x, y, size, color);
      }
   }

   public void handleChar(String text, float x, float y, float width, int color, float size) {
      if (text != null && !text.isEmpty() && !(width <= 0.0F)) {
         float var7 = x;
         float var8 = y;
         float var9 = this.computeDelegate(" ", size);
         float var10 = this.computeDelegate(size) + 2.0F;
         int var11 = text.length();
         int var12 = 0;

         for (int var13 = 0; var13 <= var11; var13++) {
            if (var13 == var11 || text.charAt(var13) == ' ') {
               if (var13 > var12) {
                  String var14 = text.substring(var12, var13);
                  float var15 = this.computeDelegate(var14, size);
                  if (var7 + var15 > x + width && var7 > x) {
                     var7 = x;
                     var8 += var10;
                  }

                  this.delegate.handleVal(SHARED_MS, var14, var7, var8, size, color);
                  var7 += var15 + var9;
               }

               var12 = var13 + 1;
            }
         }
      }
   }

   public void handleVal(String text, float x, float y, float width, int color, float size) {
      this.handleDelegate(text, x, y, size, color);
   }

   public void handleVal2(String text, float x, float y, float width, int color, float size) {
      this.handleDelegate(text, x, y, size, color);
   }

   public float computeDelegate(String text, float size) {
      return text != null && !text.isEmpty() ? this.delegate.computeFloat(text, size) : 0.0F;
   }

   public float computeDelegate(float size) {
      return this.delegate.computeFloat(size);
   }

   public float computeChar(String text, float width, float size) {
      if (text != null && !text.isEmpty() && !(width <= 0.0F)) {
         float var4 = 0.0F;
         int var5 = 1;
         float var6 = this.computeDelegate(" ", size);
         int var7 = text.length();
         int var8 = 0;

         for (int var9 = 0; var9 <= var7; var9++) {
            if (var9 == var7 || text.charAt(var9) == ' ') {
               if (var9 > var8) {
                  String var10 = text.substring(var8, var9);
                  float var11 = this.computeDelegate(var10, size);
                  if (var4 + var11 > width && var4 > 0.0F) {
                     var5++;
                     var4 = var11 + var6;
                  } else {
                     var4 += var11 + var6;
                  }
               }

               var8 = var9 + 1;
            }
         }

         return(floatVal)var5 * (this.computeDelegate(size) + 2.0F);
      } else {
         return 0.0F;
      }
   }
}
