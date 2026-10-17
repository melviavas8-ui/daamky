package dev.daamky.client.internal;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ExpandableSettingComponent2 extends ExpandableSettingComponent3.ExpandableSettingComponent {
   public final MultiBooleanSetting setting;
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final List<BooleanValue> list = new ArrayList<>();

   public ExpandableSettingComponent2(MultiBooleanSetting setting) {
      super(setting);
      this.setting = setting;
      this.handleOpen(true);
      this.handleFloat(this.getFloat());

      for (BooleanSetting var3 : setting.getValue()) {
         this.list.add(new BooleanValue(var3, true));
      }
   }

   public void handleNext(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var5 = context.getMatrices();
      this.run();
      this.animatedValue.update();
      if (this.isOpen()) {
         this.getAnimatedValue().animateTo(1.0, 220L, Easing.QUINT_OUT);
         this.animatedValue.animateTo((double)this.getFloat() >= 0.9 ? 1.0 : 0.0, 220L, Easing.QUINT_OUT);
      } else {
         this.animatedValue.animateTo(0.0, 160L, Easing.QUINT_OUT);
         if (this.animatedValue.getValue() <= 0.1) {
            this.getAnimatedValue().animateTo(0.0, 220L, Easing.QUINT_OUT);
         }
      }

      float var6 = this.getFloat();
      float var7 = (float)this.animatedValue.getValue();
      float var8 = InterfaceMetrics.computeFloat(6.4F);
      float var9 = InterfaceMetrics.computeFloat(18.5F);
      int var10 = (int)(this.clamp() * 255.0F);
      FontBootstrap.clientFont10
         .handleColor(
            var5,
            EngHelper.computeString(this.setting.getName()),
            this.getX(),
            this.getY() + var9 / 2.0F - var8 / 2.0F,
            var8,
            InterfaceMetrics.computeColor4(var10)
         );
      FontBootstrap.clientFont10
         .handleColor(
            var5,
            this.isOpen() ? "-" : "+",
            this.getX() + this.getWidth() - InterfaceMetrics.computeFloat(6.0F),
            this.getY() + var9 / 2.0F - var8 / 2.0F,
            var8,
            InterfaceMetrics.computeColor5(var10)
         );
      if ((double)var6 > 0.01 && this.clamp() > 0.01F) {
         float var11 = 0.0F;

         for (BooleanValue var13 : this.list) {
            AnimatedValue var14 = var13.getAnimatedValue();
            var14.update();
            var14.animateTo(var13.getSetting().isVisible() ? 1.0 : 0.0, 110L, Easing.SINE_OUT);
            var13.setX(this.getX());
            var13.setY(this.getY() + var9 + var11);
            var13.setWidth(this.getWidth());
            var11 += (float)((double)var13.getHeight() * var14.getValue());
         }

         this.setHeight(var9 + (var11 + this.getFloat()) * var6);
         if ((double)var7 > 0.05 && this.clamp() * var7 > 0.05F) {
            for (BooleanValue var16 : this.list) {
               float var17 = (float)(var16.getAnimatedValue().getValue() * (double)this.clamp() * (double)var7);
               if (var17 > 0.05F) {
                  var16.setAlpha(var17);
                  var16.initInit(context, mouseX, mouseY, delta);
               }
            }
         }
      } else {
         this.handleFloat(18.5F);
      }
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (Geometry.computeFlag(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)InterfaceMetrics.computeFloat(18.5F))
         )
       {
         this.run2();
      } else if (!this.check()) {
         for (BooleanValue var7 : this.list) {
            if (!(var7.getAnimatedValue().getValue() < 0.8)) {
               var7.mouseClicked(mouseX, mouseY, button);
            }
         }
      }
   }

   @Override
   public float getFloat() {
      return 18.5F;
   }

   @Override
   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   @Override
   public void handleDouble(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
   }
}
