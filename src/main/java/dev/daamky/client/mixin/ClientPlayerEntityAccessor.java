package dev.daamky.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientPlayerEntity.class})
public interface ClientPlayerEntityAccessor {
   @Accessor("lastSprinting")
   boolean getLastSprinting();

   @Accessor("lastSprinting")
   void setLastSprinting(boolean var1);

   @Accessor("lastSneaking")
   boolean getLastSneaking();

   @Accessor("lastSneaking")
   void setLastSneaking(boolean var1);
}
