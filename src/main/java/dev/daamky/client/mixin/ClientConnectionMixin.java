package dev.daamky.client.mixin;

import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.internal.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientConnection.class})
public class ClientConnectionMixin {
   @Unique
   private int daamky$lastHeldSlot = -1;
   @Unique
   private static boolean daamky$serverSprinting = false;
   @Unique
   private static boolean daamky$movePacketSentSinceSprintAction = true;

   @Inject(
      method = {"send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendPackets(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo callbackInfo) {
      if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().world != null) {
         if (packet instanceof PlayerMoveC2SPacket) {
            daamky$movePacketSentSinceSprintAction = true;
         }

         if (packet instanceof PlayerInteractEntityC2SPacket && !daamky$movePacketSentSinceSprintAction) {
            callbackInfo.cancel();
         } else {
            if (packet instanceof UpdateSelectedSlotC2SPacket var5) {
               if (var5.getSelectedSlot() == this.daamky$lastHeldSlot) {
                  callbackInfo.cancel();
                  return;
               }

               this.daamky$lastHeldSlot = var5.getSelectedSlot();
            }

            if (packet instanceof ClientCommandC2SPacket var6) {
               if (var6.getMode() == Mode.START_SPRINTING) {
                  if (daamky$serverSprinting) {
                     callbackInfo.cancel();
                     return;
                  }

                  daamky$serverSprinting = true;
                  daamky$movePacketSentSinceSprintAction = false;
               } else if (var6.getMode() == Mode.STOP_SPRINTING) {
                  if (!daamky$serverSprinting) {
                     callbackInfo.cancel();
                     return;
                  }

                  daamky$serverSprinting = false;
                  daamky$movePacketSentSinceSprintAction = false;
               }
            }

            if (packet instanceof CloseHandledScreenC2SPacket var7) {
               if (var7.getSyncId() == 0) {
                  callbackInfo.cancel();
                  return;
               }

               if (daamky$serverSprinting && MinecraftClient.getInstance().getNetworkHandler() != null && MinecraftClient.getInstance().player != null) {
                  daamky$serverSprinting = false;
                  MinecraftClient.getInstance().getNetworkHandler().sendPacket(new ClientCommandC2SPacket(MinecraftClient.getInstance().player, Mode.STOP_SPRINTING));
                  MinecraftClient.getInstance().player.setSprinting(false);
               }
            }

            if (!PacketHelper.getFlag()) {
               if (PacketEvent.getInstance().postAndCheckCancelled(new PacketEvent.PacketEventData(packet, PacketEvent.PacketEventData.PacketType.SEND))) {
                  callbackInfo.cancel();
               }
            }
         }
      } else {
         daamky$serverSprinting = false;
         daamky$movePacketSentSinceSprintAction = true;
      }
   }

   @Inject(
      method = {"handlePacket"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void receivePackets(Packet<?> packet, PacketListener listener, CallbackInfo callbackInfo) {
      if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().world != null) {
         if (packet instanceof PlayerPositionLookS2CPacket) {
            daamky$serverSprinting = false;
            daamky$movePacketSentSinceSprintAction = true;
         }

         if (packet instanceof BundleS2CPacket var3) {
            for (Packet var5 : var3.getPackets()) {
               handleSinglePacket(var5);
            }
         } else if (handleSinglePacket(packet)) {
            callbackInfo.cancel();
         }
      } else {
         daamky$serverSprinting = false;
         daamky$movePacketSentSinceSprintAction = true;
      }
   }

   @Unique
   private static boolean handleSinglePacket(Packet<?> packet) {
      return PacketEvent.getInstance().postAndCheckCancelled(new PacketEvent.PacketEventData(packet, PacketEvent.PacketEventData.PacketType.RECEIVE));
   }
}
