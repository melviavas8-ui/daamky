package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl52 implements JitterPreset {
   public MotionConfig7JitterPresetImpl52(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 160.0) * 22.0);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 140.0) * 5.0);
   }
}
