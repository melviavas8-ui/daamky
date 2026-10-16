package dev.daamky.client.internal;

public class Klass extends RuntimeException {
   public Klass(Class<?> klass) {
      super("No registered lambda listener for '" + klass.getName() + "'.");
   }
}
