package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.KeyBindingUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;

@ModuleInfo(
   name = "Inventory Move",
   category = ModuleCategory.MOTION,
   description = "Позволяет свободно перемещаться и взаимодействовать с инвентарем без десинхронизации"
)
public class InventoryMove extends Module {
   public static final InventoryMove inventoryMove = new InventoryMove();
   public final ModeSetting fast = new ModeSetting("Bypass Mode").values("Fast", "Slow", "Direct").value("Fast");
   public final List<InventoryMove.PacketInfo> list = new ArrayList<>();
   public boolean flag = false;

   public InventoryMove() {
      this.addSettings(new Setting[]{this.fast});
   }

   public static InventoryMove getInstance() {
      return inventoryMove;
   }

   public boolean is() {
      return this.fast.is("Direct");
   }

   public boolean checkEnabled() {
      return this.isEnabled() && this.fast.is("Slow");
   }

   @Override
   public void onDisable() {
      this.runNext();
      this.list.clear();
      this.flag = false;
   }

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void runNext() {
      if (mc.player != null && !this.list.isEmpty()) {
         for (InventoryMove.PacketInfo var2 : this.list) {
            mc.player.networkHandler.sendPacket(var2.packet);
         }

         this.list.clear();
      }
   }

   public void lambda$onEvent$1(TickEvent event) {
      if (mc.player != null) {
         if (!this.flag
            && mc.currentScreen != null
            && !(mc.currentScreen instanceof ChatScreen)
            && !(mc.currentScreen instanceof SignEditScreen)
            && !(mc.currentScreen instanceof AnvilScreen)
            && !(mc.currentScreen instanceof AbstractCommandBlockScreen)
            && !(mc.currentScreen instanceof StructureBlockScreen)
            && !(mc.currentScreen instanceof CreativeInventoryScreen)) {
            long var2 = mc.getWindow().getHandle();

            for (KeyBinding var7 : new KeyBinding[]{
               mc.options.forwardKey,
               mc.options.backKey,
               mc.options.leftKey,
               mc.options.rightKey,
               mc.options.jumpKey,
               mc.options.sprintKey
            }) {
               var7.setPressed(InputUtil.isKeyPressed(var2, var7.getDefaultKey().getCode()));
            }
         }

         if (!KeyBindingUtil.check4() && !this.list.isEmpty()) {
            if (this.fast.is("Slow")) {
               InventoryMove.PacketInfo var9 = this.list.remove(0);
               mc.player.networkHandler.sendPacket(var9.packet);
            } else {
               for (InventoryMove.PacketInfo var3 : this.list) {
                  mc.player.networkHandler.sendPacket(var3.packet);
               }

               this.list.clear();
            }

            if (this.list.isEmpty() && mc.currentScreen == null) {
               mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            }
         }
      }
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData event) {
      if (mc.player != null) {
         if (event.isSend()) {
            Packet var2 = event.packet();
            if (mc.currentScreen instanceof InventoryScreen && var2 instanceof ClickSlotC2SPacket var3 && KeyBindingUtil.check4() && !this.fast.is("Direct")) {
               boolean var4 = false;
               if (var3.getButton() == 1
                  && mc.player.currentScreenHandler.getCursorStack().getItem() instanceof BlockItem var5
                  && var5.getBlock() instanceof ShulkerBoxBlock) {
                  var4 = true;
               }

               int var8 = this.fast.is("Slow") ? (this.list.isEmpty() ? 1 : this.list.size() + 1) : 2;
               this.list.add(new InventoryMove.PacketInfo(var2, var8, var4));
               PacketEvent.getInstance().setCancelled(true);
            }

            if (var2 instanceof CloseHandledScreenC2SPacket) {
               if (KeyBindingUtil.check4() && mc.currentScreen instanceof InventoryScreen && !this.list.isEmpty()) {
                  PacketEvent.getInstance().setCancelled(true);
               }

               this.flag = false;
            }
         } else if (event.isReceive() && this.fast.is("Slow")) {
            Packet var7 = event.packet();
            if (var7 instanceof OpenScreenS2CPacket) {
               this.flag = true;
            }

            if (var7 instanceof CloseScreenS2CPacket) {
               this.flag = false;
            }
         }
      }
   }

   private static final class PacketInfo {
      public final Packet<?> packet;
      public final int delay;
      public final boolean shulker;

      public PacketInfo(Packet<?> packet, int delay, boolean shulker) {
         this.packet = packet;
         this.delay = delay;
         this.shulker = shulker;
      }
   }
}
