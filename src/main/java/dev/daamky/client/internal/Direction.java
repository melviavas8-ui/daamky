package dev.daamky.client.internal;

import dev.daamky.client.model.enums.AnimationDirection;

public class Direction extends Animation {
   public Direction(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public Direction(int ms, double endPoint, AnimationDirection direction) {
      super(ms, endPoint, direction);
   }

   @Override
   public double computeDuration(double x1) {
      double var3 = x1 / (double)this.duration;
      return 1.0 + 2.70158 * (var3 - 1.0) * (var3 - 1.0) * (var3 - 1.0) + 1.70158 * (var3 - 1.0) * (var3 - 1.0);
   }
}
