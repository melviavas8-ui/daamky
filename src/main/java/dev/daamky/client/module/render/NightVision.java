package dev.daamky.client.module.render;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(
   name = "Night Vision",
   category = ModuleCategory.VISUALS,
   description = "Дает бесконечное визуальное ночное зрение"
)
public class NightVision extends Module {
   public static final NightVision nightVision = new NightVision();
   public final FloatSetting brightness = new FloatSetting("Brightness").value(1.0F).range(0.0F, 1.0F).step(0.05F);

   public NightVision() {
      this.addSettings(new Setting[]{this.brightness});
   }

   @Override
   public void onEnable() {
      this.getMc();
   }

   @Override
   public void onDisable() {
      this.getMc();
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public float getEnabled() {
      return !this.isEnabled() ? 1.0F : this.brightness.getValue();
   }

   public float getEnabled2() {
      if (!this.isEnabled()) {
         return 0.0F;
      } else {
         float var1 = this.brightness.getValue();
         return Math.max(0.0F, Math.min(1.0F, var1));
      }
   }

   public void getMc() {
      if (mc.player != null) {
         if (mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
         }
      }
   }

   public static NightVision getInstance() {
      return nightVision;
   }

   public FloatSetting getBrightness() {
      return this.brightness;
   }

   public void lambda$onEvent$0(TickEvent event) {
      if (mc.player != null) {
         this.getMc();
      }
   }
}
