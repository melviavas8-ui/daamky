package dev.daamky.client.gui;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.GlyphMap;
import dev.daamky.client.internal.RenderKit;
import dev.daamky.client.model.enums.AnimationDirection;
import dev.daamky.client.module.misc.InventoryHelper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class InventoryHelperScreen extends Screen {
   public final List<InventoryHelperScreen.SlotBox> list = new ArrayList<>();
   public String string = null;
   public String string2 = null;
   public static final Color[] colorList = new Color[]{
      new Color(255, 60, 60),
      new Color(255, 140, 0),
      new Color(255, 220, 60),
      new Color(60, 255, 60),
      new Color(60, 255, 255),
      new Color(60, 160, 255),
      new Color(180, 80, 255),
      new Color(255, 105, 180),
      new Color(240, 240, 240)
   };
   public final Animation animation = new Direction2(220, 1.0, AnimationDirection.FORWARDS);

   public InventoryHelperScreen() {
      super(Text.of("Inventory Helper"));
   }

   public void init() {
      this.animation.getTimerUtil();
      this.animation.setDirection(AnimationDirection.FORWARDS);
      this.list.clear();
      byte var1 = 24;
      byte var2 = 3;
      int var3 = 9 * var1 + 8 * var2;
      int var4 = 4 * var1 + 3 * var2 + 10;
      int var5 = var1 + 12 + var3 + 12 + var1;
      int var6 = (this.width - var5) / 2 + var1 + 12;
      int var7 = (this.height - var4) / 2;
      int var8 = var6 - var1 - 12;

      for (int var9 = 0; var9 < 4; var9++) {
         this.list.add(new InventoryHelperScreen.SlotBox(39 - var9, var8, var7 + var9 * (var1 + var2), var1, var1, true, false));
      }

      for (int var12 = 0; var12 < 3; var12++) {
         for (int var10 = 0; var10 < 9; var10++) {
            int var11 = 9 + var12 * 9 + var10;
            this.list.add(new InventoryHelperScreen.SlotBox(var11, var6 + var10 * (var1 + var2), var7 + var12 * (var1 + var2), var1, var1, false, false));
         }
      }

      int var13 = var7 + 3 * (var1 + var2) + 10;

      for (int var14 = 0; var14 < 9; var14++) {
         this.list.add(new InventoryHelperScreen.SlotBox(var14, var6 + var14 * (var1 + var2), var13, var1, var1, false, false));
      }

      int var15 = var6 + 9 * (var1 + var2) + 12;
      this.list.add(new InventoryHelperScreen.SlotBox(40, var15, var13, var1, var1, false, true));
   }

   public ItemStack computeSlot(InventoryHelperScreen.SlotBox box) {
      return this.client != null && this.client.player != null
         ? this.client.player.getInventory().getStack(box.slotIndex)
         : ItemStack.EMPTY;
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      float var5 = this.animation.getEnd();
      if (!(var5 <= 0.001F)) {
         RenderKit.Render2D.handleFloat(0.0F, 0.0F, (float)this.width, (float)this.height, GlyphMap.computeRound2(-16777216, var5 * 0.7F));
         byte var6 = 24;
         byte var7 = 3;
         int var8 = 9 * var6 + 8 * var7;
         int var9 = 4 * var6 + 3 * var7 + 10;
         int var10 = var6 + 12 + var8 + 12 + var6;
         byte var11 = 14;
         byte var12 = 14;
         float var13 = (float)(this.width - var10) / 2.0F - (float)var11;
         float var14 = (float)(this.height - var9) / 2.0F - (float)var12;
         float var15 = (float)(var10 + var11 * 2);
         float var16 = (float)(var9 + var12 * 2);
         int var17 = GlyphMap.computeRound2(-14275523, var5 * 0.9F);
         int var18 = GlyphMap.computeRound2(-15921129, var5 * 0.92F);
         RenderKit.Render2D.handleFloat(var13, var14, var15, var16, var17, 8.0F);
         RenderKit.Render2D.handleFloat(var13 + 1.0F, var14 + 1.0F, var15 - 2.0F, var16 - 2.0F, var18, 7.5F);
         int var19 = (this.width - var10) / 2 + var6 + 12;
         int var20 = (this.height - var9) / 2;
         float var21 = (float)(var20 + 3 * (var6 + var7)) + 4.5F;
         RenderKit.Render2D.handleFloat((float)(var19 - 2), var21, (float)(var8 + 4), 1.0F, GlyphMap.computeRound2(-14670282, var5 * 0.8F));
         InventoryHelper var22 = InventoryHelper.getInstance();

         for (InventoryHelperScreen.SlotBox var24 : this.list) {
            boolean var25 = mouseX >= var24.x && mouseX <= var24.x + var24.w && mouseY >= var24.y && mouseY <= var24.y + var24.h;
            ItemStack var26 = this.computeSlot(var24);
            String var27 = InventoryHelper.computeTrim(var26);
            boolean var28 = var27 != null && var27.equals(this.string);
            Color var29 = var27 != null ? var22.getItemColors().get(var27) : null;
            int var30 = var25 ? -14933712 : -15460574;
            int var31 = var25 ? -12695714 : -14538952;
            if (var29 != null) {
               int var32 = var29.getRGB();
               var30 = GlyphMap.computeInt2(var30, GlyphMap.computeRound2(var32, 0.22F), 1.0F);
               var31 = GlyphMap.computeRound2(var32, var25 ? 0.95F : 0.7F);
            }

            RenderKit.Render2D.handleFloat((float)var24.x, (float)var24.y, (float)var24.w, (float)var24.h, GlyphMap.computeRound2(var31, var5), 4.5F);
            RenderKit.Render2D.handleFloat(
               (float)var24.x + 1.0F, (float)var24.y + 1.0F, (float)var24.w - 2.0F, (float)var24.h - 2.0F, GlyphMap.computeRound2(var30, var5), 4.0F
            );
            if (var28) {
               double var49 = Math.sin((double)System.currentTimeMillis() * 0.008);
               float var34 = (float)(0.65 + 0.35 * (var49 * 0.5 + 0.5));
               RenderKit.Render2D.handleFloat(
                  (float)var24.x - 1.0F,
                  (float)var24.y - 1.0F,
                  (float)var24.w + 2.0F,
                  (float)var24.h + 2.0F,
                  1.5F,
                  GlyphMap.computeRound2(-1, var5 * var34),
                  5.0F
               );
            }

            if (!var26.isEmpty()) {
               context.drawItem(var26, var24.x + 4, var24.y + 4);
               context.drawStackOverlay(this.textRenderer, var26, var24.x + 4, var24.y + 4);
            }
         }

         if (this.string != null) {
            byte var40 = 18;
            byte var41 = 6;
            int var42 = 10 * var40 + 9 * var41 + 20;
            byte var43 = 44;
            float var44 = (float)(this.width - var42) / 2.0F;
            float var45 = var14 + var16 + 12.0F;
            int var46 = GlyphMap.computeRound2(-14275523, var5 * 0.9F);
            int var47 = GlyphMap.computeRound2(-15921129, var5 * 0.95F);
            RenderKit.Render2D.handleFloat(var44, var45, (float)var42, (float)var43, var46, 7.0F);
            RenderKit.Render2D.handleFloat(var44 + 1.0F, var45 + 1.0F, (float)var42 - 2.0F, (float)var43 - 2.0F, var47, 6.5F);
            String var48 = this.string2 != null ? this.string2 : this.string;
            FontBootstrap.mheuy.handleVal(context.getMatrices(), var48, var44 + 12.0F, var45 + 6.0F, 6.0F, GlyphMap.computeRound2(-1906448, var5));
            Color var50 = var22.getItemColors().get(this.string);
            float var33 = var45 + 18.0F;
            float var51 = var44 + 10.0F;

            for (int var35 = 0; var35 < 9; var35++) {
               float var36 = var51 + (float)(var35 * (var40 + var41));
               boolean var37 = (float)mouseX >= var36
                  && (float)mouseX <= var36 + (float)var40
                  && (float)mouseY >= var33
                  && (float)mouseY <= var33 + (float)var40;
               Color var38 = colorList[var35];
               boolean var39 = var50 != null && var50.equals(var38);
               RenderKit.Render2D.handleFloat(var36, var33, (float)var40, (float)var40, GlyphMap.computeRound2(var38.getRGB(), var5), (float)var40 / 2.0F);
               if (var39) {
                  RenderKit.Render2D.handleFloat(
                     var36 - 2.0F,
                     var33 - 2.0F,
                     (float)var40 + 4.0F,
                     (float)var40 + 4.0F,
                     1.5F,
                     GlyphMap.computeRound2(-1, var5 * 0.95F),
                     ((float)var40 + 4.0F) / 2.0F
                  );
               } else if (var37) {
                  RenderKit.Render2D.handleFloat(
                     var36 - 1.0F,
                     var33 - 1.0F,
                     (float)var40 + 2.0F,
                     (float)var40 + 2.0F,
                     1.2F,
                     GlyphMap.computeRound2(-1, var5 * 0.6F),
                     ((float)var40 + 2.0F) / 2.0F
                  );
               }
            }

            float var52 = var51 + (float)(9 * (var40 + var41));
            boolean var53 = (float)mouseX >= var52 && (float)mouseX <= var52 + (float)var40 && (float)mouseY >= var33 && (float)mouseY <= var33 + (float)var40;
            int var54 = var53 ? -12902876 : -14543334;
            int var55 = var53 ? -1096636 : -9756376;
            RenderKit.Render2D.handleFloat(var52, var33, (float)var40, (float)var40, GlyphMap.computeRound2(var55, var5), 4.0F);
            RenderKit.Render2D.handleFloat(var52 + 1.0F, var33 + 1.0F, (float)var40 - 2.0F, (float)var40 - 2.0F, GlyphMap.computeRound2(var54, var5), 3.5F);
            FontBootstrap.clientFont2
               .handleVal2(
                  context.getMatrices(), "×", var52 + (float)var40 / 2.0F, var33 + 3.0F, 7.5F, GlyphMap.computeRound2(var53 ? -1096636 : -1906448, var5)
               );
         }
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         for (InventoryHelperScreen.SlotBox var7 : this.list) {
            if (mouseX >= (double)var7.x && mouseX <= (double)(var7.x + var7.w) && mouseY >= (double)var7.y && mouseY <= (double)(var7.y + var7.h)) {
               ItemStack var8 = this.computeSlot(var7);
               String var9 = InventoryHelper.computeTrim(var8);
               if (var9 == null) {
                  this.string = null;
                  this.string2 = null;
               } else {
                  this.string = var9;
                  this.string2 = var8.getName().getString();
               }

               return true;
            }
         }

         if (this.string != null) {
            byte var24 = 24;
            byte var25 = 3;
            int var26 = 9 * var24 + 8 * var25;
            int var27 = 4 * var24 + 3 * var25 + 10;
            int var10 = var24 + 12 + var26 + 12 + var24;
            byte var11 = 14;
            float var12 = (float)(this.height - var27) / 2.0F - (float)var11;
            float var13 = (float)(var27 + var11 * 2);
            byte var14 = 18;
            byte var15 = 6;
            int var16 = 10 * var14 + 9 * var15 + 20;
            byte var17 = 44;
            float var18 = (float)(this.width - var16) / 2.0F;
            float var19 = var12 + var13 + 12.0F;
            float var20 = var19 + 18.0F;
            float var21 = var18 + 10.0F;

            for (int var22 = 0; var22 < 9; var22++) {
               float var23 = var21 + (float)(var22 * (var14 + var15));
               if (mouseX >= (double)var23 && mouseX <= (double)(var23 + (float)var14) && mouseY >= (double)var20 && mouseY <= (double)(var20 + (float)var14)) {
                  InventoryHelper.getInstance().getItemColors().put(this.string, colorList[var22]);
                  InventoryHelper.getInstance().initInit();
                  return true;
               }
            }

            float var28 = var21 + (float)(9 * (var14 + var15));
            if (mouseX >= (double)var28 && mouseX <= (double)(var28 + (float)var14) && mouseY >= (double)var20 && mouseY <= (double)(var20 + (float)var14)) {
               InventoryHelper.getInstance().getItemColors().remove(this.string);
               InventoryHelper.getInstance().initInit();
               return true;
            }
         }

         this.string = null;
         this.string2 = null;
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean shouldPause() {
      return false;
   }

   private static class SlotBox {
      public final int slotIndex;
      public final int x;
      public final int y;
      public final int w;
      public final int h;
      public final boolean isArmor;
      public final boolean isOffhand;

      public SlotBox(int slotIndex, int x, int y, int w, int h, boolean isArmor, boolean isOffhand) {
         this.slotIndex = slotIndex;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
         this.isArmor = isArmor;
         this.isOffhand = isOffhand;
      }
   }
}
