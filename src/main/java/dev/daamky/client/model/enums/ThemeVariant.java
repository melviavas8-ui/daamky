package dev.daamky.client.model.enums;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.GlyphMap;
import java.awt.Color;

public enum ThemeVariant {
   NIGHT(
      "Blue",
      new Color(9151231).getRGB(),
      new Color(-1726867157, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(9151231).getRGB()
   ),
   AKAR(
      "Red",
      new Color(16747403).getRGB(),
      new Color(-1726280692, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(16747403).getRGB()
   ),
   VIOLKA(
      "Violet",
      new Color(10521599).getRGB(),
      new Color(-1726672860, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(10521599).getRGB()
   ),
   TOXIS(
      "Toxis",
      new Color(9961355).getRGB(),
      new Color(-1726996979, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(12189579).getRGB()
   ),
   White(
      "White",
      new Color(14671839).getRGB(),
      new Color(-1728053248, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(14671839).getRGB()
   ),
   Turquoise(
      "Turquoise",
      new Color(96, 255, 198).getRGB(),
      new Color(-1726797010, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(96, 255, 198).getRGB()
   ),
   Purple(
      "Purple",
      new Color(216, 68, 234).getRGB(),
      new Color(-1725690581, true).getRGB(),
      GlyphMap.computeInt5(240),
      GlyphMap.computeInt5(160),
      GlyphMap.computeColor(24, 24, 27),
      new Color(216, 68, 234).getRGB()
   );

   public final String name;
   public final int client;
   public final int rect;
   public final int text;
   public final int text_dark;
   public final int rect_two;
   public final int text_client_c;
   public Animation animation = new Direction2(300, 1.0);

   private ThemeVariant(String name, int client, int rect, int text, int text_dark, int rect_two, int text_client_c) {
      this.name = name;
      this.client = client;
      this.rect = rect;
      this.text = text;
      this.text_dark = text_dark;
      this.rect_two = rect_two;
      this.text_client_c = text_client_c;
   }

   public String getName() {
      return this.name;
   }

   public int getClient() {
      return this.client;
   }

   public int getRect() {
      return this.rect;
   }

   public int getText() {
      return this.text;
   }

   public int getText_dark() {
      return this.text_dark;
   }

   public int getRect_two() {
      return this.rect_two;
   }

   public int getText_client_c() {
      return this.text_client_c;
   }

   public static ThemeVariant[] $values() {
      return new ThemeVariant[]{NIGHT, AKAR, VIOLKA, TOXIS, White, Turquoise, Purple};
   }
}
