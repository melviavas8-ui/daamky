package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.util.KeyNames;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "Click Pearl",
   category = ModuleCategory.ACTOR,
   description = "Мгновенно кидает эндер-перл по нажатию клавиши без десинков и флагов"
)
public class ClickPearl extends Module {
   public static final ClickPearl clickPearl = new ClickPearl();
   public final IntegerSetting throwKey = new IntegerSetting("Throw key").value(-999);
   public final BooleanSetting notify = new BooleanSetting("Notify").value(true);
   public int intVal = -1;
   public int int2 = -1;
   public int int3 = 0;
   public final DelayHelper mxoeav = new DelayHelper();
   public boolean flag = false;

   public ClickPearl() {
      this.addSettings(new Setting[]{this.throwKey, this.notify});
   }

   @Override
   public void onDisable() {
      this.run();
      super.onDisable();
   }

   public void run() {
      if (this.intVal3 != 0) {
         InventoryUtil.handleFlag(true);
      }

      this.intVal3 = 0;
      this.intVal = -1;
      this.intVal2 = -1;
      this.flag = false;
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void runNotify() {
      if (mc.player != null && mc.interactionManager != null) {
         int var1 = -1;

         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = mc.player.getInventory().getStack(var2);
            if (!var3.isEmpty() && var3.isOf(Items.ENDER_PEARL)) {
               var1 = var2;
               break;
            }
         }

         if (var1 == -1) {
            if (this.notify.getValue()) {
               this.handleString("Эндер-перл не найден в инвентаре!");
            }
         } else {
            this.intVal = var1;
            this.intVal2 = mc.player.getInventory().selectedSlot;
            this.mxoeav.runCurrent();
            this.intVal3 = 1;
         }
      }
   }

   public static ClickPearl getInstance() {
      return clickPearl;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      int var2 = this.throwKey.getValue();
      if (var2 != -999 && var2 != -1 && mc.currentScreen == null) {
         boolean var3 = KeyNames.computeGlfw(var2);
         if (var3 && !this.flag && this.intVal3 == 0) {
            this.flag = true;
            this.runNotify();
         } else if (!var3) {
            this.flag = false;
         }
      }

      if (mc.player != null && mc.interactionManager != null) {
         if (this.intVal3 != 0) {
            InventoryUtil.handleFlag(false);
            AttackAura.int3 = 3;
            if (this.mxoeav.computeCurrent(50L)) {
               this.mxoeav.runCurrent();
               int var4 = mc.player.playerScreenHandler.syncId;
               switch (this.intVal3) {
                  case 1:
                     if (this.intVal < 9) {
                        mc.player.getInventory().selectedSlot = this.intVal;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
                     } else {
                        mc.interactionManager.clickSlot(var4, this.intVal, this.intVal2, SlotActionType.SWAP, mc.player);
                     }

                     this.intVal3 = 2;
                     break;
                  case 2:
                     mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                     this.intVal3 = 3;
                     break;
                  case 3:
                     if (this.intVal < 9) {
                        mc.player.getInventory().selectedSlot = this.intVal2;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal2));
                     } else {
                        mc.interactionManager.clickSlot(var4, this.intVal, this.intVal2, SlotActionType.SWAP, mc.player);
                     }

                     this.intVal3 = 4;
                     break;
                  case 4:
                     if (this.intVal >= 9) {
                        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var4));
                        mc.player.closeHandledScreen();
                     }

                     InventoryUtil.handleFlag(true);
                     this.intVal3 = 0;
                     this.intVal = -1;
                     this.intVal2 = -1;
               }
            }
         }
      }
   }
}
