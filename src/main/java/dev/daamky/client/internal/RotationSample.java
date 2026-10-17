package dev.daamky.client.internal;

public class RotationSample {
   public long timeOffset;
   public float yawOffset;
   public float pitchOffset;

   public RotationSample(long timeOffset, float yawOffset, float pitchOffset) {
      this.timeOffset = timeOffset;
      this.yawOffset = yawOffset;
      this.pitchOffset = pitchOffset;
   }
}
