package dev.daamky.client.internal;

public abstract class Widget implements Callback10 {
   public float x;
   public float y;
   public float width;
   public float height;
   public float floatVal = 1.0F;
   public static final DisplayScale displayScale = DisplayScale.getInstance();

   public float getFloat() {
      return displayScale.computeScale(2.0F);
   }

   public float getFloat2() {
      return displayScale.computeScale(3.0F);
   }

   public float computeFloat(float value) {
      return displayScale.computeScale(value);
   }

   public float getScale() {
      return displayScale.getScale();
   }

   public float clamp() {
      return Math.clamp(this.floatVal, 0.0F, 1.0F);
   }

   public void setAlpha(float alpha) {
      this.floatVal = Math.clamp(alpha, 0.0F, 1.0F);
   }

   public void setPercent(float percent) {
      this.floatVal = Math.clamp(percent, 0.0F, 100.0F) / 100.0F;
   }

   public int getInt() {
      return(intVal)(this.clamp() * 255.0F);
   }

   public int computeClamp(float multiplier) {
      return(intVal)Math.clamp(this.clamp() * 255.0F * multiplier, 0.0F, 255.0F);
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }
}
