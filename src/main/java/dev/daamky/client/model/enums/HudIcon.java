package dev.daamky.client.model.enums;

public enum HudIcon {
   COORDS("W"),
   SIGNAL("B"),
   SPEED("V"),
   WLAN("N"),
   PERFORMANCE("Y"),
   QUIT("P"),
   MULTIPLAYER("A"),
   OPTIONS("S"),
   SINGLEPLAYER("U"),
   STEP_B("K"),
   STEP_F("L"),
   PAUSE("O"),
   PLAY("I"),
   FOLDER("F"),
   RIGHTR("R"),
   REFRESH("G"),
   DOCUMENT("D"),
   TRASH("T"),
   CROSS("C");

   public final String letter;

   public static HudIcon find(String name) {
      try {
         return valueOf(name);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public String getLetter() {
      return this.letter;
   }

   private HudIcon(String letter) {
      this.letter = letterx;
   }

   public static HudIcon[] $values() {
      return new HudIcon[]{
         COORDS,
         SIGNAL,
         SPEED,
         WLAN,
         PERFORMANCE,
         QUIT,
         MULTIPLAYER,
         OPTIONS,
         SINGLEPLAYER,
         STEP_B,
         STEP_F,
         PAUSE,
         PLAY,
         FOLDER,
         RIGHTR,
         REFRESH,
         DOCUMENT,
         TRASH,
         CROSS
      };
   }
}
