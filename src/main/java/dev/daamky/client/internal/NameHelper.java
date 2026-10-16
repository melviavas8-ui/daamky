package dev.daamky.client.internal;

import dev.daamky.client.module.misc.Anonymous;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;

public final class NameHelper {
   public static final Map<String, String> map = new ConcurrentHashMap<>();
   public static final Map<Character, String> map2 = new HashMap<>();

   public static boolean computeChar(String s) {
      if (s == null) {
         return false;
      } else {
         int var1 = s.length();

         for (int var2 = 0; var2 < var1; var2++) {
            if (s.charAt(var2) > 127) {
               return true;
            }
         }

         return false;
      }
   }

   public static Text computeClass_2561(Text input, String target, String replacement) {
      if (input != null && target != null && replacement != null) {
         MutableText var3 = Text.empty().setStyle(input.getStyle());
         handleNext(var3, input, target, replacement);
         return var3;
      } else {
         return input;
      }
   }

   public static void handleNext(MutableText result, Text current, String target, String replacement) {
      TextContent var4 = current.getContent();
      Style var5 = current.getStyle();
      if (var4 instanceof Literal var6) {
         String var7 = var6.string().replace(target, replacement);
         result.append(Text.literal(var7).setStyle(var5));
      }

      for (Text var9 : current.getSiblings()) {
         handleNext(result, var9, target, replacement);
      }
   }

   public static String initAppend(String string) {
      if (string != null && computeChar(string)) {
         if (string.contains("ꕗꕘ")) {
            string = string.replace("ꕗꕘ", Formatting.YELLOW + "SPONSOR");
         }

         StringBuilder var1 = null;
         int var2 = string.length();

         for (int var3 = 0; var3 < var2; var3++) {
            char var4 = string.charAt(var3);
            String var5 = map2.get(var4);
            if (var5 != null) {
               if (var1 == null) {
                  var1 = new StringBuilder(var2 + 16);
                  var1.append(string, 0, var3);
               }

               var1.append(var5);
            } else if (var1 != null) {
               var1.append(var4);
            }
         }

         return var1 != null ? var1.toString() : string;
      } else {
         return string;
      }
   }

   public static Text computeEquals(Text text) {
      if (text == null) {
         return null;
      } else {
         String var1 = text.getString();
         if (!computeChar(var1)) {
            return text;
         } else {
            String var2 = initAppend(var1);
            return (Text)(var2.equals(var1) ? text : Text.literal(var2).setStyle(text.getStyle()));
         }
      }
   }

   public static String initAppend(String source, String target, String replacement) {
      if (source != null && target != null && replacement != null && !target.isEmpty()) {
         StringBuilder var4 = new StringBuilder();
         int var5 = 0;
         String var6 = source.toLowerCase();
         String var7 = target.toLowerCase();
         int var8 = target.length();

         int var3;
         while ((var3 = var6.indexOf(var7, var5)) != -1) {
            var4.append(source, var5, var3);
            var4.append(replacement);
            var5 = var3 + var8;
         }

         var4.append(source, var5, source.length());
         return var4.toString();
      } else {
         return source;
      }
   }

   public static String protectedString(String text) {
      if (text == null) {
         return null;
      } else {
         Anonymous var1 = Anonymous.getInstance();
         if (!var1.isEnabled()) {
            return text;
         } else {
            if (map.size() > 1000) {
               map.clear();
            }

            return map.computeIfAbsent(text, NameHelper::lambda$protectedString$0);
         }
      }
   }

   public NameHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static String lambda$protectedString$0(Anonymous streamerMode, String t) {
      String var2 = t;
      if (streamerMode.getNoFunTime().isEnabled("Name")) {
         String var3 = MinecraftClient.getInstance().getSession().getUsername();
         if (var3 != null) {
            var2 = t.replace(var3, streamerMode.getMc());
         }

         if (streamerMode.getNoFunTime().isEnabled("Hide friends")) {
            for (String var5 : FriendsStore.getInstance().getData()) {
               var2 = var2.replace(var5, streamerMode.computeEnabled(var5));
            }
         }
      }

      if (streamerMode.getNoFunTime().isEnabled("No Fun Time")) {
         String var8 = var2.toLowerCase();
         if (var8.contains("fun") || var8.contains("time") && !var8.contains("r")) {
            var2 = initAppend(var2, "fun", "Von");
            var2 = initAppend(var2, "time", "Tam");
         }

         if (var8.contains("файм") || var8.contains("тайм") && !var8.contains("р")) {
            var2 = initAppend(var2, "fun", "Вон");
            var2 = initAppend(var2, "time", "Там");
         }
      }

      return var2;
   }

   static {
      map2.put('ꔗ', Formatting.BLUE + "MODER");
      map2.put('ꔥ', Formatting.BLUE + "ST.MODER");
      map2.put('ꔡ', Formatting.LIGHT_PURPLE + "MODER+");
      map2.put('ꔀ', Formatting.GRAY + "PLAYER");
      map2.put('ꔉ', Formatting.YELLOW + "HELPER");
      map2.put('◆', "@");
      map2.put('┃', "|");
      map2.put('ꔳ', Formatting.AQUA + "ML.ADMIN");
      map2.put('ꔅ', Formatting.RED + "Y" + Formatting.WHITE + "T");
      map2.put('ꔂ', Formatting.BLUE + "D.MODER");
      map2.put('ꕠ', Formatting.YELLOW + "D.HELPER");
      map2.put('ꕄ', Formatting.RED + "DRACULA");
      map2.put('ꔖ', Formatting.AQUA + "OVERLORD");
      map2.put('ꕈ', Formatting.GREEN + "COBRA");
      map2.put('ꔨ', Formatting.LIGHT_PURPLE + "DRAGON");
      map2.put('ꔤ', Formatting.RED + "IMPERATOR");
      map2.put('ꔠ', Formatting.GOLD + "MAGISTER");
      map2.put('ꔄ', Formatting.BLUE + "HERO");
      map2.put('ꔒ', Formatting.GREEN + "AVENGER");
      map2.put('ꕒ', Formatting.WHITE + "RABBIT");
      map2.put('ꔈ', Formatting.YELLOW + "TITAN");
      map2.put('ꕀ', Formatting.DARK_GREEN + "HYDRA");
      map2.put('ꔶ', Formatting.GOLD + "TIGER");
      map2.put('ꔲ', Formatting.DARK_PURPLE + "BULL");
      map2.put('ꕖ', Formatting.BLACK + "BUNNY");
      map2.put('\ud83d', "");
      map2.put('\udd25', "@");
      map2.put('ᴀ', "A");
      map2.put('ʙ', "B");
      map2.put('ᴄ', "C");
      map2.put('ᴅ', "D");
      map2.put('ᴇ', "E");
      map2.put('ғ', "F");
      map2.put('ɢ', "G");
      map2.put('ʜ', "H");
      map2.put('ɪ', "I");
      map2.put('ᴊ', "J");
      map2.put('ᴋ', "K");
      map2.put('ʟ', "L");
      map2.put('ᴍ', "M");
      map2.put('ɴ', "N");
      map2.put('ꜱ', "S");
      map2.put('ᴏ', "O");
      map2.put('ᴘ', "P");
      map2.put('ǫ', "Q");
      map2.put('ʀ', "R");
      map2.put('ᴛ', "T");
      map2.put('ᴜ', "U");
      map2.put('ᴠ', "V");
      map2.put('ᴡ', "W");
      map2.put('ꜰ', "F");
      map2.put('ʏ', "Y");
      map2.put('ᴢ', "Z");
   }
}
