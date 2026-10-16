package dev.daamky.client.event;

public record Subscription(Runnable action) {

   public void unsubscribe() {
      this.action.run();
   }
}
