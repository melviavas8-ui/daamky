package dev.daamky.client.event;

import java.util.concurrent.ConcurrentSkipListSet;

public abstract class EventBus<T> implements EventListenerProvider<T>, EventSubscriber<EventListener<T>, T>, EventPublisher<T> {
   public final ConcurrentSkipListSet<EventListener<T>> listeners = new ConcurrentSkipListSet<>();
   public volatile EventListener<T>[] cache = new EventListener[0];

   @Override
   public EventListener<T>[] getListeners() {
      return this.cache;
   }

   public boolean isEmpty() {
      return this.cache.length != 0;
   }

   public Subscription subscribe(EventListener<T> listener) {
      this.listeners.add(listener);
      this.cache = this.listeners.toArray(EventBus::lambda$subscribe$0);
      return new Subscription(() -> this.lambda$subscribe$1(listener));
   }

   public void unsubscribe(EventListener<T> listener) {
      if (this.listeners.remove(listener)) {
         this.cache = this.listeners.toArray(EventBus::lambda$unsubscribe$2);
      }
   }

   @Override
   public void publish(T event) {
      for (EventListener var5 : this.cache) {
         var5.getHandler().accept(event);
      }
   }

   public static EventListener[] lambda$unsubscribe$2(int x$0) {
      return new EventListener[x$0];
   }

   public void lambda$subscribe$1(EventListener listener) {
      this.unsubscribe(listener);
   }

   public static EventListener[] lambda$subscribe$0(int x$0) {
      return new EventListener[x$0];
   }
}
