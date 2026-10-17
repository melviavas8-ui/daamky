package dev.daamky.client.model.enums;

public enum Priority {
   CRITICAL(5),
   REQUIRED(4),
   HIGH(3),
   MEDIUM_HIGH(2),
   MEDIUM(1),
   NORMAL(0),
   LOW(-1),
   LOWEST(-2);

   public final int priority;

   public int getPriority() {
      return this.priority;
   }

   private Priority(int priority) {
      this.priority = priority;
   }

   public static Priority[] $values() {
      return new Priority[]{CRITICAL, REQUIRED, HIGH, MEDIUM_HIGH, MEDIUM, NORMAL, LOW, LOWEST};
   }
}
