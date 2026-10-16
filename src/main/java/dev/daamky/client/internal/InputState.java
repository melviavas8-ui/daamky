package dev.daamky.client.internal;

import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;

public final class InputState {
   public boolean forwards2;
   public boolean backwards;
   public boolean left;
   public boolean right;
   public static final InputState mqipo = new InputState(false, false, false, false);
   public static final InputState inputState = new InputState(true, false, false, false);
   public static final InputState maefowq = new InputState(false, true, false, false);
   public static final InputState inputState2 = new InputState(false, false, true, false);
   public static final InputState inputState3 = new InputState(false, false, false, true);

   public InputState(boolean forwards2, boolean backwards, boolean left, boolean right) {
      this.forwards2 = forwards2x;
      this.backwards = backwardsx;
      this.left = leftx;
      this.right = rightx;
   }

   public InputState(Input input) {
      this(((Callback9)input).evelina$getUntransformed());
   }

   public void handleComp(Input input) {
      PlayerInput var2 = ((Callback9)input).evelina$getUntransformed();
      this.forwards2 = var2.forward();
      this.backwards = var2.backward();
      this.left = var2.left();
      this.right = var2.right();
   }

   public InputState(PlayerInput input) {
      this(input.forward(), input.backward(), input.left(), input.right());
   }

   public InputState(float movementForward, float movementSideways) {
      this(movementForward > 0.0F, movementForward < 0.0F, movementSideways > 0.0F, movementSideways < 0.0F);
   }

   public boolean computeLeft(Object other) {
      if (this == other) {
         return true;
      } else {
         return !(other instanceof InputState var2)
            ? false
            : this.forwards2 == var2.forwards2 && this.backwards == var2.backwards && this.left == var2.left && this.right == var2.right;
      }
   }

   public int getCode() {
      int var1 = Boolean.hashCode(this.forwards2);
      var1 = 31 * var1 + Boolean.hashCode(this.backwards);
      var1 = 31 * var1 + Boolean.hashCode(this.left);
      return 31 * var1 + Boolean.hashCode(this.right);
   }

   public boolean checkLeft() {
      return this.forwards2 || this.backwards || this.left || this.right;
   }

   public boolean isForwards() {
      return this.forwards2;
   }

   public boolean isBackwards() {
      return this.backwards;
   }

   public boolean isLeft() {
      return this.left;
   }

   public boolean isRight() {
      return this.right;
   }

   public void setForwards(boolean forwards2) {
      this.forwards2 = forwards2x;
   }

   public void setBackwards(boolean backwards) {
      this.backwards = backwardsx;
   }

   public void setLeft(boolean left) {
      this.left = leftx;
   }

   public void setRight(boolean right) {
      this.right = rightx;
   }
}
