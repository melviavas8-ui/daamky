package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.RotationEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.RotationHelper;
import dev.daamky.client.internal.RotationMath;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.legacy.setting.MotionConfig6;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

@ModuleInfo(
   name = "Nuker",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Автоматически ломает выбранные блоки вокруг игрока"
)
public class Nuker extends Module {
   public static final Nuker nuker = new Nuker();
   public final FloatSetting reach = new FloatSetting("Reach").value(4.5F).range(1.0F, 4.5F).step(0.1F);
   public final FloatSetting radius = new FloatSetting("Radius").value(4.0F).range(1.0F, 6.0F).step(1.0F);
   public final ModeSetting flat = new ModeSetting("Shape").value("Sphere").values("Sphere", "Cube", "Flat");
   public final ModeSetting farthest = new ModeSetting("Priority").value("Nearest").values("Nearest", "Farthest");
   public final BooleanSetting rotate = new BooleanSetting("Rotate").value(true);
   public final BooleanSetting silent = new BooleanSetting("Silent").value(true).visibleWhen(this::lambda$new$0);
   public final BooleanSetting swing = new BooleanSetting("Swing").value(true);
   public final BooleanSetting whitelist = new BooleanSetting("Whitelist").value(false);
   public final StringSetting obsidianNetherrack = new StringSetting("Blocks").value("").placeholder("obsidian, netherrack").visibleWhen(this::lambda$new$1);
   public final RotationHelper rotationHelper = new RotationHelper(new MotionConfig6(), true);
   public BlockPos class_2338;
   public Direction class_2350;
   public Vec3d class_243;
   public long mpunu;

   public Nuker() {
      this.addSettings(
         new Setting[]{this.reach, this.radius, this.flat, this.farthest, this.rotate, this.silent, this.swing, this.whitelist, this.obsidianNetherrack}
      );
   }

   @Override
   public void onEvent() {
      Subscription var1 = RotationEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var2 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   @Override
   public void onDisable() {
      super.onDisable();
      VecRotationHelper.getInstance().setProvider(this);
      if (mc.interactionManager != null) {
         mc.interactionManager.cancelBlockBreaking();
      }

      this.class_2338 = null;
      this.class_2350 = null;
      this.class_243 = null;
   }

   public void runProvider() {
      if (mc.player != null && mc.world != null && mc.currentScreen == null) {
         if (this.class_2338 != null) {
            Direction var2 = this.initEquals(this.class_2338);
            if (var2 != null && this.computeFlag(this.class_2338)) {
               this.class_2350 = var2;
               this.class_243 = computeClass_243(this.class_2338, var2);
               this.runLook();
               return;
            }

            this.class_2338 = null;
         }

         Nuker.Candidate var1;
         if ((var1 = this.initInit()) == null) {
            VecRotationHelper.getInstance().setProvider(this);
         } else {
            this.class_2338 = var1.pos;
            this.class_2350 = var1.face;
            this.class_243 = var1.aim;
            this.runLook();
         }
      } else {
         VecRotationHelper.getInstance().setProvider(this);
      }
   }

   public void runLook() {
      if (!this.rotate.getValue()) {
         VecRotationHelper.getInstance().setProvider(this);
      } else {
         Rotation var1 = RotationMath.initInit(RotationMath.initAtan(this.class_243));
         this.rotationHelper.withClientLook(!this.silent.getValue());
         VecRotationHelper.getInstance().handleModule(var1, this.rotationHelper, Priority.HIGH, this);
      }
   }

   public void initEquals() {
      if (mc.player != null && mc.world != null && mc.interactionManager != null) {
         if (mc.currentScreen == null && !mc.player.isSpectator()) {
            if (this.class_2338 != null) {
               if (!this.computeFlag(this.class_2338)) {
                  this.class_2338 = null;
                  mc.interactionManager.cancelBlockBreaking();
               } else {
                  if (this.rotate.getValue()) {
                     Rotation var3 = VecRotationHelper.getInstance().getServerRotation();
                     Vec3d var4 = mc.player.getRotationVector(var3.getPitch(), var3.getYaw());
                     Vec3d var5 = mc.player.getEyePos();
                     BlockHitResult var6 = mc.world
                        .raycast(
                           new RaycastContext(
                              var5,
                              var5.add(var4.multiply((double)this.reach.getValue().floatValue() + 0.5)),
                              ShapeType.OUTLINE,
                              FluidHandling.NONE,
                              mc.player
                           )
                        );
                     if (var6.getType() != Type.BLOCK || !var6.getBlockPos().equals(this.class_2338)) {
                        return;
                     }

                     this.class_2350 = var6.getSide();
                  }

                  mc.interactionManager.updateBlockBreakingProgress(this.class_2338, this.class_2350);
                  if (this.swing.getValue()) {
                     mc.player.swingHand(Hand.MAIN_HAND);
                  }

                  BlockState var1;
                  if ((var1 = mc.world.getBlockState(this.class_2338)).isAir() || var1.isReplaceable()) {
                     this.class_2338 = null;
                  }
               }
            }
         }
      }
   }

   public Nuker.Candidate initInit() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.mpunu < 60L) {
         return null;
      } else {
         this.mpunu = var1;
         ClientPlayerEntity var3 = mc.player;
         Vec3d var4 = var3.getEyePos();
         double var5 = (double)this.reach.getValue().floatValue();
         int var7 = Math.min(this.radius.getValue().intValue(), (int)Math.ceil(var5) + 1);
         boolean var8 = this.farthest.is("Nearest");
         boolean var9 = this.flat.is("Flat");
         boolean var10 = this.flat.is("Sphere");
         BlockPos var11 = var3.getBlockPos();
         int var12 = var9 ? 0 : -var7;
         int var13 = var9 ? 1 : var7;
         Nuker.Candidate var14 = null;
         double var15 = var8 ? Double.MAX_VALUE : -1.0;
         Mutable var17 = new Mutable();

         for (int var18 = -var7; var18 <= var7; var18++) {
            for (int var19 = -var7; var19 <= var7; var19++) {
               for (int var20 = var12; var20 <= var13; var20++) {
                  if (!var10 || var18 * var18 + var20 * var20 + var19 * var19 <= var7 * var7) {
                     var17.set(var11.getX() + var18, var11.getY() + var20, var11.getZ() + var19);
                     BlockState var25 = mc.world.getBlockState(var17);
                     double var21;
                     BlockHitResult var23;
                     Vec3d var24;
                     if (!var25.isAir()
                        && !var25.isReplaceable()
                        && !(var25.getHardness(mc.world, var17) < 0.0F)
                        && (!this.whitelist.getValue() || this.computeCase(var25))
                        && !((var24 = net.minecraft.util.math.Vec3d.ofCenter(var17)).distanceTo(var4) > var5 + 1.0)
                        && (var23 = mc.world.raycast(new RaycastContext(var4, var24, ShapeType.OUTLINE, FluidHandling.NONE, var3))).getType()
                           == Type.BLOCK
                        && var23.getBlockPos().equals(var17)
                        && !((var21 = var4.distanceTo(var23.getPos())) > var5)
                        && (var8 ? var21 < var15 : var21 > var15)) {
                        var15 = var21;
                        var14 = new Nuker.Candidate(var17.toImmutable(), var23.getSide(), computeClass_243(var17, var23.getSide()));
                     }
                  }
               }
            }
         }

         return var14;
      }
   }

   public Direction initEquals(BlockPos p) {
      if (mc.player == null) {
         return null;
      } else {
         Vec3d var3 = mc.player.getEyePos();
         BlockHitResult var4 = mc.world
            .raycast(new RaycastContext(var3, net.minecraft.util.math.Vec3d.ofCenter(p), ShapeType.OUTLINE, FluidHandling.NONE, mc.player));
         return var4.getType() == Type.BLOCK
               && var4.getBlockPos().equals(p)
               && var3.distanceTo(var4.getPos()) <= (double)this.reach.getValue().floatValue()
            ? var4.getSide()
            : null;
      }
   }

   public boolean computeFlag(BlockPos p) {
      BlockState var2 = mc.world.getBlockState(p);
      return !var2.isAir() && !var2.isReplaceable() ? var2.getHardness(mc.world, p) >= 0.0F : false;
   }

   public boolean computeCase(BlockState st) {
      String var2 = this.obsidianNetherrack.getValue();
      if (var2 != null && !var2.isBlank()) {
         String var3 = Registries.BLOCK.getId(st.getBlock()).getPath().toLowerCase();

         for (String var7 : var2.toLowerCase().split(",")) {
            String var8 = var7.trim();
            if (!var8.isEmpty() && var3.contains(var8)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static Vec3d computeClass_243(BlockPos pos, Direction face) {
      return net.minecraft.util.math.Vec3d.ofCenter(pos).add(net.minecraft.util.math.Vec3d.of(face.getVector()).multiply(0.5));
   }

   public static Nuker getInstance() {
      return nuker;
   }

   public void lambda$onEvent$3(TickEvent e) {
      this.initEquals();
   }

   public void lambda$onEvent$2(RotationEvent e) {
      this.runProvider();
   }

   public Boolean lambda$new$1() {
      return this.whitelist.getValue();
   }

   public Boolean lambda$new$0() {
      return this.rotate.getValue();
   }

   private static class Candidate {
      public final BlockPos pos;
      public final Direction face;
      public final Vec3d aim;

      public Candidate(BlockPos pos, Direction face, Vec3d aim) {
         this.pos = pos;
         this.face = face;
         this.aim = aim;
      }
   }
}
