package dev.daamky.client.internal;

import dev.daamky.client.event.EventHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class LambdaFactoryInfoHelper implements Callback7 {
   public final Map<Object, List<Callback8>> listenerCache;
   public final Map<Class<?>, List<Callback8>> staticListenerCache;
   public final Map<Class<?>, List<Callback8>> listenerMap;
   public final Supplier<List<Callback8>> listenerListFactory;
   public final List<LambdaFactoryInfoHelper.LambdaFactoryInfo> list = new CopyOnWriteArrayList<>();

   public LambdaFactoryInfoHelper(
      Map<Object, List<Callback8>> listenerCache,
      Map<Class<?>, List<Callback8>> staticListenerCache,
      Map<Class<?>, List<Callback8>> listenerMap,
      Supplier<List<Callback8>> listenerListFactory
   ) {
      this.listenerCache = listenerCachex;
      this.staticListenerCache = staticListenerCachex;
      this.listenerMap = listenerMapx;
      this.listenerListFactory = listenerListFactoryx;
   }

   public static LambdaFactoryInfoHelper initInit() {
      return new LambdaFactoryInfoHelper(
         Collections.synchronizedMap(new IdentityHashMap<>()), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), CopyOnWriteArrayList::new
      );
   }

   public static LambdaFactoryInfoHelper initInit2() {
      return new LambdaFactoryInfoHelper(new IdentityHashMap<>(), new IdentityHashMap<>(), new IdentityHashMap<>(), CopyOnWriteArrayList::new);
   }

   @Override
   public void initAdd(String packagePrefix, HandleFactory.Factory factory) {
      this.list.add(new LambdaFactoryInfoHelper.LambdaFactoryInfo(packagePrefix, factory));
   }

   @Override
   public boolean computeListener(Class<?> eventClass) {
      List var2 = this.listenerMap.get(eventClass);
      return var2 != null && !var2.isEmpty();
   }

   public boolean computeListener(Callback8 listener) {
      List var2 = this.listenerMap.get(listener.getValue());
      return var2 != null && ListenersHelper.computeNext(var2, listener);
   }

   public boolean computeContains(Object object) {
      return this.listenerCache.containsKey(object);
   }

   public boolean computeNext(Class<?> staticListener) {
      for (Callback8 var4 : this.staticListenerCache.get(staticListener)) {
         if (var4 instanceof HandleFactory var5) {
            if (var5.klass != staticListener) {
               break;
            }

            if (!this.computeListener(var4)) {
               return true;
            }
         }
      }

      return true;
   }

   public boolean computeNext2(Class<?> staticListener) {
      List var2 = this.staticListenerCache.get(staticListener);
      if (var2 == null) {
         return false;
      } else {
         for (Callback8 var4 : var2) {
            if (this.computeListener(var4)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean computeNext3(Class<?> staticListener) {
      List var2 = this.staticListenerCache.get(staticListener);
      if (var2 == null) {
         return false;
      } else {
         for (Callback8 var4 : var2) {
            if (!this.computeListener(var4)) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public <T> T computeNext(T event) {
      List var2 = this.listenerMap.get(event.getClass());
      if (var2 != null) {
         for (Callback8 var4 : var2) {
            try {
               var4.handleAccept(event);
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      }

      return (T)event;
   }

   @Override
   public void handleContains(Object object) {
      if (!this.listenerCache.containsKey(object)) {
         this.handleNext(this.getInstanceListeners(object), false);
      }
   }

   @Override
   public void handleListeners(Class<?> klass) {
      List var2 = this.getStaticListeners(klass);
      this.handleNext(var2, true);
   }

   @Override
   public void handleCallback8(Callback8 listener) {
      this.subscribe(listener, true);
   }

   public void handleNext(List<Callback8> listeners, boolean check) {
      for (Callback8 var4 : listeners) {
         this.subscribe(var4, check);
      }
   }

   public void subscribe(Callback8 listener, boolean check) {
      List var3 = this.listenerMap.computeIfAbsent(listener.getValue(), this::lambda$subscribe$0);
      if (!check || !ListenersHelper.computeNext(var3, listener)) {
         ListenersHelper.handleAdd(var3, listener);
      }
   }

   @Override
   public void handleListener(Object object) {
      List var2 = this.listenerCache.get(object);
      if (var2 != null) {
         this.handleNext(var2);
         this.listenerCache.remove(object);
      }
   }

   @Override
   public void handleListener(Class<?> klass) {
      List var2 = this.staticListenerCache.get(klass);
      if (var2 != null) {
         this.handleNext(var2);
      }
   }

   @Override
   public void handleListener(Callback8 listener) {
      List var2 = this.listenerMap.get(listener.getValue());
      if (var2 != null) {
         ListenersHelper.computeRemove(var2, listener);
      }
   }

   public void handleNext(List<Callback8> listeners) {
      for (Callback8 var3 : listeners) {
         this.handleListener(var3);
      }
   }

   public List<Callback8> getStaticListeners(Class<?> klass) {
      return this.staticListenerCache.computeIfAbsent(klass, this::lambda$getStaticListeners$1);
   }

   public List<Callback8> getInstanceListeners(Object object) {
      return this.listenerCache.computeIfAbsent(object, this::lambda$getInstanceListeners$2);
   }

   public List<Callback8> initInit(Class<?> klass, Object object, boolean staticOnly) {
      ArrayList var4 = new ArrayList();
      this.initAdd(var4, klass, object, staticOnly);
      return new ArrayBackedIteratorHelper<>(var4);
   }

   public boolean computeType(Method method, boolean staticOnly) {
      if (staticOnly && !Modifier.isStatic(method.getModifiers())) {
         return false;
      } else if (!method.isAnnotationPresent(EventHandler.class)) {
         return false;
      } else if (method.getReturnType() != void.class) {
         return false;
      } else {
         return method.getParameterCount() != 1 ? false : !method.getParameters()[0].getType().isPrimitive();
      }
   }

   public HandleFactory.Factory initNext(Class<?> klass) {
      for (LambdaFactoryInfoHelper.LambdaFactoryInfo var3 : this.list) {
         if (klass.getName().startsWith(var3.packagePrefix)) {
            return var3.factory;
         }
      }

      throw new Klass(klass);
   }

   public List lambda$getInstanceListeners$2(Object o) {
      return this.initInit(o.getClass(), o, false);
   }

   public List lambda$getStaticListeners$1(Class o) {
      return this.initInit((Class<?>)o, null, true);
   }

   public List lambda$subscribe$0(Class klass) {
      return this.listenerListFactory.get();
   }

   private static record LambdaFactoryInfo(String packagePrefix, HandleFactory.Factory factory) {
   }
}
