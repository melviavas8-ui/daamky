package dev.daamky.client.internal;

import dev.daamky.client.event.CameraEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.MouseDeltaEvent;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.util.math.MathHelper;

public class CameraManager implements MinecraftAccess {
   public static final CameraManager cameraManager = new CameraManager();
   public static boolean flag;
   public static float floatVal;
   public static float mrula;

   public void load() {
      MouseDeltaEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$0));
      CameraEvent.getInstance().subscribe(new EventListener<>(this::lambda$load$1));
      if (mc.player != null) {
         floatVal = mc.player.getYaw();
         mrula = mc.player.getPitch();
      }
   }

   public void handleCancelled(MouseDeltaEvent event) {
      if (flag) {
         this.handleDouble(event.getYaw(), event.getPitch());
         event.setCancelled(true);
      }
   }

   public void handleYaw(CameraEvent event) {
      if (flag) {
         event.setYaw(floatVal);
         event.setPitch(mrula);
      } else {
         floatVal = event.getYaw();
         mrula = event.getPitch();
      }
   }

   public static boolean getFlag() {
      return flag;
   }

   public static void setState(boolean state) {
      if (flag != state) {
         flag = state;
         run();
      }
   }

   public static void run() {
      if (mc.player != null) {
         float var0 = mc.player.getYaw();
         float var1 = floatVal;
         mc.player.setYaw(var0 + MathHelper.wrapDegrees(var1 - var0));
         mc.player.setPitch(mrula);
      }
   }

   public void handleDouble(double targetYaw, double targetPitch) {
      mrula = MathHelper.clamp((float)((double)mrula + targetPitch * 0.15000001238751678), -90.0F, 90.0F);
      floatVal = (float)((double)floatVal + targetYaw * 0.15000001238751678);
   }

   public static float getFloat() {
      return floatVal;
   }

   public static float getMrula() {
      return mrula;
   }

   public static void setFreeYaw(float freeYaw) {
      floatVal = freeYaw;
   }

   public static void setFreePitch(float freePitch) {
      mrula = freePitch;
   }

   public void lambda$load$1(CameraEvent event) {
      this.handleYaw(event);
   }

   public void lambda$load$0(MouseDeltaEvent event) {
      this.handleCancelled(event);
   }
}
