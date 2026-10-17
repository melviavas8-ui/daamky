package dev.daamky.client.internal;

public class AimSnapshot {
   public final float onGround;
   public final float miniHitbox;
   public final float yawDelta;
   public final float pitchDelta;
   public final float targetYaw;
   public final float targetPitch;
   public final float sinceAttack;
   public final float distance;
   public final float playerYaw;
   public final float playerPitch;
   public final float playerPosX;
   public final float playerPosY;
   public final float playerPosZ;
   public final float playerVelX;
   public final float playerVelY;
   public final float playerVelZ;
   public final float targetPosX;
   public final float targetPosY;
   public final float targetPosZ;
   public final float targetVelX;
   public final float targetVelY;
   public final float targetVelZ;
   public final float deltaX;
   public final float deltaY;
   public final float deltaZ;

   public AimSnapshot(
      float yawDelta,
      float pitchDelta,
      float targetYaw,
      float targetPitch,
      float sinceAttack,
      float distance,
      float onGround,
      float miniHitbox,
      float playerYaw,
      float playerPitch,
      float playerPosX,
      float playerPosY,
      float playerPosZ,
      float playerVelX,
      float playerVelY,
      float playerVelZ,
      float targetPosX,
      float targetPosY,
      float targetPosZ,
      float targetVelX,
      float targetVelY,
      float targetVelZ,
      float deltaX,
      float deltaY,
      float deltaZ
   ) {
      this.yawDelta = yawDelta;
      this.pitchDelta = pitchDelta;
      this.targetYaw = targetYaw;
      this.targetPitch = targetPitch;
      this.sinceAttack = sinceAttack;
      this.distance = distance;
      this.onGround = onGround;
      this.miniHitbox = miniHitbox;
      this.playerYaw = playerYaw;
      this.playerPitch = playerPitch;
      this.playerPosX = playerPosX;
      this.playerPosY = playerPosY;
      this.playerPosZ = playerPosZ;
      this.playerVelX = playerVelX;
      this.playerVelY = playerVelY;
      this.playerVelZ = playerVelZ;
      this.targetPosX = targetPosX;
      this.targetPosY = targetPosY;
      this.targetPosZ = targetPosZ;
      this.targetVelX = targetVelX;
      this.targetVelY = targetVelY;
      this.targetVelZ = targetVelZ;
      this.deltaX = deltaX;
      this.deltaY = deltaY;
      this.deltaZ = deltaZ;
   }

   public float getOnGround() {
      return this.onGround;
   }

   public float getMiniHitbox() {
      return this.miniHitbox;
   }

   public float getYawDelta() {
      return this.yawDelta;
   }

   public float getPitchDelta() {
      return this.pitchDelta;
   }

   public float getTargetYaw() {
      return this.targetYaw;
   }

   public float getTargetPitch() {
      return this.targetPitch;
   }

   public float getSinceAttack() {
      return this.sinceAttack;
   }

   public float getDistance() {
      return this.distance;
   }

   public float getPlayerYaw() {
      return this.playerYaw;
   }

   public float getPlayerPitch() {
      return this.playerPitch;
   }

   public float getPlayerPosX() {
      return this.playerPosX;
   }

   public float getPlayerPosY() {
      return this.playerPosY;
   }

   public float getPlayerPosZ() {
      return this.playerPosZ;
   }

   public float getPlayerVelX() {
      return this.playerVelX;
   }

   public float getPlayerVelY() {
      return this.playerVelY;
   }

   public float getPlayerVelZ() {
      return this.playerVelZ;
   }

   public float getTargetPosX() {
      return this.targetPosX;
   }

   public float getTargetPosY() {
      return this.targetPosY;
   }

   public float getTargetPosZ() {
      return this.targetPosZ;
   }

   public float getTargetVelX() {
      return this.targetVelX;
   }

   public float getTargetVelY() {
      return this.targetVelY;
   }

   public float getTargetVelZ() {
      return this.targetVelZ;
   }

   public float getDeltaX() {
      return this.deltaX;
   }

   public float getDeltaY() {
      return this.deltaY;
   }

   public float getDeltaZ() {
      return this.deltaZ;
   }
}
