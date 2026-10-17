package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.MovementInputEvent;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.RotationEvent;
import dev.daamky.client.event.RotationUpdateEvent;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class VecRotationHelper implements MinecraftAccess {
   public static final VecRotationHelper vecRotationHelper = new VecRotationHelper();
   public FromRotationHelper lastRotationPlan;
   public final TaskScheduler<FromRotationHelper> taskScheduler = new TaskScheduler<>();
   public Rotation currentRotation;
   public Rotation previousRotation;
   public Rotation serverRotation = Rotation.rotation2;

   public void load() {
      PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$0));
      MovementInputEvent.getInstance().subscribe(new EventListener<>(VecRotationHelper::lambda$load$1));
      UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$2));
   }

   public void initRotation(Rotation value) {
      this.previousRotation = value == null
         ? (
            this.currentRotation != null
               ? this.currentRotation
               : (mc.player != null ? new Rotation(mc.player.getYaw(), mc.player.getPitch()) : Rotation.rotation2)
         )
         : this.currentRotation;
      this.currentRotation = value;
   }

   public Rotation getCurrent() {
      if (mc.player == null) {
         return Rotation.rotation2;
      } else {
         return this.currentRotation != null ? this.currentRotation : RotationMath.initInit(mc.player.getRotationClient());
      }
   }

   public Rotation getPrevious() {
      if (mc.player == null) {
         return Rotation.rotation2;
      } else {
         return this.previousRotation != null ? this.previousRotation : RotationMath.initInit(mc.player.getRotationClient());
      }
   }

   public FromRotationHelper getLast() {
      return this.taskScheduler.getPeek() != null ? this.taskScheduler.getPeek() : this.lastRotationPlan;
   }

   public void handleRotation(Rotation.VecRotation vecRotation, LivingEntity entity, RotationHelper configurable, Priority requestPriority, Module provider) {
      this.initAdd(configurable.initClamp(vecRotation.rotation(), vecRotation.vec(), entity, provider), requestPriority, provider);
   }

   public void handleModule(Rotation rotation, RotationHelper configurable, Priority requestPriority, Module provider) {
      this.initAdd(configurable.initClamp(rotation, provider), requestPriority, provider);
   }

   public void initAdd(FromRotationHelper plan, Priority requestPriority, Module provider) {
      this.taskScheduler.addTask(new TaskScheduler.Task<>(plan.getTicksUntilReset(), requestPriority.getPriority(), provider, plan));
   }

   public void setProvider(Module provider) {
      FromRotationHelper var2 = this.taskScheduler.getPeek();
      if (var2 != null && var2.getProvider() == provider && var2.getRotationMode() != null) {
         if (!var2.getRotationMode().check()) {
            var2.getRotationMode().runCurrent();
         }

         this.lastRotationPlan = var2;
      }

      this.taskScheduler.removeTaskByProvider(provider);
      if (this.taskScheduler.getPeek() == null
         && (this.lastRotationPlan == null || this.lastRotationPlan.getRotationMode() == null || !this.lastRotationPlan.getRotationMode().check())) {
         this.initRotation(null);
         this.lastRotationPlan = null;
         RotationTaskHelper.rotationTaskHelper.runCurrent();
         CameraManager.setState(false);
      }
   }

   public void handleRotation(Module provider) {
      this.taskScheduler.removeTaskByProvider(provider);
      if (this.lastRotationPlan != null && this.lastRotationPlan.getProvider() == provider && this.lastRotationPlan.getRotationMode() != null) {
         this.lastRotationPlan.getRotationMode().run();
      }

      this.lastRotationPlan = null;
      this.initRotation(null);
      RotationTaskHelper.rotationTaskHelper.runCurrent();
      CameraManager.setState(false);
   }

   public void initRotation() {
      RotationEvent.getInstance().postAndCheckCancelled();
      FromRotationHelper var1 = this.getLast();
      if (var1 == null) {
         if (this.currentRotation != null || this.lastRotationPlan != null || RotationTaskHelper.rotationTaskHelper.equals() || CameraManager.getFlag()) {
            this.initRotation(null);
            this.lastRotationPlan = null;
            RotationTaskHelper.rotationTaskHelper.runCurrent();
            CameraManager.setState(false);
         }
      } else {
         Rotation var2 = RotationMath.initInit(mc.player != null ? mc.player.getRotationClient() : Vec2f.ZERO);
         boolean var3 = this.taskScheduler.getPeek() == null;
         if (var3) {
            if (this.lastRotationPlan == null || this.lastRotationPlan.getRotationMode() == null || !this.lastRotationPlan.getRotationMode().check()) {
               if (this.currentRotation != null || this.lastRotationPlan != null || RotationTaskHelper.rotationTaskHelper.equals() || CameraManager.getFlag()) {
                  this.initRotation(null);
                  this.lastRotationPlan = null;
                  RotationTaskHelper.rotationTaskHelper.runCurrent();
                  CameraManager.setState(false);
               }

               return;
            }

            var1 = this.lastRotationPlan;
         }

         Rotation var4 = var1.initRotation(this.currentRotation != null ? this.currentRotation : var2, var3).initYaw();
         if (!var1.isNoClamp()) {
            Rotation var5 = this.currentRotation != null ? this.currentRotation : var2;
            float var6 = MathHelper.wrapDegrees(var4.getYaw() - var5.getYaw());
            float var7 = var4.getPitch() - var5.getPitch();
            float var8 = 80.0F;
            float var9 = 40.0F;
            float var10 = MathHelper.clamp(var6, -var8, var8);
            float var11 = MathHelper.clamp(var7, -var9, var9);
            var4 = new Rotation(var5.getYaw() + var10, MathHelper.clamp(var5.getPitch() + var11, -90.0F, 90.0F)).initYaw();
         }

         this.initRotation(var4);
         RotationTaskHelper.handleCurrent(var4, 360.0F, 360.0F, 360.0F, 360.0F, 10, 1, var1.isClientLook());
         this.lastRotationPlan = var1;
         if (var3 && var1.getRotationMode() != null && !var1.getRotationMode().check()) {
            this.initRotation(null);
            this.lastRotationPlan = null;
            RotationTaskHelper.rotationTaskHelper.runCurrent();
            CameraManager.setState(false);
         }
      }
   }

   public FromRotationHelper getLastRotationPlan() {
      return this.lastRotationPlan;
   }

   public TaskScheduler<FromRotationHelper> getTaskScheduler() {
      return this.taskScheduler;
   }

   public Rotation getCurrentRotation() {
      return this.currentRotation;
   }

   public Rotation getServerRotation() {
      return this.serverRotation;
   }

   public void setLastRotationPlan(FromRotationHelper lastRotationPlan) {
      this.lastRotationPlan = lastRotationPlan;
   }

   public void setCurrentRotation(Rotation currentRotation) {
      this.currentRotation = currentRotation;
   }

   public void setPreviousRotation(Rotation previousRotation) {
      this.previousRotation = previousRotation;
   }

   public void setServerRotation(Rotation serverRotation) {
      this.serverRotation = serverRotation;
   }

   public static VecRotationHelper getInstance() {
      return vecRotationHelper;
   }

   public void lambda$load$2(UpdateEvent event) {
      this.taskScheduler.setDeltaTime(1);
   }

   public static void lambda$load$1(MovementInputEvent.MovementInputEventData event) {
      RotationUpdateEvent.getInstance().postAndCheckCancelled();
   }

   public void lambda$load$0(PacketEvent.PacketEventData event) {
      if (event.isSend()) {
         Rotation var2;
         if (event.packet() instanceof PlayerMoveC2SPacket var4) {
            if (!var4.changesLook()) {
               return;
            }

            var2 = new Rotation(var4.getYaw(1.0F), var4.getPitch(1.0F));
         } else {
            if (!(event.packet() instanceof PlayerPositionLookS2CPacket var5)) {
               return;
            }

            var2 = new Rotation(var5.change().yaw(), var5.change().pitch());
         }

         if (!PacketEvent.getInstance().isCancelled()) {
            this.serverRotation = var2;
         }
      }
   }
}
