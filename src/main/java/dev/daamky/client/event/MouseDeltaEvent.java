package dev.daamky.client.event;

public class MouseDeltaEvent extends Event<MouseDeltaEvent> {
   public static final MouseDeltaEvent mmuut = new MouseDeltaEvent();
   public double yaw;
   public double pitch;

   public static MouseDeltaEvent getInstance() {
      return mmuut;
   }

   public void handleYaw(double yaw, double pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public double getYaw() {
      return this.yaw;
   }

   public double getPitch() {
      return this.pitch;
   }

   public void setYaw(double yaw) {
      this.yaw = yaw;
   }

   public void setPitch(double pitch) {
      this.pitch = pitch;
   }
}
