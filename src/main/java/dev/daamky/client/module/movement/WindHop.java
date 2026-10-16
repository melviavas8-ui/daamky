package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.InputTransformEvent;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;

@ModuleInfo(
   name = "WindHop",
   category = ModuleCategory.MOTION,
   description = "Автоматически прыгает после использования заряда ветра (Delta)",
   subCategory = "Movement"
)
public class WindHop extends Module {
   public static final WindHop windHop = new WindHop();
   public final BooleanSetting booleanSetting = new BooleanSetting("Поворачивать голову вниз").value(true);
   public final BooleanSetting booleanSetting2 = new BooleanSetting("Авто-прыжок").value(true);
   public int intVal = -1;

   public WindHop() {
      this.addSettings(new Setting[]{this.booleanValSetting, this.booleanValSetting2});
   }

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = InputTransformEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public boolean checkEnabled() {
      return this.isEnabled() && this.booleanValSetting.getValue();
   }

   public static WindHop getInstance() {
      return windHop;
   }

   public void lambda$onEvent$2(InputTransformEvent event) {
      if (this.intVal == 0 && this.booleanValSetting2.getValue()) {
         if (mc.player != null && mc.player.isOnGround()) {
            mc.player.jump();
         }

         this.intVal = -1;
      }
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      if (this.intVal > 0) {
         this.intVal--;
      }
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData event) {
      if (mc.player != null) {
         if (event.packetType() == PacketEvent.PacketEventData.PacketType.SEND
            && event.packet() instanceof PlayerInteractItemC2SPacket var2
            && mc.player.getStackInHand(var2.getHand()).isOf(Items.WIND_CHARGE)) {
            this.intVal = 2;
         }
      }
   }
}
