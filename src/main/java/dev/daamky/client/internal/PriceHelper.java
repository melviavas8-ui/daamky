package dev.daamky.client.internal;

import dev.daamky.client.model.enums.ServerWorld;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

public class PriceHelper implements MinecraftAccess {
   public ServerWorld serverWorld = ServerWorld.FUN_TIME;

   public int computeNext(ItemStack stack) {
      for (Text var3 : stack.getTooltip(TooltipContext.DEFAULT, mc.player, TooltipType.BASIC)) {
         String var5 = this.computeReplace(var3.getString());
         int var6 = this.computeInt(var5, this.computeReplace(this.getString()));
         if (var6 != -1) {
            return var6;
         }

         var6 = this.computeContains(var5);
         if (var6 != -1) {
            return var6;
         }
      }

      return -1;
   }

   public String computeReplace(String value) {
      return value.replaceAll("§.", "").replace("¤", "").replace("₽", "").replace("$", "").replace('a', 'а').replace('A', 'А').trim();
   }

   public int computeInt(String line, String marker) {
      return !marker.isEmpty() && line.startsWith(marker) ? this.parseParse(line.substring(marker.length())) : -1;
   }

   public int computeContains(String line) {
      String var2 = line.toLowerCase();
      return !var2.contains("цен") && !var2.contains("price") ? -1 : this.parseParse(line);
   }

   public int parseParse(String value) {
      String var2 = value.replaceAll("[^0-9]", "");
      if (var2.isEmpty()) {
         return -1;
      } else {
         try {
            return Integer.parseInt(var2);
         } catch (NumberFormatException var4) {
            return -1;
         }
      }
   }

   public String getString() {
      switch (this.serverWorld) {
         case FUN_TIME:
            return "$ Ценa $";
         case SPOOKY_TIME:
            return "$ Цена: $";
         case HOLY_WORLD:
            return "▍ Цена за 1 ед.:";
         case REALLY_WORLD:
            return "Цена:";
         default:
            return "";
      }
   }
}
