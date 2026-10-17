package dev.daamky.client.internal;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class FrameSizeHelper {
   public final Identifier texture;
   public final int intVal;
   public final int rows;
   public final int totalFrames;
   public final int fps;
   public final int frameWidth;
   public final int frameHeight;
   public final int int2;
   public final int int3;

   public FrameSizeHelper(Identifier texture, int intVal, int rows, int totalFrames, int fps, int frameWidth, int frameHeight) {
      this.texture = texture;
      this.intVal = intx;
      this.rows = rows;
      this.totalFrames = totalFrames;
      this.fps = fps;
      this.frameWidth = frameWidth;
      this.frameHeight = frameHeight;
      this.int2 = intx * frameWidth;
      this.int3 = rows * frameHeight;
   }

   public FrameSizeHelper(Identifier texture, int intVal, int rows, int totalFrames, int fps, int frameSize) {
      this(texture, intx, rows, totalFrames, fps, frameSize, frameSize);
   }

   @Deprecated
   public FrameSizeHelper(Identifier texture, int intVal, int rows, int totalFrames, int fps) {
      this(texture, intx, rows, totalFrames, fps, 256, 256);
   }

   public void handleFrame(DrawContext context, float x, float y, float width, float height, Color color) {
      long var7 = System.currentTimeMillis();
      long var9 = 1000L / (long)this.fps;
      int var11 = (int)(var7 / var9 % (long)this.totalFrames);
      int var12 = var11 % this.intVal;
      int var13 = var11 / this.intVal;
      float var14 = (float)(var12 * this.frameWidth);
      float var15 = (float)(var13 * this.frameHeight);
      context.drawTexture(
         RenderLayer::getGuiTextured,
         this.texture,
         (int)x,
         (int)y,
         var14,
         var15,
         (int)width,
         (int)height,
         this.frameWidth,
         this.frameHeight,
         this.int2,
         this.int3,
         color.getRGB()
      );
   }
}
