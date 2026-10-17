package dev.daamky.client.internal;

import dev.daamky.client.config.Configurable;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.util.function.Supplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class Prediction extends Configurable {
   public Prediction.PredictMode menxeg = Prediction.PredictMode.SIMPLE;
   public final BooleanSetting prediction = new BooleanSetting("Prediction").value(true);
   public final Supplier<Boolean> supplier = this.prediction::getValue;
   public final ModeSetting mode = new ModeSetting("Mode")
      .value(Prediction.PredictMode.SIMPLE)
      .enumValues(Prediction.PredictMode.values())
      .onAction(this::lambda$new$0);
   public final BooleanSetting glidingOnly = new BooleanSetting("Gliding only").value(true);
   public final FloatSetting multiplier = new FloatSetting("Multiplier").value(1.8F).range(0.5F, 6.0F).step(0.1F);

   public Prediction() {
      this.addSettings(new Setting[]{this.prediction, this.mode, this.glidingOnly, this.multiplier});

      for (Setting var2 : this.getSettings()) {
         if (var2 != this.prediction) {
            var2.visibleWhen(this.supplier);
         }
      }
   }

   public Vec3d computePredict(LivingEntity target, Vec3d targetPosition) {
      if (this.prediction.getValue() && !(EntityHelper.computeDouble(target) < 13.0) && (!this.glidingOnly.getValue() || target.isGliding())) {
         double var3 = (double)this.multiplier.getValue().floatValue();
         return this.menxeg.predict(target, targetPosition, var3);
      } else {
         return targetPosition;
      }
   }

   public ModeSetting getMode() {
      return this.mode;
   }

   public void lambda$new$0() {
      this.menxeg = NamedChoice.getChoiceByName(this.getMode().getValue(), Prediction.PredictMode.values());
   }

   private static enum PredictMode implements ModeSetting.NamedChoice {
      SIMPLE("Simple", Prediction.PredictMode::lambda$static$0),
      VELOCITY("Velocity", Prediction.PredictMode::lambda$static$1);

      public final String name;
      public final Prediction.PredictMode.PredictFunction predict;

      @Override
      public String getName() {
         return this.name;
      }

      private PredictMode(String name, Prediction.PredictMode.PredictFunction predict) {
         this.name = name;
         this.predict = predict;
      }

      public Vec3d predict(LivingEntity target, Vec3d targetPosition, double multiplier) {
         return this.predict.predict(target, targetPosition, multiplier);
      }

      public static Vec3d lambda$static$1(LivingEntity target, Vec3d targetPosition, double multiplier) {
         Vec3d var4 = SIMPLE.predict(target, targetPosition, multiplier);
         return var4.subtract(0.0, 0.025 * multiplier * multiplier, 0.0);
      }

      public static Vec3d lambda$static$0(LivingEntity target, Vec3d targetPosition, double multiplier) {
         return targetPosition.add(target.getVelocity().multiply(multiplier));
      }

      public static Prediction.PredictMode[] $values() {
         return new Prediction.PredictMode[]{SIMPLE, VELOCITY};
      }

      @FunctionalInterface
      interface PredictFunction {
         Vec3d predict(LivingEntity var1, Vec3d var2, double var3);
      }
   }
}
