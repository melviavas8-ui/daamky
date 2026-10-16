package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class AvatarPngHelper {
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("rect/texture_rect"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
   public static final Vector4f vector4f = new Vector4f();

   public void handleColor(MatrixStack matrixStack, Identifier texture, float x, float y, float width, float height, Color color) {
      this.handleRgb(matrixStack, x, y, width, height, 0.0F, color, 0.0F, 0.0F, 1.0F, 1.0F, texture);
   }

   public void handleColor(MatrixStack matrixStack, Identifier texture, float x, float y, float width, float height, float radius, Color color) {
      this.handleRgb(matrixStack, x, y, width, height, radius, color, 0.0F, 0.0F, 1.0F, 1.0F, texture);
   }

   public void handleColor(MatrixStack matrixStack, Identifier texture, float x, float y, float width, float height, float u, float v, Color color) {
      this.handleRgb(matrixStack, x, y, width, height, 0.0F, color, u, v, 1.0F, 1.0F, texture);
   }

   public void handleClass_2960(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color, Identifier texture) {
      this.handleRgb(matrixStack, x, y, width, height, radius, color, 0.0F, 0.0F, 1.0F, 1.0F, texture);
   }

   public void handleRgb(MatrixStack matrixStack, PlayerEntity player, float x, float y, float width, float height, float gap, float radius, Color color) {
      this.handleAvatar(matrixStack, player, x, y, width, height, gap, radius, color != null ? color.getRGB() : -1);
   }

   public void handleAvatar(MatrixStack matrixStack, PlayerEntity player, float x, float y, float width, float height, float gap, float radius, int colorRGBA) {
      this.handleClass_2960(matrixStack, x, y, width, height, radius, colorRGBA, 0.0F, 0.0F, 1.0F, 1.0F, Identifier.of("daamky", "avatar.png"));
   }

   public void handleRgb(MatrixStack matrixStack, Identifier skin, float x, float y, float width, float height, float gap, float radius, Color color) {
      this.handleAvatar(matrixStack, skin, x, y, width, height, gap, radius, color != null ? color.getRGB() : -1);
   }

   public void handleAvatar(MatrixStack matrixStack, Identifier skin, float x, float y, float width, float height, float gap, float radius, int colorRGBA) {
      this.handleClass_2960(matrixStack, x, y, width, height, radius, colorRGBA, 0.0F, 0.0F, 1.0F, 1.0F, Identifier.of("daamky", "avatar.png"));
   }

   public boolean computeFlag(MatrixStack matrixStack, LivingEntity entity, float x, float y, float width, float height, float gap, float radius, int colorRGBA) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && colorRGBA >>> 24 != 0) {
         MinecraftClient var10 = MinecraftClient.getInstance();
         if (var10 != null && var10.getEntityRenderDispatcher() != null) {
            try {
               EntityRenderDispatcher var11 = var10.getEntityRenderDispatcher();
               if (!(var11.getRenderer(entity) instanceof LivingEntityRenderer var13)) {
                  return false;
               } else {
                  LivingEntityRenderState var14 = (LivingEntityRenderState)var13.getAndUpdateRenderState(entity, 1.0F);
                  Identifier var15 = var13.getTexture(var14);
                  if (var15 != null && var10.getTextureManager().getTexture(var15) != null) {
                     float var16 = 0.125F;
                     float var17 = 0.625F;
                     float var18 = gap * 2.0F;
                     int var19 = var10.getTextureManager().getTexture(var15).getGlId();
                     Render.myazem
                        .handleVal(matrixStack, x + gap, y + gap, width - var18, height - var18, radius, colorRGBA, var16, var16, var16, var16, var19);
                     Render.myazem.handleVal(matrixStack, x, y, width, height, radius, colorRGBA, var17, var16, var16, var16, var19);
                     return true;
                  } else {
                     return false;
                  }
               }
            } catch (Throwable var20) {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void handleRgb(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      float radius,
      Color color,
      float u,
      float v,
      float texWidth,
      float texHeight,
      Identifier texture
   ) {
      this.handleClass_2960(matrixStack, x, y, width, height, radius, color != null ? color.getRGB() : -1, u, v, texWidth, texHeight, texture);
   }

   public void handleClass_2960(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      float radius,
      int colorRGBA,
      float u,
      float v,
      float texWidth,
      float texHeight,
      Identifier texture
   ) {
      vector4f.set(radius, radius, radius, radius);
      this.handleBlend(matrixStack, x, y, width, height, vector4f, colorRGBA, u, v, texWidth, texHeight, texture);
   }

   public void handleRgb(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      Color color,
      float u,
      float v,
      float texWidth,
      float texHeight,
      Identifier texture
   ) {
      this.handleBlend(matrixStack, x, y, width, height, radius, color != null ? color.getRGB() : -1, u, v, texWidth, texHeight, texture);
   }

   public void handleBlend(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      int colorRGBA,
      float u,
      float v,
      float texWidth,
      float texHeight,
      Identifier texture
   ) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && colorRGBA >>> 24 != 0 && texture != null) {
         Matrix4f var13 = matrixStack.peek().getPositionMatrix();
         float var14 = 0.0F;
         float var15 = 0.8F;
         float var16 = -var15 / 2.0F + var15 * 2.0F;
         float var17 = var15 / 2.0F + var15;
         float var18 = x - var16 / 2.0F;
         float var19 = y - var17 / 2.0F;
         float var20 = width + var16;
         float var21 = height + var17;
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.setShaderTexture(0, texture);
         RenderSystem.texParameter(3553, 10241, 9728);
         RenderSystem.texParameter(3553, 10240, 9728);
         ShaderProgram var22 = RenderSystem.setShader(class_10156);
         if (var22 != null) {
            if (var22.getUniform("uSize") != null) {
               var22.getUniform("uSize").set(width, height);
            }

            if (var22.getUniform("uRadius") != null) {
               var22.getUniform("uRadius").set(radius.x, radius.z, radius.w, radius.y);
            }

            if (var22.getUniform("uSmoothness") != null) {
               var22.getUniform("uSmoothness").set(var15);
            }
         }

         BufferBuilder var23 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         var23.vertex(var13, var18, var19, var14).texture(u, v).color(colorRGBA);
         var23.vertex(var13, var18, var19 + var21, var14).texture(u, v + texHeight).color(colorRGBA);
         var23.vertex(var13, var18 + var20, var19 + var21, var14).texture(u + texWidth, v + texHeight).color(colorRGBA);
         var23.vertex(var13, var18 + var20, var19, var14).texture(u + texWidth, v).color(colorRGBA);
         BufferRenderer.drawWithGlobalProgram(var23.end());
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }

   public void handleRgb(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      float radius,
      Color color,
      float u,
      float v,
      float texWidth,
      float texHeight,
      int texture
   ) {
      this.handleVal(matrixStack, x, y, width, height, radius, color != null ? color.getRGB() : -1, u, v, texWidth, texHeight, texture);
   }

   public void handleVal(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      float radius,
      int colorRGBA,
      float u,
      float v,
      float texWidth,
      float texHeight,
      int texture
   ) {
      vector4f.set(radius, radius, radius, radius);
      this.handleBlend(matrixStack, x, y, width, height, vector4f, colorRGBA, u, v, texWidth, texHeight, texture);
   }

   public void handleRgb(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      Color color,
      float u,
      float v,
      float texWidth,
      float texHeight,
      int texture
   ) {
      this.handleBlend(matrixStack, x, y, width, height, radius, color != null ? color.getRGB() : -1, u, v, texWidth, texHeight, texture);
   }

   public void handleBlend(
      MatrixStack matrixStack,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      int colorRGBA,
      float u,
      float v,
      float texWidth,
      float texHeight,
      int texture
   ) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && colorRGBA >>> 24 != 0 && texture > 0) {
         Matrix4f var13 = matrixStack.peek().getPositionMatrix();
         float var14 = 0.0F;
         float var15 = 0.8F;
         float var16 = -var15 / 2.0F + var15 * 2.0F;
         float var17 = var15 / 2.0F + var15;
         float var18 = x - var16 / 2.0F;
         float var19 = y - var17 / 2.0F;
         float var20 = width + var16;
         float var21 = height + var17;
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.setShaderTexture(0, texture);
         RenderSystem.texParameter(3553, 10241, 9728);
         RenderSystem.texParameter(3553, 10240, 9728);
         ShaderProgram var22 = RenderSystem.setShader(class_10156);
         if (var22 != null) {
            if (var22.getUniform("uSize") != null) {
               var22.getUniform("uSize").set(width, height);
            }

            if (var22.getUniform("uRadius") != null) {
               var22.getUniform("uRadius").set(radius.x, radius.z, radius.w, radius.y);
            }

            if (var22.getUniform("uSmoothness") != null) {
               var22.getUniform("uSmoothness").set(var15);
            }
         }

         BufferBuilder var23 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         var23.vertex(var13, var18, var19, var14).texture(u, v).color(colorRGBA);
         var23.vertex(var13, var18, var19 + var21, var14).texture(u, v + texHeight).color(colorRGBA);
         var23.vertex(var13, var18 + var20, var19 + var21, var14).texture(u + texWidth, v + texHeight).color(colorRGBA);
         var23.vertex(var13, var18 + var20, var19, var14).texture(u + texWidth, v).color(colorRGBA);
         BufferRenderer.drawWithGlobalProgram(var23.end());
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }
}
