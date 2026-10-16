package dev.daamky.client.hud;

import dev.daamky.client.internal.HudElement;
import net.minecraft.client.util.math.MatrixStack;

public class GpsHud extends HudElement {
   public GpsHud() {
      super(10.0F, 150.0F);
   }

   @Override
   public String getName() {
      return "GPS";
   }

   @Override
   public void render(MatrixStack ms) {
   }
}
