package dev.daamky.client.event;

public class InteractEvent extends Event<InteractEvent> {
   public static final InteractEvent interactEvent = new InteractEvent();

   public static InteractEvent getInstance() {
      return interactEvent;
   }
}
