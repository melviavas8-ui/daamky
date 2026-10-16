package dev.daamky.client.module.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.daamky.client.gui.InventoryHelperScreen;
import dev.daamky.client.internal.ClientPaths;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

@ModuleInfo(
   name = "Inventory Helper",
   category = ModuleCategory.MICS,
   description = "Подсвечивает предмет выбранным цветом — подсветка следует за предметом в любой ячейке"
)
public class InventoryHelper extends Module {
   public static final InventoryHelper instance = new InventoryHelper();
   public final Map<String, Color> itemColors = new LinkedHashMap<>();
   public final ButtonSetting openMenu = new ButtonSetting("Open Menu").action(InventoryHelper::lambda$new$0);

   public InventoryHelper() {
      this.addSettings(new Setting[]{this.openMenu});
      this.getItemColors();
   }

   @Override
   public void onEnable() {
      if (mc.mouse != null) {
         mc.setScreen(new InventoryHelperScreen());
      }
   }

   @Override
   public void onEvent() {
   }

   public static String computeTrim(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         String var2 = Registries.ITEM.getId(stack.getItem()).toString();
         Text var3 = (Text)stack.get(DataComponentTypes.CUSTOM_NAME);
         String var1;
         return var3 != null && !(var1 = var3.getString().trim()).isEmpty() ? var2 + "|" + var1 : var2;
      } else {
         return null;
      }
   }

   public Color computeColors(ItemStack stack) {
      String var2 = computeTrim(stack);
      return var2 == null ? null : this.itemColors.get(var2);
   }

   public void handleClass_1735(DrawContext context, Slot slot) {
      if (mc.player != null) {
         Color var3 = this.computeColors(slot.getStack());
         if (var3 != null) {
            this.initBlue(context, slot.x, slot.y, var3);
         }
      }
   }

   public void initBlue(DrawContext context, int x, int y, Color c) {
      double var5 = Math.sin((double)System.currentTimeMillis() * 0.0055);
      int var7 = (int)(55.0 + 90.0 * (var5 * 0.5 + 0.5));
      int var8 = new Color(c.getRed(), c.getGreen(), c.getBlue(), var7).getRGB();
      int var9 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 210).getRGB();
      context.getMatrices().push();
      context.getMatrices().translate(0.0F, 0.0F, 300.0F);
      context.fill(x, y, x + 16, y + 16, var8);
      context.fill(x, y, x + 16, y + 1, var9);
      context.fill(x, y + 15, x + 16, y + 16, var9);
      context.fill(x, y, x + 1, y + 16, var9);
      context.fill(x + 15, y, x + 16, y + 16, var9);
      context.getMatrices().pop();
   }

   public void getItemColors() {
      this.itemColors.clear();
      File var1 = new File(ClientPaths.string7, "inventory_helper.json");
      if (var1.exists()) {
         try {
            String var2 = Files.readString(var1.toPath());
            JsonObject var3 = (JsonObject)new Gson().fromJson(var2, JsonObject.class);
            if (var3 != null && var3.has("items")) {
               JsonObject var4 = var3.getAsJsonObject("items");

               for (Entry var6 : var4.entrySet()) {
                  this.itemColors.put((String)var6.getKey(), Color.decode(((JsonElement)var6.getValue()).getAsString()));
               }
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }
   }

   public void initInit() {
      File var1 = new File(ClientPaths.string7);
      if (!var1.exists()) {
         var1.mkdirs();
      }

      File var2 = new File(var1, "inventory_helper.json");

      try {
         JsonObject var3 = new JsonObject();
         JsonObject var4 = new JsonObject();

         for (Entry var6 : this.itemColors.entrySet()) {
            Color var7 = (Color)var6.getValue();
            var4.addProperty((String)var6.getKey(), String.format("#%02X%02X%02X", var7.getRed(), var7.getGreen(), var7.getBlue()));
         }

         var3.add("items", var4);
         Files.writeString(var2.toPath(), new GsonBuilder().setPrettyPrinting().create().toJson(var3));
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   public static InventoryHelper getInstance() {
      return instance;
   }

   public static void lambda$new$0() {
      if (mc != null) {
         mc.setScreen(new InventoryHelperScreen());
      }
   }
}
