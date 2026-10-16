package dev.daamky.client.mixin;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.CustomTitleScreen;
import dev.daamky.client.internal.Render;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ConnectScreen.class})
public abstract class ConnectScreenMixin {
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
      if (System.currentTimeMillis() - longVal > 5000L) {
         string = stringList[new Random().nextInt(stringList.length)];
         longVal = System.currentTimeMillis();
      }

      ConnectScreen var6 = (ConnectScreen)this;
      CustomTitleScreen.handleVal(context.getMatrices(), var6.width, var6.height);
      Render.rectRenderer.handleRgb(context.getMatrices(), 0.0F, 0.0F, (float)var6.width, (float)var6.height, 0.0F, new Color(10, 10, 12, 180));
      long var7 = System.currentTimeMillis() % 2000L;
      float var9 = (float)var7 / 2000.0F * 360.0F;
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
            "Connecting...",
            (float)var6.width / 2.0F - FontBootstrap.mdoswa.computeFloat("Connecting...", 14.0F) / 2.0F,
            (float)var6.height / 2.0F - 20.0F,
            14.0F,
            Color.WHITE
         );
      ci.cancel();
   }
}
