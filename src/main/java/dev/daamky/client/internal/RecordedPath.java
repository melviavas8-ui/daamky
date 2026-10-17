package dev.daamky.client.internal;

import java.util.ArrayList;
import java.util.List;

public class RecordedPath {
   public boolean jump;
   public List<RotationSample> points = new ArrayList<>();

   public RecordedPath(boolean jump) {
      this.jump = jump;
   }

   public void initAdd(long timeOffset, float yawOffset, float pitchOffset) {
      this.points.add(new RotationSample(timeOffset, yawOffset, pitchOffset));
   }
}
