package dev.daamky.client.mixin;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Camera.class})
public interface CameraAccessor {
   @Invoker("setRotation")
   void setCustomRotation(float var1, float var2);

   @Invoker("setPos")
   void setCustomPos(double var1, double var3, double var5);

   @Invoker("clipToSpace")
   float setClipToSpace(float var1);
}
