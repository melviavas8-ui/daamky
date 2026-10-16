package dev.daamky.client.event;

public interface EventSubscriber<L, T> {
   Subscription subscribe(L var1);

   void unsubscribe(L var1);
}
