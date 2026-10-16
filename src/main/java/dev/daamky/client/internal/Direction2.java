package dev.daamky.client.internal;

import dev.daamky.client.model.enums.AnimationDirection;

public class Direction2 extends Animation {
   public Direction2(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public Direction2(int ms, double endPoint, AnimationDirection direction) {
      super(ms, endPoint, direction);
   }

   @Override
   public double computeDuration(double x1) {
      double var3 = x1 / (double)this.duration;
      return var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - (-2.0 * var3 + 2.0) * (-2.0 * var3 + 2.0) / 2.0;
   }
}
