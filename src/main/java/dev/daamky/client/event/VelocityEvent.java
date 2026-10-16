package dev.daamky.client.event;

import net.minecraft.util.math.Vec3d;

public class VelocityEvent extends Event<VelocityEvent.VelocityEventData> {
   public static final VelocityEvent velocityEvent = new VelocityEvent();

   public static VelocityEvent getInstance() {
      return velocityEvent;
   }

   public static class VelocityEventData {
      public Vec3d movementInput;
      public float speed;
      public float yaw;
      public Vec3d velocity;

      public Vec3d getMovementInput() {
         return this.movementInput;
      }

      public float getSpeed() {
         return this.speed;
      }

      public float getYaw() {
         return this.yaw;
      }

      public Vec3d getVelocity() {
         return this.velocity;
      }

      public VelocityEventData(Vec3d movementInput, float speed, float yaw, Vec3d velocity) {
         this.movementInput = movementInputx;
         this.speed = speedx;
         this.yaw = yawx;
         this.velocity = velocityx;
      }

      public void setMovementInput(Vec3d movementInput) {
         this.movementInput = movementInputx;
      }

      public void setVelocity(Vec3d velocity) {
         this.velocity = velocityx;
      }
   }
}
