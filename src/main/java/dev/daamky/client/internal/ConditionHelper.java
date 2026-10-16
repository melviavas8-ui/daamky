package dev.daamky.client.internal;

import dev.daamky.client.module.Module;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Item;

public final class ConditionHelper implements Callback5, MinecraftAccess {
   @Override
   public String getLabel() {
      return MacroEngine.ActionType.ENABLE_MODULE.getLabel();
   }

   @Override
   public boolean computeFlag(MacroEngine.MacroStep step, MacroPlayer ctx) {
      return switch (step.getType()) {
         case ENABLE_MODULE -> this.computeFlag(step);
         case IF_CONDITION -> this.checkNext(step, ctx);
         case EXECUTE_MACROS -> this.computeFlag2(step);
         default -> true;
      };
   }

   public boolean computeFlag(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("module");
      String var3 = step.computeParams("state");
      if (var2 != null && !var2.isEmpty()) {
         Module var4 = this.findFeatureByName(var2);
         if (var4 == null) {
            return true;
         } else {
            String var6 = var3.toLowerCase();

            boolean var5 = switch (var6) {
               case "on", "enable", "true" -> true;
               case "off", "disable", "false" -> false;
               default -> !var4.isEnabled();
            };
            var4.setEnabled(var5);
            return true;
         }
      } else {
         return true;
      }
   }

   public boolean checkNext(MacroEngine.MacroStep step, MacroPlayer ctx) {
      String var3 = step.computeParams("condition");
      String var4 = step.computeParams("value");
      if (var3 == null) {
         return true;
      } else {
         MacroEngine.ConditionType var5 = MacroEngine.ConditionType.fromLabel(var3);
         if (var5 == null) {
            return true;
         } else {
            boolean var6 = this.checkCondition(var5, var4);
            if (var6 && !step.getChildren().isEmpty()) {
               MacroStore var7 = MacroStore.getInstance();

               for (MacroEngine.MacroStep var9 : step.getChildren()) {
                  if (ctx.checkAwait()) {
                     break;
                  }

                  if (!var7.computeType(var9, ctx)) {
                     ctx.runFinished();
                     break;
                  }
               }
            }

            return true;
         }
      }
   }

   public boolean computeFlag2(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("macro");
      if (var2 != null && !var2.isEmpty()) {
         MacroEngine.Macro var3 = MacroStore.getInstance().getMacro(var2);
         if (var3 != null) {
            MacroStore.getInstance().initRunning(var3);
         }

         return true;
      } else {
         return true;
      }
   }

   public boolean checkCondition(MacroEngine.ConditionType type, String value) {
      if (mc.player == null) {
         return false;
      } else {
         return switch (type) {
            case HP -> {
               float var10 = mc.player.getHealth();
               float var11 = this.parseParse(value, 10.0F);
               yield var10 <= var11;
            }
            case ON_GROUND -> mc.player.isOnGround();
            case FALL_DISTANCE_GTE -> {
               float var9 = mc.player.fallDistance;
               float var4 = this.parseParse(value, 3.0F);
               yield var9 >= var4;
            }
            case HAS_ITEM -> {
               Item var8 = SlotHelper.computeCase(value);
               yield var8 != null && InventoryUtil.computeInt(var8, false) != -1;
            }
            case HAS_ITEM_HOTBAR -> {
               Item var7 = SlotHelper.computeCase(value);
               yield var7 != null && InventoryUtil.computeInt(var7, true) != -1;
            }
            case PLAYER_IN_RANGE_LTE -> {
               float var6 = this.parseParse(value, 10.0F);
               yield mc.world.getPlayers().stream().filter(ConditionHelper::lambda$checkCondition$0).anyMatch(ConditionHelper::lambda$checkCondition$1);
            }
            case HELD_ITEM_IS -> {
               Item var5 = SlotHelper.computeCase(value);
               yield var5 != null && mc.player.getMainHandStack().isOf(var5);
            }
            case OFFHAND_ITEM_IS -> {
               Item var3 = SlotHelper.computeCase(value);
               yield var3 != null && mc.player.getOffHandStack().isOf(var3);
            }
         };
      }
   }

   public Module findFeatureByName(String name) {
      return name == null ? null : ModuleRegistry.getInstance().getList().stream().filter(ConditionHelper::lambda$findFeatureByName$2).findFirst().orElse(null);
   }

   public float parseParse(String value, float def) {
      if (value != null && !value.isEmpty()) {
         try {
            return Float.parseFloat(value);
         } catch (NumberFormatException var4) {
            return def;
         }
      } else {
         return def;
      }
   }

   public static boolean lambda$findFeatureByName$2(String name, Module f) {
      return f.getName().equalsIgnoreCase(name);
   }

   public static boolean lambda$checkCondition$1(float range, AbstractClientPlayerEntity p) {
      return mc.player.distanceTo(p) <= range;
   }

   public static boolean lambda$checkCondition$0(AbstractClientPlayerEntity p) {
      return p != mc.player && p.isAlive();
   }
}
