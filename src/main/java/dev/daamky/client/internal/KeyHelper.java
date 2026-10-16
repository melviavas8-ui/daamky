package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.lang.reflect.Field;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;

public final class KeyHelper implements Callback5, MinecraftAccess {
   @Override
   public String getLabel() {
      return MacroEngine.ActionType.HOLD_INPUT.getLabel();
   }

   @Override
   public boolean computeFlag(MacroEngine.MacroStep step, MacroPlayer ctx) {
      return switch (step.getType()) {
         case HOLD_INPUT -> this.computeKey(step, ctx);
         case USE_COMMAND -> this.computeCommand(step);
         case SEND_MESSAGE -> this.computeFlag(step);
         case NOTIFY -> this.computeFlag2(step);
         case BREAK -> this.computeFlag2(step, ctx);
         default -> true;
      };
   }

   public boolean computeKey(MacroEngine.MacroStep step, MacroPlayer ctx) {
      String var3 = step.computeParams("key");
      int var4 = step.computeParams("ticks", 5);
      int var5 = computeCase(var3);
      if (var5 == -1) {
         return true;
      } else {
         ctx.handleHold(var5, var4);
         if (mc.player != null) {
            this.handleInt(var5, true);
         }

         return false;
      }
   }

   public boolean computeCommand(MacroEngine.MacroStep step) {
      ClientPlayerEntity var2 = mc.player;
      if (var2 == null) {
         return true;
      } else {
         String var3 = step.computeParams("command");
         if (var3 != null && !var3.isEmpty()) {
            String var4 = var3.startsWith("/") ? var3.substring(1) : var3;
            var2.networkHandler.sendChatCommand(var4);
            return true;
         } else {
            return true;
         }
      }
   }

   public boolean computeFlag(MacroEngine.MacroStep step) {
      ClientPlayerEntity var2 = mc.player;
      if (var2 == null) {
         return true;
      } else {
         String var3 = step.computeParams("message");
         if (var3 != null && !var3.isEmpty()) {
            var2.networkHandler.sendChatMessage(var3);
            return true;
         } else {
            return true;
         }
      }
   }

   public boolean computeFlag2(MacroEngine.MacroStep step) {
      if (mc.player == null) {
         return true;
      } else {
         String var2 = step.computeParams("message");
         if (var2 != null && !var2.isEmpty()) {
            TextShaper.handleString("[Macro] " + var2);
            return true;
         } else {
            return true;
         }
      }
   }

   public boolean computeFlag2(MacroEngine.MacroStep step, MacroPlayer ctx) {
      ctx.runFinished();
      return false;
   }

   public void handleInt(int keyCode, boolean pressed) {
      if (mc.options != null) {
         Key var3;
         if (keyCode <= -93) {
            int var4 = keyCode == -100 ? 0 : (keyCode == -99 ? 1 : 2);
            var3 = Type.MOUSE.createFromCode(var4);
         } else {
            var3 = Type.KEYSYM.createFromCode(keyCode);
         }

         KeyBinding.setKeyPressed(var3, pressed);
      }
   }

   public static int computeCase(String keyName) {
      if (keyName == null || keyName.isEmpty()) {
         return -1;
      } else if (keyName.equalsIgnoreCase("attack") || keyName.equalsIgnoreCase("left_click")) {
         return -100;
      } else if (!keyName.equalsIgnoreCase("use") && !keyName.equalsIgnoreCase("right_click")) {
         try {
            for (Field var4 : GLFW.class.getDeclaredFields()) {
               String var5;
               if (var4.getName().startsWith("GLFW_KEY_")
                  && var4.getType() == intVal.classVal
                  && (
                     (var5 = var4.getName().substring("GLFW_KEY_".length())).equalsIgnoreCase(keyName)
                        || var5.replace("_", "").equalsIgnoreCase(keyName.replace("_", ""))
                  )) {
                  return var4.getInt(null);
               }
            }
         } catch (Exception var6) {
         }

         return -1;
      } else {
         return -99;
      }
   }
}
