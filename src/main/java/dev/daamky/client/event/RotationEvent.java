package dev.daamky.client.event;

public class RotationEvent extends Event<RotationEvent> {
   public static final RotationEvent rotationEvent = new RotationEvent();

   public static RotationEvent getInstance() {
      return rotationEvent;
   }
}
