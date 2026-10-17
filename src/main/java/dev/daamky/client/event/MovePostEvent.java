package dev.daamky.client.event;

import net.minecraft.util.math.Vec3d;

public class MovePostEvent extends Event<MovePostEvent.EventOnMovePostData> {
   public static final MovePostEvent movePostEvent = new MovePostEvent();

   public static MovePostEvent getInstance() {
      return movePostEvent;
   }

   public static class EventOnMovePostData {
      public final float speed;
      public final Vec3d movementInput;

      public EventOnMovePostData(float speed, Vec3d movementInput) {
         this.speed = speed;
         this.movementInput = movementInput;
      }

      public float getSpeed() {
         return this.speed;
      }

      public Vec3d getMovementInput() {
         return this.movementInput;
      }

      public boolean computeInput(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof MovePostEvent.EventOnMovePostData var2)) {
            return false;
         } else if (!var2.computeFlag(this)) {
            return false;
         } else if (Float.compare(this.getSpeed(), var2.getSpeed()) != 0) {
            return false;
         } else {
            Vec3d var3 = this.getMovementInput();
            Vec3d var4 = var2.getMovementInput();
            return var3 != null ? var3.equals(var4) : var4 == null;
         }
      }

      public boolean computeFlag(Object other) {
         return other instanceof MovePostEvent.EventOnMovePostData;
      }

      public int getBits() {
         byte var1 = 59;
         int var2 = 1;
         var2 = var2 * 59 + Float.floatToIntBits(this.getSpeed());
         Vec3d var3 = this.getMovementInput();
         return var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      }

      public String getInput() {
         return "EventOnMovePost.EventOnMovePostData(speed=" + this.getSpeed() + ", movementInput=" + this.getMovementInput() + ")";
      }
   }
}
