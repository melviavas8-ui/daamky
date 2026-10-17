package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl32 implements JitterPreset {
   public MotionConfig7JitterPresetImpl32(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 28.0) * 10.0);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 85.0) * 20.0);
   }
}
