package dev.daamky.client.event;

public class BlockCollisionEvent extends Event<BlockCollisionEvent> {
   public static final BlockCollisionEvent blockCollisionEvent = new BlockCollisionEvent();

   public static BlockCollisionEvent getInstance() {
      return blockCollisionEvent;
   }
}
