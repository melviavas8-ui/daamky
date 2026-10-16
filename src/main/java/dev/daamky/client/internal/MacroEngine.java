package dev.daamky.client.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class MacroEngine {
   public static enum ActionType {
      HOLD_INPUT("HoldInput"),
      USE_COMMAND("UseCommand"),
      SEND_MESSAGE("SendMessage"),
      NOTIFY("Notify"),
      BREAK("Break"),
      SWAP_SLOT("SwapSlot"),
      SWAP_SLOT_TO_ITEM("SwapSlotToItem"),
      MOVE_ITEM_TO_OFFHAND("MoveItemToOffHand"),
      MOVE_ITEM_TO_HOTBAR("MoveItemToHotBar"),
      AWAIT("Await"),
      CLICK_ITEM("ClickItem"),
      SWAP_BETWEEN("SwapBetween"),
      ENABLE_MODULE("EnableModule"),
      IF_CONDITION("IfCondition"),
      EXECUTE_MACROS("ExecuteMacros"),
      ROTATE_BLOCK("RotateBlock"),
      ROTATE_ENTITY("RotateEntity"),
      ROTATE_OPERATION("RotateOperation"),
      ROTATE_RELATIVE("RotateRelative"),
      ROTATE_SET("RotateSet");

      public final String label;

      private ActionType(String label) {
         this.label = labelx;
      }

      public static MacroEngine.ActionType fromLabel(String label) {
         for (MacroEngine.ActionType var4 : values()) {
            if (var4.label.equalsIgnoreCase(labelx)) {
               return var4;
            }
         }

         return null;
      }

      public String getLabel() {
         return this.label;
      }

      public static MacroEngine.ActionType[] $values() {
         return new MacroEngine.ActionType[]{
            HOLD_INPUT,
            USE_COMMAND,
            SEND_MESSAGE,
            NOTIFY,
            BREAK,
            SWAP_SLOT,
            SWAP_SLOT_TO_ITEM,
            MOVE_ITEM_TO_OFFHAND,
            MOVE_ITEM_TO_HOTBAR,
            AWAIT,
            CLICK_ITEM,
            SWAP_BETWEEN,
            ENABLE_MODULE,
            IF_CONDITION,
            EXECUTE_MACROS,
            ROTATE_BLOCK,
            ROTATE_ENTITY,
            ROTATE_OPERATION,
            ROTATE_RELATIVE,
            ROTATE_SET
         };
      }
   }

   public static enum ConditionType {
      HP("hp"),
      ON_GROUND("on_ground"),
      FALL_DISTANCE_GTE("fall_distance_gte"),
      HAS_ITEM("has_item"),
      HAS_ITEM_HOTBAR("has_item_hotbar"),
      PLAYER_IN_RANGE_LTE("player_in_range_lte"),
      HELD_ITEM_IS("held_item_is"),
      OFFHAND_ITEM_IS("offhand_item_is");

      public final String label;

      private ConditionType(String label) {
         this.label = labelx;
      }

      public static MacroEngine.ConditionType[] $values() {
         return new MacroEngine.ConditionType[]{
            HP, ON_GROUND, FALL_DISTANCE_GTE, HAS_ITEM, HAS_ITEM_HOTBAR, PLAYER_IN_RANGE_LTE, HELD_ITEM_IS, OFFHAND_ITEM_IS
         };
      }
   }

   public static final class Macro {
      public String name;
      public int key;
      public final List<MacroEngine.MacroStep> steps = new ArrayList<>();

      public Macro() {
      }

      public Macro(String name, int key) {
         this.name = namex;
         this.key = keyx;
      }

      public JsonObject initAdd() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("name", this.name);
         var1.addProperty("key", this.key);
         JsonArray var2 = new JsonArray();

         for (MacroEngine.MacroStep var4 : this.steps) {
            var2.add(var4.initAdd());
         }

         var1.add("steps", var2);
         return var1;
      }

      public static MacroEngine.Macro initJson(JsonObject obj) {
         MacroEngine.Macro var1 = new MacroEngine.Macro();
         var1.name = obj.get("name").getAsString();
         var1.key = obj.get("key").getAsInt();
         if (obj.has("steps")) {
            for (JsonElement var4 : obj.getAsJsonArray("steps")) {
               var1.steps.add(MacroEngine.MacroStep.withType(var4.getAsJsonObject()));
            }
         }

         return var1;
      }

      public String getName() {
         return this.name;
      }

      public int getKey() {
         return this.key;
      }

      public List<MacroEngine.MacroStep> getSteps() {
         return this.steps;
      }

      public void setName(String name) {
         this.name = namex;
      }

      public void setKey(int key) {
         this.key = keyx;
      }
   }

   public static final class MacroStep {
      public MacroEngine.ActionType type;
      public final JsonObject params = new JsonObject();
      public final List<MacroEngine.MacroStep> children = new ArrayList<>();

      public MacroStep() {
      }

      public MacroStep(MacroEngine.ActionType type) {
         this.type = typex;
      }

      public String computeParams(String key) {
         JsonElement var2 = this.params.get(key);
         return var2 != null ? var2.getAsString() : "";
      }

      public int computeParams(String key, int def) {
         JsonElement var3 = this.params.get(key);
         return var3 != null ? var3.getAsInt() : def;
      }

      public float computeParams(String key, float def) {
         JsonElement var3 = this.params.get(key);
         return var3 != null ? var3.getAsFloat() : def;
      }

      public boolean computeParams(String key, boolean def) {
         JsonElement var3 = this.params.get(key);
         return var3 != null ? var3.getAsBoolean() : def;
      }

      public void handleAdd(String key, String value) {
         this.params.addProperty(key, value);
      }

      public void handleAdd(String key, int value) {
         this.params.addProperty(key, value);
      }

      public void handleAdd(String key, float value) {
         this.params.addProperty(key, value);
      }

      public void handleAdd(String key, boolean value) {
         this.params.addProperty(key, value);
      }

      public static MacroEngine.MacroStep withType(JsonObject obj) {
         MacroEngine.MacroStep var1 = new MacroEngine.MacroStep();
         var1.type = MacroEngine.ActionType.fromLabel(obj.get("type").getAsString());
         if (obj.has("params")) {
            JsonObject var2 = obj.getAsJsonObject("params");

            for (String var4 : var2.keySet()) {
               var1.params.add(var4, var2.get(var4));
            }
         }

         if (obj.has("children")) {
            for (JsonElement var7 : obj.getAsJsonArray("children")) {
               var1.children.add(withType(var7.getAsJsonObject()));
            }
         }

         return var1;
      }

      public MacroEngine.ActionType getType() {
         return this.type;
      }

      public JsonObject getParams() {
         return this.params;
      }

      public List<MacroEngine.MacroStep> getChildren() {
         return this.children;
      }

      public void setType(MacroEngine.ActionType type) {
         this.type = typex;
      }
   }
}
