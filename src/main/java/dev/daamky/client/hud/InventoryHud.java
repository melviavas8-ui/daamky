package dev.daamky.client.hud;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class InventoryHud extends HudElement {
   public static final ItemStack[] mdasj = new ItemStack[]{
      Items.ENDER_PEARL.getDefaultStack(), Items.FIREWORK_ROCKET.getDefaultStack(), Items.ELYTRA.getDefaultStack()
   };
   public static final int intVal = -939524096;

   public InventoryHud() {
      super(50.0F, 50.0F);
   }

   @Override
   public String getName() {
      return "Inventory";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack ms) {
      float var2 = this.computeFloat(11.2F);
      float var3 = this.computeFloat(1.8F);
      float var4 = 9.0F * var2 + 8.0F * var3;
      float var5 = 3.0F * var2 + 2.0F * var3;
      float var6 = this.computeFloat(5.5F);
      float var7 = this.computeFloat(4.5F);
      float var8 = var4 + var6 * 2.0F;
      float var9 = var5 + var7 * 2.0F;
      this.getAutoHelper().setWidth(var8);
      this.getAutoHelper().setHeight(var9);
      DrawContext var10 = this.currentEvent.getContext();
      float var11 = this.getAutoHelper().getX();
      float var12 = this.getAutoHelper().getY();
      float var13 = this.getHeight();
      Color var14 = this.initInit();
      boolean var15 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      int var16 = var15 ? 201326592 : 167772159;
      int var17 = var15 ? 469762048 : 318767103;
      if (this.getAutoHelper().isBlur()) {
         Render.shaderProgram.handleVal(ms, var11, var12, var8, var9, var13, var14.getRGB());
      } else {
         Render.rectRenderer.handleVal(ms, var11, var12, var8, var9, var13, var14.getRGB());
      }

      this.runContent();
      if (Interface.getInstance().isEnabled() && Interface.check2()) {
         Interface.handleVal(ms, var11, var12, var8, var9, var13, 1.0F, Interface.check4());
      }

      float var18 = var11 + (var8 - var4) / 2.0F;
      float var19 = var12 + (var9 - var5) / 2.0F;
      float var20 = this.computeFloat(8.5F);
      boolean var21 = false;
      if (mc.player != null && mc.player.getInventory() != null) {
         for (int var22 = 9; var22 < 36 && var22 < mc.player.getInventory().main.size(); var22++) {
            ItemStack var23 = (ItemStack)mc.player.getInventory().main.get(var22);
            if (var23 != null && !var23.isEmpty()) {
               var21 = true;
               break;
            }
         }
      }

      int var35 = 9;

      for (int var36 = 0; var36 < 3; var36++) {
         float var24 = var19 + (float)var36 * (var2 + var3);

         for (int var25 = 0; var25 < 9; var25++) {
            float var26 = var18 + (float)var25 * (var2 + var3);
            Render.rectRenderer.handleVal(ms, var26, var24, var2, var2, this.computeFloat(1.8F), var16);
            ItemStack var27 = ItemStack.EMPTY;
            if (mc.player != null && mc.player.getInventory() != null && var35 < mc.player.getInventory().main.size()) {
               ItemStack var28 = (ItemStack)mc.player.getInventory().main.get(var35);
               if (var28 != null && !var28.isEmpty()) {
                  var27 = var28;
               }
            }

            if (var27.isEmpty() && !var21 && var35 - 9 < mdasj.length) {
               var27 = mdasj[var35 - 9];
            }

            if (!var27.isEmpty()) {
               float var37 = var26 + (var2 - var20) / 2.0F;
               float var29 = var24 + (var2 - var20) / 2.0F;
               ms.push();
               ms.translate(var37, var29, 0.0F);
               float var30 = var20 / 16.0F;
               ms.scale(var30, var30, 1.0F);
               var10.drawItem(var27, 0, 0);
               if (var27.getCount() > 1) {
                  ms.translate(0.0F, 0.0F, 200.0F);
                  String var31 = Integer.toString(var27.getCount());
                  float var32 = FontBootstrap.mdoswa.computeFloat(var31, 5.0F);
                  float var33 = 16.0F - var32;
                  float var34 = 10.0F;
                  FontBootstrap.mdoswa.handleVal(ms, var31, var33 + 0.5F, var34 + 0.5F, 5.0F, -939524096);
                  FontBootstrap.mdoswa.handleVal(ms, var31, var33, var34, 5.0F, -1);
               }

               ms.pop();
            } else {
               float var38 = this.computeFloat(1.6F);
               float var39 = var26 + (var2 - var38) / 2.0F;
               float var40 = var24 + (var2 - var38) / 2.0F;
               Render.rectRenderer.handleVal(ms, var39, var40, var38, var38, var38 / 2.0F, var17);
            }

            var35++;
         }
      }
   }
}
