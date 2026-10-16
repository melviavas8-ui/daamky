package dev.daamky.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.PhaseHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.TextShaper;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.util.Collection;
import java.util.LinkedHashMap;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;

public class PotionsHud extends HudElement {
   public final LinkedHashMap<RegistryEntry<StatusEffect>, PotionsHud.Entry> linkedHashMap = new LinkedHashMap<>();
   public static final Object[][] objectList = new Object[][]{
      {StatusEffects.SPEED, "Speed II", "0:30"},
      {StatusEffects.STRENGTH, "Strength", "1:20"},
      {StatusEffects.REGENERATION, "Regeneration II", "0:12"},
      {StatusEffects.RESISTANCE, "Resistance", "2:00"},
      {StatusEffects.JUMP_BOOST, "Jump Boost II", "0:45"}
   };

   public PotionsHud() {
      super(96.0F, 32.0F);
   }

   @Override
   public String getName() {
      return "Potions";
   }

   public boolean checkCard() {
      return this.getAutoHelper().getCardMode();
   }

   public void handleCard(boolean cardMode) {
      this.getAutoHelper().setCardMode(cardMode);
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack ms) {
      if (mc.player != null) {
         boolean var2 = mc.currentScreen != null;
         Collection var3 = mc.player.getActiveStatusEffects().values();

         for (PotionsHud.Entry var5 : this.linkedHashMap.values()) {
            var5.flag = false;
         }

         for (StatusEffectInstance var41 : var3) {
            PotionsHud.Entry var6 = this.linkedHashMap.computeIfAbsent(var41.getEffectType(), PotionsHud::lambda$render$0);
            var6.class_1293 = var41;
            var6.flag = true;
         }

         for (PotionsHud.Entry var42 : this.linkedHashMap.values()) {
            var42.animatedValue.update();
            var42.animatedValue.animateTo(var42.flag ? 1.0 : 0.0, 150L, this.getEasing());
         }

         this.linkedHashMap.values().removeIf(PotionsHud::lambda$render$1);
         boolean var40 = !this.linkedHashMap.isEmpty();
         if (var40 || var2) {
            boolean var43 = !var40 && var2;
            DrawContext var44 = this.currentEvent.getContext();
            float var7 = this.getAutoHelper().getX();
            float var8 = this.getAutoHelper().getY();
            if (this.checkCard()) {
               float var45 = this.computeFloat(18.0F);
               float var46 = this.computeFloat(3.5F);
               float var47 = this.computeFloat(6.0F);
               float var48 = this.computeFloat(11.0F);
               float var49 = this.computeFloat(4.5F);
               float var50 = this.computeFloat(5.5F);
               float var51 = this.computeFloat(4.5F);
               float var52 = this.computeFloat(48.0F);
               if (var43) {
                  for (Object[] var65 : objectList) {
                     float var72 = var47 * 2.0F
                        + var48
                        + var49
                        + Math.max(this.getMheuy().computeFloat((String)var65[1], var50), this.getMkadw().computeFloat((String)var65[2], var51));
                     if (var72 > var52) {
                        var52 = var72;
                     }
                  }
               } else {
                  for (PotionsHud.Entry var57 : this.linkedHashMap.values()) {
                     StatusEffectInstance var60 = var57.class_1293;
                     String var66 = this.computeComp(var60);
                     String var73 = TextShaper.computeFormat(var60.getDuration());
                     float var77 = var47 * 2.0F
                        + var48
                        + var49
                        + Math.max(this.getMheuy().computeFloat(var66, var50), this.getMkadw().computeFloat(var73, var51));
                     if (var77 > var52) {
                        var52 = var77;
                     }
                  }
               }

               float var55 = var52;
               float var58 = var8;
               float var61 = 0.0F;
               if (var43) {
                  Object[] var67 = objectList[0];
                  this.handleColor(
                     ms,
                     var44,
                     computeClass_6880(var67[0]),
                     (String)var67[1],
                     (String)var67[2],
                     1.0F,
                     var7,
                     var8,
                     var52,
                     var45,
                     var47,
                     var48,
                     var49,
                     var50,
                     var51
                  );
                  var58 = var8 + var45 + var46;
                  var61 = 1.0F;
               } else {
                  for (PotionsHud.Entry var74 : this.linkedHashMap.values()) {
                     float var78 = (float)var74.animatedValue.getValue();
                     if (!(var78 <= 0.01F)) {
                        StatusEffectInstance var81 = var74.class_1293;
                        this.handleColor(
                           ms,
                           var44,
                           var81.getEffectType(),
                           this.computeComp(var81),
                           TextShaper.computeFormat(var81.getDuration()),
                           var78,
                           var7,
                           var58,
                           var55,
                           var45,
                           var47,
                           var48,
                           var49,
                           var50,
                           var51
                        );
                        var58 += (var45 + var46) * var78;
                        var61 += var78;
                     }
                  }
               }

               float var69 = Math.max(var45, var58 - var8 - var46);
               this.getAutoHelper().setWidth(var55);
               this.getAutoHelper().setHeight(var69);
            } else {
               float var9 = this.computeFloat(5.0F);
               float var10 = this.computeFloat(6.5F);
               float var11 = this.computeFloat(3.5F);
               float var12 = this.computeFloat(5.0F);
               float var13 = this.computeFloat(14.0F);
               float var14 = var13 + this.computeFloat(2.0F);
               float var15 = this.computeFloat(2.5F);
               float var16 = this.computeFloat(2.5F);
               float var17 = this.computeFloat(11.0F);
               float var18 = 0.0F;
               float var19 = 0.0F;
               if (var43) {
                  for (Object[] var23 : objectList) {
                     float var24 = var10
                        + var11
                        + this.getMkadw().computeFloat((String)var23[1], var12)
                        + var11
                        + this.getMkadw().computeFloat((String)var23[2], var12);
                     if (var24 > var18) {
                        var18 = var24;
                     }
                  }

                  var19 = var17;
               } else {
                  for (PotionsHud.Entry var70 : this.linkedHashMap.values()) {
                     float var75 = (float)var70.animatedValue.getValue();
                     StatusEffectInstance var79 = var70.class_1293;
                     String var82 = this.computeComp(var79);
                     float var25 = var10
                        + var11
                        + this.getMkadw().computeFloat(var82, var12)
                        + var11
                        + this.getMkadw().computeFloat(TextShaper.computeFormat(var79.getDuration()), var12);
                     if (var25 > var18) {
                        var18 = var25;
                     }

                     var19 += var17 * var75;
                  }
               }

               float var64 = this.computeFloat(96.0F);
               float var71 = this.computeFloat(6.0F);
               String var76 = EngHelper.computeString("Effects");
               float var80 = FontBootstrap.clientFont9.computeFloat(var76, var71);
               float var83 = this.computeFloat(6.5F);
               float var84 = Math.max(var64, var9 * 2.0F + var18);
               float var26 = var14 + var15 + var19 + var16;
               this.getAutoHelper().setWidth(var84);
               this.getAutoHelper().setHeight(var26);
               Color var27 = this.initInit();
               float var28 = this.getHeight();
               if (this.getAutoHelper().isBlur()) {
                  Render.shaderProgram.handleVal(ms, var7, var8, var84, var26, var28, var27.getRGB());
               } else {
                  Render.rectRenderer.handleVal(ms, var7, var8, var84, var26, var28, var27.getRGB());
               }

               this.runContent();
               if (Interface.getInstance().isEnabled() && Interface.check2()) {
                  Interface.handleVal(ms, var7, var8, var84, var26, var28, 1.0F, Interface.check4());
               }

               boolean var29 = ThemeManager.getInstance().getIsDayMode();
               int var30 = var29 ? 2030043135 : 2013923850;
               Render.rectRenderer.handleVal(ms, var7, var8, var84, var14, var28, var30);
               float var31 = var8 + (var14 - var71) / 2.0F - this.computeFloat(0.8F);
               FontBootstrap.clientFont9.handleVal(ms, var76, var7 + var9, var31, var71, this.getColor().getRGB());
               float var32 = var8 + (var14 - var83) / 2.0F - this.computeFloat(0.5F);
               this.getValue2().handleVal(ms, "Q", var7 + var84 - var9 - var83, var32, var83, this.getColor().getRGB());
               float var33 = var8 + var14 + var15;
               var44.enableScissor((int)var7, (int)var8, (int)(var7 + var84), (int)(var8 + var26));
               if (var43) {
                  PhaseHelper.Phase var34 = PhaseHelper.initInit(objectList.length, 2200L, 550L);
                  float var35 = this.computeFloat(2.0F);
                  Object[] var36 = objectList[var34.index()];
                  this.handleColor(
                     ms,
                     var44,
                     computeClass_6880(var36[0]),
                     (String)var36[1],
                     (String)var36[2],
                     1.0F - var34.k(),
                     var7,
                     var33 - var34.k() * var35,
                     var84,
                     var9,
                     var10,
                     var11,
                     var12,
                     var17
                  );
                  if (var34.k() > 0.001F) {
                     Object[] var37 = objectList[var34.next()];
                     this.handleColor(
                        ms,
                        var44,
                        computeClass_6880(var37[0]),
                        (String)var37[1],
                        (String)var37[2],
                        var34.k(),
                        var7,
                        var33 + (1.0F - var34.k()) * var35,
                        var84,
                        var9,
                        var10,
                        var11,
                        var12,
                        var17
                     );
                  }
               } else {
                  for (PotionsHud.Entry var86 : this.linkedHashMap.values()) {
                     float var87 = (float)var86.animatedValue.getValue();
                     if (!(var87 <= 0.01F)) {
                        StatusEffectInstance var88 = var86.class_1293;
                        this.handleColor(
                           ms,
                           var44,
                           var88.getEffectType(),
                           this.computeComp(var88),
                           TextShaper.computeFormat(var88.getDuration()),
                           var87,
                           var7,
                           var33,
                           var84,
                           var9,
                           var10,
                           var11,
                           var12,
                           var17
                        );
                        var33 += var17 * var87;
                     }
                  }
               }

               var44.disableScissor();
            }
         }
      }
   }

   public void handleColor(
      MatrixStack ms,
      DrawContext ctx,
      RegistryEntry<StatusEffect> type,
      String name,
      String time,
      float a,
      float cX,
      float cY,
      float totalW,
      float cardH,
      float cPad,
      float iconSize,
      float textGap,
      float titleFont,
      float durFont
   ) {
      int var16 = Math.max(0, Math.min(255, (int)(240.0F * a)));
      boolean var17 = MainMenuScreen.isDayMode || ThemeManager.getInstance().getIsDayMode();
      int var18 = var17 ? var16 << 24 | 16777215 : var16 << 24 | 921104;
      float var19 = this.getHeight();
      if (this.getAutoHelper().isBlur()) {
         Render.shaderProgram.handleVal(ms, cX, cY, totalW, cardH, var19, var18);
      } else {
         Render.rectRenderer.handleVal(ms, cX, cY, totalW, cardH, var19, var18);
      }

      this.runContent();
      if (Interface.getInstance().isEnabled() && Interface.check2()) {
         Interface.handleVal(ms, cX, cY, totalW, cardH, var19, a, Interface.check4());
      }

      Sprite var20 = mc.getStatusEffectSpriteManager().getSprite(type);
      if (var20 != null) {
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, a);
         ctx.drawSpriteStretched(RenderLayer::getGuiTextured, var20, (int)(cX + cPad), (int)(cY + (cardH - iconSize) / 2.0F), (int)iconSize, (int)iconSize);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      float var21 = cX + cPad + iconSize + textGap;
      int var22 = (int)(255.0F * a);
      int var23 = var17 ? var22 << 24 | 988970 : var22 << 24 | 16777215;
      int var24 = var17 ? var22 << 24 | 6583435 : var22 << 24 | 11842740;
      float var25 = cY + (cardH - (titleFont + this.computeFloat(1.0F) + durFont)) / 2.0F;
      this.getMheuy().handleVal(ms, name, var21, var25, titleFont, var23);
      this.getMkadw().handleVal(ms, time, var21, var25 + titleFont + this.computeFloat(1.0F), durFont, var24);
   }

   public static RegistryEntry<StatusEffect> computeClass_6880(Object o) {
      return (RegistryEntry<StatusEffect>)o;
   }

   public String computeComp(StatusEffectInstance eff) {
      String var2 = ((StatusEffect)eff.getEffectType().value()).getName().getString();
      int var3 = eff.getAmplifier();
      if (var3 > 0) {
         var2 = var2 + " " + this.computeString(var3);
      }

      return var2;
   }

   public void handleColor(
      MatrixStack ms,
      DrawContext ctx,
      RegistryEntry<StatusEffect> type,
      String name,
      String time,
      float a,
      float x,
      float cY,
      float totalW,
      float pad,
      float iconSize,
      float gap,
      float fontSize,
      float rowH
   ) {
      int var15 = Math.max(0, Math.min(255, (int)(255.0F * a)));
      int var16 = var15 << 24 | this.getColor().getRGB() & 16777215;
      float var17 = x + pad;
      Sprite var18 = mc.getStatusEffectSpriteManager().getSprite(type);
      if (var18 != null) {
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, a);
         ctx.drawSpriteStretched(RenderLayer::getGuiTextured, var18, (int)var17, (int)(cY + (rowH - iconSize) / 2.0F), (int)iconSize, (int)iconSize);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      float var19 = cY + (rowH - fontSize) / 2.0F - this.computeFloat(0.3F);
      this.getMkadw().handleVal(ms, name, var17 + iconSize + gap, var19, fontSize, var16);
      float var20 = this.getMkadw().computeFloat(time, fontSize);
      this.getMkadw().handleVal(ms, time, x + totalW - pad - var20, var19, fontSize, var16);
   }

   public String computeString(int amp) {
      switch (amp) {
         case 1:
            return "II";
         case 2:
            return "III";
         case 3:
            return "IV";
         case 4:
            return "V";
         case 5:
            return "VI";
         case 6:
            return "VII";
         case 7:
            return "VIII";
         case 8:
            return "IX";
         case 9:
            return "X";
         default:
            return String.valueOf(amp + 1);
      }
   }

   public static boolean lambda$render$1(PotionsHud.Entry e) {
      return !e.flag && e.animatedValue.getValue() <= 0.01;
   }

   public static PotionsHud.Entry lambda$render$0(RegistryEntry k) {
      return new PotionsHud.Entry();
   }

   private static final class Entry {
      public StatusEffectInstance class_1293;
      public final AnimatedValue animatedValue = new AnimatedValue();
      public boolean flag;

      public Entry() {
      }
   }
}
