package dev.daamky.client.internal;

import dev.daamky.client.event.ClickSlotEvent;
import dev.daamky.client.event.InteractBlockEvent;
import dev.daamky.client.event.InteractEvent;
import net.minecraft.block.Block;
import net.minecraft.screen.slot.SlotActionType;

public class ClickSlotEventDataHelper {
   public static boolean initCancelled(SlotActionType actionType, int slotId, int button, int syncId) {
      try {
         ClickSlotEvent.ClickSlotEventData var4 = new ClickSlotEvent.ClickSlotEventData(actionType, slotId, button, syncId);
         return ClickSlotEvent.getInstance().postAndCheckCancelled(var4);
      } catch (Throwable var5) {
         return false;
      }
   }

   public static boolean initCancelled(Block block) {
      try {
         InteractBlockEvent.InteractBlockEventData var1 = new InteractBlockEvent.InteractBlockEventData(block);
         return InteractBlockEvent.getInstance().postAndCheckCancelled(var1);
      } catch (Throwable var2) {
         return false;
      }
   }

   public static boolean checkCancelled() {
      try {
         return InteractEvent.getInstance().postAndCheckCancelled();
      } catch (Throwable var1) {
         return false;
      }
   }
}
