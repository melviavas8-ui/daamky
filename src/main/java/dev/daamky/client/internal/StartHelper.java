package dev.daamky.client.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class StartHelper {
   public static final MinecraftClient class_310 = net.minecraft.client.MinecraftClient.getInstance();

   public static List<BlockPos> compute(BlockPos start, BlockPos target, int maxIterations) {
      ArrayList var3 = new ArrayList();
      if (class_310.world == null) {
         return var3;
      } else {
         PriorityQueue var4 = new PriorityQueue<>(Comparator.comparingDouble(StartHelper::lambda$compute$0));
         HashSet var5 = new HashSet();
         HashMap var6 = new HashMap();
         StartHelper.Node var7 = new StartHelper.Node(start);
         var7.doubleVal = 0.0;
         var7.double2 = start.getSquaredDistance(target);
         var7.run();
         var4.add(var7);
         var6.put(start, var7);
         int var8 = 0;
         StartHelper.Node var9 = var7;

         while (!var4.isEmpty() && var8 < maxIterations) {
            StartHelper.Node var10 = (StartHelper.Node)var4.poll();
            var8++;
            if (var10.double2 < var9.double2) {
               var9 = var10;
            }

            if (var10.pos.getSquaredDistance(target) <= 2.0) {
               return initAdd(var7, var10);
            }

            var5.add(var10.pos);

            for (BlockPos var12 : initAdd(var10.pos)) {
               if (!var5.contains(var12)) {
                  double var13 = 1.0;
                  if (var12.getY() != var10.pos.getY()) {
                     var13 = 1.5;
                  }

                  double var15 = var10.doubleVal + var13;
                  StartHelper.Node var17 = var6.getOrDefault(var12, new StartHelper.Node(var12));
                  if (!var4.contains(var17) || var15 < var17.doubleVal) {
                     var17.doubleVal = var15;
                     var17.double2 = var12.getSquaredDistance(target);
                     var17.run();
                     var17.node = var10;
                     if (!var4.contains(var17)) {
                        var4.add(var17);
                        var6.put(var12, var17);
                     }
                  }
               }
            }
         }

         return (List<BlockPos>)(var9 != var7 ? initAdd(var7, var9) : var3);
      }
   }

   public static List<BlockPos> initAdd(StartHelper.Node startNode, StartHelper.Node endNode) {
      ArrayList var2 = new ArrayList();

      for (StartHelper.Node var3 = endNode; var3 != null && var3 != startNode; var3 = var3.node) {
         var2.add(var3.pos);
      }

      Collections.reverse(var2);
      return var2;
   }

   public static boolean computeFlag(BlockPos pos) {
      return computeFlag2(pos)
         && computeFlag2(pos.up())
         && !computeFlag2(pos.down())
         && !computeFlag3(pos)
         && !computeFlag3(pos.down());
   }

   public static boolean computeFlag2(BlockPos pos) {
      if (class_310.world == null) {
         return false;
      } else {
         BlockState var1 = class_310.world.getBlockState(pos);
         return var1.isReplaceable() || !var1.isOpaque() || var1.getCollisionShape(class_310.world, pos).isEmpty();
      }
   }

   public static boolean computeFlag3(BlockPos pos) {
      if (class_310.world == null) {
         return false;
      } else {
         BlockState var1 = class_310.world.getBlockState(pos);
         return var1.getFluidState().isStill()
            || var1.getBlock() == Blocks.LAVA
            || var1.getBlock() == Blocks.CACTUS
            || var1.getBlock() == Blocks.FIRE
            || var1.getBlock() == Blocks.SOUL_FIRE
            || var1.getBlock() == Blocks.SWEET_BERRY_BUSH;
      }
   }

   public static double lambda$compute$0(StartHelper.Node n) {
      return n.double3;
   }

   public static class Node {
      public final BlockPos pos;
      public StartHelper.Node node;
      public double doubleVal;
      public double double2;
      public double double3;

      public Node(BlockPos pos) {
         this.pos = pos;
      }

      public void run() {
         this.double3 = this.doubleVal + this.double2;
      }

      public boolean computePos(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            StartHelper.Node var2 = (StartHelper.Node)o;
            return this.pos.equals(var2.pos);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.pos.hashCode();
      }
   }
}
