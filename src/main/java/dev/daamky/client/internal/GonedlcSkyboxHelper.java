package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.render.shader.WorldShaderUniforms;
import java.awt.Color;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

public final class GonedlcSkyboxHelper {
   public static final float floatVal = 100.0F;
   public static final int intVal = 1536;
   public static final Map<Identifier, RenderLayer> map = new HashMap<>();
   public static final Map<WorldShaderUniforms, RenderLayer> map2 = new IdentityHashMap<>();
   public static VertexBuffer class_291;

   public static void computeColor(Identifier texture, Color color) {
      if (texture != null && color != null) {
         run();
         RenderSystem.setShaderColor(
            (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
         );
         class_291.draw(map.computeIfAbsent(texture, GonedlcSkyboxHelper::initInit));
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public static void computeColor(WorldShaderUniforms shader, Color color, float time, float opacity) {
      if (shader != null && color != null) {
         run();
         shader.handleEquals(time, color);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.max(0.0F, Math.min(1.0F, opacity)));
         class_291.draw(map2.computeIfAbsent(shader, GonedlcSkyboxHelper::computeShader));
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public static void handlePut(WorldShaderUniforms shader) {
      if (shader != null) {
         map2.put(shader, computeShader(shader));
      }
   }

   public static void closeClear() {
      if (class_291 != null && !class_291.isClosed()) {
         class_291.close();
      }

      class_291 = null;
      map.clear();
      map2.clear();
   }

   public static void run() {
      if (class_291 == null || class_291.isClosed()) {
         class_291 = net.minecraft.client.gl.VertexBuffer.createAndUpload(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR, GonedlcSkyboxHelper::handleClass_4588);
      }
   }

   public static RenderLayer initInit(Identifier texture) {
      return RenderLayer.of(
         "daamky_skybox",
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         1536,
         false,
         false,
         RenderLayer.MultiPhaseParameters.builder()
            .program(RenderPhase.POSITION_TEXTURE_COLOR_PROGRAM)
            .method_34577(new RenderPhase.Texture(texture, TriState.FALSE, false))
            .method_23615(RenderPhase.TRANSLUCENT_TRANSPARENCY)
            .method_23604(RenderPhase.LEQUAL_DEPTH_TEST)
            .method_23603(RenderPhase.DISABLE_CULLING)
            .method_23616(RenderPhase.COLOR_MASK)
            .method_23617(false)
      );
   }

   public static RenderLayer computeShader(WorldShaderUniforms shader) {
      return RenderLayer.of(
         "daamky_skybox_shader",
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         1536,
         false,
         false,
         RenderLayer.MultiPhaseParameters.builder()
            .program(shader.initInit())
            .method_23615(RenderPhase.TRANSLUCENT_TRANSPARENCY)
            .method_23604(RenderPhase.LEQUAL_DEPTH_TEST)
            .method_23603(RenderPhase.DISABLE_CULLING)
            .method_23616(RenderPhase.COLOR_MASK)
            .method_23617(false)
      );
   }

   public static void handleClass_4588(VertexConsumer vertexConsumer) {
      float var1 = 100.0F;
      handleVal(vertexConsumer, 1, -var1, var1, -var1, -var1, var1, var1, var1, var1, var1, var1, var1, -var1);
      handleVal(vertexConsumer, 0, -var1, -var1, var1, -var1, -var1, -var1, var1, -var1, -var1, var1, -var1, var1);
      handleVal(vertexConsumer, 2, var1, var1, -var1, var1, -var1, -var1, -var1, -var1, -var1, -var1, var1, -var1);
      handleVal(vertexConsumer, 4, -var1, var1, var1, -var1, -var1, var1, var1, -var1, var1, var1, var1, var1);
      handleVal(vertexConsumer, 3, -var1, var1, -var1, -var1, -var1, -var1, -var1, -var1, var1, -var1, var1, var1);
      handleVal(vertexConsumer, 5, var1, var1, var1, var1, -var1, var1, var1, -var1, -var1, var1, var1, -var1);
   }

   public static void handleVal(
      VertexConsumer vertexConsumer,
      int face,
      float x1,
      float y1,
      float z1,
      float x2,
      float y2,
      float z2,
      float x3,
      float y3,
      float z3,
      float x4,
      float y4,
      float z4
   ) {
      int var14 = face % 3;
      int var15 = face / 3;
      float var16 = (float)var14 / 3.0F;
      float var17 = (float)(var14 + 1) / 3.0F;
      float var18 = (float)var15 / 2.0F;
      float var19 = (float)(var15 + 1) / 2.0F;
      vertexConsumer.vertex(x1, y1, z1).texture(var16, var18).color(-1);
      vertexConsumer.vertex(x2, y2, z2).texture(var16, var19).color(-1);
      vertexConsumer.vertex(x3, y3, z3).texture(var17, var19).color(-1);
      vertexConsumer.vertex(x4, y4, z4).texture(var17, var18).color(-1);
   }
}
