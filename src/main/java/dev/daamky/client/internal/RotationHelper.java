package dev.daamky.client.internal;

import dev.daamky.client.legacy.setting.MotionConfig4;
import dev.daamky.client.legacy.setting.MotionConfig6;
import dev.daamky.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class RotationHelper {
   public final LegacySetting rotationMode;
   public final boolean moveCorrection;
   public final boolean freeCorrection;
   public final float floatVal = 2.0F;
   public int ticksUntilReset = 5;
   public boolean clientLook;
   public boolean noClamp;
   public static final RotationHelper rotationHelper = new RotationHelper(new MotionConfig6(), true, true);
   public static final RotationHelper rotationHelper2 = new RotationHelper(new MotionConfig6(), true);
   public static final RotationHelper rotationHelper3 = new RotationHelper(new MotionConfig4(), true);

   public RotationHelper(LegacySetting rotationMode, boolean moveCorrection) {
      this.rotationMode = rotationMode;
      this.moveCorrection = moveCorrection;
      this.freeCorrection = false;
   }

   public RotationHelper(LegacySetting rotationMode, boolean moveCorrection, boolean freeCorrection) {
      this.rotationMode = rotationMode;
      this.moveCorrection = moveCorrection;
      this.freeCorrection = freeCorrection;
   }

   public RotationHelper(boolean moveCorrection) {
      this(new MotionConfig6(), moveCorrection);
   }

   public RotationHelper() {
      this(new MotionConfig6(), true, true);
   }

   public FromRotationHelper initClamp(Rotation rotation, Vec3d vec, Entity entity, Module provider) {
      return new FromRotationHelper(rotation, vec, entity, this.rotationMode, this.ticksUntilReset, 2.0F, this.moveCorrection, this.freeCorrection, provider)
         .withClientLook(this.clientLook)
         .withNoClamp(this.noClamp);
   }

   public FromRotationHelper initClamp(Rotation rotation, Module provider) {
      return new FromRotationHelper(rotation, null, null, this.rotationMode, this.ticksUntilReset, 2.0F, this.moveCorrection, this.freeCorrection, provider)
         .withClientLook(this.clientLook)
         .withNoClamp(this.noClamp);
   }

   public FromRotationHelper initClamp(Rotation rotation, Vec3d vec, Entity entity, boolean moveCorrection, boolean freeCorrection, Module provider) {
      return new FromRotationHelper(rotation, vec, entity, this.rotationMode, this.ticksUntilReset, 2.0F, moveCorrection, freeCorrection, provider)
         .withClientLook(this.clientLook)
         .withNoClamp(this.noClamp);
   }

   public RotationHelper withTicksUntilReset(int ticksUntilReset) {
      this.ticksUntilReset = ticksUntilReset;
      return this;
   }

   public RotationHelper withClientLook(boolean clientLook) {
      this.clientLook = clientLook;
      return this;
   }

   public RotationHelper withNoClamp(boolean noClamp) {
      this.noClamp = noClamp;
      return this;
   }
}
