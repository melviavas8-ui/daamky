package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.SparkHelper;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(
   name = "Trails",
   category = ModuleCategory.VISUALS,
   description = "Оставляет красивый след или призрачные силуэты за вами при движении"
)
public class Trails extends Module {
   public static final Trails trails = new Trails();
   public final ModeSetting player = new ModeSetting("Mode").value("Particles").values("Particles", "Player");
   public final ModeSetting spark = new ModeSetting("Texture").value("Spark").values(SparkHelper.stringList).visibleWhen(this::lambda$new$0);
   public final FloatSetting size = new FloatSetting("Size").value(0.15F).range(0.05F, 0.5F).step(0.01F).visibleWhen(this::lambda$new$1);
   public final FloatSetting lifeTime = new FloatSetting("Life time").value(20.0F).range(5.0F, 100.0F).step(5.0F).visibleWhen(this::lambda$new$2);
   public final FloatSetting count = new FloatSetting("Count").value(2.0F).range(1.0F, 10.0F).step(1.0F).visibleWhen(this::lambda$new$3);
   public final ModeSetting comet = new ModeSetting("Physics").value("Static").values("Static", "Drop", "Comet").visibleWhen(this::lambda$new$4);
   public final BooleanSetting rotate = new BooleanSetting("Rotate").value(true).visibleWhen(this::lambda$new$5);
   public final BooleanSetting heightSpread = new BooleanSetting("Height Spread").value(false).visibleWhen(this::lambda$new$6);
   public final BooleanSetting trail = new BooleanSetting("Trail").value(false).visibleWhen(this::lambda$new$7);
   public final ModeSetting points = new ModeSetting("Trail Mode").value("Line").values("Line", "Points").visibleWhen(this::lambda$new$8);
   public final FloatSetting trailWidth = new FloatSetting("Trail Width").value(0.05F).range(0.01F, 0.3F).step(0.01F).visibleWhen(this::lambda$new$9);
   public final FloatSetting trailLength = new FloatSetting("Trail length").value(5.0F).range(1.0F, 20.0F).step(1.0F).visibleWhen(this::lambda$new$10);
   public final ModeSetting always = new ModeSetting("Trigger").value("Jump").values("Jump", "Movement", "In Air", "Always").visibleWhen(this::lambda$new$11);
   public final ModeSetting both = new ModeSetting("Style").value("Outline").values("Outline", "Fill", "Both").visibleWhen(this::lambda$new$12);
   public final ModeSetting geometric = new ModeSetting("Shader")
      .value("Default")
      .values("Default", "Nebula", "Liquid", "Geometric")
      .visibleWhen(this::lambda$new$13);
   public final FloatSetting ghostLifetime = new FloatSetting("Ghost Lifetime")
      .value(1200.0F)
      .range(300.0F, 4000.0F)
      .step(100.0F)
      .visibleWhen(this::lambda$new$14);
   public final FloatSetting maxGhosts = new FloatSetting("Max Ghosts").value(6.0F).range(1.0F, 15.0F).step(1.0F).visibleWhen(this::lambda$new$15);
   public final FloatSetting lineWidth = new FloatSetting("Line Width").value(2.0F).range(0.5F, 5.0F).step(0.1F).visibleWhen(this::lambda$new$16);
   public final FloatSetting fillAlpha = new FloatSetting("Fill Alpha").value(0.35F).range(0.05F, 1.0F).step(0.05F).visibleWhen(this::lambda$new$17);
   public final BooleanSetting additiveGlow = new BooleanSetting("Additive Glow").value(true).visibleWhen(this::lambda$new$18);
   public final BooleanSetting scaleExpand = new BooleanSetting("Scale Expand").value(false).visibleWhen(this::lambda$new$19);
   public final ColorSetting fillColor = new ColorSetting("Fill Color").computeColorSetting(new Color(120, 180, 255, 100)).computeVisible(this::lambda$new$20);
   public final ColorSetting outlineColor = new ColorSetting("Outline Color")
      .computeColorSetting(new Color(80, 160, 255, 255))
      .computeVisible(this::lambda$new$21);
   public final Supplier<Boolean> supplier = this::lambda$new$22;
   public final ColorSetting color1 = new ColorSetting("Color 1").computeColorSetting(new Color(80, 160, 255)).computeVisible(this.supplier);
   public final ColorSetting color2 = new ColorSetting("Color 2").computeColorSetting(new Color(200, 60, 255)).computeVisible(this.supplier);
   public final FloatSetting flowSpeed = new FloatSetting("Flow Speed").value(1.0F).range(0.2F, 3.0F).step(0.1F).visibleWhen(this.supplier);
   public final FloatSetting pulse = new FloatSetting("Pulse").value(0.3F).range(0.0F, 0.8F).step(0.05F).visibleWhen(this.supplier);
   public final FloatSetting intensity = new FloatSetting("Intensity").value(0.7F).range(0.2F, 1.0F).step(0.05F).visibleWhen(this.supplier);
   public final BooleanSetting inFirstPerson = new BooleanSetting("In first person").value(true);
   public final List<Trails.TrailParticle> list = new ArrayList<>();
   public final Map<Identifier, List<Trails.TrailParticle>> map = new HashMap<>();
   public final List<Trails.PlayerGhost> list2 = new ArrayList<>();
   public static final Trails.GhostVertexConsumer mvueiu = new Trails.GhostVertexConsumer();
   public final Vector3f vector3f = new Vector3f();
   public final Vector3f vector3f2 = new Vector3f();
   public final Matrix4f matrix4f = new Matrix4f();
   public final Matrix4f matrix4f2 = new Matrix4f();
   public long longVal = System.currentTimeMillis();
   public boolean flag = true;
   public double doubleVal = 0.0;
   public double double2 = 0.0;
   public double double3 = 0.0;

   public Trails() {
      this.addSettings(
         new Setting[]{
            this.player,
            this.spark,
            this.size,
            this.lifeTime,
            this.count,
            this.comet,
            this.rotate,
            this.heightSpread,
            this.trail,
            this.points,
            this.trailWidth,
            this.trailLength,
            this.always,
            this.both,
            this.geometric,
            this.ghostLifetime,
            this.maxGhosts,
            this.lineWidth,
            this.fillAlpha,
            this.additiveGlow,
            this.scaleExpand,
            this.fillColor,
            this.outlineColor,
            this.color1,
            this.color2,
            this.flowSpeed,
            this.pulse,
            this.intensity,
            this.inFirstPerson
         }
      );
   }

   @Override
   public void onEnable() {
      this.list.clear();
      this.list2.clear();
      this.longVal = System.currentTimeMillis();
      this.flag = true;
      if (mc.player != null) {
         this.doubleVal = mc.player.getX();
         this.double2 = mc.player.getY();
         this.double3 = mc.player.getZ();
      }
   }

   @Override
   public void onDisable() {
      this.list.clear();
      this.list2.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$23));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$24));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void initSize(boolean isFirstPerson) {
      this.list.removeIf(SparkHelper::checkComet);
      if (this.list.size() > 350) {
         this.list.subList(0, this.list.size() - 350).clear();
      }

      int var2 = this.count.getValue().intValue();
      float var3 = this.size.getValue();
      int var4 = this.lifeTime.getValue().intValue();
      String var5 = this.comet.getValue();
      boolean var6 = this.rotate.getValue();
      boolean var7 = this.trail.getValue();
      int var8 = this.trailLength.getValue().intValue();
      Identifier var9 = SparkHelper.computeClass_2960(this.spark.getValue());

      for (int var10 = 0; var10 < var2; var10++) {
         float var11 = (float)(mc.player.getX() + (double)EntityHelper.computeRandom(-0.1F, 0.1F));
         float var12 = this.heightSpread.getValue()
            ? (float)(
               mc.player.getY()
                  + (double)EntityHelper.computeRandom(0.0F, mc.player.getHeight())
                  + (double)EntityHelper.computeRandom(-0.05F, 0.05F)
            )
            : (float)(
               mc.player.getY()
                  + (double)(mc.player.getHeight() * (isFirstPerson ? 0.2F : 0.4F))
                  + (double)EntityHelper.computeRandom(-0.1F, 0.1F)
            );
         float var13 = (float)(mc.player.getZ() + (double)EntityHelper.computeRandom(-0.1F, 0.1F));
         float var14 = EntityHelper.computeRandom(-0.015F, 0.015F);
         float var15 = EntityHelper.computeRandom(-0.005F, 0.015F);
         float var16 = EntityHelper.computeRandom(-0.015F, 0.015F);
         this.list.add(new Trails.TrailParticle(var11, var12, var13, var14, var15, var16, this.list.size(), var9, var3, var6, var4, var7, var8, var5));
      }
   }

   public void runEquals() {
      boolean var1 = mc.player.isOnGround();
      boolean var2 = this.flag && !var1 && mc.player.getVelocity().y > 0.0;
      boolean var3 = mc.options.jumpKey.isPressed() || mc.player.input != null && mc.player.input.playerInput.jump();
      boolean var4 = false;
      String var5 = this.always.getValue();
      if ("Jump".equals(var5)) {
         if (var2 || var3 && !var1 && mc.player.fallDistance < 0.25F && this.flag) {
            var4 = true;
         }
      } else if ("In Air".equals(var5)) {
         if (!var1 && mc.player.age % 3 == 0) {
            var4 = true;
         }
      } else if ("Movement".equals(var5)) {
         double var6 = mc.player.squaredDistanceTo(this.doubleVal, this.double2, this.double3);
         if (var6 > 1.44) {
            var4 = true;
         }
      } else if ("Always".equals(var5)) {
         double var8 = mc.player.squaredDistanceTo(this.doubleVal, this.double2, this.double3);
         if (var8 > 0.64) {
            var4 = true;
         }
      }

      if (var4) {
         this.initAdd();
         this.doubleVal = mc.player.getX();
         this.double2 = mc.player.getY();
         this.double3 = mc.player.getZ();
      }

      this.flag = var1;
   }

   public void initAdd() {
      ClientPlayerEntity var1 = mc.player;
      if (var1 != null && mc.getEntityRenderDispatcher() != null) {
         if (mc.getEntityRenderDispatcher().getRenderer(var1) instanceof LivingEntityRenderer var3) {
            EntityModel var4 = var3.getModel();
            Trails.ModelPose var5 = null;
            if (var4 instanceof BipedEntityModel var6) {
               var5 = Trails.ModelPose.initInit(var6);
            }

            int var9 = this.maxGhosts.getValue().intValue();

            while (this.list2.size() >= var9 && !this.list2.isEmpty()) {
               this.list2.remove(0);
            }

            long var7 = (long)this.ghostLifetime.getValue().floatValue();
            this.list2
               .add(
                  new Trails.PlayerGhost(
                     var1.getX(), var1.getY(), var1.getZ(), var1.getYaw(), var1.getPitch(), var1.bodyYaw, var7, var5, var4
                  )
               );
         }
      }
   }

   public void handleEquals(MatrixStack matrices, Camera camera) {
      if (!this.list2.isEmpty()) {
         double var3 = camera.getPos().x;
         double var5 = camera.getPos().y;
         double var7 = camera.getPos().z;
         long var9 = System.currentTimeMillis();
         boolean var11 = this.additiveGlow.getValue();
         boolean var12 = this.scaleExpand.getValue();
         String var13 = this.both.getValue();
         boolean var14 = "Outline".equals(var13) || "Both".equals(var13);
         boolean var15 = "Fill".equals(var13) || "Both".equals(var13);
         String var16 = this.geometric.getValue();
         float var17 = this.lineWidth.getValue();
         float var18 = this.fillAlpha.getValue();
         Iterator var19 = this.list2.iterator();

         while (var19.hasNext()) {
            Trails.PlayerGhost var20 = (Trails.PlayerGhost)var19.next();
            long var21 = var9 - var20.longVal;
            if (var21 >= var20.lifetime) {
               var19.remove();
            } else {
               double var23 = var20.x - var3;
               double var25 = var20.y - var5;
               double var27 = var20.z - var7;
               double var29 = var23 * var23 + var25 * var25 + var27 * var27;
               if (!(var29 < 0.04) && !(var29 > 4096.0)) {
                  float var31 = (float)var21 / (float)var20.lifetime;
                  float var32 = (float)Math.pow((double)(1.0F - var31), 1.35F);
                  if (!(var32 <= 0.01F)) {
                     matrices.push();
                     matrices.translate(var23, var25, var27);
                     matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - var20.bodyYaw));
                     matrices.scale(-1.0F, -1.0F, 1.0F);
                     if (var12) {
                        float var33 = 1.0F + 0.06F * var31;
                        matrices.scale(var33, var33, var33);
                     }

                     matrices.translate(0.0F, -1.501F, 0.0F);
                     Trails.ModelPose var37 = null;
                     BipedEntityModel var34 = null;
                     EntityModel var36 = var20.model;
                     if (var36 instanceof BipedEntityModel) {
                        BipedEntityModel var35 = (BipedEntityModel)var36;
                        var34 = var35;
                        var37 = Trails.ModelPose.initInit(var35);
                        if (var20.pose != null) {
                           var20.pose.handleClass_572(var35);
                        }
                     }

                     if (var15) {
                        this.handleColor(matrices, var20.model, false, 1.0F, var32 * var18, var11, var16);
                     }

                     if (var14) {
                        this.handleColor(matrices, var20.model, true, var17, var32, var11, var16);
                     }

                     if (var34 != null && var37 != null) {
                        var37.handleClass_572(var34);
                     }

                     matrices.pop();
                  }
               }
            }
         }
      }
   }

   public void handleColor(MatrixStack matrices, EntityModel model, boolean isLine, float lineWidth, float alphaMult, boolean additive, String shader) {
      ShaderProgramKey var8 = null;
      if ("Nebula".equals(shader)) {
         var8 = Chams.class_10156;
      } else if ("Liquid".equals(shader)) {
         var8 = BlockTweaks.class_10156;
      } else if ("Geometric".equals(shader)) {
         var8 = BlockTweaks.class_101562;
      }

      boolean var9 = var8 != null;
      int var10;
      if (var9) {
         int var11 = (int)(255.0F * MathHelper.clamp(alphaMult, 0.0F, 1.0F));
         var10 = var11 << 24 | 16777215;
      } else {
         Color var18 = isLine ? this.outlineColor.getValue() : this.fillColor.getValue();
         int var12 = (int)((float)var18.getAlpha() * MathHelper.clamp(alphaMult, 0.0F, 1.0F));
         var10 = ColorMath.computeMin(var18.getRGB(), var12);
      }

      BufferBuilder var19 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      mvueiu.setDelegate(var19);
      model.render(matrices, mvueiu, 15728880, OverlayTexture.DEFAULT_UV, var10);
      BuiltBuffer var20 = var19.end();
      if (var20 != null) {
         RenderSystem.depthMask(false);
         RenderSystem.enableDepthTest();
         RenderSystem.enableBlend();
         if (additive) {
            RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
         } else {
            RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
         }

         RenderSystem.disableCull();
         if (var9) {
            ShaderProgram var13 = RenderSystem.setShader(var8);
            if (var13 != null) {
               float var14 = (float)mc.getWindow().getWidth();
               float var15 = (float)mc.getWindow().getHeight();
               if (var13.getUniform("uScreenSize") != null) {
                  var13.getUniform("uScreenSize").set(var14, var15);
               }

               if (var13.getUniform("uTime") != null) {
                  var13.getUniform("uTime").set((float)(System.currentTimeMillis() - this.longVal) / 1000.0F);
               }

               Color var16 = this.color1.getValue();
               Color var17 = this.color2.getValue();
               if (var13.getUniform("uColor1") != null) {
                  var13.getUniform("uColor1").set((float)var16.getRed() / 255.0F, (float)var16.getGreen() / 255.0F, (float)var16.getBlue() / 255.0F);
               }

               if (var13.getUniform("uColor2") != null) {
                  var13.getUniform("uColor2").set((float)var17.getRed() / 255.0F, (float)var17.getGreen() / 255.0F, (float)var17.getBlue() / 255.0F);
               }

               if (var13.getUniform("uFlowSpeed") != null) {
                  var13.getUniform("uFlowSpeed").set(this.flowSpeed.getValue());
               }

               if (var13.getUniform("uPulse") != null) {
                  var13.getUniform("uPulse").set(this.pulse.getValue());
               }

               if (var13.getUniform("uIntensity") != null) {
                  var13.getUniform("uIntensity").set(this.intensity.getValue());
               }
            }
         } else {
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         }

         if (isLine) {
            GL11.glPolygonMode(1032, 6913);
            RenderSystem.lineWidth(lineWidth);
         } else {
            GL11.glPolygonMode(1032, 6914);
         }

         BufferRenderer.drawWithGlobalProgram(var20);
         if (isLine) {
            GL11.glPolygonMode(1032, 6914);
            RenderSystem.lineWidth(1.0F);
         }

         RenderSystem.enableCull();
         RenderSystem.defaultBlendFunc();
         RenderSystem.depthMask(true);
      }
   }

   public void renderParticles(MatrixStack matrixStack, Camera camera) {
      if (!this.list.isEmpty()) {
         for (Trails.TrailParticle var4 : this.list) {
            var4.runSpawn();
         }

         if (this.trail.getValue() && this.points.is("Line")) {
            Render.renderEffect.handleBlend(matrixStack);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder var99 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            Vec3d var101 = mc.getEntityRenderDispatcher().camera.getPos();
            Matrix4f var5 = matrixStack.peek().getPositionMatrix();
            boolean var6 = false;
            float var7 = this.trailWidth.getValue();

            for (Trails.TrailParticle var9 : this.list) {
               int var10;
               if (var9.isTrail() && var9.getDeque().size() > 1 && (var10 = var9.getInt()) > 5) {
                  int var11 = ThemeColor.computeCurrent(var9.getIndex() * 90, var10);
                  int var12 = var11 >> 16 & 0xFF;
                  int var13 = var11 >> 8 & 0xFF;
                  int var14 = var11 & 0xFF;
                  int var15 = var11 >>> 24;
                  double var16 = (double)EntityHelper.computeFloat((double)var9.getFloat(), (double)var9.getX());
                  double var18 = (double)EntityHelper.computeFloat((double)var9.getY(), (double)var9.getFloat2());
                  double var20 = (double)EntityHelper.computeFloat((double)var9.getZ(), (double)var9.getFloat3());
                  double var22 = -9999.0;
                  double var24 = -9999.0;
                  double var26 = -9999.0;
                  double var28 = 0.0;
                  double var30 = 0.0;
                  double var32 = 0.0;
                  int var34 = 0;
                  double var35 = camera.getPos().x;
                  double var37 = camera.getPos().y;
                  double var39 = camera.getPos().z;

                  for (Vec3d var42 : var9.getDeque()) {
                     float var43 = (float)var34 / (float)var9.getDeque().size();
                     double var44 = EntityHelper.computeDouble(var42.x, var16, (double)var43);
                     double var46 = EntityHelper.computeDouble(var42.y, var18, (double)var43);
                     double var48 = EntityHelper.computeDouble(var42.z, var20, (double)var43);
                     float var50 = var7 * (1.0F - var43);
                     double var51 = (double)var50;
                     double var53 = 0.0;
                     double var55 = 0.0;
                     if (var22 != -9999.0) {
                        double var57 = var44 - var22;
                        double var59 = var46 - var24;
                        double var61 = var48 - var26;
                        double var63 = var57 * var57 + var59 * var59 + var61 * var61;
                        if (var63 > 1.0E-4) {
                           double var65 = Math.sqrt(var63);
                           double var67 = var57 / var65;
                           double var69 = var59 / var65;
                           double var71 = var61 / var65;
                           double var73 = var35 - var44;
                           double var75 = var37 - var46;
                           double var77 = var39 - var48;
                           double var79 = var73 * var73 + var75 * var75 + var77 * var77;
                           if (var79 > 1.0E-4) {
                              double var81 = Math.sqrt(var79);
                              double var83 = var77 / var81;
                              double var85 = var75 / var81;
                              double var87 = var69 * var83 - var71 * var85;
                              double var89 = var73 / var81;
                              double var91 = var71 * var89 - var67 * var83;
                              double var93 = var67 * var85 - var69 * var89;
                              double var95 = var87 * var87 + var91 * var91 + var93 * var93;
                              if (var95 > 1.0E-4) {
                                 double var97 = Math.sqrt(var95);
                                 var51 = var87 / var97 * (double)var50;
                                 var53 = var91 / var97 * (double)var50;
                                 var55 = var93 / var97 * (double)var50;
                              }
                           }
                        } else {
                           var51 = var28;
                           var53 = var30;
                           var55 = var32;
                        }
                     }

                     if (var22 != -9999.0) {
                        float var129 = (float)(var22 - var28 - var101.x);
                        float var58 = (float)(var24 - var30 - var101.y);
                        float var130 = (float)(var26 - var32 - var101.z);
                        float var60 = (float)(var22 + var28 - var101.x);
                        float var131 = (float)(var24 + var30 - var101.y);
                        float var62 = (float)(var26 + var32 - var101.z);
                        float var132 = (float)(var44 - var51 - var101.x);
                        float var64 = (float)(var46 - var53 - var101.y);
                        float var133 = (float)(var48 - var55 - var101.z);
                        float var66 = (float)(var44 + var51 - var101.x);
                        float var134 = (float)(var46 + var53 - var101.y);
                        float var68 = (float)(var48 + var55 - var101.z);
                        var99.vertex(var5, var129, var58, var130).color(var12, var13, var14, var15);
                        var99.vertex(var5, var60, var131, var62).color(var12, var13, var14, var15);
                        var99.vertex(var5, var66, var134, var68).color(var12, var13, var14, var15);
                        var99.vertex(var5, var132, var64, var133).color(var12, var13, var14, var15);
                        var6 = true;
                     }

                     var22 = var44;
                     var24 = var46;
                     var26 = var48;
                     var28 = var51;
                     var30 = var53;
                     var32 = var55;
                     var34++;
                  }
               }
            }

            if (var6) {
               BufferRenderer.drawWithGlobalProgram(var99.end());
            }

            Render.renderEffect.handleBlend2(matrixStack);
         }

         Render.renderEffect.handleBlend(matrixStack);
         Map var100 = this.map;
         var100.values().forEach(List::clear);

         for (Trails.TrailParticle var104 : this.list) {
            int var106 = var104.getInt();
            if (var106 > 2) {
               float var108 = var104.getSize() * var104.getValue();
               float var110 = EntityHelper.computeFloat((double)var104.getPrevSize(), (double)var108);
               var104.withPrevSize(var110);
               if (!(var110 < 0.005F)) {
                  var100.computeIfAbsent(var104.getIdentifier(), Trails::lambda$renderParticles$25).add(var104);
               }
            }
         }

         for (Entry var105 : var100.entrySet()) {
            Identifier var107 = (Identifier)var105.getKey();
            List var109 = (List)var105.getValue();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderTexture(0, var107);
            BufferBuilder var111 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            boolean var112 = false;

            for (Trails.TrailParticle var114 : var109) {
               Vec3d var115 = var114.initInit(var114.getFloat(), var114.getY(), var114.getZ(), var114.getX(), var114.getFloat2(), var114.getFloat3());
               double var116 = var115.x * var115.x + var115.y * var115.y + var115.z * var115.z;
               if (!(var116 > 4096.0)) {
                  int var117 = ThemeColor.computeCurrent(var114.getIndex() * 90, var114.getInt());
                  float var118 = var114.getPrevSize();
                  float var17 = (float)var115.x;
                  float var119 = (float)var115.y;
                  float var19 = (float)var115.z;
                  Vector3f var120 = this.vector3f.set(var17, var119, var19);
                  matrixStack.peek().getPositionMatrix().transformPosition(var120);
                  Matrix4f var21 = this.matrix4f.identity();
                  if (var114.isRotating()) {
                     var21.translate(var120.x, var120.y, var120.z);
                     var21.rotateZ((float)Math.toRadians((double)EntityHelper.computeFloat((double)var114.getPrevRotation(), (double)var114.getRotation())));
                     var111.vertex(var21, var118, -var118, 0.0F).texture(0.0F, 1.0F).color(var117);
                     var111.vertex(var21, -var118, -var118, 0.0F).texture(1.0F, 1.0F).color(var117);
                     var111.vertex(var21, -var118, var118, 0.0F).texture(1.0F, 0.0F).color(var117);
                     var111.vertex(var21, var118, var118, 0.0F).texture(0.0F, 0.0F).color(var117);
                  } else {
                     var111.vertex(var21, var120.x + var118, var120.y - var118, var120.z).texture(0.0F, 1.0F).color(var117);
                     var111.vertex(var21, var120.x - var118, var120.y - var118, var120.z).texture(1.0F, 1.0F).color(var117);
                     var111.vertex(var21, var120.x - var118, var120.y + var118, var120.z).texture(1.0F, 0.0F).color(var117);
                     var111.vertex(var21, var120.x + var118, var120.y + var118, var120.z).texture(0.0F, 0.0F).color(var117);
                  }

                  var112 = true;
                  if (this.trail.getValue() && this.points.is("Points") && var114.getDeque().size() > 1) {
                     int var121 = 0;

                     for (Vec3d var122 : var114.getDeque()) {
                        float var25 = (float)var121 / (float)var114.getDeque().size();
                        double var123 = EntityHelper.computeDouble(var122.x, (double)var114.getX(), (double)var25) - camera.getPos().x;
                        double var124 = EntityHelper.computeDouble(var122.y, (double)var114.getFloat2(), (double)var25)
                           - camera.getPos().y;
                        double var125 = EntityHelper.computeDouble(var122.z, (double)var114.getFloat3(), (double)var25)
                           - camera.getPos().z;
                        float var126 = var118 * (1.0F - var25 * 0.7F);
                        float var33 = (float)var114.getInt() * (1.0F - var25 * 0.8F);
                        if (var33 <= 2.0F) {
                           var121++;
                        } else {
                           int var127 = ThemeColor.computeCurrent(var114.getIndex() * 90, (int)var33);
                           Vector3f var128 = this.vector3f2.set((float)var123, (float)var124, (float)var125);
                           matrixStack.peek().getPositionMatrix().transformPosition(var128);
                           Matrix4f var36 = this.matrix4f2.identity();
                           if (var114.isRotating()) {
                              var36.translate(var128.x, var128.y, var128.z);
                              var36.rotateZ(
                                 (float)Math.toRadians((double)EntityHelper.computeFloat((double)var114.getPrevRotation(), (double)var114.getRotation()))
                              );
                              var111.vertex(var36, var126, -var126, 0.0F).texture(0.0F, 1.0F).color(var127);
                              var111.vertex(var36, -var126, -var126, 0.0F).texture(1.0F, 1.0F).color(var127);
                              var111.vertex(var36, -var126, var126, 0.0F).texture(1.0F, 0.0F).color(var127);
                              var111.vertex(var36, var126, var126, 0.0F).texture(0.0F, 0.0F).color(var127);
                           } else {
                              var111.vertex(var36, var128.x + var126, var128.y - var126, var128.z).texture(0.0F, 1.0F).color(var127);
                              var111.vertex(var36, var128.x - var126, var128.y - var126, var128.z).texture(1.0F, 1.0F).color(var127);
                              var111.vertex(var36, var128.x - var126, var128.y + var126, var128.z).texture(1.0F, 0.0F).color(var127);
                              var111.vertex(var36, var128.x + var126, var128.y + var126, var128.z).texture(0.0F, 0.0F).color(var127);
                           }

                           var112 = true;
                           var121++;
                        }
                     }
                  }
               }
            }

            if (var112) {
               BufferRenderer.drawWithGlobalProgram(var111.end());
            }
         }

         Render.renderEffect.handleBlend2(matrixStack);
      }
   }

   public static Trails getInstance() {
      return trails;
   }

   public static List lambda$renderParticles$25(Identifier k) {
      return new ArrayList<>();
   }

   public void lambda$onEvent$24(Render3DEvent.Render3DEventData event) {
      if (mc.player != null && mc.world != null) {
         if (mc.gameRenderer != null && mc.gameRenderer.getCamera() != null) {
            Camera var2 = mc.gameRenderer.getCamera();
            if (var2.getPos() != null) {
               boolean var3 = mc.options.getPerspective() == Perspective.FIRST_PERSON;
               if (!var3 || this.inFirstPerson.getValue()) {
                  MatrixStack var4 = event.getMatrixStack();
                  if (this.player.is("Particles")) {
                     this.renderParticles(var4, var2);
                  } else if (this.player.is("Player")) {
                     this.handleEquals(var4, var2);
                  }
               }
            }
         }
      }
   }

   public void lambda$onEvent$23(UpdateEvent event) {
      if (mc.player != null && mc.world != null) {
         boolean var2 = mc.options.getPerspective() == Perspective.FIRST_PERSON;
         if (!var2 || this.inFirstPerson.getValue()) {
            if (this.player.is("Particles")) {
               this.initSize(var2);
            } else if (this.player.is("Player")) {
               this.runEquals();
            }
         }
      } else {
         this.list.clear();
         this.list2.clear();
      }
   }

   public Boolean lambda$new$22() {
      return this.player.is("Player") && !this.geometric.is("Default");
   }

   public Boolean lambda$new$21() {
      return this.player.is("Player") && this.geometric.is("Default") && (this.both.is("Outline") || this.both.is("Both"));
   }

   public Boolean lambda$new$20() {
      return this.player.is("Player") && this.geometric.is("Default") && (this.both.is("Fill") || this.both.is("Both"));
   }

   public Boolean lambda$new$19() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$18() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$17() {
      return this.player.is("Player") && (this.both.is("Fill") || this.both.is("Both"));
   }

   public Boolean lambda$new$16() {
      return this.player.is("Player") && (this.both.is("Outline") || this.both.is("Both"));
   }

   public Boolean lambda$new$15() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$14() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$13() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$12() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$11() {
      return this.player.is("Player");
   }

   public Boolean lambda$new$10() {
      return this.player.is("Particles") && this.trail.getValue();
   }

   public Boolean lambda$new$9() {
      return this.player.is("Particles") && this.trail.getValue() && this.points.is("Line");
   }

   public Boolean lambda$new$8() {
      return this.player.is("Particles") && this.trail.getValue();
   }

   public Boolean lambda$new$7() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$6() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$5() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$4() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$3() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$2() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$1() {
      return this.player.is("Particles");
   }

   public Boolean lambda$new$0() {
      return this.player.is("Particles");
   }

   private static class GhostVertexConsumer implements VertexConsumer {
      public BufferBuilder delegate;

      public GhostVertexConsumer() {
      }

      public void setDelegate(BufferBuilder delegate) {
         this.delegate = delegate;
      }

      public VertexConsumer vertex(float x, float y, float z) {
         this.delegate.vertex(x, y, z);
         return this;
      }

      public VertexConsumer vertex(Matrix4f mat, float x, float y, float z) {
         this.delegate.vertex(mat, x, y, z);
         return this;
      }

      public VertexConsumer color(int r, int g, int b, int a) {
         this.delegate.color(r, g, b, a);
         return this;
      }

      public VertexConsumer texture(float u, float v) {
         return this;
      }

      public VertexConsumer overlay(int u, int v) {
         return this;
      }

      public VertexConsumer light(int u, int v) {
         return this;
      }

      public VertexConsumer normal(float x, float y, float z) {
         return this;
      }
   }

   public static class ModelPose {
      public float floatVal;
      public float float2;
      public float float3;
      public float float4;
      public float float5;
      public float mquodfu;
      public float float6;
      public float majmiod;
      public float float7;
      public float float8;
      public float float9;
      public float float10;
      public float float11;
      public float float12;
      public float float13;
      public float mzihy;
      public float float14;
      public float float15;

      public static Trails.ModelPose initInit(BipedEntityModel model) {
         Trails.ModelPose var1 = new Trails.ModelPose();
         var1.floatVal = model.head.pitch;
         var1.float2 = model.head.yaw;
         var1.float3 = model.head.roll;
         var1.float4 = model.body.pitch;
         var1.float5 = model.body.yaw;
         var1.mquodfu = model.body.roll;
         var1.float6 = model.rightArm.pitch;
         var1.majmiod = model.rightArm.yaw;
         var1.float7 = model.rightArm.roll;
         var1.float8 = model.leftArm.pitch;
         var1.float9 = model.leftArm.yaw;
         var1.float10 = model.leftArm.roll;
         var1.float11 = model.rightLeg.pitch;
         var1.float12 = model.rightLeg.yaw;
         var1.float13 = model.rightLeg.roll;
         var1.mzihy = model.leftLeg.pitch;
         var1.float14 = model.leftLeg.yaw;
         var1.float15 = model.leftLeg.roll;
         return var1;
      }

      public void handleClass_572(BipedEntityModel model) {
         model.head.pitch = this.floatVal;
         model.head.yaw = this.float2;
         model.head.roll = this.float3;
         model.body.pitch = this.float4;
         model.body.yaw = this.float5;
         model.body.roll = this.mquodfu;
         model.rightArm.pitch = this.float6;
         model.rightArm.yaw = this.majmiod;
         model.rightArm.roll = this.float7;
         model.leftArm.pitch = this.float8;
         model.leftArm.yaw = this.float9;
         model.leftArm.roll = this.float10;
         model.rightLeg.pitch = this.float11;
         model.rightLeg.yaw = this.float12;
         model.rightLeg.roll = this.float13;
         model.leftLeg.pitch = this.mzihy;
         model.leftLeg.yaw = this.float14;
         model.leftLeg.roll = this.float15;
      }
   }

   public static class PlayerGhost {
      public final double x;
      public final double y;
      public final double z;
      public final float yaw;
      public final float pitch;
      public final float bodyYaw;
      public final long longVal;
      public final long lifetime;
      public final Trails.ModelPose pose;
      public final EntityModel model;

      public PlayerGhost(double x, double y, double z, float yaw, float pitch, float bodyYaw, long lifetime, Trails.ModelPose pose, EntityModel model) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.yaw = yaw;
         this.pitch = pitch;
         this.bodyYaw = bodyYaw;
         this.longVal = System.currentTimeMillis();
         this.lifetime = lifetime;
         this.pose = pose;
         this.model = model;
      }
   }

   private static class TrailParticle extends SparkHelper {
      public final boolean trail;
      public final int trailLength;
      public final Deque<Vec3d> deque = new ArrayDeque<>();

      public TrailParticle(
         float x,
         float y,
         float z,
         float motionX,
         float motionY,
         float motionZ,
         int index,
         Identifier identifier,
         float size,
         boolean rotate,
         int lifetime,
         boolean trail,
         int trailLength,
         String physicsMode
      ) {
         super(x, y, z, lifetime);
         this.trail = trail;
         this.trailLength = trailLength;
         boolean var15 = "Comet".equalsIgnoreCase(physicsMode);
         boolean var16 = "Drop".equalsIgnoreCase(physicsMode);
         super.withCometMode(var15)
            .withGravityFalls(var16)
            .withDropPhysics(var16)
            .withSize(size)
            .withIndex(index)
            .withMotionX(motionX)
            .withMotionY(motionY)
            .withMotionZ(motionZ)
            .withIdentifier(identifier)
            .withRotating(rotate)
            .withSpawnDuration(5.0F)
            .withDyingDuration(10.0F);
         if (var15) {
            super.withCometWobbleStrength(EntityHelper.computeRandom(0.1F, 0.25F));
            super.withCometWobbleSpeed(EntityHelper.computeRandom(0.5F, 1.2F));
         }
      }

      @Override
      public boolean checkComet() {
         boolean var1 = super.checkComet();
         if (this.trail) {
            this.deque.addFirst(new Vec3d((double)this.getX(), (double)this.getFloat2(), (double)this.getFloat3()));

            while (this.deque.size() > this.trailLength) {
               this.deque.removeLast();
            }
         }

         return var1;
      }

      public boolean isTrail() {
         return this.trail;
      }

      public Deque<Vec3d> getDeque() {
         return this.deque;
      }
   }
}
