package dev.daamky.client.module.combat;

import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import net.minecraft.entity.Entity;

@ModuleInfo(
   name = "No Entity Trace",
   category = ModuleCategory.BATTLE,
   description = "Позволяет ломать блоки сквозь сущностей"
)
public class NoEntityTrace extends Module {
   public static final NoEntityTrace noEntityTrace = new NoEntityTrace();

   @Override
   public void onEvent() {
   }

   public boolean computeEnabled(Entity entity) {
      boolean var2 = NoFriendHurt.getInstance().isEnabled() && entity != null && FriendsStore.getInstance().computeContains(entity.getName().getString());
      return var2 || this.isEnabled();
   }

   public static NoEntityTrace getInstance() {
      return noEntityTrace;
   }
}
