package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.module.render.TargetESP;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Render3DEvent2 extends TargetRenderer {
   public float floatVal = 0.0F;
   public float float2 = 0.0F;
   public static final int intVal = 32;
   public final double[] doubles = new double[32];
   public final float[] floats = new float[32];
   public final float[] floats2 = new float[32];
   public final double[] doubles2 = new double[32];
   public final double[] doubles3 = new double[32];
   public int int2 = 0;
   public static final int int3 = 48;
   public static final float[] floats3 = new float[48];
   public static final float[] floats4 = new float[48];

   @Override
   public void runOrbit() {
      this.runMin();
      this.float2 = this.floatVal;
      this.floatVal = this.floatVal + TargetESP.getInstance().getRingSpeed().getValue() * 5.5F;
      if (class_1297 != null) {
         TargetESP var1 = TargetESP.getInstance();
         float var2 = (float)animatedValue2.getValue();
         float var3 = (class_1297.getWidth() * 0.86F + 0.18F) * var1.getRingSize().getValue() * var2;
         float var4 = this.computeRadians(this.floatVal);
         double var5 = class_1297.getY() + 0.03;
         double var7 = class_1297.getY() + (double)class_1297.getHeight() - 0.03;
         double var9 = var5 + (var7 - var5) * (double)var4;
         int var11 = Math.min(32, Math.max(4, var1.getTrailLength().getValue().intValue()));
         int var12 = Math.min(this.int2, var11 - 1);

         for (int var13 = var12; var13 > 0; var13--) {
            this.doubles[var13] = this.doubles[var13 - 1];
            this.floats[var13] = this.floats[var13 - 1];
            this.floats2[var13] = this.floats2[var13 - 1];
            this.doubles2[var13] = this.doubles2[var13 - 1];
            this.doubles3[var13] = this.doubles3[var13 - 1];
         }

         this.doubles[0] = var9;
         this.floats[0] = var3;
         this.floats2[0] = this.floatVal;
         this.doubles2[0] = class_1297.getX();
         this.doubles3[0] = class_1297.getZ();
         if (this.int2 < var11) {
            this.int2++;
         }
      } else {
         this.int2 = 0;
      }
   }

   @Override
   public void onRender3D(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (this.getValue()) {
         long var2 = System.currentTimeMillis();
         TargetESP var4 = TargetESP.getInstance();
         MatrixStack var5 = event.getMatrixStack();
         Camera var6 = mc.gameRenderer.getCamera();
         Vec3d var7 = var6.getPos();
         double var8 = class_1297 != null
            ? EntityHelper.computeDouble(class_1297.prevX, class_1297.getX(), (double)event.getPartialTicks())
            : getMbacy();
         double var10 = class_1297 != null
            ? EntityHelper.computeDouble(class_1297.prevZ, class_1297.getZ(), (double)event.getPartialTicks())
            : getDouble2();
         double var12 = class_1297 != null
            ? EntityHelper.computeDouble(class_1297.prevY, class_1297.getY(), (double)event.getPartialTicks())
            : getDouble();
         float var14 = (float)animatedValue2.getValue();
         float var15 = 0.72F + 0.28F * var14;
         float var16 = (TargetRenderer.getFloat4() * 0.86F + 0.18F) * var4.getRingSize().getValue() * var15;
         float var17 = EntityHelper.computeFloat(this.float2, this.floatVal, event.getPartialTicks());
         float var18 = (float)animatedValue.getValue();
         float var19 = this.computeRadians(var17);
         double var20 = var12 + (double)(TargetRenderer.getFloat3() * var19);
         double var22 = var8 - var7.x;
         double var24 = var20 - var7.y;
         double var26 = var10 - var7.z;
         var5.push();
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         if (var4.getGhostTrail().getValue() && this.int2 > 1) {
            this.handlePow(var5, var7, var18 * var14, var2);
         }

         this.handleLine(var5, var22, var24, var26, var16, var17, var18 * var14, var2);
         RenderSystem.depthMask(true);
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         var5.pop();
      }
   }

   public float computeRadians(float value) {
      return(floatVal)((Math.sin(Math.toRadians((double)value)) + 1.0) * 0.5);
   }

   public void handlePow(MatrixStack ms, Vec3d cameraPos, float alpha, long now) {
      RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      Matrix4f var6 = ms.peek().getPositionMatrix();
      BufferBuilder var7 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      int var8 = Math.min(this.int2 - 1, 8);

      for (int var9 = 0; var9 < var8; var9++) {
         float var10 = (float)var9 / (float)var8;
         float var11 = (float)(var9 + 1) / (float)var8;
         float var12 = (float)Math.pow((double)(1.0F - var10), 1.3F) * 0.7F;
         float var13 = (float)Math.pow((double)(1.0F - var11), 1.3F) * 0.7F;
         double var14 = this.doubles2[var9] - cameraPos.x;
         double var16 = this.doubles[var9] - cameraPos.y;
         double var18 = this.doubles3[var9] - cameraPos.z;
         float var20 = this.floats[var9];
         float var21 = this.floats2[var9];
         double var22 = this.doubles2[var9 + 1] - cameraPos.x;
         double var24 = this.doubles[var9 + 1] - cameraPos.y;
         double var26 = this.doubles3[var9 + 1] - cameraPos.z;
         float var28 = this.floats[var9 + 1];
         float var29 = this.floats2[var9 + 1];

         for (int var30 = 0; var30 < 48; var30++) {
            int var31 = (var30 + 1) % 48;
            float var32 = floats3[var30];
            float var33 = floats4[var30];
            float var34 = floats3[var31];
            float var35 = floats4[var31];
            int var36 = this.computeMax(ThemeColor.computeInt((int)((float)var30 * 7.5F + var21), 255, now), alpha * var12);
            int var37 = this.computeMax(ThemeColor.computeInt((int)((float)var31 * 7.5F + var21), 255, now), alpha * var12);
            int var38 = this.computeMax(ThemeColor.computeInt((int)((float)var30 * 7.5F + var29), 255, now), alpha * var13);
            int var39 = this.computeMax(ThemeColor.computeInt((int)((float)var31 * 7.5F + var29), 255, now), alpha * var13);
            float var40 = (float)(var14 + (double)(var32 * var20));
            float var41 = (float)var16;
            float var42 = (float)(var18 + (double)(var33 * var20));
            float var43 = (float)(var14 + (double)(var34 * var20));
            float var44 = (float)var16;
            float var45 = (float)(var18 + (double)(var35 * var20));
            float var46 = (float)(var22 + (double)(var34 * var28));
            float var47 = (float)var24;
            float var48 = (float)(var26 + (double)(var35 * var28));
            float var49 = (float)(var22 + (double)(var32 * var28));
            float var50 = (float)var24;
            float var51 = (float)(var26 + (double)(var33 * var28));
            var7.vertex(var6, var40, var41, var42).color(var36);
            var7.vertex(var6, var43, var44, var45).color(var37);
            var7.vertex(var6, var46, var47, var48).color(var39);
            var7.vertex(var6, var40, var41, var42).color(var36);
            var7.vertex(var6, var46, var47, var48).color(var39);
            var7.vertex(var6, var49, var50, var51).color(var38);
         }
      }

      BufferRenderer.drawWithGlobalProgram(var7.end());
   }

   public void handleLine(MatrixStack ms, double x, double y, double z, float radius, float moving, float alpha, long now) {
      RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      Matrix4f var13 = ms.peek().getPositionMatrix();
      BufferBuilder var14 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      float var15 = radius - 0.16F;
      float var16 = radius + 0.16F;

      for (int var17 = 0; var17 < 48; var17++) {
         int var18 = (var17 + 1) % 48;
         float var19 = floats3[var17];
         float var20 = floats4[var17];
         float var21 = floats3[var18];
         float var22 = floats4[var18];
         int var23 = this.computeMax(ThemeColor.computeInt((int)((float)var17 * 7.5F + moving), 255, now), alpha * 0.02F);
         int var24 = this.computeMax(ThemeColor.computeInt((int)((float)var17 * 7.5F + moving), 255, now), alpha * 0.32F);
         int var25 = this.computeMax(ThemeColor.computeInt((int)((float)var18 * 7.5F + moving), 255, now), alpha * 0.02F);
         int var26 = this.computeMax(ThemeColor.computeInt((int)((float)var18 * 7.5F + moving), 255, now), alpha * 0.32F);
         float var27 = (float)(x + (double)(var19 * var15));
         float var28 = (float)(z + (double)(var20 * var15));
         float var29 = (float)(x + (double)(var19 * radius));
         float var30 = (float)(z + (double)(var20 * radius));
         float var31 = (float)(x + (double)(var19 * var16));
         float var32 = (float)(z + (double)(var20 * var16));
         float var33 = (float)(x + (double)(var21 * var15));
         float var34 = (float)(z + (double)(var22 * var15));
         float var35 = (float)(x + (double)(var21 * radius));
         float var36 = (float)(z + (double)(var22 * radius));
         float var37 = (float)(x + (double)(var21 * var16));
         float var38 = (float)(z + (double)(var22 * var16));
         var14.vertex(var13, var27, (float)y, var28).color(var23);
         var14.vertex(var13, var33, (float)y, var34).color(var25);
         var14.vertex(var13, var35, (float)y, var36).color(var26);
         var14.vertex(var13, var27, (float)y, var28).color(var23);
         var14.vertex(var13, var35, (float)y, var36).color(var26);
         var14.vertex(var13, var29, (float)y, var30).color(var24);
         var14.vertex(var13, var29, (float)y, var30).color(var24);
         var14.vertex(var13, var35, (float)y, var36).color(var26);
         var14.vertex(var13, var37, (float)y, var38).color(var25);
         var14.vertex(var13, var29, (float)y, var30).color(var24);
         var14.vertex(var13, var37, (float)y, var38).color(var25);
         var14.vertex(var13, var31, (float)y, var32).color(var23);
      }

      BufferRenderer.drawWithGlobalProgram(var14.end());
      BufferBuilder var42 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      float var43 = radius - 0.055F;
      float var44 = radius + 0.055F;

      for (int var45 = 0; var45 < 48; var45++) {
         int var47 = (var45 + 1) % 48;
         float var49 = floats3[var45];
         float var51 = floats4[var45];
         float var53 = floats3[var47];
         float var54 = floats4[var47];
         int var55 = this.computeMax(ThemeColor.computeInt((int)((float)var45 * 7.5F + moving), 255, now), alpha * 0.12F);
         int var56 = this.computeMax(ThemeColor.computeInt((int)((float)var45 * 7.5F + moving), 255, now), alpha * 0.92F);
         int var57 = this.computeMax(ThemeColor.computeInt((int)((float)var47 * 7.5F + moving), 255, now), alpha * 0.12F);
         int var58 = this.computeMax(ThemeColor.computeInt((int)((float)var47 * 7.5F + moving), 255, now), alpha * 0.92F);
         float var59 = (float)(x + (double)(var49 * var43));
         float var60 = (float)(z + (double)(var51 * var43));
         float var61 = (float)(x + (double)(var49 * radius));
         float var62 = (float)(z + (double)(var51 * radius));
         float var63 = (float)(x + (double)(var49 * var44));
         float var64 = (float)(z + (double)(var51 * var44));
         float var65 = (float)(x + (double)(var53 * var43));
         float var66 = (float)(z + (double)(var54 * var43));
         float var67 = (float)(x + (double)(var53 * radius));
         float var39 = (float)(z + (double)(var54 * radius));
         float var40 = (float)(x + (double)(var53 * var44));
         float var41 = (float)(z + (double)(var54 * var44));
         var42.vertex(var13, var59, (float)y, var60).color(var55);
         var42.vertex(var13, var65, (float)y, var66).color(var57);
         var42.vertex(var13, var67, (float)y, var39).color(var58);
         var42.vertex(var13, var59, (float)y, var60).color(var55);
         var42.vertex(var13, var67, (float)y, var39).color(var58);
         var42.vertex(var13, var61, (float)y, var62).color(var56);
         var42.vertex(var13, var61, (float)y, var62).color(var56);
         var42.vertex(var13, var67, (float)y, var39).color(var58);
         var42.vertex(var13, var40, (float)y, var41).color(var57);
         var42.vertex(var13, var61, (float)y, var62).color(var56);
         var42.vertex(var13, var40, (float)y, var41).color(var57);
         var42.vertex(var13, var63, (float)y, var64).color(var55);
      }

      BufferRenderer.drawWithGlobalProgram(var42.end());
      RenderSystem.lineWidth(2.5F);
      BufferBuilder var46 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

      for (int var48 = 0; var48 <= 48; var48++) {
         int var50 = var48 % 48;
         int var52 = this.computeMax(ThemeColor.computeInt((int)((float)var50 * 7.5F + moving), 255, now), alpha * 1.0F);
         var46.vertex(var13, (float)(x + (double)(floats3[var50] * radius)), (float)y, (float)(z + (double)(floats4[var50] * radius)))
            .color(var52);
      }

      BufferRenderer.drawWithGlobalProgram(var46.end());
      RenderSystem.lineWidth(1.0F);
   }

   public int computeMax(int baseColorRGB, float alpha) {
      int var3 = Math.max(0, Math.min(255, (int)(255.0F * alpha)));
      int var4 = var3 << 24 | baseColorRGB & 16777215;
      if (this.float2 > 0.0F) {
         int var5 = var3 << 24 | 0xFF0000 | 8192 | 32;
         return this.computeMax(var4, var5, this.float2);
      } else {
         return var4;
      }
   }

   public int computeMax(int c1, int c2, float t) {
      t = Math.max(0.0F, Math.min(1.0F, t));
      float var4 = t * t * (3.0F - 2.0F * t);
      int var5 = c1 >> 24 & 0xFF;
      int var6 = c1 >> 16 & 0xFF;
      int var7 = c1 >> 8 & 0xFF;
      int var8 = c1 & 0xFF;
      int var9 = c2 >> 24 & 0xFF;
      int var10 = c2 >> 16 & 0xFF;
      int var11 = c2 >> 8 & 0xFF;
      int var12 = c2 & 0xFF;
      int var13 = (int)((float)var5 + (float)(var9 - var5) * var4);
      int var14 = (int)((float)var6 + (float)(var10 - var6) * var4);
      int var15 = (int)((float)var7 + (float)(var11 - var7) * var4);
      int var16 = (int)((float)var8 + (float)(var12 - var8) * var4);
      return var13 << 24 | var14 << 16 | var15 << 8 | var16;
   }

   @Override
   public void runEnabled() {
      super.runEnabled();
      if (class_1297 == null) {
         this.int2 = 0;
         this.float2 = 0.0F;
         this.intVal = 0;
         this.float2 = this.floatVal;
      }
   }

   static {
      for (int var0 = 0; var0 < 48; var0++) {
         double var1 = Math.toRadians((double)((float)var0 / 48.0F * 360.0F));
         floats3[var0] = (float)Math.cos(var1);
         floats4[var0] = (float)Math.sin(var1);
      }
   }
}
