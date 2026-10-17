package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class LegacySetting implements MinecraftAccess, ModeSetting.NamedChoice {
   public final String name;
   public boolean flag = false;
   public long timeStamp = 0L;
   public float floatVal = 0.0F;
   public float mubizl = 0.0F;
   public static final long long2 = 220L;

   public LegacySetting(String name) {
      this.name = name;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public Rotation computeRotation(Rotation currentRotation, Rotation targetRotation) {
      return this.initNext(currentRotation, targetRotation, null, null);
   }

   public Rotation computeRotation(Rotation currentRotation, Rotation targetRotation, Vec3d vec3d) {
      return this.initNext(currentRotation, targetRotation, vec3d, null);
   }

   public abstract Rotation initNext(Rotation var1, Rotation var2, Vec3d var3, Entity var4);

   public void runCurrent() {
      if (mc.player != null && !this.flag) {
         Rotation var1 = VecRotationHelper.getInstance().getCurrent();
         this.floatVal = var1.getYaw();
         this.mubizl = var1.getPitch();
         this.timeStamp = System.currentTimeMillis();
         this.flag = true;
      }
   }

   public boolean check() {
      return this.flag;
   }

   public void run() {
      this.flag = false;
   }

   public Rotation initYaw(Rotation currentRotation, Rotation targetRotation) {
      if (!this.flag) {
         return targetRotation;
      } else {
         long var3 = System.currentTimeMillis() - this.timeStamp;
         if (var3 >= 220L) {
            this.flag = false;
            return targetRotation;
         } else {
            float var5 = MathHelper.clamp((float)var3 / 220.0F, 0.0F, 1.0F);
            float var6 = Easing.CUBIC_OUT.apply(var5);
            float var7 = 1.0F - var5;
            float var8 = (float)(mc.player != null ? mc.player.age : 0)
               + (mc.getRenderTickCounter() != null ? mc.getRenderTickCounter().getTickDelta(false) : 0.0F);
            float var9 = (float)(Math.sin((double)var8 * 0.31) * 0.22) * var7;
            float var10 = (float)(Math.cos((double)var8 * 0.45) * 0.15) * var7;
            float var11 = MathHelper.wrapDegrees(this.floatVal + MathHelper.wrapDegrees(targetRotation.getYaw() - this.floatVal) * var6 + var9);
            float var12 = MathHelper.clamp(this.mubizl + (targetRotation.getPitch() - this.mubizl) * var6 + var10, -89.0F, 89.0F);
            float var13 = GameTimer.getMc2();
            if (var13 > 1.0E-4F) {
               float var14 = MathHelper.wrapDegrees(var11 - currentRotation.getYaw());
               float var15 = var12 - currentRotation.getPitch();
               float var16 = (float)Math.round(var14 / var13) * var13;
               float var17 = (float)Math.round(var15 / var13) * var13;
               return new Rotation(
                  MathHelper.wrapDegrees(currentRotation.getYaw() + var16), MathHelper.clamp(currentRotation.getPitch() + var17, -89.0F, 89.0F)
               );
            } else {
               return new Rotation(var11, var12);
            }
         }
      }
   }
}
