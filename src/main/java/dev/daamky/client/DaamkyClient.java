package dev.daamky.client;

import dev.daamky.client.config.ConfigManager;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AltsJsonHelper;
import dev.daamky.client.internal.AvaHelper;
import dev.daamky.client.internal.CritHelper;
import dev.daamky.client.internal.DisplayScale;
import dev.daamky.client.internal.DraggableManagerSaveHelper;
import dev.daamky.client.internal.DwmSetWindowAttributeHelper;
import dev.daamky.client.internal.FactoryHelper;
import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.internal.GpsHelper;
import dev.daamky.client.internal.GpsRenderer;
import dev.daamky.client.internal.IteratorHelper;
import dev.daamky.client.internal.KeyEventDataHelper;
import dev.daamky.client.internal.LastSkinHelper;
import dev.daamky.client.internal.MacroManager;
import dev.daamky.client.internal.ModuleRegistry;
import dev.daamky.client.internal.RegisterHelper;
import dev.daamky.client.internal.RotationTaskHelper;
import dev.daamky.client.internal.SoundHelper;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.internal.White;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.render.TargetESP;
import dev.daamky.client.rotation.RotationProfileManager;
import net.fabricmc.api.ClientModInitializer;

public class DaamkyClient implements ClientModInitializer {
   public static DaamkyClient daamkyClient = new DaamkyClient();

   public void onInitializeClient() {
      daamkyClient = this;

      try {
         SoundHelper.run();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      this.initializeManagers();
      this.initializeHooks();
      this.loadAutoConfig();

      try {
         FactoryHelper.getInstance().init();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public void postLoad() {
      try {
         FontBootstrap.runDlc();
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      try {
         ModuleRegistry.getInstance().getList().sort(DaamkyClient::lambda$postLoad$0);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      try {
         UHalfTexelSizeHelper.load();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      try {
         AltsJsonHelper.getInstance().getAlts();
         AltsJsonHelper.getInstance().loadNext();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public void loadAutoConfig() {
      try {
         ConfigManager.getInstance().loadJson("autoConfig");
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

      try {
         RotationProfileManager.getInstance().getFILE();
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

      try {
         DraggableManagerSaveHelper.getInstance().initShow();
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      try {
         FriendsStore.getInstance().initInit();
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      try {
         MacroManager.getInstance().run();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      try {
         MainMenuScreen.initGs2();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public void initializeManagers() {
      try {
         GpsHelper.getInstance().load();
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

      try {
         VecRotationHelper.getInstance().load();
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

      try {
         RotationTaskHelper.rotationTaskHelper.load();
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      try {
         ModuleRegistry.getInstance().load();
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      try {
         RegisterHelper.getInstance().initInit();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      try {
         White.getInstance().initTheme();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public void initializeHooks() {
      try {
         KeyEventDataHelper.getInstance().renderEvent();
      } catch (Throwable var9) {
         var9.printStackTrace();
      }

      try {
         DisplayScale.getInstance().run();
      } catch (Throwable var8) {
         var8.printStackTrace();
      }

      try {
         LastSkinHelper.getInstance().load();
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

      try {
         DwmSetWindowAttributeHelper.getInstance().run();
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

      try {
         CritHelper.getInstance().run();
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      try {
         GpsRenderer.getInstance().load();
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      try {
         TargetESP.getInstance().setEnabled(true);
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      try {
         AvaHelper.startRPC();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public void saveAutoConfig() {
      ConfigManager.getInstance().setCurrentConfig("autoConfig");
      RotationProfileManager.getInstance().closeFile();
      IteratorHelper.getInstance().runNext2();
      White.getInstance().handleTheme(true);
      DraggableManagerSaveHelper.getInstance().save();
      MacroManager.getInstance().run2();
      MainMenuScreen.initGs();
      AvaHelper.runShutdown();
   }

   public static DaamkyClient getInstance() {
      return daamkyClient;
   }

   public static int lambda$postLoad$0(Module a, Module b) {
      return Float.compare(FontBootstrap.clientFont10.computeFloat(b.getName(), 7.0F), FontBootstrap.clientFont10.computeFloat(a.getName(), 7.0F));
   }
}
