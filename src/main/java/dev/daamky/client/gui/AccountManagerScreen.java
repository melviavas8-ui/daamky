package dev.daamky.client.gui;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AltsJsonHelper;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.FromStringHelper;
import dev.daamky.client.internal.Geometry;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ScissorUtil;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.internal.TintColorHelper;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.mixin.MinecraftClientSessionAccessor;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.Session.AccountType;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AccountManagerScreen extends Screen {
   public final Screen parent;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public final AnimatedValue animatedValue4 = new AnimatedValue();
   public final AnimatedValue murki = new AnimatedValue();
   public final AnimatedValue animatedValue5 = new AnimatedValue();
   public final AnimatedValue animatedValue6 = new AnimatedValue();
   public boolean flag = false;
   public double doubleVal = 0.0;
   public double double2 = 0.0;
   public final AnimatedValue animatedValue7 = new AnimatedValue();
   public String string = "";
   public boolean flag2 = false;
   public int maeki = 0;
   public final float floatVal = 200.0F;
   public final float float2 = 256.0F;
   public static final String string2 = "abcdefghijklmnopqrstuvwxyz0123456789";
   public final Random random = new Random();

   public AccountManagerScreen(Screen parent) {
      super(Text.of(""));
      this.parent = parent;
   }

   public void run() {
      if (!this.flag) {
         this.flag = true;
         this.flag2 = false;
         this.animatedValue2.handleStart(0.0);
      }
   }

   public void init() {
      super.init();
      this.maeki = 0;
      this.animatedValue.handleStart(0.0);
      this.animatedValue2.handleStart(0.0);
      this.animatedValue3.handleStart(0.0);
      this.flag = false;
      this.animatedValue7.handleStart(0.0);
      AltsJsonHelper.getInstance().loadNext();
      String var1 = MinecraftAccess.mc.getSession().getUsername();

      for (AltsJsonHelper.AltAccount var3 : AltsJsonHelper.getInstance().getAlts()) {
         var3.active = var3.name != null && var3.name.equalsIgnoreCase(var1);
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.maeki == 0) {
         this.maeki++;
         this.animatedValue.handleStart(0.01);
      } else if (this.maeki == 1) {
         this.maeki++;
         this.animatedValue.handleStart(0.0);
         this.animatedValue.animateTo(1.0, 380L, Easing.QUINT_OUT, false);
      } else {
         this.animatedValue.update();
         this.animatedValue.animateTo(this.flag ? this.animatedValue.getValue() : 1.0, 380L, Easing.QUINT_OUT, true);
      }

      float var5 = (float)this.animatedValue.getValue();
      this.animatedValue2.update();
      this.animatedValue2.animateTo(this.flag ? 1.0 : 0.0, this.flag ? 280L : 120L, this.flag ? Easing.SINE_IN : Easing.SINE_OUT, true);
      float var6 = (float)this.animatedValue2.getValue();
      if (this.flag && var6 >= 0.99F) {
         MinecraftAccess.mc.setScreen(this.parent);
      } else {
         float var7 = this.flag ? 1.0F - var6 * 0.08F : 0.94F + 0.06F * var5;
         float var8 = var5 * (1.0F - var6);
         int var9 = (int)(255.0F * var8);
         MatrixStack var10 = context.getMatrices();
         CustomTitleScreen.handleVal(var10, this.width, this.height);
         Render.rectRenderer
            .handleRgb(var10, 0.0F, 0.0F, (float)this.width, (float)this.height, 0.0F, new Color(4, 4, 8, Math.min(255, (int)(180.0F * var8))));
         UHalfTexelSizeHelper.handleClass_4587(var10);
         float var11 = (float)this.width / 2.0F;
         float var12 = (float)this.height / 2.0F;
         var10.push();
         var10.translate(var11, var12, 0.0F);
         var10.scale(var7, var7, 1.0F);
         var10.translate(-var11, -var12, 0.0F);
         float var13 = var11 - 200.0F / 2.0F;
         float var14 = var12 - 256.0F / 2.0F;
         String var15 = "Менеджер Аккаунтов";
         float var16 = 12.5F;
         Color var17 = ThemeColor.getColor4();
         Color var18 = new Color(var17.getRed(), var17.getGreen(), var17.getBlue(), var9);
         FontBootstrap.clientFont2.handleColor2(var10, var15, var11, var14 - 34.0F, var16, var18);
         String var19 = MinecraftAccess.mc.getSession().getUsername();
         int var20 = AltsJsonHelper.getInstance().getAlts().size();
         String var21 = (var19 != null && !var19.isEmpty() ? var19 : "Не выбран") + "  |  " + var20 + " аккаунтов";
         float var22 = 6.2F;
         FontBootstrap.mkadw.handleColor2(var10, var21, var11, var14 - 16.0F, var22, new Color(200, 200, 220, (int)(220.0F * var8)));
         float var23 = 8.0F;
         Color var24 = new Color(255, 255, 255, (int)(18.0F * var8));
         Color var25 = new Color(11, 11, 13, (int)(140.0F * var8));
         TintColorHelper.handleFloat(var13, var14, 200.0F, 256.0F, var8, var23, -16645628);
         Render.rectRenderer.handleRgb(var10, var13 - 0.5F, var14 - 0.5F, 200.0F + 1.0F, 256.0F + 1.0F, var23 + 0.5F, var24);
         Render.rectRenderer.handleRgb(var10, var13, var14, 200.0F, 256.0F, var23, var25);
         float var26 = 6.0F;
         float var27 = var13 + var26;
         float var28 = var14 + var26;
         float var29 = 200.0F - var26 * 2.0F;
         float var30 = 20.0F;
         float var31 = var14 + 256.0F - var26 - var30;
         float var32 = 5.0F;
         float var33 = var31 - var32 - var28;
         List var34 = AltsJsonHelper.getInstance().getAlts();
         float var35 = 27.0F;
         float var36 = 2.5F;
         float var37 = (float)var34.size() * (var35 + var36) + 4.0F;
         this.double2 = Math.max(0.0, (double)(var37 - var33));
         this.animatedValue7.update();
         this.animatedValue7.animateTo(this.doubleVal, 160L, Easing.QUINT_OUT, true);
         float var38 = (float)this.animatedValue7.getValue();
         ScissorUtil.handleEnable(var10, var27 - 2.0F, var28 - 2.0F, var29 + 4.0F, var33 + 4.0F);
         if (var34.isEmpty()) {
            FontBootstrap.mkadw
               .handleColor2(
                  var10, "Список пуст. Введите ник.", var27 + var29 / 2.0F, var28 + var33 / 2.0F - 3.0F, 5.0F, new Color(120, 120, 140, (int)(160.0F * var8))
               );
         } else {
            float var39 = var28 + 1.5F - var38;

            for (int var40 = 0; var40 < var34.size(); var40++) {
               AltsJsonHelper.AltAccount var41 = (AltsJsonHelper.AltAccount)var34.get(var40);
               if (var41.addAnim == null) {
                  var41.addAnim = new AnimatedValue();
                  var41.addAnim.handleStart(1.0);
               }

               var41.addAnim.update();
               var41.addAnim.animateTo(1.0, 220L, Easing.CUBIC_OUT, true);
               float var42 = (float)var41.addAnim.getValue();
               if (var41.removeAnim != null) {
                  var41.removeAnim.update();
                  var41.removeAnim.animateTo(1.0, 180L, Easing.CUBIC_IN, true);
                  float var43 = (float)var41.removeAnim.getValue();
                  if (var43 >= 0.99F) {
                     AltsJsonHelper.getInstance().handleRemove(var41);
                     continue;
                  }

                  var42 *= 1.0F - var43;
               }

               if (!(var39 + var35 < var28 - 10.0F) && !(var39 > var28 + var33 + 10.0F)) {
                  boolean var93 = Geometry.computeFlag((float)mouseX, (float)mouseY, var27, var39, var29, var35);
                  if (var41.hoverAnim == null) {
                     var41.hoverAnim = new AnimatedValue();
                  }

                  var41.hoverAnim.update();
                  var41.hoverAnim.animateTo(var93 ? 1.0 : 0.0, 140L, Easing.CUBIC_OUT, true);
                  float var44 = (float)var41.hoverAnim.getValue();
                  if (var41.activeAnim == null) {
                     var41.activeAnim = new AnimatedValue();
                     var41.activeAnim.handleStart(var41.active ? 1.0 : 0.0);
                  }

                  var41.activeAnim.update();
                  var41.activeAnim.animateTo(var41.active ? 1.0 : 0.0, 220L, Easing.QUINT_OUT, true);
                  float var45 = (float)var41.activeAnim.getValue();
                  int var46 = (int)(255.0F * var8 * var42);
                  Color var47 = new Color(255, 255, 255, (int)((12.0F + 35.0F * var44) * var8 * var42));
                  Color var48 = new Color(var17.getRed(), var17.getGreen(), var17.getBlue(), (int)((130.0F + 70.0F * var44) * var8 * var42));
                  Color var49 = ColorMath.initBlue(var48, var47, var45);
                  Color var50 = new Color(16, 16, 20, (int)((220.0F + 35.0F * var44) * var8 * var42));
                  Color var51 = new Color(24, 24, 32, (int)((235.0F + 20.0F * var44) * var8 * var42));
                  Color var52 = ColorMath.initBlue(var51, var50, var45);
                  if (var41.pulseAnim != null) {
                     var41.pulseAnim.update();
                     var41.pulseAnim.animateTo(1.0, 900L, Easing.LINEAR, true);
                     float var53 = (float)var41.pulseAnim.getValue();
                     if (var53 < 0.99F) {
                        float var54 = (float)Math.max(0.0, Math.sin((double)var53 * Math.PI * 4.0)) * (1.0F - var53);
                        if (var54 > 0.01F) {
                           Color var55 = new Color(255, 255, 255, (int)(240.0F * var54 * var8 * var42));
                           Render.rectRenderer.handleRgb(var10, var27 - 1.0F, var39 - 1.0F, var29 + 2.0F, var35 + 2.0F, 5.5F, var55);
                           Render.rectRenderer
                              .handleRgb(
                                 var10,
                                 var27 - 2.0F,
                                 var39 - 2.0F,
                                 var29 + 4.0F,
                                 var35 + 4.0F,
                                 6.5F,
                                 new Color(255, 255, 255, (int)(60.0F * var54 * var8 * var42))
                              );
                        }
                     }
                  }

                  Render.rectRenderer.handleRgb(var10, var27 - 0.4F, var39 - 0.4F, var29 + 0.8F, var35 + 0.8F, 5.0F, var49);
                  Render.rectRenderer.handleRgb(var10, var27, var39, var29, var35, 4.6F, var52);
                  float var106 = var27 + 4.5F;
                  float var108 = var39 + 3.5F;
                  float var110 = 20.0F;
                  Render.rectRenderer
                     .handleRgb(var10, var106 - 0.5F, var108 - 0.5F, var110 + 1.0F, var110 + 1.0F, 4.0F, new Color(255, 255, 255, (int)(20.0F * var8 * var42)));
                  Render.rectRenderer.handleRgb(var10, var106, var108, var110, var110, 3.5F, new Color(8, 8, 12, (int)(240.0F * var8 * var42)));
                  var41.cachedSkinIdentifier = Identifier.of("daamky", "avatar_skin.png");
                  Identifier var56 = var41.cachedSkinIdentifier;
                  if (var56 == null) {
                     if (var41.skinTexturesSupplier != null) {
                        try {
                           SkinTextures var57 = var41.skinTexturesSupplier.get();
                           if (var57 != null) {
                              var56 = var57.texture();
                           }
                        } catch (Throwable var88) {
                        }
                     }

                     if (var56 == null) {
                        UUID var113 = FromStringHelper.computeName(var41.name);
                        var56 = DefaultSkinHelper.getSkinTextures(var113).texture();
                     }

                     if (var56 != null) {
                        var41.cachedSkinIdentifier = var56;
                     }
                  }

                  if (var56 != null) {
                     Render.myazem.handleRgb(var10, var56, var106, var108, var110, var110, 0.0F, 3.5F, new Color(255, 255, 255, (int)(255.0F * var8 * var42)));
                  }

                  float var114 = var106 + var110 + 6.5F;
                  float var58 = 5.8F;
                  Color var59 = ColorMath.initBlue(new Color(var17.getRed(), var17.getGreen(), var17.getBlue(), var46), new Color(240, 240, 250, var46), var45);
                  FontBootstrap.mheuy.handleColor(var10, var41.name, var114, var39 + 5.2F, var58, var59);
                  float var60 = var39 + 14.8F;
                  float var61 = 4.3F;
                  if (var41.active) {
                     FontBootstrap.mkadw.handleColor(var10, "Активен", var114, var60, var61, new Color(46, 204, 113, (int)(220.0F * var8 * var42)));
                  } else {
                     String var62 = var41.date != null ? var41.date : "Оффлайн";
                     FontBootstrap.mkadw.handleColor(var10, var62, var114, var60, var61, new Color(130, 130, 145, (int)(160.0F * var8 * var42)));
                  }

                  float var120 = 14.0F;
                  float var63 = 14.0F;
                  float var64 = var27 + var29 - var120 - 3.0F;
                  float var65 = var39 + (var35 - var63) / 2.0F;
                  boolean var66 = Geometry.computeFlag((float)mouseX, (float)mouseY, var64 - 1.0F, var39, var120 + 2.0F, var35);
                  if (var66) {
                     Render.rectRenderer.handleRgb(var10, var64, var65, var120, var63, 3.5F, new Color(255, 60, 60, (int)(30.0F * var8 * var42)));
                  }

                  Color var67 = var66
                     ? new Color(245, 75, 75, (int)(255.0F * var8 * var42))
                     : (var93 ? new Color(210, 150, 160, (int)(180.0F * var8 * var42)) : new Color(160, 145, 155, (int)(120.0F * var8 * var42)));
                  float var68 = 7.5F;
                  FontBootstrap.clientFont5.handleColor2(var10, "\ue01e", var64 + var120 / 2.0F, var65 + (var63 - var68) / 2.0F - 0.5F, var68, var67);
                  float var69 = 14.0F;
                  float var70 = 14.0F;
                  float var71 = var64 - var69 - 3.0F;
                  float var72 = var39 + (var35 - var70) / 2.0F;
                  boolean var73 = Geometry.computeFlag((float)mouseX, (float)mouseY, var71 - 1.0F, var39, var69 + 2.0F, var35);
                  if (var41.pinHoverAnim == null) {
                     var41.pinHoverAnim = new AnimatedValue();
                  }

                  var41.pinHoverAnim.update();
                  var41.pinHoverAnim.animateTo(var73 ? 1.0 : 0.0, 140L, Easing.QUINT_OUT, true);
                  float var74 = (float)var41.pinHoverAnim.getValue();
                  if (var41.pinAnim == null) {
                     var41.pinAnim = new AnimatedValue();
                     var41.pinAnim.handleStart(var41.pinned ? 1.0 : 0.0);
                  }

                  var41.pinAnim.update();
                  var41.pinAnim.animateTo(var41.pinned ? 1.0 : 0.0, 200L, Easing.QUINT_OUT, true);
                  float var75 = (float)var41.pinAnim.getValue();
                  if (var74 > 0.01F) {
                     Render.rectRenderer.handleRgb(var10, var71, var72, var69, var70, 3.5F, new Color(255, 255, 255, (int)(25.0F * var74 * var8 * var42)));
                  }

                  float var76 = 8.5F + 1.0F * var74;
                  Color var77 = new Color(160, 165, 185, (int)((140.0F + 100.0F * var74) * var8 * var42));
                  Color var78 = var41.pinned && var73
                     ? new Color(245, 90, 90, (int)(255.0F * var8 * var42))
                     : new Color(var17.getRed(), var17.getGreen(), var17.getBlue(), (int)(255.0F * var8 * var42));
                  Color var79 = ColorMath.initBlue(var78, var77, var75);
                  var10.push();
                  float var80 = var71 + var69 / 2.0F;
                  float var81 = var72 + var70 / 2.0F;
                  var10.translate(var80, var81, 0.0F);
                  if (var75 > 0.01F && var75 < 0.99F) {
                     float var82 = 1.0F + (float)Math.sin((double)var75 * Math.PI) * 0.25F;
                     var10.scale(var82, var82, 1.0F);
                  }

                  String var141 = var41.pinned ? "\ue019" : "\ue018";
                  FontBootstrap.clientFont5.handleColor2(var10, var141, 0.0F, -var76 / 2.0F - 0.5F, var76, var79);
                  var10.pop();
                  var39 += (var35 + var36) * var42;
               } else {
                  var39 += (var35 + var36) * var42;
               }
            }
         }

         ScissorUtil.handleDisable(var10);
         float var89 = 68.0F;
         float var90 = 20.0F;
         float var91 = 4.0F;
         float var92 = var29 - var90 - var89 - var91 * 2.0F;
         float var94 = var13 + var26;
         this.animatedValue3.update();
         this.animatedValue3.animateTo(this.flag2 ? 1.0 : 0.0, 180L, Easing.CUBIC_OUT, true);
         float var95 = (float)this.animatedValue3.getValue();
         boolean var96 = Geometry.computeFlag((float)mouseX, (float)mouseY, var94, var31, var92, var30);
         this.animatedValue4.update();
         this.animatedValue4.animateTo(var96 ? 1.0 : 0.0, 150L, Easing.CUBIC_OUT, true);
         float var97 = (float)this.animatedValue4.getValue();
         Color var98 = new Color(255, 255, 255, (int)((14.0F + 25.0F * var97 + 90.0F * var95) * var8));
         Color var99 = new Color(16, 16, 20, (int)(240.0F * var8));
         Render.rectRenderer.handleRgb(var10, var94 - 0.5F, var31 - 0.5F, var92 + 1.0F, var30 + 1.0F, 4.5F, var98);
         Render.rectRenderer.handleRgb(var10, var94, var31, var92, var30, 4.0F, var99);
         float var100 = var94 + 6.0F;
         float var101 = 5.5F;
         if (this.string.isEmpty() && !this.flag2) {
            FontBootstrap.mkadw.handleColor(var10, "Никнейм", var100, var31 + var30 / 2.0F - 2.8F, var101, new Color(120, 120, 140, (int)(140.0F * var8)));
         } else {
            String var102 = this.string;

            while (FontBootstrap.mkadw.computeFloat(var102, var101) > var92 - 12.0F && !var102.isEmpty()) {
               var102 = var102.substring(1);
            }

            FontBootstrap.mkadw.handleColor(var10, var102, var100, var31 + var30 / 2.0F - 2.8F, var101, new Color(245, 245, 255, var9));
            if (this.flag2 && System.currentTimeMillis() / 450L % 2L == 0L) {
               float var104 = var100 + FontBootstrap.mkadw.computeFloat(var102, var101) + 1.0F;
               Render.rectRenderer.handleRgb(var10, var104, var31 + 4.0F, 0.8F, var30 - 8.0F, 0.4F, var18);
            }
         }

         float var103 = var94 + var92 + var91;
         boolean var105 = Geometry.computeFlag((float)mouseX, (float)mouseY, var103, var31, var90, var30);
         this.murki.update();
         this.murki.animateTo(var105 ? 1.0 : 0.0, 150L, Easing.CUBIC_OUT, true);
         float var107 = (float)this.murki.getValue();
         Color var109 = new Color(255, 255, 255, (int)((14.0F + 50.0F * var107) * var8));
         Color var111 = new Color(16, 16, 20, (int)((200.0F + 55.0F * var107) * var8));
         Render.rectRenderer.handleRgb(var10, var103 - 0.5F, var31 - 0.5F, var90 + 1.0F, var30 + 1.0F, 4.5F, var109);
         Render.rectRenderer.handleRgb(var10, var103, var31, var90, var30, 4.0F, var111);
         FontBootstrap.clientFont2.handleColor2(var10, "+", var103 + var90 / 2.0F, var31 + var30 / 2.0F - 5.2F, 7.0F, new Color(245, 245, 255, var9));
         float var112 = var103 + var90 + var91;
         boolean var115 = Geometry.computeFlag((float)mouseX, (float)mouseY, var112, var31, var89, var30);
         this.animatedValue5.update();
         this.animatedValue5.animateTo(var115 ? 1.0 : 0.0, 150L, Easing.CUBIC_OUT, true);
         float var116 = (float)this.animatedValue5.getValue();
         Color var117 = new Color(255, 255, 255, (int)((14.0F + 50.0F * var116) * var8));
         Color var118 = new Color(16, 16, 20, (int)((200.0F + 55.0F * var116) * var8));
         Render.rectRenderer.handleRgb(var10, var112 - 0.5F, var31 - 0.5F, var89 + 1.0F, var30 + 1.0F, 4.5F, var117);
         Render.rectRenderer.handleRgb(var10, var112, var31, var89, var30, 4.0F, var118);
         float var119 = 6.8F;
         float var121 = 5.2F;
         float var122 = FontBootstrap.clientFont5.computeFloat("\ue056", var119);
         float var123 = FontBootstrap.mheuy.computeFloat("Случайный", var121);
         float var124 = 4.0F;
         float var125 = var122 + var124 + var123;
         float var126 = var112 + (var89 - var125) / 2.0F;
         float var127 = var31 + var30 / 2.0F;
         Color var128 = ColorMath.initBlue(new Color(195, 200, 220, var9), new Color(255, 255, 255, var9), var116);
         Color var129 = ColorMath.initBlue(new Color(230, 232, 245, var9), new Color(255, 255, 255, var9), var116);
         FontBootstrap.clientFont5.handleColor(var10, "\ue056", var126, var127 - var119 / 2.0F + 0.3F, var119, var128);
         FontBootstrap.mheuy.handleColor(var10, "Случайный", var126 + var122 + var124, var127 - var121 / 2.0F - 0.4F, var121, var129);
         float var130 = var14 + 256.0F + 8.0F;
         float var131 = 188.0F;
         float var132 = 22.0F;
         float var133 = var11 - var131 / 2.0F;
         boolean var134 = Geometry.computeFlag((float)mouseX, (float)mouseY, var133, var130, var131, var132);
         this.animatedValue6.update();
         this.animatedValue6.animateTo(var134 ? 1.0 : 0.0, 160L, Easing.CUBIC_OUT, true);
         float var135 = (float)this.animatedValue6.getValue();
         float var136 = var135 * 0.8F;
         float var137 = var133 - var136;
         float var138 = var130 - var136 * 0.5F;
         float var139 = var131 + var136 * 2.0F;
         float var140 = var132 + var136;
         Color var142 = ColorMath.initBlue(new Color(255, 75, 75, (int)(120.0F * var8)), new Color(255, 60, 60, (int)(35.0F * var8)), var135);
         Color var83 = ColorMath.initBlue(new Color(42, 14, 18, (int)(240.0F * var8)), new Color(26, 12, 14, (int)(220.0F * var8)), var135);
         Render.rectRenderer.handleRgb(var10, var137 - 0.5F, var138 - 0.5F, var139 + 1.0F, var140 + 1.0F, 6.5F, var142);
         Render.rectRenderer.handleRgb(var10, var137, var138, var139, var140, 6.0F, var83);
         if (var135 > 0.01F) {
            Color var84 = new Color(255, 60, 60, (int)(25.0F * var135 * var8));
            Render.rectRenderer.handleRgb(var10, var137 - 1.5F, var138 - 1.5F, var139 + 3.0F, var140 + 3.0F, 7.5F, var84);
         }

         float var143 = 7.5F;
         float var85 = FontBootstrap.clientFont2.computeFloat("Удалить все аккаунты", 5.2F);
         float var86 = var143 + 4.5F + var85;
         float var87 = var137 + (var139 - var86) / 2.0F;
         FontBootstrap.clientFont5
            .handleColor(var10, "\ue01e", var87, var138 + (var140 - var143) / 2.0F - 0.5F, var143, new Color(255, 100, 100, (int)(255.0F * var8)));
         FontBootstrap.clientFont2
            .handleColor(
               var10, "Удалить все аккаунты", var87 + var143 + 4.5F, var138 + var140 / 2.0F - 2.8F, 5.2F, new Color(255, 110, 110, (int)(255.0F * var8))
            );
         var10.pop();
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.flag) {
         return super.mouseClicked(mouseX, mouseY, button);
      } else {
         float var6 = (float)this.width / 2.0F;
         float var7 = (float)this.height / 2.0F;
         float var8 = var6 - 200.0F / 2.0F;
         float var9 = var7 - 256.0F / 2.0F;
         float var10 = 6.0F;
         float var11 = var9 + 256.0F + 8.0F;
         float var12 = 188.0F;
         float var13 = 22.0F;
         float var14 = var6 - var12 / 2.0F;
         if (Geometry.computeFlag((float)mouseX, (float)mouseY, var14, var11, var12, var13)) {
            AltsJsonHelper.getInstance().getAlts().clear();
            AltsJsonHelper.getInstance().closeClose();
            return true;
         } else if (!Geometry.computeFlag((float)mouseX, (float)mouseY, var8, var9 - 35.0F, 200.0F, 256.0F + 65.0F)) {
            this.run();
            return true;
         } else {
            float var15 = 20.0F;
            float var16 = var9 + 256.0F - var10 - var15;
            float var17 = 68.0F;
            float var18 = 20.0F;
            float var19 = 4.0F;
            float var20 = 200.0F - var10 * 2.0F;
            float var21 = var20 - var18 - var17 - var19 * 2.0F;
            float var22 = var8 + var10;
            if (Geometry.computeFlag((float)mouseX, (float)mouseY, var22, var16, var21, var15)) {
               this.flag2 = true;
               return true;
            } else {
               this.flag2 = false;
               float var23 = var22 + var21 + var19;
               if (Geometry.computeFlag((float)mouseX, (float)mouseY, var23, var16, var18, var15)) {
                  this.initAdd();
                  return true;
               } else {
                  float var24 = var23 + var18 + var19;
                  if (Geometry.computeFlag((float)mouseX, (float)mouseY, var24, var16, var17, var15)) {
                     this.initInit();
                     return true;
                  } else {
                     float var25 = var8 + var10;
                     float var26 = var9 + var10;
                     float var27 = 5.0F;
                     float var28 = var16 - var27 - var26;
                     if (Geometry.computeFlag((float)mouseX, (float)mouseY, var25, var26, var20, var28)) {
                        List var29 = AltsJsonHelper.getInstance().getAlts();
                        float var30 = 27.0F;
                        float var31 = 2.5F;
                        float var32 = var26 + 1.5F - (float)this.animatedValue7.getValue();

                        for (AltsJsonHelper.AltAccount var34 : (Iterable<AltsJsonHelper.AltAccount>)(Iterable)var29) {
                           if (Geometry.computeFlag((float)mouseX, (float)mouseY, var25, var32, var20, var30)) {
                              float var35 = 14.0F;
                              float var36 = var25 + var20 - var35 - 3.0F;
                              float var37 = 14.0F;
                              float var38 = var36 - var37 - 3.0F;
                              boolean var39 = (float)mouseX >= var36 - 1.0F;
                              boolean var40 = (float)mouseX >= var38 - 1.0F && (float)mouseX < var36 - 1.0F;
                              if (var39 || button == 1) {
                                 var34.markedForRemoval = true;
                                 var34.removeAnim = new AnimatedValue();
                                 var34.removeAnim.handleStart(0.0);
                                 return true;
                              }

                              if (var40 || button == 2) {
                                 boolean var41 = var34.pinned;
                                 AltsJsonHelper.getInstance().handlePinned(var34);
                                 this.doubleVal = 0.0;
                                 if (!var41) {
                                    var34.pulseAnim = new AnimatedValue();
                                    var34.pulseAnim.handleStart(0.0);
                                 }

                                 if (var34.pinAnim == null) {
                                    var34.pinAnim = new AnimatedValue();
                                 }

                                 var34.pinAnim.handleStart(0.0);
                                 return true;
                              }

                              if (button == 0) {
                                 this.initNext(var34);
                                 return true;
                              }
                           }

                           var32 += var30 + var31;
                        }
                     }

                     return super.mouseClicked(mouseX, mouseY, button);
                  }
               }
            }
         }
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.double2 > 0.0) {
         this.doubleVal -= verticalAmount * 28.0;
         this.doubleVal = Math.max(0.0, Math.min(this.double2, this.doubleVal));
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.run();
         return true;
      } else {
         if (this.flag2) {
            if (keyCode == 257) {
               this.initAdd();
               return true;
            }

            if (keyCode == 259) {
               if (!this.string.isEmpty()) {
                  this.string = this.string.substring(0, this.string.length() - 1);
               }

               return true;
            }

            if (keyCode == 86 && Screen.hasControlDown()) {
               String var4 = MinecraftAccess.mc.keyboard.getClipboard();
               if (var4 != null && !var4.isEmpty()) {
                  this.string = this.string + var4.trim();
                  if (this.string.length() > 16) {
                     this.string = this.string.substring(0, 16);
                  }
               }

               return true;
            }
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean charTyped(char chr, int modifiers) {
      if (!this.flag2 || this.string.length() >= 16 || !Character.isLetterOrDigit(chr) && chr != '_') {
         return super.charTyped(chr, modifiers);
      } else {
         this.string = this.string + chr;
         return true;
      }
   }

   public void initAdd() {
      if (this.string != null && !this.string.trim().isEmpty()) {
         String var1 = this.string.trim();
         this.string = "";
         this.flag2 = false;
         String var2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
         AltsJsonHelper.AltAccount var3 = new AltsJsonHelper.AltAccount(var1, var2);
         var3.addAnim = new AnimatedValue();
         var3.addAnim.handleStart(0.0);
         AltsJsonHelper.getInstance().addAlt(var3);
         this.initNext(var3);
      }
   }

   public void initInit() {
      int var1 = this.random.nextInt(4) + 9;
      StringBuilder var2 = new StringBuilder();
      var2.append((char)(97 + this.random.nextInt(26)));

      for (int var3 = 1; var3 < var1; var3++) {
         var2.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(this.random.nextInt("abcdefghijklmnopqrstuvwxyz0123456789".length())));
      }

      String var6 = var2.toString();
      String var4 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
      AltsJsonHelper.AltAccount var5 = new AltsJsonHelper.AltAccount(var6, var4);
      var5.addAnim = new AnimatedValue();
      var5.addAnim.handleStart(0.0);
      AltsJsonHelper.getInstance().addAlt(var5);
      this.initNext(var5);
   }

   public void initNext(AltsJsonHelper.AltAccount account) {
      UUID var2 = FromStringHelper.computeName(account.name);
      Session var3 = new Session(account.name, var2, "", Optional.empty(), Optional.empty(), AccountType.MOJANG);
      ((MinecraftClientSessionAccessor)MinecraftAccess.mc).setSession(var3);

      for (AltsJsonHelper.AltAccount var5 : AltsJsonHelper.getInstance().getAlts()) {
         var5.active = var5.equals(account);
      }

      AltsJsonHelper.getInstance().closeClose();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}
