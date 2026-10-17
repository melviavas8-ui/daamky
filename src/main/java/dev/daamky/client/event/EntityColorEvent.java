package dev.daamky.client.event;

public class EntityColorEvent extends Event<EntityColorEvent.EntityColorEventData> {
   public static final EntityColorEvent entityColorEvent = new EntityColorEvent();

   public static EntityColorEvent getInstance() {
      return entityColorEvent;
   }

   public static class EntityColorEventData {
      public int color;

      public int getColor() {
         return this.color;
      }

      public EntityColorEvent.EntityColorEventData withColor(int color) {
         this.color = color;
         return this;
      }

      public EntityColorEventData(int color) {
         this.color = color;
      }
   }
}
