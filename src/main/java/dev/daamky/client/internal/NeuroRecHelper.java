package dev.daamky.client.internal;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.RotationEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.misc.FakePlayer;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.util.MinecraftAccess;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class NeuroRecHelper implements MinecraftAccess {
   public static final NeuroRecHelper neuroRecHelper = new NeuroRecHelper();
   public final Map<String, StrokesHelper> map = new ConcurrentHashMap<>();
   public StrokesHelper strokesHelper;
   public NeuroRecHelper.RecordType recordType = NeuroRecHelper.RecordType.NONE;
   public long timeStamp = 0L;
   public int intVal = 0;
   public final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
   public Thread thread = null;
   public final List<double[]> list = new ArrayList<>();
   public final List<double[]> list2 = new ArrayList<>();
   public float floatVal;
   public float float2;
   public float float3;
   public float float4;
   public float float5 = 0.5F;
   public float float6 = 0.5F;
   public float float7 = 0.5F;
   public float float8;
   public float float9;
   public boolean flag;
   public float float10;
   public float float11;
   public int int2;
   public float float12 = 1.0F;
   public float float13 = 0.0F;
   public final List<float[]> list3 = new ArrayList<>();
   public final List<float[]> list4 = new ArrayList<>();
   public int int3 = 999;
   public LivingEntity class_1309 = null;
   public OtherClientPlayerEntity class_745 = null;
   public Subscription subscription;
   public Subscription mayzo;
   public Subscription subscription2;
   public float float14 = 0.0F;
   public float float15 = 0.0F;
   public float maumxe = 0.0F;
   public float float16 = 0.0F;

   public static NeuroRecHelper getInstance() {
      return neuroRecHelper;
   }

   public NeuroRecHelper() {
      this.loadModels();
      this.initEvents();
   }

   public void initEvents() {
      this.subscription = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$initEvents$0));
      this.mayzo = RotationEvent.getInstance().subscribe(new EventListener<>(this::lambda$initEvents$1));
      this.subscription2 = AttackEvent.getInstance().subscribe(new EventListener<>(this::lambda$initEvents$2));
   }

   public boolean checkNone() {
      return this.recordType != NeuroRecHelper.RecordType.NONE;
   }

   public NeuroRecHelper.RecordType getRecordType() {
      return this.recordType;
   }

   public boolean get() {
      return this.atomicBoolean.get();
   }

   public StrokesHelper getStrokesHelper() {
      return this.strokesHelper;
   }

   public Collection<StrokesHelper> values() {
      return this.map.values();
   }

   public boolean computeCase(String name) {
      StrokesHelper var2 = this.map.get(name.toLowerCase());
      if (var2 == null) {
         var2 = StrokesHelper.initInit(name);
      }

      if (var2 == null) {
         return false;
      } else {
         this.map.put(name.toLowerCase(), var2);
         this.strokesHelper = var2;
         StrokesHelper.createBlank(var2.name);
         return true;
      }
   }

   public boolean delete(String name) {
      StrokesHelper var2 = this.map.remove(name.toLowerCase());

      try {
         Files.deleteIfExists(StrokesHelper.path.resolve(name + ".neuro"));
         Files.deleteIfExists(StrokesHelper.path.resolve(name + ".onnx"));
      } catch (Exception var4) {
      }

      if (var2 != null && this.strokesHelper == var2) {
         this.strokesHelper = this.map.values().stream().max(Comparator.comparingInt(NeuroRecHelper::lambda$delete$3)).orElse(null);
         StrokesHelper.createBlank(this.strokesHelper == null ? null : this.strokesHelper.name);
      }

      return var2 != null;
   }

   public void loadModels() {
      try {
         if (!Files.isDirectory(StrokesHelper.path)) {
            return;
         }

         try (Stream var1 = Files.list(StrokesHelper.path)) {
            var1.filter(NeuroRecHelper::lambda$loadModels$4).forEach(this::lambda$loadModels$5);
         }

         String var7 = StrokesHelper.readRead();
         if (var7 != null && this.map.containsKey(var7.toLowerCase())) {
            this.strokesHelper = this.map.get(var7.toLowerCase());
         } else if (!this.map.isEmpty()) {
            this.strokesHelper = this.map.values().iterator().next();
         }
      } catch (Exception var6) {
      }
   }

   public StrokesHelper.Prediction initInit(double[] features) {
      return this.strokesHelper == null
         ? new StrokesHelper.Prediction(45.0F, 30.0F, 0.5F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
         : this.strokesHelper.initYaw(features);
   }

   public float[] computeSpeed(float speed) {
      return this.strokesHelper == null ? new float[]{0.0F, 0.0F} : this.strokesHelper.setSpeed(speed);
   }

   public float[] computeFloats(float yawErr, float pitchErr, float sinceAttack) {
      return this.strokesHelper == null ? null : this.strokesHelper.computeSize(yawErr, pitchErr, sinceAttack);
   }

   public void setName(String name) {
      if (name != null && !name.isBlank()) {
         if (!this.map.containsKey(name.toLowerCase())) {
            this.setName(name);
         } else {
            this.computeCase(name);
         }
      }

      if (this.strokesHelper == null) {
         TextShaper.handleString("§c[Neuro] Сначала укажи или создай модель: §f*neuro create <имя>");
      } else if (this.recordType != NeuroRecHelper.RecordType.NONE) {
         TextShaper.handleString("§7[Neuro] Запись уже активна. Напиши §f*neuro stoprec §7для завершения");
      } else {
         this.runClear();
         this.recordType = NeuroRecHelper.RecordType.LIVE_DUELS;
         NotificationManager.initNext(
            "neuro_rec", "Запись [" + this.strokesHelper.name + "] §700:00 §8| §f0 сэмплов §8| §c0 ударов", NotificationManager.Notification.Type.WARNING
         );
         TextShaper.handleString(
            "§a[Neuro] §7Запись реального PvP для модели §f" + this.strokesHelper.name + "§7 запущена! Сражайся в дуэлях. §f*neuro stoprec §7для сохранения"
         );
      }
   }

   public void handleName(String name) {
      if (name != null && !name.isBlank()) {
         if (!this.map.containsKey(name.toLowerCase())) {
            this.setName(name);
         } else {
            this.computeCase(name);
         }
      }

      if (this.strokesHelper == null) {
         TextShaper.handleString("§c[Neuro] Сначала укажи или создай модель: §f*neuro create <имя>");
      } else if (this.recordType != NeuroRecHelper.RecordType.NONE) {
         TextShaper.handleString("§7[Neuro] Запись уже активна. Напиши §f*neuro stoprec");
      } else {
         this.runClear();
         this.recordType = NeuroRecHelper.RecordType.DUMMY_TRAIN;
         FakePlayer.getInstance().setEnabledInternal(true, false);
         this.class_745 = FakePlayer.getInstance().getProfile();
         this.class_1309 = this.class_745;
         NotificationManager.initNext(
            "neuro_rec", "Запись [" + this.strokesHelper.name + "] §700:00 §8| §f0 сэмплов §8| §c0 ударов", NotificationManager.Notification.Type.WARNING
         );
         TextShaper.handleString(
            "§a[Neuro] §7Запись на FakePlayer для §f" + this.strokesHelper.name + "§7 запущена! Бей бота. §f*neuro stoprec §7для сохранения"
         );
      }
   }

   public void runNeuro() {
      if (this.recordType == NeuroRecHelper.RecordType.NONE) {
         TextShaper.handleString("§7[Neuro] Запись не активна");
      } else {
         NeuroRecHelper.RecordType var1 = this.recordType;
         this.recordType = NeuroRecHelper.RecordType.NONE;
         if (var1 == NeuroRecHelper.RecordType.DUMMY_TRAIN) {
            FakePlayer.getInstance().setEnabledInternal(false, false);
            this.class_745 = null;
         }

         int var2 = this.list.size();
         if (var2 == 0) {
            NotificationManager.initNext("neuro_rec", "Запись отменена: сэмплы не собраны", NotificationManager.Notification.Type.ERROR, 3000L);
            TextShaper.handleString("§c[Neuro] Сэмплов не собрано (цель не была в прицеле/не атакована)");
         } else {
            this.strokesHelper.initClear();

            for (int var3 = 0; var3 < var2; var3++) {
               this.strokesHelper.handleAdd(this.list.get(var3), this.list2.get(var3));
            }

            this.strokesHelper.handleNext(this.list3);
            this.strokesHelper.handleNext2(this.list4);
            this.strokesHelper.createCreate();
            long var5 = (System.currentTimeMillis() - this.longVal) / 1000L;
            NotificationManager.initNext(
               "neuro_rec",
               "Запись " + this.strokesHelper.name + " завершена: " + var2 + " сэмплов, " + this.intVal + " ударов",
               NotificationManager.Notification.Type.SUCCESS,
               4000L
            );
            TextShaper.handleString(
               String.format(
                  "§a[Neuro] §7Запись завершена! Сохранено §f%d§7 сэмплов, §f%d§7 штрихов за §f%d§7 сек (ударов: §f%d§7). Теперь напиши §f*neuro train %s",
                  var2,
                  this.list3.size(),
                  var5,
                  this.intVal,
                  this.strokesHelper.name
               )
            );
         }
      }
   }

   public void startLearn(String name, int epochs) {
      if (name != null && !name.isBlank()) {
         this.computeCase(name);
      }

      if (this.strokesHelper == null) {
         TextShaper.handleString("§c[Neuro] Модель не выбрана. Выбери или создай: §f*neuro create <имя>");
      } else if (this.atomicBoolean.get()) {
         TextShaper.handleString("§c[Neuro] Обучение уже выполняется в фоне!");
      } else {
         int var3 = Math.max(50, epochs <= 0 ? 1000 : epochs);
         List var4 = this.strokesHelper.getList();
         List var5 = this.strokesHelper.getList2();
         if (var4.isEmpty()) {
            TextShaper.handleString(
               "§c[Neuro] В датасете модели §f" + this.strokesHelper.name + "§c нет сэмплов! Сначала запиши движения: §f*neuro rec " + this.strokesHelper.name
            );
         } else {
            int var6 = var4.size();
            double[][] var7 = var4.toArray(new double[0][]);
            double[][] var8 = var5.toArray(new double[0][]);
            StrokesHelper var9 = this.strokesHelper;
            this.atomicBoolean.set(true);
            NotificationManager.initNext("neuro_train", "Обучение [" + var9.name + "] §70%", NotificationManager.Notification.Type.INFO);
            TextShaper.handleString(String.format("§a[Neuro] §7Запуск обучения модели §f%s§7: §f%d§7 эпох на §f%d§7 сэмплах...", var9.name, var3, var6));
            this.thread = new Thread(this::lambda$startLearn$6, "Gone-Neuro-Trainer");
            this.thread.setDaemon(true);
            this.thread.start();
         }
      }
   }

   public static String initAppend(int percent, int totalChars) {
      int var2 = MathHelper.clamp(percent * totalChars / 100, 0, totalChars);
      StringBuilder var3 = new StringBuilder();
      var3.append("§a");

      for (int var4 = 0; var4 < var2; var4++) {
         var3.append("█");
      }

      var3.append("§8");

      for (int var5 = var2; var5 < totalChars; var5++) {
         var3.append("░");
      }

      return var3.toString();
   }

   public void runClear() {
      this.list.clear();
      this.list2.clear();
      if (mc.player != null) {
         this.floatVal = mc.player.getYaw();
         this.floatValVal2 = mc.player.getPitch();
         this.floatValVal8 = mc.player.getYaw();
         this.floatValVal9 = mc.player.getPitch();
      }

      this.floatValVal3 = 0.0F;
      this.floatValVal4 = 0.0F;
      this.floatValVal14 = 0.0F;
      this.floatValVal15 = 0.0F;
      this.maumxe = 0.0F;
      this.floatValVal16 = 0.0F;
      this.floatValVal5 = 0.5F;
      this.floatValVal6 = 0.5F;
      this.floatValVal7 = 0.5F;
      this.flag = false;
      this.floatValVal10 = 0.0F;
      this.floatValVal11 = 0.0F;
      this.intValVal2 = 0;
      this.floatValVal12 = 1.0F;
      this.floatValVal13 = 0.0F;
      this.list3.clear();
      this.list4.clear();
      this.intValVal3 = 999;
      this.intVal = 0;
      this.longVal = System.currentTimeMillis();
      this.class_1309 = null;
   }

   public void runMin() {
      if (this.recordType != NeuroRecHelper.RecordType.NONE && mc.player != null && mc.world != null) {
         this.intValVal3 = Math.min(this.intValVal3 + 1, 999);
         if (this.recordType == NeuroRecHelper.RecordType.LIVE_DUELS) {
            this.getMc();
         } else if (this.recordType == NeuroRecHelper.RecordType.DUMMY_TRAIN) {
            if (this.class_745 == null || this.class_745.isRemoved()) {
               this.class_745 = FakePlayer.getInstance().getProfile();
            }

            this.class_1309 = this.class_745;
         }

         if (this.class_1309 != null && !this.class_1309.isDead()) {
            this.initAdd(this.class_1309);
         }
      }
   }

   public void getMc() {
      if (mc.player != null && mc.world != null) {
         if (mc.crosshairTarget instanceof EntityHitResult var1 && var1.getEntity() instanceof LivingEntity var2 && var2 != mc.player) {
            this.class_1309 = var2;
            return;
         }

         double var9 = 36.0;
         LivingEntity var11 = null;

         for (Entity var5 : mc.world.getEntities()) {
            if (var5 instanceof LivingEntity) {
               LivingEntity var6 = (LivingEntity)var5;
               if (var6 != mc.player && var6.isAlive()) {
                  double var7 = mc.player.squaredDistanceTo(var6);
                  if (var7 < var9) {
                     var9 = var7;
                     var11 = var6;
                  }
               }
            }
         }

         if (var11 != null) {
            this.class_1309 = var11;
         }
      }
   }

   public void runAbs() {
      if (this.recordType != NeuroRecHelper.RecordType.NONE && mc.player != null) {
         long var1 = System.currentTimeMillis() - this.longVal;
         long var3 = var1 / 1000L % 60L;
         long var5 = var1 / 60000L;
         String var7 = String.format("%02d:%02d", var5, var3);
         String var8 = this.strokesHelper != null ? this.strokesHelper.name : "Neuro";
         NotificationManager.initNext(
            "neuro_rec",
            "Запись [" + var8 + "] §7" + var7 + " §8| §f" + this.list.size() + " сэмплов §8| §c" + this.intVal + " ударов",
            NotificationManager.Notification.Type.WARNING
         );
         float var9 = mc.player.getYaw();
         float var10 = mc.player.getPitch();
         if (!this.flag) {
            this.floatValVal8 = var9;
            this.floatValVal9 = var10;
            this.flag = true;
         } else {
            float var11 = Math.abs(MathHelper.wrapDegrees(var9 - this.floatValVal8));
            float var12 = Math.abs(var10 - this.floatValVal9);
            float var13 = (float)Math.hypot((double)var11, (double)var12);
            this.floatValVal10 += var13;
            if (var13 > this.floatValVal11) {
               this.floatValVal11 = var13;
            }

            this.intValVal2++;
            this.floatValVal8 = var9;
            this.floatValVal9 = var10;
         }
      }
   }

   public void initAdd(LivingEntity target) {
      if (mc.player != null) {
         Vec3d var2 = mc.player.getEyePos();
         Vec3d var3 = target.getBoundingBox().getCenter();
         Vec3d var4 = var3.subtract(var2);
         float var5 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var4.z, var4.x)) - 90.0);
         float var6 = (float)(-Math.toDegrees(Math.atan2(var4.y, Math.hypot(var4.x, var4.z))));
         float var7 = this.floatVal;
         float var8 = this.floatValVal2;
         float var9 = MathHelper.wrapDegrees(var5 - var7);
         float var10 = var6 - var8;
         float var11 = mc.player.getYaw();
         float var12 = mc.player.getPitch();
         float var13 = MathHelper.wrapDegrees(var11 - var7);
         float var14 = var12 - var8;
         this.floatVal = var11;
         this.floatValVal2 = var12;
         float var15 = (float)Math.hypot((double)var13, (double)var14);
         Box var16 = target.getBoundingBox();
         boolean var17 = var16.raycast(var2, var2.add(mc.player.getRotationVector().multiply(6.0))).isPresent();
         boolean var18 = var15 > 0.02F || var17 || this.intValVal3 <= 15;
         if (!var18) {
            this.floatValVal3 = 0.0F;
            this.floatValVal4 = 0.0F;
            this.floatValVal14 = 0.0F;
            this.floatValVal15 = 0.0F;
            this.maumxe = 0.0F;
            this.floatValVal16 = 0.0F;
         } else {
            double var19 = var2.distanceTo(var3);
            float var21 = (float)(target.getY() - mc.player.getY());
            Vec3d var22 = target.getVelocity();
            Vec3d var23 = mc.player.getVelocity();
            double var24 = var4.length();
            float var26 = 0.0F;
            float var27 = 0.0F;
            if (var24 > 0.01) {
               Vec3d var28 = var4.multiply(1.0 / var24);
               Vec3d var29 = new Vec3d(-var28.z, 0.0, var28.x);
               Vec3d var30 = new Vec3d(0.0, 1.0, 0.0);
               var26 = (float)(var22.dotProduct(var29) / var24 * 20.0);
               var27 = (float)(var22.dotProduct(var30) / var24 * 20.0);
            }

            float var39 = mc.player.getAttackCooldownProgress(0.5F);
            float var40 = this.intValVal2 > 1 && this.floatValVal10 > 0.001F
               ? MathHelper.clamp((float)Math.hypot((double)var13, (double)var14) / this.floatValVal10, 0.0F, 1.0F)
               : 1.0F;
            float var41 = this.floatValVal11;
            this.floatValVal10 = 0.0F;
            this.floatValVal11 = 0.0F;
            this.intValVal2 = 0;
            Optional var31 = var16.raycast(var2, var2.add(mc.player.getRotationVector().multiply(6.0)));
            if (var31.isPresent()) {
               Vec3d var32 = (Vec3d)var31.get();
               double var33 = var16.maxX - var16.minX;
               double var35 = var16.maxY - var16.minY;
               double var37 = var16.maxZ - var16.minZ;
               if (var33 > 0.001) {
                  this.floatValVal5 = (float)MathHelper.clamp((var32.x - var16.minX) / var33, 0.1, 0.9);
               }

               if (var35 > 0.001) {
                  this.floatValVal6 = (float)MathHelper.clamp((var32.y - var16.minY) / var35, 0.1, 0.9);
               }

               if (var37 > 0.001) {
                  this.floatValVal7 = (float)MathHelper.clamp((var32.z - var16.minZ) / var37, 0.1, 0.9);
               }
            }

            float var42 = MathHelper.clamp(var13 - this.floatValVal3, -1.2F, 1.2F);
            float var43 = MathHelper.clamp(var14 - this.floatValVal4, -0.8F, 0.8F);
            double[] var34 = StrokesHelper.computeMin(
               var9,
               var10,
               this.floatValVal3,
               this.floatValVal4,
               this.floatValVal14,
               this.floatValVal15,
               this.maumxe,
               this.floatValVal16,
               this.floatValVal5,
               this.floatValVal6,
               this.floatValVal7,
               (float)var19,
               var21,
               var8,
               var6,
               var26,
               var27,
               (float)var22.x,
               (float)var22.y,
               (float)var22.z,
               (float)var23.y,
               var39,
               (float)this.intValVal3,
               var40,
               var41
            );
            double[] var44 = new double[14];
            var44[0] = MathHelper.clamp((double)(Math.abs(var13) / 180.0F), 0.0, 1.0);
            var44[1] = MathHelper.clamp((double)(Math.abs(var14) / 120.0F), 0.0, 1.0);
            var44[2] = (double)this.floatValVal5;
            var44[3] = (double)this.floatValVal6;
            var44[4] = (double)this.floatValVal7;
            var44[5] = StrokesHelper.computeDouble(var42, 1.2F);
            var44[6] = StrokesHelper.computeDouble(var43, 0.8F);
            var44[7] = StrokesHelper.computeDouble(var26 * 0.12F, 12.0F);
            var44[8] = StrokesHelper.computeDouble(var27 * 0.12F, 8.0F);
            float var36 = var13 - this.floatValVal3;
            float var45 = var14 - this.floatValVal4;
            var44[9] = StrokesHelper.computeDouble(var36, 35.0F);
            var44[10] = StrokesHelper.computeDouble(var45, 22.0F);
            var44[11] = (double)var40;
            var44[12] = 0.5;
            var44[13] = 0.5;
            this.list.add(var34);
            this.list2.add(var44);
            this.list3.add(new float[]{var42, var43, var15});
            this.list4.add(new float[]{var9, var10, var13, var14, (float)this.intValVal3});
            this.maumxe = var36;
            this.floatValVal16 = var45;
            this.floatValVal14 = this.floatValVal3;
            this.floatValVal15 = this.floatValVal4;
            this.floatValVal3 = var13;
            this.floatValVal4 = var14;
         }
      }
   }

   public void lambda$startLearn$6(double[][] X, int targetEpochs, StrokesHelper model, double[][] Y, int sampleCount) {
      try {
         long var6 = System.currentTimeMillis();
         int var8 = X.length;
         int[] var9 = new int[var8];
         int var10 = 0;

         while (var10 < var8) {
            var9[var10] = var10++;
         }

         double var31 = 0.045;
         double var12 = 0.88;
         double var14 = 0.0;
         int var16 = Math.max(1, targetEpochs / 10);

         for (int var17 = 1; var17 <= targetEpochs && this.atomicBoolean.get(); var17++) {
            double var18 = var31 * (1.0 - 0.65 * ((double)var17 / (double)targetEpochs));
            var14 = model.neuralNet.computeNext(X, Y, var18, var12, var9);
            if (var17 % var16 == 0 || var17 == targetEpochs) {
               int var20 = (int)Math.round((double)var17 / (double)targetEpochs * 100.0);
               long var21 = System.currentTimeMillis() - var6;
               double var23 = (double)var17 / (double)Math.max(1L, var21);
               int var25 = (int)Math.max(0.0, (double)(targetEpochs - var17) / (var23 * 1000.0));
               NotificationManager.initNext(
                  "neuro_train",
                  "Обучение [" + model.name + "] §a" + var20 + "% §8| §7Loss: §e" + String.format("%.4f", var14) + " §8| §f" + var25 + "с",
                  NotificationManager.Notification.Type.INFO
               );
            }
         }

         model.mvijuk = var14;
         model.int2 = sampleCount;
         model.createCreate();
         model.createAppend();
         long var32 = (System.currentTimeMillis() - var6) / 1000L;
         NotificationManager.initNext(
            "neuro_train", "Модель " + model.name + " обучена! Loss: " + String.format("%.4f", var14), NotificationManager.Notification.Type.SUCCESS, 4000L
         );
         TextShaper.handleString(
            String.format("§a[Neuro] §7Обучение модели §f%s§7 завершено за §f%dс§7! Loss: §a%.5f§7. Активирована для ротации Neuro", model.name, var32, var14)
         );
      } catch (Exception var29) {
         NotificationManager.initNext("neuro_train", "Ошибка обучения: " + var29.getMessage(), NotificationManager.Notification.Type.ERROR, 4000L);
         TextShaper.handleString("§c[Neuro] Ошибка во время обучения: " + var29.getMessage());
         var29.printStackTrace();
      } finally {
         this.atomicBoolean.set(false);
      }
   }

   public void lambda$loadModels$5(Path path) {
      String var2 = path.getFileName().toString();
      String var3 = var2.substring(0, var2.length() - ".neuro".length());
      StrokesHelper var4 = StrokesHelper.initInit(var3);
      if (var4 != null) {
         this.map.put(var3.toLowerCase(), var4);
      }
   }

   public static boolean lambda$loadModels$4(Path path) {
      return path.getFileName().toString().endsWith(".neuro");
   }

   public static int lambda$delete$3(StrokesHelper model) {
      return model.int2;
   }

   public void lambda$initEvents$2(AttackEvent.AttackEventData event) {
      if (this.recordType != NeuroRecHelper.RecordType.NONE) {
         this.intValVal3 = 0;
         Entity var2 = event.entity();
         if (var2 instanceof LivingEntity var3) {
            this.class_1309 = var3;
            this.intVal++;
            if (this.class_745 != null && var2.getId() == this.class_745.getId()) {
               if (mc.player != null) {
                  mc.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
                  mc.player.playSound(SoundEvents.ENTITY_PLAYER_HURT, 0.85F, 1.0F);
               }

               this.class_745.hurtTime = 10;
               this.class_745.maxHurtTime = 10;
               this.class_745.animateDamage(0.0F);
            }
         }
      }
   }

   public void lambda$initEvents$1(RotationEvent event) {
      this.runAbs();
   }

   public void lambda$initEvents$0(UpdateEvent event) {
      this.runMin();
   }

   public static enum RecordType {
      NONE,
      LIVE_DUELS,
      DUMMY_TRAIN;

      public static NeuroRecHelper.RecordType[] $values() {
         return new NeuroRecHelper.RecordType[]{NONE, LIVE_DUELS, DUMMY_TRAIN};
      }
   }
}
