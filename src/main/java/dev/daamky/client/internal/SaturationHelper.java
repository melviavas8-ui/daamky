package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.module.render.Gamma;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;

public class SaturationHelper implements MinecraftAccess {
   public static final SaturationHelper saturationHelper = new SaturationHelper();
   public static final Matrix4f matrix4f = new Matrix4f();
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("post/copy/copy"), VertexFormats.POSITION, Defines.EMPTY);
   public static final ShaderProgramKey class_101562 = new ShaderProgramKey(
      InputHelper.getShader("post/saturation/saturation"), VertexFormats.POSITION, Defines.EMPTY
   );
   public SimpleFramebuffer class_6367;

   public static SaturationHelper getInstance() {
      return saturationHelper;
   }

   public void initShader() {
      if (Gamma.getInstance().isEnabled()) {
         if (mc != null && mc.world != null && mc.getFramebuffer() != null) {
            int var1 = mc.getFramebuffer().textureWidth;
            int var2 = mc.getFramebuffer().textureHeight;
            if (var1 > 0 && var2 > 0) {
               if (this.class_6367 == null || this.class_6367.textureWidth != var1 || this.class_6367.textureHeight != var2) {
                  if (this.class_6367 != null) {
                     this.class_6367.delete();
                  }

                  this.class_6367 = new SimpleFramebuffer(var1, var2, false);
                  this.class_6367.setTexFilter(9729);
               }

               float var3 = Gamma.getInstance().getEnabled();
               float var4 = Gamma.getInstance().getEnabled2();
               this.class_6367.beginWrite(false);
               RenderSystem.disableDepthTest();
               RenderSystem.disableCull();
               RenderSystem.disableBlend();
               RenderSystem.setShaderTexture(0, mc.getFramebuffer().getColorAttachment());
               RenderSystem.setShader(class_10156);
               run();
               this.class_6367.endWrite();
               mc.getFramebuffer().beginWrite(false);
               RenderSystem.setShaderTexture(0, this.class_6367.getColorAttachment());
               ShaderProgram var5 = RenderSystem.setShader(class_101562);
               if (var5 != null) {
                  if (var5.getUniform("Saturation") != null) {
                     var5.getUniform("Saturation").set(var3);
                  }

                  if (var5.getUniform("Brightness") != null) {
                     var5.getUniform("Brightness").set(var4);
                  }
               }

               run();
               mc.getFramebuffer().endWrite();
               RenderSystem.setShaderTexture(0, 0);
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.enableDepthTest();
               RenderSystem.enableCull();
            }
         }
      }
   }

   public static void run() {
      BufferBuilder var0 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION);
      var0.vertex(matrix4f, -1.0F, -1.0F, 0.0F);
      var0.vertex(matrix4f, -1.0F, 1.0F, 0.0F);
      var0.vertex(matrix4f, 1.0F, 1.0F, 0.0F);
      var0.vertex(matrix4f, 1.0F, -1.0F, 0.0F);
      BufferRenderer.drawWithGlobalProgram(var0.end());
   }
}
