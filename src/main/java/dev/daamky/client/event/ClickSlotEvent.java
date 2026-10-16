package dev.daamky.client.event;

import net.minecraft.screen.slot.SlotActionType;

public class ClickSlotEvent extends Event<ClickSlotEvent.ClickSlotEventData> {
   public static final ClickSlotEvent clickSlotEvent = new ClickSlotEvent();

   public static ClickSlotEvent getInstance() {
      return clickSlotEvent;
   }

   public static record ClickSlotEventData(SlotActionType slotActionType, int slot, int button, int id) {
   }
}
