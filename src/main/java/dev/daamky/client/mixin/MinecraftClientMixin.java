package dev.daamky.client.mixin;

import dev.daamky.client.DaamkyClient;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.OsNameHelper;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.module.misc.Optimization;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MinecraftClient.class})
public class MinecraftClientMixin {
   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void gameLoopHook(boolean tick, CallbackInfo ci) {
      ((MinecraftClient)this).getWindow().setPhase("daamky cracked by bludnov [ https://t.me/secretdevelopmentpizza ]");
      MinecraftClient var3 = (MinecraftClient)this;
      if (var3.player != null) {
         VecRotationHelper.getInstance().initRotation();
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void preTickHook(CallbackInfo ci) {
      MinecraftClient var2 = (MinecraftClient)this;
      if (var2.player != null) {
         TickEvent.getInstance().postAndCheckCancelled();
      }
   }

   @Inject(
      method = {"close"},
      at = {@At("HEAD")}
   )
   public void closeHook(CallbackInfo ci) {
      DaamkyClient.getInstance().saveAutoConfig();
   }

   @Inject(
      method = {"setScreen"},
      at = {@At("HEAD")}
   )
   public void onSetScreen(Screen screen, CallbackInfo ci) {
      if (Optimization.getInstance().checkEnabled11()) {
         Optimization.cleanRam();
      }
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   public void initHook(RunArgs args, CallbackInfo ci) {
      OsNameHelper.initWindow();
      DaamkyClient.getInstance().postLoad();
   }

   @Inject(
      method = {"getWindowTitle"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void overrideCreateTitle(CallbackInfoReturnable<String> cir) {
      cir.setReturnValue("daamky cracked by bludnov [ https://t.me/secretdevelopmentpizza ]");
   }
}
