package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl9 implements JitterPreset {
   public MotionConfig7JitterPresetImpl9(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 125.0) * 13.5);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 135.0) * 13.5);
   }
}
