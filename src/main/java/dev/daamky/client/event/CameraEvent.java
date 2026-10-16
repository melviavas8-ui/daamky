package dev.daamky.client.event;

public class CameraEvent extends Event<CameraEvent> {
   public static final CameraEvent cameraEvent = new CameraEvent();
   public float yaw;
   public float pitch;
   public float partialTicks;

   public static CameraEvent getInstance() {
      return cameraEvent;
   }

   public void handlePartial(float yaw, float pitch, float partialTicks) {
      this.yaw = yawx;
      this.pitch = pitchx;
      this.partialTicks = partialTicksx;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public void setYaw(float yaw) {
      this.yaw = yawx;
   }

   public void setPitch(float pitch) {
      this.pitch = pitchx;
   }

   public void setPartialTicks(float partialTicks) {
      this.partialTicks = partialTicksx;
   }
}
