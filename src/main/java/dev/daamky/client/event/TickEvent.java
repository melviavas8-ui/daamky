package dev.daamky.client.event;

public class TickEvent extends Event<TickEvent> {
   public static final TickEvent tickEvent = new TickEvent();

   public static TickEvent getInstance() {
      return tickEvent;
   }
}
