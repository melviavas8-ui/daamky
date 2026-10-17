package dev.daamky.client.legacy.setting;

import dev.daamky.client.legacy.setting.MotionConfig7.JitterPreset;

public class MotionConfig7JitterPresetImpl4 implements JitterPreset {
   public MotionConfig7JitterPresetImpl4(final MotionConfig7 this$0) {
   }

   public float computeSin(long t) {
      return (float)(Math.sin((double)t / 52.0 + 2.7) * 17.0);
   }

   public float computeCos(long t) {
      return (float)(Math.cos((double)t / 92.0 + 1.3) * 11.0);
   }
}
