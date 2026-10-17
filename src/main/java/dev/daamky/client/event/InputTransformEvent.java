package dev.daamky.client.event;

import net.minecraft.util.PlayerInput;

public class InputTransformEvent extends Event<InputTransformEvent> {
   public static final InputTransformEvent inputTransformEvent = new InputTransformEvent();
   public PlayerInput input;
   public float forward;
   public float strafe;

   public static InputTransformEvent getInstance() {
      return inputTransformEvent;
   }

   public void handleForward(PlayerInput input, float forward, float strafe) {
      this.input = input;
      this.forward = forward;
      this.strafe = strafe;
   }

   public PlayerInput getInput() {
      return this.input;
   }

   public float getForward() {
      return this.forward;
   }

   public float getStrafe() {
      return this.strafe;
   }

   public void setInput(PlayerInput input) {
      this.input = input;
   }

   public void setForward(float forward) {
      this.forward = forward;
   }

   public void setStrafe(float strafe) {
      this.strafe = strafe;
   }
}
