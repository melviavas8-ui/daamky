package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.util.MinecraftAccess;

public class DisplayScale implements MinecraftAccess {
   public static final DisplayScale displayScale = new DisplayScale();
   public float scale = 1.0F;
   public float guiScale = 1.0F;
   public final EventListener<Render2DEvent.Render2DEventData> eventListener = new EventListener<>(this::lambda$new$0);

   public void run() {
      Render2DEvent.getInstance().subscribe(this.eventListener);
   }

   public float computeScale(float value) {
      return value * this.scale;
   }

   public float computeGui(float value) {
      return value * this.guiScale;
   }

   public void runScale() {
      if (mc.getWindow() != null) {
         float var1 = (float)mc.getWindow().getScaledWidth();
         float var2 = 683.0F;
         float var3 = (float)mc.getWindow().getScaledHeight();
         float var4 = 384.0F;
         float var5 = Math.max(var1 / 683.0F, var3 / 384.0F);
         if (Math.abs(this.scale - var5) > 0.001F) {
            this.scale = EntityHelper.computeFloat(this.scale, var5, 0.15F);
         }
      }
   }

   public void runGui() {
      if (mc.getWindow() != null) {
         float var1 = (float)mc.getWindow().getScaledWidth();
         float var2 = 683.0F;
         float var3 = (float)mc.getWindow().getScaledHeight();
         float var4 = 384.0F;
         float var5 = Math.max(var1 / 683.0F, var3 / 384.0F) * Interface.getFloat2();
         if (Math.abs(this.guiScale - var5) > 0.001F) {
            this.guiScale = EntityHelper.computeFloat(this.guiScale, var5, 0.15F);
         }
      }
   }

   public float getScale() {
      return this.scale;
   }

   public float getGuiScale() {
      return this.guiScale;
   }

   public EventListener<Render2DEvent.Render2DEventData> getEventListener() {
      return this.eventListener;
   }

   public static DisplayScale getInstance() {
      return displayScale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public void setGuiScale(float guiScale) {
      this.guiScale = guiScale;
   }

   public void lambda$new$0(Render2DEvent.Render2DEventData event) {
      this.runScale();
      this.runGui();
   }
}
