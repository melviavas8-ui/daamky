package dev.daamky.client.internal;

import java.util.HashSet;
import java.util.regex.Pattern;

public class NameFilter {
   public static boolean flag = false;
   public static final HashSet<String> hashSet = new HashSet<>();
   public static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

   public static HashSet<String> initInit() {
      return new HashSet<>(hashSet);
   }

   public static void handleNext(HashSet<String> nicknames) {
      hashSet.clear();

      for (String var2 : nicknames) {
         if (pattern.matcher(var2).matches()) {
            hashSet.add(var2.toLowerCase());
         }
      }
   }

   public static boolean computeAdd(String nickname) {
      return !pattern.matcher(nickname).matches() ? false : hashSet.add(nickname.toLowerCase());
   }

   public static boolean computeCase(String nickname) {
      return hashSet.remove(nickname.toLowerCase());
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setEnabled(boolean enabled) {
      flag = enabled;
   }
}
