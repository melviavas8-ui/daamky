package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl6 implements JitterPreset {
   public MotionConfig7JitterPresetImpl6(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return(floatVal)(Math.sin((double)t / 240.0) * 14.0);
   }

   public float computeCos(long t) {
      return(floatVal)(Math.cos((double)t / 240.0) * 14.0);
   }
}
