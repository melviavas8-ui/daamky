package dev.daamky.client.event;

public class TravelEvent extends Event<TravelEvent> {
   public static final TravelEvent travelEvent = new TravelEvent();

   public static TravelEvent getInstance() {
      return travelEvent;
   }
}
