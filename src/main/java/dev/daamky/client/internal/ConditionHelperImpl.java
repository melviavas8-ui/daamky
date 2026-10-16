package dev.daamky.client.internal;

import dev.daamky.client.internal.MacroEngine.ActionType;
import dev.daamky.client.internal.MacroEngine.ConditionType;

// $VF: synthetic class
public class ConditionHelperImpl {
   static {
      try {
         miluw[ConditionType.HP.ordinal()] = 1;
      } catch (NoSuchFieldError var11) {
      }

      try {
         miluw[ConditionType.ON_GROUND.ordinal()] = 2;
      } catch (NoSuchFieldError var10) {
      }

      try {
         miluw[ConditionType.FALL_DISTANCE_GTE.ordinal()] = 3;
      } catch (NoSuchFieldError var9) {
      }

      try {
         miluw[ConditionType.HAS_ITEM.ordinal()] = 4;
      } catch (NoSuchFieldError var8) {
      }

      try {
         miluw[ConditionType.HAS_ITEM_HOTBAR.ordinal()] = 5;
      } catch (NoSuchFieldError var7) {
      }

      try {
         miluw[ConditionType.PLAYER_IN_RANGE_LTE.ordinal()] = 6;
      } catch (NoSuchFieldError var6) {
      }

      try {
         miluw[ConditionType.HELD_ITEM_IS.ordinal()] = 7;
      } catch (NoSuchFieldError var5) {
      }

      try {
         miluw[ConditionType.OFFHAND_ITEM_IS.ordinal()] = 8;
      } catch (NoSuchFieldError var4) {
      }

      ints = new int[ActionType.values().length];

      try {
         ints[ActionType.ENABLE_MODULE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         ints[ActionType.IF_CONDITION.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         ints[ActionType.EXECUTE_MACROS.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
