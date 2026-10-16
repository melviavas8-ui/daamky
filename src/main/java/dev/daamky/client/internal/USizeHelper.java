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

public class USizeHelper {
   public static final ShaderProgramKey miigf = new ShaderProgramKey(InputHelper.getShader("rect/rect"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public static final Vector4f vector4f = new Vector4f();
   public ShaderProgram mdeomr;
   public GlUniform class_284;
   public GlUniform class_2842;
   public GlUniform mumtom;

   public void handleRgb(
      MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight
   ) {
      vector4f.set(radius, radius, radius, radius);
      this.handleBlend(matrixStack, x, y, width, height, vector4f, topLeft.getRGB(), topRight.getRGB(), bottomLeft.getRGB(), bottomRight.getRGB());
   }

   public void handleRgb(
      MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight
   ) {
      this.handleBlend(matrixStack, x, y, width, height, radius, topLeft.getRGB(), topRight.getRGB(), bottomLeft.getRGB(), bottomRight.getRGB());
   }

   public void handleBlend(
      MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, int topLeft, int topRight, int bottomLeft, int bottomRight
   ) {
      if ((topLeft | topRight | bottomLeft | bottomRight) >>> 24 != 0 && !(width <= 0.0F) && !(height <= 0.0F)) {
         Matrix4f var11 = matrixStack.peek().getPositionMatrix();
         float var12 = 0.8F;
         float var13 = var12 * 1.5F;
         float var14 = var12 * 1.5F;
         RenderSystem.enableBlend();
         if (MainMenuScreen.is3DCapturing) {
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
         } else {
            RenderSystem.defaultBlendFunc();
         }

         RenderSystem.disableCull();
         ShaderProgram var15 = RenderSystem.setShader(miigf);
         if (var15 != null) {
            if (this.mdeomr != var15) {
               this.mdeomr = var15;
               this.class_284 = var15.getUniform("uSize");
               this.class_2842 = var15.getUniform("uRadius");
               this.mumtom = var15.getUniform("uSmoothness");
            }

            if (this.class_284 != null) {
               this.class_284.set(width, height);
            }

            if (this.class_2842 != null) {
               this.class_2842.set(radius.x, radius.y, radius.z, radius.w);
            }

            if (this.mumtom != null) {
               this.mumtom.set(var12);
            }
         }

         BufferBuilder var16 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         var16.vertex(var11, x - var13, y - var14, 0.0F).color(topLeft);
         var16.vertex(var11, x - var13, y + height + var14, 0.0F).color(bottomLeft);
         var16.vertex(var11, x + width + var13, y + height + var14, 0.0F).color(bottomRight);
         var16.vertex(var11, x + width + var13, y - var14, 0.0F).color(topRight);
         BufferRenderer.drawWithGlobalProgram(var16.end());
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
