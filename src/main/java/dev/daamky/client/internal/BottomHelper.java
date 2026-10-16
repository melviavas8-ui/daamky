package dev.daamky.client.internal;

import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;

public final class BottomHelper {
   public final int code;
   public final float minU;
   public final float maxU;
   public final float minV;
   public final float maxV;
   public final float advance;
   public final float topPosition;
   public final float width;
   public final float height;

   public BottomHelper(AtlasDataHelper.GlyphData data, float atlasWidth, float atlasHeight) {
      this(data, atlasWidth, atlasHeight, "bottom");
   }

   public BottomHelper(AtlasDataHelper.GlyphData data, float atlasWidth, float atlasHeight, String yOrigin) {
      this.code = data.getUnicode();
      this.advance = data.getAdvance();
      AtlasDataHelper.BoundsData var5 = data.getAtlasBounds();
      if (var5 != null) {
         this.minU = var5.getLeft() / atlasWidth;
         this.maxU = var5.getRight() / atlasWidth;
         if ("top".equalsIgnoreCase(yOrigin)) {
            this.minV = var5.getTop() / atlasHeight;
            this.maxV = var5.getBottom() / atlasHeight;
         } else {
            this.minV = 1.0F - var5.getTop() / atlasHeight;
            this.maxV = 1.0F - var5.getBottom() / atlasHeight;
         }
      } else {
         this.maxV = 0.0F;
         this.minV = 0.0F;
         this.maxU = 0.0F;
         this.minU = 0.0F;
      }

      AtlasDataHelper.BoundsData var6 = data.getPlaneBounds();
      if (var6 != null) {
         this.width = var6.getRight() - var6.getLeft();
         this.height = var6.getTop() - var6.getBottom();
         this.topPosition = var6.getTop();
      } else {
         this.topPosition = 0.0F;
         this.height = 0.0F;
         this.width = 0.0F;
      }
   }

   public float computeMax(Matrix4f matrix, VertexConsumer consumer, float size, float x, float y, float z, int color) {
      float var8 = this.width * size;
      float var9 = this.height * size;
      float var10;
      consumer.vertex(matrix, x, var10 = y - this.topPosition * size, z).texture(this.minU, this.minV).color(color);
      consumer.vertex(matrix, x, var10 + var9, z).texture(this.minU, this.maxV).color(color);
      consumer.vertex(matrix, x + var8, var10 + var9, z).texture(this.maxU, this.maxV).color(color);
      consumer.vertex(matrix, x + var8, var10, z).texture(this.maxU, this.minV).color(color);
      return this.advance * size;
   }

   public float computeAdvance(float size) {
      return this.advance * size;
   }

   public int getCode() {
      return this.code;
   }

   public static record ColoredGlyph(char c, int color) {
   }
}
