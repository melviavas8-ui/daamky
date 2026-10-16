package dev.daamky.client.event;

import net.minecraft.util.math.Vec3d;

public class MoveEvent extends Event<MoveEvent.MoveEventData> {
   public static final MoveEvent myecl = new MoveEvent();
   public static final MoveEvent.MoveEventData moveEventData = new MoveEvent.MoveEventData(0.0, 0.0, 0.0);

   public static MoveEvent getInstance() {
      return myecl;
   }

   public static MoveEvent.MoveEventData getMoveEventData() {
      return moveEventData;
   }

   public static class MoveEventData {
      public double x;
      public double y;
      public double z;

      public void handleClass_243(Vec3d vec3d) {
         this.x = vec3d.getX();
         this.y = vec3d.getY();
         this.z = vec3d.getZ();
      }

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public double getZ() {
         return this.z;
      }

      public void setX(double x) {
         this.x = xx;
      }

      public void setY(double y) {
         this.y = yx;
      }

      public void setZ(double z) {
         this.z = zx;
      }

      public MoveEventData(double x, double y, double z) {
         this.x = xx;
         this.y = yx;
         this.z = zx;
      }
   }
}
