package dev.daamky.client.legacy.module;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.ValueToHelper;

public enum ModuleCategory {
   COMBAT("Combat", "0"),
   MOVEMENT("Movement", "1"),
   RENDER("Render", "2"),
   PLAYER("Player", "3"),
   PVE("PvE", "J"),
   OTHER("Misc", "4");

   public final String name;
   public final String icon;
   public Animation alphaS = new Direction2(300, 1.0);
   public Animation alphaS2 = new Direction2(300, 1.0);
   public ValueToHelper animation = new ValueToHelper();

      public String getName() {
      return this.name;
   }

      public String getIcon() {
      return this.icon;
   }

      public Animation getAlphaS() {
      return this.alphaS;
   }

      public Animation getAlphaS2() {
      return this.alphaS2;
   }

      public ValueToHelper getAnimation() {
      return this.animation;
   }

      private ModuleCategory(final String name, final String icon) {
      this.name = namex;
      this.icon = iconx;
   }

   public static ModuleCategory[] $values() {
      return new ModuleCategory[]{COMBAT, MOVEMENT, RENDER, PLAYER, PVE, OTHER};
   }
}
