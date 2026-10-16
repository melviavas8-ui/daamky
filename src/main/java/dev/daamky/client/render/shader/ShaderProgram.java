package dev.daamky.client.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.module.render.Interface;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ShaderProgram {
   public static final ShaderProgramKey mtuicec = new ShaderProgramKey(InputHelper.getShader("rect/blurred_rect"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
   public static final Vector4f vector4f = new Vector4f();
   public net.minecraft.client.gl.ShaderProgram class_5944;
   public GlUniform class_284;
   public GlUniform class_2842;
   public GlUniform class_2843;
   public GlUniform class_2844;
   public GlUniform class_2845;
   public GlUniform madkao;
   public GlUniform class_2846;
   public GlUniform class_2847;
   public GlUniform class_2848;
   public GlUniform class_2849;
   public GlUniform mayko;

   public void handleRgb(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color, float mix) {
      vector4f.set(radius, radius, radius, radius);
      this.handleMin(matrixStack, x, y, width, height, vector4f, color.getRGB(), color.getRGB(), color.getRGB(), color.getRGB(), mix);
   }

   public void handleVal(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int colorRGBA, float mix) {
      vector4f.set(radius, radius, radius, radius);
      this.handleMin(matrixStack, x, y, width, height, vector4f, colorRGBA, colorRGBA, colorRGBA, colorRGBA, mix);
   }

   public void handleRgb(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color) {
      vector4f.set(radius, radius, radius, radius);
      this.handleMin(matrixStack, x, y, width, height, vector4f, color.getRGB(), color.getRGB(), color.getRGB(), color.getRGB(), Interface.getDay2());
   }

   public void handleVal(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int colorRGBA) {
      vector4f.set(radius, radius, radius, radius);
      this.handleMin(matrixStack, x, y, width, height, vector4f, colorRGBA, colorRGBA, colorRGBA, colorRGBA, Interface.getDay2());
   }

   public void handleRgb(MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, Color color) {
      this.handleMin(matrixStack, x, y, width, height, radius, color.getRGB(), color.getRGB(), color.getRGB(), color.getRGB(), Interface.getDay2());
   }

   public void handleVal(MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, int colorRGBA) {
      this.handleMin(matrixStack, x, y, width, height, radius, colorRGBA, colorRGBA, colorRGBA, colorRGBA, Interface.getDay2());
   }

   public void handleRgb(
      MatrixStack matrixStack, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight
   ) {
      this.handleMin(
         matrixStack, x, y, width, height, radius, topLeft.getRGB(), topRight.getRGB(), bottomLeft.getRGB(), bottomRight.getRGB(), Interface.getDay2()
      );
   }

   public void handleRgb(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      Color topLeft,
      Color topRight,
      Color bottomLeft,
      Color bottomRight,
      float mix
   ) {
      this.handleMin(matrixStack, x, y, width, height, radius, topLeft.getRGB(), topRight.getRGB(), bottomLeft.getRGB(), bottomRight.getRGB(), mix);
   }

   public void handleMin(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      int topLeft,
      int topRight,
      int bottomLeft,
      int bottomRight,
      float mix
   ) {
      if (!(width <= 0.0F) && !(height <= 0.0F)) {
         if (UHalfTexelSizeHelper.list.isEmpty()) {
            UHalfTexelSizeHelper.handleClass_4587(matrixStack);
         }

         if (!(mix >= 1.0F) && !UHalfTexelSizeHelper.list.isEmpty()) {
            MinecraftClient var23 = MinecraftClient.getInstance();
            if (var23 != null && var23.getWindow() != null) {
               Framebuffer var24 = (Framebuffer)UHalfTexelSizeHelper.list.getFirst();
               float var25 = 0.8F;
               float var26 = (float)((topLeft >> 24 & 0xFF) + (bottomLeft >> 24 & 0xFF) + (bottomRight >> 24 & 0xFF) + (topRight >> 24 & 0xFF)) / 1020.0F;
               if (var26 <= 0.001F) {
                  var26 = 1.0F;
               }

               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.disableCull();
               RenderSystem.setShaderTexture(0, var24.getColorAttachment());
               net.minecraft.client.gl.ShaderProgram var16 = RenderSystem.setShader(mtuicec);
               if (var16 == null) {
                  Render.uSizeHelper.handleBlend(matrixStack, x, y, width, height, radius, topLeft, topRight, bottomLeft, bottomRight);
               } else {
                  if (this.class_5944 != var16) {
                     this.class_5944 = var16;
                     this.class_284 = var16.getUniform("uSize");
                     this.class_2842 = var16.getUniform("uRadius");
                     this.class_2843 = var16.getUniform("uMix");
                     this.class_2844 = var16.getUniform("uSmoothness");
                     this.class_2845 = var16.getUniform("uAlpha");
                     this.madkao = var16.getUniform("uTopLeftColor");
                     this.class_2846 = var16.getUniform("uBottomLeftColor");
                     this.class_2847 = var16.getUniform("uTopRightColor");
                     this.class_2848 = var16.getUniform("uBottomRightColor");
                     this.class_2849 = var16.getUniform("uGlowColor");
                     this.mayko = var16.getUniform("uGlowRadius");
                  }

                  if (this.class_284 != null) {
                     this.class_284.set(width, height);
                  }

                  if (this.class_2842 != null) {
                     this.class_2842.set(radius.x, radius.z, radius.w, radius.y);
                  }

                  if (this.class_2843 != null) {
                     this.class_2843.set(mix);
                  }

                  if (this.class_2844 != null) {
                     this.class_2844.set(var25);
                  }

                  if (this.class_2845 != null) {
                     this.class_2845.set(var26);
                  }

                  if (this.madkao != null) {
                     this.madkao
                        .set(
                           (float)(topLeft >> 16 & 0xFF) / 255.0F,
                           (float)(topLeft >> 8 & 0xFF) / 255.0F,
                           (float)(topLeft & 0xFF) / 255.0F,
                           (float)(topLeft >> 24 & 0xFF) / 255.0F
                        );
                  }

                  if (this.class_2846 != null) {
                     this.class_2846
                        .set(
                           (float)(bottomLeft >> 16 & 0xFF) / 255.0F,
                           (float)(bottomLeft >> 8 & 0xFF) / 255.0F,
                           (float)(bottomLeft & 0xFF) / 255.0F,
                           (float)(bottomLeft >> 24 & 0xFF) / 255.0F
                        );
                  }

                  if (this.class_2847 != null) {
                     this.class_2847
                        .set(
                           (float)(topRight >> 16 & 0xFF) / 255.0F,
                           (float)(topRight >> 8 & 0xFF) / 255.0F,
                           (float)(topRight & 0xFF) / 255.0F,
                           (float)(topRight >> 24 & 0xFF) / 255.0F
                        );
                  }

                  if (this.class_2848 != null) {
                     this.class_2848
                        .set(
                           (float)(bottomRight >> 16 & 0xFF) / 255.0F,
                           (float)(bottomRight >> 8 & 0xFF) / 255.0F,
                           (float)(bottomRight & 0xFF) / 255.0F,
                           (float)(bottomRight >> 24 & 0xFF) / 255.0F
                        );
                  }

                  if (this.class_2849 != null) {
                     this.class_2849.set(0.0F, 0.0F, 0.0F, 0.0F);
                  }

                  if (this.mayko != null) {
                     this.mayko.set(0.0F);
                  }

                  float var17 = x - 0.6F;
                  float var18 = y - 0.6F;
                  float var19 = width + 1.2F;
                  float var20 = height + 1.2F;
                  Matrix4f var21 = matrixStack.peek().getPositionMatrix();
                  BufferBuilder var22 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                  var22.vertex(var21, var17, var18, 0.0F).texture(0.0F, 0.0F).color(topLeft);
                  var22.vertex(var21, var17, var18 + var20, 0.0F).texture(0.0F, 1.0F).color(bottomLeft);
                  var22.vertex(var21, var17 + var19, var18 + var20, 0.0F).texture(1.0F, 1.0F).color(bottomRight);
                  var22.vertex(var21, var17 + var19, var18, 0.0F).texture(1.0F, 0.0F).color(topRight);
                  BufferRenderer.drawWithGlobalProgram(var22.end());
                  RenderSystem.enableCull();
                  RenderSystem.disableBlend();
               }
            }
         } else {
            int var12 = topLeft & 16777215 | Math.min(255, (int)((float)(topLeft >> 24 & 0xFF) * 2.2F)) << 24;
            int var13 = topRight & 16777215 | Math.min(255, (int)((float)(topRight >> 24 & 0xFF) * 2.2F)) << 24;
            int var14 = bottomLeft & 16777215 | Math.min(255, (int)((float)(bottomLeft >> 24 & 0xFF) * 2.2F)) << 24;
            int var15 = bottomRight & 16777215 | Math.min(255, (int)((float)(bottomRight >> 24 & 0xFF) * 2.2F)) << 24;
            Render.uSizeHelper.handleBlend(matrixStack, x, y, width, height, radius, var12, var13, var14, var15);
         }
      }
   }
}
