package dev.daamky.client.module.render;

import dev.daamky.client.event.CameraEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "Smooth Camera",
   category = ModuleCategory.VISUALS,
   description = "Плавное следование камеры за поворотами игрока"
)
public class SmoothCamera extends Module {
   public static final SmoothCamera smoothCamera = new SmoothCamera();
   public final FloatSetting smooth = new FloatSetting("Smooth").value(0.1F).range(0.01F, 0.5F).step(0.01F);
   public float floatVal;
   public float mmoobir;
   public boolean flag = false;

   public SmoothCamera() {
      this.addSettings(new Setting[]{this.smooth});
   }

   public static SmoothCamera getInstance() {
      return smoothCamera;
   }

   @Override
   public void onEnable() {
      if (mc.player != null) {
         this.floatVal = mc.player.getYaw();
         this.mmoobir = mc.player.getPitch();
         this.flag = true;
      } else {
         this.flag = false;
      }
   }

   @Override
   public void onDisable() {
      this.flag = false;
   }

   @Override
   public void onEvent() {
      Subscription var1 = CameraEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void handleTickEvent(TickEvent event) {
      if (mc.player != null) {
         if (!this.flag) {
            this.floatVal = mc.player.getYaw();
            this.mmoobir = mc.player.getPitch();
            this.flag = true;
         }
      }
   }

   public void handleYaw(CameraEvent event) {
      if (mc.player != null && this.flag) {
         float var2 = event.getYaw();
         float var3 = event.getPitch();
         float var4 = MathHelper.clamp(this.smooth.getValue(), 0.01F, 1.0F);
         float var5 = MathHelper.wrapDegrees(var2 - this.floatVal);
         this.floatVal += var5 * var4;
         this.mmoobir = this.mmoobir + (var3 - this.mmoobir) * var4;
         this.mmoobir = MathHelper.clamp(this.mmoobir, -90.0F, 90.0F);
         event.setYaw(this.floatVal);
         event.setPitch(this.mmoobir);
      }
   }

   public void lambda$onEvent$1(TickEvent event) {
      this.handleTickEvent(event);
   }

   public void lambda$onEvent$0(CameraEvent event) {
      this.handleYaw(event);
   }
}
