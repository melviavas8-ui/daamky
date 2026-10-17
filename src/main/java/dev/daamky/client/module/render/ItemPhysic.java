package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "ItemPhysic",
   category = ModuleCategory.VISUALS,
   description = "Дропнутые предметы лежат плашмя на земле"
)
public class ItemPhysic extends Module {
   public static final ItemPhysic itemPhysic = new ItemPhysic();
   public final FloatSetting height = new FloatSetting("Height").value(0.03F).range(0.0F, 0.5F).step(0.01F);
   public final BooleanSetting rotate = new BooleanSetting("Rotate").value(false);
   public final FloatSetting spinSpeed = new FloatSetting("Spin speed").value(30.0F).range(5.0F, 120.0F).step(1.0F).visibleWhen(this.rotate::getValue);

   public ItemPhysic() {
      this.addSettings(new Setting[]{this.height, this.rotate, this.spinSpeed});
   }

   @Override
   public void onEvent() {
   }

   public float getFloat() {
      return this.height.getValue();
   }

   public boolean checkRotate() {
      return this.rotate.getValue();
   }

   public float getCurrent() {
      if (!this.rotate.getValue()) {
         return 0.0F;
      } else {
         double var1 = (double)System.currentTimeMillis() / 1000.0 * (double)this.spinSpeed.getValue().floatValue();
         return (float)(var1 % 360.0);
      }
   }

   public static ItemPhysic getInstance() {
      return itemPhysic;
   }
}
