package dev.daamky.client.event;

public class FramebufferResizeEvent extends Event<FramebufferResizeEvent> {
   public static final FramebufferResizeEvent instance = new FramebufferResizeEvent();

   public static FramebufferResizeEvent getInstance() {
      return instance;
   }
}
