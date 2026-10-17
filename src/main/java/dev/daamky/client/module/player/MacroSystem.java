package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.KeyEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.MacroEngine;
import dev.daamky.client.internal.MacroStore;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "MacroSystem",
   category = ModuleCategory.ACTOR,
   description = "Позволяет создавать бинды на команды"
)
public class MacroSystem extends Module {
   public static final MacroSystem macroSystem = new MacroSystem();

   public MacroSystem() {
      this.addSettings(new Setting[0]);
   }

   @Override
   public void onEvent() {
      MacroStore var1 = MacroStore.getInstance();
      Subscription var2 = KeyEvent.getInstance().subscribe(new EventListener<>(MacroSystem::lambda$onEvent$1));
      Subscription var3 = TickEvent.getInstance().subscribe(new EventListener<>(MacroSystem::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var2, var3});
   }

   @Override
   public void onEnable() {
      MacroStore.getInstance().isInitialized();
   }

   @Override
   public void onDisable() {
      MacroStore.getInstance().getRunningMacros();
   }

   public static MacroSystem getInstance() {
      return macroSystem;
   }

   public static void lambda$onEvent$2(MacroStore manager, TickEvent event) {
      manager.runNext();
   }

   public static void lambda$onEvent$1(MacroStore manager, KeyEvent.KeyEventData event) {
      if (mc.player != null && mc.currentScreen == null) {
         if (event.action() == 1) {
            int var2 = event.key();
            manager.getList().stream().filter((p0) -> MacroSystem.lambda$onEvent$0(var2, manager, p0)).findFirst().ifPresent(manager::initRunning);
         }
      }
   }

   public static boolean lambda$onEvent$0(int key, MacroStore manager, MacroEngine.Macro m) {
      return m.getKey() == key && !manager.computeContains(m.getName());
   }
}
