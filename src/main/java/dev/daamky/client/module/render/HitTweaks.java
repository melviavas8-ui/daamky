package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.Render;
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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(
   name = "Hit Tweaks",
   category = ModuleCategory.VISUALS,
   description = "Настройка подсветки урона и вылетающих силуэтов при ударе"
)
public class HitTweaks extends Module {
   public static final HitTweaks hitTweaks = new HitTweaks();
   public final BooleanSetting customHitColor = new BooleanSetting("Custom Hit Color").value(true);
   public final ColorSetting hitColor = new ColorSetting("Hit Color")
      .computeColorSetting(new Color(255, 60, 60, 140))
      .computeVisible(this.customHitColor::getValue);
   public final BooleanSetting flyingSilhouette = new BooleanSetting("Flying Silhouette").value(true);
   public final BooleanSetting additiveGlow = new BooleanSetting("Additive Glow").value(true).visibleWhen(this.flyingSilhouette::getValue);
   public final BooleanSetting scaleExpand = new BooleanSetting("Scale Expand").value(true).visibleWhen(this.flyingSilhouette::getValue);
   public final ModeSetting both = new ModeSetting("Render Mode").value("Both").values("Fill", "Outline", "Both").visibleWhen(this.flyingSilhouette::getValue);
   public final ColorSetting fillColor = new ColorSetting("Fill Color").computeColorSetting(new Color(255, 60, 90, 90)).computeVisible(this::lambda$new$0);
   public final ColorSetting outlineColor = new ColorSetting("Outline Color")
      .computeColorSetting(new Color(255, 80, 120, 220))
      .computeVisible(this::lambda$new$1);
   public final FloatSetting outlineWidth = new FloatSetting("Outline Width").value(1.5F).range(0.5F, 4.0F).step(0.1F).visibleWhen(this::lambda$new$2);
   public final FloatSetting height = new FloatSetting("Height").value(2.2F).range(0.5F, 5.0F).step(0.1F).visibleWhen(this.flyingSilhouette::getValue);
   public final FloatSetting speed = new FloatSetting("Speed").value(1.4F).range(0.2F, 4.0F).step(0.1F).visibleWhen(this.flyingSilhouette::getValue);
   public final FloatSetting maxSilhouettes = new FloatSetting("Max Silhouettes")
      .value(6.0F)
      .range(1.0F, 12.0F)
      .step(1.0F)
      .visibleWhen(this.flyingSilhouette::getValue);
   public final List<HitTweaks.Silhouette> list = new ArrayList<>();
   public static final HitTweaks.SilhouetteVertexConsumer silhouetteVertexConsumer = new HitTweaks.SilhouetteVertexConsumer();

   public HitTweaks() {
      this.addSettings(
         new Setting[]{
            this.customHitColor,
            this.hitColor,
            this.flyingSilhouette,
            this.additiveGlow,
            this.scaleExpand,
            this.both,
            this.fillColor,
            this.outlineColor,
            this.outlineWidth,
            this.height,
            this.speed,
            this.maxSilhouettes
         }
      );
   }

   @Override
   public void onEvent() {
      Subscription var1 = AttackEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$4));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   @Override
   public void onDisable() {
      this.list.clear();
   }

   public static void handleVal(MatrixStack matrices, EntityModel<?> model, int packedColor, float width, boolean isLine) {
      handleBlend(matrices, model, packedColor, width, isLine, true);
   }

   public static void handleBlend(MatrixStack matrices, EntityModel<?> model, int packedColor, float width, boolean isLine, boolean additive) {
      BufferBuilder var6 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      silhouetteVertexConsumer.setDelegate(var6);
      model.render(matrices, silhouetteVertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, packedColor);
      BuiltBuffer var7 = var6.end();
      if (var7 != null) {
         RenderSystem.depthMask(false);
         RenderSystem.enableDepthTest();
         RenderSystem.enableBlend();
         if (additive) {
            RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
         } else {
            RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
         }

         RenderSystem.disableCull();
         if (isLine) {
            GL11.glPolygonMode(1032, 6913);
            RenderSystem.lineWidth(width);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         } else {
            GL11.glPolygonMode(1032, 6914);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         }

         BufferRenderer.drawWithGlobalProgram(var7);
         if (isLine) {
            GL11.glPolygonMode(1032, 6914);
            RenderSystem.lineWidth(1.0F);
         }

         RenderSystem.enableCull();
         RenderSystem.defaultBlendFunc();
         RenderSystem.depthMask(true);
      }
   }

   public static void initEquals(DrawContext ctx, MatrixStack matrices, float cx, float cy, float previewW, float previewH) {
      MinecraftClient var6 = MinecraftClient.getInstance();
      if (var6.player != null) {
         float var7 = cx - previewW / 2.0F;
         float var8 = cy - previewH / 2.0F;
         int var9 = (int)(previewH * 0.35F);
         if (var9 >= 5) {
            float var10 = (float)(System.currentTimeMillis() % 8000L) / 8000.0F * 360.0F;
            float var11 = cx + (float)(Math.cos(Math.toRadians((double)var10)) * (double)previewW * 0.3F);
            float var12 = cy + previewH * 0.1F;
            InventoryScreen.drawEntity(ctx, (int)var7, (int)var8, (int)(var7 + previewW), (int)(var8 + previewH), var9, 0.0F, var11, var12, var6.player);
            HitTweaks var13 = getInstance();
            String var14 = var13.both.getValue();
            Color var15 = var13.fillColor.getValue();
            if (("Fill".equals(var14) || "Both".equals(var14)) && var15.getAlpha() > 10) {
               int var16 = var15.getAlpha();
               Render.rectRenderer
                  .handleRgb(
                     matrices,
                     var7,
                     var8,
                     previewW,
                     previewH,
                     InterfaceMetrics.computeFloat(4.0F),
                     new Color(var15.getRed(), var15.getGreen(), var15.getBlue(), Math.min(var16, 80))
                  );
            }
         }
      }
   }

   public static HitTweaks getInstance() {
      return hitTweaks;
   }

   public BooleanSetting getCustomHitColor() {
      return this.customHitColor;
   }

   public ColorSetting getHitColor() {
      return this.hitColor;
   }

   public void lambda$onEvent$4(Render3DEvent.Render3DEventData event) {
      if (this.flyingSilhouette.getValue() && !this.list.isEmpty() && mc.getEntityRenderDispatcher() != null && mc.getEntityRenderDispatcher().camera != null) {
         double var2 = mc.getEntityRenderDispatcher().camera.getPos().x;
         double var4 = mc.getEntityRenderDispatcher().camera.getPos().y;
         double var6 = mc.getEntityRenderDispatcher().camera.getPos().z;
         long var8 = System.currentTimeMillis();
         MatrixStack var10 = event.getMatrixStack();
         boolean var11 = this.additiveGlow.getValue();
         boolean var12 = this.scaleExpand.getValue();
         String var13 = this.both.getValue();
         float var14 = this.outlineWidth.getValue();
         Color var15 = this.fillColor.getValue();
         Color var16 = this.outlineColor.getValue();
         Iterator var17 = this.list.iterator();

         while (var17.hasNext()) {
            HitTweaks.Silhouette var18 = (HitTweaks.Silhouette)var17.next();
            long var19 = var8 - var18.longVal;
            if (var19 >= var18.lifetime) {
               var17.remove();
            } else {
               double var21 = var18.x - var2;
               double var23 = (double)var19 / (double)var18.lifetime;
               double var25 = 1.0 - Math.pow(1.0 - var23, 3.0);
               double var27 = var25 * (double)this.height.getValue().floatValue();
               double var29 = var18.y + var27 - var4;
               double var31 = var18.z - var6;
               double var33 = var21 * var21 + var29 * var29 + var31 * var31;
               if (!(var33 < 0.01) && !(var33 > 4096.0)) {
                  var10.push();
                  var10.translate(var21, var29, var31);
                  var10.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - var18.yaw));
                  var10.scale(-1.0F, -1.0F, 1.0F);
                  if (var12) {
                     float var35 = 1.0F + 0.08F * (float)var23;
                     var10.scale(var35, var35, var35);
                  }

                  var10.translate(0.0F, -1.501F, 0.0F);
                  HitTweaks.ModelPose var40 = null;
                  BipedEntityModel var36 = null;
                  EntityModel var37 = var18.model;
                  if (var37 instanceof BipedEntityModel) {
                     BipedEntityModel var38;
                     var36 = var38 = (BipedEntityModel)var37;
                     var40 = HitTweaks.ModelPose.initInit(var38);
                     if (var18.pose != null) {
                        var18.pose.handleClass_572(var38);
                     }
                  }

                  float var41 = (float)Math.pow(1.0 - var23, 1.4);
                  if ("Fill".equals(var13) || "Both".equals(var13)) {
                     int var39 = ColorMath.computeMin(var15.getRGB(), (int)((float)var15.getAlpha() * var41));
                     handleBlend(var10, var18.model, var39, 1.0F, false, var11);
                  }

                  if ("Outline".equals(var13) || "Both".equals(var13)) {
                     int var42 = ColorMath.computeMin(var16.getRGB(), (int)((float)var16.getAlpha() * var41));
                     handleBlend(var10, var18.model, var42, var14, true, var11);
                  }

                  if (var36 != null && var40 != null) {
                     var40.handleClass_572(var36);
                  }

                  var10.pop();
               }
            }
         }
      }
   }

   public void lambda$onEvent$3(AttackEvent.AttackEventData event) {
      if (this.flyingSilhouette.getValue()) {
         if (event.entity() instanceof LivingEntity var3 && mc.getEntityRenderDispatcher().getRenderer(var3) instanceof LivingEntityRenderer var5) {
            EntityModel var6 = var5.getModel();
            HitTweaks.ModelPose var7 = null;
            if (var6 instanceof BipedEntityModel var8) {
               var7 = HitTweaks.ModelPose.initInit(var8);
            }

            double var19 = var3.getX();
            double var10 = var3.getY();
            double var12 = var3.getZ();
            float var14 = var3.getYaw();
            float var15 = var3.getPitch();
            long var16 = (long)(1000.0F * (this.height.getValue() / this.speed.getValue()));
            int var18 = (int)this.maxSilhouettes.getValue().floatValue();

            while (this.list.size() >= var18 && !this.list.isEmpty()) {
               this.list.remove(0);
            }

            this.list.add(new HitTweaks.Silhouette(var3.getUuid(), var6, var19, var10, var12, var14, var15, var16, var7));
         }
      }
   }

   public Boolean lambda$new$2() {
      return this.flyingSilhouette.getValue() && (this.both.is("Outline") || this.both.is("Both"));
   }

   public Boolean lambda$new$1() {
      return this.flyingSilhouette.getValue() && (this.both.is("Outline") || this.both.is("Both"));
   }

   public Boolean lambda$new$0() {
      return this.flyingSilhouette.getValue() && (this.both.is("Fill") || this.both.is("Both"));
   }

   public static class ModelPose {
      public float floatVal;
      public float float2;
      public float float3;
      public float float4;
      public float float5;
      public float float6;
      public float float7;
      public float float8;
      public float float9;
      public float float10;
      public float float11;
      public float float12;
      public float float13;
      public float float14;
      public float float15;
      public float float16;
      public float float17;
      public float float18;

      public static HitTweaks.ModelPose initInit(BipedEntityModel<?> model) {
         HitTweaks.ModelPose var1 = new HitTweaks.ModelPose();
         var1.floatVal = model.head.pitch;
         var1.float2 = model.head.yaw;
         var1.float3 = model.head.roll;
         var1.float4 = model.body.pitch;
         var1.float5 = model.body.yaw;
         var1.float6 = model.body.roll;
         var1.float7 = model.rightArm.pitch;
         var1.float8 = model.rightArm.yaw;
         var1.float9 = model.rightArm.roll;
         var1.float10 = model.leftArm.pitch;
         var1.float11 = model.leftArm.yaw;
         var1.float12 = model.leftArm.roll;
         var1.float13 = model.rightLeg.pitch;
         var1.float14 = model.rightLeg.yaw;
         var1.float15 = model.rightLeg.roll;
         var1.float16 = model.leftLeg.pitch;
         var1.float17 = model.leftLeg.yaw;
         var1.float18 = model.leftLeg.roll;
         return var1;
      }

      public void handleClass_572(BipedEntityModel<?> model) {
         model.head.pitch = this.floatVal;
         model.head.yaw = this.float2;
         model.head.roll = this.float3;
         model.body.pitch = this.float4;
         model.body.yaw = this.float5;
         model.body.roll = this.float6;
         model.rightArm.pitch = this.float7;
         model.rightArm.yaw = this.float8;
         model.rightArm.roll = this.float9;
         model.leftArm.pitch = this.float10;
         model.leftArm.yaw = this.float11;
         model.leftArm.roll = this.float12;
         model.rightLeg.pitch = this.float13;
         model.rightLeg.yaw = this.float14;
         model.rightLeg.roll = this.float15;
         model.leftLeg.pitch = this.float16;
         model.leftLeg.yaw = this.float17;
         model.leftLeg.roll = this.float18;
      }
   }

   public static class Silhouette {
      public final UUID uuid;
      public final EntityModel<?> model;
      public final double x;
      public final double y;
      public final double z;
      public final float yaw;
      public final float pitch;
      public final long longVal;
      public final long lifetime;
      public final HitTweaks.ModelPose pose;

      public Silhouette(UUID uuid, EntityModel<?> model, double x, double y, double z, float yaw, float pitch, long lifetime, HitTweaks.ModelPose pose) {
         this.uuid = uuidx;
         this.model = modelx;
         this.x = xx;
         this.y = yx;
         this.z = zx;
         this.yaw = yawx;
         this.pitch = pitchx;
         this.longVal = System.currentTimeMillis();
         this.lifetime = lifetimex;
         this.pose = posex;
      }
   }

   private static class SilhouetteVertexConsumer implements VertexConsumer {
      public BufferBuilder delegate;

      public SilhouetteVertexConsumer() {
      }

      public void setDelegate(BufferBuilder delegate) {
         this.delegate = delegatex;
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
}
