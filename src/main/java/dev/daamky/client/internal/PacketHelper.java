package dev.daamky.client.internal;

import dev.daamky.client.util.PlatformUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;

public final class PacketHelper {
   public static boolean flag = false;

   public static void initInit(Packet<?> packet) {
      assert getClass_746() != null;

      getClass_746().networkHandler.sendPacket(packet);
   }

   public static void closeClose(SequencedPacketCreator packet) {
      assert getClass_746() != null;

      PendingUpdateManager var1 = getClass_746().clientWorld.pendingUpdateManager.incrementSequence();

      try {
         int var2 = getClass_746().clientWorld.pendingUpdateManager.getSequence();
         getClass_746().networkHandler.sendPacket(packet.predict(var2));
      } catch (Throwable var5) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (var1 != null) {
         var1.close();
      }
   }

   public static void setPacket(Packet<?> packet) {
      try {
         flag = true;
         initInit(packet);
      } finally {
         flag = false;
      }
   }

   public static void setPacket(SequencedPacketCreator packet) {
      try {
         flag = true;
         closeClose(packet);
      } finally {
         flag = false;
      }
   }

   public static ClientPlayerEntity getClass_746() {
      return PlatformUtils.getClass_746();
   }

   public PacketHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean getFlag() {
      return flag;
   }
}
