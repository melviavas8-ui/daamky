package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class SparkHelper implements MinecraftAccess {
   public float floatVal;
   public float y;
   public float z;
   public float x;
   public float float2;
   public float float3;
   public float motionX;
   public float motionY;
   public float motionZ;
   public int lifeTime;
   public int maxLife;
   public float prevSize = 0.0F;
   public float rotation;
   public float prevRotation = 0.0F;
   public float rotateSpeed = 20.0F;
   public float size;
   public int index;
   public Identifier identifier;
   public boolean dropPhysics;
   public boolean rotating;
   public float spawnDuration;
   public float dyingDuration;
   public final DelayHelper delayHelper = new DelayHelper();
   public final AnimatedValue animatedValue = new AnimatedValue();
   public boolean gravityFalls = false;
   public boolean cometMode = false;
   public float cometPhase;
   public float cometWobbleStrength = 0.5F;
   public float cometWobbleSpeed = 1.5F;
   public boolean spawnStarted = false;
   public boolean dyingStarted = false;
   public int tickCount = 0;
   public static String[] stringList = new String[]{"Spark", "Star", "Heart", "Dollar", "Snowflake", "Glow", "Ball"};

   public SparkHelper(float x, float y, float z, int lifeTime) {
      this.floatVal = x;
      this.y = y;
      this.z = z;
      this.x = x;
      this.float2 = y;
      this.float3 = z;
      this.maxLife = EntityHelper.computeRandom(Math.max(lifeTime / 2, 0), lifeTime);
      this.rotation = EntityHelper.computeRandom(-180.0F, 180.0F);
      this.cometPhase = EntityHelper.computeRandom(0.0F, (float) (Math.PI * 2));
   }

   public static Identifier computeClass_2960(String mode) {
      byte var2 = -1;
      switch (mode.hashCode()) {
         case 80085693:
            if (mode.equals("Spark")) {
               var2 = 0;
            }
         default:
            return switch (var2) {
               case 0 -> InputHelper.getImage("particles/spark_" + EntityHelper.computeRandom(1, 4));
               default -> InputHelper.getImage("particles/" + mode.toLowerCase());
            };
      }
   }

   public boolean checkComet() {
      this.tickCount++;
      this.floatVal = this.x;
      this.y = this.float2;
      this.z = this.float3;
      if (this.cometMode) {
         this.motionX *= 0.995F;
         this.motionY *= 0.995F;
         this.motionZ *= 0.995F;
         float var2 = (float)Math.sin((double)((float)this.tickCount * 0.05F * this.cometWobbleSpeed + this.cometPhase)) * this.cometWobbleStrength;
         this.motionY += var2 * 0.003F;
         this.motionX = this.motionX + EntityHelper.computeRandom(-0.001F, 0.001F);
         this.motionZ = this.motionZ + EntityHelper.computeRandom(-0.001F, 0.001F);
         float var3 = (float)Math.sqrt((double)(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ));
         float var4 = this.cometWobbleStrength * 0.01F;
         float var5 = this.cometWobbleStrength * 0.04F;
         if (var3 < var4 && var3 > 1.0E-4F) {
            float var1 = var4 / var3;
            this.motionX *= var1;
            this.motionZ *= var1;
         }

         if (var3 > var5) {
            float var6 = var5 / var3;
            this.motionX *= var6;
            this.motionY *= var6;
            this.motionZ *= var6;
         }
      } else if (this.gravityFalls) {
         this.motionX *= 0.96F;
         this.motionY *= 0.96F;
         this.motionZ *= 0.96F;
         this.motionY -= 0.015F;
      } else {
         this.motionX *= 0.91F;
         this.motionY *= 0.91F;
         this.motionZ *= 0.91F;
      }

      this.x = this.x + this.motionX;
      this.float2 = this.float2 + this.motionY;
      this.float3 = this.float3 + this.motionZ;
      if (this.dropPhysics && !this.cometMode) {
         float var7 = this.prevSize;
         if (this.computeFlag(this.x, this.float2 - var7 - 0.05F, this.float3)) {
            this.motionY = -this.motionY * 0.4F;
            this.motionX *= 0.6F;
            this.motionZ *= 0.6F;
            this.float2 += 0.1F;
         } else if (this.computeFlag(this.x + var7, this.float2, this.float3)
            || this.computeFlag(this.x - var7, this.float2, this.float3)
            || this.computeFlag(this.x, this.float2, this.float3 + var7)
            || this.computeFlag(this.x, this.float2, this.float3 - var7)) {
            this.motionX = -this.motionX * 0.4F;
            this.motionZ = -this.motionZ * 0.4F;
         }
      }

      this.prevRotation = this.rotation;
      if (this.rotating) {
         this.rotation = this.rotation + this.rotateSpeed;
      }

      if (mc.player == null) {
         return false;
      } else {
         double var8 = mc.player.getPos().squaredDistanceTo((double)this.x, (double)this.float2, (double)this.float3);
         return var8 >= 4900.0
            ? true
            : this.animatedValue.getValue() <= 0.0 && this.delayHelper.computeCurrent((this.spawnDuration + this.dyingDuration + (float)this.maxLife) * 50.0F);
      }
   }

   public float getValue() {
      return (float)this.animatedValue.getValue();
   }

   public int getInt() {
      return (int)(255.0F * this.getValue());
   }

   public void runSpawn() {
      this.animatedValue.update();
      float var1 = this.getValue();
      if (!this.spawnStarted && (double)var1 <= 0.0 && !this.delayHelper.computeCurrent(this.spawnDuration * 50.0F)) {
         this.spawnStarted = true;
         this.animatedValue.animateTo(1.0, (long)(this.spawnDuration * 50.0F), Easing.QUINT_OUT);
      }

      if (!this.dyingStarted && (double)var1 >= 1.0 && this.delayHelper.computeCurrent((this.spawnDuration + (float)this.maxLife) * 50.0F)) {
         this.dyingStarted = true;
         this.animatedValue.animateTo(0.0, (long)(this.dyingDuration * 50.0F), Easing.QUINT_OUT);
      }
   }

   public void setMatrixStack(MatrixStack matrixStack) {
      int var3 = this.getInt();
      if (var3 > 2) {
         Vec3d var4 = this.initInit(this.floatVal, this.y, this.z, this.x, this.float2, this.float3);
         double var5 = var4.x * var4.x + var4.y * var4.y + var4.z * var4.z;
         if (!(var5 > 4096.0)) {
            Camera var7 = mc.gameRenderer.getCamera();
            Color var8 = ColorMath.initBlue(ThemeColor.computeColor(this.index * 90), var3);
            float var9 = this.size * this.getValue();
            float var2;
            this.prevSize = var2 = EntityHelper.computeFloat((double)this.prevSize, (double)var9);
            if (!(var2 < 0.005F)) {
               RenderSystem.setShaderTexture(0, this.identifier);
               matrixStack.push();
               matrixStack.translate(var4.x, var4.y, var4.z);
               if (this.rotating) {
                  matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(EntityHelper.computeFloat((double)this.prevRotation, (double)this.rotation)));
               }

               Matrix4f var10 = matrixStack.peek().getPositionMatrix();
               BufferBuilder var11 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               var11.vertex(var10, var2, -var2, 0.0F).texture(0.0F, 1.0F).color(var8.getRGB());
               var11.vertex(var10, -var2, -var2, 0.0F).texture(1.0F, 1.0F).color(var8.getRGB());
               var11.vertex(var10, -var2, var2, 0.0F).texture(1.0F, 0.0F).color(var8.getRGB());
               var11.vertex(var10, var2, var2, 0.0F).texture(0.0F, 0.0F).color(var8.getRGB());
               BufferRenderer.drawWithGlobalProgram(var11.end());
               matrixStack.pop();
            }
         }
      }
   }

   public boolean computeFlag(float x, float y, float z) {
      return mc.world == null ? false : mc.world.getBlockState(BlockPos.ofFloored((double)x, (double)y, (double)z)).isSolid();
   }

   public Vec3d initInit(float prevX, float prevY, float prevZ, float currentX, float currentY, float currentZ) {
      Vec3d var7 = mc.getEntityRenderDispatcher().camera.getPos();
      double var8 = (double)EntityHelper.computeFloat((double)prevX, (double)currentX) - var7.x;
      double var10 = (double)EntityHelper.computeFloat((double)prevY, (double)currentY) - var7.y;
      double var12 = (double)EntityHelper.computeFloat((double)prevZ, (double)currentZ) - var7.z;
      return new Vec3d(var8, var10, var12);
   }

   public SparkHelper withPrevX(float prevX) {
      this.floatVal = prevX;
      return this;
   }

   public SparkHelper withPrevY(float prevY) {
      this.y = prevY;
      return this;
   }

   public SparkHelper withPrevZ(float prevZ) {
      this.z = prevZ;
      return this;
   }

   public SparkHelper withX(float x) {
      this.x = x;
      return this;
   }

   public SparkHelper withY(float y) {
      this.float2 = y;
      return this;
   }

   public SparkHelper withZ(float z) {
      this.float3 = z;
      return this;
   }

   public SparkHelper withMotionX(float motionX) {
      this.motionX = motionX;
      return this;
   }

   public SparkHelper withMotionY(float motionY) {
      this.motionY = motionY;
      return this;
   }

   public SparkHelper withMotionZ(float motionZ) {
      this.motionZ = motionZ;
      return this;
   }

   public SparkHelper withLifeTime(int lifeTime) {
      this.lifeTime = lifeTime;
      return this;
   }

   public SparkHelper withMaxLife(int maxLife) {
      this.maxLife = maxLife;
      return this;
   }

   public SparkHelper withPrevSize(float prevSize) {
      this.prevSize = prevSize;
      return this;
   }

   public SparkHelper withRotation(float rotation) {
      this.rotation = rotation;
      return this;
   }

   public SparkHelper withPrevRotation(float prevRotation) {
      this.prevRotation = prevRotation;
      return this;
   }

   public SparkHelper withRotateSpeed(float rotateSpeed) {
      this.rotateSpeed = rotateSpeed;
      return this;
   }

   public SparkHelper withSize(float size) {
      this.size = size;
      return this;
   }

   public SparkHelper withIndex(int index) {
      this.index = index;
      return this;
   }

   public SparkHelper withIdentifier(Identifier identifier) {
      this.identifier = identifier;
      return this;
   }

   public SparkHelper withDropPhysics(boolean dropPhysics) {
      this.dropPhysics = dropPhysics;
      return this;
   }

   public SparkHelper withRotating(boolean rotating) {
      this.rotating = rotating;
      return this;
   }

   public SparkHelper withSpawnDuration(float spawnDuration) {
      this.spawnDuration = spawnDuration;
      return this;
   }

   public SparkHelper withDyingDuration(float dyingDuration) {
      this.dyingDuration = dyingDuration;
      return this;
   }

   public SparkHelper withGravityFalls(boolean gravityFalls) {
      this.gravityFalls = gravityFalls;
      return this;
   }

   public SparkHelper withCometMode(boolean cometMode) {
      this.cometMode = cometMode;
      return this;
   }

   public SparkHelper withCometPhase(float cometPhase) {
      this.cometPhase = cometPhase;
      return this;
   }

   public SparkHelper withCometWobbleStrength(float cometWobbleStrength) {
      this.cometWobbleStrength = cometWobbleStrength;
      return this;
   }

   public SparkHelper withCometWobbleSpeed(float cometWobbleSpeed) {
      this.cometWobbleSpeed = cometWobbleSpeed;
      return this;
   }

   public SparkHelper withSpawnStarted(boolean spawnStarted) {
      this.spawnStarted = spawnStarted;
      return this;
   }

   public SparkHelper withDyingStarted(boolean dyingStarted) {
      this.dyingStarted = dyingStarted;
      return this;
   }

   public SparkHelper withTickCount(int tickCount) {
      this.tickCount = tickCount;
      return this;
   }

   public float getFloat() {
      return this.floatVal;
   }

   public float getY() {
      return this.y;
   }

   public float getZ() {
      return this.z;
   }

   public float getX() {
      return this.x;
   }

   public float getFloat2() {
      return this.float2;
   }

   public float getFloat3() {
      return this.float3;
   }

   public float getMotionX() {
      return this.motionX;
   }

   public float getMotionY() {
      return this.motionY;
   }

   public float getMotionZ() {
      return this.motionZ;
   }

   public int getLifeTime() {
      return this.lifeTime;
   }

   public int getMaxLife() {
      return this.maxLife;
   }

   public float getPrevSize() {
      return this.prevSize;
   }

   public float getRotation() {
      return this.rotation;
   }

   public float getPrevRotation() {
      return this.prevRotation;
   }

   public float getRotateSpeed() {
      return this.rotateSpeed;
   }

   public float getSize() {
      return this.size;
   }

   public int getIndex() {
      return this.index;
   }

   public Identifier getIdentifier() {
      return this.identifier;
   }

   public boolean isDropPhysics() {
      return this.dropPhysics;
   }

   public boolean isRotating() {
      return this.rotating;
   }

   public float getSpawnDuration() {
      return this.spawnDuration;
   }

   public float getDyingDuration() {
      return this.dyingDuration;
   }

   public DelayHelper getDelayHelper() {
      return this.delayHelper;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public boolean isGravityFalls() {
      return this.gravityFalls;
   }

   public boolean isCometMode() {
      return this.cometMode;
   }

   public float getCometPhase() {
      return this.cometPhase;
   }

   public float getCometWobbleStrength() {
      return this.cometWobbleStrength;
   }

   public float getCometWobbleSpeed() {
      return this.cometWobbleSpeed;
   }

   public boolean isSpawnStarted() {
      return this.spawnStarted;
   }

   public boolean isDyingStarted() {
      return this.dyingStarted;
   }

   public int getTickCount() {
      return this.tickCount;
   }
}
