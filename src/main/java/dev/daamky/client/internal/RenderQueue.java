package dev.daamky.client.internal;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

public class RenderQueue {
   public static final MinecraftClient class_310 = net.minecraft.client.MinecraftClient.getInstance();
   public static final Deque<int[]> deque = new ArrayDeque<>();

   public static void handleMax(float x, float y, float width, float height, float guiScale) {
      int var5 = class_310.getWindow().getHeight();
      int var6 = (int)(x * guiScale);
      int var7 = (int)((float)var5 - (y + height) * guiScale);
      int var8 = (int)(width * guiScale);
      int var9 = (int)(height * guiScale);
      if (!deque.isEmpty()) {
         int[] var10 = deque.peek();
         int var11 = var10[0];
         int var12 = var10[1];
         int var13 = var11 + var10[2];
         int var14 = var12 + var10[3];
         int var15 = var6 + var8;
         int var16 = var7 + var9;
         var6 = Math.max(var6, var11);
         var7 = Math.max(var7, var12);
         var15 = Math.min(var15, var13);
         var16 = Math.min(var16, var14);
         var8 = Math.max(0, var15 - var6);
         var9 = Math.max(0, var16 - var7);
      }

      deque.push(new int[]{var6, var7, var8, var9});
      GL11.glEnable(3089);
      GL11.glScissor(var6, var7, var8, var9);
   }

   public static void handleFloat(float x, float y, float width, float height) {
      int var4 = class_310.getWindow().calculateScaleFactor((Integer)class_310.options.getGuiScale().getValue(), class_310.forcesUnicodeFont());
      handleMax(x, y, width, height, (float)var4);
   }

   public static void runGl() {
      if (!deque.isEmpty()) {
         deque.pop();
      }

      if (deque.isEmpty()) {
         GL11.glDisable(3089);
      } else {
         int[] var0 = deque.peek();
         GL11.glScissor(var0[0], var0[1], var0[2], var0[3]);
      }
   }

   public static void runClear() {
      deque.clear();
      GL11.glDisable(3089);
   }
}
