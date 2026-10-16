package dev.daamky.client.internal;

import dev.daamky.client.gui.MainMenuScreen;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;

public class FactoryHelper {
   public static FactoryHelper factoryHelper;
   public static final LambdaFactoryInfoHelper mudocs = LambdaFactoryInfoHelper.initInit();
   public Clear clear;
   public MainMenuScreenHelper mainMenuScreenHelper;
   public MainMenuScreen mainMenuScreen;
   public static String string = "5.0";
   public GuiSettingsHelper guiSettingsHelper;
   public boolean flag = false;

   public static FactoryHelper getInstance() {
      if (factoryHelper == null) {
         factoryHelper = new FactoryHelper();
         factoryHelper.init();
      }

      return factoryHelper;
   }

   public void init() {
      if (!this.flag) {
         factoryHelper = this;
         mudocs.initAdd("", FactoryHelper::lambda$init$0);
         EnglishHelper.runSize();
         this.clear = new Clear();
         this.clear.init();
         this.guiSettingsHelper = new GuiSettingsHelper();
         this.guiSettingsHelper.createFile();
         this.mainMenuScreenHelper = new MainMenuScreenHelper();
         this.mainMenuScreen = new MainMenuScreen();
         MainMenuScreen.selectedTheme = this.guiSettingsHelper.getTheme();
         MainMenuScreen.preSelectedTheme = this.guiSettingsHelper.getTheme();
         this.flag = true;
      }
   }

      public Clear getClear() {
      return this.clear;
   }

      public MainMenuScreenHelper getMainMenuScreenHelper() {
      return this.mainMenuScreenHelper;
   }

      public MainMenuScreen getMainMenuScreen() {
      return this.mainMenuScreen;
   }

      public GuiSettingsHelper getGuiSettingsHelper() {
      return this.guiSettingsHelper;
   }

      public boolean check() {
      return this.flag;
   }

      public static LambdaFactoryInfoHelper getMudocs() {
      return mudocs;
   }

   public static Lookup lambda$init$0(Method lookupInMethod, Class klass) throws ReflectiveOperationException {
      return (Lookup)lookupInMethod.invoke(null, klass, MethodHandles.lookup());
   }
}
