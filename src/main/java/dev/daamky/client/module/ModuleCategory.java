package dev.daamky.client.module;

public enum ModuleCategory {
   BATTLE("Combat", "0"),
   MOTION("Movement", "1"),
   VISUALS("Render", "2"),
   ACTOR("Player", "3"),
   PVE("PvE", "J"),
   MICS("Other", "4"),
   HUD("HUD", "5");

   public final String label;
   public final String icon;

   public String getLabel() {
      return this.label;
   }

   public String getIcon() {
      return this.icon;
   }

   private ModuleCategory(String label, String icon) {
      this.label = label;
      this.icon = icon;
   }

   public static ModuleCategory[] $values() {
      return new ModuleCategory[]{BATTLE, MOTION, VISUALS, ACTOR, PVE, MICS, HUD};
   }
}
