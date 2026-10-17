package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.gui.MainMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class MenuAnimation {
   public static final MinecraftClient class_310 = net.minecraft.client.MinecraftClient.getInstance();
   public static SimpleFramebuffer class_6367;
   public static MainMenuScreen mainMenuScreen = null;
   public static boolean mevjotv = false;
   public static boolean flag = false;
   public static boolean mqelu = false;
   public static long timeStamp = 0L;
   public static final float floatVal = 1600.0F;
   public static Vec3d class_243;
   public static Vec3d class_2432;
   public static Vec3d class_2433;
   public static Vec3d class_2434;
   public static final MenuAnimation.CubicBezier cubicBezier = new MenuAnimation.CubicBezier(0.62, -0.16, 0.8, 0.37);

   public static void initInit() {
      if (!mqelu) {
         mqelu = true;
         Render2DEvent.getInstance().subscribe(new EventListener<>(MenuAnimation::setEvent));
         dev.daamky.client.event.Render3DEvent.getInstance().subscribe(new EventListener<>(MenuAnimation::setEvent));
      }
   }

   public static void initCos(MainMenuScreen menu) {
      if (class_310.world != null && class_310.player != null) {
         Camera var1 = class_310.gameRenderer.getCamera();
         if (var1 == null) {
            run();
         } else {
            longVal = System.currentTimeMillis();
            mevjotv = false;
            flag = false;
            double var2 = Math.toRadians((double)var1.getYaw());
            double var4 = Math.toRadians((double)var1.getPitch());
            class_243 = new Vec3d(-Math.sin(var2) * Math.cos(var4), -Math.sin(var4), Math.cos(var2) * Math.cos(var4)).normalize();
            class_2432 = class_243.crossProduct(new Vec3d(0.0, 1.0, 0.0)).normalize();
            class_2433 = class_2432.crossProduct(class_243).normalize();
            class_2434 = var1.getPos().add(class_243.multiply(1.5));
            mainMenuScreen = menu;
         }
      } else {
         run();
      }
   }

   public static void handleMainMenuScreen(MainMenuScreen menu) {
      initCos(menu);
   }

   public static void run() {
      mainMenuScreen = null;
      mevjotv = false;
      flag = false;
   }

   public static boolean check() {
      return mainMenuScreen != null;
   }

   public static float getCurrent() {
      return longVal == 0L ? 1.0F : Math.min(1.0F, Math.max(0.0F, (float)(System.currentTimeMillis() - longVal) / 1600.0F));
   }

   public static void initInit(int fbW, int fbH) {
      if (class_6367 == null || class_6367.textureWidth != fbW || class_6367.textureHeight != fbH) {
         if (class_6367 != null) {
            class_6367.delete();
         }

         class_6367 = new SimpleFramebuffer(fbW, fbH, true);
         class_6367.setTexFilter(9729);
      }
   }

   public static void setEvent(Render2DEvent.Render2DEventData event) {
      if (mainMenuScreen != null && class_310.player != null) {
         float var1 = getCurrent();
         if (var1 >= 1.0F) {
            run();
         } else {
            if (class_310.currentScreen == null) {
               int var2 = class_310.getWindow().getFramebufferWidth();
               int var3 = class_310.getWindow().getFramebufferHeight();
               if (var2 <= 0 || var3 <= 0) {
                  return;
               }

               if (!mevjotv) {
                  initInit(var2, var3);
                  RenderSystem.colorMask(true, true, true, true);
                  class_6367.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                  class_6367.clear();
                  class_6367.beginWrite(false);
                  MainMenuScreen.is3DCapturing = true;

                  try {
                     mainMenuScreen.runDay();
                     mainMenuScreen.initDay(event.getContext(), null);
                  } catch (Throwable var8) {
                  } finally {
                     MainMenuScreen.is3DCapturing = false;
                     RenderQueue.runClear();
                     class_6367.endWrite();
                     class_310.getFramebuffer().beginWrite(true);
                  }

                  mevjotv = true;
               }

               if (!flag && mevjotv) {
                  renderShader(event);
               }
            }
         }
      }
   }

   public static void renderShader(Render2DEvent.Render2DEventData event) {
      if (class_6367 != null) {
         float var1 = (float)class_310.getWindow().getScaledWidth();
         float var2 = (float)class_310.getWindow().getScaledHeight();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableDepthTest();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         RenderSystem.setShaderTexture(0, class_6367.getColorAttachment());
         Matrix4f var3 = event.getMatrixStack().peek().getPositionMatrix();
         byte var4 = -1;
         BufferBuilder var5 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         var5.vertex(var3, 0.0F, 0.0F, 0.0F).texture(0.0F, 1.0F).color(var4);
         var5.vertex(var3, 0.0F, var2, 0.0F).texture(0.0F, 0.0F).color(var4);
         var5.vertex(var3, var1, var2, 0.0F).texture(1.0F, 0.0F).color(var4);
         var5.vertex(var3, var1, 0.0F, 0.0F).texture(1.0F, 1.0F).color(var4);
         BufferRenderer.drawWithGlobalProgram(var5.end());
         RenderSystem.enableDepthTest();
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.disableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public static void setEvent(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      if (mainMenuScreen != null && mevjotv && class_2434 != null && class_6367 != null) {
         if (class_310.player != null && class_310.gameRenderer != null) {
            Camera var1 = class_310.gameRenderer.getCamera();
            if (var1 != null) {
               float var2 = getCurrent();
               float var3 = Math.min(1.0F, var2 / 0.4F);
               float var4 = cubicBezier.computeFloat(var3, 0.0F, 1.0F, 1.0F);
               float var5 = 1.0F - var4;
               if (!(var5 <= 0.001F) && !(var2 >= 1.0F)) {
                  int var6 = class_310.getWindow().getFramebufferWidth();
                  int var7 = class_310.getWindow().getFramebufferHeight();
                  if (var6 > 0 && var7 > 0) {
                     float var8 = ((Integer)class_310.options.getFov().getValue()).floatValue();
                     float var9 = (float)Math.toRadians((double)var8);
                     float var10 = (float)(Math.tan((double)var9 / 2.0) / Math.tan(Math.toRadians(110.0) / 2.0));
                     float var11 = (3.3F + var5) * var10;
                     float var12 = var11 * ((float)var6 / (float)var7);
                     Vec3d var13 = var1.getPos();
                     Vec3d var14 = class_2432.multiply((double)var12 / 2.0);
                     Vec3d var15 = class_2433.multiply((double)var11 / 2.0);
                     Vec3d var16 = class_2434.subtract(var14).add(var15).subtract(var13);
                     Vec3d var17 = class_2434.subtract(var14).subtract(var15).subtract(var13);
                     Vec3d var18 = class_2434.add(var14).subtract(var15).subtract(var13);
                     Vec3d var19 = class_2434.add(var14).add(var15).subtract(var13);
                     RenderSystem.enableBlend();
                     RenderSystem.defaultBlendFunc();
                     RenderSystem.disableDepthTest();
                     RenderSystem.disableCull();
                     RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
                     RenderSystem.setShaderTexture(0, class_6367.getColorAttachment());
                     Matrix4f var20 = event.getMatrixStack().peek().getPositionMatrix();
                     int var21 = MathHelper.clamp((int)(255.0F * var5), 0, 255);
                     int var22 = (var21 & 0xFF) << 24 | 16777215;
                     BufferBuilder var23 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                     var23.vertex(var20, (float)var16.x, (float)var16.y, (float)var16.z)
                        .texture(0.0F, 1.0F)
                        .color(var22);
                     var23.vertex(var20, (float)var17.x, (float)var17.y, (float)var17.z)
                        .texture(0.0F, 0.0F)
                        .color(var22);
                     var23.vertex(var20, (float)var18.x, (float)var18.y, (float)var18.z)
                        .texture(1.0F, 0.0F)
                        .color(var22);
                     var23.vertex(var20, (float)var19.x, (float)var19.y, (float)var19.z)
                        .texture(1.0F, 1.0F)
                        .color(var22);
                     BufferRenderer.drawWithGlobalProgram(var23.end());
                     flag = true;
                     RenderSystem.enableDepthTest();
                     RenderSystem.enableCull();
                     RenderSystem.disableBlend();
                     RenderSystem.setShaderTexture(0, 0);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                     if (var2 >= 1.0F) {
                        run();
                     }
                  }
               } else {
                  run();
               }
            }
         }
      }
   }

   public static class CubicBezier {
      public final double x1;
      public final double y1;
      public final double x2;
      public final double y2;

      public CubicBezier(double x1, double y1, double x2, double y2) {
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
      }

      public float computeFloat(float t, float b, float c, float d) {
         if (d <= 0.0F || t <= 0.0F) {
            return b;
         } else if (t >= d) {
            return b + c;
         } else {
            float var5 = t / d;
            float var6 = this.computeAbs((float)this.x1, (float)this.x2, var5);
            float var7 = this.computeFloat(var6, (float)this.y1, (float)this.y2);
            return b + c * var7;
         }
      }

      public float computeAbs(float x1, float x2, float targetX) {
         float var4 = targetX;

         for (int var5 = 0; var5 < 8; var5++) {
            float var6 = this.computeFloat(var4, x1, x2);
            float var7 = this.computeFloat2(var4, x1, x2);
            if (Math.abs(var6 - targetX) < 1.0E-5F || Math.abs(var7) < 1.0E-6F) {
               break;
            }

            var4 -= (var6 - targetX) / var7;
            var4 = Math.max(0.0F, Math.min(1.0F, var4));
         }

         return var4;
      }

      public float computeFloat(float t, float p1, float p2) {
         return 3.0F * (1.0F - t) * (1.0F - t) * t * p1 + 3.0F * (1.0F - t) * t * t * p2 + t * t * t;
      }

      public float computeFloat2(float t, float p1, float p2) {
         return 3.0F * ((1.0F - t) * (1.0F - 3.0F * t) * p1 + (2.0F * t - 3.0F * t * t) * p2) + 3.0F * t * t;
      }
   }
}
