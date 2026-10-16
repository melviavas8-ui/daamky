package dev.daamky.client.internal;

import dev.daamky.client.internal.HandleFactory.Factory;
import dev.daamky.client.internal.HandleFactory.LookupFactory;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;

public class HandleFactoryImpl2 extends LookupFactory {
   public HandleFactoryImpl2(boolean var1, Constructor var2) {
      super(var1);
      this.constructor = var2;
   }

   public Lookup lookup(Factory lambdaFactory, Class<?> klass) throws ReflectiveOperationException {
      return (Lookup)this.constructor.newInstance(klass);
   }
}
