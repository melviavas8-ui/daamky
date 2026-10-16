package dev.daamky.client.event;

public class KeyEvent extends Event<KeyEvent.KeyEventData> {
   public static final KeyEvent keyEvent = new KeyEvent();

   public static KeyEvent getInstance() {
      return keyEvent;
   }

   public static record KeyEventData(int key, int action, boolean mouse) {

      public boolean check() {
         return this.mouse;
      }
   }
}
