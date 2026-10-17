package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.AttackEvent;
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
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Hit extends Particles.BaseSettings {
   public final ModeSetting dropSetting;
   public final FloatSetting drop;
   public final List<Hit.Particle> list;
   public final Map<Identifier, List<Hit.Particle>> map;
   public final Vector3f vector3f = new Vector3f();
   public final Matrix4f matrix4f = new Matrix4f();

   public Hit() {
      super("Hit");
      this.dropSetting = new ModeSetting(this.string + "Physics").value("Drop").values("Fly", "Drop");
      this.drop = new FloatSetting(this.string + "Motion").value(0.3F).range(0.1F, 1.0F).step(0.1F);
      this.list = new ArrayList<>();
      this.map = new HashMap<>();
      this.addSettings(new Setting[]{this.dropSetting, this.drop});
   }

   public void runClear() {
      this.list.clear();
      this.clearEventListeners();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = dev.daamky.client.event.Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var3 = AttackEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var2, var3, var1});
   }

   public void lambda$onEvent$3(AttackEvent.AttackEventData event) {
      if (event.entity() instanceof LivingEntity var3) {
         float var4 = this.drop.getValue();
         boolean var5 = !this.dropSetting.is("Fly");
         int var6 = this.getFloatSetting4().getValue().intValue();

         for (int var7 = 0; var7 < var6; var7++) {
            float var10 = EntityHelper.computeRandom(-var4, var4);
            float var8;
            float var9;
            if (var5) {
               var9 = EntityHelper.computeRandom(0.0F, var4 * 0.7F);
               var8 = EntityHelper.computeRandom(-var4, var4);
            } else {
               var9 = EntityHelper.computeRandom(-var4 * 0.4F, var4 * 0.4F);
               var8 = EntityHelper.computeRandom(-var4, var4);
            }

            this.list
               .add(
                  new Hit.Particle(
                     this,
                     this,
                     (float)(var3.getX() + (double)EntityHelper.computeRandom(-0.3F, 0.3F)),
                     (float)(var3.getY() + (double)(var3.getHeight() / 2.0F) + (double)EntityHelper.computeRandom(-0.2F, 0.2F)),
                     (float)(var3.getZ() + (double)EntityHelper.computeRandom(-0.3F, 0.3F)),
                     var10,
                     var9,
                     var8,
                     this.list.size(),
                     SparkHelper.computeClass_2960(this.getSparkSetting().getValue()),
                     this.getSpark().getValue(),
                     var5,
                     var5,
                     !var5,
                     this.getBooleanSetting().getValue(),
                     this.getFloatSetting().getValue().intValue(),
                     this.getFloatSetting2().getValue(),
                     this.getFloatSetting3().getValue()
                  )
               );
         }
      }
   }

   public void lambda$onEvent$2(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (!this.list.isEmpty() && mc.player != null) {
         MatrixStack var3 = event.getMatrixStack();
         Camera var4 = mc.gameRenderer.getCamera();
         Frustum var5 = new Frustum(var3.peek().getPositionMatrix(), RenderSystem.getProjectionMatrix());
         var5.setPosition(var4.getPos().x, var4.getPos().y, var4.getPos().z);

         for (Hit.Particle var7 : this.list) {
            var7.runSpawn();
         }

         Render.renderEffect.handleBlend(var3);
         Map var28 = this.map;
         var28.values().forEach(List::clear);

         for (Hit.Particle var8 : this.list) {
            int var2 = var8.getInt();
            if (var2 > 2) {
               float var9 = var8.getSize() * var8.getValue();
               float var10 = EntityHelper.computeFloat((double)var8.getPrevSize(), (double)var9);
               var8.withPrevSize(var10);
               if (!(var10 < 0.005F)) {
                  var28.computeIfAbsent(var8.getIdentifier(), Hit::lambda$onEvent$1).add(var8);
               }
            }
         }

         long var30 = System.currentTimeMillis();

         for (Entry var32 : var28.entrySet()) {
            Identifier var11 = (Identifier)var32.getKey();
            List var12 = (List)var32.getValue();
            if (!var12.isEmpty()) {
               boolean var13 = false;

               for (Hit.Particle var15 : (Iterable<Hit.Particle>)(Iterable)var12) {
                  Vec3d var16 = var15.initInit(var15.getFloat(), var15.getY(), var15.getZ(), var15.getX(), var15.getFloat2(), var15.getFloat3());
                  double var17 = var16.x * var16.x + var16.y * var16.y + var16.z * var16.z;
                  if (var17 <= 4096.0) {
                     var13 = true;
                     break;
                  }
               }

               if (var13) {
                  RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
                  RenderSystem.setShaderTexture(0, var11);
                  BufferBuilder var33 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

                  for (Hit.Particle var35 : (Iterable<Hit.Particle>)(Iterable)var12) {
                     Vec3d var36 = var35.initInit(var35.getFloat(), var35.getY(), var35.getZ(), var35.getX(), var35.getFloat2(), var35.getFloat3());
                     double var18 = var36.x * var36.x + var36.y * var36.y + var36.z * var36.z;
                     if (!(var18 > 4096.0)) {
                        int var20 = ThemeColor.computeInt(var35.getIndex() * 90, var35.getInt(), var30);
                        float var21 = var35.getPrevSize();
                        float var22 = (float)var36.x;
                        float var23 = (float)var36.y;
                        float var24 = (float)var36.z;
                        Vector3f var25 = this.vector3f.set(var22, var23, var24);
                        var3.peek().getPositionMatrix().transformPosition(var25);
                        Matrix4f var26 = this.matrix4f.identity();
                        if (var35.isRotating()) {
                           var26.translate(var25.x, var25.y, var25.z);
                           var26.rotateZ((float)Math.toRadians((double)EntityHelper.computeFloat((double)var35.getPrevRotation(), (double)var35.getRotation())));
                           var33.vertex(var26, var21, -var21, 0.0F).texture(0.0F, 1.0F).color(var20);
                           var33.vertex(var26, -var21, -var21, 0.0F).texture(1.0F, 1.0F).color(var20);
                           var33.vertex(var26, -var21, var21, 0.0F).texture(1.0F, 0.0F).color(var20);
                           var33.vertex(var26, var21, var21, 0.0F).texture(0.0F, 0.0F).color(var20);
                        } else {
                           var33.vertex(var26, var25.x - var21, var25.y - var21, var25.z).texture(0.0F, 1.0F).color(var20);
                           var33.vertex(var26, var25.x + var21, var25.y - var21, var25.z).texture(1.0F, 1.0F).color(var20);
                           var33.vertex(var26, var25.x + var21, var25.y + var21, var25.z).texture(1.0F, 0.0F).color(var20);
                           var33.vertex(var26, var25.x - var21, var25.y + var21, var25.z).texture(0.0F, 0.0F).color(var20);
                        }
                     }
                  }

                  try {
                     BufferRenderer.drawWithGlobalProgram(var33.end());
                  } catch (Exception var27) {
                  }
               }
            }
         }

         Render.renderEffect.handleBlend2(var3);
      }
   }

   public static List lambda$onEvent$1(Identifier k) {
      return new ArrayList<>();
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.list.removeIf(SparkHelper::checkComet);
      if (this.list.size() > 350) {
         this.list.subList(0, this.list.size() - 350).clear();
      }
   }

   private class Particle extends SparkHelper {
      public Particle(
         Hit hitParticles,
         Hit hitParticles2,
         float x,
         float y,
         float z,
         float motionX,
         float motionY,
         float motionZ,
         int index,
         Identifier identifier,
         float size,
         boolean gravityFalls,
         boolean dropPhysics,
         boolean cometMode,
         boolean rotate,
         int lifetime,
         float sp,
         float dy
      ) {
         super(x, y, z, lifetime);
         super.withCometMode(cometMode)
            .withGravityFalls(gravityFalls)
            .withDropPhysics(dropPhysics)
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
            super.withCometWobbleStrength(EntityHelper.computeRandom(0.3F, 0.8F));
            super.withCometWobbleSpeed(EntityHelper.computeRandom(1.0F, 2.5F));
         }
      }
   }
}
