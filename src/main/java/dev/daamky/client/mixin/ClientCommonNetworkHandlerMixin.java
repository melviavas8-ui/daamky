package dev.daamky.client.mixin;

import dev.daamky.client.module.misc.NoServerPack;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientCommonNetworkHandler.class})
public abstract class ClientCommonNetworkHandlerMixin {
   @Inject(
      method = {"onResourcePackSend"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onResourcePackSend(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
      if (NoServerPack.getInstance().isEnabled()) {
         ClientCommonNetworkHandler var3 = (ClientCommonNetworkHandler)this;
         var3.sendPacket(new ResourcePackStatusC2SPacket(packet.id(), Status.ACCEPTED));
         var3.sendPacket(new ResourcePackStatusC2SPacket(packet.id(), Status.SUCCESSFULLY_LOADED));
         ci.cancel();
      }
   }
}
