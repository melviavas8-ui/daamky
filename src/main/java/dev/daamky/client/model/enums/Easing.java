package dev.daamky.client.model.enums;

import org.apache.commons.lang3.StringUtils;

public enum Easing {
   LINEAR(Easing::lambda$static$0),
   SIGMOID(Easing::lambda$static$1),
   QUAD_IN(Easing::lambda$static$2),
   QUAD_OUT(Easing::lambda$static$3),
   QUAD_BOTH(Easing::lambda$static$4),
   CUBIC_IN(Easing::lambda$static$5),
   CUBIC_OUT(Easing::lambda$static$6),
   CUBIC_BOTH(Easing::lambda$static$7),
   QUART_IN(Easing::lambda$static$8),
   QUART_OUT(Easing::lambda$static$9),
   QUART_BOTH(Easing::lambda$static$10),
   QUINT_IN(Easing::lambda$static$11),
   QUINT_OUT(Easing::lambda$static$12),
   QUINT_BOTH(Easing::lambda$static$13),
   SINE_IN(Easing::lambda$static$14),
   SINE_OUT(Easing::lambda$static$15),
   SINE_BOTH(Easing::lambda$static$16),
   EXPO_IN(Easing::lambda$static$17),
   EXPO_OUT(Easing::lambda$static$18),
   EXPO_BOTH(Easing::lambda$static$19),
   CIRC_IN(Easing::lambda$static$20),
   CIRC_OUT(Easing::lambda$static$21),
   CIRC_BOTH(Easing::lambda$static$22),
   BACK_IN(Easing::lambda$static$23),
   BACK_OUT(Easing::lambda$static$24),
   BACK_BOTH(Easing::lambda$static$25),
   ELASTIC_IN(Easing::lambda$static$26),
   ELASTIC_OUT(Easing::lambda$static$27),
   ELASTIC_BOTH(Easing::lambda$static$28),
   SHRINK(Easing::lambda$static$29);

   public final Easing.EasingFunction function;

   private Easing(Easing.EasingFunction function) {
      this.function = function;
   }

   public double apply(double x) {
      return this.function.apply(x);
   }

   public float apply(float x) {
      return (float)this.function.apply((double)x);
   }

   public double ease(double x) {
      return this.function.apply(x);
   }

   public float ease(float x) {
      return (float)this.function.apply((double)x);
   }

   @Override
   public String toString() {
      return StringUtils.capitalize(super.toString().toLowerCase().replace("_", " "));
   }

   public Easing.EasingFunction getFunction() {
      return this.function;
   }

   public static double lambda$static$29(double x) {
      double var2 = 1.3;
      double var4 = var2 + 1.0;
      double var6 = x - 1.0;
      return Math.max(0.0, 1.0 + var4 * var6 * var6 * var6 + var2 * var6 * var6);
   }

   public static double lambda$static$28(double x) {
      return x <= 0.0
         ? 0.0
         : (
            x >= 1.0
               ? 1.0
               : (
                  x < 0.5
                     ? -(Math.pow(2.0, 20.0 * x - 10.0) * Math.sin((20.0 * x - 11.125) * (Math.PI * 2.0 / 9.0))) * 0.5
                     : (Math.pow(2.0, -20.0 * x + 10.0) * Math.sin((20.0 * x - 11.125) * (Math.PI * 2.0 / 9.0)) + 2.0) * 0.5
               )
         );
   }

   public static double lambda$static$27(double x) {
      return x <= 0.0 ? 0.0 : (x >= 1.0 ? 1.0 : Math.pow(2.0, -10.0 * x) * Math.sin((x * 10.0 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1.0);
   }

   public static double lambda$static$26(double x) {
      return x <= 0.0 ? 0.0 : (x >= 1.0 ? 1.0 : -Math.pow(2.0, 10.0 * x - 10.0) * Math.sin((x * 10.0 - 10.75) * (Math.PI * 2.0 / 3.0)));
   }

   public static double lambda$static$25(double x) {
      double var2 = 2.5949095;
      double var4 = var2 + 1.0;
      if (x < 0.5) {
         return 4.0 * x * x * (var4 * 2.0 * x - var2) * 0.5;
      } else {
         double var6 = 2.0 * x - 2.0;
         return (var6 * var6 * (var4 * var6 + var2) + 2.0) * 0.5;
      }
   }

   public static double lambda$static$24(double x) {
      double var2 = x - 1.0;
      return 1.0 + 2.70158 * var2 * var2 * var2 + 1.70158 * var2 * var2;
   }

   public static double lambda$static$23(double x) {
      return 2.70158 * x * x * x - 1.70158 * x * x;
   }

   public static double lambda$static$22(double x) {
      return x < 0.5 ? (1.0 - Math.sqrt(Math.max(0.0, 1.0 - 4.0 * x * x))) * 0.5 : (Math.sqrt(Math.max(0.0, 1.0 - 4.0 * (x - 1.0) * (x - 1.0))) + 1.0) * 0.5;
   }

   public static double lambda$static$21(double x) {
      double var2 = x - 1.0;
      return Math.sqrt(Math.max(0.0, 1.0 - var2 * var2));
   }

   public static double lambda$static$20(double x) {
      return 1.0 - Math.sqrt(Math.max(0.0, 1.0 - x * x));
   }

   public static double lambda$static$19(double x) {
      return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? Math.pow(2.0, 20.0 * x - 10.0) * 0.5 : (2.0 - Math.pow(2.0, -20.0 * x + 10.0)) * 0.5));
   }

   public static double lambda$static$18(double x) {
      return x == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * x);
   }

   public static double lambda$static$17(double x) {
      return x == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0);
   }

   public static double lambda$static$16(double x) {
      return (1.0 - Math.cos(Math.PI * x)) * 0.5;
   }

   public static double lambda$static$15(double x) {
      return Math.sin(x * (Math.PI / 2));
   }

   public static double lambda$static$14(double x) {
      return 1.0 - Math.cos(x * (Math.PI / 2));
   }

   public static double lambda$static$13(double x) {
      if (x < 0.5) {
         double var6 = x * x;
         return 16.0 * var6 * var6 * x;
      } else {
         double var2 = x - 1.0;
         double var4 = var2 * var2;
         return 1.0 + 16.0 * var4 * var4 * var2;
      }
   }

   public static double lambda$static$12(double x) {
      double var2 = x - 1.0;
      double var4 = var2 * var2;
      return 1.0 + var4 * var4 * var2;
   }

   public static double lambda$static$11(double x) {
      double var2 = x * x;
      return var2 * var2 * x;
   }

   public static double lambda$static$10(double x) {
      if (x < 0.5) {
         double var6 = x * x;
         return 8.0 * var6 * var6;
      } else {
         double var2 = x - 1.0;
         double var4 = var2 * var2;
         return 1.0 - 8.0 * var4 * var4;
      }
   }

   public static double lambda$static$9(double x) {
      double var2 = x - 1.0;
      double var4 = var2 * var2;
      return 1.0 - var4 * var4;
   }

   public static double lambda$static$8(double x) {
      double var2 = x * x;
      return var2 * var2;
   }

   public static double lambda$static$7(double x) {
      return x < 0.5 ? 4.0 * x * x * x : (x - 1.0) * (2.0 * x - 2.0) * (2.0 * x - 2.0) + 1.0;
   }

   public static double lambda$static$6(double x) {
      double var2 = x - 1.0;
      return var2 * var2 * var2 + 1.0;
   }

   public static double lambda$static$5(double x) {
      return x * x * x;
   }

   public static double lambda$static$4(double x) {
      return x < 0.5 ? 2.0 * x * x : -1.0 + (4.0 - 2.0 * x) * x;
   }

   public static double lambda$static$3(double x) {
      return x * (2.0 - x);
   }

   public static double lambda$static$2(double x) {
      return x * x;
   }

   public static double lambda$static$1(double x) {
      return 1.0 / (1.0 + Math.exp(-x));
   }

   public static double lambda$static$0(double x) {
      return x;
   }

   public static Easing[] $values() {
      return new Easing[]{
         LINEAR,
         SIGMOID,
         QUAD_IN,
         QUAD_OUT,
         QUAD_BOTH,
         CUBIC_IN,
         CUBIC_OUT,
         CUBIC_BOTH,
         QUART_IN,
         QUART_OUT,
         QUART_BOTH,
         QUINT_IN,
         QUINT_OUT,
         QUINT_BOTH,
         SINE_IN,
         SINE_OUT,
         SINE_BOTH,
         EXPO_IN,
         EXPO_OUT,
         EXPO_BOTH,
         CIRC_IN,
         CIRC_OUT,
         CIRC_BOTH,
         BACK_IN,
         BACK_OUT,
         BACK_BOTH,
         ELASTIC_IN,
         ELASTIC_OUT,
         ELASTIC_BOTH,
         SHRINK
      };
   }

   @FunctionalInterface
   public interface EasingFunction {
      double apply(double var1);
   }
}
