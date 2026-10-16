package dev.daamky.client.internal;

public class GetClassHelper {
   public String getCase() {
      return this.getClass().getSimpleName().toLowerCase();
   }

   public void run() {
      FactoryHelper.getMudocs().computeNext(this);
   }
}
