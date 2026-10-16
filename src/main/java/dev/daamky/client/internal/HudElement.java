package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.theme.ThemeManager;
import dev.daamky.client.util.MinecraftAccess;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public abstract class HudElement implements MinecraftAccess, Callback6 {
   public final Easing easing = Easing.SINE_OUT;
   public final long timeStamp = 100L;
   public final AutoHelper autoHelper;
   public boolean enabled;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public Render2DEvent.Render2DEventData currentEvent;
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public boolean renderContent = false;

   public HudElement(float x, float y) {
      this.autoHelper = this.computeAutoHelper(x, y, this.getName());
   }

   public abstract String getName();

   public AutoHelper computeAutoHelper(float x, float y, String name) {
      return DraggableManagerSaveHelper.getInstance().initShow(Interface.getInstance(), name, x, y);
   }

   public final void renderColor(Render2DEvent.Render2DEventData event) {
      this.autoHelper.runLast();
      this.animatedValue.update();
      this.animatedValue.animateTo(this.checkGps() ? 1.0 : 0.0, 150L, this.getEasing());
      float var2 = (float)this.animatedValue.getValue();
      if (!(var2 <= 0.01F)) {
         this.animatedValue2.update();
         this.animatedValue2.animateTo(this.autoHelper.isDragging() ? 1.06 : 1.0, 150L, this.getEasing());
         float var3 = (float)this.animatedValue2.getValue();
         this.currentEvent = event;
         this.renderContent = false;
         MatrixStack var4 = event.getMatrixStack();
         var4.push();
         float var5 = this.autoHelper.getX() + this.autoHelper.getScale2() / 2.0F;
         float var6 = this.autoHelper.getY() + this.autoHelper.getScale3() / 2.0F;
         var4.translate(var5, var6, 0.0F);
         var4.scale(var2, var2, 1.0F);
         var4.translate(-var5, -var6, 0.0F);
         float var7 = this.autoHelper.getScale() * var3;
         var4.translate(this.autoHelper.getX(), this.autoHelper.getY(), 0.0F);
         var4.scale(var7, var7, 1.0F);
         var4.translate(-this.autoHelper.getX(), -this.autoHelper.getY(), 0.0F);
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, var2);
         this.render(var4);
         this.handleVal(var4, var2);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         var4.pop();
      }
   }

   public void runContent() {
      this.renderContent = true;
   }

   public void setRenderContent(boolean renderContent) {
      this.renderContent = renderContentx;
   }

   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   public void handleVal(MatrixStack ms, float animValue) {
   }

   public float computeFloat(float value) {
      return DisplayScale.getInstance().computeScale(value);
   }

   public boolean isVisible() {
      return true;
   }

   public Color initInit() {
      boolean var1 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      if (!this.getAutoHelper().isBlur()) {
         return var1 ? new Color(255, 255, 255, 240) : new Color(12, 14, 18, 240);
      } else {
         return var1 ? new Color(255, 255, 255, 255) : new Color(7, 7, 9, 255);
      }
   }

   public float getScale() {
      return DisplayScale.getInstance().getScale();
   }

   public float getFloat() {
      return this.computeFloat(3.0F);
   }

   public ClientFont getMkadw() {
      return FontBootstrap.mkadw;
   }

   public ClientFont getMheuy() {
      return FontBootstrap.mheuy;
   }

   public ClientFont getValue() {
      return FontBootstrap.clientFont2;
   }

   public ClientFont getValue2() {
      return FontBootstrap.clientFont15;
   }

   public Color getColor() {
      return InterfaceMetrics.computeColor2(255);
   }

   public Color computeColor(int alpha) {
      return InterfaceMetrics.computeColor2(alpha);
   }

   public Easing getEasing() {
      return this.easing;
   }

   public long requireNonNull() {
      return 100L;
   }

   public AutoHelper getAutoHelper() {
      return this.autoHelper;
   }

   public boolean checkGps() {
      if (this instanceof NotificationManager) {
         return true;
      } else {
         try {
            if (Interface.getInstance() != null && Interface.getInstance().gps != null && Interface.getInstance().gps.getValues().contains(this.getName())) {
               return Interface.getInstance().gps.isEnabled(this.getName());
            }
         } catch (Exception var2) {
         }

         return this.enabled;
      }
   }

   public AnimatedValue getAnimatedValue() {
      return this.animatedValue;
   }

   public Render2DEvent.Render2DEventData getCurrentEvent() {
      return this.currentEvent;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabledx;
   }

   public void setCurrentEvent(Render2DEvent.Render2DEventData currentEvent) {
      this.currentEvent = currentEventx;
   }
}
