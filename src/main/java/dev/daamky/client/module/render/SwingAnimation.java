package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@ModuleInfo(
   name = "Swing Animation",
   category = ModuleCategory.VISUALS,
   description = "Меняет анимацию удара рукой"
)
public class SwingAnimation extends Module {
   public static final SwingAnimation swingAnimation = new SwingAnimation();
   public final ModeSetting mode7 = new ModeSetting("Mode").value("Mode 1").values("Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Mode 6", "Mode 7");
   public final BooleanSetting onlyWithAura = new BooleanSetting("Only with aura").value(false);
   public final FloatSetting strength = new FloatSetting("Strength").value(20.0F).range(10.0F, 80.0F).step(0.1F).visibleWhen(this::lambda$new$0);
   public final FloatSetting angle = new FloatSetting("Angle").value(0.0F).range(-90.0F, 90.0F).step(1.0F).visibleWhen(this::lambda$new$1);
   public final BooleanSetting slow = new BooleanSetting("Slow").value(false);
   public final FloatSetting speed = new FloatSetting("Speed").value(12.0F).range(1.0F, 50.0F).step(1.0F).visibleWhen(this.slow::getValue);

   public SwingAnimation() {
      this.addSettings(new Setting[]{this.mode7, this.onlyWithAura, this.strength, this.angle, this.slow, this.speed});
   }

   @Override
   public void onEvent() {
   }

   public void handleClass_1306(MatrixStack matrices, float swingProgress, Arm arm) {
      float var4 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
      float var5 = (float)Math.sin((double)swingProgress * (Math.PI / 2) * 2.0);
      float var6 = arm == Arm.LEFT ? -1.0F : 1.0F;
      float var7 = this.angle.getValue();
      float var8 = this.strength.getValue();
      int var9 = arm == Arm.RIGHT ? 1 : -1;
      String var10 = this.mode7.getValue();
      switch (var10) {
         case "Mode 1":
            this.handleVal(matrices, arm, 0.0F);
            float var16 = var8 / 20.0F;
            float var17 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var9 * (45.0F + var17 * -20.0F * var16)));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var9 * (var4 * -20.0F * var16 + var7)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var4 * -80.0F * var16 + var7 * 0.25F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var9 * -45.0F));
            break;
         case "Mode 2":
            this.handleVal(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(50.0F + var7 * 0.4F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var6 * -60.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var6 * (110.0F + var8 * var4 + var7)));
            break;
         case "Mode 3":
            this.handleVal(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(50.0F + var7 * 0.35F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var6 * (-30.0F * (1.0F - var4) - 30.0F + (var8 - 20.0F) * var4)));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var6 * (110.0F + var7 * 0.8F)));
            break;
         case "Mode 4":
            this.handleVal(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var6 * (90.0F + var7 * 0.4F)));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var6 * (-30.0F + var7 * 0.8F)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F - var8 * var5 + 10.0F + var7 * 0.25F));
            break;
         case "Mode 5":
            float var15 = swingProgress * -360.0F;
            this.handleVal(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var15));
            break;
         case "Mode 6":
            this.handleVal(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var6 * (65.0F + var7 * 0.4F)));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var6 * (-15.0F + var7 * 0.8F)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-70.0F - var8 * 1.25F * var5 + 5.0F + var7 * 0.25F));
            break;
         case "Mode 7":
            this.handleVal(matrices, arm, 0.0F);
            float var12 = var8 / 45.0F;
            boolean var13 = this.slow.getValue();
            float var14;
            if (var13) {
               var14 = (float)Math.sin((double)swingProgress * Math.PI);
            } else {
               var14 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
            }

            matrices.translate(0.0F, -var14 * var12, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var6 * (var14 * -12.0F + var7)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var7 * 0.3F));
      }
   }

   public void handleDegrees(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light
   ) {
      if (!player.isUsingSpyglass()) {
         boolean var11 = hand == Hand.MAIN_HAND;
         Arm var12 = var11 ? player.getMainArm() : player.getMainArm().getOpposite();
         matrices.push();
         ViewModel var13 = ViewModel.getInstance();
         if (var13.isEnabled()) {
            boolean var14 = var12 == Arm.RIGHT;
            float var15 = var14 ? 0.56F : -0.56F;
            float var16 = -0.52F;
            float var17 = -0.72F;
            if (var14) {
               float var18 = var13.rightX.getValue();
               float var19 = var13.rightY.getValue();
               float var20 = var13.rightZ.getValue();
               float var21 = var13.rightScale.getValue();
               matrices.translate(var18, var19, var20);
               matrices.translate(var15, var16, var17);
               matrices.scale(var21, var21, var21);
               matrices.translate(-var15, -var16, -var17);
            } else {
               float var35 = -var13.leftX.getValue();
               float var42 = var13.leftY.getValue();
               float var47 = var13.leftZ.getValue();
               float var52 = var13.leftScale.getValue();
               matrices.translate(var35, var42, var47);
               matrices.translate(var15, var16, var17);
               matrices.scale(var52, var52, var52);
               matrices.translate(-var15, -var16, -var17);
            }
         }

         if (item.isOf(Items.CROSSBOW)) {
            boolean var24 = CrossbowItem.isCharged(item);
            boolean var28 = var12 == Arm.RIGHT;
            int var31 = var28 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
               this.handleVal(matrices, var12, equipProgress);
               matrices.translate((float)var31 * -0.4785682F, -0.094387F, 0.05731531F);
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var31 * 65.3F));
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var31 * -9.785F));
               float var41 = (float)item.getMaxUseTime(mc.player) - ((float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F);
               float var46 = var41 / (float)CrossbowItem.getPullTime(item, mc.player);
               if (var46 > 1.0F) {
                  var46 = 1.0F;
               }

               if (var46 > 0.1F) {
                  float var51 = MathHelper.sin((var41 - 0.1F) * 1.3F);
                  float var55 = var46 - 0.1F;
                  float var22 = var51 * var55;
                  matrices.translate(var22 * 0.0F, var22 * 0.004F, var22 * 0.0F);
               }

               matrices.translate(var46 * 0.0F, var46 * 0.0F, var46 * 0.04F);
               matrices.scale(1.0F, 1.0F, 1.0F + var46 * 0.2F);
               matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)var31 * 45.0F));
            } else {
               float var40 = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float var45 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float var50 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               matrices.translate((float)var31 * var40, var45, var50);
               this.handleVal(matrices, var12, equipProgress);
               this.handleDegrees(matrices, var12, swingProgress);
               if (var24 && swingProgress < 0.001F && var11) {
                  matrices.translate((float)var31 * -0.641864F, 0.0F, 0.0F);
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var31 * 10.0F));
               }
            }

            this.handleOrdinal(player, item, var28 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !var28, matrices, vertexConsumers, light);
         } else {
            boolean var23 = var12 == Arm.RIGHT;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
               int var27 = var23 ? 1 : -1;
               String var30 = item.getUseAction().name();
               if ("EAT".equals(var30) || "DRINK".equals(var30)) {
                  this.handleDegrees(matrices, tickDelta, var12, item);
                  this.handleVal(matrices, var12, equipProgress);
               } else if ("BOW".equals(var30)) {
                  this.handleVal(matrices, var12, equipProgress);
                  matrices.translate((float)var27 * -0.2785682F, 0.18344387F, 0.15731531F);
                  matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-13.935F));
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var27 * 35.3F));
                  matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var27 * -9.785F));
                  float var33 = (float)item.getMaxUseTime(mc.player) - ((float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F);
                  float var37 = var33 / 20.0F;
                  var37 = (var37 * var37 + var37 * 2.0F) / 3.0F;
                  if (var37 > 1.0F) {
                     var37 = 1.0F;
                  }

                  if (var37 > 0.1F) {
                     float var43 = MathHelper.sin((var33 - 0.1F) * 1.3F);
                     float var48 = var37 - 0.1F;
                     float var53 = var43 * var48;
                     matrices.translate(var53 * 0.0F, var53 * 0.004F, var53 * 0.0F);
                  }

                  matrices.translate(var37 * 0.0F, var37 * 0.0F, var37 * 0.04F);
                  matrices.scale(1.0F, 1.0F, 1.0F + var37 * 0.2F);
                  matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)var27 * 45.0F));
               } else if ("SPEAR".equals(var30)) {
                  this.handleVal(matrices, var12, equipProgress);
                  matrices.translate((float)var27 * -0.5F, 0.7F, 0.1F);
                  matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-55.0F));
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var27 * 35.3F));
                  matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var27 * -9.785F));
                  float var34 = (float)item.getMaxUseTime(mc.player) - ((float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F);
                  float var39 = var34 / 10.0F;
                  if (var39 > 1.0F) {
                     var39 = 1.0F;
                  }

                  if (var39 > 0.1F) {
                     float var44 = MathHelper.sin((var34 - 0.1F) * 1.3F);
                     float var49 = var39 - 0.1F;
                     float var54 = var44 * var49;
                     matrices.translate(var54 * 0.0F, var54 * 0.004F, var54 * 0.0F);
                  }

                  matrices.translate(0.0F, 0.0F, var39 * 0.2F);
                  matrices.scale(1.0F, 1.0F, 1.0F + var39 * 0.2F);
                  matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)var27 * 45.0F));
               } else if ("BRUSH".equals(var30)) {
                  this.handleDegrees(matrices, tickDelta, var12, item, equipProgress);
               } else {
                  this.handleVal(matrices, var12, equipProgress);
               }
            } else if (player.isUsingRiptide()) {
               this.handleVal(matrices, var12, equipProgress);
               int var25 = var23 ? 1 : -1;
               matrices.translate((float)var25 * -0.4F, 0.8F, 0.3F);
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var25 * 65.0F));
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var25 * -85.0F));
            } else if (var12 == mc.options.getMainArm().getValue() && this.isEnabled() && this.checkAura()) {
               this.handleClass_1306(matrices, swingProgress, var12);
            } else {
               float var26 = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float var29 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float var32 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               int var36 = var23 ? 1 : -1;
               matrices.translate((float)var36 * var26, var29, var32);
               this.handleVal(matrices, var12, equipProgress);
               this.handleDegrees(matrices, var12, swingProgress);
            }

            this.handleOrdinal(player, item, var23 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !var23, matrices, vertexConsumers, light);
         }

         matrices.pop();
      }
   }

   public void handleDegrees(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress) {
      this.handleVal(matrices, arm, equipProgress);
      float var6 = (float)(mc.player.getItemUseTimeLeft() % 10);
      float var7 = var6 - tickDelta + 1.0F;
      float var8 = 1.0F - var7 / 10.0F;
      float var9 = -15.0F + 75.0F * MathHelper.cos(var8 * 2.0F * (float) Math.PI);
      if (arm != Arm.RIGHT) {
         matrices.translate(0.1, 0.83, 0.35);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var9));
         matrices.translate(-0.3, 0.22, 0.35);
      } else {
         matrices.translate(-0.25, 0.22, 0.35);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var9));
      }
   }

   public void handleDegrees(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack) {
      float var6 = (float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F;
      float var7 = var6 / (float)stack.getMaxUseTime(mc.player);
      if (var7 < 0.8F) {
         float var5 = MathHelper.abs(MathHelper.cos(var6 / 4.0F * (float) Math.PI) * 0.1F);
         matrices.translate(0.0F, var5, 0.0F);
      }

      float var9 = 1.0F - (float)Math.pow((double)var7, 27.0);
      int var8 = arm == Arm.RIGHT ? 1 : -1;
      matrices.translate(var9 * 0.6F * (float)var8, var9 * -0.5F, var9 * 0.0F);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var8 * var9 * 90.0F));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var9 * 10.0F));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var8 * var9 * 30.0F));
   }

   public void handleVal(MatrixStack matrices, Arm arm, float equipProgress) {
      int var4 = arm == Arm.RIGHT ? 1 : -1;
      matrices.translate((float)var4 * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
   }

   public void handleDegrees(MatrixStack matrices, Arm arm, float swingProgress) {
      int var4 = arm == Arm.RIGHT ? 1 : -1;
      float var5 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
      float var6 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var4 * (45.0F + var5 * -20.0F)));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var4 * var6 * -20.0F));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var6 * -80.0F));
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var4 * -45.0F));
   }

   public void handleOrdinal(
      LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light
   ) {
      if (!stack.isEmpty()) {
         mc.getItemRenderer()
            .renderItem(
               entity,
               stack,
               renderMode,
               leftHanded,
               matrices,
               vertexConsumers,
               entity.getWorld(),
               light,
               OverlayTexture.DEFAULT_UV,
               entity.getId() + renderMode.ordinal()
            );
      }
   }

   public boolean checkAura() {
      return !this.onlyWithAura.getValue() || AttackAura.getInstance().class_1309 != null;
   }

   public static SwingAnimation getInstance() {
      return swingAnimation;
   }

   public Boolean lambda$new$1() {
      return !this.mode7.is("Mode 5");
   }

   public Boolean lambda$new$0() {
      return !this.mode7.is("Mode 5");
   }
}
