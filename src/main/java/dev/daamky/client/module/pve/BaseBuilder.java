package dev.daamky.client.module.pve;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;

@ModuleInfo(
   name = "BaseBuilder",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Автоматически строит базу"
)
public class BaseBuilder extends Module {
   public static BaseBuilder instance;

   public static BaseBuilder getInstance() {
      if (instance == null) {
         instance = new BaseBuilder();
      }
      return instance;
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void onDisable() {
      super.onDisable();
   }
}
