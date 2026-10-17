package dev.daamky.client.event;

import dev.daamky.client.internal.InputState;

public class SprintEvent extends Event<SprintEvent.SprintEventData> {
   public static final SprintEvent sprintEvent = new SprintEvent();
   public static final SprintEvent.SprintEventData sprintEventData = new SprintEvent.SprintEventData(new InputState(false, false, false, false));

   public boolean computeCancelled(SprintEvent.SprintEventData any) {
      super.postAndCheckCancelled(any);
      return any.isSprint();
   }

   public static SprintEvent getInstance() {
      return sprintEvent;
   }

   public static SprintEvent.SprintEventData getSprintEventData() {
      return sprintEventData;
   }

   @Override
   public boolean postAndCheckCancelled(SprintEvent.SprintEventData param1) {
      return this.computeCancelled(param1);
   }

   public static class SprintEventData {
      public boolean sprint = false;
      public final InputState directionalInput;

      public void setSprint(boolean sprint) {
         this.sprint = sprint;
      }

      public boolean isSprint() {
         return this.sprint;
      }

      public InputState getDirectionalInput() {
         return this.directionalInput;
      }

      public SprintEventData(InputState directionalInput) {
         this.directionalInput = directionalInput;
      }
   }
}
