package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Camera Tweaks",
   category = ModuleCategory.VISUALS,
   description = "Позволяет изменять отдаление и вид камеры"
)
public class CameraTweaks extends Module {
   public static final CameraTweaks cameraTweaks = new CameraTweaks();
   public final FloatSetting distance = new FloatSetting("Distance").value(4.0F).range(1.0F, 10.0F).step(1.0F);
   public final BooleanSetting noClip = new BooleanSetting("No Clip").value(true);

   public CameraTweaks() {
      this.addSettings(new Setting[]{this.distance, this.noClip});
   }

   @Override
   public void onEvent() {
   }

   public static CameraTweaks getInstance() {
      return cameraTweaks;
   }
}
