package dev.daamky.client.internal;

import java.util.Random;

public class NeuralNet {
   public final int in;
   public final int hid;
   public final int out;
   public final double[][] maekw;
   public final double[] doubles;
   public final double[][] doubles2;
   public final double[] doubles3;
   public final transient double[][] myoeakg;
   public final transient double[] doubles4;
   public final transient double[][] mseud;
   public final transient double[] doubles5;
   public static final Random random = new Random();
   public static final double[] doubles6 = new double[]{1.0, 1.0, 2.8, 2.8, 2.8, 3.2, 3.2, 2.0, 2.0, 1.5, 1.5, 1.0, 1.0, 1.0};

   public NeuralNet(int inx, int hidx, int outx) {
      this.in = inx;
      this.hid = hidx;
      this.out = outx;
      this.maekw = new double[hidx][inx];
      this.doubles = new double[hidx];
      this.doubles2 = new double[outx][hidx];
      this.doubles3 = new double[outx];
      this.myoeakg = new double[hidx][inx];
      this.doubles4 = new double[hidx];
      this.mseud = new double[outx][hidx];
      this.doubles5 = new double[outx];
      double var4 = Math.sqrt(2.0 / (double)inx);
      double var6 = Math.sqrt(2.0 / (double)hidx);

      for (int var8 = 0; var8 < hidx; var8++) {
         for (int var9 = 0; var9 < inx; var9++) {
            this.maekw[var8][var9] = (random.nextDouble() * 2.0 - 1.0) * var4;
         }
      }

      for (int var10 = 0; var10 < outx; var10++) {
         for (int var11 = 0; var11 < hidx; var11++) {
            this.doubles2[var10][var11] = (random.nextDouble() * 2.0 - 1.0) * var6;
         }
      }
   }

   public static double computeExp(double x) {
      return 1.0 / (1.0 + Math.exp(-Math.max(-45.0, Math.min(45.0, x))));
   }

   public double[] computeHid(double[] x) {
      double[] var2 = x;
      if (x.length != this.in) {
         var2 = new double[this.in];
         System.arraycopy(x, 0, var2, 0, Math.min(x.length, this.in));
      }

      double[] var3 = new double[this.hid];

      for (int var4 = 0; var4 < this.hid; var4++) {
         double var5 = this.doubles[var4];

         for (int var7 = 0; var7 < this.in; var7++) {
            var5 += this.maekw[var4][var7] * var2[var7];
         }

         var3[var4] = Math.tanh(var5);
      }

      double[] var9 = new double[this.out];

      for (int var10 = 0; var10 < this.out; var10++) {
         double var6 = this.doubles3[var10];

         for (int var8 = 0; var8 < this.hid; var8++) {
            var6 += this.doubles2[var10][var8] * var3[var8];
         }

         var9[var10] = computeExp(var6);
      }

      return var9;
   }

   public double computeHid(double[] x, double[] y, double lr, double momentum) {
      double[] var7 = x;
      if (x.length != this.in) {
         var7 = new double[this.in];
         System.arraycopy(x, 0, var7, 0, Math.min(x.length, this.in));
      }

      double[] var8 = new double[this.hid];

      for (int var9 = 0; var9 < this.hid; var9++) {
         double var10 = this.doubles[var9];

         for (int var12 = 0; var12 < this.in; var12++) {
            var10 += this.maekw[var9][var12] * var7[var12];
         }

         var8[var9] = Math.tanh(var10);
      }

      double[] var20 = new double[this.out];

      for (int var21 = 0; var21 < this.out; var21++) {
         double var11 = this.doubles3[var21];

         for (int var13 = 0; var13 < this.hid; var13++) {
            var11 += this.doubles2[var21][var13] * var8[var13];
         }

         var20[var21] = computeExp(var11);
      }

      double var22 = 0.0;
      double[] var23 = new double[this.out];

      for (int var24 = 0; var24 < this.out; var24++) {
         double var14 = var24 < y.length ? y[var24] : 0.5;
         double var16 = var24 < doubles6.length ? doubles6[var24] : 1.0;
         double var18 = (var20[var24] - var14) * var16;
         var22 += var18 * var18;
         var23[var24] = var18 * var20[var24] * (1.0 - var20[var24]);
      }

      double[] var25 = new double[this.hid];

      for (int var26 = 0; var26 < this.hid; var26++) {
         double var15 = 0.0;

         for (int var17 = 0; var17 < this.out; var17++) {
            var15 += var23[var17] * this.doubles2[var17][var26];
         }

         var25[var26] = var15 * (1.0 - var8[var26] * var8[var26]);
      }

      for (int var27 = 0; var27 < this.out; var27++) {
         for (int var29 = 0; var29 < this.hid; var29++) {
            double var31 = var23[var27] * var8[var29];
            this.mseud[var27][var29] = momentum * this.mseud[var27][var29] - lr * var31;
            this.doubles2[var27][var29] = this.doubles2[var27][var29] + this.mseud[var27][var29];
         }

         this.doubles5[var27] = momentum * this.doubles5[var27] - lr * var23[var27];
         this.doubles3[var27] = this.doubles3[var27] + this.doubles5[var27];
      }

      for (int var28 = 0; var28 < this.hid; var28++) {
         for (int var30 = 0; var30 < this.in; var30++) {
            double var32 = var25[var28] * var7[var30];
            this.myoeakg[var28][var30] = momentum * this.myoeakg[var28][var30] - lr * var32;
            this.maekw[var28][var30] = this.maekw[var28][var30] + this.myoeakg[var28][var30];
         }

         this.doubles4[var28] = momentum * this.doubles4[var28] - lr * var25[var28];
         this.doubles[var28] = this.doubles[var28] + this.doubles4[var28];
      }

      return var22 / (double)this.out;
   }

   public double computeNext(double[][] X, double[][] Y, double lr, double momentum, int[] perm) {
      int var8 = X.length;
      if (var8 == 0) {
         return 0.0;
      } else {
         for (int var9 = var8 - 1; var9 > 0; var9--) {
            int var10 = random.nextInt(var9 + 1);
            int var11 = perm[var9];
            perm[var9] = perm[var10];
            perm[var10] = var11;
         }

         double var13 = 0.0;

         for (int var14 = 0; var14 < var8; var14++) {
            int var12 = perm[var14];
            var13 += this.computeHid(X[var12], Y[var12], lr, momentum);
         }

         return var13 / (double)var8;
      }
   }

   public double computeDouble(double[][] X, double[][] Y, int epochs, double initialLr) {
      double var6 = 0.0;
      int var8 = X.length;
      if (var8 == 0) {
         return 0.0;
      } else {
         double var9 = 0.88;
         int[] var11 = new int[var8];
         int var12 = 0;

         while (var12 < var8) {
            var11[var12] = var12++;
         }

         for (int var15 = 0; var15 < epochs; var15++) {
            double var13 = initialLr * (1.0 - 0.65 * ((double)var15 / (double)epochs));
            var6 = this.computeNext(X, Y, var13, var9, var11);
         }

         return var6;
      }
   }

   public String initAppend() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.in).append(' ').append(this.hid).append(' ').append(this.out).append('\n');

      for (double[] var5 : this.maekw) {
         for (double var9 : var5) {
            var1.append(var9).append(' ');
         }
      }

      var1.append('\n');

      for (double var20 : this.doubles) {
         var1.append(var20).append(' ');
      }

      var1.append('\n');

      for (double[] var21 : this.doubles2) {
         for (double var26 : var21) {
            var1.append(var26).append(' ');
         }
      }

      var1.append('\n');

      for (double var22 : this.doubles3) {
         var1.append(var22).append(' ');
      }

      var1.append('\n');
      return var1.toString();
   }

   public static NeuralNet parseParse(String data) {
      String[] var1 = data.split("\n");
      String[] var2 = var1[0].trim().split("\\s+");
      int var3 = Integer.parseInt(var2[0]);
      int var4 = Integer.parseInt(var2[1]);
      int var5 = Integer.parseInt(var2[2]);
      NeuralNet var6 = new NeuralNet(var3, var4, var5);
      double[] var7 = parseParse(var1[1]);
      int var8 = 0;

      for (int var9 = 0; var9 < var4; var9++) {
         for (int var10 = 0; var10 < var3; var10++) {
            if (var8 < var7.length) {
               var6.maekw[var9][var10] = var7[var8++];
            }
         }
      }

      double[] var14 = parseParse(var1[2]);

      for (int var15 = 0; var15 < var4; var15++) {
         if (var15 < var14.length) {
            var6.doubles[var15] = var14[var15];
         }
      }

      double[] var16 = parseParse(var1[3]);
      var8 = 0;

      for (int var11 = 0; var11 < var5; var11++) {
         for (int var12 = 0; var12 < var4; var12++) {
            if (var8 < var16.length) {
               var6.doubles2[var11][var12] = var16[var8++];
            }
         }
      }

      double[] var17 = parseParse(var1[4]);

      for (int var18 = 0; var18 < var5; var18++) {
         if (var18 < var17.length) {
            var6.doubles3[var18] = var17[var18];
         }
      }

      return var6;
   }

}
