package dev.daamky.client.event;

public class JumpEvent extends Event<JumpEvent> {
   public static final JumpEvent jumpEvent = new JumpEvent();

   public static JumpEvent getInstance() {
      return jumpEvent;
   }
}
