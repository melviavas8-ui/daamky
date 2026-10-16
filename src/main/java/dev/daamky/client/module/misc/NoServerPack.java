package dev.daamky.client.module.misc;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;

@ModuleInfo(
   name = "No Server Pack",
   category = ModuleCategory.MICS,
   description = "Запрещает серверу скачивать ресурспаки"
)
public class NoServerPack extends Module {
   public static final NoServerPack miipdu = new NoServerPack();

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static NoServerPack getInstance() {
      return miipdu;
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData event) {
      Packet var2;
      if (event.isReceive() && (var2 = event.packet()) instanceof ResourcePackSendS2CPacket) {
         ResourcePackSendS2CPacket var3 = (ResourcePackSendS2CPacket)var2;
         this.handleClass_2596(new ResourcePackStatusC2SPacket(var3.id(), Status.ACCEPTED));
         this.handleClass_2596(new ResourcePackStatusC2SPacket(var3.id(), Status.SUCCESSFULLY_LOADED));
         PacketEvent.getInstance().setCancelled(true);
      }
   }
}
