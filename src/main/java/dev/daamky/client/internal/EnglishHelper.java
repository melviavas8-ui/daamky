package dev.daamky.client.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class EnglishHelper {
   public static final Path path = Path.of("daamky/language.txt");
   public static final Map<String, String> map = ColorHelper.initPut();
   public static EnglishHelper.Language language = readCase();

   public static void runSize() {
      System.out.println("[Lang] переводов в словаре: " + map.size() + ", язык: " + getString());
   }

   public static EnglishHelper.Language getLanguage() {
      return language;
   }

   public static boolean check() {
      return language == EnglishHelper.Language.ENGLISH;
   }

   public static String getString() {
      return language == EnglishHelper.Language.ENGLISH ? "EN" : "RU";
   }

   public static void setLanguage(EnglishHelper.Language language) {
      if (languagex != null && languagex != language) {
         language = languagex;
         createCreate();
      }
   }

   public static void runLanguage() {
      setLanguage(language == EnglishHelper.Language.ENGLISH ? EnglishHelper.Language.RUSSIAN : EnglishHelper.Language.ENGLISH);
   }

   public static void run() {
   }

   public static String computeString(String ru) {
      if (ru != null && language == EnglishHelper.Language.ENGLISH) {
         String var1 = map.get(ru);
         return var1 != null ? var1 : ru;
      } else {
         return ru;
      }
   }

   public static String computeString(String ru, String en) {
      return language == EnglishHelper.Language.ENGLISH ? en : ru;
   }

   public static EnglishHelper.Language readCase() {
      try {
         if (Files.exists(path)) {
            String var0 = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (var0.equalsIgnoreCase("EN") || var0.equalsIgnoreCase("ENGLISH")) {
               return EnglishHelper.Language.ENGLISH;
            }
         }
      } catch (Exception var1) {
      }

      return EnglishHelper.Language.RUSSIAN;
   }

   public static void createCreate() {
      try {
         Files.createDirectories(path.getParent());
         Files.writeString(path, language == EnglishHelper.Language.ENGLISH ? "EN" : "RU", StandardCharsets.UTF_8);
      } catch (Exception var1) {
      }
   }

   public static enum Language {
      RUSSIAN,
      ENGLISH;

      public static EnglishHelper.Language[] $values() {
         return new EnglishHelper.Language[]{RUSSIAN, ENGLISH};
      }
   }
}
