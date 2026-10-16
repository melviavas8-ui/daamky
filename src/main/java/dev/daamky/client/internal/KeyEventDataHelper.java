package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.KeyEvent;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.ScreenEvent;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.gui.screen.ChatScreen;

public class KeyEventDataHelper implements MinecraftAccess {
   public static final KeyEventDataHelper mavixep = new KeyEventDataHelper();

   public void renderEvent() {
      this.keyEvent();
      this.render2dEvent();
      this.tickEvent();
      this.screenEvent();
   }

   public void screenEvent() {
      ScreenEvent.getInstance().subscribe(new EventListener<>(KeyEventDataHelper::lambda$screenEvent$0));
   }

   public void tickEvent() {
      TickEvent.getInstance().subscribe(new EventListener<>(KeyEventDataHelper::lambda$tickEvent$1));
   }

   public void render2dEvent() {
      Render2DEvent.getInstance().subscribe(new EventListener<>(KeyEventDataHelper::lambda$render2dEvent$3));
   }

   public void keyEvent() {
      KeyEvent.getInstance().subscribe(new EventListener<>(KeyEventDataHelper::lambda$keyEvent$5));
   }

   public static KeyEventDataHelper getInstance() {
      return mavixep;
   }

   public static void lambda$keyEvent$5(KeyEvent.KeyEventData event) {
      if (event.key() != -999 && event.key() != -1) {
         int var1 = event.action();
         int var2 = event.key() + (event.check() ? -100 : 0);
         if (mc.currentScreen == null) {
            ModuleRegistry.getInstance().getList().forEach(KeyEventDataHelper::lambda$keyEvent$4);
            if (var1 == 1) {
               MacroManager.getInstance().onKeyPressed(var2);
            }
         }
      }
   }

   public static void lambda$keyEvent$4(int key, int action, Module module) {
      int var3 = module.getBind();
      if (var3 == key && module.hasBind()) {
         if (module.isBindHold()) {
            if (action == 1) {
               module.setEnabled(true);
            }

            if (action == 0) {
               module.setEnabled(false);
            }
         } else if (action == 1) {
            module.toggle();
         }
      }
   }

   public static void lambda$render2dEvent$3(Render2DEvent.Render2DEventData event) {
      if (mc.currentScreen instanceof ChatScreen) {
         DraggableManagerSaveHelper.getInstance().getDraggables().forEach(KeyEventDataHelper::lambda$render2dEvent$2);
      }
   }

   public static void lambda$render2dEvent$2(String s, AutoHelper draggable) {
      if (draggable.getFeature().isEnabled()) {
         draggable.runRound();
      }
   }

   public static void lambda$tickEvent$1(TickEvent event) {
      EntryHelper.initNext();
      LastSkinHelper.getInstance().run();
   }

   public static void lambda$screenEvent$0(ScreenEvent.ScreenEventData event) {
      DropAllHelper.drawButton(event);
   }
}
