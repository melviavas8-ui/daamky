package dev.daamky.client.module.render;

import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.List;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

@ModuleInfo(
   name = "Shulker Preview",
   category = ModuleCategory.VISUALS,
   description = "Отображает интерактивный предпросмотр содержимого шалкер-боксов"
)
public class ShulkerPreview extends Module {
   public static final ShulkerPreview shulkerPreview = new ShulkerPreview();
   public final BooleanSetting onlyWithAlt = new BooleanSetting("Only with Alt").value(false);

   public ShulkerPreview() {
      this.addSettings(new Setting[]{this.onlyWithAlt});
   }

   public static ShulkerPreview getInstance() {
      return shulkerPreview;
   }

   @Override
   public void onEvent() {
   }

   public void initInit(DrawContext context, ItemStack shulkerStack, int mouseX, int mouseY) {
      if (shulkerStack != null && !shulkerStack.isEmpty() && mc.player != null) {
         if (this.onlyWithAlt.getValue()) {
            long var5 = mc.getWindow().getHandle();
            if (!InputUtil.isKeyPressed(var5, 342) && !InputUtil.isKeyPressed(var5, 346)) {
               return;
            }
         }

         if (shulkerStack.getItem() instanceof BlockItem var25 && var25.getBlock() instanceof ShulkerBoxBlock var6) {
            ContainerComponent var27 = (ContainerComponent)shulkerStack.get(DataComponentTypes.CONTAINER);
            if (var27 == null) {
               return;
            }

            List var8 = var27.stream().toList();
            if (var8.isEmpty()) {
               return;
            }

            float var9 = 4.0F;
            float var10 = 18.0F;
            float var11 = var9 * 2.0F + var10 * 9.0F;
            float var12 = var9 * 2.0F + var10 * 3.0F;
            int var13 = mc.getWindow().getScaledWidth();
            int var14 = mc.getWindow().getScaledHeight();
            float var15 = (float)mouseX + 10.0F;
            float var16 = (float)mouseY - var12 - 8.0F;
            if (var15 + var11 > (float)var13 - 5.0F) {
               var15 = (float)var13 - var11 - 5.0F;
            }

            if (var15 < 5.0F) {
               var15 = 5.0F;
            }

            if (var16 < 5.0F) {
               var16 = (float)mouseY + 20.0F;
            }

            MatrixStack var17 = context.getMatrices();
            var17.push();
            var17.translate(0.0F, 0.0F, 400.0F);
            Color var18 = new Color(14, 14, 18, 240);
            Render.shaderProgram.handleRgb(var17, var15, var16, var11, var12, 4.0F, var18);
            Render.rectRenderer.handleRgb(var17, var15, var16, var11, var12, 4.0F, InterfaceMetrics.computeColor3(40));

            for (int var19 = 0; var19 < 27; var19++) {
               int var20 = var19 % 9;
               int var21 = var19 / 9;
               float var22 = var15 + var9 + (float)var20 * var10;
               float var23 = var16 + var9 + (float)var21 * var10;
               Render.rectRenderer.handleRgb(var17, var22, var23, var10 - 1.0F, var10 - 1.0F, 2.0F, new Color(255, 255, 255, 12));
               if (var19 < var8.size()) {
                  ItemStack var24 = (ItemStack)var8.get(var19);
                  if (var24 != null && !var24.isEmpty()) {
                     context.drawItem(var24, (int)var22 + 1, (int)var23 + 1);
                     context.drawStackOverlay(mc.textRenderer, var24, (int)var22 + 1, (int)var23 + 1);
                  }
               }
            }

            var17.pop();
            return;
         }
      }
   }
}
