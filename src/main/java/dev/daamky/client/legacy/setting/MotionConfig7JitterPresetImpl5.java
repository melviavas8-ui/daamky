package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl5 implements JitterPreset {
   public MotionConfig7JitterPresetImpl5(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return(floatVal)(Math.cos((double)t / 130.0 + 0.8) * 20.0);
   }

   public float computeCos(long t) {
      return(floatVal)(Math.sin((double)t / 95.0) * 8.0);
   }
}
