package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "China Hat",
   category = ModuleCategory.VISUALS,
   description = "Рисует китайскую шляпу над вашей головой"
)
public class ChinaHat extends Module {
   public static final ChinaHat chinaHat = new ChinaHat();
   public final BooleanSetting self = new BooleanSetting("Self").value(true);
   public final BooleanSetting others = new BooleanSetting("Others").value(true);
   public final BooleanSetting firstPerson = new BooleanSetting("First person").value(true);
   public final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(this.self, this.others, this.firstPerson);
   public final ModeSetting halo = new ModeSetting("Render Type").value("Hat").values("Hat", "Halo");
   public final BooleanSetting followRotation = new BooleanSetting("Follow Rotation").value(true);
   public final FloatSetting equipmentOffset = new FloatSetting("Equipment Offset").value(0.15F).range(0.0F, 1.0F).step(0.01F);
   public final FloatSetting radius = new FloatSetting("Radius").value(0.45F).range(0.2F, 1.5F).step(0.05F);
   public final FloatSetting height = new FloatSetting("Height").value(0.25F).range(0.05F, 1.0F).step(0.05F);
   public final FloatSetting haloTube = new FloatSetting("Halo Tube").value(0.12F).range(0.03F, 0.5F).step(0.01F).visibleWhen(this::lambda$new$0);
   public final FloatSetting yOffsetDebug = new FloatSetting("Y Offset Debug").value(0.0F).range(-0.5F, 0.5F).step(0.01F);
   public final FloatSetting alpha = new FloatSetting("Alpha").value(0.6F).range(0.05F, 1.0F).step(0.05F);
   public final FloatSetting segments = new FloatSetting("Segments").value(28.0F).range(6.0F, 1024.0F).step(1.0F);
   public final ModeSetting rainbow = new ModeSetting("Color").value("Theme").values("Theme", "Custom", "Rainbow");
   public final ColorSetting customColor = new ColorSetting("Custom Color")
      .computeColorSetting(new Color(255, 255, 255, 255))
      .computeVisible(this::lambda$new$1);
   public final BooleanSetting glow = new BooleanSetting("Glow").value(true);
   public final FloatSetting glowSize = new FloatSetting("Glow Size").value(1.35F).range(1.05F, 2.2F).step(0.05F).visibleWhen(this.glow::getValue);
   public final FloatSetting glowAlpha = new FloatSetting("Glow Alpha").value(0.35F).range(0.05F, 1.0F).step(0.05F).visibleWhen(this.glow::getValue);
   public final BooleanSetting filled = new BooleanSetting("Filled").value(true);
   public final BooleanSetting outline = new BooleanSetting("Outline").value(true);
   public final FloatSetting outlineWidth = new FloatSetting("Outline Width").value(1.5F).range(0.5F, 4.0F).step(0.1F).visibleWhen(this.outline::getValue);
   public final BooleanSetting booleanSetting = new BooleanSetting("Physics").value(true);
   public final MultiBooleanSetting multiBooleanSetting = new MultiBooleanSetting("Options")
      .value(this.followRotation, this.glow, this.filled, this.outline, this.booleanSetting);
   public final FloatSetting swayStrength = new FloatSetting("Sway Strength")
      .value(0.35F)
      .range(0.0F, 1.5F)
      .step(0.05F)
      .visibleWhen(this.booleanSetting::getValue);
   public final FloatSetting swayStiffness = new FloatSetting("Sway Stiffness")
      .value(0.6F)
      .range(0.1F, 2.0F)
      .step(0.05F)
      .visibleWhen(this.booleanSetting::getValue);
   public final FloatSetting swayDamping = new FloatSetting("Sway Damping")
      .value(0.82F)
      .range(0.5F, 0.98F)
      .step(0.01F)
      .visibleWhen(this.booleanSetting::getValue);
   public final FloatSetting maxSway = new FloatSetting("Max Sway").value(0.35F).range(0.05F, 1.0F).step(0.05F).visibleWhen(this.booleanSetting::getValue);
   public final FloatSetting bobAmount = new FloatSetting("Bob Amount").value(0.05F).range(0.0F, 0.3F).step(0.01F).visibleWhen(this.booleanSetting::getValue);
   public final Map<UUID, ChinaHat.HatPhysics> map = new HashMap<>();
   public int segs = -1;
   public float[] floats;
   public float[] floats2;
   public int intVal = -1;
   public float[] floats3;
   public float[] floats4;
   public int innerSegs = -1;
   public float[] floats5;
   public float[] floats6;

   public ChinaHat() {
      this.addSettings(
         new Setting[]{
            this.targets,
            this.halo,
            this.multiBooleanSetting,
            this.equipmentOffset,
            this.radius,
            this.height,
            this.haloTube,
            this.yOffsetDebug,
            this.alpha,
            this.segments,
            this.rainbow,
            this.customColor,
            this.glowSize,
            this.glowAlpha,
            this.outlineWidth,
            this.swayStrength,
            this.swayStiffness,
            this.swayDamping,
            this.maxSway,
            this.bobAmount
         }
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.map.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void handleBlend(MatrixStack ms, PlayerEntity player, float tickDelta) {
      if (mc.gameRenderer != null && mc.gameRenderer.getCamera() != null) {
         Vec3d var4 = mc.gameRenderer.getCamera().getPos();
         if (var4 != null) {
            float var5 = mc.getRenderTickCounter().getTickDelta(true);
            double var6 = MathHelper.lerp((double)var5, player.prevX, player.getX());
            double var8 = MathHelper.lerp((double)var5, player.prevY, player.getY());
            double var10 = MathHelper.lerp((double)var5, player.prevZ, player.getZ());
            Vec3d var12 = this.getPhysicsOffset(player);
            double var13 = this.computeEquipment(player);
            int var15 = Math.clamp((long)this.segments.getValue().intValue(), 6, 128);
            float var16 = this.radius.getValue();
            float var17 = this.height.getValue();
            float var18 = this.haloTube.getValue();
            float var19 = (float)(System.currentTimeMillis() % 2000L) / 2000.0F;
            float var20 = 0.85F + 0.15F * (float)Math.sin((double)var19 * Math.PI * 2.0 + (double)player.getId());
            int var21 = (int)(this.alpha.getValue() * 255.0F * var20);
            float var22 = MathHelper.lerp(var5, player.prevHeadYaw, player.headYaw);
            float var23 = MathHelper.lerp(var5, player.prevPitch, player.getPitch());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            ms.push();
            if (this.followRotation.getValue()) {
               float var24 = player.getEyeHeight(player.getPose());
               double var25 = var8 + (double)var24;
               ms.translate(
                  var6 - var4.x + var12.x, var25 - var4.y + var12.y, var10 - var4.z + var12.z
               );
               ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - var22));
               ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var23));
               float var27 = player.getHeight() - var24 + this.yOffsetDebug.getValue() + (float)var13;
               ms.translate(0.0F, var27, 0.0F);
            } else {
               double var28 = var8 + (double)player.getHeight() + (double)this.yOffsetDebug.getValue().floatValue();
               ms.translate(
                  var6 - var4.x + var12.x, var28 - var4.y + var12.y + var13, var10 - var4.z + var12.z
               );
            }

            boolean var29 = this.halo.getValue().equals("Halo");
            if (var29) {
               ms.translate(0.0F, var17, 0.0F);
            }

            if (this.glow.getValue()) {
               RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
               if (var29) {
                  this.handleInner(ms, var15, var16 * this.glowSize.getValue(), var18 * 0.85F, (int)((float)var21 * this.glowAlpha.getValue()));
               } else {
                  this.handleSegs(ms, var15, var16 * this.glowSize.getValue(), var17 * 0.85F, (int)((float)var21 * this.glowAlpha.getValue()), true);
               }

               RenderSystem.defaultBlendFunc();
            }

            if (this.filled.getValue()) {
               if (var29) {
                  this.handleInner(ms, var15, var16, var18, var21);
               } else {
                  this.handleSegs(ms, var15, var16, var17, var21, false);
               }
            }

            if (this.outline.getValue()) {
               RenderSystem.lineWidth(this.outlineWidth.getValue());
               if (var29) {
                  this.handleMax(ms, var15, var16, var18, var21);
               } else {
                  this.handleSegs(ms, var15, var16, var17, var21);
               }
            }

            ms.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
         }
      }
   }

   public double computeEquipment(PlayerEntity player) {
      return player.getEquippedStack(EquipmentSlot.HEAD).isEmpty() ? 0.0 : (double)this.equipmentOffset.getValue().floatValue();
   }

   public Vec3d getPhysicsOffset(PlayerEntity player) {
      if (!this.booleanSetting.getValue()) {
         return Vec3d.ZERO;
      } else {
         ChinaHat.HatPhysics var2 = this.map.computeIfAbsent(player.getUuid(), ChinaHat::lambda$getPhysicsOffset$3);
         float var3 = MathHelper.clamp(mc.getRenderTickCounter().getLastFrameDuration(), 0.01F, 1.0F);
         float var4 = var3 / 20.0F;
         Vec3d var5 = player.getVelocity();
         Vec3d var6 = new Vec3d(-var5.x, 0.0, -var5.z).multiply((double)this.swayStrength.getValue().floatValue());
         double var7 = (double)this.maxSway.getValue().floatValue();
         double var9 = Math.sqrt(var6.x * var6.x + var6.z * var6.z);
         if (var9 > var7 && var9 > 0.0) {
            var6 = var6.multiply(var7 / var9);
         }

         double var11 = MathHelper.clamp(var5.length() * 3.0, 0.0, 1.0);
         double var13 = Math.sin((double)((float)player.age + var3) * 0.6 + (double)player.getId())
            * (double)this.bobAmount.getValue().floatValue()
            * var11;
         var6 = var6.add(0.0, var13, 0.0);
         Vec3d var15 = var6.subtract(var2.class_243);
         var2.class_2432 = var2.class_2432.add(var15.multiply((double)(this.swayStiffness.getValue() * var4)));
         double var16 = Math.pow((double)this.swayDamping.getValue().floatValue(), (double)var4 * 20.0);
         var2.class_2432 = var2.class_2432.multiply(var16);
         var2.class_243 = var2.class_243.add(var2.class_2432);
         return var2.class_243;
      }
   }

   public void setSegs(int segs) {
      if (this.segs != segs || this.floats == null) {
         this.floats = new float[segs + 1];
         this.floats2 = new float[segs + 1];

         for (int var2 = 0; var2 <= segs; var2++) {
            double var3 = (Math.PI * 2) * (double)var2 / (double)segs;
            this.floats[var2] = (float)Math.cos(var3);
            this.floats2[var2] = (float)Math.sin(var3);
         }

         this.segs = segs;
      }
   }

   public void handleCos(int segs) {
      if (this.intVal != segs || this.floats3 == null) {
         this.floats3 = new float[segs + 1];
         this.floats4 = new float[segs + 1];

         for (int var2 = 0; var2 <= segs; var2++) {
            double var3 = (Math.PI * 2) * (double)var2 / (double)segs;
            this.floats3[var2] = (float)Math.sin(var3);
            this.floats4[var2] = (float)Math.cos(var3);
         }

         this.intVal = segs;
      }
   }

   public void setInnerSegs(int innerSegs) {
      if (this.innerSegs != innerSegs || this.floats5 == null) {
         this.floats5 = new float[innerSegs + 1];
         this.floats6 = new float[innerSegs + 1];

         for (int var2 = 0; var2 <= innerSegs; var2++) {
            double var3 = (Math.PI * 2) * (double)var2 / (double)innerSegs;
            this.floats5[var2] = (float)Math.cos(var3);
            this.floats6[var2] = (float)Math.sin(var3);
         }

         this.innerSegs = innerSegs;
      }
   }

   public void handleSegs(MatrixStack ms, int segs, float r, float h, int alpha, boolean glowPass) {
      this.setSegs(segs);
      BufferBuilder var7 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      Matrix4f var8 = ms.peek().getPositionMatrix();

      for (int var9 = 0; var9 < segs; var9++) {
         float var10 = this.floats[var9] * r;
         float var11 = this.floats2[var9] * r;
         float var12 = this.floats[var9 + 1] * r;
         float var13 = this.floats2[var9 + 1] * r;
         float var14 = (float)var9 / (float)segs;
         float var15 = (float)(var9 + 1) / (float)segs;
         int var16 = this.computeRgb(var14, alpha);
         int var17 = this.computeRgb(var15, alpha);
         int var18 = this.computeBlue(alpha);
         var7.vertex(var8, 0.0F, h, 0.0F).color(var18);
         var7.vertex(var8, var10, 0.0F, var11).color(var16);
         var7.vertex(var8, var12, 0.0F, var13).color(var17);
      }

      BufferRenderer.drawWithGlobalProgram(var7.end());
   }

   public void handleSegs(MatrixStack ms, int segs, float r, float h, int alpha) {
      this.setSegs(segs);
      BufferBuilder var6 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      Matrix4f var7 = ms.peek().getPositionMatrix();

      for (int var8 = 0; var8 < segs; var8++) {
         float var9 = this.floats[var8] * r;
         float var10 = this.floats2[var8] * r;
         float var11 = this.floats[var8 + 1] * r;
         float var12 = this.floats2[var8 + 1] * r;
         float var13 = (float)var8 / (float)segs;
         float var14 = (float)(var8 + 1) / (float)segs;
         int var15 = this.computeRgb(var13, alpha);
         int var16 = this.computeRgb(var14, alpha);
         int var17 = this.computeBlue((int)((float)alpha * 0.9F));
         var6.vertex(var7, var9, 0.0F, var10).color(var15);
         var6.vertex(var7, var11, 0.0F, var12).color(var16);
         var6.vertex(var7, 0.0F, h, 0.0F).color(var17);
         var6.vertex(var7, var9, 0.0F, var10).color(var15);
      }

      BufferRenderer.drawWithGlobalProgram(var6.end());
   }

   public void handleInner(MatrixStack ms, int segs, float ringRadius, float tubeRadius, int alpha) {
      int var6 = Math.min(16, Math.max(6, segs / 4));
      this.handleCos(segs);
      this.setInnerSegs(var6);
      BufferBuilder var7 = Tessellator.getInstance().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      Matrix4f var8 = ms.peek().getPositionMatrix();

      for (int var9 = 0; var9 < segs; var9++) {
         float var10 = this.floats3[var9];
         float var11 = this.floats4[var9];
         float var12 = this.floats3[var9 + 1];
         float var13 = this.floats4[var9 + 1];
         float var14 = (float)var9 / (float)segs;
         int var15 = this.computeRgb(var14, alpha);

         for (int var16 = 0; var16 < var6; var16++) {
            float var17 = this.floats5[var16];
            float var18 = this.floats5[var16 + 1];
            float var19 = this.floats6[var16];
            float var20 = this.floats6[var16 + 1];
            float var21 = ringRadius + tubeRadius * var17;
            float var22 = ringRadius + tubeRadius * var18;
            float var23 = tubeRadius * var19;
            float var24 = tubeRadius * var20;
            float var25 = var21 * var10;
            float var26 = var21 * var11;
            float var27 = var22 * var10;
            float var28 = var22 * var11;
            float var29 = var21 * var12;
            float var30 = var21 * var13;
            float var31 = var22 * var12;
            float var32 = var22 * var13;
            var7.vertex(var8, var25, var23, var26).color(var15);
            var7.vertex(var8, var29, var23, var30).color(var15);
            var7.vertex(var8, var27, var24, var28).color(var15);
            var7.vertex(var8, var27, var24, var28).color(var15);
            var7.vertex(var8, var29, var23, var30).color(var15);
            var7.vertex(var8, var31, var24, var32).color(var15);
         }
      }

      BufferRenderer.drawWithGlobalProgram(var7.end());
   }

   public void handleMax(MatrixStack ms, int segs, float ringRadius, float tubeRadius, int alpha) {
      this.handleCos(segs);
      BufferBuilder var6 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      Matrix4f var7 = ms.peek().getPositionMatrix();
      float var8 = ringRadius + tubeRadius;
      float var9 = Math.max(0.01F, ringRadius - tubeRadius);

      for (int var10 = 0; var10 < segs; var10++) {
         float var11 = this.floats3[var10];
         float var12 = this.floats4[var10];
         float var13 = this.floats3[var10 + 1];
         float var14 = this.floats4[var10 + 1];
         float var15 = (float)var10 / (float)segs;
         float var16 = (float)(var10 + 1) / (float)segs;
         int var17 = this.computeRgb(var15, alpha);
         int var18 = this.computeRgb(var16, alpha);
         float var19 = var11 * var8;
         float var20 = var12 * var8;
         float var21 = var13 * var8;
         float var22 = var14 * var8;
         float var23 = var11 * var9;
         float var24 = var12 * var9;
         float var25 = var13 * var9;
         float var26 = var14 * var9;
         var6.vertex(var7, var19, 0.0F, var20).color(var17);
         var6.vertex(var7, var21, 0.0F, var22).color(var18);
         var6.vertex(var7, var23, 0.0F, var24).color(var17);
         var6.vertex(var7, var25, 0.0F, var26).color(var18);
      }

      BufferRenderer.drawWithGlobalProgram(var6.end());
   }

   public int computeRgb(float progress, int alpha) {
      Color var3 = this.computeColor((int)(progress * 360.0F));
      return alpha << 24 | var3.getRGB() & 16777215;
   }

   public int computeBlue(int alpha) {
      Color var2 = this.computeColor(180);
      int var3 = (int)((float)var2.getRed() + (float)(255 - var2.getRed()) * 0.35F);
      int var4 = (int)((float)var2.getGreen() + (float)(255 - var2.getGreen()) * 0.35F);
      int var5 = (int)((float)var2.getBlue() + (float)(255 - var2.getBlue()) * 0.35F);
      return alpha << 24 | var3 << 16 | var4 << 8 | var5;
   }

   public Color computeColor(int seed) {
      String var2 = this.rainbow.getValue();
      byte var3 = -1;
      switch (var2.hashCode()) {
         case 2029746065:
            if (var2.equals("Custom")) {
               var3 = 0;
            }
         default:
            return switch (var3) {
               case 0 -> (Color)this.customColor.getValue();
               default -> ThemeColor.computeColor(seed * 2);
            };
      }
   }

   public static ChinaHat getInstance() {
      return chinaHat;
   }

   public static ChinaHat.HatPhysics lambda$getPhysicsOffset$3(UUID id) {
      return new ChinaHat.HatPhysics();
   }

   public void lambda$onEvent$2(Render3DEvent.Render3DEventData event) {
      if (mc.world != null && mc.player != null) {
         MatrixStack var2 = event.getMatrixStack();
         float var3 = event.getPartialTicks();
         double var4 = mc.player.getX();
         double var6 = mc.player.getY();
         double var8 = mc.player.getZ();
         double var10 = 4096.0;

         for (PlayerEntity var13 : mc.world.getPlayers()) {
            if (var13 != null) {
               double var14 = var13.getX() - var4;
               double var16 = var13.getY() - var6;
               double var18 = var13.getZ() - var8;
               if (var14 * var14 + var16 * var16 + var18 * var18 > var10) {
                  this.map.remove(var13.getUuid());
               } else {
                  boolean var20 = var13 == mc.player;
                  boolean var21 = mc.options.getPerspective() == Perspective.FIRST_PERSON;
                  if (var20 ? this.targets.isEnabled("Self") && (!var21 || this.targets.isEnabled("First person")) : this.targets.isEnabled("Others")) {
                     this.handleBlend(var2, var13, var3);
                  }
               }
            }
         }
      }
   }

   public Boolean lambda$new$1() {
      return this.rainbow.getValue().equals("Custom");
   }

   public Boolean lambda$new$0() {
      return this.halo.getValue().equals("Halo");
   }

   public static class HatPhysics {
      public Vec3d class_243 = class_243.ZERO;
      public Vec3d class_2432 = class_243.ZERO;
   }
}
