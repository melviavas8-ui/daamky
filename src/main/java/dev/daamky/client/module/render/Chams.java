package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(
   name = "Chams",
   category = ModuleCategory.VISUALS,
   description = "Делает модельки игроков видимыми сквозь стены"
)
public class Chams extends Module {
   public static final Chams chams = new Chams();
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("chams/nebula"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public final ModeSetting nebula = new ModeSetting("Shader").value("Nebula").values("Lines", "Nebula");
   public final FloatSetting lineWidth = new FloatSetting("Line Width").value(2.0F).range(0.1F, 5.0F).step(0.1F).visibleWhen(this::lambda$new$0);
   public final ColorSetting color1 = new ColorSetting("Color 1").computeColorSetting(new Color(80, 160, 255));
   public final ColorSetting color2 = new ColorSetting("Color 2").computeColorSetting(new Color(200, 60, 255));
   public final FloatSetting flowSpeed = new FloatSetting("Flow Speed").value(1.0F).range(0.2F, 3.0F).step(0.1F);
   public final FloatSetting pulse = new FloatSetting("Pulse").value(0.3F).range(0.0F, 0.8F).step(0.05F);
   public final FloatSetting intensity = new FloatSetting("Intensity").value(0.7F).range(0.2F, 1.0F).step(0.05F);
   public final BooleanSetting players = new BooleanSetting("Players").value(true);
   public final BooleanSetting friends = new BooleanSetting("Friends").value(true);
   public final BooleanSetting mobs = new BooleanSetting("Mobs").value(false);
   public long longVal = System.currentTimeMillis();
   public static final Chams.ChamsVertexConsumer chamsVertexConsumer = new Chams.ChamsVertexConsumer();

   public Chams() {
      this.addSettings(
         new Setting[]{
            this.nebula, this.lineWidth, this.color1, this.color2, this.flowSpeed, this.pulse, this.intensity, this.players, this.friends, this.mobs
         }
      );
   }

   @Override
   public void onEnable() {
      this.longVal = System.currentTimeMillis();
   }

   @Override
   public void onEvent() {
   }

   public static void handleColor(MatrixStack matrices, EntityModel<?> model, int light, int overlay) {
      MinecraftClient var5 = MinecraftClient.getInstance();
      if (var5.getWindow() != null) {
         ShaderProgramKey var6 = class_10156;
         BufferBuilder var7 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         chamsVertexConsumer.setDelegate(var7);
         model.render(matrices, chamsVertexConsumer, light, overlay, -1);
         BuiltBuffer var8 = var7.end();
         if (var8 != null) {
            RenderSystem.depthMask(false);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
            RenderSystem.disableCull();
            ShaderProgram var9 = RenderSystem.setShader(var6);
            if (var9 != null) {
               float var10 = (float)var5.getWindow().getWidth();
               float var11 = (float)var5.getWindow().getHeight();
               if (var9.getUniform("uScreenSize") != null) {
                  var9.getUniform("uScreenSize").set(var10, var11);
               }

               if (var9.getUniform("uTime") != null) {
                  var9.getUniform("uTime").set((float)(System.currentTimeMillis() - chams.longVal) / 1000.0F);
               }

               Color var12 = chams.color1.getValue();
               Color var13 = chams.color2.getValue();
               if (var9.getUniform("uColor1") != null) {
                  var9.getUniform("uColor1").set((float)var12.getRed() / 255.0F, (float)var12.getGreen() / 255.0F, (float)var12.getBlue() / 255.0F);
               }

               if (var9.getUniform("uColor2") != null) {
                  var9.getUniform("uColor2").set((float)var13.getRed() / 255.0F, (float)var13.getGreen() / 255.0F, (float)var13.getBlue() / 255.0F);
               }

               if (var9.getUniform("uFlowSpeed") != null) {
                  var9.getUniform("uFlowSpeed").set(chams.flowSpeed.getValue());
               }

               if (var9.getUniform("uPulse") != null) {
                  var9.getUniform("uPulse").set(chams.pulse.getValue());
               }

               if (var9.getUniform("uIntensity") != null) {
                  var9.getUniform("uIntensity").set(chams.intensity.getValue());
               }
            }

            boolean var4;
            if (var4 = chams.nebula.is("Lines")) {
               GL11.glPolygonMode(1032, 6913);
               RenderSystem.lineWidth(chams.lineWidth.getValue());
               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            }

            BufferRenderer.drawWithGlobalProgram(var8);
            if (var4) {
               GL11.glPolygonMode(1032, 6914);
               RenderSystem.lineWidth(1.0F);
            }

            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         }
      }
   }

   public ModeSetting getNebula() {
      return this.nebula;
   }

   public FloatSetting getLineWidth() {
      return this.lineWidth;
   }

   public ColorSetting getColor1() {
      return this.color1;
   }

   public ColorSetting getColor2() {
      return this.color2;
   }

   public FloatSetting getFlowSpeed() {
      return this.flowSpeed;
   }

   public FloatSetting getPulse() {
      return this.pulse;
   }

   public FloatSetting getIntensity() {
      return this.intensity;
   }

   public BooleanSetting getPlayers() {
      return this.players;
   }

   public BooleanSetting getFriends() {
      return this.friends;
   }

   public BooleanSetting getMobs() {
      return this.mobs;
   }

   public long getLong() {
      return this.longVal;
   }

   public static Chams getInstance() {
      return chams;
   }

   public Boolean lambda$new$0() {
      return this.nebula.is("Lines");
   }

   private static class ChamsVertexConsumer implements VertexConsumer {
      public BufferBuilder delegate;

      public ChamsVertexConsumer() {
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
