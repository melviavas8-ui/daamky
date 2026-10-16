package dev.daamky.client.event;

import net.minecraft.entity.Entity;

public class AttackEvent extends Event<AttackEvent.AttackEventData> {
   public static final AttackEvent attackEvent = new AttackEvent();

   public static AttackEvent getInstance() {
      return attackEvent;
   }

   public static record AttackEventData(Entity entity) {
   }
}
