package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.util.Map.Entry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class SlotHelper implements Callback5, MinecraftAccess {
   @Override
   public String getLabel() {
      return MacroEngine.ActionType.SWAP_SLOT.getLabel();
   }

   @Override
   public boolean computeFlag(MacroEngine.MacroStep step, MacroPlayer ctx) {
      return switch (step.getType()) {
         case SWAP_SLOT -> this.computeSlot(step);
         case SWAP_SLOT_TO_ITEM -> this.computeItem(step);
         case MOVE_ITEM_TO_OFFHAND -> this.computeItem2(step);
         case MOVE_ITEM_TO_HOTBAR -> this.computeItem3(step);
         case AWAIT -> this.computeAwait(step, ctx);
         case CLICK_ITEM -> this.computeCase(step);
         case SWAP_BETWEEN -> this.computeItem4(step);
         default -> true;
      };
   }

   public boolean computeSlot(MacroEngine.MacroStep step) {
      int var2 = step.computeParams("slot", 0);
      var2 = MathHelper.clamp(var2, 0, 8);
      InventoryUtil.handleInt(var2);
      return true;
   }

   public boolean computeItem(MacroEngine.MacroStep step) {
      String var3 = step.computeParams("item");
      if (var3 != null && !var3.isEmpty()) {
         Item var4 = computeCase(var3);
         if (var4 == null) {
            return true;
         } else {
            int var5 = InventoryUtil.computeInt(var4, true);
            int var2;
            if (var5 == -1 && (var5 = InventoryUtil.computeInt(var4)) != -1 && var5 >= 36 && (var2 = InventoryUtil.getInt()) != -1) {
               InventoryUtil.initInit(var5 - 36, var2);
               var5 = var2;
            }

            if (var5 >= 0 && var5 <= 8) {
               InventoryUtil.handleInt(var5);
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public boolean computeItem2(MacroEngine.MacroStep step) {
      String var3 = step.computeParams("item");
      Item var4 = var3 != null && !var3.isEmpty() ? computeCase(var3) : null;
      int var2;
      if (var4 != null) {
         var2 = InventoryUtil.computeInt(var4, false);
         if (var2 == -1) {
            var2 = InventoryUtil.computeInt(var4, true);
         }
      } else {
         var2 = InventoryUtil.computeInt2(true);
      }

      if (var2 != -1) {
         if (var2 < 36) {
            var2 += 36;
         }

         InventoryUtil.initInit2(var2);
      }

      return true;
   }

   public boolean computeItem3(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("item");
      int var3 = step.computeParams("slot", 7);
      var3 = MathHelper.clamp(var3, 0, 8);
      if (var2 != null && !var2.isEmpty()) {
         Item var4 = computeCase(var2);
         if (var4 == null) {
            return true;
         } else {
            int var5 = InventoryUtil.computeInt(var4, false);
            if (var5 == -1) {
               return true;
            } else {
               if (var5 < 36) {
                  var5 += 36;
               }

               InventoryUtil.initInit(var5, var3);
               return true;
            }
         }
      } else {
         return true;
      }
   }

   public boolean computeAwait(MacroEngine.MacroStep step, MacroPlayer ctx) {
      int var3 = step.computeParams("ticks", 10);
      ctx.setAwaitTicks(var3);
      return false;
   }

   public boolean computeCase(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("hand");
      boolean var3 = "off".equalsIgnoreCase(var2) || "offhand".equalsIgnoreCase(var2);
      InventoryUtil.useItem(var3 ? Hand.OFF_HAND : Hand.MAIN_HAND);
      return true;
   }

   public boolean computeItem4(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("item1");
      String var3 = step.computeParams("item2");
      if (var2 != null && var3 != null) {
         Item var4 = computeCase(var2);
         Item var5 = computeCase(var3);
         if (var4 != null && var5 != null) {
            int var6 = InventoryUtil.computeInt(var4, true);
            int var7 = InventoryUtil.computeInt(var5, true);
            if (var6 == -1 && (var6 = InventoryUtil.computeInt(var4, false)) != -1 && var6 >= 36) {
               var6 -= 36;
            }

            if (var7 == -1 && (var7 = InventoryUtil.computeInt(var5, false)) != -1 && var7 >= 36) {
               var7 -= 36;
            }

            if (var6 != -1 && var7 != -1 && var6 != var7 && var6 >= 0 && var6 <= 8 && var7 >= 0 && var7 <= 8) {
               InventoryUtil.initInit(var6, var7);
            }

            return true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public static Item computeCase(String name) {
      if (name != null && !name.isEmpty()) {
         String var1 = name.contains(":") ? name : "minecraft:" + name;
         Identifier var2 = Identifier.of(var1);
         if (Registries.ITEM.containsId(var2)) {
            return (Item)Registries.ITEM.get(var2);
         } else {
            for (Entry var4 : Registries.ITEM.getEntrySet()) {
               String var5 = ((RegistryKey)var4.getKey()).getValue().getPath();
               if (var5.equalsIgnoreCase(name)) {
                  return (Item)var4.getValue();
               }

               if (var5.replace("_", "").equalsIgnoreCase(name.replace("_", ""))) {
                  return (Item)var4.getValue();
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }
}
