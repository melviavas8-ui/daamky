package dev.daamky.client.internal;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public class ClearHelper {
   public static final ClearHelper clearHelper = new ClearHelper();
   public List<BlockPos> list = new ArrayList<>();
   public BlockPos target = null;

   public static ClearHelper getInstance() {
      return clearHelper;
   }

   public void initInit(List<BlockPos> path, BlockPos target) {
      this.list = path != null ? new ArrayList<>(path) : new ArrayList<>();
      this.target = target;
   }

   public List<BlockPos> getList() {
      return this.list;
   }

   public BlockPos getTarget() {
      return this.target;
   }

   public void runClear() {
      this.list.clear();
      this.target = null;
   }
}
