package dev.daamky.client.event;

public class UpdateEvent extends Event<UpdateEvent> {
   public static final UpdateEvent updateEvent = new UpdateEvent();

   public static UpdateEvent getInstance() {
      return updateEvent;
   }
}
