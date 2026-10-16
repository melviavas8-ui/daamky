package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "View Model",
   category = ModuleCategory.VISUALS,
   description = "Настраивает позицию камеры от первого лица"
)
public class ViewModel extends Module {
   public static final ViewModel viewModel = new ViewModel();
   public final FloatSetting rightX = new FloatSetting("Right X").value(0.0F).range(-2.0F, 2.0F).step(1.0E-4F);
   public final FloatSetting rightY = new FloatSetting("Right Y").value(0.0F).range(-2.0F, 2.0F).step(1.0E-4F);
   public final FloatSetting rightZ = new FloatSetting("Right Z").value(0.0F).range(-2.0F, 2.0F).step(1.0E-4F);
   public final FloatSetting rightScale = new FloatSetting("Right Scale").value(1.0F).range(0.05F, 3.0F).step(1.0E-4F);
   public final FloatSetting leftX = new FloatSetting("Left X").value(0.0F).range(-2.0F, 2.0F).step(1.0E-4F);
   public final FloatSetting leftY = new FloatSetting("Left Y").value(0.0F).range(-2.0F, 2.0F).step(1.0E-4F);
   public final FloatSetting leftZ = new FloatSetting("Left Z").value(0.0F).range(-2.0F, 2.0F).step(1.0E-4F);
   public final FloatSetting leftScale = new FloatSetting("Left Scale").value(1.0F).range(0.05F, 3.0F).step(1.0E-4F);
   public final ButtonSetting resetPosition = new ButtonSetting("Reset position").action(this::runLeft);

   public ViewModel() {
      this.addSettings(
         new Setting[]{this.rightX, this.rightY, this.rightZ, this.rightScale, this.leftX, this.leftY, this.leftZ, this.leftScale, this.resetPosition}
      );
   }

   @Override
   public void onEvent() {
   }

   public void runLeft() {
      this.rightX.setValue(0.0F);
      this.rightY.setValue(0.0F);
      this.rightZ.setValue(0.0F);
      this.rightScale.setValue(1.0F);
      this.leftX.setValue(0.0F);
      this.leftY.setValue(0.0F);
      this.leftZ.setValue(0.0F);
      this.leftScale.setValue(1.0F);
   }

   public static ViewModel getInstance() {
      return viewModel;
   }
}
