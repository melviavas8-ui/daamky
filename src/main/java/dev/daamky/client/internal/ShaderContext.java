package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.module.render.HandShader;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

public class ShaderContext implements MinecraftAccess {
   public static final ShaderContext shaderContext = new ShaderContext();
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(
      Identifier.of("daamky", "core/noise/noise_shader"), VertexFormats.POSITION_COLOR, Defines.EMPTY
   );
   public final Matrix4f mfehobf = new Matrix4f();
   public SimpleFramebuffer class_6367;

   public static ShaderContext getInstance() {
      return shaderContext;
   }

   public void initEnabled() {
      if (mc.options.getPerspective().isFirstPerson() && HandShader.getInstance().isEnabled()) {
         if (mc.getFramebuffer() == null || mc.getWindow() == null) {
            return;
         }

         int var1 = mc.getWindow().getFramebufferWidth();
         int var2 = mc.getWindow().getFramebufferHeight();
         if (var1 <= 0 || var2 <= 0) {
            return;
         }

         if (this.class_6367 == null || this.class_6367.textureWidth != var1 || this.class_6367.textureHeight != var2) {
            if (this.class_6367 != null) {
               this.class_6367.delete();
            }

            this.class_6367 = new SimpleFramebuffer(var1, var2, true);
         }

         this.class_6367.copyDepthFrom(mc.getFramebuffer());
      }
   }

   public void initShader() {
      if (mc.options.getPerspective().isFirstPerson() && HandShader.getInstance().isEnabled()) {
         if (this.class_6367 == null || mc.getFramebuffer() == null) {
            return;
         }

         RenderSystem.backupProjectionMatrix();
         RenderSystem.setProjectionMatrix(this.mfehobf, ProjectionType.PERSPECTIVE);
         Matrix4fStack var1 = RenderSystem.getModelViewStack();
         var1.pushMatrix().identity();
         RenderSystem.disableDepthTest();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.setShaderTexture(0, mc.getFramebuffer().getColorAttachment());
         RenderSystem.setShaderTexture(1, mc.getFramebuffer().getDepthAttachment());
         RenderSystem.setShaderTexture(2, this.class_6367.getDepthAttachment());
         ShaderProgram var2 = RenderSystem.setShader(class_10156);
         if (var2 != null) {
            GlUniform var3 = var2.getUniform("TintColor");
            if (var3 != null) {
               Color var4 = HandShader.getInstance().getEquals();
               float var5 = HandShader.getInstance().getOpacity().getValue();
               var3.set((float)var4.getRed() / 255.0F, (float)var4.getGreen() / 255.0F, (float)var4.getBlue() / 255.0F, var5);
            }

            GlUniform var8 = var2.getUniform("Time");
            if (var8 != null) {
               float var10 = HandShader.getInstance().getSpeed().getValue();
               var8.set((float)(System.currentTimeMillis() % 1000000L) / 1000.0F * var10);
            }

            GlUniform var11 = var2.getUniform("Scale");
            if (var11 != null) {
               var11.set(HandShader.getInstance().getScale().getValue());
            }

            GlUniform var6 = var2.getUniform("Mode");
            if (var6 != null) {
               var6.set(HandShader.getInstance().getCase());
            }
         }

         int var7 = new Color(255, 255, 255, 255).getRGB();
         BufferBuilder var9 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         var9.vertex(-1.0F, -1.0F, 0.0F).color(var7);
         var9.vertex(-1.0F, 1.0F, 0.0F).color(var7);
         var9.vertex(1.0F, 1.0F, 0.0F).color(var7);
         var9.vertex(1.0F, -1.0F, 0.0F).color(var7);
         BufferRenderer.drawWithGlobalProgram(var9.end());
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.setShaderTexture(1, 0);
         RenderSystem.setShaderTexture(2, 0);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         RenderSystem.enableDepthTest();
         var1.popMatrix();
         RenderSystem.restoreProjectionMatrix();
      }
   }
}
