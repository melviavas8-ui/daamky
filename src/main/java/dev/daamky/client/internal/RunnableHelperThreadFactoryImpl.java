package dev.daamky.client.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class RunnableHelperThreadFactoryImpl implements ThreadFactory {
   public final AtomicInteger atomicInteger = new AtomicInteger(1);

   @Override
   public Thread newThread(Runnable runnable) {
      Thread var2 = new Thread(runnable, "daamky-worker-" + this.atomicInteger.getAndIncrement());
      var2.setDaemon(true);
      return var2;
   }
}
