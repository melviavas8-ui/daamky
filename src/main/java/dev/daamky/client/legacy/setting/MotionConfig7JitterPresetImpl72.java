package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl72 implements JitterPreset {
   public MotionConfig7JitterPresetImpl72(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 115.0 + 0.5) * 19.0);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 58.0 + 2.4) * 16.0);
   }
}
