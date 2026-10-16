package dev.daamky.client.internal;

import java.util.Random;

public class NextIntHelper {
   public static final Random random = new Random();
   public static int intVal = 3;
   public static int int2 = 7;
   public static int int3 = 5;
   public static int int4 = 12;
   public static int int5 = 6;
   public static int int6 = 14;
   public static boolean mzufka = true;
   public static double mpoqde = 0.03;
   public static double doubleVal = 0.15;
   public static int int7 = 10;
   public static boolean flag = true;
   public static double moozq = 0.1;

   public static int getInt() {
      return computeNext(intVal, int2);
   }

   public static int getInt2() {
      return computeNext(int3, int4);
   }

   public static int getInt3() {
      return computeNext(int5, int6);
   }

   public static boolean checkNext() {
      return mzufka && random.nextDouble() < 0.4;
   }

   public static boolean checkNext2() {
      return random.nextDouble() < mpoqde;
   }

   public static boolean checkNext3() {
      return random.nextDouble() < doubleVal;
   }

   public static int getInt7() {
      return int7;
   }

   public static boolean checkNext4() {
      return flag && random.nextDouble() < moozq;
   }

   public static int computeNext(int min, int max) {
      return min >= max ? min : random.nextInt(max - min + 1) + min;
   }

   public static int computeNext(int defaultSlot) {
      if (!mzufka) {
         return defaultSlot;
      } else if (random.nextDouble() < 0.6) {
         return defaultSlot;
      } else {
         int[] var1 = new int[]{48, 46, 45, 44, 43, 42, 41, 40, 39, 38};
         return var1[random.nextInt(var1.length)];
      }
   }

   public static void handleInt(int min, int max) {
      intVal = min;
      int2 = max;
   }

   public static void handleInt2(int min, int max) {
      int3 = min;
      int4 = max;
   }

   public static void handleInt3(int min, int max) {
      int5 = min;
      int6 = max;
   }

   public static void setV(boolean v) {
      mzufka = v;
   }

   public static void setV(double v) {
      mpoqde = v;
   }

   public static void handleDouble(double v) {
      doubleVal = v;
   }

   public static void setV(int v) {
      int7 = v;
   }

   public static void handleFlag(boolean v) {
      flag = v;
   }

   public static void handleDouble2(double v) {
      moozq = v;
   }

   public static int getValue2() {
      return intVal;
   }

   public static int getValue3() {
      return int2;
   }

   public static int getValue4() {
      return int3;
   }

   public static int getInt4() {
      return int4;
   }

   public static int getInt5() {
      return int5;
   }

   public static int getInt6() {
      return int6;
   }

   public static boolean getMzufka() {
      return mzufka;
   }

   public static double getMpoqde() {
      return mpoqde;
   }

   public static double getDouble() {
      return doubleVal;
   }

   public static boolean getFlag() {
      return flag;
   }

   public static double getMoozq() {
      return moozq;
   }
}
