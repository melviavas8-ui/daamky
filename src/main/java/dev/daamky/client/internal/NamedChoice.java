package dev.daamky.client.internal;

import dev.daamky.client.config.Configurable;
import dev.daamky.client.setting.ModeSetting;
import java.util.Arrays;

public abstract class NamedChoice extends Configurable implements ModeSetting.NamedChoice {
   @Override
   public abstract String getName();

   public static String[] getValues(NamedChoice... choices) {
      return Arrays.stream(choices).map(NamedChoice::getName).toArray(NamedChoice::lambda$getValues$0);
   }

   public static NamedChoice computeEquals(String value, NamedChoice... choices) {
      if (choices != null) {
         if (value == null) {
            return choices.length == 0 ? null : choices[0];
         } else {
            for (NamedChoice var5 : choices) {
               if (var5 != null && value.equals(var5.getName())) {
                  return var5;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   @SafeVarargs
   public static <T extends ModeSetting.NamedChoice> T getChoiceByName(String value, T... choices) {
      if (choices != null && choices.length != 0) {
         T var2 = choices[0];
         return (T)Arrays.stream(choices).filter((p0) -> NamedChoice.lambda$getChoiceByName$1(value, p0)).findFirst().orElse(var2);
      } else {
         return null;
      }
   }

   public static boolean lambda$getChoiceByName$1(String value, ModeSetting.NamedChoice wt) {
      return wt.getName().equals(value);
   }

   public static String[] lambda$getValues$0(int x$0) {
      return new String[x$0];
   }
}
