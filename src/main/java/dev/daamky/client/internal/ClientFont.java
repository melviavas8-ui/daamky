package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.misc.Anonymous;
import java.awt.Color;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

public final class ClientFont {
   public static final ShaderProgramKey shaderKey = new ShaderProgramKey(InputHelper.getShader("text"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
   public final String name;
   public AbstractTexture texture;
   public AtlasDataHelper.AtlasData atlas;
   public AtlasDataHelper.MetricsData metrics;
   public Map<Integer, BottomHelper> glyphs;
   public Map<Integer, Map<Integer, Float>> kernings;
   public BottomHelper[] fastGlyphs;
   public boolean loaded = false;
   public static final int WIDTH_CACHE_SIZE = 1024;
   public static final int WIDTH_CACHE_MASK = 1023;
   public final String[] widthCacheKeys = new String[1024];
   public final int[] widthCacheProps = new int[1024];
   public final float[] widthCacheValues = new float[1024];

   public ClientFont(String fontPath) {
      this.name = fontPath;
   }

   public ClientFont(
      String name,
      AbstractTexture texture,
      AtlasDataHelper.AtlasData atlas,
      AtlasDataHelper.MetricsData metrics,
      Map<Integer, BottomHelper> glyphs,
      Map<Integer, Map<Integer, Float>> kernings
   ) {
      this.name = name;
      this.texture = texture;
      this.atlas = atlas;
      this.metrics = metrics;
      this.glyphs = glyphs;
      this.kernings = kernings;
      this.runNext();
      this.loaded = true;
   }

   public void runNext() {
      if (this.glyphs != null) {
         this.fastGlyphs = new BottomHelper[1200];

         for (Entry var2 : this.glyphs.entrySet()) {
            int var3 = (Integer)var2.getKey();
            if (var3 >= 0 && var3 < 1200) {
               this.fastGlyphs[var3] = (BottomHelper)var2.getValue();
            }
         }
      }
   }

   public BottomHelper computeFast(int code) {
      if (code >= 0 && code < 1200 && this.fastGlyphs != null) {
         return this.fastGlyphs[code];
      } else {
         return this.glyphs != null ? this.glyphs.get(code) : null;
      }
   }

   public void loadName() {
      if (!this.loaded) {
         synchronized (this) {
            if (!this.loaded) {
               try {
                  if (MinecraftClient.getInstance() == null || MinecraftClient.getInstance().getTextureManager() == null || MinecraftClient.getInstance().getResourceManager() == null) {
                     return;
                  }

                  System.out.println("[GONE CLIENT FONT] Lazy loading font: " + this.name);
                  ClientFont var3 = initInit().computeCase(this.name).load();
                  this.texture = var3.texture;
                  this.atlas = var3.atlas;
                  this.metrics = var3.metrics;
                  this.glyphs = var3.glyphs;
                  this.kernings = var3.kernings;
                  this.runNext();
                  this.loaded = true;
                  System.out
                     .println(
                        "[GONE CLIENT FONT] Font loaded successfully: " + this.name + " with " + (this.glyphs != null ? this.glyphs.size() : 0) + " glyphs."
                     );
               } catch (Exception var5) {
                  System.err.println("[GONE CLIENT FONT ERROR] Failed to load font: " + this.name);
                  var5.printStackTrace(System.err);
               }
            }
         }
      }
   }

   public boolean computeChar(String text) {
      if (text != null && !text.isEmpty()) {
         this.loadName();
         if (this.glyphs == null) {
            return false;
         } else {
            String var2 = Anonymous.getInstance().isEnabled() ? NameHelper.protectedString(text) : text;
            int var3 = var2.length();

            for (int var4 = 0; var4 < var3; var4++) {
               if (this.computeFast(var2.charAt(var4)) != null) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean computeNext(List<BottomHelper.ColoredGlyph> glyphs) {
      this.loadName();
      if (this.glyphs != null && glyphs != null && !glyphs.isEmpty()) {
         for (BottomHelper.ColoredGlyph var3 : glyphs) {
            if (this.glyphs.containsKey(Integer.valueOf(var3.c()))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void handlePrint(
      MatrixStack matrixStack,
      Text text,
      float x,
      float y,
      float size,
      float thickness,
      float smoothness,
      float spacing,
      int outlineColor,
      float outlineThickness
   ) {
      if (text != null) {
         List var11 = TextShaper.computeClear(NameHelper.computeEquals(text));
         if (this.computeNext(var11)) {
            try {
               Matrix4f var12 = matrixStack.peek().getPositionMatrix();
               this.handleOutline(outlineThickness, thickness, smoothness, outlineColor);
               BufferBuilder var13 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               this.handleColor(
                  var12,
                  var13,
                  var11,
                  size,
                  (thickness + outlineThickness * 0.5F) * 0.5F * size,
                  spacing,
                  x,
                  y + this.getMetricsData().getDescender2() * size,
                  0.0F
               );
               this.handleBlend(var13);
            } catch (Exception var14) {
               var14.printStackTrace(System.err);
            }
         }
      }
   }

   public void handlePrint(
      MatrixStack matrixStack,
      String text,
      float x,
      float y,
      float size,
      float thickness,
      int color,
      int colorSecond,
      float offset,
      float smoothness,
      float spacing,
      int outlineColor,
      float outlineThickness
   ) {
      if (this.computeChar(text)) {
         try {
            Matrix4f var14 = matrixStack.peek().getPositionMatrix();
            this.handleOutline(outlineThickness, thickness, smoothness, outlineColor);
            BufferBuilder var15 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            this.handleChar(
               var14,
               var15,
               NameHelper.protectedString(text),
               size,
               (thickness + outlineThickness * 0.5F) * 0.5F * size,
               spacing,
               x,
               y + this.getMetricsData().getDescender2() * size,
               0.0F,
               color,
               colorSecond,
               offset
            );
            this.handleBlend(var15);
         } catch (Exception var16) {
            var16.printStackTrace(System.err);
         }
      }
   }

   public void handleBlend(BufferBuilder builder) {
      BufferRenderer.drawWithGlobalProgram(builder.end());
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.enableBlend();
      if (MainMenuScreen.is3DCapturing) {
         RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
      } else {
         RenderSystem.defaultBlendFunc();
      }
   }

   public void handleOutline(float outlineThickness, float thickness, float smoothness, int outlineColor) {
      RenderSystem.enableBlend();
      if (MainMenuScreen.is3DCapturing) {
         RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
      } else {
         RenderSystem.defaultBlendFunc();
      }

      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      boolean var5 = outlineThickness > 0.0F;
      if (this.texture != null) {
         RenderSystem.setShaderTexture(0, this.texture.getGlId());
      }

      ShaderProgram var6 = RenderSystem.setShader(shaderKey);
      if (var6 != null) {
         float var7 = this.getAtlas() != null && this.getAtlas().getRange() > 0.0F ? this.getAtlas().getRange() : 8.0F;
         if (var6.getUniform("uRange") != null) {
            var6.getUniform("uRange").set(var7);
         }

         if (var6.getUniform("uThickness") != null) {
            var6.getUniform("uThickness").set(thickness);
         }

         if (var6.getUniform("uSmoothness") != null) {
            var6.getUniform("uSmoothness").set(smoothness);
         }

         if (var6.getUniform("uOutline") != null) {
            var6.getUniform("uOutline").set(var5 ? 1 : 0);
         }

         if (var5 && var6.getUniform("uOutlineThickness") != null) {
            var6.getUniform("uOutlineThickness").set(outlineThickness);
            float[] var8 = ColorMath.computeFloats(outlineColor);
            if (var6.getUniform("uOutlineColor") != null) {
               var6.getUniform("uOutlineColor").set(var8[0], var8[1], var8[2], var8[3]);
            }
         }
      }
   }

   public void handleVal(MatrixStack matrixStack, Text text, float x, float y, float size, float thickness) {
      this.handlePrint(matrixStack, text, x, y, size, thickness, 0.5F, 0.0F, -1, 0.0F);
   }

   public void handleRgb(MatrixStack matrixStack, String text, float x, float y, float size, Color color, float thickness) {
      this.handlePrint(matrixStack, text, x, y, size, thickness, color.getRGB(), -1, -1.0F, 0.5F, 0.0F, -1, 0.0F);
   }

   public void handleRgb(MatrixStack matrixStack, String text, float x, float y, float size, Color colorFirst, Color colorSecond, float offset, float thickness) {
      this.handlePrint(matrixStack, text, x, y, size, thickness, colorFirst.getRGB(), colorSecond.getRGB(), offset, 0.5F, 0.0F, -1, 0.0F);
   }

   public void handleVal(MatrixStack matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset) {
      this.handleRgb(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0F);
   }

   public void handleVal(MatrixStack matrixStack, Text text, float x, float y, float size) {
      this.handleVal(matrixStack, text, x, y, size, 0.0F);
   }

   public void handleColor(MatrixStack matrixStack, String text, float x, float y, float size, Color color) {
      this.handleRgb(matrixStack, text, x, y, size, color, 0.0F);
   }

   public void handleVal(MatrixStack matrixStack, String text, float x, float y, float size, int color, float thickness) {
      this.handlePrint(matrixStack, text, x, y, size, thickness, color, -1, -1.0F, 0.5F, 0.0F, -1, 0.0F);
   }

   public void handleVal(MatrixStack matrixStack, String text, float x, float y, float size, int color) {
      this.handleVal(matrixStack, text, x, y, size, color, 0.0F);
   }

   public void handleVal(MatrixStack matrixStack, String text, float x, float y, float size, Color color, float thickness) {
      this.handleRgb(matrixStack, text, x - this.computeBits(text, size, thickness) / 2.0F, y, size, color, thickness);
   }

   public void handleColor2(MatrixStack matrixStack, String text, float x, float y, float size, Color color) {
      this.handleVal(matrixStack, text, x, y, size, color, 0.0F);
   }

   public void handleVal2(MatrixStack matrixStack, String text, float x, float y, float size, int color, float thickness) {
      this.handleVal(matrixStack, text, x - this.computeBits(text, size, thickness) / 2.0F, y, size, color, thickness);
   }

   public void handleVal2(MatrixStack matrixStack, String text, float x, float y, float size, int color) {
      this.handleVal2(matrixStack, text, x, y, size, color, 0.0F);
   }

   public void handleVal(MatrixStack matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset, float thickness) {
      this.handleRgb(matrixStack, text, x - this.computeBits(text, size, thickness) / 2.0F, y, size, color, colorSecond, offset, thickness);
   }

   public void handleVal2(MatrixStack matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset) {
      this.handleVal(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0F);
   }

   public void applyCurrent(
      MatrixStack matrixStack, String text, float x, float y, float width, float size, Color color, float offset, Duration cycleDuration, Duration pauseDuration
   ) {
      if (color.getAlpha() > 0) {
         float var11 = this.computeFloat(text, size);
         if (var11 <= width) {
            this.handleColor(matrixStack, text, x, y, size, color);
         } else {
            ScissorUtil.handleEnable(matrixStack, x, y - size / 4.0F, width, size * 1.5F);
            long var12 = cycleDuration.toMillis();
            long var14 = pauseDuration.toMillis();
            long var16 = var12 + var14;
            long var18 = System.currentTimeMillis() % var16;
            float var20 = var18 < var12 ? (float)var18 / (float)var12 : 1.0F;
            float var21 = Easing.SINE_BOTH.apply(var20) * (var11 + offset);
            this.handleColor(matrixStack, text, x - var21, y, size, color);
            this.handleColor(matrixStack, text, x - var21 + var11 + offset, y, size, color);
            ScissorUtil.handleDisable(matrixStack);
         }
      }
   }

   public void handleChar(
      Matrix4f matrix,
      VertexConsumer consumer,
      String text,
      float size,
      float thickness,
      float spacing,
      float x,
      float y,
      float z,
      int color,
      int colorSecond,
      float offset
   ) {
      if (text != null && !text.isEmpty()) {
         this.loadName();
         if (this.glyphs != null) {
            int var13 = -1;
            float var14 = x;
            float var15 = offset > 1.0F ? this.computeFloat(text, size) : 0.0F;
            float var16 = offset > 1.0F ? (float)(System.currentTimeMillis() % 3000L) / 3000.0F : 0.0F;
            int var17 = text.length();
            boolean var18 = this.kernings != null && !this.kernings.isEmpty();
            float var19 = thickness + spacing;

            for (int var20 = 0; var20 < var17; var20++) {
               char var21 = text.charAt(var20);
               BottomHelper var22 = this.computeFast(var21);
               if (var22 != null) {
                  if (var18 && var13 != -1) {
                     Map var23 = this.kernings.get(var13);
                     if (var23 != null) {
                        Float var24 = (Float)var23.get(Integer.valueOf(var21));
                        if (var24 != null) {
                           x += var24 * size;
                        }
                     }
                  }

                  int var25 = color;
                  if (offset > 1.0F) {
                     var25 = ColorMath.computeSin(color, colorSecond, x - var14, var15, var16, offset);
                  }

                  x += var22.computeMax(matrix, consumer, size, x, y, z, var25) + var19;
                  var13 = var21;
               }
            }
         }
      }
   }

   public void handleColor(
      Matrix4f matrix, VertexConsumer consumer, List<BottomHelper.ColoredGlyph> glyphs, float size, float thickness, float spacing, float x, float y, float z
   ) {
      if (glyphs != null && !glyphs.isEmpty()) {
         this.loadName();
         if (this.glyphs != null) {
            int var10 = -1;
            boolean var11 = this.kernings != null && !this.kernings.isEmpty();
            float var12 = thickness + spacing;
            int var13 = glyphs.size();

            for (int var14 = 0; var14 < var13; var14++) {
               BottomHelper.ColoredGlyph var15 = (BottomHelper.ColoredGlyph)glyphs.get(var14);
               char var16 = var15.c();
               int var17 = var15.color();
               BottomHelper var18 = this.computeFast(var16);
               if (var18 != null) {
                  if (var11 && var10 != -1) {
                     Map var19 = this.kernings.get(var10);
                     if (var19 != null) {
                        Float var20 = (Float)var19.get(Integer.valueOf(var16));
                        if (var20 != null) {
                           x += var20 * size;
                        }
                     }
                  }

                  x += var18.computeMax(matrix, consumer, size, x, y, z, var17) + var12;
                  var10 = var16;
               }
            }
         }
      }
   }

   public float computeFloat(float size) {
      return size;
   }

   public float computeFloat(Text text, float size) {
      return this.computeSize(text, size, 0.0F);
   }

   public float computeSize(Text text, float size, float thickness) {
      if (text == null) {
         return 0.0F;
      } else {
         this.loadName();
         if (this.glyphs == null) {
            return 0.0F;
         } else {
            List var4 = TextShaper.computeClear(text);
            if (var4 != null && !var4.isEmpty()) {
               int var5 = -1;
               float var6 = 0.0F;
               boolean var7 = this.kernings != null && !this.kernings.isEmpty();
               float var8 = 1.0F + thickness;
               int var9 = var4.size();

               for (int var10 = 0; var10 < var9; var10++) {
                  char var11 = ((BottomHelper.ColoredGlyph)var4.get(var10)).c();
                  BottomHelper var12 = this.computeFast(var11);
                  if (var12 != null) {
                     if (var7 && var5 != -1) {
                        Map var13 = this.kernings.get(var5);
                        if (var13 != null) {
                           Float var14 = (Float)var13.get(Integer.valueOf(var11));
                           if (var14 != null) {
                              var6 += var14 * size * var8;
                           }
                        }
                     }

                     var6 += var12.computeAdvance(size);
                     var5 = var11;
                  }
               }

               return var6;
            } else {
               return 0.0F;
            }
         }
      }
   }

   public float computeFloat(String text, float size) {
      return this.computeBits(text, size, 0.0F);
   }

   public float computeBits(String text, float size, float thickness) {
      if (text != null && !text.isEmpty()) {
         this.loadName();
         if (this.glyphs == null) {
            return 0.0F;
         } else {
            int var4 = Float.floatToIntBits(size) ^ Float.floatToIntBits(thickness) << 8;
            int var5 = (text.hashCode() ^ var4) & 1023;
            if (this.widthCacheProps[var5] == var4 && text.equals(this.widthCacheKeys[var5])) {
               return this.widthCacheValues[var5];
            } else {
               int var6 = -1;
               float var7 = 0.0F;
               String var8 = Anonymous.getInstance().isEnabled() ? NameHelper.protectedString(text) : text;
               int var9 = var8.length();
               boolean var10 = this.kernings != null && !this.kernings.isEmpty();
               float var11 = 1.0F + thickness;

               for (int var12 = 0; var12 < var9; var12++) {
                  char var13 = var8.charAt(var12);
                  BottomHelper var14 = this.computeFast(var13);
                  if (var14 != null) {
                     if (var10 && var6 != -1) {
                        Map var15 = this.kernings.get(var6);
                        if (var15 != null) {
                           Float var16 = (Float)var15.get(Integer.valueOf(var13));
                           if (var16 != null) {
                              var7 += var16 * size * var11;
                           }
                        }
                     }

                     var7 += var14.computeAdvance(size) * var11;
                     var6 = var13;
                  }
               }

               this.widthCacheKeys[var5] = text;
               this.widthCacheProps[var5] = var4;
               this.widthCacheValues[var5] = var7;
               return var7;
            }
         }
      } else {
         return 0.0F;
      }
   }

   public static FontLoader initInit() {
      return new FontLoader();
   }

   public AtlasDataHelper.AtlasData getAtlas() {
      this.loadName();
      return this.atlas;
   }

   public AtlasDataHelper.MetricsData getMetricsData() {
      this.loadName();
      return this.metrics;
   }
}
