package dev.daamky.client.module.render;

import dev.daamky.client.gui.ESPConfiguratorScreen;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "ESP",
   category = ModuleCategory.VISUALS,
   description = "Единая конфигурация визуалов и 3D превью игрока"
)
public class ESP extends Module {
   public static final ESP myuyt = new ESP();
   public final ButtonSetting buttonSetting = new ButtonSetting("Открыть меню").action(ESP::lambda$new$0);

   public ESP() {
      this.addSettings(new Setting[]{this.buttonSetting});
      super.setEnabledInternal(true, false);
   }

   public static ESP getInstance() {
      return myuyt;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public void setEnabledInternal(boolean enabled, boolean notification) {
      super.setEnabledInternal(true, false);
   }

   @Override
   public void onEvent() {
   }

   public static void lambda$new$0() {
      if (mc != null) {
         mc.setScreen(new ESPConfiguratorScreen(mc.currentScreen));
      }
   }
}
