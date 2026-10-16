package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.module.render.MotionBlur;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class IrisHelper {
   public final MotionBlur config;
   public IrisHelper.SatinImpl satinImpl;
   public boolean flag = false;
   public long longVal;
   public float strength = 0.0F;
   public float mosjir = 0.0F;
   public final Matrix4f matrix4f = new Matrix4f();
   public final Matrix4f matrix4f2 = new Matrix4f();
   public final Matrix4f matrix4f3 = new Matrix4f();
   public final Matrix4f matrix4f4 = new Matrix4f();

   public IrisHelper(MotionBlur config) {
      this.config = configx;

      try {
         Class.forName("org.ladysnake.satin.api.managed.ShaderEffectManager");
         this.satinImpl = new IrisHelper.SatinImpl(configx);
         this.flag = true;
      } catch (Throwable var3) {
         this.satinImpl = null;
         this.flag = false;
      }
   }

   public boolean check() {
      return this.flag;
   }

   public void registerShaderCallbacks() {
      dev.daamky.client.event.Render3DEvent.getInstance().subscribe(new EventListener<>(-1, this::lambda$registerShaderCallbacks$0));
   }

   public boolean checkConfig() {
      if (this.flag && this.satinImpl != null) {
         if (this.config.strength.getValue() != 0.0F && this.config.isEnabled()) {
            if (FabricLoader.getInstance().isModLoaded("iris")) {
               TextShaper.handleString("Удали IrisMod чтобы модуль работал");
               this.config.setEnabled(false);
               return false;
            } else {
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void setDeltaTick(float deltaTick) {
      if (this.flag && this.satinImpl != null) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         FrameTimer.runGlfw();
         int var4 = FrameTimer.getInt();
         float var2;
         float var5 = var2 = this.config.strength.getValue();
         if (this.config.useRefreshRateScaling.getValue()) {
            float var6 = var4 > 0 ? this.mosjir / (float)var4 : 1.0F;
            if (var6 < 1.0F) {
               var6 = 1.0F;
            }

            var5 = var2 * var6;
         }

         this.satinImpl.renderUniform(deltaTick, var3, this.strength, var5, (int)this.mosjir);
         this.strength = var5;
      }
   }

   public void handleVector3f(
      Matrix4f modelView, Matrix4f prevModelView, Matrix4f projection, Matrix4f prevProjection, Vector3f cameraPos, Vector3f prevCameraPos
   ) {
      if (this.flag && this.satinImpl != null) {
         this.satinImpl
            .handleUniform(
               this.matrix4f4, modelView, this.matrix4f3, projection, this.matrix4f, prevModelView, this.matrix4f2, prevProjection, cameraPos, prevCameraPos
            );
      }
   }

   public void setStrength(float strength) {
      if (this.flag && this.satinImpl != null) {
         this.satinImpl.handleUniform(strengthx);
         this.strength = strengthx;
      }
   }

   public void lambda$registerShaderCallbacks$0(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (this.flag && this.satinImpl != null) {
         long var2 = System.nanoTime();
         float var4 = (float)(var2 - this.longVal) / 1.0E9F;
         float var5 = var4 * 20.0F;
         this.longVal = var2;
         float var6 = this.mosjir = var4 > 0.0F && var4 < 1.0F ? 1.0F / var4 : 0.0F;
         if (this.checkConfig()) {
            this.setDeltaTick(var5);
         }
      }
   }

   private static class SatinImpl {
      public final ManagedShaderEffect mcexnud = ShaderEffectManager.getInstance()
         .manage(Identifier.of("daamky", "motion_blur"), IrisHelper.SatinImpl::lambda$new$0);

      public SatinImpl(MotionBlur config) {
      }

      public void handleUniform(float strength) {
         if (this.mcexnud != null) {
            this.mcexnud.setUniformValue("BlendFactor", strength);
         }
      }

      public void renderUniform(float deltaTick, MinecraftClient client, float currentBlur, float scaledStrength, int currentFPS) {
         if (this.mcexnud != null) {
            if (currentBlur != scaledStrength) {
               this.mcexnud.setUniformValue("BlendFactor", scaledStrength);
            }

            int var6 = currentFPS > 120 ? 8 : (currentFPS > 60 ? 6 : 4);
            int var7 = var6 / 2;
            float var8 = 1.0F / (float)var6;
            this.mcexnud.setUniformValue("view_res", (float)client.getFramebuffer().viewportWidth, (float)client.getFramebuffer().viewportHeight);
            this.mcexnud.setUniformValue("view_pixel_size", 1.0F / (float)client.getFramebuffer().viewportWidth, 1.0F / (float)client.getFramebuffer().viewportHeight);
            this.mcexnud.setUniformValue("motionBlurSamples", var6);
            this.mcexnud.setUniformValue("halfSamples", var7);
            this.mcexnud.setUniformValue("inverseSamples", var8);
            this.mcexnud.setUniformValue("blurAlgorithm", MotionBlur.BlurAlgorithm.BACKWARDS.ordinal());
            this.mcexnud.render(deltaTick);
         }
      }

      public void handleUniform(
         Matrix4f tempMvInverse,
         Matrix4f modelView,
         Matrix4f tempProjInverse,
         Matrix4f projection,
         Matrix4f tempPrevModelView,
         Matrix4f prevModelView,
         Matrix4f tempPrevProjection,
         Matrix4f prevProjection,
         Vector3f cameraPos,
         Vector3f prevCameraPos
      ) {
         if (this.mcexnud != null) {
            this.mcexnud.setUniformValue("mvInverse", tempMvInverse.set(modelView).invert());
            this.mcexnud.setUniformValue("projInverse", tempProjInverse.set(projection).invert());
            this.mcexnud.setUniformValue("prevModelView", tempPrevModelView.set(prevModelView));
            this.mcexnud.setUniformValue("prevProjection", tempPrevProjection.set(prevProjection));
            this.mcexnud.setUniformValue("cameraPos", cameraPos.x, cameraPos.y, cameraPos.z);
            this.mcexnud.setUniformValue("prevCameraPos", prevCameraPos.x, prevCameraPos.y, prevCameraPos.z);
         }
      }

      public static void lambda$new$0(ManagedShaderEffect shader) {
         shader.setUniformValue("BlendFactor", 1.0F);
      }
   }
}
