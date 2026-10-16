package dev.daamky.client.internal;

import dev.daamky.client.internal.MacroEngine.ActionType;

// $VF: synthetic class
public class SlotHelperImpl {
   static {
      try {
         ints[ActionType.SWAP_SLOT.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         ints[ActionType.SWAP_SLOT_TO_ITEM.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         ints[ActionType.MOVE_ITEM_TO_OFFHAND.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      try {
         ints[ActionType.MOVE_ITEM_TO_HOTBAR.ordinal()] = 4;
      } catch (NoSuchFieldError var4) {
      }

      try {
         ints[ActionType.AWAIT.ordinal()] = 5;
      } catch (NoSuchFieldError var3) {
      }

      try {
         ints[ActionType.CLICK_ITEM.ordinal()] = 6;
      } catch (NoSuchFieldError var2) {
      }

      try {
         ints[ActionType.SWAP_BETWEEN.ordinal()] = 7;
      } catch (NoSuchFieldError var1) {
      }
   }
}
