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
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Render3DEvent4 extends TargetRenderer {
   public static final Vector3f[] vector3fList = new Vector3f[]{
      new Vector3f(0.0F, 1.5F, 0.0F),
      new Vector3f(0.0F, -1.5F, 0.0F),
      new Vector3f(1.0F, 0.0F, 0.0F),
      new Vector3f(-1.0F, 0.0F, 0.0F),
      new Vector3f(0.0F, 0.0F, 1.0F),
      new Vector3f(0.0F, 0.0F, -1.0F)
   };
   public static final int[][] ints = new int[][]{{0, 2, 4}, {0, 4, 3}, {0, 3, 5}, {0, 5, 2}, {1, 4, 2}, {1, 3, 4}, {1, 5, 3}, {1, 2, 5}};
   public static final float[] floats = new float[]{1.0F, 0.8F, 0.6F, 0.9F, 0.7F, 0.5F, 0.4F, 0.6F};
   public static final int[][] ints2 = new int[][]{{0, 2}, {0, 3}, {0, 4}, {0, 5}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {2, 4}, {4, 3}, {3, 5}, {5, 2}};
   public static final Vector3f vector3f = new Vector3f();
   public static final Vector3f mabwuq = new Vector3f(0.0F, 1.0F, 0.0F);
   public final float[] mamqu = new float[18];
   public final float[] floats2 = new float[18];
   public final float[] maaia = new float[18];
   public final int[] ints3 = new int[18];
   public final Quaternionf[] quaternionfList = new Quaternionf[18];
   public float floatVal = 0.0F;

   public Render3DEvent4() {
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
         float var1 = this.getInstance().getCrystalOrbitSpeed().getValue();
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
            var3.translate(var5, var7, var9);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(770, 1, 1, 0);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            float var11 = this.getInstance().getCrystalSize().getValue() / 0.16F;
            float var12 = this.getInstance().getCrystalOrbitSpeed().getValue();
            float var13 = this.floatVal + event.getPartialTicks() * var12 * 12.0F;
            float var14 = TargetRenderer.getFloat4();
            float var15 = TargetRenderer.getFloat3();
            float var16 = var14 * 1.55F * var11;
            float var17 = 1.2F - 0.5F * var2;
            float var18 = 0.11F * var11;
            boolean var19 = this.getInstance().getCrystalGlow().getValue();
            if (var19) {
               this.handleBlend(var3, var4, var15, var16 * 0.5F, var13 * 0.02F, var2);
            }

            byte var20 = 18;
            int var21 = 0;
            float var22 = this.getFloat2();

            for (byte var23 = 0; var23 < 360; var21++) {
               float var24 = (float)Math.toRadians((double)((float)var23 + var13 * 0.3F));
               this.mamqu[var21] = (float)(Math.sin((double)var24) * (double)var16 * (double)var17);
               this.floats2[var21] = (float)(Math.cos((double)var24) * (double)var16 * (double)var17);
               this.maaia[var21] = 0.1F + var15 * Math.abs((float)Math.sin((double)var23));
               int var25 = ThemeColor.computeCurrent((int)((float)var23 + var13), (int)(255.0F * var2));
               this.ints3[var21] = this.computeMin(var25, var22);
               vector3f.set(-this.mamqu[var21], var15 * 0.5F - 1.0F, -this.floats2[var21]);
               if (vector3f.lengthSquared() < 1.0E-8F) {
                  vector3f.set(0.0F, 1.0F, 0.0F);
               } else {
                  vector3f.normalize();
               }

               this.quaternionfList[var21].rotationTo(mabwuq, vector3f);
               var23 += 20;
            }

            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder var34 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

            for (int var35 = 0; var35 < var20; var35++) {
               var3.push();
               var3.translate(this.mamqu[var35], this.maaia[var35], this.floats2[var35]);
               var3.multiply(this.quaternionfList[var35]);
               var3.scale(var18, var18, var18);
               handleMin(var3.peek().getPositionMatrix(), var34, this.ints3[var35]);
               var3.pop();
            }

            BufferRenderer.drawWithGlobalProgram(var34.end());
            RenderSystem.lineWidth(2.0F);
            BufferBuilder var36 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            for (int var37 = 0; var37 < var20; var37++) {
               var3.push();
               var3.translate(this.mamqu[var37], this.maaia[var37], this.floats2[var37]);
               var3.multiply(this.quaternionfList[var37]);
               var3.scale(var18, var18, var18);
               int var26 = this.computeMin(this.ints3[var37], var2);
               handleVal(var3.peek().getPositionMatrix(), var36, var26);
               var3.pop();
            }

            BufferRenderer.drawWithGlobalProgram(var36.end());
            RenderSystem.lineWidth(1.0F);
            if (var19) {
               RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
               BufferBuilder var38 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               float var39 = 0.55F * var11;

               for (int var27 = 0; var27 < var20; var27++) {
                  var3.push();
                  var3.translate(this.mamqu[var27], this.maaia[var27], this.floats2[var27]);
                  Matrix4f var28 = var3.peek().getPositionMatrix();
                  int var29 = this.ints3[var27];
                  int var30 = var29 >> 16 & 0xFF;
                  int var31 = var29 >> 8 & 0xFF;
                  int var32 = var29 & 0xFF;
                  int var33 = (int)((float)(var29 >> 24 & 0xFF) * 0.65F);
                  var38.vertex(var28, -var39, -var39, 0.0F).texture(0.0F, 0.0F).color(var30, var31, var32, var33);
                  var38.vertex(var28, var39, -var39, 0.0F).texture(1.0F, 0.0F).color(var30, var31, var32, var33);
                  var38.vertex(var28, var39, var39, 0.0F).texture(1.0F, 1.0F).color(var30, var31, var32, var33);
                  var38.vertex(var28, -var39, var39, 0.0F).texture(0.0F, 1.0F).color(var30, var31, var32, var33);
                  var3.pop();
               }

               BufferRenderer.drawWithGlobalProgram(var38.end());
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

      for (int var10 = 0; var10 < ints.length; var10++) {
         int[] var11 = ints[var10];
         float var12 = floats[var10];
         int var13 = (int)((float)var7 * var12);
         int var14 = (int)((float)var8 * var12);
         int var15 = (int)((float)var9 * var12);
         Vector3f var16 = vector3fList[var11[0]];
         Vector3f var17 = vector3fList[var11[1]];
         Vector3f var18 = vector3fList[var11[2]];
         buffer.vertex(pose, var16.x, var16.y, var16.z).color(var13, var14, var15, var3);
         buffer.vertex(pose, var17.x, var17.y, var17.z).color(var13, var14, var15, var3);
         buffer.vertex(pose, var18.x, var18.y, var18.z).color(var13, var14, var15, var3);
      }
   }

   public static void handleVal(Matrix4f pose, BufferBuilder buffer, int color) {
      int var3 = color >> 24 & 0xFF;
      int var4 = color >> 16 & 0xFF;
      int var5 = color >> 8 & 0xFF;
      int var6 = color & 0xFF;

      for (int[] var10 : ints2) {
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

   public void handleBlend(MatrixStack stack, Camera camera, float height, float halfWidth, float time, float alpha) {
      stack.push();
      float var7 = height * 0.5F;
      stack.translate(0.0F, var7, 0.0F);
      stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
      stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
      BufferBuilder var8 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      Matrix4f var9 = stack.peek().getPositionMatrix();
      int var10 = MainMenuScreen.getAccent();
      float var11 = (float)(var10 >> 16 & 0xFF) / 255.0F;
      float var12 = (float)(var10 >> 8 & 0xFF) / 255.0F;
      float var13 = (float)(var10 & 0xFF) / 255.0F;
      int var14 = MainMenuScreen.getAccent2();
      float var15 = (float)(var14 >> 16 & 0xFF) / 255.0F;
      float var16 = (float)(var14 >> 8 & 0xFF) / 255.0F;
      float var17 = (float)(var14 & 0xFF) / 255.0F;
      float var18 = halfWidth + 0.5F;
      float var19 = var18 * 1.65F;
      float var20 = alpha * (0.38F + 0.05F * (float)Math.sin((double)(time * 3.0F)));
      var8.vertex(var9, -var19, -var19, 0.0F).texture(0.0F, 0.0F).color(var11, var12, var13, var20);
      var8.vertex(var9, var19, -var19, 0.0F).texture(1.0F, 0.0F).color(var11, var12, var13, var20);
      var8.vertex(var9, var19, var19, 0.0F).texture(1.0F, 1.0F).color(var11, var12, var13, var20);
      var8.vertex(var9, -var19, var19, 0.0F).texture(0.0F, 1.0F).color(var11, var12, var13, var20);
      float var21 = var18 * 1.15F;
      float var22 = alpha * 0.28F;
      var8.vertex(var9, -var21, -var21, 0.0F).texture(0.0F, 0.0F).color(var15, var16, var17, var22);
      var8.vertex(var9, var21, -var21, 0.0F).texture(1.0F, 0.0F).color(var15, var16, var17, var22);
      var8.vertex(var9, var21, var21, 0.0F).texture(1.0F, 1.0F).color(var15, var16, var17, var22);
      var8.vertex(var9, -var21, var21, 0.0F).texture(0.0F, 1.0F).color(var15, var16, var17, var22);
      BufferRenderer.drawWithGlobalProgram(var8.end());
      stack.pop();
   }
}
