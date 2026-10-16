package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.mixin.WorldRendererAccessor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@ModuleInfo(
   name = "Comets",
   category = ModuleCategory.VISUALS,
   description = "Красивые летящие кометы вокруг вас с длинными хвостами"
)
public class Comets extends Module {
   public static final Comets comets = new Comets();
   public final FloatSetting amount = new FloatSetting("Amount").value(30.0F).range(5.0F, 100.0F).step(1.0F);
   public final FloatSetting range = new FloatSetting("Range").value(16.0F).range(5.0F, 32.0F).step(1.0F);
   public final FloatSetting trailLength = new FloatSetting("Trail Length").value(15.0F).range(5.0F, 30.0F).step(1.0F);
   public final FloatSetting headSize = new FloatSetting("Head Size").value(0.15F).range(0.05F, 0.4F).step(0.01F);
   public final FloatSetting thickness = new FloatSetting("Thickness").value(1.0F).range(0.2F, 3.0F).step(0.1F);
   public final List<Comets.Comet> list = new ArrayList<>();
   public final Random random = new Random();
   public final Vector3f mxiog = new Vector3f();
   public final Matrix4f matrix4f = new Matrix4f();
   public static final Vector4f vector4f = new Vector4f();
   public static final Vector4f vector4f2 = new Vector4f();
   public static final Vector4f miwxo = new Vector4f();
   public static final Vector4f vector4f3 = new Vector4f();

   public Comets() {
      this.addSettings(new Setting[]{this.amount, this.range, this.trailLength, this.headSize, this.thickness});
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.list.clear();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.list.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public static Comets getInstance() {
      return comets;
   }

   public void lambda$onEvent$2(Render3DEvent.Render3DEventData event) {
      if (mc.player != null && mc.world != null && !this.list.isEmpty() && mc.currentScreen == null) {
         MatrixStack var2 = event.getMatrixStack();
         Vec3d var3 = mc.gameRenderer.getCamera().getPos();
         Frustum var4 = null;
         if (mc.worldRenderer instanceof WorldRendererAccessor) {
            var4 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
         }

         if (var4 == null) {
            var4 = new Frustum(var2.peek().getPositionMatrix(), RenderSystem.getProjectionMatrix());
            var4.setPosition(var3.x, var3.y, var3.z);
         }

         boolean var5 = false;

         for (Comets.Comet var7 : this.list) {
            Box var8 = new Box(
               var7.class_243.x - 3.0,
               var7.class_243.y - 3.0,
               var7.class_243.z - 3.0,
               var7.class_243.x + 3.0,
               var7.class_243.y + 3.0,
               var7.class_243.z + 3.0
            );
            if (var4.isVisible(var8) && var7.deque.size() >= 2) {
               var5 = true;
               break;
            }
         }

         if (var5) {
            Render.renderEffect.handleBlend(var2);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            BufferBuilder var32 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            Color var33 = ThemeColor.getTheme().getPrimary();
            float var34 = this.headSize.getValue();
            float var9 = this.thickness.getValue();
            float var10 = (float)var33.getRed() / 255.0F;
            float var11 = (float)var33.getGreen() / 255.0F;
            float var12 = (float)var33.getBlue() / 255.0F;

            for (Comets.Comet var14 : this.list) {
               Box var16 = new Box(
                  var14.class_243.x - 3.0,
                  var14.class_243.y - 3.0,
                  var14.class_243.z - 3.0,
                  var14.class_243.x + 3.0,
                  var14.class_243.y + 3.0,
                  var14.class_243.z + 3.0
               );
               int var15;
               if (var4.isVisible(var16) && (var15 = var14.deque.size()) >= 2) {
                  int var17 = 0;

                  for (Vec3d var19 : var14.deque) {
                     double var20 = var19.x - var3.x;
                     double var22 = var19.y - var3.y;
                     double var24 = var19.z - var3.z;
                     float var26 = (float)var17 / (float)var15;
                     float var27 = var26 * var14.mruisc;
                     float var28 = var34 * (0.3F + 0.7F * var26) * var9;
                     Vector3f var29 = this.mxiog.set((float)var20, (float)var22, (float)var24);
                     var2.peek().getPositionMatrix().transformPosition(var29);
                     Matrix4f var30 = this.matrix4f.identity();
                     var32.vertex(var30, var29.x - var28, var29.y - var28, var29.z).texture(0.0F, 1.0F).color(var10, var11, var12, var27);
                     var32.vertex(var30, var29.x + var28, var29.y - var28, var29.z).texture(1.0F, 1.0F).color(var10, var11, var12, var27);
                     var32.vertex(var30, var29.x + var28, var29.y + var28, var29.z).texture(1.0F, 0.0F).color(var10, var11, var12, var27);
                     var32.vertex(var30, var29.x - var28, var29.y + var28, var29.z).texture(0.0F, 0.0F).color(var10, var11, var12, var27);
                     var17++;
                  }
               }
            }

            try {
               BufferRenderer.drawWithGlobalProgram(var32.end());
            } catch (Exception var31) {
            }

            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            Render.renderEffect.handleBlend2(var2);
         }
      }
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      if (mc.player != null && mc.world != null) {
         double var2 = (double)this.range.getValue().floatValue();
         Vec3d var4 = mc.player.getPos();
         this.list.removeIf(Comets::lambda$onEvent$0);
         int var5 = this.amount.getValue().intValue();

         while (this.list.size() > var5) {
            this.list.remove(0);
         }

         if (this.list.size() < var5) {
            this.list.add(new Comets.Comet(var4, var2));
         }

         for (Comets.Comet var7 : this.list) {
            var7.handleAdd(var4, var2, this.trailLength.getValue().intValue());
         }
      } else {
         this.list.clear();
      }
   }

   public static boolean lambda$onEvent$0(Vec3d playerPos, double maxDist, Comets.Comet c) {
      return c.class_243.squaredDistanceTo(playerPos) > maxDist * maxDist * 1.5;
   }

   private class Comet {
      public Vec3d class_243;
      public Vec3d class_2432;
      public final Deque<Vec3d> deque = new ArrayDeque<>();
      public float mruisc = 0.0F;
      public float floatVal = 1.0F;
      public int intVal = 0;
      public int int2;
      public double doubleVal;

      public Comet(Vec3d playerPos, double maxDist) {
         this.initNext(playerPos, maxDist);
      }

      public void initNext(Vec3d playerPos, double maxDist) {
         double var4 = Comets.this.random.nextDouble() * Math.PI * 2.0;
         double var6 = 1.5 + Comets.this.random.nextDouble() * Math.max(1.0, maxDist - 1.5);
         double var8 = Math.cos(var4) * var6;
         double var10 = Math.sin(var4) * var6;
         double var12 = (Comets.this.random.nextDouble() - 0.5) * 1.5;
         this.class_243 = playerPos.add(var8, var12 + 0.9, var10);
         this.class_2432 = new Vec3d(
            (Comets.this.random.nextDouble() - 0.5) * 0.12, (Comets.this.random.nextDouble() - 0.5) * 0.04, (Comets.this.random.nextDouble() - 0.5) * 0.12
         );
         this.deque.clear();
         this.mruisc = 0.0F;
         this.floatVal = 0.5F + Comets.this.random.nextFloat() * 0.5F;
         this.intVal = 0;
         this.int2 = 60 + Comets.this.random.nextInt(100);
         this.doubleVal = Comets.this.random.nextDouble() * 100.0;
      }

      public void handleAdd(Vec3d playerPos, double maxDist, int maxTrailSize) {
         this.intVal++;
         double var5 = Math.sin((double)this.intVal * 0.08 + this.doubleVal) * 0.02;
         double var7 = Math.cos((double)this.intVal * 0.05 + this.doubleVal) * 0.01;
         this.class_243 = this.class_243.add(this.class_2432.x + var7, this.class_2432.y + var5, this.class_2432.z);
         this.deque.add(this.class_243);

         while (this.deque.size() > maxTrailSize) {
            this.deque.removeFirst();
         }

         if (this.intVal < 20) {
            float var10000 = (float)this.intVal / 20.0F * this.floatVal;
         } else {
            this.mruisc = this.intVal > this.int2 - 20 ? (float)(this.int2 - this.intVal) / 20.0F * this.floatVal : this.floatVal;
         }

         if (this.intVal >= this.int2 || this.class_243.squaredDistanceTo(playerPos) > maxDist * maxDist) {
            this.initNext(playerPos, maxDist);
         }
      }
   }
}
