package dev.daamky.client.internal;

import dev.daamky.client.internal.HandleFactory.Factory;
import dev.daamky.client.internal.HandleFactory.LookupFactory;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;

public class HandleFactoryImpl extends LookupFactory {
   public HandleFactoryImpl(boolean var1, Method var2) {
      super(var1);
      this.method = var2;
   }

   public Lookup lookup(Factory lambdaFactory, Class<?> klass) throws ReflectiveOperationException {
      return lambdaFactory.lookup(this.method, klass);
   }
}
