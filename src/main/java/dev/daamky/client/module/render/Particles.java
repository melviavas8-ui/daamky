package dev.daamky.client.module.render;

import dev.daamky.client.config.Configurable;
import dev.daamky.client.internal.Hit;
import dev.daamky.client.internal.SparkHelper;
import dev.daamky.client.internal.World;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "Particles",
   category = ModuleCategory.VISUALS,
   description = "Particles"
)
public class Particles extends Module {
   public static final Particles particles = new Particles();
   public final Hit hit2 = new Hit();
   public final World world2 = new World();
   public final BooleanSetting world = new BooleanSetting("World").value(true).onAction(this::lambda$new$0);
   public final BooleanSetting hit = new BooleanSetting("Hit").value(true).onAction(this::lambda$new$1);
   public final MultiBooleanSetting spawn = new MultiBooleanSetting("Spawn").value(this.world, this.hit);

   @Override
   public void onDisable() {
      super.onDisable();
      this.hit2.runClear();
      this.world2.runClear();
      this.hit2.clearEventListeners();
      this.removeEventListeners(this.hit2.getEventListeners());
      this.world2.clearEventListeners();
      this.removeEventListeners(this.world2.getEventListeners());
   }

   public Particles() {
      this.addSettings(new Setting[]{this.spawn});

      for (Setting var2 : this.world2.getSettings()) {
         var2.visibleWhen(this::lambda$new$2);
      }

      this.addSettings(this.world2.getSettings());

      for (Setting var4 : this.hit2.getSettings()) {
         var4.visibleWhen(this::lambda$new$3);
      }

      this.addSettings(this.hit2.getSettings());
   }

   @Override
   public void onEvent() {
      if (this.hit.getValue()) {
         this.hit2.onEvent();
         this.addEventListeners(this.hit2.getEventListeners());
      }

      if (this.world.getValue()) {
         this.world2.onEvent();
         this.addEventListeners(this.world2.getEventListeners());
      }
   }

   public static Particles getInstance() {
      return particles;
   }

   public BooleanSetting getWorld() {
      return this.world;
   }

   public BooleanSetting getHit() {
      return this.hit;
   }

   public Boolean lambda$new$3() {
      return this.spawn.isEnabled(this.hit.getName());
   }

   public Boolean lambda$new$2() {
      return this.spawn.isEnabled(this.world.getName());
   }

   public void lambda$new$1() {
      if (!this.getHit().getValue()) {
         this.hit2.runClear();
         this.hit2.clearEventListeners();
         this.removeEventListeners(this.hit2.getEventListeners());
      } else if (this.isEnabled()) {
         this.hit2.onEvent();
         this.addEventListeners(this.hit2.getEventListeners());
      }
   }

   public void lambda$new$0() {
      if (!this.getWorld().getValue()) {
         this.world2.runClear();
         this.world2.clearEventListeners();
         this.removeEventListeners(this.world2.getEventListeners());
      } else if (this.isEnabled()) {
         this.world2.onEvent();
         this.addEventListeners(this.world2.getEventListeners());
      }
   }

   public static class BaseSettings extends Configurable {
      public final ModeSetting sparkSetting;
      public final FloatSetting spark;
      public final FloatSetting floatSetting;
      public final FloatSetting floatSetting2;
      public final FloatSetting floatSetting3;
      public final FloatSetting floatSetting4;
      public final BooleanSetting booleanSetting;
      public final FloatSetting floatSetting5;
      public final String string;

      public BaseSettings(String prefix) {
         this.string = prefix + ": ";
         this.sparkSetting = new ModeSetting(this.string + "Texture").value("Spark").values(SparkHelper.stringList);
         this.spark = new FloatSetting(this.string + "Size").value(0.2F).range(0.1F, 0.4F).step(0.05F);
         this.floatSetting = new FloatSetting(this.string + "Life time").value(8.0F).range(2.0F, 50.0F).step(1.0F);
         this.floatSetting2 = new FloatSetting(this.string + "Spawn duration").value(10.0F).range(0.0F, 30.0F).step(1.0F);
         this.floatSetting3 = new FloatSetting(this.string + "Dying duration").value(10.0F).range(0.0F, 30.0F).step(1.0F);
         this.floatSetting4 = new FloatSetting(this.string + "Count").value(10.0F).range(5.0F, 50.0F).step(1.0F);
         this.booleanSetting = new BooleanSetting(this.string + "Rotate").value(false);
         this.floatSetting5 = new FloatSetting(this.string + "Spawn interval").value(50.0F).range(10.0F, 500.0F).step(10.0F);
         this.addSettings(
            new Setting[]{
               this.sparkSetting,
               this.spark,
               this.floatSetting,
               this.floatSetting2,
               this.floatSetting3,
               this.floatSetting4,
               this.booleanSetting,
               this.floatSetting5
            }
         );
      }

      public ModeSetting getSparkSetting() {
         return this.sparkSetting;
      }

      public FloatSetting getSpark() {
         return this.spark;
      }

      public FloatSetting getFloatSetting() {
         return this.floatSetting;
      }

      public FloatSetting getFloatSetting2() {
         return this.floatSetting2;
      }

      public FloatSetting getFloatSetting3() {
         return this.floatSetting3;
      }

      public FloatSetting getFloatSetting4() {
         return this.floatSetting4;
      }

      public BooleanSetting getBooleanSetting() {
         return this.booleanSetting;
      }

      public FloatSetting getFloatSetting5() {
         return this.floatSetting5;
      }

      public String getString() {
         return this.string;
      }
   }
}
