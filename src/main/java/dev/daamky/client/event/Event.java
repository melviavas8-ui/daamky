package dev.daamky.client.event;

public class Event<T> extends EventBus<T> {
   public boolean cancel = false;

   public T post() {
      return (T)this;
   }

   public boolean postAndCheckCancelled() {
      this.cancel = false;
      this.publish(this.post());
      return this.cancel;
   }

   public boolean postAndCheckCancelled(T any) {
      this.cancel = false;
      this.publish((T)any);
      return this.cancel;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }
}
