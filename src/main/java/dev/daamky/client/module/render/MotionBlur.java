package dev.daamky.client.module.render;

import dev.daamky.client.internal.IrisHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Motion Blur",
   category = ModuleCategory.VISUALS,
   description = "Добавляет эффект размытия при движении камеры"
)
public class MotionBlur extends Module {
   public static final MotionBlur motionBlur = new MotionBlur();
   public final IrisHelper irisHelper;
   public final FloatSetting strength = new FloatSetting("Strength").value(-0.8F).range(-2.0F, 2.0F).step(0.1F).onAction(this::lambda$new$0);
   public final BooleanSetting useRefreshRateScaling = new BooleanSetting("Use refresh rate scaling").value(true);
   public static MotionBlur.BlurAlgorithm mcafy = MotionBlur.BlurAlgorithm.CENTERED;

   public MotionBlur() {
      this.irisHelper = new IrisHelper(this);
      this.irisHelper.registerShaderCallbacks();
      this.addSettings(new Setting[]{this.strength, this.useRefreshRateScaling});
   }

   @Override
   public void onEvent() {
   }

   public void handleFloat(float strength) {
      this.irisHelper.setStrength(strengthx);
   }

   public static MotionBlur getInstance() {
      return motionBlur;
   }

   public FloatSetting getStrength() {
      return this.strength;
   }

   public void lambda$new$0() {
      this.handleFloat(this.getStrength().getValue());
   }

   public static enum BlurAlgorithm {
      BACKWARDS,
      CENTERED;

      public static MotionBlur.BlurAlgorithm[] $values() {
         return new MotionBlur.BlurAlgorithm[]{BACKWARDS, CENTERED};
      }
   }
}
