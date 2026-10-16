package dev.daamky.client.event;

public class WindowResizeEvent extends Event<WindowResizeEvent> {
   public static final WindowResizeEvent windowResizeEvent = new WindowResizeEvent();

   public static WindowResizeEvent getInstance() {
      return windowResizeEvent;
   }
}
