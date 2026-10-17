package dev.daamky.client.gui;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.AuctionListing;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.DisplayScale;
import dev.daamky.client.internal.Geometry;
import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.NextIntHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ScissorUtil;
import dev.daamky.client.internal.SellCmdHelper;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.pve.AutoBuy;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class AutoBuyScreen extends Screen {
   public static AutoBuyScreen mjuso;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public boolean flag = true;
   public int intVal = 0;
   public double doubleVal = 0.0;
   public double double2 = 0.0;
   public String string = "";
   public boolean flag2 = false;
   public String field = "";
   public String currentValue = "";
   public final Map<String, AnimatedValue> map = new HashMap<>();
   public final Map<Integer, Boolean> map2 = new HashMap<>();
   public final ArrayList<ItemActionCatalog> arrayList = new ArrayList<>();
   public String string2 = null;

   public ArrayList<ItemActionCatalog> getCase() {
      if (this.string.equals(this.string2)) {
         return this.arrayList;
      } else {
         this.arrayList.clear();
         String var1 = this.string.toLowerCase();

         for (ItemActionCatalog var5 : ItemActionCatalog.values()) {
            if (this.string.isEmpty() || var5.getName().toLowerCase().contains(var1) || var5.getSearchString().toLowerCase().contains(var1)) {
               this.arrayList.add(var5);
            }
         }

         this.string2 = this.string;
         return this.arrayList;
      }
   }

   public AutoBuyScreen() {
      super(Text.of(""));
   }

   public static AutoBuyScreen getInstance() {
      if (mjuso == null) {
         mjuso = new AutoBuyScreen();
      }

      return mjuso;
   }

   public void init() {
      super.init();
      this.flag = true;
      this.animatedValue.handleStart(0.0);
      this.animatedValue2.handleStart((double)this.intVal);
      this.flag2 = false;
   }

   public void close() {
      this.flag = false;
      this.flag2 = false;
   }

   public void run() {
      mjuso = null;
      super.close();
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && var1.mouse != null) {
         var1.mouse.lockCursor();
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.renderBackground(context, mouseX, mouseY, delta);
      MatrixStack var5 = context.getMatrices();
      AutoBuy var6 = AutoBuy.getInstance();
      this.animatedValue.update();
      this.animatedValue.animateTo(this.flag ? 1.0 : 0.0, 240L, Easing.QUINT_OUT, true);
      this.animatedValue2.update();
      this.animatedValue2.animateTo((double)this.intVal, 220L, Easing.QUINT_OUT, true);
      float var7 = (float)this.animatedValue2.getValue();
      float var8 = (float)this.animatedValue.getValue();
      if (!this.flag && (double)var8 < 0.04) {
         this.run();
      } else {
         float var9 = (float)this.width;
         float var10 = (float)this.height;
         Render.rectRenderer.handleRgb(var5, 0.0F, 0.0F, var9, var10, 0.0F, new Color(0, 0, 0, (int)(180.0F * var8)));
         float var11 = DisplayScale.getInstance().computeGui(235.0F);
         float var12 = DisplayScale.getInstance().computeGui(172.0F);
         float var13 = (var9 - var11) / 2.0F;
         float var14 = (var10 - var12) / 2.0F - (1.0F - var8) * 14.0F;
         float var15 = InterfaceMetrics.computeFloat(7.0F);
         int var16 = (int)(255.0F * var8);
         float var17 = var8;
         Color var18 = new Color(255, 255, 255, (int)(18.0F * var8));
         Color var19 = new Color(13, 14, 18, (int)(245.0F * var8));
         Render.rectRenderer.handleRgb(var5, var13 - 0.5F, var14 - 0.5F, var11 + 1.0F, var12 + 1.0F, var15 + 0.5F, var18);
         Render.rectRenderer.handleRgb(var5, var13, var14, var11, var12, var15, var19);
         float var20 = InterfaceMetrics.computeFloat(8.0F);
         float var21 = var14 + InterfaceMetrics.computeFloat(7.0F);
         FontBootstrap.clientFont2.handleColor(var5, "AUTOBUY", var13 + var20, var21, InterfaceMetrics.computeFloat(8.5F), ThemeColor.computePrimary(var16));
         String var22 = var6.getV4();
         float var23 = InterfaceMetrics.computeFloat(4.8F);
         float var24 = FontBootstrap.mheuy.computeFloat(var22, var23);
         float var25 = InterfaceMetrics.computeFloat(4.0F);
         float var26 = InterfaceMetrics.computeFloat(2.0F);
         float var27 = var24 + var25 * 2.0F + InterfaceMetrics.computeFloat(5.0F);
         float var28 = var23 + var26 * 2.0F;
         float var29 = var13 + var11 - var20 - var27;
         float var30 = var21 - InterfaceMetrics.computeFloat(0.5F);
         Color var31 = new Color(255, 255, 255, (int)(14.0F * var8));
         Color var32 = new Color(20, 22, 30, (int)(220.0F * var8));
         Render.rectRenderer.handleRgb(var5, var29 - 0.4F, var30 - 0.4F, var27 + 0.8F, var28 + 0.8F, InterfaceMetrics.computeFloat(3.0F), var31);
         Render.rectRenderer.handleRgb(var5, var29, var30, var27, var28, InterfaceMetrics.computeFloat(2.5F), var32);
         Render.rectRenderer
            .handleRgb(
               var5,
               var29 + InterfaceMetrics.computeFloat(3.0F),
               var30 + (var28 - InterfaceMetrics.computeFloat(2.6F)) / 2.0F,
               InterfaceMetrics.computeFloat(2.6F),
               InterfaceMetrics.computeFloat(2.6F),
               InterfaceMetrics.computeFloat(1.3F),
               ThemeColor.computePositive(var16)
            );
         FontBootstrap.mheuy
            .handleColor(
               var5,
               var22,
               var29 + InterfaceMetrics.computeFloat(7.2F),
               var30 + (var28 - var23) / 2.0F - InterfaceMetrics.computeFloat(0.4F),
               var23,
               ThemeColor.computeText(var16)
            );
         float var33 = var21 + InterfaceMetrics.computeFloat(11.0F);
         String var34 = String.format("Цен: %d  |  Куплено: %d", var6.getMap().size(), var6.getV3());
         FontBootstrap.mkadw
            .handleColor(var5, var34, var13 + var20, var33, InterfaceMetrics.computeFloat(4.8F), new Color(130, 142, 160, (int)(210.0F * var8)));
         float var35 = var33 + InterfaceMetrics.computeFloat(8.5F);
         float var36 = var11 - var20 * 2.0F;
         float var37 = InterfaceMetrics.computeFloat(13.0F);
         float var38 = var36 / 3.0F;
         String[] var39 = new String[]{"Предметы", "История", "Настройки"};
         Color var40 = new Color(19, 21, 28, (int)(230.0F * var8));
         Color var41 = new Color(255, 255, 255, (int)(12.0F * var8));
         Render.rectRenderer.handleRgb(var5, var13 + var20 - 0.4F, var35 - 0.4F, var36 + 0.8F, var37 + 0.8F, InterfaceMetrics.computeFloat(3.5F), var41);
         Render.rectRenderer.handleRgb(var5, var13 + var20, var35, var36, var37, InterfaceMetrics.computeFloat(3.0F), var40);
         float var42 = var13 + var20 + var7 * var38;
         Color var43 = ColorMath.initBlue(ThemeColor.getColor4(), (int)(220.0F * var8));
         Color var44 = ColorMath.initBlue(ThemeColor.getColor4(), (int)(40.0F * var8));
         Render.rectRenderer.handleRgb(var5, var42 - 0.8F, var35 - 0.8F, var38 + 1.6F, var37 + 1.6F, InterfaceMetrics.computeFloat(3.5F), var44);
         Render.rectRenderer.handleRgb(var5, var42, var35, var38, var37, InterfaceMetrics.computeFloat(3.0F), var43);

         for (int var45 = 0; var45 < 3; var45++) {
            float var46 = var13 + var20 + (float)var45 * var38;
            boolean var47 = var45 == this.intVal;
            Color var48 = var47 ? new Color(255, 255, 255, var16) : new Color(160, 172, 195, (int)(210.0F * var17));
            FontBootstrap.mheuy
               .handleColor2(
                  var5,
                  var39[var45],
                  var46 + var38 / 2.0F,
                  var35 + (var37 - InterfaceMetrics.computeFloat(4.8F)) / 2.0F - InterfaceMetrics.computeFloat(0.5F),
                  InterfaceMetrics.computeFloat(4.8F),
                  var48
               );
         }

         float var49 = var35 + var37 + InterfaceMetrics.computeFloat(4.0F);
         Render.rectRenderer.handleRgb(var5, var13 + var20, var49, var36, 0.8F, 0.4F, new Color(255, 255, 255, (int)(10.0F * var17)));
         float var50 = var49 + InterfaceMetrics.computeFloat(3.5F);
         float var51 = var12 - (var50 - var14) - InterfaceMetrics.computeFloat(10.0F);
         ScissorUtil.handleEnable(var5, var13 + var20, var50, var36, var51);
         if (this.intVal == 0) {
            this.renderItems(context, var5, var6, var13 + var20, var50, var36, var51, var16, mouseX, mouseY);
         } else if (this.intVal == 1) {
            this.initInit(var5, var6, var13 + var20, var50, var36, var51, var16);
         } else if (this.intVal == 2) {
            this.renderSettings(var5, var6, var13 + var20, var50, var36, var51, var16);
         }

         ScissorUtil.handleDisable(var5);
         float var52 = var14 + var12 - InterfaceMetrics.computeFloat(7.0F);
         FontBootstrap.mkadw
            .handleColor2(
               var5, "ESC — закрыть", var13 + var11 / 2.0F, var52, InterfaceMetrics.computeFloat(4.2F), new Color(120, 130, 150, (int)(140.0F * var17))
            );
      }
   }

   public void renderItems(DrawContext context, MatrixStack ms, AutoBuy f, float x, float y, float w, float h, int alpha, int mouseX, int mouseY) {
      float var11 = (float)alpha / 255.0F;
      float var12 = InterfaceMetrics.computeFloat(13.0F);
      Color var13 = new Color(17, 19, 25, (int)(230.0F * var11));
      Color var14 = new Color(255, 255, 255, (int)((this.flag2 ? 45.0F : 14.0F) * var11));
      Render.rectRenderer.handleRgb(ms, x - 0.4F, y - 0.4F, w + 0.8F, var12 + 0.8F, InterfaceMetrics.computeFloat(3.0F), var14);
      Render.rectRenderer.handleRgb(ms, x, y, w, var12, InterfaceMetrics.computeFloat(2.5F), var13);
      float var15 = InterfaceMetrics.computeFloat(4.8F);
      FontBootstrap.clientFont5
         .handleColor(
            ms,
            "\ue002",
            x + InterfaceMetrics.computeFloat(4.0F),
            y + (var12 - var15) / 2.0F - InterfaceMetrics.computeFloat(0.3F),
            var15,
            new Color(140, 150, 175, (int)(180.0F * var11))
         );
      float var16 = x + InterfaceMetrics.computeFloat(12.0F);
      float var17 = InterfaceMetrics.computeFloat(4.8F);
      float var18 = y + (var12 - var17) / 2.0F - InterfaceMetrics.computeFloat(0.5F);
      if (this.string.isEmpty() && !this.flag2) {
         FontBootstrap.mkadw.handleColor(ms, "Поиск предметов...", var16, var18, var17, new Color(110, 120, 140, (int)(160.0F * var11)));
      } else {
         String var19 = this.flag2 && System.currentTimeMillis() / 450L % 2L == 0L ? "▌" : "";
         FontBootstrap.mkadw.handleColor(ms, this.string + var19, var16, var18, var17, new Color(240, 245, 255, alpha));
      }

      float var67 = y + var12 + InterfaceMetrics.computeFloat(3.0F);
      float var20 = InterfaceMetrics.computeFloat(17.5F);
      float var21 = InterfaceMetrics.computeFloat(2.5F);
      ArrayList var22 = this.getCase();
      int var23 = var22.size();
      float var24 = Math.max(0.0F, (float)var23 * (var20 + var21) - var21 - (h - var12 - InterfaceMetrics.computeFloat(4.0F)));
      if (this.doubleVal > 0.0) {
         this.doubleVal = 0.0;
      }

      if (this.doubleVal < (double)(-var24)) {
         this.doubleVal = (double)(-var24);
      }

      float var25 = var67 + (float)this.doubleVal;
      int var26 = Math.max(0, (int)(-this.doubleVal / (double)(var20 + var21)));
      int var27 = Math.min(var23, (int)(h / (var20 + var21)) + 2);

      for (int var28 = var26; var28 < Math.min(var23, var26 + var27); var28++) {
         ItemActionCatalog var29 = (ItemActionCatalog)var22.get(var28);
         float var30 = var25 + (float)var28 * (var20 + var21);
         if (!(var30 > y + h) && !(var30 + var20 < y)) {
            boolean var31 = f.getSet().contains(var29);
            boolean var32 = Geometry.computeFlag((float)mouseX, (float)mouseY, x, var30, w, var20);
            Color var33 = var32 ? new Color(24, 27, 36, (int)(235.0F * var11)) : new Color(17, 19, 25, (int)(215.0F * var11));
            Color var34 = new Color(255, 255, 255, (int)((var32 ? 25.0F : 10.0F) * var11));
            Render.rectRenderer.handleRgb(ms, x - 0.4F, var30 - 0.4F, w + 0.8F, var20 + 0.8F, InterfaceMetrics.computeFloat(3.5F), var34);
            Render.rectRenderer.handleRgb(ms, x, var30, w, var20, InterfaceMetrics.computeFloat(3.0F), var33);
            context.drawItem(new ItemStack(var29.getItemType()), (int)(x + InterfaceMetrics.computeFloat(3.0F)), (int)(var30 + (var20 - 16.0F) / 2.0F));
            float var35 = x + InterfaceMetrics.computeFloat(22.0F);
            float var36 = InterfaceMetrics.computeFloat(5.6F);
            Color var37 = var31 ? new Color(130, 135, 150, (int)(160.0F * var11)) : new Color(240, 245, 255, alpha);
            FontBootstrap.mheuy.handleColor(ms, var29.getName(), var35, var30 + (var20 - var36) / 2.0F - InterfaceMetrics.computeFloat(0.5F), var36, var37);
            float var38 = InterfaceMetrics.computeFloat(14.0F);
            float var39 = InterfaceMetrics.computeFloat(7.5F);
            float var40 = x + w - var38 - InterfaceMetrics.computeFloat(4.5F);
            float var41 = var30 + (var20 - var39) / 2.0F;
            String var42 = "price_" + var29.name();
            boolean var43 = this.field.equals(var42);
            int var44 = f.getMap().getOrDefault(var29, -1);
            String var45 = var43
               ? this.currentValue + (System.currentTimeMillis() / 400L % 2L == 0L ? "▌" : "")
               : (var44 == -1 ? "Авто" : String.format("%,d $", var44).replace(',', ' '));
            float var46 = InterfaceMetrics.computeFloat(5.0F);
            float var47 = FontBootstrap.clientFont2.computeFloat(var45, var46);
            float var48 = InterfaceMetrics.computeFloat(4.0F);
            float var49 = InterfaceMetrics.computeFloat(2.0F);
            float var50 = var47 + var48 * 2.0F;
            float var51 = var46 + var49 * 2.0F;
            float var52 = var40 - var50 - InterfaceMetrics.computeFloat(6.0F);
            float var53 = var30 + (var20 - var51) / 2.0F;
            Color var54 = var43 ? ThemeColor.computePrimary(alpha) : new Color(255, 255, 255, (int)(15.0F * var11));
            Color var55 = var43 ? new Color(30, 32, 45, (int)(240.0F * var11)) : new Color(22, 24, 32, (int)(200.0F * var11));
            Render.rectRenderer.handleRgb(ms, var52 - 0.4F, var53 - 0.4F, var50 + 0.8F, var51 + 0.8F, InterfaceMetrics.computeFloat(2.5F), var54);
            Render.rectRenderer.handleRgb(ms, var52, var53, var50, var51, InterfaceMetrics.computeFloat(2.0F), var55);
            Color var56 = var43
               ? ThemeColor.computePrimary(alpha)
               : (var44 == -1 ? new Color(140, 150, 175, (int)(200.0F * var11)) : ThemeColor.computePositive(alpha));
            FontBootstrap.clientFont2.handleColor(ms, var45, var52 + var48, var53 + var49 - InterfaceMetrics.computeFloat(0.4F), var46, var56);
            AnimatedValue var57 = this.map.computeIfAbsent("item_" + var29.name(), AutoBuyScreen::lambda$renderItems$0);
            var57.update();
            var57.animateTo(!var31 ? 1.0 : 0.0, 180L, Easing.QUINT_OUT, true);
            float var58 = (float)var57.getValue();
            Color var59 = new Color(32, 35, 45, (int)(220.0F * var11));
            Color var60 = ThemeColor.computePrimary(alpha);
            Color var61 = ColorMath.initBlue(var59, var60, var58);
            Render.rectRenderer.handleRgb(ms, var40, var41, var38, var39, var39 / 2.0F, var61);
            float var62 = InterfaceMetrics.computeFloat(1.0F);
            float var63 = var39 - var62 * 2.0F;
            float var64 = var38 - var63 - var62 * 2.0F;
            float var65 = var40 + var62 + var64 * var58;
            float var66 = var41 + var62;
            Render.rectRenderer.handleRgb(ms, var65, var66, var63, var63, var63 / 2.0F, new Color(255, 255, 255, alpha));
         }
      }

      if (var22.isEmpty()) {
         FontBootstrap.mkadw
            .handleColor2(
               ms,
               "Ничего не найдено",
               x + w / 2.0F,
               var67 + InterfaceMetrics.computeFloat(12.0F),
               InterfaceMetrics.computeFloat(5.5F),
               new Color(130, 140, 160, (int)(160.0F * var11))
            );
      }
   }

   public void initInit(MatrixStack ms, AutoBuy f, float x, float y, float w, float h, int alpha) {
      float var8 = (float)alpha / 255.0F;
      if (f.getList3().isEmpty()) {
         FontBootstrap.mkadw
            .handleColor2(
               ms,
               "История покупок пуста",
               x + w / 2.0F,
               y + h / 2.0F - InterfaceMetrics.computeFloat(4.0F),
               InterfaceMetrics.computeFloat(6.0F),
               new Color(130, 140, 160, (int)(160.0F * var8))
            );
      } else {
         float var9 = InterfaceMetrics.computeFloat(26.0F);
         float var10 = InterfaceMetrics.computeFloat(3.5F);
         float var11 = Math.max(0.0F, (float)f.getList3().size() * (var9 + var10) - var10 - h);
         if (this.doubleVal > 0.0) {
            this.doubleVal = 0.0;
         }

         if (this.doubleVal < (double)(-var11)) {
            this.doubleVal = (double)(-var11);
         }

         int var12 = Math.max(0, (int)(-this.doubleVal / (double)(var9 + var10)));
         int var13 = Math.min(f.getList3().size(), (int)(h / (var9 + var10)) + 2);

         for (int var14 = var12; var14 < Math.min(f.getList3().size(), var12 + var13); var14++) {
            AuctionListing var15 = f.getList3().get(var14);
            float var16 = y + (float)this.doubleVal + (float)var14 * (var9 + var10);
            if (!(var16 > y + h) && !(var16 + var9 < y)) {
               Color var17 = new Color(17, 19, 25, (int)(220.0F * var8));
               Color var18 = new Color(255, 255, 255, (int)(10.0F * var8));
               Render.rectRenderer.handleRgb(ms, x - 0.4F, var16 - 0.4F, w + 0.8F, var9 + 0.8F, InterfaceMetrics.computeFloat(3.5F), var18);
               Render.rectRenderer.handleRgb(ms, x, var16, w, var9, InterfaceMetrics.computeFloat(3.0F), var17);
               FontBootstrap.mheuy
                  .handleColor(
                     ms,
                     var15.getItemType().getName(),
                     x + InterfaceMetrics.computeFloat(5.0F),
                     var16 + InterfaceMetrics.computeFloat(3.0F),
                     InterfaceMetrics.computeFloat(5.6F),
                     ThemeColor.computePositive(alpha)
                  );
               String var19 = String.format(
                  "Куплено: %d  |  Рынок: %d  |  Окуп: x%.1f",
                  var15.getAllPrice(),
                  var15.getAuctionPrice(),
                  (double)var15.getAuctionPrice() / (double)Math.max(1, var15.getPrice())
               );
               FontBootstrap.mkadw
                  .handleColor(
                     ms,
                     var19,
                     x + InterfaceMetrics.computeFloat(5.0F),
                     var16 + InterfaceMetrics.computeFloat(11.5F),
                     InterfaceMetrics.computeFloat(5.0F),
                     new Color(180, 190, 210, alpha)
                  );
               String var20 = "Цена: " + var15.getPrice() + "  |  Баланс: " + var15.getBalance();
               FontBootstrap.mkadw
                  .handleColor(
                     ms,
                     var20,
                     x + InterfaceMetrics.computeFloat(5.0F),
                     var16 + InterfaceMetrics.computeFloat(18.5F),
                     InterfaceMetrics.computeFloat(4.6F),
                     new Color(120, 130, 150, (int)(160.0F * var8))
                  );
            }
         }
      }
   }

   public void renderSettings(MatrixStack ms, AutoBuy f, float x, float y, float w, float h, int alpha) {
      float var8 = (float)alpha / 255.0F;
      List var9 = this.initInit(f);
      float var10 = InterfaceMetrics.computeFloat(16.5F);
      float var11 = InterfaceMetrics.computeFloat(3.0F);
      float var12 = Math.max(0.0F, (float)var9.size() * (var10 + var11) - var11 - h);
      if (this.double2 > 0.0) {
         this.double2 = 0.0;
      }

      if (this.double2 < (double)(-var12)) {
         this.double2 = (double)(-var12);
      }

      int var13 = Math.max(0, (int)(-this.double2 / (double)(var10 + var11)));
      int var14 = Math.min(var9.size(), (int)(h / (var10 + var11)) + 2);

      for (int var15 = var13; var15 < Math.min(var9.size(), var13 + var14); var15++) {
         AutoBuyScreen.SettingEntry var16 = (AutoBuyScreen.SettingEntry)var9.get(var15);
         float var17 = y + (float)this.double2 + (float)var15 * (var10 + var11);
         if (!(var17 > y + h) && !(var17 + var10 < y)) {
            switch (var16.type()) {
               case 0:
                  FontBootstrap.clientFont2
                     .handleColor(
                        ms,
                        var16.label(),
                        x + InterfaceMetrics.computeFloat(3.0F),
                        var17 + (var10 - InterfaceMetrics.computeFloat(5.5F)) / 2.0F,
                        InterfaceMetrics.computeFloat(5.5F),
                        ThemeColor.computePrimary(alpha)
                     );
                  break;
               case 1:
                  boolean var36 = var15 != 2 || !f.getMap().isEmpty();
                  Color var38 = new Color(255, 255, 255, (int)(12.0F * var8));
                  Color var40 = var36 ? ColorMath.initBlue(ThemeColor.getColor4(), (int)(180.0F * var8)) : new Color(40, 20, 25, (int)(200.0F * var8));
                  Render.rectRenderer.handleRgb(ms, x - 0.4F, var17 - 0.4F, w + 0.8F, var10 + 0.8F, InterfaceMetrics.computeFloat(3.5F), var38);
                  Render.rectRenderer.handleRgb(ms, x, var17, w, var10, InterfaceMetrics.computeFloat(3.0F), var40);
                  FontBootstrap.clientFont2
                     .handleColor2(
                        ms,
                        var16.label(),
                        x + w / 2.0F,
                        var17 + (var10 - InterfaceMetrics.computeFloat(5.5F)) / 2.0F - InterfaceMetrics.computeFloat(0.5F),
                        InterfaceMetrics.computeFloat(5.5F),
                        new Color(255, 255, 255, alpha)
                     );
                  break;
               case 2:
                  boolean var35 = this.map2.getOrDefault(var15, false);
                  Color var37 = new Color(17, 19, 25, (int)(220.0F * var8));
                  Color var39 = new Color(255, 255, 255, (int)(10.0F * var8));
                  Render.rectRenderer.handleRgb(ms, x - 0.4F, var17 - 0.4F, w + 0.8F, var10 + 0.8F, InterfaceMetrics.computeFloat(3.5F), var39);
                  Render.rectRenderer.handleRgb(ms, x, var17, w, var10, InterfaceMetrics.computeFloat(3.0F), var37);
                  FontBootstrap.mkadw
                     .handleColor(
                        ms,
                        var16.label(),
                        x + InterfaceMetrics.computeFloat(4.5F),
                        var17 + (var10 - InterfaceMetrics.computeFloat(5.4F)) / 2.0F - InterfaceMetrics.computeFloat(0.4F),
                        InterfaceMetrics.computeFloat(5.4F),
                        new Color(230, 235, 248, alpha)
                     );
                  float var41 = InterfaceMetrics.computeFloat(13.0F);
                  float var22 = InterfaceMetrics.computeFloat(7.0F);
                  float var23 = x + w - var41 - InterfaceMetrics.computeFloat(4.0F);
                  float var24 = var17 + (var10 - var22) / 2.0F;
                  AnimatedValue var25 = this.map.computeIfAbsent("set_" + var15, AutoBuyScreen::lambda$renderSettings$1);
                  var25.update();
                  var25.animateTo(var35 ? 1.0 : 0.0, 180L, Easing.QUINT_OUT, true);
                  float var26 = (float)var25.getValue();
                  Color var27 = new Color(32, 35, 45, (int)(220.0F * var8));
                  Color var28 = ThemeColor.computePrimary(alpha);
                  Color var29 = ColorMath.initBlue(var27, var28, var26);
                  Render.rectRenderer.handleRgb(ms, var23, var24, var41, var22, var22 / 2.0F, var29);
                  float var30 = InterfaceMetrics.computeFloat(0.8F);
                  float var31 = var22 - var30 * 2.0F;
                  float var32 = var41 - var31 - var30 * 2.0F;
                  float var33 = var23 + var30 + var32 * var26;
                  float var34 = var24 + var30;
                  Render.rectRenderer.handleRgb(ms, var33, var34, var31, var31, var31 / 2.0F, new Color(255, 255, 255, alpha));
                  break;
               case 3:
                  Color var18 = new Color(17, 19, 25, (int)(220.0F * var8));
                  Color var19 = new Color(255, 255, 255, (int)(10.0F * var8));
                  Render.rectRenderer.handleRgb(ms, x - 0.4F, var17 - 0.4F, w + 0.8F, var10 + 0.8F, InterfaceMetrics.computeFloat(3.5F), var19);
                  Render.rectRenderer.handleRgb(ms, x, var17, w, var10, InterfaceMetrics.computeFloat(3.0F), var18);
                  FontBootstrap.mkadw
                     .handleColor(
                        ms,
                        var16.label(),
                        x + InterfaceMetrics.computeFloat(4.5F),
                        var17 + (var10 - InterfaceMetrics.computeFloat(5.4F)) / 2.0F - InterfaceMetrics.computeFloat(0.4F),
                        InterfaceMetrics.computeFloat(5.4F),
                        new Color(230, 235, 248, alpha)
                     );
                  float var20 = InterfaceMetrics.computeFloat(5.4F);
                  float var21 = FontBootstrap.mheuy.computeFloat(var16.value(), var20);
                  FontBootstrap.mheuy
                     .handleColor(
                        ms,
                        var16.value(),
                        x + w - var21 - InterfaceMetrics.computeFloat(5.0F),
                        var17 + (var10 - var20) / 2.0F - InterfaceMetrics.computeFloat(0.4F),
                        var20,
                        ThemeColor.computePrimary(alpha)
                     );
            }
         }
      }
   }

   public List<AutoBuyScreen.SettingEntry> initInit(AutoBuy f) {
      ArrayList var2 = new ArrayList();
      boolean var3 = !f.getMap().isEmpty();
      this.map2.clear();
      var2.add(new AutoBuyScreen.SettingEntry("ОСНОВНЫЕ", "", 0));
      var2.add(new AutoBuyScreen.SettingEntry(var3 ? "▶ ЗАПУСТИТЬ АВТОБАЙ" : "⛔ НЕТ ЦЕН", "", 1));
      var2.add(new AutoBuyScreen.SettingEntry("ПОЛУЧИТЬ ЦЕНЫ", "", 1));
      var2.add(new AutoBuyScreen.SettingEntry(var3 ? "ПРОДАТЬ ПРЕДМЕТЫ" : "⛔ НЕТ ЦЕН", "", 1));
      var2.add(new AutoBuyScreen.SettingEntry("Множитель цены", String.format("%.2f", f.getV6()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("ID владельца", f.getV2() == -1L ? "не задан" : String.valueOf(f.getV2()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("АВТОПЕРЕВОД", "", 0));
      int var4 = var2.size();
      var2.add(new AutoBuyScreen.SettingEntry("Включить", "", 2));
      this.map2.put(var4, f.isV());
      var2.add(new AutoBuyScreen.SettingEntry("Сумма перевода", String.valueOf(f.getInt3()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Оставить на балансе", String.valueOf(f.getInt2()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Ник получателя", f.getString(), 3));
      var2.add(new AutoBuyScreen.SettingEntry("ПЕРЕЗАХОД", "", 0));
      int var5 = var2.size();
      var2.add(new AutoBuyScreen.SettingEntry("Автоперезаход", "", 2));
      this.map2.put(var5, f.check2());
      var2.add(new AutoBuyScreen.SettingEntry("Задержка (сек)", String.valueOf(f.getInt5()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Номер анархии", String.valueOf(f.getInt6()), 3));
      int var6 = var2.size();
      var2.add(new AutoBuyScreen.SettingEntry("Рандомная анархия", "", 2));
      this.map2.put(var6, f.check3());
      var2.add(new AutoBuyScreen.SettingEntry("АВТОПАРСИНГ", "", 0));
      int var7 = var2.size();
      var2.add(new AutoBuyScreen.SettingEntry("Автопарсинг", "", 2));
      this.map2.put(var7, f.check4());
      var2.add(new AutoBuyScreen.SettingEntry("Задержка (мин)", String.valueOf(f.getInt7()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Сумма автопродажи", String.valueOf(f.getInt4()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("АНТИ-ДЕТЕКТ", "", 0));
      var2.add(new AutoBuyScreen.SettingEntry("Мин клик (тики)", String.valueOf(NextIntHelper.getValue2()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Макс клик (тики)", String.valueOf(NextIntHelper.getValue3()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Мин пауза стр.", String.valueOf(NextIntHelper.getValue4()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Макс пауза стр.", String.valueOf(NextIntHelper.getInt4()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Мин пауза покупка", String.valueOf(NextIntHelper.getInt5()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Макс пауза покупка", String.valueOf(NextIntHelper.getInt6()), 3));
      int var8 = var2.size();
      var2.add(new AutoBuyScreen.SettingEntry("Имитация скролла", "", 2));
      this.map2.put(var8, NextIntHelper.getMzufka());
      var2.add(new AutoBuyScreen.SettingEntry("Шанс ошибки", String.format("%.2f", NextIntHelper.getMpoqde()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Шанс паузы", String.format("%.2f", NextIntHelper.getDouble()), 3));
      var2.add(new AutoBuyScreen.SettingEntry("Пауза (тики)", String.valueOf(NextIntHelper.getInt7()), 3));
      return var2;
   }

   public boolean mouseClicked(double mx, double my, int btn) {
      float var6 = DisplayScale.getInstance().computeGui(235.0F);
      float var7 = DisplayScale.getInstance().computeGui(172.0F);
      float var8 = ((float)this.width - var6) / 2.0F;
      float var9 = ((float)this.height - var7) / 2.0F;
      float var10 = InterfaceMetrics.computeFloat(8.0F);
      float var11 = var9 + InterfaceMetrics.computeFloat(7.0F);
      float var12 = var11 + InterfaceMetrics.computeFloat(11.0F);
      float var13 = var12 + InterfaceMetrics.computeFloat(8.5F);
      float var14 = var6 - var10 * 2.0F;
      float var15 = InterfaceMetrics.computeFloat(13.0F);
      float var16 = var14 / 3.0F;

      for (int var17 = 0; var17 < 3; var17++) {
         float var18 = var8 + var10 + (float)var17 * var16;
         if (this.computeFlag(mx, my, (double)var18, (double)var13, (double)var16, var15)) {
            this.intVal = var17;
            this.runCurrent();
            return true;
         }
      }

      float var20 = var13 + var15 + InterfaceMetrics.computeFloat(4.0F);
      float var21 = var20 + InterfaceMetrics.computeFloat(3.5F);
      float var19 = var7 - (var21 - var9) - InterfaceMetrics.computeFloat(10.0F);
      if (this.intVal == 0) {
         this.handleAdd(mx, my, var8 + var10, var21, var14, var19);
      } else if (this.intVal == 2) {
         this.handleDouble(mx, my, var8 + var10, var21, var14, var19);
      }

      return true;
   }

   public void handleAdd(double mx, double my, float x, float y, float w, float h) {
      AutoBuy var9 = AutoBuy.getInstance();
      float var10 = InterfaceMetrics.computeFloat(13.0F);
      if (this.computeFlag(mx, my, (double)x, (double)y, (double)w, var10)) {
         this.flag2 = true;
         this.field = "";
      } else {
         this.flag2 = false;
         float var11 = y + var10 + InterfaceMetrics.computeFloat(3.0F);
         float var12 = InterfaceMetrics.computeFloat(17.5F);
         float var13 = InterfaceMetrics.computeFloat(2.5F);
         ArrayList var14 = this.getCase();
         int var15 = var14.size();
         float var16 = var11 + (float)this.doubleVal;
         int var17 = Math.max(0, (int)(-this.doubleVal / (double)(var12 + var13)));
         int var18 = Math.min(var15, (int)(h / (var12 + var13)) + 2);

         for (int var19 = var17; var19 < Math.min(var15, var17 + var18); var19++) {
            ItemActionCatalog var20 = (ItemActionCatalog)var14.get(var19);
            float var21 = var16 + (float)var19 * (var12 + var13);
            if (!(var21 > y + h) && !(var21 + var12 < y) && this.computeFlag(mx, my, (double)x, (double)var21, (double)w, var12)) {
               float var22 = InterfaceMetrics.computeFloat(14.0F);
               float var23 = InterfaceMetrics.computeFloat(7.5F);
               float var24 = x + w - var22 - InterfaceMetrics.computeFloat(4.5F);
               float var25 = var21 + (var12 - var23) / 2.0F;
               if (this.computeFlag(
                  mx,
                  my,
                  (double)(var24 - InterfaceMetrics.computeFloat(2.0F)),
                  (double)(var25 - InterfaceMetrics.computeFloat(2.0F)),
                  (double)(var22 + InterfaceMetrics.computeFloat(4.0F)),
                  var23 + InterfaceMetrics.computeFloat(4.0F)
               )) {
                  if (var9.getSet().contains(var20)) {
                     var9.getSet().remove(var20);
                  } else {
                     var9.getSet().add(var20);
                  }

                  return;
               }

               int var26 = var9.getMap().getOrDefault(var20, -1);
               String var27 = var26 == -1 ? "Авто" : String.format("%,d $", var26).replace(',', ' ');
               float var28 = InterfaceMetrics.computeFloat(5.0F);
               float var29 = FontBootstrap.clientFont2.computeFloat(var27, var28);
               float var30 = InterfaceMetrics.computeFloat(4.0F);
               float var31 = InterfaceMetrics.computeFloat(2.0F);
               float var32 = var29 + var30 * 2.0F;
               float var33 = var28 + var31 * 2.0F;
               float var34 = var24 - var32 - InterfaceMetrics.computeFloat(6.0F);
               float var35 = var21 + (var12 - var33) / 2.0F;
               if (this.computeFlag(mx, my, (double)var34, (double)var35, (double)var32, var33)) {
                  this.handleCurrent("price_" + var20.name(), var26 == -1 ? "" : String.valueOf(var26));
                  return;
               }

               if (var9.getSet().contains(var20)) {
                  var9.getSet().remove(var20);
               } else {
                  var9.getSet().add(var20);
               }

               return;
            }
         }
      }
   }

   public void handleDouble(double mx, double my, float x, float y, float w, float h) {
      AutoBuy var9 = AutoBuy.getInstance();
      List var10 = this.initInit(var9);
      float var11 = InterfaceMetrics.computeFloat(16.5F);
      float var12 = InterfaceMetrics.computeFloat(3.0F);
      int var13 = Math.max(0, (int)(-this.double2 / (double)(var11 + var12)));
      int var14 = Math.min(var10.size(), (int)(h / (var11 + var12)) + 2);

      for (int var15 = var13; var15 < Math.min(var10.size(), var13 + var14); var15++) {
         AutoBuyScreen.SettingEntry var16 = (AutoBuyScreen.SettingEntry)var10.get(var15);
         float var17 = y + (float)this.double2 + (float)var15 * (var11 + var12);
         if (this.computeFlag(mx, my, (double)x, (double)var17, (double)w, var11)) {
            switch (var15) {
               case 1:
                  if (!var9.getMap().isEmpty()) {
                     this.close();
                     var9.runEnabled2();
                  }
                  break;
               case 2:
                  this.close();
                  var9.runEnabled3();
                  break;
               case 3:
                  if (!var9.getMap().isEmpty()) {
                     this.close();
                     SellCmdHelper.runEnabled();
                  }
                  break;
               case 4:
                  this.handleCurrent("multiplier", String.format("%.2f", var9.getV6()));
                  break;
               case 5:
                  this.handleCurrent("ownerId", var9.getV2() == -1L ? "" : String.valueOf(var9.getV2()));
               case 6:
               case 11:
               case 16:
               case 20:
               default:
                  break;
               case 7:
                  var9.setV2(!var9.isV());
                  break;
               case 8:
                  this.handleCurrent("payWhen", String.valueOf(var9.getInt3()));
                  break;
               case 9:
                  this.handleCurrent("leftSum", String.valueOf(var9.getInt2()));
                  break;
               case 10:
                  this.handleCurrent("targetName", var9.getString());
                  break;
               case 12:
                  var9.setFlag(!var9.check2());
                  break;
               case 13:
                  this.handleCurrent("joinDelay", String.valueOf(var9.getInt5()));
                  break;
               case 14:
                  this.handleCurrent("anarchyNumber", String.valueOf(var9.getInt6()));
                  break;
               case 15:
                  var9.setFlag2(!var9.check3());
                  break;
               case 17:
                  var9.setFlag3(!var9.check4());
                  break;
               case 18:
                  this.handleCurrent("priceParseDelay", String.valueOf(var9.getInt7()));
                  break;
               case 19:
                  this.handleCurrent("autoSellMoney", String.valueOf(var9.getInt4()));
                  break;
               case 21:
                  this.handleCurrent("minClick", String.valueOf(NextIntHelper.getValue2()));
                  break;
               case 22:
                  this.handleCurrent("maxClick", String.valueOf(NextIntHelper.getValue3()));
                  break;
               case 23:
                  this.handleCurrent("minPage", String.valueOf(NextIntHelper.getValue4()));
                  break;
               case 24:
                  this.handleCurrent("maxPage", String.valueOf(NextIntHelper.getInt4()));
                  break;
               case 25:
                  this.handleCurrent("minBuy", String.valueOf(NextIntHelper.getInt5()));
                  break;
               case 26:
                  this.handleCurrent("maxBuy", String.valueOf(NextIntHelper.getInt6()));
                  break;
               case 27:
                  NextIntHelper.setV(!NextIntHelper.getMzufka());
                  break;
               case 28:
                  this.handleCurrent("staffMistake", String.format("%.2f", NextIntHelper.getMpoqde()));
                  break;
               case 29:
                  this.handleCurrent("extraWaitChance", String.format("%.2f", NextIntHelper.getDouble()));
                  break;
               case 30:
                  this.handleCurrent("extraWaitTicks", String.valueOf(NextIntHelper.getInt7()));
            }

            return;
         }
      }
   }

   public void handleCurrent(String field, String currentValue) {
      this.field = field;
      this.currentValue = currentValue;
   }

   public void handleString(String field) {
      AutoBuy var2 = AutoBuy.getInstance();

      try {
         if (field.startsWith("price_")) {
            String var3 = field.substring("price_".length());
            ItemActionCatalog var7 = ItemActionCatalog.valueOf(var3);
            if (!this.currentValue.isEmpty() && !this.currentValue.equals("-1")) {
               var2.getMap().put(var7, Math.max(1, Integer.parseInt(this.currentValue)));
            } else {
               var2.getMap().remove(var7);
            }

            this.field = "";
            this.currentValue = "";
            return;
         }

         switch (field) {
            case "multiplier":
               var2.setV(Math.max(0.0, Math.min(1.0, Double.parseDouble(this.currentValue))));
               break;
            case "ownerId":
               var2.setV(this.currentValue.isEmpty() ? -1L : Long.parseLong(this.currentValue));
               break;
            case "payWhen":
               var2.setInt3(Math.max(0, Integer.parseInt(this.currentValue)));
               break;
            case "leftSum":
               var2.setInt2(Math.max(0, Integer.parseInt(this.currentValue)));
               break;
            case "targetName":
               var2.setString(this.currentValue);
               break;
            case "joinDelay":
               var2.setInt5(Math.max(1, Integer.parseInt(this.currentValue)));
               break;
            case "anarchyNumber":
               var2.setInt6(Math.max(1, Math.min(63, Integer.parseInt(this.currentValue))));
               break;
            case "priceParseDelay":
               var2.setInt7(Math.max(1, Integer.parseInt(this.currentValue)));
               break;
            case "autoSellMoney":
               var2.setInt4(Math.max(0, Integer.parseInt(this.currentValue)));
               break;
            case "minClick":
               int var12 = Math.max(0, Integer.parseInt(this.currentValue));
               NextIntHelper.handleInt(var12, Math.max(var12, NextIntHelper.getValue3()));
               break;
            case "maxClick":
               int var11 = Math.max(1, Integer.parseInt(this.currentValue));
               NextIntHelper.handleInt(Math.min(var11, NextIntHelper.getValue2()), var11);
               break;
            case "minPage":
               int var10 = Math.max(0, Integer.parseInt(this.currentValue));
               NextIntHelper.handleInt2(var10, Math.max(var10, NextIntHelper.getInt4()));
               break;
            case "maxPage":
               int var9 = Math.max(1, Integer.parseInt(this.currentValue));
               NextIntHelper.handleInt2(Math.min(var9, NextIntHelper.getValue4()), var9);
               break;
            case "minBuy":
               int var8 = Math.max(0, Integer.parseInt(this.currentValue));
               NextIntHelper.handleInt3(var8, Math.max(var8, NextIntHelper.getInt6()));
               break;
            case "maxBuy":
               int var5 = Math.max(1, Integer.parseInt(this.currentValue));
               NextIntHelper.handleInt3(Math.min(var5, NextIntHelper.getInt5()), var5);
               break;
            case "staffMistake":
               NextIntHelper.setV(Math.max(0.0, Math.min(1.0, Double.parseDouble(this.currentValue))));
               break;
            case "extraWaitChance":
               NextIntHelper.handleDouble(Math.max(0.0, Math.min(1.0, Double.parseDouble(this.currentValue))));
               break;
            case "extraWaitTicks":
               NextIntHelper.setV(Math.max(0, Integer.parseInt(this.currentValue)));
         }
      } catch (Exception var6) {
      }

      this.field = "";
      this.currentValue = "";
   }

   public boolean mouseScrolled(double mx, double my, double hAmt, double vAmt) {
      if (this.intVal == 2) {
         this.double2 += vAmt * 16.5;
      } else if (this.intVal == 1) {
         this.doubleVal += vAmt * 25.0;
      } else {
         this.doubleVal += vAmt * 20.0;
      }

      return true;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         if (!this.field.isEmpty()) {
            this.field = "";
            this.currentValue = "";
            return true;
         } else if (this.flag2) {
            this.flag2 = false;
            return true;
         } else {
            this.close();
            return true;
         }
      } else if (!this.field.isEmpty()) {
         if (keyCode == 257) {
            this.handleString(this.field);
            return true;
         } else if (keyCode == 259 && !this.currentValue.isEmpty()) {
            this.currentValue = this.currentValue.substring(0, this.currentValue.length() - 1);
            return true;
         } else {
            return true;
         }
      } else {
         if (this.flag2) {
            if (keyCode == 257) {
               this.flag2 = false;
               return true;
            }

            if (keyCode == 259 && !this.string.isEmpty()) {
               this.string = this.string.substring(0, this.string.length() - 1);
               return true;
            }
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean charTyped(char chr, int mod) {
      if (!this.field.isEmpty()) {
         if (chr >= ' ' && chr != 167) {
            this.currentValue = this.currentValue + chr;
         }

         return true;
      } else if (this.flag2) {
         if (chr >= ' ' && chr != 167) {
            this.string = this.string + chr;
         }

         return true;
      } else {
         return super.charTyped(chr, mod);
      }
   }

   public void runCurrent() {
      this.field = "";
      this.currentValue = "";
      this.flag2 = false;
   }

   public boolean computeFlag(double mx, double my, double x, double y, double w, float h) {
      return mx >= x && mx <= x + w && my >= y && my <= y + (double)h;
   }

   public boolean computeFlag(double mx, double my, double x, double y, double w, double h) {
      return mx >= x && mx <= x + w && my >= y && my <= y + h;
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public static AnimatedValue lambda$renderSettings$1(String k) {
      return new AnimatedValue();
   }

   public static AnimatedValue lambda$renderItems$0(String k) {
      return new AnimatedValue();
   }

   private static record SettingEntry(String label, String value, int type) {
      public static final int HEADER = 0;
      public static final int ACTION = 1;
      public static final int TOGGLE = 2;
      public static final int VALUE = 3;
   }
}
