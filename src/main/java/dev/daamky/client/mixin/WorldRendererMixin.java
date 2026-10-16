package dev.daamky.client.mixin;

import dev.daamky.client.module.render.MotionBlur;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.ObjectAllocator;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldRenderer.class})
public class WorldRendererMixin {
   @Unique
   private Matrix4f prevModelView = new Matrix4f();
   @Unique
   private Matrix4f prevProjection = new Matrix4f();
   @Unique
   private Vector3f prevCameraPos = new Vector3f();
   @Unique
   private final Vector3f tempCameraPos = new Vector3f();

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void setMatrices(
      ObjectAllocator allocator,
      RenderTickCounter tickCounter,
      boolean renderBlockOutline,
      Camera camera,
      GameRenderer gameRenderer,
      Matrix4f positionMatrix,
      Matrix4f projectionMatrix,
      CallbackInfo ci
   ) {
      if (MotionBlur.getInstance().isEnabled() && MotionBlur.getInstance().irisHelper.check()) {
         float var9 = tickCounter.getTickDelta(true);
         MotionBlur.getInstance()
            .irisHelper
            .handleVector3f(
               positionMatrix,
               this.prevModelView,
               gameRenderer.getBasicProjectionMatrix(gameRenderer.getFov(camera, var9, true)),
               this.prevProjection,
               this.tempCameraPos
                  .set(
                     (float)(camera.getPos().x % 30000.0),
                     (float)(camera.getPos().y % 30000.0),
                     (float)(camera.getPos().z % 30000.0)
                  ),
               this.prevCameraPos
            );
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void setOldMatrices(
      ObjectAllocator allocator,
      RenderTickCounter tickCounter,
      boolean renderBlockOutline,
      Camera camera,
      GameRenderer gameRenderer,
      Matrix4f positionMatrix,
      Matrix4f projectionMatrix,
      CallbackInfo ci
   ) {
      if (MotionBlur.getInstance().isEnabled() && MotionBlur.getInstance().irisHelper.check()) {
         this.prevModelView.set(positionMatrix);
         float var9 = tickCounter.getTickDelta(true);
         this.prevProjection.set(gameRenderer.getBasicProjectionMatrix(gameRenderer.getFov(camera, var9, true)));
         this.prevCameraPos
            .set(
               (float)(camera.getPos().x % 30000.0),
               (float)(camera.getPos().y % 30000.0),
               (float)(camera.getPos().z % 30000.0)
            );
      }
   }
}
