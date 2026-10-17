package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class BoxRenderer implements MinecraftAccess {
   public final List<BoxRenderer.FilledBox> list = new ArrayList<>();
   public final List<BoxRenderer.OutlinedBox> mmaba = new ArrayList<>();
   public final List<BoxRenderer.StripedBox> list2 = new ArrayList<>();
   public int intVal = 0;
   public int int2 = 0;
   public int int3 = 0;

   public void initBlend(MatrixStack matrixStack) {
      if (this.intVal != 0 || this.int2 != 0 || this.int3 != 0) {
         Camera var2 = mc.gameRenderer.getCamera();
         Vec3d var3 = var2.getPos();
         Matrix4f var4 = matrixStack.peek().getPositionMatrix();
         Frustum var5 = new Frustum(var4, RenderSystem.getProjectionMatrix());
         var5.setPosition(var3.x, var3.y, var3.z);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         this.handleShader(var5, var4, var3);
         this.handleLine(var5, var4, var3);
         this.handleLine2(var5, var4, var3);
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
         this.intVal = 0;
         this.int2 = 0;
         this.int3 = 0;
      }
   }

   public void handleShader(Frustum frustum, Matrix4f model, Vec3d camPos) {
      if (this.intVal != 0) {
         boolean var4 = false;

         for (int var5 = 0; var5 < this.intVal; var5++) {
            if (this.list.get(var5).computeFlag(frustum)) {
               var4 = true;
               break;
            }
         }

         if (var4) {
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder var8 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            for (int var6 = 0; var6 < this.intVal; var6++) {
               BoxRenderer.FilledBox var7 = this.list.get(var6);
               if (var7.computeFlag(frustum)) {
                  this.handleColor(var7, camPos, model, var8);
               }
            }

            BufferRenderer.drawWithGlobalProgram(var8.end());
         }
      }
   }

   public void handleLine(Frustum frustum, Matrix4f model, Vec3d camPos) {
      if (this.int2 != 0) {
         boolean var4 = false;

         for (int var5 = 0; var5 < this.int2; var5++) {
            if (this.mmaba.get(var5).computeFlag(frustum)) {
               var4 = true;
               break;
            }
         }

         if (var4) {
            RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
            RenderSystem.lineWidth(1.5F);
            BufferBuilder var8 = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);

            for (int var6 = 0; var6 < this.int2; var6++) {
               BoxRenderer.OutlinedBox var7 = this.mmaba.get(var6);
               if (var7.computeFlag(frustum)) {
                  this.handleColor(var7, camPos, model, var8);
               }
            }

            BufferRenderer.drawWithGlobalProgram(var8.end());
         }
      }
   }

   public void handleLine2(Frustum frustum, Matrix4f model, Vec3d camPos) {
      if (this.int3 != 0) {
         boolean var4 = false;

         for (int var5 = 0; var5 < this.int3; var5++) {
            if (this.list2.get(var5).computeFlag(frustum)) {
               var4 = true;
               break;
            }
         }

         if (var4) {
            RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
            BufferBuilder var8 = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);

            for (int var6 = 0; var6 < this.int3; var6++) {
               BoxRenderer.StripedBox var7 = this.list2.get(var6);
               if (var7.computeFlag(frustum)) {
                  RenderSystem.lineWidth(var7.lineWidth);
                  this.handleColor(var7, camPos, model, var8);
               }
            }

            BufferRenderer.drawWithGlobalProgram(var8.end());
         }
      }
   }

   public void handleColor(BoxRenderer.FilledBox box, Vec3d camPos, Matrix4f model, BufferBuilder buffer) {
      float var5 = (float)((double)box.x1 - camPos.x);
      float var6 = (float)((double)box.y1 - camPos.y);
      float var7 = (float)((double)box.z1 - camPos.z);
      float var8 = (float)((double)box.x2 - camPos.x);
      float var9 = (float)((double)box.y2 - camPos.y);
      float var10 = (float)((double)box.z2 - camPos.z);
      int var11 = box.color.getRed();
      int var12 = box.color.getGreen();
      int var13 = box.color.getBlue();
      int var14 = box.color.getAlpha();
      this.handleVal(buffer, model, var5, var6, var7, var8, var6, var7, var8, var9, var7, var5, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var10, var8, var6, var10, var8, var9, var10, var5, var9, var10, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var7, var5, var6, var10, var5, var9, var10, var5, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var6, var7, var8, var6, var10, var8, var9, var10, var8, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var7, var5, var9, var7, var8, var9, var7, var8, var6, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var10, var5, var9, var10, var8, var9, var10, var8, var6, var10, var11, var12, var13, var14);
   }

   public void handleColor(BoxRenderer.OutlinedBox box, Vec3d camPos, Matrix4f model, BufferBuilder buffer) {
      float var5 = (float)((double)box.x1 - camPos.x);
      float var6 = (float)((double)box.y1 - camPos.y);
      float var7 = (float)((double)box.z1 - camPos.z);
      float var8 = (float)((double)box.x2 - camPos.x);
      float var9 = (float)((double)box.y2 - camPos.y);
      float var10 = (float)((double)box.z2 - camPos.z);
      int var11 = box.color.getRed();
      int var12 = box.color.getGreen();
      int var13 = box.color.getBlue();
      int var14 = box.color.getAlpha();
      this.handleVal(buffer, model, var5, var6, var7, var8, var6, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var6, var7, var8, var6, var10, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var6, var10, var5, var6, var10, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var10, var5, var6, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var9, var7, var8, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var9, var7, var8, var9, var10, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var9, var10, var5, var9, var10, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var9, var10, var5, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var7, var5, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var6, var7, var8, var9, var7, var11, var12, var13, var14);
      this.handleVal(buffer, model, var5, var6, var10, var5, var9, var10, var11, var12, var13, var14);
      this.handleVal(buffer, model, var8, var6, var10, var8, var9, var10, var11, var12, var13, var14);
   }

   public void handleColor(BoxRenderer.StripedBox box, Vec3d camPos, Matrix4f model, BufferBuilder buffer) {
      float var5 = (float)((double)box.x1 - camPos.x);
      float var6 = (float)((double)box.y1 - camPos.y);
      float var7 = (float)((double)box.z1 - camPos.z);
      float var8 = (float)((double)box.x2 - camPos.x);
      float var9 = (float)((double)box.y2 - camPos.y);
      float var10 = (float)((double)box.z2 - camPos.z);
      float var11 = box.gapDistance;
      this.handleMin(buffer, model, var5, var6, var7, var8, var6, var7, var11, box.color);
      this.handleMin(buffer, model, var8, var6, var7, var8, var6, var10, var11, box.color);
      this.handleMin(buffer, model, var8, var6, var10, var5, var6, var10, var11, box.color);
      this.handleMin(buffer, model, var5, var6, var10, var5, var6, var7, var11, box.color);
      this.handleMin(buffer, model, var5, var9, var7, var8, var9, var7, var11, box.color);
      this.handleMin(buffer, model, var8, var9, var7, var8, var9, var10, var11, box.color);
      this.handleMin(buffer, model, var8, var9, var10, var5, var9, var10, var11, box.color);
      this.handleMin(buffer, model, var5, var9, var10, var5, var9, var7, var11, box.color);
      this.handleMin(buffer, model, var5, var6, var7, var5, var9, var7, var11, box.color);
      this.handleMin(buffer, model, var8, var6, var7, var8, var9, var7, var11, box.color);
      this.handleMin(buffer, model, var5, var6, var10, var5, var9, var10, var11, box.color);
      this.handleMin(buffer, model, var8, var6, var10, var8, var9, var10, var11, box.color);
   }

   public void handleMin(BufferBuilder buffer, Matrix4f model, float x1, float y1, float z1, float x2, float y2, float z2, float gap, Color color) {
      float var11 = x2 - x1;
      float var12 = y2 - y1;
      float var13 = z2 - z1;
      float var14 = MathHelper.sqrt(var11 * var11 + var12 * var12 + var13 * var13);
      if (var14 != 0.0F) {
         int var15 = color.getRed();
         int var16 = color.getGreen();
         int var17 = color.getBlue();
         int var18 = color.getAlpha();
         float var19 = 0.0F;

         while (var19 < 1.0F) {
            float var20 = Math.min(var19 + gap / var14, 1.0F);
            this.handleVal(
               buffer,
               model,
               x1 + var11 * var19,
               y1 + var12 * var19,
               z1 + var13 * var19,
               x1 + var11 * var20,
               y1 + var12 * var20,
               z1 + var13 * var20,
               var15,
               var16,
               var17,
               var18
            );
            var19 = Math.min(var20 + gap / var14, 1.0F);
         }
      }
   }

   public void handleVal(BufferBuilder buffer, Matrix4f model, float x1, float y1, float z1, float x2, float y2, float z2, int r, int g, int b, int a) {
      buffer.vertex(model, x1, y1, z1).color(r, g, b, a).normal(1.0F, 0.0F, 0.0F);
      buffer.vertex(model, x2, y2, z2).color(r, g, b, a).normal(1.0F, 0.0F, 0.0F);
   }

   public void handleVal(
      BufferBuilder buffer,
      Matrix4f model,
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
      float z4,
      int r,
      int g,
      int b,
      int a
   ) {
      buffer.vertex(model, x1, y1, z1).color(r, g, b, a);
      buffer.vertex(model, x2, y2, z2).color(r, g, b, a);
      buffer.vertex(model, x3, y3, z3).color(r, g, b, a);
      buffer.vertex(model, x4, y4, z4).color(r, g, b, a);
   }

   public void handleFloat(
      float x1, float y1, float z1, float x2, float y2, float z2, float lineWidth, Color color, BoxRenderer.Render renderMode, float gapDistance
   ) {
      switch (renderMode) {
         case FILL:
            BoxRenderer.FilledBox var13;
            if (this.intVal < this.list.size()) {
               var13 = this.list.get(this.intVal);
            } else {
               var13 = new BoxRenderer.FilledBox();
               this.list.add(var13);
            }

            var13.initInit(x1, y1, z1, x2, y2, z2, color);
            this.intVal++;
            break;
         case OUTLINE:
            BoxRenderer.OutlinedBox var12;
            if (this.int2 < this.mmaba.size()) {
               var12 = this.mmaba.get(this.int2);
            } else {
               var12 = new BoxRenderer.OutlinedBox();
               this.mmaba.add(var12);
            }

            var12.initInit(x1, y1, z1, x2, y2, z2, lineWidth, color);
            this.int2++;
            break;
         case STRIPED:
            BoxRenderer.StripedBox var11;
            if (this.int3 < this.list2.size()) {
               var11 = this.list2.get(this.int3);
            } else {
               var11 = new BoxRenderer.StripedBox();
               this.list2.add(var11);
            }

            var11.initInit(x1, y1, z1, x2, y2, z2, lineWidth, color, gapDistance);
            this.int3++;
      }
   }

   public static class FilledBox {
      public float x1;
      public float y1;
      public float z1;
      public float x2;
      public float y2;
      public float z2;
      public Color color;
      public Box class_238 = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

      public void initInit(float x1, float y1, float z1, float x2, float y2, float z2, Color color) {
         this.x1 = x1;
         this.y1 = y1;
         this.z1 = z1;
         this.x2 = x2;
         this.y2 = y2;
         this.z2 = z2;
         this.color = color;
         this.class_238 = new Box((double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
      }

      public boolean computeFlag(Frustum frustum) {
         return frustum.isVisible(this.class_238);
      }
   }

   public static class OutlinedBox {
      public float x1;
      public float y1;
      public float z1;
      public float x2;
      public float y2;
      public float z2;
      public float lineWidth;
      public Color color;
      public Box class_238 = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

      public void initInit(float x1, float y1, float z1, float x2, float y2, float z2, float lineWidth, Color color) {
         this.x1 = x1;
         this.y1 = y1;
         this.z1 = z1;
         this.x2 = x2;
         this.y2 = y2;
         this.z2 = z2;
         this.lineWidth = lineWidth;
         this.color = color;
         this.class_238 = new Box((double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
      }

   }

   public static enum Render {
      FILL,
      OUTLINE,
      STRIPED;

      public static BoxRenderer.Render[] $values() {
         return new BoxRenderer.Render[]{FILL, OUTLINE, STRIPED};
      }
   }

   public static class StripedBox {
      public float x1;
      public float y1;
      public float z1;
      public float x2;
      public float y2;
      public float z2;
      public float lineWidth;
      public float gapDistance;
      public Color color;
      public Box myialia = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

      public void initInit(float x1, float y1, float z1, float x2, float y2, float z2, float lineWidth, Color color, float gapDistance) {
         this.x1 = x1;
         this.y1 = y1;
         this.z1 = z1;
         this.x2 = x2;
         this.y2 = y2;
         this.z2 = z2;
         this.lineWidth = lineWidth;
         this.color = color;
         this.gapDistance = gapDistance;
         this.myialia = new Box((double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
      }

   }
}
