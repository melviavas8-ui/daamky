package dev.daamky.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.mixin.WorldRendererAccessor;
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
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@ModuleInfo(
   name = "Skeleton ESP",
   category = ModuleCategory.VISUALS,
   description = "Рисует скелет сущностей сквозь стены"
)
public class SkeletonESP extends Module {
   public static final SkeletonESP skeletonESP = new SkeletonESP();
   public final BooleanSetting self = new BooleanSetting("Self").value(true);
   public final BooleanSetting others = new BooleanSetting("Others").value(true);
   public final BooleanSetting friends = new BooleanSetting("Friends").value(true);
   public final BooleanSetting invisibles = new BooleanSetting("Invisibles").value(true);
   public final BooleanSetting firstPerson = new BooleanSetting("First person").value(false);
   public final MultiBooleanSetting multiBooleanSetting = new MultiBooleanSetting("Targets")
      .value(this.self, this.others, this.friends, this.invisibles, this.firstPerson);
   public final BooleanSetting throughWalls = new BooleanSetting("Through walls").value(true);
   public final FloatSetting lineWidth = new FloatSetting("Line width").value(1.5F).range(0.5F, 4.0F).step(0.1F);
   public final FloatSetting alpha = new FloatSetting("Alpha").value(0.8F).range(0.1F, 1.0F).step(0.05F);
   public final ModeSetting rainbow = new ModeSetting("Color").value("Theme").values("Theme", "Custom", "Rainbow");
   public final ColorSetting customColor = new ColorSetting("Custom color").computeColorSetting(new Color(255, 255, 255)).computeVisible(this::lambda$new$0);
   public final ColorSetting friendColor = new ColorSetting("Friend color").computeColorSetting(new Color(0, 255, 100));
   public final ColorSetting invisibleColor = new ColorSetting("Invisible color").computeColorSetting(new Color(255, 100, 100));
   public static final double doubleVal = 4096.0;
   public static final int intVal = 15;
   public final MatrixStack class_4587 = new MatrixStack();
   public final Vector3f vector3f = new Vector3f();
   public final double[] doubles = new double[90];
   public static final int int2 = 0;
   public static final int int3 = 1;
   public static final int int4 = 2;
   public static final int int5 = 3;
   public static final int int6 = 4;
   public static final int int7 = 5;
   public static final int int8 = 6;
   public static final int int9 = 7;
   public static final int mosui = 8;
   public static final int int10 = 9;
   public static final int int11 = 10;
   public static final int mweormi = 11;
   public static final int int12 = 12;
   public static final int int13 = 13;
   public static final int int14 = 14;
   public static final int int15 = 15;
   public static final int mfodq = 16;
   public final double[] doubles2 = new double[51];

   public SkeletonESP() {
      this.addSettings(
         new Setting[]{
            this.multiBooleanSetting, this.throughWalls, this.lineWidth, this.alpha, this.rainbow, this.customColor, this.friendColor, this.invisibleColor
         }
      );
   }

   @Override
   public void onEvent() {
      Subscription var1 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void handleEnable(MatrixStack matrixStack, float partialTicks) {
      boolean var3 = this.self.getValue();
      boolean var4 = this.others.getValue();
      boolean var5 = this.friends.getValue();
      boolean var6 = this.invisibles.getValue();
      boolean var7 = this.firstPerson.getValue();
      boolean var8 = mc.options.getPerspective() == Perspective.FIRST_PERSON;
      boolean var9 = this.throughWalls.getValue();
      Vec3d var10 = mc.gameRenderer.getCamera().getPos();
      double var11 = mc.player.getX();
      double var13 = mc.player.getY();
      double var15 = mc.player.getZ();
      Frustum var17 = null;
      if (mc.worldRenderer instanceof WorldRendererAccessor) {
         var17 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
      }

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.lineWidth(this.lineWidth.getValue());
      if (var9) {
         RenderSystem.disableDepthTest();
         RenderSystem.depthMask(false);
      } else {
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
      }

      BufferBuilder var18 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      boolean var19 = false;
      float var20 = this.alpha.getValue();

      for (PlayerEntity var22 : mc.world.getPlayers()) {
         if (var22 != null) {
            double var27 = var22.getX() - var11;
            double var25 = var22.getY() - var13;
            double var23 = var22.getZ() - var15;
            double var29 = var27 * var27 + var25 * var25 + var23 * var23;
            if (!(var29 > 4096.0)
               && this.computeFlag(var22, var3, var4, var5, var6, var7, var8)
               && (var22 == mc.player || var17 == null || var17.isVisible(var22.getBoundingBox().expand(0.5)))) {
               Vec3d var31 = var22.getLerpedPos(partialTicks);
               double var32 = var31.x - var10.x;
               double var34 = var31.y - var10.y;
               double var36 = var31.z - var10.z;
               float var38 = MathHelper.lerpAngleDegrees(partialTicks, var22.prevBodyYaw, var22.bodyYaw);
               float var39 = MathHelper.lerpAngleDegrees(partialTicks, var22.prevHeadYaw, var22.headYaw);
               float var40 = MathHelper.lerp(partialTicks, var22.prevPitch, var22.getPitch());
               float var41 = var22.limbAnimator.getPos(partialTicks);
               float var42 = var22.limbAnimator.getSpeed(partialTicks);
               float var43 = var22.getHandSwingProgress(partialTicks);
               boolean var44 = var22.isGliding();
               boolean var45 = var22.isInSneakingPose();
               boolean var46 = var22.isSwimming();
               boolean var47 = var22.hasVehicle();
               float var48 = var20;
               if (var29 > 64.0) {
                  double var49 = Math.sqrt(var29);
                  float var51 = (float)Math.max(0.4, 1.0 - (var49 - 8.0) / 56.0);
                  var48 = var20 * var51;
               }

               int var57 = this.computeInt(var22, var48);
               float var50 = (float)(var57 >> 16 & 0xFF) / 255.0F;
               float var58 = (float)(var57 >> 8 & 0xFF) / 255.0F;
               float var52 = (float)(var57 & 0xFF) / 255.0F;
               float var53 = (float)(var57 >> 24 & 0xFF) / 255.0F;
               this.handleDegrees(var32, var34, var36, var38, var39, var40, var41, var42, var43, var22.getHeight(), var44, var45, var46, var47);
               Matrix4f var54 = matrixStack.peek().getPositionMatrix();

               for (int var55 = 0; var55 < 15; var55++) {
                  int var56 = var55 * 6;
                  var18.vertex(var54, (float)this.doubles[var56], (float)this.doubles[var56 + 1], (float)this.doubles[var56 + 2])
                     .color(var50, var58, var52, var53);
                  var18.vertex(var54, (float)this.doubles[var56 + 3], (float)this.doubles[var56 + 4], (float)this.doubles[var56 + 5])
                     .color(var50, var58, var52, var53);
                  var19 = true;
               }
            }
         }
      }

      if (var19) {
         BufferRenderer.drawWithGlobalProgram(var18.end());
      }

      RenderSystem.lineWidth(1.0F);
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public boolean computeFlag(
      PlayerEntity player, boolean renderSelf, boolean renderOthers, boolean renderFriends, boolean renderInvis, boolean firstPersonRender, boolean isFirstPerson
   ) {
      if (player == mc.player) {
         return !renderSelf ? false : !isFirstPerson || firstPersonRender;
      } else if (!renderOthers) {
         return false;
      } else {
         String var8 = player.getName().getString();
         return FriendsStore.getInstance().computeContains(var8) && !renderFriends ? false : !player.isInvisible() || renderInvis;
      }
   }

   public void handleDegrees(
      double x,
      double y,
      double z,
      float bodyYaw,
      float headYaw,
      float pitch,
      float swing,
      float swingAmt,
      float handSwing,
      float h,
      boolean elytra,
      boolean sneak,
      boolean swimming,
      boolean riding
   ) {
      MatrixStack var20 = this.class_4587;
      var20.loadIdentity();
      var20.translate(x, y, z);
      if (swimming) {
         var20.translate(0.0, (double)h * 0.5, 0.0);
      } else if (sneak && !elytra) {
         var20.translate(0.0, 0.125, 0.0);
      }

      var20.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-bodyYaw));
      float var21 = 0.0F;
      if (swimming) {
         var21 = -((float)((double)pitch * (Math.PI / 180.0)));
      } else if (elytra) {
         var21 = 1.57F + pitch / 57.2958F;
      } else if (sneak) {
         var21 = 0.5F;
      }

      if (swimming || elytra || sneak) {
         var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var21 * 57.2958F));
      }

      if (sneak && !elytra && !swimming) {
         var20.translate(0.0, -0.13, 0.0);
      }

      this.handlePosition(var20, 0);
      var20.push();
      var20.translate(0.0, (double)h * 0.75, 0.0);
      this.handlePosition(var20, 1);
      var20.push();
      var20.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(bodyYaw - headYaw));
      if (!elytra) {
         var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
      }

      var20.translate(0.0, (double)h * 0.15, 0.0);
      this.handlePosition(var20, 2);
      var20.pop();
      swingAmt = Math.min(swingAmt, 1.0F) * 0.5F;
      var20.push();
      var20.translate(0.25, 0.0, 0.0);
      this.handlePosition(var20, 3);
      float var19;
      if (elytra) {
         var19 = -0.2F;
         var20.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-5.0F));
      } else {
         var19 = MathHelper.cos(swing * 0.6662F + (float) Math.PI) * 0.8F * swingAmt;
      }

      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var19 * 57.2958F));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 4);
      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.max(0.0F, var19 * 15.0F)));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 5);
      var20.pop();
      var20.push();
      var20.translate(-0.25, 0.0, 0.0);
      this.handlePosition(var20, 6);
      float var18;
      if (elytra) {
         var18 = -0.2F;
         var20.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(5.0F));
      } else {
         var18 = MathHelper.cos(swing * 0.6662F) * 0.8F * swingAmt;
      }

      if (handSwing > 0.0F && !elytra) {
         float var22 = 1.0F - handSwing;
         var22 *= var22;
         float var23 = MathHelper.sin(var22 * (float) Math.PI);
         float var24 = headYaw - bodyYaw;
         float var25 = MathHelper.clamp(var24 / 75.0F, -1.0F, 1.0F);
         var20.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var23 * 15.0F * var25));
         var18 += -var23 * 0.8F;
      }

      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var18 * 57.2958F));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 7);
      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.max(0.0F, var18 * 15.0F)));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 8);
      var20.pop();
      var20.pop();
      var20.push();
      var20.translate(0.0, (double)h * 0.5, 0.0);
      this.handlePosition(var20, 9);
      var20.pop();
      var20.push();
      var20.translate(0.0, (double)h * 0.3, 0.0);
      this.handlePosition(var20, 10);
      var20.push();
      var20.translate(0.125, 0.0, 0.0);
      this.handlePosition(var20, 11);
      float var28 = MathHelper.cos(swing * 0.6662F) * 0.5F * swingAmt;
      if (elytra) {
         var28 = 0.1F;
      }

      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var28 * 57.2958F));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 12);
      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(var28) * 15.0F));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 13);
      var20.pop();
      var20.push();
      var20.translate(-0.125, 0.0, 0.0);
      this.handlePosition(var20, 14);
      float var29 = MathHelper.cos(swing * 0.6662F + (float) Math.PI) * 0.5F * swingAmt;
      if (elytra) {
         var29 = 0.1F;
      }

      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var29 * 57.2958F));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 15);
      var20.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(var29) * 15.0F));
      var20.translate(0.0, -0.25, 0.0);
      this.handlePosition(var20, 16);
      var20.pop();
      var20.pop();
      this.handleInt(0, 1, 2);
      this.handleInt(1, 1, 9);
      this.handleInt(2, 9, 10);
      this.handleInt(3, 1, 3);
      this.handleInt(4, 1, 6);
      this.handleInt(5, 3, 4);
      this.handleInt(6, 4, 5);
      this.handleInt(7, 6, 7);
      this.handleInt(8, 7, 8);
      this.handleInt(9, 10, 11);
      this.handleInt(10, 10, 14);
      this.handleInt(11, 11, 12);
      this.handleInt(12, 12, 13);
      this.handleInt(13, 14, 15);
      this.handleInt(14, 15, 16);
   }

   public void handlePosition(MatrixStack ms, int jointIndex) {
      Vector3f var3 = ms.peek().getPositionMatrix().transformPosition(0.0F, 0.0F, 0.0F, this.vector3f);
      int var4 = jointIndex * 3;
      this.doubles2[var4] = (double)var3.x;
      this.doubles2[var4 + 1] = (double)var3.y;
      this.doubles2[var4 + 2] = (double)var3.z;
   }

   public void handleInt(int boneIndex, int fromJoint, int toJoint) {
      int var4 = boneIndex * 6;
      int var5 = fromJoint * 3;
      int var6 = toJoint * 3;
      this.doubles[var4] = this.doubles2[var5];
      this.doubles[var4 + 1] = this.doubles2[var5 + 1];
      this.doubles[var4 + 2] = this.doubles2[var5 + 2];
      this.doubles[var4 + 3] = this.doubles2[var6];
      this.doubles[var4 + 4] = this.doubles2[var6 + 1];
      this.doubles[var4 + 5] = this.doubles2[var6 + 2];
   }

   public int computeInt(PlayerEntity player, float overrideAlpha) {
      int var3 = (int)(overrideAlpha * 255.0F);
      String var4 = player.getName().getString();
      if (FriendsStore.getInstance().computeContains(var4)) {
         return var3 << 24 | this.friendColor.getValue().getRGB() & 16777215;
      } else if (player.isInvisible()) {
         return var3 << 24 | this.invisibleColor.getValue().getRGB() & 16777215;
      } else {
         String var5 = this.rainbow.getValue();

         return switch (var5) {
            case "Custom" -> var3 << 24 | this.customColor.getValue().getRGB() & 16777215;
            case "Rainbow" -> {
               float var8 = (float)(System.currentTimeMillis() % 3600L) / 3600.0F;
               yield var3 << 24 | Color.HSBtoRGB(var8, 0.8F, 1.0F) & 16777215;
            }
            default -> {
               int var7 = player.getId() * 20;
               yield var3 << 24 | ThemeColor.computeColor(var7).getRGB() & 16777215;
            }
         };
      }
   }

   public BooleanSetting getSelf() {
      return this.self;
   }

   public BooleanSetting getOthers() {
      return this.others;
   }

   public BooleanSetting getFriends() {
      return this.friends;
   }

   public BooleanSetting getInvisibles() {
      return this.invisibles;
   }

   public BooleanSetting getFirstPerson() {
      return this.firstPerson;
   }

   public MultiBooleanSetting getMultiBooleanSetting() {
      return this.multiBooleanSetting;
   }

   public BooleanSetting getThroughWalls() {
      return this.throughWalls;
   }

   public FloatSetting getLineWidth() {
      return this.lineWidth;
   }

   public FloatSetting getAlpha() {
      return this.alpha;
   }

   public ModeSetting getRainbow() {
      return this.rainbow;
   }

   public ColorSetting getCustomColor() {
      return this.customColor;
   }

   public ColorSetting getFriendColor() {
      return this.friendColor;
   }

   public ColorSetting getInvisibleColor() {
      return this.invisibleColor;
   }

   public MatrixStack getClass_4587() {
      return this.class_4587;
   }

   public Vector3f getVector3f() {
      return this.vector3f;
   }

   public double[] getDoubles() {
      return this.doubles;
   }

   public double[] getDoubles2() {
      return this.doubles2;
   }

   public static SkeletonESP getInstance() {
      return skeletonESP;
   }

   public void lambda$onEvent$1(Render3DEvent.Render3DEventData event) {
      if (mc.world != null && mc.player != null) {
         this.handleEnable(event.getMatrixStack(), event.getPartialTicks());
      }
   }

   public Boolean lambda$new$0() {
      return this.rainbow.is("Custom");
   }
}
