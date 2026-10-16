package dev.daamky.client.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class RunnableHelper {
   public static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
      public final AtomicInteger atomicInteger = new AtomicInteger(1);

      @Override
      public Thread newThread(Runnable runnable) {
         Thread var2 = new Thread(runnable, "daamky-worker-" + this.atomicInteger.getAndIncrement());
         var2.setDaemon(true);
         return var2;
      }
   });

   public static void handleExecute(Runnable runnable) {
      executorService.execute(runnable);
   }

   public RunnableHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
