package dev.daamky.client.internal;

import dev.daamky.client.event.EventHandler;
import dev.daamky.client.gui.component.CategoryComponent;
import dev.daamky.client.gui.component.CheckComponent;
import dev.daamky.client.gui.component.SliderComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.legacy.module.ModuleCategory;
import dev.daamky.client.legacy.module.ModuleInfo;

@ModuleInfo(
   name = "Click Gui",
   category = ModuleCategory.RENDER,
   key = 344,
   desc = "Позволяет настроить вид GUI клиента",
   autoEnabled = true,
   allowDisable = false
)
public class LegacyModule2 extends Module {
   public CategoryComponent categoryComponent = new CategoryComponent(
      this,
      "Эффекты",
      new CheckComponent("Серый фон", false),
      new CheckComponent("Затемнять фон", true),
      new CheckComponent("Размывать фон", true),
      new CheckComponent("Шейдер", false),
      new CheckComponent("Частицы", true),
      new CheckComponent("Скан линии", true),
      new CheckComponent("Свечение", true),
      new CheckComponent("Точки", true)
   );
   public SliderComponent mvodq = new SliderComponent(this, "Размер", 1.0F, 0.5F, 1.5F, 0.1F);

   @EventHandler
   public void handleKey(KeyEntry event) {
      if (event.getKey() == this.getKey()) {
         mc.setScreen(FactoryHelper.factoryHelper.getMainMenuScreen());
      }
   }
}
