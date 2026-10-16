package dev.daamky.client.internal;


public final class ValueHelper {
   public static final double doubleVal = 1.70158;
   public static final double double2 = 2.5949095;
   public static final double double3 = 2.70158;
   public static final double double4 = Math.PI * 2.0 / 3.0;
   public static final double double5 = Math.PI * 4.0 / 9.0;
   public static final ApplyFunction2 applyFunction2 = ValueHelper::lambda$static$0;
   public static final ApplyFunction2 applyFunction22 = computePow(2);
   public static final ApplyFunction2 applyFunction23 = computeOut(2);
   public static final ApplyFunction2 applyFunction24 = powIN_OUT(2.0);
   public static final ApplyFunction2 applyFunction25 = computePow(3);
   public static final ApplyFunction2 applyFunction26 = computeOut(3);
   public static final ApplyFunction2 applyFunction27 = powIN_OUT(3.0);
   public static final ApplyFunction2 applyFunction28 = computePow(4);
   public static final ApplyFunction2 applyFunction29 = computeOut(4);
   public static final ApplyFunction2 applyFunction210 = powIN_OUT(4.0);
   public static final ApplyFunction2 applyFunction211 = computePow(5);
   public static final ApplyFunction2 applyFunction212 = computeOut(5);
   public static final ApplyFunction2 applyFunction213 = powIN_OUT(5.0);
   public static final ApplyFunction2 applyFunction214 = ValueHelper::lambda$static$1;
   public static final ApplyFunction2 msuavh = ValueHelper::lambda$static$2;
   public static final ApplyFunction2 applyFunction215 = ValueHelper::lambda$static$3;
   public static final ApplyFunction2 applyFunction216 = ValueHelper::lambda$static$4;
   public static final ApplyFunction2 applyFunction217 = ValueHelper::lambda$static$5;
   public static final ApplyFunction2 applyFunction218 = ValueHelper::lambda$static$6;
   public static final ApplyFunction2 applyFunction219 = ValueHelper::lambda$static$7;
   public static final ApplyFunction2 applyFunction220 = ValueHelper::lambda$static$8;
   public static final ApplyFunction2 miohuy = ValueHelper::lambda$static$9;
   public static final ApplyFunction2 applyFunction221 = ValueHelper::lambda$static$10;
   public static final ApplyFunction2 applyFunction222 = ValueHelper::lambda$static$11;
   public static final ApplyFunction2 applyFunction223 = ValueHelper::lambda$static$12;
   public static final ApplyFunction2 mulga = ValueHelper::lambda$static$13;
   public static final ApplyFunction2 applyFunction224 = ValueHelper::lambda$static$14;
   public static final ApplyFunction2 applyFunction225 = ValueHelper::lambda$static$15;
   public static final ApplyFunction2 muguw = ValueHelper::lambda$static$16;
   public static final ApplyFunction2 applyFunction226 = ValueHelper::lambda$static$17;
   public static final ApplyFunction2 applyFunction227 = ValueHelper::lambda$static$18;
   public static final ApplyFunction2 applyFunction228 = ValueHelper::lambda$static$19;

   public static ApplyFunction2 powIn(double n) {
      return ValueHelper::lambda$powIn$20;
   }

   public static ApplyFunction2 computePow(int n) {
      return powIn((double)n);
   }

   public static ApplyFunction2 powOut(double n) {
      return ValueHelper::lambda$powOut$21;
   }

   public static ApplyFunction2 computeOut(int n) {
      return powOut((double)n);
   }

   public static ApplyFunction2 powIN_OUT(double n) {
      return ValueHelper::lambda$powIN_OUT$22;
   }

      public ValueHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static double lambda$powIN_OUT$22(double n, double value) {
      return value < 0.5 ? Math.pow(2.0, n - 1.0) * Math.pow(value, n) : 1.0 - Math.pow(-2.0 * value + 2.0, n) / 2.0;
   }

   public static double lambda$powOut$21(double n, double value) {
      return 1.0 - Math.pow(1.0 - value, n);
   }

   public static double lambda$powIn$20(double n, double value) {
      return Math.pow(value, n);
   }

   public static double lambda$static$19(double value) {
      return value < 0.5 ? (1.0 - applyFunction226.apply(1.0 - 2.0 * value)) / 2.0 : (1.0 + applyFunction226.apply(2.0 * value - 1.0)) / 2.0;
   }

   public static double lambda$static$18(double value) {
      return 1.0 - applyFunction226.apply(1.0 - value);
   }

   public static double lambda$static$17(double x) {
      double var2 = 7.5625;
      double var4 = 2.75;
      if (x < 1.0 / var4) {
         return var2 * x * x;
      } else if (x < 2.0 / var4) {
         return var2 * (x - 1.5 / var4) * (x - 1.5 / var4) + 0.75;
      } else {
         return x < 2.5 / var4 ? var2 * (x - 2.25 / var4) * (x - 2.25 / var4) + 0.9375 : var2 * (x - 2.625 / var4) * (x - 2.625 / var4) + 0.984375;
      }
   }

   public static double lambda$static$16(double value) {
      return value < 0.5
         ? 2.0 * value * 2.0 * value * (7.189819 * value - 2.5949095) / 2.0
         : ((2.0 * value - 2.0) * (2.0 * value - 2.0) * (3.5949095 * (value * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0;
   }

   public static double lambda$static$15(double value) {
      return 1.0 + 2.70158 * (value - 1.0) * (value - 1.0) * (value - 1.0) + 1.70158 * (value - 1.0) * (value - 1.0);
   }

   public static double lambda$static$14(double value) {
      return 1.0 + 2.70158 * (value - 1.0) * (value - 1.0) * (value - 1.0) + 1.70158 * (value - 1.0) * (value - 1.0);
   }

   public static double lambda$static$13(double value) {
      return 2.70158 * value * value * value - 1.70158 * value * value;
   }

   public static double lambda$static$12(double value) {
      if (value != 0.0 && value != 1.0) {
         return value < 0.5 ? Math.pow(2.0, 20.0 * value - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * value + 10.0)) / 2.0;
      } else {
         return value;
      }
   }

   public static double lambda$static$11(double value) {
      return value != 1.0 ? 1.0 - Math.pow(2.0, -10.0 * value) : value;
   }

   public static double lambda$static$10(double value) {
      return value != 0.0 ? Math.pow(2.0, 10.0 * value - 10.0) : value;
   }

   public static double lambda$static$9(double value) {
      if (value != 0.0 && value != 1.0) {
         return value < 0.5
            ? -(Math.pow(2.0, 20.0 * value - 10.0) * Math.sin((20.0 * value - 11.125) * (Math.PI * 4.0 / 9.0))) / 2.0
            : Math.pow(2.0, -20.0 * value + 10.0) * Math.sin((20.0 * value - 11.125) * (Math.PI * 4.0 / 9.0)) / 2.0 + 1.0;
      } else {
         return value;
      }
   }

   public static double lambda$static$8(double value) {
      return value != 0.0 && value != 1.0 ? Math.pow(2.0, -10.0 * value) * Math.sin((value * 10.0 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1.0 : value;
   }

   public static double lambda$static$7(double value) {
      return value != 0.0 && value != 1.0 ? Math.pow(-2.0, 10.0 * value - 10.0) * Math.sin((value * 10.0 - 10.75) * (Math.PI * 2.0 / 3.0)) : value;
   }

   public static double lambda$static$6(double value) {
      return value < 0.5
         ? (1.0 - Math.sqrt(1.0 - 2.0 * value * 2.0 * value)) / 2.0
         : (Math.sqrt(1.0 - (-2.0 * value + 2.0) * (-2.0 * value + 2.0)) + 1.0) / 2.0;
   }

   public static double lambda$static$5(double value) {
      return Math.sqrt(1.0 - (value - 1.0) * (value - 1.0));
   }

   public static double lambda$static$4(double value) {
      return 1.0 - Math.sqrt(1.0 - value * value);
   }

   public static double lambda$static$3(double value) {
      return -(Math.cos(Math.PI * value) - 1.0) / 2.0;
   }

   public static double lambda$static$2(double value) {
      return Math.sin(value * Math.PI / 2.0);
   }

   public static double lambda$static$1(double value) {
      return 1.0 - Math.cos(value * Math.PI / 2.0);
   }

   public static double lambda$static$0(double value) {
      return value;
   }
}
