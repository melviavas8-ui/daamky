package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "Auto GApple",
   category = ModuleCategory.BATTLE,
   description = "Автоматически ест золотые яблоки"
)
public class AutoGApple extends Module {
   public static final AutoGApple autoGApple = new AutoGApple();
   public final FloatSetting health = new FloatSetting("Health").value(18.0F).range(4.0F, 20.0F).step(1.0F);
   public final BooleanSetting useEnchanted = new BooleanSetting("Use enchanted").value(true);
   public boolean flag;
   public int intVal = -1;

   public AutoGApple() {
      this.addSettings(new Setting[]{this.health, this.useEnchanted});
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public int getMc() {
      if (mc.player == null) {
         return -1;
      } else {
         for (int var1 = 0; var1 < 9; var1++) {
            ItemStack var2 = mc.player.getInventory().getStack(var1);
            if (this.computeUse(var2)) {
               return var1;
            }
         }

         return -1;
      }
   }

   public boolean computeUse(ItemStack stack) {
      return stack != null && !stack.isEmpty()
         ? stack.isOf(Items.GOLDEN_APPLE) || this.useEnchanted.getValue() && stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)
         : false;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (mc.player != null) {
         if (mc.player.isUsingItem() && mc.interactionManager != null) {
            mc.interactionManager.stopUsingItem(mc.player);
         }

         if (this.intVal != -1) {
            mc.player.getInventory().selectedSlot = this.intVal;
            if (mc.getNetworkHandler() != null) {
               mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
            }

            this.intVal = -1;
         }
      }

      if (this.flag && mc.options != null) {
         mc.options.useKey.setPressed(false);
      }

      this.flag = false;
   }

   public static AutoGApple getInstance() {
      return autoGApple;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      if (mc.player != null && mc.world != null && mc.interactionManager != null) {
         if (mc.currentScreen == null) {
            float var2 = mc.player.getHealth() + (mc.player.hasStatusEffect(StatusEffects.ABSORPTION) ? mc.player.getAbsorptionAmount() : 0.0F);
            float var3 = this.health.getValue();
            if (var2 <= var3) {
               Hand var4 = null;
               if (this.computeUse(mc.player.getOffHandStack())) {
                  var4 = Hand.OFF_HAND;
               } else if (this.computeUse(mc.player.getMainHandStack())) {
                  var4 = Hand.MAIN_HAND;
               } else {
                  int var5 = this.getMc();
                  if (var5 != -1) {
                     if (this.intVal == -1) {
                        this.intVal = mc.player.getInventory().selectedSlot;
                     }

                     mc.player.getInventory().selectedSlot = var5;
                     if (mc.getNetworkHandler() != null) {
                        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(var5));
                     }

                     var4 = Hand.MAIN_HAND;
                  }
               }

               if (var4 != null) {
                  this.flag = true;
                  if (!mc.player.isUsingItem()) {
                     mc.interactionManager.interactItem(mc.player, var4);
                     mc.player.setCurrentHand(var4);
                  }

                  if (mc.options != null) {
                     mc.options.useKey.setPressed(true);
                  }

                  return;
               }
            }

            if (this.flag) {
               if (mc.options != null) {
                  mc.options.useKey.setPressed(false);
               }

               if (mc.player.isUsingItem()) {
                  mc.interactionManager.stopUsingItem(mc.player);
               }

               if (this.intVal != -1 && mc.player != null) {
                  mc.player.getInventory().selectedSlot = this.intVal;
                  if (mc.getNetworkHandler() != null) {
                     mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
                  }

                  this.intVal = -1;
               }

               this.flag = false;
            }
         }
      }
   }
}
