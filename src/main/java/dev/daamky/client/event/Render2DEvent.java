package dev.daamky.client.event;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Render2DEvent extends Event<Render2DEvent.Render2DEventData> {
   public static final Render2DEvent render2DEvent = new Render2DEvent();
   public static final Render2DEvent.Render2DEventData render2DEventData = new Render2DEvent.Render2DEventData(null, null, 0.0F);

   public static Render2DEvent getInstance() {
      return render2DEvent;
   }

   public static Render2DEvent.Render2DEventData getRender2DEventData() {
      return render2DEventData;
   }

   public static class Render2DEventData {
      public DrawContext context;
      public MatrixStack matrixStack;
      public float partialTicks;

      public Render2DEventData(DrawContext context, MatrixStack matrixStack, float partialTicks) {
         this.context = contextx;
         this.matrixStack = matrixStackx;
         this.partialTicks = partialTicksx;
      }

      public DrawContext getContext() {
         return this.context;
      }

      public MatrixStack getMatrixStack() {
         return this.matrixStack;
      }

      public float getPartialTicks() {
         return this.partialTicks;
      }
   }
}
