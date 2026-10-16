package dev.daamky.client.event;

import net.minecraft.network.packet.Packet;

public class PacketEvent extends Event<PacketEvent.PacketEventData> {
   public static final PacketEvent packetEvent = new PacketEvent();

   public static PacketEvent getInstance() {
      return packetEvent;
   }

   public static record PacketEventData(Packet<?> packet, PacketEvent.PacketEventData.PacketType packetType) {

      public boolean isReceive() {
         return this.packetType == PacketEvent.PacketEventData.PacketType.RECEIVE;
      }

      public boolean isSend() {
         return this.packetType == PacketEvent.PacketEventData.PacketType.SEND;
      }

      public static enum PacketType {
         SEND,
         RECEIVE;

         public static PacketEvent.PacketEventData.PacketType[] $values() {
            return new PacketEvent.PacketEventData.PacketType[]{SEND, RECEIVE};
         }
      }
   }
}
