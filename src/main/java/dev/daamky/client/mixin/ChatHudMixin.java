package dev.daamky.client.mixin;

import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.module.misc.MinecraftTweaks;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatHud.class})
public abstract class ChatHudMixin {
   @Shadow
   @Final
   private List<Visible> field_2064;
   @Shadow
   @Final
   private List<ChatHudLine> field_2061;
   private String string = "";
   private int intVal = 1;
   private boolean flag = false;
   private float floatVal = 0.0F;
   private float float2 = 0.0F;
   private boolean flag2 = false;

   @Shadow
   public abstract void method_44811(Text var1, @Nullable MessageSignatureData var2, @Nullable MessageIndicator var3);

   @Inject(
      method = {"addMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
      if (MinecraftTweaks.getInstance().chatAntiSpam.getValue()) {
         if (!this.flag) {
            String var5 = message.getString();
            if (var5.equals(this.string)) {
               this.intVal++;
               ci.cancel();
               if (!this.field_2064.isEmpty()) {
                  this.field_2064.remove(0);
               }

               if (!this.field_2061.isEmpty()) {
                  this.field_2061.remove(0);
               }

               this.flag = true;
               MutableText var6 = message.copy().append(Text.literal(" §7[x" + this.intVal + "]"));
               this.method_44811(var6, signatureData, indicator);
               this.flag = false;
            } else {
               this.string = var5;
               this.intVal = 1;
            }
         }
      }
   }

   @Inject(
      method = {"addMessage"},
      at = {@At("RETURN")}
   )
   private void onAddMessageReturn(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
      if (MinecraftTweaks.getInstance().chatAnimations.getValue()) {
         if (MinecraftTweaks.getInstance().left.is("Left")) {
            this.float2 = 100.0F;
         } else {
            Objects.requireNonNull(MinecraftClient.getInstance().textRenderer);
            this.floatVal += 9.0F;
         }
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void onRenderHead(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
      this.flag2 = false;
      if (MinecraftTweaks.getInstance().chatAnimations.getValue()) {
         float var7 = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
         if (MinecraftTweaks.getInstance().left.is("Left") && this.float2 > 0.0F) {
            context.getMatrices().push();
            context.getMatrices().translate(-this.float2, 0.0F, 0.0F);
            this.flag2 = true;
            this.float2 = EntityHelper.computeFloat(this.float2, 0.0F, 0.45F * var7);
            if (this.float2 < 0.1F) {
               this.float2 = 0.0F;
            }
         } else if (MinecraftTweaks.getInstance().left.is("Bottom") && this.floatVal > 0.0F) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, this.floatVal, 0.0F);
            this.flag2 = true;
            this.floatVal = EntityHelper.computeFloat(this.floatVal, 0.0F, 0.45F * var7);
            if (this.floatVal < 0.1F) {
               this.floatVal = 0.0F;
            }
         } else {
            this.floatVal = 0.0F;
            this.float2 = 0.0F;
         }
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void onRenderReturn(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
      if (this.flag2) {
         context.getMatrices().pop();
         this.flag2 = false;
      }
   }
}
