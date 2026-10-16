package dev.daamky.client.module.misc;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.util.Identifier;

@ModuleInfo(
   name = "NoAnnoying",
   category = ModuleCategory.MICS,
   description = "Уменьшает громкость раздражающих звуков"
)
public class NoAnnoying extends Module {
   public static final NoAnnoying noAnnoying = new NoAnnoying();
   public final FloatSetting beaconVolume = new FloatSetting("Beacon Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);
   public final FloatSetting fireworkVolume = new FloatSetting("Firework Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);
   public final FloatSetting thunderVolume = new FloatSetting("Thunder Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);
   public final FloatSetting explosionVolume = new FloatSetting("Explosion Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);
   public final FloatSetting witherVolume = new FloatSetting("Wither Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);
   public final FloatSetting portalVolume = new FloatSetting("Portal Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);
   public final FloatSetting guardianVolume = new FloatSetting("Guardian Volume").value(10.0F).range(0.0F, 100.0F).step(1.0F);

   public NoAnnoying() {
      this.addSettings(
         new Setting[]{
            this.beaconVolume, this.fireworkVolume, this.thunderVolume, this.explosionVolume, this.witherVolume, this.portalVolume, this.guardianVolume
         }
      );
   }

   public static NoAnnoying getInstance() {
      return noAnnoying;
   }

   public float computeContains(Identifier soundId) {
      if (this.isEnabled() && soundId != null) {
         String var2 = soundId.getPath();
         if (var2.contains("beacon")) {
            return this.beaconVolume.getValue() / 100.0F;
         } else if (var2.contains("firework")) {
            return this.fireworkVolume.getValue() / 100.0F;
         } else if (var2.contains("thunder") || var2.contains("lightning")) {
            return this.thunderVolume.getValue() / 100.0F;
         } else if (var2.contains("explode") || var2.contains("explosion")) {
            return this.explosionVolume.getValue() / 100.0F;
         } else if (var2.contains("wither.spawn") || var2.contains("entity.wither.")) {
            return this.witherVolume.getValue() / 100.0F;
         } else if (var2.contains("portal")) {
            return this.portalVolume.getValue() / 100.0F;
         } else {
            return var2.contains("guardian") ? this.guardianVolume.getValue() / 100.0F : 1.0F;
         }
      } else {
         return 1.0F;
      }
   }

   @Override
   public void onEvent() {
   }
}
