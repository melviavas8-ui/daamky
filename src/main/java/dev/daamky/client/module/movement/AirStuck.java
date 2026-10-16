package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "AirStuck",
   category = ModuleCategory.MOTION,
   description = "Позволяет зависнуть в воздухе на месте"
)
public class AirStuck extends Module {
   public static final AirStuck airStuck = new AirStuck();
   public final ModeSetting modeSetting = new ModeSetting("Режим зависания").values("Обычный", "Удаляющий игрока").value("Обычный");
   public final BooleanSetting catchMoment = new BooleanSetting("Catch Moment").value(false);
   public double doubleVal = Double.NaN;
   public boolean flag;
   public Vec3d class_243 = null;

   public AirStuck() {
      this.addSettings(new Setting[]{this.modeSetting, this.catchMoment});
   }

   public static AirStuck getInstance() {
      return airStuck;
   }

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   @Override
   public void onEnable() {
      if (mc.player == null) {
         this.run();
      } else {
         this.class_243 = mc.player.getPos();
         if (this.catchMoment.getValue()) {
            this.flag = false;
            this.doubleVal = mc.player.isOnGround() ? Double.NaN : mc.player.getY();
         } else {
            this.doubleVal = mc.player.getY();
            this.getMc();
         }
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.run();
   }

   public void runCase() {
      if (mc.player == null) {
         this.run();
      } else {
         if (this.catchMoment.getValue() && !"Удаляющий игрока".equalsIgnoreCase(this.modeSetting.getValue())) {
            double var1 = mc.player.getY();
            if (mc.player.isOnGround()) {
               this.doubleVal = Double.NaN;
               this.flag = false;
            } else if (!this.flag) {
               if (Double.isNaN(this.doubleVal) || var1 > this.doubleVal) {
                  this.doubleVal = var1;
               } else if (var1 < this.doubleVal) {
                  this.getMc();
               }
            }
         }

         if (this.flag && this.class_243 != null) {
            mc.player.setVelocity(0.0, 0.0, 0.0);
            mc.player.setPosition(this.class_243.x, this.class_243.y, this.class_243.z);
            if (mc.player.input != null) {
               mc.player.input.movementForward = 0.0F;
               mc.player.input.movementSideways = 0.0F;
               mc.player.input.playerInput = PlayerInput.DEFAULT;
            }
         }
      }
   }

   public void getMc() {
      if (mc.player != null) {
         this.flag = true;
         this.class_243 = mc.player.getPos();
         this.doubleVal = mc.player.getY();
      }
   }

   public void run() {
      this.flag = false;
      this.doubleVal = Double.NaN;
      this.class_243 = null;
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      this.runCase();
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData event) {
      if (this.flag) {
         Packet var2 = event.packet();
         if (var2 instanceof PlayerMoveC2SPacket || var2 instanceof PlayerInputC2SPacket || var2 instanceof PlayerActionC2SPacket) {
            PacketEvent.getInstance().setCancelled(true);
         }
      }
   }
}
