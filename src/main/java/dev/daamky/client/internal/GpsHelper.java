package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.config.ConfigManager;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.hud.ArmorHud;
import dev.daamky.client.hud.BpsHud;
import dev.daamky.client.hud.CooldownsHud;
import dev.daamky.client.hud.CoordinatesHud;
import dev.daamky.client.hud.GpsHud;
import dev.daamky.client.hud.HotbarHud;
import dev.daamky.client.hud.InventoryHud;
import dev.daamky.client.hud.KeybindsHud;
import dev.daamky.client.hud.PotionsHud;
import dev.daamky.client.hud.ServerInfoHud;
import dev.daamky.client.hud.StaffHud;
import dev.daamky.client.hud.TargetHud;
import dev.daamky.client.hud.WatermarkHud;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.util.KeyNames;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

public class GpsHelper {
   public static final GpsHelper gpsHelper = new GpsHelper();
   public final List<HudElement> list = new ArrayList<>();
   public boolean flag = false;
   public boolean flag2 = false;
   public long timeStamp = 0L;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public HudElement activeConfigWidget = null;
   public float floatVal = 0.0F;
   public float float2 = 0.0F;
   public boolean flag3 = false;
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public HudElement widget = null;

   public float computeFloat(HudElement widget) {
      if (widgetx == null) {
         return this.computeFloat(58.0F);
      } else {
         boolean var2 = widgetx instanceof BpsHud;
         boolean var3 = widgetx instanceof WatermarkHud || widgetx instanceof NotificationManager;
         boolean var4 = widgetx instanceof PotionsHud;
         int var5 = 0;
         if (var4) {
            var5++;
         }

         if (var2) {
            var5++;
         }

         if (var3) {
            var5++;
         }

         return this.computeFloat(58.0F) + (float)var5 * this.computeFloat(14.5F);
      }
   }

   public void handleMax(HudElement widget, float mouseX, float mouseY) {
      this.activeConfigWidget = widgetx;
      this.widget = widgetx;
      MinecraftClient var4 = MinecraftClient.getInstance();
      float var5 = (float)var4.getWindow().getScaledWidth();
      float var6 = (float)var4.getWindow().getScaledHeight();
      float var7 = this.computeFloat(96.0F);
      float var8 = this.computeFloat(widgetx);
      this.floatVal = Math.max(10.0F, Math.min(mouseX, var5 - var7 - 10.0F));
      this.floatValVal2 = Math.max(10.0F, Math.min(mouseY, var6 - var8 - 10.0F));
      this.flag3 = false;
      this.animatedValue2.handleStart(0.0);
   }

   public void load() {
      this.handleAdd(
         new WatermarkHud(),
         new KeybindsHud(),
         new PotionsHud(),
         new StaffHud(),
         new CooldownsHud(),
         new ArmorHud(),
         new TargetHud(),
         new CoordinatesHud(),
         new BpsHud(),
         new ServerInfoHud(),
         new HotbarHud(),
         new InventoryHud(),
         new NotificationManager(),
         new GpsHud()
      );
      Interface.getInstance().runAdd();

      for (HudElement var2 : this.list) {
         String var3 = var2.getName();

         try {
            boolean var4 = Interface.getInstance().gps.isEnabled(var3);
            if (!var4 && var3.equalsIgnoreCase("GPS")) {
               try {
                  var4 = Interface.getInstance().gps.isEnabled("Waypoint");
               } catch (Exception var6) {
               }
            }

            var2.setEnabled(var4);
         } catch (Exception var7) {
            if (!(var2 instanceof NotificationManager)) {
               var2.setEnabled(true);
            }
         }
      }

      Render2DEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$1));
   }

   public void handleAdd(HudElement... widgets) {
      this.list.addAll(List.of(widgets));
   }

   public float computeFloat(float val) {
      return DisplayScale.getInstance().computeScale(val) * 0.65F;
   }

   public void initInit(MatrixStack ms, float mouseX, float mouseY) {
      float var4 = (float)this.animatedValue2.getValue();
      if (!(var4 < 0.001F)) {
         HudElement var5 = this.activeConfigWidget != null ? this.activeConfigWidget : this.widget;
         if (var5 != null) {
            float var7 = this.computeFloat(6.5F);
            float var8 = this.computeFloat(4.0F * MainMenuScreen.getGsRadius());
            float var9 = this.computeFloat(96.0F);
            boolean var10 = var5 instanceof BpsHud;
            boolean var11 = var5 instanceof WatermarkHud || var5 instanceof NotificationManager;
            boolean var12 = var5 instanceof PotionsHud;
            float var13 = this.computeFloat(var5);
            ms.push();
            float var14 = this.floatVal + var9 / 2.0F;
            float var15 = this.floatValVal2 + var13 / 2.0F;
            ms.translate(var14, var15, 0.0F);
            ms.scale(0.85F + 0.15F * var4, 0.85F + 0.15F * var4, 1.0F);
            ms.translate(-var14, -var15, 0.0F);
            int var16 = (int)(245.0F * var4);
            Color var17 = new Color(0, 0, 0, var16);
            Render.shaderProgram.handleRgb(ms, this.floatVal, this.floatValVal2, var9, var13, var8, var17);
            Render.rectRenderer.handleRgb(ms, this.floatVal, this.floatValVal2, var9, var13, var8, InterfaceMetrics.computeColor3((int)(20.0F * var4)));
            float var18 = this.computeFloat(6.5F);
            float var19 = this.floatValVal2 + var7 + this.computeFloat(0.5F);
            FontBootstrap.mdoswa.handleColor(ms, var5.getName(), this.floatVal + var7, var19, var18, new Color(255, 255, 255, (int)(255.0F * var4)));
            float var20 = var19 + var18 + this.computeFloat(3.5F);
            Render.rectRenderer
               .handleRgb(ms, this.floatVal + var7, var20, var9 - var7 * 2.0F, this.computeFloat(0.5F), 0.0F, new Color(255, 255, 255, (int)(30.0F * var4)));
            float var21 = var20 + this.computeFloat(4.5F);
            float var22 = this.computeFloat(5.5F);
            float var23 = var5.getAutoHelper().getScale();
            FontBootstrap.clientFont10
               .handleColor(
                  ms,
                  "Scale: " + String.format(Locale.US, "%.2f", var23) + "x",
                  this.floatVal + var7,
                  var21,
                  var22,
                  new Color(180, 180, 180, (int)(255.0F * var4))
               );
            float var24 = this.computeFloat(2.6F);
            float var25 = var21 + var22 + this.computeFloat(3.0F);
            float var26 = var9 - var7 * 2.0F;
            float var27 = 0.6F;
            float var28 = 1.5F;
            float var29 = (var23 - var27) / (var28 - var27);
            Render.rectRenderer.handleRgb(ms, this.floatVal + var7, var25, var26, var24, var24 / 2.0F, new Color(40, 40, 40, (int)(200.0F * var4)));
            Color var30 = InterfaceMetrics.computeColor3((int)(255.0F * var4));
            double var31 = 0.299 * (double)var30.getRed() + 0.587 * (double)var30.getGreen() + 0.114 * (double)var30.getBlue();
            boolean var33 = var31 > 200.0;
            if (var29 > 0.0F) {
               Render.rectRenderer.handleRgb(ms, this.floatVal + var7, var25, var26 * var29, var24, var24 / 2.0F, var30);
            }

            float var35 = this.computeFloat(5.5F);
            float var36 = this.floatVal + var7 + var26 * var29;
            float var37 = var25 + var24 / 2.0F;
            Render.ringRenderer
               .handleRgb(ms, var36, var37, var35 / 2.0F, var33 ? new Color(25, 25, 32, (int)(255.0F * var4)) : new Color(255, 255, 255, (int)(255.0F * var4)));
            float var38 = var25 + var24 + this.computeFloat(6.5F);
            FontBootstrap.clientFont10
               .handleColor(ms, "Blur Background", this.floatVal + var7, var38 + this.computeFloat(1.5F), var22, new Color(180, 180, 180, (int)(255.0F * var4)));
            float var39 = this.computeFloat(15.0F);
            float var40 = this.computeFloat(8.5F);
            float var41 = this.floatVal + var9 - var7 - var39;
            boolean var43 = var5.getAutoHelper().isBlur();
            Color var44 = new Color(36, 36, 44, (int)(255.0F * var4));
            Color var46 = var43 ? var30 : var44;
            Render.rectRenderer.handleRgb(ms, var41, var38, var39, var40, var40 / 2.0F, var46);
            float var47 = var40 - this.computeFloat(2.0F);
            float var48 = var41 + this.computeFloat(1.0F) + (var43 ? var39 - var47 - this.computeFloat(2.0F) : 0.0F);
            float var49 = var38 + this.computeFloat(1.0F);
            Color var50 = var43
               ? (var33 ? new Color(25, 25, 32, (int)(255.0F * var4)) : new Color(255, 255, 255, (int)(255.0F * var4)))
               : new Color(140, 140, 150, (int)(255.0F * var4));
            Render.ringRenderer.handleRgb(ms, var48 + var47 / 2.0F, var49 + var47 / 2.0F, var47 / 2.0F, var50);
            float var51 = var38;
            if (var5 instanceof PotionsHud var52) {
               var51 = var38 + var40 + this.computeFloat(5.5F);
               FontBootstrap.clientFont10
                  .handleColor(ms, "Cards Mode", this.floatVal + var7, var51 + this.computeFloat(1.5F), var22, new Color(180, 180, 180, (int)(255.0F * var4)));
               boolean var53 = var52.checkCard();
               Color var54 = var53 ? var30 : var44;
               Render.rectRenderer.handleRgb(ms, var41, var51, var39, var40, var40 / 2.0F, var54);
               float var55 = var41 + this.computeFloat(1.0F) + (var53 ? var39 - var47 - this.computeFloat(2.0F) : 0.0F);
               float var56 = var51 + this.computeFloat(1.0F);
               Color var57 = var53
                  ? (var33 ? new Color(25, 25, 32, (int)(255.0F * var4)) : new Color(255, 255, 255, (int)(255.0F * var4)))
                  : new Color(140, 140, 150, (int)(255.0F * var4));
               Render.ringRenderer.handleRgb(ms, var55 + var47 / 2.0F, var56 + var47 / 2.0F, var47 / 2.0F, var57);
            }

            if (var10 && var5 instanceof BpsHud var59) {
               var51 += var40 + this.computeFloat(5.5F);
               FontBootstrap.clientFont10
                  .handleColor(ms, "Graph View", this.floatVal + var7, var51 + this.computeFloat(1.5F), var22, new Color(180, 180, 180, (int)(255.0F * var4)));
               boolean var61 = var59.checkGraph();
               Color var63 = var61 ? var30 : var44;
               Render.rectRenderer.handleRgb(ms, var41, var51, var39, var40, var40 / 2.0F, var63);
               float var65 = var41 + this.computeFloat(1.0F) + (var61 ? var39 - var47 - this.computeFloat(2.0F) : 0.0F);
               float var67 = var51 + this.computeFloat(1.0F);
               Color var69 = var61
                  ? (var33 ? new Color(25, 25, 32, (int)(255.0F * var4)) : new Color(255, 255, 255, (int)(255.0F * var4)))
                  : new Color(140, 140, 150, (int)(255.0F * var4));
               Render.ringRenderer.handleRgb(ms, var65 + var47 / 2.0F, var67 + var47 / 2.0F, var47 / 2.0F, var69);
            }

            if (var11) {
               var51 += var40 + this.computeFloat(5.5F);
               FontBootstrap.clientFont10
                  .handleColor(ms, "Merge Panels", this.floatVal + var7, var51 + this.computeFloat(1.5F), var22, new Color(180, 180, 180, (int)(255.0F * var4)));
               boolean var60 = var5.getAutoHelper().getMergeCards();
               Color var62 = var60 ? var30 : var44;
               Render.rectRenderer.handleRgb(ms, var41, var51, var39, var40, var40 / 2.0F, var62);
               float var64 = var41 + this.computeFloat(1.0F) + (var60 ? var39 - var47 - this.computeFloat(2.0F) : 0.0F);
               float var66 = var51 + this.computeFloat(1.0F);
               Color var68 = var60
                  ? (var33 ? new Color(25, 25, 32, (int)(255.0F * var4)) : new Color(255, 255, 255, (int)(255.0F * var4)))
                  : new Color(140, 140, 150, (int)(255.0F * var4));
               Render.ringRenderer.handleRgb(ms, var64 + var47 / 2.0F, var66 + var47 / 2.0F, var47 / 2.0F, var68);
            }

            ms.pop();
         }
      }
   }

   public void handleActive(float mouseX) {
      if (this.activeConfigWidget != null) {
         float var2 = this.computeFloat(6.0F);
         float var3 = this.computeFloat(90.0F);
         float var4 = var3 - var2 * 2.0F;
         float var5 = mouseX - (this.floatVal + var2);
         float var6 = Math.max(0.0F, Math.min(1.0F, var5 / var4));
         float var7 = 0.6F;
         float var8 = 1.5F;
         float var9 = var7 + var6 * (var8 - var7);
         var9 = (float)Math.round(var9 / 0.05F) * 0.05F;
         this.activeConfigWidget.getAutoHelper().setScale(var9);
      }
   }

   public boolean saveActive(float mouseX, float mouseY) {
      if (this.activeConfigWidget == null) {
         return false;
      } else {
         float var3 = this.computeFloat(96.0F);
         float var4 = this.computeFloat(this.activeConfigWidget);
         if (!(mouseX < this.floatVal) && !(mouseX > this.floatVal + var3) && !(mouseY < this.floatValVal2) && !(mouseY > this.floatValVal2 + var4)) {
            float var5 = this.computeFloat(6.5F);
            float var6 = this.computeFloat(6.5F);
            float var7 = this.floatValVal2 + var5 + this.computeFloat(0.5F);
            float var8 = var7 + var6 + this.computeFloat(3.5F);
            float var9 = var8 + this.computeFloat(4.5F);
            float var10 = this.computeFloat(5.5F);
            float var11 = this.computeFloat(2.6F);
            float var12 = var9 + var10 + this.computeFloat(3.0F);
            float var13 = var3 - var5 * 2.0F;
            float var14 = this.computeFloat(4.0F);
            if (mouseX >= this.floatVal + var5 && mouseX <= this.floatVal + var5 + var13 && mouseY >= var12 - var14 && mouseY <= var12 + var11 + var14) {
               this.flag3 = true;
               this.handleActive(mouseX);
               return true;
            } else {
               float var15 = var12 + var11 + this.computeFloat(6.5F);
               float var16 = this.computeFloat(15.0F);
               float var17 = this.computeFloat(8.5F);
               float var18 = this.floatVal + var3 - var5 - var16;
               if (mouseX >= this.floatVal + var5
                  && mouseX <= var18 + var16 + var14
                  && mouseY >= var15 - this.computeFloat(2.0F)
                  && mouseY <= var15 + var17 + this.computeFloat(2.0F)) {
                  boolean var23 = this.activeConfigWidget.getAutoHelper().isBlur();
                  this.activeConfigWidget.getAutoHelper().setBlur(!var23);
                  DraggableManagerSaveHelper.getInstance().save();
                  return true;
               } else {
                  float var19 = var15;
                  if (this.activeConfigWidget instanceof PotionsHud var20) {
                     var19 = var15 + var17 + this.computeFloat(5.5F);
                     if (mouseX >= this.floatVal + var5
                        && mouseX <= var18 + var16 + var14
                        && mouseY >= var19 - this.computeFloat(2.0F)
                        && mouseY <= var19 + var17 + this.computeFloat(2.0F)) {
                        var20.handleCard(!var20.checkCard());
                        DraggableManagerSaveHelper.getInstance().save();
                        return true;
                     }
                  }

                  if (this.activeConfigWidget instanceof BpsHud var24) {
                     var19 += var17 + this.computeFloat(5.5F);
                     if (mouseX >= this.floatVal + var5
                        && mouseX <= var18 + var16 + var14
                        && mouseY >= var19 - this.computeFloat(2.0F)
                        && mouseY <= var19 + var17 + this.computeFloat(2.0F)) {
                        var24.handleGraph(!var24.checkGraph());
                        DraggableManagerSaveHelper.getInstance().save();
                        return true;
                     }
                  }

                  boolean var25 = this.activeConfigWidget instanceof WatermarkHud || this.activeConfigWidget instanceof NotificationManager;
                  if (var25) {
                     var19 += var17 + this.computeFloat(5.5F);
                     if (mouseX >= this.floatVal + var5
                        && mouseX <= var18 + var16 + var14
                        && mouseY >= var19 - this.computeFloat(2.0F)
                        && mouseY <= var19 + var17 + this.computeFloat(2.0F)) {
                        boolean var27 = this.activeConfigWidget.getAutoHelper().getMergeCards();
                        this.activeConfigWidget.getAutoHelper().setMergeCards(!var27);
                        DraggableManagerSaveHelper.getInstance().save();
                        return true;
                     }
                  }

                  return true;
               }
            }
         } else {
            this.activeConfigWidget = null;
            this.flag3 = false;
            return false;
         }
      }
   }

   public List<HudElement> getList() {
      return this.list;
   }

   public boolean check() {
      return this.flag;
   }

   public boolean check2() {
      return this.flag2;
   }

   public long getLong() {
      return this.longVal;
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public HudElement getActiveConfigWidget() {
      return this.activeConfigWidget;
   }

   public float getFloat() {
      return this.floatVal;
   }

   public float getFloat2() {
      return this.floatValVal2;
   }

   public boolean check3() {
      return this.flag3;
   }

   public AnimatedValue getAnimatedValue2() {
      return this.animatedValue2;
   }

   public HudElement getWidget() {
      return this.widget;
   }

   public static GpsHelper getInstance() {
      return gpsHelper;
   }

   public void setActiveConfigWidget(HudElement activeConfigWidget) {
      this.activeConfigWidget = activeConfigWidgetx;
   }

   public void lambda$load$1(Render2DEvent.Render2DEventData event) {
      if (Interface.getInstance().isEnabled() && !EnabledSet.getInstance().check()) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         boolean var4 = var3.currentScreen instanceof ChatScreen;
         if (this.flag && !var4) {
            this.activeConfigWidget = null;
            this.flag3 = false;
            ConfigManager.getInstance().setCurrentConfig(ConfigManager.getInstance().getCurrentConfig());
            DraggableManagerSaveHelper.getInstance().save();
            DraggableManagerSaveHelper.getInstance().getDraggables().values().forEach(GpsHelper::lambda$load$0);
         }

         this.flag = var4;
         boolean var5 = false;
         if (var4) {
            for (AutoHelper var7 : DraggableManagerSaveHelper.getInstance().getDraggables().values()) {
               if (var7.isDragging()) {
                  var5 = true;
                  break;
               }
            }
         }

         if (var4 && (var5 || KeyNames.computeGlfw(342))) {
            float var10 = (float)var3.getWindow().getScaledWidth();
            float var2 = (float)var3.getWindow().getScaledHeight();
            float var11 = (float)var3.getWindow().getScaleFactor();
            float var12 = 1.0F / var11;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder var13 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            Matrix4f var14 = event.getMatrixStack().peek().getPositionMatrix();
            int var15 = var3.getWindow().getFramebufferWidth();
            int var16 = var3.getWindow().getFramebufferHeight();
            int var17 = Math.round((float)var15 / 2.0F);
            int var18 = Math.round((float)var16 / 2.0F);
            int var19 = Math.round(20.0F * var11);
            float var20 = 110.0F * var11;
            double var21 = var3.mouse.getX();
            double var23 = var3.mouse.getY();

            int var9;
            for (int var25 = var9 = var17 % var19; var25 <= var15; var25 += var19) {
               float var26 = (float)Math.abs((double)var25 - var21);
               if (!(var26 >= var20)) {
                  float var27 = 1.0F - var26 / var20;
                  int var8 = (int)(60.0F * var27 * var27);
                  if (var8 > 0) {
                     int var33 = (var8 & 0xFF) << 24 | 16777215;
                     float var28 = (float)var25 / var11;
                     var13.vertex(var14, var28, 0.0F, 0.0F).color(var33);
                     var13.vertex(var14, var28, var2, 0.0F).color(var33);
                     var13.vertex(var14, var28 + var12, var2, 0.0F).color(var33);
                     var13.vertex(var14, var28 + var12, 0.0F, 0.0F).color(var33);
                  }
               }
            }

            int var30;
            for (int var40 = var30 = var18 % var19; var40 <= var16; var40 += var19) {
               float var41 = (float)Math.abs((double)var40 - var23);
               if (!(var41 >= var20)) {
                  float var42 = 1.0F - var41 / var20;
                  int var37 = (int)(60.0F * var42 * var42);
                  if (var37 > 0) {
                     int var34 = (var37 & 0xFF) << 24 | 16777215;
                     float var43 = (float)var40 / var11;
                     var13.vertex(var14, 0.0F, var43, 0.0F).color(var34);
                     var13.vertex(var14, 0.0F, var43 + var12, 0.0F).color(var34);
                     var13.vertex(var14, var10, var43 + var12, 0.0F).color(var34);
                     var13.vertex(var14, var10, var43, 0.0F).color(var34);
                  }
               }
            }

            BufferRenderer.drawWithGlobalProgram(var13.end());
            RenderSystem.enableCull();
         }

         for (HudElement var35 : this.list) {
            if (var35.checkGps() || !(var35.getAnimatedValue().getValue() <= 0.01F)) {
               var35.renderColor(event);
            }
         }

         if (var4) {
            float var32 = (float)var3.getWindow().getScaledWidth();
            float var29 = (float)var3.getWindow().getScaledHeight();
            float var36 = (float)(var3.mouse.getX() / var3.getWindow().getScaleFactor());
            float var38 = (float)(var3.mouse.getY() / var3.getWindow().getScaleFactor());
            if (this.activeConfigWidget != null) {
               boolean var39 = GLFW.glfwGetMouseButton(var3.getWindow().getHandle(), 0) == 1;
               if (this.flag3) {
                  if (var39) {
                     this.handleActive(var36);
                  } else {
                     this.flag3 = false;
                     DraggableManagerSaveHelper.getInstance().save();
                  }
               }
            }

            this.animatedValue2.update();
            if (this.activeConfigWidget != null) {
               this.animatedValue2.animateTo(1.0, 150L, Easing.QUINT_OUT);
            } else {
               this.animatedValue2.animateTo(0.0, 150L, Easing.QUINT_OUT);
            }

            if (this.animatedValue2.getValue() > 0.001F) {
               this.initInit(event.getMatrixStack(), var36, var38);
            }
         }
      }
   }

   public static void lambda$load$0(AutoHelper d) {
      d.setDragging(false);
   }
}
