package dev.daamky.client.internal;

import dev.daamky.client.internal.HandleFactory.Factory;
import dev.daamky.client.internal.HandleFactory.LookupFactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class HandleFactoryImpl3 extends LookupFactory {
   public HandleFactoryImpl3(boolean requireLambdaFactory) {
      super(requireLambdaFactory);
   }

   public Lookup lookup(Factory lambdaFactory, Class<?> klass) {
      return MethodHandles.publicLookup().in(klass);
   }
}
