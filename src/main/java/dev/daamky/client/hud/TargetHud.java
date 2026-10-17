package dev.daamky.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.render.Interface;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.math.MathHelper;

public class TargetHud extends HudElement {
   public float macmug;
   public LivingEntity class_1309;
   public LivingEntity msofvi;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public final ItemStack[] class_1799List = new ItemStack[5];
   public static final ItemStack class_1799 = new ItemStack(Items.GOLDEN_APPLE);

   public float computeFloat(LivingEntity target) {
      float var2 = target.getHealth() + target.getAbsorptionAmount();
      if (!(target instanceof PlayerEntity var3)) {
         return var2;
      } else {
         try {
            ScoreboardObjective var6 = var3.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
            int var5;
            if (var6 != null && (var5 = var3.getScoreboard().getScore(var3, var6).getScore()) > 0) {
               return (float)var5;
            }
         } catch (Exception var7) {
         }

         return var2;
      }
   }

   public TargetHud() {
      super(30.0F, 30.0F);
   }

   @Override
   public String getName() {
      return "Target info";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack ms) {
      if (mc.player != null) {
         LivingEntity var4 = AttackAura.getInstance().class_1309;
         if (var4 != null && var4.isAlive()) {
            this.class_1309 = var4;
         } else {
            label165: {
               if (mc.targetedEntity instanceof LivingEntity var5 && var5.isAlive()) {
                  this.class_1309 = var5;
                  break label165;
               }

               if (mc.currentScreen != null) {
                  this.class_1309 = mc.player;
               } else {
                  this.class_1309 = null;
               }
            }
         }

         if (this.class_1309 != null) {
            this.msofvi = this.class_1309;
         }

         this.animatedValue.update();
         this.animatedValue.animateTo(this.class_1309 != null ? 1.0 : 0.0, 150L, this.getEasing());
         float var55 = (float)this.animatedValue.getValue();
         this.animatedValue2.update();
         if (var55 <= 0.01F) {
            this.msofvi = null;
            this.setRenderContent(false);
         } else {
            LivingEntity var56 = this.class_1309 != null ? this.class_1309 : this.msofvi;
            if (var56 != null) {
               boolean var7 = var56.isUsingItem() && !var56.getActiveItem().isEmpty();
               this.animatedValue2.animateTo(var7 ? 1.0 : 0.0, 160L, this.getEasing());
               float var8 = (float)this.animatedValue2.getValue();
               DrawContext var9 = this.currentEvent.getContext();
               float var10 = this.getAutoHelper().getX();
               float var11 = this.getAutoHelper().getY();
               float var12 = this.computeFloat(var56);
               float var13 = var56.getMaxHealth() + var56.getAbsorptionAmount();
               if (var13 == 0.0F) {
                  var13 = 1.0F;
               }

               this.animatedValue3.update();
               this.animatedValue3.animateTo(var12, 180L, Easing.SINE_OUT);
               this.macmug = (float)this.animatedValue3.getValue();
               float var14 = Math.min(1.0F, this.macmug / var13);
               float var15 = this.computeFloat(3.5F);
               float var16 = this.computeFloat(17.5F);
               float var17 = this.computeFloat(4.0F);
               float var18 = this.computeFloat(17.5F);
               float var19 = var18 / 2.0F;
               float var20 = this.computeFloat(1.85F);
               float var21 = this.computeFloat(5.2F);
               float var22 = this.computeFloat(4.0F);
               String var23 = var56.getName().getString();
               String var24;
               if (this.macmug >= 1000.0F) {
                  var24 = String.format(Locale.US, "%.1fk", this.macmug / 1000.0F);
               } else if (Math.abs(this.macmug - (float)Math.round(this.macmug)) < 0.08F) {
                  var24 = String.valueOf(Math.round(this.macmug));
               } else {
                  var24 = String.format(Locale.US, "%.1f", this.macmug);
               }

               int var25 = 0;

               for (ItemStack var27 : var56.getArmorItems()) {
                  if (var25 < 4) {
                     this.class_1799List[var25++] = var27;
                  }
               }

               while (var25 < 4) {
                  this.class_1799List[var25++] = net.minecraft.item.ItemStack.EMPTY;
               }

               this.class_1799List[4] = var56.getMainHandStack();
               float var57 = this.getMheuy().computeFloat(var23, var21);
               byte var58 = 5;
               float var28 = this.computeFloat(9.0F);
               float var29 = this.computeFloat(1.0F);
               float var30 = (float)var58 * (var28 + var29) - var29;
               float var31 = Math.max(var57, var30) + this.computeFloat(2.0F);
               float var32 = var15 + var16 + var17 + var31 + var17 + var18 + var15;
               var32 = Math.max(var32, this.computeFloat(78.0F));
               float var33 = var15 + var16 + var15;
               this.getAutoHelper().setWidth(var32);
               this.getAutoHelper().setHeight(var33);
               Color var34 = this.initInit();
               int var35 = (int)((float)var34.getAlpha() * var55) << 24 | var34.getRGB() & 16777215;
               float var36 = this.getHeight();
               if (this.getAutoHelper().isBlur()) {
                  Render.shaderProgram.handleVal(ms, var10, var11, var32, var33, var36, var35);
               } else {
                  Render.rectRenderer.handleVal(ms, var10, var11, var32, var33, var36, var35);
               }

               this.runContent();
               if (Interface.getInstance().isEnabled() && Interface.check2()) {
                  Interface.handleVal(ms, var10, var11, var32, var33, var36, var55, Interface.check4());
               }

               float var37 = var10 + var15;
               float var38 = var11 + var15;
               int var39 = (int)(255.0F * var55);
               if (var56 instanceof PlayerEntity var40) {
                  Render.myazem.handleAvatar(ms, var40, var37, var38, var16, var16, this.computeFloat(1.5F), this.computeFloat(2.0F), var39 << 24 | 16777215);
               } else {
                  int var60 = (int)(255.0F * var55) << 24 | ThemeColor.getColor2().getRGB() & 16777215;
                  Render.rectRenderer.handleVal(ms, var37, var38, var16, var16, this.computeFloat(2.0F), var60);
                  boolean var41 = false;
                  if (var55 > 0.05F) {
                     var41 = Render.myazem.computeFlag(ms, var56, var37, var38, var16, var16, 0.0F, this.computeFloat(2.0F), var39 << 24 | 16777215);
                  }

                  if (!var41) {
                     int var42 = (int)(255.0F * var55) << 24 | ThemeColor.getColor8().getRGB() & 16777215;
                     this.getValue2()
                        .handleVal2(ms, "i", var37 + var16 / 2.0F, var38 + var16 / 2.0F - this.computeFloat(5.0F) / 2.0F, this.computeFloat(5.0F), var42);
                  }
               }

               if (var8 > 0.01F) {
                  ItemStack var61 = var56.getActiveItem();
                  if (var61.isEmpty()) {
                     var61 = class_1799;
                  }

                  int var63 = (int)(140.0F * var55 * var8);
                  Render.shaderProgram.handleVal(ms, var37, var38, var16, var16, this.computeFloat(2.0F), var63 << 24);
                  float var65 = this.computeFloat(9.0F);
                  float var43 = var37 + var16 / 2.0F;
                  float var44 = var38 + var16 / 2.0F;
                  ms.push();
                  ms.translate(var43, var44, 0.0F);
                  ms.scale(var8, var8, 1.0F);
                  ms.translate(-var43, -var44, 0.0F);
                  ms.push();
                  ms.translate(var43 - var65 / 2.0F, var44 - var65 / 2.0F - this.computeFloat(1.5F), 0.0F);
                  ms.scale(var65 / 16.0F, var65 / 16.0F, 1.0F);
                  var9.drawItem(var61, 0, 0);
                  ms.pop();
                  float var45 = var7 ? (float)var56.getItemUseTimeLeft() / 20.0F : 0.0F;
                  String var46 = String.format(Locale.US, "%.2f%s", var45, EngHelper.computeString("s"));
                  float var47 = this.computeFloat(4.0F);
                  int var48 = (int)(255.0F * var55 * var8) << 24 | 16777215;
                  this.getMheuy().handleVal2(ms, var46, var43, var44 + this.computeFloat(3.0F), var47, var48);
                  ms.pop();
               }

               float var62 = var37 + var16 + var17;
               float var64 = this.computeFloat(2.0F);
               float var66 = var11 + (var33 - (var21 + var64 + var28)) / 2.0F - this.computeFloat(0.8F);
               float var67 = var66 + var21 + var64;
               int var68 = (int)(255.0F * var55) << 24 | ThemeColor.getColor8().getRGB() & 16777215;
               FontBootstrap.clientFont9.handleVal(ms, var23, var62, var66, var21, var68);
               float var69 = var62;
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, var55);

               for (int var70 = 0; var70 < var58; var70++) {
                  ItemStack var72 = this.class_1799List[var70] != null ? this.class_1799List[var70] : net.minecraft.item.ItemStack.EMPTY;
                  if (!var72.isEmpty()) {
                     ms.push();
                     ms.translate(var69, var67, 0.0F);
                     ms.scale(var28 / 16.0F, var28 / 16.0F, 1.0F);
                     var9.drawItem(var72, 0, 0);
                     ms.pop();
                  } else {
                     int var74 = (int)(255.0F * var55) << 24 | 6579300;
                     this.getValue2()
                        .handleVal2(ms, "o", var69 + var28 / 2.0F, var67 + var28 / 2.0F - this.computeFloat(4.5F) / 2.0F, this.computeFloat(4.5F), var74);
                  }

                  var69 += var28 + var29;
               }

               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               float var71 = var10 + var32 - var15 - var19;
               float var73 = var11 + var15 + var16 / 2.0F;
               int var75 = MainMenuScreen.ACCENT_PRESETS[MathHelper.clamp(MainMenuScreen.selectedAccentIndex, 0, 4)][0];
               int var49 = var75 & 16777215;
               int var50 = (int)(255.0F * var55) << 24 | var49;
               Render.uProjHelper.runGl2();
               int var51 = (int)(120.0F * var55) << 24 | ThemeColor.getColor2().getRGB() & 16777215;
               Render.ringRenderer.handleVal(ms, var71, var73, var19, var20, var51);
               if (var14 > 0.005F) {
                  float var52 = -90.0F + 360.0F * var14;
                  Render.ringRenderer.handleRadians(ms, var71, var73, var19, var20, -90.0F, var52, var50);
               }

               float var76 = this.computeFloat(5.6F);
               float var53 = var18 - var20 * 2.0F - this.computeFloat(1.5F);
               float var54 = this.getMheuy().computeFloat(var24, var76);
               if (var54 > var53) {
                  var76 = Math.max(this.computeFloat(2.5F), var76 * (var53 / var54));
               }

               this.getMheuy().handleVal2(ms, var24, var71, var73 - var76 / 2.0F - this.computeFloat(0.8F), var76, var50);
            }
         }
      }
   }
}
