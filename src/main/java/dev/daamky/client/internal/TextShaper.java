package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.text.KeybindTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

public final class TextShaper implements MinecraftAccess {
   public static final ThreadLocal<List<BottomHelper.ColoredGlyph>> threadLocal = ThreadLocal.withInitial(TextShaper::lambda$static$0);

   public static List<BottomHelper.ColoredGlyph> computeClear(Text text) {
      List var1 = threadLocal.get();
      var1.clear();
      initComp(text, -1, var1);
      return var1;
   }

   public static void initComp(Text text, int currentColor, List<BottomHelper.ColoredGlyph> result) {
      Style var3 = text.getStyle();
      int var4 = var3.getColor() != null ? var3.getColor().getRgb() | 0xFF000000 : currentColor;
      TextContent var5 = text.getContent();
      String var6 = "";

      try {
         if (var5 instanceof Literal var7) {
            var6 = var7.string();
         } else if (var5 instanceof PlainTextContent var8) {
            var6 = var8.string();
         } else if (var5 instanceof TranslatableTextContent var9) {
            var6 = var9.getKey();
         } else if (var5 instanceof KeybindTextContent var10) {
            var6 = var10.getKey();
         } else {
            var6 = text.getString();
         }
      } catch (Throwable var11) {
         var6 = text.getString();
      }

      if (var6 == null) {
         var6 = "";
      }

      var6 = NameHelper.initAppend(var6);

      for (int var14 = 0; var14 < var6.length(); var14++) {
         char var16 = var6.charAt(var14);
         if ((var16 == 3618 || var16 == 3591) && var14 + 1 < var6.length()) {
            var14++;
         } else {
            result.add(new BottomHelper.ColoredGlyph(var16, var4));
         }
      }

      for (Text var17 : text.getSiblings()) {
         initComp(var17, var4, result);
      }
   }

   public static void handleString(String message) {
      if (mc.player != null) {
         mc.player
            .sendMessage(
               Text.literal("")
                  .append(computeChar("daamky", true))
                  .append(Text.literal(Formatting.GRAY + " >> " + Formatting.RESET + message)),
               false
            );
      }
   }

   public static String computeFormat(int ticks) {
      if (ticks == -1) {
         return "**:**";
      } else {
         int var1 = ticks / 20;
         int var2 = var1 / 60;
         int var3 = var2 / 60;
         var2 %= 60;
         int var4 = var1 % 60;
         if (var3 > 0) {
            return String.format("%d:%02d:%02d", var3, var2, var4);
         } else {
            return var2 > 0 ? String.format("%d:%02d", var2, var4) : String.format("%ds", var1);
         }
      }
   }

   public static MutableText computeChar(String message, boolean bold) {
      MutableText var2 = Text.empty();
      int var3 = message.length();

      for (int var4 = 0; var4 < var3; var4++) {
         Color var5 = ColorMath.initBlue((float)var4 / (float)Math.max(1, var3 - 1), ThemeColor.getColor4(), ThemeColor.getColor5());
         var2.append(
            Text.literal(String.valueOf(message.charAt(var4)))
               .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var5.getRGB())).withBold(bold))
         );
      }

      return var2;
   }

   public TextShaper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static List lambda$static$0() {
      return new ArrayList<>(128);
   }
}
