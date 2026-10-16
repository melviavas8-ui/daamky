package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.util.MinecraftAccess;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

public final class BlockHelper implements Callback5, MinecraftAccess {
   public final Map<String, Rotation> map = new ConcurrentHashMap<>();

   @Override
   public String getLabel() {
      return MacroEngine.ActionType.ROTATE_BLOCK.getLabel();
   }

   @Override
   public boolean computeFlag(MacroEngine.MacroStep step, MacroPlayer ctx) {
      return switch (step.getType()) {
         case ROTATE_BLOCK -> this.computeBlock(step);
         case ROTATE_ENTITY -> this.computeFlag(step);
         case ROTATE_OPERATION -> this.computeFlag2(step);
         case ROTATE_RELATIVE -> this.initInit(step);
         case ROTATE_SET -> this.initInit2(step);
         default -> true;
      };
   }

   public boolean computeBlock(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("block");
      if (var2 != null && !var2.isEmpty() && mc.player != null) {
         TextShaper.handleString("[Macro] Rotate to block: " + var2);
         return true;
      } else {
         return true;
      }
   }

   public boolean computeFlag(MacroEngine.MacroStep step) {
      Object var2 = null;
      String var3 = step.computeParams("target");
      if (mc.player != null && mc.world != null) {
         String var4 = var3 != null ? var3 : "nearest";
         String var5 = var4.toLowerCase();

         var2 = switch (var5) {
            case "friend", "nearest_friend" -> this.computeNext(true);
            default -> this.computeNext(false);
         };
         if (var2 != null) {
            VecRotationHelper.getInstance().handleModule((Rotation)var2, RotationHelper.rotationHelper, Priority.NORMAL, null);
         }

         return true;
      } else {
         return true;
      }
   }

   public boolean computeFlag2(MacroEngine.MacroStep step) {
      String var2 = step.computeParams("operation");
      String var3 = step.computeParams("name");
      if (var2 != null && mc.player != null) {
         String var4 = var2.toLowerCase();

         return switch (var4) {
            case "save" -> {
               if (var3 != null) {
                  this.map.put(var3, new Rotation(mc.player.getYaw(), mc.player.getPitch()));
               }

               yield true;
            }
            case "load" -> {
               Rotation var6 = this.map.get(var3);
               if (var6 != null) {
                  VecRotationHelper.getInstance().handleModule(var6, RotationHelper.rotationHelper, Priority.NORMAL, null);
               }

               yield true;
            }
            case "reset" -> {
               this.map.clear();
               yield true;
            }
            default -> true;
         };
      } else {
         return true;
      }
   }

   public boolean initInit(MacroEngine.MacroStep step) {
      float var2 = step.computeParams("yaw", 0.0F);
      float var3 = step.computeParams("pitch", 0.0F);
      if (mc.player == null) {
         return true;
      } else {
         float var4 = mc.player.getYaw() + var2;
         float var5 = MathHelper.clamp(mc.player.getPitch() + var3, -90.0F, 90.0F);
         VecRotationHelper.getInstance().handleModule(new Rotation(var4, var5), RotationHelper.rotationHelper, Priority.NORMAL, null);
         return true;
      }
   }

   public boolean initInit2(MacroEngine.MacroStep step) {
      float var2 = step.computeParams("yaw", 0.0F);
      float var3 = step.computeParams("pitch", 0.0F);
      VecRotationHelper.getInstance()
         .handleModule(new Rotation(var2, MathHelper.clamp(var3, -90.0F, 90.0F)), RotationHelper.rotationHelper, Priority.NORMAL, null);
      return true;
   }

   public Rotation computeNext(boolean friendsOnly) {
      if (mc.player != null && mc.world != null) {
         List var2 = mc.world.getPlayers();
         AbstractClientPlayerEntity var3 = null;
         double var4 = Double.MAX_VALUE;

         for (AbstractClientPlayerEntity var7 : var2) {
            double var8;
            if (var7 != mc.player && var7.isAlive() && (var8 = (double)mc.player.distanceTo(var7)) < var4) {
               var4 = var8;
               var3 = var7;
            }
         }

         return var3 != null ? RotationMath.initAtan(var3.getPos()) : null;
      } else {
         return null;
      }
   }
}
