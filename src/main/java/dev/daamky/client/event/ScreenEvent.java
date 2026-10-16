package dev.daamky.client.event;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class ScreenEvent extends Event<ScreenEvent.ScreenEventData> {
   public static final ScreenEvent screenEvent = new ScreenEvent();

   public static ScreenEvent getInstance() {
      return screenEvent;
   }

   public static class ScreenEventData {
      public final Screen screen;
      public final List<ButtonWidget> list = new ArrayList<>();

      public Screen getScreen() {
         return this.screen;
      }

      public List<ButtonWidget> getList() {
         return this.list;
      }

      public ScreenEventData(Screen screen) {
         this.screen = screenx;
      }
   }
}
