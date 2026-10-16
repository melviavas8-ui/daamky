package dev.daamky.client.module.misc;

import dev.daamky.client.internal.SoundHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Toggle Sounds",
   category = ModuleCategory.MICS,
   description = "Воспроизводит звук при включении модулей"
)
public class ToggleSounds extends Module {
   public static final ToggleSounds toggleSounds = new ToggleSounds();
   public final ModeSetting blop = new ModeSetting("Sound").value("Blop").values("Smooth", "Celestial", "Nursultan", "Akrien", "Tech", "Blop");
   public final FloatSetting volume = new FloatSetting("Volume").value(60.0F).range(1.0F, 100.0F).step(1.0F);

   public ToggleSounds() {
      this.addSettings(new Setting[]{this.blop, this.volume});
   }

   public static void handleFlag(boolean state) {
      if (toggleSounds.isEnabled()) {
         String var1 = toggleSounds.blop.getValue();

         SoundHelper.handleVolume(switch (var1) {
            case "Nursultan" -> state ? SoundHelper.class_34144 : SoundHelper.class_34145;
            case "Celestial" -> state ? SoundHelper.miujs : SoundHelper.class_34143;
            case "Akrien" -> state ? SoundHelper.class_34146 : SoundHelper.class_34147;
            case "Tech" -> state ? SoundHelper.mpusg : SoundHelper.class_34148;
            case "Blop" -> state ? SoundHelper.class_34149 : SoundHelper.class_341410;
            default -> state ? SoundHelper.class_3414 : SoundHelper.class_34142;
         });
      }
   }

   @Override
   public void onEvent() {
   }

   public static ToggleSounds getInstance() {
      return toggleSounds;
   }
}
