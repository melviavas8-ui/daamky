package dev.daamky.client.module.player;

import dev.daamky.client.event.ClickSlotEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

@ModuleInfo(
   name = "LockSlot",
   category = ModuleCategory.ACTOR,
   description = "Защищает предметы в выбранных слотах от случайного выброса"
)
public class LockSlot extends Module {
   public static LockSlot lockSlot;
   public final MultiBooleanSetting slot9 = new MultiBooleanSetting("Slots")
      .value(
         new BooleanSetting("Slot 1").value(false),
         new BooleanSetting("Slot 2").value(false),
         new BooleanSetting("Slot 3").value(false),
         new BooleanSetting("Slot 4").value(false),
         new BooleanSetting("Slot 5").value(false),
         new BooleanSetting("Slot 6").value(false),
         new BooleanSetting("Slot 7").value(false),
         new BooleanSetting("Slot 8").value(false),
         new BooleanSetting("Slot 9").value(false)
      );

   public LockSlot() {
      lockSlot = this;
      this.addSettings(new Setting[]{this.slot9});
   }

   public static LockSlot getInstance() {
      if (lockSlot == null) {
         lockSlot = new LockSlot();
      }

      return lockSlot;
   }

   @Override
   public void onEvent() {
      Subscription var1 = ClickSlotEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public boolean computeSlot(int hotbarIdx) {
      try {
         return this.slot9.getValue().get(hotbarIdx).getValue();
      } catch (Exception var3) {
         return false;
      }
   }

   public int computeSize(int slotId) {
      if (mc.player == null) {
         return -1;
      } else {
         ScreenHandler var2 = mc.player.currentScreenHandler;
         if (var2 != null && slotId >= 0 && slotId < var2.slots.size()) {
            Slot var3 = var2.getSlot(slotId);
            if (var3.inventory != mc.player.getInventory()) {
               return -1;
            } else {
               int var4 = var3.getIndex();
               return var4 >= 0 && var4 < 9 ? var4 : -1;
            }
         } else {
            return -1;
         }
      }
   }

   public void lambda$onEvent$2(PacketEvent.PacketEventData event) {
      if (mc.player != null) {
         PlayerActionC2SPacket var2;
         Packet var3;
         if (event.isSend()
            && (var3 = event.packet()) instanceof PlayerActionC2SPacket
            && ((var2 = (PlayerActionC2SPacket)var3).getAction() == Action.DROP_ITEM || var2.getAction() == Action.DROP_ALL_ITEMS)
            && this.computeSlot(mc.player.getInventory().selectedSlot)) {
            PacketEvent.getInstance().setCancelled(true);
         }
      }
   }

   public void lambda$onEvent$1(TickEvent event) {
      if (mc.player != null && mc.currentScreen == null) {
         if (this.computeSlot(mc.player.getInventory().selectedSlot)) {
            while (mc.options.dropKey.wasPressed()) {
            }

            mc.options.dropKey.setPressed(false);
         }
      }
   }

   public void lambda$onEvent$0(ClickSlotEvent.ClickSlotEventData event) {
      if (mc.player != null) {
         int var2;
         if (event.slotActionType() == SlotActionType.THROW && (var2 = this.computeSize(event.slot())) >= 0 && this.computeSlot(var2)) {
            ClickSlotEvent.getInstance().setCancelled(true);
         }
      }
   }
}
