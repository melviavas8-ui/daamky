package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl7 implements JitterPreset {
   public MotionConfig7JitterPresetImpl7(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return(floatVal)(Math.cos((double)t / 42.0) * 9.0);
   }

   public float computeCos(long t) {
      return(floatVal)(Math.sin((double)t / 38.0) * 17.0);
   }
}
