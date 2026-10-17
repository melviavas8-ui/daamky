package dev.daamky.client.module.combat;

import dev.daamky.client.event.BlockPlaceEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.EntryHelper;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.internal.LegacySetting;
import dev.daamky.client.internal.RaytraceUtil;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.RotationHelper;
import dev.daamky.client.internal.RotationMath;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.legacy.setting.MotionConfig3;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Auto Explosion",
   category = ModuleCategory.BATTLE,
   description = "Автоматически взрывает кристаллы"
)
public class AutoExplosion extends Module {
   public static final AutoExplosion autoExplosion = new AutoExplosion();
   public final MotionConfig3 motionConfig3 = new MotionConfig3();
   public final ModeSetting neuro = new ModeSetting("Aim mode").value("Neuro").values("Neuro");
   public final FloatSetting distance = new FloatSetting("Distance").value(3.0F).range(2.5F, 6.0F).step(0.1F);
   public final FloatSetting placeDelay = new FloatSetting("Place delay").value(2.0F).range(0.0F, 20.0F).step(1.0F);
   public final FloatSetting attackDelay = new FloatSetting("Attack delay").value(5.0F).range(0.0F, 20.0F).step(1.0F);
   public final MultiBooleanSetting delayedSwapBack = new MultiBooleanSetting("Options")
      .value(new BooleanSetting("Raytrace").value(true), new BooleanSetting("No suicide").value(true), new BooleanSetting("Delayed swap back").value(false));
   public final FloatSetting swapBackDelay = new FloatSetting("Swap back delay").value(5.0F).range(1.0F, 20.0F).step(1.0F).visibleWhen(this::lambda$new$0);
   public final DelayHelper delayHelper = new DelayHelper();
   public final DelayHelper delayHelper2 = new DelayHelper();
   public final DelayHelper delayHelper3 = new DelayHelper();
   public final DelayHelper delayHelper4 = new DelayHelper();
   public Entity entity = null;
   public BlockPos class_2338 = null;
   public int intVal = -1;
   public int int2 = -1;
   public int int3 = -1;
   public boolean flag = false;
   public Runnable runnable = null;

   public AutoExplosion() {
      this.addSettings(new Setting[]{this.neuro, this.distance, this.placeDelay, this.attackDelay, this.delayedSwapBack, this.swapBackDelay});
   }

   @Override
   public void onEnable() {
      this.runProvider();
   }

   @Override
   public void onDisable() {
      this.runProvider();
   }

   @Override
   public void onEvent() {
      Subscription var1 = BlockPlaceEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var2 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var2, var1});
   }

   public void handlePlaceEvent(BlockPlaceEvent.BlockPlaceEventData event) {
      if (event.state().getBlock() == Blocks.OBSIDIAN || event.state().getBlock() == Blocks.BEDROCK) {
         this.class_2338 = event.pos();
         boolean var2 = mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
         int var3 = InventoryUtil.computeInt(Items.END_CRYSTAL, false);
         int var4 = InventoryUtil.computeInt(Items.END_CRYSTAL, true);
         this.int3 = InventoryUtil.getMc();
         if (this.delayedSwapBack.isEnabled("Delayed swap back") && mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
            this.delayHelper3.runCurrent();
         }

         if (var2) {
            if (this.class_2338 != null) {
               this.runnable = this::lambda$handlePlaceEvent$3;
               this.delayHelper2.runCurrent();
            }
         } else if (var4 == -1 && var3 != -1 && this.int3 != -1) {
            this.runnable = () -> this.lambda$handlePlaceEvent$5(var3);
            this.delayHelper2.runCurrent();
         } else if (var4 != -1) {
            this.runnable = () -> this.lambda$handlePlaceEvent$6(var4);
            this.delayHelper2.runCurrent();
         }
      }
   }

   public void runEntity() {
      if (this.entity != null && !this.entity.isAlive()) {
         this.runProvider();
      }

      if (this.runnable != null && this.delayHelper2.computeCurrent(this.placeDelay.getValue() * 50.0F)) {
         this.runnable.run();
         this.delayHelper2.runCurrent();
         this.runnable = null;
      }

      if (this.class_2338 != null && this.delayHelper.computeCurrent(this.attackDelay.getValue() * 50.0F)) {
         for (EndCrystalEntity var2 : this.findCrystals(this.class_2338)) {
            if (this.computeEnabled(var2)) {
               this.setEntity(var2);
            }
         }
      }

      if (this.delayedSwapBack.isEnabled("Delayed swap back") && this.flag) {
         int var3 = mc.player.getInventory().selectedSlot;
         if (var3 != this.int2 && var3 != this.intVal) {
            this.flag = false;
            return;
         }

         if (this.delayHelper3.computeCurrent(this.swapBackDelay.getValue() * 50.0F)) {
            mc.player.getInventory().selectedSlot = this.intVal;
            this.flag = false;
         }
      }
   }

   public void setEntity(Entity entity) {
      if (this.computeEnabled(entity) && mc.player.getAttackCooldownProgress(1.0F) >= 1.0F) {
         EntityHitResult var2 = RaytraceUtil.raytraceEntity((double)this.distance.getValue().floatValue(), this.initInit(entity), false);
         boolean var3 = var2 != null && var2.getEntity() == entity || !this.delayedSwapBack.isEnabled("Raytrace");
         if (var3) {
            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
            this.delayHelper.runCurrent();
            this.entity = entity;
         }
      }

      if (!entity.isAlive()) {
         this.entity = null;
         this.class_2338 = null;
      }
   }

   public Rotation initInit(Entity entity) {
      Vec3d var2 = RotationMath.initInit(entity);
      Rotation var3 = RotationMath.initAtan(var2);
      RotationHelper var4 = new RotationHelper(this.getMotionConfig3(), false, false).withTicksUntilReset(this.attackDelay.getValue().intValue());
      VecRotationHelper.getInstance().handleModule(var3, var4, Priority.REQUIRED, this);
      return var3;
   }

   public LegacySetting getMotionConfig3() {
      return this.motionConfig3;
   }

   public void initInit(int slot, BlockPos pos) {
      if (mc.player != null && mc.interactionManager != null) {
         boolean var3 = mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
         Vec3d var4 = Vec3d.ofBottomCenter(pos).add(0.0, 1.0, 0.0);
         BlockHitResult var5 = new BlockHitResult(var4, Direction.UP, pos, false);
         if (var3) {
            if (mc.interactionManager.interactBlock(mc.player, Hand.OFF_HAND, var5).isAccepted()) {
               mc.player.swingHand(Hand.OFF_HAND);
            }
         } else {
            if (mc.player.getInventory().selectedSlot != slot) {
               mc.player.getInventory().selectedSlot = slot;
               if (mc.getNetworkHandler() != null) {
                  mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
               }
            }

            if (mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, var5).isAccepted()
               && mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
               mc.player.swingHand(Hand.MAIN_HAND);
            }
         }
      }
   }

   public boolean computeEnabled(Entity entity) {
      if (entity != null && this.class_2338 != null && entity.isAlive() && mc.player != null) {
         return this.delayedSwapBack.isEnabled("No suicide") && mc.player.getY() < (double)this.class_2338.getY()
            ? false
            : mc.player.getEyePos().distanceTo(RotationMath.initInit(entity)) < (double)this.distance.getValue().floatValue();
      } else {
         return false;
      }
   }

   public List<EndCrystalEntity> findCrystals(BlockPos pos) {
      return mc.world.getEntitiesByClass(EndCrystalEntity.class, new Box(pos).expand(1.0, 2.0, 1.0), AutoExplosion::lambda$findCrystals$7);
   }

   public void handleBack(int slot) {
      InventoryUtil.initInit(slot, this.int3);
      if (this.class_2338 != null) {
         this.intVal = mc.player.getInventory().selectedSlot;
         this.initInit(this.int3, this.class_2338);
         if (this.delayedSwapBack.isEnabled("Delayed swap back")) {
            this.delayHelper3.runCurrent();
            this.flag = true;
            this.int2 = mc.player.getInventory().selectedSlot;
         } else {
            mc.player.getInventory().selectedSlot = this.intVal;
         }
      }

      InventoryUtil.initInit(this.int3, slot);
   }

   public void runProvider() {
      this.entity = null;
      this.class_2338 = null;
      this.intVal = -1;
      this.int3 = -1;
      this.flag = false;
      this.int2 = -1;
      this.motionConfig3.run2();
      VecRotationHelper.getInstance().setProvider(this);
      this.delayHelper2.runCurrent();
      this.delayHelper.runCurrent();
      this.delayHelper3.runCurrent();
      this.delayHelper4.runCurrent();
      this.runnable = null;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public static AutoExplosion getInstance() {
      return autoExplosion;
   }

   public static boolean lambda$findCrystals$7(EndCrystalEntity endCrystalEntity) {
      return endCrystalEntity != null && endCrystalEntity.isAlive();
   }

   public void lambda$handlePlaceEvent$6(int slotHb) {
      if (this.class_2338 != null) {
         this.intVal = mc.player.getInventory().selectedSlot;
         this.initInit(slotHb, this.class_2338);
         if (this.delayedSwapBack.isEnabled("Delayed swap back")) {
            this.delayHelper3.runCurrent();
            this.flag = true;
            this.int2 = mc.player.getInventory().selectedSlot;
         } else {
            mc.player.getInventory().selectedSlot = this.intVal;
         }
      }
   }

   public void lambda$handlePlaceEvent$5(int slotInv) {
      if (EntryHelper.check()) {
         EntryHelper.handleLong(10L, () -> this.lambda$handlePlaceEvent$4(slotInv));
      } else {
         this.handleBack(slotInv);
      }
   }

   public void lambda$handlePlaceEvent$4(int slotInv) {
      this.handleBack(slotInv);
   }

   public void lambda$handlePlaceEvent$3() {
      this.initInit(this.int3, this.class_2338);
   }

   public void lambda$onEvent$2(TickEvent event) {
      this.runEntity();
   }

   public void lambda$onEvent$1(BlockPlaceEvent.BlockPlaceEventData event) {
      this.handlePlaceEvent(event);
   }

   public Boolean lambda$new$0() {
      return this.delayedSwapBack.isEnabled("Delayed swap back");
   }
}
