package dev.daamky.client.internal;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap.Type;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class GpsRenderer implements MinecraftAccess {
   public static final GpsRenderer gpsRenderer = new GpsRenderer();
   public Vector2i gpsPosition = null;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public boolean flag = false;

   public static GpsRenderer getInstance() {
      return gpsRenderer;
   }

   public void load() {
      if (!this.flag) {
         this.flag = true;
         Render2DEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$0));
         dev.daamky.client.event.Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$1));
      }
   }

   public void handleClass_332(DrawContext context) {
   }

   public void handleGps(DrawContext context) {
      if (mc.player != null && mc.world != null) {
         boolean var2 = this.gpsPosition != null;
         this.animatedValue.update();
         this.animatedValue.animateTo(var2 ? 1.0 : 0.0, 250L, Easing.CUBIC_OUT);
         float var3 = (float)this.animatedValue.getValue();
         if (!(var3 <= 0.01F) && this.gpsPosition != null) {
            double var4 = (double)this.gpsPosition.x + 0.5;
            double var6 = (double)this.gpsPosition.y + 0.5;
            double var8 = (double)mc.world.getTopY(Type.MOTION_BLOCKING, this.gpsPosition.x, this.gpsPosition.y);
            if (var8 <= (double)mc.world.getBottomY()) {
               var8 = mc.player.getY();
            }

            double var10 = var8 + 1.2;
            double var12 = var4 - mc.player.getX();
            double var14 = var10 - mc.player.getY();
            double var16 = var6 - mc.player.getZ();
            double var18 = Math.sqrt(var12 * var12 + var14 * var14 + var16 * var16);
            int var20 = (int)Math.round(var18);
            String var21 = var20 + "m";
            Vector2f var22 = ContextHelper.computeVector2f(var4, var10, var6);
            MatrixStack var23 = context.getMatrices();
            int var24 = mc.getWindow().getScaledWidth();
            int var25 = mc.getWindow().getScaledHeight();
            float var26 = 7.0F;
            float var27 = FontBootstrap.clientFont2.computeFloat(var21, var26);
            float var28 = 3.5F;
            float var29 = 5.0F;
            float var30 = 3.5F;
            float var31 = var29 * 2.0F + var28 + 3.0F + var27;
            float var32 = var26 + var30 * 2.0F;
            float var33 = 3.5F;
            int var34 = (int)(var3 * 255.0F);
            int var35 = (int)(var3 * 200.0F);
            int var36 = var35 << 24 | 723986;
            int var37 = (int)(var3 * 55.0F) << 24 | 16777215;
            if (var22 != null && var22.x >= -var31 && var22.x <= (float)var24 + var31 && var22.y >= -var32 && var22.y <= (float)var25 + var32) {
               float var51 = var22.x - var31 / 2.0F;
               float var52 = var22.y - var32 / 2.0F;
               Render.rectRenderer.handleVal(var23, var51 - 0.5F, var52 - 0.5F, var31 + 1.0F, var32 + 1.0F, var33 + 0.5F, var37);
               Render.rectRenderer.handleVal(var23, var51, var52, var31, var32, var33, var36);
               float var53 = var51 + var29;
               float var54 = var52 + (var32 - var28) / 2.0F;
               Render.rectRenderer.handleVal(var23, var53, var54, var28, var28, var28 / 2.0F, ThemeColor.computeInt(var34));
               float var55 = var53 + var28 + 3.0F;
               float var56 = var52 + (var32 - var26) / 2.0F - 0.5F;
               FontBootstrap.clientFont2.handleVal(var23, var21, var55, var56, var26, 16777215 | var34 << 24);
            } else {
               float var38 = (float)var24 / 2.0F;
               float var39 = (float)var25 / 2.0F;
               float var40 = (float)Math.toDegrees(Math.atan2(var16, var12)) - mc.player.getYaw() - 90.0F;
               float var41 = (float)Math.toRadians((double)var40);
               float var42 = Math.min(var38, var39) - 40.0F;
               float var43 = var38 + (float)Math.sin((double)var41) * var42;
               float var44 = var39 - (float)Math.cos((double)var41) * var42;
               float var45 = var43 - var31 / 2.0F;
               float var46 = var44 - var32 / 2.0F;
               Render.rectRenderer.handleVal(var23, var45 - 0.5F, var46 - 0.5F, var31 + 1.0F, var32 + 1.0F, var33 + 0.5F, var37);
               Render.rectRenderer.handleVal(var23, var45, var46, var31, var32, var33, var36);
               float var47 = var45 + var29;
               float var48 = var46 + (var32 - var28) / 2.0F;
               Render.rectRenderer.handleVal(var23, var47, var48, var28, var28, var28 / 2.0F, ThemeColor.computeInt(var34));
               float var49 = var47 + var28 + 3.0F;
               float var50 = var46 + (var32 - var26) / 2.0F - 0.5F;
               FontBootstrap.clientFont2.handleVal(var23, var21, var49, var50, var26, 16777215 | var34 << 24);
            }
         }
      }
   }

   public void handleBlend(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (mc.player != null && mc.world != null && this.gpsPosition != null) {
         float var2 = (float)this.animatedValue.getValue();
         if (!(var2 <= 0.01F)) {
            Camera var3 = mc.gameRenderer.getCamera();
            Vec3d var4 = var3.getPos();
            double var5 = (double)this.gpsPosition.x + 0.5;
            double var7 = (double)this.gpsPosition.y + 0.5;
            double var9 = (double)mc.world.getTopY(Type.MOTION_BLOCKING, this.gpsPosition.x, this.gpsPosition.y);
            if (var9 <= (double)mc.world.getBottomY()) {
               var9 = mc.player.getY();
            }

            double var11 = var5 - var4.x;
            double var13 = var9 - var4.y;
            double var15 = var7 - var4.z;
            float var17 = 48.0F;
            float var18 = 0.22F;
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            Matrix4f var19 = event.getMatrixStack().peek().getPositionMatrix();
            BufferBuilder var20 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
            int var21 = ThemeColor.computeInt((int)(var2 * 180.0F));
            int var22 = ThemeColor.computeInt2(0);
            byte var23 = 8;

            for (int var24 = 0; var24 < var23; var24++) {
               double var25 = Math.toRadians((double)((float)var24 / (float)var23 * 360.0F));
               double var27 = Math.toRadians((double)((float)(var24 + 1) / (float)var23 * 360.0F));
               float var29 = (float)(var11 + Math.cos(var25) * (double)var18);
               float var30 = (float)(var15 + Math.sin(var25) * (double)var18);
               float var31 = (float)(var11 + Math.cos(var27) * (double)var18);
               float var32 = (float)(var15 + Math.sin(var27) * (double)var18);
               float var33 = (float)var13;
               float var34 = (float)(var13 + (double)var17);
               var20.vertex(var19, var29, var33, var30).color(var21);
               var20.vertex(var19, var31, var33, var32).color(var21);
               var20.vertex(var19, var31, var34, var32).color(var22);
               var20.vertex(var19, var29, var33, var30).color(var21);
               var20.vertex(var19, var31, var34, var32).color(var22);
               var20.vertex(var19, var29, var34, var30).color(var22);
            }

            float var35 = 0.65F;

            for (int var36 = 0; var36 < var23; var36++) {
               double var26 = Math.toRadians((double)((float)var36 / (float)var23 * 360.0F));
               double var28 = Math.toRadians((double)((float)(var36 + 1) / (float)var23 * 360.0F));
               float var38 = (float)(var11 + Math.cos(var26) * (double)var35);
               float var39 = (float)(var15 + Math.sin(var26) * (double)var35);
               float var40 = (float)(var11 + Math.cos(var28) * (double)var35);
               float var41 = (float)(var15 + Math.sin(var28) * (double)var35);
               float var42 = (float)var13 + 0.05F;
               var20.vertex(var19, (float)var11, var42, (float)var15).color(var21);
               var20.vertex(var19, var38, var42, var39).color(var21);
               var20.vertex(var19, var40, var42, var41).color(var21);
            }

            BuiltBuffer var37 = var20.end();
            if (var37 != null) {
               BufferRenderer.drawWithGlobalProgram(var37);
            }

            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
         }
      }
   }

   public Vector2i getGpsPosition() {
      return this.gpsPosition;
   }

   public void setGpsPosition(Vector2i gpsPosition) {
      this.gpsPosition = gpsPosition;
   }

   public void lambda$load$1(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      this.handleBlend(event);
   }

   public void lambda$load$0(Render2DEvent.Render2DEventData event) {
      this.handleGps(event.getContext());
   }
}
