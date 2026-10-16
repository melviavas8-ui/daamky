package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "NoRotate",
   category = ModuleCategory.ACTOR,
   description = "Запрещает серверу поворачивать вашу камеру"
)
public class NoRotate extends Module {
   public static final NoRotate noRotate = new NoRotate();
   public final ModeSetting matrix = new ModeSetting("Mode").value("Vanilla").values("Vanilla", "Grim", "Matrix");

   public NoRotate() {
      this.addSettings(new Setting[]{this.matrix});
   }

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static NoRotate getInstance() {
      return noRotate;
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData event) {
      if (event.packet() instanceof PlayerPositionLookS2CPacket var2 && event.isReceive()) {
         String var7 = this.matrix.getValue();
         switch (var7) {
            case "Vanilla":
               var2.change().yaw = mc.player.getYaw();
               var2.change().pitch = mc.player.getPitch();
               break;
            case "Grim":
               float var5 = MathHelper.wrapDegrees(var2.change().yaw - mc.player.getYaw());
               float var6 = var2.change().pitch - mc.player.getPitch();
               if (Math.abs(var5) > 5.0F || Math.abs(var6) > 5.0F) {
                  var2.change().yaw = mc.player.getYaw();
                  var2.change().pitch = mc.player.getPitch();
               }
               break;
            case "Matrix":
               if (Math.abs(var2.change().yaw - mc.player.getYaw()) > 180.0F
                  || Math.abs(var2.change().pitch - mc.player.getPitch()) > 90.0F) {
                  var2.change().yaw = mc.player.getYaw();
                  var2.change().pitch = mc.player.getPitch();
               }
         }

         return;
      }
   }
}
