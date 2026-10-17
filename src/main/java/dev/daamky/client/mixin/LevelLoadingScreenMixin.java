package dev.daamky.client.mixin;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.CustomTitleScreen;
import dev.daamky.client.internal.Render;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LevelLoadingScreen.class})
public abstract class LevelLoadingScreenMixin {
   public static final String[] stringList = new String[]{
      "Tip: В киллауре есть продвинутые режимы ротаций для обходов Matrix и Grim.",
      "Tip: Вы можете изменить тему клиента прямо в ClickGUI.",
      "Tip: Используйте HUD редактор, чтобы перемещать элементы на экране.",
      "Tip: Нажмите правый шифт для открытия ClickGUI.",
      "Tip: Client developed with performance in mind."
   };
   public static String string = stringList[new Random().nextInt(stringList.length)];
   public static long timeStamp = System.currentTimeMillis();

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (System.currentTimeMillis() - timeStamp > 5000L) {
         string = stringList[new Random().nextInt(stringList.length)];
         timeStamp = System.currentTimeMillis();
      }

      LevelLoadingScreen var6 = (LevelLoadingScreen)this;
      CustomTitleScreen.handleVal(context.getMatrices(), var6.width, var6.height);
      Render.rectRenderer.handleRgb(context.getMatrices(), 0.0F, 0.0F, (float)var6.width, (float)var6.height, 0.0F, new Color(10, 10, 12, 180));
      FontBootstrap.clientFont10
         .handleColor(
            context.getMatrices(),
            string,
            (float)var6.width / 2.0F - FontBootstrap.clientFont10.computeFloat(string, 8.0F) / 2.0F,
            (float)(var6.height - 40),
            8.0F,
            Color.WHITE
         );
      FontBootstrap.mdoswa
         .handleColor(
            context.getMatrices(),
            "Loading World...",
            (float)var6.width / 2.0F - FontBootstrap.mdoswa.computeFloat("Loading World...", 14.0F) / 2.0F,
            (float)var6.height / 2.0F - 20.0F,
            14.0F,
            Color.WHITE
         );
      ci.cancel();
   }
}
