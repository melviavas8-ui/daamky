package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PostMotionEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import java.util.List;

@ModuleInfo(
   name = "AutoMiner",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Автоматически прыгает на /warp mine и через 7 секунд запускает Баритон к нужной руде (зависит от MineHelper)"
)
public class AutoMiner extends Module {
   public static AutoMiner autoMiner;
   public long timeStamp = 0L;
   public boolean flag = false;

   public static AutoMiner getInstance() {
      if (autoMiner == null) {
         autoMiner = new AutoMiner();
      }

      return autoMiner;
   }

   @Override
   public void onEvent() {
      this.addEventListeners(new Subscription[]{PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0))});
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.longVal = System.currentTimeMillis();
      this.flag = false;
      if (mc.player != null && mc.player.networkHandler != null) {
         this.handleString("Летим на шахту (/warp mine)... Ждем 7 секунд.");
         mc.player.networkHandler.sendChatCommand("warp mine");
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (mc.player != null && mc.player.networkHandler != null) {
         mc.player.networkHandler.sendChatMessage("#stop");
         this.handleString("Шахтер остановлен.");
      }

      this.flag = false;
   }

   public void lambda$onEvent$0(PostMotionEvent.EventPostMotionData event) {
      if (mc.player != null) {
         if (!this.flag && System.currentTimeMillis() - this.longVal >= 7000L) {
            this.flag = true;
            if (!MineHelper.getInstance().isEnabled()) {
               this.handleString("Ошибка: MineHelper выключен! Включи его, чтобы AutoMiner понял, какую руду копать.");
               return;
            }

            List var2 = MineHelper.getInstance().initAdd();
            if (var2.isEmpty()) {
               this.handleString("Ошибка: В MineHelper не выбрано ни одной руды!");
               return;
            }

            String var3 = "#mine " + String.join(" ", var2);
            this.handleString("Запускаем шахтера: " + var3);
            if (mc.player.networkHandler != null) {
               mc.player.networkHandler.sendChatMessage(var3);
            }
         }
      }
   }
}
