package dev.daamky.client.hud;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.render.Interface;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ArmorHud extends HudElement {
   public final ItemStack[] class_1799List = new ItemStack[6];
   public static final int intVal = -44976;

   public ArmorHud() {
      super(30.0F, 100.0F);
   }

   @Override
   public String getName() {
      return "Armor";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack ms) {
      if (mc.player != null) {
         int var2 = 0;
         this.class_1799List[var2++] = mc.player.getMainHandStack();
         this.class_1799List[var2++] = mc.player.getOffHandStack();

         for (int var3 = mc.player.getInventory().armor.size() - 1; var3 >= 0; var3--) {
            this.class_1799List[var2++] = (ItemStack)mc.player.getInventory().armor.get(var3);
         }

         boolean var27 = true;

         for (int var4 = 0; var4 < var2; var4++) {
            if (this.class_1799List[var4] != null && !this.class_1799List[var4].isEmpty()) {
               var27 = false;
               break;
            }
         }

         if (var27 && mc.currentScreen == null) {
            this.setRenderContent(false);
         } else {
            float var28 = this.getAutoHelper().getX();
            float var5 = this.getAutoHelper().getY();
            float var6 = this.computeFloat(9.5F);
            float var7 = this.computeFloat(2.5F);
            float var8 = this.computeFloat(3.2F);
            float var9 = var8 * 2.0F + var6 * 6.0F + var7 * 5.0F;
            float var10 = var8 * 2.0F + var6;
            boolean var11 = var28 < var9 + var7 || var28 > (float)mc.getWindow().getScaledWidth() - var9 - var7;
            float var12 = var11 ? var10 : var9;
            float var13 = var11 ? var9 : var10;
            this.getAutoHelper().setWidth(var12);
            this.getAutoHelper().setHeight(var13);
            DrawContext var14 = this.currentEvent.getContext();
            Color var15 = this.initInit();
            float var16 = this.getHeight();
            if (this.getAutoHelper().isBlur()) {
               Render.shaderProgram.handleVal(ms, var28, var5, var12, var13, var16, var15.getRGB());
            } else {
               Render.rectRenderer.handleVal(ms, var28, var5, var12, var13, var16, var15.getRGB());
            }

            this.runContent();
            if (Interface.getInstance().isEnabled() && Interface.check2()) {
               Interface.handleVal(ms, var28, var5, var12, var13, var16, 1.0F, Interface.check4());
            }

            float var17 = var28 + var8;
            float var18 = var5 + var8;

            for (int var19 = 0; var19 < var2; var19++) {
               ItemStack var20 = this.class_1799List[var19];
               if (var20 != null && !var20.isEmpty()) {
                  ms.push();
                  ms.translate(var17, var18, 0.0F);
                  ms.scale(var6 / 16.0F, var6 / 16.0F, 1.0F);
                  var14.drawItem(var20, 0, 0);
                  var14.drawStackOverlay(mc.textRenderer, var20, 0, 0);
                  ms.pop();
                  if (var20.isDamageable()) {
                     float var21 = (float)(var20.getMaxDamage() - var20.getDamage()) / (float)var20.getMaxDamage();
                     if (var21 < 0.2F) {
                        float var22 = this.computeFloat(4.5F);
                        float var23 = var17 + var6 - var22 / 2.0F;
                        float var24 = var18 - var22 / 2.0F;
                        this.getValue2().handleVal(ms, "m", var23, var24, var22, -44976);
                     }
                  }
               }

               if (var11) {
                  var18 += var6 + var7;
               } else {
                  var17 += var6 + var7;
               }
            }
         }
      }
   }
}
