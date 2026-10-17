package dev.daamky.client.event;

import dev.daamky.client.internal.InputState;
import net.minecraft.util.PlayerInput;

public class MovementInputEvent extends Event<MovementInputEvent.MovementInputEventData> {
   public static final MovementInputEvent movementInputEvent = new MovementInputEvent();

   public static MovementInputEvent getInstance() {
      return movementInputEvent;
   }

   public static class MovementInputEventData {
      public final PlayerInput playerInput;
      public boolean jump;
      public boolean sneak;
      public InputState directionalInput;

      public PlayerInput getPlayerInput() {
         return this.playerInput;
      }

      public boolean isJump() {
         return this.jump;
      }

      public boolean isSneak() {
         return this.sneak;
      }

      public InputState getDirectionalInput() {
         return this.directionalInput;
      }

      public MovementInputEventData(PlayerInput playerInput, boolean jump, boolean sneak, InputState directionalInput) {
         this.playerInput = playerInput;
         this.jump = jump;
         this.sneak = sneak;
         this.directionalInput = directionalInput;
      }

      public void setJump(boolean jump) {
         this.jump = jump;
      }

      public void setSneak(boolean sneak) {
         this.sneak = sneak;
      }
   }
}
