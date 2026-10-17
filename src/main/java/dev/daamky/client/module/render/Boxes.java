package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.mixin.WorldRendererAccessor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Boxes",
   category = ModuleCategory.VISUALS,
   description = "Отображает 2D/3D уголки, боксы и полоску здоровья сущностей сквозь стены (ESP)"
)
public class Boxes extends Module {
   public static final Boxes boxes = new Boxes();
   public final ModeSetting both = new ModeSetting("Style").value("Corners").values("Corners", "Box", "Corner+Fill", "Fill", "Both");
   public final ModeSetting shader = new ModeSetting("Color Mode").value("Theme").values("Theme", "Custom", "Health", "Shader");
   public final BooleanSetting healthBar = new BooleanSetting("Health Bar").value(true);
   public final BooleanSetting self = new BooleanSetting("Self").value(true);
   public final BooleanSetting mobs = new BooleanSetting("Mobs").value(true);
   public final FloatSetting cornerSize = new FloatSetting("Corner Size").value(0.28F).range(0.15F, 0.5F).step(0.01F).visibleWhen(this::lambda$new$0);
   public final FloatSetting lineWidth = new FloatSetting("Line Width").value(1.8F).range(0.5F, 4.0F).step(0.1F);
   public final FloatSetting fillAlpha = new FloatSetting("Fill Alpha").value(0.22F).range(0.01F, 1.0F).step(0.01F).visibleWhen(this::lambda$new$1);
   public final Supplier<Boolean> supplier = this::lambda$new$2;
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(new Color(190, 80, 255)).computeVisible(this.supplier);
   public final Supplier<Boolean> supplier2 = this::lambda$new$3;
   public final ColorSetting color1 = new ColorSetting("Color 1").computeColorSetting(new Color(80, 160, 255)).computeVisible(this.supplier2);
   public final ColorSetting color2 = new ColorSetting("Color 2").computeColorSetting(new Color(200, 60, 255)).computeVisible(this.supplier2);
   public final FloatSetting flowSpeed = new FloatSetting("Flow Speed").value(1.0F).range(0.2F, 3.0F).step(0.1F).visibleWhen(this.supplier2);
   public final FloatSetting pulse = new FloatSetting("Pulse").value(0.3F).range(0.0F, 0.8F).step(0.05F).visibleWhen(this.supplier2);
   public final FloatSetting intensity = new FloatSetting("Intensity").value(0.7F).range(0.2F, 1.0F).step(0.05F).visibleWhen(this.supplier2);
   public long longVal = System.currentTimeMillis();
   public volatile List<Entity> mcizre = new ArrayList<>();

   public Boxes() {
      this.addSettings(
         new Setting[]{
            this.both,
            this.shader,
            this.healthBar,
            this.self,
            this.mobs,
            this.cornerSize,
            this.lineWidth,
            this.fillAlpha,
            this.color,
            this.color1,
            this.color2,
            this.flowSpeed,
            this.pulse,
            this.intensity
         }
      );
   }

   public static Boxes getInstance() {
      return boxes;
   }

   @Override
   public void onEnable() {
      this.longVal = System.currentTimeMillis();
      this.mcizre = new ArrayList<>();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$5));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$6));
      this.addEventListeners(new Subscription[]{var2, var1});
   }

   public void handleVal(
      BufferBuilder buf, Matrix4f mat, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float lenFraction, int r, int g, int b, int a
   ) {
      float var14 = (maxX - minX) * lenFraction;
      float var15 = (maxY - minY) * lenFraction;
      float var16 = (maxZ - minZ) * lenFraction;
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX + var14, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY + var15, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ + var16).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX - var14, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY + var15, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ + var16).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX + var14, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY + var15, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ - var16).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX - var14, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY + var15, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ - var16).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX + var14, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY - var15, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ + var16).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX - var14, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY - var15, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ + var16).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX + var14, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY - var15, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ - var16).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX - var14, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY - var15, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ - var16).color(r, g, b, a);
   }

   public void handleVal(BufferBuilder buf, Matrix4f mat, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int r, int g, int b, int a) {
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
   }

   public void handleVal2(BufferBuilder buf, Matrix4f mat, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int r, int g, int b, int a) {
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, minZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, minZ).color(r, g, b, a);
      buf.vertex(mat, maxX, minY, maxZ).color(r, g, b, a);
      buf.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, maxY, maxZ).color(r, g, b, a);
      buf.vertex(mat, minX, minY, maxZ).color(r, g, b, a);
   }

   public void lambda$onEvent$6(Render3DEvent.Render3DEventData event) {
      if (mc.world != null && mc.player != null && !this.mcizre.isEmpty()) {
         boolean var2 = this.both.is("Corners") || this.both.is("Corner+Fill");
         boolean var3 = this.both.is("Box") || this.both.is("Both");
         boolean var4 = this.both.is("Fill") || this.both.is("Both") || this.both.is("Corner+Fill");
         boolean var5 = this.shader.is("Shader");
         boolean var6 = this.healthBar.getValue();
         Frustum var7 = null;
         if (mc.worldRenderer instanceof WorldRendererAccessor) {
            var7 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
         }

         ArrayList var8 = new ArrayList();
         float var9 = mc.getRenderTickCounter().getTickDelta(true);
         Vec3d var10 = mc.gameRenderer.getCamera().getPos();

         for (Entity var12 : this.mcizre) {
            Box var13 = var12.getBoundingBox();
            if (var7 == null || var7.isVisible(var13.expand(0.5))) {
               double var14 = MathHelper.lerp((double)var9, var12.prevX, var12.getX());
               double var16 = MathHelper.lerp((double)var9, var12.prevY, var12.getY());
               double var18 = MathHelper.lerp((double)var9, var12.prevZ, var12.getZ());
               double var20 = var14 - var12.getX() - var10.x;
               double var22 = var16 - var12.getY() - var10.y;
               double var24 = var18 - var12.getZ() - var10.z;
               float var26 = (float)(var13.minX + var20);
               float var27 = (float)(var13.minY + var22);
               float var28 = (float)(var13.minZ + var24);
               float var29 = (float)(var13.maxX + var20);
               float var30 = (float)(var13.maxY + var22);
               float var31 = (float)(var13.maxZ + var24);
               float var32 = 1.0F;
               if (var12 instanceof LivingEntity var33) {
                  var32 = MathHelper.clamp(var33.getHealth() / var33.getMaxHealth(), 0.0F, 1.0F);
               }

               Color var66;
               if (this.shader.is("Theme")) {
                  var66 = ThemeColor.getColor4();
               } else if (this.shader.is("Health")) {
                  var66 = ColorMath.initBlue(new Color(239, 68, 68), new Color(34, 197, 94), var32);
               } else if (this.shader.is("Shader")) {
                  var66 = Color.WHITE;
               } else {
                  var66 = this.color.getValue();
               }

               var8.add(new Boxes.RenderEntityData(var26, var27, var28, var29, var30, var31, var32, var66));
            }
         }

         if (!var8.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            RenderSystem.lineWidth(this.lineWidth.getValue());
            if (var5) {
               ShaderProgram var37 = RenderSystem.setShader(Chams.class_10156);
               if (var37 != null) {
                  float var39 = (float)mc.getWindow().getWidth();
                  float var41 = (float)mc.getWindow().getHeight();
                  if (var37.getUniform("uScreenSize") != null) {
                     var37.getUniform("uScreenSize").set(var39, var41);
                  }

                  if (var37.getUniform("uTime") != null) {
                     var37.getUniform("uTime").set((float)(System.currentTimeMillis() - this.longVal) / 1000.0F);
                  }

                  Color var45 = this.color1.getValue();
                  Color var15 = this.color2.getValue();
                  if (var37.getUniform("uColor1") != null) {
                     var37.getUniform("uColor1")
                        .set((float)var45.getRed() / 255.0F, (float)var45.getGreen() / 255.0F, (float)var45.getBlue() / 255.0F);
                  }

                  if (var37.getUniform("uColor2") != null) {
                     var37.getUniform("uColor2")
                        .set((float)var15.getRed() / 255.0F, (float)var15.getGreen() / 255.0F, (float)var15.getBlue() / 255.0F);
                  }

                  if (var37.getUniform("uFlowSpeed") != null) {
                     var37.getUniform("uFlowSpeed").set(this.flowSpeed.getValue());
                  }

                  if (var37.getUniform("uPulse") != null) {
                     var37.getUniform("uPulse").set(this.pulse.getValue());
                  }

                  if (var37.getUniform("uIntensity") != null) {
                     var37.getUniform("uIntensity").set(this.intensity.getValue());
                  }
               }
            } else {
               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            }

            MatrixStack var38 = event.getMatrixStack();
            var38.push();
            Matrix4f var40 = var38.peek().getPositionMatrix();
            if (var4) {
               float var42 = this.fillAlpha.getValue();
               BufferBuilder var46 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

               for (Boxes.RenderEntityData var55 : (Iterable<Boxes.RenderEntityData>)(Iterable)var8) {
                  int var17 = (int)((float)var55.color.getAlpha() * var42);
                  this.handleVal2(
                     var46,
                     var40,
                     var55.minX,
                     var55.minY,
                     var55.minZ,
                     var55.maxX,
                     var55.maxY,
                     var55.maxZ,
                     var55.color.getRed(),
                     var55.color.getGreen(),
                     var55.color.getBlue(),
                     var17
                  );
               }

               try {
                  BuiltBuffer var51 = var46.end();
                  if (var51 != null) {
                     BufferRenderer.drawWithGlobalProgram(var51);
                  }
               } catch (Exception var36) {
               }
            }

            if (var2 || var3) {
               float var43 = this.cornerSize.getValue();
               BufferBuilder var47 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

               for (Boxes.RenderEntityData var56 : (Iterable<Boxes.RenderEntityData>)(Iterable)var8) {
                  int var58 = var56.color.getRed();
                  int var60 = var56.color.getGreen();
                  int var19 = var56.color.getBlue();
                  int var63 = var5 ? 255 : var56.color.getAlpha();
                  if (var2) {
                     this.handleVal(var47, var40, var56.minX, var56.minY, var56.minZ, var56.maxX, var56.maxY, var56.maxZ, var43, var58, var60, var19, var63);
                  } else {
                     this.handleVal(var47, var40, var56.minX, var56.minY, var56.minZ, var56.maxX, var56.maxY, var56.maxZ, var58, var60, var19, var63);
                  }
               }

               try {
                  BuiltBuffer var53 = var47.end();
                  if (var53 != null) {
                     BufferRenderer.drawWithGlobalProgram(var53);
                  }
               } catch (Exception var35) {
               }
            }

            if (var6) {
               BufferBuilder var44 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

               for (Boxes.RenderEntityData var54 : (Iterable<Boxes.RenderEntityData>)(Iterable)var8) {
                  float var57 = 0.04F;
                  float var59 = 0.08F;
                  float var61 = var54.minX - var59 - var57;
                  float var62 = var54.minX - var59;
                  float var64 = var54.maxY - var54.minY;
                  float var21 = var54.minY + var64 * var54.hpPercent;
                  this.handleVal2(var44, var40, var61, var54.minY, var54.minZ, var62, var54.maxY, var54.minZ + var57, 15, 15, 20, 160);
                  Color var65 = ColorMath.initBlue(new Color(239, 68, 68), new Color(34, 197, 94), var54.hpPercent);
                  this.handleVal2(
                     var44, var40, var61, var54.minY, var54.minZ, var62, var21, var54.minZ + var57, var65.getRed(), var65.getGreen(), var65.getBlue(), 240
                  );
               }

               try {
                  BuiltBuffer var49 = var44.end();
                  if (var49 != null) {
                     BufferRenderer.drawWithGlobalProgram(var49);
                  }
               } catch (Exception var34) {
               }
            }

            RenderSystem.lineWidth(1.0F);
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            var38.pop();
         }
      }
   }

   public void lambda$onEvent$5(UpdateEvent event) {
      if (mc.world != null && mc.player != null) {
         Box var2 = mc.player.getBoundingBox().expand(64.0);
         ArrayList var3 = new ArrayList();

         for (Entity var5 : mc.world.getEntitiesByClass(LivingEntity.class, var2, Boxes::lambda$onEvent$4)) {
            if (var5 != null && var5.isAlive()) {
               boolean var6 = var5 instanceof PlayerEntity;
               boolean var7 = var5 instanceof LivingEntity && !var6;
               if (var6 ? var5 != mc.player || this.self.getValue() && !mc.options.getPerspective().isFirstPerson() : var7 && this.mobs.getValue()) {
                  double var8 = mc.player.squaredDistanceTo(var5);
                  if (!(var8 > 22500.0)) {
                     var3.add(var5);
                  }
               }
            }
         }

         this.mcizre = var3;
      }
   }

   public static boolean lambda$onEvent$4(LivingEntity e) {
      return true;
   }

   public Boolean lambda$new$3() {
      return this.shader.is("Shader");
   }

   public Boolean lambda$new$2() {
      return this.shader.is("Custom");
   }

   public Boolean lambda$new$1() {
      return this.both.is("Fill") || this.both.is("Both") || this.both.is("Corner+Fill");
   }

   public Boolean lambda$new$0() {
      return this.both.is("Corners") || this.both.is("Corner+Fill");
   }

   private static class RenderEntityData {
      public final float minX;
      public final float minY;
      public final float minZ;
      public final float maxX;
      public final float maxY;
      public final float maxZ;
      public final float hpPercent;
      public final Color color;

      public RenderEntityData(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float hpPercent, Color color) {
         this.minX = minX;
         this.minY = minY;
         this.minZ = minZ;
         this.maxX = maxX;
         this.maxY = maxY;
         this.maxZ = maxZ;
         this.hpPercent = hpPercent;
         this.color = color;
      }
   }
}
