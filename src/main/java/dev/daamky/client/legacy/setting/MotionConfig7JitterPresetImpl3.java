package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl3 implements JitterPreset {
   public MotionConfig7JitterPresetImpl3(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 65.0) * 14.0);
   }

   public float computeCos(long t) {
      return (float)(Math.sin((double)t / 90.0) * 10.0);
   }
}
