package dev.daamky.client.mixin;

import dev.daamky.client.gui.CustomTitleScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.Render;
import dev.daamky.client.model.enums.Easing;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TextFieldWidget.class})
public abstract class TextFieldWidgetMixin extends ClickableWidget {
   @Unique
   public final AnimatedValue daamky$focusAnim = new AnimatedValue();

   public TextFieldWidgetMixin(int x, int y, int width, int height, Text message) {
      super(x, y, width, height, message);
   }

   @Shadow
   public abstract String method_1882();

   @Shadow
   public abstract void method_1858(boolean var1);

   @Inject(
      method = {"<init>*"},
      at = {@At("TAIL")}
   )
   public void onInit(CallbackInfo ci) {
      this.method_1858(false);
   }

   @Inject(
      method = {"renderWidget"},
      at = {@At("HEAD")}
   )
   public void preRenderWidget(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      this.daamky$focusAnim.update();
      this.daamky$focusAnim.animateTo(this.isFocused() ? 1.0 : 0.0, 250L, Easing.QUINT_OUT, true);
      float var6 = CustomTitleScreen.floatVal;
      float var7 = CustomTitleScreen.float2;
      float var8 = CustomTitleScreen.float3;
      float var9 = 5.0F;
      Color var10 = new Color((int)var6, (int)var7, (int)var8, (int)(255.0F * this.alpha));
      Color var11 = new Color(
         Math.min(255, (int)(var6 * 1.3F)), Math.min(255, (int)(var7 * 1.3F)), Math.min(255, (int)(var8 * 1.3F)), (int)(255.0F * this.alpha)
      );
      Color var12 = new Color(10, 10, 12, (int)(150.0F * this.alpha));
      Color var13 = ColorMath.computeColor(var10, var11, this.daamky$focusAnim.getValue());
      if (this.daamky$focusAnim.getValue() > 0.01F) {
         Render.shaderProgram
            .handleRgb(
               context.getMatrices(),
               (float)this.getX() - 2.0F,
               (float)this.getY() - 2.0F,
               (float)this.getWidth() + 4.0F,
               (float)this.getHeight() + 4.0F,
               var9 + 1.0F,
               new Color(255, 255, 255, (int)((double)(15.0F * this.alpha) * this.daamky$focusAnim.getValue())),
               0.16F
            );
      }

      Render.rectRenderer
         .handleRgb(
            context.getMatrices(), (float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), var9, var13
         );
      Render.shaderProgram
         .handleRgb(
            context.getMatrices(),
            (float)this.getX() + 1.0F,
            (float)this.getY() + 1.0F,
            (float)this.getWidth() - 2.0F,
            (float)this.getHeight() - 2.0F,
            var9 - 1.0F,
            var12
         );
      context.getMatrices().push();
      context.getMatrices().translate(4.0F, (float)(this.getHeight() - 8) / 2.0F, 0.0F);
   }

   @Inject(
      method = {"renderWidget"},
      at = {@At("RETURN")}
   )
   public void postRenderWidget(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      context.getMatrices().pop();
   }
}
