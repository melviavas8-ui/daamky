package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class ContextHelper implements MinecraftAccess {
   public static final ThreadLocal<ContextHelper.Context> threadLocal = ThreadLocal.withInitial(ContextHelper.Context::new);

   public static ContextHelper.Context getContext() {
      ContextHelper.Context var0 = threadLocal.get();
      var0.runIdentity();
      return var0;
   }

   public static Vector2f computeVector2f(@NotNull Vec3d vec3d) {
      return computeVector2f(vec3d.getX(), vec3d.getY(), vec3d.getZ());
   }

   public static Vector2f computeVector2f(double x, double y, double z) {
      return getContext().initInit(x, y, z);
   }

   public static Vector2f computeVector2f(double x, double y, double z, Vector2f out) {
      return getContext().computeRadians(x, y, z, out);
   }

   public static Vector2f computeVector2f(@NotNull Vec3d vec3d, Vector2f out) {
      return getContext().computeRadians(vec3d.getX(), vec3d.getY(), vec3d.getZ(), out);
   }

   public ContextHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static class Context {
      public Vec3d class_243 = Vec3d.ZERO;
      public final Quaternionf quaternionf = new Quaternionf();
      public float maoaj;
      public boolean flag;
      public AbstractClientPlayerEntity class_742;
      public float floatVal;
      public float float2;
      public float float3;
      public final Vector3f vector3f = new Vector3f();
      public final Vector2f vector2f = new Vector2f();
      public final Quaternionf quaternionf2 = new Quaternionf();
      public final Quaternionf quaternionf3 = new Quaternionf();
      public boolean flag2 = false;
      public boolean mhoig = false;
      public float float4;
      public float float5;
      public long timeStamp = -1L;
      public final Quaternionf quaternionf4 = new Quaternionf();
      public final Quaternionf quaternionf5 = new Quaternionf();
      public float float6 = Float.NaN;
      public float float7 = Float.NaN;
      public double mzaet = Double.NaN;
      public double doubleVal = Double.NaN;
      public double miava = Double.NaN;
      public float float8 = Float.NaN;
      public int intVal = -1;
      public int int2 = -1;

      public Context() {
         this.runIdentity();
      }

      public void runIdentity() {
         if (MinecraftAccess.mc.getEntityRenderDispatcher() != null && MinecraftAccess.mc.getEntityRenderDispatcher().camera != null && MinecraftAccess.mc.getWindow() != null) {
            Camera var1 = MinecraftAccess.mc.getEntityRenderDispatcher().camera;
            float var2 = var1.getYaw();
            float var3 = var1.getPitch();
            Vec3d var4 = var1.getPos();
            float var5 = MinecraftAccess.mc.getRenderTickCounter().getTickDelta(false);
            int var6 = MinecraftAccess.mc.getWindow().getScaledWidth();
            int var7 = MinecraftAccess.mc.getWindow().getScaledHeight();
            if (var2 != this.float6
               || var3 != this.float7
               || var4.x != this.mzaet
               || var4.y != this.doubleVal
               || var4.z != this.miava
               || var5 != this.float8
               || var6 != this.intVal
               || var7 != this.int2) {
               this.float6 = var2;
               this.float7 = var3;
               this.mzaet = var4.x;
               this.doubleVal = var4.y;
               this.miava = var4.z;
               this.float8 = var5;
               this.intVal = var6;
               this.int2 = var7;
               this.class_243 = var4;
               this.quaternionf4.identity().rotationY((float)Math.toRadians((double)(-var2)));
               this.quaternionf5.identity().rotationX((float)Math.toRadians((double)var3));
               this.quaternionf4.mul(this.quaternionf5, this.quaternionf).conjugate();
               this.floatVal = var5;
               this.maoaj = MinecraftAccess.mc.gameRenderer.getFov(var1, var5, true);
               this.flag = (Boolean)MinecraftAccess.mc.options.getBobView().getValue();
               this.class_742 = MinecraftAccess.mc.getCameraEntity() instanceof AbstractClientPlayerEntity var9 ? var9 : null;
               this.float2 = (float)var6 / 2.0F;
               this.float3 = (float)var7 / 2.0F;
               if (this.flag && this.class_742 != null) {
                  float var16 = this.class_742.distanceMoved;
                  float var10 = this.class_742.lastDistanceMoved;
                  float var11 = var16 - var10;
                  float var12 = -(var16 + var11 * this.floatVal);
                  float var13 = MathHelper.lerp(this.floatVal, this.class_742.prevStrideDistance, this.class_742.strideDistance);
                  this.float4 = MathHelper.sin(var12 * (float) Math.PI) * var13 * 0.5F;
                  this.float5 = -Math.abs(MathHelper.cos(var12 * (float) Math.PI) * var13);
                  float var14 = MathHelper.sin(var12 * (float) Math.PI) * var13 * 3.0F * (float) (Math.PI / 180.0);
                  float var15 = Math.abs(MathHelper.cos(var12 * (float) Math.PI - 0.2F)) * var13 * 5.0F * (float) (Math.PI / 180.0);
                  if (var14 != 0.0F) {
                     this.quaternionf2.identity().setAngleAxis(var14, 0.0F, 0.0F, 1.0F).conjugate();
                     this.flag2 = true;
                  } else {
                     this.flag2 = false;
                  }

                  if (var15 != 0.0F) {
                     this.quaternionf3.identity().setAngleAxis(var15, 1.0F, 0.0F, 0.0F).conjugate();
                     this.mhoig = true;
                  } else {
                     this.mhoig = false;
                  }
               } else {
                  this.float4 = 0.0F;
                  this.float5 = 0.0F;
                  this.flag2 = false;
                  this.mhoig = false;
               }
            }
         }
      }

      public Vector2f initInit(double x, double y, double z) {
         return new Vector2f(this.computeRadians(x, y, z, this.vector2f));
      }

      public Vector2f computeRadians(double x, double y, double z, Vector2f out) {
         Vector3f var8 = this.vector3f
            .set((float)(this.class_243.getX() - x), (float)(this.class_243.getY() - y), (float)(this.class_243.getZ() - z));
         var8.rotate(this.quaternionf);
         if (this.flag && this.class_742 != null) {
            this.handleRotate(var8);
         }

         double var9 = (double)this.float3 / ((double)var8.z * Math.tan(Math.toRadians((double)this.maoaj / 2.0)));
         return var8.z < 0.0F
            ? out.set((float)((double)(-var8.x) * var9 + (double)this.float2), (float)((double)this.float3 - (double)var8.y * var9))
            : out.set(Float.MAX_VALUE, Float.MAX_VALUE);
      }

      public void handleRotate(Vector3f result3f) {
         if (this.flag2) {
            result3f.rotate(this.quaternionf2);
         }

         if (this.mhoig) {
            result3f.rotate(this.quaternionf3);
         }

         result3f.add(this.float4, -this.float5, 0.0F);
      }
   }
}
