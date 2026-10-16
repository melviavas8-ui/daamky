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
      this.texture = texturex;
      this.intVal = intx;
      this.rows = rowsx;
      this.totalFrames = totalFramesx;
      this.fps = fpsx;
      this.frameWidth = frameWidthx;
      this.frameHeight = frameHeightx;
      this.intValVal2 = intx * frameWidthx;
      this.intValVal3 = rowsx * frameHeightx;
   }

   public FrameSizeHelper(Identifier texture, int intVal, int rows, int totalFrames, int fps, int frameSize) {
      this(texturex, intx, rowsx, totalFramesx, fpsx, frameSize, frameSize);
   }

   @Deprecated
   public FrameSizeHelper(Identifier texture, int intVal, int rows, int totalFrames, int fps) {
      this(texturex, intx, rowsx, totalFramesx, fpsx, 256, 256);
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
         this.intValVal2,
         this.intValVal3,
         color.getRGB()
      );
   }
}
