package dev.daamky.client.model.enums;

import dev.daamky.client.internal.ApplyFunction2;
import dev.daamky.client.internal.ValueHelper;

public enum EasingPreset {
   LINEAR(ValueHelper.applyFunction2),
   QUAD_OUT(ValueHelper.applyFunction23),
   CUBIC_OUT(ValueHelper.applyFunction26),
   QUART_OUT(ValueHelper.applyFunction29),
   QUINT_OUT(ValueHelper.applyFunction212),
   SINE_OUT(ValueHelper.msuavh),
   CIRC_OUT(ValueHelper.applyFunction217),
   ELASTIC_OUT(ValueHelper.applyFunction220),
   EXPO_OUT(ValueHelper.applyFunction222),
   BACK_OUT(ValueHelper.applyFunction225),
   BOUNCE_OUT(ValueHelper.applyFunction226);

   public final ApplyFunction2 easing;

   @Override
   public String toString() {
      String var1 = this.name().toLowerCase();
      String[] var2 = var1.split("_");
      StringBuilder var3 = new StringBuilder();

      for (String var7 : var2) {
         var3.append(Character.toUpperCase(var7.charAt(0))).append(var7.substring(1)).append(" ");
      }

      return var3.toString().trim();
   }

      public ApplyFunction2 getEasing() {
      return this.easing;
   }

      private EasingPreset(final ApplyFunction2 easing) {
      this.easing = easing;
   }

   public static EasingPreset[] $values() {
      return new EasingPreset[]{LINEAR, QUAD_OUT, CUBIC_OUT, QUART_OUT, QUINT_OUT, SINE_OUT, CIRC_OUT, ELASTIC_OUT, EXPO_OUT, BACK_OUT, BOUNCE_OUT};
   }
}
