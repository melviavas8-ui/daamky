package dev.daamky.client.module.combat;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;

@ModuleInfo(
   name = "No Friend Hurt",
   category = ModuleCategory.BATTLE,
   description = "Запрещает бить добавленных друзей"
)
public class NoFriendHurt extends Module {
   public static final NoFriendHurt noFriendHurt = new NoFriendHurt();

   @Override
   public void onEvent() {
   }

   public static NoFriendHurt getInstance() {
      return noFriendHurt;
   }
}
