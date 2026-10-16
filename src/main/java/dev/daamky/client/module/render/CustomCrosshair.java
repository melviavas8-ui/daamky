package dev.daamky.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.Subscription;
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
import java.util.function.Supplier;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Custom Crosshair",
   category = ModuleCategory.VISUALS,
   description = "Заменяет прицел на кастомный"
)
public class CustomCrosshair extends Module {
   public static final CustomCrosshair customCrosshair = new CustomCrosshair();
   public final ModeSetting dot = new ModeSetting("Style").value("Cross").values("Cross", "Arrow", "Dot");
   public final Supplier<Boolean> supplier = this::lambda$new$0;
   public final Supplier<Boolean> supplier2 = this::lambda$new$1;
   public final Supplier<Boolean> supplier3 = this::lambda$new$2;
   public final FloatSetting size = new FloatSetting("Size").value(8.0F).range(1.0F, 30.0F).step(0.5F).visibleWhen(this::lambda$new$3);
   public final FloatSetting thickness = new FloatSetting("Thickness").value(2.0F).range(0.5F, 8.0F).step(0.5F).visibleWhen(this::lambda$new$4);
   public final FloatSetting length = new FloatSetting("Length").value(8.0F).range(2.0F, 30.0F).step(0.5F).visibleWhen(this.supplier);
   public final FloatSetting window = new FloatSetting("Window").value(4.0F).range(0.0F, 20.0F).step(0.5F).visibleWhen(this.supplier);
   public final BooleanSetting dynamic = new BooleanSetting("Dynamic").value(true).visibleWhen(this.supplier);
   public final FloatSetting expansionSpeed = new FloatSetting("Expansion Speed").value(10.0F).range(1.0F, 20.0F).step(0.5F).visibleWhen(this::lambda$new$5);
   public final FloatSetting expansionStrength = new FloatSetting("Expansion Strength")
      .value(2.0F)
      .range(1.0F, 5.0F)
      .step(0.1F)
      .visibleWhen(this::lambda$new$6);
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(Color.WHITE);
   public float floatVal = 4.0F;

   public CustomCrosshair() {
      this.addSettings(
         new Setting[]{this.dot, this.size, this.thickness, this.length, this.window, this.dynamic, this.expansionSpeed, this.expansionStrength, this.color}
      );
   }

   @Override
   public void onEnable() {
      this.floatVal = this.window.getValue();
   }

   @Override
   public void onEvent() {
      Subscription var1 = Render2DEvent.getInstance().subscribe(new EventListener<>(1, this::lambda$onEvent$7));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static void handleWindow(MatrixStack matrices, float cx, float cy) {
      CustomCrosshair var3 = getInstance();
      float var4 = var3.window.getValue();
      var3.handleRgb(matrices, cx, cy, var4);
   }

   public void handleRgb(MatrixStack matrices, float cx, float cy, float dynamicGap) {
      int var5 = this.color.getValue().getRGB();
      this.handleVal(matrices, cx, cy, dynamicGap, false, 0.0F, var5);
   }

   public void handleVal(MatrixStack matrices, float cx, float cy, float dynamicGap, boolean isOutline, float ot, int colorRGB) {
      String var8 = this.dot.getValue();
      switch (var8) {
         case "Cross":
            this.handleRound(matrices, cx, cy, isOutline, ot, colorRGB, dynamicGap);
            break;
         case "Arrow":
            this.handleRound(matrices, cx, cy, isOutline, ot, colorRGB);
            break;
         case "Dot":
            this.initInit(matrices, cx, cy, isOutline, ot, colorRGB);
      }
   }

   public void handleRound(MatrixStack matrices, float cx, float cy, boolean isOutline, float ot, int colorRGB, float currentGap) {
      float var8 = (float)Math.round(this.thickness.getValue());
      float var9 = (float)Math.round(this.length.getValue());
      float var10 = (float)Math.round(currentGap);
      float var11 = (float)Math.round(ot);
      float var12 = var8 / 2.0F;
      float var13 = (float)Math.round(cx - var12) + var12;
      float var14 = (float)Math.round(cy - var12) + var12;
      this.handleVal(matrices, var13 - var12, var14 - var10 - var9, var8, var9, var11, colorRGB);
      this.handleVal(matrices, var13 - var12, var14 + var10, var8, var9, var11, colorRGB);
      this.handleVal(matrices, var13 - var10 - var9, var14 - var12, var9, var8, var11, colorRGB);
      this.handleVal(matrices, var13 + var10, var14 - var12, var9, var8, var11, colorRGB);
   }

   public void handleRound(MatrixStack matrices, float cx, float cy, boolean isOutline, float ot, int colorRGB) {
      float var7 = (float)Math.round(this.size.getValue());
      float var8 = (float)Math.round(this.thickness.getValue());
      float var9 = var8 / 2.0F;
      float var10 = (float)Math.round(ot);
      float var11 = (float)Math.round(var7 / 3.0F);
      float var12 = (float)Math.round(cx - var9) + var9;
      float var13 = (float)Math.round(cy - var9) + var9;
      matrices.push();
      matrices.translate(var12, var13 - var11, 0.0F);
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45.0F));
      this.handleVal(matrices, -var9, 0.0F, var8, var7, var10, colorRGB);
      matrices.pop();
      matrices.push();
      matrices.translate(var12, var13 - var11, 0.0F);
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0F));
      this.handleVal(matrices, -var9, 0.0F, var8, var7, var10, colorRGB);
      matrices.pop();
   }

   public void initInit(MatrixStack matrices, float cx, float cy, boolean isOutline, float ot, int colorRGB) {
      float var7 = this.size.getValue();
      float var8 = isOutline ? var7 + ot * 2.0F : var7;
      float var9 = var8 / 2.0F;
      Color var10 = new Color(colorRGB, true);
      Render.rectRenderer.handleRgb(matrices, cx - var9, cy - var9, var8, var8, var9, var10);
   }

   public void handleVal(MatrixStack matrices, float x, float y, float w, float h, float ot, int colorRGB) {
      if (ot > 0.0F) {
         this.handleBlend(matrices, x - ot, y - ot, x + w + ot * 2.0F, y + h + ot * 2.0F, colorRGB);
      } else {
         this.handleBlend(matrices, x, y, x + w, y + h, colorRGB);
      }
   }

   public void handleBlend(MatrixStack matrices, float x1, float y1, float x2, float y2, int color) {
      float var7 = (float)(colorx >> 24 & 0xFF) / 255.0F;
      float var8 = (float)(colorx >> 16 & 0xFF) / 255.0F;
      float var9 = (float)(colorx >> 8 & 0xFF) / 255.0F;
      float var10 = (float)(colorx & 0xFF) / 255.0F;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder var11 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      Matrix4f var12 = matrices.peek().getPositionMatrix();
      var11.vertex(var12, x1, y2, 0.0F).color(var8, var9, var10, var7);
      var11.vertex(var12, x2, y2, 0.0F).color(var8, var9, var10, var7);
      var11.vertex(var12, x2, y1, 0.0F).color(var8, var9, var10, var7);
      var11.vertex(var12, x1, y1, 0.0F).color(var8, var9, var10, var7);
      BufferRenderer.drawWithGlobalProgram(var11.end());
      RenderSystem.disableBlend();
   }

   public static CustomCrosshair getInstance() {
      return customCrosshair;
   }

   public void lambda$onEvent$7(Render2DEvent.Render2DEventData event) {
      if (mc.currentScreen == null && mc.player != null) {
         if (!mc.options.hudHidden) {
            DrawContext var3 = event.getContext();
            float var4 = (float)mc.getWindow().getScaledWidth();
            float var5 = (float)mc.getWindow().getScaledHeight();
            float var2 = this.window.getValue();
            if (this.dynamic.getValue() && this.dot.is("Cross")) {
               float var7 = mc.player.getAttackCooldownProgress(mc.getRenderTickCounter().getTickDelta(false));
               float var8 = this.expansionStrength.getValue();
               float var9 = Math.max(var2, 4.0F);
               float var6 = var2 + var9 * (var8 - 1.0F) * (1.0F - var7);
               float var10 = this.expansionSpeed.getValue();
               float var11 = MathHelper.clamp(var10 * 0.035F, 0.05F, 0.95F);
               this.floatVal = this.floatVal + (var6 - this.floatVal) * var11;
            } else {
               this.floatVal = var2;
            }

            int var12 = (int)(var4 / 2.0F);
            int var13 = (int)(var5 / 2.0F);
            this.handleRgb(var3.getMatrices(), (float)var12, (float)var13, this.floatVal);
         }
      }
   }

   public Boolean lambda$new$6() {
      return this.dynamic.getValue() && this.supplier.get();
   }

   public Boolean lambda$new$5() {
      return this.dynamic.getValue() && this.supplier.get();
   }

   public Boolean lambda$new$4() {
      return this.supplier.get() || this.supplier2.get();
   }

   public Boolean lambda$new$3() {
      return this.supplier2.get() || this.supplier3.get();
   }

   public Boolean lambda$new$2() {
      return this.dot.is("Dot");
   }

   public Boolean lambda$new$1() {
      return this.dot.is("Arrow");
   }

   public Boolean lambda$new$0() {
      return this.dot.is("Cross");
   }
}
