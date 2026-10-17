package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.mixin.WorldRendererAccessor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@ModuleInfo(
   name = "Svetych",
   category = ModuleCategory.VISUALS,
   description = "Делает чары и предметы светящимися"
)
@Environment(EnvType.CLIENT)
public class Svetych extends Module {
   public static Svetych svetych;
   public final ModeSetting fireflies = new ModeSetting("Mode").value("Cubes").values("Cubes", "Lanterns", "Crystals", "Fireflies");
   public final FloatSetting amount = new FloatSetting("Amount").value(40.0F).range(5.0F, 150.0F).step(1.0F);
   public final FloatSetting range = new FloatSetting("Range").value(9.0F).range(3.0F, 24.0F).step(0.5F);
   public final List<Svetych.AbstractParticle> list = new ArrayList<>();
   public final List<Svetych.AbstractParticle> list2 = new ArrayList<>();
   public final Svetych.RenderContext renderContext = new Svetych.RenderContext(null, null, null, null);
   public final DelayHelper mvova = new DelayHelper();
   public final Random random = new Random();
   public int intVal = 72;
   public long now = 0L;

   public static Svetych getInstance() {
      if (svetych == null) {
         svetych = new Svetych();
      }

      return svetych;
   }

   public Svetych() {
      this.addSettings(new Setting[]{this.fireflies, this.amount, this.range});
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.list.clear();
      this.mvova.runCurrent();
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

   public void setNow(long now) {
      if (now - this.now > 1000L) {
         if (mc.world != null) {
            int var3 = mc.world.getPlayers().size();
            this.intVal = var3 > 30 ? 24 : (var3 > 20 ? 42 : 72);
         }

         this.now = now;
      }
   }

   public void lambda$onEvent$2(Render3DEvent.Render3DEventData event) {
      if (mc.currentScreen == null && mc.player != null && mc.world != null && !this.list.isEmpty()) {
         Render.renderEffect.handleBlend(event.getMatrixStack());
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
         RenderSystem.disableCull();
         MatrixStack var3 = event.getMatrixStack();
         Vec3d var4 = mc.gameRenderer.getCamera().getPos();
         long var5 = System.currentTimeMillis();
         Color var7 = ThemeColor.getTheme().getPrimary();
         double var8 = (double)(this.range.getValue() * this.range.getValue());
         Vec3d var10 = mc.player.getPos();
         Iterator var11 = this.list.iterator();

         while (var11.hasNext()) {
            Svetych.AbstractParticle var12 = (Svetych.AbstractParticle)var11.next();
            var12.handleNow(var5);
            if (var12.check() || var12.computeDouble(var10) > var8 * 1.35) {
               var11.remove();
            }
         }

         Quaternionf var27 = mc.getEntityRenderDispatcher().getRotation();
         Frustum var13 = null;
         if (mc.worldRenderer instanceof WorldRendererAccessor) {
            var13 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
         }

         this.list2.clear();

         for (Svetych.AbstractParticle var15 : this.list) {
            if (var15.computeDouble(var10) <= var8
               && (
                  var13 == null
                     || var13.isVisible(
                        new Box(
                           var15.doubleVal - 0.25, var15.miewd - 0.25, var15.double2 - 0.25, var15.doubleVal + 0.25, var15.miewd + 0.25, var15.double2 + 0.25
                        )
                     )
               )) {
               this.list2.add(var15);
            }
         }

         if (this.list2.isEmpty()) {
            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            Render.renderEffect.handleBlend2(var3);
            RenderSystem.disableBlend();
         } else {
            Svetych.RenderContext var28 = this.renderContext;
            var28.runGlow();

            for (Svetych.AbstractParticle var16 : this.list2) {
               var16.handleRenderContext(var28);
            }

            boolean var30 = var28.flag;
            boolean var31 = var28.flag2;
            boolean var17 = var28.flag3;
            if (var30) {
               BufferBuilder var2 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               var28.runGlow();
               var28.quads = var2;

               for (Svetych.AbstractParticle var19 : this.list2) {
                  var19.handleDegrees(var3, var4, var5, var7, var27, var28);
               }

               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

               try {
                  BufferRenderer.drawWithGlobalProgram(var2.end());
               } catch (Exception var24) {
               }
            }

            if (var31) {
               BufferBuilder var25 = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);
               var28.runGlow();
               var28.lines = var25;

               for (Svetych.AbstractParticle var35 : this.list2) {
                  var35.handleDegrees2(var3, var4, var5, var7, var27, var28);
               }

               RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);

               try {
                  BufferRenderer.drawWithGlobalProgram(var25.end());
               } catch (Exception var23) {
               }
            }

            if (var17) {
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, Svetych.ShapeRenderer.class_2960);
               BufferBuilder var26 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               var28.runGlow();
               var28.glowC = var26;

               for (Svetych.AbstractParticle var36 : this.list2) {
                  var36.handleGlow(var3, var4, var5, var7, var27, var28);
               }

               try {
                  BufferRenderer.drawWithGlobalProgram(var26.end());
               } catch (Exception var22) {
               }

               RenderSystem.setShaderTexture(0, Svetych.ShapeRenderer.mrulx);
               BufferBuilder var34 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               var28.runGlow();
               var28.glowG = var34;

               for (Svetych.AbstractParticle var20 : this.list2) {
                  var20.handleGlow2(var3, var4, var5, var7, var27, var28);
               }

               try {
                  BufferRenderer.drawWithGlobalProgram(var34.end());
               } catch (Exception var21) {
               }
            }

            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            Render.renderEffect.handleBlend2(var3);
            RenderSystem.disableBlend();
         }
      }
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      if (mc.player != null && mc.world != null) {
         long var2 = System.currentTimeMillis();
         this.setNow(var2);
         double var4 = (double)(this.range.getValue() * this.range.getValue());
         Vec3d var6 = mc.player.getPos();
         this.list.removeIf((p0) -> Svetych.lambda$onEvent$0(var6, var4, p0));
         int var7 = Math.min(this.amount.getValue().intValue(), this.intVal);
         if (this.list.size() > var7) {
            this.list.subList(0, this.list.size() - var7).clear();
         }

         if (this.list.size() < var7 && this.mvova.computeCurrent(230L)) {
            this.list
               .add(
                  Svetych.ParticleFactory.computeAbstractParticle(
                     this.fireflies.getValue(), var6, mc.player.getHeight(), (double)this.range.getValue().floatValue()
                  )
               );
            this.mvova.runCurrent();
         }
      } else {
         this.list.clear();
      }
   }

   public static boolean lambda$onEvent$0(Vec3d playerPos, double maxDistSq, Svetych.AbstractParticle p) {
      return p.computeDouble(playerPos) > maxDistSq * 1.25;
   }

   public abstract static class AbstractParticle {
      public double doubleVal;
      public double miewd;
      public double double2;
      public double double3;
      public double double4;
      public double double5;
      public long longVal;
      public float floatVal;
      public float float2 = 1.0F;
      public static final float float3 = 0.26F;
      public int intVal = Integer.MIN_VALUE;
      public int int2 = Integer.MIN_VALUE;
      public int int3 = Integer.MIN_VALUE;
      public boolean flag = false;

      public AbstractParticle(Vec3d pos, float height, double maxRange) {
         this.longVal = System.currentTimeMillis();
         this.floatVal = (float)(Math.random() * 100.0);
         double var5 = 1.0 + Math.random() * Math.max(0.5, maxRange - 1.0);
         double var7 = Math.random() * Math.PI * 2.0;
         this.doubleVal = pos.x + Math.cos(var7) * var5;
         this.double2 = pos.z + Math.sin(var7) * var5;
         this.miewd = pos.y + 0.3 + Math.random() * (double)(height * 0.8F);
         this.double3 = (Math.random() - 0.5) * 0.02;
         this.double4 = (Math.random() - 0.5) * 0.01;
         this.double5 = (Math.random() - 0.5) * 0.02;
      }

      public abstract void handleNow(long var1);

      public void handleRenderContext(MatrixStack matrices, Vec3d cameraPos, long now, Color baseColor, Quaternionf cameraRot, Svetych.RenderContext ctx) {
      }

      public abstract void handleRenderContext(Svetych.RenderContext var1);

      public void handleDegrees(MatrixStack m, Vec3d cam, long now, Color base, Quaternionf rot, Svetych.RenderContext ctx) {
      }

      public void handleDegrees2(MatrixStack m, Vec3d cam, long now, Color base, Quaternionf rot, Svetych.RenderContext ctx) {
      }

      public void handleGlow(MatrixStack m, Vec3d cam, long now, Color base, Quaternionf rot, Svetych.RenderContext ctx) {
      }

      public void handleGlow2(MatrixStack m, Vec3d cam, long now, Color base, Quaternionf rot, Svetych.RenderContext ctx) {
      }

      public boolean initInit(double px, double py, double pz) {
         int var7 = MathHelper.floor(px);
         int var8 = MathHelper.floor(py);
         int var9 = MathHelper.floor(pz);
         if (var7 == this.intVal && var8 == this.int2 && var9 == this.int3) {
            return this.flag;
         } else {
            this.intVal = var7;
            this.int2 = var8;
            this.int3 = var9;
            BlockPos var10 = new BlockPos(var7, var8, var9);
            this.flag = MinecraftAccess.mc.world != null
               && MinecraftAccess.mc.world.getBlockState(var10).isFullCube(MinecraftAccess.mc.world, var10);
            return this.flag;
         }
      }

      public boolean check() {
         return this.float2 <= 0.01F;
      }

      public double computeDouble(Vec3d pos) {
         double var2 = this.doubleVal - pos.x;
         double var4 = this.miewd - pos.y;
         double var6 = this.double2 - pos.z;
         return var2 * var2 + var4 * var4 + var6 * var6;
      }
   }

   public static class CrystalParticle extends Svetych.AbstractParticle {
      public CrystalParticle(Vec3d pos, float height, double maxRange) {
         super(pos, height, maxRange);
      }

      public double computeSin(long now) {
         return Math.sin((double)now / 400.0 + (double)this.floatVal) * 0.2;
      }


      @Override
      public void handleRenderContext(Svetych.RenderContext var1) {
      } // STUB (render body lost by decompiler)

      @Override
      public void handleNow(long var1) {
      } // STUB (body lost by decompiler)
   }

   public static class CubeParticle extends Svetych.AbstractParticle {
      public CubeParticle(Vec3d pos, float height, double maxRange) {
         super(pos, height, maxRange);
      }

      public float computeFloat(long now) {
         return (float)(now % 9000L) / 9000.0F * 360.0F;
      }


      @Override
      public void handleRenderContext(Svetych.RenderContext var1) {
      } // STUB (render body lost by decompiler)

      @Override
      public void handleNow(long var1) {
      } // STUB (body lost by decompiler)
   }

   public static class FireflyParticle extends Svetych.AbstractParticle {
      public double doubleVal;
      public double double2;
      public double double3;

      public FireflyParticle(Vec3d pos, float height, double maxRange) {
         super(pos, height, maxRange);
         this.runRandom();
      }

      public void runRandom() {
         this.doubleVal = this.doubleVal + (Math.random() - 0.5) * 3.0;
         this.double2 = this.miewd + (Math.random() - 0.5) * 3.0;
         this.double3 = this.double2 + (Math.random() - 0.5) * 3.0;
      }


      @Override
      public void handleRenderContext(Svetych.RenderContext var1) {
      } // STUB (render body lost by decompiler)

      @Override
      public void handleNow(long var1) {
      } // STUB (body lost by decompiler)
   }

   public static class LanternParticle extends Svetych.AbstractParticle {
      public LanternParticle(Vec3d pos, float height, double maxRange) {
         super(pos, height, maxRange);
      }

      public void handleDegrees(MatrixStack m, Vec3d cam, long now) {
         m.translate(this.doubleVal - cam.x, this.miewd - cam.y, this.double2 - cam.z);
         m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(now % 9000L) / 9000.0F * 360.0F + this.floatVal));
         m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)Math.sin((double)now / 400.0 + (double)this.floatVal) * 8.0F));
         m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)Math.cos((double)now / 500.0 + (double)this.floatVal) * 8.0F));
      }


      @Override
      public void handleRenderContext(Svetych.RenderContext var1) {
      } // STUB (render body lost by decompiler)

      @Override
      public void handleNow(long var1) {
      } // STUB (body lost by decompiler)
   }

   private static class ParticleFactory {
      public ParticleFactory() {
      }

      public static Svetych.AbstractParticle computeAbstractParticle(String type, Vec3d pos, float height, double maxRange) {
         return (Svetych.AbstractParticle)(switch (type) {
            case "Lanterns" -> new Svetych.LanternParticle(pos, height, maxRange);
            case "Crystals" -> new Svetych.CrystalParticle(pos, height, maxRange);
            case "Fireflies" -> new Svetych.FireflyParticle(pos, height, maxRange);
            default -> new Svetych.CubeParticle(pos, height, maxRange);
         });
      }
   }

   public static class RenderContext {
      public BufferBuilder quads;
      public BufferBuilder lines;
      public BufferBuilder glowC;
      public BufferBuilder glowG;
      public boolean flag = false;
      public boolean flag2 = false;
      public boolean flag3 = false;

      public RenderContext(BufferBuilder quads, BufferBuilder lines, BufferBuilder glowC, BufferBuilder glowG) {
         this.quads = quads;
         this.lines = lines;
         this.glowC = glowC;
         this.glowG = glowG;
      }

      public void runGlow() {
         this.quads = null;
         this.lines = null;
         this.glowC = null;
         this.glowG = null;
         this.flag = false;
         this.flag2 = false;
         this.flag3 = false;
      }
   }

   private static class ShapeRenderer {
      public static final Identifier class_2960 = InputHelper.getImage("masks/dashbloom");
      public static final Identifier mrulx = InputHelper.getImage("masks/dashbloomsample");
      public static final Matrix4f mapnuq = new Matrix4f();

      public ShapeRenderer() {
      }

      public static void handleBlue(BufferBuilder buffer, Matrix4f mat, Color color, float size) {
         int var4 = color.getRed();
         int var5 = color.getGreen();
         int var6 = color.getBlue();
         int var7 = color.getAlpha();
         float var8 = size / 2.0F;
         float var9 = mat.m30();
         float var10 = mat.m31();
         float var11 = mat.m32();
         buffer.vertex(mapnuq, var9 - var8, var10 - var8, var11).texture(0.0F, 1.0F).color(var4, var5, var6, var7);
         buffer.vertex(mapnuq, var9 + var8, var10 - var8, var11).texture(1.0F, 1.0F).color(var4, var5, var6, var7);
         buffer.vertex(mapnuq, var9 + var8, var10 + var8, var11).texture(1.0F, 0.0F).color(var4, var5, var6, var7);
         buffer.vertex(mapnuq, var9 - var8, var10 + var8, var11).texture(0.0F, 0.0F).color(var4, var5, var6, var7);
      }

      public static void handleVal(BufferBuilder quads, Matrix4f m, Color paperColor, float w, float h) {
         float var5 = w / 2.0F;
         float var6 = h / 2.0F;
         handleBlue(quads, m, paperColor, -var5, var6, -var5, -var5, var6, var5, var5, var6, var5, var5, var6, -var5);
         handleBlue(quads, m, paperColor, var5, var6, -var5, var5, -var6, -var5, -var5, -var6, -var5, -var5, var6, -var5);
         handleBlue(quads, m, paperColor, -var5, var6, var5, -var5, -var6, var5, var5, -var6, var5, var5, var6, var5);
         handleBlue(quads, m, paperColor, -var5, var6, -var5, -var5, -var6, -var5, -var5, -var6, var5, -var5, var6, var5);
         handleBlue(quads, m, paperColor, var5, var6, var5, var5, -var6, var5, var5, -var6, -var5, var5, var6, -var5);
      }

      public static void handleVal2(BufferBuilder lines, Matrix4f m, Color frameColor, float w, float h) {
         float var5 = w / 2.0F;
         float var6 = h / 2.0F;
         handleBlue(lines, m, -var5, var6, -var5, var5, var6, -var5, frameColor);
         handleBlue(lines, m, var5, var6, -var5, var5, var6, var5, frameColor);
         handleBlue(lines, m, var5, var6, var5, -var5, var6, var5, frameColor);
         handleBlue(lines, m, -var5, var6, var5, -var5, var6, -var5, frameColor);
         handleBlue(lines, m, -var5, -var6, -var5, var5, -var6, -var5, frameColor);
         handleBlue(lines, m, var5, -var6, -var5, var5, -var6, var5, frameColor);
         handleBlue(lines, m, var5, -var6, var5, -var5, -var6, var5, frameColor);
         handleBlue(lines, m, -var5, -var6, var5, -var5, -var6, -var5, frameColor);
         handleBlue(lines, m, -var5, -var6, -var5, -var5, var6, -var5, frameColor);
         handleBlue(lines, m, var5, -var6, -var5, var5, var6, -var5, frameColor);
         handleBlue(lines, m, var5, -var6, var5, var5, var6, var5, frameColor);
         handleBlue(lines, m, -var5, -var6, var5, -var5, var6, var5, frameColor);
         handleBlue(lines, m, -var5, -var6, 0.0F, var5, -var6, 0.0F, frameColor);
         handleBlue(lines, m, 0.0F, -var6, -var5, 0.0F, -var6, var5, frameColor);
      }

      public static void handleVal3(BufferBuilder quads, Matrix4f m, Color fillColor, float w, float h) {
         float var5 = w / 2.0F;
         float var6 = h / 2.0F;
         handleVal(quads, m, fillColor, 0.0F, var6, 0.0F, 0.0F, 0.0F, -var5, var5, 0.0F, 0.0F);
         handleVal(quads, m, fillColor, 0.0F, var6, 0.0F, var5, 0.0F, 0.0F, 0.0F, 0.0F, var5);
         handleVal(quads, m, fillColor, 0.0F, var6, 0.0F, 0.0F, 0.0F, var5, -var5, 0.0F, 0.0F);
         handleVal(quads, m, fillColor, 0.0F, var6, 0.0F, -var5, 0.0F, 0.0F, 0.0F, 0.0F, -var5);
         handleVal(quads, m, fillColor, 0.0F, -var6, 0.0F, var5, 0.0F, 0.0F, 0.0F, 0.0F, -var5);
         handleVal(quads, m, fillColor, 0.0F, -var6, 0.0F, 0.0F, 0.0F, var5, var5, 0.0F, 0.0F);
         handleVal(quads, m, fillColor, 0.0F, -var6, 0.0F, -var5, 0.0F, 0.0F, 0.0F, 0.0F, var5);
         handleVal(quads, m, fillColor, 0.0F, -var6, 0.0F, 0.0F, 0.0F, -var5, -var5, 0.0F, 0.0F);
      }

      public static void handleVal4(BufferBuilder lines, Matrix4f m, Color lineColor, float w, float h) {
         float var5 = w / 2.0F;
         float var6 = h / 2.0F;
         handleBlue(lines, m, 0.0F, var6, 0.0F, 0.0F, 0.0F, -var5, lineColor);
         handleBlue(lines, m, 0.0F, var6, 0.0F, var5, 0.0F, 0.0F, lineColor);
         handleBlue(lines, m, 0.0F, var6, 0.0F, 0.0F, 0.0F, var5, lineColor);
         handleBlue(lines, m, 0.0F, var6, 0.0F, -var5, 0.0F, 0.0F, lineColor);
         handleBlue(lines, m, 0.0F, -var6, 0.0F, 0.0F, 0.0F, -var5, lineColor);
         handleBlue(lines, m, 0.0F, -var6, 0.0F, var5, 0.0F, 0.0F, lineColor);
         handleBlue(lines, m, 0.0F, -var6, 0.0F, 0.0F, 0.0F, var5, lineColor);
         handleBlue(lines, m, 0.0F, -var6, 0.0F, -var5, 0.0F, 0.0F, lineColor);
         handleBlue(lines, m, 0.0F, 0.0F, -var5, var5, 0.0F, 0.0F, lineColor);
         handleBlue(lines, m, var5, 0.0F, 0.0F, 0.0F, 0.0F, var5, lineColor);
         handleBlue(lines, m, 0.0F, 0.0F, var5, -var5, 0.0F, 0.0F, lineColor);
         handleBlue(lines, m, -var5, 0.0F, 0.0F, 0.0F, 0.0F, -var5, lineColor);
      }

      public static void handleVal(BufferBuilder buffer, Matrix4f m, Color color, float size) {
         float var4 = size / 2.0F;
         handleBlue(buffer, m, color, -var4, var4, -var4, -var4, var4, var4, var4, var4, var4, var4, var4, -var4);
         handleBlue(buffer, m, color, -var4, -var4, -var4, var4, -var4, -var4, var4, -var4, var4, -var4, -var4, var4);
         handleBlue(buffer, m, color, -var4, var4, var4, -var4, -var4, var4, var4, -var4, var4, var4, var4, var4);
         handleBlue(buffer, m, color, -var4, var4, -var4, var4, var4, -var4, var4, -var4, -var4, -var4, -var4, -var4);
         handleBlue(buffer, m, color, -var4, var4, -var4, -var4, -var4, -var4, -var4, -var4, var4, -var4, var4, var4);
         handleBlue(buffer, m, color, var4, var4, -var4, var4, var4, var4, var4, -var4, var4, var4, -var4, -var4);
      }

      public static void handleVal2(BufferBuilder buffer, Matrix4f m, Color color, float size) {
         float var4 = size / 2.0F;
         handleBlue(buffer, m, -var4, -var4, -var4, var4, -var4, -var4, color);
         handleBlue(buffer, m, var4, -var4, -var4, var4, -var4, var4, color);
         handleBlue(buffer, m, var4, -var4, var4, -var4, -var4, var4, color);
         handleBlue(buffer, m, -var4, -var4, var4, -var4, -var4, -var4, color);
         handleBlue(buffer, m, -var4, var4, -var4, var4, var4, -var4, color);
         handleBlue(buffer, m, var4, var4, -var4, var4, var4, var4, color);
         handleBlue(buffer, m, var4, var4, var4, -var4, var4, var4, color);
         handleBlue(buffer, m, -var4, var4, var4, -var4, var4, -var4, color);
         handleBlue(buffer, m, -var4, -var4, -var4, -var4, var4, -var4, color);
         handleBlue(buffer, m, var4, -var4, -var4, var4, var4, -var4, color);
         handleBlue(buffer, m, var4, -var4, var4, var4, var4, var4, color);
         handleBlue(buffer, m, -var4, -var4, var4, -var4, var4, var4, color);
      }

      public static void handleBlue(
         BufferBuilder buffer,
         Matrix4f m,
         Color color,
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
         float z4
      ) {
         int var15 = color.getRed();
         int var16 = color.getGreen();
         int var17 = color.getBlue();
         int var18 = color.getAlpha();
         buffer.vertex(m, x1, y1, z1).color(var15, var16, var17, var18);
         buffer.vertex(m, x2, y2, z2).color(var15, var16, var17, var18);
         buffer.vertex(m, x3, y3, z3).color(var15, var16, var17, var18);
         buffer.vertex(m, x4, y4, z4).color(var15, var16, var17, var18);
      }

      public static void handleVal(
         BufferBuilder buffer, Matrix4f m, Color color, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3
      ) {
         handleBlue(buffer, m, color, x1, y1, z1, x2, y2, z2, x3, y3, z3, x3, y3, z3);
      }

      public static void handleBlue(BufferBuilder buffer, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, Color color) {
         int var9 = color.getRed();
         int var10 = color.getGreen();
         int var11 = color.getBlue();
         int var12 = color.getAlpha();
         buffer.vertex(m, x1, y1, z1).color(var9, var10, var11, var12).normal(0.0F, 1.0F, 0.0F);
         buffer.vertex(m, x2, y2, z2).color(var9, var10, var11, var12).normal(0.0F, 1.0F, 0.0F);
      }
   }
}
