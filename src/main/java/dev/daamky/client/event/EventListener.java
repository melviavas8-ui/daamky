package dev.daamky.client.event;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class EventListener<T> implements Comparable<EventListener<T>> {
   public static final AtomicLong COUNTER = new AtomicLong(0L);
   public final int priority;
   public final Consumer<T> handler2;
   public final long id;

   public EventListener(int priority, Consumer<T> handler2) {
      this.priority = priorityx;
      this.handler2 = handler2x;
      this.id = COUNTER.getAndIncrement();
   }

   public EventListener(Consumer<T> handler2) {
      this(0, handler2x);
   }

   public int compareTo(EventListener<T> other) {
      int var2 = Integer.compare(other.priority, this.priority);
      return var2 != 0 ? var2 : Long.compare(this.id, other.id);
   }

   public int getPriority() {
      return this.priority;
   }

   public Consumer<T> getHandler() {
      return this.handler2;
   }

   @Override
   public int compareTo(Object param1) {
      return this.compareTo((EventListener<T>)var1);
   }
}
