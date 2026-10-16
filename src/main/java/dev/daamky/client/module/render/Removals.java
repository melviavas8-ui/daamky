package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.Arrays;

@ModuleInfo(
   name = "Removals",
   category = ModuleCategory.VISUALS,
   description = "Убирает визуальные эффекты (огонь, тотем, тряска)"
)
public class Removals extends Module {
   public static final Removals removals = new Removals();
   public final String[] mweyn = new String[]{
      "Fire indicators", "Hurt camera", "Inwall indicators", "Water indicators", "Scoreboard", "Glow effect", "Bad effects", "Boss bar", "Totem animation"
   };
   public final MultiBooleanSetting remove = new MultiBooleanSetting("Remove")
      .value(Arrays.stream(this.mweyn).map(Removals::lambda$new$0).toArray(Removals::lambda$new$1));

   public Removals() {
      this.addSettings(new Setting[]{this.remove});
   }

   public boolean checkEnabled() {
      return this.isEnabled() && this.remove.isEnabled("Fire indicators");
   }

   public boolean checkEnabled2() {
      return this.isEnabled() && this.remove.isEnabled("Hurt camera");
   }

   public boolean checkEnabled3() {
      return this.isEnabled() && this.remove.isEnabled("Inwall indicators");
   }

   public boolean checkEnabled4() {
      return this.isEnabled() && this.remove.isEnabled("Water indicators");
   }

   public boolean checkEnabled5() {
      return this.isEnabled() && this.remove.isEnabled("Scoreboard");
   }

   public boolean checkEnabled6() {
      return this.isEnabled() && this.remove.isEnabled("Glow effect");
   }

   public boolean checkEnabled7() {
      return this.isEnabled() && this.remove.isEnabled("Bad effects");
   }

   public boolean checkEnabled8() {
      return this.isEnabled() && this.remove.isEnabled("Boss bar");
   }

   public boolean checkEnabled9() {
      return this.isEnabled() && this.remove.isEnabled("Totem animation");
   }

   @Override
   public void onEvent() {
   }

   public static Removals getInstance() {
      return removals;
   }

   public static BooleanSetting[] lambda$new$1(int x$0) {
      return new BooleanSetting[x$0];
   }

   public static BooleanSetting lambda$new$0(String name) {
      return new BooleanSetting(name).value(false);
   }
}
