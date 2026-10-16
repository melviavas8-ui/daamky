package dev.daamky.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.Geometry;
import dev.daamky.client.internal.Render;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.combat.AutoSwap;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class SwapRadialMenuScreen extends Screen {
   public final float floatVal = 46.0F;
   public final float float2 = 84.0F;
   public final List<SwapRadialMenuScreen.RadialSlot> list = new ArrayList<>();
   public int intVal = -1;
   public int mebro = -1;
   public final AnimatedValue mlovb = new AnimatedValue();
   public final AnimatedValue mkedb = new AnimatedValue();

   public SwapRadialMenuScreen() {
      super(Text.of("SwapRadialMenu"));
   }

   public void init() {
      super.init();
      this.list.clear();
      this.mlovb.handleStart(0.0);
      this.mkedb.handleStart(0.0);
      this.mebro = -1;
      this.initInit();
   }

   public void initInit() {
      this.list.clear();
      AutoSwap var1 = AutoSwap.getInstance();
      Item var2 = var1.getHead();
      Item var3 = var1.getTotem();
      Item var4 = var1.getGolden();
      this.list
         .add(
            new SwapRadialMenuScreen.RadialSlot(
               var2 == Items.AIR ? ItemStack.EMPTY : new ItemStack(var2),
               var2 == Items.AIR ? "Пусто" : var2.getName().getString()
            )
         );
      this.list
         .add(
            new SwapRadialMenuScreen.RadialSlot(
               var3 == Items.AIR ? ItemStack.EMPTY : new ItemStack(var3),
               var3 == Items.AIR ? "Пусто" : var3.getName().getString()
            )
         );
      this.list
         .add(
            new SwapRadialMenuScreen.RadialSlot(
               var4 == Items.AIR ? ItemStack.EMPTY : new ItemStack(var4),
               var4 == Items.AIR ? "Пусто" : var4.getName().getString()
            )
         );
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.mlovb.update();
      this.mlovb.animateTo(1.0, 180L, Easing.QUINT_OUT, true);
      float var5 = (float)this.mlovb.getValue();
      float var6 = (float)this.width / 2.0F;
      float var7 = (float)this.height / 2.0F;
      if (this.mebro == -1) {
         this.intVal = this.computeAtan((double)mouseX, (double)mouseY, var6, var7);
      }

      MatrixStack var8 = context.getMatrices();
      var8.push();
      float var9 = 0.88F + 0.12F * var5;
      var8.translate(var6, var7, 0.0F);
      var8.scale(var9, var9, 1.0F);
      var8.translate(-var6, -var7, 0.0F);
      int var10 = this.list.size();
      float var11 = (46.0F + 84.0F) / 2.0F;

      for (int var12 = 0; var12 < var10; var12++) {
         SwapRadialMenuScreen.RadialSlot var13 = this.list.get(var12);
         var13.animatedValue.update();
         boolean var14 = var12 == this.intVal;
         var13.animatedValue.animateTo(var14 ? 1.0 : 0.0, 160L, Easing.QUINT_OUT, true);
         float var15 = (float)var13.animatedValue.getValue();
         double var16 = (-Math.PI / 2) + (Math.PI * 2) / (double)var10 * (double)var12;
         double var18 = var16 + (Math.PI * 2) / (double)var10;
         double var20 = 0.045;
         double var22 = var16 + var20;
         double var24 = var18 - var20;
         double var26 = (var16 + var18) / 2.0;
         float var28 = var15 * 5.0F;
         float var29 = (float)Math.cos(var26) * var28;
         float var30 = (float)Math.sin(var26) * var28;
         int var31 = Math.max(0, Math.min(255, (int)(200.0F * var5)));
         int var32 = Math.max(0, Math.min(255, (int)(235.0F * var5)));
         Color var33 = new Color(12, 14, 20, var31);
         Color var34 = ColorMath.initBlue(var33, new Color(24, 34, 52, var32), var15);
         int var35 = Math.max(0, Math.min(255, (int)(25.0F * var5)));
         int var36 = Math.max(0, Math.min(255, (int)(245.0F * var5)));
         Color var37 = ColorMath.initBlue(new Color(255, 255, 255, var35), new Color(90, 168, 255, var36), var15);
         this.handleCos(var8.peek().getPositionMatrix(), var6 + var29, var7 + var30, 46.0F, 84.0F, var22, var24, var34, var37);
         float var38 = var6 + var29 + (float)Math.cos(var26) * var11;
         float var39 = var7 + var30 + (float)Math.sin(var26) * var11;
         if (!var13.stack.isEmpty() && var13.stack.getItem() != Items.AIR) {
            context.drawItem(var13.stack, (int)var38 - 8, (int)var39 - 8);
            int var54 = this.computeInt(var13.stack.getItem());
            String var55 = String.valueOf(var54);
            float var56 = FontBootstrap.mdoswa.computeFloat(var55, 6.0F);
            int var43 = Math.max(0, Math.min(255, (int)(255.0F * var5)));
            Color var44 = var54 > 0 ? new Color(255, 255, 255, var43) : new Color(255, 80, 80, var43);
            FontBootstrap.mdoswa.handleColor(var8, var55, var38 + 8.0F - var56, var39 + 2.0F, 6.0F, var44);
         } else {
            int var40 = Math.max(0, Math.min(255, (int)((140.0F + var15 * 115.0F) * var5)));
            Color var41 = ColorMath.initBlue(new Color(160, 165, 175, var40), new Color(90, 168, 255, var40), var15);
            float var42 = FontBootstrap.mdoswa.computeFloat("+", 13.0F);
            FontBootstrap.mdoswa.handleColor(var8, "+", var38 - var42 / 2.0F + 0.5F, var39 - 6.5F, 13.0F, var41);
         }
      }

      int var45 = Math.max(0, Math.min(255, (int)(255.0F * var5)));
      if (this.intVal >= 0 && this.intVal < this.list.size()) {
         SwapRadialMenuScreen.RadialSlot var47 = this.list.get(this.intVal);
         boolean var49 = var47.stack.isEmpty() || var47.stack.getItem() == Items.AIR;
         String var51 = var49 ? "Слот " + (this.intVal + 1) + " (Пусто)" : var47.label;
         float var52 = FontBootstrap.mdoswa.computeFloat(var51, 8.5F);
         FontBootstrap.mdoswa.handleColor(var8, var51, var6 - var52 / 2.0F, var7 + 84.0F + 10.0F, 8.5F, new Color(255, 255, 255, var45));
         String var17 = var49 ? "ЛКМ / ПКМ – Выбрать предмет из инвентаря" : "ЛКМ – Свапнуть  |  ПКМ – Сменить / Очистить";
         float var53 = FontBootstrap.clientFont11.computeFloat(var17, 5.5F);
         int var19 = Math.max(0, Math.min(255, (int)(220.0F * var5)));
         FontBootstrap.clientFont11.handleColor(var8, var17, var6 - var53 / 2.0F, var7 + 84.0F + 22.0F, 5.5F, new Color(130, 180, 255, var19));
      } else {
         String var46 = "Наведите курсор в любую сторону для выбора";
         float var48 = FontBootstrap.clientFont11.computeFloat(var46, 6.0F);
         int var50 = Math.max(0, Math.min(255, (int)(180.0F * var5)));
         FontBootstrap.clientFont11.handleColor(var8, var46, var6 - var48 / 2.0F, var7 + 84.0F + 14.0F, 6.0F, new Color(170, 175, 185, var50));
      }

      if (this.mebro != -1) {
         this.initInit(context, var8, mouseX, mouseY, var6, var7, var5);
      }

      var8.pop();
   }

   public void initInit(DrawContext context, MatrixStack ms, int mouseX, int mouseY, float cx, float cy, float intro) {
      this.mkedb.update();
      this.mkedb.animateTo(1.0, 160L, Easing.QUINT_OUT, true);
      float var8 = (float)this.mkedb.getValue();
      float var9 = 205.0F;
      float var10 = 126.0F;
      float var11 = cx - var9 / 2.0F;
      float var12 = cy - var10 / 2.0F;
      Vector4f var13 = new Vector4f(6.0F, 6.0F, 6.0F, 6.0F);
      int var14 = Math.max(0, Math.min(255, (int)(220.0F * var8)));
      int var15 = Math.max(0, Math.min(255, (int)(245.0F * var8)));
      int var16 = Math.max(0, Math.min(255, (int)(250.0F * var8)));
      Color var17 = new Color(90, 168, 255, var14);
      Color var18 = new Color(14, 16, 24, var15);
      Color var19 = new Color(8, 10, 16, var16);
      Render.rectRenderer.handleRgb(ms, var11 - 0.6F, var12 - 0.6F, var9 + 1.2F, var10 + 1.2F, 6.6F, var17);
      Render.shaderProgram.handleRgb(ms, var11, var12, var9, var10, var13, var18, var18, var19, var19, 0.25F);
      String var20 = "Инвентарь игрока • Слот " + (this.mebro + 1);
      FontBootstrap.mdoswa.handleColor(ms, var20, var11 + 10.0F, var12 + 8.0F, 6.5F, new Color(255, 255, 255, var14));
      float var21 = 54.0F;
      float var22 = 12.0F;
      float var23 = var11 + var9 - var21 - 8.0F;
      float var24 = var12 + 6.0F;
      boolean var25 = Geometry.computeFlag((float)mouseX, (float)mouseY, var23, var24, var21, var22);
      Color var26 = var25 ? new Color(255, 90, 90, var14) : new Color(255, 255, 255, (int)(140.0F * var8));
      Render.rectRenderer
         .handleRgb(ms, var23, var24, var21, var22, 3.0F, var25 ? new Color(255, 70, 70, (int)(50.0F * var8)) : new Color(255, 255, 255, (int)(15.0F * var8)));
      FontBootstrap.clientFont10.handleColor(ms, "[Очистить слот]", var23 + 4.0F, var24 + 2.5F, 5.0F, var26);
      float var27 = 18.0F;
      float var28 = 2.0F;
      float var29 = var11 + 10.0F;
      float var30 = var12 + 24.0F;
      if (MinecraftAccess.mc.player != null) {
         for (int var31 = 0; var31 < 3; var31++) {
            for (int var32 = 0; var32 < 9; var32++) {
               int var33 = 9 + var31 * 9 + var32;
               ItemStack var34 = MinecraftAccess.mc.player.getInventory().getStack(var33);
               float var35 = var29 + (float)var32 * (var27 + var28);
               float var36 = var30 + (float)var31 * (var27 + var28);
               boolean var37 = Geometry.computeFlag((float)mouseX, (float)mouseY, var35, var36, var27, var27);
               Color var38 = var37 ? new Color(90, 168, 255, (int)(90.0F * var8)) : new Color(255, 255, 255, (int)(12.0F * var8));
               Render.rectRenderer.handleRgb(ms, var35, var36, var27, var27, 3.5F, var38);
               if (!var34.isEmpty()) {
                  context.drawItem(var34, (int)var35 + 1, (int)var36 + 1);
                  context.drawStackOverlay(MinecraftAccess.mc.textRenderer, var34, (int)var35 + 1, (int)var36 + 1);
               }
            }
         }

         float var40 = var30 + 3.0F * (var27 + var28) + 2.0F;
         Render.rectRenderer.handleRgb(ms, var29, var40, 9.0F * var27 + 8.0F * var28, 1.0F, 0.0F, new Color(255, 255, 255, (int)(25.0F * var8)));
         float var41 = var40 + 4.0F;

         for (int var42 = 0; var42 < 9; var42++) {
            ItemStack var44 = MinecraftAccess.mc.player.getInventory().getStack(var42);
            float var46 = var29 + (float)var42 * (var27 + var28);
            boolean var48 = Geometry.computeFlag((float)mouseX, (float)mouseY, var46, var41, var27, var27);
            Color var39 = var48 ? new Color(90, 168, 255, (int)(90.0F * var8)) : new Color(255, 255, 255, (int)(16.0F * var8));
            Render.rectRenderer.handleRgb(ms, var46, var41, var27, var27, 3.5F, var39);
            if (!var44.isEmpty()) {
               context.drawItem(var44, (int)var46 + 1, (int)var41 + 1);
               context.drawStackOverlay(MinecraftAccess.mc.textRenderer, var44, (int)var46 + 1, (int)var41 + 1);
            }
         }

         float var43 = var29 + 9.0F * (var27 + var28) + 4.0F;
         boolean var45 = Geometry.computeFlag((float)mouseX, (float)mouseY, var43, var41, var27, var27);
         Render.rectRenderer
            .handleRgb(
               ms, var43, var41, var27, var27, 3.5F, var45 ? new Color(90, 168, 255, (int)(90.0F * var8)) : new Color(255, 255, 255, (int)(16.0F * var8))
            );
         ItemStack var47 = MinecraftAccess.mc.player.getOffHandStack();
         if (!var47.isEmpty()) {
            context.drawItem(var47, (int)var43 + 1, (int)var41 + 1);
            context.drawStackOverlay(MinecraftAccess.mc.textRenderer, var47, (int)var43 + 1, (int)var41 + 1);
         }
      }
   }

   public int computeAtan(double mouseX, double mouseY, float cx, float cy) {
      double var7 = Math.hypot(mouseX - (double)cx, mouseY - (double)cy);
      if (var7 < 10.0) {
         return -1;
      } else {
         double var9 = Math.atan2(mouseY - (double)cy, mouseX - (double)cx) + (Math.PI / 2);
         if (var9 < 0.0) {
            var9 += Math.PI * 2;
         }

         int var11 = this.list.size();
         if (var11 == 0) {
            return -1;
         } else {
            int var12 = (int)(var9 / ((Math.PI * 2) / (double)var11));
            return Math.min(var12, var11 - 1);
         }
      }
   }

   public void handleCos(Matrix4f matrix, float cx, float cy, float innerR, float outerR, double start, double end, Color fill, Color outline) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder var12 = Tessellator.getInstance().begin(DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
      byte var13 = 48;
      double var14 = (end - start) / (double)var13;
      float var16 = (float)fill.getRed() / 255.0F;
      float var17 = (float)fill.getGreen() / 255.0F;
      float var18 = (float)fill.getBlue() / 255.0F;
      float var19 = (float)fill.getAlpha() / 255.0F;

      for (int var20 = 0; var20 <= var13; var20++) {
         double var21 = start + var14 * (double)var20;
         float var23 = (float)Math.cos(var21);
         float var24 = (float)Math.sin(var21);
         var12.vertex(matrix, cx + var23 * outerR, cy + var24 * outerR, 0.0F).color(var16, var17, var18, var19);
         var12.vertex(matrix, cx + var23 * innerR, cy + var24 * innerR, 0.0F).color(var16, var17, var18, var19);
      }

      BufferRenderer.drawWithGlobalProgram(var12.end());
      BufferBuilder var30 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      float var31 = (float)outline.getRed() / 255.0F;
      float var22 = (float)outline.getGreen() / 255.0F;
      float var32 = (float)outline.getBlue() / 255.0F;
      float var33 = (float)outline.getAlpha() / 255.0F;

      for (int var25 = 0; var25 < var13; var25++) {
         double var26 = start + var14 * (double)var25;
         double var28 = start + var14 * (double)(var25 + 1);
         var30.vertex(matrix, cx + (float)Math.cos(var26) * outerR, cy + (float)Math.sin(var26) * outerR, 0.0F).color(var31, var22, var32, var33);
         var30.vertex(matrix, cx + (float)Math.cos(var28) * outerR, cy + (float)Math.sin(var28) * outerR, 0.0F).color(var31, var22, var32, var33);
         var30.vertex(matrix, cx + (float)Math.cos(var26) * innerR, cy + (float)Math.sin(var26) * innerR, 0.0F).color(var31, var22, var32, var33);
         var30.vertex(matrix, cx + (float)Math.cos(var28) * innerR, cy + (float)Math.sin(var28) * innerR, 0.0F).color(var31, var22, var32, var33);
      }

      var30.vertex(matrix, cx + (float)Math.cos(start) * innerR, cy + (float)Math.sin(start) * innerR, 0.0F).color(var31, var22, var32, var33);
      var30.vertex(matrix, cx + (float)Math.cos(start) * outerR, cy + (float)Math.sin(start) * outerR, 0.0F).color(var31, var22, var32, var33);
      var30.vertex(matrix, cx + (float)Math.cos(end) * innerR, cy + (float)Math.sin(end) * innerR, 0.0F).color(var31, var22, var32, var33);
      var30.vertex(matrix, cx + (float)Math.cos(end) * outerR, cy + (float)Math.sin(end) * outerR, 0.0F).color(var31, var22, var32, var33);
      BufferRenderer.drawWithGlobalProgram(var30.end());
      RenderSystem.disableBlend();
   }

   public int computeInt(Item item) {
      if (MinecraftAccess.mc.player != null && item != null && item != Items.AIR) {
         int var2 = 0;

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = MinecraftAccess.mc.player.getInventory().getStack(var3);
            if (!var4.isEmpty() && var4.getItem() == item) {
               var2 += var4.getCount();
            }
         }

         return var2;
      } else {
         return 0;
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      float var6 = (float)this.width / 2.0F;
      float var7 = (float)this.height / 2.0F;
      if (this.mebro != -1) {
         float var28 = 205.0F;
         float var9 = 126.0F;
         float var10 = var6 - var28 / 2.0F;
         float var11 = var7 - var9 / 2.0F;
         float var12 = 54.0F;
         float var13 = 12.0F;
         float var14 = var10 + var28 - var12 - 8.0F;
         float var15 = var11 + 6.0F;
         if (Geometry.computeFlag(mouseX, mouseY, (double)var14, (double)var15, (double)var12, (double)var13)) {
            AutoSwap.getInstance().handleGolden(this.mebro, Items.AIR);
            this.initInit();
            this.mebro = -1;
            return true;
         } else {
            float var16 = 18.0F;
            float var17 = 2.0F;
            float var18 = var10 + 10.0F;
            float var19 = var11 + 24.0F;
            if (MinecraftAccess.mc.player != null) {
               for (int var20 = 0; var20 < 3; var20++) {
                  for (int var21 = 0; var21 < 9; var21++) {
                     int var22 = 9 + var20 * 9 + var21;
                     ItemStack var23 = MinecraftAccess.mc.player.getInventory().getStack(var22);
                     float var24 = var18 + (float)var21 * (var16 + var17);
                     float var25 = var19 + (float)var20 * (var16 + var17);
                     if (Geometry.computeFlag(mouseX, mouseY, (double)var24, (double)var25, (double)var16, (double)var16)) {
                        Item var26 = var23.isEmpty() ? Items.AIR : var23.getItem();
                        AutoSwap.getInstance().handleGolden(this.mebro, var26);
                        this.initInit();
                        this.mebro = -1;
                        return true;
                     }
                  }
               }

               float var29 = var19 + 3.0F * (var16 + var17) + 2.0F;
               float var30 = var29 + 4.0F;

               for (int var31 = 0; var31 < 9; var31++) {
                  ItemStack var33 = MinecraftAccess.mc.player.getInventory().getStack(var31);
                  float var35 = var18 + (float)var31 * (var16 + var17);
                  if (Geometry.computeFlag(mouseX, mouseY, (double)var35, (double)var30, (double)var16, (double)var16)) {
                     Item var27 = var33.isEmpty() ? Items.AIR : var33.getItem();
                     AutoSwap.getInstance().handleGolden(this.mebro, var27);
                     this.initInit();
                     this.mebro = -1;
                     return true;
                  }
               }

               float var32 = var18 + 9.0F * (var16 + var17) + 4.0F;
               if (Geometry.computeFlag(mouseX, mouseY, (double)var32, (double)var30, (double)var16, (double)var16)) {
                  ItemStack var34 = MinecraftAccess.mc.player.getOffHandStack();
                  Item var36 = var34.isEmpty() ? Items.AIR : var34.getItem();
                  AutoSwap.getInstance().handleGolden(this.mebro, var36);
                  this.initInit();
                  this.mebro = -1;
                  return true;
               }
            }

            if (!Geometry.computeFlag(mouseX, mouseY, (double)var10, (double)var11, (double)var28, (double)var9)) {
               this.mebro = -1;
               return true;
            } else {
               return true;
            }
         }
      } else if (button == 1 && this.intVal >= 0 && this.intVal < this.list.size()) {
         this.mebro = this.intVal;
         this.mkedb.handleStart(0.0);
         return true;
      } else if (button == 0 && this.intVal >= 0 && this.intVal < this.list.size()) {
         SwapRadialMenuScreen.RadialSlot var8 = this.list.get(this.intVal);
         if (!var8.stack.isEmpty() && var8.stack.getItem() != Items.AIR) {
            this.handleClass_1792(var8.stack.getItem());
         } else {
            this.mebro = this.intVal;
            this.mkedb.handleStart(0.0);
         }

         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   public void runStack() {
      if (this.mebro == -1) {
         if (this.intVal >= 0 && this.intVal < this.list.size()) {
            SwapRadialMenuScreen.RadialSlot var1 = this.list.get(this.intVal);
            if (!var1.stack.isEmpty() && var1.stack.getItem() != Items.AIR) {
               this.handleClass_1792(var1.stack.getItem());
            } else {
               MinecraftAccess.mc.setScreen(null);
            }
         } else {
            MinecraftAccess.mc.setScreen(null);
         }
      }
   }

   public void handleClass_1792(Item item) {
      if (item != null && item != Items.AIR) {
         AutoSwap.getInstance().handleNotify(item);
         MinecraftAccess.mc.setScreen(null);
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      if (this.mebro != -1) {
         this.mebro = -1;
         return false;
      } else {
         return true;
      }
   }

   static class RadialSlot {
      public ItemStack stack;
      public String label;
      public AnimatedValue animatedValue = new AnimatedValue();

      public RadialSlot(ItemStack stack, String label) {
         this.stack = stackx;
         this.label = labelx;
      }
   }
}
