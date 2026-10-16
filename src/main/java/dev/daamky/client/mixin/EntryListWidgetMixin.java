package dev.daamky.client.mixin;

import java.lang.reflect.Method;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntryListWidget.class})
public abstract class EntryListWidgetMixin {
   @Inject(
      method = {"renderWidget"},
      at = {@At("HEAD")}
   )
   public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      MinecraftClient var7 = MinecraftClient.getInstance();
      if (var7 != null && var7.currentScreen != null) {
         Screen var6 = var7.currentScreen;
         if (var7.currentScreen instanceof MultiplayerScreen
            || var6 instanceof SelectWorldScreen
            || var6 instanceof OptionsScreen
            || var6 instanceof SkinOptionsScreen
            || var6 instanceof VideoOptionsScreen) {
            byte var8 = 40;
            int var9 = var6.height - 100;
            int var10 = var8 + 12;
            int var11 = var9 - 24;

            try {
               Method var12 = null;
               Method var13 = null;

               for (Method var17 : this.getClass().getMethods()) {
                  if (var17.getName().equals("setY") && var17.getParameterCount() == 1) {
                     var12 = var17;
                  }

                  if (var17.getName().equals("setHeight") && var17.getParameterCount() == 1) {
                     var13 = var17;
                  }
               }

               if (var12 != null && var13 != null) {
                  var12.invoke(this, var10);
                  var13.invoke(this, var11);
               }
            } catch (Exception var18) {
               var18.printStackTrace();
            }
         }
      }
   }
}
