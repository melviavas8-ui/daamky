package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Block Tweaks",
   category = ModuleCategory.VISUALS,
   description = "Улучшает внешний вид выделенного блока"
)
public class BlockTweaks extends Module {
   public static final BlockTweaks blockTweaks = new BlockTweaks();
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("blocktweaks/liquid"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public static final ShaderProgramKey class_101562 = new ShaderProgramKey(InputHelper.getShader("blocktweaks/geometric"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public final ModeSetting geometric = new ModeSetting("Mode").value("Default").values("Default", "Nebula", "Liquid", "Geometric");
   public final ModeSetting both = new ModeSetting("Style").value("Both").values("Outline", "Fill", "Both");
   public final BooleanSetting smooth = new BooleanSetting("Smooth").value(true);
   public final FloatSetting smoothSpeed = new FloatSetting("Smooth Speed   ").value(15.0F).range(1.0F, 30.0F).step(1.0F).visibleWhen(this.smooth::getValue);
   public final FloatSetting lineWidth = new FloatSetting("Line Width").value(2.0F).range(0.1F, 5.0F).step(0.1F);
   public final FloatSetting fillAlpha = new FloatSetting("Fill Alpha").value(0.3F).range(0.01F, 1.0F).step(0.01F);
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(new Color(80, 160, 255)).computeVisible(this::lambda$new$0);
   public final Supplier<Boolean> mcuejc = this::lambda$new$1;
   public final ColorSetting color1 = new ColorSetting("Color 1").computeColorSetting(new Color(80, 160, 255)).computeVisible(this.mcuejc);
   public final ColorSetting color2 = new ColorSetting("Color 2").computeColorSetting(new Color(200, 60, 255)).computeVisible(this.mcuejc);
   public final FloatSetting flowSpeed = new FloatSetting("Flow Speed").value(1.0F).range(0.2F, 3.0F).step(0.1F).visibleWhen(this.mcuejc);
   public final FloatSetting pulse = new FloatSetting("Pulse").value(0.3F).range(0.0F, 0.8F).step(0.05F).visibleWhen(this.mcuejc);
   public final FloatSetting intensity = new FloatSetting("Intensity").value(0.7F).range(0.2F, 1.0F).step(0.05F).visibleWhen(this.mcuejc);
   public long longVal = System.currentTimeMillis();
   public long long2 = System.currentTimeMillis();
   public final List<BlockTweaks.AnimBox> list = new ArrayList<>();
   public final List<Box> list2 = new ArrayList<>();

   public BlockTweaks() {
      this.addSettings(
         new Setting[]{
            this.geometric,
            this.both,
            this.smooth,
            this.smoothSpeed,
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

   @Override
   public void onEnable() {
      this.longVal = System.currentTimeMillis();
      this.long2 = System.currentTimeMillis();
      this.list.clear();
   }

   @Override
   public void onDisable() {
      this.list.clear();
      this.list2.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void handleColor(MatrixStack ms, Box bb, float boxAlpha) {
      Vec3d var6 = mc.getEntityRenderDispatcher().camera.getPos();
      ms.push();
      ms.translate(-var6.x, -var6.y, -var6.z);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);
      RenderSystem.lineWidth(this.lineWidth.getValue());
      boolean var7 = this.both.is("Outline") || this.both.is("Both");
      boolean var8 = this.both.is("Fill") || this.both.is("Both");
      boolean var9 = !this.geometric.is("Default");
      if (var9) {
         ShaderProgram var11 = null;
         if (this.geometric.is("Nebula")) {
            var11 = RenderSystem.setShader(Chams.class_10156);
         }

         if (this.geometric.is("Liquid")) {
            var11 = RenderSystem.setShader(class_10156);
         }

         if (this.geometric.is("Geometric")) {
            var11 = RenderSystem.setShader(class_101562);
         }

         if (var11 != null) {
            float var12 = (float)mc.getWindow().getWidth();
            float var13 = (float)mc.getWindow().getHeight();
            if (var11.getUniform("uScreenSize") != null) {
               var11.getUniform("uScreenSize").set(var12, var13);
            }

            if (var11.getUniform("uTime") != null) {
               var11.getUniform("uTime").set((float)(System.currentTimeMillis() - this.longVal) / 1000.0F);
            }

            Color var14 = this.color1.getValue();
            Color var15 = this.color2.getValue();
            if (var11.getUniform("uColor1") != null) {
               var11.getUniform("uColor1").set((float)var14.getRed() / 255.0F, (float)var14.getGreen() / 255.0F, (float)var14.getBlue() / 255.0F);
            }

            if (var11.getUniform("uColor2") != null) {
               var11.getUniform("uColor2").set((float)var15.getRed() / 255.0F, (float)var15.getGreen() / 255.0F, (float)var15.getBlue() / 255.0F);
            }

            if (var11.getUniform("uFlowSpeed") != null) {
               var11.getUniform("uFlowSpeed").set(this.flowSpeed.getValue());
            }

            if (var11.getUniform("uPulse") != null) {
               var11.getUniform("uPulse").set(this.pulse.getValue());
            }

            if (var11.getUniform("uIntensity") != null) {
               var11.getUniform("uIntensity").set(this.intensity.getValue());
            }
         }
      } else {
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      }

      Matrix4f var18 = ms.peek().getPositionMatrix();
      if (var8) {
         Color var19 = var9 ? Color.WHITE : this.color.getValue();
         int var21 = var19.getRed();
         int var23 = var19.getGreen();
         int var25 = var19.getBlue();
         int var5 = var9
            ? (int)(255.0F * this.fillAlpha.getValue() * boxAlpha)
            : (int)((float)this.color.getValue().getAlpha() * this.fillAlpha.getValue() * boxAlpha);
         BufferBuilder var4 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         this.handleVal2(var4, var18, bb, var21, var23, var25, var5);
         BufferRenderer.drawWithGlobalProgram(var4.end());
      }

      if (var7) {
         Color var20 = var9 ? Color.WHITE : this.color.getValue();
         int var22 = var20.getRed();
         int var24 = var20.getGreen();
         int var26 = var20.getBlue();
         int var17 = var9 ? (int)(255.0F * boxAlpha) : (int)((float)var20.getAlpha() * boxAlpha);
         BufferBuilder var16 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
         this.handleVal(var16, var18, bb, var22, var24, var26, var17);
         BufferRenderer.drawWithGlobalProgram(var16.end());
      }

      RenderSystem.lineWidth(1.0F);
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      ms.pop();
   }

   public void handleVal(BufferBuilder buf, Matrix4f mat, Box bb, int r, int g, int b, int a) {
      float var8 = (float)bb.minX;
      float var9 = (float)bb.minY;
      float var10 = (float)bb.minZ;
      float var11 = (float)bb.maxX;
      float var12 = (float)bb.maxY;
      float var13 = (float)bb.maxZ;
      buf.vertex(mat, var8, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var13).color(r, g, b, a);
   }

   public void handleVal2(BufferBuilder buf, Matrix4f mat, Box bb, int r, int g, int b, int a) {
      float var8 = (float)bb.minX;
      float var9 = (float)bb.minY;
      float var10 = (float)bb.minZ;
      float var11 = (float)bb.maxX;
      float var12 = (float)bb.maxY;
      float var13 = (float)bb.maxZ;
      buf.vertex(mat, var8, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var8, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var8, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var10).color(r, g, b, a);
      buf.vertex(mat, var11, var12, var13).color(r, g, b, a);
      buf.vertex(mat, var11, var9, var13).color(r, g, b, a);
   }

   public static BlockTweaks getInstance() {
      return blockTweaks;
   }

   public void lambda$onEvent$3(Render3DEvent.Render3DEventData event) {
      long var2 = System.currentTimeMillis();
      float var4 = (float)(var2 - this.long2) / 1000.0F;
      this.long2 = var2;
      if (var4 > 0.1F) {
         var4 = 0.1F;
      }

      this.list2.clear();
      if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
         AttackAura var9 = AttackAura.getInstance();
         boolean var10 = var9.isEnabled() && var9.class_1309 instanceof PlayerEntity;
         VoxelShape var5;
         BlockHitResult var6;
         BlockPos var7;
         BlockState var8;
         if (!var10
            && !(var8 = mc.world.getBlockState(var7 = (var6 = (BlockHitResult)mc.crosshairTarget).getBlockPos())).isAir()
            && !(var5 = var8.getOutlineShape(mc.world, var7)).isEmpty()) {
            for (Box var13 : var5.getBoundingBoxes()) {
               this.list2.add(var13.offset(var7));
            }
         }
      }

      if (!this.smooth.getValue()) {
         this.list.clear();

         for (Box var18 : this.list2) {
            this.list.add(new BlockTweaks.AnimBox(var18));
         }
      } else {
         while (this.list.size() < this.list2.size()) {
            if (this.list.isEmpty()) {
               this.list.add(new BlockTweaks.AnimBox(this.list2.get(this.list.size())));
            } else {
               this.list.add(new BlockTweaks.AnimBox(this.list.get(0).initInit()));
            }
         }

         float var14 = this.smoothSpeed.getValue();

         for (int var17 = 0; var17 < this.list.size(); var17++) {
            BlockTweaks.AnimBox var20 = this.list.get(var17);
            if (var17 < this.list2.size()) {
               var20.handleMin(this.list2.get(var17), var4, var14);
            } else {
               var20.handleMin(null, var4, var14);
            }
         }

         this.list.removeIf(BlockTweaks::lambda$onEvent$2);
      }

      for (BlockTweaks.AnimBox var19 : this.list) {
         this.handleColor(event.getMatrixStack(), var19.initInit().expand(0.002), var19.floatVal);
      }
   }

   public static boolean lambda$onEvent$2(BlockTweaks.AnimBox ab) {
      return ab.floatVal < 0.01F;
   }

   public Boolean lambda$new$1() {
      return !this.geometric.is("Default");
   }

   public Boolean lambda$new$0() {
      return this.geometric.is("Default");
   }

   private static class AnimBox {
      public double doubleVal;
      public double double2;
      public double double3;
      public double mifezol;
      public double double4;
      public double double5;
      public float floatVal;

      public AnimBox(Box init) {
         if (init != null) {
            this.doubleVal = init.minX;
            this.double2 = init.minY;
            this.double3 = init.minZ;
            this.mifezol = init.maxX;
            this.double4 = init.maxY;
            this.double5 = init.maxZ;
            this.floatVal = 1.0F;
         }
      }

      public void handleMin(Box target, float dt, float speed) {
         float var4 = Math.min(1.0F, speed * dt);
         if (target != null) {
            this.doubleVal = this.doubleVal + (target.minX - this.doubleVal) * (double)var4;
            this.double2 = this.double2 + (target.minY - this.double2) * (double)var4;
            this.double3 = this.double3 + (target.minZ - this.double3) * (double)var4;
            this.mifezol = this.mifezol + (target.maxX - this.mifezol) * (double)var4;
            this.double4 = this.double4 + (target.maxY - this.double4) * (double)var4;
            this.double5 = this.double5 + (target.maxZ - this.double5) * (double)var4;
            this.floatVal = this.floatVal + (1.0F - this.floatVal) * var4;
         } else {
            double var5 = (this.doubleVal + this.mifezol) / 2.0;
            double var7 = (this.double2 + this.double4) / 2.0;
            double var9 = (this.double3 + this.double5) / 2.0;
            this.doubleVal = this.doubleVal + (var5 - this.doubleVal) * (double)var4;
            this.double2 = this.double2 + (var7 - this.double2) * (double)var4;
            this.double3 = this.double3 + (var9 - this.double3) * (double)var4;
            this.mifezol = this.mifezol + (var5 - this.mifezol) * (double)var4;
            this.double4 = this.double4 + (var7 - this.double4) * (double)var4;
            this.double5 = this.double5 + (var9 - this.double5) * (double)var4;
            this.floatVal = this.floatVal + (0.0F - this.floatVal) * var4;
         }
      }

      public Box initInit() {
         return new Box(this.doubleVal, this.double2, this.double3, this.mifezol, this.double4, this.double5);
      }
   }
}
