package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "AutoFish",
   description = "Автоматическая рыбалка",
   category = ModuleCategory.PVE,
   subCategory = "PvE"
)
public class AutoFish extends Module {
   public static final AutoFish mqoxu = new AutoFish();
   public static FloatSetting floatSetting = new FloatSetting("Задержка (мс)").value(400.0F).range(50.0F, 1000.0F).step(10.0F);
   public boolean flag = false;
   public long timeStamp = -1L;
   public final EventListener<UpdateEvent> eventListener = new EventListener<>(this::lambda$new$0);
   public final EventListener<PacketEvent.PacketEventData> eventListener2 = new EventListener<>(this::lambda$new$1);

   public AutoFish() {
      this.addSettings(new Setting[]{floatSetting});
   }

   @Override
   public void onEvent() {
   }

   @Override
   public void onEnable() {
      super.onEnable();
      UpdateEvent.getInstance().subscribe(this.eventListener);
      PacketEvent.getInstance().subscribe(this.eventListener2);
      this.flag = false;
      this.longVal = -1L;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      UpdateEvent.getInstance().unsubscribe(this.eventListener);
      PacketEvent.getInstance().unsubscribe(this.eventListener2);
   }

   public static AutoFish getInstance() {
      return mqoxu;
   }

   public void lambda$new$1(PacketEvent.PacketEventData event) {
      if (event.isReceive()) {
         if (mc.player != null && mc.world != null && mc.interactionManager != null) {
            Packet var3 = event.packet();
            PlaySoundS2CPacket var2;
            if (var3 instanceof PlaySoundS2CPacket
               && ((SoundEvent)(var2 = (PlaySoundS2CPacket)var3).getSound().value()).equals(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH)
               && mc.player.fishHook != null) {
               double var4 = mc.player.fishHook.squaredDistanceTo(var2.getX(), var2.getY(), var2.getZ());
               if (var4 <= 64.0) {
                  mc.interactionManager
                     .interactItem(
                        mc.player, mc.player.getMainHandStack().getItem() == Items.FISHING_ROD ? Hand.MAIN_HAND : Hand.OFF_HAND
                     );
                  this.longVal = System.currentTimeMillis() + floatSetting.getValue().longValue();
               }
            }
         }
      }
   }

   public void lambda$new$0(UpdateEvent event) {
      if (mc.player != null && mc.interactionManager != null) {
         boolean var2 = mc.player.getMainHandStack().getItem() == Items.FISHING_ROD
            || mc.player.getOffHandStack().getItem() == Items.FISHING_ROD;
         if (!var2) {
            this.flag = false;
            this.longVal = -1L;
         } else {
            if (this.longVal != -1L && System.currentTimeMillis() >= this.longVal) {
               this.longVal = -1L;
               mc.interactionManager
                  .interactItem(
                     mc.player, mc.player.getMainHandStack().getItem() == Items.FISHING_ROD ? Hand.MAIN_HAND : Hand.OFF_HAND
                  );
            }
         }
      }
   }
}
