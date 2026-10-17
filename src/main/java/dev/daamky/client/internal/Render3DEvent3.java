package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.module.render.TargetESP;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Render3DEvent3 extends TargetRenderer {
   public final List<Render3DEvent3.TrailPoint> list = new ArrayList<>();
   public float mbouenw = 0.0F;
   public float floatVal = 0.0F;
   public int intVal = -1;
   public int int2 = 3;

   public TargetESP getInstance() {
      return TargetESP.getInstance();
   }

   @Override
   public void runOrbit() {
      if (this.getValue()) {
         int var1 = this.getInstance().getGhostCount().getValue().intValue();
         if (class_1297 == null || class_1297.getId() != this.intVal || var1 != this.int2) {
            this.list.clear();
            if (class_1297 != null) {
               this.intVal = class_1297.getId();
            }

            this.int2 = var1;
         }

         this.floatVal = this.mbouenw;
         this.mbouenw = this.mbouenw + this.getInstance().getGhostSpeed().getValue();
      }
   }

   @Override
   public void onRender3D(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (this.getValue()) {
         MatrixStack var2 = event.getMatrixStack();
         Camera var3 = mc.gameRenderer.getCamera();
         Vec3d var4 = var3.getPos();
         double var5 = var4.x;
         double var7 = var4.y;
         double var9 = var4.z;
         float var11 = (float)animatedValue.getValue();
         if (!(var11 <= 0.01F)) {
            float var12 = this.getInstance().getSize().getValue();
            float var13 = 0.38F * var12 * var11;
            float var14 = 1.15F * EntityHelper.computeFloat((double)this.floatVal, animatedValue2.getValue());
            float var15 = (float)(0.4 * (double)EntityHelper.computeFloat((double)this.floatVal, animatedValue2.getValue()));
            int var16 = Math.min(this.getInstance().getGhostCount().getValue().intValue(), 3);
            boolean var17 = this.getInstance().getModeSetting2().is("2");
            double var18 = (double)TargetRenderer.getFloat3();
            float var20 = this.mbouenw - this.floatVal;
            if (var20 < -180.0F) {
               var20 += 360.0F;
            }

            if (var20 > 180.0F) {
               var20 -= 360.0F;
            }

            float var21 = this.floatVal + var20 * event.getPartialTicks();
            long var22 = System.currentTimeMillis();
            long var24 = 450L;
            this.list.removeIf(Render3DEvent3::lambda$onRender3D$0);
            double[] var26 = new double[var16];
            double[] var27 = new double[var16];
            double[] var28 = new double[var16];
            float[] var29 = new float[var16];
            double var30 = TargetRenderer.getMbacy();
            double var32 = TargetRenderer.getDouble();
            double var34 = TargetRenderer.getDouble2();

            for (int var36 = 0; var36 < var16; var36++) {
               float var37 = var21 + (float)var36 * (360.0F / (float)var16);
               double var38 = (double)(TargetRenderer.getFloat4() * var14);
               double var40;
               double var42;
               double var44;
               if (var17) {
                  float var46 = var36 % 2 == 0 ? 0.0F : (float) Math.PI;
                  float var47 = (float)(Math.sin(Math.toRadians((double)(var37 * 1.2F)) + (double)var46) * 0.5 + 0.5);
                  var40 = Math.cos(Math.toRadians((double)var37)) * var38;
                  var42 = (double)var47 * var18;
                  var44 = Math.sin(Math.toRadians((double)var37)) * var38;
               } else {
                  var40 = Math.cos(Math.toRadians((double)var37)) * var38;
                  var42 = Math.sin(Math.toRadians((double)var37)) * (double)TargetRenderer.getFloat3() * (double)var15 / 2.0
                     + (double)(TargetRenderer.getFloat3() / 2.0F);
                  var44 = Math.sin(Math.toRadians((double)var37)) * var38;
               }

               var26[var36] = var30 + var40;
               var27[var36] = var32 + var42;
               var28[var36] = var34 + var44;
               var29[var36] = var37;
               this.list.add(new Render3DEvent3.TrailPoint(var26[var36], var27[var36], var28[var36], var37, var22, var36));
            }

            float var59 = (float)Math.toRadians((double)var3.getYaw());
            float var60 = (float)Math.toRadians((double)var3.getPitch());
            float var61 = (float)Math.cos((double)var59);
            float var39 = (float)Math.sin((double)var59);
            float var62 = (float)Math.cos((double)var60);
            float var41 = (float)Math.sin((double)var60);
            float var63 = var61;
            float var43 = 0.0F;
            float var64 = var39;
            float var45 = -var41 * var39;
            float var65 = var62;
            float var66 = var41 * var61;
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
            Matrix4f var48 = var2.peek().getPositionMatrix();
            BufferBuilder var49 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float var50 = this.getFloat2();

            for (int var51 = 0; var51 < var16; var51++) {
               ArrayList var52 = new ArrayList();

               for (Render3DEvent3.TrailPoint var54 : this.list) {
                  if (var54.cometIndex == var51) {
                     var52.add(var54);
                  }
               }

               int var68 = var52.size();

               for (int var69 = 0; var69 < var68; var69++) {
                  Render3DEvent3.TrailPoint var55 = (Render3DEvent3.TrailPoint)var52.get(var69);
                  float var56 = (float)var69 / (float)Math.max(1, var68 - 1);
                  float var57 = var13 * (0.4F + 0.75F * var56);
                  float var58 = (float)Math.pow((double)var56, 0.75) * var11 * 0.9F;
                  this.handleCos(
                     var49, var48, var55.x - var5, var55.y - var7, var55.z - var9, var63, var43, var64, var45, var65, var66, var55.angle, var57, var58, var50
                  );
               }

               this.handleCos(
                  var49,
                  var48,
                  var26[var51] - var5,
                  var27[var51] - var7,
                  var28[var51] - var9,
                  var63,
                  var43,
                  var64,
                  var45,
                  var65,
                  var66,
                  var29[var51],
                  var13 * 2.2F,
                  var11 * 0.45F,
                  var50
               );
               this.handleCos(
                  var49,
                  var48,
                  var26[var51] - var5,
                  var27[var51] - var7,
                  var28[var51] - var9,
                  var63,
                  var43,
                  var64,
                  var45,
                  var65,
                  var66,
                  var29[var51],
                  var13 * 1.4F,
                  var11 * 0.85F,
                  var50
               );
               this.handleCos(
                  var49,
                  var48,
                  var26[var51] - var5,
                  var27[var51] - var7,
                  var28[var51] - var9,
                  var63,
                  var43,
                  var64,
                  var45,
                  var65,
                  var66,
                  var29[var51],
                  var13 * 0.75F,
                  var11 * 1.0F,
                  var50
               );
            }

            BuiltBuffer var67 = var49.end();
            if (var67 != null) {
               BufferRenderer.drawWithGlobalProgram(var67);
            }

            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
         }
      }
   }

   public void handleCos(
      BufferBuilder buffer,
      Matrix4f mat,
      double cx,
      double cy,
      double cz,
      float rightX,
      float rightY,
      float rightZ,
      float upX,
      float upY,
      float upZ,
      float angle,
      float size,
      float alphaMultiplier,
      float impact
   ) {
      int var19 = ThemeColor.computeCurrent((int)angle, (int)(255.0F * alphaMultiplier));
      int var20 = this.computeMin(var19, impact);
      float var21 = (float)Math.toRadians((double)angle);
      float var22 = (float)Math.cos((double)var21);
      float var23 = (float)Math.sin((double)var21);
      float var24 = (rightX * var22 - upX * var23) * size;
      float var25 = (rightY * var22 - upY * var23) * size;
      float var26 = (rightZ * var22 - upZ * var23) * size;
      float var27 = (rightX * var23 + upX * var22) * size;
      float var28 = (rightY * var23 + upY * var22) * size;
      float var29 = (rightZ * var23 + upZ * var22) * size;
      float var30 = (float)(cx - (double)var24 + (double)var27);
      float var31 = (float)(cy - (double)var25 + (double)var28);
      float var32 = (float)(cz - (double)var26 + (double)var29);
      float var33 = (float)(cx + (double)var24 + (double)var27);
      float var34 = (float)(cy + (double)var25 + (double)var28);
      float var35 = (float)(cz + (double)var26 + (double)var29);
      float var36 = (float)(cx + (double)var24 - (double)var27);
      float var37 = (float)(cy + (double)var25 - (double)var28);
      float var38 = (float)(cz + (double)var26 - (double)var29);
      float var39 = (float)(cx - (double)var24 - (double)var27);
      float var40 = (float)(cy - (double)var25 - (double)var28);
      float var41 = (float)(cz - (double)var26 - (double)var29);
      buffer.vertex(mat, var30, var31, var32).texture(0.0F, 1.0F).color(var20);
      buffer.vertex(mat, var33, var34, var35).texture(1.0F, 1.0F).color(var20);
      buffer.vertex(mat, var36, var37, var38).texture(1.0F, 0.0F).color(var20);
      buffer.vertex(mat, var39, var40, var41).texture(0.0F, 0.0F).color(var20);
   }

   public static boolean lambda$onRender3D$0(long now, long maxAgeMs, Render3DEvent3.TrailPoint p) {
      return now - p.time > maxAgeMs;
   }

   private static class TrailPoint {
      public double x;
      public double y;
      public double z;
      public float angle;
      public long time;
      public int cometIndex;

      public TrailPoint(double x, double y, double z, float angle, long time, int cometIndex) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.angle = angle;
         this.time = time;
         this.cometIndex = cometIndex;
      }
   }
}
