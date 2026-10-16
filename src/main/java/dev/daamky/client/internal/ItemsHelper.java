package dev.daamky.client.internal;

import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.mixin.WorldRendererAccessor;
import dev.daamky.client.module.render.NameTags;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import org.joml.Vector2f;

public class ItemsHelper implements MinecraftAccess {
   public final NameTags mod;
   public final EnchantsHelper enchantsHelper;
   public final PotionEntryHelper potionEntryHelper;
   public final List<ItemStack> madat = new ArrayList<>();
   public static final Color color = new Color(14, 14, 16, 205);
   public static final Color color2 = new Color(50, 255, 50);
   public static final Color color3 = new Color(180, 180, 180);
   public static final Color color4 = new Color(255, 80, 80);
   public static final Color color5 = new Color(255, 80, 80);
   public static final Color maqibn = new Color(255, 200, 50);
   public static final Color color6 = new Color(80, 255, 80);
   public final Vector2f vector2f = new Vector2f();

   public ItemsHelper(NameTags mod) {
      this.mod = modx;
      this.enchantsHelper = new EnchantsHelper(modx);
      this.potionEntryHelper = new PotionEntryHelper(modx);
   }

   public void handleContext(Render2DEvent.Render2DEventData event) {
      if (mc.player != null && mc.world != null) {
         double var2 = 2304.0;
         double var4 = mc.player.getX();
         double var6 = mc.player.getY();
         double var8 = mc.player.getZ();

         for (Entity var11 : mc.world.getEntities()) {
            if (var11 != null) {
               double var12 = var11.getX() - var4;
               double var14 = var11.getY() - var6;
               double var16 = var11.getZ() - var8;
               if (!(var12 * var12 + var14 * var14 + var16 * var16 > var2)) {
                  if (var11 instanceof ItemEntity) {
                     ItemEntity var18 = (ItemEntity)var11;
                     if (this.mod.items.isEnabled("Items")) {
                        this.initFrustum(var18, event.getContext());
                     }
                  } else if (var11 instanceof LivingEntity) {
                     LivingEntity var19 = (LivingEntity)var11;
                     if (this.mod.entityFilter.computeFlag(var19)
                        || var19 == mc.player && this.mod.items.isEnabled("Self") && !mc.options.getPerspective().isFirstPerson()) {
                        this.initFrustum(var19, event.getContext());
                     }
                  }
               }
            }
         }
      }
   }

   public void initFrustum(Entity entity, DrawContext ctx) {
      double var4 = (double)EntityHelper.computeFloat(entity.prevX, entity.getX());
      double var6 = (double)EntityHelper.computeFloat(entity.prevY, entity.getY());
      double var8 = (double)EntityHelper.computeFloat(entity.prevZ, entity.getZ());
      Box var10 = entity.getBoundingBox();
      double var11 = (var10.maxX - var10.minX) * 0.5;
      double var13 = (var10.maxZ - var10.minZ) * 0.5;
      double var15 = var10.maxY - var10.minY;
      Box var17 = new Box(var4 - var11, var6, var8 - var13, var4 + var11, var6 + var15, var8 + var13);
      Frustum var3;
      if (!(mc.worldRenderer instanceof WorldRendererAccessor) || (var3 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum()) == null || var3.isVisible(var17)
         )
       {
         double var18 = Math.sqrt(mc.player.squaredDistanceTo(entity));
         float var20 = this.mod.scale.getValue();
         float var21 = (float)(1.0 + Math.min(var18, 40.0) * 0.008);
         float var22 = var20 * var21;
         int var25 = mc.getWindow().getScaledWidth();
         int var26 = mc.getWindow().getScaledHeight();
         float var23;
         float var24;
         if (entity instanceof ItemEntity) {
            ContextHelper.computeVector2f(var4, var6 + var15 * 0.5, var8, this.vector2f);
            if (this.vector2f.x == Float.MAX_VALUE) {
               return;
            }

            var23 = this.vector2f.x;
            var24 = this.vector2f.y - 10.0F * var22;
         } else {
            float var27 = Float.MAX_VALUE;
            float var28 = Float.MAX_VALUE;
            float var29 = -Float.MAX_VALUE;
            float var30 = -Float.MAX_VALUE;

            for (int var31 = 0; var31 < 8; var31++) {
               double var32 = (var31 & 1) == 0 ? var17.minX : var17.maxX;
               double var34 = (var31 >> 1 & 1) == 0 ? var17.minY : var17.maxY;
               double var36 = (var31 >> 2 & 1) == 0 ? var17.minZ : var17.maxZ;
               ContextHelper.computeVector2f(var32, var34, var36, this.vector2f);
               if (this.vector2f.x == Float.MAX_VALUE) {
                  return;
               }

               if (this.vector2f.x < var27) {
                  var27 = this.vector2f.x;
               }

               if (this.vector2f.y < var28) {
                  var28 = this.vector2f.y;
               }

               if (this.vector2f.x > var29) {
                  var29 = this.vector2f.x;
               }

               if (this.vector2f.y > var30) {
                  var30 = this.vector2f.y;
               }
            }

            var23 = (var27 + var29) / 2.0F;
            var24 = var28 - 12.0F * var22;
         }

         if (!(var23 < -60.0F) && !(var23 > (float)var25 + 60.0F) && !(var24 < -60.0F) && !(var24 > (float)var26 + 60.0F)) {
            MatrixStack var38 = ctx.getMatrices();
            if (entity instanceof PlayerEntity var39) {
               this.renderPlayer(var39, var23, var24, ctx, var38, var22, var18);
            } else if (entity instanceof ItemEntity var40) {
               this.handleMax(var40, var23, var24, ctx, var38, var22);
            } else {
               this.handleBlack(entity, var23, var24, var38, var22);
            }
         }
      }
   }

   public void handleBlack(Entity e, float cx, float y, MatrixStack ms, float s) {
      String var6 = e.getName().getString();
      float var7 = 7.0F * s;
      float var8 = 2.5F * s;
      float var9 = FontBootstrap.clientFont6.computeFloat(var6, var7);
      float var10 = cx - var9 / 2.0F - var8 * 2.0F;
      this.handleMod(ms, var10, y, var9 + var8 * 4.0F, var7 + var8 * 2.0F, s);
      FontBootstrap.clientFont6.handleColor(ms, var6, var10 + var8 * 2.0F + 0.5F * s, y + var8 + 0.5F * s, var7, Color.BLACK);
      FontBootstrap.clientFont6.handleColor(ms, var6, var10 + var8 * 2.0F, y + var8, var7, this.mod.textColor.getValue());
   }

   public void handleMax(ItemEntity itemEntity, float cx, float y, DrawContext ctx, MatrixStack ms, float s) {
      ItemStack var7 = itemEntity.getStack();
      if (!var7.isEmpty()) {
         String var8 = var7.getCount() > 1 ? " x" + var7.getCount() : "";
         String var9 = var7.getName().getString() + var8;
         ClientFont var10 = FontBootstrap.clientFont6;
         float var11 = 7.0F * s;
         float var12 = 2.5F * s;
         float var13 = 10.0F * s;
         float var14 = 2.0F * s;
         float var15 = var10.computeFloat(var9, var11);
         float var16 = var13 + var14 + var15;
         float var17 = var16 + var12 * 2.0F;
         float var18 = Math.max(var13, var11) + var12 * 2.0F;
         float var20 = cx + var12;
         float var21 = y + var12 + (var18 - var12 * 2.0F - var13) / 2.0F;
         float var22 = var13 / 16.0F;
         ms.push();
         ms.translate(var20, var21, 0.0F);
         ms.scale(var22, var22, 1.0F);
         ctx.drawItem(var7, 0, 0);
         ms.pop();
         float var23 = var20 + var13 + var14;
         float var24 = y + var12 + (var18 - var12 * 2.0F - var11) / 2.0F;
         var10.handleColor(ms, var9, var23, var24, var11, this.mod.textColor.getValue());
      }
   }

   public void renderPlayer(PlayerEntity p, float cx, float topY, DrawContext ctx, MatrixStack ms, float s, double dist) {
      ClientFont var10 = FontBootstrap.clientFont6;
      float var11 = 7.5F * s;
      float var12 = 5.5F * s;
      float var13 = 2.0F * s;
      float var14 = 2.0F * s;
      float var15 = 9.0F * s;
      int var16 = 0;
      PlayerListEntry var9;
      if (mc.getNetworkHandler() != null && (var9 = mc.getNetworkHandler().getPlayerListEntry(p.getUuid())) != null) {
         var16 = var9.getLatency();
      }

      String var17 = var16 + "ms";
      float var18 = var10.computeFloat(var17, var11);
      String var19 = p.getName().getString();
      boolean var20 = FriendsStore.getInstance().computeContains(var19);
      Color var21 = var20 ? this.mod.friendColor.getValue() : this.mod.textColor.getValue();
      float var22 = var10.computeFloat(var19, var11);
      float var23 = 1.5F * s;
      float var24 = var23 * 2.0F;
      float var25 = 9.0F * s;
      float var26 = var25 / 2.0F;
      float var27 = 0.8F * s;
      String var28 = this.mod.armor.isEnabled("Health") ? String.format("%.0f", p.getHealth()) : "";
      float var29 = this.mod.armor.isEnabled("Health") ? var25 : 0.0F;
      float var30 = var24 + var14 + var22 + var14 + var18 + (var29 > 0.0F ? var14 + var29 : 0.0F);
      float var31 = var13 * 2.0F + var30;
      float var32 = var11 + var13 * 2.0F;
      float var33 = cx - var31 / 2.0F;
      this.handleMod(ms, var33, topY, var31, var32, s);
      float var35 = var33 + var13;
      float var36 = topY + var13 - 0.25F * s;
      float var37 = var36 + var11 / 2.0F;
      float var38 = var37 - var23;
      Render.rectRenderer.handleRgb(ms, var35, var38, var23 * 2.0F, var23 * 2.0F, var23, var20 ? this.mod.friendColor.getValue() : color2);
      var10.handleColor(ms, var19, var35 + 0.5F * s + var24 + var14, var36 + 0.5F * s, var11, Color.BLACK);
      float var52;
      var10.handleColor(ms, var19, var52 = var35 + var24 + var14, var36, var11, var21);
      var35 = var52 + var22;
      var10.handleColor(ms, var17, var35 + 0.5F * s + var14, var36 + 0.5F * s, var11, Color.BLACK);
      float var54;
      var10.handleColor(ms, var17, var54 = var35 + var14, var36, var11, color3);
      var35 = var54 + var18;
      if (var29 > 0.0F) {
         float var39 = var35 + var14 + var26;
         Render.ringRenderer.handleRgb(ms, var39, var37, var26 - var27 / 2.0F, var27, new Color(10, 10, 10, 150));
         float var41 = Math.max(0.0F, Math.min(1.0F, p.getHealth() / p.getMaxHealth()));
         float var42 = -90.0F + 360.0F * var41;
         if (var41 > 0.005F) {
            Render.ringRenderer.handleRadians(ms, var39, var37, var26 - var27 / 2.0F, var27, -90.0F, var42, color4);
         }

         float var43 = 5.0F * s;
         var10.handleColor2(ms, var28, var39 + 0.5F * s, var37 - var43 / 2.0F + 0.15F * s + 0.5F * s, var43, Color.BLACK);
         var10.handleColor2(ms, var28, var39, var37 - var43 / 2.0F + 0.15F * s, var43, Color.WHITE);
      }

      Box var56 = p.getBoundingBox();
      ContextHelper.computeVector2f(p.getX(), var56.minY, p.getZ(), this.vector2f);
      float var40 = this.vector2f.y > topY ? this.vector2f.y + 5.0F * s : topY + var32 + 10.0F * s;
      if (this.vector2f.x == Float.MAX_VALUE) {
         var40 = topY + var32 + 10.0F * s;
      }

      this.madat.clear();
      if (dist <= 48.0) {
         if (this.mod.armor.isEnabled("Items")) {
            this.madat.add(p.getMainHandStack());
         }

         if (this.mod.armor.isEnabled("Armor")) {
            for (ItemStack var61 : p.getInventory().armor) {
               if (var61.getItem() != Items.AIR) {
                  this.madat.add(var61);
               }
            }
         }

         if (this.mod.armor.isEnabled("Items")) {
            this.madat.add(p.getOffHandStack());
            if (!this.mod.onlyHands.isEnabled("Only hands")) {
               for (int var58 = 0; var58 < 9; var58++) {
                  ItemStack var62 = p.getInventory().getStack(var58);
                  if (!var62.isEmpty() && var62 != p.getMainHandStack()) {
                     this.madat.add(var62);
                     break;
                  }
               }
            }
         }

         this.madat.removeIf(ItemsHelper::lambda$renderPlayer$0);
      }

      if (!this.madat.isEmpty()) {
         boolean var59 = false;

         for (ItemStack var65 : this.madat) {
            if (var65.isDamageable()) {
               var59 = true;
               break;
            }
         }

         float var64 = 0.0F;
         float var66 = var13 * 2.0F + (float)this.madat.size() * (var15 + var14) - var14;
         float var44 = var13 * 2.0F + var15 + var64;
         float var45 = cx - var66 / 2.0F;
         float var46 = topY - var15 - var13;
         float var47 = var45 + var13;
         float var48 = var46 + var13;

         for (ItemStack var50 : this.madat) {
            float var51 = var15 / 16.0F;
            ms.push();
            ms.translate(var47, var48, 0.0F);
            ms.scale(var51, var51, 1.0F);
            ctx.drawItem(var50, 0, 0);
            ctx.drawStackOverlay(mc.textRenderer, var50, 0, 0);
            ms.pop();
            if (this.mod.onlyHands.isEnabled("Enchants") && var50.hasEnchantments()) {
               this.enchantsHelper.handleNext(var50, ms, var47 + var15 / 2.0F, var48, s);
            }

            var47 += var15 + var14;
         }

         var40 += var44 + var14;
      }

      if (dist <= 48.0 && this.mod.armor.isEnabled("Potions") && !p.getStatusEffects().isEmpty()) {
         this.handleNext(p, var33 + var31 + var14, topY, ms, s);
         var40 += (float)this.potionEntryHelper.initInit(p.getStatusEffects()).size() * (var12 + 1.0F * s) + var13 * 2.0F;
      }

      if (dist <= 48.0 && this.mod.onlyHands.isEnabled("Special items")) {
         this.enchantsHelper.initContains(p, cx, var40, ctx, s);
      }
   }

   public void handleNext(PlayerEntity p, float cx, float y, MatrixStack ms, float s) {
      List var6 = this.potionEntryHelper.initInit(p.getStatusEffects());
      if (!var6.isEmpty()) {
         float var7 = 5.5F * s;
         float var8 = 1.0F * s;
         float var9 = 2.0F * s;
         float var10 = 0.0F;

         for (PotionEntryHelper.PotionEntry var12 : var6) {
            float var13 = FontBootstrap.clientFont10.computeFloat(var12.text(), var7);
            if (var13 > var10) {
               var10 = var13;
            }
         }

         float var17 = var10 + var9 * 2.0F;
         float var18 = (float)var6.size() * (var7 + var8) + var9 * 2.0F - var8;
         float var19 = cx;
         float var14 = y + var9;

         for (PotionEntryHelper.PotionEntry var16 : var6) {
            FontBootstrap.clientFont6.handleColor(ms, var16.text(), var19 + 0.5F * s, var14 + 0.5F * s, var7, Color.BLACK);
            FontBootstrap.clientFont6.handleColor(ms, var16.text(), var19, var14, var7, this.mod.textColor.getValue());
            var14 += var7 + var8;
         }
      }
   }

   public void handleMod(MatrixStack ms, float x, float y, float w, float h, float s) {
      float var7 = 3.0F * s;
      Color var8 = this.mod.color.getValue();
      if (this.mod.blur.getValue()) {
         Render.shaderProgram.handleRgb(ms, x, y, w, h, var7, var8);
      } else {
         Render.rectRenderer.handleRgb(ms, x, y, w, h, var7, var8);
      }
   }

   public static boolean lambda$renderPlayer$0(ItemStack st) {
      return st.getItem() == Items.AIR;
   }
}
