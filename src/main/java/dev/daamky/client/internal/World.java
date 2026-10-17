package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.render.Particles;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class World extends Particles.BaseSettings {
   public final ModeSetting cometSetting;
   public final FloatSetting comet;
   public final FloatSetting floatSetting;
   public final FloatSetting floatSetting2;
   public final FloatSetting floatSetting3;
   public final List<World.Particle> list;
   public final Map<Identifier, List<World.Particle>> mafol;
   public final DelayHelper delayHelper;
   public final Vector3f mopeb = new Vector3f();
   public final Matrix4f mxojd = new Matrix4f();

   public World() {
      super("World");
      this.cometSetting = new ModeSetting(this.string + "Physics").value("Drop").values("Drop", "Comet");
      this.comet = new FloatSetting(this.string + "Distance").value(8.0F).range(2.0F, 25.0F).step(1.0F);
      this.floatSetting = new FloatSetting(this.string + "Height").value(1.5F).range(0.2F, 4.0F).step(0.1F);
      this.floatSetting2 = new FloatSetting(this.string + "Speed").value(0.5F).range(0.1F, 2.0F).step(0.1F);
      this.floatSetting3 = new FloatSetting(this.string + "Spread").value(0.3F).range(0.0F, 1.0F).step(0.05F);
      this.list = new ArrayList<>();
      this.mafol = new HashMap<>();
      this.delayHelper = new DelayHelper();
      this.addSettings(new Setting[]{this.cometSetting, this.comet, this.floatSetting, this.floatSetting2, this.floatSetting3});
   }

   public void runClear() {
      this.list.clear();
      this.delayHelper.runCurrent();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = dev.daamky.client.event.Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var2, var1});
   }

   public void lambda$onEvent$2(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (!this.list.isEmpty() && mc.player != null) {
         MatrixStack var2 = event.getMatrixStack();
         Camera var3 = mc.gameRenderer.getCamera();

         for (World.Particle var5 : this.list) {
            var5.runSpawn();
         }

         Render.renderEffect.handleBlend(var2);
         Map var25 = this.mafol;
         var25.values().forEach(List::clear);

         for (World.Particle var6 : this.list) {
            int var7 = var6.getInt();
            if (var7 > 2) {
               float var8 = var6.getSize() * var6.getValue();
               float var9 = EntityHelper.computeFloat((double)var6.getPrevSize(), (double)var8);
               var6.withPrevSize(var9);
               if (!(var9 < 0.005F)) {
                  var25.computeIfAbsent(var6.getIdentifier(), World::lambda$onEvent$1).add(var6);
               }
            }
         }

         long var27 = System.currentTimeMillis();

         for (Entry var29 : var25.entrySet()) {
            Identifier var30 = (Identifier)var29.getKey();
            List var10 = (List)var29.getValue();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderTexture(0, var30);
            BufferBuilder var11 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            boolean var12 = false;

            for (World.Particle var14 : (Iterable<World.Particle>)(Iterable)var10) {
               Vec3d var15 = var14.initInit(var14.getFloat(), var14.getY(), var14.getZ(), var14.getX(), var14.getFloat2(), var14.getFloat3());
               double var16 = var15.x * var15.x + var15.y * var15.y + var15.z * var15.z;
               if (!(var16 > 4096.0)) {
                  int var18 = ThemeColor.computeInt(var14.getIndex() * 90, var14.getInt(), var27);
                  float var19 = var14.getPrevSize();
                  float var20 = (float)var15.x;
                  float var21 = (float)var15.y;
                  float var22 = (float)var15.z;
                  Vector3f var23 = this.mopeb.set(var20, var21, var22);
                  var2.peek().getPositionMatrix().transformPosition(var23);
                  Matrix4f var24 = this.mxojd.identity();
                  if (var14.isRotating()) {
                     var24.translate(var23.x, var23.y, var23.z);
                     var24.rotateZ((float)Math.toRadians((double)EntityHelper.computeFloat((double)var14.getPrevRotation(), (double)var14.getRotation())));
                     var11.vertex(var24, var19, -var19, 0.0F).texture(0.0F, 1.0F).color(var18);
                     var11.vertex(var24, -var19, -var19, 0.0F).texture(1.0F, 1.0F).color(var18);
                     var11.vertex(var24, -var19, var19, 0.0F).texture(1.0F, 0.0F).color(var18);
                     var11.vertex(var24, var19, var19, 0.0F).texture(0.0F, 0.0F).color(var18);
                  } else {
                     var11.vertex(var24, var23.x - var19, var23.y - var19, var23.z).texture(0.0F, 1.0F).color(var18);
                     var11.vertex(var24, var23.x + var19, var23.y - var19, var23.z).texture(1.0F, 1.0F).color(var18);
                     var11.vertex(var24, var23.x + var19, var23.y + var19, var23.z).texture(1.0F, 0.0F).color(var18);
                     var11.vertex(var24, var23.x - var19, var23.y + var19, var23.z).texture(0.0F, 0.0F).color(var18);
                  }

                  var12 = true;
               }
            }

            if (var12) {
               BufferRenderer.drawWithGlobalProgram(var11.end());
            }
         }

         Render.renderEffect.handleBlend2(var2);
      }
   }

   public static List lambda$onEvent$1(Identifier k) {
      return new ArrayList<>();
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.list.removeIf(SparkHelper::checkComet);
      int var2 = this.getFloatSetting4().getValue().intValue();
      int var3 = this.list.size();
      int var4 = var2 - var3;
      if (var4 > 0 && this.delayHelper.computeCurrent(this.getFloatSetting5().getValue().longValue())) {
         this.delayHelper.runCurrent();
         int var5 = Math.min(var4, 5);
         float var6 = this.comet.getValue();
         float var7 = this.floatSetting.getValue();
         float var8 = this.floatSetting2.getValue();
         float var9 = this.floatSetting3.getValue();
         boolean var10 = this.cometSetting.is("Comet");

         for (int var11 = 0; var11 < var5; var11++) {
            float var15 = EntityHelper.computeRandom(-var9, var9);
            float var16 = EntityHelper.computeRandom(-var9, var9);
            float var12;
            float var13;
            float var14;
            if (var10) {
               float var17 = EntityHelper.computeRandom(0.0F, (float) (Math.PI * 2));
               float var18 = EntityHelper.computeRandom(var8 * 0.02F, var8 * 0.08F);
               var14 = (float)Math.cos((double)var17) * var18;
               var13 = (float)Math.sin((double)var17) * var18;
               var12 = EntityHelper.computeRandom(-var8 * 0.01F, var8 * 0.02F);
            } else {
               var14 = EntityHelper.computeRandom(-var8 * 0.03F, var8 * 0.03F);
               var12 = -EntityHelper.computeRandom(var8 * 0.01F, var8 * 0.04F);
               var13 = EntityHelper.computeRandom(-var8 * 0.03F, var8 * 0.03F);
            }

            this.list
               .add(
                  new World.Particle(
                     this,
                     this,
                     (float)(mc.player.getX() + (double)EntityHelper.computeRandom(-var6, var6) + (double)(var15 * var6)),
                     (float)(mc.player.getY() + (double)EntityHelper.computeRandom(0.2F, var7)),
                     (float)(mc.player.getZ() + (double)EntityHelper.computeRandom(-var6, var6) + (double)(var16 * var6)),
                     var14,
                     var12,
                     var13,
                     this.list.size(),
                     SparkHelper.computeClass_2960(this.getSparkSetting().getValue()),
                     this.getSpark().getValue(),
                     this.getBooleanSetting().getValue(),
                     this.getFloatSetting().getValue().intValue(),
                     this.getFloatSetting2().getValue(),
                     this.getFloatSetting3().getValue(),
                     var10
                  )
               );
         }
      }
   }

   private class Particle extends SparkHelper {
      public Particle(
         World worldParticles,
         World worldParticles2,
         float x,
         float y,
         float z,
         float motionX,
         float motionY,
         float motionZ,
         int index,
         Identifier identifier,
         float size,
         boolean rotate,
         int lifetime,
         float sp,
         float dy,
         boolean cometMode
      ) {
         super(x, y, z, lifetime);
         super.withCometMode(cometMode)
            .withGravityFalls(!cometMode)
            .withDropPhysics(!cometMode)
            .withSize(size)
            .withIndex(index)
            .withMotionX(motionX)
            .withMotionY(motionY)
            .withMotionZ(motionZ)
            .withIdentifier(identifier)
            .withRotating(rotate)
            .withSpawnDuration(sp)
            .withDyingDuration(dy);
         if (cometMode) {
            super.withCometWobbleStrength(EntityHelper.computeRandom(0.15F, 0.35F));
            super.withCometWobbleSpeed(EntityHelper.computeRandom(0.5F, 1.2F));
         }
      }
   }
}
