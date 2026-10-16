package dev.daamky.client.mixin;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClickableWidget.class})
public interface ClickableWidgetAccessor {
   @Accessor("y")
   void setY(int var1);

   @Accessor("height")
   void setHeight(int var1);
}
