package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.KeyEvent;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "Elytra Swap",
   category = ModuleCategory.ACTOR,
   description = "Быстро надевает элитры вместо нагрудника и запускает фейерверк без флагов"
)
public class ElytraSwap extends Module {
   public static final ElytraSwap elytraSwap = new ElytraSwap();
   public final IntegerSetting swapKey = new IntegerSetting("Swap key").value(-999);
   public final IntegerSetting launchKey = new IntegerSetting("Launch key").value(-999);
   public final BooleanSetting autoFly = new BooleanSetting("Auto-Fly").value(false);
   public boolean flag = false;
   public boolean flag2 = false;
   public boolean flag3 = false;
   public final DelayHelper delayHelper = new DelayHelper();
   public int intVal = -1;
   public int int2 = -1;
   public int momvoj = 0;
   public final DelayHelper delayHelper2 = new DelayHelper();

   public ElytraSwap() {
      this.addSettings(new Setting[]{this.swapKey, this.launchKey, this.autoFly});
   }

   public static ElytraSwap getInstance() {
      return elytraSwap;
   }

   @Override
   public void onDisable() {
      this.run();
      super.onDisable();
   }

   public void run() {
      if (this.momvoj != 0) {
         InventoryUtil.handleFlag(true);
      }

      this.momvoj = 0;
      this.intVal = -1;
      this.int2 = -1;
      this.flag2 = false;
   }

   @Override
   public void onEvent() {
      Subscription var1 = KeyEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void runAuto() {
      if (mc.player != null && mc.interactionManager != null) {
         int var1 = this.getInt();
         if (var1 != -1) {
            boolean var2 = !InventoryUtil.getMc();
            InventoryUtil.initInit3(var1);
            if (var2 && this.autoFly.getValue()) {
               this.flag3 = true;
               this.delayHelper.runCurrent();
            }
         }
      }
   }

   public void run2() {
      this.runAuto();
   }

   public void run3() {
      if (mc.player != null && mc.interactionManager != null) {
         if (mc.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
         } else if (this.momvoj == 0) {
            int var1 = -1;

            for (int var2 = 0; var2 < 36; var2++) {
               ItemStack var3 = mc.player.getInventory().getStack(var2);
               if (!var3.isEmpty() && var3.isOf(Items.FIREWORK_ROCKET)) {
                  var1 = var2;
                  break;
               }
            }

            if (var1 != -1) {
               this.intVal = var1;
               this.int2 = mc.player.getInventory().selectedSlot;
               this.delayHelper2.runCurrent();
               this.momvoj = 1;
            }
         }
      }
   }

   public void run4() {
      if (mc.player != null && mc.interactionManager != null) {
         if (this.momvoj != 0) {
            AttackAura.int3 = 3;
            if (this.delayHelper2.computeCurrent(50L)) {
               this.delayHelper2.runCurrent();
               int var1 = mc.player.playerScreenHandler.syncId;
               switch (this.momvoj) {
                  case 1:
                     if (this.intVal < 9) {
                        mc.player.getInventory().selectedSlot = this.intVal;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
                     } else {
                        mc.interactionManager.clickSlot(var1, this.intVal, this.int2, SlotActionType.SWAP, mc.player);
                        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
                        mc.player.closeHandledScreen();
                     }

                     this.momvoj = 2;
                     break;
                  case 2:
                     mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                     this.momvoj = 3;
                     break;
                  case 3:
                     if (this.intVal < 9) {
                        mc.player.getInventory().selectedSlot = this.int2;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.int2));
                     } else {
                        mc.interactionManager.clickSlot(var1, this.intVal, this.int2, SlotActionType.SWAP, mc.player);
                        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
                        mc.player.closeHandledScreen();
                     }

                     this.momvoj = 4;
                     break;
                  case 4:
                     this.momvoj = 0;
                     this.intVal = -1;
                     this.int2 = -1;
               }
            }
         }
      }
   }

   public void runFly() {
      if (this.flag3 && this.delayHelper.computeCurrent(300L)) {
         if (this.autoFly.getValue() && mc.player != null) {
            mc.player.networkHandler.sendChatCommand("fly");
         }

         this.flag3 = false;
      }
   }

   public int computeInt(Item... items) {
      for (Item var5 : items) {
         int var6 = InventoryUtil.computeInt(var5);
         if (var6 != -1) {
            return var6;
         }
      }

      return -1;
   }

   public int getInt() {
      return InventoryUtil.getMc() ? this.getInt3() : this.getInt2();
   }

   public int getInt2() {
      return this.computeInt(Items.ELYTRA);
   }

   public int getInt3() {
      return this.computeInt(
         Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.LEATHER_CHESTPLATE
      );
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      int var2 = this.launchKey.getValue();
      if (var2 != -999 && var2 != -1 && mc.currentScreen == null) {
         boolean var3 = KeyNames.computeGlfw(var2);
         if (var3 && !this.flag2 && this.momvoj == 0) {
            this.flag2 = true;
            this.run3();
         } else if (!var3) {
            this.flag2 = false;
         }
      }

      this.run4();
      this.runFly();
   }

   public void lambda$onEvent$0(KeyEvent.KeyEventData event) {
      int var2 = this.swapKey.getValue();
      int var3 = this.launchKey.getValue();
      if (event.key() == var2) {
         if (event.action() == 1 && !this.flag && mc.currentScreen == null) {
            this.flag = true;
            this.runAuto();
         } else if (event.action() == 0) {
            this.flag = false;
         }
      }

      if (event.key() == var3) {
         if (event.action() == 1 && !this.flag2 && mc.currentScreen == null) {
            this.flag2 = true;
            this.run3();
         } else if (event.action() == 0) {
            this.flag2 = false;
         }
      }
   }
}
