package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl implements JitterPreset {
   public MotionConfig7JitterPresetImpl(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return(floatVal)(Math.cos((double)t / 90.0) * 12.0);
   }

   public float computeCos(long t) {
      return(floatVal)(Math.sin((double)t / 90.0) * 16.0);
   }
}
