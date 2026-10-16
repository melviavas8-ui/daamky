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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CooldownsHud extends HudElement {
   public static final Object[][] objectList = new Object[][]{
      {Items.ENDER_PEARL, "Ender Pearl", "0:12"},
      {Items.CHORUS_FRUIT, "Chorus Fruit", "0:06"},
      {Items.SNOWBALL, "Snowball", "0:02"},
      {Items.GOLDEN_APPLE, "Golden Apple", "0:20"}
   };
   public final LinkedHashMap<String, CooldownsHud.Entry> linkedHashMap = new LinkedHashMap<>();
   public final Set<String> set = new HashSet<>();

   public CooldownsHud() {
      super(96.0F, 32.0F);
   }

   @Override
   public String getName() {
      return "Cooldowns";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack ms) {
      if (mc.player != null) {
         boolean var2 = mc.currentScreen != null;
         ItemCooldownManager var3 = mc.player.getItemCooldownManager();
         this.set.clear();

         for (CooldownsHud.Entry var5 : this.linkedHashMap.values()) {
            var5.flag = false;
         }

         for (int var39 = 0; var39 < mc.player.getInventory().size(); var39++) {
            ItemStack var7 = mc.player.getInventory().getStack(var39);
            String var6;
            if (!var7.isEmpty() && var3.isCoolingDown(var7) && !this.set.contains(var6 = var7.getItem().toString())) {
               this.set.add(var6);
               net.minecraft.entity.player.ItemCooldownManager.Entry var8 = (net.minecraft.entity.player.ItemCooldownManager.Entry)var3.entries.get(var3.getGroup(var7));
               int var42;
               if (var8 != null && (var42 = Math.max(0, var8.endTick() - (var3.tick + (int)mc.getRenderTickCounter().getTickDelta(false)))) > 0) {
                  CooldownsHud.Entry var9 = this.linkedHashMap.computeIfAbsent(var6, CooldownsHud::lambda$render$0);
                  var9.class_1799 = var7;
                  var9.string = var7.getItem().getName().getString();
                  var9.mueeikn = TextShaper.computeFormat(var42);
                  var9.flag = true;
               }
            }
         }

         for (CooldownsHud.Entry var43 : this.linkedHashMap.values()) {
            var43.mrovz.update();
            var43.mrovz.animateTo(var43.flag ? 1.0 : 0.0, 150L, this.getEasing());
         }

         this.linkedHashMap.values().removeIf(CooldownsHud::lambda$render$1);
         boolean var41 = !this.linkedHashMap.isEmpty();
         if (var41 || var2) {
            boolean var44 = !var41 && var2;
            DrawContext var45 = this.currentEvent.getContext();
            float var46 = this.getAutoHelper().getX();
            float var47 = this.getAutoHelper().getY();
            float var10 = this.computeFloat(5.0F);
            float var11 = this.computeFloat(6.5F);
            float var12 = this.computeFloat(3.5F);
            float var13 = this.computeFloat(5.0F);
            float var14 = this.computeFloat(14.0F);
            float var15 = var14 + this.computeFloat(2.0F);
            float var16 = this.computeFloat(2.5F);
            float var17 = this.computeFloat(2.5F);
            float var18 = this.computeFloat(11.0F);
            float var19 = 0.0F;
            float var20 = 0.0F;
            if (var44) {
               for (Object[] var24 : objectList) {
                  float var25 = var11
                     + var12
                     + this.getMkadw().computeFloat((String)var24[1], var13)
                     + var12
                     + this.getMkadw().computeFloat((String)var24[2], var13);
                  if (var25 > var19) {
                     var19 = var25;
                  }
               }

               var20 = var18;
            } else {
               for (CooldownsHud.Entry var50 : this.linkedHashMap.values()) {
                  float var52 = (float)var50.mrovz.getValue();
                  float var54 = var11 + var12 + this.getMkadw().computeFloat(var50.string, var13) + var12 + this.getMkadw().computeFloat(var50.mueeikn, var13);
                  if (var54 > var19) {
                     var19 = var54;
                  }

                  var20 += var18 * var52;
               }
            }

            float var49 = this.computeFloat(96.0F);
            float var51 = this.computeFloat(6.0F);
            String var53 = EngHelper.computeString("Cooldowns");
            float var55 = FontBootstrap.clientFont9.computeFloat(var53, var51);
            float var56 = this.computeFloat(6.5F);
            float var26 = Math.max(var49, var10 * 2.0F + var19);
            float var27 = var15 + var16 + var20 + var17;
            this.getAutoHelper().setWidth(var26);
            this.getAutoHelper().setHeight(var27);
            Color var28 = this.initInit();
            float var29 = this.getHeight();
            if (this.getAutoHelper().isBlur()) {
               Render.shaderProgram.handleVal(ms, var46, var47, var26, var27, var29, var28.getRGB());
            } else {
               Render.rectRenderer.handleVal(ms, var46, var47, var26, var27, var29, var28.getRGB());
            }

            this.runContent();
            if (Interface.getInstance().isEnabled() && Interface.check2()) {
               Interface.handleVal(ms, var46, var47, var26, var27, var29, 1.0F, Interface.check4());
            }

            boolean var30 = ThemeManager.getInstance().getIsDayMode();
            int var31 = var30 ? 2030043135 : 2013923850;
            Render.rectRenderer.handleVal(ms, var46, var47, var26, var15, var29, var31);
            float var32 = var47 + (var15 - var51) / 2.0F - this.computeFloat(0.8F);
            FontBootstrap.clientFont9.handleVal(ms, var53, var46 + var10, var32, var51, this.getColor().getRGB());
            float var33 = var47 + (var15 - var56) / 2.0F - this.computeFloat(0.5F);
            this.getValue2().handleVal(ms, "G", var46 + var26 - var10 - var56, var33, var56, this.getColor().getRGB());
            float var34 = var47 + var15 + var16;
            var45.enableScissor((int)var46, (int)var47, (int)(var46 + var26), (int)(var47 + var27));
            if (var44) {
               PhaseHelper.Phase var35 = PhaseHelper.initInit(objectList.length, 2200L, 550L);
               float var36 = this.computeFloat(2.0F);
               Object[] var37 = objectList[var35.index()];
               this.handleColor(
                  ms,
                  var45,
                  new ItemStack((Item)var37[0]),
                  (String)var37[1],
                  (String)var37[2],
                  1.0F - var35.k(),
                  var46,
                  var34 - var35.k() * var36,
                  var26,
                  var10,
                  var11,
                  var12,
                  var13,
                  var18
               );
               if (var35.k() > 0.001F) {
                  Object[] var38 = objectList[var35.next()];
                  this.handleColor(
                     ms,
                     var45,
                     new ItemStack((Item)var38[0]),
                     (String)var38[1],
                     (String)var38[2],
                     var35.k(),
                     var46,
                     var34 + (1.0F - var35.k()) * var36,
                     var26,
                     var10,
                     var11,
                     var12,
                     var13,
                     var18
                  );
               }
            } else {
               for (CooldownsHud.Entry var58 : this.linkedHashMap.values()) {
                  float var59 = (float)var58.mrovz.getValue();
                  if (!(var59 <= 0.01F)) {
                     this.handleColor(ms, var45, var58.class_1799, var58.string, var58.mueeikn, var59, var46, var34, var26, var10, var11, var12, var13, var18);
                     var34 += var18 * var59;
                  }
               }
            }

            var45.disableScissor();
         }
      }
   }

   public void handleColor(
      MatrixStack ms,
      DrawContext ctx,
      ItemStack stack,
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
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, a);
      ms.push();
      ms.translate(var17, cY + (rowH - iconSize) / 2.0F, 0.0F);
      ms.scale(iconSize / 16.0F, iconSize / 16.0F, 1.0F);
      ctx.drawItem(stack, 0, 0);
      ms.pop();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      float var18 = cY + (rowH - fontSize) / 2.0F - this.computeFloat(0.3F);
      this.getMkadw().handleVal(ms, name, var17 + iconSize + gap, var18, fontSize, var16);
      float var19 = this.getMkadw().computeFloat(time, fontSize);
      this.getMkadw().handleVal(ms, time, x + totalW - pad - var19, var18, fontSize, var16);
   }

   public static boolean lambda$render$1(CooldownsHud.Entry e) {
      return !e.flag && e.mrovz.getValue() <= 0.01;
   }

   public static CooldownsHud.Entry lambda$render$0(String k) {
      return new CooldownsHud.Entry();
   }

   private static final class Entry {
      public ItemStack class_1799;
      public String string;
      public String mueeikn;
      public final AnimatedValue mrovz = new AnimatedValue();
      public boolean flag;

      public Entry() {
      }
   }
}
