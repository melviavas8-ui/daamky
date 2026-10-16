package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.gui.MainMenuScreen;
import java.awt.Color;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class RectRenderer {
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("rect/rect"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public static final Vector4f vector4f = new Vector4f();
   public ShaderProgram class_5944;
   public GlUniform class_284;
   public GlUniform class_2842;
   public GlUniform mgorv;

   public void handleRgb(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color) {
      this.handleVal(matrixStack, x, y, width, height, radius, color.getRGB());
   }

   public void handleVal(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int colorRGBA) {
      vector4f.set(radius, radius, radius, radius);
      this.handleBlend(matrixStack, x, y, width, height, vector4f, colorRGBA);
   }

   public void handleRgb(MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, Color color) {
      this.handleBlend(matrixStack, x, y, width, height, radius, color.getRGB());
   }

   public void handleBlend(MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, int colorRGBA) {
      if (colorRGBA >>> 24 != 0 && !(width <= 0.0F) && !(height <= 0.0F)) {
         Matrix4f var8 = matrixStack.peek().getPositionMatrix();
         float var9 = 0.8F;
         float var10 = var9 * 1.5F;
         float var11 = var9 * 1.5F;
         RenderSystem.enableBlend();
         if (MainMenuScreen.is3DCapturing) {
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
         } else {
            RenderSystem.defaultBlendFunc();
         }

         RenderSystem.disableCull();
         ShaderProgram var12 = RenderSystem.setShader(class_10156);
         if (var12 != null) {
            if (this.class_5944 != var12) {
               this.class_5944 = var12;
               this.class_284 = var12.getUniform("uSize");
               this.class_2842 = var12.getUniform("uRadius");
               this.mgorv = var12.getUniform("uSmoothness");
            }

            if (this.class_284 != null) {
               this.class_284.set(width, height);
            }

            if (this.class_2842 != null) {
               this.class_2842.set(radius.x, radius.y, radius.z, radius.w);
            }

            if (this.mgorv != null) {
               this.mgorv.set(var9);
            }
         }

         BufferBuilder var13 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         var13.vertex(var8, x - var10, y - var11, 0.0F).color(colorRGBA);
         var13.vertex(var8, x - var10, y + height + var11, 0.0F).color(colorRGBA);
         var13.vertex(var8, x + width + var10, y + height + var11, 0.0F).color(colorRGBA);
         var13.vertex(var8, x + width + var10, y - var11, 0.0F).color(colorRGBA);
         BufferRenderer.drawWithGlobalProgram(var13.end());
         RenderSystem.enableCull();
         RenderSystem.enableBlend();
         if (MainMenuScreen.is3DCapturing) {
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
         } else {
            RenderSystem.defaultBlendFunc();
         }
      }
   }
}
