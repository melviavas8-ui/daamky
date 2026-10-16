package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.util.PlatformUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Item.class})
public abstract class ItemMixin {
   @ModifyExpressionValue(
      method = {"raycast"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;"
      )}
   )
   private static Vec3d computeCurrent(Vec3d original, World world, PlayerEntity player, FluidHandling fluidHandling) {
      Rotation var4 = VecRotationHelper.getInstance().getCurrentRotation();
      return player == PlatformUtils.getClass_746() && var4 != null ? var4.initInit() : original;
   }
}
