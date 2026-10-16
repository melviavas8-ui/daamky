package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;

public abstract class QuintOutHelper {
   public final AnimatedValue animatedValue = new AnimatedValue();
   public boolean open = false;

   public void runOpen() {
      this.animatedValue.update();
      this.animatedValue.animateTo(this.open ? 1.0 : 0.0, this.open ? 180L : 140L, this.open ? Easing.QUINT_OUT : Easing.CUBIC_OUT);
   }

   public void runOpen2() {
      this.open = !this.open;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean open) {
      this.open = openx;
   }
}
