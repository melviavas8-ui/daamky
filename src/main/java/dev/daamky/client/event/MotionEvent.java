package dev.daamky.client.event;

public class MotionEvent extends Event<MotionEvent.MotionEventData> {
   public static final MotionEvent motionEvent = new MotionEvent();
   public static final MotionEvent.MotionEventData motionEventData = new MotionEvent.MotionEventData(0.0, 0.0, 0.0, 0.0, 0.0, false);

   public static MotionEvent getInstance() {
      return motionEvent;
   }

   public static MotionEvent.MotionEventData getMotionEventData() {
      return motionEventData;
   }

   public static class MotionEventData {
      public double x;
      public double y;
      public double z;
      public double yaw;
      public double pitch;
      public boolean ground;

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public double getZ() {
         return this.z;
      }

      public double getYaw() {
         return this.yaw;
      }

      public double getPitch() {
         return this.pitch;
      }

      public boolean isGround() {
         return this.ground;
      }

      public MotionEvent.MotionEventData withX(double x) {
         this.x = xx;
         return this;
      }

      public MotionEvent.MotionEventData withY(double y) {
         this.y = yx;
         return this;
      }

      public MotionEvent.MotionEventData withZ(double z) {
         this.z = zx;
         return this;
      }

      public MotionEvent.MotionEventData withYaw(double yaw) {
         this.yaw = yawx;
         return this;
      }

      public MotionEvent.MotionEventData withPitch(double pitch) {
         this.pitch = pitchx;
         return this;
      }

      public MotionEvent.MotionEventData withGround(boolean ground) {
         this.ground = groundx;
         return this;
      }

      public MotionEventData(double x, double y, double z, double yaw, double pitch, boolean ground) {
         this.x = xx;
         this.y = yx;
         this.z = zx;
         this.yaw = yawx;
         this.pitch = pitchx;
         this.ground = groundx;
      }
   }
}
