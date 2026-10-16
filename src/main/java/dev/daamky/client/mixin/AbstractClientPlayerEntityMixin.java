package dev.daamky.client.mixin;

import dev.daamky.client.module.render.Cape;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractClientPlayerEntity.class})
public class AbstractClientPlayerEntityMixin {
   private static final Identifier class_2960 = net.minecraft.util.Identifier.of("daamky", "textures/cape.png");
   @Unique
   private SkinTextures daamky$cachedSkinWithCape;
   @Unique
   private SkinTextures daamky$lastOriginalSkin;

   @Inject(
      method = {"getSkinTextures"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void injectGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
      AbstractClientPlayerEntity var3 = (AbstractClientPlayerEntity)this;
      SkinTextures var2;
      if (var3 == MinecraftClient.getInstance().player && Cape.getInstance().isEnabled() && (var2 = (SkinTextures)cir.getReturnValue()) != null) {
         if (this.daamky$cachedSkinWithCape == null || var2 != this.daamky$lastOriginalSkin) {
            this.daamky$lastOriginalSkin = var2;
            this.daamky$cachedSkinWithCape = new SkinTextures(var2.texture(), var2.textureUrl(), class_2960, class_2960, var2.model(), var2.secure());
         }

         cir.setReturnValue(this.daamky$cachedSkinWithCape);
      }
   }
}
