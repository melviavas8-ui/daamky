package dev.daamky.client.model.enums;

import dev.daamky.client.setting.ModeSetting;
import java.util.function.Function;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public enum AnchorPoint implements ModeSetting.NamedChoice {
   EYES("Eyes", AnchorPoint::lambda$static$0),
   CENTER("Center", AnchorPoint::lambda$static$1);

   public final String name;
   public final Function<LivingEntity, Vec3d> position;

   private AnchorPoint(String name, Function<LivingEntity, Vec3d> position) {
      this.name = name;
      this.position = position;
   }

   public Vec3d getPosition(LivingEntity target) {
      return this.position.apply(target);
   }

   @Override
   public String getName() {
      return this.name;
   }

   public static Vec3d lambda$static$1(LivingEntity target) {
      return target.getPos().add(0.0, (double)target.getHeight() / 2.0, 0.0);
   }

   public static Vec3d lambda$static$0(LivingEntity target) {
      return target.getEyePos();
   }

   public static AnchorPoint[] $values() {
      return new AnchorPoint[]{EYES, CENTER};
   }
}
