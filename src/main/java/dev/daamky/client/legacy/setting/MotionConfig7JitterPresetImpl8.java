package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl8 implements JitterPreset {
   public MotionConfig7JitterPresetImpl8(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return(floatVal)(Math.cos((double)t / 48.0 + 1.2) * 13.0);
   }

   public float computeCos(long t) {
      return(floatVal)(Math.sin((double)t / 68.0 + 0.4) * 15.0);
   }
}
