package dev.daamky.client.internal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Rotation {
   public final float yaw;
   public final float pitch;
   public boolean isNormalized;
   public static final Rotation rotation = new Rotation(0.0F, 0.0F);
   public static final Rotation rotation2 = new Rotation(0.0F, 0.0F);

   public Rotation(float yaw, float pitch) {
      this(yaw, pitch, false);
   }

   public Rotation(float yaw, float pitch, boolean isNormalized) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.isNormalized = isNormalized;
   }

   public static Rotation computeRotation(Vec3d point, Vec3d from) {
      return initAtan(point.subtract(from));
   }

   public static Rotation initAtan(Vec3d lookVec) {
      double var1 = lookVec.x;
      double var3 = lookVec.y;
      double var5 = lookVec.z;
      return new Rotation(
         (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var5, var1)) - 90.0),
         (float)MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(var3, Math.sqrt(var1 * var1 + var5 * var5))))
      );
   }

   public static Rotation initInit(Vec3d vec3d) {
      double var1 = vec3d.x - MinecraftClient.getInstance().player.getX();
      double var3 = vec3d.y - MinecraftClient.getInstance().player.getEyeY();
      double var5 = vec3d.z - MinecraftClient.getInstance().player.getZ();
      double var7 = (double)MathHelper.sqrt((float)(var1 * var1 + var5 * var5));
      float var9 = (float)(MathHelper.atan2(var5, var1) * (180.0 / Math.PI) - 90.0);
      float var10 = (float)(-MathHelper.atan2(var3, var7) * (180.0 / Math.PI));
      return new Rotation(var9, var10);
   }

   public float computeMin(Rotation other) {
      return Math.min(this.initYaw(other).sqrt(), 180.0F);
   }

   public DeltaHelper initYaw(Rotation other) {
      return new DeltaHelper(MathHelper.wrapDegrees(other.yaw - this.yaw), MathHelper.wrapDegrees(other.pitch - this.pitch));
   }

   public float computeAbs(Rotation target) {
      float var2 = MathHelper.wrapDegrees(target.getYaw() - this.yaw);
      float var3 = target.getPitch() - this.pitch;
      return (float)Math.hypot((double)Math.abs(var2), (double)Math.abs(var3));
   }

   public boolean computeFlag(Rotation other, float tolerance) {
      return this.computeMin(other) <= tolerance;
   }

   public Vec3d getYawVec() {
      return Vec3d.fromPolar(this.pitch, this.yaw);
   }

   public Vec3d initInit() {
      float var1 = this.pitch * (float) (Math.PI / 180.0);
      float var2 = -this.yaw * (float) (Math.PI / 180.0);
      float var3 = MathHelper.cos(var2);
      float var4 = MathHelper.sin(var2);
      float var5 = MathHelper.cos(var1);
      float var6 = MathHelper.sin(var1);
      return new Vec3d((double)(var4 * var5), (double)(-var6), (double)(var3 * var5));
   }

   public Vec3d initInit2() {
      float var1 = this.pitch * (float) (Math.PI / 180.0);
      float var2 = -this.yaw * (float) (Math.PI / 180.0);
      float var3 = MathHelper.cos(var2);
      float var4 = MathHelper.sin(var2);
      float var5 = MathHelper.cos(var1);
      float var6 = MathHelper.sin(var1);
      return new Vec3d((double)(var4 * var5), (double)(-var6), (double)(var3 * var5));
   }

   public Rotation initDelta(Rotation other, float horizontalFactor, float verticalFactor) {
      DeltaHelper var4 = this.initYaw(other);
      float var5 = var4.sqrt();
      float var6 = var5 == 0.0F ? 0.0F : Math.abs(var4.getDeltaYaw() / var5) * horizontalFactor;
      float var7 = var5 == 0.0F ? 0.0F : Math.abs(var4.getDeltaPitch() / var5) * verticalFactor;
      float var8 = MathHelper.clamp(var4.getDeltaYaw(), -var6, var6);
      float var9 = MathHelper.clamp(var4.getDeltaPitch(), -var7, var7);
      return new Rotation(this.yaw + var8, this.pitch + var9);
   }

   public boolean checkInfinite() {
      return Float.isInfinite(this.yaw) || Float.isNaN(this.yaw) || Float.isInfinite(this.pitch) || Float.isNaN(this.pitch);
   }

   public static float getFloat() {
      double var0 = (Double)MinecraftClient.getInstance().options.getMouseSensitivity().getValue() * 0.6F + 0.2F;
      return (float)(var0 * var0 * var0 * 8.0 * 0.15F);
   }

   public Rotation initDelta(Rotation currentRotation) {
      if (!this.isNormalized && !this.computeYaw(currentRotation)) {
         DeltaHelper var2 = currentRotation.initYaw(this);
         double var3 = (double)getFloat();
         int var5 = (int)((double)var2.getDeltaYaw() / var3);
         int var6 = (int)((double)var2.getDeltaPitch() / var3);
         return new Rotation(
            (float)((double)currentRotation.getYaw() + (double)var5 * var3), (float)((double)currentRotation.getPitch() + (double)var6 * var3), true
         );
      } else {
         return this;
      }
   }

   public Rotation initDelta(DeltaHelper diff) {
      return new Rotation(this.yaw + diff.getDeltaYaw(), this.pitch + diff.getDeltaPitch());
   }

   public Rotation initYaw() {
      double var1 = (double)Geometry.getMc();
      Rotation var3 = VecRotationHelper.getInstance().getServerRotation();
      float var4 = this.computeRound(this.yaw, var3.yaw, var1);
      float var5 = this.computeRound(this.pitch, var3.pitch, var1);
      return new Rotation(var4, MathHelper.clamp(var5, -90.0F, 90.0F));
   }

   public float computeRound(float axisValue, float previousValue, double gcdValue) {
      float var5 = axisValue - previousValue;
      return previousValue + (float)Math.round((double)var5 / gcdValue) * (float)gcdValue;
   }

   public boolean computeYaw(Object obj) {
      return !(obj instanceof Rotation var2) ? false : var2.yaw == this.yaw && var2.pitch == this.pitch;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public boolean isIsNormalized() {
      return this.isNormalized;
   }

   public void setIsNormalized(boolean isNormalized) {
      this.isNormalized = isNormalized;
   }

   public static record VecRotation(Rotation rotation, Vec3d vec) {

      @Override
      public String toString() {
         return "VecRotation(rotation=" + this.rotation + ", vec=" + this.vec + ")";
      }
   }
}
