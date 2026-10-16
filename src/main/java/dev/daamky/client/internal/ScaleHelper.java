package dev.daamky.client.internal;

import net.minecraft.client.util.math.MatrixStack;

public class ScaleHelper {
   public void handleVal(MatrixStack matrixStack, float x, float y, double scale) {
      matrixStack.push();
      matrixStack.translate(x, y, 0.0F);
      matrixStack.scale((float)scale, (float)scale, 1.0F);
      matrixStack.translate(-x, -y, 0.0F);
   }

   public void handleVal2(MatrixStack matrixStack, float x, float y, double scale) {
      matrixStack.push();
      matrixStack.translate(x, y, 0.0F);
      matrixStack.scale(1.0F, (float)scale, 1.0F);
      matrixStack.translate(-x, -y, 0.0F);
   }

   public void handleClass_4587(MatrixStack matrixStack) {
      matrixStack.pop();
   }
}
