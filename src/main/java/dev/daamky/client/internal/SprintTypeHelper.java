package dev.daamky.client.internal;

import dev.daamky.client.event.SprintEvent;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;

public class SprintTypeHelper implements MinecraftAccess {
   public SprintTypeHelper.SprintType sprintType;

   public SprintTypeHelper(SprintTypeHelper.SprintType sprintType) {
      this.sprintType = sprintType;
   }

   public void handleLegit(SprintEvent.SprintEventData event, boolean rule) {
      if (this.sprintType == SprintTypeHelper.SprintType.LEGIT && rule) {
         event.setSprint(false);
      }
   }

   public void handlePacket(boolean enable) {
      if (this.sprintType == SprintTypeHelper.SprintType.PACKET && mc.player.lastSprinting && !mc.player.isTouchingWater()) {
         this.initInit(enable);
      }
   }

   public void initInit(boolean enable) {
      this.handleClass_2596(new ClientCommandC2SPacket(mc.player, enable ? Mode.START_SPRINTING : Mode.STOP_SPRINTING));
      mc.player.setSprinting(enable);
      mc.options.sprintKey.setPressed(enable);
   }

   public static enum SprintType {
      LEGIT,
      PACKET,
      NONE;

      public static SprintTypeHelper.SprintType[] $values() {
         return new SprintTypeHelper.SprintType[]{LEGIT, PACKET, NONE};
      }
   }
}
