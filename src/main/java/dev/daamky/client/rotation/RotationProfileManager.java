package dev.daamky.client.rotation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.daamky.client.internal.ClientPaths;
import dev.daamky.client.internal.EasingTypeHelper;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class RotationProfileManager {
   public static final RotationProfileManager instance = new RotationProfileManager();
   public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public static final File FILE = new File(ClientPaths.string5, "custom_rotation_profile.json");
   public static final File PROFILES_DIR = new File(ClientPaths.string5, "rotation_profiles");
   public final List<RotationProfileManager.CurvePoint> curvePoints = new ArrayList<>();
   public final List<RotationProfileManager.AimNode> aimNodes = new ArrayList<>();
   public EasingTypeHelper.EasingType easingType = EasingTypeHelper.EasingType.EASE_OUT_CUBIC;
   public RotationProfileManager.TraversalMode traversalMode = RotationProfileManager.TraversalMode.CYCLE;
   public float rotationSpeed = 1.0F;
   public float yawSpeed = 1.0F;
   public float pitchSpeed = 0.85F;
   public float accelFactor = 0.4F;
   public float maxYawStep = 60.0F;
   public float maxPitchStep = 40.0F;
   public float noiseFrequency = 4.5F;
   public float yawNoise = 1.2F;
   public float pitchNoise = 0.8F;
   public float humanizeJitter = 0.35F;
   public float overshootChance = 0.35F;
   public float overshootDistance = 3.5F;
   public float overshootDuration = 250.0F;
   public float smoothness = 0.85F;
   public boolean smoothPath = true;
   public String currentPresetName = "Default";
   public int activeNodeIndex = 0;
   public int traversalDirection = 1;

   public RotationProfileManager() {
      this.initAdd();
      this.getFILE();
   }

   public static RotationProfileManager getInstance() {
      return instance;
   }

   public void getAimNodes() {
      for (int var1 = 0; var1 < this.aimNodes.size(); var1++) {
         this.aimNodes.get(var1).id = var1 + 1;
      }
   }

   public void initAdd() {
      this.curvePoints.clear();
      this.curvePoints.add(new RotationProfileManager.CurvePoint(0.0F, 0.2F));
      this.curvePoints.add(new RotationProfileManager.CurvePoint(0.25F, 1.45F));
      this.curvePoints.add(new RotationProfileManager.CurvePoint(0.6F, 1.1F));
      this.curvePoints.add(new RotationProfileManager.CurvePoint(0.85F, 0.7F));
      this.curvePoints.add(new RotationProfileManager.CurvePoint(1.0F, 0.3F));
      this.aimNodes.clear();
      this.aimNodes.add(new RotationProfileManager.AimNode(1, "Head", 0.0F, 1.75F, 0.0F, 1.0F));
      this.aimNodes.add(new RotationProfileManager.AimNode(2, "Upper Chest", 0.05F, 1.4F, 0.0F, 0.9F));
      this.aimNodes.add(new RotationProfileManager.AimNode(3, "Center / Pelvis", -0.05F, 1.05F, 0.0F, 0.8F));
      this.getAimNodes();
      this.easingType = EasingTypeHelper.EasingType.EASE_OUT_CUBIC;
      this.traversalMode = RotationProfileManager.TraversalMode.CYCLE;
      this.rotationSpeed = 1.0F;
      this.yawSpeed = 1.0F;
      this.pitchSpeed = 0.85F;
      this.accelFactor = 0.4F;
      this.maxYawStep = 60.0F;
      this.maxPitchStep = 40.0F;
      this.noiseFrequency = 4.5F;
      this.yawNoise = 1.2F;
      this.pitchNoise = 0.8F;
      this.humanizeJitter = 0.35F;
      this.overshootChance = 0.35F;
      this.overshootDistance = 3.5F;
      this.overshootDuration = 250.0F;
      this.smoothness = 0.85F;
      this.smoothPath = true;
      this.currentPresetName = "Default";
   }

   public void setCurrentPresetName(String presetName) {
      if (presetName != null && !"Default".equalsIgnoreCase(presetName)) {
         this.currentPresetName = presetName;
         File var2 = new File(PROFILES_DIR, presetName + ".json");
         if (var2.exists()) {
            this.closeClose(var2);
         }

         this.closeFile();
      } else {
         this.initAdd();
         this.currentPresetName = "Default";
         this.closeFile();
      }
   }

   public static List<String> getPresetList() {
      ArrayList var0 = new ArrayList();
      var0.add("Default");

      try {
         if (PROFILES_DIR.exists() && PROFILES_DIR.isDirectory()) {
            File[] var1 = PROFILES_DIR.listFiles(RotationProfileManager::lambda$getPresetList$0);
            if (var1 != null) {
               for (File var5 : var1) {
                  String var6 = var5.getName().substring(0, var5.getName().length() - 5);
                  if (!"Default".equalsIgnoreCase(var6) && !var0.contains(var6)) {
                     var0.add(var6);
                  }
               }
            }
         }
      } catch (Throwable var7) {
      }

      return var0;
   }

   public synchronized void closeClose(String name) {
      if (name != null && !name.trim().isEmpty()) {
         name = name.trim();
         this.currentPresetName = name;

         try {
            if (!PROFILES_DIR.exists()) {
               PROFILES_DIR.mkdirs();
            }

            File var2 = new File(PROFILES_DIR, name + ".json");

            try (FileWriter var3 = new FileWriter(var2)) {
               GSON.toJson(this, var3);
            }

            this.closeFile();
         } catch (Throwable var8) {
         }
      }
   }

   public synchronized void initDefault(String name) {
      if (name != null && !"Default".equalsIgnoreCase(name)) {
         try {
            File var2 = new File(PROFILES_DIR, name + ".json");
            if (var2.exists()) {
               var2.delete();
            }

            this.setCurrentPresetName("Default");
         } catch (Throwable var3) {
         }
      }
   }

   public float computeCurve(float progress) {
      float var2 = Math.max(0.0F, Math.min(1.0F, progress));
      if (this.curvePoints.isEmpty()) {
         return 1.0F;
      } else if (this.curvePoints.size() == 1) {
         return this.curvePoints.get(0).speed;
      } else {
         for (int var3 = 0; var3 < this.curvePoints.size() - 1; var3++) {
            RotationProfileManager.CurvePoint var4 = this.curvePoints.get(var3);
            RotationProfileManager.CurvePoint var5 = this.curvePoints.get(var3 + 1);
            if (var2 >= var4.time && var2 <= var5.time) {
               float var6 = var5.time - var4.time <= 1.0E-4F ? 0.0F : (var2 - var4.time) / (var5.time - var4.time);
               float var7 = this.easingType.evaluate(var6);
               return var4.speed + (var5.speed - var4.speed) * var7;
            }
         }

         return this.curvePoints.get(this.curvePoints.size() - 1).speed;
      }
   }

   public RotationProfileManager.AimNode initActive() {
      if (this.aimNodes.isEmpty()) {
         return new RotationProfileManager.AimNode(1, "Center", 0.0F, 1.4F, 0.0F, 1.0F);
      } else {
         this.activeNodeIndex = Math.max(0, Math.min(this.aimNodes.size() - 1, this.activeNodeIndex));
         return this.aimNodes.get(this.activeNodeIndex);
      }
   }

   public RotationProfileManager.AimNode initActive2() {
      if (this.aimNodes.isEmpty()) {
         return new RotationProfileManager.AimNode(1, "Center", 0.0F, 1.4F, 0.0F, 1.0F);
      } else if (this.aimNodes.size() == 1) {
         return this.aimNodes.get(0);
      } else {
         if (this.traversalMode == RotationProfileManager.TraversalMode.CYCLE) {
            this.activeNodeIndex = (this.activeNodeIndex + 1) % this.aimNodes.size();
         } else if (this.traversalMode == RotationProfileManager.TraversalMode.SEQUENCE) {
            this.activeNodeIndex = this.activeNodeIndex + this.traversalDirection;
            if (this.activeNodeIndex >= this.aimNodes.size()) {
               this.activeNodeIndex = this.aimNodes.size() - 2;
               this.traversalDirection = -1;
            } else if (this.activeNodeIndex < 0) {
               this.activeNodeIndex = 1;
               this.traversalDirection = 1;
            }
         } else if (this.traversalMode == RotationProfileManager.TraversalMode.RANDOM) {
            this.activeNodeIndex = (int)(Math.random() * (double)this.aimNodes.size());
         }

         this.activeNodeIndex = Math.max(0, Math.min(this.aimNodes.size() - 1, this.activeNodeIndex));
         return this.aimNodes.get(this.activeNodeIndex);
      }
   }

   public synchronized void closeFile() {
      try {
         File var1 = FILE.getParentFile();
         if (var1 != null && !var1.exists()) {
            var1.mkdirs();
         }

         try (FileWriter var2 = new FileWriter(FILE)) {
            GSON.toJson(this, var2);
         }
      } catch (Throwable var7) {
      }
   }

   public synchronized void getFILE() {
      this.closeClose(FILE);
   }

   public synchronized void closeClose(File file) {
      try {
         if (file == null || !file.exists()) {
            return;
         }

         try (FileReader var2 = new FileReader(file)) {
            RotationProfileManager var3 = (RotationProfileManager)GSON.fromJson(var2, RotationProfileManager.class);
            if (var3 != null) {
               this.handleSpeed(var3);
            }
         }
      } catch (Throwable var7) {
      }
   }

   public synchronized void handleJson(JsonObject json) {
      if (json != null) {
         try {
            RotationProfileManager var2 = (RotationProfileManager)GSON.fromJson(json, RotationProfileManager.class);
            if (var2 != null) {
               this.handleSpeed(var2);
            }
         } catch (Throwable var3) {
         }
      }
   }

   public synchronized void handleSpeed(RotationProfileManager loaded) {
      if (loaded != null) {
         if (loaded.curvePoints != null && !loaded.curvePoints.isEmpty()) {
            this.curvePoints.clear();
            this.curvePoints.addAll(loaded.curvePoints);
         }

         if (loaded.aimNodes != null && !loaded.aimNodes.isEmpty()) {
            this.aimNodes.clear();
            this.aimNodes.addAll(loaded.aimNodes);
         }

         if (loaded.easingType != null) {
            this.easingType = loaded.easingType;
         }

         if (loaded.traversalMode != null) {
            this.traversalMode = loaded.traversalMode;
         }

         if (loaded.rotationSpeed > 0.05F) {
            this.rotationSpeed = loaded.rotationSpeed;
         }

         if (loaded.yawSpeed > 0.05F) {
            this.yawSpeed = loaded.yawSpeed;
         }

         if (loaded.pitchSpeed > 0.05F) {
            this.pitchSpeed = loaded.pitchSpeed;
         }

         if (loaded.accelFactor > 0.01F) {
            this.accelFactor = loaded.accelFactor;
         }

         if (loaded.maxYawStep > 1.0F) {
            this.maxYawStep = loaded.maxYawStep;
         }

         if (loaded.maxPitchStep > 1.0F) {
            this.maxPitchStep = loaded.maxPitchStep;
         }

         this.noiseFrequency = loaded.noiseFrequency;
         this.yawNoise = loaded.yawNoise;
         this.pitchNoise = loaded.pitchNoise;
         this.humanizeJitter = loaded.humanizeJitter;
         this.overshootChance = loaded.overshootChance;
         this.overshootDistance = loaded.overshootDistance > 0.0F ? loaded.overshootDistance : 3.5F;
         this.overshootDuration = loaded.overshootDuration > 10.0F ? loaded.overshootDuration : 250.0F;
         this.smoothness = loaded.smoothness;
         this.smoothPath = loaded.smoothPath;
         if (loaded.currentPresetName != null) {
            this.currentPresetName = loaded.currentPresetName;
         }
      }
   }

   public float[] computeRel(float progress) {
      if (this.aimNodes.isEmpty()) {
         return new float[]{0.0F, 1.4F, 0.0F};
      } else if (this.aimNodes.size() != 1 && this.smoothPath) {
         int var13 = this.aimNodes.size();
         float var3 = Math.max(0.0F, progress) % 1.0F * (float)var13;
         int var4 = (int)var3 % var13;
         int var5 = (var4 + 1) % var13;
         float var6 = var3 - (float)((int)var3);
         float var7 = this.easingType.evaluate(var6);
         RotationProfileManager.AimNode var8 = this.aimNodes.get(var4);
         RotationProfileManager.AimNode var9 = this.aimNodes.get(var5);
         float var10 = var8.relX + (var9.relX - var8.relX) * var7;
         float var11 = var8.relY + (var9.relY - var8.relY) * var7;
         float var12 = var8.relZ + (var9.relZ - var8.relZ) * var7;
         return new float[]{var10, var11, var12};
      } else {
         RotationProfileManager.AimNode var2 = this.initActive();
         return new float[]{var2.relX, var2.relY, var2.relZ};
      }
   }

   public List<RotationProfileManager.CurvePoint> getCurvePoints() {
      return this.curvePoints;
   }

   public EasingTypeHelper.EasingType getEasingType() {
      return this.easingType;
   }

   public void setEasingType(EasingTypeHelper.EasingType easingType) {
      this.easingType = easingTypex;
   }

   public RotationProfileManager.TraversalMode getTraversalMode() {
      return this.traversalMode;
   }

   public void setTraversalMode(RotationProfileManager.TraversalMode traversalMode) {
      this.traversalMode = traversalModex;
   }

   public float getRotationSpeed() {
      return this.rotationSpeed;
   }

   public void setRotationSpeed(float rotationSpeed) {
      this.rotationSpeed = rotationSpeedx;
   }

   public float getYawSpeed() {
      return this.yawSpeed;
   }

   public void setYawSpeed(float yawSpeed) {
      this.yawSpeed = yawSpeedx;
   }

   public float getPitchSpeed() {
      return this.pitchSpeed;
   }

   public void setPitchSpeed(float pitchSpeed) {
      this.pitchSpeed = pitchSpeedx;
   }

   public float getAccelFactor() {
      return this.accelFactor;
   }

   public void setAccelFactor(float accelFactor) {
      this.accelFactor = accelFactorx;
   }

   public float getMaxYawStep() {
      return this.maxYawStep;
   }

   public void setMaxYawStep(float maxYawStep) {
      this.maxYawStep = maxYawStepx;
   }

   public float getMaxPitchStep() {
      return this.maxPitchStep;
   }

   public void setMaxPitchStep(float maxPitchStep) {
      this.maxPitchStep = maxPitchStepx;
   }

   public float getNoiseFrequency() {
      return this.noiseFrequency;
   }

   public void setNoiseFrequency(float noiseFrequency) {
      this.noiseFrequency = noiseFrequencyx;
   }

   public float getYawNoise() {
      return this.yawNoise;
   }

   public void setYawNoise(float yawNoise) {
      this.yawNoise = yawNoisex;
   }

   public float getPitchNoise() {
      return this.pitchNoise;
   }

   public void setPitchNoise(float pitchNoise) {
      this.pitchNoise = pitchNoisex;
   }

   public float getHumanizeJitter() {
      return this.humanizeJitter;
   }

   public void setHumanizeJitter(float humanizeJitter) {
      this.humanizeJitter = humanizeJitterx;
   }

   public float getOvershootChance() {
      return this.overshootChance;
   }

   public void setOvershootChance(float overshootChance) {
      this.overshootChance = overshootChancex;
   }

   public float getOvershootDistance() {
      return this.overshootDistance;
   }

   public void setOvershootDistance(float overshootDistance) {
      this.overshootDistance = overshootDistancex;
   }

   public float getOvershootDuration() {
      return this.overshootDuration;
   }

   public void setOvershootDuration(float overshootDuration) {
      this.overshootDuration = overshootDurationx;
   }

   public float getSmoothness() {
      return this.smoothness;
   }

   public void setSmoothness(float smoothness) {
      this.smoothness = smoothnessx;
   }

   public boolean isSmoothPath() {
      return this.smoothPath;
   }

   public void setSmoothPath(boolean smoothPath) {
      this.smoothPath = smoothPathx;
   }

   public String getCurrentPresetName() {
      return this.currentPresetName;
   }

   public void setCurrentPresetName2(String currentPresetName) {
      this.currentPresetName = currentPresetNamex;
   }

   public float getYawNoise2() {
      return this.yawNoise;
   }

   public void setYawNoise2(float noiseAmplitude) {
      this.yawNoise = noiseAmplitude;
   }

   public float getFloat() {
      return this.overshootDistance / 10.0F;
   }

   public void handleFloat(float overshootFactor) {
      this.overshootDistance = overshootFactor * 10.0F;
   }

   public static boolean lambda$getPresetList$0(File dir, String name) {
      return name.endsWith(".json");
   }

   public static class AimNode {
      public int id;
      public String label;
      public float relX;
      public float relY;
      public float relZ;
      public float weight;

      public AimNode() {
      }

      public AimNode(int id, String label, float relX, float relY, float relZ, float weight) {
         this.id = idx;
         this.label = labelx;
         this.relX = relXx;
         this.relY = relYx;
         this.relZ = relZx;
         this.weight = weightx;
      }
   }

   public static class CurvePoint {
      public float time;
      public float speed;

      public CurvePoint() {
      }

      public CurvePoint(float time, float speed) {
         this.time = Math.max(0.0F, Math.min(1.0F, timex));
         this.speed = Math.max(0.0F, Math.min(2.5F, speedx));
      }
   }

   public static enum TraversalMode {
      CYCLE("Cycle (1 -> 2 -> 3 -> 1)"),
      SEQUENCE("Ping-Pong (1 -> 2 -> 3 -> 2 -> 1)"),
      NEAREST("Nearest to Crosshair"),
      RANDOM("Adaptive Jitter Random");

      public final String displayName;

      private TraversalMode(String displayName) {
         this.displayName = displayNamex;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public static RotationProfileManager.TraversalMode[] $values() {
         return new RotationProfileManager.TraversalMode[]{CYCLE, SEQUENCE, NEAREST, RANDOM};
      }
   }
}
