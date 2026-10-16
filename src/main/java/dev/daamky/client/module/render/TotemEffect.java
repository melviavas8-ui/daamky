package dev.daamky.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.EntityHelper;
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
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Totem Effect",
   category = ModuleCategory.VISUALS,
   description = "Создает летающие золотые мини-тотемы при сносе тотема"
)
public class TotemEffect extends Module {
   public static final TotemEffect totemEffect = new TotemEffect();
   public static final Identifier class_2960 = net.minecraft.util.Identifier.of("minecraft", "textures/item/totem_of_undying.png");
   public final ModeSetting vortex = new ModeSetting("Physics").value("Burst").values("Burst", "Drop", "Fly", "Vortex");
   public final FloatSetting count = new FloatSetting("Count").value(24.0F).range(6.0F, 60.0F).step(1.0F);
   public final FloatSetting size = new FloatSetting("Size").value(0.28F).range(0.1F, 0.7F).step(0.02F);
   public final FloatSetting speed = new FloatSetting("Speed").value(0.65F).range(0.2F, 1.8F).step(0.05F);
   public final FloatSetting lifetime = new FloatSetting("Lifetime").value(38.0F).range(15.0F, 80.0F).step(1.0F);
   public final BooleanSetting rotate = new BooleanSetting("Rotate").value(true);
   public final BooleanSetting booleanSetting = new BooleanSetting("3D Model").value(true);
   public final BooleanSetting customColor = new BooleanSetting("Custom Color").value(false);
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(new Color(255, 220, 50, 255)).computeVisible(this.customColor::getValue);
   public final CopyOnWriteArrayList<TotemEffect.TotemParticle> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
   public static final ItemStack class_1799 = new ItemStack(Items.TOTEM_OF_UNDYING);
   public Immediate class_4598;

   public TotemEffect() {
      this.addSettings(
         new Setting[]{this.vortex, this.count, this.size, this.speed, this.lifetime, this.rotate, this.booleanSetting, this.customColor, this.color}
      );
   }

   public static TotemEffect getInstance() {
      return totemEffect;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.copyOnWriteArrayList.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public void handleClass_1297(Entity entity) {
      int var2 = (int)this.count.getValue().floatValue();
      float var3 = this.speed.getValue();
      float var4 = this.size.getValue();
      int var5 = (int)this.lifetime.getValue().floatValue();
      String var6 = this.vortex.getValue();
      double var7 = entity.getX();
      double var9 = entity.getY() + (double)entity.getHeight() * 0.55;
      double var11 = entity.getZ();

      for (int var13 = 0; var13 < var2; var13++) {
         float var17 = var4 * EntityHelper.computeRandom(0.85F, 1.25F);
         int var18 = (int)((float)var5 * EntityHelper.computeRandom(0.75F, 1.25F));
         float var19 = EntityHelper.computeRandom(-18.0F, 18.0F);
         float var14;
         float var15;
         float var16;
         switch (var6) {
            case "Drop":
               var14 = EntityHelper.computeRandom(-var3 * 0.8F, var3 * 0.8F);
               var15 = EntityHelper.computeRandom(var3 * 0.4F, var3 * 1.1F);
               var16 = EntityHelper.computeRandom(-var3 * 0.8F, var3 * 0.8F);
               break;
            case "Fly":
               var14 = EntityHelper.computeRandom(-var3 * 0.5F, var3 * 0.5F);
               var15 = EntityHelper.computeRandom(var3 * 0.1F, var3 * 0.6F);
               var16 = EntityHelper.computeRandom(-var3 * 0.5F, var3 * 0.5F);
               break;
            case "Vortex":
               double var28 = (Math.PI * 2) * (double)var13 / (double)var2 + (double)EntityHelper.computeRandom(-0.2F, 0.2F);
               double var29 = (double)EntityHelper.computeRandom(0.2F, 0.5F);
               var14 = (float)(Math.cos(var28) * (double)var3 * var29);
               var15 = EntityHelper.computeRandom(var3 * 0.3F, var3 * 0.9F);
               var16 = (float)(Math.sin(var28) * (double)var3 * var29);
               break;
            default:
               double var22 = Math.random() * Math.PI * 2.0;
               double var24 = Math.acos(2.0 * Math.random() - 1.0);
               double var26 = (double)(var3 * EntityHelper.computeRandom(0.5F, 1.3F));
               var14 = (float)(var26 * Math.sin(var24) * Math.cos(var22));
               var15 = (float)(var26 * Math.sin(var24) * Math.sin(var22) + (double)(var3 * 0.3F));
               var16 = (float)(var26 * Math.cos(var24));
         }

         this.copyOnWriteArrayList
            .add(
               new TotemEffect.TotemParticle(
                  var7 + (double)EntityHelper.computeRandom(-0.25F, 0.25F),
                  var9 + (double)EntityHelper.computeRandom(-0.25F, 0.25F),
                  var11 + (double)EntityHelper.computeRandom(-0.25F, 0.25F),
                  var14,
                  var15,
                  var16,
                  var17,
                  var18,
                  var19,
                  var6
               )
            );
      }
   }

   public void lambda$onEvent$2(Render3DEvent.Render3DEventData event) {
      if (this.isEnabled() && !this.copyOnWriteArrayList.isEmpty() && mc.player != null && mc.gameRenderer != null) {
         MatrixStack var2 = event.getMatrixStack();
         Camera var3 = mc.gameRenderer.getCamera();
         Vec3d var4 = var3.getPos();
         Frustum var5 = new Frustum(var2.peek().getPositionMatrix(), RenderSystem.getProjectionMatrix());
         var5.setPosition(var4.x, var4.y, var4.z);
         Render.renderEffect.handleBlend(var2);
         boolean var6 = false;
         if (this.booleanSetting.getValue()) {
            if (this.class_4598 == null) {
               this.class_4598 = VertexConsumerProvider.immediate(new BufferAllocator(2097152));
            }

            for (TotemEffect.TotemParticle var8 : this.copyOnWriteArrayList) {
               if (var8.intVal > 4 && !(var8.floatVal < 0.01F)) {
                  Box var9 = new Box(
                     var8.double4 - 0.5, var8.mweet - 0.5, var8.double5 - 0.5, var8.double4 + 0.5, var8.mweet + 0.5, var8.double5 + 0.5
                  );
                  if (var5.isVisible(var9)) {
                     double var10 = (double)EntityHelper.computeFloat(var8.doubleVal, var8.double4) - var4.x;
                     double var12 = (double)EntityHelper.computeFloat(var8.double2, var8.mweet) - var4.y;
                     double var14 = (double)EntityHelper.computeFloat(var8.double3, var8.double5) - var4.z;
                     double var16 = var10 * var10 + var12 * var12 + var14 * var14;
                     if (!(var16 > 4096.0)) {
                        var2.push();
                        var2.translate(var10, var12 + (double)var8.floatVal * 0.5, var14);
                        if (this.rotate.getValue()) {
                           float var18 = EntityHelper.computeFloat((double)var8.float2, (double)var8.float3);
                           var2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var18));
                        }

                        var2.scale(var8.floatVal, var8.floatVal, var8.floatVal);
                        mc.getItemRenderer()
                           .renderItem(class_1799, ModelTransformationMode.NONE, 15728880, OverlayTexture.DEFAULT_UV, var2, this.class_4598, mc.world, 0);
                        var2.pop();
                        var6 = true;
                     }
                  }
               }
            }

            if (var6) {
               this.class_4598.draw();
            }
         } else {
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderTexture(0, class_2960);
            BufferBuilder var24 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (TotemEffect.TotemParticle var26 : this.copyOnWriteArrayList) {
               if (var26.intVal > 4 && !(var26.floatVal < 0.01F)) {
                  Box var27 = new Box(
                     var26.double4 - 0.5, var26.mweet - 0.5, var26.double5 - 0.5, var26.double4 + 0.5, var26.mweet + 0.5, var26.double5 + 0.5
                  );
                  if (var5.isVisible(var27)) {
                     double var11 = (double)EntityHelper.computeFloat(var26.doubleVal, var26.double4) - var4.x;
                     double var13 = (double)EntityHelper.computeFloat(var26.double2, var26.mweet) - var4.y;
                     double var15 = (double)EntityHelper.computeFloat(var26.double3, var26.double5) - var4.z;
                     double var17 = var11 * var11 + var13 * var13 + var15 * var15;
                     if (!(var17 > 4096.0)) {
                        Color var19 = this.customColor.getValue() ? this.color.getValue() : new Color(255, 230, 80);
                        int var20 = MathHelper.clamp(var26.intVal, 0, 255) << 24 | var19.getRed() << 16 | var19.getGreen() << 8 | var19.getBlue();
                        float var21 = var26.floatVal * 0.5F;
                        var2.push();
                        var2.translate(var11, var13, var15);
                        if (this.rotate.getValue()) {
                           float var22 = EntityHelper.computeFloat((double)var26.float2, (double)var26.float3);
                           var2.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var22));
                        }

                        Matrix4f var28 = var2.peek().getPositionMatrix();
                        var24.vertex(var28, var21, -var21, 0.0F).texture(0.0F, 1.0F).color(var20);
                        var24.vertex(var28, -var21, -var21, 0.0F).texture(1.0F, 1.0F).color(var20);
                        var24.vertex(var28, -var21, var21, 0.0F).texture(1.0F, 0.0F).color(var20);
                        var24.vertex(var28, var21, var21, 0.0F).texture(0.0F, 0.0F).color(var20);
                        var2.pop();
                        var6 = true;
                     }
                  }
               }
            }

            if (var6) {
               try {
                  BufferRenderer.drawWithGlobalProgram(var24.end());
               } catch (Exception var23) {
               }
            }
         }

         Render.renderEffect.handleBlend2(var2);
      }
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      if (this.isEnabled() && !this.copyOnWriteArrayList.isEmpty() && mc.world != null) {
         ArrayList var2 = new ArrayList();

         for (TotemEffect.TotemParticle var4 : this.copyOnWriteArrayList) {
            var4.runMot();
            if (var4.checkLifetime()) {
               var2.add(var4);
            }
         }

         if (!var2.isEmpty()) {
            this.copyOnWriteArrayList.removeAll(var2);
         }

         if (this.copyOnWriteArrayList.size() > 400) {
            int var5 = this.copyOnWriteArrayList.size() - 400;

            for (int var6 = 0; var6 < var5 && !this.copyOnWriteArrayList.isEmpty(); var6++) {
               this.copyOnWriteArrayList.remove(0);
            }
         }
      }
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData event) {
      if (this.isEnabled() && event.isReceive() && mc.world != null && mc.player != null) {
         if (event.packet() instanceof EntityStatusS2CPacket var3 && var3.getStatus() == 35) {
            Entity var4 = var3.getEntity(mc.world);
            if (var4 != null && var4.distanceTo(mc.player) <= 64.0F) {
               this.handleClass_1297(var4);
            }
         }
      }
   }

   private static class TotemParticle {
      public double doubleVal;
      public double double2;
      public double double3;
      public double double4;
      public double mweet;
      public double double5;
      public float motX;
      public float motY;
      public float motZ;
      public final float size;
      public float floatVal;
      public final int lifetime;
      public int mexto;
      public float float2;
      public float float3;
      public final float rotSpeed;
      public final String mode;
      public int intVal;

      public TotemParticle(double x, double y, double z, float motX, float motY, float motZ, float size, int lifetime, float rotSpeed, String mode) {
         this.double4 = this.doubleVal = x;
         this.mweet = this.double2 = y;
         this.double5 = this.double3 = z;
         this.motX = motXx;
         this.motY = motYx;
         this.motZ = motZx;
         this.size = sizex;
         this.floatVal = 0.0F;
         this.lifetime = lifetimex;
         this.mexto = 0;
         this.float3 = this.float2 = EntityHelper.computeRandom(-180.0F, 180.0F);
         this.rotSpeed = rotSpeedx;
         this.mode = modex;
         this.intVal = 255;
      }

      public void runMot() {
         this.doubleVal = this.double4;
         this.double2 = this.mweet;
         this.double3 = this.double5;
         this.float2 = this.float3;
         this.double4 = this.double4 + (double)this.motX;
         this.mweet = this.mweet + (double)this.motY;
         this.double5 = this.double5 + (double)this.motZ;
         this.float3 = this.float3 + this.rotSpeed;
         if ("Drop".equals(this.mode) || "Burst".equals(this.mode)) {
            this.motY -= 0.016F;
            this.motX *= 0.94F;
            this.motZ *= 0.94F;
            this.motY *= 0.97F;
         } else if ("Fly".equals(this.mode)) {
            this.motY *= 0.94F;
            this.motX *= 0.95F;
            this.motZ *= 0.95F;
         } else if ("Vortex".equals(this.mode)) {
            this.motY += 0.005F;
            this.motX *= 0.96F;
            this.motZ *= 0.96F;
         }

         this.mexto++;
         float var1 = Math.min(1.0F, (float)this.mexto / 5.0F);
         float var2 = (float)this.mexto / (float)this.lifetime;
         if (var2 < 0.65F) {
            this.floatVal = this.size * var1;
            this.intVal = (int)(255.0F * var1);
         } else {
            float var3 = 1.0F - (var2 - 0.65F) / 0.35F;
            this.floatVal = this.size * Math.max(0.0F, var3);
            this.intVal = (int)(255.0F * Math.max(0.0F, var3));
         }
      }

      public boolean checkLifetime() {
         return this.mexto >= this.lifetime || this.intVal <= 2;
      }
   }
}
