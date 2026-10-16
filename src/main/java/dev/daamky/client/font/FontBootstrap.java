package dev.daamky.client.font;

import dev.daamky.client.internal.ClientFont;
import dev.daamky.client.internal.Render;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public final class FontBootstrap {
   public static final String string = "montserrat";
   public static final String string2 = "product_sans";
   public static final ClientFont mkadw = new ClientFont("inter/inter-semi");
   public static final ClientFont mheuy = new ClientFont("inter/inter-bold");
   public static final ClientFont clientFont = mheuy;
   public static final ClientFont clientFont2 = new ClientFont("inter/inter-extrabold");
   public static final ClientFont clientFont3 = new ClientFont("inter/inter-extrabold");
   public static final ClientFont clientFont4 = new ClientFont("inter/instrument-serif-italic");
   public static final ClientFont clientFont5 = new ClientFont("inter/icons");
   public static final ClientFont clientFont6 = new ClientFont("sf_pro/sf_bold");
   public static final ClientFont clientFont7 = new ClientFont("sf_pro/sf_semibold");
   public static final ClientFont clientFont8 = new ClientFont("sf_pro/sf_medium");
   public static final ClientFont moyewa = new ClientFont("sf_pro/sf_regular");
   public static final ClientFont mibpoq = new ClientFont("sf_pro/sf_light");
   public static final ClientFont clientFont9 = new ClientFont("product_sans/productsans_black");
   public static final ClientFont mdoswa = new ClientFont("product_sans/productsans_bold");
   public static final ClientFont clientFont10 = new ClientFont("product_sans/productsans_medium");
   public static final ClientFont clientFont11 = new ClientFont("product_sans/productsans_regular");
   public static final ClientFont clientFont12 = new ClientFont("product_sans/productsans_light");
   public static final ClientFont clientFont13 = new ClientFont("product_sans/productsans_thin");
   public static final ClientFont clientFont14 = new ClientFont("other/icons");
   public static final ClientFont clientFont15 = new ClientFont("other/daamky_font_v2");
   public static final ClientFont clientFont16 = new ClientFont("other/client_logo");
   public static final Identifier class_2960 = net.minecraft.util.Identifier.of("minecraft", "textures/gui/title/icon_256.png");

   public static void handleColor(MatrixStack matrixStack, float x, float y, float size, Color color) {
      Render.myazem.handleRgb(matrixStack, x, y, size, size, 0.0F, color, 0.0F, 0.0F, 1.0F, 1.0F, class_2960);
   }

   public static void runDlc() {
      try {
         clientFont6.computeFloat("Daamky", 9.0F);
         clientFont8.computeFloat("12:00", 6.0F);
         moyewa.computeFloat("Singleplayer", 7.0F);
         clientFont15.computeFloat("user", 8.0F);
      } catch (Throwable var1) {
      }
   }

   public static float getFloat() {
      return 0.08F;
   }

   public static float getFloat2() {
      return 0.12F;
   }

   public FontBootstrap() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
