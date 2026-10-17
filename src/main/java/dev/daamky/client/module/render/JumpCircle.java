package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.JumpEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Jump Circle",
   category = ModuleCategory.VISUALS,
   description = "Рисует красивый круг под ногами при прыжке"
)
public class JumpCircle extends Module {
   public static final JumpCircle jumpCircle = new JumpCircle();
   public final ModeSetting thin = new ModeSetting("Texture").value("Glow").values("Lean", "Glow", "Pentagram", "Regular", "Thick", "Thin");
   public final ModeSetting modeSetting = new ModeSetting("Out animation").value("None").values("Shrink", "None");
   public final FloatSetting size = new FloatSetting("Size").value(2.0F).range(0.1F, 3.0F).step(0.1F);
   public final FloatSetting lifeTime = new FloatSetting("Life time").value(10.0F).range(1.0F, 30.0F).step(1.0F);
   public final FloatSetting spawnDuration = new FloatSetting("Spawn duration").value(6.0F).range(1.0F, 30.0F).step(1.0F);
   public final FloatSetting dyingDuration = new FloatSetting("Dying duration").value(4.0F).range(1.0F, 30.0F).step(1.0F);
   public final BooleanSetting glow = new BooleanSetting("Glow").value(true);
   public final FloatSetting glowSize = new FloatSetting("Glow Size").value(1.5F).range(0.5F, 3.0F).step(0.1F).visibleWhen(this.glow::getValue);
   public final FloatSetting glowAlpha = new FloatSetting("Glow Alpha").value(0.4F).range(0.1F, 1.0F).step(0.05F).visibleWhen(this.glow::getValue);
   public final FloatSetting glowYOffset = new FloatSetting("Glow Y Offset").value(0.01F).range(-0.1F, 0.1F).step(0.01F).visibleWhen(this.glow::getValue);
   public final List<JumpCircle.Circle> mnexq = new ArrayList<>();

   public JumpCircle() {
      this.addSettings(
         new Setting[]{
            this.thin,
            this.modeSetting,
            this.size,
            this.lifeTime,
            this.spawnDuration,
            this.dyingDuration,
            this.glow,
            this.glowSize,
            this.glowAlpha,
            this.glowYOffset
         }
      );
   }

   public String getString() {
      String var1 = this.thin.getValue();

      return switch (var1) {
         case "Lean" -> "circle/lean";
         case "Pentagram" -> "circle/c1";
         case "Regular" -> "circle/c2";
         case "Thick" -> "circle/c3";
         case "Thin" -> "circle/c4";
         default -> "circle/glow_fat";
      };
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.mnexq.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = JumpEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public void handleNext(MatrixStack ms, Vec3d cameraPos) {
      RenderSystem.setShaderTexture(0, InputHelper.getImage("particles/glow"));
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
      float var3 = this.glowSize.getValue();
      float var4 = this.glowAlpha.getValue();
      float var5 = this.glowYOffset.getValue();
      int var6 = 0;

      for (JumpCircle.Circle var8 : this.mnexq) {
         if (var8.float2 > 0.01F) {
            var6++;
         }
      }

      if (var6 != 0) {
         BufferBuilder var11 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         boolean var12 = false;

         for (JumpCircle.Circle var10 : this.mnexq) {
            if (var10.computePosition(var11, ms, cameraPos, var3, var4, var5)) {
               var12 = true;
            }
         }

         if (var12) {
            BufferRenderer.drawWithGlobalProgram(var11.end());
         }
      }
   }

   public void handleNext2(MatrixStack ms, Vec3d cameraPos) {
      RenderSystem.setShaderTexture(0, InputHelper.getImage(this.getString()));
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO);
      int var3 = 0;

      for (JumpCircle.Circle var5 : this.mnexq) {
         if (var5.float2 > 0.01F) {
            var3++;
         }
      }

      if (var3 != 0) {
         BufferBuilder var8 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         boolean var9 = false;

         for (JumpCircle.Circle var7 : this.mnexq) {
            if (var7.computeRgb(var8, ms, cameraPos)) {
               var9 = true;
            }
         }

         if (var9) {
            BufferRenderer.drawWithGlobalProgram(var8.end());
         }
      }
   }

   public static JumpCircle getInstance() {
      return jumpCircle;
   }

   public void lambda$onEvent$2(JumpEvent event) {
      if (mc.player != null) {
         this.mnexq
            .add(
               new JumpCircle.Circle(
                  mc.player.getPos().add(0.0, 0.15, 0.0),
                  this.size.getValue(),
                  this.mnexq.size() * 9,
                  this.lifeTime.getValue().intValue() * 50,
                  this.spawnDuration.getValue().intValue() * 50,
                  this.dyingDuration.getValue().intValue() * 50,
                  this.modeSetting.getValue()
               )
            );
      }
   }

   public void lambda$onEvent$1(Render3DEvent.Render3DEventData event) {
      if (!this.mnexq.isEmpty() && mc.player != null && mc.world != null) {
         if (mc.gameRenderer != null && mc.gameRenderer.getCamera() != null) {
            Camera var2 = mc.gameRenderer.getCamera();
            Vec3d var3 = var2.getPos();
            if (var3 != null) {
               boolean var4 = false;

               for (JumpCircle.Circle var6 : this.mnexq) {
                  if (var6.float2 > 0.01F) {
                     var4 = true;
                     break;
                  }
               }

               if (var4) {
                  MatrixStack var7 = event.getMatrixStack();
                  Render.renderEffect.handleBlend(var7);
                  if (this.glow.getValue()) {
                     this.handleNext(var7, var3);
                  }

                  this.handleNext2(var7, var3);
                  Render.renderEffect.handleBlend2(var7);
               }
            }
         }
      }
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.mnexq.removeIf(JumpCircle.Circle::checkDur);
      if (this.mnexq.size() > 40) {
         this.mnexq.subList(0, this.mnexq.size() - 40).clear();
      }
   }

   private static class Circle {
      public final Vec3d position;
      public final float size;
      public final int index;
      public final int lifeTime;
      public final int spawnDur;
      public final int dyingDur;
      public final String animMode;
      public final DelayHelper delayHelper = new DelayHelper();
      public final AnimatedValue animatedValue = new AnimatedValue();
      public final AnimatedValue animatedValue2 = new AnimatedValue();
      public boolean flag = false;
      public Color color;
      public Color color2;
      public Color color3;
      public Color color4;
      public float floatVal;
      public float float2;

      public boolean checkDur() {
         if (this.delayHelper.computeCurrent((long)(this.spawnDur + this.lifeTime))) {
            this.flag = true;
         }

         this.animatedValue.update();
         this.animatedValue2.update();
         this.animatedValue2
            .animateTo(
               this.flag ? (this.animMode.contains("None") ? 1.0 : 0.0) : 1.0,
               this.flag ? (long)this.dyingDur : (long)this.spawnDur,
               this.flag ? Easing.CUBIC_IN : Easing.CUBIC_OUT
            );
         this.animatedValue
            .animateTo(this.flag ? 0.0 : 1.0, this.flag ? (long)this.dyingDur : (long)this.spawnDur, this.flag ? Easing.CUBIC_IN : Easing.CUBIC_OUT);
         this.float2 = (float)this.animatedValue.getValue();
         this.floatVal = (float)(this.animatedValue2.getValue() * (double)this.size);
         int var1 = (int)(this.float2 * 255.0F);
         this.color = ColorMath.initBlue(ThemeColor.computeColor(this.index), var1);
         this.color2 = ColorMath.initBlue(ThemeColor.computeColor(this.index + 90), var1);
         this.color3 = ColorMath.initBlue(ThemeColor.computeColor(this.index + 180), var1);
         this.color4 = ColorMath.initBlue(ThemeColor.computeColor(this.index + 240), var1);
         return this.animatedValue.getValue() <= 0.01 && this.flag;
      }

      public boolean computePosition(BufferBuilder buffer, MatrixStack ms, Vec3d cameraPos, float sizeMultiplier, float alphaMultiplier, float yOffset) {
         if (this.float2 <= 0.01F) {
            return false;
         } else {
            float var7 = this.floatVal * sizeMultiplier;
            float var8 = this.float2 * alphaMultiplier;
            Color var9 = ColorMath.initBlue(ThemeColor.computeColor(this.index + 135), (int)(var8 * 255.0F));
            ms.push();
            ms.translate(
               this.position.x - cameraPos.x,
               this.position.y - cameraPos.y + (double)yOffset,
               this.position.z - cameraPos.z
            );
            ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            Matrix4f var10 = ms.peek().getPositionMatrix();
            int var11 = var9.getRGB();
            buffer.vertex(var10, var7, -var7, 0.0F).texture(0.0F, 1.0F).color(var11);
            buffer.vertex(var10, -var7, -var7, 0.0F).texture(1.0F, 1.0F).color(var11);
            buffer.vertex(var10, -var7, var7, 0.0F).texture(1.0F, 0.0F).color(var11);
            buffer.vertex(var10, var7, var7, 0.0F).texture(0.0F, 0.0F).color(var11);
            ms.pop();
            return true;
         }
      }

      public boolean computeRgb(BufferBuilder buffer, MatrixStack ms, Vec3d cameraPos) {
         if (this.float2 <= 0.01F) {
            return false;
         } else {
            ms.push();
            ms.translate(
               this.position.x - cameraPos.x,
               this.position.y - cameraPos.y,
               this.position.z - cameraPos.z
            );
            ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)this.delayHelper.getCurrent() * 0.1F));
            Matrix4f var4 = ms.peek().getPositionMatrix();
            buffer.vertex(var4, this.floatVal, -this.floatVal, 0.0F).texture(0.0F, 1.0F).color(this.color.getRGB());
            buffer.vertex(var4, -this.floatVal, -this.floatVal, 0.0F).texture(1.0F, 1.0F).color(this.color4.getRGB());
            buffer.vertex(var4, -this.floatVal, this.floatVal, 0.0F).texture(1.0F, 0.0F).color(this.color3.getRGB());
            buffer.vertex(var4, this.floatVal, this.floatVal, 0.0F).texture(0.0F, 0.0F).color(this.color2.getRGB());
            ms.pop();
            return true;
         }
      }

      public Circle(Vec3d position, float size, int index, int lifeTime, int spawnDur, int dyingDur, String animMode) {
         this.position = position;
         this.size = size;
         this.index = index;
         this.lifeTime = lifeTime;
         this.spawnDur = spawnDur;
         this.dyingDur = dyingDur;
         this.animMode = animMode;
      }
   }
}
