package dev.daamky.client.mixin;

import dev.daamky.client.gui.CustomTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public class TitleScreenMixin extends Screen {
   protected TitleScreenMixin(Text title) {
      super(title);
   }

   @Inject(
      method = {"init"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onInit(CallbackInfo ci) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null) {
         var2.setScreen(new CustomTitleScreen());
      }

      ci.cancel();
   }
}
