package dev.daamky.client.util;

import dev.daamky.client.internal.PacketHelper;
import dev.daamky.client.internal.TextShaper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;

public interface MinecraftAccess {
   MinecraftClient mc = MinecraftClient.getInstance();

   default void handleString(String message) {
      TextShaper.handleString(message);
   }

   default void handleClass_7204(SequencedPacketCreator packet) {
      PacketHelper.closeClose(packet);
   }

   default void handlePacket(SequencedPacketCreator packet) {
      PacketHelper.setPacket(packet);
   }

   default void handleClass_2596(Packet<?> packet) {
      PacketHelper.initInit(packet);
   }

   default void handlePacket(Packet<?> packet) {
      PacketHelper.setPacket(packet);
   }
}
