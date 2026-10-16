package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.module.render.TargetESP;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Render3DEvent extends TargetRenderer {
   public static final Vector3f[] vector3fList = new Vector3f[]{
      new Vector3f(-1.0F, -1.0F, -1.0F),
      new Vector3f(1.0F, -1.0F, -1.0F),
      new Vector3f(1.0F, 1.0F, -1.0F),
      new Vector3f(-1.0F, 1.0F, -1.0F),
      new Vector3f(-1.0F, -1.0F, 1.0F),
      new Vector3f(1.0F, -1.0F, 1.0F),
      new Vector3f(1.0F, 1.0F, 1.0F),
      new Vector3f(-1.0F, 1.0F, 1.0F)
   };
   public static final int[][] mexdedg = new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 0}, {4, 5}, {5, 6}, {6, 7}, {7, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   public static final Vector3f vector3f = new Vector3f();
   public static final Vector3f vector3f2 = new Vector3f(0.0F, 1.0F, 0.0F);
   public static final Vector3f vector3f3 = new Vector3f();
   public static final Matrix4f matrix4f = new Matrix4f();
   public final float[] floats = new float[18];
   public final float[] floats2 = new float[18];
   public final float[] floats3 = new float[18];
   public final int[] ints = new int[18];
   public final Quaternionf[] quaternionfList = new Quaternionf[18];
   public float floatVal = 0.0F;

   public Render3DEvent() {
      for (int var1 = 0; var1 < 18; var1++) {
         this.quaternionfList[var1] = new Quaternionf();
      }
   }

   public TargetESP getInstance() {
      return TargetESP.getInstance();
   }

   @Override
   public void runOrbit() {
      if (this.getValue()) {
         float var1 = this.getInstance().getOrbitSpeed().getValue();
         this.floatVal += var1 * 12.0F;
      }
   }

   @Override
   public void onRender3D(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (this.getValue() && mc.player != null) {
         float var2 = MathHelper.clamp((float)animatedValue.getValue(), 0.0F, 1.0F);
         if (!(var2 <= 0.01F)) {
            MatrixStack var3 = event.getMatrixStack();
            Camera var4 = mc.gameRenderer.getCamera();
            double var5 = getMbacy() - var4.getPos().x;
            double var7 = getDouble() - var4.getPos().y;
            double var9 = getDouble2() - var4.getPos().z;
            var3.push();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(770, 1, 1, 0);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            float var11 = this.getInstance().getCubeSize().getValue() / 0.12F;
            float var12 = this.getInstance().getOrbitSpeed().getValue();
            float var13 = this.floatVal + event.getPartialTicks() * var12 * 12.0F;
            float var14 = TargetRenderer.getFloat4();
            float var15 = TargetRenderer.getFloat3();
            float var16 = var14 * 1.55F * var11;
            float var17 = 1.2F - 0.5F * var2;
            float var18 = 0.09F * var11;
            boolean var19 = this.getInstance().getCubeGlow().getValue();
            if (var19) {
               this.initBlend(var3, var4, var5, var7, var9, var15, var16 * 0.5F, var13 * 0.02F, var2);
            }

            byte var20 = 18;
            int var21 = 0;
            float var22 = this.getFloat2();

            for (byte var23 = 0; var23 < 360; var21++) {
               float var24 = (float)Math.toRadians((double)((float)var23 + var13 * 0.3F));
               this.floatValVals[var21] = (float)(Math.sin((double)var24) * (double)var16 * (double)var17);
               this.floatValVals2[var21] = (float)(Math.cos((double)var24) * (double)var16 * (double)var17);
               this.floatValVals3[var21] = 0.1F + var15 * Math.abs((float)Math.sin((double)var23));
               int var25 = ThemeColor.computeCurrent((int)((float)var23 + var13), (int)(255.0F * var2));
               this.intValVals[var21] = this.computeMin(var25, var22);
               vector3f.set(-this.floatValVals[var21], var15 * 0.5F - 1.0F, -this.floatValVals2[var21]);
               if (vector3f.lengthSquared() < 1.0E-8F) {
                  vector3f.set(0.0F, 1.0F, 0.0F);
               } else {
                  vector3f.normalize();
               }

               this.quaternionfList[var21].rotationTo(vector3f2, vector3f);
               var23 += 20;
            }

            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder var35 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

            for (int var36 = 0; var36 < var20; var36++) {
               var3.push();
               var3.translate(var5 + (double)this.floatValVals[var36], var7 + (double)this.floatValVals3[var36], var9 + (double)this.floatValVals2[var36]);
               var3.multiply(this.quaternionfList[var36]);
               var3.scale(var18, var18, var18);
               handleMin(var3.peek().getPositionMatrix(), var35, this.intValVals[var36]);
               var3.pop();
            }

            BufferRenderer.drawWithGlobalProgram(var35.end());
            RenderSystem.lineWidth(2.0F);
            BufferBuilder var37 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            for (int var38 = 0; var38 < var20; var38++) {
               var3.push();
               var3.translate(var5 + (double)this.floatValVals[var38], var7 + (double)this.floatValVals3[var38], var9 + (double)this.floatValVals2[var38]);
               var3.multiply(this.quaternionfList[var38]);
               var3.scale(var18, var18, var18);
               int var26 = this.computeMin(this.intValVals[var38], var2);
               handleVal(var3.peek().getPositionMatrix(), var37, var26);
               var3.pop();
            }

            BufferRenderer.drawWithGlobalProgram(var37.end());
            RenderSystem.lineWidth(1.0F);
            if (var19) {
               RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
               BufferBuilder var39 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               float var40 = 0.55F * var11;

               for (int var27 = 0; var27 < var20; var27++) {
                  Vector3f var28 = vector3f3.set(
                     (float)(var5 + (double)this.floatValVals[var27]), (float)(var7 + (double)this.floatValVals3[var27]), (float)(var9 + (double)this.floatValVals2[var27])
                  );
                  var3.peek().getPositionMatrix().transformPosition(var28);
                  Matrix4f var29 = matrix4f.identity();
                  var29.translate(var28.x, var28.y, var28.z);
                  int var30 = this.intValVals[var27];
                  int var31 = var30 >> 16 & 0xFF;
                  int var32 = var30 >> 8 & 0xFF;
                  int var33 = var30 & 0xFF;
                  int var34 = (int)((float)(var30 >> 24 & 0xFF) * 0.65F);
                  var39.vertex(var29, -var40, -var40, 0.0F).texture(0.0F, 0.0F).color(var31, var32, var33, var34);
                  var39.vertex(var29, var40, -var40, 0.0F).texture(1.0F, 0.0F).color(var31, var32, var33, var34);
                  var39.vertex(var29, var40, var40, 0.0F).texture(1.0F, 1.0F).color(var31, var32, var33, var34);
                  var39.vertex(var29, -var40, var40, 0.0F).texture(0.0F, 1.0F).color(var31, var32, var33, var34);
               }

               BufferRenderer.drawWithGlobalProgram(var39.end());
            }

            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            var3.pop();
         }
      }
   }

   public static void handleMin(Matrix4f pose, BufferBuilder buffer, int color) {
      int var3 = color >> 24 & 0xFF;
      int var4 = color >> 16 & 0xFF;
      int var5 = color >> 8 & 0xFF;
      int var6 = color & 0xFF;
      int var7 = Math.min(255, (int)((float)var4 * 0.78F + 56.1F));
      int var8 = Math.min(255, (int)((float)var5 * 0.78F + 51.7F));
      int var9 = Math.min(255, (int)((float)var6 * 0.78F + 56.1F));
      float var10 = 1.0F;
      handleVal(buffer, pose, -var10, var10, -var10, var10, var10, -var10, var10, var10, var10, -var10, var10, var10, var7, var8, var9, var3);
      handleVal(
         buffer,
         pose,
         -var10,
         -var10,
         var10,
         var10,
         -var10,
         var10,
         var10,
         -var10,
         -var10,
         -var10,
         -var10,
         -var10,
         (int)((float)var7 * 0.7F),
         (int)((float)var8 * 0.7F),
         (int)((float)var9 * 0.7F),
         var3
      );
      handleVal(
         buffer,
         pose,
         -var10,
         -var10,
         -var10,
         var10,
         -var10,
         -var10,
         var10,
         var10,
         -var10,
         -var10,
         var10,
         -var10,
         (int)((float)var7 * 0.9F),
         (int)((float)var8 * 0.9F),
         (int)((float)var9 * 0.9F),
         var3
      );
      handleVal(
         buffer,
         pose,
         var10,
         -var10,
         var10,
         -var10,
         -var10,
         var10,
         -var10,
         var10,
         var10,
         var10,
         var10,
         var10,
         (int)((float)var7 * 0.85F),
         (int)((float)var8 * 0.85F),
         (int)((float)var9 * 0.85F),
         var3
      );
      handleVal(
         buffer,
         pose,
         -var10,
         -var10,
         var10,
         -var10,
         -var10,
         -var10,
         -var10,
         var10,
         -var10,
         -var10,
         var10,
         var10,
         (int)((float)var7 * 0.88F),
         (int)((float)var8 * 0.88F),
         (int)((float)var9 * 0.88F),
         var3
      );
      handleVal(
         buffer,
         pose,
         var10,
         -var10,
         -var10,
         var10,
         -var10,
         var10,
         var10,
         var10,
         var10,
         var10,
         var10,
         -var10,
         (int)((float)var7 * 0.95F),
         (int)((float)var8 * 0.95F),
         (int)((float)var9 * 0.95F),
         var3
      );
   }

   public static void handleVal(
      BufferBuilder buffer,
      Matrix4f pose,
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
      buffer.vertex(pose, x1, y1, z1).color(r, g, b, a);
      buffer.vertex(pose, x2, y2, z2).color(r, g, b, a);
      buffer.vertex(pose, x3, y3, z3).color(r, g, b, a);
      buffer.vertex(pose, x1, y1, z1).color(r, g, b, a);
      buffer.vertex(pose, x3, y3, z3).color(r, g, b, a);
      buffer.vertex(pose, x4, y4, z4).color(r, g, b, a);
   }

   public static void handleVal(Matrix4f pose, BufferBuilder buffer, int color) {
      int var3 = color >> 24 & 0xFF;
      int var4 = color >> 16 & 0xFF;
      int var5 = color >> 8 & 0xFF;
      int var6 = color & 0xFF;

      for (int[] var10 : mexdedg) {
         Vector3f var11 = vector3fList[var10[0]];
         Vector3f var12 = vector3fList[var10[1]];
         buffer.vertex(pose, var11.x, var11.y, var11.z).color(var4, var5, var6, var3);
         buffer.vertex(pose, var12.x, var12.y, var12.z).color(var4, var5, var6, var3);
      }
   }

   @Override
   public int computeMin(int rgb, float anim) {
      int var3 = Math.min(255, (int)((float)(rgb >> 16 & 0xFF) * 1.35F));
      int var4 = Math.min(255, (int)((float)(rgb >> 8 & 0xFF) * 1.35F));
      int var5 = Math.min(255, (int)((float)(rgb & 0xFF) * 1.35F));
      int var6 = (int)(255.0F * anim);
      return var6 << 24 | var3 << 16 | var4 << 8 | var5;
   }

   public void initBlend(MatrixStack stack, Camera camera, double baseX, double baseY, double baseZ, float height, float halfWidth, float time, float alpha) {
      stack.push();
      float var13 = height * 0.5F;
      Vector3f var14 = new Vector3f((float)baseX, (float)(baseY + (double)var13), (float)baseZ);
      stack.peek().getPositionMatrix().transformPosition(var14);
      Matrix4f var15 = new Matrix4f();
      var15.translate(var14.x, var14.y, var14.z);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
      BufferBuilder var16 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      int var17 = MainMenuScreen.getAccent();
      float var18 = (float)(var17 >> 16 & 0xFF) / 255.0F;
      float var19 = (float)(var17 >> 8 & 0xFF) / 255.0F;
      float var20 = (float)(var17 & 0xFF) / 255.0F;
      int var21 = MainMenuScreen.getAccent2();
      float var22 = (float)(var21 >> 16 & 0xFF) / 255.0F;
      float var23 = (float)(var21 >> 8 & 0xFF) / 255.0F;
      float var24 = (float)(var21 & 0xFF) / 255.0F;
      float var25 = halfWidth + 0.5F;
      float var26 = var25 * 1.65F;
      float var27 = alpha * (0.38F + 0.05F * (float)Math.sin((double)(time * 3.0F)));
      var16.vertex(var15, -var26, -var26, 0.0F).texture(0.0F, 0.0F).color(var18, var19, var20, var27);
      var16.vertex(var15, var26, -var26, 0.0F).texture(1.0F, 0.0F).color(var18, var19, var20, var27);
      var16.vertex(var15, var26, var26, 0.0F).texture(1.0F, 1.0F).color(var18, var19, var20, var27);
      var16.vertex(var15, -var26, var26, 0.0F).texture(0.0F, 1.0F).color(var18, var19, var20, var27);
      float var28 = var25 * 1.15F;
      float var29 = alpha * 0.28F;
      var16.vertex(var15, -var28, -var28, 0.0F).texture(0.0F, 0.0F).color(var22, var23, var24, var29);
      var16.vertex(var15, var28, -var28, 0.0F).texture(1.0F, 0.0F).color(var22, var23, var24, var29);
      var16.vertex(var15, var28, var28, 0.0F).texture(1.0F, 1.0F).color(var22, var23, var24, var29);
      var16.vertex(var15, -var28, var28, 0.0F).texture(0.0F, 1.0F).color(var22, var23, var24, var29);
      BufferRenderer.drawWithGlobalProgram(var16.end());
      stack.pop();
   }
}
