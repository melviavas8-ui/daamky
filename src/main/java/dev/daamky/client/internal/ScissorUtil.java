package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.util.math.MatrixStack;

public final class ScissorUtil implements MinecraftAccess {
   public static void handleEnable(MatrixStack matrixStack, float x, float y, float width, float height) {
      float var5 = (float)mc.getWindow().getScaleFactor();
      float var6 = (float)mc.getWindow().getScaledHeight() - y;
      float var7 = x * var5;
      float var8 = var6 * var5;
      float var9 = width * var5;
      float var10 = height * var5;
      RenderSystem.enableScissor((int)var7, (int)(var8 - var10), (int)var9, (int)var10);
   }

   public static void handleDisable(MatrixStack matrixStack) {
      RenderSystem.disableScissor();
   }

   public ScissorUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
