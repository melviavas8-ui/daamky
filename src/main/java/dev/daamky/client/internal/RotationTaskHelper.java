package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.InputTransformEvent;
import dev.daamky.client.event.RotationEvent;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class RotationTaskHelper implements MinecraftAccess {
   public static final RotationTaskHelper rotationTaskHelper = new RotationTaskHelper();
   public static final float floatVal = 38.0F;
   public static final float float2 = 9.0F;
   public long timeStamp = 0L;
   public long long2 = 0L;
   public long long3 = 0L;
   public float float3 = 0.0F;
   public RotationTaskHelper.RotationTask currentTask = RotationTaskHelper.RotationTask.IDLE;
   public float currentYawSpeed;
   public float currentPitchSpeed;
   public float currentYawReturnSpeed;
   public float currentPitchReturnSpeed;
   public int currentPriority;
   public int currentTimeout;
   public int idleTicks;
   public Rotation targetRotation = new Rotation(0.0F, 0.0F);

   public void load() {
      UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$0));
      RotationEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$1));
      CameraManager.cameraManager.load();
   }

   public static double computeRadians(float rotationYaw, float moveForward, float moveStrafing) {
      if (moveForward < 0.0F) {
         rotationYaw += 180.0F;
      }

      float var3 = 1.0F;
      if (moveForward < 0.0F) {
         var3 = -0.5F;
      }

      if (moveForward > 0.0F) {
         var3 = 0.5F;
      }

      if (moveStrafing > 0.0F) {
         rotationYaw -= 90.0F * var3;
      }

      if (moveStrafing < 0.0F) {
         rotationYaw += 90.0F * var3;
      }

      return Math.toRadians((double)rotationYaw);
   }

   public void handleInputTransformEvent(InputTransformEvent event) {
   }

   public void initInit() {
      if (mc.player == null) {
         this.runCurrent();
      } else {
         long var1 = System.nanoTime();
         if (this.long2 == 0L) {
            this.long2 = var1;
            this.long3 = var1;
            this.float3 = (float)(Math.random() * 1000.0);
         }

         float var3 = (float)(var1 - this.long3) / 1.0E9F;
         this.long3 = var1;
         var3 = MathHelper.clamp(var3, 0.0F, 0.1F);
         float var4 = (float)(var1 - this.long2) / 1.0E9F;
         float var5 = CameraManager.getFloat();
         float var6 = CameraManager.getMrula();
         Rotation var7 = new Rotation(mc.player.getYaw(), mc.player.getPitch());
         float var8 = var7.computeAbs(new Rotation(var5, var6));
         if (!(var8 < 1.2F) && !(var4 > 1.2F)) {
            float var18 = MathHelper.clamp(var4 / 0.2F, 0.0F, 1.0F);
            float var10 = var18 * var18 * (3.0F - 2.0F * var18);
            float var11 = 75.0F + var10 * 450.0F;
            float var12 = var11 * MathHelper.clamp(var8 / 45.0F, 0.6F, 1.6F) * var3;
            float var13 = MathHelper.clamp(var8 / 25.0F, 0.0F, 1.0F);
            float var14 = (float)(Math.sin((double)var4 * 8.3 + (double)this.float3) * 0.55 + Math.sin((double)var4 * 3.7 + (double)this.float3 * 1.3) * 0.35)
               * var13;
            float var15 = (float)(Math.cos((double)var4 * 6.9 + (double)this.float3 * 0.7) * 0.4) * var13;
            Rotation var16 = new Rotation(var5 + var14, MathHelper.clamp(var6 + var15, -90.0F, 90.0F));
            this.initAbs(var16, var12, var12);
         } else {
            float var9 = mc.player.getYaw();
            mc.player.setYaw(var9 + MathHelper.wrapDegrees(var5 - var9));
            mc.player.setPitch(MathHelper.clamp(var6, -90.0F, 90.0F));
            this.long2 = 0L;
            this.long3 = 0L;
            this.runCurrent();
         }
      }
   }

   public void setEvent(UpdateEvent event) {
      if (this.getCurrentTask().equals(RotationTaskHelper.RotationTask.AIM) && this.getIdleTicks() > this.getCurrentTimeout()) {
         this.withCurrentTask(RotationTaskHelper.RotationTask.RESET);
      }

      this.idleTicks++;
   }

   public static Vec2f initRound(Vec2f rotation, Vec2f previousRotation) {
      double var2 = (Double)mc.options.getMouseSensitivity().getValue();
      double var4 = Math.pow(var2 * 0.6F + 0.2F, 3.0) * 8.0;
      double var6 = (double)previousRotation.x;
      double var8 = (double)previousRotation.y;
      double var10 = (double)rotation.x;
      double var12 = (double)rotation.y;
      double var14 = (double)Math.round((var10 - var6) / var4 / 0.15F) * var4 * 0.15F;
      double var16 = (double)Math.round((var12 - var8) / var4 / 0.15F) * var4 * 0.15F;
      return new Vec2f((float)(var6 + var14), (float)(var8 + var16));
   }

   public static void handleCurrent(
      Rotation target, float yawSpeed, float pitchSpeed, float yawReturnSpeed, float pitchReturnSpeed, int timeout, int priority, boolean clientRotation
   ) {
      RotationTaskHelper var8 = rotationTaskHelper;
      if (var8.getCurrentPriority() <= priority) {
         if (!clientRotation) {
            CameraManager.setState(true);
         }

         var8.withCurrentYawSpeed(yawSpeed);
         var8.withCurrentPitchSpeed(pitchSpeed);
         var8.withCurrentYawReturnSpeed(yawReturnSpeed);
         var8.withCurrentPitchReturnSpeed(pitchReturnSpeed);
         var8.withCurrentTimeout(timeout);
         var8.withCurrentPriority(priority);
         var8.withCurrentTask(RotationTaskHelper.RotationTask.AIM);
         var8.withTargetRotation(target);
         var8.long2 = 0L;
         var8.long3 = 0L;
         var8.initAbs(target, yawSpeed, pitchSpeed);
      }
   }

   public static void handleVal(Rotation targetRotation, float turnSpeed, float returnSpeed, int timeout, int priority) {
      handleCurrent(targetRotation, turnSpeed, turnSpeed, returnSpeed, returnSpeed, timeout, priority, false);
   }

   public static void handleVal(Rotation targetRotation, float yawSpeed, float pitchSpeed, float returnSpeed, int timeout, int priority) {
      handleCurrent(targetRotation, yawSpeed, pitchSpeed, returnSpeed, returnSpeed, timeout, priority, false);
   }

   public boolean initAbs(Rotation rotation, float turnYawSpeed, float turnPitchSpeed) {
      if (mc.player == null) {
         return false;
      } else {
         Rotation var4 = new Rotation(mc.player.getYaw(), mc.player.getPitch());
         float var5 = MathHelper.wrapDegrees(rotation.getYaw() - var4.getYaw());
         float var6 = rotation.getPitch() - var4.getPitch();
         float var7 = Math.abs(var5) + Math.abs(var6);
         float var8 = var7 == 0.0F ? 0.0F : Math.abs(var5 / var7) * turnYawSpeed;
         float var9 = var7 == 0.0F ? 0.0F : Math.abs(var6 / var7) * turnPitchSpeed;
         Vec2f var10 = initRound(
            new Vec2f(
               mc.player.getYaw() + MathHelper.clamp(var5, -var8, var8),
               MathHelper.clamp(mc.player.getPitch() + MathHelper.clamp(var6, -var9, var9), -90.0F, 90.0F)
            ),
            new Vec2f(mc.player.getYaw(), mc.player.getPitch())
         );
         mc.player.setYaw(var10.x);
         mc.player.setPitch(var10.y);
         Rotation var11 = new Rotation(mc.player.getYaw(), mc.player.getPitch());
         this.withIdleTicks(0);
         return (double)var11.computeAbs(rotation)
            < (
               this.currentTask.equals(RotationTaskHelper.RotationTask.RESET)
                  ? Math.hypot((double)this.currentYawReturnSpeed, (double)this.currentPitchReturnSpeed)
                  : Math.hypot((double)this.currentYawSpeed, (double)this.currentPitchSpeed)
            );
      }
   }

   public void runCurrent() {
      this.withCurrentTask(RotationTaskHelper.RotationTask.IDLE);
      this.withCurrentPriority(0);
      this.long2 = 0L;
      this.long3 = 0L;
      this.withIdleTicks(0);
      CameraManager.setState(false);
   }

   public boolean equals() {
      return !this.currentTask.equals(RotationTaskHelper.RotationTask.IDLE);
   }

   public RotationTaskHelper.RotationTask getCurrentTask() {
      return this.currentTask;
   }

   public float getCurrentYawSpeed() {
      return this.currentYawSpeed;
   }

   public float getCurrentPitchSpeed() {
      return this.currentPitchSpeed;
   }

   public float getCurrentYawReturnSpeed() {
      return this.currentYawReturnSpeed;
   }

   public float getCurrentPitchReturnSpeed() {
      return this.currentPitchReturnSpeed;
   }

   public int getCurrentPriority() {
      return this.currentPriority;
   }

   public int getCurrentTimeout() {
      return this.currentTimeout;
   }

   public int getIdleTicks() {
      return this.idleTicks;
   }

   public Rotation getTargetRotation() {
      return this.targetRotation;
   }

   public RotationTaskHelper withCurrentTask(RotationTaskHelper.RotationTask currentTask) {
      this.currentTask = currentTask;
      return this;
   }

   public RotationTaskHelper withCurrentYawSpeed(float currentYawSpeed) {
      this.currentYawSpeed = currentYawSpeed;
      return this;
   }

   public RotationTaskHelper withCurrentPitchSpeed(float currentPitchSpeed) {
      this.currentPitchSpeed = currentPitchSpeed;
      return this;
   }

   public RotationTaskHelper withCurrentYawReturnSpeed(float currentYawReturnSpeed) {
      this.currentYawReturnSpeed = currentYawReturnSpeed;
      return this;
   }

   public RotationTaskHelper withCurrentPitchReturnSpeed(float currentPitchReturnSpeed) {
      this.currentPitchReturnSpeed = currentPitchReturnSpeed;
      return this;
   }

   public RotationTaskHelper withCurrentPriority(int currentPriority) {
      this.currentPriority = currentPriority;
      return this;
   }

   public RotationTaskHelper withCurrentTimeout(int currentTimeout) {
      this.currentTimeout = currentTimeout;
      return this;
   }

   public RotationTaskHelper withIdleTicks(int idleTicks) {
      this.idleTicks = idleTicks;
      return this;
   }

   public RotationTaskHelper withTargetRotation(Rotation targetRotation) {
      this.targetRotation = targetRotation;
      return this;
   }

   public void lambda$load$1(RotationEvent event) {
      if (this.getCurrentTask().equals(RotationTaskHelper.RotationTask.RESET)) {
         this.initInit();
      }
   }

   public void lambda$load$0(UpdateEvent event) {
      this.setEvent(event);
   }

   public static enum RotationTask {
      AIM,
      RESET,
      IDLE;

      public static RotationTaskHelper.RotationTask[] $values() {
         return new RotationTaskHelper.RotationTask[]{AIM, RESET, IDLE};
      }
   }
}
