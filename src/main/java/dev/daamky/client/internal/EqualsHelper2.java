package dev.daamky.client.internal;

import dev.daamky.client.font.FontRegistry;

public class EqualsHelper2 {
   public final float slide;
   public String next = "";
   public String mgikn = "";
   public final ValueToHelper valueToHelper = new ValueToHelper();

   public EqualsHelper2() {
      this(4.0F);
   }

   public EqualsHelper2(float slide) {
      this.slide = slide;
   }

   public void setNext(String next) {
      if (next != null && !next.equals(this.next)) {
         this.mgikn = this.next;
         this.next = next;
         this.valueToHelper.handleAction(0.0);
         this.valueToHelper.computeValueToHelper(1.0, 0.18, ValueHelper.applyFunction23);
      }
   }

   public String getNext() {
      return this.next;
   }

   public float computeNext(FontRegistry font, float size) {
      return font.computeDelegate(this.next, size);
   }

   public void handleNext(FontRegistry font, float x, float y, float size, int color) {
      this.valueToHelper.getFinishAction();
      float var6 = this.valueToHelper.getValue();
      float var7 = x;

      for (int var8 = 0; var8 < this.next.length(); var8++) {
         String var9 = this.next.substring(var8, var8 + 1);
         int var10 = this.mgikn.length() - (this.next.length() - var8);
         String var11 = var10 >= 0 && var10 < this.mgikn.length() ? this.mgikn.substring(var10, var10 + 1) : null;
         if (!(var6 >= 1.0F) && !var9.equals(var11)) {
            font.handleDelegate(var9, var7, y + this.slide * (1.0F - var6), size, GlyphMap.computeRound3(color, var6));
            if (var11 != null) {
               font.handleDelegate(var11, var7, y - this.slide * var6, size, GlyphMap.computeRound3(color, 1.0F - var6));
            }
         } else {
            font.handleDelegate(var9, var7, y, size, color);
         }

         var7 += font.computeDelegate(var9, size);
      }
   }
}
