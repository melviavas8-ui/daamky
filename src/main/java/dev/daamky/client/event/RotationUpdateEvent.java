package dev.daamky.client.event;

public class RotationUpdateEvent extends Event<RotationUpdateEvent> {
   public static final RotationUpdateEvent rotationUpdateEvent = new RotationUpdateEvent();

   public static RotationUpdateEvent getInstance() {
      return rotationUpdateEvent;
   }
}
