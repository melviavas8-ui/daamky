package dev.daamky.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.daamky.client.internal.LastSkinHelper;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PlayerListEntry.class})
public class PlayerListEntryMixin {
   @ModifyReturnValue(
      method = {"getSkinTextures"},
      at = {@At("RETURN")}
   )
   private SkinTextures computeEquals(SkinTextures original) {
      Supplier var3 = LastSkinHelper.getSupplier();
      ClientPlayerEntity var4 = MinecraftClient.getInstance().player;
      PlayerListEntry var2;
      if (var4 != null && var3 != null && (var2 = var4.getPlayerListEntry()) != null && var2.equals(this)) {
         original = (SkinTextures)var3.get();
      }

      return original;
   }

   @ModifyExpressionValue(
      method = {"texturesSupplier"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/MinecraftClient;uuidEquals(Ljava/util/UUID;)Z"
      )}
   )
   private static boolean computeFlag(boolean original) {
      return original || LastSkinHelper.getSupplier() != null;
   }
}
