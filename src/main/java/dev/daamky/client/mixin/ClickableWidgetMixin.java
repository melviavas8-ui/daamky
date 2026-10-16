package dev.daamky.client.mixin;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.CustomTitleScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.Render;
import dev.daamky.client.model.enums.Easing;
import java.awt.Color;
import java.lang.reflect.Field;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClickableWidget.class})
public abstract class ClickableWidgetMixin {
   @Unique
   public static final Field daamky$sliderValueField;
   @Shadow
   public boolean field_22763;
   @Shadow
   public boolean field_22764;
   @Shadow
   public float field_22765;
   @Unique
   public final AnimatedValue daamky$hoverAnim = new AnimatedValue();

   @Shadow
   public abstract int method_46426();

   @Shadow
   public abstract int method_46427();

   @Shadow
   public abstract int method_25368();

   @Shadow
   public abstract int method_25364();

   @Shadow
   public abstract boolean method_49606();

   @Shadow
   public abstract Text method_25369();

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      ClickableWidgetMixin var6 = this;
      if (!(this instanceof TextFieldWidget) && !(this instanceof EntryListWidget) && !(this instanceof AbstractTextWidget) && this.field_22764) {
         Screen var8 = MinecraftClient.getInstance().currentScreen;
         if (!(var8 instanceof HandledScreen)) {
            return;
         }

         if (!this.method_25369().getString().equals("Drop all")) {
            return;
         }

         this.daamky$hoverAnim.update();
         boolean var9 = this.method_49606() && this.field_22763;
         this.daamky$hoverAnim.animateTo(var9 ? 1.0 : 0.0, 250L, Easing.QUINT_OUT, true);
         int var10 = this.method_46426();
         int var11 = this.method_46427();
         int var12 = this.method_25368();
         int var13 = this.method_25364();
         String var14 = this.method_25369().getString();
         float var15 = this.field_22765;
         if (!this.field_22763) {
            var15 *= 0.5F;
         }

         float var16 = CustomTitleScreen.floatVal;
         float var17 = CustomTitleScreen.float2;
         float var18 = CustomTitleScreen.float3;
         float var19 = 5.0F;
         Color var20 = new Color((int)var16, (int)var17, (int)var18, (int)(255.0F * var15));
         Color var21 = new Color(
            Math.min(255, (int)(var16 * 1.3F)), Math.min(255, (int)(var17 * 1.3F)), Math.min(255, (int)(var18 * 1.3F)), (int)(255.0F * var15)
         );
         Color var22 = new Color(15, 15, 18, (int)(100.0F * var15));
         Color var23 = new Color((int)(var16 * 0.3F), (int)(var17 * 0.3F), (int)(var18 * 0.3F), (int)(130.0F * var15));
         Color var24 = new Color(165, 165, 170, (int)(255.0F * var15));
         Color var25 = ColorMath.computeColor(var20, var21, this.daamky$hoverAnim.getValue());
         Color var26 = ColorMath.computeColor(var22, var23, this.daamky$hoverAnim.getValue());
         Color var27 = ColorMath.computeColor(var24, new Color(255, 255, 255, (int)(255.0F * var15)), this.daamky$hoverAnim.getValue());
         if (this.daamky$hoverAnim.getValue() > 0.01F) {
            Render.shaderProgram
               .handleRgb(
                  context.getMatrices(),
                  (float)var10 - 2.0F,
                  (float)var11 - 2.0F,
                  (float)var12 + 4.0F,
                  (float)var13 + 4.0F,
                  var19 + 1.0F,
                  new Color(255, 255, 255, (int)((double)(15.0F * var15) * this.daamky$hoverAnim.getValue())),
                  0.16F
               );
         }

         Render.rectRenderer.handleRgb(context.getMatrices(), (float)var10, (float)var11, (float)var12, (float)var13, var19, var25);
         Render.shaderProgram
            .handleRgb(context.getMatrices(), (float)var10 + 1.0F, (float)var11 + 1.0F, (float)var12 - 2.0F, (float)var13 - 2.0F, var19 - 1.0F, var26);
         double var28 = -1.0;
         if (this instanceof SliderWidget && daamky$sliderValueField != null) {
            try {
               var28 = daamky$sliderValueField.getDouble(var6);
            } catch (Throwable var34) {
            }
         }

         int var7;
         if (var28 >= 0.0 && var28 <= 1.0 && (var7 = (int)((double)var12 * var28)) > 0) {
            Render.rectRenderer
               .handleRgb(
                  context.getMatrices(),
                  (float)var10,
                  (float)var11,
                  (float)var7,
                  (float)var13,
                  var19 - 1.0F,
                  new Color((int)var16, (int)var17, (int)var18, (int)(180.0F * var15))
               );
         }

         float var30 = 6.4F;
         float var31 = FontBootstrap.clientFont10.computeFloat(var14, var30);
         float var32 = (float)var10 + (float)var12 / 2.0F - var31 / 2.0F;
         float var33 = (float)var11 + (float)var13 / 2.0F;
         FontBootstrap.clientFont10.handleColor(context.getMatrices(), var14, var32, var33 - var30 / 2.0F, var30, var27);
         ci.cancel();
      }
   }

   static {
      Field var0 = null;

      try {
         for (Field var4 : SliderWidget.class.getDeclaredFields()) {
            if (var4.getType() == doubleVal.classVal) {
               var4.setAccessible(true);
               var0 = var4;
               break;
            }
         }
      } catch (Throwable var5) {
      }

      daamky$sliderValueField = var0;
   }
}
