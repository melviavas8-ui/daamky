package dev.daamky.client.model;

import org.jetbrains.annotations.NotNull;

public record RecordData2(float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
   public static final RecordData2 ZERO = new RecordData2(0.0F, 0.0F, 0.0F, 0.0F);

   public static RecordData2 all(float radius) {
      return new RecordData2(radius, radius, radius, radius);
   }

   public static RecordData2 topLeft(float radius) {
      return new RecordData2(radius, 0.0F, 0.0F, 0.0F);
   }

   public static RecordData2 topRight(float radius) {
      return new RecordData2(0.0F, radius, 0.0F, 0.0F);
   }

   public static RecordData2 bottomRight(float radius) {
      return new RecordData2(0.0F, 0.0F, radius, 0.0F);
   }

   public static RecordData2 bottomLeft(float radius) {
      return new RecordData2(0.0F, 0.0F, 0.0F, radius);
   }

   public static RecordData2 top(float leftRadius, float rightRadius) {
      return new RecordData2(leftRadius, rightRadius, 0.0F, 0.0F);
   }

   public static RecordData2 bottom(float leftRadius, float rightRadius) {
      return new RecordData2(0.0F, 0.0F, rightRadius, leftRadius);
   }

   public static RecordData2 left(float topRadius, float bottomRadius) {
      return new RecordData2(topRadius, 0.0F, 0.0F, bottomRadius);
   }

   public static RecordData2 right(float topRadius, float bottomRadius) {
      return new RecordData2(0.0F, topRadius, bottomRadius, 0.0F);
   }

   @NotNull
   @Override
   public String toString() {
      return "BorderRadius{topLeftRadius="
         + this.topLeftRadius
         + ", topRightRadius="
         + this.topRightRadius
         + ", bottomRightRadius="
         + this.bottomRightRadius
         + ", bottomLeftRadius="
         + this.bottomLeftRadius
         + "}";
   }
}
