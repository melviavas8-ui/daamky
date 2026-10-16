package dev.daamky.client.internal;

import dev.daamky.client.item.ItemActionCatalog;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class ItemInfo {
   public static List<String> initAdd(ItemStack item) {
      ArrayList var1 = new ArrayList();
      var1.add(item.getName().getString());
      var1.add(item.getItem().toString());
      var1.add(item.getComponents().toString());
      return var1;
   }

   public static String computeJoin(List<String> tags) {
      return String.join(" ", tags);
   }

   public static ItemActionCatalog computeNext(ItemStack stack) {
      for (ItemActionCatalog var4 : ItemActionCatalog.values()) {
         if (stack.getItem() == var4.getItemType()) {
            String var5 = computeJoin(initAdd(stack));
            int var6 = var4.getTags().size();

            for (String var8 : var4.getTags()) {
               if (var5.contains(var8)) {
                  var6--;
               }
            }

            if (var6 <= 0) {
               return var4;
            }
         }
      }

      return null;
   }
}
