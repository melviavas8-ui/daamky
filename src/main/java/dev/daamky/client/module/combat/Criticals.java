package dev.daamky.client.module.combat;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

@ModuleInfo(
   name = "Criticals",
   category = ModuleCategory.BATTLE,
   description = "Позволяет наносить критические удары без прыжка"
)
public class Criticals extends Module {
   public static final Criticals criticals = new Criticals();
   public Subscription subscription;

   @Override
   public void onEvent() {
      this.subscription = AttackEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{this.subscription});
   }

   public void getMc() {
      double var1 = mc.player.getX();
      double var3 = mc.player.getY();
      double var5 = mc.player.getZ();
      this.initInit(var1, var3 + 0.0625, var5, false);
      this.initInit(var1, var3, var5, false);
      this.initInit(var1, var3 + 1.1E-5, var5, false);
      this.initInit(var1, var3, var5, false);
   }

   public void initInit(double x, double y, double z, boolean onGround) {
      if (mc.getNetworkHandler() != null && mc.player != null) {
         mc.getNetworkHandler().sendPacket(new PositionAndOnGround(x, y, z, onGround, mc.player.horizontalCollision));
      }
   }

   public static Criticals getInstance() {
      return criticals;
   }

   public void lambda$onEvent$0(AttackEvent.AttackEventData event) {
      if (mc.player != null && mc.world != null && mc.getNetworkHandler() != null) {
         if (!AttackAura.getInstance().isEnabled()) {
            if (mc.player.isOnGround() && !mc.player.isTouchingWater() && !mc.player.isInLava()) {
               this.getMc();
            }
         }
      }
   }
}
