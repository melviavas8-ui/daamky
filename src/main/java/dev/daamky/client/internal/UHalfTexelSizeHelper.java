package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.WindowResizeEvent;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.module.render.NameTags;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public final class UHalfTexelSizeHelper {
   public static final int mehguwe = 4;
   public static final float floatVal = 24.0F;
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("post/blur/upscale"), VertexFormats.POSITION, Defines.EMPTY);
   public static final ShaderProgramKey class_101562 = new ShaderProgramKey(InputHelper.getShader("post/blur/downscale"), VertexFormats.POSITION, Defines.EMPTY);
   public static final List<SimpleFramebuffer> list = new ArrayList<>();
   public static boolean flag = false;
   public static boolean muwelap = false;
   public static long timeStamp = 0L;
   public static final Matrix4f muwbesz = new Matrix4f();

   public static void load() {
      if (!flag) {
         initMax();
         WindowResizeEvent.getInstance().subscribe(new EventListener<>(UHalfTexelSizeHelper::lambda$load$0));
         flag = true;
      }
   }

   public static void runClear() {
      list.forEach(Framebuffer::delete);
      list.clear();
      initMax();
   }

   public static void initMax() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0 != null && var0.getWindow() != null) {
         int var1 = Math.max(1, var0.getWindow().getFramebufferWidth());
         int var2 = Math.max(1, var0.getWindow().getFramebufferHeight());

         for (int var3 = 0; var3 < 4; var3++) {
            SimpleFramebuffer var4 = new SimpleFramebuffer(var1, var2, false);
            var4.setTexFilter(9729);
            list.add(var4);
         }
      }
   }

   public static void handleEnabled(MatrixStack matrixStack) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && var1.player != null && var1.world != null && !var1.options.hudHidden) {
         long var2 = System.currentTimeMillis();
         if (var2 - timeStamp > 200L) {
            timeStamp = var2;
            muwelap = false;
            if (var1.currentScreen != null) {
               muwelap = true;
            } else if (!Interface.getInstance().isEnabled()) {
               muwelap = false;
            } else {
               for (HudElement var5 : GpsHelper.getInstance().getList()) {
                  if (var5.checkGps() && var5.getAutoHelper().isBlur()) {
                     muwelap = true;
                     break;
                  }
               }

               if (!muwelap && NameTags.getInstance().isEnabled() && NameTags.getInstance().blur.getValue()) {
                  muwelap = true;
               }
            }
         } else if (var1.currentScreen != null) {
            muwelap = true;
         }

         if (muwelap) {
            runBlend();
         }
      }
   }

   public static void handleClass_4587(MatrixStack matrixStack) {
      runBlend();
   }

   public static void run() {
      runBlend();
   }

   public static void runBlend() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0 != null && var0.getFramebuffer() != null) {
         if (list.size() != 4) {
            runClear();
         }

         if (!list.isEmpty()) {
            byte var1 = 3;

            try {
               handleDisable(class_101562, var0.getFramebuffer(), (Framebuffer)list.get(0), 0);

               for (int var2 = 0; var2 < 3; var2++) {
                  handleDisable(class_101562, (Framebuffer)list.get(var2), (Framebuffer)list.get(var2 + 1), var2 + 1);
               }

               for (int var6 = 3; var6 > 0; var6--) {
                  handleDisable(class_10156, (Framebuffer)list.get(var6), (Framebuffer)list.get(var6 - 1), var6);
               }
            } finally {
               var0.getFramebuffer().beginWrite(false);
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
            }
         }
      }
   }

   public static void handleDisable(ShaderProgramKey key, Framebuffer src, Framebuffer dst, int passIndex) {
      dst.beginWrite(false);
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.disableBlend();
      RenderSystem.setShaderTexture(0, src.getColorAttachment());
      ShaderProgram var4 = RenderSystem.setShader(key);
      if (var4 == null) {
         dst.endWrite();
      } else {
         GlUniform var5 = var4.getUniform("uHalfTexelSize");
         GlUniform var6 = var4.getUniform("uOffset");
         if (var5 != null) {
            var5.set(0.5F / (float)src.textureWidth, 0.5F / (float)src.textureHeight);
         }

         if (var6 != null) {
            var6.set(24.0F * ((float)passIndex / 3.0F));
         }

         run2();
         dst.endWrite();
      }
   }

   public static void run2() {
      BufferBuilder var0 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION);
      var0.vertex(muwbesz, -1.0F, -1.0F, 0.0F);
      var0.vertex(muwbesz, -1.0F, 1.0F, 0.0F);
      var0.vertex(muwbesz, 1.0F, 1.0F, 0.0F);
      var0.vertex(muwbesz, 1.0F, -1.0F, 0.0F);
      BufferRenderer.drawWithGlobalProgram(var0.end());
   }

   public UHalfTexelSizeHelper() {
      throw new UnsupportedOperationException("Utility class");
   }

   public static void lambda$load$0(WindowResizeEvent event) {
      runClear();
   }
}
