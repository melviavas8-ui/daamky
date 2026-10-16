package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.module.render.TargetESP;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class ThickSquare extends TargetRenderer {
   public float floatVal = 1.0F;
   public float float2 = 0.0F;
   public float mihvu = 1.0F;
   public boolean flag = false;
   public static final Map<String, String> map = new HashMap<>();

   @Override
   public void runOrbit() {
      if (this.getValue()) {
         this.floatVal2 = this.floatVal;
         if (animatedValue.getValue() > 0.05) {
            float var1 = (float)animatedValue.getValue();
            this.mihvu = (this.flag ? this.mihvu - 0.5F : this.mihvu + 0.5F) * var1;
            if (this.mihvu > 25.0F) {
               this.flag = true;
            }

            if (this.mihvu < -25.0F) {
               this.flag = false;
            }

            this.floatVal = this.floatVal + this.mihvu;
         }
      }
   }

   @Override
   public void onRender3D(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (this.getValue()) {
         Camera var2 = mc.gameRenderer.getCamera();
         MatrixStack var3 = event.getMatrixStack();
         this.handleBlend(var2, var3);
      }
   }

   public void handleBlend(Camera camera, MatrixStack matrices) {
      double var3 = getMbacy() - camera.getPos().x;
      double var5 = getDouble() - camera.getPos().y;
      double var7 = getDouble2() - camera.getPos().z;
      double var9 = (double)EntityHelper.computeFloat((double)this.floatVal, animatedValue2.getValue());
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      matrices.push();
      matrices.translate(var3, var5 + (double)(TargetRenderer.getFloat3() / 2.0F), var7);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-(camera.getYaw() + 180.0F)));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-camera.getPitch()));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(EntityHelper.computeFloat((double)this.floatVal2, (double)this.floatVal)));
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      int var11 = (int)(animatedValue.getValue() * 255.0);
      float var12 = this.getFloat2();
      int var13 = this.computeMin(ThemeColor.computeInt(var11), var12);
      int var14 = this.computeMin(ThemeColor.computeInt2(var11), var12);
      RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
      Matrix4f var15 = matrices.peek().getPositionMatrix();
      BufferBuilder var16 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      float var17 = (float)var9 * 0.95F;
      int var18 = this.computeMin(ThemeColor.computeInt((int)((float)var11 * 0.4F)), var12);
      var16.vertex(var15, -var17, -var17, -0.005F).texture(0.0F, 0.0F).color(var18);
      var16.vertex(var15, var17, -var17, -0.005F).texture(1.0F, 0.0F).color(var18);
      var16.vertex(var15, var17, var17, -0.005F).texture(1.0F, 1.0F).color(var18);
      var16.vertex(var15, -var17, var17, -0.005F).texture(0.0F, 1.0F).color(var18);
      float var19 = (float)var9 * 0.65F;
      int var20 = this.computeMin(ThemeColor.computeInt2((int)((float)var11 * 0.7F)), var12);
      var16.vertex(var15, -var19, -var19, -0.007F).texture(0.0F, 0.0F).color(var20);
      var16.vertex(var15, var19, -var19, -0.007F).texture(1.0F, 0.0F).color(var20);
      var16.vertex(var15, var19, var19, -0.007F).texture(1.0F, 1.0F).color(var20);
      var16.vertex(var15, -var19, var19, -0.007F).texture(0.0F, 1.0F).color(var20);
      BuiltBuffer var21 = var16.end();
      if (var21 != null) {
         BufferRenderer.drawWithGlobalProgram(var21);
      }

      String var22 = TargetESP.getInstance().getTriangle().getValue();
      String var23 = map.getOrDefault(var22, "t1");
      RenderSystem.setShaderTexture(0, InputHelper.getImage("quads/" + var23));
      matrices.push();
      matrices.translate(-var9 / 2.0, -var9 / 2.0, -0.01);
      Matrix4f var24 = matrices.peek().getPositionMatrix();
      BufferBuilder var25 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      var25.vertex(var24, 0.0F, (float)var9, 0.0F).texture(0.0F, 1.0F).color(var13);
      var25.vertex(var24, (float)var9, (float)var9, 0.0F).texture(1.0F, 1.0F).color(var14);
      var25.vertex(var24, (float)var9, 0.0F, 0.0F).texture(1.0F, 0.0F).color(var13);
      var25.vertex(var24, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F).color(var14);
      BuiltBuffer var26 = var25.end();
      if (var26 != null) {
         BufferRenderer.drawWithGlobalProgram(var26);
      }

      matrices.pop();
      matrices.pop();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableBlend();
   }

   static {
      map.put("Thick Square", "t1");
      map.put("Circle", "t2");
      map.put("Thin Square", "t3");
      map.put("Nurik", "t4");
      map.put("Triangle", "t7");
   }
}
