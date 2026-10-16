package dev.daamky.client.module.render;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;

@ModuleInfo(
   name = "Cape",
   category = ModuleCategory.VISUALS,
   description = "Отображает кастомный плащ на вашем персонаже"
)
public class Cape extends Module {
   public static final Cape cape = new Cape();

   @Override
   public void onEvent() {
   }

   public static Cape getInstance() {
      return cape;
   }
}
