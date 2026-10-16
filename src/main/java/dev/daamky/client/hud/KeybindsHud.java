package dev.daamky.client.hud;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.ModuleRegistry;
import dev.daamky.client.internal.PhaseHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import dev.daamky.client.util.KeyNames;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.util.math.MatrixStack;

public class KeybindsHud extends HudElement {
   public static final Color color = new Color(50, 50, 50);
   public static final String[][] stringList = new String[][]{{"AttackAura", "R"}, {"Fly", "V"}, {"Speed", "G"}, {"Scaffold", "H"}};
   public final Map<Module, AnimatedValue> map = new HashMap<>();

   public KeybindsHud() {
      super(96.0F, 32.0F);
   }

   @Override
   public String getName() {
      return "Keybinds";
   }

   @Override
   public void render(MatrixStack matrixStack) {
      if (mc.world != null) {
         List var2 = ModuleRegistry.getInstance().getList();
         float var3 = this.getAutoHelper().getX();
         float var4 = this.getAutoHelper().getY();
         float var5 = this.computeFloat(5.0F);
         float var6 = this.computeFloat(5.0F);
         float var7 = this.computeFloat(14.0F);
         float var8 = var7 + this.computeFloat(2.0F);
         float var9 = this.computeFloat(2.5F);
         float var10 = this.computeFloat(2.5F);
         float var11 = this.computeFloat(11.0F);
         float var12 = 0.0F;
         float var13 = 0.0F;
         float var14 = 0.0F;
         boolean var15 = false;

         for (Module var17 : var2) {
            boolean var18 = var17.isEnabled() && var17.hasBind() && var17.getBind() != 0 && var17.getBind() != -1;
            AnimatedValue var19 = this.map.computeIfAbsent(var17, KeybindsHud::lambda$render$0);
            var19.update();
            var19.animateTo(var18 ? 1.0 : 0.0, 150L, this.getEasing());
            float var20 = (float)var19.getValue();
            if (var20 > 0.01F) {
               var15 = true;
               var12 = Math.max(var12, this.getMkadw().computeFloat(var17.getName(), var6));
               var13 = Math.max(var13, this.getMkadw().computeFloat(KeyNames.computeNone(var17.getBind()).toUpperCase(), var6));
               var14 += var11 * var20;
            }
         }

         boolean var38 = !var15 && mc.currentScreen != null;
         if (var15 || mc.currentScreen != null) {
            if (var38) {
               for (String[] var45 : stringList) {
                  var12 = Math.max(var12, this.getMkadw().computeFloat(var45[0], var6));
                  var13 = Math.max(var13, this.getMkadw().computeFloat(var45[1], var6));
               }

               var14 = var11;
            }

            float var40 = this.computeFloat(96.0F);
            float var42 = this.computeFloat(6.0F);
            String var44 = EngHelper.computeString("Keybinds");
            float var46 = FontBootstrap.clientFont9.computeFloat(var44, var42);
            float var21 = this.computeFloat(6.5F);
            float var22 = this.computeFloat(3.5F);
            float var23 = var12 + var22 + var13;
            float var24 = Math.max(var40, var5 * 2.0F + var23);
            float var25 = var8 + var9 + var14 + var10;
            this.getAutoHelper().setWidth(var24);
            this.getAutoHelper().setHeight(var25);
            Color var26 = this.initInit();
            float var27 = this.getHeight();
            if (this.getAutoHelper().isBlur()) {
               Render.shaderProgram.handleVal(matrixStack, var3, var4, var24, var25, var27, var26.getRGB());
            } else {
               Render.rectRenderer.handleVal(matrixStack, var3, var4, var24, var25, var27, var26.getRGB());
            }

            this.runContent();
            if (Interface.getInstance().isEnabled() && Interface.check2()) {
               Interface.handleVal(matrixStack, var3, var4, var24, var25, var27, 1.0F, Interface.check4());
            }

            boolean var28 = ThemeManager.getInstance().getIsDayMode();
            int var29 = var28 ? 2030043135 : 2013923850;
            Render.rectRenderer.handleVal(matrixStack, var3, var4, var24, var8, var27, var29);
            float var30 = var4 + (var8 - var42) / 2.0F - this.computeFloat(0.8F);
            FontBootstrap.clientFont9.handleVal(matrixStack, var44, var3 + var5, var30, var42, this.getColor().getRGB());
            float var31 = var4 + (var8 - var21) / 2.0F - this.computeFloat(0.5F);
            this.getValue2().handleVal(matrixStack, "Z", var3 + var24 - var5 - var21, var31, var21, this.getColor().getRGB());
            float var32 = var4 + var8 + var9;
            this.currentEvent.getContext().enableScissor((int)var3, (int)var4, (int)(var3 + var24), (int)(var4 + var25));
            if (var38) {
               PhaseHelper.Phase var33 = PhaseHelper.initInit(stringList.length, 2200L, 550L);
               float var34 = this.computeFloat(2.0F);
               String[] var35 = stringList[var33.index()];
               this.handleMax(matrixStack, var35[0], var35[1], 1.0F - var33.k(), var3, var32 - var33.k() * var34, var24, var5, var6, var11);
               if (var33.k() > 0.001F) {
                  String[] var36 = stringList[var33.next()];
                  this.handleMax(matrixStack, var36[0], var36[1], var33.k(), var3, var32 + (1.0F - var33.k()) * var34, var24, var5, var6, var11);
               }
            } else {
               for (Module var48 : var2) {
                  AnimatedValue var49 = this.map.get(var48);
                  if (var49 != null) {
                     float var50 = (float)var49.getValue();
                     if (!(var50 <= 0.01F)) {
                        String var37 = KeyNames.computeNone(var48.getBind()).toUpperCase();
                        this.handleMax(matrixStack, var48.getName(), var37, var50, var3, var32, var24, var5, var6, var11);
                        var32 += var11 * var50;
                     }
                  }
               }
            }

            this.currentEvent.getContext().disableScissor();
         }
      }
   }

   public void handleMax(MatrixStack ms, String name, String key, float a, float x, float cY, float totalWidth, float pad, float fontSize, float rowH) {
      if (!(a <= 0.01F)) {
         boolean var11 = ThemeManager.getInstance().getIsDayMode();
         int var12 = Math.max(0, Math.min(255, (int)(255.0F * a)));
         int var13 = var12 << 24 | this.getColor().getRGB() & 16777215;
         int var14 = (int)((float)var12 * 0.75F) << 24 | (var11 ? 4674921 : 14870768);
         float var15 = cY + (rowH - fontSize) / 2.0F - this.computeFloat(0.3F);
         this.getMkadw().handleVal(ms, name, x + pad, var15, fontSize, var13);
         float var16 = this.getMkadw().computeFloat(key, fontSize);
         float var17 = this.computeFloat(6.5F);
         float var18 = x + totalWidth - pad - var17 / 2.0F;
         float var19 = var18 - var16 / 2.0F;
         this.getMkadw().handleVal(ms, key, var19, var15, fontSize, var14);
      }
   }

   public static AnimatedValue lambda$render$0(Module k) {
      return new AnimatedValue();
   }
}
