package dev.daamky.client.internal;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class InvokeHelper {
   public final MinecraftClient mkaitw = MinecraftClient.getInstance();
   public float floatVal;
   public float float2;
   public float max;
   public float float3 = 8.0F;
   public boolean enabled;
   public double doubleVal;
   public float float4;

   public InvokeHelper() {
      this.setEnabled(true);
      GLFW.glfwSetScrollCallback(this.mkaitw.getWindow().getHandle(), this::lambda$new$0);
   }

   public void runMax() {
      float var1 = (float)this.doubleVal * this.float3 * 10.0F;
      float var2 = 0.0F;
      this.float2 = this.computeFloat(this.float2, this.floatVal, (double)(this.float3 / 100.0F));
      if (!this.enabled) {
         this.doubleVal = 0.0;
      } else {
         this.floatVal = Math.min(Math.max(this.floatVal + var1 / 2.0F, this.max - (var1 == 0.0F ? 0.0F : var2)), var1 == 0.0F ? 0.0F : var2);
         this.doubleVal = 0.0;
      }
   }

   public void handleMax(float x, float y, float width, float height, float alpha) {
      if (this.max != 0.0F) {
         float var6 = this.getFloat2() / this.getMax();
         this.float4 = this.computeFloat(height - this.getMax() / (this.getMax() - height) * height, this.float4, 0.9F);
         boolean var7 = this.float4 < height;
         if (var7) {
            float var9 = y + height * var6 - this.float4 * var6;
         }
      }
   }

   public void run() {
      this.float2 = 0.0F;
      this.floatVal = 0.0F;
   }

   public void handleMax(float max, float height) {
      this.max = -max + height;
   }

   public float computeFloat(float a, float b, double f) {
      return (float)((double)a + f * (double)(b - a));
   }

   public float computeMax(float val, float min, float max) {
      return Math.max(min, Math.min(max, val));
   }

   public float getFloat2() {
      return this.float2;
   }

   public float getMax() {
      return this.max;
   }

   public void setMax(float max) {
      this.max = max;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public void lambda$new$0(long window, double xOffset, double yOffset) {
      this.doubleVal += yOffset;
   }
}
