package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.misc.InventoryHelper;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class HotbarHud extends HudElement {
   public final Map<Integer, AnimatedValue> mxacy = new HashMap<>();

   public HotbarHud() {
      super(-1.0F, -1.0F);
   }

   @Override
   public String getName() {
      return "Hotbar";
   }

   @Override
   public boolean isVisible() {
      return true;
   }

   @Override
   public void render(MatrixStack ms) {
      if (mc.player != null) {
         float var2 = this.computeFloat(4.0F);
         float var3 = this.computeFloat(18.0F);
         float var4 = this.computeFloat(2.0F);
         float var5 = var2 * 2.0F + var3 * 9.0F + var4 * 8.0F;
         float var6 = var2 * 2.0F + var3;
         int var7 = mc.getWindow().getScaledWidth();
         int var8 = mc.getWindow().getScaledHeight();
         float var9 = this.getAutoHelper().getScale();
         float var10 = var5 * var9;
         float var11 = var6 * var9;
         float var12 = (float)Math.round(((float)var7 - var10) / 2.0F);
         float var13 = (float)Math.round((float)var8 - var11 - this.computeFloat(6.0F));
         this.getAutoHelper().setWidth(var5);
         this.getAutoHelper().setHeight(var6);
         if (this.getAutoHelper().getX() < 0.0F || this.getAutoHelper().getY() < 0.0F) {
            this.getAutoHelper().setX(var12);
            this.getAutoHelper().setY(var13);
            this.getAutoHelper().lastScreenWidth = (float)var7;
            this.getAutoHelper().lastScreenHeight = (float)var8;
         }

         float var14 = this.getAutoHelper().getX();
         float var15 = this.getAutoHelper().getY();
         this.getAutoHelper().visualYOffset = 0.0F;
         DrawContext var16 = this.currentEvent.getContext();
         Color var17 = this.initInit();
         float var18 = this.getHeight();
         if (this.getAutoHelper().isBlur()) {
            Render.shaderProgram.handleRgb(ms, var14, var15, var5, var6, var18, var17);
         } else {
            Render.rectRenderer.handleRgb(ms, var14, var15, var5, var6, var18, var17);
         }

         this.runContent();
         if (Interface.getInstance().isEnabled() && Interface.check2()) {
            Interface.handleVal(ms, var14, var15, var5, var6, var18, 1.0F, Interface.check4());
         }

         int var19 = mc.player.getInventory().selectedSlot;
         float var20 = var14 + var2;
         float var21 = var15 + var2;

         for (int var22 = 0; var22 < 9; var22++) {
            boolean var24 = var22 == var19;
            AnimatedValue var25 = this.mxacy.computeIfAbsent(var22, HotbarHud::lambda$render$0);
            var25.update();
            var25.animateTo(var24 ? 1.0 : 0.0, 100L, this.getEasing());
            float var26 = (float)var25.getValue();
            boolean var27 = MainMenuScreen.isDayMode || ThemeManager.getInstance().getIsDayMode();
            Color var28 = var27 ? new Color(220, 220, 220, 100) : new Color(30, 30, 30, 80);
            if (var26 > 0.0F) {
               var28 = EntityHelper.initBlue(var28, var27 ? new Color(0, 0, 0, 40) : new Color(255, 255, 255, 50), var26);
            }

            Render.rectRenderer.handleRgb(ms, var20, var21, var3, var3, this.computeFloat(2.0F), var28);
            ItemStack var29 = (ItemStack)mc.player.getInventory().main.get(var22);
            InventoryHelper var30 = InventoryHelper.getInstance();
            Color var23;
            if (var30.isEnabled() && (var23 = var30.computeColors(var29)) != null) {
               double var31 = Math.sin((double)System.currentTimeMillis() * 0.0055);
               int var33 = (int)(60.0 + 90.0 * (var31 * 0.5 + 0.5));
               Render.rectRenderer
                  .handleRgb(ms, var20, var21, var3, var3, this.computeFloat(2.0F), new Color(var23.getRed(), var23.getGreen(), var23.getBlue(), var33));
            }

            if (!var29.isEmpty()) {
               ms.push();
               float var36 = this.computeFloat(16.0F);
               float var32 = (var3 - var36) / 2.0F;
               ms.translate(var20 + var32, var21 + var32, 0.0F);
               ms.scale(var36 / 16.0F, var36 / 16.0F, 1.0F);
               var16.drawItem(var29, 0, 0);
               if (var29.getCount() > 1) {
                  ms.translate(0.0F, 0.0F, 200.0F);
                  String var39 = String.valueOf(var29.getCount());
                  float var34 = this.getMkadw().computeFloat(var39, 5.5F);
                  this.getMkadw().handleColor(ms, var39, 17.0F - var34, 11.0F, 5.5F, Color.WHITE);
               }

               ms.pop();
            } else {
               float var37 = var20 + var3 / 2.0F;
               float var38 = var21 + var3 / 2.0F;
               String var40 = String.valueOf(var22 + 1);
               float var41 = this.computeFloat(5.5F);
               float var35 = this.getValue().computeFloat(var40, var41);
               this.getValue().handleColor(ms, var40, var37 - var35 / 2.0F, var38 - var41 / 2.0F + 0.5F, var41, new Color(255, 255, 255, 60));
            }

            var20 += var3 + var4;
         }
      }
   }

   public static AnimatedValue lambda$render$0(Integer k) {
      return new AnimatedValue();
   }
}
