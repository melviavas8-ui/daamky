package dev.daamky.client.mixin;

import dev.daamky.client.event.BlockCollisionEvent;
import dev.daamky.client.module.misc.Optimization;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractBlockState.class})
public abstract class AbstractBlockStateMixin {
   @Inject(
      method = {"getRenderType"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void cullGrassRender(CallbackInfoReturnable<BlockRenderType> cir) {
      Block var2;
      if (Optimization.flag && ((var2 = ((AbstractBlockState)this).getBlock()) instanceof PlantBlock || var2 instanceof TallPlantBlock)) {
         cir.setReturnValue(BlockRenderType.INVISIBLE);
      }
   }

   @Inject(
      method = {"isOpaque"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void optimizeLeaves(CallbackInfoReturnable<Boolean> cir) {
      if (Optimization.getInstance().checkEnabled10()) {
         Block var2 = ((AbstractBlockState)this).getBlock();
         if (var2 instanceof LeavesBlock) {
            cir.setReturnValue(true);
         }
      }
   }

   @Inject(
      method = {"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void removeXZCollision(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
      if (BlockCollisionEvent.getInstance().postAndCheckCancelled()) {
         VoxelShape var5 = (VoxelShape)cir.getReturnValue();
         double var6 = var5.getMin(Axis.Y);
         double var8 = var5.getMax(Axis.Y);
         if (var6 >= var8) {
            var8 = var6 + 0.001;
         }

         VoxelShape var10 = VoxelShapes.union(VoxelShapes.cuboid(0.0, var6, 0.0, 0.0, var8, 0.0), new VoxelShape[0]);
         cir.setReturnValue(var10);
      }
   }
}
