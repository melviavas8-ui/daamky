package dev.daamky.client.mixin;

import dev.daamky.client.internal.FromRotationHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FireworkRocketEntity.class})
public class FireworkRocketEntityMixin {
   @Shadow
   private LivingEntity field_7616;

   @Redirect(
      method = {"tick"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
      )
   )
   public Vec3d fixFireworkVelocity(LivingEntity instance) {
      if (instance != PlatformUtils.getClass_746()) {
         return instance.getRotationVector();
      } else {
         VecRotationHelper var2 = VecRotationHelper.getInstance();
         Rotation var3 = var2.getCurrent();
         FromRotationHelper var4 = var2.getLast();
         return var4 == null ? instance.getRotationVector() : var3.initInit();
      }
   }
}
