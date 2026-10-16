package dev.daamky.client.util;

import dev.daamky.client.internal.GlyphMap;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class FormattingParser {
   public static final Pattern pattern = Pattern.compile("\\$\\{(rgba|rgb)\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3})(?:,(\\d{1,3}))?\\)}|\\$\\{reset}", 2);
   public static final HashMap<String, String> hashMap = new HashMap<>();

   public static String computeNext(String input) {
      if (input == null || input.isEmpty()) {
         return input;
      } else if (input.indexOf("${") < 0) {
         return input;
      } else {
         String var1 = input;

         for (Entry var3 : hashMap.entrySet()) {
            var1 = var1.replace((CharSequence)var3.getKey(), (CharSequence)var3.getValue());
         }

         return var1;
      }
   }

   public static String computeString(int red, int green, int blue) {
      return "${rgb(" + red + "," + green + "," + blue + ")}";
   }

   public static String computeString(int red, int green, int blue, int alpha) {
      return "${rgba(" + red + "," + green + "," + blue + "," + alpha + ")}";
   }

   public static String computeString(int color) {
      return computeString(GlyphMap.computeInt(color), GlyphMap.computeInt2(color), GlyphMap.computeInt3(color), GlyphMap.computeInt4(color));
   }

   public static Text gradient(String message) {
      MutableText var1 = Text.empty();

      for (int var2 = 0; var2 < message.length(); var2++) {
         int var3 = GlyphMap.computeInt6(var2);
         var1.append(Text.literal(String.valueOf(message.charAt(var2))).styled(FormattingParser::lambda$gradient$0));
      }

      return var1;
   }

   public static int computeInt(int firstColor, int secondColor, float factorPercent) {
      return GlyphMap.computeInt2(firstColor, secondColor, factorPercent);
   }

   public static String getReset() {
      return "${reset}";
   }

   public static String computeReplace(String text) {
      if (text == null || text.isEmpty()) {
         return text;
      } else {
         return text.indexOf("${") < 0 ? text : pattern.matcher(text).replaceAll("");
      }
   }

   public static String getRgb() {
      return "rgb";
   }

   public static String getRgba() {
      return "rgba";
   }

   public static String computeQuote(String text, int red, int green, int blue) {
      return pattern.matcher(text).replaceAll(Matcher.quoteReplacement(computeString(red, green, blue)));
   }

   public static String computeQuote(String text, int red, int green, int blue, int alpha) {
      return pattern.matcher(text).replaceAll(Matcher.quoteReplacement(computeString(red, green, blue, alpha)));
   }

   public static FormattingParser.ColorTag parseGroup(String text, int index, int defaultColor) {
      if (text == null || index < 0 || index >= text.length() - 1) {
         return null;
      } else if (text.charAt(index) == '$' && text.charAt(index + 1) == '{') {
         Matcher var3 = pattern.matcher(text);
         if (!var3.find(index) || var3.start() != index) {
            return null;
         } else if (var3.group().equalsIgnoreCase(getReset())) {
            return new FormattingParser.ColorTag(defaultColor, var3.end() - index);
         } else {
            String var4 = var3.group(1).toLowerCase();
            int var5 = computeMax(Integer.parseInt(var3.group(2)));
            int var6 = computeMax(Integer.parseInt(var3.group(3)));
            int var7 = computeMax(Integer.parseInt(var3.group(4)));
            int var8 = var4.equals(getRgba()) && var3.group(5) != null ? computeMax(Integer.parseInt(var3.group(5))) : GlyphMap.computeInt4(defaultColor);
            return new FormattingParser.ColorTag(GlyphMap.getColor(var5, var6, var7, var8), var3.end() - index);
         }
      } else {
         return null;
      }
   }

   public static String computeString(String text) {
      return text != null && !text.isEmpty() ? computeReplace(computeNext(text)) : text;
   }

   public static int computeMax(int value) {
      return Math.max(0, Math.min(255, value));
   }

      public FormattingParser() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Style lambda$gradient$0(int fade, Style style) {
      return style.withColor(fade);
   }

   static {
      hashMap.put("${black}", Formatting.BLACK.toString());
      hashMap.put("${dark_blue}", Formatting.DARK_BLUE.toString());
      hashMap.put("${dark_green}", Formatting.DARK_GREEN.toString());
      hashMap.put("${dark_aqua}", Formatting.DARK_AQUA.toString());
      hashMap.put("${dark_red}", Formatting.DARK_RED.toString());
      hashMap.put("${dark_purple}", Formatting.DARK_PURPLE.toString());
      hashMap.put("${orange}", Formatting.GOLD.toString());
      hashMap.put("${gray}", Formatting.GRAY.toString());
      hashMap.put("${dark_gray}", Formatting.DARK_GRAY.toString());
      hashMap.put("${blue}", Formatting.BLUE.toString());
      hashMap.put("${green}", Formatting.GREEN.toString());
      hashMap.put("${aqua}", Formatting.AQUA.toString());
      hashMap.put("${red}", Formatting.RED.toString());
      hashMap.put("${purple}", Formatting.LIGHT_PURPLE.toString());
      hashMap.put("${yellow}", Formatting.YELLOW.toString());
      hashMap.put("${white}", Formatting.WHITE.toString());
      hashMap.put("${bold}", Formatting.BOLD.toString());
      hashMap.put("${reset}", Formatting.RESET.toString());
   }

   public static record ColorTag(int color, int length) {
   }
}
