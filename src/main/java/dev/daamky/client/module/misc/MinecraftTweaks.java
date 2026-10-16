package dev.daamky.client.module.misc;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Minecraft Tweaks",
   category = ModuleCategory.MICS,
   description = "Различные мелкие фиксы и улучшения"
)
public class MinecraftTweaks extends Module {
   public static final MinecraftTweaks minecraftTweaks = new MinecraftTweaks();
   public final BooleanSetting chatAntiSpam = new BooleanSetting("Chat Anti-Spam").value(true);
   public final BooleanSetting chatAnimations = new BooleanSetting("Chat Animations").value(true);
   public final ModeSetting left = new ModeSetting("Chat Animation Mode").value("Bottom").values("Bottom", "Left").visibleWhen(this.chatAnimations::getValue);
   public final BooleanSetting tabAnimations = new BooleanSetting("Tab Animations").value(true);

   public MinecraftTweaks() {
      this.addSettings(new Setting[]{this.chatAntiSpam, this.chatAnimations, this.left, this.tabAnimations});
   }

   @Override
   public void onEvent() {
   }

   public static MinecraftTweaks getInstance() {
      return minecraftTweaks;
   }
}
