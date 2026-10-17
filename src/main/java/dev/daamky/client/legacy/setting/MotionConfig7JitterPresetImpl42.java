package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl42 implements JitterPreset {
   public MotionConfig7JitterPresetImpl42(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 70.0) * 12.0);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 80.0) * 11.0);
   }
}
