package dev.daamky.client.mixin;

import dev.daamky.client.event.ScreenEvent;
import dev.daamky.client.module.misc.InventoryHelper;
import dev.daamky.client.module.pve.AuctionHelper;
import dev.daamky.client.module.render.ShulkerPreview;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
   @Shadow
   protected Slot field_2787;

   protected HandledScreenMixin(Text title) {
      super(title);
   }

   @Shadow
   protected abstract boolean method_2387(Slot var1, double var2, double var4);

   @Shadow
   protected abstract void method_2383(Slot var1, int var2, int var3, SlotActionType var4);

   @Inject(
      method = {"init"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo ci) {
      ScreenEvent.ScreenEventData var2 = new ScreenEvent.ScreenEventData(this);
      ScreenEvent.getInstance().postAndCheckCancelled(var2);

      for (ButtonWidget var4 : var2.getList()) {
         this.addDrawableChild(var4);
      }
   }

   @Inject(
      method = {"drawSlot"},
      at = {@At("TAIL")}
   )
   protected void drawSlotHook(DrawContext context, Slot slot, CallbackInfo ci) {
      AuctionHelper var5 = AuctionHelper.getInstance();
      if (var5.isEnabled()) {
         var5.initContains(context, slot);
      }

      InventoryHelper var4;
      if ((var4 = InventoryHelper.getInstance()).isEnabled()) {
         var4.handleClass_1735(context, slot);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void onRenderTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      ShulkerPreview var6 = ShulkerPreview.getInstance();
      if (var6.isEnabled() && this.field_2787 != null && this.field_2787.hasStack()) {
         var6.initInit(context, this.field_2787.getStack(), mouseX, mouseY);
      }
   }
}
