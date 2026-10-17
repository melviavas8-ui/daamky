package dev.daamky.client.internal;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class StopHelper {
   public static final MinecraftClient class_310 = net.minecraft.client.MinecraftClient.getInstance();
   public boolean flag = false;
   public boolean flag2 = false;
   public boolean flag3 = false;
   public BlockPos goal = null;
   public int intVal = 0;
   public Vec3d class_243 = null;

   public void handleGoal(BlockPos goal, int tolerance) {
      if (goal != null && class_310.player != null) {
         this.goal = goal;
         this.flag = true;
         this.flag2 = false;
         this.flag3 = false;
         this.intVal = 0;
         this.class_243 = null;
         if (class_310.player.networkHandler != null) {
            class_310.player.networkHandler.sendChatMessage("#goto " + goal.getX() + " " + goal.getY() + " " + goal.getZ());
         }
      }
   }

   public void runGoal() {
      if (this.flag && this.goal != null && class_310.player != null && class_310.world != null) {
         double var1 = class_310.player.getBlockPos().getSquaredDistance(this.goal);
         if (var1 <= 9.0) {
            this.flag2 = true;
            this.runStop();
         } else {
            Vec3d var3 = class_310.player.getPos();
            if (this.class_243 != null && var3.squaredDistanceTo(this.class_243) < 0.01) {
               this.intVal++;
               if (this.intVal > 100) {
                  this.flag3 = true;
                  this.runStop();
                  return;
               }
            } else {
               this.intVal = 0;
            }

            this.class_243 = var3;
            if (class_310.player.getY() < 20.0) {
               boolean var4 = false;
               BlockPos var5 = class_310.player.getBlockPos();

               for (int var6 = 1; var6 <= 20; var6++) {
                  BlockPos var7 = var5.down(var6);
                  BlockState var8 = class_310.world.getBlockState(var7);
                  if (!var8.isAir() && var8.isSolidBlock(class_310.world, var7)) {
                     var4 = true;
                     break;
                  }
               }

               if (!var4) {
                  this.flag3 = true;
                  this.runStop();
               }
            }
         }
      }
   }

   public void runStop() {
      this.flag = false;
      if (class_310.player != null && class_310.player.networkHandler != null) {
         class_310.player.networkHandler.sendChatMessage("#stop");
      }
   }

   public boolean check() {
      return this.flag;
   }

   public boolean check2() {
      return this.flag2;
   }

   public boolean check3() {
      return this.flag3;
   }

   public BlockPos getGoal() {
      return this.goal;
   }
}
