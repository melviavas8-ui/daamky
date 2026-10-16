package dev.daamky.client.module.render;

import dev.daamky.client.event.EntityColorEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;

@ModuleInfo(
   name = "See Invisibles",
   category = ModuleCategory.VISUALS,
   description = "Позволяет видеть невидимых игроков"
)
public class SeeInvisibles extends Module {
   public static final SeeInvisibles seeInvisibles = new SeeInvisibles();
   public final FloatSetting alpha = new FloatSetting("Alpha").value(0.3F).range(0.0F, 1.0F).step(0.1F);

   public SeeInvisibles() {
      this.addSettings(new Setting[]{this.alpha});
   }

   @Override
   public void onEvent() {
      Subscription var1 = EntityColorEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static SeeInvisibles getInstance() {
      return seeInvisibles;
   }

   public void lambda$onEvent$0(EntityColorEvent.EntityColorEventData event) {
      int var2 = (int)(this.alpha.getValue() * 255.0F);
      event.withColor(ColorMath.computeMin(event.getColor(), var2));
      EntityColorEvent.getInstance().setCancelled(true);
   }
}
