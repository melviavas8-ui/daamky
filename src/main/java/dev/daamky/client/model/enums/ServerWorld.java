package dev.daamky.client.model.enums;

import dev.daamky.client.setting.ModeSetting;

public enum ServerWorld implements ModeSetting.NamedChoice {
   FUN_TIME("Fun Time"),
   SPOOKY_TIME("Spooky Time"),
   HOLY_WORLD("Holy World"),
   REALLY_WORLD("Really World");

   public final String name;

   @Override
   public String getName() {
      return this.name;
   }

   private ServerWorld(String name) {
      this.name = namex;
   }

   public static ServerWorld[] $values() {
      return new ServerWorld[]{FUN_TIME, SPOOKY_TIME, HOLY_WORLD, REALLY_WORLD};
   }
}
