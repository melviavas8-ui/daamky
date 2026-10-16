package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.combat.AutoExplosion;
import dev.daamky.client.module.combat.TriggerBot;
import dev.daamky.client.module.misc.FakePlayer;
import dev.daamky.client.module.render.TargetESP;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public abstract class TargetRenderer implements MinecraftAccess {
   public static final AnimatedValue animatedValue = new AnimatedValue();
   public static final AnimatedValue animatedValue2 = new AnimatedValue();
   public static Entity class_1297 = null;
   public float mluvoa = 0.0F;
   public float floatVal = 0.0F;
   public float float2 = 0.0F;
   public int intVal = 0;
   public static double mbacy = -1.0;
   public static double doubleVal = -1.0;
   public static double double2 = -1.0;
   public static double mawxi = -1.0;
   public static double double3 = -1.0;
   public static double double4 = -1.0;
   public static float float3 = 1.8F;
   public static float float4 = 0.6F;
   public static float float5 = 1.8F;
   public static float float6 = 0.6F;
   public static Entity class_12972 = null;
   public static long timeStamp = 0L;

   public static void handleOut(Entity target) {
      if (target != null && target.isAlive()) {
         class_12972 = target;
         longVal = System.currentTimeMillis();
         class_1297 = target;
         mbacy = target.getX();
         doubleVal = target.getY();
         double2 = target.getZ();
         mawxi = target.getX();
         double3 = target.getY();
         double4 = target.getZ();
         float3 = target.getHeight();
         float4 = target.getWidth();
         float5 = target.getHeight();
         float6 = target.getWidth();
         float var1 = TargetESP.getInstance().getSize().getValue();
         animatedValue.animateTo(1.0, 200L, Easing.SINE_OUT);
         animatedValue2.animateTo(var1, 200L, Easing.SINE_OUT);
      }
   }

   public AttackAura getInstance() {
      return AttackAura.getInstance();
   }

   public void runEnabled() {
      LivingEntity var1 = this.getInstance() != null && this.getInstance().isEnabled() ? this.getInstance().class_1309 : null;
      LivingEntity var2 = this.getInstance() != null && this.getInstance().isEnabled() ? this.getInstance().getClass_1309() : null;
      Entity var3 = AutoExplosion.getInstance().isEnabled() ? AutoExplosion.getInstance().getEntity() : null;
      Profile var4 = FakePlayer.getInstance().isEnabled() ? FakePlayer.getInstance().getProfile() : null;
      if (var1 != null && var1.isAlive()) {
         class_1297 = var1;
      } else if (var2 != null && var2.isAlive()) {
         class_1297 = var2;
      } else if (var3 != null && var3.isAlive()) {
         class_1297 = var3;
      } else if (var4 != null && var4.isAlive()) {
         class_1297 = var4;
      } else {
         class_1297 = null;
      }
   }

   public void handleLong(long duration, String mode, float size, float in, float out) {
      this.mluvoa = (float)animatedValue.getValue();
      this.floatVal = (float)animatedValue2.getValue();
      animatedValue2.update();

      double var7 = switch (mode) {
         case "In" -> (double)in;
         case "Out" -> (double)out;
         default -> (double)size;
      };
      boolean var9 = this.checkEnabled();
      long var12 = Math.max(150L, duration);
      animatedValue2.animateTo(var9 ? (double)size : var7, var12, Easing.SINE_OUT);
      animatedValue.update();
      animatedValue.animateTo(var9 ? 1.0 : 0.0, var12, Easing.SINE_OUT);
      this.runMin();
   }

   public void runMin() {
      TargetESP var1 = TargetESP.getInstance();
      if (var1.getRedOnImpact().getValue() && class_1297 != null) {
         float var2 = var1.getImpactFadeIn().getValue();
         float var3 = var1.getImpactFadeOut().getValue();
         float var4 = var1.getImpactIntensity().getValue();
         int var5 = class_1297 instanceof LivingEntity var6 ? var6.hurtTime : 0;
         this.floatVal2 = var5 <= this.intVal && (var5 <= 0 || this.intVal != 0)
            ? (var5 > 0 ? Math.min(var4, this.floatVal2 + var2 * 0.5F) : Math.max(0.0F, this.floatVal2 - var3))
            : Math.min(var4, this.floatVal2 + var2);
         this.intVal = var5;
      } else {
         this.floatVal2 = 0.0F;
         this.intVal = 0;
      }
   }

   public float getFloat2() {
      return this.floatVal2;
   }

   public int computeMin(int originalColor, float impact) {
      if (impact <= 0.01F) {
         return originalColor;
      } else {
         int var3 = originalColor >> 24 & 0xFF;
         int var4 = originalColor >> 16 & 0xFF;
         int var5 = originalColor >> 8 & 0xFF;
         int var6 = originalColor & 0xFF;
         short var7 = 255;
         byte var8 = 25;
         byte var9 = 45;
         int var10 = (int)((float)var4 + (float)(var7 - var4) * impact);
         int var11 = (int)((float)var5 + (float)(var8 - var5) * impact);
         int var12 = (int)((float)var6 + (float)(var9 - var6) * impact);
         return var3 << 24 | Math.min(255, var10) << 16 | Math.min(255, var11) << 8 | Math.min(255, var12);
      }
   }

   public boolean checkEnabled() {
      if (!this.getInstance().isEnabled()
         && !this.getInstance().isEnabled()
         && !AutoExplosion.getInstance().isEnabled()
         && !FakePlayer.getInstance().isEnabled()) {
         return false;
      } else if (class_1297 == null || !class_1297.isAlive()) {
         return false;
      } else {
         return mc.player == null ? false : mc.player.distanceTo(class_1297) <= 32.0F;
      }
   }

   public boolean getValue() {
      return mc.player != null && mc.world != null ? animatedValue.getValue() > 0.001 : false;
   }

   public static void runLast() {
      animatedValue.update();
      animatedValue2.update();
      float var0 = (float)animatedValue.getValue();
      float var1 = (float)animatedValue.getToValue();
      boolean var2 = TargetESP.getInstance().lastPosition.getValue();
      boolean var3 = var2 && (double)var1 == 0.0 && var0 <= 0.9F;
      if (class_1297 != null && class_1297.isAlive() && (!var3 || mawxi == -1.0)) {
         float var4 = mc.getRenderTickCounter().getTickDelta(false);
         mawxi = EntityHelper.computeDouble(class_1297.prevX, class_1297.getX(), (double)var4);
         double3 = EntityHelper.computeDouble(class_1297.prevY, class_1297.getY(), (double)var4);
         double4 = EntityHelper.computeDouble(class_1297.prevZ, class_1297.getZ(), (double)var4);
         float5 = class_1297.getHeight();
         float6 = class_1297.getWidth();
      }

      if (mawxi != -1.0) {
         mbacy = mawxi;
         doubleVal = double3;
         double2 = double4;
         float3 = float5;
         float4 = float6;
      } else if (class_1297 != null) {
         mbacy = class_1297.getX();
         doubleVal = class_1297.getY();
         double2 = class_1297.getZ();
         float3 = class_1297.getHeight();
         float4 = class_1297.getWidth();
      }
   }

   public abstract void runOrbit();

   public abstract void onRender3D(dev.daamky.client.event.Render3DEvent.Render3DEventData var1);

   public static double getMbacy() {
      return mbacy;
   }

   public static double getDouble() {
      return doubleVal;
   }

   public static double getDouble2() {
      return double2;
   }

   public static float getFloat3() {
      return float3;
   }

   public static float getFloat4() {
      return float4;
   }

   static {
      mbacy = -1.0;
      doubleVal = -1.0;
      double2 = -1.0;
      mawxi = -1.0;
      double3 = -1.0;
      double4 = -1.0;
      float3 = 1.8F;
      float4 = 0.6F;
      float5 = 1.8F;
      float6 = 0.6F;
   }
}
