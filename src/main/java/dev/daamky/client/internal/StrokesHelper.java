package dev.daamky.client.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrokesHelper {
   public static final Path path = Path.of("daamky/neuro");
   public static final Path path2 = path.resolve("current.txt");
   public static final int mjiwudl = 30;
   public static final int intVal = 48;
   public static final int mtuuk = 14;
   public static final int mwacj = 60000;
   public static final float floatVal = 180.0F;
   public static final float float2 = 120.0F;
   public static final float float3 = 90.0F;
   public static final float float4 = 1.2F;
   public static final float mviotfo = 0.8F;
   public static final float float5 = 12.0F;
   public static final float float6 = 8.0F;
   public static final float float7 = 35.0F;
   public static final float float8 = 22.0F;
   public static final float float9 = 45.0F;
   public static final float float10 = 30.0F;
   public final String name;
   public NeuralNet neuralNet;
   public int int2;
   public double mvijuk = -1.0;
   public final List<double[]> list = new ArrayList<>();
   public final List<double[]> list2 = new ArrayList<>();
   public static final int int3 = 3;
   public static final int int4 = 50000;
   public final List<float[]> list3 = new ArrayList<>();
   public int int5 = -1;
   public static final int int6 = 5;
   public static final int mbawq = 60000;
   public final List<float[]> list4 = new ArrayList<>();
   public int int7 = -1;

   public StrokesHelper(String name) {
      this.name = namex;
      this.neuralNet = new NeuralNet(30, 48, 14);
   }

   public float[] computeSpeed(float yawErr, float pitchErr) {
      StrokesHelper.Prediction var3 = this.computePrediction(yawErr, pitchErr);
      return new float[]{var3.yawSpeed, var3.pitchSpeed};
   }

   public StrokesHelper.Prediction computePrediction(float yawErr, float pitchErr) {
      double[] var3 = computeDoubles(yawErr, pitchErr);
      return this.initYaw(var3);
   }

   public StrokesHelper.Prediction initYaw(double[] x) {
      StrokesHelper.Prediction var2 = this.initMax(x);
      if (!this.list.isEmpty() && !this.list2.isEmpty()) {
         int var3 = this.list.size();
         int var4 = Math.min(var3, 128);
         int var5 = Math.max(1, var3 / var4);
         int var6 = -1;
         double var7 = Double.MAX_VALUE;

         for (int var9 = 0; var9 < var3; var9 += var5) {
            double[] var10 = this.list.get(var9);
            double var11 = x[0] - var10[0];
            double var13 = x[1] - var10[1];
            double var15 = x.length > 16 && var10.length > 16 ? x[16] - var10[16] : 0.0;
            double var17 = var11 * var11 * 2.0 + var13 * var13 * 2.0 + var15 * var15;
            if (var17 < var7) {
               var7 = var17;
               var6 = var9;
               if (var17 < 0.001) {
                  break;
               }
            }
         }

         if (var6 >= 0 && var6 < this.list2.size()) {
            double[] var23 = this.list2.get(var6);
            float var24 = (float)(var23[0] * 180.0);
            float var25 = (float)(var23[1] * 120.0);
            float var12 = computeFloat2((float)var23[2]);
            float var26 = computeFloat2((float)var23[3]);
            float var14 = computeFloat2((float)var23[4]);
            float var27 = computeFloat((float)var23[5]) * 1.2F;
            float var16 = computeFloat((float)var23[6]) * 0.8F;
            float var28 = computeFloat((float)var23[7]) * 12.0F;
            float var18 = computeFloat((float)var23[8]) * 8.0F;
            float var19 = computeFloat((float)var23[9]) * 35.0F;
            float var20 = computeFloat((float)var23[10]) * 22.0F;
            float var21 = 0.7F;
            float var22 = 0.3F;
            return new StrokesHelper.Prediction(
               var2.yawSpeed * var21 + var24 * var22,
               var2.pitchSpeed * var21 + var25 * var22,
               var2.hitX * var21 + var12 * var22,
               var2.hitY * var21 + var26 * var22,
               var2.hitZ * var21 + var14 * var22,
               var2.yawShake * var21 + var27 * var22,
               var2.pitchShake * var21 + var16 * var22,
               var2.yawLead * var21 + var28 * var22,
               var2.pitchLead * var21 + var18 * var22,
               var2.yawAccel * var21 + var19 * var22,
               var2.pitchAccel * var21 + var20 * var22,
               var2.smoothness,
               var2.curveArc,
               var2.overshootRatio,
               0.0F,
               false
            );
         } else {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public StrokesHelper.Prediction initMax(double[] x) {
      double[] var2 = this.neuralNet.computeHid(x);
      float var3 = (float)(var2[0] * 180.0);
      float var4 = (float)(var2[1] * 120.0);
      return var2.length < 14
         ? new StrokesHelper.Prediction(
            Math.max(var3, 12.0F), Math.max(var4, 10.0F), 0.5F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false
         )
         : new StrokesHelper.Prediction(
            Math.max(var3, 12.0F),
            Math.max(var4, 10.0F),
            computeFloat2((float)var2[2]),
            computeFloat2((float)var2[3]),
            computeFloat2((float)var2[4]),
            computeFloat((float)var2[5]) * 1.2F,
            computeFloat((float)var2[6]) * 0.8F,
            computeFloat((float)var2[7]) * 12.0F,
            computeFloat((float)var2[8]) * 8.0F,
            computeFloat((float)var2[9]) * 35.0F,
            computeFloat((float)var2[10]) * 22.0F,
            computeFloat2((float)var2[11]),
            computeFloat((float)var2[12]) * 3.5F,
            computeFloat2((float)var2[13]),
            0.0F,
            false
         );
   }

   public void handleAdd(double[][] x, double[][] y) {
      for (int var3 = 0; var3 < x.length && var3 < y.length; var3++) {
         if (this.list.size() >= 60000) {
            this.list.remove(0);
            this.list2.remove(0);
         }

         this.list.add((double[])x[var3].clone());
         this.list2.add((double[])y[var3].clone());
      }
   }

   public static double[] computeDoubles(float yawErr, float pitchErr) {
      return computeMin(
         yawErr,
         pitchErr,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.5F,
         0.5F,
         0.5F,
         3.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         1.0F,
         0.0F,
         1.0F,
         0.0F
      );
   }

   public static double[] computeMin(
      float yawErr,
      float pitchErr,
      float prevAppliedYaw,
      float prevAppliedPitch,
      float prevPrevAppliedYaw,
      float prevPrevAppliedPitch,
      float prevYawAccel,
      float prevPitchAccel,
      float hitX,
      float hitY,
      float hitZ,
      float distance,
      float elevationDelta,
      float currentPitch,
      float relativePitchToTarget,
      float targetRelYawSpeed,
      float targetRelPitchSpeed,
      float targetVelX,
      float targetVelY,
      float targetVelZ,
      float playerVelY,
      float attackCdProgress,
      float sinceAttack,
      float prevSmoothness,
      float prevPeak
   ) {
      float var25 = (float)Math.hypot((double)yawErr, (double)pitchErr);
      return new double[]{
         computeDouble(yawErr, 180.0F),
         computeDouble(pitchErr, 90.0F),
         Math.min((double)Math.abs(yawErr) / 180.0, 1.0),
         Math.min((double)Math.abs(pitchErr) / 90.0, 1.0),
         Math.min((double)var25 / 180.0, 1.0),
         computeDouble(prevAppliedYaw, 180.0F),
         computeDouble(prevAppliedPitch, 120.0F),
         computeDouble(prevPrevAppliedYaw, 180.0F),
         computeDouble(prevPrevAppliedPitch, 120.0F),
         computeDouble(prevYawAccel, 35.0F),
         computeDouble(prevPitchAccel, 22.0F),
         computeDouble(prevYawAccel - prevPitchAccel, 45.0F),
         computeDouble(prevPitchAccel - prevYawAccel, 30.0F),
         (double)computeFloat2(hitX),
         (double)computeFloat2(hitY),
         (double)computeFloat2(hitZ),
         Math.min((double)Math.max(distance, 0.0F) / 8.0, 1.0),
         computeDouble(elevationDelta, 4.0F),
         computeDouble(currentPitch, 90.0F),
         computeDouble(relativePitchToTarget, 90.0F),
         computeDouble(targetRelYawSpeed, 35.0F),
         computeDouble(targetRelPitchSpeed, 20.0F),
         computeDouble(targetVelX, 1.5F),
         computeDouble(targetVelY, 1.5F),
         computeDouble(targetVelZ, 1.5F),
         computeDouble(playerVelY, 1.5F),
         (double)computeFloat2(attackCdProgress),
         Math.min((double)Math.max(sinceAttack, 0.0F) / 30.0, 1.0),
         (double)computeFloat2(prevSmoothness),
         (double)computeFloat2(prevPeak)
      };
   }

   public void handleNext(List<float[]> recorded) {
      for (float[] var3 : recorded) {
         if (var3.length == 3) {
            if (this.list3.size() >= 50000) {
               this.list3.remove(0);
            }

            this.list3.add((float[])var3.clone());
         }
      }
   }

   public int size() {
      return this.list3.size();
   }

   public float[] setSpeed(float speed) {
      if (this.list3.isEmpty()) {
         return new float[]{0.0F, 0.0F};
      } else {
         if (this.intVal5 >= 0 && this.intVal5 < this.list3.size()) {
            float[] var2 = this.list3.get(this.intVal5);
            if (Math.abs(var2[2] - speed) <= computeMax(speed)) {
               this.intVal5++;
               return new float[]{var2[0], var2[1]};
            }
         }

         int var8 = 0;
         float var3 = Float.MAX_VALUE;
         int var4 = ThreadLocalRandom.current().nextInt(this.list3.size());

         for (int var5 = 0; var5 < this.list3.size(); var5++) {
            int var6 = (var4 + var5) % this.list3.size();
            float var7 = Math.abs(this.list3.get(var6)[2] - speed);
            if (var7 < var3) {
               var3 = var7;
               var8 = var6;
            }

            if (var7 <= computeMax(speed) * 0.25F) {
               var8 = var6;
               break;
            }
         }

         this.intVal5 = var8 + 1;
         float[] var9 = this.list3.get(var8);
         return new float[]{var9[0], var9[1]};
      }
   }

   public static float computeMax(float speed) {
      return Math.max(3.0F, speed * 0.6F);
   }

   public void handleNext2(List<float[]> recorded) {
      for (float[] var3 : recorded) {
         if (var3.length == 5) {
            if (this.list4.size() >= 60000) {
               this.list4.remove(0);
            }

            this.list4.add((float[])var3.clone());
         }
      }
   }

   public int size2() {
      return this.list4.size();
   }

   public float[] computeSize(float yawErr, float pitchErr, float sinceAttack) {
      if (this.list4.isEmpty()) {
         return null;
      } else {
         if (this.intVal7 >= 0 && this.intVal7 < this.list4.size()) {
            float[] var4 = this.list4.get(this.intVal7);
            if (var4[4] >= 0.0F && computeMin(var4, yawErr, pitchErr, sinceAttack) <= computeFloat(yawErr, pitchErr)) {
               this.intVal7++;
               return computeFloats(var4, yawErr, pitchErr);
            }
         }

         int var11 = -1;
         float var5 = Float.MAX_VALUE;
         int var6 = ThreadLocalRandom.current().nextInt(this.list4.size());

         for (int var7 = 0; var7 < this.list4.size(); var7++) {
            int var8 = (var6 + var7) % this.list4.size();
            float[] var9 = this.list4.get(var8);
            if (!(var9[4] < 0.0F)) {
               float var10 = computeMin(var9, yawErr, pitchErr, sinceAttack);
               if (var10 < var5) {
                  var5 = var10;
                  var11 = var8;
               }

               if (var10 <= computeFloat(yawErr, pitchErr) * 0.2F) {
                  var11 = var8;
                  break;
               }
            }
         }

         if (var11 >= 0 && !(var5 > computeFloat(yawErr, pitchErr) * 4.0F)) {
            this.intVal7 = var11 + 1;
            return computeFloats(this.list4.get(var11), yawErr, pitchErr);
         } else {
            this.intVal7 = -1;
            return null;
         }
      }
   }

   public static float[] computeFloats(float[] s, float yawErr, float pitchErr) {
      float var3 = (float)Math.hypot((double)s[0], (double)s[1]);
      float var4 = (float)Math.hypot((double)yawErr, (double)pitchErr);
      float var5 = var3 < 0.5F ? 1.0F : computeMin(var4 / var3, 0.7F, 1.35F);
      return new float[]{s[2] * var5, s[3] * var5};
   }

   public static float computeMin(float[] s, float yawErr, float pitchErr, float sinceAttack) {
      float var4 = s[0] - yawErr;
      float var5 = s[1] - pitchErr;
      float var6 = (Math.min(s[4], 40.0F) - Math.min(sinceAttack, 40.0F)) * 1.2F;
      return var4 * var4 + var5 * var5 + var6 * var6;
   }

   public static float computeFloat(float yawErr, float pitchErr) {
      float var2 = (float)Math.hypot((double)yawErr, (double)pitchErr);
      float var3 = 6.0F + var2 * 0.35F;
      return var3 * var3;
   }

   public void initClear() {
      if (this.neuralNet.in != 30 || this.neuralNet.out != 14) {
         this.neuralNet = new NeuralNet(30, 48, 14);
         this.intVal2 = 0;
         this.mvijuk = -1.0;
         this.list.clear();
         this.list2.clear();
      }
   }

   public static double computeDouble(float value, float maxAbs) {
      return maxAbs <= 0.0F ? 0.5 : (double)((computeMin(value / maxAbs, -1.0F, 1.0F) + 1.0F) * 0.5F);
   }

   public static float computeFloat(float value) {
      return computeFloat2(value) * 2.0F - 1.0F;
   }

   public static float computeFloat2(float value) {
      return computeMin(value, 0.0F, 1.0F);
   }

   public static float computeMin(float value, float min, float max) {
      return value < min ? min : Math.min(value, max);
   }

   public void handleAdd(double[] x, double[] y) {
      if (this.list.size() >= 60000) {
         this.list.remove(0);
         this.list2.remove(0);
      }

      this.list.add((double[])x.clone());
      this.list2.add((double[])y.clone());
      this.intVal2++;
   }

   public String initAppend() {
      StringBuilder var1 = new StringBuilder();
      var1.append("STROKES ").append(this.list3.size()).append('\n');

      for (float[] var3 : this.list3) {
         var1.append(var3[0]).append(' ').append(var3[1]).append(' ').append(var3[2]).append('\n');
      }

      return var1.toString();
   }

   public String initAppend2() {
      StringBuilder var1 = new StringBuilder();
      var1.append("TRAJ ").append(this.list4.size()).append('\n');

      for (float[] var3 : this.list4) {
         var1.append(var3[0]).append(' ').append(var3[1]).append(' ').append(var3[2]).append(' ').append(var3[3]).append(' ').append(var3[4]).append('\n');
      }

      return var1.toString();
   }

   public String initAppend3() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MEMORY ").append(this.list.size()).append('\n');

      for (int var2 = 0; var2 < this.list.size(); var2++) {
         for (double var6 : this.list.get(var2)) {
            var1.append(var6).append(' ');
         }

         var1.append("| ");

         for (double var11 : this.list2.get(var2)) {
            var1.append(var11).append(' ');
         }

         var1.append('\n');
      }

      return var1.toString();
   }

   public Path resolve() {
      return path.resolve(this.name + ".neuro");
   }

   public static void createBlank(String name) {
      try {
         Files.createDirectories(path);
         if (namex != null && !namex.isBlank()) {
            Files.writeString(path2, namex, StandardCharsets.UTF_8);
         } else {
            Files.deleteIfExists(path2);
         }
      } catch (Exception var2) {
      }
   }

   public static String readRead() {
      try {
         if (!Files.exists(path2)) {
            return null;
         } else {
            String var0 = Files.readString(path2, StandardCharsets.UTF_8).trim();
            return var0.isEmpty() ? null : var0;
         }
      } catch (Exception var1) {
         return null;
      }
   }

   public List<double[]> getList() {
      return this.list;
   }

   public List<double[]> getList2() {
      return this.list2;
   }

   public void createAppend() {
      try {
         Files.createDirectories(path);
         Path var1 = path.resolve(this.name + ".onnx");
         StringBuilder var2 = new StringBuilder();
         var2.append("// GONE Neuro ONNX Model Descriptor: ").append(this.name).append("\n");
         var2.append("version=1.0\n");
         var2.append("inputs=").append(30).append("\n");
         var2.append("hidden=").append(48).append("\n");
         var2.append("outputs=").append(14).append("\n");
         var2.append("samples=").append(this.intVal2).append("\n");
         var2.append("loss=").append(this.mvijuk).append("\n");
         var2.append("weights=\n").append(this.neuralNet.initAppend());
         Files.writeString(var1, var2.toString(), StandardCharsets.UTF_8);
      } catch (Exception var3) {
      }
   }

   public void createCreate() {
      try {
         Files.createDirectories(path);
         String var1 = this.intVal2 + " " + this.mvijuk + "\n" + this.neuralNet.initAppend() + this.initAppend3() + this.initAppend() + this.initAppend2();
         Files.writeString(this.resolve(), var1, StandardCharsets.UTF_8);
         this.createAppend();
      } catch (Exception var2) {
      }
   }

   public static StrokesHelper initInit(String name) {
      try {
         Path var1 = path.resolve(namex + ".neuro");
         if (!Files.exists(var1)) {
            String var2 = System.getProperty("user.home") + "\\OneDrive\\Робочий стіл\\" + namex + ".neuro";
            var1 = Path.of(var2);
            if (!Files.exists(var1)) {
               var2 = System.getProperty("user.home") + "\\Desktop\\" + namex + ".neuro";
               var1 = Path.of(var2);
               if (!Files.exists(var1)) {
                  return null;
               }
            }
         }

         String var9 = Files.readString(var1, StandardCharsets.UTF_8).trim();
         if (var9.startsWith("{")) {
            StrokesHelper var10 = new StrokesHelper(namex);
            var10.setJson(var9);
            return var10;
         } else {
            int var3 = var9.indexOf(10);
            if (var3 < 0) {
               return null;
            } else {
               String var4 = var9.substring(0, var3).trim();
               String[] var5 = var4.split("\\s+");
               StrokesHelper var6 = new StrokesHelper(namex);
               var6.int2 = Integer.parseInt(var5[0]);
               var6.mvijuk = Double.parseDouble(var5[1]);
               var6.neuralNet = NeuralNet.parseParse(var9.substring(var3 + 1));
               var6.handleAdd(var9.substring(var3 + 1));
               var6.parseParse(var9.substring(var3 + 1));
               var6.parseParse2(var9.substring(var3 + 1));
               return var6;
            }
         }
      } catch (Exception var7) {
         return null;
      }
   }

   public void setJson(String json) {
      try {
         Pattern var2 = Pattern.compile(
            "\\{\\s*\"distance\":\\s*([0-9.\\-E]+),\\s*\"yawError\":\\s*([0-9.\\-E]+),\\s*\"pitchError\":\\s*([0-9.\\-E]+),\\s*\"targetSpeed\":\\s*([0-9.\\-E]+),\\s*\"yawGain\":\\s*([0-9.\\-E]+),\\s*\"pitchGain\":\\s*([0-9.\\-E]+),\\s*\"yawStepAbs\":\\s*([0-9.\\-E]+),\\s*\"pitchStepAbs\":\\s*([0-9.\\-E]+),\\s*\"rayAligned\":\\s*(true|false)",
            8
         );
         Matcher var3 = var2.matcher(json);

         while (var3.find()) {
            double var4 = Double.parseDouble(var3.group(1));
            double var6 = Double.parseDouble(var3.group(2));
            double var8 = Double.parseDouble(var3.group(3));
            double var10 = Double.parseDouble(var3.group(4));
            double var12 = Double.parseDouble(var3.group(5));
            double var14 = Double.parseDouble(var3.group(6));
            double var16 = Double.parseDouble(var3.group(7));
            double var18 = Double.parseDouble(var3.group(8));
            boolean var20 = Boolean.parseBoolean(var3.group(9));
            double[] var21 = computeDoubles((float)var6, (float)var8);
            var21[16] = Math.min(Math.max(var4, 0.0) / 8.0, 1.0);
            var21[20] = computeDouble((float)var10, 35.0F);
            double[] var22 = new double[14];
            var22[0] = Math.min(var16 / 180.0, 1.0);
            var22[1] = Math.min(var18 / 120.0, 1.0);
            var22[2] = 0.5;
            var22[3] = 0.5;
            var22[4] = 0.5;
            var22[11] = var20 ? 1.0 : 0.6;
            this.list.add(var21);
            this.list2.add(var22);
            this.intVal2++;
         }
      } catch (Exception var23) {
      }
   }

   public void handleAdd(String data) {
      String[] var2 = data.split("\n");
      int var3 = -1;

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var2[var4].startsWith("MEMORY ")) {
            var3 = var4 + 1;
            break;
         }
      }

      if (var3 >= 0) {
         this.list.clear();
         this.list2.clear();

         for (int var9 = var3; var9 < var2.length && this.list.size() < 60000; var9++) {
            String var5 = var2[var9].trim();
            if (!var5.isEmpty()) {
               if (var5.startsWith("STROKES ")) {
                  break;
               }

               int var6 = var5.indexOf(124);
               if (var6 >= 0) {
                  double[] var7 = parseParse(var5.substring(0, var6), 30);
                  double[] var8 = parseParse(var5.substring(var6 + 1), 14);
                  if (var7 != null && var8 != null && (!(var8[0] < 0.002) || !(var8[1] < 0.002) || var7.length <= 25 || !(var7[25] > 0.3))) {
                     this.list.add(var7);
                     this.list2.add(var8);
                  }
               }
            }
         }
      }
   }

   public void parseParse(String data) {
      String[] var2 = data.split("\n");
      int var3 = -1;

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var2[var4].startsWith("STROKES ")) {
            var3 = var4 + 1;
            break;
         }
      }

      if (var3 >= 0) {
         this.list3.clear();

         for (int var9 = var3; var9 < var2.length && this.list3.size() < 50000; var9++) {
            String var5 = var2[var9].trim();
            if (!var5.isEmpty()) {
               if (var5.startsWith("TRAJ ")) {
                  break;
               }

               String[] var6 = var5.split("\\s+");
               if (var6.length >= 3) {
                  try {
                     float var7 = Float.parseFloat(var6[2]);
                     if (!(var7 < 0.05F)) {
                        this.list3.add(new float[]{Float.parseFloat(var6[0]), Float.parseFloat(var6[1]), var7});
                     }
                  } catch (Exception var8) {
                  }
               }
            }
         }
      }
   }

   public void parseParse2(String data) {
      String[] var2 = data.split("\n");
      int var3 = -1;

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var2[var4].startsWith("TRAJ ")) {
            var3 = var4 + 1;
            break;
         }
      }

      if (var3 >= 0) {
         this.list4.clear();

         for (int var10 = var3; var10 < var2.length && this.list4.size() < 60000; var10++) {
            String var5 = var2[var10].trim();
            if (!var5.isEmpty()) {
               String[] var6 = var5.split("\\s+");
               if (var6.length >= 5) {
                  try {
                     float var7 = Float.parseFloat(var6[2]);
                     float var8 = Float.parseFloat(var6[3]);
                     if (!(Math.hypot((double)var7, (double)var8) < 0.05F)) {
                        this.list4.add(new float[]{Float.parseFloat(var6[0]), Float.parseFloat(var6[1]), var7, var8, Float.parseFloat(var6[4])});
                     }
                  } catch (Exception var9) {
                  }
               }
            }
         }
      }
   }

   public static class Prediction {
      public final float yawSpeed;
      public final float pitchSpeed;
      public final float hitX;
      public final float hitY;
      public final float hitZ;
      public final float yawShake;
      public final float pitchShake;
      public final float yawLead;
      public final float pitchLead;
      public final float yawAccel;
      public final float pitchAccel;
      public final float smoothness;
      public final float curveArc;
      public final float overshootRatio;
      public final float peak;
      public final boolean remembered;

      public Prediction(
         float yawSpeed,
         float pitchSpeed,
         float hitX,
         float hitY,
         float hitZ,
         float yawShake,
         float pitchShake,
         float yawLead,
         float pitchLead,
         float yawAccel,
         float pitchAccel,
         float smoothness,
         float curveArc,
         float overshootRatio,
         float peak,
         boolean remembered
      ) {
         this.yawSpeed = yawSpeedx;
         this.pitchSpeed = pitchSpeedx;
         this.hitX = hitXx;
         this.hitY = hitYx;
         this.hitZ = hitZx;
         this.yawShake = yawShakex;
         this.pitchShake = pitchShakex;
         this.yawLead = yawLeadx;
         this.pitchLead = pitchLeadx;
         this.yawAccel = yawAccelx;
         this.pitchAccel = pitchAccelx;
         this.smoothness = smoothnessx;
         this.curveArc = curveArcx;
         this.overshootRatio = overshootRatiox;
         this.peak = peakx;
         this.remembered = rememberedx;
      }

      public StrokesHelper.Prediction initHit(float yawSpeed, float pitchSpeed) {
         return new StrokesHelper.Prediction(
            yawSpeedx,
            pitchSpeedx,
            this.hitX,
            this.hitY,
            this.hitZ,
            this.yawShake,
            this.pitchShake,
            this.yawLead,
            this.pitchLead,
            this.yawAccel,
            this.pitchAccel,
            this.smoothness,
            this.curveArc,
            this.overshootRatio,
            this.peak,
            this.remembered
         );
      }
   }
}
