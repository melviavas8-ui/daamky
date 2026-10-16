package dev.daamky.client.module.misc;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.FactoryHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.client.MinecraftClient;

@ModuleInfo(
   name = "Configs",
   category = ModuleCategory.MICS,
   description = "Управление локальными конфигурациями клиента"
)
public class Configs extends Module {
   public static final Configs configs = new Configs();
   public final ButtonSetting openMenu = new ButtonSetting("Open Menu").action(Configs::lambda$new$0);

   public Configs() {
      this.addSettings(new Setting[]{this.openMenu});
   }

   public static Configs getInstance() {
      return configs;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null) {
         MainMenuScreen.currentView = MainMenuScreen.ViewMode.CONFIGS;
         var1.setScreen(FactoryHelper.getInstance().getMainMenuScreen());
      }

      this.setEnabledInternal(false, false);
   }

   @Override
   public void onEvent() {
   }

   public static void lambda$new$0() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0 != null) {
         MainMenuScreen.currentView = MainMenuScreen.ViewMode.CONFIGS;
         var0.setScreen(FactoryHelper.getInstance().getMainMenuScreen());
      }
   }
}
