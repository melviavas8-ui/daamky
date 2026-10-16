package dev.daamky.client.module.render;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.theme.ThemeManager;
import net.minecraft.client.util.math.MatrixStack;

@ModuleInfo(
   name = "Interface",
   category = ModuleCategory.VISUALS
)
public class Interface extends Module {
   public static final Interface interfaceVal = new Interface();
   public final MultiBooleanSetting gps = new MultiBooleanSetting("Widgets")
      .value(
         new BooleanSetting("Watermark").value(true),
         new BooleanSetting("Target info").value(true),
         new BooleanSetting("Staffs").value(true),
         new BooleanSetting("XYZ").value(true),
         new BooleanSetting("BPS").value(true),
         new BooleanSetting("Inventory").value(true),
         new BooleanSetting("ServerInfo").value(true),
         new BooleanSetting("Potions").value(true),
         new BooleanSetting("Armor").value(true),
         new BooleanSetting("Hotbar").value(true),
         new BooleanSetting("Cooldowns").value(true),
         new BooleanSetting("Keybinds").value(true),
         new BooleanSetting("GPS").value(true)
      );

   public static float getFloat() {
      return 0.9F;
   }

   public static float getFloat2() {
      return 1.0F;
   }

   public static boolean check() {
      return true;
   }

   public static float getDay() {
      boolean var0 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      return var0 ? 0.35F : 0.45F;
   }

   public static float getDay2() {
      boolean var0 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      return var0 ? 0.35F : 0.45F;
   }

   public static float getDay3() {
      boolean var0 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
      return var0 ? 0.35F : 0.45F;
   }

   public static int getInt() {
      return 2;
   }

   public static float getFloat3() {
      return 2.25F;
   }

   public static boolean check2() {
      return true;
   }

   public static boolean check3() {
      return true;
   }

   public static boolean check4() {
      return true;
   }

   public static void handleVal(MatrixStack ms, float x, float y, float w, float h, float a, boolean right) {
   }

   public static void handleVal(MatrixStack ms, float x, float y, float w, float h, float r, float a, boolean right) {
   }

   public static void handleVal(MatrixStack ms, float x, float y, float w1, float gap, float w2, float h, float r, float a, boolean right) {
   }

   public void runAdd() {
      this.addSettings(new Setting[]{this.gps});
   }

   @Override
   public void onEvent() {
   }

      public static Interface getInstance() {
      return interfaceVal;
   }
}
