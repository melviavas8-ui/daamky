package dev.daamky.client.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class BlockPlaceEvent extends Event<BlockPlaceEvent.BlockPlaceEventData> {
   public static final BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent();

   public static BlockPlaceEvent getInstance() {
      return blockPlaceEvent;
   }

   public static record BlockPlaceEventData(Block block, BlockState state, BlockPos pos, LivingEntity placer) {
   }
}
