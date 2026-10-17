package dev.daamky.client.internal;

import dev.daamky.client.module.Module;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class FromRotationHelper implements MinecraftAccess {
   public final Rotation rotation;
   public final Vec3d vec3d;
   public final Entity entity;
   public final LegacySetting rotationMode;
   public final int ticksUntilReset;
   public final float resetThreshold;
   public final boolean moveCorrection;
   public boolean freeMoveCorrection;
   public boolean clientLook;
   public boolean noClamp;
   public final Module provider;

   public FromRotationHelper(
      Rotation rotation,
      Vec3d vec3d,
      Entity entity,
      LegacySetting rotationMode,
      int ticksUntilReset,
      float resetThreshold,
      boolean moveCorrection,
      boolean freeMoveCorrection,
      Module provider
   ) {
      this.rotation = rotation;
      this.vec3d = vec3d;
      this.entity = entity;
      this.rotationMode = rotationMode;
      this.ticksUntilReset = ticksUntilReset;
      this.resetThreshold = resetThreshold;
      this.moveCorrection = moveCorrection;
      this.freeMoveCorrection = freeMoveCorrection;
      this.provider = provider;
   }

   public Rotation initRotation(Rotation fromRotation, boolean isResetting) {
      if (isResetting || this.rotationMode != null && this.rotationMode.check()) {
         float var3 = CameraManager.getFlag() ? CameraManager.getFloat() : (mc.player != null ? mc.player.getYaw() : 0.0F);
         float var4 = CameraManager.getFlag() ? CameraManager.getMrula() : (mc.player != null ? mc.player.getPitch() : 0.0F);
         Rotation var5 = new Rotation(var3, var4);
         return this.rotationMode != null ? this.rotationMode.initYaw(fromRotation, var5) : fromRotation;
      } else {
         return this.rotationMode.initNext(fromRotation, this.rotation, this.vec3d, this.entity);
      }
   }

   public Rotation getRotation() {
      return this.rotation;
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public LegacySetting getRotationMode() {
      return this.rotationMode;
   }

   public int getTicksUntilReset() {
      return this.ticksUntilReset;
   }

   public float getResetThreshold() {
      return this.resetThreshold;
   }

   public boolean isMoveCorrection() {
      return this.moveCorrection;
   }

   public boolean isFreeMoveCorrection() {
      return this.freeMoveCorrection;
   }

   public boolean isClientLook() {
      return this.clientLook;
   }

   public boolean isNoClamp() {
      return this.noClamp;
   }

   public Module getProvider() {
      return this.provider;
   }

   public FromRotationHelper withFreeMoveCorrection(boolean freeMoveCorrection) {
      this.freeMoveCorrection = freeMoveCorrection;
      return this;
   }

   public FromRotationHelper withClientLook(boolean clientLook) {
      this.clientLook = clientLook;
      return this;
   }

   public FromRotationHelper withNoClamp(boolean noClamp) {
      this.noClamp = noClamp;
      return this;
   }
}
