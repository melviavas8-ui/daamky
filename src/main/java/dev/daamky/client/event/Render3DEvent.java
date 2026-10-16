package dev.daamky.client.event;

import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event<Render3DEvent.Render3DEventData> {
   public static final Render3DEvent render3DEvent = new Render3DEvent();
   public static final Render3DEvent.Render3DEventData mbenap = new Render3DEvent.Render3DEventData(null, 0.0F);

   public static Render3DEvent getInstance() {
      return render3DEvent;
   }

   public static Render3DEvent.Render3DEventData getMbenap() {
      return mbenap;
   }

   public static class Render3DEventData {
      public MatrixStack matrixStack;
      public float partialTicks;

      public Render3DEventData(MatrixStack matrixStack, float partialTicks) {
         this.matrixStack = matrixStackx;
         this.partialTicks = partialTicksx;
      }

      public MatrixStack getMatrixStack() {
         return this.matrixStack;
      }

      public float getPartialTicks() {
         return this.partialTicks;
      }
   }
}
