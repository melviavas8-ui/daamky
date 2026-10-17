package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl12 implements JitterPreset {
   public MotionConfig7JitterPresetImpl12(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 65.0 + 2.1) * 16.0);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 45.0) * 14.0);
   }
}
