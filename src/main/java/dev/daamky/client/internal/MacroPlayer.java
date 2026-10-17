package dev.daamky.client.internal;

public final class MacroPlayer {
   public final MacroEngine.Macro macro2;
   public int currentIndex;
   public boolean finished;
   public int awaitTicks;
   public int holdKey = -1;
   public int holdTicksRemaining;

   public MacroPlayer(MacroEngine.Macro macro2) {
      this.macro2 = macro2;
      this.currentIndex = 0;
      this.finished = false;
   }

   public boolean checkSize() {
      return this.finished || this.currentIndex >= this.macro2.getSteps().size();
   }

   public void getCurrentIndex() {
      this.currentIndex++;
   }

   public void runFinished() {
      this.finished = true;
   }

   public void setAwaitTicks(int ticks) {
      this.awaitTicks = Math.max(ticks, 1);
   }

   public void handleHold(int keyCode, int ticks) {
      this.holdKey = keyCode;
      this.holdTicksRemaining = ticks;
   }

   public void runHold() {
      if (this.awaitTicks > 0) {
         this.awaitTicks--;
      }

      if (this.holdTicksRemaining > 0) {
         this.holdTicksRemaining--;
         if (this.holdTicksRemaining <= 0) {
            this.holdKey = -1;
         }
      }
   }

   public boolean checkAwait() {
      return this.awaitTicks > 0 || this.holdKey != -1;
   }

   public MacroEngine.Macro getMacro2() {
      return this.macro2;
   }

   public int getAwaitTicks() {
      return this.awaitTicks;
   }

   public int getHoldKey() {
      return this.holdKey;
   }

   public int getHoldTicksRemaining() {
      return this.holdTicksRemaining;
   }
}
