package dev.daamky.client.internal;

import java.awt.Color;

public abstract class ElementColor extends ColorTheme {
   public ElementColor(String name) {
      super(name);
   }

   public abstract Color initInit();

   public abstract Color initInit2();

   public abstract Color initInit3();

   public abstract Color initInit4();

   public abstract Color initInit5();

   public abstract Color initInit6();

   public abstract Color initInit7();

   public abstract Color initInit8();

   public abstract Color initInit9();

   public dev.daamky.client.internal.ElementColor getElementColor() {
      for (ColorTheme.ElementColor var2 : this.getList()) {
         String var3 = var2.getName();
         switch (var3) {
            case "Primary":
               var2.setColor(this.initInit());
               break;
            case "Secondary":
               var2.setColor(this.initInit2());
               break;
            case "Blur":
               var2.setColor(this.initInit3());
               break;
            case "Widget blur":
               var2.setColor(this.initInit4());
               break;
            case "Background blur":
               var2.setColor(this.initInit5());
               break;
            case "Text":
               var2.setColor(this.initInit6());
               break;
            case "Inactive text":
               var2.setColor(this.initInit7());
               break;
            case "Knob":
               var2.setColor(this.initInit8());
               break;
            case "Inactive knob":
               var2.setColor(this.initInit9());
         }
      }

      return this;
   }
}
