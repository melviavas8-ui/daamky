package dev.daamky.client.internal;

public interface Callback {
   void handleFlag(boolean var1);

   default void run() {
      this.handleFlag(true);
   }

   boolean check();
}
