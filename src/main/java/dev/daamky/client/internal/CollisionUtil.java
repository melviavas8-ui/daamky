package dev.daamky.client.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class CollisionUtil {
   public static final MinecraftClient class_310 = net.minecraft.client.MinecraftClient.getInstance();
   public static final Pattern pattern = Pattern.compile("^\\w{3,16}$");

   public static boolean check() {
      return class_310.player.isUsingItem() && class_310.player.getActiveItem().getComponents().contains(DataComponentTypes.FOOD);
   }

   public static boolean initInit(Vec3d to) {
      BlockHitResult var1 = class_310.world
         .raycast(new RaycastContext(class_310.getCameraEntity().getEyePos(), to, ShapeType.OUTLINE, FluidHandling.NONE, class_310.getCameraEntity()));
      return var1 == null || var1.getType() == Type.MISS;
   }

   public static boolean check2() {
      if (class_310.player == null) {
         return false;
      } else {
         return class_310.world == null
            ? false
            : class_310.player.isSubmergedInWater()
               || class_310.world.getBlockState(class_310.player.getBlockPos().add(0, 0, 0)).getBlock() == Blocks.WATER;
      }
   }

   public static boolean isInWeb() {
      if (class_310.player == null) {
         return false;
      } else {
         Box var0 = class_310.player.getBoundingBox();
         BlockPos var1 = class_310.player.getBlockPos();
         return initInit(var1).stream().anyMatch(CollisionUtil::lambda$isInWeb$0);
      }
   }

   public static boolean initInit(Box playerBox, BlockPos blockPos) {
      return playerBox.intersects(new Box(blockPos))
         && class_310.world != null
         && class_310.world.getBlockState(blockPos).getBlock() == Blocks.COBWEB;
   }

   public static List<BlockPos> initInit(BlockPos center) {
      ArrayList var1 = new ArrayList();

      for (int var2 = center.getX() - 2; var2 <= center.getX() + 2; var2++) {
         for (int var3 = center.getY() - 1; var3 <= center.getY() + 4; var3++) {
            for (int var4 = center.getZ() - 2; var4 <= center.getZ() + 2; var4++) {
               var1.add(new BlockPos(var2, var3, var4));
            }
         }
      }

      return var1;
   }

   public static Block initInit(float x, float y, float z) {
      Vec3d var3 = class_310.player.getPos();
      return class_310.world
         .getBlockState(
            new BlockPos(new Vec3i((int)(var3.x + (double)x), (int)(var3.y + (double)y), (int)(var3.z + (double)z)))
         )
         .getBlock();
   }

   public static boolean computeFlag(Entity entity) {
      return computeFlag(entity, 0.0F);
   }

   public static boolean computeFlag(Entity entity, float expand) {
      Box var2 = class_310.player.getBoundingBox();
      Box var3 = entity.getBoundingBox().expand((double)expand, 0.0, (double)expand);
      return var2.maxX > var3.minX
         && var2.maxY > var3.minY
         && var2.maxZ > var3.minZ
         && var2.minX < var3.maxX
         && var2.minY < var3.maxY
         && var2.minZ < var3.maxZ;
   }

   public static boolean computeFlag(String name) {
      return pattern.matcher(name).matches();
   }

   public CollisionUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean lambda$isInWeb$0(Box playerBox, BlockPos pos) {
      return initInit(playerBox, pos);
   }
}
