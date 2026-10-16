package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PostMotionEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(
   name = "DeathTravel",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Сохраняет место смерти и позволяет по кнопке вернуться к нему"
)
public class DeathTravel extends Module {
   public static DeathTravel deathTravel;
   public BlockPos mgaxd = null;
   public boolean flag = false;
   public boolean flag2 = false;
   public final ButtonSetting buttonSetting = new ButtonSetting("Отправиться к месту смерти").action(this::runName);

   public static DeathTravel getInstance() {
      if (deathTravel == null) {
         deathTravel = new DeathTravel();
      }

      return deathTravel;
   }

   public DeathTravel() {
      this.addSettings(new Setting[]{this.buttonSetting});
   }

   public void runName() {
      if (this.mgaxd == null) {
         this.buttonSetting.setName("Нет точки смерти");
      } else {
         if (this.flag) {
            if (mc.player != null && mc.player.networkHandler != null) {
               mc.player.networkHandler.sendChatMessage("#stop");
            }

            this.flag = false;
            this.buttonSetting.setName("Отправиться к месту смерти");
         } else {
            if (mc.player != null && mc.player.networkHandler != null) {
               mc.player.networkHandler.sendChatMessage("#goto " + this.mgaxd.getX() + " " + this.mgaxd.getY() + " " + this.mgaxd.getZ());
            }

            this.flag = true;
            this.buttonSetting.setName("Отменить");
         }
      }
   }

   @Override
   public void onEvent() {
      this.addEventListeners(new Subscription[]{PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0))});
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (this.flag && mc.player != null && mc.player.networkHandler != null) {
         mc.player.networkHandler.sendChatMessage("#stop");
      }

      this.flag = false;
      this.buttonSetting.setName("Отправиться к месту смерти");
   }

   public void lambda$onEvent$0(PostMotionEvent.EventPostMotionData event) {
      if (mc.player != null) {
         if (!(mc.player.getHealth() <= 0.0F) && !(mc.currentScreen instanceof DeathScreen)) {
            if (mc.player.isAlive()) {
               this.flag2 = false;
            }
         } else if (!this.flag2) {
            this.mgaxd = mc.player.getBlockPos();
            this.flag2 = true;
            this.handleString("Точка смерти сохранена: X:" + this.mgaxd.getX() + " Y:" + this.mgaxd.getY() + " Z:" + this.mgaxd.getZ());
            this.buttonSetting.setName("Отправиться к месту смерти");
            this.flag = false;
         }
      }
   }
}
