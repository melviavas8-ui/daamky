package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.hit.BlockHitResult;

@ModuleInfo(
   name = "Auto Tool",
   category = ModuleCategory.ACTOR,
   description = "Автоматически выбирает лучший инструмент для блока"
)
public class AutoTool extends Module {
   public static final AutoTool autoTool = new AutoTool();
   public int intVal = -1;

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public int getMc() {
      if (mc.player != null && mc.world != null) {
         if (mc.crosshairTarget instanceof BlockHitResult var2) {
            BlockState var3 = mc.world.getBlockState(var2.getBlockPos());
            int var4 = mc.player.getInventory().selectedSlot;
            float var5 = 1.0F;

            for (int var6 = 0; var6 < 9; var6++) {
               ItemStack var7 = mc.player.getInventory().getStack(var6);
               if (!var7.isEmpty()) {
                  float var8 = var7.getMiningSpeedMultiplier(var3);
                  if (var8 > var5) {
                     var5 = var8;
                     var4 = var6;
                  }
               }
            }

            return var4;
         } else {
            return mc.player.getInventory().selectedSlot;
         }
      } else {
         return -1;
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (this.intVal != -1 && mc.player != null) {
         mc.player.getInventory().selectedSlot = this.intVal;
         if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
         }
      }

      this.intVal = -1;
   }

   public static AutoTool getInstance() {
      return autoTool;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      if (mc.player != null && mc.world != null && mc.interactionManager != null && !mc.player.isCreative()) {
         boolean var2 = mc.interactionManager.isBreakingBlock();
         if (var2) {
            if (this.intVal == -1) {
               this.intVal = mc.player.getInventory().selectedSlot;
            }

            int var3 = this.getMc();
            if (var3 != -1 && mc.player.getInventory().selectedSlot != var3) {
               mc.player.getInventory().selectedSlot = var3;
               if (mc.getNetworkHandler() != null) {
                  mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(var3));
               }
            }
         } else if (this.intVal != -1) {
            mc.player.getInventory().selectedSlot = this.intVal;
            if (mc.getNetworkHandler() != null) {
               mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
            }

            this.intVal = -1;
         }
      } else {
         this.intVal = -1;
      }
   }
}
