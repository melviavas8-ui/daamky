package dev.daamky.client.internal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorTheme {
   public final String name;
   public final List<ColorTheme.ElementColor> list = new ArrayList<>();

   public ColorTheme(String name) {
      this.name = namex;
      this.list.add(new ColorTheme.ElementColor("Primary", new Color(255, 255, 255)));
      this.list.add(new ColorTheme.ElementColor("Secondary", new Color(200, 200, 200)));
      this.list.add(new ColorTheme.ElementColor("Blur", new Color(10, 10, 10)));
      this.list.add(new ColorTheme.ElementColor("Widget blur", new Color(5, 5, 5, 200)));
      this.list.add(new ColorTheme.ElementColor("Background blur", new Color(5, 5, 5, 200)));
      this.list.add(new ColorTheme.ElementColor("Text", new Color(255, 255, 255, 255)));
      this.list.add(new ColorTheme.ElementColor("Inactive text", new Color(102, 102, 102)));
      this.list.add(new ColorTheme.ElementColor("Knob", new Color(255, 255, 255)));
      this.list.add(new ColorTheme.ElementColor("Inactive knob", new Color(85, 85, 85)));
      this.list.add(new ColorTheme.ElementColor("Positive", new Color(130, 255, 130)));
      this.list.add(new ColorTheme.ElementColor("Middle", new Color(255, 200, 95)));
      this.list.add(new ColorTheme.ElementColor("Negative", new Color(255, 80, 80)));
   }

   public Color getPrimary() {
      return this.initColor("Primary");
   }

   public Color getSecondary() {
      return this.initColor("Secondary");
   }

   public Color getBlur() {
      return this.initColor("Blur");
   }

   public Color getBlur2() {
      return this.initColor("Widget blur");
   }

   public Color getBackground() {
      return this.initColor("Background blur");
   }

   public Color getText() {
      return this.initColor("Text");
   }

   public Color getInactive() {
      return this.initColor("Inactive text");
   }

   public Color getKnob() {
      return this.initColor("Knob");
   }

   public Color getInactive2() {
      return this.initColor("Inactive knob");
   }

   public Color getPositive() {
      return this.initColor("Positive");
   }

   public Color getColor() {
      return this.initColor("Middle");
   }

   public Color getNegative() {
      return this.initColor("Negative");
   }

   public Color initColor(String elementName) {
      ColorTheme.ElementColor var2 = this.computeNext(elementName);
      return var2 != null ? var2.getColor() : new Color(-1);
   }

   public ColorTheme.ElementColor computeNext(String elementName) {
      for (ColorTheme.ElementColor var3 : this.list) {
         if (var3.getName().equalsIgnoreCase(elementName)) {
            return var3;
         }
      }

      return null;
   }

   public String getName() {
      return this.name;
   }

   public List<ColorTheme.ElementColor> getList() {
      return this.list;
   }

   public static class ElementColor {
      public final String name;
      public Color color;
      public final ExpandableSettingComponent expandableSettingComponent;

      public ElementColor(String name, Color color) {
         this.name = namex;
         this.color = colorx;
         this.expandableSettingComponent = new ExpandableSettingComponent(this);
      }

      public ExpandableSettingComponent getExpandableSettingComponent() {
         return this.expandableSettingComponent;
      }

      public void setColor(Color color) {
         this.color = colorx;
      }
   }
}
