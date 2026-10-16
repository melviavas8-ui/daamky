package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
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

public class RingRenderer {
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("rect/ring"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public ShaderProgram class_5944;
   public GlUniform class_284;
   public GlUniform class_2842;
   public GlUniform meavim;
   public GlUniform class_2843;
   public GlUniform mulva;
   public GlUniform class_2844;

   public void handleRgb(MatrixStack matrixStack, float cx, float cy, float outerRadius, float thickness, Color color) {
      this.handleBlend(matrixStack, cx, cy, outerRadius, thickness, 0.0F, 0.0F, (float) -Math.PI, (float) Math.PI, true, color.getRGB());
   }

   public void handleVal(MatrixStack matrixStack, float cx, float cy, float outerRadius, float thickness, int colorRGBA) {
      this.handleBlend(matrixStack, cx, cy, outerRadius, thickness, 0.0F, 0.0F, (float) -Math.PI, (float) Math.PI, true, colorRGBA);
   }

   public void handleRgb(MatrixStack matrixStack, float cx, float cy, float radius, Color color) {
      this.handleBlend(matrixStack, cx, cy, radius, radius + 3.0F, 0.0F, 0.0F, (float) -Math.PI, (float) Math.PI, true, color.getRGB());
   }

   public void handleVal(MatrixStack matrixStack, float cx, float cy, float radius, int colorRGBA) {
      this.handleBlend(matrixStack, cx, cy, radius, radius + 3.0F, 0.0F, 0.0F, (float) -Math.PI, (float) Math.PI, true, colorRGBA);
   }

   public void handleRadians(MatrixStack matrixStack, float cx, float cy, float outerRadius, float thickness, float startDeg, float endDeg, Color color) {
      float var9 = (float)Math.toRadians((double)startDeg);
      float var10 = (float)Math.toRadians((double)endDeg);
      this.handleBlend(matrixStack, cx, cy, outerRadius, thickness, 0.0F, 0.0F, var9, var10, false, color.getRGB());
   }

   public void handleRadians(MatrixStack matrixStack, float cx, float cy, float outerRadius, float thickness, float startDeg, float endDeg, int colorRGBA) {
      float var9 = (float)Math.toRadians((double)startDeg);
      float var10 = (float)Math.toRadians((double)endDeg);
      this.handleBlend(matrixStack, cx, cy, outerRadius, thickness, 0.0F, 0.0F, var9, var10, false, colorRGBA);
   }

   public void handleRgb(MatrixStack matrixStack, float cx, float cy, float outerRadius, float innerRadius, float offX, float offY, Color color) {
      this.handleVal(matrixStack, cx, cy, outerRadius, innerRadius, offX, offY, color != null ? color.getRGB() : -1);
   }

   public void handleVal(MatrixStack matrixStack, float cx, float cy, float outerRadius, float innerRadius, float offX, float offY, int colorRGBA) {
      this.handleBlend(matrixStack, cx, cy, outerRadius, outerRadius - innerRadius, offX, offY, (float) -Math.PI, (float) Math.PI, true, colorRGBA);
   }

   public void handleVal(
      MatrixStack matrixStack, float cx, float cy, float outerRadius, float thickness, float startRad, float endRad, boolean fullCircle, int colorRGBA
   ) {
      this.handleBlend(matrixStack, cx, cy, outerRadius, thickness, 0.0F, 0.0F, startRad, endRad, fullCircle, colorRGBA);
   }

   public void handleBlend(
      MatrixStack matrixStack,
      float cx,
      float cy,
      float outerRadius,
      float thickness,
      float offX,
      float offY,
      float startRad,
      float endRad,
      boolean fullCircle,
      int colorRGBA
   ) {
      if (colorRGBA >>> 24 != 0 && !(outerRadius <= 0.0F)) {
         float var12 = outerRadius * 2.0F;
         float var13 = cx - outerRadius;
         float var14 = cy - outerRadius;
         float var15 = outerRadius - thickness;
         Matrix4f var16 = matrixStack.peek().getPositionMatrix();
         float var17 = 1.2F;
         float var18 = var17 + 1.0F;
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         ShaderProgram var19 = RenderSystem.setShader(class_10156);
         if (var19 != null) {
            if (this.class_5944 != var19) {
               this.class_5944 = var19;
               this.class_284 = var19.getUniform("uSize");
               this.class_2842 = var19.getUniform("uInnerRadius");
               this.meavim = var19.getUniform("uSmoothness");
               this.class_2843 = var19.getUniform("uStartAngle");
               this.mulva = var19.getUniform("uEndAngle");
               this.class_2844 = var19.getUniform("uInnerOffset");
            }

            if (this.class_284 != null) {
               this.class_284.set(var12, var12);
            }

            if (this.class_2842 != null) {
               this.class_2842.set(var15);
            }

            if (this.meavim != null) {
               this.meavim.set(var17);
            }

            if (this.class_2843 != null) {
               this.class_2843.set(startRad);
            }

            if (this.mulva != null) {
               this.mulva.set(fullCircle ? startRad + 7.0F : endRad);
            }

            if (this.class_2844 != null) {
               this.class_2844.set(offX, offY);
            }
         }

         BufferBuilder var20 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         var20.vertex(var16, var13 - var18, var14 - var18, 0.0F).color(colorRGBA);
         var20.vertex(var16, var13 - var18, var14 + var12 + var18, 0.0F).color(colorRGBA);
         var20.vertex(var16, var13 + var12 + var18, var14 + var12 + var18, 0.0F).color(colorRGBA);
         var20.vertex(var16, var13 + var12 + var18, var14 - var18, 0.0F).color(colorRGBA);
         BufferRenderer.drawWithGlobalProgram(var20.end());
         RenderSystem.enableCull();
         RenderSystem.enableBlend();
      }
   }
}
