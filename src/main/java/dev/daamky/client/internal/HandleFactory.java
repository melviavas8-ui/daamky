package dev.daamky.client.internal;

import dev.daamky.client.event.EventHandler;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class HandleFactory implements Callback8 {
   public static final HandleFactory.LookupFactory lookupFactory;
   public static final Map<Method, MethodHandle> map = new ConcurrentHashMap<>();
   public final Class<?> classVal;
   public final boolean flag;
   public final int intVal;
   public final Consumer<Object> consumer;
   public final Class<?> klass;

   public HandleFactory(HandleFactory.Factory factory, Class<?> klass, Object object, Method method) {
      this.classVal = method.getParameters()[0].getType();
      this.flag = Modifier.isStatic(method.getModifiers());
      this.intVal = method.getAnnotation(EventHandler.class).priority();
      this.klass = klass;

      try {
         this.consumer = this.flag
            ? (Consumer)computeType(factory, klass, method).invoke()
            : (Consumer)computeType2(factory, klass, method).invoke((Object)object);
      } catch (Throwable var6) {
         throw new RuntimeException(var6);
      }
   }

   public static MethodHandle computeType(HandleFactory.Factory factory, Class<?> klass, Method method) throws Throwable {
      String var3 = method.getName();
      Lookup var4 = lookupFactory.lookup(factory, klass);
      MethodType var5 = MethodType.methodType(void.class, method.getParameters()[0].getType());
      MethodHandle var6 = var4.findStatic(klass, var3, var5);
      MethodType var7 = MethodType.methodType(Consumer.class);
      return LambdaMetafactory.metafactory(var4, "accept", var7, MethodType.methodType(void.class, Object.class), var6, var5).getTarget();
   }

   public static MethodHandle computeType2(HandleFactory.Factory factory, Class<?> klass, Method method) throws Throwable {
      MethodHandle var3 = map.get(method);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = method.getName();
         Lookup var5 = lookupFactory.lookup(factory, klass);
         MethodType var6 = MethodType.methodType(void.class, method.getParameters()[0].getType());
         MethodHandle var7 = var5.findVirtual(klass, var4, var6);
         MethodType var8 = MethodType.methodType(Consumer.class, klass);
         var3 = LambdaMetafactory.metafactory(var5, "accept", var8, MethodType.methodType(void.class, Object.class), var7, var6).getTarget();
         map.put(method, var3);
         return var3;
      }
   }

   @Override
   public void handleAccept(Object event) {
      this.consumer.accept(event);
   }

   @Override
   public Class<?> getClass() {
      return this.classVal;
   }

   public int getInt() {
      return this.intVal;
   }

   @Override
   public boolean check() {
      return this.flag;
   }

   public static boolean checkFactory() {
      return lookupFactory.requireLambdaFactory;
   }

   static {
      HandleFactory.LookupFactory var0;
      try {
         try {
            final Method var1 = MethodHandles.class.getDeclaredMethod("privateLookupIn", Class.class, Lookup.class);
            var0 = new HandleFactory.LookupFactory(true) {
               @Override
               public Lookup lookup(HandleFactory.Factory lambdaFactory, Class<?> klass) throws ReflectiveOperationException {
                  return lambdaFactory.lookup(var1, klass);
               }
            };
         } catch (NoSuchMethodException var3) {
            final Constructor var2 = Lookup.class.getDeclaredConstructor(Class.class);
            var2.setAccessible(true);
            var0 = new HandleFactory.LookupFactory(false) {
            };
         }
      } catch (SecurityException | ReflectiveOperationException var4) {
         var0 = new HandleFactory.LookupFactory(false) {
         };
      }

      lookupFactory = var0;
   }

   public interface Factory {
      Lookup lookup(Method var1, Class<?> var2) throws ReflectiveOperationException;
   }

   abstract static class LookupFactory {
      public final boolean requireLambdaFactory;

      public LookupFactory(boolean requireLambdaFactory) {
         this.requireLambdaFactory = requireLambdaFactory;
      }

   }
}
