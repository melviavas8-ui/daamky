package dev.daamky.client.module.movement;

import com.mojang.authlib.GameProfile;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.InputTransformEvent;
import dev.daamky.client.event.MouseDeltaEvent;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.Profile;
import dev.daamky.client.internal.Render;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "FreeCam",
   category = ModuleCategory.MOTION,
   description = "Свободная камера для осмотра окружения"
)
public class FreeCam extends Module {
   public static final FreeCam freeCam = new FreeCam();
   public final FloatSetting speed = new FloatSetting("Speed").value(3.0F).range(0.1F, 15.0F).step(0.1F);
   public final BooleanSetting displayCoords = new BooleanSetting("Display Coords").value(true);
   public Vec3d class_243 = null;
   public Vec3d class_2432 = null;
   public float floatVal = 0.0F;
   public float float2 = 0.0F;
   public Profile profile = null;
   public final AnimatedValue animatedValue = new AnimatedValue();

   public FreeCam() {
      this.addSettings(new Setting[]{this.speed, this.displayCoords});
   }

   public static FreeCam getInstance() {
      return freeCam;
   }

   public Vec3d initInit(float tickDelta) {
      if (this.class_243 == null) {
         return null;
      } else {
         return this.class_2432 == null
            ? this.class_243
            : new Vec3d(
               MathHelper.lerp((double)tickDelta, this.class_2432.x, this.class_243.x),
               MathHelper.lerp((double)tickDelta, this.class_2432.y, this.class_243.y),
               MathHelper.lerp((double)tickDelta, this.class_2432.z, this.class_243.z)
            );
      }
   }

   public float[] getFloats() {
      return new float[]{this.floatVal, this.floatVal2};
   }

   @Override
   public void onEvent() {
      Subscription var1 = MouseDeltaEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = InputTransformEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var4 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      Subscription var5 = Render2DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$4));
      this.addEventListeners(new Subscription[]{var1, var2, var3, var4, var5});
   }

   public void runSpeed() {
      if (mc.player != null && this.class_243 != null) {
         this.class_2432 = this.class_243;
         float var1 = this.speed.getValue() * 0.15F;
         float var2 = 0.0F;
         float var3 = 0.0F;
         if (mc.options.forwardKey.isPressed()) {
            var2++;
         }

         if (mc.options.backKey.isPressed()) {
            var2--;
         }

         if (mc.options.leftKey.isPressed()) {
            var3++;
         }

         if (mc.options.rightKey.isPressed()) {
            var3--;
         }

         double var4 = 0.0;
         if (mc.options.jumpKey.isPressed()) {
            var4 += (double)var1;
         }

         if (mc.options.sneakKey.isPressed()) {
            var4 -= (double)var1;
         }

         double[] var6 = computeRadians(this.floatVal, var2, var3, (double)var1);
         this.class_243 = this.class_243.add(var6[0], var4, var6[1]);
         mc.player.setVelocity(0.0, mc.player.getVelocity().y, 0.0);
      }
   }

   public static double[] computeRadians(float yaw, float forward, float sideways, double distance) {
      if (forward != 0.0F) {
         if (sideways > 0.0F) {
            yaw += forward > 0.0F ? -45.0F : 45.0F;
         } else if (sideways < 0.0F) {
            yaw += forward > 0.0F ? 45.0F : -45.0F;
         }

         sideways = 0.0F;
         forward = forward > 0.0F ? 1.0F : -1.0F;
      }

      double var5 = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double var7 = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double var9 = (double)forward * distance * var7 + (double)sideways * distance * var5;
      double var11 = (double)forward * distance * var5 - (double)sideways * distance * var7;
      return new double[]{var9, var11};
   }

   public void initFormat(MatrixStack ms) {
      if (mc.player != null && this.class_243 != null) {
         boolean var2 = this.isEnabled() && this.displayCoords.getValue();
         this.animatedValue.update();
         this.animatedValue.animateTo(var2 ? 1.0 : 0.0, 180L, Easing.CUBIC_OUT);
         float var3 = (float)this.animatedValue.getValue();
         if (!(var3 <= 0.01F)) {
            double var4 = this.class_243.x;
            double var6 = this.class_243.y;
            double var8 = this.class_243.z;
            double var10 = 0.0;
            if (this.profile != null) {
               double var12 = var4 - this.profile.getX();
               double var14 = var6 - this.profile.getY();
               double var16 = var8 - this.profile.getZ();
               var10 = Math.sqrt(var12 * var12 + var14 * var14 + var16 * var16);
            }

            String var27 = String.format(Locale.US, "Cam: %.1f, %.1f, %.1f", var4, var6, var8);
            String var13 = String.format(Locale.US, "(%.1fm)", var10);
            float var28 = 5.5F;
            float var15 = FontBootstrap.mdoswa.computeFloat(var27, var28) + FontBootstrap.clientFont11.computeFloat(" " + var13, var28);
            float var29 = 5.0F;
            float var17 = var15 + var29 * 2.0F + 12.0F;
            float var18 = 15.0F;
            float var19 = (float)mc.getWindow().getScaledWidth();
            float var20 = (var19 - var17) / 2.0F;
            float var21 = 18.0F;
            ms.push();
            float var22 = var3 * 255.0F;
            Color var23 = new Color(12, 12, 16, (int)(var22 * 0.85F));
            Render.shaderProgram.handleRgb(ms, var20, var21, var17, var18, 4.0F, var23);
            Render.rectRenderer.handleRgb(ms, var20, var21, var17, var18, 4.0F, InterfaceMetrics.computeColor3((int)(var22 * 0.4F)));
            FontBootstrap.clientFont15.handleColor(ms, "T", var20 + var29, var21 + var18 / 2.0F - 3.0F, 6.0F, InterfaceMetrics.computeColor3((int)var22));
            float var24 = var20 + var29 + 10.0F;
            float var25 = var21 + var18 / 2.0F - var28 / 2.0F;
            FontBootstrap.mdoswa.handleColor(ms, var27, var24, var25, var28, new Color(255, 255, 255, (int)var22));
            float var26 = FontBootstrap.mdoswa.computeFloat(var27, var28);
            FontBootstrap.clientFont11.handleColor(ms, " " + var13, var24 + var26, var25, var28, InterfaceMetrics.computeColor4((int)var22));
            ms.pop();
         }
      }
   }

   @Override
   public void onEnable() {
      if (mc.player != null && mc.world != null) {
         this.class_2432 = this.class_243 = mc.player.getEyePos();
         this.floatVal = mc.player.getYaw();
         this.floatVal2 = mc.player.getPitch();
         this.profile = new Profile(mc.world, new GameProfile(UUID.randomUUID(), mc.getSession().getUsername()));
         this.profile.copyFrom(mc.player);
         this.profile.copyPositionAndRotation(mc.player);
         this.profile.run();
      }
   }

   @Override
   public void onDisable() {
      if (this.profile != null) {
         if (mc.world != null) {
            this.profile.run2();
         }

         this.profile = null;
      }

      this.class_243 = null;
      this.class_2432 = null;
   }

   public void lambda$onEvent$4(Render2DEvent.Render2DEventData event) {
      this.initFormat(event.getMatrixStack());
   }

   public void lambda$onEvent$3(PacketEvent.PacketEventData event) {
      if (event.packet() instanceof PlayerRespawnS2CPacket || event.packet() instanceof GameJoinS2CPacket) {
         this.setEnabled(false);
      }
   }

   public void lambda$onEvent$2(UpdateEvent event) {
      this.runSpeed();
   }

   public void lambda$onEvent$1(InputTransformEvent event) {
      if (this.isEnabled()) {
         event.setForward(0.0F);
         event.setStrafe(0.0F);
      }
   }

   public void lambda$onEvent$0(MouseDeltaEvent event) {
      if (this.isEnabled() && mc.player != null) {
         this.floatVal = this.floatVal + (float)(event.getYaw() * 0.15);
         this.floatVal2 = MathHelper.clamp(this.floatVal2 + (float)(event.getPitch() * 0.15), -90.0F, 90.0F);
         event.setCancelled(true);
      }
   }
}
