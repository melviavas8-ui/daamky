package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl10 implements JitterPreset {
   public MotionConfig7JitterPresetImpl10(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return(floatVal)(Math.sin((double)t / 35.0) * 8.0);
   }

   public float computeCos(long t) {
      return(floatVal)(Math.cos((double)t / 110.0) * 18.0);
   }
}
