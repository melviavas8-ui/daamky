package dev.daamky.client.model.enums;

public enum AnimationDirection {
   FORWARDS,
   BACKWARDS;

   public AnimationDirection opposite() {
      return this == FORWARDS ? BACKWARDS : FORWARDS;
   }

   public static AnimationDirection[] $values() {
      return new AnimationDirection[]{FORWARDS, BACKWARDS};
   }
}
