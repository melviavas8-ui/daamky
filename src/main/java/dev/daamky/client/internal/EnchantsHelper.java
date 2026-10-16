package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.module.render.NameTags;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantsHelper {
   public final NameTags module;
   public static final Set<String> set = new HashSet<>(
      Arrays.asList(
         "minecraft:protection",
         "minecraft:fire_protection",
         "minecraft:blast_protection",
         "minecraft:projectile_protection",
         "minecraft:thorns",
         "minecraft:sharpness",
         "minecraft:fire_aspect",
         "minecraft:knockback",
         "minecraft:looting",
         "minecraft:unbreaking",
         "minecraft:efficiency",
         "minecraft:power",
         "minecraft:punch",
         "minecraft:infinity",
         "minecraft:mending"
      )
   );

   public EnchantsHelper(NameTags module) {
      this.module = modulex;
   }

   public void handleNext(ItemStack stack, MatrixStack matrices, float x, float y, float scale) {
      if (this.module.onlyHands.isEnabled("Enchants")) {
         if (stack.hasEnchantments()) {
            float var6 = 5.0F * scale;
            float var7 = 0.0F;
            ItemEnchantmentsComponent var8 = EnchantmentHelper.getEnchantments(stack);

            for (Entry var10 : var8.getEnchantmentEntries()) {
               RegistryEntry var11 = (RegistryEntry)var10.getKey();
               if (set.contains(var11.getIdAsString())) {
                  int var12 = var10.getIntValue();
                  int var13 = ((Enchantment)var11.value()).getMaxLevel();
                  String var14 = var11.getIdAsString();
                  String var15 = var14.substring(var14.indexOf(58) + 1, Math.min(var14.indexOf(58) + 4, var14.length()));
                  var15 = var15.substring(0, 1).toUpperCase() + var15.substring(1);
                  Color var16 = var12 < var13 + 1 ? ThemeColor.getColor8() : ThemeColor.getColor12();
                  FontBootstrap.clientFont10.handleColor(matrices, var15 + var12, x, y + var7, var6, var16);
                  var7 -= var6 + 0.5F;
               }
            }
         }
      }
   }

   public void initContains(PlayerEntity player, float centerX, float y, DrawContext context, float scale) {
      ArrayList var6 = new ArrayList();
      MatrixStack var7 = context.getMatrices();

      for (int var8 = 0; var8 < player.getInventory().size(); var8++) {
         ItemStack var9 = player.getInventory().getStack(var8);
         String var10 = var9.getName().getString();
         boolean var11 = (
               var9.getItem() == Items.TOTEM_OF_UNDYING || var9.getItem() == Items.PLAYER_HEAD || var9.getItem() == Items.POPPED_CHORUS_FRUIT
            )
            && (var10.contains("Сфера") || var10.contains("Руна") || var10.contains("Шар") || var10.contains("Талисман"));
         boolean var12 = var10.contains("Крылья ангела") && var9.getItem() == Items.ELYTRA;
         boolean var13 = var10.contains("Круш");
         if (var12 || var13 || var11) {
            var6.add(var9.copy());
         }
      }

      if (!var6.isEmpty()) {
         float var19 = 5.5F * scale;
         float var20 = 1.5F * scale;
         float var21 = 2.0F * scale;
         float var22 = 0.0F;

         for (ItemStack var25 : var6) {
            float var14 = FontBootstrap.clientFont10.computeFloat(var25.getName().getString(), var19);
            if (var14 > var22) {
               var22 = var14;
            }
         }

         float var24 = var22 + var21 * 2.0F;
         float var26 = (float)var6.size() * (var19 + var20) + var21 * 2.0F - var20;
         float var27 = centerX - var24 / 2.0F;
         float var15 = y + 6.0F * scale;
         if (this.module.blur.getValue()) {
            Render.shaderProgram.handleRgb(var7, var27, var15, var24, var26, 3.0F * scale, this.module.color.getValue(), 1.0F - this.module.glassy.getValue());
         } else {
            Render.rectRenderer.handleRgb(var7, var27, var15, var24, var26, 3.0F * scale, this.module.color.getValue());
         }

         float var16 = var15 + var21;

         for (ItemStack var18 : var6) {
            FontBootstrap.clientFont10.handleColor(var7, var18.getName().getString(), var27 + var21, var16, var19, new Color(255, 255, 255));
            var16 += var19 + var20;
         }
      }
   }
}
