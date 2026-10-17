package dev.daamky.client.event;

import net.minecraft.block.Block;

public class InteractBlockEvent extends Event<InteractBlockEvent.InteractBlockEventData> {
   public static final InteractBlockEvent interactBlockEvent = new InteractBlockEvent();

   public static InteractBlockEvent getInstance() {
      return interactBlockEvent;
   }

   public static class InteractBlockEventData {
      public final Block block;
      public boolean cancelled;

      public InteractBlockEventData(Block block) {
         this.block = block;
         this.cancelled = false;
      }

      public Block getBlock() {
         return this.block;
      }

      public boolean isCancelled() {
         return this.cancelled;
      }

      public void setCancelled(boolean cancelled) {
         this.cancelled = cancelled;
      }
   }
}
