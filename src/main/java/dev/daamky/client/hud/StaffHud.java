package dev.daamky.client.hud;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.CollisionUtil;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.PhaseHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.StaffHudHelper;
import dev.daamky.client.internal.StaffStore;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.GameMode;

public class StaffHud extends HudElement {
   public static final int intVal = -12320924;
   public static final int int2 = -47546;
   public static final int int3 = -9156;
   public static final int int4 = -6908266;
   public static final int int5 = 2030043135;
   public static final int int6 = 2013923850;
   public static final String[] stringList = new String[]{"IIoner", "Akvi4"};
   public final LinkedHashMap<String, StaffHud.Entry> linkedHashMap = new LinkedHashMap<>();
   public final StaffHudHelper staffHudHelper = new StaffHudHelper(false);
   public List<StaffHud.Staff> mdeimn = new ArrayList<>();

   public StaffHud() {
      super(96.0F, 32.0F);
   }

   @Override
   public String getName() {
      return "Staffs";
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   @Override
   public void render(MatrixStack matrixStack) {
      boolean var2 = mc.currentScreen != null;
      List var3 = this.getStaffList();

      for (StaffHud.Entry var5 : this.linkedHashMap.values()) {
         var5.flag = false;
      }

      for (StaffHud.Staff var40 : (Iterable<StaffHud.Staff>)(Iterable)var3) {
         StaffHud.Entry var6 = this.linkedHashMap.computeIfAbsent(var40.n(), StaffHud::lambda$render$0);
         var6.intVal = var40.c();
         var6.flag = true;
      }

      for (StaffHud.Entry var41 : this.linkedHashMap.values()) {
         var41.animatedValue.update();
         var41.animatedValue.animateTo(var41.flag ? 1.0 : 0.0, 150L, this.getEasing());
      }

      this.linkedHashMap.values().removeIf(StaffHud::lambda$render$1);
      boolean var39 = !this.linkedHashMap.isEmpty();
      if (var39 || var2) {
         boolean var42 = !var39 && var2;
         float var7 = this.getAutoHelper().getX();
         float var8 = this.getAutoHelper().getY();
         float var9 = this.computeFloat(5.0F);
         float var10 = this.computeFloat(5.0F);
         float var11 = this.computeFloat(14.0F);
         float var12 = var11 + this.computeFloat(2.0F);
         float var13 = this.computeFloat(2.5F);
         float var14 = this.computeFloat(2.5F);
         float var15 = this.computeFloat(11.0F);
         float var16 = 0.0F;
         float var17 = 0.0F;
         if (var42) {
            for (String var21 : stringList) {
               var17 = Math.max(var17, this.getMkadw().computeFloat(var21, var10));
            }

            var16 = var15;
         } else {
            for (Map.Entry var45 : this.linkedHashMap.entrySet()) {
               var17 = Math.max(var17, this.getMkadw().computeFloat((String)var45.getKey(), var10));
               var16 += var15 * (float)((StaffHud.Entry)var45.getValue()).animatedValue.getValue();
            }
         }

         float var44 = this.computeFloat(96.0F);
         float var46 = this.computeFloat(6.0F);
         String var47 = EngHelper.computeString("Staff Online");
         float var48 = FontBootstrap.clientFont9.computeFloat(var47, var46);
         float var22 = this.computeFloat(6.5F);
         float var23 = this.computeFloat(3.5F);
         float var24 = this.computeFloat(6.5F) + var23 + var17;
         float var25 = Math.max(var44, var9 * 2.0F + var24);
         float var26 = var12 + var13 + var16 + var14;
         this.getAutoHelper().setWidth(var25);
         this.getAutoHelper().setHeight(var26);
         Color var27 = this.initInit();
         float var28 = this.getHeight();
         if (this.getAutoHelper().isBlur()) {
            Render.shaderProgram.handleVal(matrixStack, var7, var8, var25, var26, var28, var27.getRGB());
         } else {
            Render.rectRenderer.handleVal(matrixStack, var7, var8, var25, var26, var28, var27.getRGB());
         }

         this.runContent();
         if (Interface.getInstance().isEnabled() && Interface.check2()) {
            Interface.handleVal(matrixStack, var7, var8, var25, var26, var28, 1.0F, Interface.check4());
         }

         boolean var29 = ThemeManager.getInstance().getIsDayMode();
         int var30 = var29 ? 2030043135 : 2013923850;
         Render.rectRenderer.handleVal(matrixStack, var7, var8, var25, var12, var28, var30);
         float var31 = var8 + (var12 - var46) / 2.0F - this.computeFloat(0.8F);
         FontBootstrap.clientFont9.handleVal(matrixStack, var47, var7 + var9, var31, var46, this.getColor().getRGB());
         float var32 = var8 + (var12 - var22) / 2.0F - this.computeFloat(0.5F);
         this.getValue2().handleVal(matrixStack, "k", var7 + var25 - var9 - var22, var32, var22, this.getColor().getRGB());
         float var33 = var8 + var12 + var13;
         this.currentEvent.getContext().enableScissor((int)var7, (int)var8, (int)(var7 + var25), (int)(var8 + var26));
         if (var42) {
            PhaseHelper.Phase var34 = PhaseHelper.initInit(stringList.length, 2200L, 550L);
            float var35 = this.computeFloat(2.0F);
            this.handleMax(matrixStack, stringList[var34.index()], -12320924, 1.0F - var34.k(), var7, var33 - var34.k() * var35, var9, var10, var15);
            if (var34.k() > 0.001F) {
               this.handleMax(matrixStack, stringList[var34.next()], -12320924, var34.k(), var7, var33 + (1.0F - var34.k()) * var35, var9, var10, var15);
            }
         } else {
            for (Map.Entry var50 : this.linkedHashMap.entrySet()) {
               float var36 = (float)((StaffHud.Entry)var50.getValue()).animatedValue.getValue();
               if (!(var36 <= 0.01F)) {
                  this.handleMax(matrixStack, (String)var50.getKey(), ((StaffHud.Entry)var50.getValue()).intVal, var36, var7, var33, var9, var10, var15);
                  var33 += var15 * var36;
               }
            }
         }

         this.currentEvent.getContext().disableScissor();
      }
   }

   public void handleMax(MatrixStack ms, String name, int dotColor, float a, float x, float cY, float pad, float fontS, float rowH) {
      int var10 = Math.max(0, Math.min(255, (int)(255.0F * a)));
      int var11 = var10 << 24 | this.getColor().getRGB() & 16777215;
      int var12 = var10 << 24 | dotColor & 16777215;
      float var13 = this.computeFloat(6.5F);
      float var14 = this.computeFloat(3.5F);
      float var15 = this.computeFloat(3.5F);
      float var16 = x + pad + (var13 - var14) / 2.0F;
      float var17 = cY + (rowH - var14) / 2.0F;
      Render.rectRenderer.handleVal(ms, var16, var17, var14, var14, var14 / 2.0F, var12);
      float var18 = cY + (rowH - fontS) / 2.0F - this.computeFloat(0.3F);
      this.getMkadw().handleVal(ms, name, x + pad + var13 + var15, var18, fontS, var11);
   }

   public List<StaffHud.Staff> getStaffList() {
      this.staffHudHelper.handleFps(15, this::lambda$getStaffList$2);
      return this.mdeimn;
   }

   public void lambda$getStaffList$2() {
      ArrayList var1 = new ArrayList();
      if (!mc.isInSingleplayer() && mc.player != null && mc.getNetworkHandler() != null && mc.world != null) {
         HashSet var2 = new HashSet();
         HashSet var3 = new HashSet();

         for (AbstractClientPlayerEntity var5 : mc.world.getPlayers()) {
            var3.add(var5.getGameProfile().getName());
         }

         for (PlayerListEntry var12 : mc.getNetworkHandler().getPlayerList()) {
            String var6 = var12.getProfile().getName();
            var2.add(var6);
            if (CollisionUtil.computeFlag(var6)) {
               Team var7 = var12.getScoreboardTeam();
               String var8 = var7 != null ? var7.getPrefix().getString().toLowerCase() : "";
               if (StaffStore.getInstance().computeContains(var6)
                  || var8.contains("helper")
                  || var8.contains("admin")
                  || var8.contains("moder")
                  || var8.contains("staff")) {
                  int var9 = -12320924;
                  if (var12.getGameMode() == GameMode.SPECTATOR) {
                     var9 = -47546;
                  } else if (var3.contains(var6)) {
                     var9 = -9156;
                  }

                  var1.add(new StaffHud.Staff(var6, var9));
               }
            }
         }

         for (Team var13 : mc.world.getScoreboard().getTeams()) {
            for (String var15 : var13.getPlayerList()) {
               if (CollisionUtil.computeFlag(var15) && !var2.contains(var15)) {
                  var1.add(new StaffHud.Staff(var15, -6908266));
               }
            }
         }
      }

      this.mdeimn = var1;
   }

   public static boolean lambda$render$1(StaffHud.Entry e) {
      return !e.flag && e.animatedValue.getValue() <= 0.01;
   }

   public static StaffHud.Entry lambda$render$0(String k) {
      return new StaffHud.Entry();
   }

   private static final class Entry {
      public int intVal;
      public final AnimatedValue animatedValue = new AnimatedValue();
      public boolean flag;

      public Entry() {
      }
   }

   public static record Staff(String n, int c) {
   }
}
