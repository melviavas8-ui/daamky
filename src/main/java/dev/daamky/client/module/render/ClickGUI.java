package dev.daamky.client.module.render;

import dev.daamky.client.internal.EnabledSet;
import dev.daamky.client.internal.FactoryHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;

@ModuleInfo(
   name = "Click GUI",
   category = ModuleCategory.VISUALS,
   bind = 344,
   description = "Меню клиента"
)
public class ClickGUI extends Module {
   public static final ClickGUI clickGUI = new ClickGUI();

   @Override
   public void onEnable() {
      if (EnabledSet.getInstance().check()) {
         this.setEnabledInternal(false, false);
      } else {
         if (mc != null && mc.currentScreen == null) {
            mc.setScreen(FactoryHelper.getInstance().getMainMenuScreen());
         }

         this.setEnabledInternal(false, false);
      }
   }

   @Override
   public void onEvent() {
   }

   public static ClickGUI getInstance() {
      return clickGUI;
   }
}
